package com.leewan.worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.leewan.bean.Worker;
import com.leewan.config.ConfigManager;
import com.leewan.util.Constants;
import com.leewan.util.StringUtils;
import com.leewan.util.ZKUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class WorkerContainer {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	private List<Worker> allWorkers = new ArrayList<Worker>();
	
	private List<Worker> workers = new ArrayList<Worker>();
	
	private List<Worker> loadBalances = new ArrayList<Worker>();
	
	private Map<String, Worker> nodeMap = new HashMap<>();
	
	private static WorkerContainer instance = new WorkerContainer();
	
	public static WorkerContainer getInstance() {
		return instance;
	}
	
	public List<Worker> getWorkers(){
		return this.workers;
	}
	
	public List<Worker> getAllWorkers(){
		return this.workers;
	}
	
	public List<Worker> getLoadBalances(){
		return this.loadBalances;
	}
	
	public Worker getWorker(String id){
		return this.nodeMap.get(id);
	}
	
	
	public JSONArray formatAllWorkers() {
		StringBuffer sb = new StringBuffer("[");
		
		for(int i=0;i<this.allWorkers.size();i++) {
			sb.append(this.allWorkers.get(i).toString());
			if(i != this.allWorkers.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return JSONArray.parseArray(sb.toString());
	}
	
	public JSONArray formatWorkers() {
		StringBuffer sb = new StringBuffer("[");
		
		for(int i=0;i<this.workers.size();i++) {
			sb.append(this.workers.get(i).toString());
			if(i != this.workers.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return JSONArray.parseArray(sb.toString());
	}
	
	public JSONArray formaBalancetWorkers() {
		StringBuffer sb = new StringBuffer("[");
		
		for(int i=0;i<this.loadBalances.size();i++) {
			sb.append(this.loadBalances.get(i).toString());
			if(i != this.loadBalances.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return JSONArray.parseArray(sb.toString());
	}
	
	public void addStatus(JSONObject status) {
		String id = status.getString("id");
		if(this.nodeMap.containsKey(id)) {
			this.nodeMap.get(id).addStatus(status);
		}
	}
	
	protected WorkerContainer() {}
	
	
	public Worker chooseWorker() {
		
		return null;
	}
	
	
	
	public void refresh(List<String> children) {
		List<Worker> workers = new ArrayList<Worker>();
		List<Worker> allWorkers = new ArrayList<Worker>();
		List<Worker> loadBalances = new ArrayList<Worker>();
		Map<String, Worker> nodeMap = new HashMap<>();
		
		if(children != null && !children.isEmpty()) {
			for(String child : children) {
				byte[] data = null;
				Stat stat = new Stat();
				try {
					data = ZKUtils.getZKClient()
							.getData(ConfigManager.getString("zk.mamager.worker.path") + "/" + child
									, null, stat);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String content = StringUtils.getString(data);
				if(this.nodeMap.containsKey(child)) {
					this.nodeMap.get(child).update(content, stat.getCtime());
					workers.add(this.nodeMap.get(child));
					nodeMap.put(child, this.nodeMap.get(child));
					continue;
				}
				try {
					Worker worker = new Worker(child, content, stat.getCtime());
					if(Constants.WORKER_TYPE.equals(worker.getType())) {
						workers.add(worker);
					}
					
					if(Constants.LOADBALANCE_TYPE.equals(worker.getType())) {
						loadBalances.add(worker);
					}
					allWorkers.add(worker);
					nodeMap.put(child, worker);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		this.allWorkers = allWorkers;
		this.workers = workers;
		this.loadBalances = loadBalances;
		this.nodeMap = nodeMap;
	}
	
	
	
}
