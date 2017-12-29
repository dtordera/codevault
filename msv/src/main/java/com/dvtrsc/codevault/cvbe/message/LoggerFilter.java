package com.dvtrsc.codevault.cvbe.message;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;


@Component
public class LoggerFilter extends OncePerRequestFilter {

	private final static RequestLogger logger = new RequestLogger(LoggerFilter.class);
	
	Random rnd = new Random();
			
	private int maxPayloadLength = 100000;

	private String getContentAsString(byte[] buf, int maxLength, String charsetName) {
	    if (buf == null || buf.length == 0) return "";
	    int length = Math.min(buf.length, this.maxPayloadLength);
	    try {
	    	return new String(buf, 0, length, charsetName);
	    } catch (UnsupportedEncodingException ex) {
	    	return "Unsupported Encoding";
	    }
	}
	
	private boolean isSwagger(HttpServletRequest request)
	{
		return request.getServletPath().indexOf("/v2/") > -1 || request.getServletPath().indexOf("swagger") > -1;
	}
	
	private void setUID(HttpServletRequest request)
	{
		long uid = rnd.nextLong() % 1000000000;		
		uid = uid < 0 ? -uid : uid;		
		
		request.setAttribute("uid", uid);
		logger.info(request, "Request from " + request.getLocalAddr() + " (proxied from " + request.getHeader("X-Real-IP") + ")");		
	}
	
	private void cloneHeadersToResponse(HttpServletRequest request, HttpServletResponse response)
	{
		response.setHeader("UID", request.getAttribute("uid").toString());
	}
	
	private void showRequestInfo(HttpServletRequest request)
	{
		StringBuffer reqInfo = new StringBuffer()			
				.append(request.getMethod())
				.append(" ")
				.append(request.getRequestURL());
		
			String queryString = request.getQueryString();		
			if (queryString != null) 	reqInfo.append("?").append(queryString);
			
			if (!isSwagger(request)) logger.info(request, "=> " + reqInfo);			
	}
	
	private void showRequestBody(ContentCachingRequestWrapper wrappedrequest)
	{
		String requestBody = 
				this.getContentAsString(wrappedrequest.getContentAsByteArray(), this.maxPayloadLength, wrappedrequest.getCharacterEncoding());
		
		if (!isSwagger(wrappedrequest) && requestBody.length() > 0) 
			logger.info(wrappedrequest, "=> " + requestBody);
	}
	
	private void showResponseStatus(ContentCachingRequestWrapper wrappedRequest, ContentCachingResponseWrapper wrappedResponse, long duration)
	{
		logger.info(wrappedRequest, "<= responsed with status " + 
				wrappedResponse.getStatus() + " in " + duration + "ms. " + 
				(useEncrypt(wrappedRequest)? "encrypted. ":"") + 
				(zipResponse(wrappedRequest)? "zipped.":""));			
	}
	
	private boolean useEncrypt(HttpServletRequest request)
	{
		try
		{
			return request.getHeader("Accept").contains("encrypt");
		}
		catch(Exception E)
		{
			return false;
		}		
	}
	
	private boolean zipResponse(HttpServletRequest request)
	{
		try
		{
			return request.getHeader("Accept-Encoding").contains("gzip");
		}
		catch(Exception E)
		{
			return false;
		}
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException 
	{
		long startTime = System.currentTimeMillis(), duration;
		
		setUID(request);				
		cloneHeadersToResponse(request,response);								
	
		showRequestInfo(request);
		
		ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
		ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
		
		showRequestBody(wrappedRequest);
		
		// continue process chain...
		filterChain.doFilter(request, response);
		
		duration = System.currentTimeMillis() - startTime;	

		showResponseStatus(wrappedRequest,wrappedResponse,duration);
		
	    wrappedResponse.copyBodyToResponse();  
	}
}
