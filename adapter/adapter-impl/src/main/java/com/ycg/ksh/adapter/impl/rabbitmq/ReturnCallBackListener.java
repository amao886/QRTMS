package com.ycg.ksh.adapter.impl.rabbitmq;

import com.ycg.ksh.common.extend.rabbitmq.RabbitmqUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;

/**
 * 监听返回
 *
 * @author wangke
 * @create 2018-07-16 11:39
 **/
public class ReturnCallBackListener implements ReturnCallback {

    final Logger logger = LoggerFactory.getLogger(ReturnCallBackListener.class);

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        logger.info("replyCode:" + replyCode + ",replyText:" + replyText + ",exchange:" + exchange + ",routingKey:" + routingKey);
        if(message != null){
            String messageKey = RabbitmqUtil.messageKey(message);
            Object messageType = RabbitmqUtil.messageType(message);
            logger.warn("发送消息失败：{} {}", messageType, messageKey);
        }
    }
}
