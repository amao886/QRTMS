package com.ycg.ksh.common.extend.cache.redis;

import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.common.util.Assert;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class RedisCacheManager implements CacheManager {

	private String regionName;

	protected RedisTemplate<Serializable, Serializable> redisTemplate;

	private Serializable key(Serializable key) {
		if(regionName != null && regionName.length() > 0){
			return regionName + "." + String.valueOf(key);
		}
		return key;
	}
	
	private ValueOperations<Serializable, Serializable> opsForValue(){
		return redisTemplate.opsForValue();
	}

	@Override
	public void delete(Serializable key) {
		redisTemplate.delete(key(key));
	}

	@Override
	public  <T> T get(Serializable key) {
		return (T)opsForValue().get(key(key));
	}

	/**
	 * @param key
	 * @return
	 */
	@Override
	public <T> T get(Serializable key, long m, Supplier<T> supplier) {
		Object object = get(key);
		if(object != null){
			return (T) object;
		}
		T t = supplier.get();
		if(t != null){
			if(m > 0){
				set(key, t, m, TimeUnit.MINUTES);
			}else{
				set(key, t);
			}
		}
		return t;
	}

	@Override
	public void set(Serializable key, Object value) throws ClassCastException {
		set(key, value, 0l, TimeUnit.SECONDS);
	}

	@Override
	public void set(Serializable key, Object value, Long expire, TimeUnit expireTimeUnit) throws ClassCastException {
		Serializable value_temp = null;
		if(value != null) {
			Assert.assignable(value, Serializable.class);
			value_temp = (Serializable) value;
		}
		Serializable _key = key(key);
		if (expire == null || expire <= 0) {
			opsForValue().set(_key, value_temp);
		} else {
			opsForValue().set(_key, value_temp);
			redisTemplate.expire(_key, expire, expireTimeUnit);
		}
	}

	@Override
	public Object getAndSet(Serializable key, Object newValue) throws ClassCastException {
		Serializable value_temp = null;
		if(newValue != null) {
			Assert.assignable(newValue, Serializable.class);
			value_temp = (Serializable) newValue;
		}
		return opsForValue().getAndSet(key(key), value_temp);
	}

	@Override
	public boolean setNotExist(Serializable key, Object value) throws ClassCastException {
		Serializable value_temp = null;
		if(value != null) {
			Assert.assignable(value, Serializable.class);
			value_temp = (Serializable) value;
		}
		return opsForValue().setIfAbsent(key(key), value_temp);
	}
	
	@Override
	public long increment(Serializable key, int delta) throws ClassCastException {
		return opsForValue().increment(key, delta);
	}
	
	@Override
	public Boolean expire(Serializable key, Long expire, TimeUnit expireTimeUnit) throws ClassCastException {
		boolean flag = false;
		if (expire == null || expire <= 0) {
			flag = redisTemplate.expire(key, 0L, TimeUnit.SECONDS);
		} else {
			flag = redisTemplate.expire(key, expire, expireTimeUnit);
		}
		return flag;
	}
	
	@Override
	public Long getExpire(Serializable key) throws ClassCastException {
		long timeOut = redisTemplate.getExpire(key);
		return timeOut;
	}
	
	@Override
    public long getIncrValue(final String key) {  
        return redisTemplate.execute(new RedisCallback<Long>() {  
            @Override  
            public Long doInRedis(RedisConnection connection) throws DataAccessException {  
                RedisSerializer<String> serializer=redisTemplate.getStringSerializer();  
                byte[] rowkey=serializer.serialize(key);  
                byte[] rowval=connection.get(rowkey);  
                try {  
                    String val=serializer.deserialize(rowval);  
                    return Long.parseLong(val);  
                } catch (Exception e) {  
                    return 0L;  
                }  
            }  
        });  
    }  
	
	public void setRedisTemplate(RedisTemplate<Serializable, Serializable> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

}
