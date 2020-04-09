package com.leewan.dao.service;

import java.util.List;
import java.util.Map;

public interface ServiceDao {

	void save(Map<String, Object> data);
	
	void update(Map<String, Object> data);
	
	List<Map<String, Object>> query(Map<String, Object> data);
	
	List<Map<String, Object>> queryContainers(Map<String, Object> data);
	
	Map<String, Object> getService(String serviceId);
	
	void saveContainer(List<Map<String, Object>> containers);
	
	void delContainers(Map<String, Object> data);
	
}
