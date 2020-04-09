package com.leewan.util;


import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import com.leewan.config.ConfigManager;

public class ZKUtils {

	public static void close(ZooKeeper zk) {
		try {
			if(zk != null) {
				zk.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static ZooKeeper zkClient;
	
	public static ZooKeeper getZKClient() {
		initZookeeper();
		return zkClient;
	}
	
	
	private static void initZookeeper() {
		if(zkClient == null) {
			synchronized (ZKUtils.class) {
				if(zkClient == null) {
					CountDownLatch latch = new CountDownLatch(1);
					try {
						zkClient = new ZooKeeper(ConfigManager.getString("zk.hosts"), 
								60000, new Watcher() {
							public void process(WatchedEvent event) {
								if (KeeperState.SyncConnected == event.getState()) {
									System.out.println("zk连接成功");
									latch.countDown();
								}
							}
						});
						latch.await();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}
}
