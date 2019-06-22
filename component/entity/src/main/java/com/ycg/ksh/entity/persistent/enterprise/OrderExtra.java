package com.ycg.ksh.entity.persistent.enterprise;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.RegionUtils;
import com.ycg.ksh.common.util.StringUtils;

import javax.persistence.*;
import java.util.Optional;

@Table(name = "`T_ORDER_EXTRA`")
public class OrderExtra extends BaseEntity {
    /**
     * 订单编号
     */
    @Id
    @Column(name = "`KEY`")
    private Long key;

    @Column(name = "`DATA_TYPE`")
    private Integer dataType;

    @Column(name = "`DATA_KEY`")
    private Long dataKey;

    /**
     * 发货地址
     */
    @Column(name = "`DISTRIBUTE_ADDRESS`")
    private String distributeAddress;

    /**
     * 始发地
     */
    @Column(name = "`ORIGIN_STATION`")
    private String originStation;

    /**
     * 目的地
     */
    @Column(name = "`ARRIVAL_STATION`")
    private String arrivalStation;
    /**
     * 发站
     */
    @Column(name = "`START_STATION`")
    private String startStation;

    /**
     * 到站
     */
    @Column(name = "`END_STATION`")
    private String endStation;

    /**
     * 承运商联系人
     */
    @Column(name = "`CONVEYER_NAME`")
    private String conveyerName;

    /**
     * 承运商联系电话
     */
    @Column(name = "`CONVEYER_CONTACT`")
    private String conveyerContact;
    /**
     * 车牌号
     */
    @Column(name = "`CARE_NO`")
    private String careNo;

    /**
     * 司机姓名
     */
    @Column(name = "`DRIVER_NAME`")
    private String driverName;

    /**
     * 司机联系方式
     */
    @Column(name = "`DRIVER_CONTACT`")
    private String driverContact;
    /**
     * 收入
     */
    @Column(name = "`INCOME`")
    private Double income;

    /**
     * 支出
     */
    @Column(name = "`EXPENDITURE`")
    private Double expenditure;


    //运输应收
    @Column(name = "`TRANSPORT_RECEIVABLE`")
    private Double transportReceivable;

    //其他应收
    @Column(name = "`OTHERTOTAL_RECEIVABLE`")
    private Double otherTotalReceivable;

    //应收合计
    @Column(name = "`TOTAL_RECEIVABLE`")
    private Double totalReceivable;

    //备注
    @Column(name = "`OTHER_RECEIVABLE_REMARK`")
    private String otherReceivableRemark;

    /**
     * 合并订单号
     */
    @Column(name = "`MERGER_ORDER`")
    private String mergerOrder;


    public OrderExtra() {
    }

    public OrderExtra(Integer dataType, Long dataKey) {
        this.dataType = dataType;
        this.dataKey = dataKey;
    }

    public OrderExtra(String careNo, String driverName, String driverContact) {
        this.careNo = careNo;
        this.driverName = driverName;
        this.driverContact = driverContact;
    }

    public OrderExtra(String conveyerName, String conveyerContact) {
        this.conveyerName = conveyerName;
        this.conveyerContact = conveyerContact;
    }

    public void modify(OrderExtra extra) {

        if (StringUtils.isNotBlank(extra.distributeAddress)) {
            distributeAddress = extra.distributeAddress;
        }
        if (StringUtils.isNotBlank(extra.originStation)) {
            originStation = extra.originStation;
        }
        if (StringUtils.isNotBlank(extra.arrivalStation)) {
            arrivalStation = extra.arrivalStation;
        }
        if (StringUtils.isNotBlank(extra.startStation)) {
            startStation = extra.startStation;
        }
        if (StringUtils.isNotBlank(extra.endStation)) {
            endStation = extra.endStation;
        }
        if (StringUtils.isNotBlank(extra.conveyerName)) {
            conveyerName = extra.conveyerName;
        }
        if (StringUtils.isNotBlank(extra.conveyerContact)) {
            conveyerContact = extra.conveyerContact;
        }
        if (StringUtils.isNotBlank(extra.careNo)) {
            careNo = extra.careNo;
        }
        if (StringUtils.isNotBlank(extra.driverName)) {
            driverName = extra.driverName;
        }
        if (StringUtils.isNotBlank(extra.driverContact)) {
            driverContact = extra.driverContact;
        }
        if (extra.income != null && extra.income > 0) {
            income = extra.income;
        }
        if (extra.expenditure != null && extra.expenditure > 0) {
            expenditure = extra.expenditure;
        }
        if (extra.transportReceivable != null && extra.transportReceivable > 0) {
            transportReceivable = extra.transportReceivable;
        }
        if (extra.otherTotalReceivable != null && extra.otherTotalReceivable > 0) {
            otherTotalReceivable = extra.otherTotalReceivable;
        }
        if (extra.totalReceivable != null && extra.totalReceivable > 0) {
            totalReceivable = extra.totalReceivable;
        }
        if (StringUtils.isNotBlank(otherReceivableRemark)) {
            otherReceivableRemark = extra.otherReceivableRemark;
        }

    }

