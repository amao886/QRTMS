package com.ycg.ksh.service.impl.adventive;

import com.ycg.ksh.common.dubbo.ServiceMonitor;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.common.util.encrypt.RSA;
import com.ycg.ksh.common.util.encrypt.SignUtil;
import com.ycg.ksh.constant.LocationEvent;
import com.ycg.ksh.constant.LocationType;
import com.ycg.ksh.constant.ObjectType;
import com.ycg.ksh.constant.OrderEventType;
import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.entity.persistent.CompanyEmployee;
import com.ycg.ksh.entity.persistent.LocationTrack;
import com.ycg.ksh.entity.persistent.Order;
import com.ycg.ksh.entity.persistent.PaperReceipt;
import com.ycg.ksh.entity.persistent.adventive.Adventive;
import com.ycg.ksh.entity.persistent.adventive.AdventiveNote;
import com.ycg.ksh.entity.persistent.adventive.AdventivePull;
import com.ycg.ksh.entity.persistent.enterprise.AbstractOrder;
import com.ycg.ksh.entity.service.enterprise.OrderTemplate;
import com.ycg.ksh.service.api.CompanyService;
import com.ycg.ksh.service.api.LocationTrackService;
import com.ycg.ksh.service.api.OrderService;
import com.ycg.ksh.service.api.ReceiptService;
import com.ycg.ksh.service.api.adventive.AdventiveService;
import com.ycg.ksh.service.core.entity.service.adventive.*;
import com.ycg.ksh.service.observer.AdventiveObserverAdapter;
import com.ycg.ksh.service.observer.LocationObserverAdapter;
import com.ycg.ksh.service.observer.OrderObserverAdapter;
import com.ycg.ksh.service.observer.ReceiptObserverAdapter;
import com.ycg.ksh.service.persistence.adventive.AdventiveMapper;
import com.ycg.ksh.service.persistence.adventive.AdventiveNoteMapper;
import com.ycg.ksh.service.persistence.adventive.AdventivePullMapper;
import com.ycg.ksh.service.util.O;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("ksh.core.service.adventiveService")
public class AdventiveServiceImpl implements AdventiveService, OrderObserverAdapter, ServiceMonitor, LocationObserverAdapter, ReceiptObserverAdapter {


    private static final String ADVENTIVE_NOTE_LOAD_DB_KEY = "ADVENTIVE_NOTE_LOAD_DB_KEY";

    private static final String ADVENTIVE_KEY = "Adventive#";

    @Resource
    CacheManager cacheManager;

    @Resource
    AdventiveMapper adventiveMapper;
    @Resource
    AdventiveNoteMapper adventiveNoteMapper;
    @Resource
    AdventivePullMapper adventivePullMapper;

    @Resource
    OrderService orderService;
    @Resource
    LocationTrackService trackService;
    @Resource
    ReceiptService receiptService;
    @Resource
    CompanyService companyService;


    private static final Adventive LIST_BY_FETTLE = new Adventive(1);

    @Autowired(required = false)
    Collection<AdventiveDeliveryer> observers;

    @Autowired(required = false)
    Collection<AdventiveObserverAdapter> adventivers;

    private AdventiveDispatcher dispatcher;//推送器

    private AdventiveDeliveryer defaultDeliveryer;//默认的数据输送者

    @PostConstruct
    public void initializeSomething(){
        dispatcher = new AdventiveDispatcher();
        dispatcher.ready((PossibleObject p) -> filterSomething(p), (AdventiveNote a) -> delivery(a), (AdventiveNote a) -> modify(a));
        //dispatcher.commence();
        //dispatcher.dispatch(new MediaObject(LOAD_FROM_DB, 5, 1, 1));
    }

