package com.leewan.dao.user;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.leewan.bean.Role;
import com.leewan.bean.User;

public interface UserDao {

	public User getUser(Map map);
	
	public Role getRole(User user);
	
	public Set<String> getAuthorities(Role role);
	
	public List<Role> queryOuterRoleList();
}
