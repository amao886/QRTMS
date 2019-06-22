package com.ycg.ksh.entity.service.enterprise;

import com.ycg.ksh.entity.persistent.CompanySeal;
import com.ycg.ksh.entity.persistent.PersonalSeal;
import com.ycg.ksh.entity.persistent.UserLegalize;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Collection;

/**
 * 个人认证授权整合类
 *
 * @author wangke
 * @create 2018-06-27 10:43
 **/
public class MergeUserCertified extends UserLegalize {

    //公司印章
    private Collection<CompanySeal> enterprises;

    //个人印章
    private Collection<PersonalSeal> personals;

    public MergeUserCertified() {
    }

    public MergeUserCertified(UserLegalize userLegalize) throws Exception {
        super();
        BeanUtils.copyProperties(this, userLegalize);
    }

    public static MergeUserCertified buildCertified(UserLegalize userLegalize) throws Exception {
        if (userLegalize instanceof MergeUserCertified) {
            return (MergeUserCertified) userLegalize;
        }
        return new MergeUserCertified(userLegalize);
    }

    public Collection<CompanySeal> getEnterprises() {
        return enterprises;
    }

    public void setEnterprises(
        Collection<CompanySeal> enterprises) {
        this.enterprises = enterprises;
    }

    public Collection<PersonalSeal> getPersonals() {
        return personals;
    }

    public void setPersonals(
        Collection<PersonalSeal> personals) {
        this.personals = personals;
    }
}
