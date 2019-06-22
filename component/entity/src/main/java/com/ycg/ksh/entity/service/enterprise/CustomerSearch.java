package com.ycg.ksh.entity.service.enterprise;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/5
 */

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Collection;

/**
 * 企业客户查询
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/5
 */
public class CustomerSearch extends BaseEntity {

    private Integer userKey;//用户编号
    private Long ownerKey;//所属企业编号

    private String likeName;//名称模糊查询
    private Integer type;//客户类型
    private Integer status = 1;//状态

    private Collection<Long> customerKeys;//有权限的客户编号

    private Boolean reg;//是否已经注册
    private Boolean userHost;//是否使用用户热点

    public CustomerSearch() {
    }

    public CustomerSearch(Integer userKey, Long ownerKey) {
        this.userKey = userKey;
        this.ownerKey = ownerKey;
    }

    public Integer getUserKey() {
        return userKey;
    }

    public void setUserKey(Integer userKey) {
        this.userKey = userKey;
    }

    public Long getOwnerKey() {
        return ownerKey;
    }

    public void setOwnerKey(Long ownerKey) {
        this.ownerKey = ownerKey;
    }

    public String getLikeName() {
        return likeName;
    }

    public void setLikeName(String likeName) {
        this.likeName = likeName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getReg() {
        return reg;
    }

    public void setReg(Boolean reg) {
        this.reg = reg;
    }

    public Boolean getUserHost() {
        return userHost;
    }

    public void setUserHost(Boolean userHost) {
        this.userHost = userHost;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Collection<Long> getCustomerKeys() {
        return customerKeys;
    }

    public void setCustomerKeys(Collection<Long> customerKeys) {
        this.customerKeys = customerKeys;
    }
}

