package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * 回单上传
 *
 * @author wangke
 * @create 2018-06-14 15:57
 **/
@Table(name = "T_PAPER_RECEIPT")
public class PaperReceipt extends BaseEntity {


    @Column(name = "`ID`")
    private Long id;

    @Column(name = "`ORDER_KEY`")
    private Long orderKey;

    @Column(name = "`USER_ID`")
    private Integer userId;

    @Column(name = "`CREATE_TIME`")
    private Date createTime;

    @Column(name = "`RECEIPT_COUNT`")
    private Integer receiptCount;

    @Column(name = "`LONGITUDE`")
    private Double longitude;//回单上传-经度

    @Column(name = "`LATITUDE`")
    private Double latitude;//回单上传-纬度

    @Column(name = "`LOCATION`")
    private String location;//回单上传-位置地址

    public PaperReceipt() {
        super();
    }

    public PaperReceipt(Long orderKey) {
        this.orderKey = orderKey;
    }

    public PaperReceipt(Long id, Long orderKey, Integer userId) {
        super();
        this.userId = userId;
        this.id = id;
        this.orderKey = orderKey;
        this.createTime = new Date();
    }

    public PaperReceipt(Long id, Long orderKey, Integer userId, Double longitude, Double latitude, String location) {
        this.orderKey = orderKey;
        this.userId = userId;
        this.createTime = new Date();
        this.longitude = longitude;
        this.latitude = latitude;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(Long orderKey) {
        this.orderKey = orderKey;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getReceiptCount() {
        return receiptCount;
    }

    public void setReceiptCount(Integer receiptCount) {
        this.receiptCount = receiptCount;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