    public String getMergerOrder() {
        return mergerOrder;
    }

    public void setMergerOrder(String mergerOrder) {
        this.mergerOrder = mergerOrder;
    }

    public boolean isEmpty() {
        return StringUtils.isBlank(distributeAddress) && StringUtils.isBlank(originStation) &&
                StringUtils.isBlank(arrivalStation) && StringUtils.isBlank(startStation) &&
                StringUtils.isBlank(endStation) && StringUtils.isBlank(conveyerName) &&
                StringUtils.isBlank(conveyerContact) && StringUtils.isBlank(careNo) &&
                StringUtils.isBlank(driverName) && StringUtils.isBlank(driverContact) &&
                (income == null || income <= 0) && (expenditure == null || expenditure <= 0);
    }

    public Double getTransportReceivable() {
        return transportReceivable;
    }

    public void setTransportReceivable(Double transportReceivable) {
        this.transportReceivable = transportReceivable;
    }

    public Double getOtherTotalReceivable() {
        return otherTotalReceivable;
    }

    public void setOtherTotalReceivable(Double otherTotalReceivable) {
        this.otherTotalReceivable = otherTotalReceivable;
    }

    public Double getTotalReceivable() {
        return totalReceivable;
    }

    public void setTotalReceivable(Double totalReceivable) {
        if ((null != transportReceivable && transportReceivable > 00 && (null != otherTotalReceivable && otherTotalReceivable > 0))) {
            this.totalReceivable = transportReceivable + otherTotalReceivable;
        } else {
            this.totalReceivable = totalReceivable;
        }
    }

    public String getOtherReceivableRemark() {
        return otherReceivableRemark;
    }

    public void setOtherReceivableRemark(String otherReceivableRemark) {
        this.otherReceivableRemark = otherReceivableRemark;
    }

    private String string(String source) {
        return StringUtils.isNotBlank(source) ? source : StringUtils.EMPTY;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = string(startStation);
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = string(endStation);
    }

    /**
     * 获取订单编号
     *
     * @return KEY - 订单编号
     */
    public Long getKey() {
        return key;
    }

    /**
     * 设置订单编号
     *
     * @param key 订单编号
     */
    public void setKey(Long key) {
        this.key = key;
    }

    /**
     * 获取发货地址
     *
     * @return DISTRIBUTE_ADDRESS - 发货地址
     */
    public String getDistributeAddress() {
        return distributeAddress;
    }

    /**
     * 设置发货地址
     *
     * @param distributeAddress 发货地址
     */
    public void setDistributeAddress(String distributeAddress) {
        this.distributeAddress = string(distributeAddress);
    }

    /**
     * 获取始发地
     *
     * @return ORIGIN_STATION - 始发地
     */
    public String getOriginStation() {
        return originStation;
    }

    /**
     * 设置始发地
     *
     * @param originStation 始发地
     */
    public void setOriginStation(String originStation) {
        this.originStation = string(originStation);
    }

    /**
     * 获取目的地
     *
     * @return ARRIVAL_STATION - 目的地
     */
    public String getArrivalStation() {
        return arrivalStation;
    }

    /**
     * 设置目的地
     *
     * @param arrivalStation 目的地
     */
    public void setArrivalStation(String arrivalStation) {
        this.arrivalStation = string(arrivalStation);
    }

    /**
     * 获取承运商联系人
     *
     * @return CONVEYER_NAME - 承运商联系人
     */
    public String getConveyerName() {
        return conveyerName;
    }

    /**
     * 设置承运商联系人
     *
     * @param conveyerName 承运商联系人
     */
    public void setConveyerName(String conveyerName) {
        this.conveyerName = string(conveyerName);
    }

    /**
     * 获取承运商联系电话
     *
     * @return CONVEYER_CONTACT - 承运商联系电话
     */
    public String getConveyerContact() {
        return conveyerContact;
    }

    /**
     * 设置承运商联系电话
     *
     * @param conveyerContact 承运商联系电话
     */
    public void setConveyerContact(String conveyerContact) {
        this.conveyerContact = string(conveyerContact);
    }

    public String getCareNo() {
        return careNo;
    }

    public void setCareNo(String careNo) {
        this.careNo = string(careNo);
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = string(driverName);
    }

    public String getDriverContact() {
        return driverContact;
    }

    public void setDriverContact(String driverContact) {
        this.driverContact = string(driverContact);
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

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public Long getDataKey() {
        return dataKey;
    }

    public void setDataKey(Long dataKey) {
        this.dataKey = dataKey;
    }
}