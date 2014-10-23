package com.marspotato.supportsmallshop.BO;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.ibatis.session.SqlSession;

import com.google.gson.annotations.Expose;
import com.marspotato.supportsmallshop.util.Config;
import com.marspotato.supportsmallshop.util.ConnectionContainer;

public class Shop {
	@Expose
	public String id;
	@Expose
	public String name;
	@Expose
	public String shortDescription;
	@Expose
	public String fullDescription;
	@Expose
	public String searchTags;
	
	@Expose
	public String shopType;
	@Expose
	public String openHours;
	@Expose
	public int district;
	@Expose
	public String address;
	@Expose
	public String phone;
	@Expose
	public int latitude1000000; /* the value of latitude * 1000000, the Accuracy is ~0.1m */
	@Expose
	public int longitude1000000; /* the value of longitude * 1000000, the Accuracy is ~0.1m */
	@Expose
	public String photoUrl;
	
	//eulerian distance is used, some error but acceptable for HongKong
	public boolean isWithinSearch(int latitude1000000, int longitude1000000, double searchRange)
	{
		final double rangeInDegree1000000 = searchRange / (Config.EARTH_RADIUS * 2 * Math.PI) * 360 * 1000000;
		double distanceSquare = Math.pow(this.latitude1000000 - latitude1000000, 2) + Math.pow(this.longitude1000000 - longitude1000000, 2);
		return distanceSquare <= Math.pow(rangeInDegree1000000, 2); 
	}
	
	//if searchRange > 0, 
	//	then eulerian distance between the shop location and the input location 
	//	must be with in searchRange(measured in meter)    
	public static Shop[] getShops(String searchWord, int latitude1000000, int longitude1000000, double searchRange, int district, String shopType) {
		List<Shop> shops = null;
		SqlSession session = ConnectionContainer.getDBConnection();
		try {
			HashMap<String, Object> h = new HashMap<String, Object>();
			if (searchRange > 0)
			{
				AreaBlock[] areaBlocks = AreaBlock.getInvolvedAreaBlock(latitude1000000, longitude1000000, searchRange);
	
				h.put("hasGeoSearch", 1);
				h.put("areaBlockArray", areaBlocks);
			}
			if (searchWord != null && searchWord.isEmpty() == false)
				h.put("searchWord", searchWord);
			if (shopType != null && shopType.isEmpty() == false)
				h.put("shopType", shopType);
			if (district == Config.HK_ISLAND || district == Config.KOWL0ON || district == Config.NEW_TERRITORIES)
				h.put("district", district);
			
			shops = session.selectList("getShops", h);
		} finally {
			session.close();
		}
		//filter out the shop not in the circle
		//do NOT perform it in database to avoid affecting the execution plan
		//the discarded record should be small, thus network overhead is not huge
		Vector<Shop> v = new Vector<Shop>();
		for (Shop s : shops)
			// if searchRange < 0, whole list is included
			if (searchRange < 0 || s.isWithinSearch(latitude1000000, longitude1000000, searchRange))
				v.add(s);
		
		Shop[] output = new Shop[v.size()];
		v.toArray(output);
		return output;
	}
}
