package com.ycg.ksh.entity.service.enterprise;

import com.ycg.ksh.entity.persistent.Company;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Date;

/**
 * 公司/用户/公章整合类
 *
 * @author wangke
 * @create 2018-04-18 17:18
 **/
public class CompanyIntegrationy extends CompanyConcise {

    private String sealDate; //印章图片地址

    private String userSealPath; //个人签章图片信息

    private Integer employeeKey; //编号

    private String employeeName; //名称

    private String mobilePhone; //用户手机

    private String idCardNo; //身份证号码

    private Date sealTime;//签章时间

    public CompanyIntegrationy() {

    }

    public CompanyIntegrationy(Long id, String companyName) {
        super(id, companyName);
    }

    public CompanyIntegrationy(Company company) throws Exception {
        super();
        BeanUtils.copyProperties(this, company);
    }

    public Date getSealTime() {
        return sealTime;
    }

    public void setSealTime(Date sealTime) {
        this.sealTime = sealTime;
    }

    public String getSealDate() {
        return sealDate;
    }

    public void setSealDate(String sealDate) {
        this.sealDate = sealDate;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public Integer getEmployeeKey() {
        return employeeKey;
    }

    public void setEmployeeKey(Integer employeeKey) {
        this.employeeKey = employeeKey;
    }

    public String getUserSealPath() {
        return userSealPath;
    }

    public void setUserSealPath(String userSealPath) {
        this.userSealPath = userSealPath;
    }
}