    /**
     * 服务启动完成
     *
     * @param activeProfile
     */
    @Override
    public void onServerStrated(ApplicationContext context, String activeProfile) throws BusinessException {
        boolean flag = false;
        try{
            dispatcher.commence();

            defaultDeliveryer = observers.stream().filter(o -> { return  o.isDefault(); }).findAny().get();
            logger.info("default deliveryer -> {}", defaultDeliveryer);

            if(cacheManager.setNotExist(ADVENTIVE_NOTE_LOAD_DB_KEY, System.nanoTime())){
                cacheManager.expire(ADVENTIVE_NOTE_LOAD_DB_KEY, 1L, TimeUnit.HOURS);
                flag = true;
                logger.info("加载未推送完成的数据 {} 条,继续推送..11111111111111.");
                //从数据库加载，推送
               /* Example example = new Example(AdventiveNote.class);
                example.createCriteria().andEqualTo("fettle", 1);
                Collection<AdventiveNote> adventiveNotes = adventiveNoteMapper.selectByExample(example);
                logger.info("加载未推送完成的数据 {} 条,继续推送...", adventiveNotes.size());
                for (AdventiveNote adventiveNote : adventiveNotes) {
                    Adventive adventive = getAdventive(adventiveNote.getAdventiveId());
                    dispatcher.dispatch(new MediaObject(adventiveNote, adventive.getFrequency(), adventive.getFrequencyType(), adventive.getMaxTimes()), true);
                }*/
            }
        }catch (Exception e){
            throw new BusinessException("服务器启动异常", e);
        }finally {
            cacheManager.delete(ADVENTIVE_NOTE_LOAD_DB_KEY);
        }
    }

    public Collection<AdventiveValidate> loadAdventiveValidate(Long adventiveKey) {
        //return cacheManager.get("A#P#VALIDATE#" + adventiveKey, 60L, ()->{
        Collection<AdventivePull> validates = adventivePullMapper.selectByAdventiveKey(adventiveKey);
        if(validates != null){
            return validates.stream().map(AdventiveValidate::initialize).collect(Collectors.toList());
        }
        return null;
        //});
    };

    @Override
    public Collection<Adventive> list() throws ParameterException, BusinessException {
        return adventiveMapper.selectAll();
    }

    public AdventiveAlliance loadAdventiveAlliance(Long adventiveKey) throws ParameterException, BusinessException{
        Assert.notBlank(adventiveKey, "编号不能为空");
        Adventive adventive = getAdventive(adventiveKey);
        if(adventive == null){
            throw new ParameterException("查询异常");
        }
        try {
            AdventiveAlliance alliance = new AdventiveAlliance(adventive);
            alliance.setCompany(companyService.getCompanyConciseByKey(alliance.getCompanyKey()));
            alliance.setPulls(adventivePullMapper.selectByAdventiveKey(alliance.getAccessKey()));
            return alliance;
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("加载推送配置信息异常", e);
        }
    }

    @Override
    public Adventive saveOrUpdate(Adventive adventive) throws ParameterException, BusinessException {
        Assert.notNull(adventive, "推送配置信息不能为空");
        try {
            if(adventive.getAccessKey() != null && adventive.getAccessKey() > 0){
                adventiveMapper.updateByPrimaryKeySelective(adventive);
                cacheManager.delete(ADVENTIVE_KEY + adventive.getAccessKey());
            }else{
                adventive.setAccessKey(Globallys.nextKey());
                adventive.setFrequency(Optional.ofNullable(adventive.getFrequency()).orElse(1));
                adventive.setFrequencyType(Optional.ofNullable(adventive.getFrequencyType()).orElse(1));
                adventive.setFettle(1);

                Map<String, Object> keyPairs = RSA.genKeyPair();

                adventive.setPublicKey(RSA.getPublicKey(keyPairs));
                adventive.setPrivateKey(RSA.getPrivateKey(keyPairs));

                adventiveMapper.insertSelective(adventive);
            }
            return getAdventive(adventive.getAccessKey());
        } catch (Exception e) {
            logger.error("推送配置信息处理异常 {}", adventive, e);
            throw new BusinessException("推送配置信息处理异常", e);
        }
    }

