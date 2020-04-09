package com.leewan.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {

	public static String getDateString(long time) {
		return getDateString(time, "yyyyMMdd");
	}
	
	public static String getNow() {
		return getNow("yyyyMMddHHmmss");
	}
	
	public static String getNow(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date());
	}
	
	public static String getNowTimeStamp() {
		return getNow("yyyyMMddHHmmss");
	}
	
	public static boolean isToday(long time) {
		String pattern = "yyyyMMdd";
		String dateString = getDateString(time, pattern);
		String now = getNow(pattern);
		return now.equals(dateString);
	}
	
	
	public static String getDateString(long time, String pattern) {
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	public static String getQuarter(long time) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(time);
		int min = instance.get(Calendar.MINUTE);
		min = min/15 * 15;
		instance.set(Calendar.MINUTE, min);
		return getDateString(instance.getTimeInMillis(),"yyyyMMddHHmm");
	}
	
	public static void main(String[] args) {
		System.out.println(getQuarter(1573647300000L));
	}
	
}
