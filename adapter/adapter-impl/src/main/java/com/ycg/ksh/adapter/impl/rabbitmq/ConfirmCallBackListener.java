package com.ycg.ksh.adapter.impl.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;

/**
 * MQ监听
 *
 * @author wangke
 * @create 2018-07-16 11:32
 **/
public class ConfirmCallBackListener implements ConfirmCallback {

    final Logger logger = LoggerFactory.getLogger(ConfirmCallBackListener.class);

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        logger.info("confirm :" + correlationData + ",ack:" + ack + ",cause:" + cause);
        if(ack == false && correlationData != null){
            logger.warn("发送消息失败：{}", correlationData.getId());
        }
    }
}
