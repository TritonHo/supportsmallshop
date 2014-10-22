package com.marspotato.supportsmallshop.BO;

import java.util.HashMap;

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
