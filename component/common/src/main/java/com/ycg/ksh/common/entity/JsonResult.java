package com.ycg.ksh.common.entity;

import java.util.HashMap;

/**
 * 控制层返回json格式数据时使用的数据传递实体
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-06 12:23:43
 */
public class JsonResult extends HashMap<String, Object> {

	private static final long serialVersionUID = 4939149293296228160L;

	public static final JsonResult SUCCESS = new JsonResult();
	
	private static final String SUCCESS_KEY = "success";
	private static final String SUCCESS_CODE = "SUCCESS";
	private static final String SUCCESS_MESSAGE = "操作成功";
	private static final String MESSAGE_KEY = "message";
	private static final String CODE_KEY = "code";
	
	
	public JsonResult() {
		super();
		setCode(SUCCESS_CODE);
		setSuccess(true);
		setMessage(SUCCESS_MESSAGE);
	}
	
	public JsonResult(String code, boolean success, String message) {
		super();
		setCode(code);
		setSuccess(success);
		setMessage(message);
	}
	
	public JsonResult(String code, String message) {
		this(code, false, message);
	}
	
	public JsonResult(boolean success, String message) {
		setSuccess(success);
		setMessage(message);
	}

	public JsonResult modify(boolean success, String message){
		setSuccess(success);
		setMessage(message);
		return this;
	}
	
	public JsonResult modify(String code, boolean success, String message){
		setCode(code);
		setSuccess(success);
		setMessage(message);
		return this;
	}
	
	public JsonResult modify(String code, String message){
		setCode(code);
		setMessage(message);
		return this;
	}

    public JsonResult message(String message){
        setMessage(message);
        return this;
    }

	public JsonResult add(String key, Object object){
		put(key, object);
		return this;
	}
	public JsonResult put(BaseEntity entity){
		putAll(entity.toMap());
		return this;
	}

	public boolean isSuccess() {
		Object success = get(SUCCESS_KEY);
		if(success != null){
			return new Boolean(String.valueOf(success));
		}
		return false;
	}


	private void setSuccess(boolean success) {
		put(SUCCESS_KEY, success);
	}
	public String getMessage() {
		return String.valueOf(get(MESSAGE_KEY));
	}
	private void setMessage(String message) {
		put(MESSAGE_KEY, message);
	}
	
	public String getCode() {
		return String.valueOf(get(CODE_KEY));
	}
	private void setCode(String code) {
		put(CODE_KEY, code);
	}
}
