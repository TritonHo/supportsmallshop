package com.marspotato.supportsmallshop.BO;

import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;

import com.google.gson.annotations.Expose;
import com.marspotato.supportsmallshop.util.Config;
import com.marspotato.supportsmallshop.util.ConnectionContainer;

public abstract class Submission {
	/*
		Rule for submission processing:
		
		if serious_reject >= 1 && (serious_reject + reject) >= 2
			preform serious reject
		else
		{ 
			if (reject + serious_reject - accept) >= REJECT_SUBMISSION_THRESHOLD
				preform reject()
			if (accept - reject) >= ACCEPT_SUBMISSION_THRESHOLD
				preform accept()
		}
	 */
	
	@Expose
	public String id;
	@Expose
	public String helperId;
	
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
	
	public int rejectCount;
	public int seriousRejectCount;
	public int acceptCount;
	public boolean isProcessed;

	public abstract void onAcceptAction(SqlSession session); 
	
	
	public void checkAndProcessSubmission(CreateUpdateShopResponseType type, SqlSession session)
	{
		//this implementation have slight concurrency issue that delay the execution of final processing of Submission
		//but still acceptable
		rejectCount += type.isReject?1:0;
		seriousRejectCount += type.isSeriousReject?1:0;
		acceptCount += type.isAccept?1:0;
		int acceptDiff = acceptCount - rejectCount - seriousRejectCount;
		
		//determine if the submission reach take action threfold
		if 	( 
				(seriousRejectCount >= 1 && seriousRejectCount + rejectCount >= 2) 
				|| (acceptDiff * -1 >= Config.REJECT_SUBMISSION_THRESHOLD )
				|| (acceptDiff >= Config.ACCEPT_SUBMISSION_THRESHOLD)
			)
			isProcessed = true;
		
		//update the submission 
		HashMap<String, Object> h = new HashMap<String, Object>();
		h.put("id", this.id);
		h.put("rejectIncrement", type.isReject?1:0 );
		h.put("seriousRejectIncrement", type.isSeriousReject?1:0 );
		h.put("acceptIncrement", type.isAccept?1:0 );
		if (isProcessed == true)
			h.put("isProcessed", isProcessed );
		int updateResult = session.update("updateSubmission", h);
		
		//if updateResult = 0, then there is concurrent update, and thus no need to process the submission
		if (updateResult > 0 && isProcessed == true)
		{
			if (seriousRejectCount >= 1 && seriousRejectCount + rejectCount >= 2)
			{
				//TODO: preform serious reject 
			}
			else
			{
				if (acceptDiff >= Config.ACCEPT_SUBMISSION_THRESHOLD)
					onAcceptAction(session);
			}
		}	
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
		int output = session.selectOne("getSubmissionReviewerCount", h);
		session.close();
		return output > 0;
	}
}
