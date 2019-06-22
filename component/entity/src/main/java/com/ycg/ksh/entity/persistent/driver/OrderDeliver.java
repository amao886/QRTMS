package com.ycg.ksh.entity.persistent.driver;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`T_ORDER_DELIVER`")
public class OrderDeliver extends BaseEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "`ID`")
    private Long id;

    /**
     * 订单编号
     */
    @Column(name = "`ORDER_KEY`")
    private Long orderKey;

    /**
     * 司机的用户编号
     */
    @Column(name = "`USER_KEY`")
    private Integer userKey;

    /**
     * 记录时间
     */
    @Column(name = "`CREATE_TIME`")
    private Date createTime;

    /**
     * 获取编号
     *
     * @return ID - 编号
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置编号
     *
     * @param id 编号
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取订单编号
     *
     * @return ORDER_KEY - 订单编号
     */
    public Long getOrderKey() {
        return orderKey;
    }

    /**
     * 设置订单编号
     *
     * @param orderKey 订单编号
     */
    public void setOrderKey(Long orderKey) {
        this.orderKey = orderKey;
    }

    /**
     * 获取司机的用户编号
     *
     * @return USER_KEY - 司机的用户编号
     */
    public Integer getUserKey() {
        return userKey;
    }

    /**
     * 设置司机的用户编号
     *
     * @param userKey 司机的用户编号
     */
    public void setUserKey(Integer userKey) {
        this.userKey = userKey;
    }

    /**
     * 获取记录时间
     *
     * @return CREATE_TIME - 记录时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置记录时间
     *
     * @param createTime 记录时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}