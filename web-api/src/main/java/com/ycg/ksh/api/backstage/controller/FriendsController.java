package com.ycg.ksh.api.backstage.controller;

import com.alibaba.fastjson.JSON;
import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.entity.persistent.Friends;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.service.FriendsSerach;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.service.api.FriendsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 好友控制器
 *
 * @author wangke
 * @create 2018-01-19 9:12
 **/

@Controller("backstage.friend.controller")
@RequestMapping("/backstage/friend")
public class FriendsController extends BaseController {

    @Resource
    FriendsService friendsService;

    @RequestMapping(value = "select")
    @ResponseBody
    public JsonResult select(@RequestBody(required = false) RequestObject requestObject, HttpServletRequest request) throws Exception {
        logger.info("-----------loadFriend------------{}", requestObject);
        User u = RequestUitl.getUserInfo(request);
        if (requestObject == null) {
            requestObject = new RequestObject();
        }
        requestObject.put("userId", u.getId());
        Integer pageSize = requestObject.getInteger("size");
        Integer pageNum = requestObject.getInteger("num");

        JsonResult jsonResult = new JsonResult();

        FriendsSerach serach = requestObject.toJavaBean(FriendsSerach.class);
        //serach.setRegistered(true);
        jsonResult.put("result", friendsService.pageFriendUser(serach, new PageScope(pageNum, pageSize)));
        return jsonResult;
    }

    /**
     * 跳转到好友列表页
     *
     * @param request
     * @param model
     * @return
     * @author wangke
     * @date 2018/1/19 9:19
     */
    @RequestMapping(value = "search")
    public String loadFriend(HttpServletRequest request, Model model) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        logger.info("-----------loadFriend------------{}", body);
        User u = RequestUitl.getUserInfo(request);
        body.put("userId", u.getId());
        Integer pageSize = body.getInteger("size");
        Integer pageNum = body.getInteger("num");
        CustomPage<Friends> list = friendsService.pageListFriends(body.toJavaBean(FriendsSerach.class), new PageScope(pageNum, pageSize));
        model.addAttribute("page", list);
        model.addAttribute("search", body);
        return "/backstage/friends/manage";
    }

    /**
     * 添加好友
     *
     * @param requestObject
     * @return JsonResult
     * @author wangke
     * @date 2018/1/19 10:09
     */
    @RequestMapping(value = "addFriends")
    @ResponseBody
    public JsonResult addFriends(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        Assert.notNull(requestObject, Constant.PARAMS_ERROR);
        User u = RequestUitl.getUserInfo(request);
        requestObject.put("userid", u.getId());
        friendsService.saveFriends(u, requestObject.toJavaBean(Friends.class));
        return JsonResult.SUCCESS;
    }


    /**
     * 修改好友
     *
     * @param requestObject
     * @param response
     * @return
     * @author wangke
     * @date 2018/1/19 10:12
     */
    @RequestMapping(value = "updateFriends")
    @ResponseBody
    public JsonResult updateFriends(@RequestBody RequestObject requestObject, HttpServletResponse response, HttpServletRequest request) throws Exception {
        Assert.notNull(requestObject, Constant.PARAMS_ERROR);
        User u = RequestUitl.getUserInfo(request);
        requestObject.put("userid", u.getId());
        friendsService.updateFriends(requestObject.toJavaBean(Friends.class));
        return JsonResult.SUCCESS;
    }

    /**
     * 批量删除好友
     *
     * @param requestObject
     * @param response
     * @return
     * @author wangke
     * @date 2018/1/19 10:14
     */
    @RequestMapping(value = "deleFriends")
    @ResponseBody
    public JsonResult deleFriends(@RequestBody RequestObject requestObject, HttpServletResponse response) {
        Assert.notNull(requestObject, Constant.PARAMS_ERROR);
        List deleIds = JSON.parseArray(requestObject.get("deleIds"));
        for (int i = 0; i < deleIds.size(); i++) {
            friendsService.delteFriends(Integer.valueOf(deleIds.get(i).toString()));
        }
        return JsonResult.SUCCESS;
    }


    /**
     * 查询好友详情
     *
     * @param friendsId
     * @return
     * @author wangke
     * @date 2018/1/22 8:56
     */
    @RequestMapping(value = "getByFriendsId/{friendsId}")
    @ResponseBody
    public JsonResult getByFriendsId(@PathVariable Integer friendsId) {
        Assert.notBlank(friendsId, Constant.PARAMS_ERROR);
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("friends", friendsService.queryFriendsById(friendsId));
        return jsonResult;
    }

}
