package com.ycg.ksh.api.common.socket;

import org.springframework.web.socket.WebSocketSession;

import java.io.Serializable;
import java.util.Map;

/**
 * 常量
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-30 16:59:15
 */
public abstract class SocketConstant {
	
	/**socket session 存储客户端唯一标识的KEY值*/
	public static final String UNIQUE_KEY = "UNIQUE_KEY";
	/**socket session 存储客户端类型的KEY值*/
	public static final String CLIENT_TYPE_KEY = "CLIENT_TYPE_KEY";
	
	/**消息类型*/
	public static final String MSG_TYPE_SCAN_OPEN = "scan_open";//打开扫描
	public static final String MSG_TYPE_SCAN_RESET = "scan_reset";//重置扫描
	public static final String MSG_TYPE_SCAN = "scan";//开始扫描
	public static final String MSG_TYPE_EXCEPTION = "exception";//异常消息
	public static final String MSG_TYPE_HEARTBEAT = "heartbeat";//心跳消息
	
	/**客户端类型*/
	public static final String CLIENT_TYPE_PC = "pc";//电脑端
	public static final String CLIENT_TYPE_MOBILE = "mobile";//手机端
	public static final String CLIENT_TYPE_LOGIN_PC = "login_pc";//电脑端扫码登陆


	public static Serializable getUniqueKey(WebSocketSession session) {
		Map<String, Object> map = session.getAttributes();
		return (Serializable) map.get(SocketConstant.UNIQUE_KEY);
	}
	public static Serializable getClientType(WebSocketSession session) {
		Map<String, Object> map = session.getAttributes();
		return (Serializable) map.get(SocketConstant.CLIENT_TYPE_KEY);
	}


	public static Serializable mergeKey(WebSocketSession session) {
		if(session != null) {
			Map<String, Object> map = session.getAttributes();
			Object clientType = map.get(SocketConstant.CLIENT_TYPE_KEY);
			Object uniqueKey = map.get(SocketConstant.UNIQUE_KEY);
			return mergeKey(clientType, uniqueKey);
		}
		return null;
	}

	public static Serializable mergeKey(Object clientType, Object uniqueKey) {
		return String.valueOf(clientType) +"#"+ String.valueOf(uniqueKey);
	}



}
