package com.leewan.bean;

import java.util.List;
import java.util.Set;

public class User {

	private int id;
	
	private String code;
	
	private String name;
	
	Role role;
	
	private Set<String> authority;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Set<String> getAuthority() {
		return authority;
	}

	public void setAuthority(Set<String> authority) {
		this.authority = authority;
	}
	
	
}
