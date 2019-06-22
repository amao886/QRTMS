/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-12 20:43:47
 */
package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.entity.common.wechat.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 微信相关
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-12 20:43:47
 */
public interface WechatService {

	final Logger logger = LoggerFactory.getLogger(WechatService.class);

	/**
	 * 接受一条微信推送消息
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-12 20:46:42
	 * @param message  微信推送消息
	 * @return  要发送给用户的信息，为空表示不发送任何信息
	 * @throws ParameterException  参数错误时
	 * @throws BusinessException   业务逻辑处理异常时
	 */
	Message receive(Message message) throws ParameterException, BusinessException;

	/**
	 * 接受消息
	 * @param xmlString
	 *
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	void receive(String xmlString) throws ParameterException, BusinessException;
}
