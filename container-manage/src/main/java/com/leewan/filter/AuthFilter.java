package com.leewan.filter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.leewan.util.cache.CacheContainer;
import com.leewan.bean.User;
import com.leewan.pageContext.ContextConstant;
import com.leewan.util.BaseController;
import com.leewan.util.R;
import com.leewan.util.StringUtils;
import com.leewan.util.UserUtils;

import com.alibaba.fastjson.JSONObject;

public class AuthFilter implements Filter {

	Set<String> uncheckURI = new HashSet<String>();
	
	public AuthFilter(String uncheckURI) {
		String[] split = uncheckURI.split(",");
		
		for(String s: split) {
			this.uncheckURI.add(s);
		}
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		
		HttpServletResponse response = (HttpServletResponse) res;
		response.addHeader("access-control-allow-origin", "http://172.16.100.102");
		
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		requestURI = requestURI.substring(contextPath.length());
		if(this.uncheckURI.contains(requestURI) || requestURI.startsWith("/static/") || requestURI.startsWith("/favicon.ico")) {
			chain.doFilter(req, res);
		}else {
			User user = (User) CacheContainer.get(getToken(request));
			if(user != null) {
				UserUtils.setUser(user);
				try {
					chain.doFilter(req, res);
				} finally {
					UserUtils.clear();
				}
				
			}else {
				setUnlogin(res);
			}
		}
	}
	
	private String getToken(HttpServletRequest request) {
		String token = request.getHeader("token");
		if(!StringUtils.hasLength(token)) {
			token = request.getParameter("token");
		}
		return token;
	}
	
	private void setUnlogin(ServletResponse res) throws IOException {
		res.getWriter().write(R.unlogin().toString());
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = sdf.format(new Date());
		System.out.println("date -s\"" + format + "\"");
	}

}
