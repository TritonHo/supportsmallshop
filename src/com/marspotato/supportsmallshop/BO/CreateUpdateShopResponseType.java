package com.marspotato.supportsmallshop.BO;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import com.google.gson.annotations.Expose;
import com.marspotato.supportsmallshop.util.ConnectionContainer;

public class CreateUpdateShopResponseType {
	@Expose
	public int id;
	@Expose
	public String message;
	@Expose
	public boolean isReject;
	@Expose
	public boolean isSeriousReject;
	@Expose
	public boolean isAccept;

	public static CreateUpdateShopResponseType getCreateUpdateShopResponseType(int id)
	{
		CreateUpdateShopResponseType output = null;
		SqlSession session = ConnectionContainer.getDBConnection();
		try {
			output = session.selectOne("getCreateUpdateShopResponseType", id);
		} finally {
			session.close();
		}
		return output;
	}
	public static CreateUpdateShopResponseType[] getCreateUpdateShopResponseTypes()
	{
		List<CreateUpdateShopResponseType> records = null;
		SqlSession session = ConnectionContainer.getDBConnection();
		try {
			records = session.selectList("getCreateUpdateShopResponseTypes");
		} finally {
			session.close();
		}
			
		CreateUpdateShopResponseType[] output = new CreateUpdateShopResponseType[records.size()];
		records.toArray(output);
		return output;
	}
}
