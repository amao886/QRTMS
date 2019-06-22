package com.ycg.ksh.service.persistence;

import com.github.pagehelper.Page;
import com.ycg.ksh.common.extend.mybatis.CustomMapper;
import com.ycg.ksh.entity.persistent.Order;
import com.ycg.ksh.entity.service.enterprise.OrderSearch;
import org.apache.ibatis.session.RowBounds;

import java.util.Collection;

public interface OrderMapper extends CustomMapper<Order> {

    /**
     * 根据绑定的二维码查询订单信息
     *
     * @param bindCode
     * @return
     */
    Order selectByCode(String bindCode);

    /**
     * @param search
     * @param rowBounds
     * @return
     */
    Page<Order> listBySomething(OrderSearch search, RowBounds rowBounds);


    /**
     * 根据条件查询订单信息
     *
     * @param search
     * @return
     */
    Collection<Order> listBySomething(OrderSearch search);


    /**
     * 修改配送单号
     * @param orderKey
     * @param deliveryNo
     */
    void updateDeliveryNo(Long orderKey, String deliveryNo);
}