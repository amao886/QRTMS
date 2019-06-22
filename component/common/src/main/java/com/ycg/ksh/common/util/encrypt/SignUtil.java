package com.ycg.ksh.common.util.encrypt;


import com.ycg.ksh.common.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 签名工具类
 */
public class SignUtil {

	//编码集
	public static final String CHARSET_ENCODER = "UTF-8";

	/**
	 * <p>map参数排序</p>
	 * @param params
	 * @return
	 * 2017年1月12日下午4:29:45
	 */
	public static String createLinkString(Map<String, Object> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			builder.append(key).append("=");
			Object value = params.get(key);
			if(value != null && StringUtils.isNotBlank(value.toString())){
				builder.append(value);
			}
			builder.append("&");
		}
		if(builder.length() > 0){
			builder.deleteCharAt(builder.length() - 1);
		}
		return builder.toString();
	}
	
}
