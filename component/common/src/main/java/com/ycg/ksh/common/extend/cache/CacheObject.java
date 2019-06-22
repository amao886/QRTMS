package com.ycg.ksh.common.extend.cache;

import com.ycg.ksh.common.entity.BaseEntity;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class CacheObject extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -3705690983283582226L;
	
	private Object value;
	private Long expired;
	private Long expiration;
	
	/**
	 * 创建一个新的 Effective实例. 
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-06 18:02:43
	 * @param value
	 * @param expired
	 */
	public CacheObject(Object value, long expired, TimeUnit unit) {
		super();
		this.value = value;
		this.expired = unit.toMillis(expired);
		this.expiration = System.currentTimeMillis() + this.expired;
	}
	
	public boolean isAvailable() {
		if(expired != null && expired > 0) {
			return expiration > System.currentTimeMillis();
		}
		return true;
	}
	
	public Object getValue() {
		return value;
	}
	
	public long getExpired() {
		return expired;
	}

	public Long getExpiration() {
		return expiration;
	}
}
