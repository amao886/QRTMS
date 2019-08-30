package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.StringUtils;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 运单实体
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-24 17:14:57
 */
@Table(name = "`waybill_tab`")
public class Waybill extends BaseEntity {

    private static final long serialVersionUID = 1770699795311508556L;
    
    /**
     * 主键id
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 条码主键
     */
    @Column(name = "`barcodeid`")
    private Integer barcodeid;
    /**
     * 条码
     */
    @Column(name = "`barcode`")
    private String barcode;
    /**
     * group主键
     */
    @Column(name = "`groupid`")
    private Integer groupid;

    /**
     * 订单摘要
     */
    @Column(name = "`order_summary`")
    private String orderSummary;

    /**
     * 收货客户主键
     */
    @Column(name = "`customerid`")
    private Integer customerid;

    /**
     * user主键
     */
    @Column(name = "`userid`")
    private Integer userid;

    /**
     * 运单最新位置
     */
    @Column(name = "`address`")
    private String address;

    /**
     * 运单最新位置上报时间
     */
    @Column(name = "`loaction_time`")
    private Date loactionTime;

    /**
     * 要求到货时间
     */
    @Column(name = "`arrivaltime`")
    private Date arrivaltime;

    /**
     * 绑定时间
     */
    @Column(name = "`createtime`")
    private Date createtime;

    /**
     * 修改时间
     */
    @Column(name = "`updatetime`")
    private Date updatetime;

    /**
     * 实际达到时间
     */
    @Column(name = "`actual_arrival_time`")
    private Date actualArrivalTime;

    /**
     * 备注
     */
    @Column(name = "`remark`")
    private String remark;

    /**
     * 重量
     */
    @Column(name = "`weight`")
    private Double weight;

    /**
     * 体积
     */
    @Column(name = "`volume`")
    private Double volume;

    /**
     * 数量
     */
    @Column(name = "`number`")
    private Integer number;

    /**
     * 送货单号
     */
    @Column(name = "`delivery_number`")
    private String deliveryNumber;

    /**
     * 纸质回单状态(0：未回收，1:已回收,2:已送客户,3:已退供应商,4:客户退回)
     */
    @Column(name = "`papery_receipt_status`")
    private Integer paperyReceiptStatus;

    /**
     * 确认送达状态修改的方式(1:后台操作，2:上传回单，3:电子围栏)
     */
    @Column(name = "`confirm_delivery_way`")
    private Integer confirmDeliveryWay;

    /**
     * 收货客户名称/公司名称
     */
    @Column(name = "`receiver_name`")
    private String receiverName;

    /**
     * 收获客户座机
     */
    @Column(name = "`receiver_tel`")
    private String receiverTel;

    /**
     * 收货地址
     */
    @Column(name = "`receive_address`")
    private String receiveAddress;

    /**
     * 收货联系人姓名
     */
    @Column(name = "`contact_name`")
    private String contactName;

    /**
     * 收货联系人电话
     */
    @Column(name = "`contact_phone`")
    private String contactPhone;

    /**
     * 经度
     */
    @Column(name = "`longitude`")
    private String longitude;

    /**
     * 纬度
     */
    @Column(name = "`latitude`")
    private String latitude;

    /**
     * 电子围栏开关1开0关
     */
    @Column(name = "`fence_status`")
    private Integer fenceStatus;

    /**
     * 电子围栏半径,默认5公里
     */
    @Column(name = "`fence_radius`")
    private Double fenceRadius;

    /**
     * 定位次数
     */
    @Column(name = "`position_count`")
    private Integer positionCount;

    /**
     * 回单数
     */
    @Column(name = "`receipt_count`")
    private Integer receiptCount;

    /**
     * 回单审核数
     */
    @Column(name = "`receipt_verify_count`")
    private Integer receiptVerifyCount;

    /**
     * 回单审核不合格数
     */
    @Column(name = "`receipt_unqualify_count`")
    private Integer receiptUnqualifyCount;

    /**
     * 回单审核状态(1:待审核,2:审核中,3:已审核)
     */
    @Column(name = "`receipt_verify_status`")
    private Integer receiptVerifyStatus;

    /**
     * 任务状态(10:未发货,20:发货,30:运输中,35:送达,40:收货)
     */
    @Column(name = "`waybill_status`")
    private Integer waybillStatus;

    /**
     * 是否延迟(0:没有延迟，1:延迟)
     */
    @Column(name = "`delay`")
    private Integer delay;

    /**
     * 货主名称
     */
    @Column(name = "`shipper_name`")
    private String shipperName;

    /**
     * 始发地
     */
    @Column(name = "`start_station`")
    private String startStation;

    /**
     * 始发地简称
     */
    @Column(name = "`simple_start_station`")
    private String simpleStartStation;

