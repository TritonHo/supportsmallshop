package com.marspotato.supportsmallshop.BO;

import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;

import com.google.gson.annotations.Expose;
import com.marspotato.supportsmallshop.util.Config;
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
	

	public void checkAndProcessSubmission(boolean isAccept, SqlSession session)
	{
		//this implementation have slight concurrency issue that delay the execution of final processing of Submission
		//but still acceptable
		
		if (isAccept == true)
			acceptCount++;
		else
			rejectCount++;
		int acceptDiff = acceptCount - rejectCount;
		
		//determine if the submission reach take action threfold
		if 	( 
				(acceptDiff * -1 >= Config.REJECT_SUBMISSION_THRESHOLD )
				|| (acceptDiff >= Config.ACCEPT_SUBMISSION_THRESHOLD)
			)
			isProcessed = true;
		
		//update the submission 
		HashMap<String, Object> h = new HashMap<String, Object>();
		h.put("id", this.id);
		h.put("rejectIncrement", isAccept == false?1:0 );
		h.put("acceptIncrement", isAccept == true?1:0 );
		if (isProcessed == true)
			h.put("isProcessed", isProcessed );
		int updateResult = session.update("updateSubmission", h);
		
		//if updateResult = 0, then there is concurrent update, and thus no need to process the submission
		if (updateResult > 0 && isProcessed == true && acceptDiff >= Config.ACCEPT_SUBMISSION_THRESHOLD)
			session.delete("deleteShopWithSubmission", this.shopId);
	}
}
