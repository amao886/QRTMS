package com.ycg.ksh.common.util;

import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**        
 * 名称：    MapUtil
 * 描述：    map操作类
 * 创建人：yangc    
 * 创建时间：2017年7月14日 上午10:58:24    
 * @version        
 */
public class MapUtils {
	private static Map<String, String> map = new ConcurrentHashMap<String, String>();
	
	//添加map
	public static synchronized Map<String, String> putMap(String key){
		map.put(key, key);
		return map;
	}
	
	//删除存在的key
	public static synchronized Map<String, String> removeMap(String key){
		String redisKeymap = map.get(key);
		if(StringUtils.isNotEmpty(redisKeymap)){
			map.remove(key);
		}
		return map;
	}
	
	//删除存在的key
	public static String getMap(String key){
		return map.get(key);
	}
}
