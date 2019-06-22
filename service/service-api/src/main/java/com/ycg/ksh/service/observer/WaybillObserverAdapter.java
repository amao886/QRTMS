/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-06 13:21:48
 */
package com.ycg.ksh.service.observer;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.entity.persistent.Waybill;
import com.ycg.ksh.entity.service.MergeWaybill;
import com.ycg.ksh.entity.service.WaybillAssociate;
import com.ycg.ksh.entity.service.WaybillContext;

/**
 * 运单观察者,确认送达、确认收货都会通知该接口的实现者
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-06 13:21:48
 */
public interface WaybillObserverAdapter {

    /**
     * 删除任务单
     * <p>
     * @param uKey  操作人
     * @param waybill  要删除的任务单
     * @throws BusinessException
     */
    public default void onDeleteWaybill(Integer uKey, Waybill waybill) throws BusinessException{
        //default do nothing
    }
    /**
     * 确认收货通知
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-15 11:51:57
     * @param context 任务单上下文
     * @throws BusinessException
     */
    public default void onWaybillFettleChange(WaybillContext context) throws BusinessException{
        //default do nothing
    }
    /**
     * 组装运单信息
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-02 16:04:29
     * @param waybill
     * @param associate
     * @throws BusinessException
     */
    public default void onMergeWaybill(MergeWaybill waybill, WaybillAssociate associate) throws BusinessException{
        //default do nothing
    }

    /**
     * 任务单初始化事件
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-15 11:34:56
     * @param context 任务单上下文
     * @throws BusinessException
     */
    public default void onInitializeWaybill(WaybillContext context) throws BusinessException{
        //default do nothing
    }

    /**
     * 任务单绑定或更新完成事件
     * <p>
     * @param context 任务单上下文
     * @param binding  true:绑定，false:更新
     * @throws BusinessException
     */
    public default void onCompleteWaybill(WaybillContext context, boolean binding) throws BusinessException{
        //default do nothing
    }
}
