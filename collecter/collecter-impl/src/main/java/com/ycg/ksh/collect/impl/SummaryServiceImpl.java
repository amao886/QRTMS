package com.ycg.ksh.collect.impl;

import com.ycg.ksh.adapter.api.MessageQueueService;
import com.ycg.ksh.collect.jdbc.CollectJdbcTemplate;
import com.ycg.ksh.collect.jdbc.ServiceJdbcTemplate;
import com.ycg.ksh.collect.observer.DaySummaryBeginObserverAdapter;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.extend.rabbitmq.MediaMessage;
import com.ycg.ksh.common.extend.rabbitmq.QueueKeys;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.constant.CompanyConfigType;
import com.ycg.ksh.constant.CoreConstants;
import com.ycg.ksh.constant.LocationType;
import com.ycg.ksh.constant.SignFettle;
import com.ycg.ksh.entity.collecter.DaySummary;
import com.ycg.ksh.entity.collecter.OrderCollect;
import com.ycg.ksh.entity.collecter.SummaryOrderAssess;
import com.ycg.ksh.entity.common.constant.PartnerType;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 统计
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/10/30 0030
 */
@Service("ksh.collect.summaryService")
public class SummaryServiceImpl implements DaySummaryBeginObserverAdapter {


        private final Logger logger = LoggerFactory.getLogger(SummaryServiceImpl.class);

        private static final String SUMMARY_TYPE_ORDERTIMEOUT = "com.ycg.ksh.entity.collecter.SummaryOrderTimeout";//统计订单超时签收
        private static final String SUMMARY_TYPE_ORDERTRACK = "com.ycg.ksh.entity.collecter.SummaryOrderTrack";//统计订单跟踪率
        private static final String SUMMARY_TYPE_ORDERASSESS = "com.ycg.ksh.entity.collecter.SummaryOrderAssess";//统计订单考核数据

        private static final int TRACK_PASS_LEVEL = 100;

        @Resource
        ServiceJdbcTemplate serviceJdbcTemplate;
        @Resource
        CollectJdbcTemplate collectJdbcTemplate;


        @Resource
        MessageQueueService queueService;

        private boolean createTableNotExist(String tableName, String createDll){
                if(!collectJdbcTemplate.existTableName(tableName)){
                        collectJdbcTemplate.createTable(String.format(createDll, tableName));
                        logger.info("新建表---> {}", tableName);
                }
                return true;
        }

        @Override
        public String[] loadSupportSummary() {
                return new String[]{SUMMARY_TYPE_ORDERTIMEOUT, SUMMARY_TYPE_ORDERTRACK, SUMMARY_TYPE_ORDERASSESS};
        }

        @Override
        public LocalDateTime daySummaryBegin(DaySummary daySummary) throws BusinessException {
                if (SUMMARY_TYPE_ORDERTIMEOUT.equalsIgnoreCase(daySummary.getSummaryNo())) {
                        return summaryOrderTimeout(daySummary);//统计订单超时签收;
                }
                if (SUMMARY_TYPE_ORDERTRACK.equalsIgnoreCase(daySummary.getSummaryNo())) {
                        return summaryOrderTrack(daySummary);//订单跟踪率
                }
                if (SUMMARY_TYPE_ORDERASSESS.equalsIgnoreCase(daySummary.getSummaryNo())) {
                        return summaryOrderAssess(daySummary);//订单考核统计
                }
                return null;
        }

