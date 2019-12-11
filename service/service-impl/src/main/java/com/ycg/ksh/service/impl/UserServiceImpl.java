/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-15 11:16:44
 */
package com.ycg.ksh.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.ycg.ksh.adapter.api.AuthenticateService;
import com.ycg.ksh.adapter.api.ESignService;
import com.ycg.ksh.adapter.api.SmsService;
import com.ycg.ksh.adapter.api.WeChatApiService;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.constant.Directory;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.common.extend.rabbitmq.RabbitMessageListener;
import com.ycg.ksh.common.system.*;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.common.util.UserUtil;
import com.ycg.ksh.common.validate.Validator;
import com.ycg.ksh.constant.*;
import com.ycg.ksh.entity.adapter.esign.Personal;
import com.ycg.ksh.entity.adapter.esign.SealMoulage;
import com.ycg.ksh.entity.adapter.esign.Signer;
import com.ycg.ksh.entity.common.wechat.message.MessageBuilder;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.persistent.user.ResourceChange;
import com.ycg.ksh.entity.persistent.user.UserResource;
import com.ycg.ksh.entity.service.AuthorizeUser;
import com.ycg.ksh.entity.service.ConciseUser;
import com.ycg.ksh.entity.service.TimeCycle;
import com.ycg.ksh.entity.service.enterprise.MergeUserCertified;
import com.ycg.ksh.entity.service.esign.ReceiptSignature;
import com.ycg.ksh.entity.service.user.UserContext;
import com.ycg.ksh.message.InviteCooperationMessage;
import com.ycg.ksh.service.api.CompanyService;
import com.ycg.ksh.service.api.ManagingUsersService;
import com.ycg.ksh.service.api.SupportService;
import com.ycg.ksh.service.api.UserService;
import com.ycg.ksh.service.observer.ActivityObserverAdapter;
import com.ycg.ksh.service.observer.FinanceObserverAdapter;
import com.ycg.ksh.service.observer.ReceiptObserverAdapter;
import com.ycg.ksh.service.observer.UserObserverAdapter;
import com.ycg.ksh.service.persistence.*;
import com.ycg.ksh.service.persistence.user.ResourceChangeMapper;
import com.ycg.ksh.service.persistence.user.UserResourceMapper;
import com.ycg.ksh.service.support.assist.DrawSeal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * 用户相关逻辑实现
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-15 11:16:44
 */
@Service("ksh.core.service.userService")
public class UserServiceImpl implements UserService, ReceiptObserverAdapter, ActivityObserverAdapter, FinanceObserverAdapter, RabbitMessageListener {

    protected final Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final String CACHE_PREFIX_USER = "sys.user.";


    @Resource
    CacheManager cacheManager;
    @Resource
    UserMapper userMapper;
    @Resource
    SysRoleMapper roleMapper;
    @Resource
    UserCommonlyMapper userCommonlyMapper;
    @Resource
    UserLegalizeMapper legalizeMapper;
    @Resource
    UserTrackMapper userTrackMapper;
    @Resource
    AuthenticateService authenticateService;
    @Resource
    ESignService esignService;
    @Resource
    SupportService supportService;
    @Resource
    CompanyService companyService;
    @Resource
    ManagingUsersService managingUsersService;
    @Resource
    PersonalSealMapper personalSealMapper;
    @Resource
    UserResourceMapper userResourceMapper;
    @Resource
    ResourceChangeMapper resourceChangeMapper;
    @Resource
    WeChatApiService wechatService;
    @Resource
    SmsService smsService;

    @Autowired(required = false)
    Collection<UserObserverAdapter> observers;

    /*
    enum CacheType {
        KEY, OPENID, UNIONID;
    }

    protected LocalCacheManager<User> userCache = new LocalCacheManager<User>(User.class, new CacheAdapter() {
        @Override
        public CacheManager getCacheManager() {
            return cacheManager;
        }

        @Override
        public Object persistence(Serializable... parameters) throws ParameterException, BusinessException {
            CacheType type = (CacheType) parameters[0];
            if (CacheType.KEY == type) {
                return userMapper.selectByPrimaryKey(parameters[1]);
            }
            if (CacheType.OPENID == type) {
                return userMapper.loadUserByOpenId(String.valueOf(parameters[1]));
            }
            if (CacheType.UNIONID == type) {
                return userMapper.loadUserByUnionId(String.valueOf(parameters[1]));
            }
            return null;
        }
    });
    */

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-06 13:58:58
     * @see com.ycg.ksh.service.api.UserService#validateMobile(java.lang.String)
     * <p>
     */
    @Override
    public boolean validateMobile(String mobile) throws ParameterException, BusinessException {
        Assert.notBlank(mobile, "要验证的手机号不能为空");
        return userMapper.countByMobile(mobile) > 0;
    }

    /**
     * 获取用户简要信息
     *
     * @param userKey 用户编号
     *
     * @auther: baymax
     * @date: 2018/8/23 14:43
     */
    @Override
    public ConciseUser getConciseUser(Integer userKey) throws ParameterException, BusinessException {
        User user = userMapper.selectByPrimaryKey(userKey);
        if(user != null){
            return new ConciseUser(user);
        }
        return null;
    }

    /**
     * @param userKey 用户编号
     * @return 用户信息
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-15 11:16:44
     * @see com.ycg.ksh.service.api.UserService#getUserByKey(java.lang.Integer)
     * <p>
     */
    @Override
    public User getUserByKey(Integer userKey) throws ParameterException, BusinessException {
        try {
            return userMapper.selectByPrimaryKey(userKey);
        } catch (Exception e) {
            logger.error("用户信息查询异常:{}", userKey, e);
            throw BusinessException.dbException("用户信息查询异常");
        }
    }

