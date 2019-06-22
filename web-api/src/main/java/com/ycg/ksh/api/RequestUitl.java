package com.ycg.ksh.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.encrypt.DES;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.service.AuthorizeUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.*;

public class RequestUitl {

	public static final String GUESTS_KEY = "guests-key";
	public static final String CATEGORY_KEY = "category-key";

	/**请求参数 Token的键*/
	//private static final String PARAMS_TOKEN_KEY = "token";

	private static String SERVER_PATH_KEY = "SYS-SERVER-PATH";

	/**请求参数 Token的键*/
	private static final String PARAMS_TOKEN_KEY = "token";
	private static final String PARAMS_TOKEN_UID_KEY = "token_uid";

	public static final String X_REQUEST_TYPE_KEY = "X-Request-Type";
	public static final String X_REQUEST_TYPE = "ajax";
	public static final String X_REQUESTED_WITH_KEY = "X-Requested-With";
	public static final String X_REQUESTED_WITH = "XMLHttpRequest";


	public static final String SESSION_USER_INFO_KEY = "SESSION_USER_INFO";//回话存储用户信息的键值
	public static final String REQUEST_USER_PERMISSION_KEY = "user_permission";//回话存储用户权限的键值
	public static final String REQUEST_SERIAL_KEY = "REQUEST_SERIAL_KEY";//用户请求数据暂存的KEY值
	public static final String REQUEST_GROUP_KEY = "REQUEST_GROUP_KEY";//用户项目组暂存的KEY值
	//public static final String MANAGINGUSER_FALG_KEY = "MANAGINGUSER_FALG_KEY";//用户是否绑定微信帐号

	public static final String[] DEVICEARRAY = new String[]{"android", "iphone", "ipod","ipad", "windows phone", "mqqbrowser"};


	public static HttpSession session(HttpServletRequest request){
		return request.getSession();
	}

	public static Object session(HttpServletRequest request, String sessionKey){
		return session(request).getAttribute(sessionKey);
	}

	public static void session(HttpServletRequest request, String sessionKey, Object sessionValue){
		session(request).setAttribute(sessionKey, sessionValue);
	}


	public static void write(HttpServletResponse response, Object object) throws IOException {
		write(response, JSON.toJSONString(object));
	}

