package com.ycg.ksh.common.extend.rabbitmq;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/29 0029
 */

import com.rabbitmq.client.Channel;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.adapter.AbstractAdaptableMessageListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @TODO 类描述
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/29 0029
 */
public class RabbitAdaptableListener extends AbstractAdaptableMessageListener {


    Logger logger = LoggerFactory.getLogger(RabbitAdaptableListener.class);

    private static final long MAX_ERRORS = 3;

    private Map<String, Integer> errorCounts = new ConcurrentHashMap<String, Integer>();

    private Map<String, RabbitMessageListener> listeners;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        Long deliveryTag = RabbitmqUtil.deliveryTag(message);
        String messageType = RabbitmqUtil.messageType(message);
        String messageKey = RabbitmqUtil.messageKey(message);
        logger.info("接受消息 {} {}", messageType, messageKey);
        try {
            RabbitMessageListener listener = listeners.get(messageType);
            if(listener != null){
                Object object = getMessageConverter().fromMessage(message);
                if(object == null || listener.handleMessage(messageType, messageKey, object)){
                    channel.basicAck(deliveryTag, false);
                }else{
                    Integer count = errorCounts.getOrDefault(messageKey, 0);
                    channel.basicReject(deliveryTag, count <= MAX_ERRORS);//放回队列重新处理, 反之放弃消息
                    errorCounts.put(messageKey, count + 1);
                }
            }
        } catch (Exception e) {
            Integer count = errorCounts.getOrDefault(messageKey, 0);
            channel.basicReject(deliveryTag, count <= MAX_ERRORS);//处理失败，如果失败次数小于 MAX_ERRORS 放回队列重新处理, 反之放弃消息
            errorCounts.put(messageKey, count + 1);
            logger.error("消息处理异常 {} {} 失败次数 {}", messageType, messageKey, count, e);
        }
    }
    public void setListeners(Map<String, RabbitMessageListener> listeners) {
        this.listeners = listeners;
    }
}
