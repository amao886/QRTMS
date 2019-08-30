package com.ycg.ksh.service.persistence;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.ycg.ksh.entity.persistent.Waybill;
import com.ycg.ksh.entity.service.MergeWaybill;
import com.ycg.ksh.entity.service.WaybillSerach;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * 运单持久类
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
 * 2017-11-17 09:29:29
 */
public interface WaybillMapper extends Mapper<Waybill> {

    /**
     * 查询待绑单的数量
     *
     * @param uKey
     * @param gKey
     * @return
     */
    Integer countWaitBind(Integer uKey, Integer gKey);

    /**
     * 查询绑定数量
     * <p>
     *
     * @param barcode
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 10:17:31
     */
    Integer countByCode(String barcode);

    /**
     * 查询项目组编号
     * <p>
     *
     * @param waybillId
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 10:17:31
     */
    Integer selectGroupKeyById(Integer waybillId);

    /**
     * 根据条码查询运单信息
     * <p>
     *
     * @param barcode
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-04 09:15:07
     */
    Waybill selectByCode(String barcode);

    /**
     * 根据运单编号和用户编号查询运单信息
     * <p>
     *
     * @param waybillId 运单编号
     * @param userId    用户编号
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
     * 2017-11-27 10:03:50
     */
    Waybill selectByUserId(Integer waybillId, Integer userId);

    /**
     * 查询
     * <p>
     *
     * @param serach 查询条件
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
     * 2017-11-23 11:01:54
     */
    Collection<Waybill> listBySomething(WaybillSerach serach);

    /**
     * 分页查询
     * <p>
     *
     * @param serach 查询条件
     * @param bounds 分页信息
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
     * 2017-11-21 11:19:25
     */
    Page<Waybill> listBySomething(WaybillSerach serach, RowBounds bounds);


    /**
     * 每日统计分页查询
     * <p>
     *
     * @param serach
     * @param bounds
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-04 13:48:36
     */
    Page<Waybill> listTotalBySomething(WaybillSerach serach, RowBounds bounds);


    /**
     * 分页查询(包含运单指派的任务单)
     * <p>
     *
     * @param serach 查询条件
     * @param bounds 分页信息
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
     * 2017-11-21 11:19:25
     */
    Page<Waybill> listBySomethingConveyance(WaybillSerach serach, RowBounds bounds);


    /**
     * 查询(包含运单指派的任务单)
     * <p>
     *
     * @param serach
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-04 13:48:36
     */
    Collection<Waybill> listBySomethingConveyance(WaybillSerach serach);

    /**
     * 我的任务分页查询 TODO Add description
     * <p>
     *
     * @param serach
     * @param bounds
     * @return
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at
     * 2017-11-28 17:31:17
     */
    Page<MergeWaybill> queryMyTaskPage(WaybillSerach serach, RowBounds bounds);

    /**
     * 修改延迟状态
     *
     * @Author：wangke
     * @description：
     * @Date：11:06 2017/11/29
     */
    void updateDelayStatus(Integer status, Integer id);

    /**
     * 批量更新延迟状态
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-07 14:20:08
     * @param map key:运单ID, value:延迟状态
     */
    /**
     * 批量更新延迟状态
     * <p>
     *
     * @param delayStatus 要更新的延迟状态
     * @param list        要更新的运单主键
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-07 15:28:11
     */
    void batchUpdateDelayStatus(Integer delayStatus, List<Integer> list);

    /**
     * 批量导入任务单
     * <p>
     *
     * @param waybills
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-12 17:19:54
     */
    void batchInsert(Collection<Waybill> waybills);

    /**
     * 修改要求到货时间
     *
     * @author wangke
     * @date 2018/2/2 15:14
     */
    void updateArrivalTime(Integer waybillid);

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
     * 	查询要导出的任务单
     * @param key
     * @return
     */
	Collection<Waybill> listExportWabills(JSONObject req);
}