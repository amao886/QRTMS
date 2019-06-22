package com.ycg.ksh.core.driver.domain.model;

import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.encrypt.MD5;
import com.ycg.ksh.core.common.domain.Model;

import java.util.Objects;
import java.util.Optional;

/**
 *  司机车辆值对象
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/03 0003
 */
public class CompanyDriverCar extends Model {

    private String id;//编号

    private Long driverKey; //司机标识-------所属企业司机的唯一标识
    private Integer type;//车型-------厢式、板车...（车长载重等可能也应该是值对象，同时也只能选不能改）
    private Float length; //车长-------车辆长度，4.2米、6.2米、7.2米.... 单位是不是都是 米
    private Float loadValue;//载重-------限载最大重量，5吨、8吨、10吨.... 单位是不是都是 吨
    private String  license;//车牌-------沪A00001

    public CompanyDriverCar() {
    }

    public CompanyDriverCar(Integer type, Float length, Float loadValue, String license) {
        this.setType(type);
        this.setLength(length);
        this.setLoadValue(loadValue);
        this.setLicense(license);
    }

    public CompanyDriverCar(Long driverKey, Integer type, Float length, Float loadValue, String license) {
        this.setDriverKey(driverKey);
        this.setType(type);
        this.setLength(length);
        this.setLoadValue(loadValue);
        this.setLicense(license);
    }
    public CompanyDriverCar modify(Long driverKey){
        return new CompanyDriverCar(driverKey, type, length, loadValue, license);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyDriverCar driverCar = (CompanyDriverCar) o;
        return Objects.equals(driverKey, driverCar.driverKey) && Objects.equals(type, driverCar.type) && Objects.equals(length, driverCar.length) && Objects.equals(loadValue, driverCar.loadValue) && Objects.equals(license, driverCar.license);
    }

    @Override
    public int hashCode() {
        return Objects.hash(driverKey, type, length, loadValue, license);
    }

    public void setDriverKey(Long driverKey) {
        this.driverKey = driverKey;
    }

    private void setType(Integer type) {
        Assert.notBlank(type, "车辆类型不能为空");
        this.type = type;
    }

    private void setLength(Float length) {
        Assert.notBlank(length, "车辆长度不能为空");
        this.length = length;
    }

    private void setLoadValue(Float loadValue) {
        this.loadValue = Optional.ofNullable(loadValue).orElse(0F);
    }

    private void setLicense(String license) {
        Assert.notBlank(license, "车牌不能为空");
        this.license = license;
    }

    protected void setId(String id) {
        this.id = id;
    }

    protected String getId() {
        return Optional.ofNullable(id).orElse(MD5.encrypt(driverKey +"#"+ type +"#"+ length +"#"+ loadValue +"#"+ license));
    }

    public Long getDriverKey() {
        return driverKey;
    }

    public Integer getType() {
        return type;
    }

    public Float getLength() {
        return length;
    }

    public Float getLoadValue() {
        return loadValue;
    }

    public String getLicense() {
        return license;
    }
}
