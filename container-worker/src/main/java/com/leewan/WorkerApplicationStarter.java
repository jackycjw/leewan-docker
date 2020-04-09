package com.leewan;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.leewan.config.ConfigManager;
import com.leewan.util.StringUtils;

@SpringBootApplication
public class WorkerApplicationStarter {

	public static void main(String[] args) {
		initConfig(args);
		SpringApplication.run(WorkerApplicationStarter.class, args);
	}
	
	private static void initConfig(String[] args) {
		String path = "/springcloud/config/config.properties";
		if(args != null && args.length > 0) {
			path = args[0];
		}
		ConfigManager.init(path);
	}
}
