package com.ycg.ksh.entity.service.moutai;

import com.ycg.ksh.common.entity.RequestEntity;

public class MoutaiCustomerSearch extends RequestEntity {
    /**
     * 客户编码
     */
    private String customerNo;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 联系人
     */
    private String contactName;

    /**
     * 联系电话
     */
    private String contactPhone;

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public void handle(){
        customerNo = customerNo == null? "" : customerNo.trim();
        customerName = customerName == null ? "" : customerName.trim();
        contactName = contactName == null ? "" : contactName.trim();
        contactPhone = contactPhone == null ? "" : contactPhone.trim();
    }

}
