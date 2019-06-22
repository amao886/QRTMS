package com.ycg.ksh.service.persistence.driver;

import com.github.pagehelper.Page;
import com.ycg.ksh.common.extend.mybatis.CustomMapper;
import com.ycg.ksh.entity.persistent.Order;
import com.ycg.ksh.entity.persistent.driver.OrderDeliver;
import com.ycg.ksh.entity.service.enterprise.OrderSearch;
import org.apache.ibatis.session.RowBounds;

import java.util.Collection;

public interface OrderDeliverMapper extends CustomMapper<OrderDeliver> {


    /**
     * 查询运单记录
     * @param search
     * @return
     */
    Collection<Order> pageOrderDeliver(OrderSearch search);
    /**
     * 分页查询运单记录
     * @param search
     * @param rowBounds
     * @return
     */
    Page<Order> pageOrderDeliver(OrderSearch search, RowBounds rowBounds);

}