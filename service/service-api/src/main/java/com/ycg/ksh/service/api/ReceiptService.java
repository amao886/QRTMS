/**
 * TODO Add description
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-07 14:53:47
 */
package com.ycg.ksh.service.api;

import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.service.*;
import com.ycg.ksh.entity.service.enterprise.OrderAlliance;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 回单管理
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-07 14:53:47
 */
public interface ReceiptService {

    /**
     * 查询临时回单信息
     * <p>
     *
     * @param barcode
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-14 15:06:06
     */
    Collection<MergeTransitionReceipt> listTransitionReceipt(String barcode) throws ParameterException, BusinessException;

    /**
     * 导出纸质回单信息
     * <p>
     *
     * @param paperyReceipt 查询参数
     * @param sendCycle     送交客户时间段
     * @param deliverCycle  发货日期时间段
     * @param type          类型 1库存导出2已送达客户
     * @return 导出的文件信息
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-18 16:33:34
     */
    FileEntity exportPaperyReceipt(PaperyReceipt paperyReceipt, TimeCycle sendCycle, TimeCycle deliverCycle, Integer type) throws ParameterException, BusinessException;

    /**
     * 批量保存临时回单
     * <p>
     *
     * @param uKey    用户编号
     * @param barcode 二维码
     * @param paths   图片路径信息
     * @param fromWx  是否要从微信服务器下载
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-10 09:34:27
     */
    void transitionReceipt(Integer uKey, String barcode, Collection<String> paths, boolean fromWx) throws ParameterException, BusinessException;

    /**
     * 保存回单信息
     * <p>
     *
     * @param user      用户
     * @param waybillId 运单编号
     * @param images    图片路径信息
     * @param fromWx    是否要从微信服务器下载
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    void saveReceipt(User user, Integer waybillId, Collection<String> images, boolean fromWx) throws ParameterException, BusinessException;

    /**
     * 上传回单
     *
     * @param receipt
     * @param images
     * @param fromWx
     * @throws ParameterException
     * @throws BusinessException
     */
    void savePaperReceipt(PaperReceipt receipt, Collection<String> images, boolean fromWx) throws ParameterException, BusinessException;

    /**
     * 更新图片审核信息
     * <p>
     *
     * @param waybillId 运单编号
     * @param imageInfo 图片审核信息
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-23 17:36:49
     */
    void modifyVerify(Integer waybillId, ImageInfo imageInfo) throws ParameterException, BusinessException;

    /**
     * 根据运单编号查询运单回单信息
     * <p>
     *
     * @param waybillId 运单编号
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-27 09:35:56
     */
    List<MergeReceipt> listByWaybillId(Integer waybillId) throws ParameterException, BusinessException;

    /**
     * 库存回单和送交客回单
     *
     * @param map
     * @return
     */
    CustomPage<WaybillReceiptView> queryReceiptPage(Map<String, Object> map, PageScope pageScope) throws ParameterException, BusinessException;

    /**
     * 历史回单查询
     *
     * @Author：wangke @description： @Date：14:38 2017/11/28
     */
    CustomPage<WaybillReceiptView> historyRecordList(Map<String, Object> map, PageScope pageScope) throws ParameterException, BusinessException;


    /***
     * 回单扫描处理
     * @param paperyReceipt
     * @return MergeReceipt
     * @throws ParameterException, BusinessException
     */
    MergeWayBillReceipt singleScan(PaperyReceipt paperyReceipt, Integer flag) throws ParameterException, BusinessException;


    /***
     * 获取批次号
     * @author wangke
     * @date 2018/3/5 14:24
     * @return
     * @throws ParameterException, BusinessException
     */
    String getBatchNumber() throws ParameterException, BusinessException;

    /***
     *  批量修改回单状态
     * @author wangke
     * @date 2018/3/5 14:51
     * @param mergeReceipt
     * @return
     * @throws ParameterException, BusinessException
     */
    Integer modifyReceiptStatus(MergeReceiptStatus mergeReceipt) throws ParameterException, BusinessException;

