package com.leewan.dao.module;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

import com.leewan.bean.Role;

public interface ModuleDao {

	public List<Map<String, Object>> queryModules(Role role);
}
