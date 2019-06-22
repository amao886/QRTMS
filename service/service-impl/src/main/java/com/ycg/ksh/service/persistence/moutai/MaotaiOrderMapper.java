package com.ycg.ksh.service.persistence.moutai;

import com.github.pagehelper.Page;
import com.ycg.ksh.entity.persistent.moutai.Order;
import com.ycg.ksh.entity.service.moutai.MaotaiOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;
import java.util.List;

public interface MaotaiOrderMapper extends Mapper<Order> {


    void inserts(Collection<Order> collection);


    Page<Order> searchOrderPage(MaotaiOrder search, RowBounds bounds);

    void updateOrderForAddDepot(@Param("depot") String depot, @Param("ids")List<String> ids);

    void deleteBatchByIds(@Param("ids") Collection<String> ids);

    Collection<Order> selectOrderByIds(@Param("ids") Collection<Long> ids);

    /**
     * sign: 打印状态 1:已打印,0:未打印
     * User: wyj
     */
    void updatePrintSignByIds(@Param("ids")Collection<Long> ids,@Param("sign")Integer sign);
}