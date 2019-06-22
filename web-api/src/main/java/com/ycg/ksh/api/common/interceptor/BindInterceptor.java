package com.ycg.ksh.api.common.interceptor;

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.common.exception.LoginException;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class BindInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(BindInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse HttpServletResponse, Object handler) throws Exception {
        String sessionKey = RequestUitl.getClientKey(httpServletRequest);
        logger.debug("绑定验证拦截器->clientKey:{} uri:{}", sessionKey, httpServletRequest.getRequestURI());
        User user = RequestUitl.getUserInfo(httpServletRequest);
        if(user != null && StringUtils.isBlank(user.getMobilephone())) {
            throw LoginException.notBindException("用户未绑定手机号");
        }
        return super.preHandle(httpServletRequest, HttpServletResponse, handler);
    }
}
