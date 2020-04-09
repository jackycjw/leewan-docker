package com.leewan.service.register;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.leewan.config.ConfigManager;
import com.leewan.util.Constants;
import com.leewan.util.NetUtils;
import com.leewan.util.StringUtils;
import com.leewan.util.ZKUtils;


@Component
public class CenterService implements ApplicationRunner {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	public static boolean MANAGER_CENTER_UP = false;
	
	@Value("${server.port}")
	public int port;
	
	ZooKeeper zk = ZKUtils.getZKClient();
	
	int sessionTimeout = ConfigManager.getInt("zk.sessionTimeout");
	
	
	private String mamagerCenter = ConfigManager.getString("zk.mamager.center.path");
	
	private String mamagerWorker  = ConfigManager.getString("zk.mamager.worker.path");
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		this.getCenterInfo();
		this.registerWorker();
	}
	
	private void registerWorker() {
		String path = this.mamagerWorker + "/" + ConfigManager.getString("zk.mamager.worker.id");
		try {
			Stat stat = zk.exists(path, null);
			if(stat != null) {
				zk.delete(path, stat.getVersion());
			}
			zk.create(path, getRegisterContent(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private byte[] getRegisterContent() {
		String type = ConfigManager.getString("worker.type",Constants.WORKER_TYPE);
		JSONObject content = new JSONObject();
		content.put("host", NetUtils.getIP());
		content.put("webPort", port);
		content.put("dockerApiPort", getDockerApiPort());
		content.put("type", type);
		return content.toJSONString().getBytes();
	}
	
	private int getDockerApiPort() {
		try {
			File file = new File("/usr/lib/systemd/system/docker.service");
			Properties prop = new Properties();
			prop.load(new FileInputStream(file));
			String ExecStart = prop.getProperty("ExecStart");
			
			Pattern pattern = Pattern.compile("tcp[:][/][/]\\S+[:](\\d+)\\s*");
			
			
			Matcher matcher = pattern.matcher(ExecStart);
			
			if(matcher.find()) {
				String port = matcher.group(1);
				return Integer.parseInt(port);
			}
		} catch (Exception e) {
			
		}
		throw new RuntimeException("未开启Docker API");
	}
	
	public static void main(String[] args) {
		Pattern pattern = Pattern.compile("tcp[:][/][/]\\S+[:](\\d+)\\s*");
		
		String s = "/usr/bin/dockerd -H=tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock";
		
		Matcher matcher = pattern.matcher(s);
		if(matcher.find()) {
			System.out.println(matcher.group());
			String group = matcher.group(1);
			System.out.println(group);
			
		}
	}
	
	public void getCenterInfo() {
		try {
			byte[] data = zk.getData(mamagerCenter + "/" + ConfigManager.getString("zk.mamager.center.node"), new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					CenterService.this.getCenterInfo();
				}
			}, null);
			formatData(data);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	
	private static String managerUrl;
	
	public static String registryUrl;
	public static String registryUser;
	public static String registryPwd;
	
	
	public static String getManagerUrl() {
		return "http://" + managerUrl;
	}
	
	private void formatData(byte[] data) {
		if(data == null || data.length == 0) {
			MANAGER_CENTER_UP = false;
			return;
		} else {
			MANAGER_CENTER_UP = true;
		}
		String string = StringUtils.getString(data);
		JSONObject obj = JSONObject.parseObject(string);
		managerUrl = obj.getString("host") + ":" + obj.getIntValue("port");
		
		registryUrl = obj.getString("docker.registry.url");
		registryUser = obj.getString("docker.registry.user");
		registryPwd = obj.getString("docker.registry.password");
	}

	
}
