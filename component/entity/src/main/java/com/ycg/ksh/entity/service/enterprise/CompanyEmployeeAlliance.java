package com.ycg.ksh.entity.service.enterprise;

import com.ycg.ksh.entity.persistent.CompanyEmployee;
import com.ycg.ksh.entity.persistent.UserLegalize;
import com.ycg.ksh.entity.service.ConciseUser;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Collection;

/**
 * 整合类
 *
 * @author wangke
 * @create 2018-05-04 15:37
 **/
public class CompanyEmployeeAlliance extends CompanyEmployee {

    private ConciseUser user;
    private UserLegalize legalize;
    //已有的权限
    private Collection<Integer> authoritys;
    private Integer companySealCount;//企业授权印章数

    public CompanyEmployeeAlliance(CompanyEmployee employee) throws Exception {
        this();
        BeanUtils.copyProperties(this, employee);
    }

    public CompanyEmployeeAlliance() {
    }

    public Collection<Integer> getAuthoritys() {
        return authoritys;
    }

    public void setAuthoritys(Collection<Integer> authoritys) {
        this.authoritys = authoritys;
    }

    public ConciseUser getUser() {
        return user;
    }

    public void setUser(ConciseUser user) {
        this.user = user;
    }

    public UserLegalize getLegalize() {
        return legalize;
    }

    public void setLegalize(UserLegalize legalize) {
        this.legalize = legalize;
    }

    public Integer getCompanySealCount() {
        return companySealCount;
    }

    public void setCompanySealCount(Integer companySealCount) {
        this.companySealCount = companySealCount;
    }
}
