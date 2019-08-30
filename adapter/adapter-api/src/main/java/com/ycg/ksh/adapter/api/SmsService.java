/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-20 10:04:55
 */
package com.ycg.ksh.adapter.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;

/**
 * 发送短信相关接口
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-20 10:04:55
 */
public interface SmsService {
    
    /**
     * 生成验证码
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-20 10:07:46
     * @param count
     * @return
     */
    String randomCode(int count);


    /**
     * 发送短信验证码
     * @param mobile
     * @throws ParameterException
     * @throws BusinessException
     */
    String sendSmsCode(String mobile) throws ParameterException, BusinessException;
    /**
     * 发送短信验证码
     * @param mobile
     * @param template
     * @throws ParameterException
     * @throws BusinessException
     */
    String sendCode(String mobile, String template) throws ParameterException, BusinessException;
   
    
    /**
     * 发送短信接口
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-20 10:07:36
     * @param mobile
     * @param content
     * @throws ParameterException
     * @throws BusinessException
     */
    void sendmsg(String mobile, String content) throws ParameterException, BusinessException;
}
