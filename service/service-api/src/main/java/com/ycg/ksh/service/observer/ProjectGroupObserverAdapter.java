/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 14:52:28
 */
package com.ycg.ksh.service.observer;

import com.ycg.ksh.common.exception.BusinessException;

/**
 * 项目组观察者，组成员、角色、权限变更都会通知该类的实现者
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 14:52:28
 */
public interface ProjectGroupObserverAdapter {
    
    int PROJECT_GROUP_ADD = 1;//创建项目组
    int PROJECT_GROUP_MEMBER_ADD = 2;//新增项目成员
    int PROJECT_GROUP_MEMBER_DELETE = 3;//删除项目成员
    int PROJECT_GROUP_ROLE_CHANGE = 4;//项目组成员角色变化
    
    
    /**
     * 项目组信息变化同住
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 15:05:40
     * @param gKey 项目组编号
     * @param uKey 用户编号
     * @param type 变化类型
     * @throws BusinessException
     */
    public default void notifyProjectGroupChange(Integer gKey, Integer uKey, Integer type) throws BusinessException{
        //default do noting
    }
}
