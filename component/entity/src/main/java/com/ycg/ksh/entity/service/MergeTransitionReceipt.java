/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 09:36:15
 */
package com.ycg.ksh.entity.service;

import com.ycg.ksh.entity.persistent.ImageStorage;
import com.ycg.ksh.entity.persistent.TransitionReceipt;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Collection;

/**
 * TODO Add description
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 09:36:15
 */
public class MergeTransitionReceipt extends TransitionReceipt {

	private AssociateUser reporter;//上报人
	private Collection<ImageStorage> images;//回单图片信息
	
	public MergeTransitionReceipt() {
		super();
	}
	public MergeTransitionReceipt(TransitionReceipt transitionReceipt) throws Exception {
		super();
		BeanUtils.copyProperties(this, transitionReceipt);
	}
	public AssociateUser getReporter() {
		return reporter;
	}
	public void setReporter(AssociateUser reporter) {
		this.reporter = reporter;
	}
	public Collection<ImageStorage> getImages() {
		return images;
	}
	public void setImages(Collection<ImageStorage> images) {
		this.images = images;
	}
}
