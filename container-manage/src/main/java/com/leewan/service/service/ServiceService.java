package com.leewan.service.service;

import java.util.List;
import java.util.Map;

import com.leewan.bean.User;

public interface ServiceService {

	void save(Map<String, Object> data, User user);
	
	void update(Map<String, Object> data, User user);
	
	List<Map<String, Object>> query(Map<String, Object> data);
	
	List<Map<String, Object>> queryContainers(Map<String, Object> data);
	
	
	Map<String, Object> getService(String serviceId);
	
	Map<String, Object> startService(String serviceId, User user);
	
	void reStartService(String serviceId, User user);
	
}
