package com.ycg.ksh.common.event;

/**
 * 领域事件订阅者
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/04 0004
 */
public interface DomainEventSubscriber<T> {


    /**
     * 处理领域事件
     * @param event  领域事件
     */
    void handleEvent(T event);

    /**
     * 订阅的领域事件类型
     * @return
     */
    Class<?>[] subscribedType();
}
