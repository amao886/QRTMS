package com.ycg.ksh.entity.adapter;

import com.ycg.ksh.common.entity.BaseEntity;

public class AutoMapLocation extends BaseEntity {

	private static final long serialVersionUID = 329352118762479060L;
	
	private String formatAddress;//结构化地址
	private String adcode;//邮政编码
    private String province;//省/直辖市
	private String city;//市
    private String district;//区
    private String street;//街道
    private String number;//门牌
	private String latitude;//纬度
	private String longitude;//经度

	private String address;
	
	
	/**
	 * getter method for formatAddress
	 * @return the formatAddress
	 */
	public String getFormatAddress() {
		return formatAddress;
	}

	/**
	 * setter method for formatAddress
	 * @param formatAddress the formatAddress to set
	 */
	public void setFormatAddress(String formatAddress) {
		this.formatAddress = formatAddress;
	}

	/**
	 * getter method for adcode
	 * @return the adcode
	 */
	public String getAdcode() {
		return adcode;
	}

	/**
	 * setter method for adcode
	 * @param adcode the adcode to set
	 */
	public void setAdcode(String adcode) {
		this.adcode = adcode;
	}

	/**
	 * getter method for province
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * setter method for province
	 * @param province the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * getter method for city
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * setter method for city
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * getter method for district
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * setter method for district
	 * @param district the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	/**
	 * getter method for street
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * setter method for street
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * getter method for number
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * setter method for number
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * getter method for latitude
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}
	/**
	 * setter method for latitude
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	/**
	 * getter method for longitude
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}
	/**
	 * setter method for longitude
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