    /**
     * 目的地
     */
    @Column(name = "`end_station`")
    private String endStation;

    /**
     * 目的地简称
     */
    @Column(name = "`simple_end_station`")
    private String simpleEndStation;

    /**
     * 发货方地址
     */
    @Column(name = "`shipper_address`")
    private String shipperAddress;

    /**
     * 发货方固定电话
     */
    @Column(name = "`shipper_tel`")
    private String shipperTel;

    /**
     * 发货方联系人
     */
    @Column(name = "`shipper_contact_name`")
    private String shipperContactName;

    /**
     * 发货方联系人电话
     */
    @Column(name = "`shipper_contact_tel`")
    private String shipperContactTel;

    /**
     * 绑定时间
     */
    @Column(name = "`bind_time`")
    private Date bindTime;

    /**
     * 发货运输开始时间
     */
    @Column(name = "`delivery_time`")
    private Date deliveryTime;
    
    @Column(name = "`load_no`")
    private String loadNo;
    
    @Column(name = "`car_type`")
    private String carType;
    
    @Column(name = "`distance`")
    private BigDecimal distance;
    
    @Column(name = "`load_time`")
    private String loadTime;

    public Waybill() {
        super();
    }

    public Waybill(Integer id) {
        super();
        this.id = id;
    }

    public Waybill(Integer id, Integer groupid) {
        super();
        this.id = id;
        this.groupid = groupid;
    }
    

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

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
     * getter method for barcode
     *
     * @return the barcode
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * setter method for barcode
     *
     * @param barcode the barcode to set
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * 获取group主键
     *
     * @return groupid - group主键
     */
    public Integer getGroupid() {
        return groupid;
    }

    /**
     * 设置group主键
     *
     * @param groupid group主键
     */
    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
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

    /**
     * 获取user主键
     *
     * @return userid - user主键
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置user主键
     *
     * @param userid user主键
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
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
     * 获取运单最新位置上报时间
     * @return the loactionTime
     */
    public Date getLoactionTime() {
        return loactionTime;
    }

