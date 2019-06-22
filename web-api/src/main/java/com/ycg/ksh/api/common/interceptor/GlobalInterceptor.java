package com.ycg.ksh.api.common.interceptor;

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.ResistDuplicateRequest;
import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.common.system.SystemUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局拦截
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-27 09:04:08
 */
public class GlobalInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalInterceptor.class);
	

    @Resource
    protected CacheManager cacheManger;
	/**
	 * @see HandlerInterceptorAdapter#preHandle(HttpServletRequest, HttpServletResponse, Object)
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-07 10:32:15
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//为日志框架打印用户ID准备数据

		String sessionKey = RequestUitl.getSessionId(request), token = RequestUitl.getToken(request), clientKey = RequestUitl.getClientKey(request);
		logger.debug("-----------------------------------------------request---------------------------------------------------------------------------");
		logger.debug("URI : {}", request.getRequestURI());
		logger.debug("sessionKey : {}", sessionKey);
		logger.debug("token : {}", token);
		logger.debug("clientKey : {}", clientKey);
		logger.debug("host : {}", RequestUitl.getRemoteHost(request));
		logger.debug("-----------------------------------------------------------------------------------------------------------------------------------------------");

		MDC.put("wclient", clientKey);
		request.setAttribute("baseImg", SystemUtils.imagepath());
		boolean resist = ResistDuplicateRequest.build().validate(request, sessionKey, cacheManger);
        if(resist) {
            return super.preHandle(request, response, handler);
        }
        //重复请求
        return false;
	}


    /**
     * @see HandlerInterceptorAdapter#afterCompletion(HttpServletRequest, HttpServletResponse, Object, Exception)
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-07 10:32:10
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ResistDuplicateRequest.build().clean(request, RequestUitl.getSessionId(request), cacheManger);
        super.afterCompletion(request, response, handler, ex);
    }
}
