/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-25 14:25:41
 */
package com.ycg.ksh.entity.adapter.wechat;

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 微信JSAPI校验数据实体
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-25 14:25:41
 */
public class JsApiTicket extends BaseEntity {
    
    private static final long serialVersionUID = -719198693381576526L;
    
    private String appId;
    private String ticket;
    private String nonceStr;
    private Long timestamp;
    private String signature;
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
     * getter method for nonceStr
     * @return the nonceStr
     */
    public String getNonceStr() {
        return nonceStr;
    }
    /**
     * setter method for nonceStr
     * @param nonceStr the nonceStr to set
     */
    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }
    /**
     * getter method for timestamp
     * @return the timestamp
     */
    public Long getTimestamp() {
        return timestamp;
    }
    /**
     * setter method for timestamp
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    /**
     * getter method for signature
     * @return the signature
     */
    public String getSignature() {
        return signature;
    }
    /**
     * setter method for signature
     * @param signature the signature to set
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }
    /**
     * getter method for ticket
     * @return the ticket
     */
    public String getTicket() {
        return ticket;
    }
    /**
     * setter method for ticket
     * @param ticket the ticket to set
     */
    public void setTicket(String ticket) {
        this.ticket = ticket;
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
