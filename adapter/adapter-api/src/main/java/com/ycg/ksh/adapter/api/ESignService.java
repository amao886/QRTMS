package com.ycg.ksh.adapter.api;

import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.entity.adapter.esign.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;

/**
 * 电子签收服务
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/22
 */
public interface ESignService {

    final Logger logger = LoggerFactory.getLogger(WeChatApiService.class);

    /**
     * 创建或者更新个人用户信息
     * @param signer
     * @param personal
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public Personal buildPersonal(Signer signer, Personal personal) throws ParameterException, BusinessException;

    /**
     * 创建或者更新企业用户
     * @param signer
     * @param enterprise
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public Enterprise buildEnterprise(Signer signer, Enterprise enterprise) throws ParameterException, BusinessException;


    /**
     * 创建印章(企业或者个人)
     * @param signer
     * @param sealMoulage
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public SealMoulage buildSeal(Signer signer, SealMoulage sealMoulage) throws ParameterException, BusinessException;


    /**
     * 创建合同
     * @param signer
     * @param contract
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public Contract buildContract(Signer signer, Contract contract) throws ParameterException, BusinessException;

    /**
     * 添加合同签署者
     * @param signer
     * @param contractNo  合同自定义编号
     * @param signers  签署者
     * @throws ParameterException
     * @throws BusinessException
     */
    public void addContractSigner(Signer signer, Serializable contractNo, Collection<ContractSigner> signers) throws ParameterException, BusinessException;

    /**
     * 签署合同
     * @param signer
     * @param contractNo 合同编号
     * @param signerId  签署者编号
     * @param moulageId 印章编号
     * @throws ParameterException
     * @throws BusinessException
     */
    public void signContract(Signer signer, Serializable contractNo, Serializable signerId, Serializable moulageId) throws ParameterException, BusinessException;

    /**
     * 合同下载
     * @param signer
     * @param contractNo
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public FileEntity download(Signer signer, Serializable contractNo) throws ParameterException, BusinessException;
}
