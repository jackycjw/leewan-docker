package com.leewan.dao.company;

import java.util.List;
import java.util.Map;

import com.leewan.util.interceptor.PageInfo;


public interface CompanyDao {

	public List<Map<String, Object>> queryCompanyList(Map map);
	
	public List<Map<String, Object>> queryCompanyList(Map map, PageInfo page);
	
	public void saveCompany(Map map);
	
	public void deleteCompany(int companyId);
}
