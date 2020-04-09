package com.leewan.util;

import java.util.HashMap;

public class ChenjwMap extends HashMap<String, Object> {

	public static ChenjwMap getInstance() {
		return new ChenjwMap();
	}
	
	public ChenjwMap put(String key, Object val) {
		super.put(key, val);
		return this;
	}
}
