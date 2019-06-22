package com.ycg.ksh.common.event;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 领域事件发布者
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/04 0004
 */
public class DomainEventPublisher {

    private static final ThreadLocal<Collection<DomainEventSubscriber>> subscribers = ThreadLocal.withInitial(ArrayList::new);

    private static final ThreadLocal<Boolean> publishing = ThreadLocal.withInitial(() -> Boolean.FALSE);


    private enum Singleton{

        PUBLISHER;

        private DomainEventPublisher instance;

        private Singleton(){
            instance = new DomainEventPublisher();
        }

        public DomainEventPublisher instance(){
            return instance;
        }
    }

    public static DomainEventPublisher instance(){
        return Singleton.PUBLISHER.instance();
    }

    public <T> void publish(final T event){
        if(publishing.get()){
            return;
        }
        try {
            publishing.set(Boolean.TRUE);
            final Class<?> eventType = event.getClass();
            Collection<DomainEventSubscriber> regSubscribers = subscribers.get();
            if(CollectionUtils.isNotEmpty(regSubscribers)){
                for (DomainEventSubscriber<T> subscriber : regSubscribers) {
                    subscriber.handleEvent(event);
                }
            }
        } finally {
            publishing.set(Boolean.FALSE);
        }
    }

    public <T> void subscribe(DomainEventSubscriber<T> subscriber){
        if(publishing.get()){
            return;
        }
        subscribers.get().add(subscriber);
    }


}
