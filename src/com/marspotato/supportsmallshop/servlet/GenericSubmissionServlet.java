package com.marspotato.supportsmallshop.servlet;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.marspotato.supportsmallshop.BO.GenericSubmission;
import com.marspotato.supportsmallshop.util.Config;
import com.marspotato.supportsmallshop.util.InputUtil;
import com.marspotato.supportsmallshop.util.OutputUtil;


@WebServlet("/GenericSubmission")
public class GenericSubmissionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GenericSubmissionServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//TODO: involve full time scan, has performance issue, need to add caching and randonSeedPaging? 
		boolean getLatest = InputUtil.getBoolean(request, "getLatest", false);
		String searchWord = InputUtil.getString(request, "searchWord", "");
		String shopType = InputUtil.getStringInRangeAllowNull(request, "shopType", Config.shopTypes);
		int district = InputUtil.getIntegerInEnumWithDefaultValue(request, "district", Config.districtType, Config.WHOLE_HK);
		
		GenericSubmission[] output = GenericSubmission.getGenericSubmissions(searchWord, district, shopType, getLatest);
		OutputUtil.response(response, HttpServletResponse.SC_OK , Config.defaultGSON.toJson(output));
	}
}
