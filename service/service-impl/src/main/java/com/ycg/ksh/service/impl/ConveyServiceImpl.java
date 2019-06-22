package com.ycg.ksh.service.impl;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/10
 */

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.MessageException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.entity.persistent.enterprise.OrderConvey;
import com.ycg.ksh.entity.service.enterprise.PropertyDescribe;
import com.ycg.ksh.entity.service.enterprise.TemplateContext;
import com.ycg.ksh.service.persistence.enterprise.OrderConveyMapper;
import com.ycg.ksh.service.api.ConveyService;
import com.ycg.ksh.service.observer.OrderObserverAdapter;
import com.ycg.ksh.service.observer.PlanOrderObserverAdapter;
import com.ycg.ksh.service.observer.TemplateObserverAdapter;
import com.ycg.ksh.service.support.assist.ObjectUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * 运输
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/10
 */
@Service("ksh.core.service.conveyService")
public class ConveyServiceImpl implements ConveyService, TemplateObserverAdapter, OrderObserverAdapter, PlanOrderObserverAdapter {

    @Resource
    OrderConveyMapper orderConveyMapper;

    @Override
    public void importSomething(TemplateContext context, Collection<PropertyDescribe> propertyDescribes) throws MessageException {
        if(CollectionUtils.isNotEmpty(propertyDescribes)){
            try{
                OrderConvey orderConvey = ObjectUtils.reduceObject(OrderConvey.class, propertyDescribes);//运输数据
                if(orderConvey != null && !orderConvey.isEmpty()){
                    orderConvey.setKey(context.getUniqueKey());
                    orderConveyMapper.insert(orderConvey);
                }
            } catch (BusinessException | ParameterException e){
                throw new MessageException(e.getFriendlyMessage(), e);
            } catch (Exception e){
                throw new MessageException("车辆数据保存异常", e);
            }
        }
    }

}
