package com.dvtrsc.codevault.cvbe.message;

import java.security.InvalidParameterException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dvtrsc.codevault.cvbe.FH;
import com.dvtrsc.codevault.cvbe.entities.Constants;

/*
 * ValidationInterceptor. Search for validation headers (user, saltedhash, salt, originip) and verifies them
 * D.Tordera, 20171031 
 */

@Component
public class ValidationInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = Logger.getLogger(ValidationInterceptor.class);
			
	@Override 
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	{
		String user = "", origin = "", hash = ""; 
		long salt = 0L; 
		
		try
		{
			user = request.getHeader("U");						// User
			salt = Long.parseLong(request.getHeader("ST")); 	// Salt			
			hash = request.getHeader("SH"); 					// Salted hash			
			origin = request.getHeader("X-Real-IP"); 			// Origin IP. Informed by nginx reverse proxy.			
				
			validate(user, hash, salt, origin);
			logger.info(FH.uid(request) + "Request validated with " + user + ", " + hash + ", " + salt + ", " + origin);
			
			return true; 
		}
		catch(Exception E) // In case of any validation error, return nothing.
		{		
			response.setStatus(401); // set unauthorized 
			logger.error(FH.uid(request) + "Request failed at validation (" + E.getClass().getSimpleName() + ", " + E.getMessage() + 
					") with "  + user + ", " + hash + ", " + salt + ", " + origin + ". Request will not be responsed."); 
			return false;
		}				
	}
	
	private String gethashedpassword(String user) throws Exception
	{
		return FH.sha256hex(user.equals(Constants.__USER) ? Constants.__PASS : user);		
	}
	
	private void validatehash(String hash, String user, long salt) throws Exception
	{		
		if (!hash.equals(FH.sha256hex(gethashedpassword(user) + FH.sha256hex(Long.toString(salt))))) 
			throw new InvalidParameterException("Hash mismatch");
	}		
	
	private void validate(String user, String hash, long salt, String origin) throws Exception
	{		
		validateexistence(user, hash, salt, origin);
		validateorigin(origin);
		validatesalt(salt);		
		validatehash(hash, user, salt);				
	}	
	
	private void validatesalt(long salt)
	{	
		if (Math.abs(System.currentTimeMillis()/1000 - salt) > Constants.__SALTLIFE) 
			throw new InvalidParameterException("Caducated salt");
	}	
		
	private void validateexistence(String user, String hash, long salt, String origin)
	{
		if (hash == null || user == null || origin == null || salt == 0L)
			throw new InvalidParameterException("Incorrect authentification"); 		
	}
	
	private void validateorigin(String origin)
	{
		if (!isIpAddress(origin)) throw new InvalidParameterException("Incorrect origin");
	}
		  
	public static boolean isIpAddress(String ipAddress)
	{
	    Matcher m1 = VALID_IPV4_PATTERN.matcher(ipAddress);
	    if (m1.matches()) {
	      return true;
	    }
	    Matcher m2 = VALID_IPV6_PATTERN.matcher(ipAddress);
	    return m2.matches();
	}	
	
	private static Pattern VALID_IPV4_PATTERN = null;
	private static Pattern VALID_IPV6_PATTERN = null;
	
	static
	{
	    try
	    {
	      VALID_IPV4_PATTERN = Pattern.compile("(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])", 2);
	      VALID_IPV6_PATTERN = Pattern.compile("([0-9a-f]{1,4}:){7}([0-9a-f]){1,4}", 2);
	    }
	    catch (PatternSyntaxException localPatternSyntaxException) {}
	}
}