    @Override
    public User getUserByWechat(String unionId, String openId) throws ParameterException, BusinessException {
        try {
            User user = null;
            if (StringUtils.isNotBlank(unionId)) {
                user = userMapper.loadUserByUnionId(unionId);
            }
            if (user == null && StringUtils.isNotBlank(openId)) {
                user = userMapper.loadUserByOpenId(openId);
            }
            return user;
        } catch (Exception e) {
            logger.error("用户信息查询异常:{}", openId, e);
            throw BusinessException.dbException("用户信息查询异常");
        }
    }

    /**
     * @param openId 微信openId
     * @return 用户信息
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-15 11:16:44
     * @see com.ycg.ksh.service.api.UserService#getUserByOpenId(java.lang.String)
     * <p>
     */
    @Override
    public User getUserByOpenId(String openId) throws ParameterException, BusinessException {
        try {
            return userMapper.loadUserByOpenId(openId);
        } catch (Exception e) {
            logger.error("用户信息查询异常:{}", openId, e);
            throw BusinessException.dbException("用户信息查询异常");
        }
    }

    /**
     * @param unionId 微信unionId
     * @return 用户信息
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-15 11:16:44
     * @see com.ycg.ksh.service.api.UserService#getUserByUnionId(java.lang.String)
     * <p>
     */
    @Override
    public User getUserByUnionId(String unionId) throws ParameterException, BusinessException {
        try {
            return userMapper.loadUserByUnionId(unionId);
        } catch (Exception e) {
            logger.error("用户信息查询异常:{}", unionId, e);
            throw BusinessException.dbException("用户信息查询异常");
        }
    }

    @Override
    public void logout(Integer userKey) throws ParameterException, BusinessException {
        try {
            noticeUserObserverAdapter(get(userKey), CoreConstants.USER_REFRESH, null);
            cacheManager.delete(CACHE_PREFIX_USER + userKey);
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("用户登陆异常: userKey: {}", userKey, e);
        }
    }

    @Override
    public AuthorizeUser get(Integer userKey) throws ParameterException, BusinessException {
        try {
            return loadAuthorizeUser(userKey, true);
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("获取用户信息异常: userKey: {}", userKey, e);
            throw new BusinessException("获取用户信息异常");
        }
    }

    @Override
    public AuthorizeUser register(UserContext context) throws ParameterException, BusinessException {
        int event = CoreConstants.USER_REGISTER;
        User exister = getUserByMobile(context.getMobilephone());
        if(exister != null){
            if(StringUtils.isNotEmpty(exister.getUsername()) || StringUtils.isNotEmpty(exister.getOpenid())){
                throw new BusinessException("手机号["+ context.getMobilephone() +"]已经注册过");
            }else{
                updateUser(exister, context);
            }
        }else{
            exister = saveUser(context);
        }
        AuthorizeUser authorizeUser = loadAuthorizeUser(exister.getId(), false);
        if (authorizeUser != null) {
            authorizeUser.setAdminKey(context.getAdminKey());
            noticeUserObserverAdapter(authorizeUser, event, context);
        }
        logger.info("register: {}", authorizeUser);
        return authorizeUser;
    }

    @Override
    public AuthorizeUser login(UserContext context) throws ParameterException, BusinessException {
        User user = null;
        int event = CoreConstants.USER_LOGIN;
        if(context.getUserKey() == null || context.getUserKey() <= 0){
            if(StringUtils.isNotBlank(context.getMobilephone())){
                user =  getUserByMobile(context.getMobilephone());
            }else{
                user = getUserByWechat(context.getUnionid(), context.getOpenid());
            }
            if(user == null && context.isCreate()){
                user = saveUser(context);
                event = CoreConstants.USER_REGISTER;
            }
            if(user != null){
                context.setUserKey(user.getId());
            }
        }else{
            user = getUserByKey(context.getUserKey());
        }
        if(user != null){
        	ManagingUsers managingUsers = null;
            //djq update set userType
            if(context.getAdminKey() != null) {
            	managingUsers = managingUsersService.queryUserById(context.getAdminKey());
            	if(managingUsers != null && managingUsers.getUserType() == 3) {
            		user.setUserType(1);//设置为货主
            	}
            }
            boolean cache = updateUser(user, context);
            if(context.getAdminKey() != null && context.getAdminKey() > 0){
                managingUsersService.bindUser(context.getUserKey(), context.getAdminKey());
            }
            AuthorizeUser authorizeUser = loadAuthorizeUser(user.getId(), cache);
            if (authorizeUser != null) {
                authorizeUser.setAdminKey(context.getAdminKey());
                noticeUserObserverAdapter(authorizeUser, event, context);
            }
            logger.info("login: {}", authorizeUser);
            return authorizeUser;
        }
        return null;
    }

    private User saveUser(UserContext context){
        User user = new User();
        user.setPassword(Optional.ofNullable(context.getSubscribe()).map(Object::toString).orElse(StringUtils.EMPTY));
        user.setUname(context.getUname());
        user.setUsername(context.getOpenid());
        user.setHeadImg(context.getHeadImg());
        user.setOpenid(context.getUnionid());
        user.setMobilephone(context.getMobilephone());
        user.setCreatetime(new Date());
        user.setUpdatetime(user.getCreatetime());
        user.setMobilephone(context.getMobilephone());
        user.setUserType(0);
        userMapper.insert(user);

        UserCommonly commonly = new UserCommonly(user.getId(), CoreConstants.USER_CATEGORY_CONVEY);
        commonly.setCommonly(SystemUtils.get(SystemKey.SYSTEM_MOBILE_COMMONLY));//旧版
        commonly.setCommonlyKeys(SystemUtils.get(SystemKey.SYSTEM_MOBILE_COMMONLYKEYS));//新版非司机
        commonly.setDriverCommonlyKeys(SystemUtils.get(SystemKey.SYSTEM_MOBILE_DRIVERCOMMONLYKEYS));//新版司机
        commonly.setIdentityKey(CoreConstants.USER_CATEGORY_SHIPPER);
        userCommonlyMapper.insert(commonly);

        return user;
    }

