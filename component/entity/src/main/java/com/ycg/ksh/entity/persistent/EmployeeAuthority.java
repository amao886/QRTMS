package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`T_EMPLOYEE_AUTHORITY`")
public class EmployeeAuthority extends BaseEntity{
    /**
     * 员工权限主键
     */
    @Id
    @Column(name = "`ID`")
    private Long id;

    /**
     * 企业编号
     */
    @Column(name = "`COMPANY_ID`")
    private Long companyId;

    /**
     * 员工编号(用户ID)
     */
    @Column(name = "`EMPLOYEE_ID`")
    private Integer employeeId;

    /**
     * 权限编号
     */
    @Column(name = "`AUTHORITY_ID`")
    private Integer authorityId;

    /**
     * 保存时间
     */
    @Column(name = "`CREATE_TIME`")
    private Date createTime;

    public EmployeeAuthority() {
    }

    public EmployeeAuthority(Long companyId, Integer employeeId) {
        this.companyId = companyId;
        this.employeeId = employeeId;
    }

    public EmployeeAuthority(Long id, Long companyId, Integer employeeId, Integer authorityId, Date createTime) {
        this.id = id;
        this.companyId = companyId;
        this.employeeId = employeeId;
        this.authorityId = authorityId;
        this.createTime = createTime;
    }

    /**
     * 获取员工权限主键
     *
     * @return ID - 员工权限主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置员工权限主键
     *
     * @param id 员工权限主键
     */
    public void setId(Long id) {
        this.id = id;
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
     * 获取员工编号(用户ID)
     *
     * @return EMPLOYEE_ID - 员工编号(用户ID)
     */
    public Integer getEmployeeId() {
        return employeeId;
    }

    /**
     * 设置员工编号(用户ID)
     *
     * @param employeeId 员工编号(用户ID)
     */
    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * 获取权限编号
     *
     * @return AUTHORITY_ID - 权限编号
     */
    public Integer getAuthorityId() {
        return authorityId;
    }

    /**
     * 设置权限编号
     *
     * @param authorityId 权限编号
     */
    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }

    /**
     * 获取保存时间
     *
     * @return CREATE_TIME - 保存时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置保存时间
     *
     * @param createTime 保存时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}