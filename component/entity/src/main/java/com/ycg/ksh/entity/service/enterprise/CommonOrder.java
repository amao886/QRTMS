package com.ycg.ksh.entity.service.enterprise;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/17
 */

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.RegionUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.OrderCommodity;
import com.ycg.ksh.entity.persistent.enterprise.CustomData;
import com.ycg.ksh.entity.persistent.enterprise.OrderExtra;

import java.util.Collection;

/**
 * 订单信息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/17
 */
public class CommonOrder extends BaseEntity {

    private CustomerConcise shipper;
    private CustomerConcise receive;
    private CustomerConcise convey;

    private OrderTemplate order;
    private OrderExtra extra;//订单额外信息
    private Collection<OrderCommodity> commodities;//货物信息
    private Collection<CustomData> customDatas;//自定义数据

    private String[] receiveAddress;
    private String[] distributeAddress;
    private String[] originStation;
    private String[] arrivalStation;

    public void analyzeSomething(){
        if(order != null && StringUtils.isNotBlank(order.getReceiveAddress())){
            receiveAddress = RegionUtils.analyze(order.getReceiveAddress(), true);
        }
        if(extra != null){
            if(StringUtils.isNotBlank(extra.getDistributeAddress())){
                distributeAddress = RegionUtils.analyze(extra.getDistributeAddress(), true);
            }
            if(StringUtils.isNotBlank(extra.getOriginStation())){
                originStation = RegionUtils.analyze(extra.getOriginStation(), false);
            }
            if(StringUtils.isNotBlank(extra.getArrivalStation())){
                arrivalStation = RegionUtils.analyze(extra.getArrivalStation(), false);
            }
        }
    }

    public OrderTemplate getOrder() {
        return order;
    }

    public void setOrder(OrderTemplate order) {
        this.order = order;
    }

    public CustomerConcise getShipper() {
        return shipper;
    }

    public void setShipper(CustomerConcise shipper) {
        this.shipper = shipper;
    }

    public CustomerConcise getReceive() {
        return receive;
    }

    public void setReceive(CustomerConcise receive) {
        this.receive = receive;
    }

    public CustomerConcise getConvey() {
        return convey;
    }

    public void setConvey(CustomerConcise convey) {
        this.convey = convey;
    }

    public OrderExtra getExtra() {
        return extra;
    }

    public void setExtra(OrderExtra extra) {
        this.extra = extra;
    }

    public Collection<OrderCommodity> getCommodities() {
        return commodities;
    }

    public void setCommodities(Collection<OrderCommodity> commodities) {
        this.commodities = commodities;
    }

    public Collection<CustomData> getCustomDatas() {
        return customDatas;
    }

    public void setCustomDatas(Collection<CustomData> customDatas) {
        this.customDatas = customDatas;
    }

    public String[] getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String[] receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String[] getDistributeAddress() {
        return distributeAddress;
    }

    public void setDistributeAddress(String[] distributeAddress) {
        this.distributeAddress = distributeAddress;
    }

    public String[] getOriginStation() {
        return originStation;
    }

    public void setOriginStation(String[] originStation) {
        this.originStation = originStation;
    }

    public String[] getArrivalStation() {
        return arrivalStation;
    }

    public void setArrivalStation(String[] arrivalStation) {
        this.arrivalStation = arrivalStation;
    }
}
