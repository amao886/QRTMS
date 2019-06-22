package com.ycg.ksh.api.backstage.controller;

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.entity.common.constant.WaybillFettle;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.ProjectGroup;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.service.ConveyanceSearch;
import com.ycg.ksh.entity.service.MergeRoute;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.Station;
import com.ycg.ksh.service.api.ConveyanceService;
import com.ycg.ksh.service.api.ProjectGroupService;
import com.ycg.ksh.service.api.RouteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 运单控制层
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/24
 */
@Controller("backstage.conveyance.controller")
@RequestMapping("/backstage/conveyance")
public class ConveyanceController extends BaseController {

    @Resource
    ConveyanceService conveyanceService;
    @Resource
    RouteService routeService;
    @Resource
    ProjectGroupService projectGroupService;


    @RequestMapping(value = "/search")
    public String search(Model model, HttpServletRequest request) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        User u = RequestUitl.getUserInfo(request);
        List<ProjectGroup> projectGroups = projectGroupService.listByUserKey(u.getId());
        Integer groupId = body.getInteger("groupKey");
        if (null != groupId) {
            RequestUitl.modifyGroupKey(request, groupId);
        }else{
            body.put("groupKey", RequestUitl.getGroupKey(request, 0));
        }
        ConveyanceSearch search = body.toJavaBean(ConveyanceSearch.class);
        search.setCreateKey(u.getId());
        Integer timeType = body.getInteger("timeType");
        if(timeType == null){
            timeType = 0;
        }
        if(timeType > 0){
            search.setTime(new Date(), 0 - timeType);
        }
        if(StringUtils.isBlank(body.get("fettle"))){
            search.setFettle(null);
        }
        if(StringUtils.isBlank(body.get("assignFettle"))){
            search.setAssignFettle(null);
        }
        body.put("timeType", timeType);
        if(search.getFettle() == null || search.getFettle() != 0){
            search.setCancel(false);
        }

        logger.info("运单查询 -> {}", body);
        PageScope pageScope = new PageScope(body.getInteger("num"), body.getInteger("size"));
        model.addAttribute("page", conveyanceService.listWaybillConveyance(search, pageScope));
        model.addAttribute("routes", routeService.listActiveRoutes(u.getId(), 5));
        model.addAttribute("groups", projectGroups);
        model.addAttribute("search", body);
        return "/backstage/conveyance/manage";
    }

    @RequestMapping(value = "/edit/fettle")
    @ResponseBody
    public JsonResult editFettle(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        Long conveyancekey = body.getLong("conveyanceKey");
        Assert.notBlank(conveyancekey, "运单编号不能为空");
        User u = RequestUitl.getUserInfo(request);
        WaybillFettle fettle = conveyanceService.changeFettle(u.getId(), conveyancekey);
        if(fettle != null){
            jsonResult.put("fettle", fettle.getCode());
        }
        return jsonResult;
    }

    @RequestMapping(value = "/assign/pre")
    @ResponseBody
    public JsonResult assignPre(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        User u = RequestUitl.getUserInfo(request);
        Integer routeKey = body.getInteger("routeKey");
        Collection<Long> conveyanceKeys = StringUtils.longCollection(body.get("conveyanceKeys"));
        Assert.notEmpty(conveyanceKeys, "至少选择一个运单");
        Station station = conveyanceService.validate(conveyanceKeys);
        MergeRoute mergeRoute = routeService.getByKey(u.getId(), routeKey);
        if(mergeRoute == null){
            throw new BusinessException("所选路由不存在");
        }
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("station", station);
        jsonResult.put("line", mergeRoute);
        return jsonResult;
    }

    @RequestMapping(value = "/assign/save")
    @ResponseBody
    public JsonResult assign(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        User u = RequestUitl.getUserInfo(request);
        Integer routeKey = body.getInteger("routeKey");
        Assert.notBlank(routeKey, "请选择一个路由信息");
        Collection<Long> conveyanceKeys = StringUtils.longCollection(body.get("conveyanceKeys"));
        Assert.notEmpty(conveyanceKeys, "至少选择一个运单");
        conveyanceService.saveRouteAssign(u.getId(), conveyanceKeys, routeKey);
        return JsonResult.SUCCESS;
    }

    @RequestMapping(value = "/assign/whole")
    @ResponseBody
    public JsonResult assignWhole(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        User u = RequestUitl.getUserInfo(request);
        Integer assignKey = body.getInteger("assignKey");
        Assert.notBlank(assignKey, "请选择承运人");
        Collection<Long> conveyanceKeys = StringUtils.longCollection(body.get("conveyanceKeys"));
        Assert.notEmpty(conveyanceKeys, "至少选择一个运单");
        conveyanceService.saveWholeAssign(u.getId(), conveyanceKeys, assignKey);
        return JsonResult.SUCCESS;
    }

    @RequestMapping(value = "/share/group")
    @ResponseBody
    public JsonResult shareGroup(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        User u = RequestUitl.getUserInfo(request);
        Integer groupKey = body.getInteger("groupKey");
        Assert.notBlank(groupKey, "请选择一个项目组");
        Collection<Long> conveyanceKeys = StringUtils.longCollection(body.get("conveyanceKeys"));
        Assert.notEmpty(conveyanceKeys, "至少选择一个运单");
        conveyanceService.shareGroup(u.getId(), conveyanceKeys, groupKey);
        return JsonResult.SUCCESS;
    }

    @RequestMapping(value = "/edit/number")
    @ResponseBody
    public JsonResult editNumber(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        User u = RequestUitl.getUserInfo(request);
        Long conveyanceKey = body.getLong("conveyanceKey");
        Assert.notBlank(conveyanceKey, "请选择一个运单");
        String number = body.get("number");
        Assert.notBlank(number, "运单号不能为空");
        conveyanceService.editNumber(u.getId(), conveyanceKey, number);
        return JsonResult.SUCCESS;
    }

    @RequestMapping(value = "/edit/assign")
    @ResponseBody
    public JsonResult editAssign(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        User u = RequestUitl.getUserInfo(request);
        Long id = body.getLong("id");
        Assert.notBlank(id, "请选择一个运单");
        Integer ownerKey = body.getInteger("ownerKey");
        Assert.notBlank(ownerKey, "请选择一个承运人");
        conveyanceService.editAssign(u.getId(), id, ownerKey);
        return JsonResult.SUCCESS;
    }

    @RequestMapping(value = "/cancel/assign")
    @ResponseBody
    public JsonResult cancelAssign(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        User u = RequestUitl.getUserInfo(request);
        Collection<Long> conveyanceKeys = StringUtils.longCollection(body.get("conveyanceKeys"));
        Assert.notEmpty(conveyanceKeys, "至少选择一个运单");
        conveyanceService.cancelAssign(u.getId(), conveyanceKeys);
        return JsonResult.SUCCESS;
    }
}