    @Override
    public AdventivePull saveOrUpdate(AdventivePull pull) throws ParameterException, BusinessException {
        Assert.notNull(pull, "推送配置信息不能为空");
        Assert.notBlank(pull.getAdventiveKey(), "推送配置编号不能为空");
        try {
            if(pull.getId() != null && pull.getId() > 0){
                adventivePullMapper.updateByPrimaryKeySelective(pull);
            }else{
                pull.setId(Globallys.nextKey());
                adventivePullMapper.insertSelective(pull);
            }
            cacheManager.delete("A#P#VALIDATE#" + pull.getAdventiveKey());
            return adventivePullMapper.selectByPrimaryKey(pull.getId());
        } catch (Exception e) {
            logger.error("推送配置信息处理异常 {}", pull, e);
            throw new BusinessException("推送配置信息处理异常", e);
        }
    }


    /**
     * @param key
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Adventive getAdventive(Long key) throws ParameterException, BusinessException {
        return cacheManager.get(ADVENTIVE_KEY + key, 60L, () -> {
            return adventiveMapper.selectByPrimaryKey(key);
        });
    }

    @Override
    public Adventive validate(Long accessKey, String signVal, Map<String, Object> parameters) throws ParameterException, BusinessException {
        Assert.notBlank(accessKey, "唯一标识不能为空");
        Assert.notBlank(signVal, "签名字符串不能为空");
        try{
            Adventive adventive = getAdventive(accessKey);
            if(adventive == null){
                throw new BusinessException("AccessKey["+ accessKey +"]不存在");
            }
            String linkContext = SignUtil.createLinkString(parameters);
            if(RSA.verify(linkContext, signVal, adventive.getPublicKey())){
                return adventive;
            }else{
                throw new BusinessException("签名校验不通过");
            }
        }catch (ParameterException | BusinessException e){
            throw e;
        }catch (Exception e){
            logger.error("签名校验异常 {} {} {}", accessKey, signVal, parameters, e);
            throw new BusinessException("签名校验异常");
        }
    }

    @Override
    public void enforcePushNote(Long noteKey) throws ParameterException, BusinessException {
        Assert.notBlank(noteKey, "推送记录编号不能为空");
        AdventiveNote adventiveNote = adventiveNoteMapper.selectByPrimaryKey(noteKey);
        if(adventiveNote == null){
            throw new ParameterException("推送记录["+ noteKey +"]不存在");
        }
        delivery(adventiveNote);
    }



    @Override
    public void modifyOrder(Integer uKey, Order order, OrderEventType eventType) throws BusinessException {
        if(order != null){
            try{
                if(eventType.isBindCode()){//订单绑码
                    dispatcher.dispatch(new PossibleObject(ObjectType.ORDER, order.getId(), order.getDeliveryNo(), order.getShipperId(), order.getReceiveId(), order.getConveyId()));
                }
                if(eventType.isReceive()){//确认收货
                    dispatcher.dispatch(new PossibleObject(ObjectType.SYNCFETTLE, order.getId(), order.getDeliveryNo(), order.getShipperId(), order.getReceiveId(), order.getConveyId()));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void notifyLocationReport(LocationEvent locationEvent, LocationType locationType, LocationTrack locationTrack) throws BusinessException {
        if(locationType.isOrder() && locationTrack != null){
            try{
                Order order = orderService.getOrderByKey(Long.parseLong(locationTrack.getHostId()));
                if(order != null){
                    dispatcher.dispatch(new PossibleObject(ObjectType.LOCATIONTRACK, locationTrack.getId(), order.getDeliveryNo(), order.getShipperId(), order.getReceiveId(), order.getConveyId()));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void notifyUploadReceipt(PaperReceipt receipt) throws BusinessException {
        if(receipt != null){
            try{
                Order order = orderService.getOrderByKey(receipt.getOrderKey());
                if(order != null){
                    dispatcher.dispatch(new PossibleObject(ObjectType.RECEIPT, receipt.getId(), order.getDeliveryNo(), order.getShipperId(), order.getReceiveId(), order.getConveyId()));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private Collection<MediaObject> filterSomething(PossibleObject possibleObject){
        Collection<MediaObject> mediaObjects = new ArrayList<MediaObject>();
        for (Adventive adventive : listAdventives()) {
            if(validate(adventive, possibleObject)){
                AdventiveNote adventiveNote = new AdventiveNote(Globallys.nextKey(), adventive.getAccessKey(), possibleObject.getNoteType(), possibleObject.getObjectKey(), possibleObject.getOppositeKey());
                mediaObjects.add(new MediaObject(adventiveNote, adventive.getFrequency(), adventive.getFrequencyType(), adventive.getMaxTimes()));
            }
        }
        Collection<AdventiveNote> collections = mediaObjects.stream().map(MediaObject::getTarget).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(collections)){
        	adventiveNoteMapper.inserts(collections);
        }
        return mediaObjects;
    }
    /**
     *
     * @TODO 方法说明
     *
     * @param:
     * @return:
     * @exception:
     * @auther: baymax
     * @date: 2018/8/21 17:49
     */
    private void modify(AdventiveNote adventiveNote){
        adventiveNote.setLastTime(new Date());
        if(adventiveNote.getId() != null && adventiveNote.getId() > 0){
            adventiveNoteMapper.updateByPrimaryKeySelective(adventiveNote);
        }else{
            adventiveNote.setId(Globallys.nextKey());
            adventiveNoteMapper.insertSelective(adventiveNote);
        }
    }

