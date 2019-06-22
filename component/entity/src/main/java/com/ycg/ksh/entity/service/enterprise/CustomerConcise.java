package com.ycg.ksh.entity.service.enterprise;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/13
 */

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Optional;

/**
 * 客户简要信息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/13
 */
public class CustomerConcise extends BaseEntity {

    protected Long companyKey;//企业编号
    protected String companyName;//客户名称

    protected Long id;//客户编号
    protected String customerName;//客户名称


    public CustomerConcise() {
    }

    public CustomerConcise(Long customerKey, String customerName) {
        this.id = customerKey;
        this.customerName = customerName;
    }

    public CustomerConcise(Long customerKey, String customerName, Long companyKey, String companyName) {
        this(customerKey, customerName);
        this.companyKey = companyKey;
        this.companyName = companyName;
    }

    public boolean isEnterprise() {
        return companyKey != null && companyKey > 0;
    }

    public Long getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(Long companyKey) {
        this.companyKey = companyKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return Optional.ofNullable(companyName).orElse(customerName);
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
