package com.ycg.ksh.api.user.controller;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/24
 */

import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.service.api.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 活动相关控制层
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/24
 */
@Controller("sys.activity.controller")
@RequestMapping("/sys/activity")
public class ActivityController extends BaseController {

    @Resource
    ActivityService activityService;


    @RequestMapping(value = "/loaction/lottery")
    @ResponseBody
    public JsonResult loactionLottery(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        String lotteryKey = body.get("lotteryKey");
        Assert.notBlank(lotteryKey, "抽奖资格编号不能为空");
        Integer awardType = body.getInteger("awardType");//1:红包，2:积分，3:其他
        Assert.notBlank(awardType, "奖励类型不能为空");
        jsonResult.put("result", activityService.lottery(loadUserKey(request), lotteryKey, awardType));
        return jsonResult;
    }
}
