package com.ycg.ksh.common.extend.rabbitmq;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/21
 */

import com.ycg.ksh.common.util.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.util.Map;
import java.util.Optional;

/**
 * mq工具类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/21
 */
public class RabbitmqUtil {
    public static MessageProperties properties(Message message) {
        return message.getMessageProperties();
    }
    public static Map<String, Object> headers(Message message) {
        return properties(message).getHeaders();
    }
    public static String messageType(Message message) {
        return Optional.ofNullable(headers(message)).map(h -> h.get(QueueKeys.MESSAGE_TYPE_KEY)).map(Object::toString).orElse(QueueKeys.MESSAGE_TYPE_EMPTY);
    }

    public static String messageKey(Message message) {
        return properties(message).getMessageId();
    }

    public static void messageType(Message message, String type) {
        properties(message).setHeader(QueueKeys.MESSAGE_TYPE_KEY, type);
    }

    public static void messageKey(Message message, String key) {
        properties(message).setMessageId(Optional.ofNullable(key).orElse(StringUtils.UUID()));
    }

    public static Long deliveryTag(Message message){
        return Optional.ofNullable(properties(message)).map(MessageProperties::getDeliveryTag).orElse(0L);
    }
}
