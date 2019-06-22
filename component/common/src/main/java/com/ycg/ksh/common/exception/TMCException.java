/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-29 22:47:49
 */
package com.ycg.ksh.common.exception;

import com.ycg.ksh.common.system.SystemKey;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.StringUtils;

/**
 * 合同物流管理平台一场
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-29 22:47:49
 */
public class TMCException extends RuntimeException {

	private static final long serialVersionUID = -2155697037851690878L;
	
	public static final String RPC_MESSAGE = "服务请求异常,稍后重试!!!";
	public static final String SERVER_MESSAGE = "服务器异常!!!";
	
	
	protected static String appName;
	protected static String host;
	
	protected static String prefixMesssage;
	
	protected String friendlyMessage;
	protected boolean printThrowable;

	protected static String prefixMesssage(String message) {
		if(StringUtils.isBlank(prefixMesssage)) {
			appName = SystemUtils.get(SystemKey.GLOBAL_APP_NAME);
			host = SystemUtils.get(SystemKey.SYS_LOCALHOST);
			StringBuilder builder = new StringBuilder();
			if(StringUtils.isNotBlank(appName)) {
				builder.append(builder.length() > 0 ? " ":"").append(appName);
			}
			if(StringUtils.isNotBlank(host)) {
				builder.append(builder.length() > 0 ? " ":"").append(host);
			}
			prefixMesssage = builder.toString();
		}
		if(StringUtils.isNotBlank(prefixMesssage)) {
			return prefixMesssage +" "+ message;
		}
		return message;
	}
	/**
	 * 创建一个新的 TMCException实例. 
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-29 22:48:53
	 * @param message
	 * @param cause
	 */
	public TMCException(String message, Throwable cause) {
		super(prefixMesssage(message), cause);
		printThrowable = true;
		friendlyMessage = message;
	}
	/**
	 * 创建一个新的 TMCException实例. 
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-29 22:48:53
	 * @param message
	 */
	public TMCException(String message) {
		super(prefixMesssage(message));
		friendlyMessage = message;
	}
	/**
	 * 创建一个新的 TMCException实例. 
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-29 22:48:53
	 * @param cause
	 */
	public TMCException(Throwable cause) {
		super(cause);
		printThrowable = true;
	}
	/**
	 * getter method for friendlyMessage
	 * @return the friendlyMessage
	 */
	public String getFriendlyMessage() {
		return friendlyMessage;
	}

	public boolean isPrintThrowable() {
		return printThrowable;
	}
}
