package com.ycg.ksh.service.util;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/13
 */

import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.plan.PlanCommodity;

/**
 * 订单相关工具类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/13
 */
public class P {

    public static final int enterprise = 1;//要加载企业信息
    public static final int commodities = 2;//货物信息
    public static final int customdata = 5;//自定义数据
    public static final int extradata = 6;//额外的数据数据
    public static final int designate = 7;//指派相关数据数据
    public static final int accept = 8;//被指派相关数据数据

    //查询订单详情时，需要加载的数据
    public static final Integer[] DETAILS = new Integer[]{enterprise, commodities, customdata, extradata, designate, accept};
    //查询订单列表时，需要加载的数据
    public static final Integer[] LISTS = new Integer[]{enterprise, designate, accept};

    public static boolean isEmptyCommodity(PlanCommodity commodity) {
        if (commodity == null) {
            return true;
        }
        if(StringUtils.isNotBlank(commodity.getCommodityName()) ||
                StringUtils.isNotBlank(commodity.getCommodityNo()) ||
                StringUtils.isNotBlank(commodity.getCommodityType()) ||
                StringUtils.isNotBlank(commodity.getCommodityUnit()) ||
                StringUtils.isNotBlank(commodity.getRemark())){
            return false;
        }
        if((commodity.getWeight() != null && commodity.getWeight() > 0) ||
                (commodity.getVolume() != null && commodity.getVolume() > 0) ||
                (commodity.getQuantity() != null && commodity.getQuantity() > 0) ||
                (commodity.getBoxCount() != null && commodity.getBoxCount() > 0)){
            return false;
        }
        return true;
    }

}
