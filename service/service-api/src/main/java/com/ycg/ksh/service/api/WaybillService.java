/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-07 14:51:49
 */
package com.ycg.ksh.service.api;

import com.alibaba.fastjson.JSONObject;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.entity.service.*;
import com.ycg.ksh.entity.persistent.Customer;
import com.ycg.ksh.entity.persistent.Goods;
import com.ycg.ksh.entity.persistent.Waybill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * 任务单相关逻辑
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
 * 2017-11-07 14:51:49
 */
public interface WaybillService {

    final Logger logger = LoggerFactory.getLogger(WaybillService.class);

    /**
     * 确认到货
     * <p>
     *
     * @param userId      用户编号
     * @param waybillKeys 运单编号
     * @param way         方式
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-30 11:59:23
     */
    void confirmReceive(Integer userId, Collection<Integer> waybillKeys, Integer way) throws ParameterException, BusinessException;

    /**
     * 确认送达
     * <p>
     *
     * @param userId      用户编号
     * @param waybillKeys
     * @param way         方式
     * @throws ParameterException
     * @throws BusinessException
     */
    void confirmArrive(Integer userId, Collection<Integer> waybillKeys, Integer way) throws ParameterException, BusinessException;

    /**
     * 更新任务单, 判断编辑权限,谁绑定谁修改
     * <p>
     *
     * @param waybillContext
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     */
    void update(WaybillContext waybillContext) throws ParameterException, BusinessException;

    /**
     * 记录分享
     * <p>
     *
     * @param userId    当前用户编号
     * @param shareId   发起分享的用户编号
     * @param waybillId 分享的任务编号
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
     * 2017-11-27 13:13:19
     */
    void insertShare(Integer userId, Integer shareId, Integer waybillId) throws ParameterException, BusinessException;

    /**
     * 绑定运单
     * <p>
     *
     * @param waybillContext
     * @return
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     */
    Waybill saveBind(WaybillContext waybillContext) throws ParameterException, BusinessException;

    /**
     * 绑定已经存在的任务单
     * <p>
     *
     * @param waybillContext
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Waybill saveSelectBind(WaybillContext waybillContext) throws ParameterException, BusinessException;
    
    /**
     * 	批量绑定运单
     * @param con
     * @throws ParameterException
     * @throws BusinessException
     */
    void batchBind(Integer userKey,Collection<Waybill> con) throws ParameterException, BusinessException;
    
    /**
     * 保存任务单信息
     * <p>
     *
     * @param waybillContext
     * @return
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     */
    Waybill save(WaybillContext waybillContext) throws ParameterException, BusinessException;

    /**
     * 删除任务单信息(只能删除未绑定状态的任务单)
     * <p>
     *
     * @param uKey              操作人ID
     * @param deleteWaybillKeys
     * @throws ParameterException
     * @throws BusinessException
     */
    void delete(Integer uKey, Collection<Integer> deleteWaybillKeys) throws ParameterException, BusinessException;

    /**
     * 根据条码获取运单信息
     * <p>
     *
     * @param barcodeString
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-04 09:24:37
     */
    Waybill getWaybillByCode(String barcodeString) throws ParameterException, BusinessException;

    /**
     * APP根据条码获取运单信息
     * TODO Add description
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-04-27 15:08:34
     * @param barcodeString
     * @param associate
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    MergeWaybill getWaybillByCode(String barcodeString,WaybillAssociate associate) throws ParameterException, BusinessException;


    /**
     * 微信端任务单绑定
     * <p>
     *
     * @param wKey      运单编号
     * @param associate 关联信息
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    MergeWaybill getWaybillById(Integer wKey, WaybillAssociate associate) throws ParameterException, BusinessException;

    /**
     * 根据ID查询任务单信息
     * <p>
     *
     * @param wKey 运单编号
     * @return 对应编号的运单信息
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
     * 2017-11-07 15:08:50
     */
    Waybill getWaybillById(Integer wKey) throws ParameterException, BusinessException;

    /**
     * 根据ID查询任务单信息
     * <p>
     *
     * @param uKey      操作用户编号
     * @param wKey      任务单编号
     * @param associate 关联信息
     * @return 对应编号的运单信息
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     */
    MergeWaybill getWaybillById(Integer uKey, Integer wKey, WaybillAssociate associate) throws ParameterException, BusinessException;


    /**
     * 查询所有已绑定的运单，按照时间降序
     * <p>
     *
     * @param uKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-20 18:18:50
     */
    Collection<Waybill> listBindWaybill(Integer uKey) throws ParameterException, BusinessException;
    /**
     * 	查询所有未绑定的订单
     * @param uKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<Waybill> listunBindWaybill(JSONObject json) throws ParameterException, BusinessException;
    
    /**
     * 查询运单信息
     * <p>
     *
     * @param serach
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-10 17:38:52
     */
    Collection<Waybill> listWaybill(WaybillSerach serach) throws ParameterException, BusinessException;

