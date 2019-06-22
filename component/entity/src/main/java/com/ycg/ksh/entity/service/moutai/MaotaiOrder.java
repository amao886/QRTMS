package com.ycg.ksh.entity.service.moutai;

import com.ycg.ksh.common.entity.RequestEntity;

import java.util.Date;

/**
 * 茅台发货单查询的实体
 * <p>
 * @developer Create by <a href="mailto:108252@ycgwl.com">wyj</a> at 2018-04-18
 */
public class MaotaiOrder extends RequestEntity {

    /** 客户编号 */
    private String customerId;

    /** 购货单位 */
    private String buyerUnit;

   /* *//** 承运商 *//*
    private String carrierUnit;*/

    private Long conveyId;

    /** 导入起始时间 */
    private Date startTime;

    /** 导入结束时间 */
    private Date endTime;

    private Integer printSign;

    private Integer depotId;

    private String scheduleNo;

    public String getScheduleNo() {
        return scheduleNo;
    }

    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    public Integer getDepotId() {
        return depotId;
    }

    public void setDepotId(Integer depotId) {
        this.depotId = depotId;
    }

    public Integer getPrintSign() {
        return printSign;
    }

    public void setPrintSign(Integer printSign) {
        this.printSign = printSign;
    }

    public String getCustomerId() {
        return customerId;
    }

    public MaotaiOrder setCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public String getBuyerUnit() {
        return buyerUnit;
    }

    public MaotaiOrder setBuyerUnit(String buyerUnit) {
        this.buyerUnit = buyerUnit;
        return this;
    }

    public Long getConveyId() {
        return conveyId;
    }

    public MaotaiOrder setConveyId(Long conveyId) {
        this.conveyId = conveyId;
        return this;
    }

    public Date getStartTime() {
        return startTime;
    }

    public MaotaiOrder setStartTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }

    public Date getEndTime() {
        return endTime;
    }

    public MaotaiOrder setEndTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }
    public void handle(){
        customerId = customerId == null ? "" : customerId.trim();
        buyerUnit = buyerUnit == null ? "" : buyerUnit.trim();
        endTime = new Date(endTime.getTime() + 86400000);
        scheduleNo = scheduleNo == null ? "" : scheduleNo.trim();
    }
}
