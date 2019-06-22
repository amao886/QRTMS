/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-21 10:47:24
 */
package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Optional;

/**
 * 分页,页面范围
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-21 10:47:24
 */
public class PageScope extends BaseEntity {

	private static final long serialVersionUID = 5383455573946013237L;

	
	public static final PageScope DEFAULT = new PageScope();
	
	private Integer pageSize;
	private Integer pageNum;

	public PageScope() {
		super();
		validate(10, 1);
	}
	public PageScope(Integer pageNum, Integer pageSize) {
		super();
		this.setPageNum(pageNum);
		this.setPageSize(pageSize);
		validate(10, 1);
	}
	
	public void validate(int size, int num) {
		if(pageSize == null || pageSize <= 0) {
			pageSize = size;
		}
		if(pageNum == null || pageNum <= 0) {
			pageNum = num;
		}
	}
	
	
	/**
	 * getter method for pageSize
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}
	/**
	 * setter method for pageSize
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = Math.max(1, Optional.ofNullable(pageSize).orElse(10));
	}
	/**
	 * getter method for pageNum
	 * @return the pageNum
	 */
	public Integer getPageNum() {
		return pageNum;
	}
	/**
	 * setter method for pageNum
	 * @param pageNum the pageNum to set
	 */
	public void setPageNum(Integer pageNum) {
		this.pageNum = Math.max(1, Optional.ofNullable(pageNum).orElse(1));
	}
}
