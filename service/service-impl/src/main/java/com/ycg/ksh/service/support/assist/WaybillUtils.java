package com.ycg.ksh.service.support.assist;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/3
 */

import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.util.DateUtils;
import com.ycg.ksh.common.util.RegionUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.Goods;
import com.ycg.ksh.entity.persistent.Waybill;
import com.ycg.ksh.entity.service.WaybillContext;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * 任务单工具类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/3
 */
public class WaybillUtils {


    // 计算已运天数
    public static Integer transportDays(Waybill waybill) {
        Date endDate = null;
        if (waybill.getActualArrivalTime() != null) {
            endDate = waybill.getActualArrivalTime();
        } else {
            endDate = new Date();
        }
        return DateUtils.differentDays(waybill.getCreatetime(), endDate);
    }

    public static Integer[] relativelys(Date createTime, Date bindTime, Date deliveryTime, Date arrivalTime) {
        Integer[] relativelys = new Integer[2];
        if(arrivalTime != null){
            Date relativelyTime = relativelyTime(createTime, bindTime, deliveryTime);
            Calendar calendar = Calendar.getInstance();
            relativelys[0] = DateUtils.differentDays(calendar, relativelyTime, arrivalTime);
            calendar.setTime(arrivalTime);
            relativelys[1] = calendar.get(Calendar.HOUR_OF_DAY);
        }else{
            relativelys[0] = relativelys[1] = null;
        }
        return relativelys;
    }

    public static Date relativelyTime(Date createTime, Date bindTime, Date deliveryTime){
        return (deliveryTime != null ? deliveryTime : (bindTime != null ? bindTime : createTime));
    }

    public static boolean validateRelatively(WaybillContext context) {
        Waybill persistence = context.getPersistence(), update = context.getUpdate();
        if(update.getCreatetime() != null || update.getDeliveryTime() != null || update.getBindTime() != null){
            return true;
        }
        if((context.getArriveDay() != null && context.getArriveDay() >= 0) || (context.getArriveHour() != null && context.getArriveHour() >= 0)){
            return true;
        }
        return false;
    }

    public static Date arrivaltime(WaybillContext context) {
        if(context.getArriveDay() != null && context.getArriveDay() >= 0){
            Date relativelyTime = relativelyTime(context.getCreatetime(), context.getBindTime(), context.getDeliveryTime());
            return arrivaltime(relativelyTime, context.getArriveDay(), context.getArriveHour());
        }else{
            Waybill p = context.getPersistence();
            if(p != null && p.getArrivaltime() != null){
                Integer[] relativelys = relativelys(p.getCreatetime(), p.getBindTime(), p.getDeliveryTime(), p.getArrivaltime());
                if(relativelys[0] != null && relativelys[0] != relativelys[1]){
                    Date relativelyTime = relativelyTime(context.getCreatetime(), context.getBindTime(), context.getDeliveryTime());
                    return arrivaltime(relativelyTime, relativelys[0], relativelys[1]);
                }
            }
        }
        return null;
    }

    public static Date arrivaltime(Date relativelyTime, Waybill w) {
        if(w.getArrivaltime() != null){
            Integer[] relativelys = relativelys(w.getCreatetime(), w.getBindTime(), w.getDeliveryTime(), w.getArrivaltime());
            if(relativelys[0] != null && relativelys[0] != relativelys[1]){
                return arrivaltime(relativelyTime, relativelys[0], relativelys[1]);
            }
        }
        return null;
    }

    public static Date arrivaltime(Waybill waybill, Integer arriveday, Integer arrivehour) {
        if(arriveday != null && arriveday >= 0){
            Date relativelyTime = relativelyTime(waybill.getCreatetime(), waybill.getBindTime(), waybill.getDeliveryTime());
            return arrivaltime(relativelyTime, arriveday, arrivehour);
        }
        return null;
    }

    public static Date arrivaltime(Date relativelyTime, Integer arriveday, Integer arrivehour) {
        if (arriveday != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(relativelyTime);
            calendar.add(Calendar.DAY_OF_YEAR, arriveday);
            if (arrivehour != null) {
                calendar.set(Calendar.HOUR_OF_DAY, arrivehour);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
            }
            return calendar.getTime();
        }
        return null;
    }

    public static void completeSomething(WaybillContext context) {
        WaybillUtils.completeSomething(context.getUpdate(), context.getCommodities());
        if (StringUtils.isNotBlank(context.getUpdate().getStartStation())) {
            String[] arrays = RegionUtils.split(context.getStartStation());
            context.setStartStation(RegionUtils.merge(arrays));
            context.setSimpleStartStation(RegionUtils.simple(arrays));
        }
        if (StringUtils.isNotBlank(context.getUpdate().getEndStation())) {
            String[] arrays = RegionUtils.split(context.getEndStation());
            context.setEndStation(RegionUtils.merge(arrays));
            context.setSimpleEndStation(RegionUtils.simple(arrays));
        }
    }

    public static Waybill initializeSomething(Waybill waybill, Integer status) {
        if (waybill.getGroupid() == null) {
            waybill.setGroupid(0);
        }
        waybill.setWaybillStatus(status);
        waybill.setCreatetime(new Date());
        waybill.setPaperyReceiptStatus(Constant.PAPERY_RECEIPT_STATUS_NOT);
        waybill.setUpdatetime(waybill.getCreatetime());
        waybill.setPositionCount(0);
        waybill.setReceiptCount(0);
        waybill.setReceiptVerifyCount(0);
        waybill.setReceiptVerifyStatus(Constant.RECEIPT_VERIFY_STATUS_NO);
        waybill.setDelay(Constant.WAYBILL_DAY_WAIT);
        if (waybill.getFenceRadius() == null) {
            waybill.setFenceRadius(5d);
        }
        if (waybill.getFenceStatus() == null) {
            waybill.setFenceStatus(Constant.GRATESATUS_ON);//电子围栏默认开启
        }
        return waybill;
    }

    public static void completeSomething(Waybill waybill, Collection<Goods> collection) {
        if (CollectionUtils.isNotEmpty(collection)) {
            int q = 0;
            double w = 0D, v = 0D;
            for (Iterator<Goods> iterator = collection.iterator(); iterator.hasNext(); ) {
                Goods goods = iterator.next();
                boolean remove = true;
                if (goods.getGoodsQuantity() != null && goods.getGoodsQuantity() > 0) {
                    remove = false;
                    q += goods.getGoodsQuantity();
                } else {
                    goods.setGoodsQuantity(0);
                }
                if (goods.getGoodsWeight() != null && goods.getGoodsWeight() > 0) {
                    remove = false;
                    w += goods.getGoodsWeight();
                } else {
                    goods.setGoodsWeight(0D);
                }
                if (goods.getGoodsVolume() != null && goods.getGoodsVolume() > 0) {
                    remove = false;
                    v += goods.getGoodsVolume();
                } else {
                    goods.setGoodsVolume(0D);
                }
                if (remove) {
                    iterator.remove();
                }
            }
            waybill.setNumber(q);
            waybill.setWeight(w);
            waybill.setVolume(v);
        }else{
            waybill.setNumber(null);
            waybill.setWeight(null);
            waybill.setVolume(null);
        }
    }
}