        @Transactional
        LocalDateTime summaryOrderTimeout(DaySummary daySummary) {
                Collection<OrderCollect> collection = serviceJdbcTemplate.collectOrderSummaryTimeout(daySummary.getLastTime().minusDays(15L));
                if (CollectionUtils.isNotEmpty(collection)) {
                        LocalDateTime now = LocalDateTime.now();
                        Collection<Long> timeoutKeys = new ArrayList<Long>();
                        for (OrderCollect collect : collection) {
                                if (collect.getArrivedTime() != null) {//已经到货了
                                        if (collect.getArrivedTime().plusDays(15L).isBefore(now)) {
                                                timeoutKeys.add(collect.getId());
                                        }
                                } else {
                                        if (collect.getDeliveryTime().plusDays(30L).isBefore(now)) {
                                                timeoutKeys.add(collect.getId());
                                        }
                                }
                        }
                        if (CollectionUtils.isNotEmpty(timeoutKeys)) {
                                queueService.sendCoreMessage(timeoutKeys.stream().map(k -> new MediaMessage(QueueKeys.MESSAGE_TYPE_ORDER_TIMEOUT, Globallys.UUID(), k)).collect(Collectors.toList()));
                        }
                }
                return null;
        }
        @Transactional
        LocalDateTime summaryOrderTrack(DaySummary daySummary) {
                Collection<OrderCollect> collection = serviceJdbcTemplate.collectOrderSummaryTrack(daySummary.getLastTime().minusDays(1L));
                if (CollectionUtils.isNotEmpty(collection)) {
                        Map<Long, Integer> cache = new ConcurrentHashMap<Long, Integer>();
                        for (Iterator<OrderCollect> iterator = collection.iterator(); iterator.hasNext(); ) {
                                OrderCollect collect = iterator.next();
                                Long companyKey = companyKey(collect);
                                if (companyKey == null || companyKey <= 0) {
                                        iterator.remove();
                                }
                                Integer loactionCount = loactionCount(companyKey, cache);
                                if (loactionCount == null || loactionCount <= 0) {
                                        collect.setPositioningCheck(TRACK_PASS_LEVEL);
                                } else {
                                        float count = serviceJdbcTemplate.collectLocateCount(LocationType.ORDER, collect.getId());
                                        if (count <= 0) {
                                                collect.setPositioningCheck(0);
                                        } else {
                                                collect.setPositioningCheck(new Float(count / new Float(loactionCount) * TRACK_PASS_LEVEL).intValue());
                                        }
                                }
                        }
                        if (CollectionUtils.isNotEmpty(collection)) {
                                serviceJdbcTemplate.modifyOrderPositioningCheck(collection);
                        }
                }
                return null;
        }
        @Transactional
        LocalDateTime summaryOrderAssess(DaySummary daySummary) {
                LocalDateTime yesterday = daySummary.getLastTime().minusDays(1L);
                Collection<OrderCollect> collection = serviceJdbcTemplate.collectOrderSummaryAssess(daySummary.getLeastTime(), yesterday);
                if (CollectionUtils.isNotEmpty(collection)) {
                        Map<String, SummaryOrderAssess> summaryAssesss = new HashMap<String, SummaryOrderAssess>();
                        for (OrderCollect summary : collection) {
                                LocalDate summaryDay = summary.getDeliveryTime().toLocalDate();
                                for (PartnerType partnerType : PartnerType.values()) {
                                        Long companyKey = companyKey(partnerType, summary);
                                        Long[] otherSideKeys = otherSideKeys(partnerType, summary) ;
                                        //System.out.println(companyKey +" : "+ partnerType +"-->"+ Arrays.toString(otherSideKeys));
                                        if (companyKey > 0 && partnerType.registered(summary.getClientType()) && otherSideKeys != null && otherSideKeys.length > 0) {
                                                for (Long otherSideKey : otherSideKeys) {
                                                        String summaryNo = SummaryOrderAssess.summaryNo(summaryDay, companyKey, partnerType.getCode(), otherSideKey);
                                                        System.out.println((summaryDay.format(Globallys.DF_YMD) +" "+ companyKey +" "+partnerType +" "+ otherSideKey));
                                                        SummaryOrderAssess summaryOrderAssess = summaryAssesss.getOrDefault(summaryNo, new SummaryOrderAssess(summaryDay, companyKey, partnerType.getCode(), otherSideKey));
                                                        summaryAssesss.put(summaryNo, reduceAssess(summary, summaryOrderAssess));
                                                }
                                        }
                                }
                        }
                        if (!summaryAssesss.isEmpty()) {
                                persistence(daySummary, summaryAssesss.values());
                        }
                }
                return serviceJdbcTemplate.collectLeastTimeSummaryAssess();
        }

