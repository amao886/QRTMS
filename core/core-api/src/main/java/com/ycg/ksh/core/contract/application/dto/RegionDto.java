package com.ycg.ksh.core.contract.application.dto;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.StringUtils;

import java.util.Optional;

/**
 *  地区
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2019/01/03 0003
 */
public class RegionDto extends BaseEntity {

    private String province;
    private String city;
    private String county;

    public RegionDto() {
    }

    public RegionDto(String province, String city, String county) {
        this.province = Optional.ofNullable(province).orElse(StringUtils.EMPTY);
        this.city = Optional.ofNullable(city).orElse(StringUtils.EMPTY);
        this.county = Optional.ofNullable(county).orElse(StringUtils.EMPTY);
    }
    public String address() {
        return province + city + county;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getCounty() {
        return county;
    }

}
