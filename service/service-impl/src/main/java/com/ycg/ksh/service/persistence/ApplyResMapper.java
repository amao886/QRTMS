package com.ycg.ksh.service.persistence;

import com.github.pagehelper.Page;
import com.ycg.ksh.entity.persistent.ApplyRes;
import com.ycg.ksh.entity.service.MergeApplyRes;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.common.Mapper;

/**
 * 资源申请持久类
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 09:16:54
 */
public interface ApplyResMapper extends Mapper<ApplyRes> {

    /**
     * 条码管理分页查询列表
     *
     * @Author：wangke
     * @description：
     * @Date：14:50 2018/1/4
     */
    Page<MergeApplyRes> queryApplyResList(ApplyRes applyRes, RowBounds bounds);

    /**
     * 根据用户ID查询已使用条数和总条数
     *
     * @Author：wangke
     * @description：
     * @Date：13:26 2018/1/5
     */
    MergeApplyRes queryTotalCount(Integer userKey);

    Page<MergeApplyRes> queryApplyResListv2(ApplyRes applyRes, RowBounds rowBounds);
    /**
     * 
     * TODO 查询所有条码申请记录
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-08-08 11:04:16
     * @param uname
     * @param mobilePhone
     * @param rowBounds
     * @return
     */
    Page<MergeApplyRes> queryAllApplayRes(@Param("uname") String uname, @Param("mobilePhone") String mobilePhone,RowBounds rowBounds);
}