package com.ycg.ksh.service.support.assist;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/31
 */

import com.ycg.ksh.entity.common.constant.AssignFettle;
import com.ycg.ksh.entity.common.constant.WaybillFettle;
import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.entity.persistent.Conveyance;

/**
 * 运单校验
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/31
 */
public class ConveyanceValidate extends BaseEntity {

    private Conveyance conveyance;

    private Integer operateKey;
    private Integer validateCode;

    private boolean owner;
    private boolean parent;

    private AssignFettle assignFettle;
    private WaybillFettle fettle;

    public ConveyanceValidate(Conveyance conveyance, Integer operateKey, Integer validateCode) {
        this.conveyance = conveyance;
        this.operateKey = operateKey;
        this.validateCode = validateCode;
    }

    public Integer getOperateKey() {
        return operateKey;
    }

    public void setOperateKey(Integer operateKey) {
        this.operateKey = operateKey;
    }

    public Integer getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(Integer validateCode) {
        this.validateCode = validateCode;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }

    public AssignFettle getAssignFettle() {
        return assignFettle;
    }

    public void setAssignFettle(AssignFettle assignFettle) {
        this.assignFettle = assignFettle;
    }

    public WaybillFettle getFettle() {
        return fettle;
    }

    public void setFettle(WaybillFettle fettle) {
        this.fettle = fettle;
    }

    public Conveyance getConveyance() {
        return conveyance;
    }

    public void setConveyance(Conveyance conveyance) {
        this.conveyance = conveyance;
    }
}