    /**
     * 查询回单图片信息
     * <p>
     *
     * @param waybillId
     * @return
     * @throws ParameterException, BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-26 08:52:53
     */
    List<MergeWayBillReceipt> listImagesByWaybillId(Integer waybillId) throws ParameterException, BusinessException;


    /**
     * 根据条件查询分组回单统计分析
     *
     * @author wangke
     * @date 2018/3/8 10:39
     */
    CustomPage<MergeReceiptStatistics> statisticsWaybill(MergeReceiptStatistics statistics, PageScope pageScope) throws ParameterException, BusinessException;


    /**
     * 回单统计分析
     *
     * @param statistics
     * @param pageScope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @author wangke
     * @date 2018/3/9 9:12
     */
    CustomPage<MergeReceiptStatistics> receiptAnalysis(MergeReceiptStatistics statistics, PageScope pageScope) throws ParameterException, BusinessException;


    /**
     * 批量修改回单入库
     *
     * @param mergeReceiptStatus
     * @throws ParameterException
     * @throws BusinessException
     * @author wangke
     * @date 2018/3/23 14:07
     */
    Integer[] receiptStorageList(MergeReceiptStatus mergeReceiptStatus) throws ParameterException, BusinessException;

    /**
     * 查询回单列表
     *
     * @param serach
     * @param pageScope
     * @return
     * @author wangke
     */
    CustomPage<MergeReceiptResult> queryReceiptList(WaybillSerach serach, PageScope pageScope);

    /**
     * 查询操作记录
     *
     * @param record
     * @return
     * @author wangke
     * @date 2018/4/9 14:38
     */
    CustomPage<MergeOperationRecord> queryOperationRecord(MergeOperationRecord record, PageScope pageScope);


    /**
     * 查询数字化回单
     * @param orderKey
     * @return
     * @throws BusinessException
     * @throws ParameterException
     */
    OrderReceipt getDigitizedReceiptByKey(Long orderKey) throws BusinessException, ParameterException;

    /**
     * 生成电子回单
     *
     * @param uKey
     * @param alliance
     * @return
     * @throws BusinessException
     * @throws ParameterException
     */
    FileEntity buildReceipt(Integer uKey, OrderAlliance alliance) throws BusinessException, ParameterException;
    /**
     * 发送回单签署验证码
     * @param uKey
     * @param orderKeys
     * @throws BusinessException
     * @throws ParameterException
     */
    void sendSignatureCode(Integer uKey, Collection<Long> orderKeys) throws BusinessException, ParameterException;

    /**
     * 签署校验
     *
     * @param uKey
     * @throws BusinessException
     * @throws ParameterException
     */
    void signatureValidate(Integer uKey) throws BusinessException, ParameterException;

    /**
     * 签署电子回单
     *
     * @param code 短信校验码
     * @param signature  签署信息
     * @param exception  异常信息
     * @throws BusinessException
     * @throws ParameterException
     */
    void signature(String code, OrderSignature signature, String exception) throws BusinessException, ParameterException;

    /**
     * 批量签署
     * @param code
     * @param signature
     * @param orderkeys
     * @param exception
     * @throws BusinessException
     * @throws ParameterException
     */
    int signature(String code, OrderSignature signature, Collection<Long> orderkeys, String exception) throws BusinessException, ParameterException;

    /**
     * 导出电子回单
     *
     * @param uKey
     * @param orderKeys
     * @return
     * @throws BusinessException
     * @throws ParameterException
     */
    FileEntity exportReceipt(Integer uKey, Collection<Long> orderKeys) throws BusinessException, ParameterException;


    /**
     * 验证是否已经签署
     *
     * @param orderKey
     * @param companyKey
     * @return
     * @throws BusinessException
     * @throws ParameterException
     */
    boolean validateSing(Long orderKey, Long companyKey) throws BusinessException, ParameterException;

    /**
     * 根据订单编号查询回单图片信息
     * @param orderKey
     * @return
     * @throws BusinessException
     * @throws ParameterException
     */
    Collection<ImageStorage> listIamgesByOrderKey(Long orderKey) throws BusinessException, ParameterException;

    /**
     * 三菱物流专享
     * 批量上传回单
     * @param user
     * @param collection
     */
    void saveReceipt(User user, HashMap<Integer, String> collection);
}