    private boolean delivery(AdventiveNote adventiveNote) {
        try{
            if(CollectionUtils.isNotEmpty(adventivers) && CollectionUtils.isNotEmpty(observers)){
                Object object = null;
                ObjectType objectType = ObjectType.convert(adventiveNote.getNoteType());
                Adventive adventive = getAdventive(adventiveNote.getAdventiveId());
                AdventiveDeliveryer deliveryer = observers.stream().filter(o -> {
                    return o.validate(adventive, adventiveNote);
                }).findAny().orElse(defaultDeliveryer);
                for (AdventiveObserverAdapter adapter : adventivers) {
                    if((object = adapter.adventiveObjectByKey(objectType, adventiveNote.getObjectKey())) != null){
                        break;
                    }
                }
                if(object != null){
                    object = ConvertUtil.convert(objectType, object);

                    return deliveryer.process(adventive, adventiveNote, object);
                }
            }
        }catch (Exception e){
            logger.error("推送异常 {}", adventiveNote);
        }
        return false;
    }

    private boolean validate(Adventive adventive, PossibleObject possibleObject){
        Collection<AdventiveValidate> validates = loadAdventiveValidate(adventive.getAccessKey());
        boolean result = false;
        if(CollectionUtils.isNotEmpty(validates)){
            for (AdventiveValidate validate : validates) {
                result = validate.validate(possibleObject.getShipperKey(), possibleObject.getReceiveKey(), possibleObject.getConveyKey(), possibleObject.getNoteType());
                if(result){
                    result = adventiveNoteMapper.selectCount(new AdventiveNote(adventive.getAccessKey(), possibleObject.getNoteType(), possibleObject.getObjectKey())) <= 0;
                }
                if(result){  break; }
            }
        }
        return result;
    }

    private Collection<Adventive> listAdventives(){
        return adventiveMapper.select(LIST_BY_FETTLE);
    }


    private CompanyEmployee validateAdventive(Long accessKey) {
        Adventive adventive = getAdventive(accessKey);
        if(adventive == null || adventive.getCompanyKey() == null || adventive.getCompanyKey() <= 0){
            throw new BusinessException("当前配置信息有误");
        }
        CompanyEmployee employee = companyService.getCompanyAdmin(adventive.getCompanyKey());
        if(employee == null){
            throw new BusinessException("当前企业配置信息有误");
        }
        return employee;
    }

