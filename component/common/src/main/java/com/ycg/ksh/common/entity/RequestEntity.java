package com.ycg.ksh.common.entity;

/**
 * 控制层接收分页数据的数据传递实体
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-06 12:25:01
 */
public class RequestEntity extends BaseEntity {
	
	private static final long serialVersionUID = -1033910905210340704L;
	
	/**
	 * 当前页码
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-06 12:25:36
	 */
	private Integer num;
	private Integer pageNum;
	/**
	 * 每页显示的数量
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-06 12:25:39
	 */
	private Integer size;
	private Integer pageSize;
	
	
	
	public Integer getNum() {
		if(num == null || num <= 0){
			num = 1;
		}
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	public void initializePage(){
		this.num=this.getNum();		
		if(this.getSize() == null || this.getSize() <= 0) {
			this.setSize(20);
		}
	}
}
