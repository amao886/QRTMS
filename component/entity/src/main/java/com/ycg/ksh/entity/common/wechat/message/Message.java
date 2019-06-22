package com.ycg.ksh.entity.common.wechat.message;

import com.ycg.ksh.common.entity.BaseEntity;

public abstract class Message extends BaseEntity {

	private static final long serialVersionUID = -7817073801278153263L;

	protected String msgId;//消息编号
	protected String toUserName;//开发者微信号
	protected String fromUserName;//发送方帐号（一个OpenID）
	protected Long createTime;//消息创建时间 （整型）
	protected String msgType;//消息类型(text,image,voice,video,location,link)

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
}