        //获取订单的创建方
        private Long companyKey(OrderCollect o) {
                PartnerType partner = PartnerType.convert(o.getInsertType());
                Long companyKey = 0L;
                if (!partner.shipper() && partner.registered(o.getClientType())) {//非货主并且是注册企业
                        companyKey = partner.convey() ? o.getConveyId() : o.getReceiveId();
                }
                //不是承运商也不是收货发，并且货主是注册企业
                if ((companyKey == null || companyKey <= 0) && PartnerType.SHIPPER.registered(o.getClientType())) {
                        companyKey = o.getShipperId();
                }
                return companyKey;
        }

        //获取定位频次配置数
        private Integer loactionCount(Long companyKey, Map<Long, Integer> cache) {
                Integer count = cache.get(companyKey);
                if (count == null) {
                        String value = serviceJdbcTemplate.loadCompanyConfigValue(companyKey, CompanyConfigType.TRACKING);
                        if (StringUtils.isNotBlank(value)) {
                                count = Integer.parseInt(value);
                        } else {
                                count = Integer.parseInt(CompanyConfigType.TRACKING.getDefaultValue());
                        }
                        cache.put(companyKey, count);
                }
                return count;
        }

        private Long[] otherSideKeys(PartnerType p, OrderCollect o) {
                if(p.shipper()){
                        return new Long[]{ o.getConveyId() };
                }
                if(p.convey()){
                       if(o.getSubordinateId() != null && o.getSubordinateId() > 0){
                                return new Long[]{0L,o.getSubordinateId()};
                       }else{
                               return  new Long[]{o.getSubordinateId() };
                       }
                }
                return null;
        }
        private Long companyKey(PartnerType p, OrderCollect o) {
                return p.shipper() ? o.getShipperId() : p.convey() ? o.getConveyId() : 0L;
        }

        private SummaryOrderAssess reduceAssess(OrderCollect orderCollect, SummaryOrderAssess summaryAssess) {
                summaryAssess.setTotalCount(summaryAssess.getTotalCount() + 1);
                summaryAssess.setComplaintCount(summaryAssess.getComplaintCount() + (orderCollect.isComplaint() ? 1 : 0));

                //到货及时数, 没有要求到货时间的或者收货时间没有超过要求到货时间
                if (orderCollect.getArrivalTime() == null || orderCollect.getArrivedTime().isBefore(orderCollect.getArrivalTime())) {
                        summaryAssess.setArrivalCount(summaryAssess.getArrivalCount() + 1);
                }
                //好评数
                if (Optional.ofNullable(orderCollect.getEvaluation()).orElse(0) > 0) {
                        summaryAssess.setEvaluateCount(summaryAssess.getEvaluateCount() + 1);
                }
                //派车及时数, 派车时间在发货时间之前的
                if (orderCollect.getCarTime() != null && orderCollect.getCarTime().isBefore(orderCollect.getDeliveryTime())) {
                        summaryAssess.setSendCarCount(summaryAssess.getSendCarCount() + 1);
                }
                //跟踪及时数
                if (Optional.ofNullable(orderCollect.getPositioningCheck()).orElse(0) >= 100) {
                        summaryAssess.setTrackCount(summaryAssess.getTrackCount() + 1);
                }
                //正常签收数
                if (SignFettle.convert(orderCollect.getSignFettle()).isIntact()) {
                        summaryAssess.setSignCount(summaryAssess.getSignCount() + 1);
                }
                //延迟数
                if (CoreConstants.DELAY_WARNING_NORMAL != orderCollect.getDelayWarning()) {
                        summaryAssess.setDelayCount(summaryAssess.getDelayCount() + 1);
                }
                return summaryAssess;
        }

        private void persistence(DaySummary daySummary, Collection<SummaryOrderAssess> collection) {
                Map<Long, List<SummaryOrderAssess>> map = collection.stream().collect(Collectors.groupingBy(SummaryOrderAssess::getCompanyKey));
                for (Map.Entry<Long, List<SummaryOrderAssess>> entry : map.entrySet()) {
                        String tableName = daySummary.tableName(String.valueOf(entry.getKey()));
                        if(createTableNotExist(tableName, daySummary.getDdlString())){
                                collectJdbcTemplate.modifyOrderSummaryAssess(tableName, entry.getValue());
                        }
                }
        }
}
