package com.leewan.service.service.thread;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.alibaba.fastjson.JSONObject;
import com.leewan.bean.Worker;
import com.leewan.util.HTTPUtils;

public class PullImageThread  extends Thread{
	
	CountDownLatch latch;
	
	private Worker worker;
	
	private Map<String, Object> service;
	
	public PullImageThread(CountDownLatch latch, Worker worker, Map<String, Object> service) {
		super();
		this.latch = latch;
		this.worker = worker;
		this.service = service;
	}


	@Override
	public void run() {
		String container = (String) service.get("container");
		JSONObject obj = JSONObject.parseObject(container);
		String image = obj.getString("Image");
		JSONObject param = new JSONObject();
		param.put("name", image);
		String serviceId = (String) service.get("id");
		try {
			HTTPUtils.post(this.worker.getWorkerUrl()+"/image/pull", param.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.latch.countDown();
		}
		
	}
}