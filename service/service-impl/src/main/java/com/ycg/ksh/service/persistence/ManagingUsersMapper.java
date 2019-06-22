package com.ycg.ksh.service.persistence;

import com.github.pagehelper.Page;
import com.ycg.ksh.entity.persistent.ManagingUsers;
import com.ycg.ksh.entity.service.ManageUser;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.common.Mapper;

/**
 * 后台管理用户持久类
 *
 * @author wangke
 * @date 2018/3/20 9:56
 */

public interface ManagingUsersMapper extends Mapper<ManagingUsers> {


    /**
     * 分页查询用户列表
     *
     * @param managingUsers
     * @param rowBounds
     * @return
     * @author wangke
     * @date 2018/3/20 10:44
     */
    Page<ManageUser> queryUsersList(ManagingUsers managingUsers, RowBounds rowBounds);

    /**
     * 添加用户
     *
     * @param users
     * @author wangke
     * @date 2018/3/20 10:45
     */
    void saveUser(ManagingUsers users);


    /**
     * 校验用户是否存在
     *
     * @param users
     * @return
     * @author wangke
     * @date 2018/3/20 14:11
     */
    ManagingUsers checkUserInfo(ManagingUsers users);


    /**
     * 根据用户ID查询管理信息
     * @param userKey
     * @return
     */
    ManagingUsers loadByUserID(Integer userKey);
}
