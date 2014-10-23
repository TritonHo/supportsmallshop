package com.marspotato.supportsmallshop.util;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;

public class InputUtil {
	public static String getString(HttpServletRequest r, String fieldName, String defaultValue)
	{
		String output = r.getParameter(fieldName);
		return output!=null?output:defaultValue;
	}
	public static String getStringInRange(HttpServletRequest r, String fieldName, String[] ranges, boolean allowNull) throws Exception 
	{
		String output = r.getParameter(fieldName);
		if (allowNull == true && output == null)
			return output;
		
		boolean matched = false;
		for (int i = 0; i < ranges.length; i++)
			if (output.equals(ranges[i]))
			{
				matched = true;
				break;
			}
		if (!matched)
			throw new Exception("Parameter '" +fieldName+ "' is invalid" );
		
		return output;
	}
	public static String getEncryptedString(HttpServletRequest r, String fieldName) throws Exception 
	{
		String temp = r.getParameter(fieldName);
		if (temp == null || temp.isEmpty())
			throw new Exception("Parameter '" +fieldName+ "' is invalid" );
		
		String output = EncryptUtil.decrypt(temp);
		if (output == null || output.isEmpty())
			throw new Exception("Parameter '" +fieldName+ "' is invalid" );
		
		return output;
	}
	
	public static String getNonEmptyString(HttpServletRequest r, String fieldName) throws Exception 
	{
		String output = r.getParameter(fieldName);
		if (output == null || output.isEmpty())
			throw new Exception("Parameter '" +fieldName+ "' is invalid" );
		
		return output;
	}

	public static int getIntegerWithRange(HttpServletRequest r, String fieldName, int minValue, int maxValue) throws Exception 
	{
		int output = -1;
		try
		{
			output = Integer.parseInt(r.getParameter(fieldName));
		}
		catch (Exception ex)
		{
			throw new Exception("Parameter '" +fieldName+ "' is invalid" );
		}
		if (output > maxValue || output < minValue)
			throw new Exception("Parameter '" +fieldName+ "' is invalid" );
		
		return output;
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
	public static DateTime getMandatoryDateTime(HttpServletRequest r, String fieldName)throws Exception
	{
		DateTime output = null;
		try
		{
			output = Config.defaultDateTimeFormatter.parseDateTime(r.getParameter(fieldName));
		}
		catch (Exception ex)
		{
			throw new Exception("Parameter '" +fieldName+ "' is invalid" );
		}
		return output;
	}
	public static boolean getMandatoryBoolean(HttpServletRequest r, String fieldName)throws Exception
	{
		String output = r.getParameter(fieldName);
		if (output == null || output.isEmpty())
			throw new Exception("Parameter '" +fieldName+ "' is invalid" );
		if ("0".equals(output) == false && "1".equals(output) == false)
			throw new Exception("Parameter '" +fieldName+ "' is invalid" );
		return "1".equals(output);
	}
}
