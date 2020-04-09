package com.leewan.service.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leewan.bean.User;
import com.leewan.bean.Worker;
import com.leewan.config.ConfigManager;
import com.leewan.dao.service.ServiceDao;
import com.leewan.service.service.thread.CreateContainerThread;
import com.leewan.service.service.thread.PullImageThread;
import com.leewan.service.service.thread.RemoveContainerThread;
import com.leewan.service.service.thread.StartContainerThread;
import com.leewan.service.service.thread.StopContainerThread;
import com.leewan.util.DateUtils;
import com.leewan.util.HTTPUtils;
import com.leewan.util.R;
import com.leewan.util.UID;
import com.leewan.worker.Choose;
import com.leewan.worker.RichWorkerContainer;
import com.alibaba.fastjson.JSONObject;

@Service
public class ServiceServiceImpl implements ServiceService {

	@Autowired
	ServiceDao serviceDao;
	
	@Override
	public void save(Map<String, Object> data, User user) {
		String id = UID.getUUID();
		data.put("id", id);
		System.out.println(data.get("container").getClass());
		String container = data.get("container").toString();
		data.put("container", container);
		System.out.println(data.get("container").getClass());
		data.put("time", DateUtils.getNowTimeStamp());
		data.put("userId", user.getId());
		this.serviceDao.save(data);
	}

	@Override
	public void update(Map<String, Object> data, User user) {
		data.put("container", data.get("container").toString());
		data.put("time", DateUtils.getNow("getNowTimeStamp"));
		this.serviceDao.update(data);
	}

	@Override
	public List<Map<String, Object>> query(Map<String, Object> data) {
		return this.serviceDao.query(data);
	}

	@Override
	public List<Map<String, Object>> queryContainers(Map<String, Object> data) {
		return this.serviceDao.queryContainers(data);
	}

	@Override
	public Map<String, Object> getService(String serviceId) {
		return this.serviceDao.getService(serviceId);
	}

	public Set<Worker> removeRepeat(List<Worker> workers){
		Set<Worker> set = new HashSet<>();
		for(Worker worker : workers) {
			set.add(worker);
		}
		return set;
	}
	
	
	@Override
	public Map<String, Object> startService(String serviceId, User user) {
		RichWorkerContainer workerContainer = RichWorkerContainer.getInstance();
		Map<String, Object> service = this.serviceDao.getService(serviceId);
		List<Worker> workers = workerContainer.choose(service);
		if(workers == null || workers.size() == 0) {
			return R.failure("机器资源不足，无法选出合适的主机运行应用！");
		}
		
		List<Map<String, Object>> result = Collections.synchronizedList(new ArrayList<Map<String, Object>>());
		
		
		Set<Worker> removeRepeat = removeRepeat(workers);
		CountDownLatch latch = new CountDownLatch(removeRepeat.size());
		
		//拉取镜像
		for(Worker worker : removeRepeat) {
			new PullImageThread(latch, worker, service).start();
		}
		try {
			latch.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//创建容器
		latch = new CountDownLatch(workers.size());
		for(Worker worker : workers) {
			new CreateContainerThread(latch, worker, service, result).start();
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.serviceDao.saveContainer(result);
		
		//启动容器
		latch = new CountDownLatch(result.size());
		for(Map<String, Object> container : result) {
			new StartContainerThread(latch, container).start();
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		service.put("time", DateUtils.getNowTimeStamp());
		service.put("update_user", user.getId());
		service.put("status", "2");
		this.serviceDao.update(service);
		return service;
	}

	@Override
	public void reStartService(String serviceId, User user) {
		String now = DateUtils.getNowTimeStamp();
		startService(serviceId, user);
		
		Map<String, Object> param = new HashMap<>();
		param.put("maxTime", now);
		param.put("serviceId", serviceId);
		List<Map<String, Object>> containers = this.serviceDao.queryContainers(param);
		
		CountDownLatch latch = new CountDownLatch(containers.size());
		
		for(Map<String, Object> container: containers) {
			new StopContainerThread(latch, container).start();;
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		latch = new CountDownLatch(containers.size());
		
		for(Map<String, Object> container: containers) {
			new RemoveContainerThread(latch, container).start();;
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.serviceDao.delContainers(param);
	}
}

