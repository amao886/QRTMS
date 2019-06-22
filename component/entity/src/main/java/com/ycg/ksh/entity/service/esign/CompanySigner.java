package com.ycg.ksh.entity.service.esign;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/14
 */

import com.ycg.ksh.entity.service.enterprise.CustomerConcise;

import java.util.Date;

/**
 * 企业签署信息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/14
 */
public class CompanySigner extends CustomerConcise{

    private String sealDate; //印章图片地址
    private String userSealPath; //个人签章图片信息
    private Integer employeeKey; //编号
    private String employeeName; //名称
    private String mobilePhone; //用户手机
    private String idCardNo; //身份证号码
    private Date sealTime;//签章时间

    public CompanySigner() {
    }

    public CompanySigner(Long customerKey, String customerName) {
        super(customerKey, customerName);
    }

    public CompanySigner(Long customerKey, String customerName, Long companyKey, String companyName) {
        super(customerKey, customerName, companyKey, companyName);
    }

    public String getSealDate() {
        return sealDate;
    }

    public void setSealDate(String sealDate) {
        this.sealDate = sealDate;
    }

    public String getUserSealPath() {
        return userSealPath;
    }

    public void setUserSealPath(String userSealPath) {
        this.userSealPath = userSealPath;
    }

    public Integer getEmployeeKey() {
        return employeeKey;
    }

    public void setEmployeeKey(Integer employeeKey) {
        this.employeeKey = employeeKey;
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

    public Date getSealTime() {
        return sealTime;
    }

    public void setSealTime(Date sealTime) {
        this.sealTime = sealTime;
    }
}
