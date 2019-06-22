/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-06 13:41:55
 */
package com.ycg.ksh.service.observer;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.entity.service.AuthorizeUser;
import com.ycg.ksh.entity.service.user.UserContext;

/**
 * 用户观察者，用户注册、登陆等操作都会通知该接口的实现者
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-06 13:41:55
 */
public interface UserObserverAdapter {

    /**
     * 用户事件通知
     * <p>
      * @param authorizeUser
     * @param type 事件类型
     * @throws BusinessException
     */
    public default void notifyUserChange(AuthorizeUser authorizeUser, Integer type, UserContext context) throws BusinessException{
        //default do nothing
    }

    /**
     * 初始化用户信息
     * <p>
     * @param authorizeUser
     * @param type 事件类型
     * @throws BusinessException
     */
    public default void initializeUser(AuthorizeUser authorizeUser, Integer type) throws BusinessException{
        //default do nothing
    }
}
