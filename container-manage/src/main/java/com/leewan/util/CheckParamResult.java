package com.leewan.util;

public class CheckParamResult {

	public boolean pass = true;
	
	public Object message;

	public CheckParamResult(boolean pass, Object message) {
		super();
		this.pass = pass;
		this.message = message;
	}

	public CheckParamResult() {
	}
	
	
}
