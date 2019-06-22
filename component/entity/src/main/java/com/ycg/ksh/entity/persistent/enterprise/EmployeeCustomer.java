package com.ycg.ksh.entity.persistent.enterprise;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;

@Table(name = "`T_EMPLOYEE_CUSTOMER`")
public class EmployeeCustomer extends BaseEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "`ID`")
    private Long id;

    /**
     * 员工编号
     */
    @Column(name = "`EMPLOYEE_KEY`")
    private Integer employeeKey;

    /**
     * 司机的用户编号
     */
    @Column(name = "`CUSTOMER_KEY`")
    private Long customerKey;

    public EmployeeCustomer() {
    }
    public EmployeeCustomer(Integer employeeKey, Long customerKey) {
        this.employeeKey = employeeKey;
        this.customerKey = customerKey;
    }
    public EmployeeCustomer(Long id, Integer employeeKey, Long customerKey) {
        this.id = id;
        this.employeeKey = employeeKey;
        this.customerKey = customerKey;
    }

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
     * 获取员工编号
     *
     * @return EMPLOYEE_KEY - 员工编号
     */
    public Integer getEmployeeKey() {
        return employeeKey;
    }

    /**
     * 设置员工编号
     *
     * @param employeeKey 员工编号
     */
    public void setEmployeeKey(Integer employeeKey) {
        this.employeeKey = employeeKey;
    }

    /**
     * 获取司机的用户编号
     *
     * @return CUSTOMER_KEY - 司机的用户编号
     */
    public Long getCustomerKey() {
        return customerKey;
    }

    /**
     * 设置司机的用户编号
     *
     * @param customerKey 司机的用户编号
     */
    public void setCustomerKey(Long customerKey) {
        this.customerKey = customerKey;
    }
}