package com.ycg.ksh.service.persistence;

import com.github.pagehelper.Page;
import com.ycg.ksh.entity.persistent.Friends;
import com.ycg.ksh.entity.service.FriendUser;
import com.ycg.ksh.entity.service.FriendsSerach;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

public interface FriendsMapper extends Mapper<Friends> {
    /**
     * 校验用户是否存在
     *
     * @Author：wangke
     * @description：
     * @Date：17:29 2017/12/7
     */
    int checkFriendsFlag(Friends friends);


    /**
     * 查询好友列表
     *
     * @Author：wangke
     * @description：
     * @Date：17:13 2017/12/5
     */
    Collection<Friends> listFriends(FriendsSerach friendsSerach);

    /**
     * 分页查询好友列表
     *
     * @param friendsSerach
     * @param bounds
     * @return
     * @author wangke
     * @date 2018/1/19 14:27
     */
    Page<Friends> listFriends(FriendsSerach friendsSerach, RowBounds bounds);

    /**
     * 根据用户ID查询好友用户信息
     * @param uKey  要查询的好友用户ID
     * @param cKey  当前用户ID
     * @return
     */
    FriendUser getFriendUser(Integer uKey, Integer cKey);

    /**
     * 查询好友用户信息
     * @param friendsSerach
     * @return
     */
    Collection<FriendUser> listFriendUser(FriendsSerach serach);

    /**
     * 分页查询好友用户信息
     * @param serach
     * @param bounds
     * @return
     */
    Page<FriendUser> listFriendUser(FriendsSerach serach, RowBounds bounds);
}