package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Optional;

@Table(name = "`T_ORDER_COMMODITY`")
public class OrderCommodity extends BaseEntity {
    /**
     * 订单明细编号
     */
    @Id
    @Column(name = "`ID`")
    private Long id;

    /**
     * 订单编号
     */
    @Column(name = "`ORDER_ID`")
    private Long orderId;

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
     * 物料类型
     */
    @Column(name = "`COMMODITY_TYPE`")
    private String commodityType;

    /**
     * 单位
     */
    @Column(name = "`COMMODITY_UNIT`")
    private String commodityUnit;
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

    public OrderCommodity() {
    }

    public boolean validate(){
        return getBoxCount() + getQuantity() + getVolume() + getWeight() > 0;
    }

    public OrderCommodity(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取订单明细编号
     *
     * @return ID - 订单明细编号
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置订单明细编号
     *
     * @param id 订单明细编号
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取订单编号
     *
     * @return ORDER_ID - 订单编号
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 设置订单编号
     *
     * @param orderId 订单编号
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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
        return Optional.ofNullable(quantity).orElse(0);
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
        return Optional.ofNullable(boxCount).orElse(0);
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
        return Optional.ofNullable(volume).map(v -> {
            return new Double(new DecimalFormat("#.00").format(v));
        }).orElse(0.0);
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
        return Optional.ofNullable(weight).map(v -> {
            return new Double(new DecimalFormat("#.00").format(v));
        }).orElse(0.0);
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

    public String getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(String commodityType) {
        this.commodityType = commodityType;
    }

    public String getCommodityUnit() {
        return commodityUnit;
    }

    public void setCommodityUnit(String commodityUnit) {
        this.commodityUnit = commodityUnit;
    }
}