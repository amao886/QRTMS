package com.ycg.ksh.api.common.interceptor;

import com.ycg.ksh.api.Authoritys;
import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.annotation.SecurityPermission;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.LoginException;
import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.entity.service.AuthorizeUser;
import com.ycg.ksh.service.api.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Optional;


public class SecurityInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);

    @Resource
    UserService userService;
    @Resource
    CacheManager cacheManager;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse HttpServletResponse, Object handler) throws Exception {
        HttpServletRequest requestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
        String sessionKey = RequestUitl.getSessionId(requestWrapper), token = RequestUitl.getToken(requestWrapper);
        AuthorizeUser user = null;
        Object object = cacheManager.get(sessionKey);
        if(object != null){
            user = (AuthorizeUser) object;
            cacheManager.delete(sessionKey);
        }
        user = Optional.ofNullable(user).orElseGet(() -> {
            if (StringUtils.isNotBlank(token)) {
                return userService.loginByToken(token);
            }
            return RequestUitl.getUserInfo(requestWrapper);
        });
        if(user == null){
            if(RequestUitl.isMobileDevice(requestWrapper)){//如果是移动端，就提示 token 无效
                throw new BusinessException("Token失效");
            }else{
                throw LoginException.notLoginException("用户未登陆");//如果是PC端，就跳转去登陆页面
            }
        }
        requestWrapper.setAttribute(RequestUitl.GUESTS_KEY, user.getId());
        if(RequestUitl.isMobileDevice(requestWrapper)){
            requestWrapper.setAttribute(RequestUitl.CATEGORY_KEY, "mobile");
        }else{
            requestWrapper.setAttribute(RequestUitl.CATEGORY_KEY, "backstage");
        }
        MDC.put("wclient", String.valueOf(user.getId()));
        RequestUitl.modifyUserInfo(requestWrapper, user);
        RequestUitl.modifyUserID(requestWrapper, user.getId());

        if(handler instanceof HandlerMethod) {
            validatePermission((HandlerMethod) handler, user.getAuthorityKeys());
        }
        return super.preHandle(requestWrapper, HttpServletResponse, handler);
    }

    private void validatePermission(HandlerMethod handler, Collection<String> authoritys){
        SecurityPermission permission = handler.getMethodAnnotation(SecurityPermission.class);
        if (permission != null && StringUtils.isNotBlank(permission.code())) {
            if(!authoritys.contains(permission.code())){
                throw new BusinessException("您没有权限对此进行["+ Authoritys.convert(permission.code()) +"]操作");
            }
        }
    }
}
