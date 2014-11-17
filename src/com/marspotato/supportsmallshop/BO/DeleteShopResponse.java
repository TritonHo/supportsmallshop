package com.marspotato.supportsmallshop.BO;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;

import com.google.gson.annotations.Expose;
import com.marspotato.supportsmallshop.util.ConnectionContainer;


public class DeleteShopResponse  {
	@Expose
	public String removalId;
	@Expose
	public String helperId;
	@Expose
	public boolean isAccept;

	public DeleteShopResponse(String removalId, String helperId, boolean isAccept)
	{
		this.removalId = removalId;
		this.helperId = helperId;
		this.isAccept = isAccept;
	}
	
	public int processRequest(DeleteShopSubmission submission)
	{

		int step = 0;
		SqlSession session = ConnectionContainer.getDBConnection();
		try
		{
	   		session.insert("saveDeleteResponseRecord", this);
	   		step = 1;
	   		submission.checkAndProcessSubmission(isAccept, session);
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
				return HttpServletResponse.SC_FORBIDDEN;
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
