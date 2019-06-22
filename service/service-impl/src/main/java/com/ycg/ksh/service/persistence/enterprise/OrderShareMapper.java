package com.ycg.ksh.service.persistence.enterprise;

import com.github.pagehelper.Page;
import com.ycg.ksh.common.extend.mybatis.CustomMapper;
import com.ycg.ksh.entity.persistent.enterprise.OrderShare;
import com.ycg.ksh.entity.service.enterprise.CompanyConcise;
import com.ycg.ksh.entity.service.enterprise.OrderAlliance;
import com.ycg.ksh.entity.service.enterprise.ShareOrderSearch;
import org.apache.ibatis.session.RowBounds;

import java.util.Collection;

public interface OrderShareMapper extends CustomMapper<OrderShare> {

    /**
     * 我分享的发货公司
     *
     * @param shareCompanyKey
     * @param likeString
     * @return
     */
    Collection<CompanyConcise> selectMySharingCompanyConcise(Integer type, Long shareCompanyKey, String likeString);

    /**
     * 查询我的分享/分享给我的-货主
     *
     * @param type            1:我的分享, 3:分享给我的
     * @param shareCompanyKey
     * @param likeString
     * @return
     */
    Collection<CompanyConcise> selectShipperCompanyConcise(Integer type, Long shareCompanyKey, String likeString);

    /**
     * 分页查询订单信息
     *
     * @param shareSearch 查询条件
     * @param rowBounds   分页信息
     * @return
     */
    Page<OrderAlliance> selectOrderBySomething(ShareOrderSearch shareSearch, RowBounds rowBounds);
}