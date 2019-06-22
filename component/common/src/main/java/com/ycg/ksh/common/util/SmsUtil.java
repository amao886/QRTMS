package com.ycg.ksh.common.util;

import com.alibaba.fastjson.JSONObject;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.extend.http.HttpClient;
import com.ycg.ksh.common.system.SystemUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**        
 * 名称：SmsUtil    
 * 描述：    发短信工具类
 * 创建人：yangc    
 * 创建时间：2017年7月14日 上午10:58:24    
 * @version        
 */
public class SmsUtil {

	private static final Logger logger = LoggerFactory.getLogger(SmsUtil.class);
	
	public static final String SMS_USERNAME = "sms.username";
	public static final String SMS_PASSWORD = "sms.password";
    public static final String SMS_SITECODE = "sms.sitecode";
    public static final String SMS_SMSTYPE = "sms.smstype";
	public static final String SMS_LOGIN_URL = "sms.login.url";
	public static final String SMS_SEND_URL = "sms.send.url";
	
    // 生成随机数
    public static String randomCode(int count) {
    	StringBuilder vcode = new StringBuilder();
        for (int i = 0; i < 6; i++) {
        	vcode.append((int) (Math.random() * 9));
        }
        return vcode.toString();
    }
	
	
	private static String getToken() {
		HttpClient httpClient = new HttpClient(SystemUtils.get(SMS_LOGIN_URL));
		httpClient.parameter("userName", SystemUtils.get(SMS_USERNAME));
		httpClient.parameter("passWord", SystemUtils.get(SMS_PASSWORD));
		try {
			String result = httpClient.json();
			logger.debug("发送短信获取Token请求信息 {}", httpClient);
			if(StringUtils.isNotBlank(result)) {
				JSONObject httpresult = JSONObject.parseObject(result);
				if(httpresult.containsKey("loginToken")) {
					return  httpresult.getString("loginToken");
				}
			}
		} catch (Exception e) {
			logger.error("发送短信获取Token异常", e);
		}
		throw new BusinessException("发送短信获取Token异常");
	}
	
	public static void send(String mobile, String content){
		String loginToken = getToken();
		if(StringUtils.isNotBlank(loginToken)) {
			HttpClient httpClient = new HttpClient(SystemUtils.get(SMS_SEND_URL));
			httpClient.property("auth-token",loginToken);
			httpClient.parameter("siteCode", SystemUtils.get(SMS_SITECODE));
			httpClient.parameter("smsType", SystemUtils.get(SMS_SMSTYPE));
			httpClient.parameter("mobile", mobile);
			httpClient.parameter("content", content+ "【远成物流】");
			try {
				String result = httpClient.json();
				logger.debug("发送短信请求信息 {}", httpClient);
				if(StringUtils.isNotBlank(result)) {
					JSONObject httpresult = JSONObject.parseObject(result);
					String errorMessage = httpresult.getString("errorMessage");
					if (StringUtils.isNotEmpty(errorMessage)) {
						throw new BusinessException("发送短信异常: " + errorMessage);
					}
				}
			} catch (Exception e) {
				logger.error("发送短信异常 {} {}", mobile, content, e);
				throw new BusinessException("发送短信异常");
			}
		}else {
			throw new BusinessException("发送短信获取Token异常");
		}
	}
}
