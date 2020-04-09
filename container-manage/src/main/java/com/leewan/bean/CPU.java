package com.leewan.bean;

public class CPU {

	private double percent;
	
	private int cores;
	
	

	public CPU(double percent, int cores) {
		super();
		this.percent = percent;
		this.cores = cores;
	}

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	public int getCores() {
		return cores;
	}

	public void setCores(int cores) {
		this.cores = cores;
	}
	
	
	
	
}
