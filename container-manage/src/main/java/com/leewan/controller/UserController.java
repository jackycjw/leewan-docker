package com.leewan.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.leewan.bean.User;
import com.leewan.util.cache.CacheContainer;
import com.leewan.dao.company.CompanyDao;
import com.leewan.dao.dic.DicDao;
import com.leewan.dao.finance.FinanceDao;
import com.leewan.dao.user.UserDao;
import com.leewan.pageContext.ContextConstant;
import com.leewan.util.BaseController;
import com.leewan.util.EncryptUtils;
import com.leewan.util.R;
import com.leewan.util.UID;

@RestController
@RequestMapping("user")
public class UserController extends BaseController {

	@Autowired
	UserDao userDao;
	
	
	@RequestMapping("login")
	public String login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> paramters = getParamters();
		String password = (String) paramters.get("password");
		password = EncryptUtils.encrypt(password);
		paramters.put("password", password);
		
		
		User user = this.userDao.getUser(paramters);
		if(user != null) {
			user.setRole(this.userDao.getRole(user));
			user.setAuthority(this.userDao.getAuthorities(user.getRole()));
			String token = UID.getUUID();
			CacheContainer.save(token, user, 30 * 60 * 1000);
			return R.success().setData(token).toString();
		}else {
			return R.failure().setData("用户名或密码错误").toString();
		}
	}
	
	@RequestMapping("loadUser")
	public Object loadUser() throws IOException {
		return R.success().setData(getUser());
	}
	
	
	@RequestMapping("getOuterRoleList")
	public Object getOuterRoleList() throws IOException {
		return R.success().setData(this.userDao.queryOuterRoleList());
	}
	
}
