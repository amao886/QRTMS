package com.ycg.ksh.core.contract.domain.model;

import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.encrypt.MD5;
import com.ycg.ksh.core.common.domain.Model;
import com.ycg.ksh.core.common.domain.Region;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 零担计价配置
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/27 0027
 */
public class FragmentaryValuation extends Model implements Comparable<FragmentaryValuation> {

    private long id;
    private ValidityPeriod period;//计价有效期，计价生效的时间段,不能为空，同一份合同中，多个计价配置的计价有效期不能有重叠的时间
    private int commodityCategory;//货物类型--------------不限(数量)、重货(重量)、轻货(体积),不能为空
    private int fareType;//计价维度--------------按重量、体积、件数三者取其一,不能为空
    private int commodityUnit;//单位--------------按数量计价时为件、重量计价时为吨或者千克、按体积计价时为立方

    private CapacityInterval[] intervals;
    private String intervalStr;

    private List<FragmentaryUnitPrice> units;//单价配置
    private Map<String, FragmentaryUnitPrice> prices;//单价配置

    public FragmentaryValuation() {
    }

    public FragmentaryValuation(ValidityPeriod period, int commodityCategory, int fareType, int commodityUnit, CapacityInterval[] intervals, List<FragmentaryUnitPrice> units) {
        this.setPeriod(period);
        this.setCommodityCategory(commodityCategory);
        this.setFareType(fareType);
        this.setCommodityUnit(commodityUnit);
        this.setIntervals(intervals);
        this.setUnits(units);
    }

    @Override
    public int compareTo(FragmentaryValuation o) {
        return toString().compareTo(o.toString());
    }

    /**
     * 比较是否相同
     *
     * @param valuation     计价配置信息
     * @param excludePeriod 是否要排除有效期
     * @return true:相同， false:不相同
     */
    protected boolean identical(FragmentaryValuation valuation, boolean excludePeriod) {
        if (valuation != null) {
            boolean result = Objects.equals(commodityCategory, valuation.commodityCategory) && Objects.equals(fareType, valuation.fareType);
            if (!result && !excludePeriod) {
                result = Objects.equals(period, valuation.period);
            }
            if (result) {
                result = Arrays.equals(intervals, valuation.intervals);
            }
            if (result && (result = units != null && valuation.units != null) && (result = (units.size() - valuation.units.size() == 0))) {
                //排序
                List<FragmentaryUnitPrice> os = units.stream().sorted().collect(Collectors.toList()), ns = valuation.units.stream().sorted().collect(Collectors.toList());
                for (int i = 0; i < os.size(); i++) {
                    if ((result = os.get(i).identical(ns.get(i)))) {
                        break;
                    }
                }
            }
            return result;
        }
        return false;
    }

    private String mappingKey(String origin, String destination) {
        return origin + "-" + destination;
    }

    @Override
    public String toString() {
        return "category:" + commodityCategory + ", fareType:" + fareType + ", period:" + period.toString();
    }

    private int intervalIndex(double number) {
        int index = -1;
        for (int i = 0; i < intervals.length; i++) {
            if (intervals[i].include(number)) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * 计算费用
     */
    public double expense(Region origin, Region destination, Double number) {
        int index = intervalIndex(number);
        if (index < 0) {
            throw new ParameterException("计价配置中不包含[" + number + "]");
        }
        String originString = origin.address();
        FragmentaryUnitPrice fragmentaryUnitPrice = null;
        //目的地的匹配优先级：三级 > 二级 > 一级
        for (String mappingKey : new String[]{mappingKey(originString, destination.address(3)), mappingKey(originString, destination.address(2)), mappingKey(originString, destination.address(1))}) {
            fragmentaryUnitPrice = prices.get(mappingKey);
            if (fragmentaryUnitPrice != null) {
                break;
            }
        }
        if (fragmentaryUnitPrice == null) {
            throw new ParameterException("单价配置中不包含[" + mappingKey(originString, destination.address()) + "]");
        }
        return fragmentaryUnitPrice.price(index) * number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FragmentaryValuation)) return false;
        FragmentaryValuation valuation = (FragmentaryValuation) o;
        return commodityCategory == valuation.commodityCategory && fareType == valuation.fareType && commodityUnit == valuation.commodityUnit && Objects.equals(period, valuation.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(period, commodityCategory, fareType, commodityUnit);
    }

    private long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public ValidityPeriod getPeriod() {
        return period;
    }

    public int getCommodityCategory() {
        return commodityCategory;
    }

    public int getFareType() {
        return fareType;
    }

    private void setPeriod(ValidityPeriod period) {
        this.period = period;
    }

    private void setCommodityCategory(int commodityCategory) {
        this.commodityCategory = commodityCategory;
    }

    private void setFareType(int fareType) {
        this.fareType = fareType;
    }

    public int getCommodityUnit() {
        return commodityUnit;
    }

    private void setCommodityUnit(int commodityUnit) {
        this.commodityUnit = commodityUnit;
    }

    private void setUnits(List<FragmentaryUnitPrice> unitPrices) {
        Assert.notEmpty(unitPrices, "单价配置不能为空");
        this.units = unitPrices;
        this.prices = units.stream().collect(Collectors.toMap(p -> mappingKey(p.getOrigin(), p.getDestination()), p -> p));
    }

    public List<FragmentaryUnitPrice> getUnits() {
        return units;
    }

    public CapacityInterval[] getIntervals() {
        return intervals;
    }

    private void setIntervals(CapacityInterval[] intervals) {
        this.intervals = intervals;
        this.intervalStr = Arrays.stream(intervals).map(CapacityInterval::toString).collect(Collectors.joining(","));
    }

    public String getIntervalStr() {
        return intervalStr;
    }

    public void setIntervalStr(String intervalStr) {
        Assert.notBlank(intervalStr, "区间配置数据不能为空");
        String[] items = intervalStr.split(",");
        if (items == null || items.length < 1) {
            throw new ParameterException("区间数据配置有误");
        }
        this.intervalStr = intervalStr;
        this.intervals = Arrays.stream(items).map(CapacityInterval::new).toArray(CapacityInterval[]::new);
    }
}
