package com.ycg.ksh.adapter.impl;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/22
 */

import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.entity.adapter.esign.*;
import com.ycg.ksh.adapter.api.AuthenticateService;
import com.ycg.ksh.adapter.api.ESignService;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * 电子签收对接
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/22
 */
public class ESignServiceProxy implements ESignService, AuthenticateService {


    private AuthenticateService authenticateService;
    private Map<String, ESignService> services;

    private ESignService service(Signer signer) throws BusinessException{
        ESignService service = null;
        if (services != null && !services.isEmpty()){
            service = services.get(signer.getCode());
        }
        if(service == null){
            throw new BusinessException("没有对应的接口实现 "+ signer.getName() +" "+ signer.getCode());
        }
        return service;
    }
    /**
     * 个人实名认证
     *
     * @param signer   供应商
     * @param personal 个人认证信息
     * @return 此次请求的编号
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Personal legalize(Personal personal) throws ParameterException, BusinessException {
        return authenticateService.legalize(personal);
    }

    /**
     * 企业认证
     *
     * @param signer     供应商
     * @param enterprise 企业认证信息
     * @return 此次请求的编号
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Enterprise legalize(Enterprise enterprise) throws ParameterException, BusinessException {
        return authenticateService.legalize(enterprise);
    }

    /**
     * 创建或者更新个人用户信息
     *
     * @param signer
     * @param personal
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Personal buildPersonal(Signer signer, Personal personal) throws ParameterException, BusinessException {
        return service(signer).buildPersonal(signer, personal);
    }

    /**
     * 创建或者更新企业用户
     *
     * @param signer
     * @param enterprise
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Enterprise buildEnterprise(Signer signer, Enterprise enterprise) throws ParameterException, BusinessException {
        return service(signer).buildEnterprise(signer, enterprise);
    }

    /**
     * 创建印章(企业或者个人)
     *
     * @param signer
     * @param sealMoulage
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public SealMoulage buildSeal(Signer signer, SealMoulage sealMoulage) throws ParameterException, BusinessException {
        return service(signer).buildSeal(signer, sealMoulage);
    }

    /**
     * 创建合同
     *
     * @param signer
     * @param contract
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Contract buildContract(Signer signer, Contract contract) throws ParameterException, BusinessException {
        return service(signer).buildContract(signer, contract);
    }

    /**
     * 添加合同签署者
     *
     * @param signer
     * @param contractNo 合同自定义编号
     * @param signers    签署者
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void addContractSigner(Signer signer, Serializable contractNo, Collection<ContractSigner> signers) throws ParameterException, BusinessException {
        service(signer).addContractSigner(signer, contractNo, signers);
    }

    /**
     * 签署合同
     *
     * @param signer
     * @param contractNo 合同编号
     * @param signerId   签署者编号
     * @param moulageId  印章编号
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void signContract(Signer signer, Serializable contractNo, Serializable signerId, Serializable moulageId) throws ParameterException, BusinessException {
        service(signer).signContract(signer, contractNo, signerId, moulageId);
    }

    /**
     * 合同下载
     *
     * @param signer
     * @param contractNo
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public FileEntity download(Signer signer, Serializable contractNo) throws ParameterException, BusinessException {
        return service(signer).download(signer, contractNo);
    }

    public void setServices(Map<String, ESignService> services) {
        this.services = services;
    }

    public void setAuthenticateService(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }
}
