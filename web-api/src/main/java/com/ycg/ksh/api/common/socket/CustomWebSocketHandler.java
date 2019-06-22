package com.ycg.ksh.api.common.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ycg.ksh.adapter.api.MessageQueueService;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.extend.rabbitmq.MediaMessage;
import com.ycg.ksh.common.extend.rabbitmq.QueueKeys;
import com.ycg.ksh.common.extend.rabbitmq.RabbitMessageListener;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.ApplyRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomWebSocketHandler implements WebSocketHandler, RabbitMessageListener {
	
	private final Logger logger = LoggerFactory.getLogger(CustomWebSocketHandler.class);

	private static final Map<Serializable, Object> localCache = new ConcurrentHashMap<Serializable, Object>();

	@Resource
	MessageQueueService queueService;

	@Override
	public void afterConnectionClosed(WebSocketSession socketSession, CloseStatus closeStatus) throws Exception {
		Serializable clientType = SocketConstant.getClientType(socketSession);
		Serializable uniqueKey = SocketConstant.getUniqueKey(socketSession);
		logger.debug("socket->close connection clientType:{} uniqueKey:{}", clientType, uniqueKey);
		removeSocketSession(SocketConstant.mergeKey(clientType, uniqueKey));
		if(SocketConstant.CLIENT_TYPE_MOBILE.equals(clientType)) {//如果是手机端关闭，通知PC重置
			sendTextMessage(SocketConstant.CLIENT_TYPE_PC,  uniqueKey,  SocketConstant.MSG_TYPE_SCAN_RESET,  System.currentTimeMillis());
		}
		if(SocketConstant.CLIENT_TYPE_PC.equals(clientType)){//如果是PC端关闭，通知手机端重置
			//sendTextMessage(SocketConstant.CLIENT_TYPE_MOBILE,  uniqueKey,  SocketConstant.MSG_TYPE_SCAN_RESET,  System.currentTimeMillis());
			closeSocketSession(SocketConstant.CLIENT_TYPE_MOBILE, uniqueKey);
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession socketSession) throws Exception {
		Serializable mergeKey = SocketConstant.mergeKey(socketSession);
		logger.debug("socket->open connection {}", mergeKey);
		if(mergeKey != null) {
			cacheSocketSession(mergeKey, socketSession);
		}
	}

	@Override
	public void handleMessage(WebSocketSession socketSession, WebSocketMessage<?> socketMessage) throws Exception {
		if(socketMessage instanceof TextMessage && socketMessage.getPayloadLength() > 0) {
			SocketMessage message = Globallys.toJavaObject(socketMessage.getPayload().toString(), SocketMessage.class) ;
			message.setClientType(SocketConstant.getClientType(socketSession));
			message.setUniqueKey(SocketConstant.getUniqueKey(socketSession));
			message.setMessage(socketMessage.getPayload().toString());
			if(!message.isEmpty()){
				handleMessage(socketSession, message);
			}
		}
	}

	@Override
	public boolean handleMessage(String messageType, String messageKey, Object object) throws BusinessException {
		try {
			logger.debug("queue message -> {}",  object);
			if (StringUtils.equalsIgnoreCase(messageType, SocketMessage.class.getName())) {
				SocketMessage message = Globallys.toJavaObject(object.toString(), SocketMessage.class) ;
				if(message == null || message.isEmpty()){
					return true;
				}
				WebSocketSession session = loadSocketSession(message.getClientType(), message.getUniqueKey());
				if(session != null){
					return handleMessage(session, message);
				}
				logger.debug("session -> {}", session);
				return false;
			}
			return true;
		} catch (Exception ex) {
			throw new BusinessException("处理"+ messageType +"消息异常", ex);
		}
	}

	public boolean handleMessage(WebSocketSession session, SocketMessage message) throws BusinessException {
		try {
			logger.debug("socket -> handle message {}", message);
			if(SocketConstant.MSG_TYPE_HEARTBEAT.equalsIgnoreCase(message.getEventType())) {//心跳检测
				heartbeat(message, session);
			}else{
				sendTextMessage(session, message);
			}
			return true;
		} catch (Exception ex) {
			throw new BusinessException("处理"+ message +"消息异常", ex);
		}
	}
	private void heartbeat(SocketMessage message, WebSocketSession session) {
		if(SocketConstant.CLIENT_TYPE_MOBILE.equals(message.getClientType())) {//手机端心跳检测
			if(!isAvailable(loadSocketSession(SocketConstant.CLIENT_TYPE_PC, message.getUniqueKey()))) {//如果PC端连接不可用，则断开手机端
				closeSocketSession(message.getClientType(), message.getUniqueKey());//关闭手机端连接
			}
		}else if(SocketConstant.CLIENT_TYPE_PC.equals(message.getClientType())) {//PC端心跳检测
			if(!isAvailable(loadSocketSession(SocketConstant.CLIENT_TYPE_MOBILE, message.getUniqueKey()))) {//如果手机端连接不可用，则通知PC端重置
				sendTextMessage(session, new SocketMessage(SocketConstant.CLIENT_TYPE_PC,  message.getUniqueKey(),  SocketConstant.MSG_TYPE_SCAN_RESET,  System.currentTimeMillis()));
			}
		}else{
			sendTextMessage(session, new SocketMessage(message.getClientType(),  message.getUniqueKey(),  SocketConstant.MSG_TYPE_HEARTBEAT,  System.currentTimeMillis()));
		}
	}

	@Override
	public void handleTransportError(WebSocketSession socketSession, Throwable throwable) throws Exception {
		logger.warn("socket->transport error {}", SocketHelper.mergeKey(socketSession));
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	public  void cacheSocketSession(Serializable uniqueKey, WebSocketSession webSocketSession) {
		localCache.put(uniqueKey, webSocketSession);
	}

	public  void removeSocketSession(Object mergeKey) {
		if(mergeKey != null) {
			localCache.remove(mergeKey);
		}
	}
	public  WebSocketSession loadSocketSession(Serializable clientType, Serializable uniqueKey) {
		return loadSocketSession(SocketConstant.mergeKey(clientType, uniqueKey));
	}
	public  WebSocketSession loadSocketSession(Serializable mergeKey) {
		if(mergeKey != null) {
			Object object = localCache.get(mergeKey);
			if(object != null) {
				return (WebSocketSession) object;
			}
		}
		return null;
	}

	public  void closeSocketSession(Serializable clientType, Serializable uniqueKey) {
		closeSocketSession(loadSocketSession(clientType, uniqueKey));
	}

	public  void closeSocketSession(WebSocketSession socketSession) {
		try {
			if(isAvailable(socketSession)) {
				socketSession.close();
			}
		} catch (Exception e) {
		} finally {
			removeSocketSession(SocketConstant.mergeKey(socketSession));
		}
	}

	public  boolean isAvailable(WebSocketSession webSocketSession) {
		if(webSocketSession == null) {
			return false;
		}
		return webSocketSession.isOpen();
	}

	public  void sendTextMessage(Serializable clientType, Serializable uniqueKey, String eventType, Object context) throws BusinessException {
		sendTextMessage(loadSocketSession(clientType, uniqueKey),  new SocketMessage(clientType, uniqueKey,  eventType, context));
	}
	public  void sendTextMessage(WebSocketSession session, String message) throws BusinessException {
		try {
			if(isAvailable(session)){
				logger.debug("socket-> send message {}", message);
				session.sendMessage(new TextMessage(message, true));
			}
		} catch (IOException e) {
			throw new  BusinessException("send text message exception ",e);
		}
	}
	public  void sendTextMessage(WebSocketSession session, SocketMessage message) throws BusinessException {
		if (session != null){
			sendTextMessage(session, message.getMessage());
		}else{
			sendMessage(message);
		}
	}

	private void sendMessage(SocketMessage message) throws BusinessException {
		queueService.sendApiMessage(new MediaMessage(SocketMessage.class.getName(),  Globallys.UUID(), Globallys.toJsonString(message), 10));
	}
}
