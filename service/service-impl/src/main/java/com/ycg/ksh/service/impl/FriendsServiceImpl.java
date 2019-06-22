package com.ycg.ksh.service.impl;

import com.github.pagehelper.Page;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.common.validate.Validator;
import com.ycg.ksh.adapter.api.SmsService;
import com.ycg.ksh.constant.CoreConstants;
import com.ycg.ksh.entity.persistent.Friends;
import com.ycg.ksh.entity.persistent.SmsInfo;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.service.AuthorizeUser;
import com.ycg.ksh.entity.service.FriendUser;
import com.ycg.ksh.entity.service.FriendsSerach;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.user.UserContext;
import com.ycg.ksh.service.persistence.FriendsMapper;
import com.ycg.ksh.service.persistence.SmsInfoMapper;
import com.ycg.ksh.service.persistence.UserMapper;
import com.ycg.ksh.service.api.FriendsService;
import com.ycg.ksh.service.observer.UserObserverAdapter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author:wangke
 * @Description:
 * @Date:Create in 16:03 2017/12/5
 * @Modified By:
 */
@Service("ksh.core.service.friendsService")
public class FriendsServiceImpl implements FriendsService, UserObserverAdapter {

    @Resource
    FriendsMapper friendsMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    SmsInfoMapper smsInfoMapper;

    @Resource
    SmsService smsService;

    private static final Pattern REGEX = Pattern.compile("[\\u4E00-\\u9FA5A-Za-z0-9]+");

    private String eliminate(String fullName) {
        if (StringUtils.isNotBlank(fullName)) {
            StringBuilder builder = new StringBuilder();
            Matcher matcher = REGEX.matcher(fullName);
            while (matcher.find()) {
                builder.append(matcher.group());
            }
            return builder.toString();
        }
        return null;
    }

    /**
     * 查询用户信息
     *
     * @param uKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public User loadUser(Integer uKey) throws ParameterException, BusinessException {
        return userMapper.selectByPrimaryKey(uKey);
    }

    /**
     * 查询好友用户信息
     *
     * @param mKey 当前用户ID
     * @param fKey 要查询的好友用户ID
     * @return
     */
    @Override
    public FriendUser loadFriendUser(Integer mKey, Integer fKey) {
        Assert.notBlank(mKey, "当前用户ID不能为空");
        Assert.notBlank(fKey, "要查询的好友用户ID");
        return friendsMapper.getFriendUser(mKey, fKey);
    }

    @Override
    public void saveFriends(User user, Friends friends) throws ParameterException, BusinessException {
        Assert.notBlank(friends.getUserid(), "添加人用户ID不能为空");
        Assert.notBlank(friends.getMobilePhone(), "好友手机号为空");
        friends.setMobilePhone(friends.getMobilePhone().trim());
        if (user.getMobilephone().equals(friends.getMobilePhone())) {
            throw new BusinessException("不能添加自己为好友");
        }
        if (friendsMapper.checkFriendsFlag(friends) > 0) {
            throw new BusinessException("该用户已经是您的好友");
        }
        User other = userMapper.loadUserByMobile(friends.getMobilePhone());
        if (other == null) {
            if (StringUtils.isBlank(friends.getFullName())) {
                friends.setFullName(friends.getMobilePhone());
            }
        } else {
            friends.setPid(other.getId());
            if (StringUtils.isBlank(friends.getFullName())) {
                friends.setFullName(eliminate(other.getUnamezn()));
                if (StringUtils.isBlank(friends.getFullName())) {
                    friends.setFullName(other.getMobilephone());
                }
            }
        }
        friends.setCreateTime(new Date());
        friendsMapper.insert(friends);
    }

    @Override
    public Collection<Friends> listFriends(FriendsSerach friendsSerach) throws ParameterException, BusinessException {
        try {
            return friendsMapper.listFriends(friendsSerach);
        } catch (Exception e) {
            logger.error("listFriends -> friends:{}", friendsSerach, e);
            throw BusinessException.dbException("好友查询异常");
        }
    }

