package com.ycg.ksh.api.common.controller;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/23
 */

import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.service.api.SupportService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 辅助支撑服务
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/23
 */
@Controller("common.support.controller")
@RequestMapping("/special/support")
public class SupportController extends BaseController {


    private static final int VERSION = 1;

    @Resource
    private SupportService supportService;

    @RequestMapping(value="/brank/data")
    @ResponseBody
    public JsonResult brankData(HttpServletRequest request)throws Exception{
        JsonResult result = new JsonResult();
        result.put("results", supportService.loadBrankData());
        return result;
    }

    @RequestMapping(value="/brank/branch")
    @ResponseBody
    public JsonResult branch(@RequestBody RequestObject object, HttpServletRequest request)throws Exception{
        JsonResult result = new JsonResult();
        String brankName = object.get("brank");

        Assert.notBlank(brankName, "银行名称不能为空");

        String province = object.get("province");
        String city = object.get("city");
        result.put("results", supportService.loadBranks(brankName, province, city));
        return result;
    }

    @RequestMapping(value="/area")
    @ResponseBody
    public JsonResult area(@RequestBody RequestObject object, HttpServletRequest request)throws Exception{
        JsonResult result = new JsonResult();
        result.put("result", supportService.listAreaByParentKey(Optional.ofNullable(object.getInteger("parentid")).orElse(0)));
        return result;
    }


    @RequestMapping(value="/area/all")
    @ResponseBody
    public JsonResult areaAll(@RequestBody RequestObject object, HttpServletRequest request)throws Exception{
        JsonResult result = new JsonResult();
        Integer ver = object.getInteger("version");
        if (ver == null || VERSION - ver != 0){
            result.put("result", supportService.listGradeArea(Optional.ofNullable(object.getInteger("parentid")).orElse(0)));
        }
        result.put("version", VERSION);
        return result;
    }
}
