package com.ycg.ksh.common.exception;

public class LoginException extends TMCException {

	private Integer type;//1:未登录，2:没有绑定手机号
	/**
	 * serialVersionUID : TODO Add description
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-12 12:49:34
	 */
	private static final long serialVersionUID = 3808966262471418288L;

	public static LoginException notLoginException(String message){
		return new LoginException(1, message);
	}
	public static LoginException notBindException(String message){
		return new LoginException(2, message);
	}

	public LoginException(Integer type, String message) {
		super(message);
		this.type = type;
	}

	public boolean notLogin(){
		return type == 1;
	}
	public boolean notBind(){
		return type == 2;
	}
}
