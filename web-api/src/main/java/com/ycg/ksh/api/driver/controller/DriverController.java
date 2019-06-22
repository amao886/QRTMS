package com.ycg.ksh.api.driver.controller;

import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.core.driver.application.DriverApplicationService;
import com.ycg.ksh.core.driver.application.dto.CompanyDriverDto;
import com.ycg.ksh.core.driver.application.dto.DriverAwaitInfoDto;
import com.ycg.ksh.core.driver.application.dto.DriverDto;
import com.ycg.ksh.core.driver.application.dto.InviteAskDto;
import com.ycg.ksh.entity.persistent.CompanyEmployee;
import com.ycg.ksh.entity.service.PageScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 司机管理控制器
 *
 * @author: wangke
 * @create: 2018-12-04 11:26
 **/
@Controller("enterprise.driver.controller")
@RequestMapping("/driver")
public class DriverController extends BaseController {

    @Resource
    DriverApplicationService applicationService;


    /**
     * 完善司机资料
     *
     * @return
     * @Author: wangke
     * @Date: 2018/12/4
     */
    @RequestMapping(value = "/personal/register")
    @ResponseBody
    public JsonResult register(@RequestBody DriverDto driverDto, HttpServletRequest request) throws Exception {
        Assert.notBlank(driverDto.getName(), "司机姓名不能为空");
        Assert.notBlank(driverDto.getPhone(), "司机手机号码不能为空");
        Assert.notEmpty(driverDto.getCars(), "车辆信息不能为空");
        Assert.notEmpty(driverDto.getRoutes(), "路线信息不能为空");
        applicationService.register(loadUserKey(request), driverDto);
        return JsonResult.SUCCESS;
    }

    /**
     * 完善司机资料
     *
     * @return
     * @Author: wangke
     * @Date: 2018/12/4
     */
    @RequestMapping(value = "/personal/modify")
    @ResponseBody
    public JsonResult modifyDriverInfo(@RequestBody DriverDto driverDto, HttpServletRequest request) throws Exception {
        Assert.notBlank(driverDto.getDriverKey(), "司机编号不能为空");
        Assert.notBlank(driverDto.getName(), "司机姓名不能为空");
        Assert.notBlank(driverDto.getPhone(), "司机手机号码不能为空");
        Assert.notEmpty(driverDto.getCars(), "车辆信息不能为空");
        Assert.notEmpty(driverDto.getRoutes(), "路线信息不能为空");
        applicationService.modify(driverDto);
        return JsonResult.SUCCESS;
    }

    /**
     * 查看司机资料
     *
     * @return
     * @Author: wangke
     * @Date: 2018/12/4
     */
    @RequestMapping(value = "/personal/info")
    @ResponseBody
    public JsonResult driverInfo(HttpServletRequest request) {
        Long driverKey = Long.valueOf(loadUserKey(request));
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("driver", applicationService.getDriver(driverKey));
        return jsonResult;
    }

    /**
     * 发布等货信息
     *
     * @param driverAwaitInfoDto
     * @return
     * @Author: wangke
     * @Date: 2018/12/4
     */
    @RequestMapping(value = "/personal/releaseWaitInfo")
    @ResponseBody
    public JsonResult releaseWaitInfo(@RequestBody DriverAwaitInfoDto driverAwaitInfoDto, HttpServletRequest request) throws Exception {
        Assert.notNull(driverAwaitInfoDto.getStartTime(), "开始等货时间不能为空");
        Assert.notBlank(driverAwaitInfoDto.getStart(), "起点不能为空");
        Assert.notBlank(driverAwaitInfoDto.getEnd(), "终点不能为空");
        Assert.notBlank(driverAwaitInfoDto.getLicense(), "车牌不能为空");
        Assert.notBlank(driverAwaitInfoDto.getCarType(), "车型不能为空");
        Assert.notNull(driverAwaitInfoDto.getLength(), "车长不能为空");
        Assert.notBlank(driverAwaitInfoDto.getRouteType(),"路线类型不能为空");
        driverAwaitInfoDto.setDriverKey(Long.valueOf(loadUserKey(request)));
        applicationService.releaseWaitInfo(driverAwaitInfoDto);
        return JsonResult.SUCCESS;
    }

    /**
     * 我要找货查询
     *
     * @param body
     * @return
     * @Author: wangke
     * @Date: 2018/12/4
     */
    @RequestMapping(value = "/personal/search")
    @ResponseBody
    public JsonResult searchAwaitInfoByDriver(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        LocalDateTime releaseTime = body.getLocalDateTime("releaseTime");
        Long driverKey = Long.valueOf(loadUserKey(request));
        String star = body.get("start");
        PageScope scope = new PageScope(body.getInteger("num"), body.getInteger("size"));
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("page", applicationService.searchAwaitInfoByDriver(driverKey, releaseTime, star, scope));
        return jsonResult;
    }

