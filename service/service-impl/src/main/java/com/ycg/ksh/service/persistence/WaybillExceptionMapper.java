package com.ycg.ksh.service.persistence;

import com.github.pagehelper.Page;
import com.ycg.ksh.entity.persistent.WaybillException;
import com.ycg.ksh.entity.service.ExceptionSearch;
import com.ycg.ksh.entity.service.MergeExceptionRepor;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 运单异常持久类
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 09:29:17
 */
public interface WaybillExceptionMapper extends Mapper<WaybillException> {

    /**
     * 根据运单编号查询异常信息
     * <p>
     *
     * @param waybillId
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-30 11:22:38
     */
    List<WaybillException> selectByWaybillId(Integer waybillId);

    /**
     * 录入异常信息
     *
     * @param exception
     * @author wangke
     * @date 2018/3/2 10:25
     */
    void insertException(WaybillException exception);


    /**
     * 查询异常信息列表
     *
     * @param search
     * @param bounds
     * @return
     */
    Page<MergeExceptionRepor> searchExceptionPage(ExceptionSearch search, RowBounds bounds);
}