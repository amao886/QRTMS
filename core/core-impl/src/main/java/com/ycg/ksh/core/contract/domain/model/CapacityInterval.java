package com.ycg.ksh.core.contract.domain.model;

import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.core.common.domain.Model;

import java.util.Objects;

/**
 * 区间
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2019/01/03 0003
 */
public class CapacityInterval extends Model {

    private Integer min;
    private Integer max;

    public CapacityInterval() {
    }

    public CapacityInterval(Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }

    public CapacityInterval(String src) {
        String[] items = src.split(":");
        if(items == null || items.length < 2){
            throw  new ParameterException("区间数据配置有误");
        }
        this.min = Integer.parseInt(items[0]);
        this.max = Integer.parseInt(items[1]);
    }


    public boolean include(double value){
        return value > min && value < max;
    }

    @Override
    public String toString() {
        return min +":"+ max;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CapacityInterval)) return false;
        CapacityInterval that = (CapacityInterval) o;
        return Objects.equals(min, that.min) && Objects.equals(max, that.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max);
    }

    public Integer getMin() {
        return min;
    }

    private void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    private void setMax(Integer max) {
        this.max = max;
    }
}
