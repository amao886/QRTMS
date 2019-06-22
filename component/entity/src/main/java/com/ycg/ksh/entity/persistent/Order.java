package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.entity.persistent.enterprise.AbstractOrder;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.Optional;

@Table(name = "`T_ORDER`")
public class Order extends AbstractOrder {

    /**
     * 是否上传回单 0:否,大于0:是
     */
    @Column(name = "`IS_RECEIPT`")
    protected Integer isReceipt;
    /**
     * 数据入库方式(1:发货方,2:收货方,3:承运方)
     */
    @Column(name = "`INSERT_TYPE`")
    protected Integer insertType;


    /**
     * 提货预警  1：正常提货 2：延迟提货
     */
    @Column(name = "`PICK_UP_WARNING`")
    protected Integer pickupWarning;

    /**
     * 延迟预警1：正常运输 2：可能延迟 3：已延迟
     */
    @Column(name = "`DELAY_WARNING`")
    protected Integer delayWarning;


    /**
     * 定位检测 1，达标  0，不达标
     */
    @Column(name = "`POSITIONING_CHECK`")
    protected Integer positioningCheck;

    /**
     * 评价 0 差评 1好评
     */
    @Column(name = "`EVALUATION`")
    protected Integer evaluation;

    /**
     * 是否投诉
     */
    @Column(name = "`IS_COMPLAINT`")
    private Boolean isComplaint;
    /**
     * 要求提货时间
     */
    @Column(name = "`COLLECT_TIME`")
    private Date collectTime;


    public Integer getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Integer evaluation) {
        this.evaluation = evaluation;
    }

    public Boolean getIsComplaint() {
        return Optional.ofNullable(isComplaint).orElse(false);
    }

    public void setIsComplaint(Boolean complaint) {
        isComplaint = complaint;
    }

    public Order() {
    }

    public Order(Long id) {
        this.id = id;
    }

    public Order(Long shipperId, Long id) {
        this.shipperId = shipperId;
        this.id = id;
    }

    public Order(Long shipperId, String deliveryNo) {
        this.deliveryNo = deliveryNo;
        this.shipperId = shipperId;
    }

    public Integer getPositioningCheck() {
        return positioningCheck;
    }

    public void setPositioningCheck(Integer positioningCheck) {
        if (positioningCheck == null) {
            this.positioningCheck = 0;
        } else {
            this.positioningCheck = positioningCheck;
        }
    }

    public Integer getIsReceipt() {
        return isReceipt;
    }

    public void setIsReceipt(Integer isReceipt) {
        this.isReceipt = isReceipt;
    }

    public Integer getPickupWarning() {
        return pickupWarning;
    }

    public void setPickupWarning(Integer pickupWarning) {
        this.pickupWarning = pickupWarning;
    }

    public Integer getDelayWarning() {
        return delayWarning;
    }

    public void setDelayWarning(Integer delayWarning) {
        this.delayWarning = delayWarning;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }
}