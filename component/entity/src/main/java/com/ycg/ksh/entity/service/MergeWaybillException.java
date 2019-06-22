/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-27 08:42:16
 */
package com.ycg.ksh.entity.service;

import com.ycg.ksh.entity.persistent.ImageInfo;
import com.ycg.ksh.entity.persistent.WaybillException;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Collection;

/**
 * 任务单异常,包括图片异常图片信息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-27 08:42:16
 */
public class MergeWaybillException extends WaybillException {

	private static final long serialVersionUID = 3815751301231178326L;
	
	private Collection<ImageInfo> images;
	
	private AssociateUser user;

	public MergeWaybillException() {
		super();
	}

	public MergeWaybillException(WaybillException exception) throws ReflectiveOperationException {
		this();
		BeanUtils.copyProperties(this, exception);
	}

	/**
	 * getter method for images
	 * @return the images
	 */
	public Collection<ImageInfo> getImages() {
		return images;
	}

	/**
	 * setter method for images
	 * @param images the images to set
	 */
	public void setImages(Collection<ImageInfo> images) {
		this.images = images;
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
