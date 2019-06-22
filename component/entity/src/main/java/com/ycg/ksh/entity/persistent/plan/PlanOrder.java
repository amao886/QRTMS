package com.ycg.ksh.entity.persistent.plan;

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`T_PLAN_ORDER`")
public class PlanOrder extends BaseEntity {
    /**
     * 订单编号
     */
    @Id
    @Column(name = "`ID`")
    protected Long id;

    /**
     * 计划单号
     */
    @Column(name = "`PLAN_NO`")
    protected String planNo;

    /**
     * 所属企业编号
     */
    @Column(name = "`COMPANY_KEY`")
    protected Long companyKey;

    /**
     * 发货方客户编号
     */
    @Column(name = "`SHIPPER_ID`")
    protected Long shipperId;

    /**
     * 收货方客户编号
     */
    @Column(name = "`RECEIVE_ID`")
    protected Long receiveId;

    /**
     * 收货人
     */
    @Column(name = "`RECEIVER_NAME`")
    protected String receiverName;

    /**
     * 联系方式
     */
    @Column(name = "`RECEIVER_CONTACT`")
    protected String receiverContact;

    /**
     * 收货地址
     */
    @Column(name = "`RECEIVE_ADDRESS`")
    protected String receiveAddress;

    /**
     * 运输路线
     */
    @Column(name = "`TRANSPORT_ROUTE`")
    protected String transportRoute;//运输路线
    /**
     * 状态(待定)
     */
    @Column(name = "`FETTLE`")
    protected Integer fettle;

    /**
     * 备注
     */
    @Column(name = "`REMARK`")
    protected String remark;

    /**
     * 客户订单编号
     */
    @Column(name = "`ORDER_NO`")
    protected String orderNo;

    /**
     * 发货时间
     */
    @Column(name = "`DELIVERY_TIME`")
    protected Date deliveryTime;

    /**
     * 要求到货时间
     */
    @Column(name = "`ARRIVAL_TIME`")
    protected Date arrivalTime;

    /**
     * 要求提货时间
     */
    @Column(name = "`COLLECT_TIME`")
    protected Date collectTime;

