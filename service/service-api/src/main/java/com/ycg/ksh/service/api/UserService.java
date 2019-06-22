/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-07 14:52:29
 */
package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.constant.ResType;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.persistent.user.ResourceChange;
import com.ycg.ksh.entity.persistent.user.UserResource;
import com.ycg.ksh.entity.service.AuthorizeUser;
import com.ycg.ksh.entity.service.ConciseUser;
import com.ycg.ksh.entity.service.TimeCycle;
import com.ycg.ksh.entity.service.enterprise.MergeUserCertified;
import com.ycg.ksh.entity.service.user.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * 用户相关逻辑接口
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-15 10:52:11
 */
public interface UserService {

    final Logger logger = LoggerFactory.getLogger(UserService.class);

    /**
     * 退出登陆
     * @param userKey 用户编号
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    void logout(Integer userKey) throws ParameterException, BusinessException;

    /**
     * 获取用户信息，有缓存
     * @param userKey 用户编号
     *
     * @return
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    AuthorizeUser get(Integer userKey) throws ParameterException, BusinessException;


    /**
     * 用户登陆
     * @param context
     *
     * @return
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    AuthorizeUser login(UserContext context) throws ParameterException, BusinessException;

    /**
     * 用户注册
     * @param context
     *
     * @return
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    AuthorizeUser register(UserContext context) throws ParameterException, BusinessException;

    /**
     * @param token
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public AuthorizeUser loginByToken(String token) throws ParameterException, BusinessException;

    /**
     * 验证手机号
     * <p>
     *
     * @param mobile 要验证的手机号
     * @return true:可以使用,false:已经使用了不能再使用
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-06 13:58:01
     */
    boolean validateMobile(String mobile) throws ParameterException, BusinessException;

    /**
     *
     * 获取用户简要信息
     *
     * @param userKey 用户编号
     * @auther: baymax
     * @date: 2018/8/23 14:43
     */
    ConciseUser getConciseUser(Integer userKey) throws ParameterException, BusinessException;
    /**
     * 根据用户编号查询用户信息
     * <p>
     *
     * @param userKey 用户编号
     * @return 用户信息
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-15 10:56:52
     */
    User getUserByKey(Integer userKey) throws ParameterException, BusinessException;


    /**
     * 获取用户信息,
     * @param unionId
     * @param openId
     *
     * @return
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    User getUserByWechat(String unionId, String openId) throws ParameterException, BusinessException;

    /**
     * 根据openId查询用户信息
     * <p>
     *
     * @param openId 微信openId
     * @return 用户信息
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-15 10:56:55
     */
    User getUserByOpenId(String openId) throws ParameterException, BusinessException;

    /**
     * 根据unionId查询用户信息
     * <p>
     *
     * @param unionId 微信unionId
     * @return 用户信息
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-15 10:56:58
     */
    User getUserByUnionId(String unionId) throws ParameterException, BusinessException;

    /**
     * 更新用户信息
     * <p>
     *
     * @param user 要更新的用户信息
     * @return 更新后的用户信息
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-15 10:57:03
     */
    AuthorizeUser update(User user) throws ParameterException, BusinessException;

    /**
     * 绑定手机号
     * <p>
     *
     * @param uKey 用户ID
     * @param phone
     * @param name
     * @param identity
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @return
     */
    AuthorizeUser bindMobile(Integer uKey, String phone, String name, Integer identity) throws ParameterException, BusinessException;

    /**
     * 查询用户信息
     * <p>
     *
     * @param user 查询条件
     * @return 满足条件的用户信息
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-15 10:57:06
     */
    Collection<User> listUserBySomething(User user) throws ParameterException, BusinessException;


    /**
     * 新增用户轨迹
     * <p>
     *
     * @param track 要保存的轨迹信息
     * @return 保存后的轨迹信息
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-15 11:12:54
     */
    UserTrack save(UserTrack track) throws ParameterException, BusinessException;

