package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.entity.persistent.Friends;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.service.FriendUser;
import com.ycg.ksh.entity.service.FriendsSerach;
import com.ycg.ksh.entity.service.PageScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * 通讯录业务逻辑接口
 *
 * @Author:wangke
 * @Description:
 * @Date:Create in 16:02 2017/12/5
 * @Modified By:
 */
public interface FriendsService {
    final Logger logger = LoggerFactory.getLogger(FriendsService.class);

    /**
     * 查询用户信息
     * @param uKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    User loadUser(Integer uKey) throws ParameterException, BusinessException;
    /**
     * 添加好友
     *
     * @Author：wangke
     * @description：
     * @Date：16:10 2017/12/5
     */
    void saveFriends(User user, Friends friends) throws ParameterException, BusinessException;

    /**
     * 查询好友列表
     *
     * @Author：wangke
     * @description：
     * @Date：17:13 2017/12/5
     */
    Collection<Friends> listFriends(FriendsSerach friendsSerach) throws ParameterException, BusinessException;


    /**
     * 分页查询好友列表
     *
     * @param friendsSerach
     * @param pageScope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @author wangke
     * @date 2018/1/19 14:25
     */
    CustomPage<Friends> pageListFriends(FriendsSerach friendsSerach, PageScope pageScope) throws ParameterException, BusinessException;


    /**
     * 查询好友用户
     * @param friendsSerach
     * @param pageScope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    CustomPage<FriendUser> pageFriendUser(FriendsSerach friendsSerach, PageScope pageScope) throws ParameterException, BusinessException;

    /**
     * 根据ID查询好友详情
     *
     * @Author：wangke
     * @description：
     * @Date：17:19 2017/12/5
     */
    Friends queryFriendsById(Integer id) throws ParameterException, BusinessException;

    /**
     * 编辑好友信息
     *
     * @Author：wangke
     * @description：
     * @Date：17:20 2017/12/5
     */
    void updateFriends(Friends friends) throws ParameterException, BusinessException;

    /**
     * 添加好友
     * <p>
     *
     * @param user   操作人信息
     * @param friend 好友信息
     * @param both   是否是双方添加好友
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-06 10:46:04
     */
    void save(User user, Friends friend, boolean both) throws ParameterException, BusinessException;


    /**
     * 发送短信
     * <p>
     *
     * @param uKey    发送人ID
     * @param mobile  手机号
     * @param context 发送内容
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-06 11:32:34
     */
    void sendSms(Integer uKey, String mobile, String context) throws ParameterException, BusinessException;

    /**
     * 删除好友
     *
     * @Author：wangke
     * @description：
     * @Date：17:17 2017/12/7
     */
    void delteFriends(Integer id);

    /**
     * 校验用户是否存在
     *
     * @Author：wangke
     * @description：
     * @Date：17:29 2017/12/7
     */
    int checkFriendsFlag(Friends friends);

    /**
     * 查询好友用户信息
     * @param mKey 当前用户ID
     * @param fKey 要查询的好友用户ID
     * @return
     */
    FriendUser loadFriendUser(Integer  mKey, Integer fKey);


}
