package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "`T_COMPANY_SIGNED`")
public class CompanySigned extends BaseEntity {

    /**
     * 编号
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
     * 签署总量
     */
    @Column(name = "`SIGN_TOTAL`")
    private Integer signTotal;

    /**
     * 已使用的签署数量
     */
    @Column(name = "`SIGN_USED`")
    private Integer signUsed;

    public Long getId() {
        return id;
    }

    public CompanySigned setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public CompanySigned setCompanyId(Long companyId) {
        this.companyId = companyId;
        return this;
    }

    public Integer getSignTotal() {
        return signTotal;
    }

    public CompanySigned setSignTotal(Integer signTotal) {
        this.signTotal = signTotal;
        return this;
    }

    public Integer getSignUsed() {
        return signUsed;
    }

    public CompanySigned setSignUsed(Integer signUsed) {
        this.signUsed = signUsed;
        return this;
    }
}
