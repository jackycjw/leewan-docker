package com.leewan.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leewan.aop.Authority;
import com.leewan.aop.OperateAuth;
import com.leewan.bean.User;
import com.leewan.bean.Worker;
import com.leewan.service.service.ServiceService;
import com.leewan.util.BaseController;
import com.leewan.util.ChenjwMap;
import com.leewan.util.HTTPUtils;
import com.leewan.util.R;
import com.leewan.worker.RichWorkerContainer;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("service")
public class ServiceController extends BaseController {

	@Autowired
	private ServiceService service;
	
	@RequestMapping("save")
	@OperateAuth(Authority.NEW)
	public Object save() {
		User user = this.getUser();
		JSONObject data = super.getSteamParamters();
		this.service.save(data, user);
		return R.success();
	}
	
	@RequestMapping("update")
	@OperateAuth(Authority.UPDATE)
	public Object update() {
		User user = this.getUser();
		JSONObject data = super.getSteamParamters();
		this.service.update(data, user);
		return R.success();
	}
	
	@RequestMapping("query")
	public Object query() {
		User user = this.getUser();
		JSONObject data = super.getSteamParamters();
		List rs = this.service.query(data);
		return R.ok().setData(rs);
	}
	
	@RequestMapping("queryContainers")
	public Object queryContainers() {
		Map<String, Object> data = super.getSteamParamters();
		List<Map<String, Object>> rs = this.service.queryContainers(data);
		return R.ok().setData(rs);
	}
	
	@RequestMapping("queryContainerStatus")
	public Object queryContainerStatus() throws IOException {
		Map<String, Object> data = super.getSteamParamters();
		List<Map<String, Object>> rs = this.service.queryContainers(data);
		
		Worker worker = RichWorkerContainer.getInstance().getWorker((String)rs.get(0).get("WORKER_ID"));
		StringBuilder sb = new StringBuilder();
		sb.append(worker.getDockerUrl()).append("/containers/json?all=true")
			.append("&filters=").append(filters(rs));
		String res = HTTPUtils.get(sb.toString());
		return R.ok().setData(JSONArray.parseArray(res).get(0));
	}
	
	private String filters(List<Map<String, Object>> rs) throws UnsupportedEncodingException {
		JSONObject obj = new JSONObject();
		JSONArray ids = new JSONArray();
		
		for(Map<String, Object> c : rs) {
			String containerId = (String) c.get("CONTAINER_ID");
			ids.add(containerId);
		}
		obj.put("id", ids);
		return URLEncoder.encode(obj.toJSONString(), "utf-8");
	}
	
	
	
	
	
	@RequestMapping("startService")
	public Object startService() {
		User user = this.getUser();
		Map<String, Object> data = super.getSteamParamters();
		String serviceId = (String) data.get("serviceId");
		return this.service.startService(serviceId, user);
	}
	
	
	@RequestMapping("reStartService")
	public Object reStartService() {
		User user = this.getUser();
		Map<String, Object> data = super.getSteamParamters();
		String serviceId = (String) data.get("serviceId");
		this.service.reStartService(serviceId, user);
		return R.ok();
	}
}
