package com.leewan.controller.image;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leewan.service.register.CenterService;
import com.leewan.util.BaseController;
import com.leewan.util.R;

@RestController
@RequestMapping("image")
public class ImageController extends BaseController {

	@RequestMapping("pull")
	public Object pull() {
		Map<String, Object> paramters = super.getSteamParamters();
		String name = (String) paramters.get("name");
		this.pullImage(name);
		return R.ok() ;
	}
	
	
	public static void pullImage(String name) {
		String[] cmd = new String[] {"sh","-c","docker pull "+CenterService.registryUrl+"/"+name};
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
