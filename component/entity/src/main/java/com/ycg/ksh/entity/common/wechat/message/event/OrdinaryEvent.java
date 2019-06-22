package com.ycg.ksh.entity.common.wechat.message.event;

public class OrdinaryEvent extends CommonEvent {
	
	private static final long serialVersionUID = -467611872271168039L;

	protected String eventKey;
	

	public String getEventKey() {
		return eventKey;
	}
	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
}
