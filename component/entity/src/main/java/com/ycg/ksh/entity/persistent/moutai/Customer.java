package com.ycg.ksh.entity.persistent.moutai;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`MOUTAI_CUSTOMER`")
public class Customer extends BaseEntity {
    /**
     * 客户编码
     */
    @Id
    @Column(name = "`CUSTOMER_NO`")
    private String customerNo;

    /**
     * 客户名称
     */
    @Column(name = "`CUSTOMER_NAME`")
    private String customerName;

    /**
     * 省
     */
    @Column(name = "`PROVINCE`")
    private String province;

    /**
     * 市
     */
    @Column(name = "`CITY`")
    private String city;

    /**
     * 详细地址
     */
    @Column(name = "`ADDRESS`")
    private String address;

    /**
     * 联系人
     */
    @Column(name = "`CONTACT_NAME`")
    private String contactName;

    /**
     * 联系电话
     */
    @Column(name = "`CONTACT_PHONE`")
    private String contactPhone;

    /**
     * 传真
     */
    @Column(name = "`FAX`")
    private String fax;

    /**
     * 创建人
     */
    @Column(name = "`CREATE_USER_ID`")
    private Integer createUserId;

    /**
     * 创建时间
     */
    @Column(name = "`CREATE_TIME`")
    private Date createTime;

    /**
     * 获取客户编码
     *
     * @return CUSTOMER_NO - 客户编码
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * 设置客户编码
     *
     * @param customerNo 客户编码
     */
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    /**
     * 获取客户名称
     *
     * @return CUSTOMER_NAME - 客户名称
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * 设置客户名称
     *
     * @param customerName 客户名称
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * 获取省
     *
     * @return PROVINCE - 省
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省
     *
     * @param province 省
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 获取市
     *
     * @return CITY - 市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置市
     *
     * @param city 市
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取详细地址
     *
     * @return ADDRESS - 详细地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置详细地址
     *
     * @param address 详细地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取联系人
     *
     * @return CONTACT_NAME - 联系人
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * 设置联系人
     *
     * @param contactName 联系人
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * 获取联系电话
     *
     * @return CONTACT_PHONE - 联系电话
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * 设置联系电话
     *
     * @param contactPhone 联系电话
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    /**
     * 获取传真
     *
     * @return FAX - 传真
     */
    public String getFax() {
        return fax;
    }

    /**
     * 设置传真
     *
     * @param fax 传真
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * 获取创建人
     *
     * @return CREATE_USER_ID - 创建人
     */
    public Integer getCreateUserId() {
        return createUserId;
    }

    /**
     * 设置创建人
     *
     * @param createUserId 创建人
     */
    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * 获取创建时间
     *
     * @return CREATE_TIME - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}