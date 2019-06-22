package com.ycg.ksh.entity.common.wechat.message.common;

public class LocationMessage extends CommonMessage {

	private static final long serialVersionUID = -3559108409098129455L;
	
	private String x;//地理位置维度
	private String y;//地理位置经度
	private String scale;//地图缩放大小
	private String label;//地理位置信息
	
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
}
