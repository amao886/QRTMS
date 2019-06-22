package com.ycg.ksh.entity.service.enterprise;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/24
 */

import com.ycg.ksh.entity.persistent.CompanyEmployee;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * 员工详情
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/24
 */
public class EmployeeAlliance extends CompanyEmployee {

    /**
     * 用户名称
     */
    private String unamezn;
    /**
     * 手机号
     */
    private String mobilephone;

    /**
     * 微信头像
     */
    private String headImg;
    /**
     * 真实姓名
     */
    private String name;

    /**
     * 身份证号码
     */
    private String idCardNo;

    /**
     * 认证时间
     */
    private Date legalizeTime;

    /**
     * 0:没有认证,1:已经认证
     */
    private Integer fettle;

    /**
     * 银行账号
     */
    private String brankCardNo;

    public EmployeeAlliance() {
    }

    public EmployeeAlliance(CompanyEmployee employee){
        try{
            BeanUtils.copyProperties(employee, this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getUnamezn() {
        return unamezn;
    }

    public void setUnamezn(String unamezn) {
        this.unamezn = unamezn;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public Date getLegalizeTime() {
        return legalizeTime;
    }

    public void setLegalizeTime(Date legalizeTime) {
        this.legalizeTime = legalizeTime;
    }

    public Integer getFettle() {
        return fettle;
    }

    public void setFettle(Integer fettle) {
        this.fettle = fettle;
    }

    public String getBrankCardNo() {
        return brankCardNo;
    }

    public void setBrankCardNo(String brankCardNo) {
        this.brankCardNo = brankCardNo;
    }
}
