package com.leewan.service.doc;

import java.util.List;
import java.util.Map;

public interface DocumentService {

	/**
	 * 新增树形文档节点
	 * @param param
	 */
	void addDocTreeItem(Map<String, Object> param);
	
	List<Map<String, Object>> getDocTree();
}
