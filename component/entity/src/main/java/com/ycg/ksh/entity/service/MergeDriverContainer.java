/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 09:32:48
 */
package com.ycg.ksh.entity.service;

import com.ycg.ksh.entity.persistent.DriverContainer;
import com.ycg.ksh.entity.persistent.ImageStorage;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Collection;

/**
 * 司机装车信息
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 09:32:48
 */
public class MergeDriverContainer extends DriverContainer {

    private static final long serialVersionUID = 7155308732601201080L;

    private AssociateUser loader;//装车人
    private AssociateUser unloader;//卸货人

    private Collection<ImageStorage> images;

    private Collection<MergeTransitionReceipt> receipts;//回单信息

    private Collection<MergeDriverTrack> tracks;

    public MergeDriverContainer() {
        super();
    }

    public MergeDriverContainer(DriverContainer driverContainer) throws Exception {
        super();
        BeanUtils.copyProperties(this, driverContainer);
    }

    public AssociateUser getLoader() {
        return loader;
    }

    public void setLoader(AssociateUser loader) {
        this.loader = loader;
    }

    public AssociateUser getUnloader() {
        return unloader;
    }

    public void setUnloader(AssociateUser unloader) {
        this.unloader = unloader;
    }

    public Collection<ImageStorage> getImages() {
        return images;
    }

    public void setImages(Collection<ImageStorage> images) {
        this.images = images;
    }

    public Collection<MergeTransitionReceipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(Collection<MergeTransitionReceipt> receipts) {
        this.receipts = receipts;
    }

    public Collection<MergeDriverTrack> getTracks() {
        return tracks;
    }

    public void setTracks(Collection<MergeDriverTrack> tracks) {
        this.tracks = tracks;
    }
}
