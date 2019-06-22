/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-07 13:19:56
 */
package com.ycg.ksh.service.support.assist;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.cache.CacheAdapter;
import com.ycg.ksh.common.extend.cache.LocalCacheManager;
import com.ycg.ksh.entity.service.AssociateUser;
import com.ycg.ksh.entity.service.FriendUser;
import com.ycg.ksh.service.persistence.UserMapper;
import com.ycg.ksh.service.api.FriendsService;
import org.apache.commons.lang.StringUtils;
import tk.mybatis.mapper.common.Mapper;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地局部缓存创建工厂
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-07 13:19:56
 */
public class LocalCacheFactory {


    private static final Map<Object, LocalCacheManager<?>> adapters = new ConcurrentHashMap<Object, LocalCacheManager<?>>();
    
    @SuppressWarnings("unchecked")
    public static <V extends BaseEntity> LocalCacheManager<V> createCache(Mapper<? extends BaseEntity> mapper){
        LocalCacheManager<V> manager = (LocalCacheManager<V>) adapters.get(mapper);
        if(manager == null) {
            String cacheKey = mapper.getClass().getSimpleName();
            manager = new LocalCacheManager<V>(cacheKey, new CacheAdapter() {
                @Override
                public Object persistence(Serializable...keys) throws ParameterException, BusinessException {
                    return mapper.selectByPrimaryKey(keys[0]);
                }
            });
            adapters.put(mapper, manager);
        }
        return manager;
    }
    @SuppressWarnings("unchecked")
    public static LocalCacheManager<AssociateUser> createUserCache(UserMapper mapper){
        LocalCacheManager<AssociateUser> manager = (LocalCacheManager<AssociateUser>) adapters.get(mapper);
        if(manager == null) {
            String cacheKey = mapper.getClass().getSimpleName();
            manager = new LocalCacheManager<AssociateUser>(cacheKey, new CacheAdapter() {
                @Override
                public Object persistence(Serializable...keys) throws ParameterException, BusinessException {
                    return new AssociateUser(mapper.selectByPrimaryKey(keys[0]));
                }
            });
            adapters.put(mapper, manager);
        }
        return manager;
    }

    public static LocalCacheManager<AssociateUser> createFriendUserCache(FriendsService friendsService){
        LocalCacheManager<AssociateUser> manager = (LocalCacheManager<AssociateUser>) adapters.get(friendsService);
        if(manager == null) {
            String cacheKey = friendsService.getClass().getSimpleName();
            manager = new LocalCacheManager<AssociateUser>(cacheKey, new CacheAdapter() {
                @Override
                public Object persistence(Serializable...keys) throws ParameterException, BusinessException {
                    if(keys[0] == null || keys[1] == null){  return null; }
                    Integer uKey = Integer.parseInt(keys[0].toString());
                    Integer fKey = Integer.parseInt(keys[1].toString());
                    if(uKey * fKey == 0){ return null; }
                    if(uKey - fKey == 0){
                        return new AssociateUser(friendsService.loadUser(uKey));
                    }else{
                        FriendUser friendUser = friendsService.loadFriendUser(Integer.parseInt(keys[0].toString()), Integer.parseInt(keys[1].toString()));
                        if(friendUser != null){
                            AssociateUser associateUser = new AssociateUser();
                            associateUser.setEncryptName(friendUser.getEncryptName());
                            associateUser.setCreatetime(friendUser.getCreatetime());
                            associateUser.setId(friendUser.getFriendKey());
                            if(StringUtils.isNotBlank(friendUser.getRemarkName())){
                                associateUser.setUnamezn(friendUser.getRemarkName());
                            }else{
                                associateUser.setUnamezn(friendUser.getUserName());
                            }
                            associateUser.setType(friendUser.getType());
                            associateUser.setMobilephone(friendUser.getMobile());
                            associateUser.setCompany(friendUser.getCompany());
                            return associateUser;
                        }
                    }
                    return null;
                }
            });
            adapters.put(friendsService, manager);
        }
        return manager;
    }

    public static <V extends BaseEntity> LocalCacheManager<V> createCache(Class<V> clazz, CacheAdapter adapter){
        LocalCacheManager<V> cacheManager = (LocalCacheManager<V>) adapters.get(clazz.getName());
        return Optional.ofNullable(cacheManager).orElseGet(() -> {
            LocalCacheManager<V> manager = new LocalCacheManager<V>(clazz.getName(), adapter);
            adapters.put(clazz.getName(), manager);
            return manager;
        });
    }
}
