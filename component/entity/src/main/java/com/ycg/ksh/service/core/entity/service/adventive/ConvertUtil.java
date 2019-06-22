package com.ycg.ksh.service.core.entity.service.adventive;

import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.constant.ObjectType;
import com.ycg.ksh.entity.persistent.ImageStorage;
import com.ycg.ksh.entity.persistent.OrderCommodity;
import com.ycg.ksh.entity.persistent.enterprise.CustomData;
import com.ycg.ksh.entity.persistent.enterprise.OrderExtra;
import com.ycg.ksh.entity.service.AllianceLocation;
import com.ycg.ksh.entity.service.enterprise.OrderAlliance;
import com.ycg.ksh.entity.service.enterprise.OrderTemplate;
import org.apache.commons.collections4.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public abstract class ConvertUtil {

    private static final ThreadLocal<SimpleDateFormat> format = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));


    public static String format(Date date){
        if(date != null){
            return format.get().format(date);
        }
        return null;
    }

    public static Date parse(String dateString){
        if(StringUtils.isNotBlank(dateString)){
            try {
                return format.get().parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Object convert(ObjectType objectType, Object object){
        if(objectType == ObjectType.ORDER){
            return adventiveOrder((OrderAlliance) object);
        }
        if(objectType == ObjectType.LOCATIONTRACK){
            return adventiveTrack((AllianceLocation) object);
        }
        if(objectType == ObjectType.RECEIPT){
            return adventiveReceipt((ImageStorage) object);
        }
        return object;
    }

    public static AdventiveOrder adventiveOrder(OrderAlliance alliance){
        AdventiveOrder adventiveOrder = new AdventiveOrder();
        adventiveOrder.setKey(alliance.getId());
        adventiveOrder.setBindCode(alliance.getBindCode());
        if(alliance.getShipper() != null){
            adventiveOrder.setShipperKey(alliance.getShipper().getId());
            adventiveOrder.setShipperName(alliance.getShipper().getCompanyName());
        }
        if(alliance.getReceive() != null){
            adventiveOrder.setReceiveKey(alliance.getReceive().getId());
            adventiveOrder.setReceiveName(alliance.getReceive().getCompanyName());
        }
        if(alliance.getConvey() != null){
            adventiveOrder.setConveyKey(alliance.getConvey().getId());
            adventiveOrder.setConveyName(alliance.getConvey().getCompanyName());
        }
        adventiveOrder.setDeliveryNo(alliance.getDeliveryNo());
        adventiveOrder.setOrderNo(alliance.getOrderNo());

        adventiveOrder.setReceiverName(alliance.getReceiverName());
        adventiveOrder.setReceiverContact(alliance.getReceiverContact());
        adventiveOrder.setReceiveAddress(alliance.getReceiveAddress());
        if (alliance.getException() != null) {
            adventiveOrder.setException(alliance.getException().getContent());
        }
        adventiveOrder.setLocation(alliance.getLocation());

        adventiveOrder.setCreateTime(format(alliance.getCreateTime()));
        adventiveOrder.setDeliveryTime(format(alliance.getDeliveryTime()));
        adventiveOrder.setReceiveTime(format(alliance.getReceiveTime()));

        adventiveOrder.setFettle(alliance.getFettle());
        adventiveOrder.setSignFettle(alliance.getSignFettle());
        adventiveOrder.setRemark(alliance.getRemark());

        if(alliance.getCommodities() != null){
            adventiveOrder.setCommodities(adventiveCommoditys(alliance.getCommodities()));
        }

        if(alliance.getExtra() != null){
            adventiveOrder.setDistributeAddress(alliance.getExtra().getDistributeAddress());
            adventiveOrder.setOriginStation(alliance.getExtra().getOriginStation());
            adventiveOrder.setArrivalStation(alliance.getExtra().getArrivalStation());
            adventiveOrder.setStartStation(alliance.getExtra().getStartStation());
            adventiveOrder.setEndStation(alliance.getExtra().getEndStation());

            adventiveOrder.setCarNo(alliance.getExtra().getCareNo());
            adventiveOrder.setDriverName(alliance.getExtra().getDriverName());
            adventiveOrder.setDriverContact(alliance.getExtra().getDriverContact());

            adventiveOrder.setIncome(alliance.getExtra().getIncome());
            adventiveOrder.setExpenditure(alliance.getExtra().getExpenditure());

        }
        if(CollectionUtils.isNotEmpty(alliance.getCustomDatas())){
            adventiveOrder.setOthers(alliance.getCustomDatas().stream().collect(Collectors.toMap(c -> { return c.getCustomName();}, c -> { return c.getCustomValue();})));
        }
        return adventiveOrder;
    }

    public static  OrderTemplate orderTemplate(AdventiveOrder order){
        OrderTemplate template = new OrderTemplate();
        template.setUniqueKey(order.getKey());
        template.setShipperKey(order.getShipperKey());
        template.setShipperName(order.getShipperName());
        template.setReceiveKey(order.getReceiveKey());
        template.setReceiveName(order.getReceiveName());
        template.setConveyKey(order.getConveyKey());
        template.setConveyName(order.getConveyName());
        template.setDeliveryNo(order.getDeliveryNo());
        template.setOrderNo(order.getOrderNo());

        template.setReceiverName(order.getReceiverName());
        template.setReceiverContact(order.getReceiverContact());
        template.setReceiveAddress(order.getReceiveAddress());
        template.setDeliveryTime(parse(order.getDeliveryTime()));
        template.setReceiveTime(parse(order.getReceiveTime()));
        template.setRemark(order.getRemark());

        return template;
    }
    public static OrderExtra orderExtra(AdventiveOrder order){
        OrderExtra orderExtra = new OrderExtra();

        orderExtra.setDistributeAddress(order.getDistributeAddress());
        orderExtra.setOriginStation(order.getOriginStation());
        orderExtra.setArrivalStation(order.getArrivalStation());
        orderExtra.setStartStation(order.getStartStation());
        orderExtra.setEndStation(order.getEndStation());

        orderExtra.setCareNo(order.getCarNo());
        orderExtra.setDriverName(order.getDriverName());
        orderExtra.setDriverContact(order.getDriverContact());

        orderExtra.setIncome(order.getIncome());
        orderExtra.setExpenditure(order.getExpenditure());

        return orderExtra;
    }

    public static OrderCommodity orderCommodity(AdventiveCommodity adventiveCommodity){
        OrderCommodity orderCommodity = new OrderCommodity();
        orderCommodity.setCommodityName(adventiveCommodity.getCommodityName());
        orderCommodity.setCommodityNo(adventiveCommodity.getCommodityNo());
        orderCommodity.setCommodityType(adventiveCommodity.getCommodityType());
        orderCommodity.setCommodityUnit(adventiveCommodity.getCommodityUnit());
        orderCommodity.setBoxCount(adventiveCommodity.getBoxCount());
        orderCommodity.setQuantity(adventiveCommodity.getQuantity());
        orderCommodity.setVolume(adventiveCommodity.getVolume());
        orderCommodity.setWeight(adventiveCommodity.getWeight());
        orderCommodity.setRemark(adventiveCommodity.getRemark());
        return orderCommodity;
    }


    public static AdventiveCommodity adventiveCommodity(OrderCommodity orderCommodity){
        AdventiveCommodity adventiveCommodity = new AdventiveCommodity();
        adventiveCommodity.setCommodityName(orderCommodity.getCommodityName());
        adventiveCommodity.setCommodityNo(orderCommodity.getCommodityNo());
        adventiveCommodity.setCommodityType(orderCommodity.getCommodityType());
        adventiveCommodity.setCommodityUnit(orderCommodity.getCommodityUnit());
        adventiveCommodity.setBoxCount(orderCommodity.getBoxCount());
        adventiveCommodity.setQuantity(orderCommodity.getQuantity());
        adventiveCommodity.setVolume(orderCommodity.getVolume());
        adventiveCommodity.setWeight(orderCommodity.getWeight());
        adventiveCommodity.setRemark(orderCommodity.getRemark());
        return adventiveCommodity;
    }


    public static Collection<CustomData> customData(Map<String, Object> others){
        if (others != null && !others.isEmpty()){
            Collection<CustomData> collection = new ArrayList<CustomData>();
            for (Map.Entry<String, Object> entry : others.entrySet()) {
                collection.add(new CustomData(entry.getKey(), Optional.ofNullable(entry.getValue()).orElse(StringUtils.EMPTY).toString()));
            }
            return collection;
        }
        return null;
    }


    public static Collection<AdventiveCommodity> adventiveCommoditys(Collection<OrderCommodity> commodities){
        if(CollectionUtils.isNotEmpty(commodities)){
            return commodities.parallelStream().map(ConvertUtil::adventiveCommodity).collect(Collectors.toList());
        }
        return null;
    }

    public static AdventiveReceipt adventiveReceipt(ImageStorage imageStorage){
        return new AdventiveReceipt(format(imageStorage.getStorageTime()), SystemUtils.imagepath(imageStorage.getStoragePath()));//回单图片
    }

    public static Collection<AdventiveReceipt> adventiveReceipts(Collection<ImageStorage> imageStorages){
        if(CollectionUtils.isNotEmpty(imageStorages)){
            return imageStorages.parallelStream().map(ConvertUtil::adventiveReceipt).collect(Collectors.toList());
        }
        return null;
    }

    public static AdventiveTrack adventiveTrack(AllianceLocation l){
        return new AdventiveTrack(l.getLongitude(), l.getLatitude(), l.getLocation(), format(l.getCreateTime()), l.getReporterName(), l.getReporterMobile());
    }

    public static Collection<AdventiveTrack> adventiveTracks(Collection<AllianceLocation> locationTracks){
        if(CollectionUtils.isNotEmpty(locationTracks)){
            return locationTracks.parallelStream().map(ConvertUtil::adventiveTrack).collect(Collectors.toList());
        }
        return null;
    }
}
