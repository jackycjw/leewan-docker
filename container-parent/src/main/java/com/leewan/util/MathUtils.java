package com.leewan.util;


public class MathUtils {

	public static Integer parseInt(String n) {
		if(!StringUtils.hasLength(n)) {
			return null;
		}
		return Integer.parseInt(n.trim());
	}
	
	public static Long parseLong(String n) {
		if(!StringUtils.hasLength(n)) {
			return null;
		}
		return Long.parseLong(n.trim());
	}
	
	public static Double parseDouble(String n) {
		if(!StringUtils.hasLength(n)) {
			return null;
		}
		return Double.parseDouble(n.trim());
	}
	
	public static void main(String[] args) {
		System.out.println(parseInt(""));
	}
}
