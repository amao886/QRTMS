package com.ycg.ksh.api.backstage.controller;

import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.entity.RequestSerial;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.entity.persistent.SysRequestSerial;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.service.api.SysRequestSerialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author:wangke
 * @Description: 用户行为数据跟踪
 * @Date:Create in 16:31 2017/10/26
 * @Modified By:
 */

@Controller("backstage.requestserial.controller")
@RequestMapping("/backstage/requestserial")
public class RequestSerialController extends BaseController {

	@Resource
	private SysRequestSerialService sysRequestSerialService;
    /**
     * 用户行为数据列表查询
     * @param requestSerial
     * @param request
     * @return
     */
    @RequestMapping(value = "queryRequestSerialList")
    public String queryRequestSerialList(HttpServletRequest request,Model model) throws Exception{
        RequestObject param = new RequestObject(request.getParameterMap());
        Assert.notNull(param, Constant.PARAMS_ERROR);
        if(logger.isDebugEnabled()){
        	logger.info("-------queryRequestSerialList-->:{}", param);
        }
        RequestSerial requestSerial = param.toJavaBean(RequestSerial.class);
        CustomPage<SysRequestSerial> page  = sysRequestSerialService.queryPageList(requestSerial, new PageScope(requestSerial.getPageNum(), requestSerial.getPageSize()));
        if(logger.isDebugEnabled()){
        	logger.info("---queryRequestSerialList--->page:{}" + page);
        }
        model.addAttribute("page", page);
        model.addAttribute("requestSerialList", param);
        return "/backstage/requestSerial/list";
    }

}
