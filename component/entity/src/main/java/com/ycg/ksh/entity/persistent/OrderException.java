package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`T_ORDER_EXCEPTION`")
public class OrderException extends BaseEntity {
    /**
     * 订单编号
     */
    @Id
    @Column(name = "`ORDER_NO`")
    private Long orderNo;

    /**
     * 异常描述
     */
    @Column(name = "`CONTENT`")
    private String content;

    /**
     * 创建人
     */
    @Column(name = "`USER_ID`")
    private Integer userId;

    /**
     * 创建时间
     */
    @Column(name = "`CREATE_TIME`")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "`UPDATE_TIME`")
    private Date updateTime;

    /**
     * 异常类型
     */
    @Column(name = "`TYPE`")
    private Integer type;

    public OrderException() {
    }

    public OrderException(Long orderNo, String content, Integer userId, Date createTime, Integer type) {
        this.orderNo = orderNo;
        this.content = content;
        this.userId = userId;
        this.createTime = createTime;
        this.type = type;
    }

    public OrderException(String content, Integer userId) {
        this.content = content;
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取订单编号
     *
     * @return ORDER_NO - 订单编号
     */
    public Long getOrderNo() {
        return orderNo;
    }

    /**
     * 设置订单编号
     *
     * @param orderNo 订单编号
     */
    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取电子回单存储路径
     *
     * @return CONTENT - 电子回单存储路径
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置电子回单存储路径
     *
     * @param content 电子回单存储路径
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取创建人
     *
     * @return USER_ID - 创建人
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置创建人
     *
     * @param userId 创建人
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
     * 获取更新时间
     *
     * @return UPDATE_TIME - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}