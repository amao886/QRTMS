package com.ycg.ksh.common.exception;

/**
 * 业务逻辑异常，任何业务逻辑处理错误时，均可抛出此异常
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-05-25 12:51:11
 */
public class BusinessException extends TMCException {

	private static final long serialVersionUID = 1763729725812293662L;
	
	private static final String DB_EXCEPTION = "DB-999";
	
	/**
	 * 逻辑异常代码
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-05-25 12:51:44
	 */
	private String code;

	/**
	 * 创建一个新的 BusinessException实例. 
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-05-25 12:51:56
	 * @param code  异常代码
	 * @param message 异常提示消息
	 * @param cause 异常链
	 */
	public BusinessException(String code, String message, Throwable cause) {
		super(String.format("业务异常:code = %d, %s", code, message), cause);
		this.code = code;
		this.friendlyMessage = message;
	}

	/**
	 * 创建一个新的 BusinessException实例. 
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-30 16:09:55
	 * @param code  异常代码
	 * @param message 异常提示消息
	 */
	public BusinessException(String code, String message) {
		super(String.format("业务异常:code = %s, %s", code, message));
		this.code = code;
		this.friendlyMessage = message;
	}

	/**
	 * 创建一个新的 BusinessException实例. 
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-05-25 12:53:22
	 * @param message  异常消息
	 * @param cause 异常链
	 */
	public BusinessException(String message, Throwable cause) {
		super(message, cause);
		this.friendlyMessage = message;
	}

	/**
	 * 创建一个新的 BusinessException实例. 
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-05-25 12:54:39
	 * @param message 异常提示消息
	 */
	public BusinessException(String message) {
		super(message);
		this.friendlyMessage = message;
	}

	
	/**
	 * 数据库操作异常
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-15 11:22:06
	 * @param message
	 * @return
	 */
	public static BusinessException dbException(String message) {
		return new BusinessException(DB_EXCEPTION, message);
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
