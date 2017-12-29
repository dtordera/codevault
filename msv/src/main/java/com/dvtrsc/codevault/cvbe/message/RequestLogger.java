package com.dvtrsc.codevault.cvbe.message;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class RequestLogger {
	
	private Logger _logger;
	
	private String uid(HttpServletRequest request)
	{
		return request != null ? "[" + request.getAttribute("uid") + "] (" + request.getServletPath() + ") " : ""; 		
	}
	
	public RequestLogger(@SuppressWarnings("rawtypes") Class C)
	{
		_logger = Logger.getLogger(C);
	}
	
	public void info(HttpServletRequest r, String m)
	{
		_logger.info(uid(r) + m);
	}
	
	public void warn(HttpServletRequest r, String m)
	{
		_logger.warn(uid(r) + m);
	}
	
	public void error(HttpServletRequest r, String m)
	{
		_logger.error(uid(r) + m);
	}
	
	public void exception(HttpServletRequest r, Exception E, String m)
	{
		error(r, m + " (" + E.getClass().getSimpleName() + ", " + E.getMessage() + ")");
	}
}
