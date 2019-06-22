package com.ycg.ksh.core.contract.domain.model;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.core.Registrys;
import com.ycg.ksh.core.common.domain.Model;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 收支记录
 *
 * @author: wangke
 * @create: 2019-01-04 15:07
 **/

public class IncomeRecord extends Model {

    //发货时间
    private LocalDateTime deliveryDate;

    //发货人姓名
    private String deliveryCustomer;

    //收货客户
    private String receiveCustomer;

    //送货单号
    private String deliveryNo;

    //总重量
    private double totalWeight;

    //总体积
    private double totalVolume;

    //总数量
    private Integer totalQuantity;

    //运输应收
    private double transportReceivable;

    //其他应收
    private double otherTotalReceivable;

    //应收合计
    private double totalReceivable;

    //确认状态 0 待确认， 1 已确认
    private Integer confirmState;

    //备注
    private String otherReceivableRemark;

    //创建时间
    private LocalDateTime createTime;

    private Long companyKey;

    private Long orderKey;

    public IncomeRecord() {

    }

    public void save(IncomeRecord t) throws BusinessException {
        Registrys.incomeRepository().save(t);
    }

    public void modify(IncomeRecord t) {
        Registrys.incomeRepository().modify(t);
    }

    public IncomeRecord(String deliveryCustomer, String receiveCustomer, String deliveryNo, double totalWeight, double totalVolume, Integer totalQuantity
            , double transportReceivable, double otherTotalReceivable, double totalReceivable, Integer confirmState, String otherReceivableRemark, Long orderKey,
                        LocalDateTime deliveryDate, LocalDateTime createTime, Long companyKey) {
        this.setDeliveryCustomer(deliveryCustomer);
        this.setReceiveCustomer(receiveCustomer);
        this.setDeliveryNo(deliveryNo);
        this.setTotalWeight(totalWeight);
        this.setTotalVolume(totalVolume);
        this.setTotalQuantity(totalQuantity);
        this.setTransportReceivable(transportReceivable);
        this.setOtherTotalReceivable(otherTotalReceivable);
        this.setTotalReceivable(totalReceivable);
        this.setConfirmState(confirmState);
        this.setOtherReceivableRemark(otherReceivableRemark);
        this.setOrderKey(orderKey);
        this.setDeliveryDate(deliveryDate);
        this.setCreateTime(createTime);
        this.setCompanyKey(companyKey);
    }


    public Long getCompanyKey() {
        return companyKey;
    }

    private void setCompanyKey(Long companyKey) {
        this.companyKey = companyKey;
    }

    public Long getOrderKey() {
        return orderKey;
    }

    private void setOrderKey(Long orderKey) {
        this.orderKey = orderKey;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    private void setCreateTime(LocalDateTime createTime) {
        Optional.ofNullable(this.createTime = createTime).orElse(this.createTime = LocalDateTime.now());
    }


    public String getOtherReceivableRemark() {
        return otherReceivableRemark;
    }

    private void setOtherReceivableRemark(String otherReceivableRemark) {
        Assert.notBlank(otherReceivableRemark, "其他应收备注不能为空");
        this.otherReceivableRemark = otherReceivableRemark;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    private void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryCustomer() {
        return deliveryCustomer;
    }

    private void setDeliveryCustomer(String deliveryCustomer) {
        this.deliveryCustomer = deliveryCustomer;
    }

    public String getReceiveCustomer() {
        return receiveCustomer;
    }

    private void setReceiveCustomer(String receiveCustomer) {
        this.receiveCustomer = receiveCustomer;
    }

    public String getDeliveryNo() {
        return deliveryNo;
    }

    private void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    private void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public double getTotalVolume() {
        return totalVolume;
    }

    private void setTotalVolume(double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    private void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getTransportReceivable() {
        return transportReceivable;
    }

    private void setTransportReceivable(double transportReceivable) {
        this.transportReceivable = transportReceivable;
    }

    public double getOtherTotalReceivable() {
        return otherTotalReceivable;
    }

    private void setOtherTotalReceivable(double otherTotalReceivable) {
        Assert.notNull(otherTotalReceivable, "其他应收合计不能为空");
        this.otherTotalReceivable = otherTotalReceivable;
    }

    public double getTotalReceivable() {
        return totalReceivable;
    }

    private void setTotalReceivable(double totalReceivable) {
        this.totalReceivable = totalReceivable;
    }

    public Integer getConfirmState() {
        return confirmState;
    }

    private void setConfirmState(Integer confirmState) {
        Optional.ofNullable(this.confirmState = confirmState).orElse(this.confirmState = 0);
    }
}
