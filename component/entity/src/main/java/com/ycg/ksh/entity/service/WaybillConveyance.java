package com.ycg.ksh.entity.service;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/23
 */

import com.ycg.ksh.entity.persistent.Conveyance;
import com.ycg.ksh.entity.persistent.Waybill;
import org.apache.commons.beanutils.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 运单和任务单
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/23
 */
public class WaybillConveyance extends Conveyance {

    private Waybill waybill;//任务单

    private AssociateUser owner;

    private List<WaybillConveyance> children;

    //位置状态
    private Integer addressStatus;
    private Date addressTime;

    //回单状态
    private Integer receiptStatus;
    private Date receiptTime;

    public Integer getAddressStatus() {
        return addressStatus;
    }

    public void setAddressStatus(Integer addressStatus) {
        this.addressStatus = addressStatus;
    }

    public Integer getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(Integer receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public WaybillConveyance() { }

    public WaybillConveyance(Conveyance conveyance) throws ReflectiveOperationException {
        super();
        BeanUtils.copyProperties(this, conveyance);
    }

    public WaybillConveyance(Conveyance conveyance, Waybill _waybill) throws ReflectiveOperationException {
        this(conveyance);
        waybill = _waybill;
    }

    public void addChildren(WaybillConveyance conveyance){
        if(children == null){
            children = new ArrayList<WaybillConveyance>();
        }
        children.add(conveyance);
    }


    public Waybill getWaybill() {
        return waybill;
    }

    public void setWaybill(Waybill waybill) {
        this.waybill = waybill;
    }

    public List<WaybillConveyance> getChildren() {
        return children;
    }

    public void setChildren(List<WaybillConveyance> children) {
        this.children = children;
    }

    public AssociateUser getOwner() {
        return owner;
    }

    public void setOwner(AssociateUser owner) {
        this.owner = owner;
    }

    public Date getAddressTime() {
        return addressTime;
    }

    public void setAddressTime(Date addressTime) {
        this.addressTime = addressTime;
    }

    public Date getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(Date receiptTime) {
        this.receiptTime = receiptTime;
    }
}
