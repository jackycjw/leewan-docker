package com.leewan.bean;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import com.leewan.util.DateUtils;
import com.leewan.util.R;

import com.alibaba.fastjson.JSONObject;

public class Worker {
	
	private String id;

	private String host;
	
	private int port;
	
	private int dockerApiPort;
	
	private String type;
	
	private long date = System.currentTimeMillis();
	
	private long activeTime = System.currentTimeMillis();
	
	LinkedList<Memeroy> memeroy = new LinkedList<Memeroy>();
	LinkedList<CPU> cpu = new LinkedList<CPU>();
	
	private ReentrantLock lock = new ReentrantLock();
	
	public double getRate() {
		return activeTime;
	}
	
	public void addStatus(JSONObject status) {
		lock.lock();
		try {
			activeTime = System.currentTimeMillis();
			
			JSONObject cpu = status.getJSONObject("cpu");
			double rate = cpu.getDouble("rate");
			int cores = cpu.getIntValue("cores");
			
			this.cpu.addLast(new CPU(rate, cores));
			if(this.cpu.size() > 1) {
				this.cpu.removeFirst();
			}
			
			JSONObject mem = status.getJSONObject("memeroy");
			long total = mem.getLong("total");
			long free = mem.getLong("free");
			
			this.memeroy.add(new Memeroy(total, free));
			if(this.memeroy.size() > 1) {
				this.memeroy.removeFirst();
			}
		} finally {
			lock.unlock();
		}
	}
	
	public JSONObject getCpu() {
		lock.lock();
		try {
			JSONObject cpu = new JSONObject();
			if(this.cpu.size() > 0) {
				CPU last = this.cpu.getLast();
				cpu.put("rate", last.getPercent());
				cpu.put("cores", last.getCores());
			}else {
				cpu.put("rate", -1);
				cpu.put("cores", -1);
			}
			return cpu;
		} finally {
			lock.unlock();
		}
	}
	
	public JSONObject getMemeroy() {
		lock.lock();
		try {
			Iterator<Memeroy> iterator = this.memeroy.iterator();
			int num = 0;
			long total = 0;
			long free = 0;
			while(iterator.hasNext()) {
				Memeroy next = iterator.next();
				num ++;
				total += next.getTotal();
				free += next.getFree();
			}
			
			JSONObject rs = new JSONObject();
			rs.put("total", num == 0 ? -1 : total / num);
			rs.put("free", num == 0 ? -1 : free / num);
			rs.put("percent", num == 0 ? -1 : (double)(total - free) / total);
			return rs;
		} finally {
			lock.unlock();
		}
	}
	
	
	@Override
	public String toString() {
		R rs = R.ok().put("id", id)
			.put("host", host)
			.put("type", type)
			.put("port", port)
			.put("cpu", getCpu())
			.put("date", DateUtils.getDateString(date, "yyyy-MM-dd HH:mm:ss"))
			.put("lastActiveTime", (System.currentTimeMillis() - activeTime)/1000);
		JSONObject mem = getMemeroy();
		long total = mem.getLong("total");
		long free = mem.getLong("free");
		double percent = mem.getDouble("percent");
		rs.put("mem-total", total);
		rs.put("mem-free", free);
		rs.put("mem-percent", percent);
		return rs.toString();
	}
	
	public R toJSONString() {
		R rs = R.ok().put("id", id)
			.put("host", host)
			.put("port", port)
			.put("cpu", getCpu())
			.put("date", DateUtils.getDateString(date, "yyyy-MM-dd HH:mm:ss"))
			.put("lastActiveTime", (System.currentTimeMillis() - activeTime)/1000);
		JSONObject mem = getMemeroy();
		long total = mem.getLong("total");
		long free = mem.getLong("free");
		double percent = mem.getDouble("percent");
		rs.put("mem-total", total);
		rs.put("mem-free", free);
		rs.put("mem-percent", percent);
		return rs;
	}
	
	public Worker(String nodeName, String content, long date) {
		this.id = nodeName;
		update(content, date);
	}
	
	public void update(String content, long create) {
		String[] split = content.split(":");
		JSONObject json = JSONObject.parseObject(content);
		this.host = json.getString("host");
		this.port = json.getIntValue("webPort");
		this.dockerApiPort = json.getIntValue("dockerApiPort");
		this.date = create;
		this.type = json.getString("type");
	}
	
	
	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getWorkerUrl() {
		return "http://" + host + ":" + port;
	}
	
	public String getDockerUrl() {
		return "http://" + host + ":" + dockerApiPort;
	}
	

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	

	public int getDockerApiPort() {
		return dockerApiPort;
	}


	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + port;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Worker other = (Worker) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (port != other.port)
			return false;
		return true;
	}
	
}
