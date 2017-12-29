package com.dvtrsc.codevault.cvbe.message;

import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class EncryptionConverter extends AbstractHttpMessageConverter<Object> {	
	
	private final static RequestLogger logger = new RequestLogger(LoggerFilter.class);
	
	private final static byte[] __KEY = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F};
	private final static byte[] __IV = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F};
		
	private ObjectMapper objectMapper;
	
	public EncryptionConverter()
	{
		super(MediaType.APPLICATION_JSON, new MediaType("application","encrypt"));
		objectMapper = new ObjectMapper();
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		return objectMapper.readValue(inputMessage.getBody(), clazz);
	}
		
	private byte[] encrypt(String uid, Object o, byte[] key, byte[] iv)
	{	
		byte[] R = "FF".getBytes() ;
		
		try
		{
			SecretKeySpec keyspec = new SecretKeySpec(key,"AES");
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
			
			return cipher.doFinal(objectMapper.writeValueAsBytes(o));			
		}
		catch(Exception E)
		{
			logger.exception(null, E, "Encryption failed for request uid " + uid);
		}
		
		return R;
	}
	
	private boolean isApplicationHex(HttpOutputMessage message)
	{
		try
		{
			return message.getHeaders().getFirst("Content-Type").contains("encrypt");
		}
		catch(Exception E)
		{
			return false;
		}
	}
	
	@Override
	protected void writeInternal(Object t, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {						

		String uid = outputMessage.getHeaders().getFirst("uid");	
			
		logger.info(null, "[" + uid + "] <= " + objectMapper.writeValueAsString(t));
		
		outputMessage.getBody().write(
				isApplicationHex(outputMessage) ? encrypt(uid,t,__KEY,__IV) : objectMapper.writeValueAsBytes(t));			
	}
}
