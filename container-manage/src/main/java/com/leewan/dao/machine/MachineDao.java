package com.leewan.dao.machine;

import java.util.List;
import java.util.Map;

import com.leewan.util.interceptor.PageInfo;

public interface MachineDao {

	public List<Map<String, Object>> selectMachineList(PageInfo page, Map<String, Object> param);
}
