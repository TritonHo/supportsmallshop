package com.marspotato.supportsmallshop.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;

import com.marspotato.supportsmallshop.BO.AuthCode;
import com.marspotato.supportsmallshop.util.Config;
import com.marspotato.supportsmallshop.util.InputUtil;
import com.marspotato.supportsmallshop.util.OutputUtil;


@WebServlet("/AuthCode")
public class AuthCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AuthCodeServlet() {
        super();
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		//TODO: consider the dt to handle double post
		String regId = null, deviceType = null;
		DateTime dt = null;
		try 
		{
			regId = InputUtil.getNonEmptyString(request, "regId");
			deviceType = InputUtil.getStringInRange(request, "deviceType", Config.deviceTypes);
			dt = InputUtil.getMandatoryDateTime(request, "dt");
		}
		catch (Exception ex)
		{
			OutputUtil.response(response, HttpServletResponse.SC_BAD_REQUEST, "{Error : \""+ex.getMessage()+"\"}");
			return;
		}
		AuthCode ac = AuthCode.generateAuthCode(regId, deviceType, dt);
		ac.storeIntoRedisOutbox();

		OutputUtil.response(response, HttpServletResponse.SC_OK, Config.dummyJson);
	}
}
