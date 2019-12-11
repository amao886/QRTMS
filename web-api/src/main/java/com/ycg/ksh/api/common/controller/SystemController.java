package com.ycg.ksh.api.common.controller;

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.common.constant.Directory;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.core.driver.application.DriverApplicationService;
import com.ycg.ksh.core.driver.application.dto.DriverDto;
import com.ycg.ksh.entity.adapter.wechat.UserInfo;
import com.ycg.ksh.entity.common.constant.HotspotType;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.http.HttpClient;
import com.ycg.ksh.common.system.FrontUtils;
import com.ycg.ksh.common.system.SecurityTokenUtil;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.*;
import com.ycg.ksh.common.util.encrypt.RSA;
import com.ycg.ksh.common.util.encrypt.SignUtil;
import com.ycg.ksh.common.validate.Validator;
import com.alibaba.fastjson.JSONObject;
import com.ycg.ksh.adapter.api.MessageQueueService;
import com.ycg.ksh.adapter.api.SmsService;
import com.ycg.ksh.constant.CoreConstants;
import com.ycg.ksh.constant.MenuType;
import com.ycg.ksh.entity.common.wechat.message.Message;
import com.ycg.ksh.entity.common.wechat.message.MessageBuilder;
import com.ycg.ksh.entity.common.wechat.message.common.TextMessage;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.service.AuthorityMenu;
import com.ycg.ksh.entity.service.AuthorizeUser;
import com.ycg.ksh.entity.service.user.UserContext;
import com.ycg.ksh.service.api.AccessoryService;
import com.ycg.ksh.service.api.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller
public class SystemController extends BaseController {

	@Resource
	AccessoryService accessoryService;
	@Resource
	SmsService smsService;

	@Resource
	MessageQueueService queueService;
	@Resource
	PermissionService permissionService;
	@Resource
	DriverApplicationService driverService;


	@RequestMapping(value = "/backstage/modify/hotspot")
	@ResponseBody
	public JsonResult modifyHotspot(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
		User u = RequestUitl.getUserInfo(request);
		Integer type = body.getInteger("type");
		String keys = body.get("key");
		Assert.notBlank(keys, "热点数据编号不能为空");
		Set<Serializable> collection = new HashSet<Serializable>();
		StringTokenizer tokenizer = new StringTokenizer(keys, ",");
		while (tokenizer.hasMoreTokens()){
			collection.add(tokenizer.nextToken());
		}
		accessoryService.modifyHotspot(u.getId(), HotspotType.convert(type), collection);
		return JsonResult.SUCCESS;
	}
	
	public MenuType getMenuType(AuthorizeUser u) {
		if(u.getSysRole().getId() == 5) {
			return MenuType.BACK;
		}else if(u.getSysRole().getId() == 3) {
			return MenuType.BACKSTAGE;
		}
		return MenuType.GROUP;
	}
	
	@RequestMapping(value={"/group"})
	public String group(HttpServletRequest request, Model model)throws Exception{
		AuthorizeUser u = loadUser(request);
		Collection<AuthorityMenu> menuList = permissionService.loadAuthoritys(u, getMenuType(u));
		model.addAttribute("menus", menuList);
		return "/group";
	}
	@RequestMapping(value={"/backstage"})
	public String backstage(HttpServletRequest request, Model model)throws Exception{
		AuthorizeUser u = loadUser(request);
		model.addAttribute("menus", permissionService.loadAuthoritys(u, getMenuType(u)));
		return "/backstage";
	}

	@RequestMapping(value={"/", "/index"})
	public String index(HttpServletRequest request, Model model)throws Exception{
		AuthorizeUser u = loadUser(request);
		//model.addAttribute("menus", permissionService.loadAuthoritys(loadUserKey(request), MenuType.NORMAL));
		Collection<AuthorityMenu> menuList = permissionService.loadAuthoritys(u, getMenuType(u));
		model.addAttribute("menus", menuList);
		return "/group";
	}

	@RequestMapping(value="/special/logout")
    @ResponseBody
	public JsonResult logout(HttpServletRequest request, Model model)throws Exception{
		User user = RequestUitl.getUserInfo(request);
	    logger.debug("logout -> {}", user);
		cacheManger.delete(RequestUitl.getSessionId(request));
		RequestUitl.cleanSession(request);
		if(user != null){
            userService.logout(user.getId());
        }
		return JsonResult.SUCCESS;
	}

