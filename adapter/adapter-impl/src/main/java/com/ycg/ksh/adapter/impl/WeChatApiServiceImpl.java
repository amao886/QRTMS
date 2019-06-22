/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-25 15:11:20
 */
package com.ycg.ksh.adapter.impl;

import com.alibaba.fastjson.JSONObject;
import com.ycg.ksh.adapter.api.WeChatApiService;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.common.extend.cache.CacheObject;
import com.ycg.ksh.common.extend.http.HttpClient;
import com.ycg.ksh.common.extend.rabbitmq.RabbitMessageListener;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.system.SystemKey;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.HttpUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.common.util.encrypt.MD5;
import com.ycg.ksh.common.util.encrypt.SHA;
import com.ycg.ksh.entity.common.wechat.SnsapiScope;
import com.ycg.ksh.entity.common.wechat.WeChatConstant;
import com.ycg.ksh.entity.common.wechat.message.Message;
import com.ycg.ksh.entity.common.wechat.message.MessageBuilder;
import com.ycg.ksh.entity.common.wechat.message.common.TextMessage;
import com.ycg.ksh.adapter.SysNetKey;
import com.ycg.ksh.entity.adapter.wechat.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.annotation.Resource;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 微信接口实现
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-25 15:11:20
 */
@Service("ksh.net.service.weChatApiService")
public class WeChatApiServiceImpl implements WeChatApiService, RabbitMessageListener {

    private static final String PRE_URL = "https://api.weixin.qq.com/cgi-bin";

    @Resource
    CacheManager cacheManager;
    
    private String url(String baseUrl, String subUrl){
        if(baseUrl.endsWith("/")){
            return baseUrl + subUrl;
        }
        return baseUrl +"/"+ subUrl;
    }
    
    
    private CacheObject localCache(String key) {
        Object cacheValue = cacheManager.get(key);
        if(cacheValue != null) {
            return (CacheObject) cacheValue;
        }
        return null;
    }
    
    private CacheObject localCache(String key, CacheObject effective) {
        if(key != null && effective != null && effective.getValue() != null) {
            cacheManager.set(key, effective, effective.getExpired(), TimeUnit.MILLISECONDS);
        }
        return effective;
    }
    
    private JSONObject validate(String resultString){
        if(StringUtils.isNotBlank(resultString)) {
            JSONObject jsonObject = JSONObject.parseObject(resultString);
            String errcode = jsonObject.getString("errcode");
            if(StringUtils.isNotBlank(errcode) && !StringUtils.equalsIgnoreCase("0", jsonObject.getString("errcode"))){
                throw new BusinessException(jsonObject.getString("errmsg"));
            }
            return jsonObject;
        }else {
            throw new BusinessException("解析微信返回异常 :" + resultString);
        }
    }
    
    /**
     * @see WeChatApiService#getAppId(SnsapiScope)
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 10:49:02
     */
    @Override
    public String getAppId(SnsapiScope scope) {
        if(SnsapiScope.LOGIN == scope) {
            return SystemUtils.get(SysNetKey.NET_WX_LOGIN_APPID);
        }else {
            return SystemUtils.get(SysNetKey.NET_WX_COMMON_APPID);
        }
    }


    /**
     * @see WeChatApiService#authorizeUrl(java.lang.String, java.lang.String, SnsapiScope)
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 11:36:23
     */
    @Override
    public String authorizeUrl(String redirect_uri, String state, SnsapiScope scope) {
        if(scope == null) {
            scope = SnsapiScope.USERINFO;
        }
        String appId = getAppId(scope);
        try {
            redirect_uri = URLEncoder.encode(redirect_uri, "utf-8");
        } catch (UnsupportedEncodingException e1) { }
        return "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+ appId +"&redirect_uri="+ redirect_uri +"&response_type=code&scope="+ scope.getKey() +"&state="+ state +"#wechat_redirect";
    }