    private boolean updateUser(User user, UserContext context){
        context.setSubscribe(Optional.ofNullable(context.getSubscribe()).orElse(StringUtils.EMPTY));
        int update = 0;
        if(!StringUtils.equalsIgnoreCase(user.getPassword(), context.getSubscribe())){
            user.setPassword(context.getSubscribe());
            update++;
        }
        if(!StringUtils.equalsIgnoreCase(user.getUsername(), context.getOpenid())){
            user.setUsername(context.getOpenid());
            update++;
        }
        if(!StringUtils.equalsIgnoreCase(user.getOpenid(), context.getUnionid())){
            user.setOpenid(context.getUnionid());
            update++;
        }
        if(!StringUtils.equalsIgnoreCase(user.getMobilephone(), context.getMobilephone())){
            user.setMobilephone(context.getMobilephone());
            update++;
        }
        if(!StringUtils.equalsIgnoreCase(user.getHeadImg(), context.getHeadImg())){
            user.setHeadImg(context.getHeadImg());
            update++;
        }
        if(user.getUserType()>0){
        	update++;
        }
        if(update > 0){
            userMapper.updateByPrimaryKeySelective(user);
        }
        return update > 0;
    }

    private void refreshUserObserverAdapter(Integer userKey) {
        noticeUserObserverAdapter(get(userKey), CoreConstants.USER_REFRESH, null);
    }

    private void noticeUserObserverAdapter(AuthorizeUser authorizeUser, Integer eventType, UserContext context) {
        if (CollectionUtils.isNotEmpty(observers)) {
            for (UserObserverAdapter userObserverAdapter : observers) {
                userObserverAdapter.notifyUserChange(authorizeUser, eventType, context);
            }
        }
    }

    private AuthorizeUser loadAuthorizeUser(Integer userId) throws BusinessException {
        AuthorizeUser authorizeUser = new AuthorizeUser(getUserByKey(userId));
        authorizeUser.setToken(SecurityTokenUtil.createToken(String.valueOf(authorizeUser.getId())));
        UserCommonly commonly = loadUserCommonlyByUserKey(userId);
        if(commonly != null){
            authorizeUser.setIdentityKey(commonly.getIdentityKey());
        }
        if (CollectionUtils.isNotEmpty(observers)) {
            for (UserObserverAdapter userObserverAdapter : observers) {
                userObserverAdapter.initializeUser(authorizeUser, CoreConstants.USER_LOGIN);
            }
            for (UserObserverAdapter userObserverAdapter : observers) {
                userObserverAdapter.initializeUser(authorizeUser, CoreConstants.USER_AUTHORITY);
            }
        }
        return authorizeUser;
    }

    private AuthorizeUser loadAuthorizeUser(Integer userId, boolean cache) throws BusinessException {
        if (cache) {
            return cacheManager.get(CACHE_PREFIX_USER + userId, 30L, () -> loadAuthorizeUser(userId));
        } else {
            AuthorizeUser authorizeUser = loadAuthorizeUser(userId);
            if(authorizeUser != null){
                cacheManager.set(CACHE_PREFIX_USER + userId, authorizeUser, 30L, TimeUnit.MINUTES);
            }
            return authorizeUser;
        }
    }

    /**
     * @param token
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public AuthorizeUser loginByToken(String token) throws ParameterException, BusinessException {
        try {
            String userId = SecurityTokenUtil.getUserIdByToken(token);
            if (StringUtils.isBlank(userId)) {
                throw new BusinessException("无效的Token [" + token + "]");
            }
            return loadAuthorizeUser(Integer.parseInt(userId), true);
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Token获取用户信息异常: {}", token, e);
            throw new BusinessException("Token获取用户信息异常");
        }
    }

    /*@Override
    public AuthorizeUser save(User user) throws ParameterException, BusinessException {
        Assert.notNull(user, "用户信息不能为空");
        Assert.notBlank(user.getOpenid(), "UnionId不能为空");
        Assert.notBlank(user.getUsername(), "OpenId不能为空");
        try {
            AuthorizeUser authorizeUser = null;
            User exister = getUserByWechat(user.getOpenid(), user.getUsername());
            if (exister == null) {
                user.setCreatetime(new Date());
                user.setUpdatetime(user.getCreatetime());
                userMapper.insert(user);

                UserCommonly commonly = new UserCommonly(user.getId(), CoreConstants.USER_CATEGORY_CONVEY);
                commonly.setCommonly(SystemUtils.get(SystemKey.SYSTEM_MOBILE_COMMONLY));//旧版
                commonly.setCommonlyKeys(SystemUtils.get(SystemKey.SYSTEM_MOBILE_COMMONLYKEYS));//新版非司机
                commonly.setDriverCommonlyKeys(SystemUtils.get(SystemKey.SYSTEM_MOBILE_DRIVERCOMMONLYKEYS));//新版司机
                userCommonlyMapper.insert(commonly);

                authorizeUser = loadAuthorizeUser(user.getId(), false);

                if (CollectionUtils.isNotEmpty(observers)) {
                    noticeUserObserverAdapter(authorizeUser, CoreConstants.USER_REGISTER, null);
                }
            } else {
                if (StringUtils.isNotBlank(user.getOpenid())) {
                    exister.setOpenid(user.getOpenid());
                }
                if (StringUtils.isNotBlank(user.getUsername())) {
                    exister.setUsername(user.getUsername());
                }
                exister.setPassword(user.getPassword());
                exister.setHeadImg(user.getHeadImg());
                authorizeUser = update(exister);
            }
            return authorizeUser;
        } catch (Exception e) {
            logger.error("save -> user:{}", user, e);
            throw BusinessException.dbException("用户信息新增异常");
        }
    }*/

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-06 13:50:30
     * @see com.ycg.ksh.service.api.UserService# bindMobile(java.lang.Integer, java.lang.String)
     * <p>
     */
    @Override
    public AuthorizeUser bindMobile(Integer uKey, String phone, String name, Integer identity) throws ParameterException, BusinessException {
        Assert.notNull(uKey, "用户编号不能为空");
        Assert.notBlank(phone, "要绑定的手机号不能为空");
        if (userMapper.countByMobile(phone) > 0) {
            throw new BusinessException("手机号[" + phone + "]已经绑定了另一个账号!");
        }
        try {
            User user = new User();
            user.setId(uKey);
            user.setMobilephone(phone);
            if (StringUtils.isNotBlank(name)) {
                user.setUname(UserUtil.encodeName(name));
            }
            AuthorizeUser authorizeUser = update(user);
            if (authorizeUser != null) {
                noticeUserObserverAdapter(authorizeUser, CoreConstants.USER_BINDMOBILEPHONE, null);
            }
            if (identity != null && identity - 1 == 0) {
                userCommonlyMapper.updateByPrimaryKey(new UserCommonly(uKey, identity));
            }
            return authorizeUser;
        } catch (Exception e) {
            logger.error("bind mobile -> uKey:{} phone:{} name:{} identity:{}", uKey, phone, name, identity, e);
            throw BusinessException.dbException("用户绑定手机号异常");
        }
    }