	@RequestMapping(value="/special/image/code")
	public void imageCode(HttpServletRequest request, HttpServletResponse response)throws Exception{
		RequestObject object = new RequestObject(request.getParameterMap());
		Integer width = object.getInteger("w"),
				height = object.getInteger("h"),
				count = object.getInteger("c");
		logger.debug("image code -> {}", object);

		width = Math.min(145, Optional.ofNullable(width).orElse(145));
		height = Math.min(32, Optional.ofNullable(height).orElse(32));
		count = Math.min(10, Optional.ofNullable(count).orElse(4));

		String codeString = RandomUtils.string(count);
		logger.debug("image code -> code:{}", codeString);
		BufferedImage bufferedImage = RandomUtils.createCode(codeString, width, height);
		if(bufferedImage != null){
			try{
				ImageIO.write(bufferedImage, "png", response.getOutputStream());
			}catch (Exception e){
				logger.error("图片验证码,数据写入异常");
			}
		}
		RequestUitl.session(request, IMAGE_CODE_PREFIX, codeString);
	}

	@RequestMapping(value="/special/sms/code")
	@ResponseBody
	public JsonResult smsCode(@RequestBody RequestObject object, HttpServletRequest request, HttpServletResponse response)throws Exception{
		logger.info("smsCode-> {} ", object);
		String phone = object.get("phone"), code = object.get("code");
		if (StringUtils.isBlank(phone)) {
			throw new ParameterException(phone, "手机号不能为空");
		}
		if (StringUtils.isBlank(code)) {
			throw new ParameterException(code, "验证码不能为空");
		}
		Object cache_code = RequestUitl.session(request, IMAGE_CODE_PREFIX);
		if(cache_code == null){
			throw new ParameterException("验证码已失效");
		}
		if(!code.equalsIgnoreCase(cache_code.toString())){
			throw new ParameterException("验证码输入错误");
		}
		smsService.sendSmsCode(phone);
		return new JsonResult(true, "验证码发送成功");
	}
	
	@RequestMapping(value = "/special/smsCode/{phone}")
    @ResponseBody
    public JsonResult smsCode(@PathVariable String phone, HttpServletRequest request) throws Exception {
        logger.info("-----------smsCode------------phone: {} ", phone);
        if (StringUtils.isBlank(phone)) {
        	throw new ParameterException(phone, "手机号不能为空");
        }
		smsService.sendSmsCode(phone);
        return new JsonResult(true, "验证码发送成功");
    }


