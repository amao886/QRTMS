package com.ycg.ksh.api.mobile.controller;

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.api.mobile.util.WxConstant;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.system.FrontUtils;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.common.validate.Validator;
import com.ycg.ksh.entity.common.wechat.SnsapiScope;
import com.ycg.ksh.entity.common.wechat.message.Message;
import com.ycg.ksh.entity.common.wechat.message.MessageBuilder;
import com.ycg.ksh.entity.common.wechat.message.common.TextMessage;
import com.ycg.ksh.entity.adapter.wechat.AccessToken;
import com.ycg.ksh.entity.adapter.wechat.JsApiTicket;
import com.ycg.ksh.entity.adapter.wechat.UserInfo;
import com.ycg.ksh.constant.CoreConstants;
import com.ycg.ksh.constant.EmployeeType;
import com.ycg.ksh.constant.OrderRoleType;
import com.ycg.ksh.entity.persistent.Barcode;
import com.ycg.ksh.entity.persistent.CompanyEmployee;
import com.ycg.ksh.entity.persistent.ProjectGroupMember;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.persistent.enterprise.CompanyCustomer;
import com.ycg.ksh.entity.service.AuthorizeUser;
import com.ycg.ksh.entity.service.barcode.BarcodeContext;
import com.ycg.ksh.entity.service.barcode.CompanyCodeContext;
import com.ycg.ksh.entity.service.barcode.GroupCodeContext;
import com.ycg.ksh.entity.service.user.UserContext;
import com.ycg.ksh.service.api.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Controller("mobile.wechat.controller")
@RequestMapping("/mobile/wechat")
public class WeChatController extends BaseController {

    private static final String QR_CODE_EXPIRED_PREFIX = "QR_CODE_DOWNLOAD_";

    private static ExecutorService executorService;
    @Resource
    WechatService wechatService;
    @Resource
    MapAddressService mapAddressService;
    @Resource
    ProjectGroupService groupService;

    @Resource
    CustomerService customerService;

    @Resource
    CompanyService companyService;

    @PostConstruct
    public void init() {
        executorService = Executors.newCachedThreadPool();
    }


    @RequestMapping(value = "/auth")
    public void auth(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = request.getParameter(WxConstant.PARMA_KEY_CODE);
        String state = request.getParameter(WxConstant.PARMA_KEY_STATE);
        logger.debug("微信授权回调 -> code: {}, state : {}", code, state);
        if (StringUtils.isEmpty(code)) {
            AuthorizeUser authorize = loadUser(request);
            if (authorize != null) {
                response.sendRedirect(redirect(request, authorize, authorize.getToken(), state));
            }
            logger.warn("微信授权回调 -> 用户放弃授权");
        } else {
            AccessToken accessToken = null;
            try {
            	logger.debug("开始了==============》");
                accessToken = apiService.swapAccessTokenByCode(code, SnsapiScope.USERINFO);//code换取accessToken异常
                logger.debug("accessToken============>{}:",accessToken);
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("微信授权回调 -> Code换取AccessToken异常 {} {}", code, e.getMessage());
                response.sendRedirect(buildTwiceAuthUrl(request, state));
                return;
            }
            logger.info("{}================>:", accessToken);
            SnsapiScope scope = SnsapiScope.parse(accessToken.getScope());
            UserContext context = new UserContext(CoreConstants.LOGIN_TYPE_SCAN, CoreConstants.LOGIN_CLIENT_WX, SystemUtils.env());
            context.setUnionid(accessToken.getUnionId());
            context.setOpenid(accessToken.getOpenId());
            if (SnsapiScope.USERINFO == scope) {
                UserInfo userInfo = convertUserByWeChatInfo(context, accessToken);//转换用户信息
                User user = userService.getUserByWechat(context.getUnionid(), context.getOpenid());
                if(user == null){//还未注册

                    logger.debug("用户未注册 {}", accessToken.getOpenId());

                    String uuid = Globallys.UUID();
                    cacheManger.set(uuid, userInfo, 1L, TimeUnit.HOURS);
                    response.sendRedirect(sacnregisterpage(request,uuid));
                    return;
                }
            }else{
                User user = userService.getUserByWechat(context.getUnionid(), context.getOpenid());
                if (user == null || StringUtils.isBlank(user.getOpenid()) || StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getUname()) || StringUtils.isBlank(user.getHeadImg())) {
                    response.sendRedirect(buildTwiceAuthUrl(request, state));
                    return;
                }
            }
            logger.debug("用户登陆 {}", accessToken.getOpenId());
            AuthorizeUser authorize = userService.login(context);
            RequestUitl.modifyToken(request, authorize.getToken());
            response.sendRedirect(redirect(request, authorize, authorize.getToken(), state));
        }
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


