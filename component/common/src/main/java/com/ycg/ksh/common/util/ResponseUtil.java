package com.ycg.ksh.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public abstract class ResponseUtil {

	public static final String toJsonObject(int code, String val) {
		return toJsonObject(code, val, null);
	}

	public static final String toJsonObject(int resultCode, String reason,
			Object resultInfo) {
		return JSON.toJSONString(toJson(resultCode, reason, resultInfo),SerializerFeature.DisableCircularReferenceDetect);
	}
	
	public static final JSONObject toJson(int resultCode, String reason,
			Object resultInfo) {
		JSONObject json = new JSONObject();
		json.put("resultCode", Integer.valueOf(resultCode));
		json.put("reason", reason);
		json.put("resultInfo", resultInfo);
		return json;
	}
}
