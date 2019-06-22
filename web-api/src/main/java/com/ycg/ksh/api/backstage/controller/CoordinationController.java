package com.ycg.ksh.api.backstage.controller;

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.constant.Directory;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.extend.FileUploadHelper;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.*;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.service.*;
import com.ycg.ksh.service.api.ConveyanceService;
import com.ycg.ksh.service.api.ExceptionService;
import com.ycg.ksh.service.api.ProjectGroupService;
import com.ycg.ksh.service.api.RemindersThingsService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 协同控制器
 *
 * @author wangke
 * @create 2018-02-01 10:23
 **/
@Controller("backstage.coordination.controller")
@RequestMapping("/backstage/coordination")
public class CoordinationController extends BaseController {

    @Resource
    RemindersThingsService remindersThingsService;

    @Resource
    ConveyanceService conveyanceService;

    @Resource
    ProjectGroupService projectGroupService;
    @Resource
    ExceptionService exceptionService;


    @RequestMapping(value = "search/todo")
    public String searchTodo(Model model, HttpServletRequest request) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        User u = RequestUitl.getUserInfo(request);
        List<ProjectGroup> projectGroups = projectGroupService.listByUserKey(u.getId());
        /*
        Integer groupId = body.getInteger("groupKey");
        if (null != groupId && groupId <= 0) {
            RequestUitl.modifyGroupKey(request, groupId);
        }
        if (null == groupId) {
            if (CollectionUtils.isNotEmpty(projectGroups)) {
                body.put("groupKey", RequestUitl.getGroupKey(request, projectGroups.get(0).getId()));
            } else {
                body.put("groupKey", RequestUitl.getGroupKey(request, 0));
            }
        }
        */
        ReminderSearch search = body.toJavaBean(ReminderSearch.class);
        //search.setUserKey(u.getId());
        search.setSendKey(u.getId());
        Integer fettle = body.getInteger("fettle");
        if(fettle == null || fettle < 0){
            search.setFettles(null);
            body.put("fettle", -1);
        }else{
            search.setFettles(new Integer[]{ fettle});
        }
        Calendar calendar = Calendar.getInstance();
        if(search.getSecondTime() == null){
            search.setSecondTime(DateUtils.maxOfDay(calendar));
        }else{
            search.setSecondTime(DateUtils.maxOfDay(search.getSecondTime()));
        }
        if(search.getFirstTime() == null){
            calendar.add(Calendar.DAY_OF_YEAR, -30);
            search.setFirstTime(DateUtils.maxOfDay(calendar));
        }else{
            search.setFirstTime(DateUtils.maxOfDay(search.getFirstTime()));
        }
        body.put("secondTime", DateFormatUtils.format(search.getSecondTime(), "yyyy-MM-dd"));
        body.put("firstTime", DateFormatUtils.format(search.getFirstTime(), "yyyy-MM-dd"));
        if(search.getMsgType() == null || search.getMsgType() <= 0){
            search.setMsgType(null);
            body.put("msgType", -1);
        }
        logger.info("待办进度查询 -> {}", body);
        PageScope pageScope = new PageScope(body.getInteger("num"), 10);
        model.addAttribute("results", remindersThingsService.listMergeRemindersThings(u.getId(), search, pageScope));
        model.addAttribute("groups", projectGroups);
        model.addAttribute("search", body);
        return "/backstage/coordination/progress";
    }

    /**
     * 查询待办事项/进度跟踪
     *
     * @param request
     * @return
     * @author wangke
     * @date 2018/2/27 13:19
     */
    @RequestMapping(value = "/list/todo/log")
    @ResponseBody
    public JsonResult listTodoLog(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        logger.info("list todo log -> {}", body);
        Integer todoKey = body.getInteger("todoKey");
        Assert.notBlank(todoKey, "待办事项编号不能为空");
        User user = RequestUitl.getUserInfo(request);
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("conveyance", remindersThingsService.getByTodoKey(todoKey));
        jsonResult.put("logs", remindersThingsService.listTodologByKey(user.getId(), todoKey));
        return jsonResult;
    }
    /**
     * TODO 协同列表页跳转
     *
     * @param model
     * @param request
     * @return
     * @author wangke
     * @date 2018/2/1 10:26
     */
    @RequestMapping(value = "search")
    public String search(Model model, HttpServletRequest request) throws Exception {
        User user = RequestUitl.getUserInfo(request);
        List<ProjectGroup> projectGroups = projectGroupService.listByUserKey(user.getId());
        Integer groupKey = RequestUitl.getGroupKey(request, 0);
        model.addAttribute("groupKey", groupKey);
        model.addAttribute("groups", projectGroups);
        model.addAttribute("today", DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
        return "/backstage/coordination/manage";
    }

    /**
     * 查询待办事项/进度跟踪
     *
     * @param request
     * @return
     * @author wangke
     * @date 2018/2/27 13:19
     */
    @RequestMapping(value = "/backlog/list")
    @ResponseBody
    public JsonResult getBacklog(HttpServletRequest request) throws Exception {
        User user = RequestUitl.getUserInfo(request);
        JsonResult jsonResult = new JsonResult();
        Collection<MergeRemindersThings> todos = remindersThingsService.listTodos(user.getId());
        Integer locations = 0, receipts = 0;
        for (MergeRemindersThings todo : todos) {
            if(todo.getMsgType() == 1){
                locations += 1;
            }else{
                receipts += 1;
            }
        }
        jsonResult.put("withList", todos);
        jsonResult.put("locations", locations);
        jsonResult.put("receipts", receipts);
        jsonResult.put("trackList", remindersThingsService.listFollows(user.getId(), 3));
        return jsonResult;
    }

    /**
     * 待办转发
     *
     * @param body
     * @param request
     * @return
     * @author wangke
     * @date 2018/2/28 9:20
     */
    @RequestMapping(value = "/conveyance/forward")
    @ResponseBody
    public JsonResult forwardConveyance(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        Assert.notNull(body, Constant.PARAMS_ERROR);
        User user = RequestUitl.getUserInfo(request);
        RemindersThings reminder = body.toJavaBean(RemindersThings.class);
        Integer groupKey = body.getInteger("groupKey");
        if (groupKey != null && groupKey > 0) {
            remindersThingsService.forwardProjectGroup(user.getId(), groupKey, reminder);
        } else {
            Collection<Long> subordinateKeys = StringUtils.longCollection(body.get("forwardId"));
            Assert.notEmpty(subordinateKeys, "至少选择一个下级");
            remindersThingsService.forwardSubordinate(user.getId(), subordinateKeys, reminder);
        }
        return JsonResult.SUCCESS;
    }

    /**
     * 查询催办对象
     *
     * @param conveyanceId
     * @param request
     * @return
     * @author wangke
     * @date 2018/2/28 9:24
     */
    @RequestMapping(value = "/get/reminders/{conveyanceId}")
    @ResponseBody
    public JsonResult queryRemindersList(@PathVariable Long conveyanceId, HttpServletRequest request) {
        User user = RequestUitl.getUserInfo(request);
        JsonResult jsonResult = new JsonResult();
        Conveyance conveyance = conveyanceService.queryConveyanceInfo(conveyanceId);
        if (null == conveyance) {
            jsonResult.modify(false, "当前运单不存在");
        } else {
            boolean flag = false;
            if (conveyance.getOwnerGroupKey() != null && conveyance.getOwnerGroupKey() <= 0) {
                Collection<ProjectGroup> projectGroups = projectGroupService.listByUserKey(user.getId());
                flag = CollectionUtils.isNotEmpty(projectGroups);
                jsonResult.put("group", projectGroups);
            } else {
                jsonResult.put("group", Collections.emptyList());
            }
            if (conveyance.getHaveChild()) {
                Collection<MergeUserConveyance> collection = remindersThingsService.queryUserList(user.getId(), conveyanceId);
                flag = CollectionUtils.isNotEmpty(collection);
                jsonResult.put("user", collection);
            } else {
                jsonResult.put("user", Collections.emptyList());
            }
            if (!flag) {
                jsonResult.modify(false, "该项不能转催");
            }
        }
        return jsonResult;
    }


    /**
     * 查询运单列表
     *
     * @param object
     * @param request
     * @return
     * @author wangke
     * @date 2018/2/28 17:20
     */
    @RequestMapping(value = "/get/conveyanceList")
    @ResponseBody
    public JsonResult conveyanceList(@RequestBody RequestObject object, HttpServletRequest request) throws ReflectiveOperationException {
        User user = RequestUitl.getUserInfo(request);
        ConveyanceSearch search = object.toJavaBean(ConveyanceSearch.class);
        search.setCreateKey(user.getId());
        if (search.getFettle() == null || search.getFettle() <= 0) {
            search.setFettle(null);
        }
        Date deliveryTime = object.getDate("deliveryTime");
        if (deliveryTime != null) {
            search.setTime(deliveryTime, 0);
        }
        search.setLocationCount(object.getInteger("location"));
        if (search.getLocationCount() != null && search.getLocationCount() == -1) {
            search.setLocationCount(object.getInteger("locationCount"));
        }
        Integer timeType = object.getInteger("request");
        if (timeType != null) {
            if (timeType == -1) {
                search.setRequirements(object.getDate("requestTime"));
            } else {
                search.setRequirementsTime(new Date(), timeType);
            }
        }
        JsonResult jsonResult = new JsonResult();
        PageScope pageScope = new PageScope(object.getInteger("num"), 8);
        jsonResult.put("page", remindersThingsService.pageConveyance(search, pageScope));
        return jsonResult;
    }

    /**
     * 录入异常信息
     *
     * @param object
     * @param request
     * @return
     * @author wangke
     * @date 2018/3/2 10:18
     */
    @RequestMapping(value = "/save/exception", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult enteringException(HttpServletRequest request) throws Exception {
        User user = RequestUitl.getUserInfo(request);
        String content = request.getParameter("content");
        Assert.notBlank(content, "异常说明");
        String waybillid = request.getParameter("waybillid");
        Assert.notBlank(waybillid, "任务单编号不能为空");
        String conveyanceId = request.getParameter("conveyanceId");
        Assert.notBlank(conveyanceId, "运单编号不能为空");

        WaybillException waybillException = new WaybillException();
        waybillException.setUserid(user.getId());
        waybillException.setContent(content);
        waybillException.setWaybillid(Integer.parseInt(waybillid));
        waybillException.setConveyanceId(Long.parseLong(conveyanceId));
        Collection<String> collection = FileUploadHelper.saveImage(request, SystemUtils.directoryUpload(), Directory.UPLOAD_EXCEPTION.getDir());
        exceptionService.saveWaybillException(waybillException, collection, false);
        return JsonResult.SUCCESS;
    }

    /**
     * 查询异常列表
     *
     * @param model
     * @param request
     * @return
     * @author wangke
     * @date 2018/3/2 11:01
     */
    @RequestMapping(value = "/get/searchExceptionPage")
    public String searchExceptionPage(Model model, HttpServletRequest request) throws ReflectiveOperationException {
        RequestObject body = new RequestObject(request.getParameterMap());
        User user = RequestUitl.getUserInfo(request);
        PageScope pageScope = new PageScope(body.getInteger("num"), 20);
        ExceptionSearch exceptionSearch = body.toJavaBean(ExceptionSearch.class);
        Calendar calendar = Calendar.getInstance();
        if(exceptionSearch.getSecondTime() == null){
            exceptionSearch.setSecondTime(DateUtils.maxOfDay(calendar));
        }else{
            exceptionSearch.setSecondTime(DateUtils.maxOfDay(exceptionSearch.getSecondTime()));
        }
        if(exceptionSearch.getFirstTime() == null){
            calendar.add(Calendar.DAY_OF_YEAR, -30);
            exceptionSearch.setFirstTime(DateUtils.maxOfDay(calendar));
        }else{
            exceptionSearch.setFirstTime(DateUtils.maxOfDay(exceptionSearch.getFirstTime()));
        }
        exceptionSearch.setSendKey(user.getId());
        body.put("secondTime", DateFormatUtils.format(exceptionSearch.getSecondTime(), "yyyy-MM-dd"));
        body.put("firstTime", DateFormatUtils.format(exceptionSearch.getFirstTime(), "yyyy-MM-dd"));
        if(exceptionSearch.getAssignKey() == null || exceptionSearch.getAssignKey() <= 0){
            body.put("assignName", "选择承运人");
        }
        model.addAttribute("page", exceptionService.pageConveyanceException(user.getId(), exceptionSearch, pageScope));
        model.addAttribute("search", body);
        model.addAttribute("imagePath", imagepath());// 图片服务器的路径
        return "/backstage/coordination/exception_list";
    }

    /**
     * 查询异常详情
     *
     * @param conveyanceId
     * @param request
     * @return
     * @author wangke
     * @date 2018/3/2 11:43
     */
    @RequestMapping(value = "/getExceptionInfo/{conveyanceId}")
    @ResponseBody
    public JsonResult getExceptionInfo(@PathVariable Long conveyanceId, HttpServletRequest request) {
        Conveyance conveyance = conveyanceService.queryConveyanceInfo(conveyanceId);
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("conveyance", conveyance);
        jsonResult.put("uname", UserUtil.decodeName(conveyance.getContactName()));
        return jsonResult;
    }

    /**
     * 上报位置
     */
    @RequestMapping(value="/loaction", method= RequestMethod.POST)
    @ResponseBody
    public JsonResult reportLoaction(@RequestBody RequestObject object, HttpServletRequest request)throws Exception{
        logger.debug("reportLoaction -> {}", object);
        JsonResult jsonResult = new JsonResult();
        Assert.notNull(object, Constant.PARAMS_ERROR);
        User user = RequestUitl.getUserInfo(request);
        WaybillTrack waybillTrack = new WaybillTrack();
        Integer waybillKey = object.getInteger("waybillKey");
        Assert.notBlank(waybillKey, "需要先选择任务单再上报位置");
        waybillTrack.setLocations(object.get("area") + object.get("street"));
        Assert.notBlank(waybillTrack.getLocations(), "上报地址不能为空");
        waybillTrack.setUserid(object.getInteger("driverKey"));
        waybillTrack.setLatitude(object.getDouble("lat"));
        waybillTrack.setLongitude(object.getDouble("lng"));
        waybillTrack.setWaybillid(waybillKey);
        Integer msgKey = object.getInteger("msgkey");
        Boolean punctual = true;
        Integer p = object.getInteger("punctual");
        if(p == null || p == 0){
            punctual = false;
        }
        Date selectTime = object.getDate("selectTime");
        remindersThingsService.location(user.getId(), waybillTrack, msgKey, punctual, selectTime);
        return jsonResult;
    }

}