    @RequestMapping(value = "/register")
    @ResponseBody
    public JsonResult register(@RequestBody RequestObject object,HttpServletRequest request) throws Exception {
        String uuid = object.get("uuid"), name = object.get("name"), phone = object.get("phone"), code = object.get("code");
        JsonResult jsonResult = new JsonResult(true, "注册成功");
        Object obj = cacheManger.get(uuid);
        if(obj == null){
            throw new BusinessException("请从公众号进入注册");
        }
        Validator validator = Validator.MOBILE;
        if (!validator.verify(phone)) {
            throw new ParameterException(phone, validator.getMessage("手机号"));
        }
        validator = Validator.CHINESENAME;
        if (!validator.verify(name)) {
            throw new ParameterException(name, validator.getMessage("姓名"));
        }
        Assert.notBlank(code, "验证码不能为空");
        validateSmsCode(phone, code);

        UserInfo userInfo = (UserInfo) obj;
        UserContext context = new UserContext(CoreConstants.LOGIN_TYPE_REG, CoreConstants.LOGIN_CLIENT_WX, SystemUtils.env());
        context.setUnionid(userInfo.getUnionid());
        context.setOpenid(userInfo.getOpenid());//用户的openid
        context.setSubscribe(userInfo.getSubscribe());//用户关注公众号的状态
        context.setHeadImg(userInfo.getHeadimgurl());
        context.setUname(name);
        context.setMobilephone(phone);

        AuthorizeUser user = userService.register(context);
        if (user == null) {
            throw new BusinessException("注册异常");
        }
        jsonResult.put("token", user.getToken());
        jsonResult.put("callback", FrontUtils.index(user.getToken()));
        RequestUitl.modifyUserInfo(request, user);
        clearSmsCode(phone);
        return jsonResult;
    }

