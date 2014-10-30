package com.marspotato.supportsmallshop.BO;

import java.util.UUID;

import org.joda.time.DateTime;

import redis.clients.jedis.Jedis;

import com.marspotato.supportsmallshop.util.Config;
import com.marspotato.supportsmallshop.util.ConnectionContainer;
import com.marspotato.supportsmallshop.util.EncryptUtil;

public class AuthCode {
	//remarks: this design cannot protect from cross-API concurrency problem
	//but enough for this project  
	
	public String code;
	public String regId;
	public String deviceType;
	public DateTime dt;
	public boolean used;
	
	public void storeIntoRedisOutbox()
	{
		Jedis connection = ConnectionContainer.getRedisConnection();
		if (deviceType.equals("google-android"))
			connection.lpush("gcm_auth_code_list", "" + EncryptUtil.encrypt(code)+":"+regId);
		else
		{
			//TODO: inplement the ios version
		}
		
		ConnectionContainer.returnRedisConnection(connection);
	}
	
	public String getUsageValue(String actionName)
	{
		Jedis connection = ConnectionContainer.getRedisConnection();
		String output = connection.get(actionName + ":" + code);
		ConnectionContainer.returnRedisConnection(connection);
		return output;
	}
	public boolean registerUsage(String actionName, String value)
	{
		if (this.used == true)
			return false;
		used = true;
		
		Jedis connection = ConnectionContainer.getRedisConnection();
		long result = connection.setnx(actionName + ":" + code, value);
		if (result > 0)
			connection.setex("auth_code:"+code, Config.VERIFICATION_PERIOD, Config.redisGSON.toJson(this));
		ConnectionContainer.returnRedisConnection(connection);
		return result > 0;
	}
	
	public static AuthCode getAuthCode(String code)
	{
		Jedis connection = ConnectionContainer.getRedisConnection();
		String value = connection.get("auth_code:"+code);
		ConnectionContainer.returnRedisConnection(connection);
		
		if (value == null || value.isEmpty())
			return null;
		
		AuthCode output = Config.redisGSON.fromJson(value, AuthCode.class);
		return output;
	}
	
	public static AuthCode generateAuthCode(String regId, String deviceType, DateTime dt)
	{
		AuthCode ac = new AuthCode();
		ac.regId = regId;
		ac.deviceType = deviceType;
		ac.dt = dt;
		ac.used = false;

		boolean getResult = false; 
		
		Jedis connection = ConnectionContainer.getRedisConnection();
		while(!getResult)
		{
			ac.code = UUID.randomUUID().toString();
			Long result = connection.setnx("auth_code:"+ac.code , Config.redisGSON.toJson(ac));
			getResult = (result.intValue() == 1);
		}
		
		//after creating the session, adjust the expiration
		connection.expire("auth_code:"+ac.code, Config.VERIFICATION_PERIOD);
		ConnectionContainer.returnRedisConnection(connection);
		return ac;
	}	
}
