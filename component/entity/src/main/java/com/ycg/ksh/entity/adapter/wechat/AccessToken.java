/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-25 14:32:17
 */
package com.ycg.ksh.entity.adapter.wechat;

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 微信授权CODE换取的数据
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-25 14:32:17
 */
public class AccessToken extends BaseEntity {
    
    private static final long serialVersionUID = -4626443001016642060L;
    
    private String appId;
    private String secret;
    private String accessToken;
    private String refreshToken;
    private String unionId;
    private String openId;
    private String scope;
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
    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public String getUnionId() {
        return unionId;
    }
    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }
    public String getOpenId() {
        return openId;
    }
    public void setOpenId(String openId) {
        this.openId = openId;
    }
    public String getScope() {
        return scope;
    }
    public void setScope(String scope) {
        this.scope = scope;
    }
    public Long getExpires() {
        return expires;
    }
    public void setExpires(Long expires) {
        this.expires = expires;
    }
}
