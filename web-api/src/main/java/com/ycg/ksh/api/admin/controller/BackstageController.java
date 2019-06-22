package com.ycg.ksh.api.admin.controller;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/11
 */

import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.entity.persistent.Company;
import com.ycg.ksh.service.api.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 后台控制层
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/11
 */

@Controller("admin.backstage.controller")
@RequestMapping("/special/backstage")
public class BackstageController extends BaseController {

    @Resource
    private CompanyService companyService;

    @RequestMapping(value="/company/save")
    @ResponseBody
    public JsonResult saveCompany(@RequestBody RequestObject object, HttpServletRequest request)throws Exception{
        logger.info("backstage save company---> {}", object);
        Assert.notEmpty(object, Constant.PARAMS_ERROR);
        Company company = object.toJavaBean(Company.class);
        companyService.saveByBackstage(company);
        return JsonResult.SUCCESS;
    }

}