    /**
     * 查询用户坐标
     * <p>
     *
     * @param userKey   用户编号
     * @param timeCycle 要查询的时间段，空置表示查询所有时间段的轨迹信息
     * @return 满足条件的用户轨迹信息
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-15 11:13:06
     */
    Collection<UserTrack> listTrackByUserKey(Integer userKey, TimeCycle timeCycle) throws ParameterException, BusinessException;

    /**
     * 根据手机号查用户信息 LIST
     *
     * @Author：wangke
     * @description：
     * @Date：17:02 2018/1/4
     */
    List<User> getUserListByMobile(String mobile) throws ParameterException, BusinessException;

    /**
     * 根据手机号查询单条用户信息
     *
     * @param mobile
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    User getUserByMobile(String mobile) throws ParameterException, BusinessException;

    /**
     * 根据用户ID更新用户关注状态
     *
     * @param openId    用户微信编号
     * @param subscribe 关注状态
     * @throws ParameterException
     * @throws BusinessException
     */
    void modifySubscribe(String openId, String subscribe) throws ParameterException, BusinessException;

    /**
     * 查询用户常用功能设置
     *
     * @param userKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    UserCommonly loadUserCommonlyByUserKey(Integer userKey) throws ParameterException, BusinessException;

    /**
     * 获取用户身份标识
     * @param userKey 用户编号
     *
     * @return
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    Integer loadUserIdentityKey(Integer userKey) throws ParameterException, BusinessException;

    /**
     * 更新常用功能(新增或者删除)
     *
     * @param userKey 用户编号
     * @param codes
     * @throws ParameterException
     * @throws BusinessException
     */
    void modifyCommonlyKeys(Integer userKey, Collection<Integer> codes) throws ParameterException, BusinessException;

    /**
     * 更新常用功能(新增或者删除)
     *
     * @param userKey 用户编号
     * @param codes
     * @throws ParameterException
     * @throws BusinessException
     */
    void modifyCommonlyByUserKey(Integer userKey, Collection<Integer> codes) throws ParameterException, BusinessException;

    /**
     * 切换身份
     *
     * @param userKey 用户编号
     * @param identityKey
     * @throws ParameterException
     * @throws BusinessException
     */
    AuthorizeUser modifyIdentity(Integer userKey, Integer identityKey) throws ParameterException, BusinessException;


    /**
     * 是否已经实名认证
     *
     * @param uKey
     * @return
     */
    boolean isLegalize(Integer uKey);

    /**
     * 获取用户认证信息
     *
     * @param userKey
     * @return
     */
    UserLegalize getUserLegalize(Integer userKey);

    /**
     * 个人实名认证
     *
     * @param userLegalize
     * @throws ParameterException
     * @throws BusinessException
     * @author wangke
     * @date 2018/5/7 11:50
     */
    void bindPersonalAuth(UserLegalize userLegalize) throws ParameterException, BusinessException;

    /**
     * 个人签章样式
     *
     * @param personalSeal
     * @throws ParameterException
     * @throws BusinessException
     */
    void bindSealType(PersonalSeal personalSeal) throws ParameterException, BusinessException;

    /**
     * 拆线呢所有角色信息
     *
     * @return
     */
    Collection<SysRole> listSysRoles();

    /**
     * 查询个人印章信息
     * @param sealKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    PersonalSeal getPersonalSealByKey(Long sealKey) throws ParameterException, BusinessException;

    /**
     * 查询个人印章信息
     * @param uKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<PersonalSeal> listPersonalSeal(Integer uKey) throws ParameterException, BusinessException;

    /**
     * 查询当前用户印章和授权印章
     *
     * @param userKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    MergeUserCertified getUserCertified(Integer userKey) throws ParameterException, BusinessException;

    /**
     * 查询用户资源信息
     * @param uKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    UserResource loadResource(Integer uKey) throws ParameterException, BusinessException;

    /**
     * 查询用户资源变更明细，按照记录时间倒序排序
     * @param uKey 用户编号
     * @param resType 资源类型
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<ResourceChange> listResourceChange(Integer uKey, ResType resType) throws ParameterException, BusinessException;
}
