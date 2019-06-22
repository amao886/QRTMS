/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-07 14:53:47
 */
package com.ycg.ksh.service.api;

import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.entity.persistent.ApplyRes;
import com.ycg.ksh.entity.persistent.Barcode;
import com.ycg.ksh.entity.service.BarCodeDescription;
import com.ycg.ksh.entity.service.BarcodeSearch;
import com.ycg.ksh.entity.service.MergeApplyRes;
import com.ycg.ksh.entity.service.MergeBarcode;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.barcode.BarcodeContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 条码逻辑接口
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-07 14:53:47
 */
public interface BarCodeService {

    final Logger logger = LoggerFactory.getLogger(BarCodeService.class);

    /**
     * 保存barcode
     * @param uKey
     * @param gKey
     * @param barcode
     * @throws ParameterException
     * @throws BusinessException
     */
    void save(Integer uKey, Integer gKey, String barcode) throws ParameterException, BusinessException;
    /**
     * 校验条码的合法性
     * <p>
     *
     * @param code
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 14:22:01
     */
    Barcode validate(String code) throws ParameterException, BusinessException;

    /**
     * 验证条码
     * <p>
     *
     * @param userId
     * @param code
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 12:41:51
     */
    BarcodeContext validate(Integer userId, String code) throws ParameterException, BusinessException;


    BarcodeContext validateNotDecrypt(Integer userId, String code) throws ParameterException, BusinessException;

    /**
     * 条码检测
     * <p>
     *
     * @param barcode
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-11-24 13:33:13
     */
    Barcode getBarcode(String barcode) throws ParameterException, BusinessException;

    /**
     * 查询条码信息
     * <p>
     *
     * @param barcode
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-11-24 13:33:13
     */
    Barcode loadBarcode(String barcode) throws ParameterException, BusinessException;


    /**
     * 申请二维码
     * <p>
     *
     * @param uKey
     * @param applyRes
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-02 09:48:29
     */
    void applyRes(Integer uKey, ApplyRes applyRes) throws ParameterException, BusinessException;

    /**
     * 查询所有未生成条码的申请信息
     * <p>
     *
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-29 14:08:05
     */
    Collection<ApplyRes> listNotBuildApplies() throws ParameterException, BusinessException;

    /**
     * 查询指定天数
     * <p>
     *
     * @param applyRes
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-29 13:42:59
     */
    void saveBuildCode(ApplyRes applyRes) throws ParameterException, BusinessException;

    /**
     * 准备申请
     * <p>
     *
     * @param uKey
     * @param applyId
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-11 09:14:00
     */
    void saveBuildReady(Integer uKey, Integer applyId) throws ParameterException, BusinessException;

    /**
     * 条码管理列表查询
     * type: 区分是旧版本还是新版本,1 : 旧版本,2 : 新版本
     *
     * @Author：wangke
     * @description：
     * @Date：14:56 2018/1/4
     */
    CustomPage<MergeApplyRes> pageApplyResList(MergeApplyRes applyRes, PageScope pageScope,Integer type) throws BusinessException;


    /**
     * 根据用户ID查询条码数
     *
     * @Author：wangke
     * @description：
     * @Date：13:27 2018/1/5
     */
    MergeApplyRes queryTotalCount(Integer userKey) throws ParameterException, BusinessException;


    /**
     * 条码变更 老版本 项目组
     *
     * @Author：wangke
     * @description：
     * @Date：14:58 2018/1/6
     */
    void updateBarCodeChangeOld(MergeApplyRes mergeApplyRes) throws ParameterException, BusinessException;


    /**
     * barcode列表查询
     *
     * @Author：wangke
     * @description：
     * @Date：12:15 2018/1/7
     */
    List<Map<String, Object>> barcodeListByResId(Integer resId) throws ParameterException, BusinessException;

    /**
     * 生成PDF文件
     * <p>
     *
     * @param uKey
     * @param resKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-12 09:05:02
     */
    FileEntity buildPDF(Integer uKey, Integer resKey) throws ParameterException, BusinessException;

    /**
     * 更新条码状态
     * <p>
     *
     * @param barcode
     * @throws ParameterException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-02 18:49:56
     */
    void updateStatusById(Barcode barcode) throws ParameterException, BusinessException;
    /**
     * 
     * TODO 条码变更-企业版
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-08-02 17:05:28
     * @param applyRes
     * @param modifycompanyId
     * @throws ParameterException
     * @throws BusinessException
     */
    void updateBarCodeChange(ApplyRes applyRes) throws ParameterException, BusinessException ;
    
    BarCodeDescription getBarcodeDescription(String barcode)throws BusinessException,ParameterException;
    /**
     * 查询条码申请记录 V3.2.4
     * TODO Add description
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-08-02 13:52:02
     * @param uname
     * @param mobilePhone
     * @param pageScope
     * @param type
     * @return
     * @throws BusinessException
     */
    CustomPage<MergeApplyRes> pageAllApplyRes(String uname,String mobilePhone, PageScope pageScope) throws BusinessException;
    
    /**
     * 
     * TODO 根据条码号段、公司查询所属企业条码
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-08-07 15:43:05
     * @param startNum
     * @param endNum
     * @return
     * @throws BusinessException
     * @throws ParameterException
     */
    CustomPage<MergeBarcode> pageBarcodeToCompany(BarcodeSearch barcode,PageScope pageScope) throws BusinessException, ParameterException;
}
