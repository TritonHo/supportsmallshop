package com.marspotato.supportsmallshop.servlet;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.marspotato.supportsmallshop.BO.AuthCode;
import com.marspotato.supportsmallshop.BO.Helper;
import com.marspotato.supportsmallshop.BO.Shop;
import com.marspotato.supportsmallshop.BO.UpdateShopSubmission;
import com.marspotato.supportsmallshop.output.UpdateShopSubmissionOutput;
import com.marspotato.supportsmallshop.util.Config;
import com.marspotato.supportsmallshop.util.CounterUtil;
import com.marspotato.supportsmallshop.util.InputUtil;
import com.marspotato.supportsmallshop.util.OutputUtil;


@WebServlet("/UpdateShopSubmission")
public class UpdateShopSubmissionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String pageActionName = "UpdateShop";
	
       
    public UpdateShopSubmissionServlet() {
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
    	UpdateShopSubmissionOutput output = new UpdateShopSubmissionOutput();
    	output.s = UpdateShopSubmission.getUpdateShopSubmission(submissionId);
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
		UpdateShopSubmission s = new UpdateShopSubmission();

		try 
		{
			code = InputUtil.getEncryptedString(request, "code");
			
			s.id = UUID.randomUUID().toString();
			//mandatory fields
			s.shopId = InputUtil.getNonEmptyString(request, "shopId");
			s.updateShopType = InputUtil.getMandatoryBoolean(request, "updateShopType");
			s.updateDistrict = InputUtil.getMandatoryBoolean(request, "updateDistrict");
			s.updateLocation = InputUtil.getMandatoryBoolean(request, "updateLocation");
			
			//mandatory fields
			if (InputUtil.getMandatoryBoolean(request, "updateName") )
				s.name = InputUtil.getNonEmptyString(request, "name");
			if (s.updateShopType == true)
				s.shopType = InputUtil.getStringInRange(request, "shopType", Config.shopTypes);
			if (InputUtil.getMandatoryBoolean(request, "updateShortDescription") )
				s.shortDescription = InputUtil.getNonEmptyString(request, "shortDescription");
			if (InputUtil.getMandatoryBoolean(request, "updateFullDescription") )
				s.fullDescription = InputUtil.getNonEmptyString(request, "fullDescription");

			if (s.updateDistrict == true)
				s.district = InputUtil.getIntegerInEnum(request, "district", Config.districtType);
			if (InputUtil.getMandatoryBoolean(request, "updateAddress") )
				s.address = InputUtil.getNonEmptyString(request, "address");
					
			//optional fields
			s.phone = request.getParameter("phone");
			s.searchTags = request.getParameter("searchTags");
			
			if (s.updateLocation == true)
			{
				s.latitude1000000 = InputUtil.getIntegerWithDefaultValue(request, "latitude1000000", 0);
				s.longitude1000000 = InputUtil.getIntegerWithDefaultValue(request, "longitude1000000", 0);
			}
			//TODO: implement the photo and remove this hardcode
			s.photoUrl = null;
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
				s = UpdateShopSubmission.getUpdateShopSubmission(id);
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
		if (CounterUtil.increaseShopActionCount(h.id))
		{
			OutputUtil.response(response, HttpServletResponse.SC_FORBIDDEN, "");
			return;
		}
		s.helperId = h.id;
		s.saveUpdateShopRecord();

		OutputUtil.response(response, HttpServletResponse.SC_OK, Config.defaultGSON.toJson(s));
	}
}
