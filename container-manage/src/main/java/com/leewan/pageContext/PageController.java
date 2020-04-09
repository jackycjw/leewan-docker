package com.leewan.pageContext;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
	
	@Autowired
	PageContext pageContext;
	
	@RequestMapping("getPage")
	public void pageLoad(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String pageName = request.getParameter("pageName");
		String content = this.pageContext.getContent(pageName);
		response.setCharacterEncoding("utf-8");
		response.getWriter().write(content);
	}
	
}
