package com.ycg.ksh.api.enterprise.controller;

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 收集汇总
 *
 * @author: wangke
 * @create: 2018-10-30 08:41
 **/

@Controller("enterprise.collect.controller")
@RequestMapping("/enterprise/collect")
public class CollectController extends BaseController {

    /**
     * 考核管理入口
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
        return "/enterprise/collect/" + viewKey;
    }
}