    /**
     * 分页查询运单信息
     * <p>
     *
     * @param serach    查询条件
     * @param pageScope 分页信息
     * @return
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
     * 2017-11-21 11:09:04
     */
    CustomPage<MergeWaybill> pageMergeWaybill(WaybillContext waybillContext)
            throws ParameterException, BusinessException;

    /**
     * 任务单部分数据查询
     *
     * @param uKey
     * @param serach
     * @param pageScope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    CustomPage<WaybillSimple> pageWaybillSimple(Integer uKey, WaybillSerach serach, PageScope pageScope) throws ParameterException, BusinessException;

    /**
     * 每日统计详情分页查询
     * <p>
     *
     * @param serach
     * @param pageScope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-04 08:18:21
     */
    CustomPage<MergeWaybill> pageDailyWaybill(WaybillContext waybillContext)
            throws ParameterException, BusinessException;

    /**
     * 分页查询运单信息，不包含项目组等信息
     * <p>
     *
     * @param serach
     * @param pageScope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-04 08:18:57
     */
    CustomPage<Waybill> pageWaybill(WaybillContext waybillContext) throws ParameterException, BusinessException;

    /**
     * 查询运单信息
     * <p>
     *
     * @param serach 查询条件
     * @return
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
     * 2017-11-23 10:57:24
     */
    Collection<MergeWaybill> listMergeWaybill(WaybillContext waybillContext) throws ParameterException, BusinessException;

    /**
     * 我的任务查询分页
     *
     * @param serach参数对象
     * @param pageScope分页对象
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:58469@ycgwl.com">zcl</a> at
     * 2017-11-28 17:31:17
     */
    public CustomPage<MergeWaybill> queryMyTaskPage(WaybillContext waybillContext)
            throws ParameterException, BusinessException;

    /**
     * 修改运单延迟状态
     *
     * @Author：wangke
     * @description：
     * @Date：11:08 2017/11/29
     */
    void updateDelayStatus() throws ParameterException, BusinessException;

    /**
     * 根据任务单ID查询货物
     *
     * @Author：wangke
     * @description：
     * @Date：13:53 2017/12/15
     */
    Collection<Goods> listGoodsById(Integer waybillId) throws ParameterException, BusinessException;

    /**
     * 我要发货
     *
     * @param waybillContext
     * @throws ParameterException
     * @throws BusinessException
     */
    void saveWcWaybill(WaybillContext waybillContext) throws ParameterException, BusinessException;

    /**
     * 编辑我要发货
     *
     * @Author：wangke
     * @description：
     * @Date：11:22 2017/12/20
     */
    void updateWcWaybill(WaybillContext waybillContext) throws ParameterException, BusinessException;

    /**
     * 修改运单电子围栏
     *
     * @Author：wangke
     * @description：
     * @Date：13:52 2017/12/19
     */
    Integer updateFenceStatus(Integer waybillId, Integer status) throws ParameterException, BusinessException;

    /**
     * 更新回单状态
     * <p>
     *
     * @param waybill
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-02 18:36:20
     */
    void updateWaybillStatusById(Waybill waybill);

    /**
     * 打印查询
     *
     * @param userKey
     * @param waybillKeys
     * @param count
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<MergeWaybill> listPrint(Integer userKey, Collection<Integer> waybillKeys, Integer count) throws ParameterException, BusinessException;


    /**
     * 编辑货品信息
     *
     * @param waybillContext
     * @throws ParameterException
     * @throws BusinessException
     * @author wangke
     * @date 2018/4/11 15:46
     */
    void updateGoods(WaybillContext waybillContext) throws ParameterException, BusinessException;


    /**
     * 删除货品信息
     * @author wangke
     * @date 2018/4/12 16:34
     * @param waybillContext
     * @throws ParameterException
     * @throws BusinessException
     */
    void deleteGoods(WaybillContext waybillContext) throws ParameterException, BusinessException;

    /**
     * 批量保存任务单
     * @param uKey
     * @param gKey
     * @param customer
     * @param collection
     * @throws ParameterException
     * @throws BusinessException
     */
    void saves(Integer uKey, Integer gKey, Customer customer, Collection<MergeWaybill> collection) throws ParameterException, BusinessException;
    
    /***
     *	 导出运单数据
     * @param outboundIds
     * @return
     */
	FileEntity listExportWaybill(JSONObject req) throws ParameterException, BusinessException;
}
