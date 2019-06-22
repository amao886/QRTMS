package com.ycg.ksh.entity.service;

import com.ycg.ksh.entity.persistent.SignedRelation;

public class SignRelationRes  extends SignedRelation {
    private Long companyId;

    private Boolean status;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
