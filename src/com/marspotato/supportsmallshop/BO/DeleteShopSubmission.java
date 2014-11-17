package com.marspotato.supportsmallshop.BO;

import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;

import com.google.gson.annotations.Expose;
import com.marspotato.supportsmallshop.util.ConnectionContainer;

public class DeleteShopSubmission {
	@Expose
	public String id;
	@Expose
	public String helperId;
	@Expose
	public String shopId;
	@Expose
	public String reason;

	public int rejectCount;
	public int acceptCount;
	public boolean isProcessed;
	
	
	public void saveDeleteShopRecord()
	{
		SqlSession session = ConnectionContainer.getDBConnection();
		try
		{
	   		session.insert("saveDeleteShopRecord", this);
			session.commit();
		}
		finally
		{
			session.close();
		}
	}
	public static DeleteShopSubmission getDeleteShopSubmission(String id)
	{
		SqlSession session = ConnectionContainer.getDBConnection();
		DeleteShopSubmission output = null;
		try
		{
			output = session.selectOne("getDeleteShopSubmission", id);
		}
		finally
		{
			session.close();
		}
		return output;
	}

	//TODO:
	/*
	public void onAcceptAction(SqlSession session) {
		session.update("deleteShopWithSubmission", id);
	}
	*/
	
	//check if the input helperId is one of the reviewer
	public boolean isReviewer(String helperId)
	{
		if (helperId.isEmpty())
			return false;
		HashMap<String, Object> h = new HashMap<String, Object>();
		h.put("helperId", helperId);
		h.put("id", this.id);
		
		SqlSession session = ConnectionContainer.getDBConnection();
		int output = session.selectOne("getDeleteShopSubmissionReviewerCount", h);
		session.close();
		return output > 0;
	}
}
