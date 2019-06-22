package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.constant.ObjectType;
import com.ycg.ksh.constant.OperateEventType;
import com.ycg.ksh.constant.OperateType;
import com.ycg.ksh.entity.persistent.ESignBrank;
import com.ycg.ksh.entity.persistent.SignAssociate;
import com.ycg.ksh.entity.persistent.support.Area;
import com.ycg.ksh.entity.service.esign.BrankData;
import com.ycg.ksh.entity.service.support.GradeAreaLast;
import com.ycg.ksh.entity.service.support.OperateNoteAlliance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/23
 */
public interface SupportService {

	final Logger logger = LoggerFactory.getLogger(SupportService.class);

    /** 加载银行数据
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public BrankData loadBrankData() throws ParameterException, BusinessException;

    /**
     * 根据大额行号获取银行数据
     * @param prcptcd
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public ESignBrank getESignBrankByPrcptcd(String prcptcd) throws ParameterException, BusinessException;

    /**
     * 查询支行数据
     * @param brank
     * @param province
     * @param city
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public Collection<ESignBrank> loadBranks(String brank, String province, String city) throws ParameterException, BusinessException;

    /**
     * 操作日志记录
     * @param uKey
     * @param operateType
     * @param hostKey
     * @param eventType
     * @param context
     */
    public void saveOperateNote(Integer uKey, OperateType operateType, Serializable hostKey, OperateEventType eventType, String context);

    /**
     * 操作日志记录
     * @param uKey
     * @param operateType
     * @param hostKey
     * @param eventType
     * @param context
     * @param adjunctKey
     */
    public void saveOperateNote(Integer uKey, OperateType operateType, Serializable hostKey, OperateEventType eventType, String context, Serializable adjunctKey);
    /**
     * 查询操作数据
     * @param operateType
     * @param hostKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public Collection<OperateNoteAlliance> listOperateNoteByType(OperateType operateType, Serializable hostKey) throws ParameterException, BusinessException;

    /**
     * 保存第三方关联信息
     * @param associate
     * @throws ParameterException
     * @throws BusinessException
     */
    public void saveSignAssociate(SignAssociate associate) throws ParameterException, BusinessException;

    /**
     * 查询第三方关联信息
     * @param objectType
     * @param objectKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public SignAssociate getSignAssociate(ObjectType objectType, Serializable objectKey) throws ParameterException, BusinessException;

    /**
     * @param parentKey
     *
     * @return
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    public Collection<Area> listAreaByParentKey(Integer parentKey) throws ParameterException, BusinessException;


    /**
     * @param parentKey
     * @return 省市区
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    public Collection<GradeAreaLast> listGradeArea(Integer parentKey) throws ParameterException, BusinessException;

    /**验证地址是否符合行政区划
     * @param address
     *
     * @return
     */
    boolean validateArea(String address);

    /**
     * 验证省市区是否符合行政区划
     * @param p  省
     * @param c  市
     * @param d  区
     *
     * @return
     */
    boolean validateArea(String p, String c, String d);

}
