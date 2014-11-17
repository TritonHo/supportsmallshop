package com.marspotato.supportsmallshop.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.marspotato.supportsmallshop.BO.AuthCode;
import com.marspotato.supportsmallshop.BO.DeleteShopResponse;
import com.marspotato.supportsmallshop.BO.DeleteShopSubmission;
import com.marspotato.supportsmallshop.BO.Helper;
import com.marspotato.supportsmallshop.util.Config;
import com.marspotato.supportsmallshop.util.InputUtil;
import com.marspotato.supportsmallshop.util.OutputUtil;


@WebServlet("/DeleteShopResponse")
public class DeleteShopResponseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String pageActionName = "DeleteShopResponse";
       
    public DeleteShopResponseServlet() {
        super();
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String code = null;
		String removalId = null;
		boolean isAccept = true;

		try 
		{
			code = InputUtil.getEncryptedString(request, "code");
			removalId = InputUtil.getNonEmptyString(request, "removalId");
			isAccept = InputUtil.getMandatoryBoolean(request, "isAccept");
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
		
		DeleteShopSubmission s = DeleteShopSubmission.getDeleteShopSubmission(removalId);
		if (s == null)
		{
			OutputUtil.response(response, HttpServletResponse.SC_NOT_FOUND, "{Error : \"Submission ID not found\"}");
			return;
		}
		
		Helper h = Helper.getHelper(ac.deviceType, ac.regId);
		DeleteShopResponse dsr = new DeleteShopResponse(removalId, h.id, isAccept);
		int output = dsr.processRequest(s);

		OutputUtil.response(response, output, Config.defaultGSON.toJson(dsr));
	}
}
