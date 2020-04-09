package com.leewan.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leewan.aop.Authority;
import com.leewan.aop.OperateAuth;
import com.leewan.dao.doc.DocumentDao;
import com.leewan.service.doc.DocumentService;
import com.leewan.util.BaseController;
import com.leewan.util.CheckParamResult;
import com.leewan.util.R;

@RestController
@RequestMapping("doc")
public class DocumentController extends BaseController {

	@Autowired
	DocumentService documentService;
	
	@RequestMapping("getDocTree")
	@OperateAuth
	public Object getDocTree() {
		return R.success().setData(this.documentService.getDocTree());
	}
	
	
	/**
	 * 新增树形节点
	 * @return
	 */
	@RequestMapping("addDocTreeItem")
	@OperateAuth(Authority.NEW)
	public Object addDocTreeItem() {
		Map<String, Object> paramters = super.getParamters();
		CheckParamResult check = checkAddDocTreeItem(paramters);
		if(!check.pass) {
			return R.illegalParam().setData(check.message);
		}
		this.documentService.addDocTreeItem(paramters);
		return R.success();
	}
	
	private CheckParamResult checkAddDocTreeItem(Map<String, Object> paramters) {
		String name = (String) paramters.get("name");
		if(!StringUtils.hasLength(name)) {
			return new CheckParamResult(false, "name不能为空");
		}
		String type = (String) paramters.get("type");
		if(!StringUtils.hasLength(type)) {
			return new CheckParamResult(false, "type不能为空");
		}
		if(!"1".equals(type)) {
			String pid = (String) paramters.get("pid");
			if(!StringUtils.hasLength(pid)) {
				return new CheckParamResult(false, "pid不能为空");
			}
		}
		
		String auth = (String) paramters.get("auth");
		if(!StringUtils.hasLength(auth)) {
			return new CheckParamResult(false, "auth不能为空");
		}
		try {
			paramters.put("auth", Integer.parseInt(auth));
		} catch (Exception e) {
			return new CheckParamResult(false, "auth不是数字");
		}
		return new CheckParamResult();
	}
	
}
