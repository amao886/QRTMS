package com.ycg.ksh.core.driver.application;

import com.ycg.ksh.common.entity.Page;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.core.driver.application.dto.CompanyDriverDto;
import com.ycg.ksh.core.driver.application.dto.DriverAwaitInfoDto;
import com.ycg.ksh.core.driver.application.dto.DriverDto;
import com.ycg.ksh.core.driver.application.dto.InviteAskDto;
import com.ycg.ksh.entity.service.PageScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * 司机应用服务 - 控制层调用
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/03 0003
 */
public interface DriverApplicationService {


    final Logger logger = LoggerFactory.getLogger(DriverApplicationService.class);

    /**
     * 查看司机信息
     * @param driverKey  司机标识
     *
     * @return
     *
     * @throws BusinessException
     * @throws ParameterException
     */
    DriverDto getDriver(Long driverKey) throws ParameterException, BusinessException;

    /**
     * 查看企业司机信息
     * @param driverKey  企业司机标识
     *
     * @return
     *
     * @throws BusinessException
     * @throws ParameterException
     */
    CompanyDriverDto getCompanyDriver(Long driverKey) throws  ParameterException, BusinessException;
    /**
     *  注册司机
     * @param userKey  用户编号
     * @param driverDto
     * @throws BusinessException
     * @throws ParameterException
     */
    void register(Integer userKey, DriverDto driverDto) throws  ParameterException, BusinessException;

    /**
     *  更新司机资料
     * @param driverDto
     * @throws BusinessException
     * @throws ParameterException
     */
    void modify(DriverDto driverDto) throws  ParameterException, BusinessException;
    /**
     *  更新企业司机资料
     * @param driverDto
     * @throws BusinessException
     * @throws ParameterException
     */
    void modify(CompanyDriverDto driverDto) throws  ParameterException, BusinessException;

    /**
     * 发布等货信息
     * @param awaitInfoDto
     * @throws BusinessException
     * @throws ParameterException
     */
    void releaseWaitInfo(DriverAwaitInfoDto awaitInfoDto) throws   ParameterException, BusinessException;


    /**
     * 查询企业司机信息
     * @param companyKey  企业编号
     * @param status  状态
     * @param likeString  模糊查询字段
     * @param scope  分页信息
     *
     * @return
     *
     * @throws BusinessException
     * @throws ParameterException
     */
    Page<CompanyDriverDto> searchCompanyDriver(Long companyKey, Integer status, String likeString, PageScope scope) throws  ParameterException, BusinessException;
    /**
     * 企业查询等货信息
     *
     * @param companyKey 企业编号
     * @param carType    车型
     * @param length     车长
     * @param start    等货起点
     * @param scope      分页信息
     *
     * @return 满足条件的等货信息
     *
     * @throws BusinessException
     * @throws ParameterException
     */
    Page<DriverAwaitInfoDto> searchAwaitInfoByCompany(Long companyKey, Integer carType, Float length, String start, PageScope scope) throws  ParameterException, BusinessException;

    /**
     * 司机查询其发布的等货信息
     * @param driverKey  司机标识
     * @param startTime  开始事件
     * @param start  等货起点
     * @param scope       分页信息
     * @return 满足条件的等货信息
     *
     * @throws BusinessException
     * @throws ParameterException
     */
    Page<DriverAwaitInfoDto> searchAwaitInfoByDriver(Long driverKey, LocalDateTime startTime, String start, PageScope scope) throws   ParameterException, BusinessException;

    /**
     * 发起邀请
     * @param askDto  邀请信息
     *
     * @throws BusinessException
     * @throws ParameterException
     */
    void inviteAsk(InviteAskDto askDto) throws  ParameterException, BusinessException;

    /**
     * 处理邀请
     * @param driverKey  发起处理的司机标识
     * @param inviteKey  要处理的邀请编号
     * @param result  处理结果(true:同意,false:失败)
     *
     * @throws BusinessException
     * @throws ParameterException
     */
    void handleInviteAsk(Long driverKey, Long inviteKey, Boolean result) throws  ParameterException, BusinessException;
}
