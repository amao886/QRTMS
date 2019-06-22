package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.EmployeeAuthority;
import com.ycg.ksh.entity.persistent.SysMenu;
import com.ycg.ksh.entity.service.MergeAuthorityMenu;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

public interface EmployeeAuthorityMapper extends Mapper<EmployeeAuthority> {

    Collection<SysMenu> listSysMenu(Long cKey, Integer eKey);
    /**
     * 查询用户拥有的权限
     *
     * @param cKey
     * @param eKey
     * @param mType
     * @return
     * @author wangke
     * @date 2018/6/7 16:16
     */
    Collection<EmployeeAuthority> listHaveAuth(Long cKey, Integer eKey, Integer mType);

    /**
     * 
     * TODO Add description
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-06-29 16:24:14
     * @param cKey
     * @param eKey
     * @return
     */
    Collection<MergeAuthorityMenu> listAuthorityMenu(Long cKey, Integer eKey);
    

    /**
     * 批量插入
     * @param authorities
     */
    void inserts(Collection<EmployeeAuthority> authorities);

}