/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-24 15:46:39
 */
package com.ycg.ksh.common.extend.cache;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 查询回调接口
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-24 15:46:39
 */
public abstract class CacheAdapter {
	protected TimeUnit timeUnit;
	protected long time;

	public CacheAdapter() {
		this(TimeUnit.HOURS, 1L);
	}

	public CacheAdapter(TimeUnit timeUnit, long time) {
		this.timeUnit = timeUnit;
		this.time = time;
	}

	protected abstract Object persistence(Serializable...keys) throws ParameterException, BusinessException;

	protected Object getCache(Serializable cacheKey) throws ParameterException, BusinessException{
        if(getCacheManager() != null){
			return getCacheManager().get(cacheKey);
		}
		return null;
    }

	protected void setCache(Serializable cacheKey, Object value) throws ParameterException, BusinessException{
		if(getCacheManager() != null){
			if(getTime() <= 0){
				getCacheManager().set(cacheKey, value);
			}else{
				getCacheManager().set(cacheKey, value, getTime(), getTimeUnit());
			}
		}
	}

	protected void deleteCache(Serializable cacheKey) throws ParameterException, BusinessException{
		if(getCacheManager() != null){
			getCacheManager().delete(cacheKey);
		}
	}


	public CacheManager getCacheManager(){ return null; };

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public long getTime() {
		return time;
	}
}
