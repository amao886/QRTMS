package com.ycg.ksh.service.persistence;

import com.github.pagehelper.Page;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.entity.service.*;
import com.ycg.ksh.entity.persistent.ReceiptVerify;
import com.ycg.ksh.entity.persistent.WaybillReceipt;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 运单回单持久类
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 09:29:40
 */
public interface WaybillReceiptMapper extends Mapper<WaybillReceipt> {


    /**
     * 根据运单ID查询回单信息
     * <p>
     *
     * @param waybillId
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-30 11:09:53
     */
    List<WaybillReceipt> selectByWaybillId(Integer waybillId);

    /**
     * 根据运单编号查询回单审核信息
     * <p>
     *
     * @param waybillId 运单编号
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-21 13:08:19
     */
    ReceiptVerify getReceiptVerifyKey(Integer waybillId);

    /**
     * 查询回单图片信息
     * <p>
     *
     * @param waybillId
     * @return
     * @throws ParameterException, BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-26 08:52:53
     */
    List<MergeWayBillReceipt> listImagesByWaybillId(Integer waybillId);


    /**
     * 根据条件查询分组回单统计分析
     *
     * @param statistics keys:selectYear,selectMonth,groupName
     * @return PageInfo<Waybill>
     * @author wangke
     * @date 2018/3/8 10:32
     */
    Page<MergeReceiptStatistics> statisticsWaybill(MergeReceiptStatistics statistics, RowBounds bounds);


    /**
     * 根据项目分组查询总数
     *
     * @param statistics
     * @param bounds
     * @return
     * @author wangke
     * @date 2018/3/9 9:22
     */
    Page<MergeReceiptStatistics> listWaybillGroupBy(MergeReceiptStatistics statistics, RowBounds bounds);

    /**
     * 查询回单列表
     *
     * @param serach
     * @param rowBounds
     * @return
     * @author wangke
     * @date 2018/4/3 16:51
     */
    Page<MergeReceiptResult> queryReceiptList(WaybillSerach serach, RowBounds rowBounds);

    /**
     * 查询纸质回单列表
     *
     * @param record
     * @param rowBounds
     * @return
     * @author wangke
     * @date 2018/4/9 14:34
     */
    Page<MergeOperationRecord> queryPaperyReceiptRecord(MergeOperationRecord record, RowBounds rowBounds);

    /**
     * 查询电子回单列表
     *
     * @param record
     * @param rowBounds
     * @return
     * @author wangke
     * @date 2018/4/9 14:34
     */
    Page<MergeOperationRecord> queryElectronicReceiptRecord(MergeOperationRecord record, RowBounds rowBounds);
}