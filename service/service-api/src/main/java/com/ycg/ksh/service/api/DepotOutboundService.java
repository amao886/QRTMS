package com.ycg.ksh.service.api;

import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.entity.persistent.depot.InboundOrder;
import com.ycg.ksh.entity.persistent.depot.OutboundDetail;
import com.ycg.ksh.entity.persistent.depot.OutboundOrder;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.depot.DepotAlliance;
import com.ycg.ksh.entity.service.depot.DepotBatchSomething;
import com.ycg.ksh.entity.service.depot.DepotSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 * 仓库出入库逻辑接口
 *
 * @Auther: wangke
 * @Date: 2018/9/4 15:51
 * @Description:
 */
public interface DepotOutboundService {

    final Logger logger = LoggerFactory.getLogger(DepotOutboundService.class);


    /**
     * 根据批次号获取批次详情
     *
     * @param batchNumber
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    InboundOrder getByBatchNumber(String batchNumber) throws ParameterException, BusinessException;

    /**
     * 功能描述: 导入入库单
     *
     * @param uKey
     * @param map
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @auther: wangke
     * @date: 2018/9/5 11:19
     */
    void saveInboundOrders(Integer uKey, Map<String, InboundOrder> map) throws ParameterException, BusinessException;

    /**
     * 分页查询
     *
     * @param search
     * @param scope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    CustomPage<DepotAlliance> pageInboundOrder(Integer uKey, DepotSearch search, PageScope scope) throws ParameterException, BusinessException;

    /**
     * 批次查询
     *
     * @param uKey
     * @param search
     * @param scope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    CustomPage<DepotBatchSomething> pageBatchSomething(Integer uKey, DepotSearch search, PageScope scope) throws ParameterException, BusinessException;

    /**
     * 出库
     *
     * @param uKey
     * @param order
     * @param details
     * @throws ParameterException
     * @throws BusinessException
     */
    void takeOutDepot(Integer uKey, OutboundOrder order, Collection<OutboundDetail> details) throws ParameterException, BusinessException;

    /**
     * 删除入库单
     *
     * @param ids
     * @throws ParameterException
     * @throws BusinessException
     */
    void deleteInboun(Collection<Long> ids) throws ParameterException, BusinessException;


    /**
     * 导出出库单数据
     *
     * @param key
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    FileEntity listExportOrders(Collection<Long> key) throws Exception;


    /**
     * 查询打印的出货数据
     *
     * @param key
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<DepotAlliance> listPrintGroup(Collection<Long> key) throws ParameterException, BusinessException;
}
