package com.ycg.ksh.service.util;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/14
 */

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.RegionUtils;
import com.ycg.ksh.entity.persistent.OrderCommodity;
import com.ycg.ksh.entity.persistent.enterprise.CustomData;
import com.ycg.ksh.entity.persistent.enterprise.OrderExtra;
import com.ycg.ksh.entity.persistent.plan.PlanCommodity;
import com.ycg.ksh.entity.service.enterprise.*;
import com.ycg.ksh.entity.service.plan.PlanAlliance;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 对象转换
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/14
 */
public abstract class Transform {

    private static final String DOT = ".";

    private static String attribute(TemplateDescribe describe) {
        return describe.getClassName() + DOT + describe.getDataKey();
    }

    private static void append(BaseEntity entity, Map<String, TemplateDescribe> mappings, Collection<TemplateDescribe> collection) {
        if (entity != null) {
            if (entity instanceof CustomData) {
                CustomData customData = (CustomData) entity;
                TemplateDescribe templateDescribe = mappings.get(entity.getClass().getName() + DOT + customData.getCustomName());
                if (templateDescribe != null) {
                    collection.add(new TemplateDescribe(templateDescribe, customData.getCustomValue()));
                }
            } else {
                for (Map.Entry<String, Object> entry : entity.toMap().entrySet()) {
                    TemplateDescribe templateDescribe = mappings.get(entity.getClass().getName() + DOT + entry.getKey());
                    if (templateDescribe != null) {
                        collection.add(new TemplateDescribe(templateDescribe, entry.getValue()));
                    }
                }
            }
        }
    }

