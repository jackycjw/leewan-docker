package com.leewan;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import com.leewan.config.ConfigManager;
import com.leewan.controller.ImageRepositoryController;
import com.leewan.util.DateUtils;
import com.leewan.util.ZKUtils;

@SpringBootApplication
@EnableWebSocket
@MapperScan("com.leewan.dao")
public class ManagerApplicationStarter {

	public static void main(String[] args) throws Exception {
		initConfig(args);
		SpringApplication.run(ManagerApplicationStarter.class, args);
	}
	
	public static void initConfig(String[] args) {
		String path = "E:\\container\\config.properties";
		if(args != null && args.length > 0) {
			path = args[0];
		}
		ConfigManager.init(path);
	}
}
