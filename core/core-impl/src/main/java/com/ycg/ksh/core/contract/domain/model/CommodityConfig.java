package com.ycg.ksh.core.contract.domain.model;

import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.encrypt.MD5;
import com.ycg.ksh.core.Registrys;
import com.ycg.ksh.core.common.domain.Model;

import java.util.Objects;

/**
 * 合同物料配置
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/27 0027
 */
public class CommodityConfig extends Model {

    private long id;
    private String commodityCode;//物料编号--------------物料的编号
    private String commodityName; //物料名称--------------物料的名称
    private double unitWeight; //单位重量--------------一个单位的重量
    private double unitVolume; //单位体积--------------一个单位的体积
    private int fareType; //计价方式--------------计价方式(重量、体积、件数)

    public CommodityConfig() {
    }

    public CommodityConfig(String commodityCode, String commodityName, double unitWeight, double unitVolume, int fareType) {
        this.setCommodityCode(commodityCode);
        this.setCommodityName(commodityName);
        this.setUnitWeight(unitWeight);
        this.setUnitVolume(unitVolume);
        this.setFareType(fareType);
    }

    public CommodityConfig(Long id, String commodityCode, String commodityName, double unitWeight, double unitVolume, int fareType) {
        this.setId(id);
        this.setCommodityCode(commodityCode);
        this.setCommodityName(commodityName);
        this.setUnitWeight(unitWeight);
        this.setUnitVolume(unitVolume);
        this.setFareType(fareType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommodityConfig)) return false;
        CommodityConfig that = (CommodityConfig) o;
        return Objects.equals(commodityCode, that.commodityCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commodityCode);
    }

    private void setId(long id) {
        this.id = id;
    }

    private void setCommodityCode(String commodityCode) {
        Assert.notBlank(commodityCode, "物料编号不能为空");
        this.commodityCode = commodityCode;
    }

    private void setCommodityName(String commodityName) {
        Assert.notBlank(commodityName, "物料名称不能为空");
        this.commodityName = commodityName;
    }

    private long getId() {
        return id;
    }

    private void setUnitWeight(double unitWeight) {
        this.unitWeight = unitWeight;
    }

    private void setUnitVolume(double unitVolume) {
        this.unitVolume = unitVolume;
    }

    private void setFareType(int fareType) {
        this.fareType = fareType;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public double getUnitWeight() {
        return unitWeight;
    }

    public double getUnitVolume() {
        return unitVolume;
    }

    public int getFareType() {
        return fareType;
    }
}
