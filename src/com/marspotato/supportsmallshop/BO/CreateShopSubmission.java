package com.marspotato.supportsmallshop.BO;

import java.util.HashMap;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;

import com.marspotato.supportsmallshop.util.ConnectionContainer;

public class CreateShopSubmission extends Submission{


	
	public void saveCreateShopRecord()
	{
		SqlSession session = ConnectionContainer.getDBConnection();
		try
		{
	   		session.insert("saveCreateShopRecord", this);
			session.commit();
		}
		finally
		{
			session.close();
		}
	}
	public static CreateShopSubmission getCreateShopSubmission(String id)
	{
		SqlSession session = ConnectionContainer.getDBConnection();
		CreateShopSubmission output = null;
		try
		{
			output = session.selectOne("getCreateShopSubmission", id);
		}
		finally
		{
			session.close();
		}
		return output;
	}

	@Override
	public void onAcceptAction(SqlSession session) {
		HashMap<String, String> h1 = new HashMap<String, String>();
		h1.put("id", this.id);
		h1.put("shopId", UUID.randomUUID().toString());
		session.insert("createNewShopFromSubmission", h1);
	}
}
