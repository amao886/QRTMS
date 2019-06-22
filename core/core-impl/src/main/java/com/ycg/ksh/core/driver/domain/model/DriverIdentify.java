package com.ycg.ksh.core.driver.domain.model;

import com.ycg.ksh.core.common.domain.Model;

import java.util.Objects;
import java.util.Optional;

/**
 *  司机标识值对象
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/30 0030
 */
public class DriverIdentify extends Model {

    private Long driverKey;

    public DriverIdentify() {
    }

    public DriverIdentify(Long driverKey) {
        this.setDriverKey(driverKey);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DriverIdentify that = (DriverIdentify) o;
        return Objects.equals(driverKey, that.driverKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(driverKey);
    }

    public Long getDriverKey() {
        return driverKey;
    }

    public void setDriverKey(Long driverKey) {
        this.driverKey = Optional.of(driverKey).orElseThrow(()-> new IllegalArgumentException("driver key can not be null"));
    }

}
