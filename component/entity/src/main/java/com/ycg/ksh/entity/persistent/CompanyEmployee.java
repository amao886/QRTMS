package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`T_COMPANY_EMPLOYEE`")
public class CompanyEmployee extends BaseEntity {
    /**
     * 企业编号
     */
    @Column(name = "`COMPANY_ID`")
    private Long companyId;

    /**
     * 用户编号
     */
    @Id
    @Column(name = "`EMPLOYEE_ID`")
    private Integer employeeId;

    /**
     * 添加时间
     */
    @Column(name = "`CREATE_TIME`")
    private Date createTime;

    /**
     * 操作人
     */
    @Column(name = "`USER_ID`")
    private Integer userId;

    /**
     * 0:普通员工,1:管理
     */
    @Column(name = "`EMPLOYEE_TYPE`")
    private Integer employeeType;

    /**
     * 员工姓名
     */
    @Column(name = "`EMPLOYEE_NAME`")
    private String employeeName;

    /**
     * 用户状态
     */
    @Column(name = "`USER_FETTLE`")
    private Integer userFettle;


    public CompanyEmployee() {
    }


    public Integer getUserFettle() {
        return userFettle;
    }

    public void setUserFettle(Integer userFettle) {
        this.userFettle = userFettle;
    }

    public CompanyEmployee(Long companyId, Integer employeeId) {
        this.companyId = companyId;
        this.employeeId = employeeId;
    }

    /**
     * 获取企业编号
     *
     * @return COMPANY_ID - 企业编号
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     * 设置企业编号
     *
     * @param companyId 企业编号
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取用户编号
     *
     * @return EMPLOYEE_ID - 用户编号
     */
    public Integer getEmployeeId() {
        return employeeId;
    }

    /**
     * 设置用户编号
     *
     * @param employeeId 用户编号
     */
    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * 获取添加时间
     *
     * @return CREATE_TIME - 添加时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置添加时间
     *
     * @param createTime 添加时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取操作人
     *
     * @return USER_ID - 操作人
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置操作人
     *
     * @param userId 操作人
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取0:普通员工,1:管理
     *
     * @return EMPLOYEE_TYPE - 0:普通员工,1:管理
     */
    public Integer getEmployeeType() {
        return employeeType;
    }

    /**
     * 设置0:普通员工,1:管理
     *
     * @param employeeType 0:普通员工,1:管理
     */
    public void setEmployeeType(Integer employeeType) {
        this.employeeType = employeeType;
    }

    /**
     * 获取员工姓名
     *
     * @return EMPLOYEE_NAME - 员工姓名
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * 设置员工姓名
     *
     * @param employeeName 员工姓名
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}