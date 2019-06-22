package com.ycg.ksh.entity.service.depot;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/7
 */

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Date;

/**
 * 仓库查询
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/7
 */
public class DepotSearch extends BaseEntity {

    private String likefirst;//批次号，单号，车牌号
    private String likesecond;
    private Long companyKey;//企业编号
    private String batchNumber;//批次号
    private String deliveryNo;//送货单号
    private String customerName;//客户名称
    private String materialName;//物料名称
    private String licensePlate;//车牌号
    private Date firstTime;
    private Date secondTime;

    public String getLikesecond() {
        return likesecond;
    }

    public void setLikesecond(String likesecond) {
        this.likesecond = likesecond;
    }

    public String getLikefirst() {
        return likefirst;
    }

    public void setLikefirst(String likefirst) {
        this.likefirst = likefirst;
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

    public Date getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Date firstTime) {
        this.firstTime = firstTime;
    }

    public Date getSecondTime() {
        return secondTime;
    }

    public void setSecondTime(Date secondTime) {
        this.secondTime = secondTime;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Long getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(Long companyKey) {
        this.companyKey = companyKey;
    }
}
