package com.leewan.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;




public class IOUtils {

	public static void close(Closeable closeable) {
		try {
			if(closeable != null) {
				closeable.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public static void mai1n(String[] args) {
		byte[] bytes = BitConverter.getBytes(114381113.987);
		
		for(byte b : bytes) {
			System.out.print(b);
			System.out.print(",");
		}
	}
	
	
	public static void close(AutoCloseable closeable) {
		try {
			if(closeable != null) {
				closeable.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void copy(File from, File dest) throws Exception {
		InputStream in = new FileInputStream(from);
		byte[] bs = new byte[in.available()];
		in.read(bs);
		
		OutputStream out = new FileOutputStream(dest, true);
		out.write(bs);
		out.close();
		in.close();
	}
}
