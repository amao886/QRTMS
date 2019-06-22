package com.ycg.ksh.api.report.controller;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/2 0002
 */

import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.collect.api.ReportService;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.entity.collecter.report.ColumnValue;
import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.entity.service.AuthorizeUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 报表
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/2 0002
 */
@Controller("report.controller")
@RequestMapping("/report")
public class ReportController extends BaseController {

    @Resource
    ReportService reportService;

    private RequestObject requestObject(HttpServletRequest request){
        RequestObject object = new RequestObject(request.getParameterMap());
        AuthorizeUser authorizeUser = loadUser(request);
        PartnerType partner = PartnerType.identityKey(authorizeUser.getIdentityKey());
        object.put("partnerType", partner.getCode());
        object.put("companyKey", authorizeUser.getCompanyKey());
        return object;
    }


    @RequestMapping(value = "/init/{reportKey}")
    @ResponseBody
    public JsonResult initReport(@PathVariable Integer reportKey, HttpServletRequest request) throws Exception {
        RequestObject object = requestObject(request);
        logger.info("sum report -> {} {}", reportKey,  object);
        JsonResult jsonResult = new JsonResult();
        //初始化一个报表页面
        //jsonResult.put("results", reportService.reduceReport(reportKey, object)) ;
        return jsonResult;
    }

    @RequestMapping(value = "/sum/{reportKey}")
    @ResponseBody
    public JsonResult sumReport(@PathVariable Integer reportKey, HttpServletRequest request) throws Exception {
        RequestObject object = requestObject(request);
        logger.info("sum report -> {} {}", reportKey,  object);
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("results", reportService.reduceReport(reportKey, object)) ;
        return jsonResult;
    }

    @RequestMapping(value = "/list/{reportKey}")
    @ResponseBody
    public JsonResult listReport(@PathVariable Integer reportKey, HttpServletRequest request) throws Exception {
        RequestObject object = requestObject(request);
        logger.info("list report -> {} {}", reportKey,  object);
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("results", reportService.listReport(reportKey, object)) ;
        return jsonResult;
    }

    @RequestMapping(value = "/page/{reportKey}")
    @ResponseBody
    public JsonResult pageReport(@PathVariable Integer reportKey, HttpServletRequest request) throws Exception {
        RequestObject object = requestObject(request);
        logger.info("page report -> {} {}", reportKey,  object);
        JsonResult jsonResult = new JsonResult();

        int num = Optional.ofNullable(object.getInteger("num")).orElse(1);
        int size = Optional.ofNullable(object.getInteger("size")).orElse(1);

        jsonResult.put("page", reportService.pageReport(reportKey, num, size, object)) ;
        return jsonResult;
    }

    @RequestMapping(value = "/export/{reportKey}")
    @ResponseBody
    public JsonResult exportReport(@PathVariable Integer reportKey, HttpServletRequest request) throws Exception {
        RequestObject object = requestObject(request);
        logger.info("page report -> {} {}", reportKey,  object);
        JsonResult jsonResult = new JsonResult();
        FileEntity fileEntity = reportService.exportReport(reportKey,  object);
        logger.info("fileEntity -> {}", fileEntity);
        if (fileEntity != null) {
            jsonResult.put("url", FileUtils.buildDownload(fileEntity.getPath(), FileUtils.appendSuffix(fileEntity.getAliasName(), fileEntity.getSuffix()), true));
            jsonResult.put("count", fileEntity.getCount());
            jsonResult.put("size", fileEntity.getSize());
        } else {
            jsonResult.modify(false, "报表文件生成异常");
        }
        return jsonResult;
    }
}
