package com.leewan.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.leewan.bean.Worker;
import com.leewan.util.BaseController;
import com.leewan.util.NetUtils;
import com.leewan.util.R;
import com.leewan.worker.RichWorkerContainer;

@RestController
@RequestMapping("loadBalance")
public class LoadBalanceController extends BaseController {

	@RequestMapping("checkPortUsed")
	public Object checkPortUsed() {
		JSONObject param = super.getSteamParamters();
		String workerId = param.getString("workerId");
		int port = param.getIntValue("port");
		Worker worker = RichWorkerContainer.getInstance().getWorker(workerId);
 		return R.ok().setData(NetUtils.portUsed(worker.getHost(), port));
	}
	
	@RequestMapping("saveBalance")
	public Object saveBalance() {
		JSONObject steamParamters = super.getSteamParamters();
		return request;
	}
}
