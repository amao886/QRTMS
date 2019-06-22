package com.ycg.ksh.api.common.socket;

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;


public class CustomHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
	protected final Logger logger = LoggerFactory.getLogger(CustomHandshakeInterceptor.class);

	/**
	 * @see HttpSessionHandshakeInterceptor#beforeHandshake(ServerHttpRequest, ServerHttpResponse, WebSocketHandler, Map)
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-30 14:38:02
	 * @param serverHttpRequest
	 * @param serverHttpResponse
	 * @param webSocketHandler
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
		String uri = serverHttpRequest.getURI().getPath(), client_type_key = null, unique_key = null;
		if(StringUtils.isNotBlank(uri)) {
			int s = uri.lastIndexOf('/') + 1, e = uri.indexOf('?');
			client_type_key = (e > 0 ? uri.substring(s, e) : uri.substring(s));
		}
		if (serverHttpRequest instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) serverHttpRequest;
			User user = RequestUitl.getUserInfo(servletServerHttpRequest.getServletRequest());
			if(user != null) {
				unique_key = String.valueOf(user.getId());
			}
			if(StringUtils.isBlank(unique_key) || SocketConstant.CLIENT_TYPE_LOGIN_PC.equalsIgnoreCase(client_type_key)){
				unique_key = RequestUitl.getSessionId(servletServerHttpRequest.getServletRequest());
			}
		}
		map.put(SocketConstant.CLIENT_TYPE_KEY, client_type_key);
		map.put(SocketConstant.UNIQUE_KEY, unique_key);
		logger.debug("websocket handshake -> {} {}", client_type_key, unique_key);
		return super.beforeHandshake(serverHttpRequest, serverHttpResponse, webSocketHandler, map);
	}
	/**
	 * @see HttpSessionHandshakeInterceptor#afterHandshake(ServerHttpRequest, ServerHttpResponse, WebSocketHandler, Exception)
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-30 14:38:02
	 * @param request
	 * @param response
	 * @param wsHandler
	 * @param ex
	 */
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
		//System.out.println("++++++++++++++++ HandshakeInterceptor: afterHandshake  ++++++++++++++");
		super.afterHandshake(request, response, wsHandler, ex);
	}
}
