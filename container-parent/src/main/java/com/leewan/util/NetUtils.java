package com.leewan.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;



public class NetUtils {

	public static String getIP() {
		String hostAddress = "localhost";
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hostAddress;
	}
	
	
	private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException(
                    "Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }
	
	
	
	public static void main(String[] args) {
		long t = System.currentTimeMillis();
		System.out.println(portUsed("docker01", 22));
		System.out.println("耗时："+(System.currentTimeMillis() - t));
	}
	
	public static boolean portUsed(String host, int port) {
		
		CountDownLatch latch = new CountDownLatch(1);
		PortTestThread thread = new PortTestThread(host, port, latch);
		try {
			thread.start();
			latch.await(1000, TimeUnit.MILLISECONDS);
			if(thread.isAlive()) {
				thread.interrupt();
			}
		} catch (Exception e) {
		}
		return thread.isUsed();
	}
}


class PortTestThread extends Thread{
	
	private String host;
	
	private int port;
	
	private CountDownLatch latch;
	
	
	public PortTestThread(String host, int port, CountDownLatch latch) {
		super();
		this.host = host;
		this.port = port;
		this.latch = latch;
	}

	private boolean used = false;
	
	public boolean isUsed() {
		return this.used;
	}
	
	@Override
	public void run() {
		try {
			Socket socket = new Socket(host, port);
			socket.close();
			this.used = true;
		} catch (Exception e) {
		} finally {
			latch.countDown();
		}
	}
}