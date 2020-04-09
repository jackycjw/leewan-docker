package com.leewan.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.leewan.ManagerApplicationStarter;
import com.leewan.aop.Authority;
import com.leewan.aop.OperateAuth;
import com.leewan.util.BaseController;
import com.leewan.util.HTTPUtils;
import com.leewan.util.ImageRepositoryUtils;
import com.leewan.util.R;
import com.leewan.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("image")
public class ImageRepositoryController extends BaseController {

	@RequestMapping("getImageList")
	public Object getImageList() throws IOException{
		HttpClient client = HTTPUtils.getClient();
		HttpGet request = ImageRepositoryUtils.getRequest("/v2/_catalog");
		HttpResponse response = client.execute(request);
		InputStream content = response.getEntity().getContent();
		String res = StringUtils.getFromSteam(content);
		JSONObject obj = JSONObject.parseObject(res);
		
		JSONArray jsonArray = obj.getJSONArray("repositories");
		return R.success().setData(jsonArray);
	}
	
	@RequestMapping(path = "getImageTags")
	public Object getImageTags() throws IOException{
		Map<String, Object> paramters = super.getSteamParamters();
		String name = (String) paramters.get("name");
		HttpClient client = HTTPUtils.getClient();
		HttpGet request = ImageRepositoryUtils.getRequest("/v2/" + name + "/tags/list");
		HttpResponse response = client.execute(request);
		InputStream content = response.getEntity().getContent();
		String res = StringUtils.getFromSteam(content);
		JSONObject obj = JSONObject.parseObject(res);
		Object object = obj.get("tags");
		if(object == null) {
			return R.success().setData(new JSONArray());
		}else {
			JSONArray tags = obj.getJSONArray("tags");
			return R.success().setData(tags);
		}
	}
	
	
	@RequestMapping(path = "deleteImage")
	@OperateAuth(Authority.DELETE)
	public Object deleteImage() throws IOException{
		Map<String, Object> paramters = super.getParamters();
		String name = (String) paramters.get("name");
		String tag = (String) paramters.get("tag");
		HttpClient client = HTTPUtils.getClient();
		HttpGet request = ImageRepositoryUtils.getRequest("/v2/" + name + "/manifests/" + tag);
		request.addHeader("Accept", "application/vnd.docker.distribution.manifest.v2+json");
		
		HttpResponse response = client.execute(request);
		Header header = response.getFirstHeader("Docker-Content-Digest");
		String digest = header.getValue().trim();
		
		HttpDelete delRequest = ImageRepositoryUtils.getDelRequest("/v2/" + name + "/manifests/" + digest);
		
		response = client.execute(delRequest);
		if(response.getStatusLine().getStatusCode() == 202) {
			return R.success();
		}else {
			return R.failure();
		}
	}
}
