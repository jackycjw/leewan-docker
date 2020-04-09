package com.leewan.service.doc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.leewan.bean.Role;
import com.leewan.dao.doc.DocumentDao;
import com.leewan.util.DateUtils;
import com.leewan.util.R;
import com.leewan.util.UID;
import com.leewan.util.UserUtils;

@Component
public class DocumentServiceImpl implements DocumentService {

	@Autowired
	DocumentDao documentDao;
	
	@Override
	@Transactional
	public void addDocTreeItem(Map<String, Object> param) {
		//插入文档节点
		String docTreeId = UID.getUUID();
		String now = DateUtils.getNow();
		int userId = UserUtils.getUser().getId();
		param.put("id", docTreeId);
		param.put("createTime", now);
		param.put("userId",userId);
		this.documentDao.addDocTreeItem(param);
		
		int auth = (Integer) param.get("auth");
		if(auth > 3) {
			param.put("docTreeId",docTreeId);
			Object rolesObj = param.get("roles");
			if(rolesObj instanceof String[]) {
				String[] roles = (String[]) rolesObj;
				for(String role : roles) {
					String id = UID.getUUID();
					param.put("id",id);
					param.put("roleId", role);
					this.documentDao.addDocTreeItemOpen(param);
				}
			}else {
				String id = UID.getUUID();
				param.put("id",id);
				param.put("roleId", rolesObj);
				this.documentDao.addDocTreeItemOpen(param);
			}
		}
	}

	@Override
	public List<Map<String, Object>> getDocTree() {
		Role role = UserUtils.getUser().getRole();
		
		List<Map<String, Object>> list = null;
		//内部
		if(role.getAuthLevel() < 4) {
			list = this.documentDao.queryDocList(R.ok().put("auth", role.getAuthLevel()));
		}else {
			list = this.documentDao.queryDocList(R.ok().put("role", role.getId()));
		}
		return formatTree(list);
	}
	
	public static String DOC_TYPE_API = "3";
	
	/**
	 * fun:对数据进行格式化
	 * @param list
	 * @return
	 */
	private List<Map<String, Object>> formatTree(List<Map<String, Object>> list){
//		list = getUseFullDocTreeItem(list);
		Map<String, Map<String, Object>> mapping = new HashMap<>();
		
		for(Map<String, Object> item : list) {
			String id = (String) item.get("id");
			mapping.put(id, item);
		}
		
		List<Map<String, Object>> result = new ArrayList<>();
		
		for(Map<String, Object> item : list) {
			String pid = (String) item.get("pid");
			System.out.println(!StringUtils.hasLength(pid));
			if(!StringUtils.hasLength(pid)) {
				result.add(item);
			}else {
				Map<String, Object> parent = mapping.get(pid);
				if(!parent.containsKey("children")) {
					parent.put("children", new ArrayList<>());
				}
				List<Map<String, Object>> children = (List<Map<String, Object>>) parent.get("children");
				children.add(item);
			}
		}
		return result;
	}
	
	/**
	 * fun：获取有用的节点
	 * @param list
	 * @return
	 */
	private List<Map<String, Object>> getUseFullDocTreeItem(List<Map<String, Object>> list){
		Map<String, Map<String, Object>> mapping = new HashMap<>();
		List<Map<String, Object>> apis = new ArrayList<>();
		
		for(Map<String, Object> item : list) {
			String id = (String) item.get("id");
			mapping.put(id, item);
			
			String type = (String) item.get("type");
			if(DOC_TYPE_API.equals(type)) {
				apis.add(item);
			}
		}
		
		List<Map<String, Object>> useFull = new ArrayList<>();
		
		for(Map<String, Object> item : apis) {
			do {
				String pid = (String) item.get("pid");
				useFull.add(item);
				item = mapping.get(pid);
			}while(item != null);
		}
		return useFull;
	}

}
