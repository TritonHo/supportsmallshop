package com.marspotato.supportsmallshop.servlet;


import java.io.IOException;











import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.marspotato.supportsmallshop.BO.Shop;
import com.marspotato.supportsmallshop.util.Config;
import com.marspotato.supportsmallshop.util.InputUtil;
import com.marspotato.supportsmallshop.util.OutputUtil;


@WebServlet("/Shop")
public class ShopServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShopServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		String searchWord = InputUtil.getString(request, "searchWord", "");
		String shopType = InputUtil.getString(request, "shopType", "");
		int latitude1000000 = InputUtil.getIntegerWithDefaultValue(request, "latitude1000000", 0);
		int longitude1000000 = InputUtil.getIntegerWithDefaultValue(request, "longitude1000000", 0);
		double range = InputUtil.getDoubleWithDefaultValue(request, "range", -1);
		int district = InputUtil.getIntegerWithDefaultValue(request, "district", -1);

		boolean isValidShopType = false;
		for (int i = 0; i < Config.shopTypes.length; i++)
			if ( Config.shopTypes[i].equals(shopType))
				isValidShopType = true;
		if (isValidShopType == false)
			shopType = null;
		
		Shop[] shops = Shop.getShops(searchWord, latitude1000000, longitude1000000, range, district, shopType);
		OutputUtil.response(response, HttpServletResponse.SC_OK , Config.defaultGSON.toJson(shops));
	}
}
