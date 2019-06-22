/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-06 12:36:23
 */
package com.ycg.ksh.service.observer;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.constant.ReceiptEventType;
import com.ycg.ksh.entity.persistent.PaperReceipt;
import com.ycg.ksh.entity.persistent.TransitionReceipt;
import com.ycg.ksh.entity.persistent.WaybillReceipt;
import com.ycg.ksh.entity.service.WaybillContext;
import com.ycg.ksh.entity.service.esign.ReceiptSignature;

/**
 * 回单观察者，回单上传、回单审核都会通知该类的实现者
 * <p>
 * 
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-06 12:36:23
 */
public interface ReceiptObserverAdapter {

    public static final int SIGN_RECEIPT_VERIFY = 1;//回单签署校验
    public static final int SIGN_RECEIPT = 2;//回单签署

    /**
     * 回单上传通知
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-15 11:02:13
     * @param context 任务单上下文
     * @param receipt 回单信息
     * @param count 回单数量
     * @throws BusinessException
     */
    public default void notifyUploadReceipt(WaybillContext context, WaybillReceipt receipt, Integer count) throws BusinessException{
      //default do noting
    }

    /**
     * 回单审核通知
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-15 10:58:39
     * @param context 任务单上下文
     * @param status 当前状态
     * @param preteritStatus 之前状态
     * @param count 数量
     * @throws BusinessException
     */
    public default void notifyVerifyReceipt(WaybillContext context, Integer status, Integer preteritStatus, Integer count) throws BusinessException{
        //default do noting
    }

    /**
     * 上传临时回单通知
     * @param receipt
     * @param count
     * @throws BusinessException
     */
    public default void notifyTransitionReceipt(TransitionReceipt receipt, Integer count) throws BusinessException{
        //default do noting
    }




    /**
     * 通知回单上传
     * @param paperReceipt  回单信息
     * @throws BusinessException
     */
    public default void notifyUploadReceipt(PaperReceipt paperReceipt) throws BusinessException{
        //default do noting
    }

    /**
     * 通知回单签署
     * @param eventType
     * @param signature 签署信息
     * @param exception 异常信息
     * @throws BusinessException
     */
    public default void notifySignReceipt(ReceiptEventType eventType, ReceiptSignature signature, String exception) throws BusinessException{
        //default do noting
    }


}
