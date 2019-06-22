package com.ycg.ksh.service.observer;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.constant.FinanceType;

import java.io.Serializable;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/23
 */
public interface FinanceObserverAdapter {

    /**
     * 财务变化通知
     * @param financeType
     * @param ownerKey
     * @param changeValue
     * @throws BusinessException
     */
    void notifyFinanceChange(FinanceType financeType, Serializable ownerKey, Long changeValue) throws BusinessException;
}
