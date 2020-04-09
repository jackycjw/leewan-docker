package com.leewan.bean;

public class Memeroy {

	private long total;
	
	private long free;
	
	

	public Memeroy(long total, long free) {
		super();
		this.total = total;
		this.free = free;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getFree() {
		return free;
	}

	public void setFree(long free) {
		this.free = free;
	}
	
	
}
