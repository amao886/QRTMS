package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.OrderCommodity;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

public interface OrderCommodityMapper extends Mapper<OrderCommodity> {

    void inserts(Collection<OrderCommodity> collection);

    /**
     * 根据订单编号查询货物编号
     * @param orderKey
     * @return
     */
    Collection<Long> listKeyByOrderKey(Long orderKey);


    /**
     * 批量删除货物信息
     * @param keys
     */
    void deleteByKeys(Collection<Long> keys);
}