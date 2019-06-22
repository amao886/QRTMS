package com.ycg.ksh.entity.persistent.enterprise;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.StringUtils;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "`T_FINANCE`")
public class Finance extends BaseEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "`KEY`")
    private Long key;

    /**
     * 财务数据类型(1:订单...)
     */
    @Column(name = "`FINANCE_TYPE`")
    private Integer financeType;

    /**
     * 数据编号,与财务数据类型相对应
     */
    @Column(name = "`OBJECT_KEY`")
    private String objectKey;

    /**
     * 收入
     */
    @Column(name = "`INCOME`")
    private Double income;

    /**
     * 支出
     */
    @Column(name = "`EXPENDITURE`")
    private Double expenditure;

    /**
     * 创建人
     */
    @Column(name = "`USER_KEY`")
    private Integer userKey;

    /**
     * 创建时间
     */
    @Column(name = "`CREATE_TIME`")
    private Date createTime;

    public boolean isEmpty(){
        return income == null && expenditure == null;
    }

    public Finance(Integer financeType, Serializable objectKey) {
        this.financeType = financeType;
        this.objectKey = String.valueOf(objectKey);
    }

    public Finance() {}

    /**
     * 获取编号
     *
     * @return KEY - 编号
     */
    public Long getKey() {
        return key;
    }

    /**
     * 设置编号
     *
     * @param key 编号
     */
    public void setKey(Long key) {
        this.key = key;
    }

    /**
     * 获取财务数据类型(1:订单...)
     *
     * @return FINANCE_TYPE - 财务数据类型(1:订单...)
     */
    public Integer getFinanceType() {
        return financeType;
    }

    /**
     * 设置财务数据类型(1:订单...)
     *
     * @param financeType 财务数据类型(1:订单...)
     */
    public void setFinanceType(Integer financeType) {
        this.financeType = financeType;
    }

    /**
     * 获取数据编号,与财务数据类型相对应
     *
     * @return OBJECT_KEY - 数据编号,与财务数据类型相对应
     */
    public String getObjectKey() {
        return objectKey;
    }

    /**
     * 设置数据编号,与财务数据类型相对应
     *
     * @param objectKey 数据编号,与财务数据类型相对应
     */
    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    /**
     * 获取收入
     *
     * @return INCOME - 收入
     */
    public Double getIncome() {
        return income;
    }

    /**
     * 设置收入
     *
     * @param income 收入
     */
    public void setIncome(Double income) {
        this.income = income;
    }

    /**
     * 获取支出
     *
     * @return EXPENDITURE - 支出
     */
    public Double getExpenditure() {
        return expenditure;
    }

    /**
     * 设置支出
     *
     * @param expenditure 支出
     */
    public void setExpenditure(Double expenditure) {
        this.expenditure = expenditure;
    }

    /**
     * 获取创建人
     *
     * @return USER_KEY - 创建人
     */
    public Integer getUserKey() {
        return userKey;
    }

    /**
     * 设置创建人
     *
     * @param userKey 创建人
     */
    public void setUserKey(Integer userKey) {
        this.userKey = userKey;
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