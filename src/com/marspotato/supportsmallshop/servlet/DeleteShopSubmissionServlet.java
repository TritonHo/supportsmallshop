package com.marspotato.supportsmallshop.servlet;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.marspotato.supportsmallshop.BO.AuthCode;
import com.marspotato.supportsmallshop.BO.DeleteShopSubmission;
import com.marspotato.supportsmallshop.BO.Helper;
import com.marspotato.supportsmallshop.BO.Shop;
import com.marspotato.supportsmallshop.output.DeleteShopSubmissionOutput;
import com.marspotato.supportsmallshop.util.Config;
import com.marspotato.supportsmallshop.util.InputUtil;
import com.marspotato.supportsmallshop.util.OutputUtil;


@WebServlet("/DeleteShopSubmission")
public class DeleteShopSubmissionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String pageActionName = "DeleteShop";
	
       
    public DeleteShopSubmissionServlet() {
        super();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
    	String submissionId = null, helperId = "";
    	try 
		{
			submissionId = InputUtil.getNonEmptyString(request, "submissionId");
			helperId = InputUtil.getString(request, "helperId", "");
		}
		catch (Exception ex)
		{
			OutputUtil.response(response, HttpServletResponse.SC_BAD_REQUEST, "{Error : \""+ex.getMessage()+"\"}");
			return;
		}
    	DeleteShopSubmissionOutput output = new DeleteShopSubmissionOutput();
    	output.s = DeleteShopSubmission.getDeleteShopSubmission(submissionId);
    	if (output.s == null)
    	{
       		OutputUtil.response(response, HttpServletResponse.SC_NOT_FOUND, "");
       		return;
    	}
    	output.isCreator = output.s.helperId.equals(helperId);
    	output.isReviewer = output.s.isReviewer(helperId);
    	output.shop = Shop.getShopById(output.s.shopId);
    	output.s.helperId = null;//maximum protection on privacy
    	
    	OutputUtil.response(response, HttpServletResponse.SC_OK, Config.defaultGSON.toJson(output));
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		String code = null;
		DeleteShopSubmission s = new DeleteShopSubmission();

		try 
		{
			code = InputUtil.getEncryptedString(request, "code");
			s.id = UUID.randomUUID().toString();
			//mandatory fields
			s.shopId = InputUtil.getNonEmptyString(request, "shopId");
			s.reason = InputUtil.getStringInRange(request, "reason", Config.deleteShopReasons);
		}
		catch (Exception ex)
		{
			OutputUtil.response(response, HttpServletResponse.SC_BAD_REQUEST, "{Error : \""+ex.getMessage()+"\"}");
			return;
		}
		
		AuthCode ac = AuthCode.getAuthCode(code);
		if (ac == null)
		{
			OutputUtil.response(response, HttpServletResponse.SC_UNAUTHORIZED, "");
			return;
		}
		if (ac.registerUsage(pageActionName, s.id) == false)
		{
			//used action, try to get back the value
			String id = ac.getUsageValue(pageActionName);
			if (id != null)
			{
				s = DeleteShopSubmission.getDeleteShopSubmission(id);
				OutputUtil.response(response, HttpServletResponse.SC_OK, Config.defaultGSON.toJson(s));
			}
			else
				OutputUtil.response(response, HttpServletResponse.SC_CONFLICT, "");
			return;
		}
		//check if the shop record exists
		Shop shop = Shop.getShopById(s.shopId);
		if (shop == null)
		{
			OutputUtil.response(response, HttpServletResponse.SC_NOT_FOUND, "");
			return;
		}
		//save the record into database
		Helper h = Helper.getHelper(ac.deviceType, ac.regId);
		s.helperId = h.id;
		s.saveDeleteShopRecord();

		OutputUtil.response(response, HttpServletResponse.SC_OK, Config.defaultGSON.toJson(s));
	}
}