    /**
     * 我要找车
     *
     * @param body
     * @param request
     * @return
     * @Author: wangke
     * @Date: 2018/12/4
     */
    @RequestMapping(value = "/company/search")
    @ResponseBody
    public JsonResult searchAwaitInfoByCompany(@RequestBody RequestObject body, HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        Integer carType = body.getInteger("carType");//车型
        Float carLength = body.getFloat("carLength");//车长
        String address = body.get("address");//地点
        PageScope scope = new PageScope(body.getInteger("num"), body.getInteger("size"));
        jsonResult.put("page",
                applicationService.searchAwaitInfoByCompany(loadCompanyKey(request), carType, carLength, address, scope));
        return jsonResult;
    }

    /**
     * 企业查询司机资料
     *
     * @param driverKey
     * @return
     * @Author: wangke
     * @Date: 2018/12/5
     */
    @RequestMapping(value = "/company/getDriver/{driverKey}")
    @ResponseBody
    public JsonResult getDriverByCompany(@PathVariable Long driverKey) {
        Assert.notBlank(driverKey, "司机编号不能为空");
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("driver", applicationService.getCompanyDriver(driverKey));
        return jsonResult;
    }

    /**
     * 企业邀请司机
     *
     * @param body
     * @return
     * @Author: wangke
     * @Date: 2018/12/5
     */
    @RequestMapping(value = "/company/inviteAsk")
    @ResponseBody
    public JsonResult inviteAsk(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        InviteAskDto inviteAskDto = body.toJavaBean(InviteAskDto.class);
        Assert.notBlank(inviteAskDto.getDriverName(), "司机姓名不能为空");
        Assert.notBlank(inviteAskDto.getDriverPhone(), "司机手机号码不能为空");

        CompanyEmployee employee = loadCompanyEmployee(request);

        inviteAskDto.setInviteUserKey(employee.getEmployeeId());
        inviteAskDto.setInviteUserName(employee.getEmployeeName());

        inviteAskDto.setCompanyKey(loadCompanyKey(request));
        applicationService.inviteAsk(inviteAskDto);
        return JsonResult.SUCCESS;
    }

    /**
     * 处理企业邀请
     *
     * @param body
     * @return
     * @Author: wangke
     * @Date: 2018/12/5
     */
    @RequestMapping(value = "/company/handleInviteAsk")
    @ResponseBody
    public JsonResult handleInviteAsk(@RequestBody RequestObject body, HttpServletRequest request) {
        Long inviteKey = body.getLong("inviteKey");
        Assert.notZero(inviteKey, "邀请信息已过期");
        Boolean result = body.getBoolean("result");
        Assert.notNull(result, "请选择是否同意");
        applicationService.handleInviteAsk(Long.valueOf(loadUserKey(request)), inviteKey, result);
        return JsonResult.SUCCESS;
    }

    /**
     * 司机管理列表查询
     *
     * @return
     * @Author: wangke
     * @Date: 2018/12/5
     */
    @RequestMapping(value = "/company/driver")
    @ResponseBody
    public JsonResult searchCompanyDriver(@RequestBody RequestObject body, HttpServletRequest request) {
        Integer status = body.getInteger("status");
        String likeString = body.get("likeString");
        PageScope scope = new PageScope(body.getInteger("num"), body.getInteger("size"));
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("page", applicationService.searchCompanyDriver(loadCompanyKey(request), status, likeString, scope));
        return jsonResult;
    }

    /**
     * 企业司机完善资料
     *
     * @return
     * @Author: wangke
     * @Date: 2018/12/5
     */
    @RequestMapping(value = "/company/modify")
    @ResponseBody
    public JsonResult companyModify(@RequestBody CompanyDriverDto companyDriverDto) {
        Assert.notBlank(companyDriverDto.getIdentify(), "司机编号不能为空");
        Assert.notBlank(companyDriverDto.getName(), "司机姓名不能为空");
        Assert.notBlank(companyDriverDto.getPhone(), "司机手机号码不能为空");
        Assert.notEmpty(companyDriverDto.getCars(), "车辆信息不能为空");
        Assert.notEmpty(companyDriverDto.getRoutes(), "路线信息不能为空");
        applicationService.modify(companyDriverDto);
        return JsonResult.SUCCESS;
    }
}
