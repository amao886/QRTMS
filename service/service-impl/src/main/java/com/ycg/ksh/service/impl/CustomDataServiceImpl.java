package com.ycg.ksh.service.impl;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/10
 */

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.MessageException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.service.util.P;
import com.ycg.ksh.constant.CustomDataType;
import com.ycg.ksh.entity.persistent.enterprise.CustomData;
import com.ycg.ksh.entity.service.enterprise.OrderAlliance;
import com.ycg.ksh.entity.service.enterprise.PropertyDescribe;
import com.ycg.ksh.entity.service.enterprise.TemplateContext;
import com.ycg.ksh.entity.service.plan.PlanAlliance;
import com.ycg.ksh.service.persistence.enterprise.CustomDataMapper;
import com.ycg.ksh.service.api.CustomDataService;
import com.ycg.ksh.service.observer.OrderObserverAdapter;
import com.ycg.ksh.service.observer.PlanOrderObserverAdapter;
import com.ycg.ksh.service.observer.TemplateObserverAdapter;
import com.ycg.ksh.service.util.O;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 自定义数据
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/10
 */
@Service("ksh.core.service.customDataService")
public class CustomDataServiceImpl implements CustomDataService, TemplateObserverAdapter, OrderObserverAdapter, PlanOrderObserverAdapter {

    @Resource
    CustomDataMapper customDataMapper;
    /**
     * 查询自定义数据
     *
     * @param customDataType
     * @param objectKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Collection<CustomData> listByType(CustomDataType customDataType, Serializable objectKey) throws ParameterException, BusinessException {
        Assert.notNull(customDataType, "数据类型不能为空");
        Assert.notNull(objectKey, "数据编号 不能为空");

        Example example = new Example(CustomData.class);
        example.createCriteria().andEqualTo("dataType", customDataType.getCode()).andEqualTo("dataKey", objectKey);
        return customDataMapper.selectByExample(example);
    }

    @Override
    public void importSomething(TemplateContext context, Collection<PropertyDescribe> propertyDescribes) throws MessageException {
        if(CollectionUtils.isNotEmpty(propertyDescribes)){
            try{
                Map<String, Object> temporaries = new HashMap<String, Object>();
                for (PropertyDescribe propertyDescribe : propertyDescribes) {
                    if(propertyDescribe.isCustom()){
                        for (Map.Entry<String,Object> entry : propertyDescribe.getProperties().entrySet()) {
                            Object object = temporaries.get(entry.getKey());
                            if(object == null || StringUtils.isBlank(object.toString())){
                                temporaries.put(entry.getKey(), entry.getValue());
                            }
                        }
                    }
                }
                if(temporaries != null && !temporaries.isEmpty()){
                    Collection<CustomData> collection = new ArrayList<CustomData>();
                    for (Map.Entry<String, Object> entry : temporaries.entrySet()) {
                        collection.add(new CustomData(Globallys.nextKey(), CustomDataType.convert(context.getCategory()), context.getUniqueKey() , entry.getKey(), entry.getValue()));
                    }
                    customDataMapper.inserts(collection);
                }
            } catch (Exception e){
                throw new MessageException("保存自定义数据异常", e);
            }
        }
    }

    /**
     * 联合订单数据
     *
     * @param uKey
     * @param alliance
     * @param flags
     * @throws BusinessException
     */
    @Override
    public void allianceOrder(Integer uKey, OrderAlliance alliance, Integer... flags) throws BusinessException {
        if(Arrays.binarySearch(flags, O.customdata) >= 0){
            alliance.setCustomDatas(listByType(CustomDataType.ORDER, alliance.getId()));
        }
    }

    /**
     * 联合计划数据
     *
     * @param uKey     当前操作人的用户编号
     * @param alliance 当前订单
     * @param flags    要加载的数据标识
     *
     * @throws BusinessException 业务逻辑异常
     */
    @Override
    public void allianceOrder(Integer uKey, PlanAlliance alliance, Integer... flags) throws BusinessException {
        if(Arrays.binarySearch(flags, P.customdata) >= 0){
            alliance.setCustomDatas(listByType(CustomDataType.PLAN, alliance.getId()));
        }
    }

    /**
     * @param customDatas
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void save(Collection<CustomData> customDatas) throws ParameterException, BusinessException {
        customDataMapper.inserts(customDatas);
    }

    @Override
    public void save(CustomDataType dataType, Serializable dataKey, Collection<CustomData> customDatas) throws ParameterException, BusinessException {
        Collection<Long> customDataKeys = Optional.ofNullable(listByType(dataType, dataKey)).map(cs->{
            return cs.stream().map(CustomData::getKey).collect(Collectors.toList());
        }).orElse(Collections.emptyList());
        if(CollectionUtils.isNotEmpty(customDatas)){
            for (CustomData customData : customDatas) {
                customData.setDataType(dataType.getCode());
                customData.setDataKey(String.valueOf(dataKey));
                if (customData.getKey() != null && customData.getKey() > 0) {
                    customDataMapper.updateByPrimaryKeySelective(customData);
                    customDataKeys.remove(customData.getKey());
                } else {
                    customData.setKey(Globallys.nextKey());
                    customDataMapper.insertSelective(customData);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(customDataKeys)) {
            customDataMapper.deleteByIdentities(customDataKeys);
        }
    }

    @Override
    public void delete(CustomDataType dataType, Collection<? extends Serializable> dataKeys) throws ParameterException, BusinessException {
        if(CollectionUtils.isNotEmpty(dataKeys)){
            Example example = new Example(CustomData.class);
            example.createCriteria().andEqualTo("dataType", dataType.getCode()).andIn("dataKey", dataKeys.stream().map(Object::toString).collect(Collectors.toList()));
            customDataMapper.deleteByExample(example);
        }
    }

    @Override
    public void delete(CustomDataType dataType, Serializable dataKey) throws ParameterException, BusinessException {
        if(dataKey != null){
            customDataMapper.delete(new CustomData(dataType.getCode(), String.valueOf(dataKey)));
        }
    }
}