    public static Collection<TemplateDescribe> transform(Collection<TemplateDescribe> describes, PlanAlliance alliance) {
        Map<String, TemplateDescribe> mappings = describes.stream().collect(Collectors.toMap(Transform::attribute, (p) -> p, (p, c) -> p));
        Collection<TemplateDescribe> collection = new ArrayList<TemplateDescribe>();

        OrderTemplate template = new OrderTemplate();
        CustomerConcise shipper = alliance.getShipper(), receive = alliance.getReceive(), convey = alliance.getConvey();
        if (alliance.getShipperId() != null && alliance.getShipperId() > 0) {
            template.setShipperName(shipper.getCustomerName());
        }
        template.setReceiveName(receive.getCustomerName());
        if (convey != null) {
            template.setConveyName(convey.getCustomerName());
        }
        template.setDeliveryTime(alliance.getDeliveryTime());
        template.setArrivalTime(alliance.getArrivalTime());
        template.setRemark(alliance.getRemark());
        template.setReceiverName(alliance.getReceiverName());
        template.setReceiverContact(alliance.getReceiverContact());
        template.setReceiveAddress(alliance.getReceiveAddress());
        template.setOrderNo(alliance.getOrderNo());

        append(template, mappings, collection);

        append(alliance.getExtra(), mappings, collection);

        Collection<OrderCommodity> commodities = Optional.ofNullable(alliance.getCommodities()).map(cc -> {
            return cc.stream().map(c -> {
                OrderCommodity commodity = new OrderCommodity();
                commodity.copyProperties(c);
                return commodity;
            }).collect(Collectors.toList());
        }).orElse(Collections.emptyList());
        for (OrderCommodity commodity : commodities) {
            append(commodity, mappings, collection);
        }
        if (CollectionUtils.isNotEmpty(alliance.getCustomDatas())) {
            for (CustomData customData : alliance.getCustomDatas()) {
                append(customData, mappings, collection);
            }
        }
        return collection;
    }
    public static OrderTemplate orderTemplateform(PlanAlliance alliance) {
        OrderTemplate template = new OrderTemplate();
        if(alliance != null){
            if (alliance.getShipper() != null) {
                template.setShipperKey(alliance.getShipper().getId());
                template.setShipperName(alliance.getShipper().getCustomerName());
            }
            if (alliance.getReceive() != null) {
                template.setReceiveKey(alliance.getReceive().getId());
                template.setReceiveName(alliance.getReceive().getCustomerName());
            }
            if (alliance.getConvey() != null) {
                template.setConveyKey(alliance.getConvey().getId());
                template.setConveyName(alliance.getConvey().getCustomerName());
            }
            if(alliance.getCollectTime() != null){
                template.setCollectTime(alliance.getCollectTime());
            }
            template.setDeliveryTime(alliance.getDeliveryTime());
            template.setArrivalTime(alliance.getArrivalTime());
            template.setRemark(alliance.getRemark());
            template.setReceiverName(alliance.getReceiverName());
            template.setReceiverContact(alliance.getReceiverContact());
            template.setReceiveAddress(alliance.getReceiveAddress());
            template.setOrderNo(alliance.getOrderNo());
        }
        return template;
    }
    public static OrderExtra orderExtraform(OrderExtra orderExtra) {
        OrderExtra extra = new OrderExtra();
        if(orderExtra != null){
            extra.setDistributeAddress(orderExtra.getDistributeAddress());
            extra.setOriginStation(orderExtra.getOriginStation());
            extra.setArrivalStation(orderExtra.getArrivalStation());
            extra.setStartStation(orderExtra.getStartStation());
            extra.setEndStation(orderExtra.getEndStation());
            extra.setConveyerName(orderExtra.getConveyerName());
            extra.setConveyerContact(orderExtra.getConveyerContact());
            extra.setCareNo(orderExtra.getCareNo());
            extra.setDriverName(orderExtra.getDriverName());
            extra.setDriverContact(orderExtra.getDriverContact());
            extra.setIncome(orderExtra.getIncome());
            extra.setExpenditure(orderExtra.getExpenditure());
        }
        return extra;
    }
    public static OrderCommodity orderCommodityform(PlanCommodity commodity) {
        OrderCommodity object = new OrderCommodity();
        if(commodity != null){
            object.setCommodityName(commodity.getCommodityName());
            object.setCommodityNo(commodity.getCommodityNo());
            object.setCommodityType(commodity.getCommodityType());
            object.setCommodityUnit(commodity.getCommodityUnit());
            object.setQuantity(commodity.getQuantity());
            object.setVolume(commodity.getVolume());
            object.setWeight(commodity.getWeight());
            object.setBoxCount(commodity.getBoxCount());
            object.setRemark(commodity.getRemark());
        }
        return object;
    }
    public static CustomData customDataform(CustomData customData) {
        return new CustomData(customData.getCustomName(), customData.getCustomValue());
    }
    public static Map<String, Object> transform(PlanAlliance alliance) {
        Map<String, Object> results = new HashMap<String, Object>(4);
        CustomerConcise shipper = alliance.getShipper(), receive = alliance.getReceive(), convey = alliance.getConvey();
        Map<String, Object> values = new HashMap<String, Object>(13);
        if (shipper != null) {
            values.put("shipperName", shipper.getCustomerName());
            values.put("shipperKey", shipper.getId());
        }
        if (receive != null) {
            values.put("receiveName", receive.getCustomerName());
            values.put("receiveKey", receive.getId());
        }
        if (convey != null) {
            values.put("conveyName", convey.getCustomerName());
            values.put("conveyKey", convey.getId());
        }
        values.put("deliveryTime", alliance.getDeliveryTime());
        values.put("arrivalTime", alliance.getArrivalTime());
        values.put("remark", alliance.getRemark());
        values.put("receiverName", alliance.getReceiverName());
        values.put("receiverContact", alliance.getReceiverContact());
        values.put("receiveAddress", RegionUtils.analyze(alliance.getReceiveAddress(), true));
        values.put("orderNo", alliance.getOrderNo());
        values.put("planNo", alliance.getPlanNo());
        values.put("transportRoute", alliance.getTransportRoute());
        values.put("collectTime", alliance.getCollectTime());
        values.put("id", alliance.getId());
        results.put("order", values);

        OrderExtra orderExtra = Optional.ofNullable(alliance.getExtra()).orElse(new OrderExtra());
        Map<String, Object> vs = new HashMap<String, Object>(13);
        vs.put("distributeAddress", RegionUtils.analyze(orderExtra.getDistributeAddress(), true));
        vs.put("originStation", RegionUtils.analyze(orderExtra.getOriginStation(), false));
        vs.put("arrivalStation", RegionUtils.analyze(orderExtra.getArrivalStation(), false));
        vs.put("startStation", orderExtra.getStartStation());
        vs.put("endStation", orderExtra.getEndStation());
        vs.put("conveyerName", orderExtra.getConveyerName());
        vs.put("conveyerContact", orderExtra.getConveyerContact());
        vs.put("careNo", orderExtra.getCareNo());
        vs.put("driverName", orderExtra.getDriverName());
        vs.put("driverContact", orderExtra.getDriverContact());
        vs.put("income", orderExtra.getIncome());
        vs.put("expenditure", orderExtra.getExpenditure());
        vs.put("key",orderExtra.getKey());

        results.put("extra", vs);

        if (CollectionUtils.isNotEmpty(alliance.getCommodities())) {
            Collection<Map<String, Object>> commodities = new ArrayList<Map<String, Object>>(alliance.getCommodities().size());
            for (PlanCommodity commodity : alliance.getCommodities()) {
                Map<String, Object> cs = new HashMap<String, Object>(9);
                cs.put("commodityName", commodity.getCommodityName());
                cs.put("commodityNo", commodity.getCommodityNo());
                cs.put("commodityType", commodity.getCommodityType());
                cs.put("commodityUnit", commodity.getCommodityUnit());
                cs.put("quantity", commodity.getQuantity());
                cs.put("volume", commodity.getVolume());
                cs.put("weight", commodity.getWeight());
                cs.put("boxCount", commodity.getBoxCount());
                cs.put("remark", commodity.getRemark());
                cs.put("id", commodity.getId());
                commodities.add(cs);
            }
            results.put("commodities", commodities);
        }
        if (CollectionUtils.isNotEmpty(alliance.getCustomDatas())) {
            Collection<String[]> ds = new ArrayList<String[]>(alliance.getCustomDatas().size());
            for (CustomData customData : alliance.getCustomDatas()) {
                ds.add(new String[]{customData.getCustomName(), customData.getCustomValue()});
            }
            results.put("customDatas", ds);
        }
        return results;
    }

