package com.marspotato.supportsmallshop.BO;

import java.util.UUID;

import org.joda.time.DateTime;

import redis.clients.jedis.Jedis;

import com.marspotato.supportsmallshop.util.Config;
import com.marspotato.supportsmallshop.util.ConnectionContainer;

public class AuthCode {
	public static String generateAuthCode(String regId, String deviceType, DateTime dt)
	{
		String value = regId + ":" + deviceType + ":" + Config.defaultDateTimeFormatter.print(dt) + ":0";
		String authCode = null;
		boolean getResult = false; 
		
		Jedis connection = ConnectionContainer.getRedisConnection();
		while(!getResult)
		{
			authCode = UUID.randomUUID().toString();
			Long result = connection.setnx("auth_code:"+authCode , value);
			getResult = (result.intValue() == 1);
		}
		
		//after creating the session, adjust the expiration
		connection.expire("auth_code:"+authCode, Config.VERIFICATION_PERIOD);
		
		return authCode;
	}	
}
