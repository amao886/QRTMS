package com.ycg.ksh.adapter.impl.rabbitmq;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/21
 */

import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.adapter.mq.QueueKeys;
import org.springframework.amqp.core.Message;

import java.util.Optional;

/**
 * mq工具类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/21
 */
public class RabbitmqUtil {

    public static String messageType(Message message) {
        return Optional.ofNullable(message.getMessageProperties()).map(mp -> mp.getHeaders()).map(h -> h.get(QueueKeys.MESSAGE_TYPE_KEY)).map(t -> t.toString()).orElse(QueueKeys.MESSAGE_TYPE_EMPTY);
    }

    public static String messageKey(Message message) {
        return message.getMessageProperties().getMessageId();
    }

    public static void messageType(Message message, String type) {
        message.getMessageProperties().setHeader(QueueKeys.MESSAGE_TYPE_KEY, type);
    }

    public static void messageKey(Message message, String key) {
        message.getMessageProperties().setMessageId(Optional.ofNullable(key).orElse(StringUtils.UUID()));
    }
}
