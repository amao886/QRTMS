package com.ycg.ksh.service.observer;

import com.ycg.ksh.common.exception.BusinessException;

/**
 * 企业观察者
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/16
 */
public interface CompanyObserverAdapter {

    /**
     * 通知企业注册
     * @param uKey 操作用户编号
     * @param companyKey  注册企业编号
     * @param customerKey 客户编号
     * @throws BusinessException  逻辑异常
     */
    void notifyCompanyRegister(Integer uKey, Long companyKey, Long customerKey) throws BusinessException;

}
