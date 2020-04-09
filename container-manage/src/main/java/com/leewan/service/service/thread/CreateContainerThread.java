package com.leewan.service.service.thread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.alibaba.fastjson.JSONObject;
import com.leewan.bean.Worker;
import com.leewan.config.ConfigManager;
import com.leewan.util.DateUtils;
import com.leewan.util.HTTPUtils;

public class CreateContainerThread extends Thread{
	
	CountDownLatch latch;
	
	private Worker worker;
	
	private Map<String, Object> service;
	
	private List<Map<String, Object>> result;
	

	public CreateContainerThread(CountDownLatch latch, Worker worker, Map<String, Object> service,
			List<Map<String, Object>> result) {
		super();
		this.latch = latch;
		this.worker = worker;
		this.service = service;
		this.result = result;
	}

	private String formatCreateParam(String container) {
		JSONObject objContainer = JSONObject.parseObject(container);
		String Image = objContainer.getString("Image");
		objContainer.put("Image", ConfigManager.getString("docker.registry.url")+"/"+Image);
		Long memory = objContainer.getJSONObject("HostConfig").getLong("Memory");
		objContainer.getJSONObject("HostConfig").put("Memory", memory * 1024 * 1024);
		return objContainer.toJSONString();
		
	}

	@Override
	public void run() {
		String container = (String) service.get("container");
		String serviceId = (String) service.get("id");
		try {
			String rs = HTTPUtils.post(this.worker.getDockerUrl()+"/containers/create", formatCreateParam(container));
			JSONObject obj = JSONObject.parseObject(rs);
			String containerId = obj.getString("Id");
			String workerId = this.worker.getId();
			
			Map<String, Object> containerObj = new HashMap<String, Object>();
			containerObj.put("containerId", containerId);
			containerObj.put("serviceId", serviceId);
			containerObj.put("workerId", workerId);
			containerObj.put("createTime", DateUtils.getNowTimeStamp());
			this.result.add(containerObj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.latch.countDown();
		}
		
	}
}
