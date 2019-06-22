package com.ycg.ksh.api.user.controller;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/24
 */

import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.constant.ResType;
import com.ycg.ksh.entity.persistent.UserLegalize;
import com.ycg.ksh.entity.persistent.user.ResourceChange;
import com.ycg.ksh.entity.persistent.user.UserResource;
import com.ycg.ksh.service.api.FinanceService;
import com.ycg.ksh.service.api.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户控制层
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/24
 */
@Controller("sys.user.controller")
@RequestMapping("/sys/user")
public class UserController extends BaseController {

    @Resource
    UserService userService;
    @Resource
    FinanceService financeService;

    /**
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/wallet/detail")
    @ResponseBody
    public JsonResult wallet(HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        UserResource resource = userService.loadResource(loadUserKey(request));
        if(resource != null){
            jsonResult.put("changes", userService.listResourceChange(resource.getUserId(), ResType.MONEY));
        }
        jsonResult.put("money", resource.getMoney());
        return jsonResult;
    }

    /**
     * 提现申请
     * @param requestObject
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/wallet/extract")
    @ResponseBody
    public JsonResult extract(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        Integer extractValue = requestObject.getInteger("extractValue");//单位分
        Assert.notBlank(extractValue, "提现金额不能为空");
        financeService.applyMoneyExtract(loadUserKey(request), extractValue);
        return jsonResult;
    }


    /**
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/integral/detail")
    @ResponseBody
    public JsonResult integral(HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        UserResource resource = userService.loadResource(loadUserKey(request));
        if(resource != null){
            jsonResult.put("changes", userService.listResourceChange(resource.getUserId(), ResType.INTEGRAL));
        }
        jsonResult.put("integral", resource.getIntegral());
        return jsonResult;
    }
    
    /**
     * 我的金额及积分
     * TODO Add description
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-07-25 15:23:19
     * @param uKey
     * @param resType
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/resource/{resType}")
    @ResponseBody
    public JsonResult userResource(@PathVariable Integer resType, HttpServletRequest request) throws Exception {
        Assert.notBlank(resType, "资源类型不能为空");
    	JsonResult jsonResult = new JsonResult();
        ResType res = null;
        if(resType==1){
        	res = ResType.MONEY;
        }else if(resType == 2){
        	res = ResType.INTEGRAL; 
        }
        Integer uKey = loadUserKey(request);
        UserResource userResource = userService.loadResource(uKey);
        UserLegalize userLegalize = userService.getUserLegalize(uKey);
        Collection<ResourceChange> listResource = userService.listResourceChange(uKey, res);
        jsonResult.put("userResource", userResource == null ? new UserResource(uKey,0L,0L) : userResource);
        jsonResult.put("listResource", listResource);
        jsonResult.put("userLegalize", userLegalize);
        return jsonResult;
    }

}
