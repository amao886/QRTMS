package com.ycg.ksh.core.driver.domain.repos;

import com.ycg.ksh.common.entity.Page;
import com.ycg.ksh.core.common.domain.DomainRepository;
import com.ycg.ksh.core.driver.domain.model.DriverAwaitInfo;
import com.ycg.ksh.core.driver.infrastructure.persistence.DriverAwaitInfoCondition;

import java.util.Collection;

/**
 * 司机资源库
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/03 0003
 */
public interface DriverAwaitInfoRepository  extends DomainRepository<DriverAwaitInfo, Long> {

    /**
     * 查询指定司机所有有效的等货信息
     * @param driverKey  司机标识
     *
     * @return
     */
    Collection<DriverAwaitInfo> listEffective(Long driverKey);

    Page<DriverAwaitInfo> searchAwaitInfo(DriverAwaitInfoCondition condition, int num, int size);

}
