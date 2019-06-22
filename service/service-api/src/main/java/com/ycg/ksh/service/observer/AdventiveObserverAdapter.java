package com.ycg.ksh.service.observer;

import com.ycg.ksh.constant.ObjectType;

import java.io.Serializable;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/9
 */
public interface AdventiveObserverAdapter {

    /**
     * 查询详情数据
     * @param objectType 对象类型
     * @param objectKey  对象编号
     * @return
     */
    Object adventiveObjectByKey(ObjectType objectType, Serializable objectKey) throws Exception;
}
