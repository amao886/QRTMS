package com.ycg.ksh.core.contract.application.dto;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.RegionUtils;

/**
 * 区间单价
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2019/01/03 0003
 */
public class IntervalPriceDto extends BaseEntity {

    private RegionDto area;// 终点城市省市区
    private Integer cityLevel;// 城市级别: 0：不限 1：一级/省会 2：二级/地级市 3：三级/县、区级
    private Double[] num;//价格

    public IntervalPriceDto() {
    }

    public IntervalPriceDto(RegionDto area, Integer cityLevel, Double[] num) {
        this.area = area;
        this.cityLevel = cityLevel;
        this.num = num;
    }

    public RegionDto getArea() {
        return area;
    }

    public void setArea(RegionDto area) {
        this.area = area;
    }

    public Integer getCityLevel() {
        return cityLevel;
    }

    public void setCityLevel(Integer cityLevel) {
        this.cityLevel = cityLevel;
    }

    public Double[] getNum() {
        return num;
    }

    public void setNum(Double[] num) {
        this.num = num;
    }
}
