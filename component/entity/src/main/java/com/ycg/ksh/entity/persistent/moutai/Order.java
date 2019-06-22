package com.ycg.ksh.entity.persistent.moutai;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`MOUTAI_ORDER`")
public class Order extends BaseEntity {
    /**
     * 编号(发货编号)
     */
    @Id
    @Column(name = "`ID`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 调拨编号
     */
    @Column(name = "`SCHEDULE_NO`")
    private String scheduleNo;

    /**
     * 客户编号
     */
    @Column(name = "`CUSTOMER_NO`")
    private String customerNo;

    /**
     * 仓库编号
     */
    @Column(name = "`STORE_ID`")
    private Integer storeId;

    /**
     * 承运单位编号
     */
    @Column(name = "`CONVEY_ID`")
    private Long conveyId;

    /**
     * 规格
     */
    @Column(name = "`SPECIFICATION`")
    private String specification;

    /**
     * 瓶数
     */
    @Column(name = "`BOTTLES`")
    private Integer bottles;

    /**
     * 件数
     */
    @Column(name = "`QUANTITY`")
    private Integer quantity;

    /**
     * 购货单位
     */
    @Column(name = "`BUY_ORG`")
    private String buyOrg;
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
     * 是否进行过打印
     */
    @Column(name = "`PRINT_SIGN`")
    private Integer printSign;

    /*
     * 本单的自选省份(不使用默认配置)
     */
    @Column(name = "`PROVINCE`")
    private String province;

    /*
     * 本单的自选城市(不使用默认配置)
     */
    @Column(name = "`CITY`")
    private String city;

    /*
     * 本单的送货地址(不使用默认配置)
     */
    @Column(name = "`ADDRESS`")
    private String address;

    @Column(name = "`CONTACT_NAME`")
    private String contactName;

    @Column(name = "`CONTACT_TEL`")
    private String contactTel;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取编号(发货编号)
     *
     * @return ID - 编号(发货编号)
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置编号(发货编号)
     *
     * @param id 编号(发货编号)
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取调拨编号
     *
     * @return SCHEDULE_NO - 调拨编号
     */
    public String getScheduleNo() {
        return scheduleNo;
    }

    /**
     * 设置调拨编号
     *
     * @param scheduleNo 调拨编号
     */
    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    /**
     * 获取客户编号
     *
     * @return CUSTOMER_NO - 客户编号
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * 设置客户编号
     *
     * @param customerNo 客户编号
     */
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    /**
     * 获取仓库编号
     *
     * @return STORE_ID - 仓库编号
     */
    public Integer getStoreId() {
        return storeId;
    }

    /**
     * 设置仓库编号
     *
     * @param storeId 仓库编号
     */
    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    /**
     * 获取承运单位编号
     *
     * @return CONVEY_ID - 承运单位编号
     */
    public Long getConveyId() {
        return conveyId;
    }

    /**
     * 设置承运单位编号
     *
     * @param conveyId 承运单位编号
     */
    public void setConveyId(Long conveyId) {
        this.conveyId = conveyId;
    }

    /**
     * 获取规格
     *
     * @return SPECIFICATION - 规格
     */
    public String getSpecification() {
        return specification;
    }

    /**
     * 设置规格
     *
     * @param specification 规格
     */
    public void setSpecification(String specification) {
        this.specification = specification;
    }

    /**
     * 获取瓶数
     *
     * @return BOTTLES - 瓶数
     */
    public Integer getBottles() {
        return bottles;
    }

    /**
     * 设置瓶数
     *
     * @param bottles 瓶数
     */
    public void setBottles(Integer bottles) {
        this.bottles = bottles;
    }

    /**
     * 获取件数
     *
     * @return QUANTITY - 件数
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 设置件数
     *
     * @param quantity 件数
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getBuyOrg() {
        return buyOrg;
    }

    public void setBuyOrg(String buyOrg) {
        this.buyOrg = buyOrg;
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

    public Integer getPrintSign() {
        return printSign;
    }

    public void setPrintSign(Integer printSign) {
        this.printSign = printSign;
    }
}