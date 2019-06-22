package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.entity.persistent.enterprise.Vehicle;
import com.ycg.ksh.entity.persistent.enterprise.VehicleDesignate;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.CompanyConcise;
import com.ycg.ksh.entity.service.enterprise.VehicleAlliance;
import com.ycg.ksh.entity.service.enterprise.VehicleSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * 车辆管理接口
 */
public interface VehicleService {
    Logger logger = LoggerFactory.getLogger(VehicleService.class);

    /**
     * 派车查询,物流商派车
     *
     * @param pageScope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @Author: wangke
     * @Date: 2018/10/21
     */
    CustomPage<VehicleAlliance> listBySendCar(Integer uKey, VehicleSearch search, PageScope pageScope) throws ParameterException, BusinessException;

    /**
     * 要车查询，货主和物流商都可能
     *
     * @param pageScope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @Author: wangke
     * @Date: 2018/10/21
     */
    CustomPage<VehicleAlliance> listByNeedCar(Integer uKey, VehicleSearch search, PageScope pageScope) throws ParameterException, BusinessException;


    /**
     * 车辆录入
     *
     * @param vehicle
     * @Author: wangke
     * @Date: 2018/10/21
     */
    void saveVehicle(Vehicle vehicle, VehicleDesignate designate) throws ParameterException, BusinessException;


    /**
     * 要车
     *
     * @param uKey
     * @param designate
     * @throws ParameterException
     * @throws BusinessException
     */
    void wantCar(Integer uKey, VehicleDesignate designate) throws ParameterException, BusinessException;

    /**
     * 车辆删除
     *
     * @param key
     * @throws ParameterException
     * @throws BusinessException
     * @Author: wangke
     * @Date: 2018/10/21
     */
    void deleteVehicle(Long key) throws ParameterException, BusinessException;

    /**
     * 确认派车
     *
     * @param key
     * @throws ParameterException
     * @throws BusinessException
     * @Author: wangke
     * @Date: 2018/10/22
     */
    void confirmCar(Long key) throws ParameterException, BusinessException;


    /**
     * 派车调整
     *
     * @param vehicle
     * @throws ParameterException
     * @throws BusinessException
     */
    void modifyVehicle(Vehicle vehicle, Collection<Long> vids) throws ParameterException, BusinessException;

    /**
     * 上级企业
     *
     * @param companyKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<CompanyConcise> listSource(Long companyKey) throws ParameterException, BusinessException;

    /**
     * 查询要车单详情
     *
     * @param key
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Vehicle queryVehicle(Long key) throws ParameterException, BusinessException;
}
