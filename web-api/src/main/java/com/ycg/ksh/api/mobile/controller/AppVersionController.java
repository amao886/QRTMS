package com.ycg.ksh.api.mobile.controller;

import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.system.SystemKey;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.entity.persistent.AppVersion;
import com.ycg.ksh.service.api.AppVersionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller("mobile.version.controller")
@RequestMapping("/mobile/version")
public class AppVersionController extends BaseController {

    @Resource
    private AppVersionService appVersionService;

    /**
     * 查询最新版本信息
     * TODO Add description
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-04-20 11:52:55
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/check/version")
    @ResponseBody
    public JsonResult validate(HttpServletRequest request) throws Exception {
    	JsonResult jsonResult = new JsonResult();
    	AppVersion appVersion = appVersionService.queryLastEntity();
        jsonResult.put("results", appVersion);
        return jsonResult;
    }
    /**
     * 版本信息添加
     * TODO Add description
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-04-20 11:48:19
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/toAdd")
    public String toAddBarCode(HttpServletRequest request, Model model) {
        return "";
    }
    /**
     * 
     * TODO APP帮助接口
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-05-07 16:01:17
     * @param request
     * @return
     */
    @RequestMapping(value = "/toAppHelper")
    public void toAppHelper(HttpServletResponse response) throws Exception{
    	response.sendRedirect(SystemUtils.buildUrl(SystemUtils.get(SystemKey.WEIXIN_CALLBACK_DOMAIN),Constant.APPINDEXPAGE));
    }

}
