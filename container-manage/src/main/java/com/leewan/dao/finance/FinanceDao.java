package com.leewan.dao.finance;

import java.util.List;
import java.util.Map;

import com.leewan.util.interceptor.PageInfo;


public interface FinanceDao {
	
	public List<Map<String, Object>> queryFinanceList(Map map);

	public List<Map<String, Object>> queryFinanceList(Map map, PageInfo page);
	
	public Map<String, Object> getFinance(int financeId);
	
	public void saveFinance(Map map);
	
	public void updateFinance(Map map);
}
