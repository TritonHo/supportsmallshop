package com.marspotato.supportsmallshop.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Config {
	public final static int EARTH_RADIUS = 6371000; 
	
	public final static int WHOLE_HK = 0;
	public final static int HK_ISLAND = 1;
	public final static int KOWL0ON = 2;	
	public final static int NEW_TERRITORIES = 3;
	
	//the period that a record will wait for gcm verification
	public final static int VERIFICATION_PERIOD = 30 * 60; // 30 minutes
	
	public final static String[] shopTypes = new String[]{"食肆", "零售（食物）","零售（其他）", "服務"}; 
	public final static String[] deviceTypes = new String[]{"ios", "google-android"}; 
	public final static String[] deleteShopReasons = new String[]{"小店已倒閉", "已有重複紀錄", "含有色情、暴力或歧視性內容"}; 
	
	//static resource object
	public static DateTimeFormatter defaultDateTimeFormatter = ISODateTimeFormat.basicDateTimeNoMillis();
	public static Gson defaultGSON = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeTypeConverter()).setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
	public static Gson redisGSON = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeTypeConverter()).create();	
}
