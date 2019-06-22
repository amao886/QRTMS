package com.ycg.ksh.entity.common.wechat.message.common;

public class TextMessage extends CommonMessage {

	private static final long serialVersionUID = 7226672231839335861L;
	
	private String content;//文本消息内容

	public TextMessage(String content) {
		super();
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
}
