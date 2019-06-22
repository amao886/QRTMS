package com.ycg.ksh.entity.common.wechat.message.event;

public class LocationReportEvent extends CommonEvent {

	private static final long serialVersionUID = 1591429265111593224L;
	
	private String latitude;//地理位置纬度
	private String longitude;//地理位置经度
	private String precision;//地理位置精度
	
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getPrecision() {
		return precision;
	}
	public void setPrecision(String precision) {
		this.precision = precision;
	}	
}
