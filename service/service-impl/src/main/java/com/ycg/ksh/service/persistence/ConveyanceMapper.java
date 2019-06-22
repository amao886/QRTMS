package com.ycg.ksh.service.persistence;

import com.github.pagehelper.Page;
import com.ycg.ksh.entity.persistent.Conveyance;
import com.ycg.ksh.entity.service.ConveyanceNode;
import com.ycg.ksh.entity.service.ConveyanceSearch;
import com.ycg.ksh.entity.service.Station;
import com.ycg.ksh.entity.service.WaybillConveyance;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

public interface ConveyanceMapper extends Mapper<Conveyance> {

    /**
     * 批量取消
     * @param collection
     */
    void cancel(Collection<Long> collection);
    /**
     * 根据父节点ID查询子节点
     * @param parentKey
     * @return
     */
    Collection<ConveyanceNode> listConveyanceNodeByParentKey(Long parentKey);
    /**
     * 根据运单ID查询站点信息
     * @param conveyanceKeys
     * @return
     */
    Collection<Station> listStation(Collection<Long> conveyanceKeys);

    /**
     * 批量插入运单数据
     * @param collection
     */
    void inserts(Collection<Conveyance> collection);
    /**
     * 根据任务单查询运单数据
     * @param waybillKey
     * @return
     */
    Collection<Conveyance> listByWaybillKey(Integer waybillKey);
    /**
     * 根据waybillKey获取顶级运单
     * @param waybillKey
     * @return
     */
    Conveyance getByWaybillKey(Integer waybillKey);
    /**
     * 获取还未指派的顶级运单或者指派给指定用户的运单
     * @param barcode 任务单
     * @param uKey 用户ID
     * @return
     */
    Integer countByCode(String barcode, Integer uKey);
    /**
     * 根据运单ID等信息查询运单信息
     * 指定用户创建的、指派给指定用户的、指定用所在项目组的
     * @param search 查询条件
     * @return
     */
    Conveyance getBySomething(ConveyanceSearch search);

    /**
     * 根据父运单查询正常的子运单信息
     * @param parentKey
     * @return
     */
    Collection<Conveyance> listByParentKey(Long parentKey);
    /**
     * 多条件查询运单信息
     * 指定用户创建的、指派给指定用户的、指定用所在项目组的
     * @param search
     * @return
     */
    Collection<WaybillConveyance> listWaybillConveyanceBySomething(ConveyanceSearch search);

    /**
     * 多条件分页查询运单信息
     * 指定用户创建的、指派给指定用户的、指定用所在项目组的
     * @param search
     * @param bounds
     * @return
     */
    Page<WaybillConveyance> listWaybillConveyanceBySomething(ConveyanceSearch search, RowBounds bounds);
}