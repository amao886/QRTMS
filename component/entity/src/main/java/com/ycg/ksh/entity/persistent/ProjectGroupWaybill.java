package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.util.Date;

/**
 * 项目组任务
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-24 17:13:41
 */
@Table(name = "`user_Bill_bCode_view`")
public class ProjectGroupWaybill extends BaseEntity {

	private static final long serialVersionUID = -6930390439867286170L;

	/**
     * 主键id
     */
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 订单摘要
     */
    @Column(name = "`order_summary`")
    private String orderSummary;

    /**
     * 送货单号
     */
    @Column(name = "`delivery_number`")
    private String deliveryNumber;

    /**
     * 用户id
     */
    @Column(name = "`userid`")
    private Integer userid;

    /**
     * 组id
     */
    @Column(name = "`groupid`")
    private Integer groupid;

    /**
     * 运单最新位置
     */
    @Column(name = "`address`")
    private String address;

    /**
     * 条码编号
     */
    @Column(name = "`barcode`")
    private String barcode;

    /**
     * 条码状态（10:已分配，20:已绑定，30:运输中，40:已结束）
     */
    @Column(name = "`bindstatus`")
    private Integer bindstatus;

    /**
     * 绑定时间
     */
    @Column(name = "`createtime`")
    private Date createtime;

    /**
     * 要求到货时间
     */
    @Column(name = "`arrivaltime`")
    private Date arrivaltime;

    /**
     * 实际达到时间
     */
    @Column(name = "`actual_arrival_time`")
    private Date actualArrivalTime;

    /**
     * 条码主键
     */
    @Column(name = "`barcodeid`")
    private Integer barcodeid;

    /**
     * 收货客户主键
     */
    @Column(name = "`customerid`")
    private Integer customerid;

    /**
     * 获取主键id
     *
     * @return id - 主键id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键id
     *
     * @param id 主键id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取订单摘要
     *
     * @return order_summary - 订单摘要
     */
    public String getOrderSummary() {
        return orderSummary;
    }

    /**
     * 设置订单摘要
     *
     * @param orderSummary 订单摘要
     */
    public void setOrderSummary(String orderSummary) {
        this.orderSummary = orderSummary;
    }

    /**
     * 获取送货单号
     *
     * @return delivery_number - 送货单号
     */
    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    /**
     * 设置送货单号
     *
     * @param deliveryNumber 送货单号
     */
    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    /**
     * 获取用户id
     *
     * @return userid - 用户id
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置用户id
     *
     * @param userid 用户id
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * 获取组id
     *
     * @return groupid - 组id
     */
    public Integer getGroupid() {
        return groupid;
    }

    /**
     * 设置组id
     *
     * @param groupid 组id
     */
    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    /**
     * 获取运单最新位置
     *
     * @return address - 运单最新位置
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置运单最新位置
     *
     * @param address 运单最新位置
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取条码编号
     *
     * @return barcode - 条码编号
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * 设置条码编号
     *
     * @param barcode 条码编号
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * 获取条码状态（10:已分配，20:已绑定，30:运输中，40:已结束）
     *
     * @return bindstatus - 条码状态（10:已分配，20:已绑定，30:运输中，40:已结束）
     */
    public Integer getBindstatus() {
        return bindstatus;
    }

    /**
     * 设置条码状态（10:已分配，20:已绑定，30:运输中，40:已结束）
     *
     * @param bindstatus 条码状态（10:已分配，20:已绑定，30:运输中，40:已结束）
     */
    public void setBindstatus(Integer bindstatus) {
        this.bindstatus = bindstatus;
    }

    /**
     * 获取绑定时间
     *
     * @return createtime - 绑定时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 设置绑定时间
     *
     * @param createtime 绑定时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取要求到货时间
     *
     * @return arrivaltime - 要求到货时间
     */
    public Date getArrivaltime() {
        return arrivaltime;
    }

    /**
     * 设置要求到货时间
     *
     * @param arrivaltime 要求到货时间
     */
    public void setArrivaltime(Date arrivaltime) {
        this.arrivaltime = arrivaltime;
    }

    /**
     * 获取实际达到时间
     *
     * @return actual_arrival_time - 实际达到时间
     */
    public Date getActualArrivalTime() {
        return actualArrivalTime;
    }

    /**
     * 设置实际达到时间
     *
     * @param actualArrivalTime 实际达到时间
     */
    public void setActualArrivalTime(Date actualArrivalTime) {
        this.actualArrivalTime = actualArrivalTime;
    }

    /**
     * 获取条码主键
     *
     * @return barcodeid - 条码主键
     */
    public Integer getBarcodeid() {
        return barcodeid;
    }

    /**
     * 设置条码主键
     *
     * @param barcodeid 条码主键
     */
    public void setBarcodeid(Integer barcodeid) {
        this.barcodeid = barcodeid;
    }

    /**
     * 获取收货客户主键
     *
     * @return customerid - 收货客户主键
     */
    public Integer getCustomerid() {
        return customerid;
    }

    /**
     * 设置收货客户主键
     *
     * @param customerid 收货客户主键
     */
    public void setCustomerid(Integer customerid) {
        this.customerid = customerid;
    }
}