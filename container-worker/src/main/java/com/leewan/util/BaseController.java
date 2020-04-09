package com.leewan.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


import com.alibaba.fastjson.JSONObject;


public class BaseController {

	@Autowired
	protected HttpServletRequest request;
	
	
	
	
	public static Map<String, Object> getUrlParamValue(String urlParams){
		// publicCode=abcd&type=yx
		Map<String, Object> values = new HashMap<String, Object>();
		if(urlParams != null && !"".equals(urlParams)){
			String[] params = urlParams.split("&");
			for(String param : params){
				String[] split = param.split("=");
				String key = split[0];
				String value = split.length>1?split[1]:"";
				values.put(key, value);
			}
		}
		return values;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> parseJsonPostParameters(JSONObject jo){
		Map<String, Object> dest = new HashMap<String, Object>(jo);
		return dest;
	}
	
	protected Map<String, Object> getParamters() {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		String params = request.getQueryString();
		paramsMap.putAll(getUrlParamValue(params));
		
		Map<String, String[]> tmp = request.getParameterMap();
		for(Entry<String, String[]> item : tmp.entrySet()) {
			if(item.getValue() == null || item.getValue().length == 0) {
				paramsMap.put(item.getKey(), null);
			}else if(item.getValue().length == 1) {
				paramsMap.put(item.getKey(), item.getValue()[0]);
			}else {
				paramsMap.put(item.getKey(), item.getValue());
			}
		}
		return paramsMap;
	}
	
	protected Map<String, Object> getSteamParamters() {
		try {
			ServletInputStream in = request.getInputStream();
			String fromSteam = StringUtils.getFromSteam(in);
			return JSONObject.parseObject(fromSteam);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
