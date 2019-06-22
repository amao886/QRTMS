package com.ycg.ksh.core.scene.application.impl;

import com.ycg.ksh.common.entity.Page;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.core.Registrys;
import com.ycg.ksh.core.scene.application.SceneApplicationService;
import com.ycg.ksh.core.scene.application.dto.SceneConfirmDto;
import com.ycg.ksh.core.scene.application.dto.VehicleRegistrationDto;
import com.ycg.ksh.core.scene.application.transform.RegistrationDtoTransformer;
import com.ycg.ksh.core.scene.domain.model.VehicleRegistration;
import com.ycg.ksh.core.scene.search.dto.VehicleConfirmDto;
import com.ycg.ksh.core.scene.infrastructure.persistence.VehicleRegistrationCondition;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.CustomerConcise;
import com.ycg.ksh.service.api.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

/**
 * 应用层实现类
 *
 * @author: wangke
 * @create: 2018-12-12 09:56
 **/
@Service("ksh.core.scene.sceneApplicationService")
public class SceneApplicationServiceImpl implements SceneApplicationService {

    @Resource
    private CustomerService customerService;

    private RegistrationDtoTransformer dtoTransformer;

    public SceneApplicationServiceImpl() {
        dtoTransformer = new RegistrationDtoTransformer();
    }

    @Override
    @Transactional
    public void save(VehicleRegistrationDto vehicleRegistrationDto) throws ParameterException, BusinessException {
        try {
            VehicleRegistration exist = Registrys.vehicleRegistrationRepository().findByKey(vehicleRegistrationDto.getOrderKey());
            if (exist != null) {
                throw new BusinessException("该订单已做到车登记");
            }
            Registrys.sceneService().registration(vehicleRegistrationDto.getOrderKey(),
                    dtoTransformer.transformArrivalsCar(vehicleRegistrationDto.getArrivalsCarDto()),
                    dtoTransformer.transformDeliveryCar(vehicleRegistrationDto.getDeliveryCarDto()));
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("车辆登记异常", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SceneConfirmDto> searchPage(Long shipperKey, Integer status, String likeString, Integer arrivalType, LocalDate pickTime, PageScope scope)
            throws ParameterException, BusinessException {
        //准备查询条件
        Collection<Long> companyKeys = Collections.emptyList();
        if (StringUtils.isNotBlank(likeString)) {
            companyKeys = customerService.listKeyLikeName(shipperKey, likeString);
        }
        VehicleRegistrationCondition condition = VehicleRegistrationCondition.build(shipperKey, likeString, status, arrivalType, pickTime, companyKeys);
        //调用查询资源库查询
        Page<VehicleConfirmDto> page = Registrys.sceneQueryRepository().searchVehicleConfirmPage(condition, scope.getPageNum(), scope.getPageSize());
        return new Page<SceneConfirmDto>(page.getNum(), page.getSize(), page.getTotal(), dtoTransformer.transformVehicleConfirmDto(page.getResults(), (identify) -> {
            CustomerConcise concise = customerService.loadCustomerConcise(identify.getIdentify(), identify.reg());
            if (concise != null) {
                return concise.getCustomerName();
            }
            return "已删除";
        }));
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleRegistrationDto getVehicleRegistration(Long orderKey) throws ParameterException, BusinessException {
        VehicleRegistration registration = Registrys.vehicleRegistrationRepository().findByKey(orderKey);
        if (null != registration) {
            return dtoTransformer.VehicleRegistrationFromDto(registration);
        }
        return null;
    }
}
