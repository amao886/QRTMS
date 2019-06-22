/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-06 13:21:48
 */
package com.ycg.ksh.service.observer;

import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.MessageException;
import com.ycg.ksh.entity.service.plan.PlanAlliance;
import com.ycg.ksh.entity.service.plan.PlanTemplate;

/**
 * 订单观察者
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-06 13:21:48
 */
public interface PlanOrderObserverAdapter {

    /**
     * 初始化订单数据
     * @param uKey       操作用户编号
     * @param partnerType    操作人角色
     * @param companyKey 操作人所属企业编号
     * @param template   计划订单模板数据
     * @param insert
     * @throws BusinessException  业务逻辑异常
     * @throws MessageException 消息异常
     */
    public default void initializeTemplate(Integer uKey, PartnerType partnerType, Long companyKey, PlanTemplate template, boolean insert) throws MessageException {
        //default do nothing
    }

    /**
     * 联合订单数据
     * @param uKey  当前操作人的用户编号
     * @param alliance 当前订单
     * @param flags 要加载的数据标识
     * @throws BusinessException  业务逻辑异常
     */
    public default void allianceOrder(Integer uKey, PlanAlliance alliance, Integer... flags) throws BusinessException{
        //default do nothing
    }

}
