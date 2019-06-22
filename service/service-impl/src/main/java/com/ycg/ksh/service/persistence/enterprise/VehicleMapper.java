package com.ycg.ksh.service.persistence.enterprise;

import com.github.pagehelper.Page;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.CustomMapper;
import com.ycg.ksh.entity.persistent.enterprise.Vehicle;
import com.ycg.ksh.entity.service.enterprise.CompanyConcise;
import com.ycg.ksh.entity.service.enterprise.VehicleAlliance;
import com.ycg.ksh.entity.service.enterprise.VehicleSearch;
import org.apache.ibatis.session.RowBounds;

import java.util.Collection;

public interface VehicleMapper extends CustomMapper<Vehicle> {

    /**
     * 分页查询要车管理
     *
     * @param search
     * @param rowBounds
     * @return
     * @Author: wangke
     * @Date: 2018/10/21
     */
    Page<VehicleAlliance> listNeedCar(VehicleSearch search, RowBounds rowBounds);

    /**
     * 分页查询派车管理
     * @param search
     * @param rowBounds
     * @return
     */
    Page<VehicleAlliance> listSendCar(VehicleSearch search, RowBounds rowBounds);

    /**
     * 上级企业
     * @param companyKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<CompanyConcise> listSource(Long companyKey);
}
