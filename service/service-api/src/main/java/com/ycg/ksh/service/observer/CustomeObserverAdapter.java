package com.ycg.ksh.service.observer;

import com.ycg.ksh.common.exception.BusinessException;

/**
 * 客户观察者
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/16
 */
public interface CustomeObserverAdapter {

    /**
     * 通知客户关联注册
     * @param companyKey  注册企业编号
     * @param customerKey 客户编号
     * @throws BusinessException  逻辑异常
     */
    void associateCustomerCompany(Long customerKey, Long companyKey) throws BusinessException;

}
