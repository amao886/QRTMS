package com.ycg.ksh.core.driver.domain.service;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.core.Registrys;
import com.ycg.ksh.core.driver.domain.model.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * 司机领域服务实现
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/03 0003
 */
@Service
public class DriverServiceImpl implements DriverService {


    @Override
    public void createCompanyDriver(Long companyKey, Long driverKey) {
        Driver d = Optional.ofNullable(Registrys.driverRepository().findByKey(driverKey)).orElseThrow(()-> new BusinessException("未找到该司机信息"));
        CompanyDriver companyDriver = Registrys.companyDriverRepository().findByDriverKey(companyKey, d.getIdentify());
        if(companyDriver == null){
            new CompanyDriver(companyKey).create(d);
        }else{
            companyDriver.synchronize(d);
        }
    }

    @Override
    public void createDriver(Long userKey, String name, String phone, Collection<DriverCar> _cars, Collection<DriverRoute> _routes) {
        Long dirverKey = Optional.ofNullable(userKey).map(Long::new).orElseThrow(()-> new ParameterException("用户编号不能为空"));
        new Driver().register(dirverKey, name, phone, _cars, _routes);
    }

    @Override
    public void releaseWaitInfo(Long driverKey, LocalDate startTime, DriverCar car, DriverRoute route) throws BusinessException {
        Driver driver = Optional.ofNullable(Registrys.driverRepository().findByKey(driverKey)).orElseThrow(()-> new BusinessException("当前用户还没有注册成为司机"));
        //获取有效的等货信息
        Collection<DriverAwaitInfo> effectives = Registrys.driverAwaitInfoRepository().listEffective(driverKey);
        //创建并发布等货信息
        driver.driverAwaitInfo().release(startTime, car, route);
        if(CollectionUtils.isNotEmpty(effectives)){
            for (DriverAwaitInfo effective : effectives) {
                //等货信息失效
                effective.invalid();
            }
        }
    }

    @Override
    public void createInviteAsk(Long companyKey, Integer userKey, String userName, String driverName, String driverPhone) {
        new InviteAsk().invite(companyKey, userKey, userName, driverName, driverPhone);
    }

    @Override
    public void handleInviteAsk(Long driverKey, Long inviteKey, Boolean result) throws BusinessException {
        InviteAsk inviteAsk = Registrys.inviteAskRepository().findByKey(inviteKey);//调用资源库初始化
        if(inviteAsk == null){
            throw new BusinessException("邀请信息已过期");
        }
        Driver driver = Registrys.driverRepository().findByKey(driverKey);
        if(driver == null){
            createDriver(driverKey, inviteAsk.getDriverName(), inviteAsk.getDriverPhone(), Collections.emptyList(), Collections.emptyList());
        }
        //处理邀请，并发出处理邀请的领域事件
        inviteAsk.handle(driverKey, Optional.ofNullable(result).orElse(Boolean.FALSE));
    }
}
