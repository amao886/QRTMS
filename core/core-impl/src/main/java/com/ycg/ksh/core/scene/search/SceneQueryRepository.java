package com.ycg.ksh.core.scene.search;

import com.ycg.ksh.common.entity.Page;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.core.scene.search.dto.VehicleConfirmDto;
import com.ycg.ksh.core.scene.infrastructure.persistence.VehicleRegistrationCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 现场模块查询的支撑者
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/13 0013
 */
public interface SceneQueryRepository {

    final Logger logger = LoggerFactory.getLogger(SceneQueryRepository.class);
    /**
     * 分页查询派车管理
     *
     * @param condition  查询条件
     * @param num        当前页
     * @param size        每页条数
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Page<VehicleConfirmDto> searchVehicleConfirmPage(VehicleRegistrationCondition condition, int num, int size) throws ParameterException, BusinessException;
}
