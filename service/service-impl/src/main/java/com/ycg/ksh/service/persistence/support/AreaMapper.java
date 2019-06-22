package com.ycg.ksh.service.persistence.support;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/30
 */

import com.ycg.ksh.common.extend.mybatis.CustomMapper;
import com.ycg.ksh.entity.persistent.support.Area;

import java.util.Collection;

/**
 * 省市区
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/30
 */
public interface AreaMapper extends CustomMapper<Area> {

    /**
     * 根据父编号查询
     *
     * @param parentKey
     *
     * @return
     */
    Collection<Area> selectByParentKey(Integer parentKey);
}
