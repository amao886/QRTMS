package com.ycg.ksh.api;

import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.common.util.encrypt.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class ResistDuplicateRequest {
    
    private static final Logger logger = LoggerFactory.getLogger(ResistDuplicateRequest.class);

	private static final Map<String, Long> duplicate = new ConcurrentHashMap<String, Long>();
	
	private Pattern pattern;

	private enum EnumObjectDuplicateRequest{  
        
    	SINGLETON_OBJECT;  
          
        private ResistDuplicateRequest instance;  
          
        private EnumObjectDuplicateRequest(){
            instance = new ResistDuplicateRequest();  
            instance.pattern = Pattern.compile("[^.]*(page|search|jsapi|list|deatil|query|user/info|package/count|dispatch/delete)[^.]*");
        }  
        public ResistDuplicateRequest getInstance(){  
        	return instance;  
        }
    }
	
	public static ResistDuplicateRequest build() {
		return EnumObjectDuplicateRequest.SINGLETON_OBJECT.getInstance();
	}
	
	public ResistDuplicateRequest() {
		super();
	}

	private boolean setNotExist(String key, Long value) {
	    synchronized (duplicate) {
	        Long requestTime = duplicate.get(key);
	        if(requestTime == null || requestTime <= 0) {
	            duplicate.put(key, value);
	            return true;
	        }
	        return false;
        }
	}
	
	//添加map
	public boolean validate(HttpServletRequest httpServletRequest, String sessionKey, CacheManager cacheManager){
	    if (pattern != null && pattern.matcher(httpServletRequest.getRequestURI()).find()) {
	    	logger.info("validate()==================>:{}",httpServletRequest.getRequestURI());
            return true;
        }
	    try {
	        String requestKey = MD5.encrypt(sessionKey +"#"+ httpServletRequest.getRequestURI());
	        Long ctime = System.currentTimeMillis();
	        if(setNotExist(requestKey, ctime)){//本机
	            if(cacheManager.setNotExist(requestKey, ctime)) {//分布式
	            	logger.info("validate()==================>requestKey:{}",requestKey);
	                return true;
	            }
	        }
        } catch (Exception e) {
            logger.error("validate ->sessionKey:{} uri:{}", sessionKey, httpServletRequest.getRequestURI(), e);
        }
        return false;
	}
	
	//删除存在的key
	public void clean(HttpServletRequest httpServletRequest, String userKey, CacheManager cacheManager){
		try {
		    String requestKey = MD5.encrypt(userKey +"#"+ httpServletRequest.getRequestURI());
	        cacheManager.delete(requestKey);
	        duplicate.remove(requestKey);
        } catch (Exception e) {
            logger.error("clean ->uKey:{} uri:{}", userKey, httpServletRequest.getRequestURI(), e);
        }
	}
}
