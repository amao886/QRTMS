package com.ycg.ksh.core.driver.domain.service;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.core.driver.domain.model.DriverCar;
import com.ycg.ksh.core.driver.domain.model.DriverRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collection;

/**
 * 司机领域服务
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/03 0003
 */
public interface DriverService {

    final Logger logger = LoggerFactory.getLogger(DriverService.class);

    /**
     * 创建企业司机
     * @param companyKey   企业编号
     * @param driverKey  司机编号
     */
    void createCompanyDriver(Long companyKey,  Long driverKey);


    /**
     * 创建司机
     * @param name 姓名
     * @param phone  手机号
     * @param _cars  车辆信息
     * @param _routes  路线信息
     */
    void createDriver(Long userKey, String name, String phone, Collection<DriverCar> _cars, Collection<DriverRoute>  _routes);


    /**
     *  发布等货信息
     * @param driverKey  司机标识
     * @param startTime  开始事件
     * @param car  车辆信息
     * @param route 路线信息
     *
     * @throws BusinessException
     */
    void releaseWaitInfo(Long driverKey, LocalDate startTime, DriverCar car, DriverRoute route) throws BusinessException;


    /**
     * 创建邀请
     * @param companyKey  企业编号
     * @param userKey  用户编号
     * @param userName  用户名称
     * @param driverName  司机名称
     * @param driverPhone  司机电话
     */
    void createInviteAsk(Long companyKey, Integer userKey, String userName, String driverName, String driverPhone);

    /**
     *  处理邀请
     * @param driverKey  司机编号
     * @param inviteKey  邀请编号
     * @param result  结果
     *
     * @throws BusinessException
     */
    void handleInviteAsk(Long driverKey, Long inviteKey, Boolean result) throws BusinessException;

}
