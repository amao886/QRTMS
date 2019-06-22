package com.ycg.ksh.entity.service.enterprise;

import com.ycg.ksh.entity.persistent.Order;
import com.ycg.ksh.entity.persistent.enterprise.Complaint;

/**
 * 投诉管理整合类
 *
 * @author: wangke
 * @create: 2018-10-30 15:54
 **/

public class ComplaintAlliance extends Complaint {

    private CompanyConcise shipper; //发货方
    private CompanyConcise receive; //收货方
    private Order order;//订单信息

    public CompanyConcise getShipper() {
        return shipper;
    }

    public void setShipper(CompanyConcise shipper) {
        this.shipper = shipper;
    }

    public CompanyConcise getReceive() {
        return receive;
    }

    public void setReceive(CompanyConcise receive) {
        this.receive = receive;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
