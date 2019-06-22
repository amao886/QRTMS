package com.ycg.ksh.core.driver.domain.repos;

import com.ycg.ksh.core.common.domain.DomainRepository;
import com.ycg.ksh.core.driver.domain.model.InviteAsk;

/**
 * 邀请信息资源库
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/03 0003
 */
public interface InviteAskRepository extends DomainRepository<InviteAsk, Long> {

    boolean validateInviteAsk(Long companyKey, String phone);

}
