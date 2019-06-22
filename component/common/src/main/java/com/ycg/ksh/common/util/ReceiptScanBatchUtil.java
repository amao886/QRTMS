package com.ycg.ksh.common.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * 批次号生成工具类
 * wangke
 */
public class ReceiptScanBatchUtil {
    private static ReceiptScanBatchUtil primaryGenerater = null;

    public static ReceiptScanBatchUtil getInstance() {
        if (primaryGenerater == null) {
            synchronized (ReceiptScanBatchUtil.class) {
                if (primaryGenerater == null) {
                    primaryGenerater = new ReceiptScanBatchUtil();
                }
            }
        }
        return primaryGenerater;
    }

    /**
     * 每天从01开始计数
     */
    public static synchronized String getnumber(SimpleDateFormat formatter, String thisCode) {
        String batchNumber = null;
        Date date = new Date();
        //判断数据取出来的最后一个批次号是不是当天的
        if (null == thisCode) {
            //如果是新的一天的就直接变成01
            batchNumber = formatter.format(date) + "-01";
        } else {
            DecimalFormat df = new DecimalFormat("-00");
            //不是新的一天就累加
            batchNumber = formatter.format(date) + df.format(1 + Integer.parseInt(thisCode.substring(thisCode.indexOf("-") + 1, thisCode.length())));
            System.out.println(batchNumber);
        }
        return batchNumber;
    }
}