package com.ycg.ksh.common.entity;

import com.ycg.ksh.common.util.encrypt.Base64;

import java.util.Date;

/**
 * 用户的请求信息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-27 10:50:30
 */
public class RequestSerial extends RequestEntity {

	private static final long serialVersionUID = -7417747785112735055L;

	private String appType;
	private Date dateTime;
	
	private String userKey;//用户KEY
	private String uri;//请求地址
	private long time = 0l;//请求时间
	private String host;//客户端主机
	private String method;//请求方法
	private String os;//系统
	private String browser;//浏览器
	
	@Override
	public String toString() {
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(Base64.encodeString(uri)).append(" ");
			builder.append(time).append(" ");
			builder.append(method).append(" ");
			builder.append(host).append(" ");
			builder.append(userKey).append(" ");
			builder.append(Base64.encodeString(os)).append(" ");
			builder.append(Base64.encodeString(browser));
			return builder.toString();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
}
