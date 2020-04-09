package com.leewan.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leewan.util.BaseController;
import com.leewan.util.R;
import com.leewan.worker.RichWorkerContainer;
import com.leewan.ws.MachineWebSocket;
import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("machine")
public class MachineController extends BaseController {

	@RequestMapping("getMachineList")
	public R getMachineList() {
		RichWorkerContainer container = RichWorkerContainer.getInstance();
		return R.success().put("data", container.formatAllWorkers());
	}
	
	@RequestMapping("getBalanceList")
	public R getBalanceList() {
		RichWorkerContainer container = RichWorkerContainer.getInstance();
		return R.success().put("data", container.formaBalancetWorkers());
	}
	
	
	@RequestMapping("chooseMachine")
	public R chooseMachine() {
		RichWorkerContainer container = RichWorkerContainer.getInstance();
		return R.success().put("data", JSONObject.parseObject(container.chooseWorker().toString()));
	}
	
	@Autowired
	MachineWebSocket webSocket;
	
	@RequestMapping("upLoadMachineStatus")
	public R upLoadMachineStatus() throws IOException {
		JSONObject status = getSteamParamters();
		RichWorkerContainer instance = RichWorkerContainer.getInstance();
		instance.addStatus(status);
		this.webSocket.sendMessage(instance.formatAllWorkers().toJSONString());
		return R.success();
	}
	
	
}
