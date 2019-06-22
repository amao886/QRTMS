package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.entity.persistent.enterprise.AbstractOrder;
import org.apache.commons.beanutils.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`T_ORDER_INVALID`")
public class ObsoleteOrder extends AbstractOrder {

    /**
     * 作废操作人编号
     */
    @Column(name = "`INVALID_USER_ID`")
    private Integer invalidUserId;

    /**
     * 作废时间
     */
    @Column(name = "`INVALID_TIME`")
    private Date invalidTime;

    public ObsoleteOrder() {  }

    public ObsoleteOrder(Order order, Integer invalidUserId, Date invalidTime) {
        this(order);
        this.invalidUserId = invalidUserId;
        this.invalidTime = invalidTime;
    }

    public ObsoleteOrder(Order order){
        try {
            BeanUtils.copyProperties(this, order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取作废操作人编号
     *
     * @return INVALID_USER_ID - 作废操作人编号
     */
    public Integer getInvalidUserId() {
        return invalidUserId;
    }

    /**
     * 设置作废操作人编号
     *
     * @param invalidUserId 作废操作人编号
     */
    public void setInvalidUserId(Integer invalidUserId) {
        this.invalidUserId = invalidUserId;
    }

    /**
     * 获取作废时间
     *
     * @return INVALID_TIME - 作废时间
     */
    public Date getInvalidTime() {
        return invalidTime;
    }

    /**
     * 设置作废时间
     *
     * @param invalidTime 作废时间
     */
    public void setInvalidTime(Date invalidTime) {
        this.invalidTime = invalidTime;
    }
}