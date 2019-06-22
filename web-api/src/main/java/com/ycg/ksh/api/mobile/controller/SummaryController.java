package com.ycg.ksh.api.mobile.controller;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/21
 */

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.util.DateUtils;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.service.ConveyanceSearch;
import com.ycg.ksh.entity.service.WaybillSerach;
import com.ycg.ksh.service.api.SummaryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 数据汇总
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/21
 */
@Controller("mobile.summary.controller")
@RequestMapping("/mobile/summary")
public class SummaryController extends BaseController {

    @Resource
    private SummaryService summaryService;

    //项目组任务查询
    @RequestMapping(value = "waybill")
    @ResponseBody
    public JsonResult summaryWaybill(@RequestBody(required = false) RequestObject body, HttpServletRequest request) throws Exception {
        logger.info("summary waybill -> {} ", body);
        JsonResult jsonResult = new JsonResult();
        User u = RequestUitl.getUserInfo(request);
        if (body == null) {
            body = new RequestObject();
        }
        WaybillSerach serach = new WaybillSerach();
        serach.setGroupId(body.getInteger("groupId"));
        serach.setUserId(u.getId());

        Date selectTime = body.getDate("selectTime");
        if(selectTime == null){
            selectTime = new Date();
        }
        serach.setFirstTime(DateUtils.minOfDay(selectTime));
        serach.setSecondTime(DateUtils.maxOfDay(selectTime));
        jsonResult.put("summarys", summaryService.summaryWaybill(serach));
        return jsonResult;
    }

    //项目组任务查询
    @RequestMapping(value = "conveyance")
    @ResponseBody
    public JsonResult summaryConveyance(@RequestBody(required = false) RequestObject body, HttpServletRequest request) throws Exception {
        logger.info("summary conveyance -> {} ", body);
        JsonResult jsonResult = new JsonResult();
        User u = RequestUitl.getUserInfo(request);
        if (body == null) {
            body = new RequestObject();
        }
        ConveyanceSearch serach = new ConveyanceSearch();
        serach.setGroupKey(body.getInteger("groupId"));
        serach.setCreateKey(u.getId());

        Date selectTime = body.getDate("selectTime");
        if(selectTime == null){
            selectTime = new Date();
        }
        serach.setFirstTime(DateUtils.minOfDay(selectTime));
        serach.setSecondTime(DateUtils.maxOfDay(selectTime));
        jsonResult.put("summary", summaryService.summaryConveyance(serach));
        return jsonResult;
    }

}
