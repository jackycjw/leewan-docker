package com.leewan.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class HTTPUtils {

	public static HttpClient getClient() {
		return HttpClientBuilder.create().build();
	}
	
	
	public static RequestConfig getRequestConfig() {
		Builder builder = RequestConfig.custom().setConnectTimeout(10000)
			.setSocketTimeout(10000)
			.setConnectionRequestTimeout(10000);
		return builder.build();
	}
	
	
	public static String post(String url, String data) throws ClientProtocolException, IOException {
		HttpClient client = getClient();
		HttpPost post = new HttpPost(url);
		if(StringUtils.hasLength(data)) {
			post.setEntity(new StringEntity(data, StringUtils.DEFAULT_CHARSET));
		}
		post.setConfig(getRequestConfig());
		post.addHeader("Content-Type", "application/json");
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if(statusCode == 200 || statusCode == 201) {
			String resBody = StringUtils.getFromSteam(response.getEntity().getContent());
			return resBody;
		}else {
			return null;
		}
	}
	
	
	public static String get(String url) throws ClientProtocolException, IOException {
		HttpClient client = getClient();
		HttpGet get = new HttpGet(url);
		get.setConfig(getRequestConfig());
		HttpResponse response = client.execute(get);
		int statusCode = response.getStatusLine().getStatusCode();
		if(statusCode == 200 || statusCode == 201) {
			String resBody = StringUtils.getFromSteam(response.getEntity().getContent());
			return resBody;
		}else {
			return null;
		}
	}
	
	public static String delete(String url) throws ClientProtocolException, IOException {
		HttpClient client = getClient();
		HttpDelete method = new HttpDelete(url);
		method.setConfig(getRequestConfig());
		HttpResponse response = client.execute(method);
		int statusCode = response.getStatusLine().getStatusCode();
		if(statusCode == 200 || statusCode == 201) {
			String resBody = StringUtils.getFromSteam(response.getEntity().getContent());
			return resBody;
		}else {
			return null;
		}
	}
	
}
