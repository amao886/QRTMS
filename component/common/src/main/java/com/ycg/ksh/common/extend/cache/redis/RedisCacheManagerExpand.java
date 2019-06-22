/**
 * TODO Add description
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-29 11:16:10
 */
package com.ycg.ksh.common.extend.cache.redis;

import com.ycg.ksh.common.extend.cache.CacheManagerExpand;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 缓存接口拓展
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-29 11:16:10
 */
public class RedisCacheManagerExpand<PK extends java.io.Serializable,M extends java.io.Serializable>  implements CacheManagerExpand<PK,M>{
	  	RedisTemplate<PK,M> redisTemplates;  
	    public RedisTemplate<PK,M> getRedisTemplates() {  
	        return redisTemplates;  
	    }  
	  
	    public void setRedisTemplates(RedisTemplate<PK,M> redisTemplates) {  
	        this.redisTemplates = redisTemplates;  
	    }  
	    
	    @Override
	    public M getModel(PK key) throws ClassCastException {  
	        return redisTemplates.opsForValue().get(key);
	    }
	    
	    @Override
	    public void set(PK key, M model) throws ClassCastException {
	    	redisTemplates.opsForValue().set(key, model);
	    }
	    
	    //设置多个键值
	    @Override
	    public void multiSet(Map<? extends PK, ? extends M> maps) throws ClassCastException {
	    	redisTemplates.opsForValue().multiSet(maps);
	    }
	    
	    //获取键对应集合的值
	    @Override
	    public List<M> multiGet(Collection<PK> key) throws ClassCastException {
	    	return redisTemplates.opsForValue().multiGet(key);
	    }
	    
	    //批量把集合插入列表中
	    @Override
	    public Long leftPushAll(PK key, Collection<M> values) throws ClassCastException {
			return redisTemplates.opsForList().leftPushAll(key, values);
	    }
	    
	    //把对象插入列表中
	    @Override
	    public Long leftPush(PK key, M model) throws ClassCastException {
			return redisTemplates.opsForList().leftPush(key, model);
	    }


}
