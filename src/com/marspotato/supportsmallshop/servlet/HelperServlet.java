package com.marspotato.supportsmallshop.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.marspotato.supportsmallshop.BO.Helper;
import com.marspotato.supportsmallshop.util.Config;
import com.marspotato.supportsmallshop.util.InputUtil;
import com.marspotato.supportsmallshop.util.OutputUtil;


@WebServlet("/Helper")
public class HelperServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public HelperServlet() {
        super();
    }
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		String id = null, oldRegId = null, newRegId = null, deviceType = null;
		try 
		{
			id = InputUtil.getNonEmptyString(request, "id");
			oldRegId = InputUtil.getNonEmptyString(request, "oldRegId");
			newRegId = InputUtil.getNonEmptyString(request, "newRegId");
			deviceType = InputUtil.getStringInRange(request, "deviceType", Config.deviceTypes);
		}
		catch (Exception ex)
		{
			OutputUtil.response(response, HttpServletResponse.SC_BAD_REQUEST, "{Error : \""+ex.getMessage()+"\"}");
			return;
		}
		Helper.updateHelperRegId(id, deviceType, newRegId, oldRegId);
		
		//no matter if the update success, always reply OK
		OutputUtil.response(response, HttpServletResponse.SC_OK, Config.dummyJson);
	}
}
