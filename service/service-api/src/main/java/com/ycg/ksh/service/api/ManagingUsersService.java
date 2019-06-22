package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.entity.persistent.ManagingUsers;
import com.ycg.ksh.entity.service.ManageUser;
import com.ycg.ksh.entity.service.PageScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 后台管理用户业务类
 *
 * @author wangke
 * @date 2018/3/20 10:49
 */
public interface ManagingUsersService {

    final Logger logger = LoggerFactory.getLogger(ManagingUsersService.class);

    /**
     * 分页查询用户列表
     *
     * @param managingUsers
     * @param pageScope
     * @return
     * @author wangke
     * @date 2018/3/20 10:44
     */
    CustomPage<ManageUser> queryUsersList(ManagingUsers managingUsers, PageScope pageScope) throws ParameterException, BusinessException;

    /**
     * 添加用户
     *
     * @param uKey
     * @param users
     * @author wangke
     * @throws ParameterException
     * @throws BusinessException
     */
    void saveUser(int uKey, ManagingUsers users) throws ParameterException, BusinessException;

    /**
     * 修改用户真实姓名
     *
     * @param users
     * @author wangke
     * @date 2018/3/20 10:45
     */
    void updateUserInfo(int uKey, ManagingUsers users) throws ParameterException, BusinessException;

    /**
     * 删除用户
     *
     * @param id
     * @author wangke
     * @date 2018/3/20 10:46
     */
    void deleteUser(int uKey, Integer id) throws ParameterException, BusinessException;

    /**
     * 根据ID 查询用户信息
     *
     * @param id
     * @return
     * @author wangke
     * @date 2018/3/20 10:47
     */
    ManagingUsers queryUserById(Integer id) throws ParameterException, BusinessException;

    /**
     * 绑定微信用户
     *
     * @param users
     * @author wangke
     * @date 2018/3/20 11:04
     */
    void bindUser(ManagingUsers users) throws ParameterException, BusinessException;


    /**
     * 用户登陆
     *
     * @return
     * @author wangke
     * @date 2018/3/20 14:01
     */
    ManagingUsers userlogin(ManagingUsers users) throws ParameterException, BusinessException;


    /**
     * 查询该用户是否绑定
     *
     * @param userId
     * @param adminKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @author wangke
     */
    ManagingUsers bindUser(Integer userId, Integer adminKey) throws ParameterException, BusinessException;
}
