package com.leewan.util;

import com.alibaba.fastjson.JSONObject;

public class ResponseUtils {

	public static boolean isSuccess(JSONObject json) {
		if(json == null) {
			return false;
		}
		Object code = json.get("code");
		return R.SUCCESS_CODE.equals(code);
	}
}
