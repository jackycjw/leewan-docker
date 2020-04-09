package com.leewan.pageContext;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.leewan.exception.ContextException;


@Component
@Configuration
public class PageContext implements InitializingBean {

	/**
	 * static resource path
	 */
	@Value("${page.rootPath}")
	private String rootPath;
	
	private Map<String, StaticResource> resource = new ConcurrentHashMap<String, StaticResource>();

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	
	private void findStaticResource(File path) {
		File[] listFiles = path.listFiles(new FileFilter() {
			public boolean accept(File file) {
				// TODO Auto-generated method stub
				if(file.isDirectory()) {
					return true;
				}else {
					return PageContext.this.isStaticResource(file);
				}
			}
		});
		
		if(listFiles != null && listFiles.length > 0) {
			for(File file : listFiles) {
				if(file.isDirectory()) {
					this.findStaticResource(file);
				}else {
					StaticResource rs = StaticResource.createResource(file);
					this.resource.put(rs.getResourceName(), rs);
				}
			}
		}
	}
	
	private boolean isStaticResource(File file) {
		String name = file.getName();
		if(name.endsWith(ContextConstant.HTML) || name.endsWith(ContextConstant.JS)
				|| name.endsWith(ContextConstant.CSS)) {
			return true;
		}
		return false;
	}
	
	private String getJSResource(String name) {
		String jsName = name + ContextConstant.JS;
		StaticResource jsRs = this.resource.get(jsName);
		if(null == jsRs) {
			return "";
		}
		StringBuffer rs = new StringBuffer();
		rs.append("<script type=\"text/javascript\">");
		rs.append(SystemConstant.CRLF);
		rs.append(jsRs.getContent());
		rs.append(SystemConstant.CRLF);
		rs.append("</script>");
		return rs.toString();
	}
	
	
	private String getCSSResource(String name) {
		String cssName = name + ContextConstant.CSS;
		StaticResource cssRs = this.resource.get(cssName);
		if(null == cssRs) {
			return "";
		}
		StringBuffer rs = new StringBuffer();
		rs.append("<style>");
		rs.append(SystemConstant.CRLF);
		rs.append(cssRs.getContent());
		rs.append(SystemConstant.CRLF);
		rs.append("</style>");
		return rs.toString();
	}
	
	//--对外开放
	
	//获取组件内容
	public String getContent(String name) {
		StringBuffer rs = new StringBuffer();
		String htmlName = name + ContextConstant.HTML;
		if(!this.resource.containsKey(htmlName)) {
			this.loadResource();
		}
		StaticResource htmlRs = this.resource.get(htmlName);
		if(htmlRs == null) {
			throw new ContextException("resource: "+htmlName + " not found");
		}
		rs.append(htmlRs.getContent());
		
		String jsRs = this.getJSResource(name);
		rs.append(jsRs);
		
		String cssRs = this.getCSSResource(name);
		rs.append(cssRs);
		return rs.toString();
	}

	private void loadResource() {
		try {
			File root = ResourceUtils.getFile(rootPath);
			this.findStaticResource(root);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		this.loadResource();
	}
}
