package com.ycg.ksh.core.driver.application.impl;

import com.ycg.ksh.adapter.api.MessageQueueService;
import com.ycg.ksh.common.entity.Page;
import com.ycg.ksh.common.event.DomainEventPublisher;
import com.ycg.ksh.common.event.DomainEventSubscriber;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.rabbitmq.MediaMessage;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.core.Registrys;
import com.ycg.ksh.core.driver.application.DriverApplicationService;
import com.ycg.ksh.core.driver.application.dto.CompanyDriverDto;
import com.ycg.ksh.core.driver.application.dto.DriverAwaitInfoDto;
import com.ycg.ksh.core.driver.application.dto.DriverDto;
import com.ycg.ksh.core.driver.application.dto.InviteAskDto;
import com.ycg.ksh.core.driver.application.transform.DriverDtoTransformer;
import com.ycg.ksh.core.driver.domain.event.InviteCreatedEvent;
import com.ycg.ksh.core.driver.domain.event.InviteHandledEvent;
import com.ycg.ksh.core.driver.domain.model.CompanyDriver;
import com.ycg.ksh.core.driver.domain.model.Driver;
import com.ycg.ksh.core.driver.domain.model.DriverAwaitInfo;
import com.ycg.ksh.core.driver.infrastructure.persistence.CompanyDriverCondition;
import com.ycg.ksh.core.driver.infrastructure.persistence.DriverAwaitInfoCondition;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.message.InviteCooperationMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

/**
 * 司机应用服务实现
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/03 0003
 */
@Service("ksh.core.driver.driverApplicationService")
public class DriverApplicationServiceImpl implements DriverApplicationService {

    DriverDtoTransformer transformer;

    @Resource
    MessageQueueService queueService;


    public DriverApplicationServiceImpl() {
        this.transformer = new DriverDtoTransformer();
    }

    @Override
    @Transactional
    public void register(Integer userKey, DriverDto driverDto) throws ParameterException, BusinessException {
        try {
            Registrys.driverService().createDriver(new Long(userKey), driverDto.getName(), driverDto.getPhone(), transformer.transformDriverCar(driverDto.getCars()), transformer.transformDriverRoute(driverDto.getRoutes()));
        } catch (ParameterException | BusinessException e) {
            logger.error("register->: {}, : {}, : {}, : {}, : {}", userKey, driverDto, e);
            throw  e;
        }catch (Exception e) {
            logger.error("register->: {}, : {}, : {}, : {}, : {}", userKey, driverDto, e);
            throw new BusinessException("注册司机异常", e);
        }
    }

    @Override
    @Transactional
    public void modify(DriverDto driverDto) throws  ParameterException, BusinessException {
        try {
            Driver driver = Registrys.driverRepository().findByKey(driverDto.getDriverKey());
            if(driver != null){
                driver.modify(driverDto.getName(), driverDto.getPhone(),  transformer.transformDriverCar(driverDto.getCars()), transformer.transformDriverRoute(driverDto.getRoutes()));
            }else{
                throw new BusinessException("没找到指定司机信息["+ driverDto.getDriverKey() +"]");
            }
        } catch (ParameterException | BusinessException e) {
            logger.error("modify exception ->: {}, : {}, : {}, : {}, : {}", driverDto, e);
            throw  e;
        }catch (Exception e) {
            logger.error("modify exception ->: {}, : {}, : {}, : {}, : {}", driverDto, e);
            throw new BusinessException("更新司机信息异常", e);
        }
    }

    @Override
    @Transactional
    public void releaseWaitInfo(DriverAwaitInfoDto dto) throws  ParameterException, BusinessException {
        try {
            Registrys.driverService().releaseWaitInfo(dto.getDriverKey(), dto.getStartTime(), transformer.transformDriverCar(dto), transformer.transformDriverRoute(dto));
        } catch (ParameterException | BusinessException e) {
            logger.error("release waitinfo exception ->: {}, : {}, : {}, : {}, : {}", dto, e);
            throw  e;
        }catch (Exception e) {
            logger.error("release waitinfo exception ->: {}, : {}, : {}, : {}, : {}", dto, e);
            throw new BusinessException("发布等货信息异常", e);
        }
    }


