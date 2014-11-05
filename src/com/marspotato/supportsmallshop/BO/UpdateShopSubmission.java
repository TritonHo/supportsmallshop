package com.marspotato.supportsmallshop.BO;

import org.apache.ibatis.session.SqlSession;

import com.google.gson.annotations.Expose;
import com.marspotato.supportsmallshop.util.ConnectionContainer;

public class UpdateShopSubmission extends Submission{
	@Expose
	public String shopId;
	@Expose
	public boolean updateShopType;
	@Expose
	public boolean updateDistrict;
	@Expose
	public boolean updateLocation;

	
	public void saveUpdateShopRecord()
	{
		SqlSession session = ConnectionContainer.getDBConnection();
		try
		{
	   		session.insert("saveUpdateShopRecord", this);
			session.commit();
		}
		finally
		{
			session.close();
		}
	}
	public static UpdateShopSubmission getUpdateShopSubmission(String id)
	{
		SqlSession session = ConnectionContainer.getDBConnection();
		UpdateShopSubmission output = null;
		try
		{
			output = session.selectOne("getUpdateShopSubmission", id);
		}
		finally
		{
			session.close();
		}
		return output;
	}

	@Override
	public void onAcceptAction(SqlSession session) {
		session.update("mergeShopWithSubmission", id);
	}
}
