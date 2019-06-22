package com.ycg.ksh.entity.service.enterprise;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/26
 */

import com.ycg.ksh.constant.OrderRoleType;
import com.ycg.ksh.entity.persistent.Order;

/**
 * 订单工具类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/26
 */
public class OrderUtil {

    public static OrderRoleType signRoleType(Order order, Long companyKey) {
        if (companyKey - order.getShipperId() == 0) {
            return OrderRoleType.SHIPPER;
        }
        if (companyKey - order.getReceiveId() == 0) {
            return OrderRoleType.RECEIVE;
        }
        if (companyKey - order.getConveyId() == 0) {
            return OrderRoleType.CONVEY;
        }
        return null;
    }
}
