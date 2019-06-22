package com.ycg.ksh.entity.common.wechat.message.common;

public class VideoMessage extends CommonMessage {

	private static final long serialVersionUID = -6562346587646164688L;
	
	private String mediaId;//视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
	private String thumbMediaId;//视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
	
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public String getThumbMediaId() {
		return thumbMediaId;
	}
	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}
	
}