    /**
     * 设置运单最新位置上报时间
     * @param loactionTime the loactionTime to set
     */
    public void setLoactionTime(Date loactionTime) {
        this.loactionTime = loactionTime;
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
     * 获取修改时间
     *
     * @return updatetime - 修改时间
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 设置修改时间
     *
     * @param updatetime 修改时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
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
     * 获取备注
     *
     * @return remark - 备注
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
     * 获取重量
     *
     * @return weight - 重量
     */
    public Double getWeight() {
        return weight;
    }

    /**
     * 设置重量
     *
     * @param weight 重量
     */
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    /**
     * 获取体积
     *
     * @return volume - 体积
     */
    public Double getVolume() {
        return volume;
    }

    /**
     * 设置体积
     *
     * @param volume 体积
     */
    public void setVolume(Double volume) {
        this.volume = volume;
    }

    /**
     * 获取数量
     *
     * @return number - 数量
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * 设置数量
     *
     * @param number 数量
     */
    public void setNumber(Integer number) {
        this.number = number;
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
     * 获取纸质回单状态(0：未回收，1:已回收,2:已送客户,3:已退供应商,4:客户退回)
     *
     * @return papery_receipt_status - 纸质回单状态(0：未回收，1:已回收,2:已送客户,3:已退供应商,4:客户退回)
     */
    public Integer getPaperyReceiptStatus() {
        return paperyReceiptStatus;
    }

    /**
     * 设置纸质回单状态(0：未回收，1:已回收,2:已送客户,3:已退供应商,4:客户退回)
     *
     * @param paperyReceiptStatus 纸质回单状态(0：未回收，1:已回收,2:已送客户,3:已退供应商,4:客户退回)
     */
    public void setPaperyReceiptStatus(Integer paperyReceiptStatus) {
        this.paperyReceiptStatus = paperyReceiptStatus;
    }

    /**
     * 获取确认送达状态修改的方式(1:后台操作，2:上传回单，3:电子围栏)
     *
     * @return confirm_delivery_way - 确认送达状态修改的方式(1:后台操作，2:上传回单，3:电子围栏)
     */
    public Integer getConfirmDeliveryWay() {
        return confirmDeliveryWay;
    }

    /**
     * 设置确认送达状态修改的方式(1:后台操作，2:上传回单，3:电子围栏)
     *
     * @param confirmDeliveryWay 确认送达状态修改的方式(1:后台操作，2:上传回单，3:电子围栏)
     */
    public void setConfirmDeliveryWay(Integer confirmDeliveryWay) {
        this.confirmDeliveryWay = confirmDeliveryWay;
    }

    /**
     * 获取收货客户名称/公司名称
     *
     * @return receiver_name - 收货客户名称/公司名称
     */
    public String getReceiverName() {
        return receiverName;
    }

    /**
     * 设置收货客户名称/公司名称
     *
     * @param receiverName 收货客户名称/公司名称
     */
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    /**
     * 获取收获客户座机
     *
     * @return receiver_tel - 收获客户座机
     */
    public String getReceiverTel() {
        return receiverTel;
    }

    /**
     * 设置收获客户座机
     *
     * @param receiverTel 收获客户座机
     */
    public void setReceiverTel(String receiverTel) {
        this.receiverTel = receiverTel;
    }

    /**
     * 获取收货地址
     *
     * @return receive_address - 收货地址
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
     * 获取收货联系人姓名
     *
     * @return contact_name - 收货联系人姓名
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * 设置收货联系人姓名
     *
     * @param contactName 收货联系人姓名
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * 获取收货联系人电话
     *
     * @return contact_phone - 收货联系人电话
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * 设置收货联系人电话
     *
     * @param contactPhone 收货联系人电话
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    /**
     * 获取经度
     *
     * @return longitude - 经度
     */
    public String getLongitude() {
        if(StringUtils.isEmptyNull(longitude)) {
            return null;
        }
        if(longitude != null && longitude.length() <= 0) {
            return null;
        }
        return longitude;
    }

    /**
     * 设置经度
     *
     * @param longitude 经度
     */
    public void setLongitude(String longitude) {
        if(StringUtils.isEmptyNull(longitude)) {
            longitude = null;
        }
        if(longitude != null && longitude.length() <= 0) {
            longitude = null;
        }
        this.longitude = longitude;
    }

    /**
     * 获取纬度
     *
     * @return latitude - 纬度
     */
    public String getLatitude() {
        if(StringUtils.isEmptyNull(latitude)) {
            return null;
        }
        if(latitude != null && latitude.length() <= 0) {
            return null;
        }
        return latitude;
    }

    /**
     * 设置纬度
     *
     * @param latitude 纬度
     */
    public void setLatitude(String latitude) {
        if(StringUtils.isEmptyNull(latitude)) {
            latitude = null;
        }
        if(latitude != null && latitude.length() <= 0) {
            latitude = null;
        }
        this.latitude = latitude;
    }

    /**
     * 获取电子围栏开关1开0关
     *
     * @return fence_status - 电子围栏开关1开0关
     */
    public Integer getFenceStatus() {
        return fenceStatus;
    }

    /**
     * 设置电子围栏开关1开0关
     *
     * @param fenceStatus 电子围栏开关1开0关
     */
    public void setFenceStatus(Integer fenceStatus) {
        this.fenceStatus = fenceStatus;
    }

    /**
     * 获取定位次数
     *
     * @return position_count - 定位次数
     */
    public Integer getPositionCount() {
        return positionCount;
    }

    /**
     * 设置定位次数
     *
     * @param positionCount 定位次数
     */
    public void setPositionCount(Integer positionCount) {
        this.positionCount = positionCount;
    }

    /**
     * 获取回单数
     *
     * @return receipt_count - 回单数
     */
    public Integer getReceiptCount() {
    	return receiptCount;
    }

    /**
     * 设置回单数
     *
     * @param receiptCount 回单数
     */
    public void setReceiptCount(Integer receiptCount) {
        this.receiptCount = receiptCount;
    }

    /**
     * 获取回单审核数
     *
     * @return receipt_verify_count - 回单审核数
     */
    public Integer getReceiptVerifyCount() {
    	return receiptVerifyCount;
    }

    /**
     * 设置回单审核数
     *
     * @param receiptVerifyCount 回单审核数
     */
    public void setReceiptVerifyCount(Integer receiptVerifyCount) {
        this.receiptVerifyCount = receiptVerifyCount;
    }

    /**
     * 获取回单审核状态(1:待审核,2:审核中,3:已审核)
     *
     * @return receipt_verify_status - 回单审核状态(1:待审核,2:审核中,3:已审核)
     */
    public Integer getReceiptVerifyStatus() {
        return receiptVerifyStatus;
    }

    /**
     * 设置回单审核状态(1:待审核,2:审核中,3:已审核)
     *
     * @param receiptVerifyStatus 回单审核状态(1:待审核,2:审核中,3:已审核)
     */
    public void setReceiptVerifyStatus(Integer receiptVerifyStatus) {
        this.receiptVerifyStatus = receiptVerifyStatus;
    }

    /**
     * 获取任务状态(20:发货,30:运输中,35:送达,40:收货)
     *
     * @return waybill_status - 任务状态(20:发货,30:运输中,35:送达,40:收货)
     */
    public Integer getWaybillStatus() {
        return waybillStatus;
    }

    /**
     * 设置任务状态(20:发货,30:运输中,35:送达,40:收货)
     *
     * @param waybillStatus 任务状态(20:发货,30:运输中,35:送达,40:收货)
     */
    public void setWaybillStatus(Integer waybillStatus) {
        this.waybillStatus = waybillStatus;
    }

    /**
     * getter method for fenceRadius
     *
     * @return the fenceRadius
     */
    public Double getFenceRadius() {
        return fenceRadius;
    }

    /**
     * setter method for fenceRadius
     *
     * @param fenceRadius the fenceRadius to set
     */
    public void setFenceRadius(Double fenceRadius) {
        this.fenceRadius = fenceRadius;
    }

    /**
     * getter method for receiptUnqualifyCount
     *
     * @return the receiptUnqualifyCount
     */
    public Integer getReceiptUnqualifyCount() {
        return receiptUnqualifyCount;
    }

    /**
     * setter method for receiptUnqualifyCount
     *
     * @param receiptUnqualifyCount the receiptUnqualifyCount to set
     */
    public void setReceiptUnqualifyCount(Integer receiptUnqualifyCount) {
        this.receiptUnqualifyCount = receiptUnqualifyCount;
    }

    /**
     * 获取始发地
     *
     * @return start_station - 始发地
     */
    public String getStartStation() {
        return startStation;
    }

    /**
     * 设置始发地
     *
     * @param startStation 始发地
     */
    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    /**
     * 获取始发地简称
     *
     * @return simple_start_station - 始发地简称
     */
    public String getSimpleStartStation() {
        return simpleStartStation;
    }

    /**
     * 设置始发地简称
     *
     * @param simpleStartStation 始发地简称
     */
    public void setSimpleStartStation(String simpleStartStation) {
        this.simpleStartStation = simpleStartStation;
    }

    /**
     * 获取目的地
     *
     * @return end_station - 目的地
     */
    public String getEndStation() {
        return endStation;
    }

    /**
     * 设置目的地
     *
     * @param endStation 目的地
     */
    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    /**
     * 获取目的地简称
     *
     * @return simple_end_station - 目的地简称
     */
    public String getSimpleEndStation() {
        return simpleEndStation;
    }

    /**
     * 设置目的地简称
     *
     * @param simpleEndStation 目的地简称
     */
    public void setSimpleEndStation(String simpleEndStation) {
        this.simpleEndStation = simpleEndStation;
    }
    /**
     * 获取发货方地址
     *
     * @return shipper_address - 发货方地址
     */
    public String getShipperAddress() {
        return shipperAddress;
    }

    /**
     * 设置发货方地址
     *
     * @param shipperAddress 发货方地址
     */
    public void setShipperAddress(String shipperAddress) {
        this.shipperAddress = shipperAddress;
    }

    /**
     * 获取发货方固定电话
     *
     * @return shipper_tel - 发货方固定电话
     */
    public String getShipperTel() {
        return shipperTel;
    }

    /**
     * 设置发货方固定电话
     *
     * @param shipperTel 发货方固定电话
     */
    public void setShipperTel(String shipperTel) {
        this.shipperTel = shipperTel;
    }

    /**
     * 获取发货方联系人
     *
     * @return shipper_contact_name - 发货方联系人
     */
    public String getShipperContactName() {
        return shipperContactName;
    }

    /**
     * 设置发货方联系人
     *
     * @param shipperContactName 发货方联系人
     */
    public void setShipperContactName(String shipperContactName) {
        this.shipperContactName = shipperContactName;
    }

    /**
     * 获取发货方联系人电话
     *
     * @return shipper_contact_tel - 发货方联系人电话
     */
    public String getShipperContactTel() {
        return shipperContactTel;
    }

    /**
     * 设置发货方联系人电话
     *
     * @param shipperContactTel 发货方联系人电话
     */
    public void setShipperContactTel(String shipperContactTel) {
        this.shipperContactTel = shipperContactTel;
    }

    /**
     * 获取绑定时间
     *
     * @return bind_time - 绑定时间
     */
    public Date getBindTime() {
        return bindTime;
    }

    /**
     * 设置绑定时间
     *
     * @param bindTime 绑定时间
     */
    public void setBindTime(Date bindTime) {
        this.bindTime = bindTime;
    }

    /**
     * 获取发货运输开始时间
     *
     * @return delivery_time - 发货运输开始时间
     */
    public Date getDeliveryTime() {
        return deliveryTime;
    }

    /**
     * 设置发货运输开始时间
     *
     * @param deliveryTime 发货运输开始时间
     */
    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

	public String getLaodNo() {
		return loadNo;
	}

	public void setLaodNo(String loadNo) {
		this.loadNo = loadNo;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public BigDecimal getDistance() {
		return distance;
	}

	public void setDistance(BigDecimal distance) {
		this.distance = distance;
	}

	public String getLoadTime() {
		return loadTime;
	}

	public void setLoadTime(String loadTime) {
		this.loadTime = loadTime;
	}
}