    /**
     * @param user 要更新的用户信息
     * @return 更新后的用户信息
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-15 11:16:44
     * @see com.ycg.ksh.service.api.UserService#update(User)
     * <p>
     */
    @Override
    public AuthorizeUser update(User user) throws ParameterException, BusinessException {
        Assert.notNull(user, "用户信息不能为空");
        Assert.notBlank(user.getId(), "用户主键不能为空");
        try {
            user.setUpdatetime(new Date());
            userMapper.updateByPrimaryKeySelective(user);
            return loadAuthorizeUser(user.getId(), false);
        } catch (Exception e) {
            logger.error("update -> user:{}", user, e);
            throw BusinessException.dbException("用户信息更新异常");
        }
    }

    /**
     * @param user 查询条件
     * @return 满足条件的用户信息
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-15 11:16:44
     * @see com.ycg.ksh.service.api.UserService#listUserBySomething(User)
     * <p>
     */
    @Override
    public Collection<User> listUserBySomething(User user) throws ParameterException, BusinessException {
        try {
            Example example = new Example(User.class);
            Criteria criteria = example.createCriteria();
            if (StringUtils.isNotBlank(user.getOpenid())) {
                criteria.andEqualTo("openid", user.getOpenid());
            }
            if (StringUtils.isNotBlank(user.getUsername())) {
                criteria.andEqualTo("username", user.getUsername());
            }
            if (StringUtils.isNotBlank(user.getMobilephone())) {
                criteria.andEqualTo("mobilephone", user.getMobilephone());
            }
            example.orderBy("createtime").desc();
            return userMapper.selectByExample(example);
        } catch (Exception e) {
            logger.error("listUserBySomething -> user:{}", user, e);
            throw BusinessException.dbException("用户信息查询异常");
        }
    }

    /**
     * @param track 要保存的轨迹信息
     * @return 保存后的轨迹信息
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-15 11:16:44
     * @see com.ycg.ksh.service.api.UserService#save(UserTrack)
     * <p>
     */
    @Override
    public UserTrack save(UserTrack track) throws ParameterException, BusinessException {
        Assert.notNull(track, "用户信息不能为空");
        Assert.notNull(track.getLatitude(), "经纬度不能为空");
        Assert.notNull(track.getLongitude(), "经纬度不能为空");
        try {
            track.setModifyTime(new Date());
            userTrackMapper.insert(track);
            return track;
        } catch (Exception e) {
            logger.error("save -> track:{}", track, e);
            throw BusinessException.dbException("用户轨迹信息新增异常");
        }
    }

    /**
     * @param userKey   用户编号
     * @param timeCycle 时间段
     * @return 满足条件的用户轨迹信息
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-15 11:16:44
     * @see com.ycg.ksh.service.api.UserService#listTrackByUserKey(java.lang.Integer, TimeCycle)
     * <p>
     */
    @Override
    public Collection<UserTrack> listTrackByUserKey(Integer userKey, TimeCycle timeCycle)
            throws ParameterException, BusinessException {
        try {
            Example example = new Example(UserTrack.class);
            Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId", userKey);
            if (timeCycle != null) {
                criteria.andBetween("modifyTime", timeCycle.getStime(), timeCycle.getEtime());
            }
            example.orderBy("modifyTime").desc();
            return userTrackMapper.selectByExample(example);
        } catch (Exception e) {
            logger.error("listTrackByUserKey -> userKey:{}, timeCycle:{}", userKey, timeCycle, e);
            throw BusinessException.dbException("用户轨迹信息查询异常");
        }
    }

    @Override
    public List<User> getUserListByMobile(String mobile) throws ParameterException, BusinessException {
        Example example = new Example(User.class);
        if (StringUtils.isNotBlank(mobile)) {
            example.createCriteria().andEqualTo("mobilephone", mobile);
        } else {
            example.createCriteria().andIsNotNull("mobilephone");
        }
        example.orderBy("createtime").asc();
        List<User> list = userMapper.selectByExample(example);
        return list;
    }

