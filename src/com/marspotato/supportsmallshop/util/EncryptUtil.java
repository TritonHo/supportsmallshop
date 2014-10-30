package com.marspotato.supportsmallshop.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;

public class EncryptUtil {

	private static final PooledPBEStringEncryptor currentEncryptor;
	private static final PooledPBEStringEncryptor oldEncryptor;

	static
	{		
		String currentEncryptionKey = null, oldEncryptionKey = null;
		try {
			Context ctx = new InitialContext();
			ctx = (Context) ctx.lookup("java:comp/env");
			currentEncryptionKey = (String) ctx.lookup("supportsmallshop-currentEncryptionKey");
			oldEncryptionKey = (String) ctx.lookup("supportsmallshop-oldEncryptionKey");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		currentEncryptor = new PooledPBEStringEncryptor();
		currentEncryptor.setPoolSize(16);//should be enough for big instance
		currentEncryptor.setPassword(currentEncryptionKey);
		currentEncryptor.setAlgorithm("PBEWITHSHA1ANDDESEDE");
		
		oldEncryptor = new PooledPBEStringEncryptor();
		oldEncryptor.setPoolSize(16);//should be enough for big instance
		oldEncryptor.setPassword(oldEncryptionKey);
		oldEncryptor.setAlgorithm("PBEWITHSHA1ANDDESEDE");
	}
	
	public static String encrypt(String input)
	{
		return currentEncryptor.encrypt(input);
	}
	public static String decrypt(String input)
	{
		String output = null;
		try 
		{
			String temp = currentEncryptor.decrypt(input);
			output = temp;
		}
		catch (Exception Ex)
		{
			//no need to care the failed decryption
		}
		try 
		{
			String temp = oldEncryptor.decrypt(input);
			output = temp;
		}
		catch (Exception Ex)
		{
			//no need to care the failed decryption
		}
		return output;
	}
}
