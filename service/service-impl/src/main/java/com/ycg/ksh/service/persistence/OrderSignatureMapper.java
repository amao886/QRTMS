package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.OrderSignature;
import tk.mybatis.mapper.common.Mapper;

public interface OrderSignatureMapper extends Mapper<OrderSignature> {

    /**
     * 根据订单ID 查询签署信息
     *
     * @param signRole
     * @return
     * @author wangke
     * @date 2018/4/20 14:04
     */
    OrderSignature getByOrderId(Long orderId, Integer signRole);

    OrderSignature selectByCompanyKey(Long companyKey, Long orderKey);
}