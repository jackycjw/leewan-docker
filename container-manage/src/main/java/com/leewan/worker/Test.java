package com.leewan.worker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Test {

	static File file = new File("/app/nginx/tt");
	
	public static void main(String[] args) throws IOException {
		InputStream in = new FileInputStream(file);
		byte[] bs = new byte[in.available()];
		in.read(bs);
		in.close();
		OutputStream out = new FileOutputStream(file);
		out.write(bs);
		out.close();
		
		
	}
}
