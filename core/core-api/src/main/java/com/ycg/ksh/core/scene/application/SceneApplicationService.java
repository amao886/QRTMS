package com.ycg.ksh.core.scene.application;

import com.ycg.ksh.common.entity.Page;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.core.scene.application.dto.SceneConfirmDto;
import com.ycg.ksh.core.scene.application.dto.VehicleRegistrationDto;
import com.ycg.ksh.entity.service.PageScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

/**
 * 应用服务 - 控制层调用
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/03 0003
 */
public interface SceneApplicationService {

    final Logger logger = LoggerFactory.getLogger(SceneApplicationService.class);


    /**
     * 车辆登记
     *
     * @param vehicleRegistrationDto
     * @throws ParameterException
     * @throws BusinessException
     */
    void save(VehicleRegistrationDto vehicleRegistrationDto) throws ParameterException, BusinessException;

    /**
     * @param shipperKey  货主公司编号
     * @param status     到车状态 全部，未到车，已到车
     * @param likeString 模糊查询  送货单号 物流商
     * @param arrivalType
     * @param pickTime   提货时间  全部，其他时间
     * @param scope      分页参数
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Page<SceneConfirmDto> searchPage(Long shipperKey, Integer status, String likeString, Integer arrivalType, LocalDate pickTime, PageScope scope)
            throws ParameterException, BusinessException;


    /**
     * 查询派车详情
     *
     * @param orderKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    VehicleRegistrationDto getVehicleRegistration(Long orderKey) throws ParameterException, BusinessException;

}
