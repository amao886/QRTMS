package com.ycg.ksh.entity.persistent;


import java.util.Date;

public class WaybillReceiptView extends Waybill {
    /**
     *
     */
    private static final long serialVersionUID = 8961219221561200105L;

    private Integer groupid;

    private String groupName;

    private Date modifyTime;

    private String contacts;

    private String contactNumber;

    private String batchNumber;

    private String waybillDeliveryNumber;//送货单号和任务单号查询条件

    private String deliverStartTime;//发货日期起 查询条件

    private String deliverEndTime;//发货日 期至 查询条件

    private String serviceStartTime;

    private String serviceEndTime;

    private String sendStartTime;

    private String sendEndTime;

    private Integer receiptStatus;

    @Override
    public Integer getGroupid() {
        return groupid;
    }

    @Override
    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    private String uname;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    private String companyName;

    public Integer getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(Integer receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts == null ? null : contacts.trim();
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber == null ? null : contactNumber.trim();
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber == null ? null : batchNumber.trim();
    }

    public String getWaybillDeliveryNumber() {
        return waybillDeliveryNumber;
    }

    public void setWaybillDeliveryNumber(String waybillDeliveryNumber) {
        this.waybillDeliveryNumber = waybillDeliveryNumber;
    }

    public String getDeliverStartTime() {
        return deliverStartTime;
    }

    public void setDeliverStartTime(String deliverStartTime) {
        this.deliverStartTime = deliverStartTime;
    }

    public String getDeliverEndTime() {
        return deliverEndTime;
    }

    public void setDeliverEndTime(String deliverEndTime) {
        this.deliverEndTime = deliverEndTime;
    }

    public String getServiceStartTime() {
        return serviceStartTime;
    }

    public void setServiceStartTime(String serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }

    public String getServiceEndTime() {
        return serviceEndTime;
    }

    public void setServiceEndTime(String serviceEndTime) {
        this.serviceEndTime = serviceEndTime;
    }

    public String getSendStartTime() {
        return sendStartTime;
    }

    public void setSendStartTime(String sendStartTime) {
        this.sendStartTime = sendStartTime;
    }

    public String getSendEndTime() {
        return sendEndTime;
    }

    public void setSendEndTime(String sendEndTime) {
        this.sendEndTime = sendEndTime;
    }
}