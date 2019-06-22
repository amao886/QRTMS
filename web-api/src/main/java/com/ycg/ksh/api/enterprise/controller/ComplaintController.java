package com.ycg.ksh.api.enterprise.controller;

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.constant.CoreConstants;
import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.entity.service.AuthorizeUser;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.ComplaintSearch;
import com.ycg.ksh.service.api.ComplaintService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * 投诉管理
 *
 * @author: wangke
 * @create: 2018-10-30 14:54
 **/
@Controller("enterprise.complaint.controller")
@RequestMapping("/enterprise/complaint")
public class ComplaintController extends BaseController {

    @Resource
    ComplaintService complaintService;

    /**
     * 投訴管理入口
     *
     * @param viewKey
     * @param model
     * @param request
     * @throws Exception
     * @Author: wangke
     */
    @RequestMapping(value = "/search/{viewKey}")
    public String search(@PathVariable String viewKey, Model model, HttpServletRequest request) {
        model.addAllAttributes(RequestUitl.getRequestParameters(request));
        return "enterprise/complaint/" + viewKey;
    }

    /**
     * 客户投诉
     *
     * @param body
     * @param request
     * @return
     */
    @RequestMapping("/entry/complaint")
    @ResponseBody
    public JsonResult complaint(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        Collection<Long> orderKeys = StringUtils.longCollection(body.get("orderKeys"));
        Assert.notEmpty(orderKeys, "至少选择一项需要投诉的订单");
        String content = body.get("content");
        Assert.notBlank(content, "投诉内容不能为空");
        complaintService.modifyComplaint(loadUserKey(request), content, orderKeys);
        return JsonResult.SUCCESS;
    }

    /**
     * 投诉列表
     *
     * @param body
     * @param request
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public JsonResult complaintsList(@RequestBody RequestObject body, HttpServletRequest request) {
        ComplaintSearch search = new ComplaintSearch();
        search.setFirstTime(body.getDate("firstTime"));
        search.setSecondTime(body.getDate("secondTime"));
        search.setLikeString(body.get("likeString"));
        AuthorizeUser user = loadUser(request);
        if (CoreConstants.USER_CATEGORY_SHIPPER - user.getIdentityKey() == 0) {
            search.setPartnerType(PartnerType.SHIPPER);
        } else if (CoreConstants.USER_CATEGORY_CONVEY - user.getIdentityKey() == 0) {
            search.setPartnerType(PartnerType.CONVEY);
        } else if (CoreConstants.USER_CATEGORY_RECEIVE - user.getIdentityKey() == 0) {
            search.setPartnerType(PartnerType.RECEIVE);
        }
        search.setUesrKey(user.getId());
        PageScope scope = new PageScope(body.getInteger("num"), body.getInteger("size"));
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("page", complaintService.pageComplaint(search, scope));
        return jsonResult;
    }

}
