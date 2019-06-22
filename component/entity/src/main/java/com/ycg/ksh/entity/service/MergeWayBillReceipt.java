package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.entity.persistent.ReceiptScanBatch;
import com.ycg.ksh.entity.persistent.Waybill;

import java.util.List;

/**
 * 运单/回单整合类
 */
public class MergeWayBillReceipt extends BaseEntity {

    private static final long serialVersionUID = -6622042893372259126L;

    private List<MergeReceipt> receiptList;

    private Waybill waybill;

    private ReceiptScanBatch batch;

    private String path;

    private Integer sendCustomerFlag;

    public Integer getSendCustomerFlag() {
        return sendCustomerFlag;
    }

    public void setSendCustomerFlag(Integer sendCustomerFlag) {
        this.sendCustomerFlag = sendCustomerFlag;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ReceiptScanBatch getBatch() {
        return batch;
    }

    public void setBatch(ReceiptScanBatch batch) {
        this.batch = batch;
    }

    public List<MergeReceipt> getReceiptList() {
        return receiptList;
    }

    public void setReceiptList(List<MergeReceipt> receiptList) {
        this.receiptList = receiptList;
    }

    public Waybill getWaybill() {
        return waybill;
    }

    public void setWaybill(Waybill waybill) {
        this.waybill = waybill;
    }
}
