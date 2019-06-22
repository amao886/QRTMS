package com.ycg.ksh.adapter.impl;

import com.ycg.ksh.adapter.api.MessageQueueService;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.extend.rabbitmq.MediaMessage;
import com.ycg.ksh.common.extend.rabbitmq.QueueKeys;
import com.ycg.ksh.common.extend.rabbitmq.RabbitmqUtil;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * MQ推送实现
 *
 * @author wangke
 * @create 2018-07-13 15:41
 **/
@Service("ksh.net.service.messageQueueService")
public class MessageQueueServiceImpl implements MessageQueueService {

    @Resource
    private RabbitTemplate template;

    private Message complete(Message message, MediaMessage mediaMessage){
        if(mediaMessage.getExpiration() > 0){
            message.getMessageProperties().setExpiration(String.valueOf(TimeUnit.SECONDS.toMicros(mediaMessage.getExpiration())));
        }
        RabbitmqUtil.messageKey(message, mediaMessage.getMessageKey());
        String messageType = mediaMessage.getMessageType();
        if(StringUtils.isBlank(messageType) && mediaMessage.getObject() != null){
            messageType = mediaMessage.getObject().getClass().getName();
        }
        RabbitmqUtil.messageType(message, messageType);
        return message;
    }

    @Override
    public void sendMessage(String exchangeKey, String routingKey, MediaMessage mediaMessage) {
        logger.info("发送消息 {} {} {}", exchangeKey, routingKey, mediaMessage.correlationKey());
        try{
            template.convertAndSend(exchangeKey, routingKey, mediaMessage.getObject(), m -> complete(m, mediaMessage), new CorrelationData(mediaMessage.correlationKey()));
        }catch (AmqpException e){
            throw new BusinessException("消息发送异常", e);
        }
    }

    public void sendMessage(MediaMessage mediaMessage) {
        sendMessage(QueueKeys.ROUTE_IN_DIRECT, mediaMessage);
    }
    @Override
    public void sendMessage(String routingKey, MediaMessage mediaMessage) {
        sendMessage(QueueKeys.EXCHANGE_DIRECT, routingKey, mediaMessage);
    }

    @Override
    public void sendNetMessage(MediaMessage mediaMessage) {
        sendMessage(QueueKeys.ROUTE_IN_DIRECT_NET, mediaMessage);
    }

    @Override
    public void sendCoreMessage(MediaMessage mediaMessage) {
        sendMessage(QueueKeys.ROUTE_IN_DIRECT_CORE, mediaMessage);
    }

    @Override
    public void sendCoreMessage(Collection<MediaMessage> collection) {
        for (MediaMessage mediaMessage : collection) {
            sendCoreMessage(mediaMessage);
        }
    }

    @Override
    public void sendCollectMessage(MediaMessage mediaMessage) {
        sendMessage(QueueKeys.ROUTE_IN_DIRECT_COLLECT, mediaMessage);
    }

    @Override
    public void sendCollectMessage(Collection<MediaMessage> collection) {
        for (MediaMessage mediaMessage : collection) {
            sendCollectMessage(mediaMessage);
        }
    }

    @Override
    public void sendApiMessage(MediaMessage mediaMessage) {
        sendMessage(QueueKeys.ROUTE_IN_DIRECT_API, mediaMessage);
    }

    @Override
    public void sendApiMessage(Collection<MediaMessage> collection) {
        for (MediaMessage mediaMessage : collection) {
            sendApiMessage(mediaMessage);
        }
    }

}
