package com.leewan.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.leewan.bean.User;
import com.leewan.dao.module.ModuleDao;
import com.leewan.util.BaseController;
import com.leewan.util.R;

import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("module")
public class ModuleController extends BaseController {

	private String name = "1234";
	
	@Autowired
	ModuleDao moduleDao;
	
	@RequestMapping("queryModules")
	public Object queryModules(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = super.getUser();
		List<Map<String, Object>> list = this.moduleDao.queryModules(user.getRole());
		return R.success().setData(list);
	}
	
}
