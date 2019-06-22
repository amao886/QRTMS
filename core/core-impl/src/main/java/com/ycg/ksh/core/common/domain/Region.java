package com.ycg.ksh.core.common.domain;

import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.core.common.domain.Model;

import java.util.Optional;

/**
 * 地区
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/27 0027
 */
public class Region extends Model {

    private String province;
    private String city;
    private String district;

    public Region() {
    }

    public Region(String province, String city, String district) {
        this.province = Optional.ofNullable(province).orElse(StringUtils.EMPTY);
        this.city = Optional.ofNullable(city).orElse(StringUtils.EMPTY);
        this.district = Optional.ofNullable(district).orElse(StringUtils.EMPTY);
    }

    public String address() {
        return province + city + district;
    }

    public String address(int l) {
        if (l == 1) {
            return province;
        } else if (l == 2) {
            return province + city;
        } else {
            return address();
        }
    }

    private void setProvince(String province) {
        this.province = province;
    }

    private void setCity(String city) {
        this.city = city;
    }

    private void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }
}
