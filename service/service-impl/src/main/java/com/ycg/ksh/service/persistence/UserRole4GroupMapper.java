package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.UserRole4Group;
import tk.mybatis.mapper.common.Mapper;

/**
 * 用户相对与组的角色持久类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 09:28:25
 */
public interface UserRole4GroupMapper extends Mapper<UserRole4Group> {
    
    /**
     * 更新项目成员角色(根据项目和用户编号)
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 14:35:44
     * @param role4Group
     */
    void update(UserRole4Group role4Group);
    
    /**
     * 查询
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-29 11:20:44
     * @param groupid  项目组编号
     * @param userid   用户编号
     * @param roleid   角色编号
     */
    UserRole4Group get(Integer groupid, Integer userid, Integer roleid);
    
}