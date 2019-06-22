package com.ycg.ksh.service.persistence.enterprise;

import com.ycg.ksh.common.extend.mybatis.CustomMapper;
import com.ycg.ksh.entity.persistent.enterprise.OrderExtra;

public interface OrderExtraMapper extends CustomMapper<OrderExtra> {

    OrderExtra selectByType(Integer type, Long key);
}