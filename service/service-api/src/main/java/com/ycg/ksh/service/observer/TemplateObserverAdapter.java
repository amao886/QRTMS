package com.ycg.ksh.service.observer;

import com.ycg.ksh.common.exception.MessageException;
import com.ycg.ksh.entity.service.enterprise.PropertyDescribe;
import com.ycg.ksh.entity.service.enterprise.TemplateContext;

import java.util.Collection;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/9
 */
public interface TemplateObserverAdapter {

    /**
     * 指定模板导入数据时触发的事件
     * @param context 模板上下文
     * @param propertyDescribes  解析后的数据
     *
     * @throws MessageException  处理解析后的数据异常时,所有异常必须转换成 MessageException 异常抛出，否则可能会引起事务回滚
     */
    void importSomething(TemplateContext context, Collection<PropertyDescribe> propertyDescribes) throws MessageException;


    /**
     * 去除重复数据
     * @param context
     * @param propertyDescribes
     *
     * @throws MessageException
     */
    default void distinctSomething(TemplateContext context, Collection<PropertyDescribe> propertyDescribes) throws MessageException{

    }
}
