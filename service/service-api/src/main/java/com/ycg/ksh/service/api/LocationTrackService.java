package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.constant.LocationType;
import com.ycg.ksh.entity.persistent.LocationTrack;
import com.ycg.ksh.entity.service.AllianceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;

/**
 * 位置轨迹接口
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/30
 */
public interface LocationTrackService {

    final Logger logger = LoggerFactory.getLogger(LocationTrackService.class);

    /**
     * 保存位置轨迹
     * @param uKey
     * @param locationType
     * @param locationTrack
     * @throws ParameterException
     * @throws BusinessException
     */
    void saveTrack(Integer uKey, LocationType locationType, LocationTrack locationTrack) throws ParameterException, BusinessException;

    /**
     * 根据定位类型和关联编号查询定位信息
     * @param locationType
     * @param hostKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<LocationTrack> listBySomething(LocationType locationType, Serializable hostKey) throws ParameterException, BusinessException;
    int countBySomething(LocationType locationType, Serializable hostKey) throws ParameterException, BusinessException;


    Collection<AllianceLocation> listAllianceBySomething(LocationType locationType, Serializable hostKey) throws ParameterException, BusinessException;
}
