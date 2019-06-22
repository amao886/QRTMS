package com.ycg.ksh.api.common.controller;

import com.google.common.collect.Lists;
import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.common.constant.Directory;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.common.system.SystemKey;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.entity.common.wechat.WeChatConstant;
import com.ycg.ksh.adapter.api.WeChatApiService;
import com.ycg.ksh.constant.OrderFettleType;
import com.ycg.ksh.entity.persistent.Company;
import com.ycg.ksh.entity.persistent.CompanyEmployee;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.service.AuthorizeUser;
import com.ycg.ksh.entity.service.enterprise.CompanyConcise;
import com.ycg.ksh.service.api.BarCodeService;
import com.ycg.ksh.service.api.CompanyService;
import com.ycg.ksh.service.api.PermissionService;
import com.ycg.ksh.service.api.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@Controller
public class BaseController {


    protected final Logger logger = LoggerFactory.getLogger(getClass());


    public static final String SESSION_USER_INFO_KEY = "SESSION_USER_INFO";//回话存储用户信息的键值

    private static final String DATEFORMAT = "yyyy-MM-dd HH:mm";
    public static final String IMAGE_CODE_PREFIX = "IMAGE_VALID_CODE_";//图片验证码存储前缀

    @Resource
    protected CacheManager cacheManger;
    @Resource
    protected CompanyService companyService;
    @Resource
    protected UserService userService;
    @Resource
    protected PermissionService permissionService;
    @Resource
    protected WeChatApiService apiService;
    @Resource
    protected BarCodeService barCodeService;

    protected AuthorizeUser loadUser(HttpServletRequest request) {
        return RequestUitl.getUserInfo(request);
    }

    protected Integer loadUserKey(HttpServletRequest request) {
        AuthorizeUser user = loadUser(request);
        if (user != null) {
            return user.getId();
        }
        return null;
    }
    protected CompanyConcise loadCompanyConcise(HttpServletRequest request) {
        AuthorizeUser user = loadUser(request);
        if (user != null ) {
            if(user.getCompanyKey() != null && user.getCompanyKey() > 0){
                return new CompanyConcise(user.getCompanyKey(), user.getCompanyName());
            }
            Company company = companyService.getCompanyByUserKey(user.getId());
            if(company != null){
                user.setCompanyKey(company.getId());
                user.setCompanyName(company.getCompanyName());

                RequestUitl.modifyUserInfo(request, user);
                return new CompanyConcise(company.getId(), company.getCompanyName());
            }
        }
        throw new BusinessException("当前用户没有加入企业");
    }
    protected Long loadCompanyKey(HttpServletRequest request) {
        AuthorizeUser user = loadUser(request);
        if (user != null ) {
            if(user.getCompanyKey() != null && user.getCompanyKey() > 0){
                return user.getCompanyKey();
            }
            Company company = companyService.getCompanyByUserKey(user.getId());
            if(company != null){
                user.setCompanyKey(company.getId());
                user.setCompanyName(company.getCompanyName());

                RequestUitl.modifyUserInfo(request, user);
                return user.getCompanyKey();
            }
        }
        throw new BusinessException("当前用户没有加入企业");
    }
    protected CompanyEmployee loadCompanyEmployee(HttpServletRequest request) {
        AuthorizeUser user = loadUser(request);
        if (user != null) {
            if(user.getEmployee() == null){
                CompanyEmployee employee = companyService.getCompanyEmployee(user.getId());
                if(employee != null){
                    user.setEmployee(employee);
                    RequestUitl.modifyUserInfo(request, user);
                }
            }
            if(user.getEmployee() != null){
                return user.getEmployee();
            }
        }
        throw new BusinessException("当前用户没有加入企业");
    }

    protected void validateSmsCode(String mobile, String code) throws ParameterException, BusinessException{
        Object validCode = cacheManger.get(SystemUtils.smsKey(mobile));
        logger.debug("bindphone============================>phone {} code {} validCode {}", mobile, code, validCode);
        if (validCode == null) {
            throw new ParameterException("验证码过期，请重新获取");
        }
        if (!validCode.equals(code)) {
            throw new ParameterException("验证码不正确");
        }
    }
    protected void clearSmsCode(String mobile){
        cacheManger.delete(SystemUtils.smsKey(mobile));
    }

    protected boolean subscribe(User user) {
        if (user != null) {
            if (StringUtils.isBlank(user.getPassword())) {
                boolean subscribe = apiService.subscribe(user.getUsername());
                if (apiService.subscribe(user.getUsername())) {
                    user.setPassword(subscribe ? "1" : "0");
                    try {
                        userService.modifySubscribe(user.getOpenid(), user.getPassword());
                    } catch (Exception e) {
                        logger.warn("更新老数据的关注状态异常 {}", e.getMessage());
                    }
                } else {
                    return false;
                }
            }
            return WeChatConstant.SUBSCRIBE_OK.equals(user.getPassword()) ? true : false;
        }
        return false;
    }

    protected String imagepath() {
        return SystemUtils.buildUrl(SystemUtils.get(SystemKey.STATIC_PATH_PREFIX), Directory.UPLOAD.getDir());
    }

    protected String downpath() {
        return SystemUtils.buildUrl(SystemUtils.get(SystemKey.STATIC_PATH_PREFIX), Directory.DOWN.getDir());
    }
    

    public Collection<Integer> convertOrderFettles(Integer fettle) {
        if (null != fettle && fettle > 0) {
            if (fettle >= 4) {
                return Lists.newArrayList(OrderFettleType.COMPLETE.getCode());
            } else {
                return Lists.newArrayList(OrderFettleType.DEFAULT.getCode(), OrderFettleType.ING.getCode());
            }
        }
        return null;
    }
}