    @Override
    public JsApiTicket swapJsApiTicket(String targetUrl) throws ParameterException, BusinessException {
        Assert.notNull(targetUrl, "目标地址不能为空");
        JsApiTicket ticket = loadJsApiTicket(); 
        if(ticket != null) {
            try {
                ticket.setNonceStr(StringUtils.UUID());
                ticket.setTimestamp(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                String signature = SHA.encrypt("jsapi_ticket=" + ticket.getTicket() + "&noncestr=" + ticket.getNonceStr() + "&timestamp=" + ticket.getTimestamp() + "&url=" + targetUrl);
                ticket.setSignature(signature);
                logger.debug("jsapi -> targetUrl:{} ticket:{}", targetUrl, ticket);
            } catch (Exception e) {
                logger.error("SwapJsApiTicket targetUrl:{}", targetUrl, e);
                throw new BusinessException("swapJsApiTicket 异常", e);
            }
        }
        return ticket;
    }
    
    private synchronized JsApiTicket loadJsApiTicket() {
        CacheObject effective = localCache(WeChatConstant.CACHE_KEY_JSAPITICKET);
        if(effective == null || !effective.isAvailable()){
            try {
                JsApiTicket ticket = new JsApiTicket();
                logger.info("sync JsApiTicket");
                CommonToken token = loadToken(SnsapiScope.BASE);
                ticket.setAppId(token.getAppId());
                String response = HttpUtils.get(url(PRE_URL, "ticket/getticket?access_token="+ token.getToken() +"&type=jsapi" ));
                logger.info("sync JsApiTicket {}", response);
                JSONObject jsonObject = validate(response);
                ticket.setTicket(jsonObject.getString("ticket"));
                ticket.setExpires(jsonObject.getLong("expires_in"));
                effective = localCache(WeChatConstant.CACHE_KEY_JSAPITICKET, new CacheObject(ticket, ticket.getExpires() - 10, TimeUnit.SECONDS));
            } catch (Exception e) {
                logger.error("LoadJsApiTicket scope:{}", SnsapiScope.BASE, e);
                throw new BusinessException("JsApiTicket 获取异常", e);
            }
        }
        if(effective != null) {
            return (JsApiTicket) effective.getValue(); 
        }
        return null;
    }

    @Override
    public String long2short(String url) {
        try {
            CommonToken token = loadToken(SnsapiScope.BASE);
            HttpClient client = new HttpClient(url(PRE_URL, "shorturl?access_token="+ token.getToken()));
            client.parameter("action", "long2short");
            client.parameter("long_url", url);
            String respone = client.json();
            logger.info("发送模板消息 : {}", client);
            JSONObject jsonObject = validate(respone);
            return jsonObject.getString("short_url");
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch(Exception e){
            logger.error("长链接转短链接异常 {}", url, e);
            throw new BusinessException("长链接转短链接 异常", e) ;
        }
    }

    @Override
    public CommonToken loadToken(SnsapiScope scope) throws ParameterException, BusinessException {
        CacheObject effective = localCache(scope.getCacheKey());
        if(effective == null || !effective.isAvailable()){
            effective = syncToken(scope);
        }
        CommonToken commonToken = (CommonToken) effective.getValue();
        if(!validateToken(commonToken.getToken())) {//校验token的有效性
            effective = syncToken(scope);
        }
        return (CommonToken)effective.getValue();
    }
    
    private synchronized CacheObject syncToken(SnsapiScope scope){
        try {
            CommonToken token = new CommonToken();
            logger.info("sync token 失效需要重新获取");
            token.setAppId(SystemUtils.get(SysNetKey.NET_WX_COMMON_APPID));
            token.setSecret(SystemUtils.get(SysNetKey.NET_WX_COMMON_APPSECRET));
            if(SnsapiScope.LOGIN == scope) {
                token.setAppId(SystemUtils.get(SysNetKey.NET_WX_LOGIN_APPID));
                token.setSecret(SystemUtils.get(SysNetKey.NET_WX_LOGIN_APPSECRET));
            }
            String response = HttpUtils.get(url(PRE_URL, "token?grant_type=client_credential&appid="+ token.getAppId() +"&secret="+ token.getSecret() ));
            logger.info("SyncToken {}", response);
            JSONObject jsonObject = validate(response);
            token.setToken(jsonObject.getString("access_token"));
            token.setExpires(jsonObject.getLong("expires_in"));
            return localCache(scope.getCacheKey(), new CacheObject(token, token.getExpires() - 10, TimeUnit.SECONDS));
        } catch (Exception e) {
            logger.error("SyncToken scope:{}", scope, e);
            throw new BusinessException("access_token 获取异常", e);
        }
    }
    
    private boolean validateToken(String token) {
        try {
            validate(HttpUtils.get(url(PRE_URL, "getcallbackip?access_token="+ token )));
            return true;
        } catch (Exception e) {
            logger.warn("validateToken : {}", e.getMessage());
            return false;
        }
    }
    
    private UserInfo convert(JSONObject jsonObject) {
        UserInfo weChatInfo = new UserInfo();
        weChatInfo.setSubscribe(jsonObject.getString("subscribe"));
        weChatInfo.setOpenid(jsonObject.getString("openid"));
        weChatInfo.setCountry(jsonObject.getString("country"));
        weChatInfo.setPrivilege(jsonObject.getString("province"));
        weChatInfo.setCity(jsonObject.getString("city"));
        weChatInfo.setHeadimgurl(jsonObject.getString("headimgurl"));
        weChatInfo.setNickname(jsonObject.getString("nickname"));
        weChatInfo.setSex(jsonObject.getString("sex"));
        weChatInfo.setPrivilege(jsonObject.getString("privilege"));
        
        String unionid = jsonObject.getString("unionid");
        if(StringUtils.isNotBlank(unionid)) {
            weChatInfo.setUnionid(unionid);
        }else {
            weChatInfo.setUnionid(weChatInfo.getOpenid());
        }
        return weChatInfo;
    }

    @Override
    public UserInfo swapInfoByOpenId(String openId, SnsapiScope scope) throws ParameterException, BusinessException{
        CacheObject effective = localCache(openId);
        if(effective == null || !effective.isAvailable()){
            CommonToken token = loadToken(scope);
            String urlString = url(PRE_URL, "user/info?access_token="+ token.getToken() +"&openid="+ openId +"&lang=zh_CN");
            try {
                String respone = HttpUtils.get(urlString);
                logger.info("SwapInfoByOpenId : {}", respone);
                UserInfo userInfo = convert(validate(respone));
                effective = localCache(userInfo.getOpenid(), new CacheObject(userInfo, 24, TimeUnit.HOURS));
            } catch (Exception e) {
                logger.error("SwapInfoByOpenId openId:{} scope:{}", openId, scope, e);
                throw new BusinessException("获取微信用户信息异常", e);
            }
        }
        if(effective != null) {
            return (UserInfo) effective.getValue();
        }
        return null;
    }

    @Override
    public UserInfo swapInfoByCode(String code, SnsapiScope scope) throws ParameterException, BusinessException{
        return swapInfoByAccessToken(swapAccessTokenByCode(code, scope));
    }

    @Override
    public AccessToken swapAccessTokenByCode(String code, SnsapiScope scope) throws ParameterException, BusinessException{
        try {
        	logger.debug("swapAccessTokenByCode===================>:{} scope:{}",code,scope);
            CacheObject effective = localCache(code);
            if(effective == null || !effective.isAvailable()){
                AccessToken token = new AccessToken();
                token.setAppId(SystemUtils.get(SysNetKey.NET_WX_COMMON_APPID));
                token.setSecret(SystemUtils.get(SysNetKey.NET_WX_COMMON_APPSECRET));
                if(SnsapiScope.LOGIN == scope) {
                	logger.debug("scope==============>:{}",scope);
                    token.setAppId(SystemUtils.get(SysNetKey.NET_WX_LOGIN_APPID));
                    token.setSecret(SystemUtils.get(SysNetKey.NET_WX_LOGIN_APPSECRET));
                }
                String urlString = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+ token.getAppId() +"&secret="+ token.getSecret() +"&code="+ code +"&grant_type=authorization_code";
                String result =  HttpUtils.get(urlString);
                logger.info("SwapAccessTokenByCode ==================>{}:", result);
                JSONObject jsonObject = validate(result);
                token.setAccessToken(jsonObject.getString("access_token"));
                token.setRefreshToken(jsonObject.getString("refresh_token"));
                token.setOpenId(jsonObject.getString("openid"));
                token.setUnionId(jsonObject.getString("unionid"));
                if(StringUtils.isBlank(token.getUnionId()) && !SystemUtils.needUnionid()){
                    token.setUnionId(token.getOpenId());
                }
                token.setScope(jsonObject.getString("scope"));
                token.setExpires(jsonObject.getLong("expires_in"));
                effective = localCache(code, new CacheObject(token, 290, TimeUnit.SECONDS));
            }
            logger.debug("AccessToken value===================>:{}",effective.getValue());
            return (AccessToken) effective.getValue();
        } catch (Exception e) {
            logger.error("SwapAccessTokenByCode code:{} scope:{}", code, scope, e);
            throw new BusinessException("SwapAccessTokenByCode 异常", e);
        }
    }
    
    /**
     * @see WeChatApiService#swapInfoByAccessToken(com.ycg.ksh.entity.adapter.wechat.AccessToken)
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-25 15:11:20
     */
    @Override
    public UserInfo swapInfoByAccessToken(AccessToken accessToken) throws ParameterException, BusinessException{
        CacheObject effective = localCache(accessToken.getOpenId());
        if(effective == null || !effective.isAvailable()){
            String urlString = "https://api.weixin.qq.com/sns/userinfo?access_token="+ accessToken.getAccessToken() +"&openid="+ accessToken.getOpenId() +"&lang=zh_CN";
            try {
                String respone = HttpUtils.get(urlString);
                logger.info("SwapInfoByAccessToken : {}", respone);
                UserInfo weChatInfo = convert(validate(respone));
                effective = localCache(weChatInfo.getOpenid(), new CacheObject(weChatInfo, 1, TimeUnit.HOURS));
            } catch (Exception e) {
                logger.error("SwapInfoByAccessToken {}", accessToken, e);
                throw new BusinessException("获取微信用户信息异常", e);
            }
        }
        return (UserInfo) effective.getValue();
    }
    
    /**
     * @see WeChatApiService#subscribe(java.lang.String)
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-25 15:11:20
     */
    @Override
    public boolean subscribe(String openId) throws ParameterException, BusinessException{
        CommonToken token = loadToken(SnsapiScope.BASE);
        String urlString = url(PRE_URL, "user/info?access_token="+ token.getToken() +"&openid="+ openId +"&lang=zh_CN");
        try {
            String respone = HttpUtils.get(urlString);
            logger.info("subscribe : {}", respone);
            JSONObject jsonObject = validate(respone);
            if("1".equals(jsonObject.getString("subscribe"))) {
                return true;
            }
        } catch (Exception e) {
            logger.error("验证是否关注时, 获取微信用户信息异常", e);//此处不抛异常，因为是否关注影响不大
        }
        return false;
    }
    
    /**
     * @see WeChatApiService#sendMessage(java.lang.String)
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-25 15:11:20
     */
    @Override
    public void sendMessage(String message) throws ParameterException, BusinessException{
        try {
            CommonToken token = loadToken(SnsapiScope.BASE);
            String url = url(PRE_URL, "message/custom/send?access_token=" + token.getToken() );
            logger.info("push message : {}", message);
            String result = HttpUtils.post(url, message);
            logger.info("push message result : {}", result);
        }catch(Exception e){
            logger.error("push message exception", e);
        }
    }
    
    /**
     * @see WeChatApiService#sendMessage(Message)
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-25 15:11:20
     */
    @Override
    public void sendMessage(Message message) throws ParameterException, BusinessException{
        sendMessage(Globallys.toJsonString(message));
    }

	@Override
	public Collection<String> downImages(Collection<String> serverKeys) throws ParameterException, BusinessException {
		if(CollectionUtils.isNotEmpty(serverKeys)) {
			Collection<String> collection = new ArrayList<String>(serverKeys.size());
			CommonToken accessToken = loadToken(SnsapiScope.BASE);
			String parent = "wx";//DateUtils.formatDate(new Date(), "yyyyMMdd");
			for (String mediaId : serverKeys) {
				collection.add(download(accessToken.getToken(), mediaId, parent, 3));
			}
			return collection;
		}
		return Collections.emptyList();
	}
	
	private String download(String accessToken, String mediaId, String parent, int maxCount) {
		String filePath = null;
		String url = url(PRE_URL, "media/get?access_token=" + accessToken + "&media_id=" + mediaId);
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpget = new HttpGet(url);
            try (CloseableHttpResponse response = httpclient.execute(httpget)) {
            	String suffix = null;
            	Header header = response.getLastHeader("Content-Type");
            	if(header != null) {
            		suffix = StringUtils.trim(header.getValue().substring(6));//image/gif  image/jpeg  image/png 
            	}
            	if(!FileUtils.isImage(suffix)) {
            		suffix = "jpeg";
            	}
            	HttpEntity entity = response.getEntity();
            	filePath = FileUtils.path(parent, FileUtils.appendSuffix(MD5.encrypt(mediaId), suffix));
                File desc = FileUtils.file(FileUtils.path(SystemUtils.directoryUpload(), filePath), true);
                try (InputStream is = entity.getContent();  OutputStream os = new FileOutputStream(desc)) {
                    StreamUtils.copy(is, os);
                }
                logger.info("downimage -> {} to {}", mediaId, desc);
            }
        } catch(Throwable e){
        	if(maxCount <= 0) {
        		throw new BusinessException("从微信服务器下载多媒体文件异常 :" + mediaId, e) ;
        	}else {
        		return download(accessToken, mediaId, parent, --maxCount);
        	}
        }
		return "/"+ filePath;
	}

    /**
     * 发送模板消息
     *
     * @param messsage
     * @throws ParameterException
     * @throws BusinessException
     * @return
     */
    @Override
    public String sendTemplateMessage(TemplateMesssage messsage) throws ParameterException, BusinessException {
        String templateId = SystemUtils.get(messsage.templateKey(SystemKey.WX_MSG_TEMPLATE_ID));
        if(StringUtils.isBlank(templateId)){
            throw new ParameterException("模板消息ID没有配置");
        }
        try {
            CommonToken token = loadToken(SnsapiScope.BASE);
            HttpClient client = new HttpClient(url(PRE_URL, "message/template/send?access_token=" + token.getToken()));
            client.parameter("touser", messsage.getTouser());
            client.parameter("template_id", templateId);
            client.parameter("url", messsage.getUrl());
            client.parameter("data", messsage.getTemplateDatas());
            String respone = client.json();
            logger.info("发送模板消息 : {}", client);
            JSONObject jsonObject = validate(respone);
            return jsonObject.getString("msgid");
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch(Exception e){
            logger.error("发送模板消息 {}", messsage, e);
            throw new BusinessException("发送模板消息异常", e) ;
        }
    }

    @Override
    public boolean handleMessage(String messageType, String messageKey, Object object) throws BusinessException {
        if(StringUtils.equalsIgnoreCase(TemplateMesssage.class.getName(), messageType)){
            try{
                String msgKey = sendTemplateMessage((TemplateMesssage) object);
                logger.info("发送模板消息 {}", msgKey);
            }catch (Exception e){
                logger.error("发送模板消息 {}", object, e);
            }
            return true;
        }else if(StringUtils.equalsIgnoreCase(Message.class.getName(), messageType)){
            MessageBuilder builder = MessageBuilder.build();
            Message message = (Message) object;
            try{
                logger.debug("发送微信消息:{}", message);
                if (message instanceof TextMessage) {
                    sendMessage(builder.textMessage(message.getFromUserName(), ((TextMessage) message).getContent()));
                }
            }catch (Exception e){
                sendMessage(builder.textMessage(message.getFromUserName(), "系统繁忙,请稍后再试"));
                logger.error("发送微信消息 {}", object, e);
            }
            return true;
        }
        return false;
    }
}
