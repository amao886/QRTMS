/**
 * TODO Add description
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-27 15:42:17
 */
package com.ycg.ksh.common.entity;

import java.util.Date;

/**
 * 要持久化的基类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-27 15:42:17
 */
public class PersistentEntity extends BaseEntity {


	private static final long serialVersionUID = 3947590724285291494L;
	/**
	 * 创建时间
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-27 14:58:33
	 */
	protected Date createTime;
	/**
	 * 最后一次修改时间
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-27 14:58:45
	 */
	protected Date modifyTime;
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

}
