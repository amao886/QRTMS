package com.ycg.ksh.service.persistence.depot;

import com.github.pagehelper.Page;
import com.ycg.ksh.common.extend.mybatis.CustomMapper;
import com.ycg.ksh.entity.persistent.depot.InboundOrder;
import com.ycg.ksh.entity.service.depot.DepotAlliance;
import com.ycg.ksh.entity.service.depot.DepotSearch;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.Collection;

public interface InboundOrderMapper extends CustomMapper<InboundOrder> {


    /**
     * 分页查询入库单列表
     *
     * @param search
     * @param bounds
     * @return
     */
    Page<DepotAlliance> listInboundOrder(DepotSearch search, RowBounds bounds);


    InboundOrder selectByBatchNumber(String batchNumber);

    /**
     * 批量刪除
     *
     * @param ids
     */
    void deletes(@Param("inbounds") Collection<Long> ids);
}
