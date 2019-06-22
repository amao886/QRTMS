/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-06 13:10:44
 */
package com.ycg.ksh.service.observer;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.entity.persistent.WaybillTrack;
import com.ycg.ksh.entity.service.WaybillContext;

/**
 * 运单轨迹观察者, 上报运单轨迹时会通知该类的实现者
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-06 13:10:44
 */
public interface TrackObserverAdapter {
    

    /**
     * 通知任务单位置上报
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-15 12:54:43
     * @param waybillContext
     * @param waybillTrack
     * @param driverScan
     * @throws BusinessException
     */
    public default void notifyLocationReport(WaybillContext waybillContext, WaybillTrack waybillTrack, boolean driverScan) throws BusinessException{
        //default do nothing
    }

}
