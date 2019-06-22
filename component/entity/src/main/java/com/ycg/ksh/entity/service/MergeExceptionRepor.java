package com.ycg.ksh.entity.service;

import com.ycg.ksh.entity.persistent.ImageInfo;
import com.ycg.ksh.entity.persistent.WaybillException;

import java.util.Collection;

/**
 * 异常信息整合
 *
 * @author wangke
 * @create 2018-03-02 14:42
 **/
public class MergeExceptionRepor extends WaybillException {

    //任务单号
    private String barcode;
    //任务单号
    private String deliveryNumber;

    //运单号码
    private String conveyanceNumber;

    //承运人
    private Integer ownerKey;
    private String assignName;

    /**
     * 始发地
     */
    private String startStation;

    /**
     * 目的地
     */
    private String endStation;

    private Collection<ImageInfo> images;


    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getConveyanceNumber() {
        return conveyanceNumber;
    }

    public void setConveyanceNumber(String conveyanceNumber) {
        this.conveyanceNumber = conveyanceNumber;
    }

    public Integer getOwnerKey() {
        return ownerKey;
    }

    public void setOwnerKey(Integer ownerKey) {
        this.ownerKey = ownerKey;
    }

    public String getAssignName() {
        return assignName;
    }

    public void setAssignName(String assignName) {
        this.assignName = assignName;
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

    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    public Collection<ImageInfo> getImages() {
        return images;
    }

    public void setImages(Collection<ImageInfo> images) {
        this.images = images;
    }
}
