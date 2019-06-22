/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-27 09:42:34
 */
package com.ycg.ksh.entity.service;

import com.ycg.ksh.entity.persistent.WaybillTrack;
import org.apache.commons.beanutils.BeanUtils;

/**
 * 查询运单轨迹信息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-27 09:42:34
 */
public class MergeTrack extends WaybillTrack {
	
	private static final long serialVersionUID = 1545119951605025451L;

	private AssociateUser user;

	public MergeTrack() {
		super();
	}

	public MergeTrack(WaybillTrack track) throws ReflectiveOperationException {
		super();
		BeanUtils.copyProperties(this, track);
	}

	public MergeTrack(WaybillTrack track, AssociateUser user) throws ReflectiveOperationException {
		this(track);
		this.user = user;
	}

	/**
	 * getter method for user
	 * @return the user
	 */
	public AssociateUser getUser() {
		return user;
	}

	/**
	 * setter method for user
	 * @param user the user to set
	 */
	public void setUser(AssociateUser user) {
		this.user = user;
	}
}
