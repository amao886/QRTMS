package com.ycg.ksh.entity.common.wechat.message.common;

import com.ycg.ksh.entity.common.wechat.message.Message;

public class CommonMessage extends Message {

	private static final long serialVersionUID = -227509369314252167L;
	
	protected String msgId;//消息id，64位整型
	
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
}
