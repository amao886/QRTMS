package com.ycg.ksh.api.admin.controller;

import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.util.DateUtils;
import com.ycg.ksh.entity.service.MergeBehaviorTotal;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.service.api.BusinessStatisticsService;
import com.ycg.ksh.service.api.ProjectGroupService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 业务数据统计控制器
 *
 * @author wangke
 * @create 2018-03-13 14:17
 **/
@Controller("admin.business.controller")
@RequestMapping("/admin/business")
public class StatisticsController extends BaseController {

    @Resource
    BusinessStatisticsService businessStatisticsService;

    @Resource
    ProjectGroupService projectGroupService;

    /**
     * 数据列表
     *
     * @param model
     * @param request
     * @return
     * @author wangke
     * @date 2018/3/13 14:19
     */
    @RequestMapping(value = "/search")
    public String search(Model model, HttpServletRequest request) {
        return "/admin/dataanalysis/data_list";
    }

    /**
     * 用户行为列表查询
     *
     * @param model
     * @param request
     * @return
     * @author wangke
     * @date 2018/3/13 14:23
     */
    @RequestMapping(value = "/queryUserbehavior")
    public String queryUserbehavior(Model model, HttpServletRequest request) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        PageScope pageScope = new PageScope(body.getInteger("num"), body.getInteger("size"));

        if (body.getInteger("falg") == null || body.getInteger("falg") <= 0) {
            body.put("falg", 999);
        }
        model.addAttribute("page", businessStatisticsService.queryUserbehaviorPage(body.toJavaBean(MergeBehaviorTotal.class), pageScope));
        model.addAttribute("groups", projectGroupService.listGroup());
        model.addAttribute("search", body);
        return "/admin/dataanalysis/userbehavior";
    }

    /**
     * 用户行为统计页面
     *
     * @param model
     * @param request
     * @return
     * @author wangke
     * @date 2018/3/15 9:53
     */
    @RequestMapping(value = "/loadBehaviorTotal")
    public String loadBehaviorTotal(Model model, HttpServletRequest request) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        //PageScope pageScope = new PageScope(body.getInteger("num"), body.getInteger("size"));
        model.addAttribute("page", businessStatisticsService.behaviorTotalPage(body.toJavaBean(MergeBehaviorTotal.class), new PageScope(1, 60)));
        model.addAttribute("dates", DateUtils.getDateList());
        model.addAttribute("search", body);
        return "/admin/dataanalysis/behaviorTotal";
    }

    /**
     * 任务单列表
     *
     * @param model
     * @param request
     * @return
     * @author wangke
     * @date 2018/3/20 13:47
     */
    @RequestMapping(value = "/loadWaybillList")
    public String loadWaybillList(Model model, HttpServletRequest request) {
        return "/admin/dataanalysis/waybill_List";
    }

    /**
     * 查询任务单详情
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/loadWaybillInfo")
    public String loadWaybillInfo(Model model, HttpServletRequest request) {
        return "/admin/dataanalysis/waybill_detail";
    }

    /**
     * 查询回单列表
     *
     * @param model
     * @param request
     * @return
     * @author wangke
     * @date 2018/3/20 13:49
     */
    @RequestMapping(value = "/laodReceiptList")
    public String laodReceiptList(Model model, HttpServletRequest request) {
        return "/admin/dataanalysis/receipt_List";
    }

    /**
     * 查询回单详情
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/loadReceiptInfo")
    public String loadReceiptInfo(Model model, HttpServletRequest request){

        return "/admin/dataanalysis/loadReceiptInfo";
    }

}