    /**
     * 创建时间
     */
    @Column(name = "`CREATE_TIME`")
    protected Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "`UPDATE_TIME`")
    protected Date updateTime;

    /**
     * 生成的发货订单编号
     */
    @Column(name = "`ORDER_KEY`")
    protected Long orderKey;
    /**
     * 创建人用户编号
     */
    @Column(name = "`USER_KEY`")
    protected Integer userKey;

    /**
     * 派车状态 0 未派车  1 已派车
     */
    @Column(name = "`CAR_STATUS`")
    protected  Integer carStatus;

    /**
     * 派车时间
     */
    @Column(name = "`CAR_TIME`")
    protected Date carTime;

    public PlanOrder() {
    }

    public PlanOrder(Long id, Long orderKey) {
        this.id = id;
        this.orderKey = orderKey;
    }

    public PlanOrder(Long companyKey, String planNo) {
        this.planNo = planNo;
        this.companyKey = companyKey;
    }

    public PlanOrder(Integer carStatus, Date carTime) {
        this.carStatus = carStatus;
        this.carTime = carTime;
    }

    public Integer getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(Integer carStatus) {
        this.carStatus = carStatus;
    }

    /**
     * 获取订单编号
     *
     * @return ID - 订单编号
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置订单编号
     *
     * @param id 订单编号
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取计划单号
     *
     * @return PLAN_NO - 计划单号
     */
    public String getPlanNo() {
        return planNo;
    }

    /**
     * 设置计划单号
     *
     * @param planNo 计划单号
     */
    public void setPlanNo(String planNo) {
        this.planNo = planNo;
    }

    /**
     * 获取所属企业编号
     *
     * @return COMPANY_KEY - 所属企业编号
     */
    public Long getCompanyKey() {
        return companyKey;
    }

    /**
     * 设置所属企业编号
     *
     * @param companyKey 所属企业编号
     */
    public void setCompanyKey(Long companyKey) {
        this.companyKey = companyKey;
    }

    /**
     * 获取发货方客户编号
     *
     * @return SHIPPER_ID - 发货方客户编号
     */
    public Long getShipperId() {
        return shipperId;
    }

    /**
     * 设置发货方客户编号
     *
     * @param shipperId 发货方客户编号
     */
    public void setShipperId(Long shipperId) {
        this.shipperId = shipperId;
    }

    /**
     * 获取收货方客户编号
     *
     * @return RECEIVE_ID - 收货方客户编号
     */
    public Long getReceiveId() {
        return receiveId;
    }

    /**
     * 设置收货方客户编号
     *
     * @param receiveId 收货方客户编号
     */
    public void setReceiveId(Long receiveId) {
        this.receiveId = receiveId;
    }

    /**
     * 获取收货人
     *
     * @return RECEIVER_NAME - 收货人
     */
    public String getReceiverName() {
        return receiverName;
    }

    /**
     * 设置收货人
     *
     * @param receiverName 收货人
     */
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    /**
     * 获取联系方式
     *
     * @return RECEIVER_CONTACT - 联系方式
     */
    public String getReceiverContact() {
        return receiverContact;
    }

    /**
     * 设置联系方式
     *
     * @param receiverContact 联系方式
     */
    public void setReceiverContact(String receiverContact) {
        this.receiverContact = receiverContact;
    }

    /**
     * 获取收货地址
     *
     * @return RECEIVE_ADDRESS - 收货地址
     */
    public String getReceiveAddress() {
        return receiveAddress;
    }

    /**
     * 设置收货地址
     *
     * @param receiveAddress 收货地址
     */
    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }


    /**
     * 获取状态(待定)
     *
     * @return FETTLE - 状态(待定)
     */
    public Integer getFettle() {
        return fettle;
    }

    /**
     * 设置状态(待定)
     *
     * @param fettle 订单状态(待定)
     */
    public void setFettle(Integer fettle) {
        this.fettle = fettle;
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
     * 获取客户订单编号
     *
     * @return ORDER_NO - 客户订单编号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置客户订单编号
     *
     * @param orderNo 客户订单编号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取发货时间
     *
     * @return DELIVERY_TIME - 发货时间
     */
    public Date getDeliveryTime() {
        return deliveryTime;
    }

    /**
     * 设置发货时间
     *
     * @param deliveryTime 发货时间
     */
    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    /**
     * 获取要求到货时间
     *
     * @return ARRIVAL_TIME - 要求到货时间
     */
    public Date getArrivalTime() {
        return arrivalTime;
    }

    /**
     * 设置要求到货时间
     *
     * @param arrivalTime 要求到货时间
     */
    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
     * 获取要求提货时间
     *
     * @return COLLECT_TIME - 要求提货时间
     */
    public Date getCollectTime() {
        return collectTime;
    }

    /**
     * 设置要求提货时间
     *
     * @param collectTime 要求提货时间
     */
    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
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
     * 获取生成的发货订单编号
     *
     * @return ORDER_KEY - 生成的发货订单编号
     */
    public Long getOrderKey() {
        return orderKey;
    }

    /**
     * 设置生成的发货订单编号
     *
     * @param orderKey 生成的发货订单编号
     */
    public void setOrderKey(Long orderKey) {
        this.orderKey = orderKey;
    }

    public String getTransportRoute() {
        return transportRoute;
    }

    public void setTransportRoute(String transportRoute) {
        this.transportRoute = transportRoute;
    }

    public Integer getUserKey() {
        return userKey;
    }

    public void setUserKey(Integer userKey) {
        this.userKey = userKey;
    }

    public Date getCarTime() {
        return carTime;
    }

    public void setCarTime(Date carTime) {
        this.carTime = carTime;
    }
}