	@RequestMapping(value = "/special/sms/login/code/{phone}")
	@ResponseBody
	public JsonResult smsLoginCode(@PathVariable String phone, HttpServletRequest request) throws Exception {
		logger.info("-----------sms login code------------phone: {} ", phone);
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isBlank(phone)) {
			jsonResult.modify(false, "手机号不能为空");
		}else{
			User user = userService.getUserByMobile(phone);
			if(user == null){
				jsonResult.modify(false, "该手机号没有注册合同物流管理平台平台");
			}else{
				smsService.sendCode(phone, "您的验证码是%s,您正在使用短信登陆合同物流管理平台平台,请尽快提交验证码完成登陆,10分钟内有效.");
				jsonResult.modify(true, "验证码发送成功");
			}
		}
		return jsonResult;
	}

	@RequestMapping(value = "/special/sms/login/{phone}/{code}")
	@ResponseBody
	public JsonResult smsLogin(@PathVariable String phone, @PathVariable String code, HttpServletRequest request) throws Exception {
		logger.info("-----------smsCode------------phone: {} ", phone);
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isBlank(phone)) {
			jsonResult.modify(false, "手机号不能为空");
		}else if (StringUtils.isBlank(code)) {
			jsonResult.modify(false, "登陆验证码不能为空");
		}else{
			validateSmsCode(phone, code);
			User user = userService.getUserByMobile(phone);
			if(user == null){
				jsonResult.modify(false, "该手机号没有注册合同物流管理平台平台");
			}else{
				UserContext context = new UserContext(user.getId(), CoreConstants.LOGIN_TYPE_SMS, CoreConstants.LOGIN_CLIENT_SMS, SystemUtils.env());
				context.setClientHost(RequestUitl.getRemoteHost(request));
				AuthorizeUser authorizeUser = userService.login(context);
				jsonResult.put("id", authorizeUser.getId());
				jsonResult.put("username", authorizeUser.getUnamezn());
				jsonResult.put("mobilephone", authorizeUser.getMobilephone());
				jsonResult.put("headImg", authorizeUser.getHeadImg());
				if(authorizeUser.getEmployee() != null){
					jsonResult.put("companyKey", authorizeUser.getEmployee().getCompanyId());
					jsonResult.put("companyName", authorizeUser.getCompanyName());
				}
				jsonResult.put("token", authorizeUser.getToken());
                jsonResult.put("url", FrontUtils.pdaIndex(authorizeUser.getToken()));
				RequestUitl.modifyUserInfo(request, authorizeUser);
				jsonResult.modify(true, "登陆成功");
			}
			clearSmsCode(phone);
		}
		return jsonResult;
	}

	/**
	 * 绑定手机号
	 * <p>
	 *
	 * @param object
	 * @param request
	 * @return
	 * @throws Exception
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-04 09:49:50
	 */
	@RequestMapping(value = "/bind/phone")
	@ResponseBody
	public JsonResult bindPhone(@RequestBody RequestObject object, HttpServletRequest request,HttpServletResponse response) throws Exception {
		// 用户注册和绑定手机号特殊处理
		logger.debug("===========bindphone===========start");
		String phone = object.get("phone"), name = object.get("name"), code = object.get("code");
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

		AuthorizeUser user = userService.bindMobile(loadUserKey(request), phone, name, object.getInteger("identity"));
		if (user != null) {
			RequestUitl.modifyUserInfo(request, user);
		}
		clearSmsCode(phone);
		return new JsonResult(true, "手机号绑定成功");
	}

	@RequestMapping(value = "/special/register")
	@ResponseBody
	public JsonResult register(@RequestBody RequestObject object,HttpServletRequest request) throws Exception {
		String name = object.get("name"), phone = object.get("phone"), code = object.get("code");
		JsonResult jsonResult = new JsonResult(true, "注册成功");
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

		UserContext context = new UserContext(CoreConstants.LOGIN_TYPE_REG, CoreConstants.LOGIN_CLIENT_WX, SystemUtils.env());
		context.setUname(name);
		context.setMobilephone(phone);
		AuthorizeUser user = userService.register(context);
		if (user == null) {
			throw new BusinessException("注册异常");
		}
		String callback= FrontUtils.inviteaskpage(object.getLong("inviteKey"), user.getToken());
		jsonResult.put("token", user.getToken());
		jsonResult.put("callback", callback);

		logger.info("callback --> {}", callback);

		RequestUitl.modifyUserInfo(request, user);
		clearSmsCode(phone);
		return jsonResult;
	}

	@RequestMapping(value = "/special/redirect")
	public void redirect(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String token = request.getParameter("token"), inviteKey = request.getParameter("inviteKey");
		if(StringUtils.isNotBlank(token)){
			response.sendRedirect(FrontUtils.inviteaskpage(Long.parseLong(inviteKey), token));
		}else{
			response.sendRedirect(FrontUtils.registerpage(Long.parseLong(inviteKey)));
		}
	}


	/**
	 * 运维验证
	 */
	@RequestMapping(value="/special/validate")
	@ResponseBody
	public String validate(HttpServletRequest request,HttpServletResponse response)throws Exception{
		logger.debug("======validate========");
		return "success";
	}

	/**
	 * 生成token
	 */
	@RequestMapping(value="/special/token/{uid}")
	@ResponseBody
	public JsonResult token(@PathVariable Integer uid, HttpServletRequest request,HttpServletResponse response)throws Exception{
		JsonResult result = new JsonResult();
		result.put("uid", uid);
		result.put("token", SecurityTokenUtil.createToken(uid +""));
		return result;
	}

	@RequestMapping(value="/special/test")
	@ResponseBody
	public String test(HttpServletRequest request,HttpServletResponse response)throws Exception{
		logger.debug("======test========");

//		MediaMessage message = new MediaMessage("--------ID------------");
//		message.setMessageType("--------string------------");
//		//message.setObject(Globallys.toJsonString(object));
//		message.setObject("--------hello world------------"+ System.currentTimeMillis());
//
//		queueService.sendMessage(QueueKeys.ROUTE_OUT_KYLIN, message);



		//orderKey=FHTZD1806120028;qlm-access-key=373163470661632;qlm-nonce=VBLOQ;qlm-timestamp=1538118371
		//请求方标识
		String api_access_key = "373163470661632";
		//对应的私钥
		String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAODOI4k1dCw+MYIAvTT1NMlhoYJiz1RlZ2y4DwlAsDtQ/GILOVQix/+clm2cDpeQyTfyT7x2e7Rg9TpeGXgqelxMhL4Fh+de9JkK1QSKz0F0qLVS2HeaCOJdJKekwyM989vB2VPrbdtOkVNSOe6eHrtC25B7D/eIteSQlI7tDum9AgMBAAECgYAKxxdBDsUBEgJBn1Ny5IahWQLrQ115SFtLBRADe4x3a4yODsey1vALuzAjFSZF8fUTr5RPDscjgLqBD93cuvyetTJuvetdFpCoBJH2fSWea9hdKieNxrSY4QsVX8QzoCRCQAWd3sSti6tadE4lq7yUduQPfYO9jSbNe4htwvRBKQJBAPUAMSS2Y/6gUS1QPNd75mrtXHRpi8HgWuP2Zp+YEj5FqwJPjjLpX5H+JVXrcAG0GKIr9I9hjSYPWRjHPgH+aUsCQQDq5daq30g63ugclVCna9V1tL8pVVfchxN60DIfRn38l/8v/KnTCMaqvV/gjjYbLIiZJbglbfZKLb6bqLWCxdwXAkApnYq7ba+2hIzFYae0Anu1FfRqYbM3j2Bg8G2mFjKjGTpe/hxtUW7GYaD94yv3XhwBD+5OnIdZO6oqp01FEW3ZAkA0inJoyAa8/E0Iz7E5sZoOeP5lPWyorCIET3tWmYrmrAzta+OEi6r8V92ROd4KV/y53MECxkTXtMG0yZjIBn/DAkB8bO85pvE/ffjdk33/lOjojRDyPg0SFwC7Gd//cj/C41rXIjIgrNqXSWoaCr4ja/wE0UzCYcepl7dv0XGyZqfi";

		Long api_timestamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
		String api_nonce = RandomUtils.string(5);

		//设置校验参数
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("qlm-access-key", api_access_key);
		parameters.put("qlm-timestamp", api_timestamp);
		parameters.put("qlm-nonce", api_nonce);

		//parameters.put("qlm-timestamp", 1538185710);
		//parameters.put("qlm-nonce", "JLHSB");

		//String url = "http://localhost:8080/adventive/order/transport";
		String url = "http://kshdev.ycgwl.com/adventive/order/keys";
		//接口测试
		HttpClient client = new HttpClient(url, HttpClient.Type.POST);

		//parameters.put("orderString", "[{\"receiveName\":\"收货客户\",\"orderNo\":\"3435eqweqw\",\"receiveKey\":360544089072640,\"deliveryTime\":1537200000000,\"receiverContact\":\"联系方式\",\"receiveAddress\":\"河北省保定市涞水县收货地址\",\"arrivalTime\":1538064000000,\"receiverName\":\"收货人\",\"shipperName\":\"发货方\",\"remark\":\"123213123\",\"shipperKey\":319135638070272,\"deliveryNo\":\"1212121212121212\",\"conveyName\":\"213\",\"conveyKey\":353096301536256,\"income\":345,\"expenditure\":123,\"conveyerContact\":\"联系电话\",\"originStation\":\"陕西省榆林市靖边县\",\"endStation\":\"到站\",\"conveyerName\":\"联系人\",\"distributeAddress\":\"辽宁省丹东市东港市发货地址\",\"startStation\":\"发站\",\"driverName\":\"司机姓名\",\"arrivalStation\":\"山西省忻州市宁武县\",\"careNo\":\"沪A88905\",\"driverContact\":\"司机电话\",\"commodities\":[{\"volume\":19,\"quantity\":18,\"boxCount\":22,\"weight\":10.25,\"commodityUnit\":\"单位\",\"remark\":\"物料备注\",\"commodityNo\":\"物料编号\",\"commodityType\":null,\"commodityName\":\"产品描述\"}],\"others\":{\"自定义字段2\":\"自定义字段222222222\",\"自定义字段1\":\"自定义字段121111111\"}}]");
		parameters.put("orderKey", "FHTZD1806120028");
		String signContext = SignUtil.createLinkString(parameters);

		System.out.println("签名源字符串:"+ signContext);
		//生成签名
		String signVal = RSA.sign(signContext, privateKey);
		parameters.put("qlm-sign-val", signVal);

		//parameters.put("orderKey", "242763490009081");

		System.out.println(parameters);

		client.parameter(parameters);

		//请求并打印结果
		System.out.println(client.request());

		return "success";
	}
	
	@RequestMapping(value="/upload/image")
	@ResponseBody
	public JsonResult uploadImage(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception{
		JsonResult jsonResult = new JsonResult();
		if(request instanceof DefaultMultipartHttpServletRequest) {
			DefaultMultipartHttpServletRequest multipartHttpServletRequest = (DefaultMultipartHttpServletRequest) request;
			for (Map.Entry<String,MultipartFile> entry : multipartHttpServletRequest.getFileMap().entrySet()) {
				MultipartFile file = entry.getValue();
				String path = null;// 文件路径
	            String fileName = file.getOriginalFilename(); //文件原名称
	            logger.info("upload image {}", fileName);
	            //判断文件类型
	            String type = FileUtils.suffix(fileName);
	            if (StringUtils.isNotBlank(type)) {// 判断文件类型是否为空
	            	// 项目在容器中实际发布运行的根路径
	                String realPath = SystemUtils.fileRootPath(Directory.UPLOAD.getDir());
	                // 自定义的文件名称
	                String trueFileName = StringUtils.UUID() +"."+ type.toLowerCase();
	                // 设置存放图片文件的路径
	                path = FileUtils.path(realPath, trueFileName);
	                logger.info("upload image to {}", path);
	                // 转存文件到指定的路径
	                file.transferTo(new File(path));
	            }else {
	            	jsonResult.modify(false, "文件类型有误,请重新选择上传!");
	            }
			}
		}
		return jsonResult;
	}

	@RequestMapping(value = "/special/download/{tag}")
	public void downloadByTag(@PathVariable Integer tag,  HttpServletRequest request, HttpServletResponse response) throws Exception{
		if(101 - tag == 0){
			down(SystemUtils.fileRootPath(Directory.TEMPLATE.sub("contract_valuation_template.xlsx")), "合同计价模板", false, request, response);
		}
		if(102 - tag == 0){
			down(SystemUtils.fileRootPath(Directory.TEMPLATE.sub("contract_commodity_template.xlsx")), "合同物料模板", false, request, response);
		}
	}

	public void down(String filePath, String fileName, boolean delete, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String suffix = FileUtils.suffix(filePath);
		if(StringUtils.isNotBlank(fileName)) {
			fileName = FileUtils.appendSuffix(FileUtils.name(fileName), suffix);
		}
		logger.info("download -> file:{} name:{}", filePath, fileName);
		File file = new File(filePath);
		if(!file.exists() || file.isDirectory()) {
			return;
		}
		//判断文件类型
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		if(mimeType == null) {
			mimeType = "application/octet-stream;charset=UTF-8";
		}
		response.setContentType(mimeType);
		//设置文件响应大小
		//response.setContentLengthLong(file.length());
		if (StringUtils.isBlank(fileName)) {
			fileName = FileUtils.appendSuffix("F_" + System.nanoTime(), suffix);
		} else {
			try {
				String userAgentString = RequestUitl.userAgentString(request);
				// 针对IE或者以IE为内核的浏览器：
				if (userAgentString.matches("^.*[MSIE|Trident]+.*$")) {
					fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
				} else {
					//非IE浏览器的处理：
					fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
				}
			} catch (UnsupportedEncodingException e) { }
		}
		//设置Content-Disposition响应头，一方面可以指定下载的文件名，另一方面可以引导浏览器弹出文件下载窗口
		response.setHeader("Content-Disposition", "attachment;fileName=\"" + fileName + "\"");
		response.setCharacterEncoding("UTF-8");
		//文件下载
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new BufferedInputStream(new FileInputStream(file));
			os = response.getOutputStream();
			IOUtils.copy(is, os);
		} finally {
			IOUtils.closeQuietly(is, os);
			if(delete) {
				FileUtils.forceDelete(file);
			}
		}
	}

	@RequestMapping(value = "/backstage/special/download")
	public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] source = FileUtils.download(request.getParameter("p"));
		if(source != null) {
			String filePath = source[0], fileName = null;
			if(filePath.length() > 1) {
				fileName = source[1];
			}
			boolean delete = false;
			if(filePath.length() > 2) {
			    delete = new Boolean(source[2]);
			}
			down(filePath, fileName, delete, request, response);
		}
	}
}