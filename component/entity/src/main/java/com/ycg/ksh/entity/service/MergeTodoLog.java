package com.ycg.ksh.entity.service;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/16
 */

import com.ycg.ksh.entity.persistent.TodoLog;
import org.apache.commons.beanutils.BeanUtils;

/**
 * 待办日志
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/16
 */
public class MergeTodoLog extends TodoLog {


    private AssociateUser user;
    private String timeString;
    private String typeString;

    public MergeTodoLog() {
        super();
    }
    public MergeTodoLog(TodoLog todoLog) throws Exception {
        super();
        BeanUtils.copyProperties(this, todoLog);
    }

    public AssociateUser getUser() {
        return user;
    }

    public void setUser(AssociateUser user) {
        this.user = user;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }
}
