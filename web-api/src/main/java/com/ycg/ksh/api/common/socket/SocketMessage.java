package com.ycg.ksh.api.common.socket;

import com.ycg.ksh.common.system.Globallys;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 *  websocket消息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/19 0019
 */
public class SocketMessage implements Serializable {

    private Serializable clientType;
    private Serializable uniqueKey;
    private String message;

    private String eventType;
    private Object context;

    public SocketMessage() {
    }

    public SocketMessage(Serializable clientType, Serializable uniqueKey, String eventType, Object context) {
        this.clientType = clientType;
        this.uniqueKey = uniqueKey;
        this.eventType = eventType;
        this.context = context;
        this.message = Globallys.createJsonObject().fluentPut("eventType",  eventType).fluentPut("context",  context).toJSONString();
    }

    public boolean isEmpty(){
        return  clientType == null && uniqueKey == null && StringUtils.isEmpty(message) && StringUtils.isEmpty(eventType) && context == null || !message.contains("eventType");
    }

    @Override
    public String toString() {
        return "SocketMessage{" + "clientType=" + clientType + ", uniqueKey=" + uniqueKey + ", eventType='" + eventType + '\'' + ", context='" + context + '\'' + '}';
    }

    public Serializable getClientType() {
        return clientType;
    }

    public void setClientType(Serializable clientType) {
        this.clientType = clientType;
    }

    public Serializable getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(Serializable uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Object getContext() {
        return context;
    }

    public void setContext(Object context) {
        this.context = context;
    }

}
