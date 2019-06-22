package com.ycg.ksh.common.extend.rabbitmq;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/16
 */

import com.ycg.ksh.common.entity.BaseEntity;

import java.io.Serializable;

/**
 * 消息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/16
 */
public class MediaMessage extends BaseEntity {

    private String messageType;
    private String messageKey;
    private Object object;
    private long expiration;

    public MediaMessage() {
    }
    public MediaMessage(Serializable messageKey) {
        this.messageKey = String.valueOf(messageKey);
    }
    public MediaMessage(Serializable messageKey, Object object) {
        this(messageKey);
        this.messageType = object.getClass().getName();
        this.object = object;
    }
    public MediaMessage(Serializable messageKey, Object object, long expiration) {
        this(messageKey, object);
        this.expiration = expiration;
    }

    public MediaMessage(String messageType, String messageKey, Object object) {
        this(messageKey, object);
        this.messageType = messageType;
    }
    public MediaMessage(String messageType, String messageKey, Object object, long expiration) {
        this(messageKey, object, expiration);
        this.messageType = messageType;
    }

    public String correlationKey(){
        return messageType +"#"+ messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }
}
