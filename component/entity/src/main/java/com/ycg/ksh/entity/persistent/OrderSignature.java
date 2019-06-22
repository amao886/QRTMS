package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`T_ORDER_SIGNATURE`")
public class OrderSignature extends BaseEntity {
    /**
     * 订单明细编号
     */
    @Id
    @Column(name = "`ID`")
    private Long id;

    /**
     * 订单编号
     */
    @Column(name = "`ORDER_ID`")
    private Long orderId;

    /**
     * 签章角色(1:发货方、2:承运商、3:收货方)
     */
    @Column(name = "`SIGN_ROLE`")
    private Integer signRole;

    /**
     * 签章公司编号
     */
    @Column(name = "`COMPANY_ID`")
    private Long companyId;

    /**
     * 签章人
     */
    @Column(name = "`USER_ID`")
    private Integer userId;

    /**
     * 创建时间
     */
    @Column(name = "`CREATE_TIME`")
    private Date createTime;

    /**
     * 公章
     */
    @Column(name = "`SEAL_ID`")
    private Long sealId;
    /**
     * 个人印章
     */
    @Column(name = "`PERSONAL_SEAL`")
    private Long personalSeal;

    /**
     * 获取订单明细编号
     *
     * @return ID - 订单明细编号
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置订单明细编号
     *
     * @param id 订单明细编号
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取订单编号
     *
     * @return ORDER_ID - 订单编号
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 设置订单编号
     *
     * @param orderId 订单编号
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取签章角色(1:发货方、2:承运商、3:收货方)
     *
     * @return SIGN_ROLE - 签章角色(1:发货方、2:承运商、3:收货方)
     */
    public Integer getSignRole() {
        return signRole;
    }

    /**
     * 设置签章角色(1:发货方、2:承运商、3:收货方)
     *
     * @param signRole 签章角色(1:发货方、2:承运商、3:收货方)
     */
    public void setSignRole(Integer signRole) {
        this.signRole = signRole;
    }

    /**
     * 获取签章公司编号
     *
     * @return COMPANY_ID - 签章公司编号
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     * 设置签章公司编号
     *
     * @param companyId 签章公司编号
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取签章人
     *
     * @return USER_ID - 签章人
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置签章人
     *
     * @param userId 签章人
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
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

    /**
     * 获取公章
     *
     * @return SEAL_ID - 公章
     */
    public Long getSealId() {
        return sealId;
    }

    /**
     * 设置公章
     *
     * @param sealId 公章
     */
    public void setSealId(Long sealId) {
        this.sealId = sealId;
    }

    public Long getPersonalSeal() {
        return personalSeal;
    }

    public void setPersonalSeal(Long personalSeal) {
        this.personalSeal = personalSeal;
    }
}