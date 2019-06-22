package com.ycg.ksh.entity.service.enterprise;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.entity.persistent.CompanySigned;

public class CompanySignedResult extends BaseEntity {

    private Long id;
    //公司名称
    private String companyName;
    //签署总量
    private Integer signTotal;
    //使用过的
    private Integer signUsed;
    //未使用的
    private Integer signRest;


    public CompanySignedResult(){}

    public CompanySignedResult(String companyName, CompanySigned signed){
        this.id = signed.getId();
        this.companyName = companyName;
        this.signTotal = signed.getSignTotal();
        this.signUsed = signed.getSignUsed();
        this.signRest = signTotal - signUsed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getSignTotal() {
        return signTotal;
    }

    public void setSignTotal(Integer signTotal) {
        this.signTotal = signTotal;
    }

    public Integer getSignUsed() {
        return signUsed;
    }

    public void setSignUsed(Integer signUsed) {
        this.signUsed = signUsed;
    }

    public Integer getSignRest() {
        return signRest;
    }

    public void setSignRest(Integer signRest) {
        this.signRest = signRest;
    }
}
