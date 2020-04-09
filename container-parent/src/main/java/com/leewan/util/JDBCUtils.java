package com.leewan.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JDBCUtils {

	//	public static final String url = "jdbc:mysql://172.16.100.30:3306/elefirst?autoReconnect=true";
	
	public static final String url = "jdbc:oracle:thin:@58.222.192.26:2521/ORCL";
	
	public static final String user = "txprod";
	
	public static final String pwd = "w4hhj2eh2dzw@tk";
	
	public static Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(url, user, pwd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return connection;
	}
	
	public static void main(String[] args) throws Exception {
		Connection con = getConnection();
		PreparedStatement pst = con.prepareStatement("select * from eng_meter t where rownum <2");
		ResultSet rs = pst.executeQuery();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
		if(rs.next()) {
			Time time = rs.getTime(19);
			Timestamp timestamp = rs.getTimestamp(19);
			Date date = timestamp;
			System.out.println(sdf.format(date));
		}
	}
}