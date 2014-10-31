package com.marspotato.supportsmallshop.BO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.ibatis.session.SqlSession;

import com.google.gson.annotations.Expose;
import com.marspotato.supportsmallshop.util.Config;
import com.marspotato.supportsmallshop.util.ConnectionContainer;


public class GenericSubmission {
	public static final int CREATE_TYPE = 1;
	public static final int UPDATE_TYPE = 2;
	public static final int DELETE_TYPE = 3;
	
	@Expose
	public int submissionType;
	@Expose
	public String submissionId;//null for CreateShopSubmission
	@Expose
	public String shopName;
	@Expose
	public String shopShortDesc;
	

	public static GenericSubmission[] getGenericSubmissions(String searchWord, int district, String shopType, boolean getLatest) {
		List<GenericSubmission> cuRecords = null;
		List<GenericSubmission> removalRecords = null;
		SqlSession session = ConnectionContainer.getDBConnection();
		try {
			HashMap<String, Object> h = new HashMap<String, Object>();
			h.put("MAX_CREATE_UPDATE_SUBMISSION_RECORD_LIMIT", Config.MAX_CREATE_UPDATE_SUBMISSION_RECORD_LIMIT);
			h.put("MAX_REMOVAL_SUBMISSION_RECORD_LIMIT", Config.MAX_REMOVAL_SUBMISSION_RECORD_LIMIT);
			h.put("CREATE_TYPE", CREATE_TYPE);
			h.put("UPDATE_TYPE", UPDATE_TYPE);
			h.put("DELETE_TYPE", DELETE_TYPE);
			
			if (getLatest == true)
				h.put("getLatest", getLatest);
			if (searchWord != null && searchWord.isEmpty() == false)
				h.put("searchWord", searchWord);
			if (shopType != null && shopType.isEmpty() == false)
				h.put("shopType", shopType);
			if (district != Config.WHOLE_HK)
				h.put("district", district);
			
			cuRecords = session.selectList("getCUGenericSubmissions", h);
			//TODO: implement it
			//removalRecords = session.selectList("getRemovalGenericSubmissions", h);
			removalRecords = new ArrayList<GenericSubmission>();
		} finally {
			session.close();
		}

		GenericSubmission[] output = new GenericSubmission[cuRecords.size() + removalRecords.size()];
		int t = 0;
		for (GenericSubmission gs : cuRecords)
			output[t++] = gs;
		for (GenericSubmission gs : removalRecords)
			output[t++] = gs;
		
		
		Random r = new Random();
		//Fisher–Yates shuffle
		for (int i = 0; i < output.length * 2; i++)
		{
			int pos1 = r.nextInt(output.length);
			int pos2 = r.nextInt(output.length);
			GenericSubmission temp = output[pos1];
			output[pos1] = output[pos2];
			output[pos2] = temp;
		}
		return output;
	}
	
}
