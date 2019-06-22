package com.ycg.ksh.entity.persistent.enterprise;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 要车关联
 *
 * @author: wangke
 * @create: 2018-10-22 14:16
 **/
@Table(name = "`T_VEHICLE_DESIGNATE`")
public class VehicleDesignate extends BaseEntity {

    /**
     * 编号
     */
    @Id
    @Column(name = "`KEY`")
    private Long key;

    /**
     * 联系人
     */
    @Column(name = "`CONTACT`")
    private String contact;

    /**
     * 联系电话
     */
    @Column(name = "`CONTACT_NUMBER`")
    private String contactNumber;

    /**
     * 物流商ID
     */
    @Column(name = "`CONVEY_ID`")
    private Long conveyId;

    /**
     * 上级企业ID
     */
    @Column(name = "`LAST_COMPANY_KEY`")
    private Long lastCompanyKey;

    /**
     * 要车单ID
     */
    @Column(name = "`V_ID`")
    private Long vId;

    /**
     * 创建时间
     */
    @Column(name = "`CREATE_TIME`")
    private Date createTime;

    public VehicleDesignate() {
    }

    public VehicleDesignate(Long vId) {
        this.vId = vId;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Long getConveyId() {
        return conveyId;
    }

    public void setConveyId(Long conveyId) {
        this.conveyId = conveyId;
    }

    public Long getLastCompanyKey() {
        return lastCompanyKey;
    }

    public void setLastCompanyKey(Long lastCompanyKey) {
        this.lastCompanyKey = lastCompanyKey;
    }

    public Long getvId() {
        return vId;
    }

    public void setvId(Long vId) {
        this.vId = vId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
