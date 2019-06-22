package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户行为数据
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-24 17:14:09
 */
@Table(name = "`sys_request_serial`")
public class SysRequestSerial extends BaseEntity {

	private static final long serialVersionUID = -1605270274015212065L;

	@Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`app_type`")
    private String appType;

    @Column(name = "`date_time`")
    private Date dateTime;

    @Column(name = "`uri_key`")
    private String uriKey;

    @Column(name = "`standard_uri`")
    private String standardUri;

    @Column(name = "`uri`")
    private String uri;

    @Column(name = "`time`")
    private Long time;

    @Column(name = "`method`")
    private String method;

    @Column(name = "`host`")
    private String host;

    @Column(name = "`user_key`")
    private String userKey;

    @Column(name = "`os`")
    private String os;

    @Column(name = "`browser`")
    private String browser;

    @Column(name = "`agent_string`")
    private String agentString;

    @Column(name = "`content_type`")
    private String contentType;

    @Column(name = "`class_name`")
    private String className;

    @Column(name = "`signature`")
    private String signature;

    @Column(name = "`args`")
    private String args;

    @Column(name = "`exception`")
    private String exception;

    @Column(name = "`session_key`")
    private String sessionKey;

    @Column(name = "`return_value`")
    private String returnValue;
    /**
	 * 创建一个新的 SysRequestSerial实例. 
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-27 22:05:56
	 */
	public SysRequestSerial() {
		super();
	}

	/**
	 * 创建一个新的 SysRequestSerial实例. 
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-27 22:05:51
	 * @param agentString
	 */
	public SysRequestSerial(String appType, String agentString) {
		super();
		this.appType = appType;
		this.agentString = agentString;
	}

	/**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return app_type
     */
    public String getAppType() {
        return appType;
    }

    /**
     * @param appType
     */
    public void setAppType(String appType) {
        this.appType = appType;
    }

    /**
     * @return date_time
     */
    public Date getDateTime() {
        return dateTime;
    }

    /**
     * @param dateTime
     */
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * @return uri_key
     */
    public String getUriKey() {
        return uriKey;
    }

    /**
     * @param uriKey
     */
    public void setUriKey(String uriKey) {
        this.uriKey = uriKey;
    }

    /**
     * @return standard_uri
     */
    public String getStandardUri() {
        return standardUri;
    }

    /**
     * @param standardUri
     */
    public void setStandardUri(String standardUri) {
        this.standardUri = standardUri;
    }

    /**
     * @return uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * @return time
     */
    public Long getTime() {
        return time;
    }

    /**
     * @param time
     */
    public void setTime(Long time) {
        this.time = time;
    }

    /**
     * @return method
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * @return host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return user_key
     */
    public String getUserKey() {
        return userKey;
    }

    /**
     * @param userKey
     */
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    /**
     * @return os
     */
    public String getOs() {
        return os;
    }

    /**
     * @param os
     */
    public void setOs(String os) {
        this.os = os;
    }

    /**
     * @return browser
     */
    public String getBrowser() {
        return browser;
    }

    /**
     * @param browser
     */
    public void setBrowser(String browser) {
        this.browser = browser;
    }

	/**
	 * getter method for agentString
	 * @return the agentString
	 */
	public String getAgentString() {
		return agentString;
	}

	/**
	 * setter method for agentString
	 * @param agentString the agentString to set
	 */
	public void setAgentString(String agentString) {
		this.agentString = agentString;
	}

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }
}