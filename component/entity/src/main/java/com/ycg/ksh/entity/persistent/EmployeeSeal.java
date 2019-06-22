package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 个人印章关联
 *
 * @author wangke
 * @create 2018-06-20 14:31
 **/
@Table(name = "`T_EMPLOYEE_SEAL`")
public class EmployeeSeal extends BaseEntity {

    /**
     * 主键
     */
    @Id
    @Column(name = "`ID`")
    private Long id;

    /**
     * 公司ID
     */
    @Column(name = "`COMPANY_ID`")
    private Long companyId;

    /**
     * 个人ID
     */
    @Column(name = "`EMPLOYEE_ID`")
    private Integer employeeId;

    /**
     * 印章ID
     */
    @Column(name = "`AUTHORIZE_SEAL_ID`")
    private Long authorizeSealId;

    /**
     * 授权时间
     */
    @Column(name = "`AUTHORIZE_TIME`")
    private Date authorizeTime;

    public EmployeeSeal() {

    }

    public EmployeeSeal(Long authorizeSealId) {
        this.authorizeSealId = authorizeSealId;
    }

    public EmployeeSeal(Long companyId, Integer employeeId) {
        this.companyId = companyId;
        this.employeeId = employeeId;
    }

    public EmployeeSeal(Long companyId, Integer employeeId, Long authorizeSealId) {
        this.companyId = companyId;
        this.employeeId = employeeId;
        this.authorizeSealId = authorizeSealId;
    }

    public EmployeeSeal(Long id, Long companyId, Integer employeeId, Long authorizeSealId, Date authorizeTime) {
        this.id = id;
        this.companyId = companyId;
        this.employeeId = employeeId;
        this.authorizeSealId = authorizeSealId;
        this.authorizeTime = authorizeTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Long getAuthorizeSealId() {
        return authorizeSealId;
    }

    public void setAuthorizeSealId(Long authorizeSealId) {
        this.authorizeSealId = authorizeSealId;
    }

    public Date getAuthorizeTime() {
        return authorizeTime;
    }

    public void setAuthorizeTime(Date authorizeTime) {
        this.authorizeTime = authorizeTime;
    }
}
