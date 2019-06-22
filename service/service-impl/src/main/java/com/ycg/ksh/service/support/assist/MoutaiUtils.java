package com.ycg.ksh.service.support.assist;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ycg.ksh.entity.persistent.moutai.Convey;
import com.ycg.ksh.entity.persistent.moutai.Customer;
import com.ycg.ksh.entity.persistent.moutai.Order;
import com.ycg.ksh.entity.persistent.moutai.Taker;
import com.ycg.ksh.entity.service.moutai.OrderPrint;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MoutaiUtils {



    //打印的时候一页最多*条数据,超过*条分割
    public static final int PAGE_SIZE = 8;
    /** 承运商  id-> 承运商*/
    public static Map<Long, Convey> conveyConvert(Collection<Convey> collection){
        if (CollectionUtils.isNotEmpty(collection)) {
            return collection.parallelStream().collect(Collectors.toMap(c ->{
                return c.getId();
            }, Function.identity()));
        }
        return Collections.emptyMap();
        /*
        Map<String, Convey> conveyMap = Maps.newHashMap();         //承运商
        for(Convey convey :conveys) {
            conveyMap.put(String.valueOf(convey.getId()), convey);
        }
        return conveyMap;
        */
    }
    /** 承运商  承运商编号(唯一)-> 承运商*/
    public  static Map<String, Customer> conveyCustomer(Collection<Customer> collection){
        if (CollectionUtils.isNotEmpty(collection)) {
            return collection.parallelStream().collect(Collectors.toMap(c ->{
                return c.getCustomerNo();
            }, Function.identity()));
        }
        return Collections.emptyMap();

        /*
        Map<String, Customer> customerMap = Maps.newHashMap();   //收货客户
        for (Customer customer : customers) {
            customerMap.put(customer.getCustomerNo(), customer);
        }
        return customerMap;
        */
    }

    /** 收货人 承运商Id-> 当前启用的收货人*/
    public static Map<Long, Taker> conveyTaker(Collection<Taker> collection ){
        if (CollectionUtils.isNotEmpty(collection)) {
            return collection.stream().collect(Collectors.toMap(t ->{
                return t.getConveyId();
            }, Function.identity()));
        }
        return Collections.emptyMap();
        /*
        Map<String, Taker> takerMap = Maps.newHashMap();
        for (Taker taker : takers) {
            takerMap.put(String.valueOf(taker.getConveyId()), taker);
        }
        return takerMap;
        */
    }
    /*
     * 超过八条需要分成多条
     */
    public static  Map<String, List<OrderPrint>> handle( Map<String , List<OrderPrint>> during){
        Map<String,  List<OrderPrint>> result = Maps.newHashMap();
        for (String key : during.keySet()) {
            List<OrderPrint> list = during.get(key);
            int size;
            if((size = list.size()) > PAGE_SIZE){
                double ceil = Math.ceil((double)size / PAGE_SIZE);  //一共需要分成ceil页
                for (int i = 0; i < ceil; i++) {
                    String resultKey = key + "-" + i;
                    int start = PAGE_SIZE*i;
                    int end = (PAGE_SIZE+start) > size ? size : (PAGE_SIZE+start);
                    List<OrderPrint> orderPrints = list.subList(PAGE_SIZE * i, end);
                    result.put(resultKey, orderPrints);
                }
            }else{
                result.put(key,list);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        System.out.println(Math.ceil(10d/3));
    }

    /**
     * @Title: ${file_name}
     * @Description: 以后可能会将报错的类型分离出来
     * @author
     * @date 2018/4/28 10:32
     */
    
    public static  List<Long> handleFilterOrderPrint(Collection<Order> orders, List<Long> conveyList, List<String> customerList , Map<Long,Taker> takerMap) {
        List<Long> errorId = Lists.newArrayList();
        for (Order order : orders) {
            if(order.getStoreId() == null || order.getStoreId()<1){
                //未勾选仓库
                System.out.println("仓库"+order.getStoreId());
                errorId.add(order.getId());
                continue;
            }
            if(!conveyList.contains(order.getConveyId())){
                //承运商
                System.out.println("承运商"+order.getConveyId());
                errorId.add(order.getId());
                continue;
            }
            if(!customerList.contains(order.getCustomerNo())){
                //客户编码
                System.out.println("客户编码"+order.getCustomerNo());
                errorId.add(order.getId());
                continue;
            }
            System.out.println(takerMap.keySet().toArray().toString()+ order.getConveyId());
            if(takerMap.get(order.getConveyId()) == null){
                //取货人都处于禁用状态
                System.out.println("取货人都处于禁用状态"+order.getConveyId());
                errorId.add(order.getId());
                continue;
            }
        }
        return errorId;
    }


}
