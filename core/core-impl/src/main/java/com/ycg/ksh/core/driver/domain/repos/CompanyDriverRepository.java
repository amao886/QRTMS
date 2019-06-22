package com.ycg.ksh.core.driver.domain.repos;

import com.ycg.ksh.common.entity.Page;
import com.ycg.ksh.core.common.domain.DomainRepository;
import com.ycg.ksh.core.driver.domain.model.CompanyDriver;
import com.ycg.ksh.core.driver.domain.model.CompanyDriverCar;
import com.ycg.ksh.core.driver.infrastructure.persistence.CompanyDriverCondition;

import java.util.Collection;

/**
 * 司机资源库
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/03 0003
 */
public interface CompanyDriverRepository extends DomainRepository<CompanyDriver, Long> {

    /**
     *  根据司机标识获取司机聚合
     * @param companyKey  企业标识
     * @param driverKey  司机标识
     *
     * @return
     */
    CompanyDriver findByDriverKey(Long companyKey, Long driverKey);
    /**
     * 检测指定手机号是否已经注册司机,并且不是指定司机
     * @param phnoe  手机号
     * @param driverKey  司机标识
     * @return  true:已经注册, false:未注册
     */
    boolean existByPhnoe(String phnoe, Long driverKey);

    /**
     *  根据车牌号查询车辆信息
     * @param licenses 车牌号
     *
     * @return
     */
    Collection<CompanyDriverCar> listCarByLicense(Collection<String> licenses);


    /**
     *  根据车牌号查询车辆信息
     * @param condition
     * @param num
     * @param size
     * @return
     */
    Page<CompanyDriver> searchCompanyDriver(CompanyDriverCondition condition, int num, int size);
}
