package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;

/**
 * 财务
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/10
 */
public interface FinanceService {

    /**
     * 提现申请
     * @param uKey
     * @param value
     * @throws ParameterException
     * @throws BusinessException
     */
    void applyMoneyExtract(Integer uKey, Integer value) throws ParameterException, BusinessException;
}
