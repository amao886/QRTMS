/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-26 16:12:31
 */
package com.ycg.ksh.entity.adapter.wechat;

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 常规TOKEN
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-26 16:12:31
 */
public class CommonToken extends BaseEntity {
    
    private static final long serialVersionUID = 8184164758245456034L;
    
    private String appId;
    private String secret;
    private String token;
    private Long expires;
    /**
     * getter method for appId
     * @return the appId
     */
    public String getAppId() {
        return appId;
    }
    /**
     * setter method for appId
     * @param appId the appId to set
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }
    /**
     * getter method for secret
     * @return the secret
     */
    public String getSecret() {
        return secret;
    }
    /**
     * setter method for secret
     * @param secret the secret to set
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }
    /**
     * getter method for token
     * @return the token
     */
    public String getToken() {
        return token;
    }
    /**
     * setter method for token
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }
    /**
     * getter method for expires
     * @return the expires
     */
    public Long getExpires() {
        return expires;
    }
    /**
     * setter method for expires
     * @param expires the expires to set
     */
    public void setExpires(Long expires) {
        this.expires = expires;
    }
}
