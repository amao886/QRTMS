package com.ycg.ksh.entity.service.depot;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/7
 */

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.UserUtil;

import java.util.Date;

/**
 * 出库批次查询信息
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/7
 */
public class DepotBatchSomething extends BaseEntity {

    private Long key;//编号
    private String batchNumber;//批次号
    private String materialName;//品名
    private Integer outboundQuantity;//出库数量
    private String receiveName;//收货名称
    private String shipperName;//发货名称
    private String deliveryNo;//送货单号
    private Date deliveryTime;//发货日期
    private Date storageTime;//入库日期
    private String uname;//操作人
    private String customerName;//客户名称

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Integer getOutboundQuantity() {
        return outboundQuantity;
    }

    public void setOutboundQuantity(Integer outboundQuantity) {
        this.outboundQuantity = outboundQuantity;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Date getStorageTime() {
        return storageTime;
    }

    public void setStorageTime(Date storageTime) {
        this.storageTime = storageTime;
    }

    public String getUname() {
        return UserUtil.decodeName(uname);
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}
