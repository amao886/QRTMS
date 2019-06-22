package com.ycg.ksh.api.mobile.controller;

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.PinyinUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.Friends;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.service.FriendsSerach;
import com.ycg.ksh.service.api.FriendsService;
import com.ycg.ksh.service.api.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 通讯录控制器
 *
 * @Author:wangke
 * @Description:
 * @Date:Create in 16:56 2017/12/5
 * @Modified By:
 */
@Controller("mobile.friend.controller")
@RequestMapping("/mobile/friends")
public class FriendsController extends BaseController {

    @Resource
    FriendsService friendsService;

    @Resource
    UserService userService;

    /**
     * TODO 扫描添加好友
     * <p>
     *
     * @param friends
     * @param request
     * @return
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-06 15:30:04
     */
    @RequestMapping(value = "/scan/add/friend")
    @ResponseBody
    public JsonResult scanAddFriends(@RequestBody Friends friend, HttpServletRequest request) {
        Assert.notNull(friend, Constant.PARAMS_ERROR);
        Assert.notBlank(friend.getPid(), "要添加的好友编号不能为空");
        Assert.notBlank(friend.getFullName(), "好友备注不能为空");
        User u = RequestUitl.getUserInfo(request);
        friend.setUserid(u.getId());
        friendsService.save(u, friend, true);
        return JsonResult.SUCCESS;
    }

    /**
     * TODO 添加好友
     * <p>
     *
     * @param friends
     * @param request
     * @return
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-06 15:30:25
     */
    @RequestMapping(value = "/add/friend")
    @ResponseBody
    public JsonResult addFriends(@RequestBody Friends friends, HttpServletRequest request) {
        Assert.notNull(friends, Constant.PARAMS_ERROR);
        Assert.notBlank(friends.getMobilePhone(), "要添加的好友编号不能为空");
        User u = RequestUitl.getUserInfo(request);
        friends.setUserid(u.getId());
        friendsService.saveFriends(u, friends);
        return JsonResult.SUCCESS;
    }

    @RequestMapping(value = "/update/friend")
    @ResponseBody
    public JsonResult updateFriends(@RequestBody Friends friends) {
        logger.info("-----------updateFriends------------friends: {}", friends);
        Assert.notNull(friends, Constant.PARAMS_ERROR);
        friendsService.updateFriends(friends);
        return JsonResult.SUCCESS;
    }

    /**
     * TODO 查询当前用户所有好友
     * <p>
     *
     * @param params
     * @param request
     * @return
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-06 16:06:04
     */
    @RequestMapping(value = "/query/friends")
    @ResponseBody
    public JsonResult queryMyFriends(@RequestBody(required = false) RequestObject params, HttpServletRequest request) {
        logger.info("-----------queryMyFriends------------params: {}", params);
        if (params == null) {
            params = new RequestObject();
        }
        JsonResult jsonResult = new JsonResult();
        User user = RequestUitl.getUserInfo(request);
        FriendsSerach friendsSerach = new FriendsSerach();
        friendsSerach.setUserId(user.getId());
        if (params.containsKey("likeString")) {
            friendsSerach.setLikeString(params.get("likeString"));
        }
        Collection<Friends> listFriends = friendsService.listFriends(friendsSerach);

        jsonResult.put("results", mergeByInitials(listFriends));
        return jsonResult;
    }

    /**
     * TODO 好友信息查询
     * <p>
     *
     * @param friendkey
     * @return
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-06 17:15:48
     */
    @RequestMapping(value = "/query/friend/info/{friendkey}")
    @ResponseBody
    public JsonResult queryFrindInfo(@PathVariable Integer friendkey) {
        logger.info("-----------queryFrindInfo------------friendkey: {}", friendkey);
        Assert.notBlank(friendkey, "好友主键为空");
        JsonResult jsonResult = new JsonResult();
        Friends friend = friendsService.queryFriendsById(friendkey);
        jsonResult.put("result", friend);
        logger.info("-----------queryFrindInfo------------friend: {}", friend);
        return jsonResult;
    }

    /**
     * TODO 好友短信发送
     * <p>
     *
     * @param request
     * @return
     * @throws Exception
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-06 18:03:08
     */
    @RequestMapping(value = "/send/msg")
    @ResponseBody
    public JsonResult sendMsg(@RequestBody RequestObject object, HttpServletRequest request) {
        logger.info("sendMsg -> {}", object);
        User user = RequestUitl.getUserInfo(request);
        String mobile = object.get("mobilePhone");
        String context = object.get("remark");
        friendsService.sendSms(user.getId(), mobile, context);
        return new JsonResult(true, "短信发送成功");
    }

    /**
     * TODO 数据组合
     * <p>
     *
     * @param collection
     * @return
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-07 14:23:04
     */
    private Map<String, List<Friends>> mergeByInitials(Collection<Friends> collection) {
        if (CollectionUtils.isNotEmpty(collection)) {
            Map<String, List<Friends>> map = new TreeMap<String, List<Friends>>(new Comparator<String>() {
                public int compare(String obj1, String obj2) {
                    return obj1.compareTo(obj2);// 升序排序
                }
            });
            for (Friends friends : collection) {
                String initialsKey = PinyinUtils.getPinYinHeadChar(StringUtils.isNotBlank(friends.getFullName()) ? friends.getFullName().substring(0, 1) : "").toUpperCase();
                List<Friends> items = map.get(initialsKey);
                if (items == null) {
                    items = new ArrayList<Friends>();
                }
                items.add(friends);
                map.put(initialsKey, items);
            }
            return map;
        }
        return Collections.emptyMap();
    }

    @RequestMapping(value = "/delete/friend/{friendkey}")
    @ResponseBody
    public JsonResult deleteFriend(@PathVariable Integer friendkey) {
        logger.info("-----------deleteFriend------------friendkey: {}", friendkey);
        friendsService.delteFriends(friendkey);
        return JsonResult.SUCCESS;
    }
}
