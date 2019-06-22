package com.ycg.ksh.service.core.entity.service.adventive;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.constant.LocationEvent;

public class AdventiveTrack extends BaseEntity {

    private Double longitude;//经度
    private Double latitude;//纬度
    private String location;//位置地址
    private String reportTime;//上报时间

    private String reporterName;//上报人微信昵称
    private String reporterMobile;//上报人手机号


    private Integer reportCategory;//类型(1:扫码上报)

    public AdventiveTrack() {
    }

    public AdventiveTrack(Double longitude, Double latitude, String location, String reportTime, String reporterName, String reporterMobile) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.location = location;
        this.reportTime = reportTime;
        this.reporterName = reporterName;
        this.reporterMobile = reporterMobile;
        this.reportCategory = LocationEvent.SCANCODE.getCode();
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getReporterMobile() {
        return reporterMobile;
    }

    public void setReporterMobile(String reporterMobile) {
        this.reporterMobile = reporterMobile;
    }

    public Integer getReportCategory() {
        return reportCategory;
    }

    public void setReportCategory(Integer reportCategory) {
        this.reportCategory = reportCategory;
    }
}
