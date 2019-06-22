package com.ycg.ksh.service.observer;

import com.ycg.ksh.common.exception.BusinessException;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/23
 */
public interface ActivityObserverAdapter {

    /**
     * 通知奖励
     * @param uKey  用户编号
     * @param awardType 奖励类型
     * @param value  奖励的资源数值
     * @throws BusinessException 业务逻辑异常
     */
    void notifyAwardSomething(Integer uKey, Integer awardType, Long value) throws BusinessException;

}