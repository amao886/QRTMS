package com.ycg.ksh.api.mobile.interceptor;

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.ResistDuplicateRequest;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.entity.persistent.SysRequestSerial;
import com.ycg.ksh.entity.service.AuthorizeUser;
import com.ycg.ksh.service.api.SysRequestSerialService;
import com.ycg.ksh.service.api.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 局部拦截，只拦截需要登陆的请求
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-27 09:41:06
 */
public class MobileInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(MobileInterceptor.class);

	private static final ExecutorService threadPool = Executors.newCachedThreadPool();
	private static final ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();

	@Resource
	protected CacheManager cacheManger;
	@Resource
	protected UserService userService;
	@Resource
	protected SysRequestSerialService requestSerialService;

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
		//拦截微信未登录用户
		String token = RequestUitl.getToken(httpServletRequest);
		if (StringUtils.isNotBlank(token)) {
			boolean resist = ResistDuplicateRequest.build().validate(httpServletRequest, token, cacheManger);
			if(resist) {
				threadLocal.set(System.currentTimeMillis());
				AuthorizeUser user = userService.loginByToken(token);
				if(user == null) {
					throw new BusinessException("Token无效["+ token +"]");
				}
				MDC.put("wclient", String.valueOf(user.getId()));
				RequestUitl.modifyUserInfo(httpServletRequest, user);
				RequestUitl.modifyUserID(httpServletRequest, user.getId());
				return super.preHandle(requestWrapper, httpServletResponse, handler);
			}
	        //重复请求
	        return false;
		}else {
			throw new ParameterException("缺少必要参数：token");
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
		String token = RequestUitl.getToken(request);
		ResistDuplicateRequest.build().clean(request, token, cacheManger);
		super.afterCompletion(request, response, handler, ex);
		Long stime = threadLocal.get();
		if(stime != null && stime > 0) {
			SysRequestSerial serial = new SysRequestSerial("api", request.getHeader("User-Agent"));
			serial.setAppType("api");
			serial.setDateTime(new Date());
			serial.setUserKey(RequestUitl.getUserID(request));
			serial.setUri(request.getRequestURI());
			serial.setHost(RequestUitl.getRemoteHost(request));
			serial.setMethod(request.getMethod());
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						requestSerialService.save(serial);
					} catch (Exception e) {
						logger.warn("用户请求持久化失败");
					}
				}
			});
		}
	}
}
