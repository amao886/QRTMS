/**
 * TODO Add description
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-29 11:16:10
 */
package com.ycg.ksh.common.extend.cache;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 缓存接口
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-29 11:16:10
 */
public interface CacheManager{
	/**
	 * 放入缓存
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-29 11:17:03
	 * @param key 缓存KEY
	 * @param value 缓存值,必须实现 {@link Serializable} 接口
	 * @return 
	 * @throws ClassCastException 参数 value 没有实现 {@link Serializable} 接口
	 */
	public void set(Serializable key, Object value) throws ClassCastException ;

	/**
	 * 放入缓存，并设置超时时间
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-29 11:17:30
	 * @param key 缓存KEY
	 * @param value 缓存值,必须实现 {@link Serializable} 接口
	 * @param expire 超时时间值
	 * @param expireTimeUnit  超时时间单位
	 * @throws ClassCastException 参数 value 没有实现 {@link Serializable} 接口
	 */
	public void set(Serializable key, Object value, Long expire, TimeUnit expireTimeUnit) throws ClassCastException ;
	
	/**
	 * 从缓存中获取值
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-29 11:18:32
	 * @param key 缓存KEY
	 * @return 与KEY对应的值
	 */
	public  <T> T get(Serializable key);

	/**
	 * @param key
	 * @param <T>
	 * @return
	 */
	public <T> T get(Serializable key, long m, Supplier<T> supplier);
	
	/**
	 * 从缓存中移除
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-29 11:19:15
	 * @param key  要移除的KEY
	 */
	public void delete(Serializable key);
	
	/**
	 * 获取指定key的值, 并设置先新值
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-29 11:19:42
	 * @param key  缓存KEY
	 * @param newValue 要设置的新值,必须实现 {@link Serializable} 接口
	 * @return  旧的值
	 * @throws ClassCastException 参数 value 没有实现 {@link Serializable} 接口
	 */
	public Object getAndSet(Serializable key, Object newValue) throws ClassCastException ;
	
	/**
	 * 放入缓存，如果缓存中不存在对应的key
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-29 11:20:33
	 * @param key  缓存KEY
	 * @param value 缓存值,必须实现 {@link Serializable} 接口
	 * @return true:放入成功，false:对应的KEY已经存在放入失败
	 * @throws ClassCastException 参数 value 没有实现 {@link Serializable} 接口
	 */
	public boolean setNotExist(Serializable key, Object value) throws ClassCastException;
	/**
	 * 缓存计数器，指定key的值及需要增加的值
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-29 11:19:42
	 * @param key  缓存KEY
	 * @param i 要设置的新值,必须实现 {@link Serializable} 接口
	 * @return  增长的值
	 * @throws ClassCastException 参数 value 没有实现 {@link Serializable} 接口
	 */
	long increment(Serializable key, int i) throws ClassCastException;
	/**
	 * 设置key失效时间
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-29 11:17:30
	 * @param key 缓存KEY
	 * @param expire 过期时间值
	 * @param expireTimeUnit  过期时间单位
	 * @throws ClassCastException 参数 value 没有实现 {@link Serializable} 接口
	 */
	Boolean expire(Serializable key, Long expire, TimeUnit expireTimeUnit) throws ClassCastException;
	/**
	 * 获取key失效时间
	 * @param key
	 * @return
	 * @throws ClassCastException
	 */
	Long getExpire(Serializable key) throws ClassCastException;
	
	/**
	 * 重写spring-data-redis中根据key获取计数器中的值
	 * @param key
	 * @return
	 * @throws ClassCastException
	 */
	long getIncrValue(String key);

}
