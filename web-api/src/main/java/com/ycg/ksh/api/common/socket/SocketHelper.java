package com.ycg.ksh.api.common.socket;

import com.alibaba.fastjson.JSONObject;
import com.ycg.ksh.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocketsession辅助类，如果再分布式环境下此类需要修改，需要使用MQ，原因在于WebSocketSession不可序列号，不能实现共享
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-30 16:59:28
 */
public abstract class SocketHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(SocketHelper.class);

	private static final Map<Serializable, Object> localCache = new ConcurrentHashMap<Serializable, Object>();
	
	public static void cacheSocketSession(Serializable uniqueKey, WebSocketSession webSocketSession) {
		localCache.put(uniqueKey, webSocketSession);
	}
	
	public static void removeSocketSession(Object mergeKey) {
		if(mergeKey != null) {
			localCache.remove(mergeKey);
		}
	}
	
	public static WebSocketSession loadSocketSession(Serializable mergeKey) {
		if(mergeKey != null) {
			Object object = localCache.get(mergeKey);
			if(object != null) {
				return (WebSocketSession) object;
			}
		}
		logger.debug("{}===========socket", mergeKey);
		return null;
	}
	
	
	public static void closeSocketSession(Serializable clientType, Serializable uniqueKey) {
		closeSocketSession(loadSocketSession(mergeKey(clientType, uniqueKey)));
	}
	
	public static void closeSocketSession(WebSocketSession socketSession) {
		try {
			if(isAvailable(socketSession)) {
				socketSession.close();
			}
		} catch (Exception e) {
		} finally {
			removeSocketSession(mergeKey(socketSession));
		}
	}
	
	public static boolean validate(Object uniqueKey) {
		//判断电脑端是否关闭
		if(!isAvailable(loadSocketSession(mergeKey(SocketConstant.CLIENT_TYPE_PC, uniqueKey)))) {
			return false;
		}
		//判断手机端是否关闭
		if(!isAvailable(loadSocketSession(mergeKey(SocketConstant.CLIENT_TYPE_MOBILE, uniqueKey)))) {
			return false;
		}
		return true;
	}
	public static boolean isAvailable(WebSocketSession webSocketSession) {
		if(webSocketSession == null) {
			return false;
		}
		return webSocketSession.isOpen();
	}
	public static void sendTextMessage(WebSocketSession session, String message) throws BusinessException {
		if(isAvailable(session)) {
			try {
				session.sendMessage(new TextMessage(message, true));
				logger.debug("socket->send message {} {}", mergeKey(session), message);
			} catch (Exception e) {
				throw new BusinessException("web_socket_send_error","消息发送失败", e);
			}
		}else {
			throw new BusinessException("web_socket_session_not_available","客户端链接不可用,需要在电脑端重新打开");
		}
	}
	public static void sendTextMessage(Serializable clientType, Serializable uniqueKey, String message) throws BusinessException {
		sendTextMessage(loadSocketSession(mergeKey(clientType, uniqueKey)), message);
	}
	
	public static void sendTextMessage(WebSocketSession session, String eventType, Object context) throws BusinessException {
		JSONObject object = new JSONObject();
		object.put("eventType", eventType);
		object.put("context", context);
		sendTextMessage(session, object.toJSONString());
	}
	
	public static void sendTextMessage(Serializable clientType, Serializable uniqueKey, String eventType, Object context) throws BusinessException {
		JSONObject object = new JSONObject();
		object.put("eventType", eventType);
		object.put("context", context);
		sendTextMessage(clientType, uniqueKey, object.toJSONString());
	}
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
