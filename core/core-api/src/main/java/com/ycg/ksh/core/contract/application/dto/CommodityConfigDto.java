package com.ycg.ksh.core.contract.application.dto;

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 合同物料配置
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/27 0027
 */
public class CommodityConfigDto extends BaseEntity {

    private String commodityCode;//物料编号--------------物料的编号
    private String commodityName; //物料名称--------------物料的名称
    private Double unitWeight; //单位重量--------------一个单位的重量
    private Double unitVolume; //单位体积--------------一个单位的体积
    private Integer fareType; //计价方式--------------计价方式(重量、体积、件数)

    public CommodityConfigDto() {
    }

    public CommodityConfigDto(String commodityCode, String commodityName, Double unitWeight, Double unitVolume, Integer fareType) {
        this.commodityCode = commodityCode;
        this.commodityName = commodityName;
        this.unitWeight = unitWeight;
        this.unitVolume = unitVolume;
        this.fareType = fareType;
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

    public Double getUnitWeight() {
        return unitWeight;
    }

    public void setUnitWeight(Double unitWeight) {
        this.unitWeight = unitWeight;
    }

    public Double getUnitVolume() {
        return unitVolume;
    }

    public void setUnitVolume(Double unitVolume) {
        this.unitVolume = unitVolume;
    }

    public Integer getFareType() {
        return fareType;
    }

    public void setFareType(Integer fareType) {
        this.fareType = fareType;
    }
}
