package com.ycg.ksh.adapter.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.rabbitmq.MediaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * TMS 推送
 *
 * @author wangke
 * @date 2018/7/13 15:39
 */
public interface MessageQueueService {

    Logger logger = LoggerFactory.getLogger(MessageQueueService.class);

    /**
     * 发送消息
     * @param exchangeKey 交换机KEY
     * @param routingKey  路由KEY
     * @param mediaMessage 消息
     * @throws ParameterException
     * @throws BusinessException
     */
    void sendMessage(String exchangeKey, String routingKey, MediaMessage mediaMessage);

    /**
     * 指定路由发生消息
     * @param routingKey  路由KEY
     * @param mediaMessage 消息
     */
    void sendMessage(String routingKey, MediaMessage mediaMessage);

    /**
     * 发送net消息
     * @param mediaMessage 消息
     */
    void sendNetMessage(MediaMessage mediaMessage);

    /**
     * 发送core消息
     * @param mediaMessage 消息
     */
    void sendCoreMessage(MediaMessage mediaMessage);
    void sendCoreMessage(Collection<MediaMessage> collection);


    /**
     * 发送collect消息
     * @param mediaMessage 消息
     */
    void sendCollectMessage(MediaMessage mediaMessage);
    void sendCollectMessage(Collection<MediaMessage> collection);

    /**
     * 发送api消息
     * @param mediaMessage 消息
     */
    void sendApiMessage(MediaMessage mediaMessage);
    void sendApiMessage(Collection<MediaMessage> collection);

}
