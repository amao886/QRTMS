/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-24 15:26:59
 */
package com.ycg.ksh.common.extend.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 本地局部缓存
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-24 15:26:59
 */
public class LocalCacheManager<V>  {

	final Logger logger = LoggerFactory.getLogger(LocalCacheManager.class);

	private ThreadLocal<Map<Serializable, V>> managerCache = new ThreadLocal<Map<Serializable, V>>();
	
	private CacheAdapter adapter;

    private String cacheKey;

	private boolean cache = true;

	/**
	 * 创建一个新的 LocalCacheManager实例.
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-26 22:01:28
	 * @param adapter
	 */
	public LocalCacheManager(String cacheKey,  CacheAdapter adapter) {
		this(cacheKey, true, adapter);
	}


	/**
	 * 创建一个新的 LocalCacheManager实例. 
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-26 22:01:28
	 * @param adapter
	 */
	public LocalCacheManager(String cacheKey, boolean cache,  CacheAdapter adapter) {
		this.cache = cache;
		this.cacheKey = cacheKey;
		this.adapter = adapter;

	}

    /**
     * 创建一个新的 LocalCacheManager实例.
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-26 22:01:28
     * @param adapter
     */
    public LocalCacheManager(Class<?> clazz, CacheAdapter adapter) {
        this(clazz, true, adapter);
    }

	/**
	 * 创建一个新的 LocalCacheManager实例.
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-26 22:01:28
	 * @param adapter
	 */
	public LocalCacheManager(Class<?> clazz, boolean cache, CacheAdapter adapter) {
		this(clazz.getSimpleName().toUpperCase(), cache, adapter);
	}



	private Serializable toCacheKey(Serializable...keys){
		StringBuilder builder = new StringBuilder(cacheKey);
		for (Serializable key : keys){
			if(key != null){
				builder.append("#").append(key.toString());
			}
		}
		return builder.toString();
	}

	/**
	 * 单线程
	 * <p>
	 * @param keys
	 * @return
	 */
	public V get(Serializable...keys) {
		if(keys == null || keys.length <= 0){
			return null;
		}
		return loadThread(toCacheKey(keys), keys);
	}

	public V getByKey(Serializable cacheKey, Serializable...keys) {
		if(cacheKey != null){
			return loadThread(cacheKey, keys);
		}
		return null;
	}


	public V refresh(Serializable...keys) {
		logger.info("刷新缓存 -> {}", Arrays.toString(keys));
		if(keys != null && keys.length > 0){
			Serializable cacheKey = toCacheKey(keys);
			delete(cacheKey);
			return getByKey(cacheKey, keys);
		}
		return null;
	}

	public void delete(Serializable...keys) {
		if(keys != null && keys.length > 0){
			deleteKey(toCacheKey(keys));
		}
	}

	public void deleteKey(Serializable cacheKey) {
		if(cacheKey != null){
			Map<Serializable, V> threadCache = managerCache.get();
			if(threadCache != null){
				threadCache.remove(cacheKey);
			}
			if(adapter != null){
				adapter.deleteCache(cacheKey);
			}
		}
	}

	private V threadCeche(Serializable cacheKey, V v){
		if(Optional.ofNullable(v).isPresent()) {
			logger.info("设置线程缓存 -> {} : {}", cacheKey, v);
			//设置局部缓存
			threadCache().put(cacheKey, v);
		}
		return v;
	}


	private V loadThread(Serializable cacheKey, Serializable...keys){
		Map<Serializable, V> threadCache = threadCache();
		//从局部缓存取
		V value = threadCache.get(cacheKey);
		logger.info("线程缓存 -> {} : {}", cacheKey, value);
		if(cache){
			value = Optional.ofNullable(value).orElseGet(() -> {
				return threadCeche(cacheKey, loadCache(cacheKey, keys));
			});
		}else{
			value = threadCeche(cacheKey, loadPersistence(cacheKey, keys));
		}
		return value;
	}

	private V loadCache(Serializable cacheKey, Serializable...keys){
		V v = null;
		//从缓存中去
		Object cacheObject = adapter.getCache(cacheKey);
		logger.info("全局缓存 -> {} : {}", cacheKey, cacheObject);
		if(!Optional.ofNullable(cacheObject).isPresent()){
			v = (V) cacheObject;
		}
		return Optional.ofNullable(v).orElseGet(() -> {
			//从数据库取
			V cacheObj = loadPersistence(cacheKey, keys);
			if(Optional.ofNullable(cacheObj).isPresent()){
				logger.info("设置全局缓存 -> {} : {}", cacheKey, cacheObj);
				//设置缓存
				adapter.setCache(cacheKey, cacheObj);
			}
			return cacheObj;
		});
	}

	private V loadPersistence(Serializable cacheKey, Serializable...keys){
		//从数据库取
		Object cacheObject = adapter.persistence(keys);
		logger.info("数据库 -> {} : {}", cacheKey, cacheObject);
		if(Optional.ofNullable(cacheObject).isPresent()){
			return (V) cacheObject;
		}
		return null;
	}


	private Map<Serializable, V> threadCache(){
		Map<Serializable, V> cache = managerCache.get();
		if(cache == null){
			managerCache.set(new HashMap<Serializable, V>());
		}
		return managerCache.get();
	}
}