    @Override
    @Transactional
    public void inviteAsk(InviteAskDto dto) throws  ParameterException, BusinessException {
        try {
            DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<InviteCreatedEvent>() {
                @Override
                public void handleEvent(InviteCreatedEvent event) {
                    //同意邀请
                    //发出邀请信息,交由用户service处理
                    queueService.sendCoreMessage(new MediaMessage(Globallys.UUID(), new InviteCooperationMessage(event.getInviteKey(), event.getCompanyKey(), event.getOperatorKey(), event.getDriverName(), event.getDriverPhone())));
                }
                @Override
                public Class<?>[] subscribedType() {
                    return new Class[]{ InviteCreatedEvent.class };
                }
            });
            Registrys.driverService().createInviteAsk(dto.getCompanyKey(), dto.getInviteUserKey(), dto.getInviteUserName(), dto.getDriverName(), dto.getDriverPhone());
        } catch (ParameterException | BusinessException e) {
            logger.error("inviteAsk exception ->: {}", dto, e);
            throw  e;
        }catch (Exception e) {
            logger.error("inviteAsk exception ->: {}", dto, e);
            throw new BusinessException("发起邀请异常", e);
        }
    }

    @Override
    @Transactional
    public void handleInviteAsk(Long driverKey, Long inviteKey, Boolean result) throws  ParameterException, BusinessException {
        try {
            //订阅领域事件
            DomainEventPublisher.instance().subscribe(new DomainEventSubscriber<InviteHandledEvent>() {
                @Override
                public void handleEvent(InviteHandledEvent event) {
                    //同意邀请
                    if(Optional.ofNullable(event.getResult()).orElse(Boolean.FALSE)){
                        //创建企业司机,并同步司机信息
                        Registrys.driverService().createCompanyDriver(event.getCompanyKey(), event.getDriverKey());
                    }
                }
                @Override
                public Class<?>[] subscribedType() {
                    return new Class[]{ InviteHandledEvent.class };
                }
            });
            //处理邀请，并发出处理邀请的领域事件
            Registrys.driverService().handleInviteAsk(driverKey, inviteKey, result);
        } catch (ParameterException | BusinessException e) {
            logger.error("handle inviteAsk exception ->:{}, : {}, : {}", driverKey, inviteKey, result, e);
            throw  e;
        }catch (Exception e) {
            logger.error("handle inviteAsk exception ->:{}, : {}, : {}", driverKey, inviteKey, result, e);
            throw new BusinessException("发起邀请异常", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public DriverDto getDriver(Long driverKey) throws  ParameterException, BusinessException {
        try {
            Driver driver = Registrys.driverRepository().findByKey(driverKey);
            if(driver != null){
                return  transformer.dtoFromDriver(driver);
            }
            return null;
        } catch (ParameterException | BusinessException e) {
            logger.error("get driver exception ->: {}", driverKey, e);
            throw  e;
        }catch (Exception e) {
            logger.error("get driver exception ->: {}", driverKey, e);
            throw new BusinessException("获取司机信息异常", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyDriverDto getCompanyDriver(Long driverKey) throws  ParameterException, BusinessException {
        try {
            CompanyDriver driver = Registrys.companyDriverRepository().findByKey(driverKey);
            if(driver != null){
                return  transformer.dtoFromCompanyDriver(driver);
            }
            return null;
        } catch (ParameterException | BusinessException e) {
            logger.error("get company driver exception ->: {}", driverKey, e);
            throw  e;
        }catch (Exception e) {
            logger.error("get company driver exception ->: {}", driverKey, e);
            throw new BusinessException("获取司机信息异常", e);
        }
    }

    @Override
    @Transactional
    public void modify(CompanyDriverDto driverDto) throws  ParameterException, BusinessException {
        try {
            CompanyDriver driver = Registrys.companyDriverRepository().findByKey(driverDto.getIdentify());
            if(driver == null){
                throw new BusinessException("指定["+ driverDto.getIdentify() +"]司机信息未找到");
            }
            driver.modify(driverDto.getName(), driver.getPhone(), transformer.transformCompanyDriverCar(driverDto.getCars()), transformer.transformCompanyDriverRoute(driverDto.getRoutes()));
        } catch (ParameterException | BusinessException e) {
            logger.error("modify exception ->: {}, : {}, : {}, : {}, : {}", driverDto, e);
            throw  e;
        }catch (Exception e) {
            logger.error("modify exception ->: {}, : {}, : {}, : {}, : {}", driverDto, e);
            throw new BusinessException("更新企业司机信息异常", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompanyDriverDto> searchCompanyDriver(Long companyKey, Integer status, String likeString, PageScope scope) throws  ParameterException, BusinessException {
        try {
            Page<CompanyDriver> page = Registrys.companyDriverRepository().searchCompanyDriver(CompanyDriverCondition.build(companyKey, likeString, status), scope.getPageNum(), scope.getPageSize());
            if(page != null && !page.isEmpty()){
                return new Page<CompanyDriverDto>(page.getNum(), page.getSize(), page.getTotal(), transformer.dtoFromCompanyDriver(page.getResults()));
            }
            return new Page<CompanyDriverDto>(scope.getPageNum(), scope.getPageSize(), 0, Collections.emptyList());
        } catch (ParameterException | BusinessException e) {
            logger.error("search company driver exception ->: {}, : {}, : {},  : {}", companyKey, status, likeString, scope, e);
            throw  e;
        }catch (Exception e) {
            logger.error("search company driver exception ->: {}, : {}, : {},  : {}", companyKey, status, likeString, scope, e);
            throw new BusinessException("企业司机查询异常", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DriverAwaitInfoDto> searchAwaitInfoByCompany(Long companyKey, Integer carType, Float length, String address, PageScope scope) throws  ParameterException, BusinessException {
        try {
            Page<DriverAwaitInfo> page = Registrys.driverAwaitInfoRepository().searchAwaitInfo(DriverAwaitInfoCondition.build(companyKey, carType, length, address), scope.getPageNum(), scope.getPageSize());
            if(page != null && !page.isEmpty()){
                return new Page<DriverAwaitInfoDto>(page.getNum(), page.getSize(), page.getTotal(), transformer.dtoFromDriverAwaitInfo(page.getResults()));
            }
            return new Page<DriverAwaitInfoDto>(scope.getPageNum(), scope.getPageSize(), 0, Collections.emptyList());
        } catch (ParameterException | BusinessException e) {
            logger.error("search awaitinfo by company exception ->: {}, : {}, : {}, : {}, : {}", companyKey, carType, length, address, scope, e);
            throw  e;
        }catch (Exception e) {
            logger.error("search awaitinfo by company exception ->: {}, : {}, : {}, : {}, : {}", companyKey, carType, length, address, scope, e);
            throw new BusinessException("企业找车查询异常", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DriverAwaitInfoDto> searchAwaitInfoByDriver(Long driverKey, LocalDateTime startTime, String start, PageScope scope) throws  ParameterException, BusinessException {
        try {
            Page<DriverAwaitInfo> page = Registrys.driverAwaitInfoRepository().searchAwaitInfo(DriverAwaitInfoCondition.build(driverKey, start, startTime), scope.getPageNum(), scope.getPageSize());
            if(page != null && !page.isEmpty()){
                return new Page<DriverAwaitInfoDto>(page.getNum(), page.getSize(), page.getTotal(), transformer.dtoFromDriverAwaitInfo(page.getResults()));
            }
            return new Page<DriverAwaitInfoDto>(scope.getPageNum(), scope.getPageSize(), 0, Collections.emptyList());
        } catch (ParameterException | BusinessException e) {
            logger.error("search awaitinfo by driver exception ->:  {}, : {}, : {}, : {}", driverKey, startTime, start, scope, e);
            throw  e;
        }catch (Exception e) {
            logger.error("search awaitinfo by driver exception ->:  {}, : {}, : {}, : {}", driverKey, startTime, start, scope, e);
            throw new BusinessException("司机发布等货信息查询异常", e);
        }
    }
}