    @RequestMapping(value = "/callback")
    public void callback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String signature = request.getParameter("signature"),
                timestamp = request.getParameter("timestamp"),
                nonce = request.getParameter("nonce"),
                echostr = request.getParameter("echostr");
        logger.debug("callback ==> signature:{} timestamp:{} nonce:{} echostr:{}", signature, timestamp, nonce, echostr);
        if (StringUtils.isNotBlank(echostr)) {
            RequestUitl.write(response, echostr);
        } else {
            try {
                final MessageBuilder builder = MessageBuilder.build();
                final String xmlString = builder.xmlMessage(request.getInputStream());
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Message message = builder.create(xmlString);
                            logger.debug("解析后的消息:{}", message);
                            try {
                                message = wechatService.receive(message);
                                if (message instanceof TextMessage) {
                                    apiService.sendMessage(builder.textMessage(message.getFromUserName(), ((TextMessage) message).getContent()));
                                }
                            } catch (ParameterException | BusinessException e) {
                                apiService.sendMessage(builder.textMessage(message.getFromUserName(), e.getMessage()));
                            }
                        } catch (Exception e) {
                            logger.error("微信推送消息解析处理异常", e);
                        }
                    }
                });
            } catch (Exception e) {
                logger.error("处理微信推送消息线程执行异常", e);
            }
        }
    }

    private String redirect(HttpServletRequest request, AuthorizeUser user, String token, String state) throws Exception {
        //logger.debug("微信授权回调 -> 跳转: {}", user.getOpenid() +" state "+state);
        String redirect_url = null;
        if (bindphone(user, state)) {
            redirect_url = bindPhone(request, token, state);
        } else if (null != state) {
            if (WxConstant.STATE_INDEX.equals(state)) {//跳转到首页
                redirect_url = index(request, user, token, state);
            } else if (WxConstant.STATE_BING.equals(state)) {
                redirect_url = bindPhone(request, token, state);
            } else if (WxConstant.STATE_SHARE.equals(state)) {//前往分享页面
                redirect_url = shareWaybill(request, user, token, state);
            } else if (WxConstant.STATE_CENTER.equals(state)) {//跳转到个人中心
                redirect_url = center(request, user, token, state);
            } else if (WxConstant.STATE_GROUP.equals(state)) {//分享二维码添加组
                redirect_url = addGroup(request, user, token, state);
            } else if (WxConstant.STATE_DISPATCH.equals(state)) {//授权进入路径规划
                redirect_url = dispatch(request, user, token, state);
            } else if (WxConstant.STATE_ADDRESS.equals(state)) {//分享地址
                redirect_url = shareAddress(request, user, token, state);
            } else if (WxConstant.STATE_SCAN.equals(state)) {//扫码条码
                redirect_url = scanCode(request, user, token, state);
            } else if (WxConstant.STATE_FRIEND.equals(state)) {
                redirect_url = addFriend(request, token);
            } else if (WxConstant.STATE_COMPANY.equals(state)) {
                redirect_url = bindCompany(request, user, token, state);
            }
        }
        if (StringUtils.isBlank(redirect_url)) {
            return FrontUtils.index(token);
        }
        return redirect_url;
    }


    @RequestMapping(value = "/login")
    @ResponseBody
    public JsonResult login(@RequestBody RequestObject object, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("login -> {}", object);
        JsonResult jsonResult = new JsonResult();
        String openId = object.get("openid");
        Assert.notBlank(openId, "微信用户openId不能为空");
        UserInfo userInfo = object.toJavaBean(UserInfo.class);
        UserContext context = new UserContext(CoreConstants.LOGIN_TYPE_SCAN, CoreConstants.LOGIN_CLIENT_WX, SystemUtils.env());
        context.setUname(userInfo.getNickname());
        context.setOpenid(userInfo.getOpenid());
        context.setUnionid(userInfo.getUnionid());
        context.setSubscribe(userInfo.getSubscribe());
        context.setClientHost(RequestUitl.getRemoteHost(request));
        AuthorizeUser authorize = userService.login(context);
        jsonResult.put("results", authorize);
        RequestUitl.modifyToken(request, authorize.getToken());
        jsonResult.put("token", authorize.getToken());
        logger.info("login -> {}", authorize);
        return jsonResult;
    }

    @RequestMapping(value = "/jsapi")
    @ResponseBody
    public JsonResult jsapi(@RequestBody RequestObject object, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String targetUrl = object.get("targetUrl");
        Assert.notNull(targetUrl, "目标地址不能为空");
        JsonResult jsonResult = new JsonResult();
        targetUrl = URLDecoder.decode(targetUrl, Constant.CHARACTER_ENCODING_UTF8);
        JsApiTicket apiTicket = apiService.swapJsApiTicket(targetUrl);
        jsonResult.put("appId", apiTicket.getAppId());
        jsonResult.put("nonceStr", apiTicket.getNonceStr());
        jsonResult.put("timestamp", apiTicket.getTimestamp());
        jsonResult.put("signature", apiTicket.getSignature());
        logger.info("-------------------{}", jsonResult);
        return jsonResult;
    }

    //扫描二维码定位及绑定
    @RequestMapping(value = "/scan/{barcode}")
    public void scanCode(@PathVariable String barcode, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("扫码 -> barcode:{}", barcode);
        Barcode object = barCodeService.validate(barcode);
        if (object != null) {
            String callback_uri = SystemUtils.getCallBackDomain() + "/mobile/wechat/auth?barcode=" + barcode;
            String redirect_uri = apiService.authorizeUrl(callback_uri, WxConstant.STATE_SCAN, SnsapiScope.BASE);
            logger.debug("扫码 -> callback_uri:{} redirect_uri:{}", callback_uri, redirect_uri);
            response.sendRedirect(redirect_uri);
        } else {
            throw new ParameterException(barcode, "无效条码");
        }
    }

    //分享任务链接
    @RequestMapping(value = "/share/{shareId}/{waybillId}")
    public void share(@PathVariable Integer shareId, @PathVariable Integer waybillId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("分享任务 -> shareId:{} waybillId:{}", shareId, waybillId);
        String callback_uri = SystemUtils.getCallBackDomain() + "/mobile/wechat/auth?shareId=" + shareId + "&waybillId=" + waybillId;
        String redirect_uri = apiService.authorizeUrl(callback_uri, WxConstant.STATE_SHARE, SnsapiScope.BASE);
        logger.debug("分享任务 -> callback_uri:{} redirect_uri:{}", callback_uri, redirect_uri);
        response.sendRedirect(redirect_uri);
    }

    //分享地址链接
    @RequestMapping(value = "/share/address")
    public void shareAddress(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String keys = request.getParameter("keys");
        logger.debug("分享地址 -> keys:{}", keys);
        if (StringUtils.isBlank(keys)) {
            throw new ParameterException("对方没有分享任何内容！！！");
        }
        String callback_uri = SystemUtils.getCallBackDomain() + "/mobile/wechat/auth?keys=" + keys;
        String redirect_uri = apiService.authorizeUrl(callback_uri, WxConstant.STATE_ADDRESS, SnsapiScope.BASE);
        logger.debug("分享地址 -> callback_uri:{} redirect_uri:{}", callback_uri, redirect_uri);
        response.sendRedirect(redirect_uri);
    }

    //扫描二维码添加组
    @RequestMapping(value = "/group/{groupId}")
    public void addGroup(@PathVariable Integer groupId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("扫码加组 -> groupId:{}", groupId);
        String callback_uri = SystemUtils.getCallBackDomain() + "/mobile/wechat/auth?groupId=" + groupId;
        String redirect_uri = apiService.authorizeUrl(callback_uri, WxConstant.STATE_GROUP, SnsapiScope.BASE);
        logger.debug("扫码加组 -> callback_uri:{} redirect_uri:{}", callback_uri, redirect_uri);
        response.sendRedirect(redirect_uri);
    }

    //客户关系绑定 (/mobile/wechat/company/associate/{customerKey}/0)
    @RequestMapping(value = "/company/associate/{customerKey}/{expire}")
    public void associateCompany(@PathVariable Long customerKey, @PathVariable Long expire, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("关联 -> customerKey:{} expire:{}", customerKey, expire);
        String callback_uri = SystemUtils.getCallBackDomain() + "/mobile/wechat/auth?customerKey=" + customerKey + "&expire=" + expire;
        String redirect_uri = apiService.authorizeUrl(callback_uri, WxConstant.STATE_COMPANY, SnsapiScope.BASE);
        logger.debug("关联 -> callback_uri:{} redirect_uri:{}", callback_uri, redirect_uri);
        response.sendRedirect(redirect_uri);
    }

    //客户管理-扫描二维码绑定企业
    private String bindCompany(HttpServletRequest request, AuthorizeUser user, String token, String state) {
        Long customerKey = Long.parseLong(request.getParameter("customerKey"));        //客户管理表的主键
        Long expire = Long.parseLong(request.getParameter("expire"));        //客户管理表的主键
        //1.判断expire 是否过期 --2.查询该二维码是否已经被人绑定了 3.缓存中是否存在
        if (expire > 0 && (System.currentTimeMillis() > expire || cacheManger.get(QR_CODE_EXPIRED_PREFIX + customerKey) == null)) {
            return FrontUtils.failurePage(token, expire > 0 ? "二维码已失效" : "链接已经失效");
        }
        CompanyCustomer companyCustomer = customerService.getCompanyCustomer(customerKey);
        if (companyCustomer == null || companyCustomer.getCompanyKey() != null && companyCustomer.getCompanyKey() > 0) {
            return FrontUtils.failurePage(token, expire > 0 ? "二维码已失效" : "链接已经失效");
        }
        //手机号判断
        if (StringUtils.isNotBlank(companyCustomer.getScanPhone())) {
            if (!companyCustomer.getScanPhone().equals(user.getMobilephone())) {
                return FrontUtils.failurePage(token, "与客户指定号码不一致，无法创建企业");
            }
        }
        //2. 查询是否有已经有企业了
        CompanyEmployee employee = companyService.getCompanyEmployee(user.getId());
        if (employee != null && employee.getCompanyId() > 0) {    //有企业
            if (EmployeeType.convert(employee.getEmployeeType()).isManage()) {//管理員
                //绑定的操作
                if (customerService.isCustomer(companyCustomer.getOwnerKey(), employee.getCompanyId())) {//两家企业已经有关联关系,不可重复关联
                    return FrontUtils.failurePage(token, "不可重复关联");
                }
                customerService.associateCompany(customerKey, employee.getCompanyId());
                if (expire > 0) {
                    cacheManger.delete(QR_CODE_EXPIRED_PREFIX + customerKey);
                }
                return FrontUtils.successPage(token, "操作成功");
            } else {
                return FrontUtils.failurePage(token, "您无权限操作，请联系管理员扫码");
            }
        } else {
            String redirect_url = FrontUtils.addCompany(token, customerKey);
            logger.debug("跳转到添加企业页面上去: {}", redirect_url);
            return redirect_url;
        }
    }

    private String buildTwiceAuthUrl(HttpServletRequest request, String state) throws Exception {
        String callback = RequestUitl.buildUrl(request, WxConstant.PARMA_KEY_CODE, WxConstant.PARMA_KEY_STATE);
        logger.debug("网页授权回调 -> 二次授权: state {} url {}", state, callback);
        return apiService.authorizeUrl(callback, state, SnsapiScope.USERINFO);
    }
    private String sacnregisterpage(HttpServletRequest request, String uuid) throws Exception {
        String callback = RequestUitl.buildUrl(request);
        String redirect_url = FrontUtils.sacnregisterpage(uuid, callback);
        logger.debug("微信授权回调 - 用户未注册，重定向注册 redirect_url:{}, callback:{}", redirect_url, callback);
        return redirect_url;
    }
    private String bindPhone(HttpServletRequest request, String token, String state) throws Exception {
        String callback = RequestUitl.buildUrl(request);
        String redirect_url = FrontUtils.bingPhone(token, callback);
        logger.debug("微信授权回调 - 用户未绑定手机号，重定向绑定手机号 redirect_url:{}, callback:{}", redirect_url, callback);
        return redirect_url;
    }

    private String shareWaybill(HttpServletRequest request, User user, String token, String state) throws Exception {
        String shareId = request.getParameter("shareId");
        String waybillId = request.getParameter("waybillId");
        String redirect_url = FrontUtils.taskDetail(token, shareId, waybillId);
        logger.debug("微信授权回调 -> 跳转分享链接 : {}", redirect_url);
        return redirect_url;
    }

    private String index(HttpServletRequest request, User user, String token, String state) throws Exception {
        String redirect_url = FrontUtils.index(token);
        logger.debug("微信授权回调 -> 重定向首页 : {}", redirect_url);
        return redirect_url;
    }

    private String center(HttpServletRequest request, User user, String token, String state) throws Exception {
        String redirect_url = FrontUtils.center(token);
        logger.debug("微信授权回调 -> 重定向个人中心 : {}", redirect_url);
        return redirect_url;
    }

    private String addGroup(HttpServletRequest request, User user, String token, String state) throws Exception {
        String groupId = request.getParameter("groupId");
        if (StringUtils.isNotBlank(groupId)) {
            ProjectGroupMember projectGroupMember = new ProjectGroupMember();

            projectGroupMember.setGroupid(Integer.parseInt(groupId));
            projectGroupMember.setUserid(user.getId());

            groupService.saveMember(projectGroupMember);
        }
        String redirect_url = FrontUtils.projectTeam(token);
        logger.debug("微信授权回调 -> 分享二维码添加组,重定向个人中心 : {}", redirect_url);
        return redirect_url;
    }

    private String dispatch(HttpServletRequest request, User user, String token, String state) throws Exception {
        String redirect_url = FrontUtils.mapaddress(token);
        logger.debug("微信授权回调 -> 重定向派送地址 : {}", redirect_url);
        return redirect_url;
    }

    private String shareAddress(HttpServletRequest request, User user, String token, String state) throws Exception {
        String keys = request.getParameter("keys");
        if (StringUtils.isNotBlank(keys)) {
            Collection<Integer> collection = new ArrayList<Integer>();
            StringTokenizer tokenizer = new StringTokenizer(keys, ",");
            while (tokenizer.hasMoreTokens()) {
                try {
                    collection.add(Integer.parseInt(tokenizer.nextToken()));
                } catch (Exception e) {
                }
            }
            mapAddressService.copyAddress(user.getId(), collection);
        }
        String redirect_url = FrontUtils.mapaddress(token);
        logger.debug("微信授权回调 -> 重定向派送地址 : {}", redirect_url);
        return redirect_url;
    }

    private String scanCode(HttpServletRequest request, User user, String token, String state) throws Exception {
        String barcode = request.getParameter("barcode");
        logger.debug("微信授权回调 -> 扫码条码 : {} barcode:{} state:{} token:{}", user, barcode, state, token);
        if (StringUtils.isBlank(barcode)) {
            throw new ParameterException(barcode, "无效的条码号");
        }
        //根据条码状态跳转到不同的页面,跳转前端页面时务必带上 Token 和其他的参数 比如条码号
        BarcodeContext context = barCodeService.validate(user.getId(), barcode);
        if (context instanceof GroupCodeContext) {//项目组版本的二维码
            return versionGroup((GroupCodeContext) context, token);
        } else {//企业版本的二维码
            return versionEnterprise((CompanyCodeContext) context, token);
        }
    }


    private String versionGroup(GroupCodeContext context, String token) {
        logger.info("项目组版本二维码 -> {}", context);
        String redirect_url = null;
        if (context.isAllowBind()) {//有绑定权限的
            if (context.getStatus().unbind()) {//有权限并且未绑定跳转绑定页面
                if (context.getWaitBindCount() != null && context.getWaitBindCount() > 0) {
                    redirect_url = FrontUtils.bindBarcodes(token, context.getBarcode(), context.getGroupId());
                } else {
                    redirect_url = FrontUtils.bindBarcode(token, context.getBarcode(), context.getGroupId());
                }
                logger.debug("扫码未绑定，有绑定权限 -> 重定向到条码绑定页: {}", redirect_url);
            } else if (context.getStatus().bind() || context.getStatus().ing()) {//已绑定跳转定位页面
                redirect_url = FrontUtils.positioninfo(token, context.getBarcode());
                logger.debug("微信授权回调 -> 扫码重定向到定位页: {}", redirect_url);
            } else {
                redirect_url = FrontUtils.taskDetail(token, null, context.getWaybillId());
                logger.debug("微信授权回调 -> 扫码重定向到任务详情页111: {}", redirect_url);
            }
        } else {
            if (context.getStatus().unbind()) {//没有权限并且未绑定跳转绑定页面
                if (context.getLoadKey() != null && !context.isNeedBingImage()) {//已经装车
                    //临时位置上报页面
                    redirect_url = FrontUtils.transitionPosition(token, context.getBarcode());
                    logger.debug("扫码未绑定，没有绑定权限,已装车 -> 重定向到临时位置上报页面: {}", redirect_url);
                } else {
                    //重定向到上传绑码图片页面
                    redirect_url = FrontUtils.bindimage(token, context.getBarcode());
                    logger.debug("扫码未绑定，没有绑定权限,未装车 -> 重定向到上传绑码图片页面: {}", redirect_url);
                }
            } else {
                if (context.getStatus().bind() || context.getStatus().ing()) {
                    redirect_url = FrontUtils.positioninfo(token, context.getBarcode());
                    logger.debug("微信授权回调 -> 扫码重定向到定位页: {}", redirect_url);
                } else {
                    if (context.getSourceType() != null && (context.getSourceType().scan() ||
                            context.getSourceType().share() || context.getSourceType().assign())) {
                        redirect_url = FrontUtils.taskDetail(token, null, context.getWaybillId());
                        logger.debug("微信授权回调 -> 扫码重定向到任务详情页222: {}", redirect_url);
                    } else {
                        redirect_url = FrontUtils.index(token);
                        logger.debug("微信授权回调 -> 没有权限到首页: {}", redirect_url);
                    }
                }
            }
        }
        return redirect_url;
    }

    private String versionEnterprise(CompanyCodeContext context, String token) {
        logger.info("企业版本二维码 -> {}", context);
        String redirect_url = null;
        OrderRoleType roleType = context.getRoleType();
        if (context.fettle().unbind()) {//没有绑定
            if (context.isAllowBind()) {//有绑定权限的
                redirect_url = FrontUtils.enterpriseBindCode(token, context.getBarcode());//进入绑定选择页面
            } else {//没有绑定权限
                throw new BusinessException("没有绑定权限");
            }
        } else {//已经绑定
            if (roleType != null) {//是订单的参与方
                if (roleType.isShipper() && context.isAllowBind()) {//发货方 并且 有绑定权限的
                    redirect_url = FrontUtils.enterpriseBindtips(token, context.getBarcode());//绑码提示
                } else {//收货方和承运方
                    if (context.isComplete() || roleType.isReceive()) {//订单已完成或者是收货发
                        redirect_url = FrontUtils.enterpriseBindCodeDetail(token, context.getOrderKey());//进入订单详情页
                    }else{
                        redirect_url = FrontUtils.enterpriseLocation(token, context.getBarcode());//进入定位页面
                    }
                }
            } else {//不是订单的参与方
                if (context.getFettleType().isComplete()) {//订单已到货
                    throw new BusinessException("订单已签收");
                } else {
                    redirect_url = FrontUtils.enterpriseLocation(token, context.getBarcode());//进入定位页面
                }
            }
        }
        return redirect_url;
    }


    private UserInfo convertUserByWeChatInfo(UserContext context, AccessToken accessToken) throws Exception {
        UserInfo weChatInfo = apiService.swapInfoByAccessToken(accessToken);
        logger.info("微信授权回调 -> 获取用户信息 : {}", weChatInfo);
        context.setUnionid(accessToken.getUnionId());
        context.setOpenid(weChatInfo.getOpenid());//用户的openid
        context.setUname(weChatInfo.getNickname());
        context.setSubscribe(weChatInfo.getSubscribe());//用户关注公众号的状态
        context.setHeadImg(weChatInfo.getHeadimgurl());
        return weChatInfo;
    }

    private boolean bindphone(User user, String state) {
        if (StringUtils.isEmpty(user.getMobilephone())) {
			/*
			if(WxConstant.STATE_DISPATCH.equals(state) || WxConstant.STATE_ADDRESS.equals(state)) {
				return false;
			}
			*/
            return true;
        }
        return false;
    }

    /**
     * TODO 扫描二维码添加好友（扫描过程）
     * <p>
     *
     * @param friendId
     * @param request
     * @param response
     * @throws Exception
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-06 11:46:49
     */
    @RequestMapping(value = "/scan/add/friend/{friendId}")
    public void addFriend(@PathVariable Integer friendId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("扫码加好友 -> friendId:{}", friendId);
        //String fId = SecurityTokenUtil.getUserIdByToken(authToken);//通过用户token解析userId
        String callback_uri = SystemUtils.getCallBackDomain() + "/mobile/wechat/auth?friendId=" + friendId;
        String redirect_uri = apiService.authorizeUrl(callback_uri, WxConstant.STATE_FRIEND, SnsapiScope.BASE);
        logger.debug("扫码加好友 -> callback_uri:{} redirect_uri:{}", callback_uri, redirect_uri);
        response.sendRedirect(redirect_uri);
    }

    /**
     * TODO 扫描成功后跳转到备注好友姓名页面
     * <p>
     *
     * @param request
     * @param token
     * @return
     * @throws Exception
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-06 11:46:38
     */
    private String addFriend(HttpServletRequest request, String token) throws Exception {
        String friendId = request.getParameter("friendId");
        String redirect_url = FrontUtils.addFriends(token, friendId);
        return redirect_url;
    }
}
