package com.marspotato.supportsmallshop.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.marspotato.supportsmallshop.BO.AuthCode;
import com.marspotato.supportsmallshop.BO.CreateUpdateShopResponseType;
import com.marspotato.supportsmallshop.BO.Helper;
import com.marspotato.supportsmallshop.BO.Submission;
import com.marspotato.supportsmallshop.util.InputUtil;
import com.marspotato.supportsmallshop.util.OutputUtil;


@WebServlet("/CreateUpdateShopResponse")
public class CreateUpdateShopResponseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String pageActionName = "CreateUpdateShopResponse";
       
    public CreateUpdateShopResponseServlet() {
        super();
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String code = null;
		String submissionId = null;
		int responseTypeId = 0;

		try 
		{
			code = InputUtil.getEncryptedString(request, "code");
			submissionId = InputUtil.getNonEmptyString(request, "submissionId");
			responseTypeId = InputUtil.getInteger(request, "responseTypeId");
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
		if (ac.registerUsage(pageActionName, "0") == false)
		{
			//used action, try to get back the value
			String value = ac.getUsageValue(pageActionName);
			if (value != null && value.isEmpty() == false)
				OutputUtil.response(response, HttpServletResponse.SC_OK, "");
			else
				OutputUtil.response(response, HttpServletResponse.SC_CONFLICT, "");
			return;
		}
		
		Submission s = Submission.getSubmission(submissionId);
		if (s == null)
		{
			OutputUtil.response(response, HttpServletResponse.SC_NOT_FOUND, "{Error : \"Submission ID not found\"}");
			return;
		}
		CreateUpdateShopResponseType type = CreateUpdateShopResponseType.getCreateUpdateShopResponseType(responseTypeId);
		if (type == null)
		{
			OutputUtil.response(response, HttpServletResponse.SC_NOT_FOUND, "{Error : \"Response Type not found\"}");
			return;
		}
		Helper h = Helper.getHelper(ac.deviceType, ac.regId);
		int output = s.processRequest(type, h.id);
		OutputUtil.response(response, output, "");
	}
}