    @Override
    public void acceptOrders(Long accessKey, Collection<AdventiveOrder> adventiveOrders, PartnerType partner) throws ParameterException, BusinessException {
        CompanyEmployee employee = validateAdventive(accessKey);
        for (AdventiveOrder adventiveOrder : adventiveOrders) {
            OrderTemplate template = ConvertUtil.orderTemplate(adventiveOrder);
            if(StringUtils.isBlank(template.getDeliveryNo())){
                throw new ParameterException(template.getDeliveryNo(), "发货单号不能为空");
            }
            template.setShipperKey(employee.getCompanyId());
            template.setOrderExtra(ConvertUtil.orderExtra(adventiveOrder));
            template.setCommodities(Optional.ofNullable(adventiveOrder.getCommodities()).map(cs->{
                return cs.stream().map(ConvertUtil::orderCommodity).collect(Collectors.toList());
            }).orElse(Collections.emptyList()));
            template.setCustomDatas(ConvertUtil.customData(adventiveOrder.getOthers()));
            orderService.saveOrder(employee.getEmployeeId(), partner, template, OrderEventType.CREATE_SYNC);
        }
    }

    @Override
    public Map<Long, Integer> listOrderFettleByKeys(Long accessKey, Collection<Long> orderKeys) throws ParameterException, BusinessException {
        CompanyEmployee employee = validateAdventive(accessKey);
        return Optional.ofNullable(orderKeys).map(keys->{
            return keys.stream().map(k->orderService.getOrderByKey(k)).collect(Collectors.toMap(Order::getId, Order::getFettle));
        }).orElse(Collections.emptyMap());
    }

    @Override
    public Map<String, Integer> listOrderFettleByDeliveryNos(Long accessKey, Collection<String> deliveryNos) throws ParameterException, BusinessException {
        CompanyEmployee employee = validateAdventive(accessKey);
        return Optional.ofNullable(deliveryNos).map(keys-> orderService.listByDeliveryNo(PartnerType.SHIPPER, employee.getCompanyId(), keys)).map(orders->{
            return orders.stream().collect(Collectors.toMap(Order::getDeliveryNo, Order::getFettle));
        }).orElse(Collections.emptyMap());
    }

    @Override
    public Collection<AdventiveOrder> listOrderByKeys(Long accessKey, Collection<Long> orderKeys) throws ParameterException, BusinessException {
        CompanyEmployee employee = validateAdventive(accessKey);
        return Optional.ofNullable(orderKeys).map(keys->{
            return keys.stream().map(k->orderService.alliance(employee.getEmployeeId(), k, O.enterprise, O.commodities, O.customdata, O.extradata)).map(ConvertUtil::adventiveOrder).collect(Collectors.toList());
        }).orElse(Collections.emptyList());
    }

    @Override
    public Map<Long, String>listDeliveryNoByOrderNo(Long accessKey, String orderNo) throws ParameterException, BusinessException {
        CompanyEmployee employee = validateAdventive(accessKey);
        return Optional.ofNullable(orderService.listByOrderNo(PartnerType.SHIPPER, employee.getCompanyId(),  orderNo)).map(orders-> orders.stream().collect(Collectors.toMap(AbstractOrder::getId, AbstractOrder::getDeliveryNo))).orElse(Collections.emptyMap());
    }

    @Override
    public Collection<AdventiveOrder> listOrderDeliveryNos(Long accessKey, Collection<String> deliveryNos) throws ParameterException, BusinessException {
        CompanyEmployee employee = validateAdventive(accessKey);
        return Optional.ofNullable(deliveryNos).map(keys-> orderService.listByDeliveryNo(PartnerType.SHIPPER, employee.getCompanyId(), keys)).map(orders->{
            return orders.stream().map(k->orderService.alliance(employee.getEmployeeId(), k, O.enterprise, O.commodities, O.customdata, O.extradata)).map(ConvertUtil::adventiveOrder).collect(Collectors.toList());
        }).orElse(Collections.emptyList());
    }

