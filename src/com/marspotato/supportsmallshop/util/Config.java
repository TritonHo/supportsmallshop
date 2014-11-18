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
	
	//in case no reply content, a dummy JSON to test if the mobile network get hijacked by WIFI provider 
	public final static String dummyJson = "{ \"intColumn\": 123, \"stringColumn\": \"456\" }";
	
	public final static int[] districtType = new int[]{WHOLE_HK, HK_ISLAND, KOWL0ON, NEW_TERRITORIES};
	
	//if a submission get (ACCEPT - REJECT - SERIOUS_REJECT) >= 3, 
	//the submission will be accepted and merged into production 
	public final static int ACCEPT_SUBMISSION_THRESHOLD = 3;
	
	//if a submission get (REJECT + SERIOUS_REJECT - ACCEPT) >= 2, 
	//the submission will be rejected
	public final static int REJECT_SUBMISSION_THRESHOLD = 2;
	
	//every 24 hours, one user can create/update/delete at most 5 shops 
	public final static int MAX_SHOP_ACTION = 5;
	public final static String SHOP_ACTION_COUNT = "shop_action_count";
	
	//every 24 hours, one user can review others at most 10 times 
	public final static int MAX_REVIEW_ACTION = 10;
	public final static String REVIEW_ACTION_COUNT = "review_action_count";
	
	//the period that a record will wait for gcm verification
	public final static int VERIFICATION_PERIOD = 30 * 60; // 30 minutes
	
	//the max number of shop record that will output in listing mode
	public final static int MAX_SHOP_RECORD_LIMIT = 100;
	
	//the max number of shop record that will output in listing mode
	public final static int MAX_CREATE_UPDATE_SUBMISSION_RECORD_LIMIT = 80;
	public final static int MAX_REMOVAL_SUBMISSION_RECORD_LIMIT = 20;
	
	public final static String[] shopTypes = new String[]{"食肆", "零售（食物）","零售（其他）", "服務"}; 
	public final static String[] deviceTypes = new String[]{"ios", "google-android"}; 
	public final static String[] deleteShopReasons = new String[]{"小店已倒閉", "已有重複紀錄", "含有色情、暴力或歧視性內容"}; 
	
	//static resource object
	public static DateTimeFormatter defaultDateTimeFormatter = ISODateTimeFormat.basicDateTimeNoMillis();
	public static Gson defaultGSON = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeTypeConverter()).setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
	public static Gson redisGSON = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeTypeConverter()).create();	
}
