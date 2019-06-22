package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.constant.CustomDataType;
import com.ycg.ksh.entity.persistent.enterprise.CustomData;

import java.io.Serializable;
import java.util.Collection;

/**
 * 自定义数据
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/10
 */
public interface CustomDataService {

    /**
     * 查询自定义数据
     * @param customDataType
     * @param objectKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<CustomData> listByType(CustomDataType customDataType, Serializable objectKey) throws ParameterException, BusinessException;

    /**
     * @param customDatas
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    void save(Collection<CustomData> customDatas) throws ParameterException, BusinessException;
    void save(CustomDataType dataType, Serializable dataKey, Collection<CustomData> customDatas) throws ParameterException, BusinessException;

    void delete(CustomDataType dataType, Collection<? extends Serializable> dataKeys) throws ParameterException, BusinessException;
    void delete(CustomDataType dataType, Serializable dataKey) throws ParameterException, BusinessException;
}
