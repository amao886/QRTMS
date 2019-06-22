package com.ycg.ksh.api.mobile.controller;

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 名称：DailyTotalController 描述： 每日统计 创建人：yangc 创建时间：2017年7月26日 上午9:36:31
 */

@Controller("mobile.dailyTotal.controller")
@RequestMapping("/mobile/dailyTotal")
public class DailyTotalController extends BaseController {

    @Resource
    WayBillTotalService wayBillTotalService;

    @Resource
    protected ProjectGroupService groupService;

    @Resource
    WaybillService waybillService;

    /**
     * 日统计任务单列表
     *
     * @param mergeBillTotal
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/search")
    @ResponseBody
    public JsonResult dailyTotal(@RequestBody(required = false) MergeBillTotal mergeBillTotal, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("======dailyTotal========" + mergeBillTotal);
        Assert.notNull(mergeBillTotal, Constant.PARAMS_ERROR);
        JsonResult jsonResult = new JsonResult();
        mergeBillTotal.setGroupid(mergeBillTotal.getGroupid());
        if (mergeBillTotal.getGroupid() == null) {
            User u = RequestUitl.getUserInfo(request);
            // 查询资源组
            List<ProjectGroup> projectGroups = groupService.listByUserKey(u.getId());
            if (CollectionUtils.isNotEmpty(projectGroups)) {
                mergeBillTotal.setGroupid(projectGroups.get(0).getId());// 默认选择第一个资源组
            } else {
                jsonResult.put("entity", null);
                return jsonResult;
            }
        }
        LocalDate localDate = LocalDate.now();
        if (mergeBillTotal.getYear() == null || "".equals(mergeBillTotal.getYear())) {
            mergeBillTotal.setYear(String.valueOf(localDate.getYear()));
        }
        if (mergeBillTotal.getMonth() == null || "".equals(mergeBillTotal.getMonth())) {
            mergeBillTotal.setMonth(String.valueOf(localDate.getMonthValue()));
        }
        localDate = LocalDate.of(Integer.valueOf(mergeBillTotal.getYear()), Integer.valueOf(mergeBillTotal.getMonth()), 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        mergeBillTotal.setStartTime(localDate.format(formatter));
        mergeBillTotal.setEndTime(localDate.with(TemporalAdjusters.lastDayOfMonth()).format(formatter));
        mergeBillTotal.setFlag(999);
        mergeBillTotal.setSort("desc");
        CustomPage<WaybillTotalView> page = wayBillTotalService.queryTotalPage(mergeBillTotal, new PageScope(1, localDate.getMonth().maxLength()));
        jsonResult.put("entity", page.getCollection());
        logger.debug("===================数据==============================" + page.getCollection());
        return jsonResult;
    }

    /**
     * 日统计任务单详情搜索列表
     *
     * @param object
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult dailyDetailSearch(@RequestBody RequestObject object, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JsonResult jsonResult = new JsonResult();
        WaybillSerach waybillSerach = new WaybillSerach();
        User u = RequestUitl.getUserInfo(request);
        waybillSerach.setUserId(u.getId());
        logger.info("-----------dailyDetailSearch------------" + object);
        if (StringUtils.isNotEmpty(object.get("customerName"))) {
            waybillSerach.setLikeString(StringUtils.trimToEmpty(object.get("customerName")));
        }
        if (object.getInteger("bindstatus") != null) {
            waybillSerach.setWaybillFettles(new Integer[]{object.getInteger("bindstatus")});
        }
        Calendar cal = Calendar.getInstance();
        Date bindTime = object.getDate("bindtime");
        if (bindTime != null) {
            cal.setTime(bindTime);
        }
        waybillSerach.setFirstTime(DateUtils.minOfDay(cal));
        waybillSerach.setSecondTime(DateUtils.maxOfDay(cal));
        Integer pageSize = object.getInteger("size");
        Integer pageNum = object.getInteger("num");


        WaybillContext context = WaybillContext.buildContext(u, waybillSerach, new PageScope(pageNum, pageSize));
        context.setAssociate(WaybillAssociate.associateProjectGroup());
        jsonResult.put("page", waybillService.pageDailyWaybill(context));
        return jsonResult;
    }
}
