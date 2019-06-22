package com.ycg.ksh.entity.service;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/20
 */

import com.ycg.ksh.entity.persistent.ImageStorage;
import com.ycg.ksh.entity.persistent.TransitionException;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Collection;

/**
 * 临时异常信息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/20
 */
public class MergeTransitionException extends TransitionException {

    private AssociateUser reporter;//上报人
    private Collection<ImageStorage> images;//回单图片信息

    public MergeTransitionException() {
        super();
    }
    public MergeTransitionException(TransitionException transitionException) throws Exception {
        super();
        BeanUtils.copyProperties(this, transitionException);
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
