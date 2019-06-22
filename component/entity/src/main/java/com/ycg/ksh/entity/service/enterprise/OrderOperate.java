package com.ycg.ksh.entity.service.enterprise;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/29
 */

import com.ycg.ksh.constant.OrderRoleType;
import com.ycg.ksh.constant.ReceiptFettleType;
import com.ycg.ksh.entity.persistent.*;
import org.apache.commons.beanutils.BeanUtils;

/**
 * 订单操作
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/29
 */
public class OrderOperate extends Order {

    private UserLegalize legalize;//当前用户的认证信息
    private ReceiptFettleType feceiptFettle;//电子回单状态
    private CompanyEmployee employee;//当前员工信息
    private Company company;//当前公司信息
    private CompanySeal companySeal;//当前印章信息
    private OrderRoleType signRoleType;//角色

    public OrderOperate(Order order) throws Exception {
        BeanUtils.copyProperties(this, order);
    }

}
