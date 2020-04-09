package com.leewan.util;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.sun.management.OperatingSystemMXBean;

import com.mysql.jdbc.Driver;

public class Test2 {

	public static void main(String[] args) throws Exception {
		System.out.println("--------------");
		for(int i=0;i<6 * 2;i++) {
			Thread.sleep(10000);
			System.out.println((i+1)*10+"秒");
		}
		System.out.println("---------------------------");
	}
}
