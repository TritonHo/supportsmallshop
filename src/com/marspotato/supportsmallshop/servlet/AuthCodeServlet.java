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
import com.marspotato.supportsmallshop.util.EncryptUtil;
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
		String regId = null, deviceType = null;
		DateTime dt = null;
		try 
		{
			regId = InputUtil.getNonEmptyString(request, "regId");
			deviceType = InputUtil.getStringInRange(request, "deviceType", Config.deviceTypes, false);
			dt = InputUtil.getMandatoryDateTime(request, "dt");
		}
		catch (Exception ex)
		{
			OutputUtil.response(response, HttpServletResponse.SC_BAD_REQUEST, "{Error : \""+ex.getMessage()+"\"}");
			return;
		}
		AuthCode ac = AuthCode.generateAuthCode(regId, deviceType, dt);
		
		System.out.println("authCode = " + ac.code);
		System.out.println("encrypted authCode = " + EncryptUtil.encrypt(ac.code) );
		//TODO: send the authCode to client by GCM or apple message
		//REMARK: the authCode should be encrypted before sending to client

		OutputUtil.response(response, HttpServletResponse.SC_OK, "");
	}
}
