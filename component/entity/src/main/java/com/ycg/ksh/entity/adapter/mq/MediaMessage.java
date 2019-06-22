package com.ycg.ksh.entity.adapter.mq;/**
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

    public MediaMessage(String messageType, String messageKey, Object object) {
        this.messageType = messageType;
        this.messageKey = messageKey;
        this.object = object;
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
}
