package com.marspotato.supportsmallshop.util;

import javax.servlet.http.HttpServletRequest;

public class InputUtil {
	public static String getString(HttpServletRequest r, String fieldName, String defaultValue)
	{
		String output = r.getParameter(fieldName);
		return output!=null?output:defaultValue;
	}

	public static int getIntegerWithDefaultValue(HttpServletRequest r, String fieldName, int defaultValue)
	{
		int output = defaultValue;
		try
		{
			output = Integer.parseInt(r.getParameter(fieldName));
		}
		catch (Exception ex)
		{
			//no need to handle
		}
		return output;
	}
	public static double getDoubleWithDefaultValue(HttpServletRequest r, String fieldName, double defaultValue)
	{
		double output = defaultValue;
		try
		{
			output = Double.parseDouble(r.getParameter(fieldName));
		}
		catch (Exception ex)
		{
			//no need to handle
		}
		return output;
	}
}
