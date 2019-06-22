package com.ycg.ksh.service.persistence.depot;

import com.github.pagehelper.Page;
import com.ycg.ksh.common.extend.mybatis.CustomMapper;
import com.ycg.ksh.entity.persistent.depot.OutboundDetail;
import com.ycg.ksh.entity.service.depot.DepotAlliance;
import com.ycg.ksh.entity.service.depot.DepotBatchSomething;
import com.ycg.ksh.entity.service.depot.DepotSearch;
import com.ycg.ksh.entity.service.depot.OutBoundPrintAlliance;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.Collection;

public interface OutboundDetailMapper extends CustomMapper<OutboundDetail> {


    Collection<DepotBatchSomething> listBySomething(DepotSearch search);


    Page<DepotBatchSomething> listBySomething(DepotSearch search, RowBounds bounds);

    /**
     * 查询需要导出的值
     *
     * @param keys
     * @return
     */
    Collection<DepotAlliance> listExportOrders(@Param("orders") Collection<Long> keys);

    /**
     * 查询需要打印的数据
     *
     * @param keys 选中的数据ID
     * @return
     */
    Collection<DepotAlliance> listPrintGroup(@Param("orders") Collection<Long> keys);


    /**
     * 根据客户和日期查询需要打印的数据
     *
     * @param customerName 客户名称
     * @param dateFormat   日期
     * @param materialName 品名
     * @return
     */
    Collection<OutBoundPrintAlliance> listOutBoundByGroup(@Param("customerName") String customerName,
                                                          @Param("dateFormat") String dateFormat, @Param("materialName") String materialName);
}