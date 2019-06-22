package com.ycg.ksh.entity.common.wechat.message.event;

import com.ycg.ksh.entity.common.wechat.WeChatConstant;

public class SubscribeEvent extends OrdinaryEvent {

	private static final long serialVersionUID = -1308120982147334641L;

	protected String ticket;//二维码的ticket，可用来换取二维码图片
	private String parameter;//带参二维码扫码时的参数值

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	
	public boolean subscribe() {
		return WeChatConstant.MSGEVENT_UNSUBSCRIBE.equals(eventKey) ? false : true;
	}
}
