package com.leewan.util;

import java.util.Base64;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;

import com.leewan.config.ConfigManager;

public class ImageRepositoryUtils {
	
	private static String rootUrl;

	public static HttpGet getRequest(String url) {
		HttpGet get = new HttpGet(getRootUrl() + url);
		get.setConfig(HTTPUtils.getRequestConfig());
		get.addHeader("Authorization", getAuthorization());
		return get;
	}
	
	public static HttpDelete getDelRequest(String url) {
		HttpDelete delete = new HttpDelete(getRootUrl() + url);
		delete.setConfig(HTTPUtils.getRequestConfig());
		delete.addHeader("Authorization", getAuthorization());
		return delete;
	}
	
	public static String getRootUrl() {
		if(StringUtils.hasLength(rootUrl)) {
			return rootUrl;
		}
		rootUrl = "http://" + ConfigManager.getString("docker.registry.url");
		return rootUrl;
	}
	
	public static String getAuthorization() {
		String user = ConfigManager.getString("docker.registry.user");
		String pwd = ConfigManager.getString("docker.registry.password");
		
		String auth = user + ":" + pwd;
		byte[] encode = Base64.getEncoder().encode(auth.getBytes());
		return "Basic " + new String(encode);
	}
}
