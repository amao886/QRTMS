package com.ycg.ksh.entity.persistent.enterprise;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/1 0001
 */

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * 订单的抽象
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/1 0001
 */
public class AbstractOrder extends BaseEntity {

    /**
     * 订单编号
     */
    @Id
    @Column(name = "`ID`")
    protected Long id;
    /**
     * 绑定的二维码
     */
    @Column(name = "`BIND_CODE`")
    protected String bindCode;

    /**
     * 客户订单号
     */
    @Column(name = "`ORDER_NO`")
    protected String orderNo;

    /**
     * 配送单号
     */
    @Column(name = "`DELIVERY_NO`")
    protected String deliveryNo;

    /**
     * 发货方编号
     */
    @Column(name = "`SHIPPER_ID`")
    protected Long shipperId;

    /**
     * 收货方编号
     */
    @Column(name = "`RECEIVE_ID`")
    protected Long receiveId;
    /**
     * 收货方类型
     * Y:注册企业   N:客户关联
     * 发货方[Y] 收货方[Y] 承运方[Y] -------------> 0
     * 发货方[N] 收货方[N] 承运方[N] -------------> 7
     * 发货方[Y] 收货方[Y] 承运方[N] -------------> 1
     * 发货方[Y] 收货方[N] 承运方[Y] -------------> 2
     * 发货方[N] 收货方[Y] 承运方[Y] -------------> 4
     * 发货方[N] 收货方[N] 承运方[Y] -------------> 6
     * 发货方[N] 收货方[Y] 承运方[N] -------------> 5
     * 发货方[Y] 收货方[N] 承运方[N] -------------> 3
     */
    @Column(name = "`CLIENT_TYPE`")
    protected Integer clientType;

    /**
     * 承运方编号
     */
    @Column(name = "`CONVEY_ID`")
    protected Long conveyId;

    /**
     * 最新位置
     */
    @Column(name = "`LOCATION`")
    protected String location;
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
     * 订单状态(0:默认,99:作废)
     */
    @Column(name = "`FETTLE`")
    protected Integer fettle;

    /**
     * 签署状态(0:未签收,1:正常签收,2:异常签收)
     */
    @Column(name = "`SIGN_FETTLE`")
    protected Integer signFettle;

    /**
     * 发货时间
     */
    @Column(name = "`DELIVERY_TIME`")
    protected Date deliveryTime;

    /**
     * 到货时间
     */
    @Column(name = "`RECEIVE_TIME`")
    protected Date receiveTime;

    /**
     * 要求到货时间
     */
    @Column(name = "`ARRIVAL_TIME`")
    protected Date arrivalTime;
    /**
     * 到货时间
     */
    @Column(name = "`ARRIVED_TIME`")
    protected Date arrivedTime;
    /**
     * 备注
     */
    @Column(name = "`REMARK`")
    protected String remark;

    /**
     * 创建人
     */
    @Column(name = "`CREATE_USER_ID`")
    protected Integer createUserId;

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
     * 数据入库方式(1:发货方,2:收货方,3:承运方)
     */
    @Column(name = "`INSERT_TYPE`")
    protected Integer insertType;

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
     * 获取配送单号
     *
     * @return DELIVERY_NO - 配送单号
     */
    public String getDeliveryNo() {
        return deliveryNo;
    }

    /**
     * 设置配送单号
     *
     * @param deliveryNo 配送单号
     */
    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    /**
     * 获取发货方编号
     *
     * @return shipperId - 发货方编号
     */
    public Long getShipperId() {
        return shipperId;
    }

    /**
     * 设置发货方编号
     *
     * @param shipperId 发货方编号
     */
    public void setShipperId(Long shipperId) {
        this.shipperId = shipperId;
    }

    /**
     * 获取收货方编号
     *
     * @return RECEIVE_ID - 收货方编号
     */
    public Long getReceiveId() {
        return receiveId;
    }

    /**
     * 设置收货方编号
     *
     * @param receiveId 收货方编号
     */
    public void setReceiveId(Long receiveId) {
        this.receiveId = receiveId;
    }

    /**
     * 获取承运方编号
     *
     * @return CONVEY_ID - 承运方编号
     */
    public Long getConveyId() {
        return conveyId;
    }

    /**
     * 设置承运方编号
     *
     * @param conveyId 承运方编号
     */
    public void setConveyId(Long conveyId) {
        this.conveyId = conveyId;
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
     * 获取订单状态(0:默认,99:作废)
     *
     * @return FETTLE - 订单状态(0:默认,99:作废)
     */
    public Integer getFettle() {
        return fettle;
    }

    /**
     * 设置订单状态(0:默认,99:作废)
     *
     * @param fettle 订单状态(0:默认,99:作废)
     */
    public void setFettle(Integer fettle) {
        this.fettle = fettle;
    }

    public Integer getSignFettle() {
        return signFettle;
    }

    public void setSignFettle(Integer signFettle) {
        this.signFettle = signFettle;
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
     * 获取到货时间
     *
     * @return RECEIVE_TIME - 到货时间
     */
    public Date getReceiveTime() {
        return receiveTime;
    }

    /**
     * 设置到货时间
     *
     * @param receiveTime 到货时间
     */
    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getBindCode() {
        return bindCode;
    }

    public void setBindCode(String bindCode) {
        this.bindCode = bindCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

    public Integer getInsertType() {
        return insertType;
    }

    public void setInsertType(Integer insertType) {
        this.insertType = insertType;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Date getArrivedTime() {
        return arrivedTime;
    }

    public void setArrivedTime(Date arrivedTime) {
        this.arrivedTime = arrivedTime;
    }
}
