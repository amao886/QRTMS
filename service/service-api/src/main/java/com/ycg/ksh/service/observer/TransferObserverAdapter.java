package com.ycg.ksh.service.observer;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/4
 */

import com.ycg.ksh.common.exception.MessageException;
import com.ycg.ksh.entity.persistent.Company;
import com.ycg.ksh.entity.persistent.Order;

import java.util.Collection;

/**
 * 传输/分享
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/4
 */
public interface TransferObserverAdapter {

    /**
     * 订单分享
     * @param company  发起分享的企业
     * @param receives 被分享的企业
     * @param orders   被分享的订单信息
     *
     * @throws MessageException
     */
    void onShareOrders(Company company, Collection<Company> receives, Collection<Order> orders) throws MessageException;
}
