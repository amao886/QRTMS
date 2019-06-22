package com.ycg.ksh.api.external.interceptor;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/22
 */

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.encrypt.RSA;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 对外接口拦截器
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/22
 */
public class ExternalInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ExternalInterceptor.class);

    private static final String ACCESS_KEY = "access_key";
    private static final String SECRET_KEY = "secret_key";
    private static final String SIGN_KEY = "sign_val";
    private static final String TIMESTAMP_KEY = "timestamp";
    private static final String NONCE_KEY = "nonce";


    private static final long MAX_TIME = TimeUnit.SECONDS.toMinutes(1L);

    @Resource
    protected CacheManager cacheManger;

    private String getValue(HttpServletRequest requestWrapper, String key){
        String value = requestWrapper.getHeader(key);
        if(StringUtils.isBlank(value)){
            value = requestWrapper.getParameter(key);
        }
        return value;
    }

    private boolean validate(String accessKey, HttpServletRequest request){
        Map<String, String> parameters = parameters(request);
        String sign = parameters.remove(SIGN_KEY);
        String content = createLinkString(parameters);
        String publicKey = "";
        try {
            if(!RSA.verify(content, sign, publicKey)){
                throw new BusinessException("接口安全验证未通过");
            }
        } catch (Exception e) {
            throw new BusinessException("接口服务器异常");
        }
        return true;
    }

    private boolean validate(String accessKey, String nonce, String timestamp){
        String cacheKey = accessKey +"#"+ nonce;
        Long ctime = Long.parseLong(timestamp);
        Object lastTime = cacheManger.get(cacheKey);
        if(lastTime != null){
            long times = Long.parseLong(lastTime.toString());
            if(ctime - times < MAX_TIME){
                throw new BusinessException("接口安全验证未通过");
            }
        }
        cacheManger.set(cacheKey, ctime, 1L, TimeUnit.MINUTES);
        return true;
    }


    private boolean validate(HttpServletRequest requestWrapper){
        String accessKey = getValue(requestWrapper, ACCESS_KEY);
        Assert.notBlank(accessKey, "AccessKey不能为空");
        String nonce = getValue(requestWrapper, NONCE_KEY);
        Assert.notBlank(nonce, "参数["+ NONCE_KEY +"]不能为空");
        String timestamp = getValue(requestWrapper, TIMESTAMP_KEY);
        Assert.notBlank(timestamp, "参数["+ TIMESTAMP_KEY +"]不能为空");
        return validate(accessKey, nonce, timestamp) && validate(accessKey, requestWrapper);
    }

    private Map<String, String> parameters(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            map.put(paramName, request.getParameter(paramName));
        }
        return map;
    }

    /**
     * <p>map参数排序</p>
     * @param params
     * @return
     * 2017年1月12日下午4:29:45
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            builder.append(key).append("=").append(value).append("&");
        }
        if(builder.length() > 0){
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }


    /**
     * @see HandlerInterceptorAdapter#preHandle(HttpServletRequest, HttpServletResponse, Object)
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-27 09:41:28
     * @param httpServletRequest
     * @param httpServletResponse
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        HttpServletRequest requestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
        logger.info("对外接口安全校验拦截器: {}", requestWrapper.getRequestURI());
        if (validate(requestWrapper)) {
            return super.preHandle(requestWrapper, httpServletResponse, handler);
        }else {
            throw new BusinessException("接口安全验证未通过");
        }
    }

    /**
     * @see HandlerInterceptorAdapter#postHandle(HttpServletRequest, HttpServletResponse, Object, ModelAndView)
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-27 09:41:36
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * @see HandlerInterceptorAdapter#afterCompletion(HttpServletRequest, HttpServletResponse, Object, Exception)
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-27 09:41:40
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception{

    }
}
