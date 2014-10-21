package com.marspotato.supportsmallshop.BO;

import java.util.Vector;

import com.marspotato.supportsmallshop.util.Config;

public class AreaBlock {

	public int latitude1000;
	public int longitude1000;
	
	public AreaBlock(int latitude1000, int longitude1000)
	{
		this.latitude1000 = latitude1000;
		this.longitude1000 = longitude1000;
	}
	
	
	//if some area in the block interact with the circle, return true
	//Pseudo code comes from:
	//http://stackoverflow.com/questions/401847/circle-rectangle-collision-detection-intersection
	public boolean isWithInDistance(int latitude1000000, int longitude1000000, double range)
	{
		final int halfAreaLength = 500;
		final int rangeInDegree1000000 = (int)(range / (Config.EARTH_RADIUS * 2 * Math.PI) * 360 * 1000000);
		
		int distanceY = Math.abs(latitude1000000 - (latitude1000 * 1000 + halfAreaLength) );
		int distanceX = Math.abs(longitude1000000 - (longitude1000 * 1000 + halfAreaLength) );
		
		//too far ways, always no intersaction
		if (distanceY > halfAreaLength + rangeInDegree1000000 || distanceX > halfAreaLength + rangeInDegree1000000 )
			return false;
		
		if (distanceY <= halfAreaLength || distanceX <= halfAreaLength )
			return true;
		
		double cornerDistanceSquare = Math.pow(distanceY - halfAreaLength, 2) + Math.pow(distanceX - halfAreaLength, 2);
		return cornerDistanceSquare <= Math.pow(rangeInDegree1000000, 2);
	}	
	
	//get all blocks that have interaction with the searching circle.
	//the range is the radius, measured in meter
	//eulerian distance is used, some error but acceptable for HongKong
	public static AreaBlock[] getInvolvedAreaBlock(int latitude1000000, int longitude1000000, double range) {
		//the value of degree * 100
		final int rangeInDegree1000 = (int)(range / (Config.EARTH_RADIUS * 2 * Math.PI) * 360 * 1000);

		int latMin1000 = latitude1000000 / 1000 - rangeInDegree1000;
		int latMax1000 = latitude1000000 / 1000 + rangeInDegree1000;
		int longMin1000 = longitude1000000 / 1000 - rangeInDegree1000;
		int longMax1000 = longitude1000000 / 1000 + rangeInDegree1000;
		
		Vector<AreaBlock> output = new Vector<AreaBlock>(latitude1000000 / 1000, longitude1000000 / 1000);
		for (int y = latMin1000 - 1; y <= latMax1000; y++)
			for (int x = longMin1000 - 1; x <= longMax1000; x++)
			{
				AreaBlock ab = new AreaBlock(y, x);
				if (ab.isWithInDistance(latitude1000000, longitude1000000, range))
					output.add(ab);
			}
		
		AreaBlock[] result = new AreaBlock[output.size()];
		output.toArray(result);
		return result;
	}
}