    @Override
    public Collection<AdventiveTrack> listTrackByKey(Long accessKey, Long orderKey) throws ParameterException, BusinessException {
        return Optional.ofNullable(orderKey).map(k->trackService.listAllianceBySomething(LocationType.ORDER, k)).map(ls->{
            return ls.stream().map(ConvertUtil::adventiveTrack).collect(Collectors.toList());
        }).orElse(Collections.emptyList());
    }
    @Override
    public Collection<AdventiveTrack> listTrackByDeliveryNo(Long accessKey, String deliveryNo) throws ParameterException, BusinessException {
        CompanyEmployee employee = validateAdventive(accessKey);
        Collection<AdventiveTrack> adventiveTracks = new ArrayList<AdventiveTrack>();
        Collection<Order> collection = orderService.listByDeliveryNo(PartnerType.SHIPPER, employee.getCompanyId(), Stream.of(deliveryNo).collect(Collectors.toList()));
        for (Order o : collection) {
            adventiveTracks.addAll(listTrackByKey(accessKey, o.getId()));
        }
        return adventiveTracks;
    }

    /**
     * 查询轨迹信息
     *
     * @param accessKey
     * @param keys
     * @param qlmKey
     *
     * @return
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Map<Serializable, Collection<AdventiveTrack>> mapTracks(Long accessKey, Collection<? extends Serializable> keys, boolean qlmKey) throws ParameterException, BusinessException {
        Map<Serializable, Collection<AdventiveTrack>> map = new HashMap<Serializable, Collection<AdventiveTrack>>(keys.size());
        if(qlmKey){
            for (Serializable key : keys) {
                if(NumberUtils.isNumber(key.toString())){
                    map.put(key, listTrackByKey(accessKey, Long.parseLong(key.toString())));
                }else{
                    map.put(key, Collections.emptyList());
                }
            }
        }else{
            CompanyEmployee employee = validateAdventive(accessKey);
            Collection<Order> collection = orderService.listByDeliveryNo(PartnerType.SHIPPER, employee.getCompanyId(), keys.stream().map(s->s.toString()).collect(Collectors.toList()));
            for (Order o : collection) {
                map.put(o.getDeliveryNo(), listTrackByKey(accessKey, o.getId()));
            }
        }
        return map;
    }

    @Override
    public Collection<AdventiveReceipt> listReceiptByKey(Long accessKey, Long orderKey) throws ParameterException, BusinessException {
        return Optional.ofNullable(orderKey).map(k->receiptService.listIamgesByOrderKey(k)).map(ls->{
            return ls.stream().map(ConvertUtil::adventiveReceipt).collect(Collectors.toList());
        }).orElse(Collections.emptyList());
    }


    @Override
    public Collection<AdventiveReceipt> listReceiptByDeliveryNo(Long accessKey, String deliveryNo) throws ParameterException, BusinessException {
        CompanyEmployee employee = validateAdventive(accessKey);
        Collection<AdventiveReceipt> adventiveReceipts = new ArrayList<AdventiveReceipt>();
        Collection<Order> collection = orderService.listByDeliveryNo(PartnerType.SHIPPER, employee.getCompanyId(), Stream.of(deliveryNo).collect(Collectors.toList()));
        for (Order o : collection) {
            adventiveReceipts.addAll(listReceiptByKey(accessKey, o.getId()));
        }
        return adventiveReceipts;
    }

    /**
     * 查询轨迹信息
     *
     * @param accessKey
     * @param keys
     * @param qlmKey
     *
     * @return
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Map<Serializable, Collection<AdventiveReceipt>> mapReceipts(Long accessKey, Collection<? extends Serializable> keys, boolean qlmKey) throws ParameterException, BusinessException {
        Map<Serializable, Collection<AdventiveReceipt>> map = new HashMap<Serializable, Collection<AdventiveReceipt>>(keys.size());
        if(qlmKey){
            for (Serializable key : keys) {
                if(NumberUtils.isNumber(key.toString())){
                    map.put(key, listReceiptByKey(accessKey, Long.parseLong(key.toString())));
                }else{
                    map.put(key, Collections.emptyList());
                }
            }
        }else{
            CompanyEmployee employee = validateAdventive(accessKey);
            Collection<Order> collection = orderService.listByDeliveryNo(PartnerType.SHIPPER, employee.getCompanyId(), keys.stream().map(s->s.toString()).collect(Collectors.toList()));
            for (Order o : collection) {
                map.put(o.getDeliveryNo(), listReceiptByKey(accessKey, o.getId()));
            }
        }
        return map;
    }

}
