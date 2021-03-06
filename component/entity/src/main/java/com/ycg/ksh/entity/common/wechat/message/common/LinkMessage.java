package com.ycg.ksh.entity.common.wechat.message.common;

public class LinkMessage extends CommonMessage {

	private static final long serialVersionUID = -1135886556144076372L;
	
	private String title;//消息标题
	private String description;//消息描述
	private String url;//消息链接
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
