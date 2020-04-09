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
import com.leewan.worker.RichWorkerContainer;

public class StopContainerThread extends Thread{
	
	CountDownLatch latch;
	
	
	private Map<String, Object> container;
	
	

	public StopContainerThread(CountDownLatch latch, Map<String, Object> container) {
		super();
		this.latch = latch;
		this.container = container;
	}


	@Override
	public void run() {
		String containerId = (String) this.container.get("CONTAINER_ID");
		String workerId = (String) this.container.get("WORKER_ID");
		
		try {
			Worker worker = RichWorkerContainer.getInstance().getWorker(workerId);
			HTTPUtils.post(worker.getDockerUrl()+"/containers/"+containerId+"/stop", null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.latch.countDown();
		}
		
	}
}
