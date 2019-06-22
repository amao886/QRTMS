package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.entity.persistent.Customer;
import com.ycg.ksh.entity.persistent.Goods;
import com.ycg.ksh.entity.persistent.Waybill;

import java.util.Collection;

/**
 * @Author:wangke
 * @Description:
 * @Date:Create in 14:25 2017/12/13
 * @Modified By:
 */
public class MergeGoods extends BaseEntity {


    private static final long serialVersionUID = 1456246408116967737L;
    //货物集合
    private Collection<Goods> goods;

    //客户
    private Customer customer;

    //运单
    private Waybill waybill;

    //是否添加
    private Integer toAddFlag;

    public Integer getToAddFlag() {
        return toAddFlag;
    }

    public void setToAddFlag(Integer toAddFlag) {
        this.toAddFlag = toAddFlag;
    }

    public Collection<Goods> getGoods() {
        return goods;
    }

    public void setGoods(Collection<Goods> goods) {
        this.goods = goods;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Waybill getWaybill() {
        return waybill;
    }

    public void setWaybill(Waybill waybill) {
        this.waybill = waybill;
    }
}
