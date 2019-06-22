package com.ycg.ksh.entity.service.barcode;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/28
 */

import com.ycg.ksh.entity.common.constant.SourceType;
import com.ycg.ksh.entity.common.constant.WaybillFettle;
import com.ycg.ksh.constant.BarCodeFettle;

/**
 *  项目组二维码上线文
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/28
 */
public class GroupCodeContext extends BarcodeContext {

    private WaybillFettle status;//状态
    private Integer groupId;//条码所在组编号
    private Integer ownerKey;//条码所属人ID
    private Integer waybillId;//绑定的任务单ID

    private SourceType sourceType;//来源

    public GroupCodeContext() {
    }

    public GroupCodeContext(Integer groupId, Integer ownerKey, WaybillFettle status) {
        this.groupId = groupId;
        this.ownerKey = ownerKey;
        this.status = status;
    }

    @Override
    public BarCodeFettle fettle() {
        if(super.fettle().unbind()){
            if(waybillId != null && waybillId > 0){
                return BarCodeFettle.BIND;
            }
        }
        return super.fettle();
    }

    @Override
    public boolean isComplete() {
        return status.arrive() || status.receive();
    }

    @Override
    public boolean isCompany() {
        return false;
    }

    public WaybillFettle getStatus() {
        return status;
    }

    public void setStatus(WaybillFettle status) {
        this.status = status;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getOwnerKey() {
        return ownerKey;
    }

    public void setOwnerKey(Integer ownerKey) {
        this.ownerKey = ownerKey;
    }

    public Integer getWaybillId() {
        return waybillId;
    }

    public void setWaybillId(Integer waybillId) {
        this.waybillId = waybillId;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }
}