    public static Map<String, Object> transform(OrderAlliance alliance) {
        Map<String, Object> results = new HashMap<String, Object>(4);
        CustomerConcise shipper = alliance.getShipper(), receive = alliance.getReceive(), convey = alliance.getConvey();
        Map<String, Object> values = new HashMap<String, Object>(13);
        values.put("uniqueKey", alliance.getId());
        if(shipper != null){
            values.put("shipperName", shipper.getCustomerName());
            values.put("shipperKey", shipper.getId());
        }
        if (receive != null) {
            values.put("receiveName", receive.getCustomerName());
            values.put("receiveKey", receive.getId());
        }
        if (convey != null) {
            values.put("conveyName", convey.getCustomerName());
            values.put("conveyKey", convey.getId());
        }
        values.put("deliveryTime", alliance.getDeliveryTime());
        values.put("arrivalTime", alliance.getArrivalTime());
        values.put("remark", alliance.getRemark());
        values.put("receiverName", alliance.getReceiverName());
        values.put("receiverContact", alliance.getReceiverContact());
        values.put("receiveAddress", RegionUtils.analyze(alliance.getReceiveAddress(), true));
        values.put("orderNo", alliance.getOrderNo());
        values.put("bindCode", alliance.getBindCode());
        values.put("deliveryNo", alliance.getDeliveryNo());
        results.put("order", values);

        OrderExtra orderExtra = Optional.ofNullable(alliance.getExtra()).orElse(new OrderExtra());
        Map<String, Object> vs = new HashMap<String, Object>(13);
        vs.put("distributeAddress", RegionUtils.analyze(orderExtra.getDistributeAddress(), true));
        vs.put("originStation", RegionUtils.analyze(orderExtra.getOriginStation(), false));
        vs.put("arrivalStation", RegionUtils.analyze(orderExtra.getArrivalStation(), false));
        vs.put("startStation", orderExtra.getStartStation());
        vs.put("endStation", orderExtra.getEndStation());
        vs.put("conveyerName", orderExtra.getConveyerName());
        vs.put("conveyerContact", orderExtra.getConveyerContact());
        vs.put("careNo", orderExtra.getCareNo());
        vs.put("driverName", orderExtra.getDriverName());
        vs.put("driverContact", orderExtra.getDriverContact());
        vs.put("income", orderExtra.getIncome());
        vs.put("expenditure", orderExtra.getExpenditure());

        results.put("extra", vs);

        if (CollectionUtils.isNotEmpty(alliance.getCommodities())) {
            Collection<Map<String, Object>> commodities = new ArrayList<Map<String, Object>>(alliance.getCommodities().size());
            for (OrderCommodity commodity : alliance.getCommodities()) {
                Map<String, Object> cs = new HashMap<String, Object>(9);
                cs.put("commodityName", commodity.getCommodityName());
                cs.put("commodityNo", commodity.getCommodityNo());
                cs.put("commodityType", commodity.getCommodityType());
                cs.put("commodityUnit", commodity.getCommodityUnit());
                cs.put("quantity", commodity.getQuantity());
                cs.put("volume", commodity.getVolume());
                cs.put("weight", commodity.getWeight());
                cs.put("boxCount", commodity.getBoxCount());
                cs.put("remark", commodity.getRemark());
                commodities.add(cs);
            }
            results.put("commodities", commodities);
        }
        if (CollectionUtils.isNotEmpty(alliance.getCustomDatas())) {
            Collection<String[]> ds = new ArrayList<String[]>(alliance.getCustomDatas().size());
            for (CustomData customData : alliance.getCustomDatas()) {
                ds.add(new String[]{customData.getCustomName(), customData.getCustomValue()});
            }
            results.put("customDatas", ds);
        }
        return results;
    }

}
