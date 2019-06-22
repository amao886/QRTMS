package com.ycg.ksh.service.observer;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.entity.persistent.Barcode;
import com.ycg.ksh.entity.persistent.DriverContainer;
import com.ycg.ksh.entity.persistent.DriverTrack;
import com.ycg.ksh.entity.service.barcode.BarcodeContext;

import java.util.Collection;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/25
 */
public interface DriverContainerObserverAdapter {

    /**
     * 通知装车
     * @param container
     * @param context
     * @throws BusinessException
     */
    default void notifyLoadContainer(DriverContainer container, BarcodeContext context) throws BusinessException{
        // do nothing
    }


    /**
     * 位置上报通知
     * @param uKey
     * @param driverTrack
     * @param barcodes
     * @throws BusinessException
     */
    public default void notifyLocationReport(Integer uKey, DriverTrack driverTrack, Collection<Barcode> barcodes) throws BusinessException{
        //default do nothing
    }

}