    @Override
    public CustomPage<Friends> pageListFriends(FriendsSerach friendsSerach, PageScope pageScope) throws ParameterException, BusinessException {
        if (pageScope == null) {
            pageScope = PageScope.DEFAULT;
        }
        Page<Friends> page = friendsMapper.listFriends(friendsSerach, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
        return new CustomPage<Friends>(page.getPageNum(), page.getPageSize(), page.getTotal(), page);
    }

    /**
     * 查询好友用户
     *
     * @param friendsSerach
     * @param pageScope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public CustomPage<FriendUser> pageFriendUser(FriendsSerach friendsSerach, PageScope pageScope) throws ParameterException, BusinessException {
        Assert.notNull(friendsSerach, "查询条件不能为空");
        Assert.notBlank(friendsSerach.getUserId(), "操作用户ID不能为空");
        if (pageScope == null) {
            pageScope = PageScope.DEFAULT;
        }
        Page<FriendUser> page = friendsMapper.listFriendUser(friendsSerach, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
        if(CollectionUtils.isNotEmpty(page)){
            for (FriendUser user : page) {
                if(user.getFriendKey() == null || user.getFriendKey() <= 0){
                    user.setUserName(user.getRemarkName());
                    user.setMobile(user.getRemarkMobile());
                }
            }
        }
        return new CustomPage<FriendUser>(page.getPageNum(), page.getPageSize(), page.getTotal(), page);
    }

    @Override
    public Friends queryFriendsById(Integer id) throws ParameterException, BusinessException {
        return friendsMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateFriends(Friends friends) throws ParameterException, BusinessException {
        Assert.notNull(friends, "好友信息不能为空");
        Assert.notBlank(friends.getMobilePhone(), "好友手机号不能为空");
        //判断当前修改的号码是否更新
        Friends exter = friendsMapper.selectByPrimaryKey(friends.getId());
        Assert.notNull(exter, "用户信息不存在");
        if (!friends.getMobilePhone().equals(exter.getMobilePhone())) {
            if (friendsMapper.checkFriendsFlag(friends) > 0) {
                throw new BusinessException("该用户已经是您的好友");
            }
        }
        try {
            User user = userMapper.loadUserByMobile(friends.getMobilePhone());
            if (null != user) {
                friends.setPid(user.getId());
            }
            if (null != friendsMapper.selectByPrimaryKey(friends.getId())) {
                friends.setUpdateTime(new Date());
                friendsMapper.updateByPrimaryKeySelective(friends);
            } else {
                throw new BusinessException("好友信息不存在!");
            }
        } catch (Exception e) {
            logger.error("用户信息更新异常 {}", friends, e);
            throw BusinessException.dbException("用户信息更新异常");
        }
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-06 10:46:42
     * @see com.ycg.ksh.service.api.FriendsService# save(Friends, boolean)
     * <p>
     */
    @Override
    public void save(User user, Friends friend, boolean both) throws ParameterException, BusinessException {
        Assert.notBlank(friend.getUserid(), "添加人用户ID不能为空");
        Assert.notBlank(friend.getPid(), "好友用户ID为空");
        Validator validator = Validator.NORMALWORD;
        if (!validator.verify(friend.getFullName(), false)) {
            throw new ParameterException(friend.getFullName(), "好友备注名称");
        }
        if (friend.getPid() - friend.getUserid() == 0) {
            throw new BusinessException("不能添加自己为好友");
        }
        try {
            User other = userMapper.selectByPrimaryKey(friend.getPid());
            if (other == null) {
                throw new BusinessException("要添加的好友信息不存在");
            }
            //首先检查对方是不是我的好友，如果不是就添加
            Friends temp = new Friends(user.getId(), other.getId());
            if (friendsMapper.checkFriendsFlag(friend) <= 0) {
                temp.setMobilePhone(other.getMobilephone());
                temp.setFullName(friend.getFullName());
                temp.setCreateTime(new Date());
                temp.setUpdateTime(friend.getCreateTime());
                friendsMapper.insert(temp);//添加好友
            }
            if (both) {
                //判断我是不是对方的好友，如果不是就添加
                temp = new Friends(other.getId(), user.getId());
                if (friendsMapper.checkFriendsFlag(temp) <= 0) {
                    temp.setMobilePhone(user.getMobilephone());
                    temp.setFullName(eliminate(user.getUnamezn()));
                    if (StringUtils.isBlank(temp.getFullName())) {
                        temp.setFullName(user.getMobilephone());
                    }
                    temp.setCreateTime(new Date());
                    temp.setUpdateTime(temp.getCreateTime());
                    friendsMapper.insert(temp);//添加好友
                }
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("扫码添加好友异常 -> {} {} ", user, friend, both, e);
            throw BusinessException.dbException("扫码添加好友异常");
        }
    }

    @Override
    public void sendSms(Integer uKey, String mobile, String context) throws ParameterException, BusinessException {
        Assert.notNull(context, "短信信息不能为空");
        Assert.notBlank(mobile, "接收人号码为空");
        Assert.notBlank(uKey, "发送人Id为空");

        Validator validator = Validator.MOBILE;
        if (!validator.verify(mobile)) {
            throw new ParameterException(mobile, "收收人手机号");
        }
        SmsInfo smsInfo = new SmsInfo();
        smsInfo.setContext(context);
        smsInfo.setUserid(uKey);
        smsInfo.setMobilePhone(mobile);
        smsInfo.setSendtime(new Date());
        if (smsInfoMapper.querySendCountById(smsInfo) > 0) {
            throw new BusinessException("今天已经邀请过该用户");
        } else {
            smsService.send(smsInfo.getMobilePhone(), smsInfo.getContext());
            //SmsUtil.send(smsInfo.getMobilePhone(), smsInfo.getRemark());
            smsInfoMapper.insert(smsInfo);
        }
    }

    @Override
    public void delteFriends(Integer id) {
        Assert.notBlank(id, "好友Id为空");
        friendsMapper.deleteByPrimaryKey(id);

    }

    @Override
    public int checkFriendsFlag(Friends friends) {
        return friendsMapper.checkFriendsFlag(friends);
    }

    @Override
    public void notifyUserChange(AuthorizeUser authorizeUser, Integer type, UserContext context) throws BusinessException {
        //logger.info("收到用户登录通知 -> {} type:{}", authorizeUser, type);
        if (CoreConstants.USER_BINDMOBILEPHONE - type == 0) {
            Friends friends = new Friends();
            Example example = new Example(Friends.class);
            example.createCriteria().andEqualTo("mobilePhone", authorizeUser.getMobilephone());
            friends.setPid(authorizeUser.getId());
            friendsMapper.updateByExampleSelective(friends, example);
        }
    }
}
