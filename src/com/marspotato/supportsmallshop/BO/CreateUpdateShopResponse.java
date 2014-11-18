package com.marspotato.supportsmallshop.BO;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;

import com.google.gson.annotations.Expose;
import com.marspotato.supportsmallshop.util.ConnectionContainer;


public class CreateUpdateShopResponse  {
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
	
	public int processRequest(CreateUpdateShopResponseType type, Submission submission)
	{

		int step = 0;
		SqlSession session = ConnectionContainer.getDBConnection();
		try
		{
	   		session.insert("saveCreateUpdateShopResponseRecord", this);
	   		step = 1;
	   		submission.checkAndProcessSubmission(type, session);
	   		step = 2;
	   		session.update("updateHelperLastUpdateTime", helperId);
		   	step = 3;
			session.commit();
		}
		catch (Exception ex)
		{
			if (step == 0)
				//there is checking on Submission record, 
				//thus if fail on this step, Must be caused by duplicate PK problem
				return HttpServletResponse.SC_OK;
			else
			{
				ex.printStackTrace();
				//some strange server error
				return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			}
		}
		finally
		{
			session.close();
		}
		return HttpServletResponse.SC_OK;
	}
}
