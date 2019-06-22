package com.ycg.ksh.entity.persistent.user;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;

@Table(name = "`T_USER_RESOURCE`")
public class UserResource extends BaseEntity {
    /**
     * 用户编号
     */
    @Id
    @Column(name = "`USER_ID`")
    private Integer userId;

    /**
     * 积分
     */
    @Column(name = "`INTEGRAL`")
    private Long integral;

    /**
     * 金额(单位分)
     */
    @Column(name = "`MONEY`")
    private Long money;
    
    public UserResource(){
    	super();
    }
    
    public UserResource(Integer userId,Long integral,Long money){
    	super();
    	this.userId = userId;
    	this.integral = integral;
    	this.money = money;
    }

    /**
     * 获取用户编号
     *
     * @return USER_ID - 用户编号
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户编号
     *
     * @param userId 用户编号
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取积分
     *
     * @return INTEGRAL - 积分
     */
    public Long getIntegral() {
        return integral;
    }

    /**
     * 设置积分
     *
     * @param integral 积分
     */
    public void setIntegral(Long integral) {
        this.integral = integral;
    }

    /**
     * 获取金额
     *
     * @return MONEY - 金额
     */
    public Long getMoney() {
        return money;
    }

    /**
     * 设置金额
     *
     * @param money 金额
     */
    public void setMoney(Long money) {
        this.money = money;
    }
}