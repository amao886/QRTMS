package com.ycg.ksh.entity.collecter;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/10/31 0031
 */

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * 订单统计数据
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/10/31 0031
 */
public class OrderCollect extends BaseEntity {

    /**
     * 订单编号
     */
    protected Long id;
    /**
     * 发货方编号
     */
    protected Long shipperId;

    /**
     * 收货方编号
     */
    protected Long receiveId;

    /**
     * 承运方编号
     */
    protected Long conveyId;
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
    protected Integer clientType;
    /**
     * 下级物流商编号
     */
    protected Long subordinateId;

    /**
     * 订单创建方
     */
    protected Integer insertType;
    /**
     * 发货时间
     */
    protected LocalDateTime deliveryTime;

    /**
     * 到货时间
     */
    protected LocalDateTime receiveTime;

    /**
     * 要求到货时间
     */
    protected LocalDateTime arrivalTime;
    /**
     * 到货时间
     */
    protected LocalDateTime arrivedTime;
    /**
     * 创建时间
     */
    protected LocalDateTime createTime;
    /**
     * 派车时间
     */
    protected LocalDateTime carTime;
    /**
     * 是否上传回单 0:否,大于0:是
     */
    protected Integer isReceipt;
    /**
     * 提货预警  1：正常提货 2：延迟提货
     */
    protected Integer pickupWarning;
    /**
     * 延迟预警1：正常运输 2：可能延迟 3：已延迟
     */
    protected Integer delayWarning;
    /**
     * 定位检测 1，达标  0，不达标
     */
    protected Integer positioningCheck;

    /**
     * 评价 0 差评 1好评
     */
    protected Integer evaluation;

    /**
     * 是否投诉
     */
    private boolean isComplaint;
    /**
     * 签署状态(0:未签收,1:正常签收,2:异常签收)
     */
    private Integer signFettle;


    public OrderCollect() {
    }

    public OrderCollect(Long id, Long shipperId, Long receiveId, Long conveyId, Integer clientType, Integer insertType, Integer positioningCheck) {
        this.id = id;
        this.shipperId = shipperId;
        this.receiveId = receiveId;
        this.conveyId = conveyId;
        this.clientType = clientType;
        this.insertType = insertType;
        this.positioningCheck = positioningCheck;
    }

    public OrderCollect(Long id, LocalDateTime deliveryTime, LocalDateTime receiveTime, LocalDateTime arrivalTime, LocalDateTime arrivedTime, LocalDateTime createTime) {
        this.id = id;
        this.deliveryTime = deliveryTime;
        this.receiveTime = receiveTime;
        this.arrivalTime = arrivalTime;
        this.arrivedTime = arrivedTime;
        this.createTime = createTime;
    }

    public OrderCollect(Long id, Long shipperId, Long receiveId, Long conveyId, Integer clientType, Long subordinateId,
                        LocalDateTime deliveryTime, LocalDateTime receiveTime, LocalDateTime arrivalTime, LocalDateTime arrivedTime,
                        LocalDateTime createTime, Integer isReceipt, Integer pickupWarning, Integer delayWarning,
                        Integer positioningCheck, Integer evaluation, boolean isComplaint, Integer signFettle, LocalDateTime carTime) {
        this.id = id;
        this.shipperId = shipperId;
        this.receiveId = receiveId;
        this.conveyId = conveyId;
        this.clientType = clientType;
        this.subordinateId = subordinateId;
        this.deliveryTime = deliveryTime;
        this.receiveTime = receiveTime;
        this.arrivalTime = arrivalTime;
        this.arrivedTime = arrivedTime;
        this.createTime = createTime;
        this.isReceipt = isReceipt;
        this.pickupWarning = pickupWarning;
        this.delayWarning = delayWarning;
        this.positioningCheck = positioningCheck;
        this.evaluation = evaluation;
        this.isComplaint = isComplaint;
        this.signFettle = signFettle;
        this.carTime = carTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShipperId() {
        return shipperId;
    }

    public void setShipperId(Long shipperId) {
        this.shipperId = shipperId;
    }

    public Long getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(Long receiveId) {
        this.receiveId = receiveId;
    }

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

    public Long getConveyId() {
        return conveyId;
    }

    public void setConveyId(Long conveyId) {
        this.conveyId = conveyId;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public LocalDateTime getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(LocalDateTime receiveTime) {
        this.receiveTime = receiveTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
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

    public Integer getPositioningCheck() {
        return positioningCheck;
    }

    public void setPositioningCheck(Integer positioningCheck) {
        this.positioningCheck = positioningCheck;
    }

    public Integer getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Integer evaluation) {
        this.evaluation = evaluation;
    }

    public boolean isComplaint() {
        return isComplaint;
    }

    public void setComplaint(boolean complaint) {
        isComplaint = complaint;
    }

    public Integer getInsertType() {
        return insertType;
    }

    public void setInsertType(Integer insertType) {
        this.insertType = insertType;
    }

    public Integer getSignFettle() {
        return signFettle;
    }

    public void setSignFettle(Integer signFettle) {
        this.signFettle = signFettle;
    }

    public LocalDateTime getCarTime() {
        return carTime;
    }

    public void setCarTime(LocalDateTime carTime) {
        this.carTime = carTime;
    }

    public LocalDateTime getArrivedTime() {
        return arrivedTime;
    }

    public void setArrivedTime(LocalDateTime arrivedTime) {
        this.arrivedTime = arrivedTime;
    }

    public Long getSubordinateId() {
        return subordinateId;
    }

    public void setSubordinateId(Long subordinateId) {
        this.subordinateId = subordinateId;
    }
}
