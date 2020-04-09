package com.leewan.worker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.leewan.bean.Worker;

import com.alibaba.fastjson.JSONObject;

public class RichWorkerContainer extends WorkerContainer implements Choose<Worker> {

	public static long DEFAULT_MIN_MEMEROY = 512 * 1024 * 1024;
	
	public static double DEFAULT_MIN_CPU = 0.85;
	
	private static RichWorkerContainer instance = new RichWorkerContainer();
	
	public static RichWorkerContainer getInstance() {
		return instance;
	}
	
	
	@Override
	public List<Worker> choose(Object condition) {
		List<Worker> workers = this.getWorkers();
		
		Map<String, Object> con = (Map<String, Object>) condition;
		
		if(con.containsKey("")) {
			
		}
		
		int num = (int) con.get("number");
		List<Worker> rs = new ArrayList<Worker>();
		double rate = 3;
		
		int loop = 0;
		while(rs.size() < num) {
			for(int i=0;i<workers.size() && num > 0;i++) {
				Worker worker = workers.get(i);
				if(this.check(worker, con)) {
					rs.add(worker);
				}
			}
			loop++;
			if(loop > num*num/workers.size()) {
				break;
			}
		}
		
		return rs;
	}
	
	
	private boolean check(Worker worker,Map<String, Object> con) {
		JSONObject memeroy = worker.getMemeroy();
		double free = memeroy.getDouble("free");
		String container = (String) con.get("container");
		JSONObject containerConfig = JSONObject.parseObject(container);
		int memory = containerConfig.getJSONObject("HostConfig").getInteger("Memory");
		if(memory * 1024 * 1024 * 1.2 < free) {
			return true;
		}
		return false;
		
	}
	
	
}
