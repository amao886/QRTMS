/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-25 14:01:43
 */
package com.ycg.ksh.adapter.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.entity.common.wechat.SnsapiScope;
import com.ycg.ksh.entity.common.wechat.message.Message;
import com.ycg.ksh.entity.adapter.wechat.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * 微信接口
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-25 14:01:43
 */
public interface WeChatApiService {
    
    final Logger logger = LoggerFactory.getLogger(WeChatApiService.class);

    public String getAppId(SnsapiScope scope);
    
    public String authorizeUrl(String redirect_uri, String state, SnsapiScope scope);


    /**
     * 长连接转短链接
     * @param url
     *
     * @return
     */
    public String long2short(String url);
    /**
     * 获取token
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 12:44:02
     * @param scope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public CommonToken loadToken(SnsapiScope scope) throws ParameterException, BusinessException;
    /**
     * 交换jsapi校验数据
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-25 14:44:57
     * @param targetUrl
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public JsApiTicket swapJsApiTicket(String targetUrl) throws ParameterException, BusinessException;

    /**
     * 通过openid交换微信用户信息
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-25 14:45:13
     * @param openId
     * @param scope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public UserInfo swapInfoByOpenId(String openId, SnsapiScope scope) throws ParameterException, BusinessException ;
    
    /**
     * 通过临时CODE交换微信用户数据
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-25 14:45:32
     * @param code
     * @param scope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public UserInfo swapInfoByCode(String code, SnsapiScope scope) throws ParameterException, BusinessException ;
    
    /**
     * 通过临时CODE交换accessToken
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-25 14:45:51
     * @param code
     * @param scope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public AccessToken swapAccessTokenByCode(String code, SnsapiScope scope) throws ParameterException, BusinessException ;
    
    /**
     * 通过accessToken交换微信用户数据
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-25 14:46:30
     * @param accessToken
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public UserInfo swapInfoByAccessToken(AccessToken accessToken) throws ParameterException, BusinessException ;
    
    /**
     * 检查用户是否关注了公众号
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-25 14:46:45
     * @param openId
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public boolean subscribe(String openId) throws ParameterException, BusinessException ;

    /**
     * 发送消息
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-25 14:47:10
     * @param message
     * @throws ParameterException
     * @throws BusinessException
     */
    public void sendMessage(String message) throws ParameterException, BusinessException ;

    /**
     * 发送消息
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-25 14:47:18
     * @param message
     * @throws ParameterException
     * @throws BusinessException
     */
    public void sendMessage(Message message) throws ParameterException, BusinessException;
    
    /**
     * 下载图片信息
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-10 09:59:27
     * @param serverKes
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public Collection<String> downImages(Collection<String> serverKes) throws ParameterException, BusinessException;

    /**
     * 发送模板消息
     * @param messsage
     * @throws ParameterException
     * @throws BusinessException
     * @return
     */
    public String sendTemplateMessage(TemplateMesssage messsage) throws ParameterException, BusinessException;
}
