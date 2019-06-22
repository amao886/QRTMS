package com.ycg.ksh.api.adventive.interceptor;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/22
 */

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.TMCException;
import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.entity.persistent.adventive.Adventive;
import com.ycg.ksh.service.api.adventive.AdventiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 对外接口拦截器
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/22
 */
public class AdventiveInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AdventiveInterceptor.class);

    private static final String ACCESS_KEY = "qlm-access-key";
    private static final String SIGN_KEY = "qlm-sign-val";
    private static final String TIMESTAMP_KEY = "qlm-timestamp";
    private static final String NONCE_KEY = "qlm-nonce";


    private static final long MAX_TIME = TimeUnit.MINUTES.toSeconds(1L);

    @Resource
    protected CacheManager cacheManger;

    @Resource
    protected AdventiveService adventiveService;


    private boolean validate(Object accessKey, HttpServletRequest requestWrapper){
        Map<String, String> headers = headers(requestWrapper);
        Map<String, Object> parameters = parameters(requestWrapper);
        Object nonce = RequestUitl.getValue(requestWrapper, NONCE_KEY);
        Assert.notNull(nonce, "参数["+ NONCE_KEY +"]不能为空");
        Object timestamp = RequestUitl.getValue(requestWrapper, TIMESTAMP_KEY);
        Assert.notNull(timestamp, "参数["+ TIMESTAMP_KEY +"]不能为空");
        Object signVal = RequestUitl.getValue(requestWrapper, SIGN_KEY);
        Assert.notNull(signVal, "签名字符串不能为空");

        logger.info("必填参数 标识:{} 随机字符:{} 时间戳:{} 签名串:{}", accessKey, nonce, timestamp, signVal);

        long currentime = Long.parseLong(timestamp.toString()), ctime = System.currentTimeMillis();
        if(TimeUnit.MILLISECONDS.toSeconds(ctime) - currentime > MAX_TIME){
            throw new BusinessException("参数timestamp过期,请重新生成后发起请求");
        }
        String cacheKey = accessKey +"#"+ nonce;
        Object lastTime = cacheManger.get(cacheKey);
        if(lastTime != null){
            throw new BusinessException("参数nonce重复,请重新生成后发起请求");
        }
        logger.info("随机字符和时间戳校验通过 标识:{} 随机字符:{} 时间戳:{} 签名串:{}", accessKey, nonce, timestamp, signVal);
        if(!parameters.containsKey(TIMESTAMP_KEY)){
            parameters.put(TIMESTAMP_KEY, timestamp);
        }
        if(!parameters.containsKey(NONCE_KEY)){
            parameters.put(NONCE_KEY, nonce);
        }
        if(!parameters.containsKey(ACCESS_KEY)){
            parameters.put(ACCESS_KEY, accessKey);
        }
        parameters.remove(SIGN_KEY);
        Adventive adventive = adventiveService.validate(Long.parseLong(accessKey.toString()), String.valueOf(signVal), parameters);
        if(adventive == null){
            throw new BusinessException("接口安全验证未通过");
        }
        cacheManger.set(cacheKey, ctime, 1L, TimeUnit.MINUTES);
        logger.info("签名校验通过 标识:{} 随机字符:{} 时间戳:{} 签名串:{}", accessKey, nonce, timestamp, signVal);
        return true;
    }

    private Map<String, Object> parameters(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        Enumeration<String> paramNames = request.getParameterNames();
        logger.info("参数--------------------------------------------------------------");
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            Object value = request.getParameter(name);
            map.put(name, value);
            logger.info("{} : {}", name, value);
        }
        logger.info("-----------------------------------------------------------------");
        return map;
    }

    private Map<String, String> headers(HttpServletRequest request){
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> paramNames = request.getHeaderNames();
        logger.info("头部--------------------------------------------------------------");
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement(), value = request.getHeader(name);
            map.put(name, value);
            logger.info("{} : {}", name, value);
        }
        logger.info("-----------------------------------------------------------------");
        return map;
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
        try{
            HttpServletRequest requestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
            Object accessKey = RequestUitl.getValue(requestWrapper, ACCESS_KEY);
            logger.debug("对外接口安全校验拦截器: {} {}", accessKey, requestWrapper.getRequestURI());
            Assert.notNull(accessKey, "AccessKey不能为空");
            if (validate(accessKey, requestWrapper)) {
                requestWrapper.setAttribute(RequestUitl.GUESTS_KEY, accessKey);
                requestWrapper.setAttribute(RequestUitl.CATEGORY_KEY, "api");
                return super.preHandle(requestWrapper, httpServletResponse, handler);
            }else {
                throw new BusinessException("接口安全验证未通过");
            }
        }catch (Exception e){
            String message;
            if(e instanceof TMCException){
                message = ((TMCException) e).getFriendlyMessage();
            }else{
                message = "系统异常";
            }
            RequestUitl.response(httpServletResponse, new JsonResult(false, message));
        }
        return false;
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
