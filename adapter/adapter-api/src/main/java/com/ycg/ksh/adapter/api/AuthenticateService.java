package com.ycg.ksh.adapter.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.entity.adapter.esign.Enterprise;
import com.ycg.ksh.entity.adapter.esign.Personal;

/**
 * 实名认证服务
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/5
 */
public interface AuthenticateService {
    /**
     * 个人实名认证
     * @param signer   供应商
     * @param personal 个人认证信息
     * @return 此次请求的编号
     * @throws ParameterException
     * @throws BusinessException
     */
    public Personal legalize(Personal personal) throws ParameterException, BusinessException;

    /**
     * 企业认证
     * @param signer   供应商
     * @param enterprise 企业认证信息
     * @return 此次请求的编号
     * @throws ParameterException
     * @throws BusinessException
     */
    public Enterprise legalize(Enterprise enterprise) throws ParameterException, BusinessException;
}
