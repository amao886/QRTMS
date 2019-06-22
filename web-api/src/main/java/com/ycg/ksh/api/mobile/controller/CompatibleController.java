package com.ycg.ksh.api.mobile.controller;

import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.system.SystemKey;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.entity.common.wechat.SnsapiScope;
import com.ycg.ksh.adapter.api.WeChatApiService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller("mobile.compatible.controller")
public class CompatibleController extends BaseController {
	
	private static final String STATE_SCAN = "SCAN";//扫码
    @Resource
    WeChatApiService apiService;
	
	@RequestMapping(value="/weixin/weixinAuth.html")
	public void scanCode(HttpServletRequest request, HttpServletResponse response)throws Exception{
		//type=5&code=20170828000022 
		String type = request.getParameter("type");
		String code = request.getParameter("code");
		
		logger.debug("===========scan code==================> type:{} code:{}", type, code);
		
		String callback_uri = SystemUtils.get(SystemKey.WEIXIN_CALLBACK_DOMAIN) + "/mobile/wechat/auth?barcode="+ code;
		
		String redirect_uri = apiService.authorizeUrl(callback_uri, STATE_SCAN, SnsapiScope.BASE);

		logger.debug("===========scan code==================> callback_uri:{} redirect_uri:{}", callback_uri, redirect_uri);
		response.sendRedirect(redirect_uri);
	}
	
	/**
	 * 运维验证
	 */
	@RequestMapping(value="/mobile/validate")
	@ResponseBody
	public String validate(HttpServletRequest request, HttpServletResponse response)throws Exception{
		logger.debug("======validate========");
		return "success";
	}
	
	/**
	 * 运维验证
	 */
	@RequestMapping(value="/mobile/example")
	public String example(HttpServletRequest request, HttpServletResponse response)throws Exception{
		logger.debug("======validate========");
		return "example";
	}
	
}
