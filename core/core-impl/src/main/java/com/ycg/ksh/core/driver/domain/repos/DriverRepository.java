package com.ycg.ksh.core.driver.domain.repos;

import com.ycg.ksh.core.common.domain.DomainRepository;
import com.ycg.ksh.core.driver.domain.model.Driver;
import com.ycg.ksh.core.driver.domain.model.DriverCar;

import java.util.Collection;

/**
 * 司机资源库
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/03 0003
 */
public interface DriverRepository  extends DomainRepository<Driver, Long> {

    /**
     * 检测指定用户是否已经注册司机
     * @param driverKey  司机标识
     * @return  true:已经注册, false:未注册
     */
    boolean existByDriverKey(Long driverKey);
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
    Collection<DriverCar> listCarByLicense(Collection<String> licenses);
}
