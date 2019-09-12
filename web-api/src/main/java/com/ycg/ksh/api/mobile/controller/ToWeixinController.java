package com.ycg.ksh.api.mobile.controller;


import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.persistent.WaybillTrack;
import com.ycg.ksh.service.api.WaybillTrackService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Controller("mobile.toweixin.controller")
@RequestMapping("/mobile")
public class ToWeixinController extends BaseController{
	
	@Resource
	WaybillTrackService trackService;

	/**
	 * 扫描上传位置
	 */
	@RequestMapping(value="/uploadPosition", method=RequestMethod.POST)
	public @ResponseBody JsonResult uploadPosition(@RequestBody RequestObject object, HttpServletRequest request)throws Exception{
		logger.debug("uploadPosition -> {}", object);
		JsonResult jsonResult = new JsonResult();
		Assert.notNull(object, Constant.PARAMS_ERROR);
		User user = RequestUitl.getUserInfo(request);
		WaybillTrack track = object.toJavaBean(WaybillTrack.class);
		StringBuilder subStr = new StringBuilder();
        track.setDescribe(subStr.append(object.get("city")).append("【扫描定位】").toString());
		trackService.saveLoactionReport(user.getId(), track);
		return jsonResult;
	}
}
