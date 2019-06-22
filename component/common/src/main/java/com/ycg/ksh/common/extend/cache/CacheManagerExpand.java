/**
 * TODO Add description
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-29 11:16:10
 */
package com.ycg.ksh.common.extend.cache;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 缓存接口拓展
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-29 11:16:10
 */
public interface CacheManagerExpand<PK extends Serializable,M extends Serializable>{
		/**
		 * 从缓存中获取值
		 * <p>
		 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-29 11:18:32
		 * @param key 缓存KEY
		 * @return 与KEY对应的对象
		 */
	    public M getModel(PK key) throws ClassCastException;
	    /**
		 * 放入缓存
		 * <p>
		 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-29 11:17:03
		 * @param key 缓存KEY
		 * @param value 缓存值,必须实现 {@link Serializable} 接口
		 * @return 
		 * @throws ClassCastException 参数 value 没有实现 {@link Serializable} 接口
		 */
	    public void set(PK key, M model) throws ClassCastException;
	    /**
		 * 放入缓存
		 * <p>
		 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-29 11:17:03
		 * @param map 缓存设置多个键值
		 * @throws ClassCastException 参数 value 没有实现 {@link Serializable} 接口
		 */
	    public void multiSet(Map<? extends PK, ? extends M> maps) throws ClassCastException;
	    /**
		 * 获取缓存
		 * <p>
		 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-29 11:17:03
		 * @param key 缓存集合键KEY
		 * @return 获取键对应集合的值
		 * @throws ClassCastException
		 */
	    public List<M> multiGet(Collection<PK> key) throws ClassCastException;
	    
	    /**
		 * 放入缓存
		 * <p>
		 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-29 11:17:03
		 * @param key 批量把集合插入列表中
		 * @return 放入集合的长度
		 * @throws ClassCastException
		 */
	    public Long leftPushAll(PK key, Collection<M> values)  throws ClassCastException;
	    /**
		 * 放入缓存
		 * <p>
		 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-29 11:17:03
		 * @param key 对象插入列表中
		 * @return 放入对象的长度
		 * @throws ClassCastException
		 */
	    public Long leftPush(PK key, M model) throws ClassCastException;

}
