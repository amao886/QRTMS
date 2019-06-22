package com.ycg.ksh.entity.service.plan;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/12
 */

import com.ycg.ksh.common.util.NumberToCNUtils;
import com.ycg.ksh.entity.persistent.enterprise.CustomData;
import com.ycg.ksh.entity.persistent.enterprise.OrderExtra;
import com.ycg.ksh.entity.persistent.plan.PlanCommodity;
import com.ycg.ksh.entity.persistent.plan.PlanDesignate;
import com.ycg.ksh.entity.persistent.plan.PlanOrder;
import com.ycg.ksh.entity.service.enterprise.CompanyConcise;
import com.ycg.ksh.entity.service.enterprise.CustomerConcise;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * 计划订单
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/12
 */
public class PlanAlliance extends PlanOrder {

    private CustomerConcise shipper;//发货方
    private CustomerConcise receive;//收货方
    private CustomerConcise convey;//承运方

    private Collection<PlanCommodity> commodities;//货物信息
    private Collection<CustomData> customDatas;//自定义数据

    private OrderExtra extra;//订单额外信息

    private Integer count;//货物数
    private Integer quantity;//数量(件)
    private Integer boxCount;//箱数(箱)
    private Double volume;//体积(立方米)
    private Double weight;//重量(千克)

    private PlanDesignate accept;//接收数据(被指派的数据)
    private PlanDesignate designate;//指派数据
    private CompanyConcise source;//来源
    private CompanyConcise target;//分配对象

    private boolean allocate;//是否时分配的


    public PlanAlliance() {
        count = quantity = boxCount = 0;
        volume = weight = 0D;
    }

    public static PlanAlliance buildAlliance(PlanOrder order) throws Exception {
        if (order instanceof PlanAlliance) {
            return (PlanAlliance) order;
        }
        return new PlanAlliance(order);
    }


    public PlanAlliance(PlanOrder order) throws Exception {
        this();
        BeanUtils.copyProperties(this, order);
    }

    public boolean isAllocate() {
        return allocate;
    }

    public void setAllocate(boolean allocate) {
        this.allocate = allocate;
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

    public Collection<PlanCommodity> getCommodities() {
        return commodities;
    }

    public void addCommodity(PlanCommodity commodity) {
        Collection<PlanCommodity> collection = Optional.ofNullable(commodities).orElseGet(() -> {
            commodities = new ArrayList<PlanCommodity>();
            return commodities;
        });
        if (commodity.getBoxCount() != null) {
            setBoxCount(boxCount + commodity.getBoxCount());
        }
        if (commodity.getQuantity() != null) {
            setQuantity(quantity + commodity.getQuantity());
        }
        if (commodity.getVolume() != null) {
            setVolume(volume + commodity.getVolume());
        }
        if (commodity.getWeight() != null) {
            setWeight(weight + commodity.getWeight());
        }
        collection.add(commodity);
        setCount(collection.size());
    }

    public void setCommodities(Collection<PlanCommodity> commodities) {
        if (CollectionUtils.isNotEmpty(commodities)) {
            for (PlanCommodity commodity : commodities) {
                addCommodity(commodity);
            }
        }
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getBoxCount() {
        return boxCount;
    }

    public void setBoxCount(Integer boxCount) {
        this.boxCount = boxCount;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        if (null != weight && weight > 0) {
            this.weight = NumberToCNUtils.formatDecimal(weight);
        } else {
            this.weight = weight;
        }
    }

    public Collection<CustomData> getCustomDatas() {
        return customDatas;
    }

    public void setCustomDatas(Collection<CustomData> customDatas) {
        this.customDatas = customDatas;
    }

    public OrderExtra getExtra() {
        return extra;
    }

    public void setExtra(OrderExtra extra) {
        this.extra = extra;
    }

    public PlanDesignate getDesignate() {
        return designate;
    }

    public void setDesignate(PlanDesignate designate) {
        this.designate = designate;
    }

    public CompanyConcise getSource() {
        return source;
    }

    public void setSource(CompanyConcise source) {
        this.source = source;
    }
    public CompanyConcise getTarget() {
        return target;
    }

    public void setTarget(CompanyConcise target) {
        this.target = target;
    }

    public PlanDesignate getAccept() {
        return accept;
    }

    public void setAccept(PlanDesignate accept) {
        this.accept = accept;
    }
}

