package com.marspotato.supportsmallshop.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.marspotato.supportsmallshop.BO.CreateUpdateShopResponseType;
import com.marspotato.supportsmallshop.util.Config;
import com.marspotato.supportsmallshop.util.OutputUtil;


@WebServlet("/CreateUpdateShopResponseType")
public class CreateUpdateShopResponseTypeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CreateUpdateShopResponseTypeServlet() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{			
		OutputUtil.response(response, HttpServletResponse.SC_OK , Config.defaultGSON.toJson(CreateUpdateShopResponseType.getCreateUpdateShopResponseTypes() ));
	}
}
