package com.marspotato.supportsmallshop.util;

import java.util.Set;

import org.joda.time.DateTime;
import redis.clients.jedis.Jedis;

public class CounterUtil {
	
	public static boolean increaseShopActionCount(String helperId)
	{
		return increaseActionCount(Config.SHOP_ACTION_COUNT, Config.MAX_SHOP_ACTION, helperId);
	}
	public static boolean increaseReviewActionCount(String helperId)
	{
		return increaseActionCount(Config.REVIEW_ACTION_COUNT, Config.MAX_REVIEW_ACTION, helperId);
	}
	//true: action is grant.
	//false: cannot take the action
	private static boolean increaseActionCount(String actionSetName, int threhold, String helperId)
	{
		String key = actionSetName + ":" + helperId;
		int existingRecordCount = 0;
		Jedis connection = ConnectionContainer.getRedisConnection();
		Set<String> set = connection.smembers(key);
		
		DateTime limit = DateTime.now().minusDays(1);
		
        for (String s : set) {
        	DateTime t = Config.defaultDateTimeFormatter.parseDateTime(s);
        	if (t.isBefore(limit)) 
        		//too old record, simply delete it
        		connection.srem(key, s);
        	else
        		existingRecordCount++;
        }
        //if there is still vacancy, add it
        if (existingRecordCount < threhold)
        {
        	connection.sadd(key, Config.defaultDateTimeFormatter.print(DateTime.now()));
            connection.expire(key, 3600 * 24); //one day
        }
		ConnectionContainer.returnRedisConnection(connection);//return the jedis connection ASAP
		
		return existingRecordCount < threhold;
	}
}
