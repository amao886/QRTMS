package com.ycg.ksh.api.common.interceptor;

import com.alibaba.dubbo.rpc.RpcException;
import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.ResistDuplicateRequest;
import com.ycg.ksh.api.mobile.util.WxConstant;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.LoginException;
import com.ycg.ksh.common.exception.PromptException;
import com.ycg.ksh.common.exception.TMCException;
import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.common.system.FrontUtils;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.DateUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.common.wechat.SnsapiScope;
import com.ycg.ksh.adapter.api.WeChatApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionResolver extends SimpleMappingExceptionResolver {

	final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);

    @Resource
    protected CacheManager cacheManger;
	@Resource
	private WeChatApiService apiService;

	private static final ModelAndView EMPTY = new ModelAndView();


	private ModelAndView loginException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, LoginException ex){
		logger.debug("登录异常==============> {}", ex.getMessage());
		ResponseBody body = handler.getMethodAnnotation(ResponseBody.class);
		if (body == null) {
			if(RequestUitl.isMobileDevice(request)){
				String token = RequestUitl.getToken(request);
				if(ex.notBind()){
					return new ModelAndView(new RedirectView(FrontUtils.bingPhone(token, RequestUitl.buildUrl(request))));
				}
				request.setAttribute("exception_url", request.getRequestURL());
				request.setAttribute("exception_message", ex.getMessage());
				setDefaultErrorView("mobile_exception");
			}else{
				LoginException exception = (LoginException) ex;
				if(exception.notLogin()){
					return new ModelAndView("forward:/special/wechat/scan/login");
				}
				if(exception.notBind()){
					return new ModelAndView("/bind");
				}
			}
			return super.doResolveException(request, response, handler, ex);
		}
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		RequestUitl.response(response, new JsonResult(false, ex.getMessage()));

		return EMPTY;
	}

	private ModelAndView promptException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, PromptException ex){
		logger.debug("提示==============>{}", ex.getPromptMessage());
		ResponseBody body = handler.getMethodAnnotation(ResponseBody.class);
		if (body == null) {
			request.setAttribute("exception_url", request.getRequestURL());
			request.setAttribute("exception_message", ex.getPromptMessage());
			if(RequestUitl.isMobileDevice(request)){
				request.setAttribute("prompt_exception", true);
				setDefaultErrorView("mobile_exception");
			}else{
				request.setAttribute("exception_type", ex.getClass().getName());
				request.setAttribute("exception_time", DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
				setDefaultErrorView("exception");
			}
			return super.doResolveException(request, response, handler, ex);
		}
		response.setStatus(HttpStatus.OK.value());
		RequestUitl.response(response, new JsonResult(false, ex.getPromptMessage()));

		return EMPTY;
	}

	private ModelAndView exception(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception ex){
		String message = ex.getMessage(), code = null;
		boolean printThrowable = true;
		if(ex instanceof TMCException) {
			TMCException tmcex = (TMCException) ex;
			message = tmcex.getFriendlyMessage();
			if(ex instanceof BusinessException){
				code = ((BusinessException) ex).getCode();
			}
			printThrowable = tmcex.isPrintThrowable();
		}else if(ex instanceof RpcException){
			message = TMCException.RPC_MESSAGE;
		}else {
			message = TMCException.SERVER_MESSAGE;
		}
		if(printThrowable){
			logger.error("异常: {} -> {}", message, request.getRequestURL(), ex);
		}else{
			logger.error("异常: {} -> {}", message, request.getRequestURL());
		}
		ResponseBody body = handler.getMethodAnnotation(ResponseBody.class);
		if (body == null) {
			request.setAttribute("exception_url", request.getRequestURL());
			request.setAttribute("exception_message", message);
			request.setAttribute("exception_type", ex.getClass().getName());
			request.setAttribute("exception_time", DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			if(RequestUitl.isMobileDevice(request)){
				String front_index = null, token = RequestUitl.getToken(request);
				if(StringUtils.isNotBlank(token)) {
					front_index = FrontUtils.index(token);
				}else {
					String callback = SystemUtils.getCallBackDomain() + "/mobile/wechat/auth";
					front_index = apiService.authorizeUrl(callback, WxConstant.STATE_INDEX, SnsapiScope.BASE);
				}
				request.setAttribute("front_index", front_index);
				setDefaultErrorView("mobile_exception");
			}else{
				setDefaultErrorView("exception");
			}
			return super.doResolveException(request, response, handler, ex);
		}
		response.setStatus(HttpStatus.OK.value());
		RequestUitl.response(response, new JsonResult(code, false, message));

		return EMPTY;
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		response.setCharacterEncoding(Constant.CHARACTER_ENCODING_UTF8);
		ResistDuplicateRequest.build().clean(request, RequestUitl.getSessionId(request), cacheManger);
		if(handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			if(ex instanceof LoginException){
				return loginException(request, response, handlerMethod, (LoginException) ex);
			} else if(ex instanceof PromptException){
				return promptException(request, response, handlerMethod, (PromptException) ex);
			} else {
				return exception(request, response, handlerMethod, ex);
			}
		}
		return super.doResolveException(request, response, handler, ex);
	}

}