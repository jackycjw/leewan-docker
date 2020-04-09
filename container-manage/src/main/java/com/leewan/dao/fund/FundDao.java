package com.leewan.dao.fund;

import java.util.List;
import java.util.Map;


public interface FundDao {

	public List<Map<String, Object>> queryFundList(Map map);
	
	public Map<String, Object> getFund(int fundId);
	
	public void saveFund(Map map);
	
	public void deleteFund(int fundId);
	
	public void updateFund(Map map);
	
}
