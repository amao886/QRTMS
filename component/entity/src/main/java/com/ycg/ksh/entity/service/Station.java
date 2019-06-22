package com.ycg.ksh.entity.service;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/26
 */

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 站点
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/26
 */
public class Station extends BaseEntity {

    private String startStation;
    private String simpleStartStation;
    private String endStation;
    private String simpleEndStation;
    private Integer assignFettle;

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getSimpleStartStation() {
        return simpleStartStation;
    }

    public void setSimpleStartStation(String simpleStartStation) {
        this.simpleStartStation = simpleStartStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public String getSimpleEndStation() {
        return simpleEndStation;
    }

    public void setSimpleEndStation(String simpleEndStation) {
        this.simpleEndStation = simpleEndStation;
    }

    public Integer getAssignFettle() {
        return assignFettle;
    }

    public void setAssignFettle(Integer assignFettle) {
        this.assignFettle = assignFettle;
    }
}
