package com.ycg.ksh.entity.common.wechat.message.event;

import com.ycg.ksh.entity.common.wechat.message.Message;

public class CommonEvent extends Message {

	private static final long serialVersionUID = -5631357336176011551L;
	
	protected String event;//事件类型，SCAN
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
}
