package com.leewan.dao.doc;

import java.util.List;
import java.util.Map;

public interface DocumentDao {

	/**
	 * 新增文档节点
	 * @param entity
	 */
	public void addDocTreeItem(Map<String, Object> entity);
	
	/**
	 * 新增文档开放数据
	 * @param entity
	 */
	public void addDocTreeItemOpen(Map<String, Object> entity);
	
	public List<Map<String, Object>> queryDocList(Map<String, Object> parameter);
}
