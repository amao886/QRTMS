package com.ycg.ksh.service.util;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/13
 */

import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.OrderCommodity;

/**
 * 订单相关工具类
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/13
 */
public class O {

    public static final int enterprise = 1;//要加载企业信息
    public static final int commodities = 2;//货物信息
    public static final int paperreceipt = 3;//回单图片
    public static final int loaction = 4;//位置信息
    public static final int expetion = 5;//异常
    public static final int receiptfettle = 8;//电子回单状态
    public static final int sign = 9;//签署信息
    public static final int allowreceive = 10;//是否有确认收货的权限
    public static final int customdata = 11;//自定义数据
    public static final int extradata = 12;//额外的数据数据
    public static final int share = 13;//分享相关数据数据
    public static final int operatenote = 14;//定位日志数据
    public static final int complaint = 15;//投诉建议


    //查询订单详情时，需要加载的数据
    public static final Integer[] DETAILS = new Integer[]{enterprise, commodities, paperreceipt, loaction, expetion, receiptfettle, sign, allowreceive, customdata, extradata, complaint};
    //查询订单详情时，需要加载的数据
    public static final Integer[] SHARE_DETAILS = new Integer[]{enterprise, commodities, paperreceipt, loaction, expetion, customdata, extradata, share};

    //查询订单列表时，需要加载的数据
    public static final Integer[] DEFAULT_LISTS = new Integer[]{enterprise};
    //查询订单列表时，需要加载的数据
    public static final Integer[] LISTS = new Integer[]{enterprise, receiptfettle, operatenote};
    //查询分享订单列表时，需要加载的数据
    public static final Integer[] SHARE_LISTS = new Integer[]{enterprise, share};


    public static boolean isEmptyCommodity(OrderCommodity commodity) {
        if (commodity == null) {
            return true;
        }
        if (StringUtils.isNotBlank(commodity.getCommodityName()) ||
                StringUtils.isNotBlank(commodity.getCommodityNo()) ||
                StringUtils.isNotBlank(commodity.getCommodityType()) ||
                StringUtils.isNotBlank(commodity.getCommodityUnit()) ||
                StringUtils.isNotBlank(commodity.getRemark())) {
            return false;
        }
        if ((commodity.getWeight() != null && commodity.getWeight() > 0) ||
                (commodity.getVolume() != null && commodity.getVolume() > 0) ||
                (commodity.getQuantity() != null && commodity.getQuantity() > 0) ||
                (commodity.getBoxCount() != null && commodity.getBoxCount() > 0)) {
            return false;
        }
        return true;
    }

}
