/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 09:39:02
 */
package com.ycg.ksh.entity.service;

import com.ycg.ksh.entity.persistent.DriverTrack;
import org.apache.commons.beanutils.BeanUtils;

/**
 * TODO Add description
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 09:39:02
 */
public class MergeDriverTrack extends DriverTrack {


	private static final long serialVersionUID = 6829012912915327417L;
	
	private AssociateUser reporter;//上报人

    public MergeDriverTrack() {
        super();
    }

    public MergeDriverTrack(DriverTrack transitionTrack) throws Exception {
        super();
        BeanUtils.copyProperties(this, transitionTrack);
    }

    public AssociateUser getReporter() {
        return reporter;
    }

    public void setReporter(AssociateUser reporter) {
        this.reporter = reporter;
    }
}
