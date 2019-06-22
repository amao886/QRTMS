package com.ycg.ksh.core.driver.application.dto;

import com.ycg.ksh.common.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 司机等货信息数据传输对象
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/04 0004
 */
public class DriverAwaitInfoDto  extends BaseEntity {

    private Long awaitKey;//标识
    private Long driverKey;//司机标识-------所属司机的唯一标识
    private String driverName;//司机名称
    private String driverPhone;//司机电话
    private LocalDate startTime;//等货开始时间-------时间的精确度
    private LocalDateTime releaseTime;//发布时间-------发布等货信息的时间，系统自动记录为系统当前时间，精确到时分秒？
    private Integer status;//状态-------有效、失效...

    private Integer carType;//车型-------厢式、板车...（车长载重等可能也应该是值对象，同时也只能选不能改）
    private Float length; //车长-------车辆长度，4.2米、6.2米、7.2米.... 单位是不是都是 米
    private Float loadValue;//载重-------限载最大重量，5吨、8吨、10吨.... 单位是不是都是 吨
    private String  license;//车牌-------沪A00001

    private Integer routeType;//路线类型-------长途、短途
    private String start; //路线起点-------省市区，三级行政区划格式
    private String end;//路线终点-------省市区，三级行政区划格式

    public Long getAwaitKey() {
        return awaitKey;
    }

    public void setAwaitKey(Long awaitKey) {
        this.awaitKey = awaitKey;
    }

    public Long getDriverKey() {
        return driverKey;
    }

    public void setDriverKey(Long driverKey) {
        this.driverKey = driverKey;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(LocalDateTime releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCarType() {
        return carType;
    }

    public void setCarType(Integer carType) {
        this.carType = carType;
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

    public Integer getRouteType() {
        return routeType;
    }

    public void setRouteType(Integer routeType) {
        this.routeType = routeType;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
