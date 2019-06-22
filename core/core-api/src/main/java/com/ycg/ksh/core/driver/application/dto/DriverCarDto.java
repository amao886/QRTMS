package com.ycg.ksh.core.driver.application.dto;

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 司机车辆信息数据传输对象
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/03 0003
 */
public class DriverCarDto extends BaseEntity {

    private Integer type;//车型-------厢式、板车...（车长载重等可能也应该是值对象，同时也只能选不能改）
    private Float length; //车长-------车辆长度，4.2米、6.2米、7.2米.... 单位是不是都是 米
    private Float loadValue;//载重-------限载最大重量，5吨、8吨、10吨.... 单位是不是都是 吨
    private String  license;//车牌-------沪A00001

    public DriverCarDto() {
    }

    public DriverCarDto(Integer type, Float length, Float loadValue, String license) {
        this.type = type;
        this.length = length;
        this.loadValue = loadValue;
        this.license = license;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Float getLength() {
        return length;
    }

    public void setLength(Float length) {
        this.length = length;
    }

    public Float getLoadValue() {
        return loadValue;
    }

    public void setLoadValue(Float loadValue) {
        this.loadValue = loadValue;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
