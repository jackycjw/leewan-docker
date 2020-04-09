package com.leewan.util;

import java.util.concurrent.locks.ReentrantLock;

public class GloblConstant {

	public static ReentrantLock lock = new ReentrantLock();
	
	public static boolean inited = false;
}
