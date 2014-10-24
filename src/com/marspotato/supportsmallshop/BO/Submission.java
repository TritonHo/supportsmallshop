package com.marspotato.supportsmallshop.BO;

import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;

import com.google.gson.annotations.Expose;
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

	public int processRequest(CreateUpdateShopResponseType type, String helperId)
	{
		int step = 0;
		SqlSession session = ConnectionContainer.getDBConnection();
		try
		{
	   		session.insert("saveCreateUpdateShopResponseRecord", new CreateUpdateShopResponse(this.id, helperId, type.id));
	   		step = 1;
			HashMap<String, Object> h = new HashMap<String, Object>();
			h.put("rejectIncrement", type.isReject?1:0 );
			h.put("seriousRejectIncrement", type.isSeriousReject?1:0 );
			h.put("acceptIncrement", type.isAccept?1:0 );
			h.put("id", this.id);
			session.update("increaseResponseCount", h);
	   		step = 2;
	   		session.update("updateHelperLastUpdateTime", helperId);
			session.commit();
		}
		catch (Exception ex)
		{
			if (step == 0)
				//there is checking on Submission record, 
				//thus if fail on this step, Must be caused by duplicate PK problem
				return HttpServletResponse.SC_FORBIDDEN;
			else
				//some strange server error
				return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		finally
		{
			session.close();
		}
		//TODO: handle the create new shop and merge shop record
		return HttpServletResponse.SC_OK;
	}
	public void saveCreateShopRecord()
	{
		SqlSession session = ConnectionContainer.getDBConnection();
   		session.insert("saveCreateShopRecord", this);
		session.commit();
		session.close();
	}
	public static Submission getSubmission(String id)
	{
		SqlSession session = ConnectionContainer.getDBConnection();
		Submission output = session.selectOne("getSubmission", id);
		session.commit();
		session.close();
		return output;
	}
}
