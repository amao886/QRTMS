package com.ycg.ksh.service.core.entity.service.adventive;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/29 0029
 */

import com.ycg.ksh.entity.persistent.adventive.Adventive;
import com.ycg.ksh.entity.persistent.adventive.AdventivePull;
import com.ycg.ksh.entity.service.enterprise.CompanyConcise;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Collection;

/**
 * @TODO 类描述
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/29 0029
 */
public class AdventiveAlliance extends Adventive {

    private CompanyConcise company;
    private Collection<AdventivePull> pulls;

    public AdventiveAlliance() {
    }

    public AdventiveAlliance(Adventive adventive) throws Exception {
        BeanUtils.copyProperties(this, adventive);
    }

    public CompanyConcise getCompany() {
        return company;
    }

    public void setCompany(CompanyConcise company) {
        this.company = company;
    }

    public Collection<AdventivePull> getPulls() {
        return pulls;
    }

    public void setPulls(Collection<AdventivePull> pulls) {
        this.pulls = pulls;
    }
}
