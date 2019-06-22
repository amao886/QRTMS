package com.ycg.ksh.core.contract.domain.model;

import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.core.common.domain.Model;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 零担单价配置
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/27 0027
 */
public class FragmentaryUnitPrice extends Model implements Comparable<FragmentaryUnitPrice> {


    private static final String SPLIT = ",";
    private static final Pattern REGEXP = Pattern.compile("(\\d+(\\.\\d+)?)(,\\d+(\\.\\d+)?)?");

    private long id;
    private String origin;//始发地--------------发货地省市区,不能为空
    private String destination;//目的地--------------收货地省市区,不能为空
    private int citylv;//目的地城市级别--------------不限、一级省会、二级地级市、三级区县
    private int fareCategory;//计价方式--------------固定价格、阶梯价格
    /**
     * 固定价格时为单个数值，阶梯价格时为 区间单价 ，元/单位
     * 区间单价: 比如: 100,150,250 与计价配置中的区间对应
     */
    private String priceSrc;//单价，元/单位

    private Double[] prices;//区间单价

    public FragmentaryUnitPrice(String destination, int citylv, Double[] prices) {
        this.setDestination(destination);
        this.setCitylv(citylv);
        this.setFareCategory(2);//阶梯价
        this.setPrices(prices);
    }


    public FragmentaryUnitPrice() {
    }

    public FragmentaryUnitPrice(String origin, String destination, int citylv, Double[] prices) {
        this.setOrigin(origin);
        this.setDestination(destination);
        this.setCitylv(citylv);
        this.setFareCategory(2);//阶梯价
        this.setPrices(prices);
    }

    public FragmentaryUnitPrice(String destination, int citylv, int fareCategory, Double[] prices) {
        this.setDestination(destination);
        this.setCitylv(citylv);
        this.setFareCategory(fareCategory);
        this.setPrices(prices);
    }

    private void setPriceSrc(String priceSrc) {
        if (REGEXP.matcher(priceSrc).matches()) {
            prices = StringUtils.doubleArray(priceSrc);
        } else {
            Assert.notBlank(priceSrc, "区间单价配置有误)");
        }
        this.priceSrc = priceSrc;
    }

    private void setPrices(Double[] prices) {
        Assert.notNull(prices, "单价配置不能为空");
        this.prices = prices;
        priceSrc = Arrays.stream(prices).map(String::valueOf).collect(Collectors.joining(SPLIT));
    }

    public double price(int index) {
        if (index < prices.length) {
            return prices[index];
        }
        throw new ParameterException("单价配置不包含[" + index + "]");
    }

    protected boolean identical(FragmentaryUnitPrice up) {
        boolean result = this.equals(up);
        if (!result) {
            result = Arrays.equals(prices, up.prices);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FragmentaryUnitPrice)) return false;
        FragmentaryUnitPrice unitPrice = (FragmentaryUnitPrice) o;
        return citylv == unitPrice.citylv && fareCategory == unitPrice.fareCategory && Objects.equals(origin, unitPrice.origin) && Objects.equals(destination, unitPrice.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, destination, citylv, fareCategory);
    }

    @Override
    public int compareTo(FragmentaryUnitPrice o) {
        return (origin + destination).compareTo(o.origin + o.destination);
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    private void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    private void setDestination(String destination) {
        this.destination = destination;
    }

    public int getCitylv() {
        return citylv;
    }

    private void setCitylv(int citylv) {
        this.citylv = citylv;
    }

    public int getFareCategory() {
        return fareCategory;
    }

    private void setFareCategory(int fareCategory) {
        this.fareCategory = fareCategory;
    }

    public String getPriceSrc() {
        return priceSrc;
    }

    public Double[] getPrices() {
        return prices;
    }
}
