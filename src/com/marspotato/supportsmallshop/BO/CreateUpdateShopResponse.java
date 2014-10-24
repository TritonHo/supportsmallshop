package com.marspotato.supportsmallshop.BO;

import org.apache.ibatis.session.SqlSession;

import com.google.gson.annotations.Expose;
import com.marspotato.supportsmallshop.util.ConnectionContainer;


public class CreateUpdateShopResponse {
	@Expose
	public String submissionId;
	@Expose
	public String helperId;
	@Expose
	public int responseId;

	public CreateUpdateShopResponse(String submissionId, String helperId, int responseId)
	{
		this.submissionId = submissionId;
		this.helperId = helperId;
		this.responseId = responseId;
	}
	
	public void saveCreateUpdateShopResponseRecord()
	{
		SqlSession session = ConnectionContainer.getDBConnection();
   		session.insert("saveCreateUpdateShopResponseRecord", this);
		session.commit();
	}
}
