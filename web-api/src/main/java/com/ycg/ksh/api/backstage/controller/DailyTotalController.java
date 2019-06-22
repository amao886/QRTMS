package com.ycg.ksh.api.backstage.controller;

import com.alibaba.fastjson.JSONArray;
import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.backstage.model.BillTotal;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.DateUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.service.*;
import com.ycg.ksh.entity.persistent.ProjectGroup;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.persistent.WaybillTotalView;
import com.ycg.ksh.service.api.ProjectGroupService;
import com.ycg.ksh.service.api.WayBillTotalService;
import com.ycg.ksh.service.api.WaybillService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * 统计
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-17 14:29:30
 */
@Controller("backstage.dailyTotal.controller")
@RequestMapping("/backstage/dailyTotal")
public class DailyTotalController extends BaseController {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Resource
    protected WaybillService waybillService;
    @Resource
    protected ProjectGroupService groupService;

    @Resource
    protected WayBillTotalService wayBillTotalService;

    /**
     * 统计详情搜索列表
     *
     * @param search  参数对象
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/detail")
    public String dailyDetailSearch(HttpServletRequest request, Model model) throws Exception {
        RequestObject object = new RequestObject(request.getParameterMap());
        logger.info("-----------dailyDetailSearch------------" + object);
        Assert.notNull(object, Constant.PARAMS_ERROR);
        User u = RequestUitl.getUserInfo(request);
        // 查询资源组
        List<ProjectGroup> projectGroups = groupService.listByUserKey(u.getId());
        if (CollectionUtils.isNotEmpty(projectGroups)) {
            model.addAttribute("groups", projectGroups);
            Integer groupId = object.getInteger("groupid");
            if (groupId == null || groupId <= 0) {
                groupId = RequestUitl.getGroupKey(request, projectGroups.get(0).getId());
                object.put("groupid", groupId + "");
            }
            WaybillSerach serach = new WaybillSerach();
            serach.setUserId(u.getId());
            serach.setGroupId(groupId);

            serach.setLikeString(StringUtils.trimToEmpty(object.get("likeString")));
            serach.setWaybillFettles(StringUtils.integerArray(object.get("waybillFettles")));

            Calendar cal = Calendar.getInstance();
            Date bindTime = object.getDate("bindtime");
            if (bindTime != null) {
                cal.setTime(bindTime);
            }
            serach.setFirstTime(DateUtils.minOfDay(cal));
            serach.setSecondTime(DateUtils.maxOfDay(cal));

            Integer pageSize = object.getInteger("size");
            if (pageSize == null || pageSize <= 0) {
                pageSize = 30;
            }
            Integer pageNum = object.getInteger("num");
            WaybillContext context = WaybillContext.buildContext(u, serach, new PageScope(pageNum, pageSize));
            context.setAssociate(WaybillAssociate.associateProjectGroup());
            model.addAttribute("page", waybillService.pageDailyWaybill(context));
        }
        model.addAttribute("search", object);
        return "/backstage/dailyTotal/detail";
    }

    /**
     * 按日统计任务单数据
     */
    @RequestMapping(value = "/searchDayCount")
    public String customerSearch(Model model, HttpServletRequest request) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        logger.info("-----------searchDayCount------------{}", body);
        User u = RequestUitl.getUserInfo(request);
        // 查询资源组
        List<ProjectGroup> groupList = groupService.listByUserKey(u.getId());
        if (CollectionUtils.isNotEmpty(groupList)) {
            model.addAttribute("groups", groupList);
            if (body.getInteger("groupid") == null || body.getInteger("groupid") <= 0) {
                body.put("groupid", RequestUitl.getGroupKey(request, groupList.get(0).getId()));
            }
        } else {
            return "/backstage/dailyTotal/manage";
        }
        if (body.getInteger("flag") == null || body.getInteger("flag") <= 0) {
            body.put("flag", 0);
        }
        Integer pageSize = body.getInteger("size");
        Integer pageNum = body.getInteger("num");
        body.put("sort", "desc");
        CustomPage<WaybillTotalView> page = wayBillTotalService.queryTotalPage(body.toJavaBean(MergeBillTotal.class), new PageScope(pageNum, pageSize));
        model.addAttribute("page", page);
        model.addAttribute("billTotal", body);
        return "/backstage/dailyTotal/manage";
    }

    /**
     * 月统计图页面跳转
     * <p>
     *
     * @param object  参数
     * @param request
     * @param model
     * @return
     * @throws Exception
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-07 10:06:39
     */
    @RequestMapping(value = "monthchart")
    public String monthchart(HttpServletRequest request, Model model) throws Exception {
        LocalDate localDate = LocalDate.now().minusMonths(1);//前一个月
        model.addAttribute("cyear", localDate.getYear());//默认年
        model.addAttribute("cmonth", localDate.getMonthValue());//默认月
        User u = RequestUitl.getUserInfo(request);
        // 查询资源组
        List<ProjectGroup> groupList = groupService.listByUserKey(u.getId());
        if (CollectionUtils.isNotEmpty(groupList)) {
            model.addAttribute("groups", groupList);//项目组信息
            model.addAttribute("groupid", RequestUitl.getGroupKey(request, groupList.get(0).getId()));//默认选中项目组
        }
        return "/backstage/dailyTotal/monthchart";
    }

    @RequestMapping(value = "monthchart/search")
    @ResponseBody
    public JsonResult monthchart(@RequestBody RequestObject object, HttpServletRequest request, Model model) throws Exception {
        Integer groupId = object.getInteger("groupid");
        if (groupId == null || groupId <= 0) {
            throw new ParameterException(groupId, "必须选择一个项目组");
        }
        Integer selectYear = object.getInteger("selectYear");
        LocalDate localDate = LocalDate.now().minusMonths(1);//前一个月l
        if (selectYear != null && selectYear > 0) {
            localDate = localDate.withYear(selectYear);
        }
        Integer selectMonth = object.getInteger("selectMonth");
        if (selectMonth != null && selectMonth > 0) {
            localDate = localDate.withMonth(selectMonth);
        }
        //查询时间段的开始时间
        String stime = localDate.with(TemporalAdjusters.firstDayOfMonth()).format(formatter);
        //查询时间段的结束时间
        String etime = localDate.with(TemporalAdjusters.lastDayOfMonth()).format(formatter);
        object.put("startTime", stime);
        object.put("endTime", etime);
        CustomPage<WaybillTotalView> page = wayBillTotalService.queryTotalPage(object.toJavaBean(MergeBillTotal.class), new PageScope(1, localDate.getMonth().maxLength()));
        logger.info("---结果：---" + page.getCollection());
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("results", getDayData(page.getCollection()));
        return jsonResult;
    }

    //追加空日期数据
    private JSONArray getDayData(Collection<WaybillTotalView> collection) throws ParseException {
        List<WaybillTotalView> list = new ArrayList<WaybillTotalView>();
        JSONArray jsonArray = new JSONArray();
        if (collection.isEmpty()) {
            return jsonArray;
        }
        list.addAll(collection);
        int minday = 1, maxday = LocalDate.parse(list.get(0).getCreateTime(), formatter).getMonth().maxLength();
        for (int i = 0; i < list.size(); i++) {//计算数据值大于最小数
            WaybillTotalView waybillTotalView = list.get(i);
            int createTimeDay = Integer.parseInt(LocalDate.parse(waybillTotalView.getCreateTime(), formatter).format(DateTimeFormatter.ofPattern("dd")));//当前天数
            int t = createTimeDay - minday;
            if (t > 1) {
                if (i == 0) {
                    String startTime = LocalDate.parse(waybillTotalView.getCreateTime(), formatter).with(TemporalAdjusters.firstDayOfMonth()).format(formatter);
                    BillTotal total = addModel(waybillTotalView.getGroupid());
                    total.setCreateTime(startTime);
                    jsonArray.add(total);
                    getAddData(jsonArray, t - 1, startTime, waybillTotalView.getGroupid());
                    jsonArray.add(waybillTotalView);
                    minday = minday + t;
                } else {
                    getAddData(jsonArray, t - 1, list.get(i - 1).getCreateTime(), waybillTotalView.getGroupid());
                    jsonArray.add(waybillTotalView);
                    minday = minday + t;
                }
            } else {
                jsonArray.add(waybillTotalView);
                if (i != 0) {
                    minday = minday + 1;
                }
            }
        }
        if (minday != maxday) {//计算当前天数小于本月最大天数
            int t = maxday - minday;
            getAddData(jsonArray, t, list.get(list.size() - 1).getCreateTime(), list.get(list.size() - 1).getGroupid());
        }
        return jsonArray;
    }

    //递归空余数据
    private JSONArray getAddData(JSONArray jsonArray, int day, String dateTime, Integer groupid) throws ParseException {
        BillTotal total = addModel(groupid);
        if (day != 0) {
            total.setCreateTime(DateUtils.getSpecifiedDayAfter(dateTime, 1));
            jsonArray.add(total);
            return getAddData(jsonArray, day - 1, total.getCreateTime(), groupid);
        }
        return jsonArray;
    }

    //对象赋值
    private BillTotal addModel(Integer groupid) {
        BillTotal total = new BillTotal();
        total.setCompanyName(null);
        total.setGroupid(groupid);
        total.setSendRate("0%");
        total.setSendCount(0);
        total.setTimeCount(0);
        total.setToCount(0);
        total.setAllCount(0);
        return total;
    }
}
