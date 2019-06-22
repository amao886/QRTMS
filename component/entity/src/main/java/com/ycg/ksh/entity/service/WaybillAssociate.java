/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-07 16:07:11
 */
package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 任务单关联信息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-07 16:07:11
 */
public class WaybillAssociate extends BaseEntity {

	private static final long serialVersionUID = -1229841909647732731L;

	public static final WaybillAssociate EMPTY = new WaybillAssociate();

    private boolean associateConveyance = false;//是否包含指派的任务单
    private boolean associateTrack = false;//是否关联轨迹
    private boolean associateReceipt = false;//是否关联回单
    private boolean associateException = false;//是否关联异常
    private boolean associateCommodity = false;//是否关联货物明细
    private boolean associateProjectGroup = false;//是否关联项目组信息

    public static WaybillAssociate associateConveyance() {
        return new WaybillAssociate(true, false, false, false, false, false);
    }
    public static WaybillAssociate associateTrack() {
        return new WaybillAssociate(false, true, false, false, false, false);
    }
    public static WaybillAssociate associateReceipt() {
        return new WaybillAssociate(false, false, true, false, false, false);
    }
    public static WaybillAssociate associateException() {
        return new WaybillAssociate(false, false, false, true, false, false);
    }
    public static WaybillAssociate associateCommodity() {
        return new WaybillAssociate(false, false, false, false, true, false);
    }
    public static WaybillAssociate associateProjectGroup() {
        return new WaybillAssociate(false, false, false, false, false, true);
    }

    public static WaybillAssociate associateEmpty() {
        return new WaybillAssociate();
    }

    public static WaybillAssociate associateDetail() {
        return new WaybillAssociate(false, true, true, true, true, true);
    }

    public static WaybillAssociate associate(boolean associateConveyance, boolean associateTrack, boolean associateReceipt, boolean associateException, boolean associateCommodity, boolean associateProjectGroup) {
        return new WaybillAssociate(associateConveyance, associateTrack, associateReceipt, associateException, associateCommodity, associateProjectGroup);
    }

    private WaybillAssociate() {}

    private WaybillAssociate(boolean associateConveyance, boolean associateTrack, boolean associateReceipt, boolean associateException, boolean associateCommodity, boolean associateProjectGroup) {
        this.associateConveyance = associateConveyance;
        this.associateTrack = associateTrack;
        this.associateReceipt = associateReceipt;
        this.associateException = associateException;
        this.associateCommodity = associateCommodity;
        this.associateProjectGroup = associateProjectGroup;
    }

    public boolean isAssociateConveyance() {
        return associateConveyance;
    }

    public void setAssociateConveyance(boolean associateConveyance) {
        this.associateConveyance = associateConveyance;
    }

    public boolean isAssociateTrack() {
        return associateTrack;
    }

    public void setAssociateTrack(boolean associateTrack) {
        this.associateTrack = associateTrack;
    }

    public boolean isAssociateReceipt() {
        return associateReceipt;
    }

    public void setAssociateReceipt(boolean associateReceipt) {
        this.associateReceipt = associateReceipt;
    }

    public boolean isAssociateException() {
        return associateException;
    }

    public void setAssociateException(boolean associateException) {
        this.associateException = associateException;
    }

    public boolean isAssociateCommodity() {
        return associateCommodity;
    }

    public void setAssociateCommodity(boolean associateCommodity) {
        this.associateCommodity = associateCommodity;
    }

    public boolean isAssociateProjectGroup() {
        return associateProjectGroup;
    }

    public void setAssociateProjectGroup(boolean associateProjectGroup) {
        this.associateProjectGroup = associateProjectGroup;
    }
}
