package com.ycg.ksh.api.backstage.controller;

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.entity.common.wechat.SnsapiScope;
import com.ycg.ksh.entity.common.wechat.WeChatConstant;
import com.ycg.ksh.entity.adapter.wechat.UserInfo;
import com.ycg.ksh.adapter.api.SmsService;
import com.ycg.ksh.adapter.api.WeChatApiService;
import com.ycg.ksh.constant.CoreConstants;
import com.ycg.ksh.entity.persistent.ManagingUsers;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.service.AuthorizeUser;
import com.ycg.ksh.entity.service.user.UserContext;
import com.ycg.ksh.service.api.ManagingUsersService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;


@Controller("backstage.wechat.controller")
@RequestMapping("/backstage/wechat")
public class WeChatController extends BaseController {

    @Resource
    WeChatApiService apiService;

    @Resource
    SmsService smsService;

    @Resource
    ManagingUsersService managingUsersService;

    @RequestMapping(value = "/auth")
    public void auth(HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestObject object = new RequestObject(request.getParameterMap());
        if (SystemUtils.debug()) {
            String openId = object.get("openid");
            logger.debug("======调试模式登陆========> openid: {}", openId);
            Assert.notNull(openId, "调试模式下 OPENID 不能为空");

            UserContext context = new UserContext(CoreConstants.LOGIN_TYPE_DEBUG, CoreConstants.LOGIN_CLIENT_PC, SystemUtils.env());
            context.setUnionid(openId);
            context.setCreate(false);
            context.setClientHost(RequestUitl.getRemoteHost(request));
            AuthorizeUser authorize = userService.login(context);
            if (authorize == null) {
                throw new BusinessException("用户[" + openId + "]未注册");
            }
            RequestUitl.modifyUserInfo(request, authorize);
            response.sendRedirect(RequestUitl.getServerPath(request));
        } else {
            String code = object.get("code");
            String state = object.get("state");
            Integer adminKey = object.getInteger("adminKey");
            logger.debug("======网页授权回调========> code: {}, state : {} adminKey: {}", code, state, adminKey);
            Assert.notNull(code, "微信授权码获取异常");
            UserInfo weChatInfo = apiService.swapInfoByCode(code, SnsapiScope.LOGIN);
            if (weChatInfo != null) {
                UserContext context = new UserContext(CoreConstants.LOGIN_TYPE_SCAN, CoreConstants.LOGIN_CLIENT_PC, SystemUtils.env());
                context.setUnionid(weChatInfo.getUnionid());
                context.setOpenid(weChatInfo.getOpenid());
                context.setSubscribe(weChatInfo.getSubscribe());
                context.setUname(weChatInfo.getNickname());
                context.setHeadImg(weChatInfo.getHeadimgurl());
                context.setClientHost(RequestUitl.getRemoteHost(request));
                logger.debug("======网页授权回调========> 用户注册 : {}", context);
                AuthorizeUser authorize = userService.login(context);
                RequestUitl.modifyUserInfo(request, authorize);
                response.sendRedirect(RequestUitl.getServerPath(request));
            } else {
                throw new BusinessException("获取微信用户信息异常");
            }
        }
    }

    /**
     * 跳转登陆页面
     * <p>
     *
     * @param request
     * @param model
     * @return
     * @throws Exception
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-12 10:56:40
     */
    @RequestMapping(value = "/login")
    public String login(HttpServletRequest request, Model model) throws Exception {
        boolean debug = SystemUtils.debug();
        if (debug) {
            try {
                Collection<User> collection = userService.listUserBySomething(new User());
                collection = collection.stream().parallel().filter(u -> {
                    //过滤一些没有用的
                    if (StringUtils.isNotEmpty(u.getOpenid()) && StringUtils.isNotEmpty(u.getUname())) {
                        String phone = u.getMobilephone();
                        if (StringUtils.isNotEmpty(phone) && phone.length() >= 11) {
                            return true;
                        }
                    }
                    return false;
                }).collect(Collectors.toCollection(ArrayList::new));
                model.addAttribute("users", collection);
                String call_back_url = SystemUtils.buildUrl(RequestUitl.getServerPath(request), "backstage/wechat/auth");
                model.addAttribute("redirect_uri", call_back_url);
            } catch (Exception e) {
                logger.error("获取用户异常，调试模式取消", e);
                debug = false;
            }
        }
        if (!debug) {
            String call_back_url = SystemUtils.buildUrl(RequestUitl.getServerPath(request), "backstage/wechat/auth");
            call_back_url = call_back_url.replace("qlm.scmjia.com", "ksh.ycgwl.com");
            model.addAttribute("redirect_uri", URLEncoder.encode(call_back_url, Constant.CHARACTER_ENCODING_UTF8));
            model.addAttribute("appId", apiService.getAppId(SnsapiScope.LOGIN));
            model.addAttribute("scope", WeChatConstant.AUTH_SCOPE_LOGIN);
            model.addAttribute("state", RequestUitl.getSessionId(request));
        }
        model.addAttribute("debug", debug);
        return "/login";
    }

    /**
     * 用户登陆
     *
     * @param requestObject
     * @param request
     * @return
     * @author wangke
     * @date 2018/3/12 16:58
     */
    @RequestMapping(value = "/login/admin")
    @ResponseBody
    public JsonResult adminLogin(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        Assert.notNull(object, Constant.PARAMS_ERROR);
        Assert.notBlank(object.get("verificationCode"), "验证码不能为空");
        JsonResult jsonResult = new JsonResult();
        Object validCode = request.getSession().getAttribute(IMAGE_CODE_PREFIX);
        if (validCode == null) {
            throw new ParameterException("验证码过期，请重新获取");
        }
        if (!String.valueOf(validCode).equalsIgnoreCase(object.get("verificationCode"))) {
            throw new ParameterException("验证码不正确");
        }
        ManagingUsers manager = new ManagingUsers();
        manager.setUsername(object.get("username"));
        manager.setPassword(object.get("password"));
        Assert.notBlank(manager.getUsername(), "登陆账号不能为空");
        Assert.notBlank(manager.getPassword(), "登陆密码不能为空");
        manager.setLastLoginIp(RequestUitl.getRemoteHost(request));
        manager = managingUsersService.userlogin(manager);
        if (null == manager.getUserId() || manager.getUserId() <= 0) {
            String call_back_url = SystemUtils.buildUrl(RequestUitl.getServerPath(request), "backstage/wechat/auth?adminKey=" + manager.getId());
            call_back_url = call_back_url.replace("qlm.scmjia.com", "ksh.ycgwl.com");
            jsonResult.put("redirect_uri", URLEncoder.encode(call_back_url, Constant.CHARACTER_ENCODING_UTF8));
            jsonResult.put("appId", apiService.getAppId(SnsapiScope.LOGIN));
            jsonResult.put("scope", WeChatConstant.AUTH_SCOPE_LOGIN);
            jsonResult.put("state", RequestUitl.getSessionId(request));
            jsonResult.put("status", false);
        } else {
            jsonResult.put("status", true);
            UserContext context = new UserContext(manager.getUserId(), CoreConstants.LOGIN_TYPE_ACCOUNT, CoreConstants.LOGIN_CLIENT_PC, manager.getLastLoginIp(), SystemUtils.env());
            RequestUitl.modifyUserInfo(request, userService.login(context));
        }
        return jsonResult;
    }

}
