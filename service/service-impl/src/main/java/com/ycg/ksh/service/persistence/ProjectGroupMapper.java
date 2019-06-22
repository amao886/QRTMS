package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.ProjectGroup;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 项目组持久类
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 09:23:04
 */
public interface ProjectGroupMapper extends Mapper<ProjectGroup> {

    /**
     * 查询用户组信息
     * <p>
     *
     * @param userKey 用户编号
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-24 09:37:49
     */
    List<ProjectGroup> listByUserKey(Integer userKey);

    /**
     * 根据收货人联系电话查询项目组
     * <p>
     *
     * @param number
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-02 10:25:10
     */
    List<ProjectGroup> listByContactNumber(String number);

}