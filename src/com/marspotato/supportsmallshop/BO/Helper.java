package com.marspotato.supportsmallshop.BO;

import java.util.HashMap;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;

import com.google.gson.annotations.Expose;
import com.marspotato.supportsmallshop.util.ConnectionContainer;

public class Helper {
	@Expose
	public String id;
	@Expose
	public String deviceType;
	@Expose
	public String regId;
	
	public Helper(String id, String deviceType, String regId)
	{
		this.id = id;
		this.deviceType = deviceType;
		this.regId = regId;
	}
	
	public static Helper getHelper(String deviceType, String regId)
	{
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("deviceType", deviceType);
		h.put("regId", regId);
		
		Helper output = null;
		String id = null;
		SqlSession session = ConnectionContainer.getDBConnection();
		try {
			id = session.selectOne("getHelperID", h);
			if (id == null)
			{
				id = UUID.randomUUID().toString();
				h.put("id", id);
				session.insert("createHelper", h);
				session.commit();
			}
			output = new Helper(id, deviceType, regId);
		} finally {
			session.close();
		}
		return output;
	}
	
	public static void updateHelperRegId(String id, String deviceType, String newRegId, String oldRegId)
	{
		SqlSession session = ConnectionContainer.getDBConnection();
		try {
			HashMap<String, String> h = new HashMap<String, String>();
			h.put("id", id);
			h.put("deviceType", deviceType);
			h.put("newRegId", newRegId);
			h.put("oldRegId", oldRegId);
			
			session.update("updateHelperRegId", h);
			session.commit();
		} finally {
			session.close();
		}
	}
}
