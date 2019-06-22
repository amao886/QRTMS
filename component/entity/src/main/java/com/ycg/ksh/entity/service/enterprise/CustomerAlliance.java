package com.ycg.ksh.entity.service.enterprise;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/16
 */

import com.ycg.ksh.entity.persistent.enterprise.CompanyCustomer;
import org.apache.commons.beanutils.BeanUtils;

/**
 * 企业客户关系联合实体
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/16
 */
public class CustomerAlliance extends CompanyCustomer {

    private CompanyConcise company;
    private String regName;//企业注册人名称(user.unamezn)
    private String regMobile;//企业注册人手机号(user.mobilephone)

    private boolean commonly = false;//是否常用

    public CustomerAlliance() {
    }

    public CustomerAlliance(CompanyCustomer customer) throws Exception {
        BeanUtils.copyProperties(this, customer);
    }

    public CompanyConcise getCompany() {
        return company;
    }

    public void setCompany(CompanyConcise company) {
        this.company = company;
    }

    public String getRegName() {
        return regName;
    }

    public void setRegName(String regName) {
        this.regName = regName;
    }

    public String getRegMobile() {
        return regMobile;
    }

    public void setRegMobile(String regMobile) {
        this.regMobile = regMobile;
    }

    public boolean isCommonly() {
        return commonly;
    }

    public void setCommonly(boolean commonly) {
        this.commonly = commonly;
    }
}
