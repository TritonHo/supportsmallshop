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
import com.marspotato.supportsmallshop.BO.CreateShopSubmission;
import com.marspotato.supportsmallshop.util.Config;
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
    	/*
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
    	SubmissionOutput submissionOutput = new SubmissionOutput();
    	submissionOutput.s = CreateShopSubmission.getCreateShopSubmission(submissionId);

    	
    	if (submissionOutput.s.shopId == null)
    	{
    		OutputUtil.response(response, HttpServletResponse.SC_BAD_REQUEST, "{Error : \"The Submission Type is invalid\"}");
    		return;
    	}
    	submissionOutput.isCreator = submissionOutput.s.helperId.equals(helperId);
    	submissionOutput.isReviewer = submissionOutput.s.isReviewer(helperId);
    	submissionOutput.shop = Shop.getShopById(submissionOutput.s.shopId);
    	
    	OutputUtil.response(response, HttpServletResponse.SC_OK, Config.defaultGSON.toJson(submissionOutput));
    	*/
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		/*
		String code = null;
		CreateShopSubmission s = new CreateShopSubmission();
		boolean updateDistrict, updateLocation;
		try 
		{
			code = InputUtil.getEncryptedString(request, "code");
			
			s.id = UUID.randomUUID().toString();
			//mandatory fields
			s.shopId = InputUtil.getNonEmptyString(request, "name");
			
			if (InputUtil.getMandatoryBoolean(request, "updateName") )
				s.name = InputUtil.getNonEmptyString(request, "name");
			if (InputUtil.getMandatoryBoolean(request, "updateShopType") )
				s.shopType = InputUtil.getStringInRange(request, "shopType", Config.shopTypes);
			if (InputUtil.getMandatoryBoolean(request, "updateShortDescription") )
				s.shortDescription = InputUtil.getNonEmptyString(request, "shortDescription");
			if (InputUtil.getMandatoryBoolean(request, "updateFullDescription") )
				s.fullDescription = InputUtil.getNonEmptyString(request, "fullDescription");
			updateDistrict = InputUtil.getMandatoryBoolean(request, "updateDistrict");
			if (updateDistrict == true )
				s.district = InputUtil.getIntegerInEnum(request, "district", Config.districtType);
			if (InputUtil.getMandatoryBoolean(request, "updateAddress") )
				s.address = InputUtil.getNonEmptyString(request, "address");
					
			//optional fields
			s.phone = request.getParameter("phone");
			s.searchTags = request.getParameter("searchTags");
			
			updateLocation = InputUtil.getMandatoryBoolean(request, "updateLocation");
			if (updateLocation == true)
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
				s = CreateShopSubmission.getCreateShopSubmission(id);
				OutputUtil.response(response, HttpServletResponse.SC_OK, Config.defaultGSON.toJson(s));
			}
			else
				OutputUtil.response(response, HttpServletResponse.SC_CONFLICT, "");
			return;
		}
		
		//save the record into database
		Helper h = Helper.getHelper(ac.deviceType, ac.regId);
		s.helperId = h.id;
	//	Submission.saveUpdateShopRecord(s, updateDistrict, updateLocation);
		OutputUtil.response(response, HttpServletResponse.SC_OK, Config.defaultGSON.toJson(s));
		*/
	}
}
