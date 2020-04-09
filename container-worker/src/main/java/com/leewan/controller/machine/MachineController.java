package com.leewan.controller.machine;

import java.io.IOException;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leewan.config.ConfigManager;
import com.leewan.service.register.CenterService;
import com.leewan.util.BaseController;
import com.leewan.util.HTTPUtils;
import com.leewan.util.R;
import com.leewan.util.SystemUtils;

import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("machine")
@EnableScheduling
public class MachineController extends BaseController {
	
	@Autowired
	CenterService centerService;

	@Scheduled(fixedRate= 10 * 1000)
	public void uploadStatus() {
		JSONObject status = SystemUtils.getMachineStatus();
		status.put("id", ConfigManager.getString("zk.mamager.worker.id"));
		try {
			HTTPUtils.post(centerService.getManagerUrl() + "/machine/upLoadMachineStatus", status.toString());
		} catch (Exception e) {
			centerService.getCenterInfo();
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping("certificate")
	public String certificate() {
		Map<String, Object> paramters = super.getParamters();
		String certificate = (String) paramters.get("certificate");
		return R.success().toString();
	}
	
	
}
