package com.ycg.ksh.entity.persistent.depot;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Table(name = "`T_INBOUND_ORDER`")
public class InboundOrder extends BaseEntity {

    /**
     * 编号
     */
    @Id
    @Column(name = "`KEY`")
    private Long key;
    /**
     * 企业编号
     */
    @Column(name = "`COMPANY_KEY`")
    private Long companyKey;

    /**
     * 批次号
     */
    @Column(name = "`BATCH_NUMBER`")
    private String batchNumber;

    /**
     * 送货单号
     */
    @Column(name = "`DELIVERY_NO`")
    private String deliveryNo;

    /**
     * 客户ID
     */
    @Column(name = "`CUSTOMER_ID`")
    private Long customerId;

    /**
     * 客户名称
     */
    @Column(name = "`CUSTOMER_NAME`")
    private String customerName;

    /**
     * 物料名称
     */
    @Column(name = "`MATERIAL_NAME`")
    private String materialName;

    /**
     * 实际发货日期
     */
    @Column(name = "`DELIVERY_TIME`")
    private Date deliveryTime;

    /**
     * 拣配数量
     */
    @Column(name = "`PICKING_QUANTITY`")
    private Integer pickingQuantity;

    /**
     * 物料号
     */
    @Column(name = "`PICKING_NO`")
    private String pickingNo;

    /**
     * 交货数量
     */
    @Column(name = "`DELIVERY_QUANTITY`")
    private Integer deliveryQuantity;

    /**
     * 车牌
     */
    @Column(name = "`LICENSE_PLATE`")
    private String licensePlate;

    /**
     * 入库时间
     */
    @Column(name = "`STORAGE_TIME`")
    private Date storageTime;

    /**
     * 导入次数
     */
    @Column(name = "`IMPORT_TIMES`")
    private Integer importTimes;

    /**
     * 操作人
     */
    @Column(name = "`OPERATOR_ID`")
    private Integer operatorId;

    /**
     * 修改时间
     */
    @Column(name = "`UPDATE_TIME`")
    private Date updateTime;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public InboundOrder() {
    }

    public Long getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(Long companyKey) {
        this.companyKey = companyKey;
    }

    public InboundOrder(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getImportTimes() {
        return importTimes;
    }

    public void setImportTimes(Integer importTimes) {
        this.importTimes = importTimes;
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

    public String getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getPickingQuantity() {
        return pickingQuantity;
    }

    public void setPickingQuantity(Integer pickingQuantity) {
        this.pickingQuantity = pickingQuantity;
    }

    public String getPickingNo() {
        return pickingNo;
    }

    public void setPickingNo(String pickingNo) {
        this.pickingNo = pickingNo;
    }

    public Integer getDeliveryQuantity() {
        return deliveryQuantity;
    }

    public void setDeliveryQuantity(Integer deliveryQuantity) {
        this.deliveryQuantity = deliveryQuantity;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Date getStorageTime() {
        return storageTime;
    }

    public void setStorageTime(Date storageTime) {
        this.storageTime = storageTime;
    }
}