	public static void write(HttpServletResponse response, String message) throws IOException {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(message);
		} finally {
			if (null != out) {
				out.close();
				out = null;
			}
		}
	}

	public static boolean isAjax(HttpServletRequest request){
		boolean ajax = X_REQUESTED_WITH.equalsIgnoreCase(request.getHeader(X_REQUESTED_WITH_KEY));
		String request_type = request.getHeader(X_REQUEST_TYPE_KEY);
		if(StringUtils.isBlank(request_type)){
			request_type = String.valueOf(request.getAttribute(X_REQUEST_TYPE_KEY));
		}
		if(StringUtils.isNotBlank(request_type) && X_REQUEST_TYPE.equalsIgnoreCase(request_type)){
			ajax = true;
		}
		return ajax;
	}

	public static void setAjax(HttpServletRequest request){
		request.setAttribute(X_REQUEST_TYPE_KEY, X_REQUEST_TYPE);
	}

	public static void response(HttpServletResponse response, JsonResult result){
		try{
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			String jsonString = JSONObject.toJSONString(result);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(jsonString.getBytes(Constant.CHARACTER_ENCODING_UTF8));
			outputStream.flush();
			outputStream.close();
		}catch (Exception e){ }
	}
	public static String getToken(HttpServletRequest request) {
		String authToken = request.getHeader(PARAMS_TOKEN_KEY);
		if(StringUtils.isBlank(authToken)) {
			authToken = request.getParameter(PARAMS_TOKEN_KEY);
		}
		if(StringUtils.isBlank(authToken)) {
			Object attribute = request.getAttribute(PARAMS_TOKEN_KEY);
			if(attribute != null) {
				authToken = String.valueOf(attribute);
			}
		}
		return authToken;
	}
	public static void modifyToken(HttpServletRequest request, String token) {
		request.setAttribute(PARAMS_TOKEN_KEY, token);
	}

	public static void modifyUserID(HttpServletRequest request, Object userId) {
		request.setAttribute(PARAMS_TOKEN_UID_KEY, userId);
	}

	public static String getUserID(HttpServletRequest request) {
		Object userId = request.getAttribute(PARAMS_TOKEN_UID_KEY);
		if(userId != null) {
			return String.valueOf(userId);
		}
		return null;
	}
	 /**
     * 更新当前会话中的用户信息
     * <p>
     * @param request
	  * @param object
	  */
	public static void modifyUserInfo(HttpServletRequest request, Object object) {
		session(request, SESSION_USER_INFO_KEY, object);
	}

	/**
	 * 更新回话中权限信息
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-25 14:11:48
	 * @param request
	 * @param object
	 */
	public static void modifyUserPermission(HttpServletRequest request, Object object) {
		session(request, REQUEST_USER_PERMISSION_KEY, object);
	}

	/**
	 * 获取回话缓存中的项目组编号
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-25 14:11:15
	 * @param request
	 * @param defaultValue
	 * @return
	 */
	public static Integer getGroupKey(HttpServletRequest request, Integer defaultValue) {
		Object obj = session(request, REQUEST_GROUP_KEY);
		if(obj != null) {
			Integer gid = Integer.parseInt(String.valueOf(obj));
			if(gid > 0){
				return gid;
			}
		}
		modifyGroupKey(request, defaultValue);
		return defaultValue;
	}

	/**
	 * 更新回话缓存中的项目组编号
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-25 14:11:13
	 * @param request
	 * @param object
	 */
	public static void modifyGroupKey(HttpServletRequest request, Object object) {
		if(object != null) {
			session(request, REQUEST_GROUP_KEY, object);
		}
	}


    /**
     * 从当前回话中获取用户信息
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-04 09:41:41
     * @param request
     * @return
     */
	public static AuthorizeUser getUserInfo(HttpServletRequest request) {
		Object userInfo = session(request, SESSION_USER_INFO_KEY);
		if(userInfo != null) {
			return (AuthorizeUser) userInfo;
		}
		return null;
	}

    /**
     * 清楚当前会话中的用户信息
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-04 09:42:01
     * @param request
     */
	public static void cleanUserInfo(HttpServletRequest request) {
		session(request).removeAttribute(SESSION_USER_INFO_KEY);
	}
	public static void cleanSession(HttpServletRequest request) {
		HttpSession session = session(request);
		Enumeration<String> enumeration = session.getAttributeNames();
		while (enumeration.hasMoreElements()){
			session.removeAttribute(enumeration.nextElement());
		}
	}

	public static String getSessionId(HttpServletRequest request) {
		HttpSession session = session(request);
		if(session != null) {
			return session.getId();
		}
		return null;
	}

	/**
	 * 重新组装url地址
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-28 15:24:36
	 * @param request
	 * @param excludes 要排除的参数名称
	 * @return
	 */
	public static String buildQueryString(HttpServletRequest request, String...excludes) {
		StringBuilder builder = new StringBuilder();
		boolean exclude = false;
		if(excludes != null && excludes.length > 1) {
			Arrays.sort(excludes);
			exclude = true;
		}
		Enumeration<String> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = enumeration.nextElement();
			if("nsukey".equalsIgnoreCase(name)){
				continue;
			}
			if(exclude && Arrays.binarySearch(excludes, name) >= 0) {
				continue;
			}
			String[] values = request.getParameterValues(name);
			for (String value : values) {
				builder.append(name).append("=").append(value).append("&");
			}
		}
		if(builder.length() > 0) {
			builder.deleteCharAt(builder.length() - 1);
		}
		return builder.toString();
	}

	public static String buildUrl(HttpServletRequest request, String...excludes) {
		String queryString = buildQueryString(request, excludes);
		if(StringUtils.isNotBlank(queryString)) {
			return request.getRequestURL().append("?").append(queryString).toString();
		}
		return request.getRequestURL().toString();
	}

	/**
	 * 获取请求客户端主机名称
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-28 15:25:12
	 * @param request
	 * @return
	 */
	public static String getRemoteHost(HttpServletRequest request){
        String ipAddress = request.getHeader("x-forwarded-for"), unknown = "unknown";
        if(StringUtils.isBlank(ipAddress) || unknown.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(StringUtils.isBlank(ipAddress) || unknown.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(StringUtils.isBlank(ipAddress) || unknown.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(ipAddress == null || ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
                //根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					ipAddress = Optional.of(InetAddress.getLocalHost()).map(InetAddress::getHostAddress).get();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ipAddress!=null && ipAddress.length() > 15){ //"***.***.***.***".length() = 15
            int index = ipAddress.indexOf(",");
        	if(index > 0){
                ipAddress = ipAddress.substring(0, index);
            }
        }
        return ipAddress;
    }

	public static String getServerPath(HttpServletRequest request) {
		String server_path = null;
		Object pathObject = request.getAttribute(SERVER_PATH_KEY);
		if(pathObject != null) {
			server_path = String.valueOf(pathObject);
		}else {
			StringBuilder builder = new StringBuilder();
			builder.append(request.getScheme()).append("://");
			builder.append(request.getServerName());
			Integer serverPort = request.getServerPort();
			if(80 - serverPort != 0) {
				builder.append(":").append(serverPort);
			}
			String contextPath = request.getContextPath();
			if(StringUtils.isNotBlank(contextPath)) {
				builder.append(contextPath);
			}
			server_path = builder.toString();
			request.setAttribute(SERVER_PATH_KEY, server_path);
		}
		return server_path;
	}


	public static String getClientKey(HttpServletRequest request) {
		User user = getUserInfo(request);
		if(user != null) {
			return String.valueOf(user.getId());
		}else {
			return getSessionId(request);
		}
	}

    public static Map<String, String> getRequestParameters(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<String, String>();

        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            if (StringUtil.isNotEmpty(paramValue)) {
             map.put(paramName, paramValue);
            }
        }
        return map;
    }

    public static final String userAgentString(HttpServletRequest request) {
    	return request.getHeader("User-Agent");
    }


	public static String buildDownload(String filePath, String fileName) {
		return buildDownload(filePath, fileName, true);
	}

	public static String buildDownload(String filePath, String fileName, boolean delete) {
		try {
			String request = SystemUtils.buildUrl(SystemUtils.getCallBackDomain(), "/special/download");
			return request + "?p=" + DES.encrypt(filePath +"&"+ fileName+"&"+ delete, SystemUtils.secretKey());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String[] download(String desdata) {
		if (com.ycg.ksh.common.util.StringUtils.isNotBlank(desdata)) {
			try {
				String source = DES.decrypt(desdata, SystemUtils.secretKey());
				if (com.ycg.ksh.common.util.StringUtils.isNotBlank(source)) {
					return source.split("&");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String encode(String source) {
		try {
			return URLEncoder.encode(source, "utf8");
		} catch (UnsupportedEncodingException e) {
		}
		return source;
	}

	public static String decode(String source) {
		try {
			return URLDecoder.decode(source, "utf8");
		} catch (UnsupportedEncodingException e) {
		}
		return source;
	}


	/**
	 * 判断是否是移动设备的请求
	 * @param request
	 * @return
	 */
	public static boolean isMobileDevice(HttpServletRequest request){
		String agentString = userAgentString(request);
		if(StringUtils.isNotBlank(agentString)){
			agentString = agentString.toLowerCase();
			for (String device : DEVICEARRAY) {
				if(agentString.indexOf(device) > 0){
					return true;
				}
			}
		}
		return false;
	}


	public static Object getValue(HttpServletRequest requestWrapper, String key){
		Object value = requestWrapper.getHeader(key);
		if(value == null){
			value = requestWrapper.getParameter(key);
		}
		if(value == null){
			value = requestWrapper.getAttribute(key);
		}
		return value;
	}

}
