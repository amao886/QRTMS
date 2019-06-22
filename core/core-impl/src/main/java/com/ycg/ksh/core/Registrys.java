package com.ycg.ksh.core;

import com.ycg.ksh.core.contract.domain.repos.ContractRepository;
import com.ycg.ksh.core.contract.domain.repos.IncomeRepository;
import com.ycg.ksh.core.contract.search.ContractQueryRepository;
import com.ycg.ksh.core.driver.domain.repos.CompanyDriverRepository;
import com.ycg.ksh.core.driver.domain.repos.DriverAwaitInfoRepository;
import com.ycg.ksh.core.driver.domain.repos.DriverRepository;
import com.ycg.ksh.core.driver.domain.repos.InviteAskRepository;
import com.ycg.ksh.core.driver.domain.service.DriverService;
import com.ycg.ksh.core.scene.domain.repos.VehicleRegistrationRepository;
import com.ycg.ksh.core.scene.domain.service.SceneService;
import com.ycg.ksh.core.scene.search.SceneQueryRepository;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 资源注册
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/03 0003
 */
public class Registrys implements ApplicationContextAware {

    private static ApplicationContext context;

    public static DriverRepository driverRepository() {
        return context.getBean(DriverRepository.class);
    }

    public static CompanyDriverRepository companyDriverRepository() {
        return context.getBean(CompanyDriverRepository.class);
    }

    public static DriverAwaitInfoRepository driverAwaitInfoRepository() {
        return context.getBean(DriverAwaitInfoRepository.class);
    }

    public static InviteAskRepository inviteAskRepository() {
        return context.getBean(InviteAskRepository.class);
    }

    public static VehicleRegistrationRepository vehicleRegistrationRepository() {
        return context.getBean(VehicleRegistrationRepository.class);
    }

    public static ContractRepository contractRepository() {
        return context.getBean(ContractRepository.class);
    }

    public static IncomeRepository incomeRepository() {
        return context.getBean(IncomeRepository.class);
    }


    public static ContractQueryRepository contractQueryRepository() {
        return context.getBean(ContractQueryRepository.class);
    }

    public static SceneQueryRepository sceneQueryRepository() {
        return context.getBean(SceneQueryRepository.class);
    }

    public static SceneService sceneService() {
        return context.getBean(SceneService.class);
    }

    public static DriverService driverService() {
        return context.getBean(DriverService.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Registrys.context = applicationContext;
    }
}
