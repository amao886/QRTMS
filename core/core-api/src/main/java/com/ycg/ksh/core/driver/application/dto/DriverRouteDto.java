package com.ycg.ksh.core.driver.application.dto;

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 司机路线信息数据传输对象
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/03 0003
 */
public class DriverRouteDto extends BaseEntity {

    private Integer type;//路线类型-------长途、短途
    private String[] starts; //路线起点-------省市区，三级行政区划格式
    private String[] ends;//路线终点-------省市区，三级行政区划格式

    public DriverRouteDto() {
    }

    public DriverRouteDto(Integer type, String[] starts, String[] ends) {
        this.type = type;
        this.starts = starts;
        this.ends = ends;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String[] getStarts() {
        return starts;
    }

    public void setStarts(String[] starts) {
        this.starts = starts;
    }

    public String[] getEnds() {
        return ends;
    }

    public void setEnds(String[] ends) {
        this.ends = ends;
    }
}
