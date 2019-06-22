package com.ycg.ksh.service.core.entity.service.adventive;

import com.ycg.ksh.entity.persistent.Order;
import com.ycg.ksh.entity.persistent.enterprise.Finance;
import com.ycg.ksh.entity.persistent.enterprise.OrderConvey;
import com.ycg.ksh.entity.service.enterprise.CompanyIntegrationy;
import org.apache.commons.beanutils.BeanUtils;

/**
 * 订单推送实体
 *
 * @author wangke
 * @create 2018-07-12 13:51
 **/
public class OrderPushObject extends Order {

    private CompanyIntegrationy shipper;//发货方
    private CompanyIntegrationy receive;//收货方
    private CompanyIntegrationy convey;//承运方
    private Finance finance; //财务数据
    private OrderConvey orderConvey;// 运输数据

    public OrderPushObject() {

    }
    public static OrderPushObject buildAlliance(Order order) throws Exception {
        if (order instanceof OrderPushObject) {
            return (OrderPushObject) order;
        }
        return new OrderPushObject(order);
    }


    public OrderPushObject(Order order) throws Exception {
        this();
        BeanUtils.copyProperties(this, order);
    }


    public CompanyIntegrationy getShipper() {
        return shipper;
    }

    public void setShipper(CompanyIntegrationy shipper) {
        this.shipper = shipper;
    }

    public CompanyIntegrationy getReceive() {
        return receive;
    }

    public void setReceive(CompanyIntegrationy receive) {
        this.receive = receive;
    }

    public CompanyIntegrationy getConvey() {
        return convey;
    }

    public void setConvey(CompanyIntegrationy convey) {
        this.convey = convey;
    }

    public Finance getFinance() {
        return finance;
    }

    public void setFinance(Finance finance) {
        this.finance = finance;
    }

    public OrderConvey getOrderConvey() {
        return orderConvey;
    }

    public void setOrderConvey(OrderConvey orderConvey) {
        this.orderConvey = orderConvey;
    }
}
