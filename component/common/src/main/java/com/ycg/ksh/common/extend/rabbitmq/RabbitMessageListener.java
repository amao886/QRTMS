package com.ycg.ksh.common.extend.rabbitmq;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/29 0029
 */

import com.ycg.ksh.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RabbitMessage
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/29 0029
 */
public interface RabbitMessageListener {

    boolean handleMessage(String messageType, String messageKey, Object object) throws BusinessException;
}