    @Override
    public User getUserByMobile(String mobile) throws ParameterException, BusinessException {
        Assert.notBlank(mobile, Constant.PARAMS_ERROR);
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("mobilephone", mobile);
        example.orderBy("createtime").asc();
        List<User> list = userMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void modifySubscribe(String openId, String subscribe) throws ParameterException, BusinessException {
        try {
            userMapper.modifySubscribe(openId, subscribe);
        } catch (Exception e) {
            logger.error("modifySubscribe -> openId:{}, subscribe:{}", openId, subscribe, e);
            throw BusinessException.dbException("更新用户关注状态异常");
        }
    }

    /**
     * 查询用户常用功能设置
     *
     * @param userKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public UserCommonly loadUserCommonlyByUserKey(Integer userKey) throws ParameterException, BusinessException {
        Assert.notNull(userKey, "用户编号不能为空");
        try {
            return userCommonlyMapper.selectByPrimaryKey(userKey);
        } catch (Exception e) {
            logger.error("load UserCommonly By UserKey -> userKey:{}", userKey, e);
            throw BusinessException.dbException("获取用户常用功能设置异常");
        }
    }

    @Override
    public Integer loadUserIdentityKey(Integer userKey) throws ParameterException, BusinessException {
        return Optional.ofNullable(loadUserCommonlyByUserKey(userKey)).map(UserCommonly::getIdentityKey).orElse(CoreConstants.USER_CATEGORY_DRIVER);
    }

    /**
     * 更新常用功能(新增或者删除)
     *
     * @param userKey 用户编号
     * @param codes
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void modifyCommonlyKeys(Integer userKey, Collection<Integer> codes) throws ParameterException, BusinessException {
        Assert.notNull(userKey, "用户编号不能为空");
        Assert.notEmpty(codes, "至少选择一个常用功能");
        try {
            UserCommonly commonly = loadUserCommonlyByUserKey(userKey);
            Collection<String> commonlies = commonly.commonlies(true);
            if (!commonlies.isEmpty()) {
                commonlies.clear();
            }
            for (Integer code : codes) {
                String codeString = String.valueOf(code);
                if (!commonlies.contains(codeString)) {
                    commonlies.add(codeString);
                }
            }
            commonly.setUserKey(userKey);
            if(commonly.isDriver()){
                commonly.setDriverCommonlyKeys(StringUtils.join(commonlies, "#"));
            }else{
                commonly.setCommonlyKeys(StringUtils.join(commonlies, "#"));
            }
            userCommonlyMapper.updateByPrimaryKey(commonly);
            //清除缓存
            refreshUserObserverAdapter(userKey);
        } catch (Exception e) {
            logger.error("modify Commonly By UserKey -> userKey:{} codes:{}", userKey, codes, e);
            throw BusinessException.dbException("更新用户常用功能异常");
        }
    }

    /**
     * 更新常用功能(新增或者删除)
     *
     * @param userKey 用户编号
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void modifyCommonlyByUserKey(Integer userKey, Collection<Integer> codes) throws ParameterException, BusinessException {
        Assert.notNull(userKey, "用户编号不能为空");
        Assert.notEmpty(codes, "至少选择一个常用功能");
        try {
            UserCommonly commonly = loadUserCommonlyByUserKey(userKey);
            Collection<String> commonlies = commonly.commonlies(false);
            if (!commonlies.isEmpty()) {
                commonlies.clear();
            }
            for (Integer code : codes) {
                String codeString = String.valueOf(code);
                if (!commonlies.contains(codeString)) {
                    commonlies.add(codeString);
                }
            }
            commonly.setUserKey(userKey);
            commonly.setCommonly(StringUtils.join(commonlies, "#"));
            userCommonlyMapper.updateByPrimaryKey(commonly);
            //清除缓存
            refreshUserObserverAdapter(userKey);
        } catch (Exception e) {
            logger.error("modify Commonly By UserKey -> userKey:{} codes:{}", userKey, codes, e);
            throw BusinessException.dbException("更新用户常用功能异常");
        }
    }

    /**
     * 获取用户认证信息
     *
     * @param userKey
     * @return
     */
    @Override
    public UserLegalize getUserLegalize(Integer userKey) throws ParameterException, BusinessException {
        Assert.notNull(userKey, "用户编号不能为空");
        try {
            return legalizeMapper.selectByPrimaryKey(userKey);
        } catch (Exception e) {
            logger.error("get user legalize -> userKey:{}", userKey, e);
            throw BusinessException.dbException("获取用户认证信息异常");
        }
    }

    /**
     * 是否已经实名认证
     *
     * @param uKey
     * @return
     */
    @Override
    public boolean isLegalize(Integer uKey) {
        UserLegalize legalize = getUserLegalize(uKey);
        if (legalize == null) {
            return false;
        }
        if (legalize.getFettle() == 0) {
            return false;
        }
        return true;
    }


    @Override
    public void bindPersonalAuth(UserLegalize legalize) throws ParameterException, BusinessException {
        Assert.notNull(legalize, "认证信息为空");
        Assert.notBlank(legalize.getId(), "认证用户编号不能为空");
        Assert.notBlank(legalize.getMobilePhone(), "手机号为空");
        Assert.notBlank(legalize.getIdCardNo(), "证件号码为空");
        Assert.notBlank(legalize.getName(), "姓名为空");
        Validator validator = Validator.IDCARD;
        if (!validator.verify(legalize.getIdCardNo())) {
            throw new BusinessException(validator.getMessage("证件号码"));
        }
        validator = Validator.MOBILE;
        if (!validator.verify(legalize.getMobilePhone())) {
            throw new BusinessException(validator.getMessage("手机号"));
        }
        validator = Validator.CHINESENAME;
        if (!validator.verify(legalize.getName())) {
            throw new BusinessException(validator.getMessage("姓名"));
        }
        UserLegalize exister = getUserLegalize(legalize.getId());
        if (exister != null) {
            if (!StringUtils.equals(legalize.getIdCardNo(), exister.getIdCardNo())) {
                throw new BusinessException("已经认证过, 身份证号码不能变更");
            }
            legalize.setFettle(exister.getFettle());
        } else {
            if (legalizeMapper.checkIdCardCount(legalize.getIdCardNo()) > 0) {
                throw new BusinessException("该身份证已被实名认证");
            }
            legalize.setFettle(LegalizeFettle.NOTVERIFY.getCode());
        }
        legalize.setFettle(legalize(legalize).getCode());
        legalize.setLegalizeTime(new Date());
        if (null != exister) {
            legalizeMapper.updateByPrimaryKeySelective(legalize);
        } else {
            legalizeMapper.insertSelective(legalize);
        }
    }

    @Override
    public void bindSealType(PersonalSeal personalSeal) throws ParameterException, BusinessException {
        Assert.notNull(personalSeal, "印章信息不能为空");
        Assert.notBlank(personalSeal.getUserId(), "用户编号不能为空");
        Assert.notBlank(personalSeal.getSealStyle(), "个人签章样式不能为空");
        UserLegalize legalize = getUserLegalize(personalSeal.getUserId());
        if (legalize == null || legalize.getFettle() == 0) {
            throw new BusinessException("当前用户未通过实名认证");
        }
        try {
            personalSeal.setCreateTime(new Date());
            personalSeal.setId(Globallys.nextKey());
            personalSeal.setSealType(Constant.PERSONAL_CHAPTER_NUM);
            SealStyle sealStyle = SealStyle.convert(personalSeal.getSealStyle());
            personalSeal.setSealStyle(sealStyle.getCode());

            BufferedImage buffered = DrawSeal.rectangle(legalize.getName(), sealStyle.getFontName());
            if (buffered != null) {
                FileEntity fileEntity = new FileEntity();
                fileEntity.setDirectory(SystemUtils.fileRootPath());
                fileEntity.setSubDir(Directory.SEAL.getDir());
                fileEntity = DrawSeal.saveImage(fileEntity, buffered);
                personalSeal.setSealImgPath(fileEntity.persistence());
            }

            personalSealMapper.insertSelective(personalSeal);
            SealMoulage sealMoulage = SealMoulage.personal("local#legalize#" + personalSeal.getId(), "B1", personalSeal.getSealStyle());
            if (SystemUtils.esignEnable()) {
                SignAssociate associate = supportService.getSignAssociate(ObjectType.USERLEGALIZE, personalSeal.getUserId());
                if (associate == null) {
                    throw new BusinessException("当前用户未做实名认证");
                }
                sealMoulage.setSignerId(associate.getThirdObjectKey());
                sealMoulage = esignService.buildSeal(Signer.YUNHETONG, sealMoulage);
                if (sealMoulage == null || StringUtils.isBlank(sealMoulage.getRequestKey())) {
                    throw new BusinessException("个人印章创建异常");
                }
                supportService.saveSignAssociate(new SignAssociate(ObjectType.USERSEAL, personalSeal.getId(), Signer.YUNHETONG, sealMoulage.getRequestKey(), personalSeal.getUserId()));
            }
            supportService.saveOperateNote(personalSeal.getUserId(), OperateType.ESIGN, personalSeal.getId(), ESignEventType.SEALPERSONAL, sealMoulage.toString());
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("个人印章创建异常");
        }
    }

    private LegalizeFettle legalize(UserLegalize legalize) {
        LegalizeFettle fettle = LegalizeFettle.convert(legalize.getFettle());
        if (fettle.isNotVerify()) {
            Personal personal = Personal.verify(legalize.getName(), legalize.getIdCardNo(), legalize.getBrankCardNo(), legalize.getMobilePhone());
            if (SystemUtils.esignEnable()) {
                authenticateService.legalize(personal);
            }
            fettle = LegalizeFettle.VERIFY;
            supportService.saveOperateNote(legalize.getId(), OperateType.ESIGN, legalize.getId(), ESignEventType.PERSONALVERIFY, personal.toString());
        }
        if (fettle.isVerify()) {
            if (SystemUtils.esignEnable()) {
                SignAssociate signAssociate = supportService.getSignAssociate(ObjectType.USERLEGALIZE, legalize.getId());
                if (signAssociate == null) {
                    Personal personal = esignService.buildPersonal(Signer.YUNHETONG, Personal.create(legalize.getName(), legalize.getIdCardNo(), legalize.getMobilePhone()));
                    if (personal != null) {
                        supportService.saveSignAssociate(new SignAssociate(ObjectType.USERLEGALIZE, legalize.getId(), Signer.YUNHETONG, personal.getRequestKey(), legalize.getId()));
                    }
                    supportService.saveOperateNote(legalize.getId(), OperateType.ESIGN, legalize.getId(), ESignEventType.PERSONALCREATE, personal.toString());
                } else {
                    Personal personal = Personal.modify(signAssociate.getThirdObjectKey(), legalize.getName(), legalize.getMobilePhone());
                    esignService.buildPersonal(Signer.YUNHETONG, personal);
                    supportService.saveOperateNote(legalize.getId(), OperateType.ESIGN, legalize.getId(), ESignEventType.PERSONALMODIFY, personal.toString());
                }
            }
            fettle = LegalizeFettle.CREATE;
        }
        return fettle;
    }


    /**
     * 拆线呢所有角色信息
     *
     * @return
     */
    @Override
    public Collection<SysRole> listSysRoles() {
        Example example = new Example(SysRole.class);
        example.createCriteria().andEqualTo("roleFettle", 1);
        return roleMapper.selectByExample(example);
    }

    /**
     * 查询个人印章信息
     */
    @Override
    public PersonalSeal getPersonalSealByKey(Long sealKey) throws ParameterException, BusinessException {
        //Assert.notBlank(sealKey, "印章编号不能为空");
        try {
            return personalSealMapper.selectByPrimaryKey(sealKey);
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("查询个人印章异常 {}", sealKey, e);
            throw BusinessException.dbException("查询个人印章异常");
        }
    }

    /**
     * 查询个人印章信息
     *
     * @param uKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Collection<PersonalSeal> listPersonalSeal(Integer uKey) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "用户编号不能为空");
        Example example = new Example(PersonalSeal.class);
        example.createCriteria().andEqualTo("userId", uKey);
        example.orderBy("createTime");
        return personalSealMapper.selectByExample(example);
    }

    @Override
    public MergeUserCertified getUserCertified(Integer userKey) throws ParameterException, BusinessException {
        Assert.notBlank(userKey, "用户编号不能为空");
        try {
            MergeUserCertified userCertified = null;
            UserLegalize legalize = getUserLegalize(userKey);
            if (legalize != null) {
                userCertified = MergeUserCertified.buildCertified(legalize);
            } else {
                userCertified = new MergeUserCertified();
            }
            userCertified.setEnterprises(companyService.getCompanySeal(userKey));
            userCertified.setPersonals(getPersonalSeals(userKey));
            return userCertified;
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("userService - > getUserCertified: userKey:{}", userKey, e);
            throw BusinessException.dbException("获取个人印章和授权印章异常");
        }
    }

    public Collection<PersonalSeal> getPersonalSeals(Integer userKey) {
        Example example = new Example(PersonalSeal.class);
        example.createCriteria().andEqualTo("userId", userKey);
        return personalSealMapper.selectByExample(example);
    }


    /**
     * 通知回单签署
     *
     * @param signature 签署信息
     * @param exception 异常信息
     * @throws BusinessException
     */
    @Override
    public void notifySignReceipt(ReceiptEventType eventType, ReceiptSignature signature, String exception) throws BusinessException {
        if (eventType.isValidate()) {//签署准备
            signature.setLegalize(getUserLegalize(signature.getUserId()));
            if (null == signature.getLegalize() || signature.getLegalize().getFettle() == 0) {
                throw new BusinessException("个人实名认证尚未通过");
            }
            /*
            if(personalSealMapper.selectCount(new PersonalSeal(signature.getUserId())) <= 0){
                throw new BusinessException("您尚未设置个人签章，无法完成签署，请先至微信公众号完成签章设置。");
            }
            if(signature.getPersonalSeal() != null && signature.getPersonalSeal() > 0){
                signature.setUserSeal(personalSealMapper.selectByPrimaryKey(signature.getPersonalSeal()));
                if(null == signature.getUserSeal()){
                    throw new BusinessException("获取个人印章信息异常");
                }
            }
            */
        }
    }

    /**
     * 查询用户资源信息
     *
     * @param uKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public UserResource loadResource(Integer uKey) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, Constant.UKEY_NOTNULL_INFO);
        try {
            return userResourceMapper.selectByPrimaryKey(uKey);
        } catch (Exception e) {
            throw BusinessException.dbException("查询用户资源信息异常");
        }
    }

    /**
     * 查询用户资源变更明细，按照记录时间倒序排序
     *
     * @param uKey    用户编号
     * @param resType 资源类型
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Collection<ResourceChange> listResourceChange(Integer uKey, ResType resType) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, Constant.UKEY_NOTNULL_INFO);
        Assert.notNull(resType, "资源类型不能为空");
        Example example = new Example(ResourceChange.class);
        try {
            Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId", uKey);
            if (resType.isMoney() || resType.isIntegral()) {
                criteria.andEqualTo("resType", resType.getCode());
            }
            return resourceChangeMapper.selectByExample(example);
        } catch (Exception e) {
            throw BusinessException.dbException("查询用户资源变更明细异常");
        }
    }

    /**
     * 通知奖励
     *
     * @param uKey      用户编号
     * @param awardType 奖励类型
     * @param value     奖励的资源数值
     * @throws BusinessException 业务逻辑异常
     */
    @Override
    public void notifyAwardSomething(Integer uKey, Integer awardType, Long value) throws BusinessException {
        if (CoreConstants.LOTTERY_AWARD_RED_ENVELOPE - awardType != 0 && CoreConstants.LOTTERY_AWARD_INTEGRAL - awardType != 0) {
            return;//不是资源
        }
        //变更用户资源
        //value 为正数时是增加，负数表示减少
        //1.变更用户资源
        //2.记录资源变更明细
        Assert.notBlank(uKey, Constant.UKEY_NOTNULL_INFO);
        Assert.notBlank(awardType, "奖励类型不能为空");
        Assert.notBlank(value, "奖励金额不能为空");
        try {
            //User user = userMapper.selectByPrimaryKey(uKey);
            UserResource userResource = userResourceMapper.selectByPrimaryKey(uKey);
            if (null != userResource) {
                if (CoreConstants.LOTTERY_AWARD_RED_ENVELOPE - awardType == 0) {//红包
                    userResource.setMoney(userResource.getMoney() + value);
                }
                if (CoreConstants.LOTTERY_AWARD_INTEGRAL - awardType == 0) {//积分
                    userResource.setIntegral(userResource.getIntegral() + value);
                }
                userResourceMapper.updateByPrimaryKeySelective(userResource);
            } else {
                if (CoreConstants.LOTTERY_AWARD_RED_ENVELOPE - awardType == 0) {//红包
                    userResource = new UserResource(uKey, 0L, value);
                }
                if (CoreConstants.LOTTERY_AWARD_INTEGRAL - awardType == 0) {//积分
                    userResource = new UserResource(uKey, value, 0L);
                }
                userResourceMapper.insertSelective(userResource);
            }
            resourceChangeMapper.insertSelective(new ResourceChange(Globallys.nextKey(), uKey, awardType, 1, value, new Date()));
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw BusinessException.dbException("通知奖励异常");
        }
    }

    /**
     * 财务变化通知
     *
     * @param financeType
     * @param ownerKey
     * @param changeValue
     * @throws BusinessException
     */
    @Override
    public void notifyFinanceChange(FinanceType financeType, Serializable ownerKey, Long changeValue) throws BusinessException {
        Assert.notNull(ownerKey, Constant.UKEY_NOTNULL_INFO);
        Assert.notZero(changeValue, "金额有误");
        try {
            //变更用户资源
            if (FinanceType.WALLET == financeType) {
                //ownerKey 为用户编号
                //changeValue 为正数时是增加，负数表示减少
                //1.减少时，判断资源是否足够，不够抛异常
                //2.变更用户资源
                //3.记录资源变更明细
                UserResource userResource = userResourceMapper.selectByPrimaryKey(ownerKey);
                if (userResource == null) throw new BusinessException("未找到该用户资源");
                //用户金额必须不小于300元才可以体现
                if (userResource.getMoney() < 30000) throw new BusinessException("金额未达到提现要求");
                if (userResource.getMoney() <= 0 || userResource.getMoney() < Math.abs(changeValue))
                    throw new BusinessException("资源金额不足");
                userResource.setMoney(userResource.getMoney() + changeValue);
                if (userResourceMapper.updateByPrimaryKeySelective(userResource) > 0) {
                    resourceChangeMapper.insertSelective(new ResourceChange(Globallys.nextKey(), userResource.getUserId(), ResType.MONEY.getCode(), 2, changeValue, new Date()));
                }
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw BusinessException.dbException("提现异常");
        }
    }

    /**
     * 切换身份
     *
     * @param userKey 用户编号
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public AuthorizeUser modifyIdentity(Integer userKey, Integer identityKey) throws ParameterException, BusinessException {
        Assert.notBlank(userKey, "用户编号不能为空");
        Assert.notBlank(identityKey, "用户身份标识不能为空");
        //更新 UserCommonly 中的 identityKey 字段
        try {
            if(Stream.of(CoreConstants.USER_CATEGORY_DRIVER, CoreConstants.USER_CATEGORY_SHIPPER, CoreConstants.USER_CATEGORY_CONVEY, CoreConstants.USER_CATEGORY_RECEIVE).noneMatch(c -> c - identityKey == 0)){
                throw new ParameterException(identityKey, "身份标识不合法");
            }
            UserCommonly userCommonly = userCommonlyMapper.selectByPrimaryKey(userKey);
            if (userCommonly == null) {
                throw new BusinessException("数据查询异常");
            }
            userCommonly.setIdentityKey(identityKey);
            userCommonlyMapper.updateByPrimaryKeySelective(userCommonly);

            refreshUserObserverAdapter(userKey);

            return loadAuthorizeUser(userKey, false);
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.info("用户切换身份异常 userKey:{}", userKey);
            throw BusinessException.dbException("用户切换身份异常");
        }
    }

    @Override
    public boolean handleMessage(String messageType, String messageKey, Object object) throws BusinessException {
        if(StringUtils.equalsIgnoreCase(InviteCooperationMessage.class.getName(), messageType)){
            try{
                InviteCooperationMessage inviteMessage = (InviteCooperationMessage) object;
                Company company = companyService.getCompanyByKey(inviteMessage.getCompanyKey());
                CompanyEmployee employee = companyService.getCompanyEmployee(inviteMessage.getOperatorKey());
                if(company != null && employee != null){
                    User euser = get(employee.getEmployeeId());
                    User user = getUserByMobile(inviteMessage.getDriverPhone());
                    String redirect = null;
                    if(user != null ){
                        String token = SecurityTokenUtil.createToken(user.getId() +"");
                        redirect = SystemUtils.buildUrl(SystemUtils.getCallBackDomain(),  "/special/redirect?inviteKey="+ inviteMessage.getInviteKey() +"&token="+ token);
                        if(StringUtils.isNotBlank(user.getUsername())){
                            try {
                                //String href = wechatService.long2short(FrontUtils.inviteaskpage(inviteMessage.getInviteKey(), token));
                                String href = FrontUtils.inviteaskpage(inviteMessage.getInviteKey(), token);
                                logger.debug("用户已注册,发送公众号消息 {}", href);
                                String message = "尊敬的"+ inviteMessage.getDriverName() +",您好！"+ company.getCompanyName() + employee.getEmployeeName() + "想邀请您成为他的长期合作司机,征求您的同意, <a href='"+ href +"'>点击这里处理</a> "+ employee.getEmployeeName() +"联系电话："+ euser.getMobilephone();
                                wechatService.sendMessage(MessageBuilder.build().textMessage(user.getUsername(), message));
                            } catch (Exception e) {
                                logger.error("发送微信消息 {}", object, e);
                            }
                        }
                    }else{
                        redirect = SystemUtils.buildUrl(SystemUtils.getCallBackDomain(),  "/special/redirect?inviteKey="+ inviteMessage.getInviteKey());
                    }
                    //发短信
                    String href = wechatService.long2short(redirect);
                    logger.debug("发送短信 {} ---- {}", redirect, href);
                    String message = "尊敬的"+ inviteMessage.getDriverName() +",您好！\r\n"+ company.getCompanyName() + employee.getEmployeeName() + "想邀请您成为他的长期合作司机,征求您的同意~" + href + " , "+employee.getEmployeeName() +"\r\n系电话："+ euser.getMobilephone() ;
                    smsService.sendmsg(inviteMessage.getDriverPhone(), message);
                }
                return true;
            }catch (Exception e){
                logger.error("发送邀请消息异常 {}", object, e);
            }
        }
        return false;
    }
}
