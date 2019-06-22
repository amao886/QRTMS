package com.ycg.ksh.entity.persistent.plan;

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`T_PLAN_COMMODITY`")
public class PlanCommodity extends BaseEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "`ID`")
    private Long id;

    /**
     * 计划编号
     */
    @Column(name = "`PLAN_ID`")
    private Long planId;

    /**
     * 物料编号
     */
    @Column(name = "`COMMODITY_NO`")
    private String commodityNo;

    /**
     * 物料名称
     */
    @Column(name = "`COMMODITY_NAME`")
    private String commodityName;

    /**
     * 数量(件)
     */
    @Column(name = "`QUANTITY`")
    private Integer quantity;

    /**
     * 箱数(箱)
     */
    @Column(name = "`BOX_COUNT`")
    private Integer boxCount;

    /**
     * 体积(立方米)
     */
    @Column(name = "`VOLUME`")
    private Double volume;

    /**
     * 重量(千克)
     */
    @Column(name = "`WEIGHT`")
    private Double weight;

    /**
     * 备注
     */
    @Column(name = "`REMARK`")
    private String remark;

    /**
     * 创建人
     */
    @Column(name = "`CREATE_USER_ID`")
    private Integer createUserId;

    /**
     * 创建时间
     */
    @Column(name = "`CREATE_TIME`")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "`UPDATE_TIME`")
    private Date updateTime;

    /**
     * 物料型号
     */
    @Column(name = "`COMMODITY_TYPE`")
    private String commodityType;

    /**
     * 物料单位
     */
    @Column(name = "`COMMODITY_UNIT`")
    private String commodityUnit;

    public PlanCommodity() {
    }

    public PlanCommodity(Long planId) {
        this.planId = planId;
    }

    /**
     * 获取编号
     *
     * @return ID - 编号
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置编号
     *
     * @param id 编号
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取计划编号
     *
     * @return PLAN_ID - 计划编号
     */
    public Long getPlanId() {
        return planId;
    }

    /**
     * 设置计划编号
     *
     * @param planId 计划编号
     */
    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    /**
     * 获取物料编号
     *
     * @return COMMODITY_NO - 物料编号
     */
    public String getCommodityNo() {
        return commodityNo;
    }

    /**
     * 设置物料编号
     *
     * @param commodityNo 物料编号
     */
    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
    }

    /**
     * 获取物料名称
     *
     * @return COMMODITY_NAME - 物料名称
     */
    public String getCommodityName() {
        return commodityName;
    }

    /**
     * 设置物料名称
     *
     * @param commodityName 物料名称
     */
    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    /**
     * 获取数量(件)
     *
     * @return QUANTITY - 数量(件)
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 设置数量(件)
     *
     * @param quantity 数量(件)
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * 获取箱数(箱)
     *
     * @return BOX_COUNT - 箱数(箱)
     */
    public Integer getBoxCount() {
        return boxCount;
    }

    /**
     * 设置箱数(箱)
     *
     * @param boxCount 箱数(箱)
     */
    public void setBoxCount(Integer boxCount) {
        this.boxCount = boxCount;
    }

    /**
     * 获取体积(立方米)
     *
     * @return VOLUME - 体积(立方米)
     */
    public Double getVolume() {
        return volume;
    }

    /**
     * 设置体积(立方米)
     *
     * @param volume 体积(立方米)
     */
    public void setVolume(Double volume) {
        this.volume = volume;
    }

    /**
     * 获取重量(千克)
     *
     * @return WEIGHT - 重量(千克)
     */
    public Double getWeight() {
        return weight;
    }

    /**
     * 设置重量(千克)
     *
     * @param weight 重量(千克)
     */
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    /**
     * 获取备注
     *
     * @return REMARK - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取创建人
     *
     * @return CREATE_USER_ID - 创建人
     */
    public Integer getCreateUserId() {
        return createUserId;
    }

    /**
     * 设置创建人
     *
     * @param createUserId 创建人
     */
    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * 获取创建时间
     *
     * @return CREATE_TIME - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return UPDATE_TIME - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取物料型号
     *
     * @return COMMODITY_TYPE - 物料型号
     */
    public String getCommodityType() {
        return commodityType;
    }

    /**
     * 设置物料型号
     *
     * @param commodityType 物料型号
     */
    public void setCommodityType(String commodityType) {
        this.commodityType = commodityType;
    }

    /**
     * 获取物料单位
     *
     * @return COMMODITY_UNIT - 物料单位
     */
    public String getCommodityUnit() {
        return commodityUnit;
    }

    /**
     * 设置物料单位
     *
     * @param commodityUnit 物料单位
     */
    public void setCommodityUnit(String commodityUnit) {
        this.commodityUnit = commodityUnit;
    }
}