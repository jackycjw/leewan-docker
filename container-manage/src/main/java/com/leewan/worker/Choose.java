package com.leewan.worker;

import java.util.List;

public interface Choose<T> {

	/**
	 * choose an appropriate Object by condition
	 * @return
	 */
	public List<T> choose(Object condition);
	
}
