package com.marspotato.supportsmallshop.BO;

import org.apache.ibatis.session.SqlSession;
import org.joda.time.DateTime;

import redis.clients.jedis.Jedis;

import com.google.gson.annotations.Expose;
import com.marspotato.supportsmallshop.util.Config;
import com.marspotato.supportsmallshop.util.ConnectionContainer;

public class Submission {
	@Expose
	public String id;
	@Expose
	public String helperId;
	@Expose
	public String shopId;
	
	@Expose
	public String name;
	@Expose
	public String shortDescription;
	@Expose
	public String fullDescription;
	@Expose
	public String searchTags;
	
	@Expose
	public String shopType;
	@Expose
	public String openHours;
	@Expose
	public int district;
	@Expose
	public String address;
	@Expose
	public String phone;
	@Expose
	public int latitude1000000; /* the value of latitude * 1000000, the Accuracy is ~0.1m */
	@Expose
	public int longitude1000000; /* the value of longitude * 1000000, the Accuracy is ~0.1m */
	@Expose
	public String photoUrl;

	
	public void saveCreateShopRecord()
	{
		SqlSession session = ConnectionContainer.getDBConnection();
   		session.insert("saveCreateShopRecord", this);
		session.commit();
	}
	public static Submission getSubmission(String id)
	{
		SqlSession session = ConnectionContainer.getDBConnection();
		Submission output = session.selectOne("getSubmission", id);
		session.commit();
		
		return output;
	}
}
