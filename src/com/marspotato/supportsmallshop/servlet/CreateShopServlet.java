package com.marspotato.supportsmallshop.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;

import com.marspotato.supportsmallshop.BO.Helper;
import com.marspotato.supportsmallshop.BO.Submission;
import com.marspotato.supportsmallshop.util.Config;
import com.marspotato.supportsmallshop.util.EncryptUtil;
import com.marspotato.supportsmallshop.util.InputUtil;
import com.marspotato.supportsmallshop.util.OutputUtil;


@WebServlet("/CreateShop")
public class CreateShopServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CreateShopServlet() {
        super();
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		Submission s = new Submission();
		String deviceType = null, regId = null;
		DateTime dt = null;
		try 
		{
			deviceType = InputUtil.getStringInRange(request, "deviceType", Config.deviceTypes, false);
			regId = InputUtil.getNonEmptyString(request, "regId");
			dt = InputUtil.getMandatoryDateTime(request, "dt"); 
			
			//mandatory fields
			s.name = InputUtil.getNonEmptyString(request, "name");
			s.shopType = InputUtil.getStringInRange(request, "shopType", Config.shopTypes, false);
			s.shortDescription = InputUtil.getNonEmptyString(request, "shortDescription");
			s.fullDescription = InputUtil.getNonEmptyString(request, "fullDescription");
			s.district = InputUtil.getIntegerWithRange(request, "district", Config.WHOLE_HK, Config.NEW_TERRITORIES);
			s.address = InputUtil.getNonEmptyString(request, "address");
					
			//optional fields
			s.phone = InputUtil.getString(request, "phone", "");
			s.openHours = InputUtil.getString(request, "openHours", "");
			s.searchTags  = InputUtil.getString(request, "searchTags", "");
			s.latitude1000000 = InputUtil.getIntegerWithDefaultValue(request, "latitude1000000", 0);
			s.longitude1000000 = InputUtil.getIntegerWithDefaultValue(request, "longitude1000000", 0);
			//TODO: implement the photo and remove this hardcode
			s.photoUrl = "";
		}
		catch (Exception ex)
		{
			OutputUtil.response(response, HttpServletResponse.SC_BAD_REQUEST, "{Error : \""+ex.getMessage()+"\"}");
			return;
		}
		
		Helper h = Helper.getHelper(deviceType, regId);
		s.helperId = h.id;

		String authCode = EncryptUtil.generateRandomAuthCode();
		int result = s.saveCreateShopSubmissionToRedis(s.helperId, dt, authCode);
		if (result > 0)
		{
			//TODO: implement the server push to client for the authCode
		}
		
		//no matter if the update success, always reply OK
		OutputUtil.response(response, HttpServletResponse.SC_OK, Config.defaultGSON.toJson(s));
		//TODO: add GCM send message
	}
}
