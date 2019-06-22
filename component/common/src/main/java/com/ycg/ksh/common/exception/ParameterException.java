/**
 * kylin-common
 * com.ycgwl.kylin.exception
 */
package com.ycg.ksh.common.exception;


/**
 * 系统参数异常,任何参数有不合理之处都可抛出此异常
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-05-25 12:47:30
 */
public class ParameterException extends TMCException {

	private static final long serialVersionUID = -5898218861108473496L;

	/**
	 * code :错误码
	 * <p>
	 * @developer Create by <a href="mailto:86756@ycgwl.com">yanxf</a> at 2017年7月21日
	 */
	private String code;
	
	/**
	 * 当前参数的值
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-05-25 12:49:26
	 */
	private Object target;

	/**
	 * 创建一个新的 ParameterException实例. 
	 * <p>
	 * @developer Create by <a href="mailto:86756@ycgwl.com">yanxf</a> at 2017年7月21日
	 * @param code 异常编码
	 * @param target  参数当前值
	 * @param message  参数异常提示消息
	 */
	public ParameterException(String code, Object target, String message) {
		super(String.format("参数异常:%s, %s, %s", code, String.valueOf(target), message));
		this.friendlyMessage = message;
		this.code = code;
		this.target = target;
	}
	
	/**
	 * 创建一个新的 ParameterException实例. 
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-08-30 15:55:41
	 * @param target
	 */
	public ParameterException(Object target, String message) {
		super(String.format("参数异常: %s, %s", String.valueOf(target), message));
		this.friendlyMessage = message;
		this.target = target;
	}

	/**
	 * 创建一个新的 ParameterException实例. 
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-05-25 12:48:52
	 * @param message  参数异常提示消息
	 */
	public ParameterException(String message) {
		super(message);
		this.friendlyMessage = message;
	}
	
	public String getCode() {
		return code;
	}

	public Object getTarget() {
		return target;
	}

}
