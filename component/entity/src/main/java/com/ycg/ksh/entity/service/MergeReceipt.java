/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-27 09:33:06
 */
package com.ycg.ksh.entity.service;

import com.ycg.ksh.entity.persistent.ImageInfo;
import com.ycg.ksh.entity.persistent.WaybillReceipt;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Collection;

/**
 * 回单信息,包括回单图片信息
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-27 09:33:06
 */
public class MergeReceipt extends WaybillReceipt {

    private static final long serialVersionUID = 2852112489116768267L;

    private Collection<ImageInfo> images;

    private AssociateUser user;

    public MergeReceipt() {
        super();
    }

    public MergeReceipt(WaybillReceipt receipt) throws ReflectiveOperationException {
        super();
        BeanUtils.copyProperties(this, receipt);
    }

    /**
     * getter method for images
     *
     * @return the images
     */
    public Collection<ImageInfo> getImages() {
        return images;
    }

    /**
     * setter method for images
     *
     * @param images the images to set
     */
    public void setImages(Collection<ImageInfo> images) {
        this.images = images;
    }

    /**
     * getter method for user
     *
     * @return the user
     */
    public AssociateUser getUser() {
        return user;
    }

    /**
     * setter method for user
     *
     * @param user the user to set
     */
    public void setUser(AssociateUser user) {
        this.user = user;
    }
}
