package com.ycg.ksh.service.core.entity.service.adventive;

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Collection;
import java.util.Map;

public class AdventiveOrder extends BaseEntity {

    private Long key;//合同物流管理平台订单编号(合同物流管理平台平台中具有唯一性)
    private String bindCode;//绑定的二维码
    private String orderNo;//客户订单号
    private String deliveryNo;//配送单号
    private Long shipperKey;//发货方公司编号
    private String shipperName;//发货方公司名称
    private Long receiveKey;//收货方公司编号
    private String receiveName;//收货方公司名称
    private Long conveyKey;//承运方公司编号
    private String conveyName;//承运方公司名称
    private String location;//最新位置
    private String receiverName;//收货人名称
    private String receiverContact;//联系方式
    private String receiveAddress;//收货地址
    private Integer fettle;//订单状态(0:默认;1:运输中;4:确认收货;99:作废)
    private Integer signFettle;//签署状态(0:未签收,1:正常签收,2:异常签收)
    private String deliveryTime;//发货时间
    private String receiveTime;//到货时间
    private String remark;//备注
    private String createTime;//创建时间

    private String exception;//异常信息

    //订单运输信息(车辆，司机等)
    private String carNo;//车牌号
    private String driverName;//司机姓名
    private String driverContact;//司机联系方式

    //订单额外信息
    private String distributeAddress;//发货地址
    private String originStation;//始发地
    private String arrivalStation;//目的地
    private String startStation;//发站
    private String endStation;//到站

    //订单财务信息(成本，支出等)
    private Double income;//收入
    private Double expenditure;//支出

    //其他数据
    private Map<String, Object> others;

    private Collection<AdventiveCommodity> commodities;//货物明细

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getBindCode() {
        return bindCode;
    }

    public void setBindCode(String bindCode) {
        this.bindCode = bindCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    public Long getShipperKey() {
        return shipperKey;
    }

    public void setShipperKey(Long shipperKey) {
        this.shipperKey = shipperKey;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public Long getReceiveKey() {
        return receiveKey;
    }

    public void setReceiveKey(Long receiveKey) {
        this.receiveKey = receiveKey;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public Long getConveyKey() {
        return conveyKey;
    }

    public void setConveyKey(Long conveyKey) {
        this.conveyKey = conveyKey;
    }

    public String getConveyName() {
        return conveyName;
    }

    public void setConveyName(String conveyName) {
        this.conveyName = conveyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverContact() {
        return receiverContact;
    }

    public void setReceiverContact(String receiverContact) {
        this.receiverContact = receiverContact;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public Integer getFettle() {
        return fettle;
    }

    public void setFettle(Integer fettle) {
        this.fettle = fettle;
    }

    public Integer getSignFettle() {
        return signFettle;
    }

    public void setSignFettle(Integer signFettle) {
        this.signFettle = signFettle;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverContact() {
        return driverContact;
    }

    public void setDriverContact(String driverContact) {
        this.driverContact = driverContact;
    }

    public String getDistributeAddress() {
        return distributeAddress;
    }

    public void setDistributeAddress(String distributeAddress) {
        this.distributeAddress = distributeAddress;
    }

    public String getOriginStation() {
        return originStation;
    }

    public void setOriginStation(String originStation) {
        this.originStation = originStation;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }

    public void setArrivalStation(String arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public Double getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(Double expenditure) {
        this.expenditure = expenditure;
    }

    public Map<String, Object> getOthers() {
        return others;
    }

    public void setOthers(Map<String, Object> others) {
        this.others = others;
    }

    public Collection<AdventiveCommodity> getCommodities() {
        return commodities;
    }

    public void setCommodities(Collection<AdventiveCommodity> commodities) {
        this.commodities = commodities;
    }
}
