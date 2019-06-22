package com.ycg.ksh.entity.persistent.enterprise;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.StringUtils;

import javax.persistence.*;

@Table(name = "`T_ORDER_CONVEY`")
public class OrderConvey extends BaseEntity {
    /**
     * 订单编号
     */
    @Id
    @Column(name = "`KEY`")
    private Long key;

    /**
     * 车牌号
     */
    @Column(name = "`CARE_NO`")
    private String careNo;

    /**
     * 司机姓名
     */
    @Column(name = "`DRIVER_NAME`")
    private String driverName;

    /**
     * 司机联系方式
     */
    @Column(name = "`DRIVER_CONTACT`")
    private String driverContact;

    public boolean isEmpty(){
        return StringUtils.isBlank(careNo) && StringUtils.isBlank(driverName) && StringUtils.isBlank(driverContact);
    }

    /**
     * 获取订单编号
     *
     * @return KEY - 订单编号
     */
    public Long getKey() {
        return key;
    }

    /**
     * 设置订单编号
     *
     * @param key 订单编号
     */
    public void setKey(Long key) {
        this.key = key;
    }

    /**
     * 获取车牌号
     *
     * @return CARE_NO - 车牌号
     */
    public String getCareNo() {
        return careNo;
    }

    /**
     * 设置车牌号
     *
     * @param careNo 车牌号
     */
    public void setCareNo(String careNo) {
        this.careNo = careNo;
    }

    /**
     * 获取司机姓名
     *
     * @return DRIVER_NAME - 司机姓名
     */
    public String getDriverName() {
        return driverName;
    }

    /**
     * 设置司机姓名
     *
     * @param driverName 司机姓名
     */
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    /**
     * 获取司机联系方式
     *
     * @return DRIVER_CONTACT - 司机联系方式
     */
    public String getDriverContact() {
        return driverContact;
    }

    /**
     * 设置司机联系方式
     *
     * @param driverContact 司机联系方式
     */
    public void setDriverContact(String driverContact) {
        this.driverContact = driverContact;
    }
}