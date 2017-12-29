package com.dvtrsc.codevault.cvbe;


import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Hex;

/*
 * FH (FunctionalHelper). Common use functions. 
 * D.Tordera, 20171031
 */

public class FH {
		
	private FH() {}
	
	// hashing
	public static String sha256hex(String s)
	{
		return Hex.encodeHexString(sha256(s));
	}	
	
	public static byte[] sha256(String s)
	{
	    return sha256(s.getBytes());
	}	
	          
	public static byte[] sha256(byte[] b)
	{
	    try {
	    	MessageDigest md = MessageDigest.getInstance("SHA-256");
	          
	        md.update(b);
	        return md.digest();
	          
	    } catch (NoSuchAlgorithmException ex) {
	        return null;
	    }               
	}   

	// Properties
	public static Properties loadProperties(String propFileName) throws IOException
	{
    	Properties p = new Properties();
    	FileInputStream is = null;
    	
    	try
    	{
    		is = new FileInputStream(propFileName);
    		if (is != null)
    			p.load(is);
    	}
    	finally {
    		if (is != null) try { is.close(); } catch(IOException e) { }
    	}     	     	
    	
        return p;    	
	}
	
	// Log 
	public static String uid(HttpServletRequest request)
	{
		return "[" + request.getAttribute("uid") + "] "; 		
	}
}
