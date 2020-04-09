package com.leewan.service.register;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.leewan.config.ConfigManager;
import com.leewan.util.NetUtils;
import com.leewan.util.StringUtils;
import com.leewan.util.ZKUtils;
import com.leewan.worker.RichWorkerContainer;
import com.leewan.worker.WorkerContainer;



@Component
public class CenterService implements ApplicationRunner {

	@Value("${server.port}")
	private int port;
	
	
	private String mamagerCenter  = ConfigManager.getString("zk.mamager.center.path");
	
	private String mamagerWorker  = ConfigManager.getString("zk.mamager.worker.path");
	
	private String node = ConfigManager.getString("zk.mamager.center.node");
	
	ZooKeeper zooKeeper;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		zooKeeper = ZKUtils.getZKClient();
		this.registerManager();
		this.getWorkers();
	}
	
	private RichWorkerContainer workers;
	
	public void getWorkers() {
		try {
			List<String> children = zooKeeper.getChildren(mamagerWorker, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					if(EventType.NodeChildrenChanged == event.getType()) {
						getWorkers();
					}
				}
			});
			if(workers == null) {
				workers = RichWorkerContainer.getInstance();
			}
			workers.refresh(children);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	private void registerManager() throws Exception {
		String[] paths = mamagerCenter.split("/",-1);
		
		StringBuffer path = new StringBuffer();
		for(String item : paths) {
			if(StringUtils.hasLength(item)) {
				path.append("/").append(item);
				Stat stat = zooKeeper.exists(path.toString(), false);
				if(stat == null) {
					zooKeeper.create(path.toString(), null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				}
			}
		}
		zooKeeper.addAuthInfo("digest", ConfigManager.getBytes("zk.mamager.center.auth"));
		path.append("/").append(node);
		
		Stat exists = zooKeeper.exists(path.toString(), null);
		if(exists != null) {
			zooKeeper.delete(path.toString(), exists.getAversion());
		}
		zooKeeper.create(path.toString(), StringUtils.getBytes(getRegisterContent()), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		
		
		paths = mamagerWorker.split("/",-1);
		
		path = new StringBuffer();
		for(String item : paths) {
			if(StringUtils.hasLength(item)) {
				path.append("/").append(item);
				Stat stat = zooKeeper.exists(path.toString(), false);
				if(stat == null) {
					zooKeeper.create(path.toString(), null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				}
			}
		}
	}
	private String getRegisterContent() {
		JSONObject json = new JSONObject();
		json.put("host", NetUtils.getIP());
		json.put("port", port);
		json.put("docker.registry.url", ConfigManager.getString("docker.registry.url"));
		json.put("docker.registry.password", ConfigManager.getString("docker.registry.password"));
		json.put("docker.registry.user", ConfigManager.getString("docker.registry.user"));
		return json.toJSONString();
	}



}
