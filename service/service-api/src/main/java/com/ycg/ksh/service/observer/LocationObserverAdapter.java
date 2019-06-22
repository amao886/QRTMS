package com.ycg.ksh.service.observer;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.constant.LocationEvent;
import com.ycg.ksh.constant.LocationType;
import com.ycg.ksh.entity.persistent.LocationTrack;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/30
 */
public interface LocationObserverAdapter {

    /**
     * 轨迹上报事件
     * @param locationEvent  轨迹上报事件类型
     * @param locationType   轨迹类型
     * @param locationTrack  轨迹信息
     *
     * @throws BusinessException  业务逻辑异常
     */
    public default void notifyLocationReport(LocationEvent locationEvent, LocationType locationType, LocationTrack locationTrack) throws BusinessException {
        //default do nothing
    }

}
