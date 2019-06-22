package com.ycg.ksh.core.contract.search.dto;

import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.core.common.domain.Model;

import java.util.Objects;

/**
 * 合同物料配置
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/27 0027
 */
public class CommodityConfigDtoJdbc extends Model {

    private long id;
    private String commodityCode;//物料编号--------------物料的编号
    private String commodityName; //物料名称--------------物料的名称
    private double unitWeight; //单位重量--------------一个单位的重量
    private double unitVolume; //单位体积--------------一个单位的体积
    private int fareType; //计价方式--------------计价方式(重量、体积、件数)

    public CommodityConfigDtoJdbc() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public double getUnitWeight() {
        return unitWeight;
    }

    public void setUnitWeight(double unitWeight) {
        this.unitWeight = unitWeight;
    }

    public double getUnitVolume() {
        return unitVolume;
    }

    public void setUnitVolume(double unitVolume) {
        this.unitVolume = unitVolume;
    }

    public int getFareType() {
        return fareType;
    }

    public void setFareType(int fareType) {
        this.fareType = fareType;
    }
}
