package com.ycg.ksh.service.impl;

import com.github.pagehelper.Page;
import com.ycg.ksh.entity.common.constant.AssignFettle;
import com.ycg.ksh.entity.common.constant.RouteLineType;
import com.ycg.ksh.entity.common.constant.SourceType;
import com.ycg.ksh.entity.common.constant.WaybillFettle;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.cache.LocalCacheManager;
import com.ycg.ksh.common.extend.lock.DistributedSynchronize;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.HexConvert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.common.util.UserUtil;
import com.ycg.ksh.entity.service.*;
import com.ycg.ksh.entity.persistent.Barcode;
import com.ycg.ksh.entity.persistent.Conveyance;
import com.ycg.ksh.entity.persistent.Waybill;
import com.ycg.ksh.entity.persistent.WaybillTrack;
import com.ycg.ksh.entity.service.barcode.BarcodeContext;
import com.ycg.ksh.entity.service.barcode.GroupCodeContext;
import com.ycg.ksh.service.persistence.ConveyanceMapper;
import com.ycg.ksh.service.api.ConveyanceService;
import com.ycg.ksh.service.api.FriendsService;
import com.ycg.ksh.service.api.ProjectGroupService;
import com.ycg.ksh.service.api.RouteService;
import com.ycg.ksh.service.observer.*;
import com.ycg.ksh.service.support.assist.ConveyanceUtils;
import com.ycg.ksh.service.support.assist.ConveyanceValidate;
import com.ycg.ksh.service.support.assist.LocalCacheFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * 运单业务逻辑实现
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/23
 */
@Service("ksh.core.service.conveyanceService")
public class ConveyanceServiceImpl implements ConveyanceService, WaybillObserverAdapter, DriverContainerObserverAdapter, TrackObserverAdapter, ReceiptObserverAdapter, BarcodeObserverAdapter {

    private static final String LOCK = "CONVEYANCE";


    private static final int ASSIGN = 0;//指派
    private static final int EDIT_NUMBER = 1;//修改运单号
    private static final int EDIT_ASSIGN = 2;//修改承运商
    private static final int CANCEL_ASSIGN = 3;//取消指派
    private static final int SHARE_GROUP = 4;//分享到项目组
    private static final int ARRIVE = 5;//确认到货
    private static final int EDIT_FETTLE = 6;//修改状态

    @Resource
    DistributedSynchronize distributedSynchronize;
    @Resource
    ConveyanceMapper conveyanceMapper;
    @Resource
    FriendsService friendsService;
    @Resource
    ProjectGroupService groupService;
    @Resource
    RouteService routeService;

    @PostConstruct
    public void initialize(){
        try {
            distributedSynchronize.initialize(LOCK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String buildNumber(String barcode, Integer level, Integer index){
        return barcode +"-"+ HexConvert.convert(level, 10, 16) + HexConvert.convert(index, 10, 16);
    }

    private Conveyance defaultProperty(Conveyance conveyance){
        if(conveyance != null){
            if(conveyance.getParentKey() == null){
                conveyance.setParentKey(0L);
            }
            if(conveyance.getParentKey() == null){
                conveyance.setParentKey(0L);
            }
            if(conveyance.getOwnerGroupKey() == null){
                conveyance.setOwnerGroupKey(0);
            }
            if(conveyance.getAssignFettle() == null){
                conveyance.setAssignFettle(AssignFettle.NOTYET.getCode());
            }
            if(conveyance.getConveyanceFettle() == null){
                conveyance.setConveyanceFettle(WaybillFettle.BOUND.getCode());
            }
            if(conveyance.getHaveChild() == null){
                conveyance.setHaveChild(false);
            }
            if(conveyance.getNextKey() == null){
                conveyance.setNextKey(0L);
            }
            if(conveyance.getNodeLevel() == null){
                conveyance.setNodeLevel(1);
            }
            if(conveyance.getConveyanceNumber() == null){
                conveyance.setConveyanceNumber(StringUtils.EMPTY);
            }
        }
        return conveyance;
    }

    private void updateConveyance(Conveyance conveyance, AssignFettle fettle){
        conveyanceMapper.updateByPrimaryKeySelective(conveyance);
        if(fettle.whole()){
            Map<Long, Conveyance> mapChildren = mapChildren(conveyance.getId(), null, false);
            if(mapChildren != null && !mapChildren.isEmpty()){
                conveyance.setId(null);
                Example example = new Example(Conveyance.class);
                example.createCriteria().andIn("id", mapChildren.keySet());
                conveyanceMapper.updateByExampleSelective(conveyance, example);
            }
        }
    }

    private void updateByConveyanceKeys(Collection<Long> conveyanceKeys, Conveyance updater){
        Example example = new Example(Conveyance.class);
        example.createCriteria().andIn("id", conveyanceKeys);
        conveyanceMapper.updateByExampleSelective(updater, example);
    }

    private void updateByWaybillKey(Conveyance updater, Integer waybillKey){
        Example example = new Example(Conveyance.class);
        example.createCriteria().andEqualTo("waybillKey", waybillKey);
        conveyanceMapper.updateByExampleSelective(updater, example);
    }

    /**
     * 根据运单编号验证始发地和目的地
     *
     * @param conveyanceKeys
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Station validate(Collection<Long> conveyanceKeys) throws ParameterException, BusinessException {
        Assert.notEmpty(conveyanceKeys, "运单编号不能为空");
        Collection<Station> stations = conveyanceMapper.listStation(conveyanceKeys);
        Station standard = null;
        for (Station station : stations) {
            if(standard != null){
                if(!AssignFettle.convert(standard.getAssignFettle()).notyet()){
                    throw new BusinessException("所选运单包含已经指派的");
                }
                if(!StringUtils.equals(standard.getStartStation(), station.getStartStation())){//发站不一致
                    throw new BusinessException("所选运单始发地不一致");
                }
                if(!StringUtils.equals(standard.getEndStation(), station.getEndStation())){//到站不一致
                    throw new BusinessException("所选运单目的地不一致");
                }
            }else{
                standard = station;
            }
        }
        if(!AssignFettle.convert(standard.getAssignFettle()).notyet()){
            throw new BusinessException("所选运单包含已经指派的");
        }
        return standard;
    }

    @Override
    public Collection<WaybillConveyance> listWaybillConveyance(ConveyanceSearch search) throws ParameterException, BusinessException {
        if(search == null){
            search = new ConveyanceSearch();
        }
        Collection<WaybillConveyance> conveyances = conveyanceMapper.listWaybillConveyanceBySomething(search);
        if (CollectionUtils.isNotEmpty(conveyances)){
            try {
                LocalCacheManager<AssociateUser> associateUserCache = LocalCacheFactory.createFriendUserCache(friendsService);
                for (WaybillConveyance conveyance : conveyances) {
                    if(conveyance.getHaveChild()){
                        Collection<Conveyance> collection = conveyanceMapper.listByParentKey(conveyance.getId());
                        if(CollectionUtils.isEmpty(collection)){ continue; }
                        for (Conveyance childConveyance : collection) {
                            conveyance.addChildren(new WaybillConveyance(childConveyance, conveyance.getWaybill()));
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("运单查询异常 {}", search, e);
                throw new BusinessException("运单查询异常");
            }
        }
        return conveyances;
    }

    @Override
    public Collection<Conveyance> listConveyanceByParentKey(Long parentKey) throws ParameterException, BusinessException {
        return conveyanceMapper.listByParentKey(parentKey);
    }

    /**
     * 根据条件查询任务单-运单信息
     * @param search
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public CustomPage<WaybillConveyance> listWaybillConveyance(ConveyanceSearch search, PageScope scope) throws ParameterException, BusinessException {
        Assert.notNull(search, "查询条件不能为空");
        Assert.notBlank(search.getCreateKey(), "当前操作人用户ID不能为空");
        if (scope == null) {
            scope = PageScope.DEFAULT;
        }
        Page<WaybillConveyance> page = conveyanceMapper.listWaybillConveyanceBySomething(search, new RowBounds(scope.getPageNum(), scope.getPageSize()));
        if (CollectionUtils.isNotEmpty(page)){
            try {
                LocalCacheManager<AssociateUser> associateUserCache = LocalCacheFactory.createFriendUserCache(friendsService);
                for (WaybillConveyance conveyance : page) {
                    if(conveyance.getHaveChild()){
                        Collection<Conveyance> collection = conveyanceMapper.listByParentKey(conveyance.getId());
                        if(CollectionUtils.isEmpty(collection)){ continue; }
                        for (Conveyance childConveyance : collection) {
                            WaybillConveyance childWaybillConveyance = new WaybillConveyance(childConveyance, conveyance.getWaybill());
                            childWaybillConveyance.setOwner(associateUserCache.get(search.getCreateKey(), childConveyance.getOwnerKey()));
                            conveyance.addChildren(childWaybillConveyance);
                        }
                    }
                    conveyance.setOwner(associateUserCache.get(search.getCreateKey(), conveyance.getOwnerKey()));
                }
            } catch (Exception e) {
                logger.error("运单查询异常 {} {}", search, scope, e);
                throw new BusinessException("运单查询异常");
            }
        }
        return new CustomPage<WaybillConveyance>(page.getPageNum(), page.getPageSize(), page.getTotal(), page);
    }

    private Collection<ConveyanceValidate> validateConveyance(Collection<Long> conveyanceKeys, Integer uKey, int pcode){
        Collection<ConveyanceValidate> collection = new ArrayList<ConveyanceValidate>(conveyanceKeys.size());
        for (Long conveyanceKey : conveyanceKeys) {
            Conveyance conveyance = conveyanceMapper.selectByPrimaryKey(conveyanceKey);
            if(conveyance != null){
                ConveyanceValidate validate = validateConveyance(conveyance, uKey, pcode);
                if(validate(validate)){
                    collection.add(validate);
                }else{
                    throw new BusinessException("权限不足");
                }
            }else{
                throw new BusinessException("权限不足");
            }
        }
        return collection;
    }

    private Collection<Conveyance> validateCycleAssign(Collection<Conveyance> conveyances){
        for (Conveyance conveyance : conveyances) {
            if(conveyance.getOwnerKey() != null && conveyance.getOwnerKey() > 0){
                validateCycleAssign(conveyance, conveyance.getOwnerKey());
            }
        }
        return conveyances;
    }

    private boolean validateCycleAssign(Conveyance conveyance, Integer assignKey){
        Example example = new Example(Conveyance.class);
        example.createCriteria().andEqualTo("waybillKey", conveyance.getWaybillKey()).andEqualTo("ownerKey", assignKey).andNotEqualTo("conveyanceFettle", WaybillFettle.CANCEL.getCode());
        if(conveyanceMapper.selectCountByExample(example) > 0){
            throw new BusinessException("该承运人已被指派一次,不能继续指派");
        }
        if(conveyance.getOwnerGroupKey() != null && conveyance.getOwnerGroupKey() > 0){
            if(groupService.isMember(conveyance.getOwnerGroupKey(), assignKey)){
                throw new BusinessException("该承运人与您在同一个项目组中,不能指派");
            }
        }
        return true;
    }

    private ConveyanceValidate validateConveyance(Conveyance conveyance, Integer uKey, int pcode){
        ConveyanceValidate validate = new ConveyanceValidate(conveyance, uKey, pcode);
        validate.setAssignFettle(AssignFettle.convert(conveyance.getAssignFettle()));//指派状态
        validate.setFettle(WaybillFettle.convert(conveyance.getConveyanceFettle()));//运单状态
        validate.setOwner(conveyance.getOwnerKey() - uKey == 0);//运单拥有者
        if(!validate.isOwner()){
            validate.setOwner(groupService.isMember(conveyance.getOwnerGroupKey(), uKey));//项目组
        }
        if(conveyance.getParentKey() > 0){
            Conveyance parentConveyance = conveyanceMapper.selectByPrimaryKey(conveyance.getParentKey());
            if(parentConveyance != null){
                validate.setParent(parentConveyance.getOwnerKey() - uKey == 0);//父运单拥有者
                if(parentConveyance.getOwnerGroupKey() > 0 && !validate.isParent()){
                    validate.setParent(groupService.isMember(parentConveyance.getOwnerGroupKey(), uKey));//父运单组
                }
            }
        }
        return validate;
    }

    private boolean validate(ConveyanceValidate validate){
        //只要创建人和运单所在项目组成员可以修改承运人么？
        //只要创建人和运单所在项目组成员可以取消指派？
        //是创建者或者和创建者在同一个项目组
        if(ASSIGN == validate.getValidateCode()){//指派
            if(!validate.getAssignFettle().notyet()){//已经指派的运单不能在指派
                throw new BusinessException("运单已经指派");
            }
            if(validate.getFettle().getCode() >= WaybillFettle.ARRIVE.getCode()){//已送达后的运单不能指派
                throw new BusinessException("运单已经"+ validate.getFettle().getDesc() +"不能再指派了");
            }
        }
        if(SHARE_GROUP == validate.getValidateCode()){//分享到项目组
            Integer groupKey = validate.getConveyance().getOwnerGroupKey();
            if(groupKey != null && groupKey > 0){//已经在项目组的运单不能继续分享
                throw new BusinessException("运单已经在项目组了不能继续分享");
            }
            if(!validate.isOwner()){//不是运单的所有者不能分享
                throw new BusinessException("没有分享权限");
            }
        }
        if(EDIT_FETTLE == validate.getValidateCode()){//修改运单状态
            if(!validate.isOwner() && !validate.isParent()){//不是运单的所有者或者父级所有者不能修改状态
                throw new BusinessException("没有修改状态的权限");
            }
        }
        if(EDIT_ASSIGN == validate.getValidateCode()){//修改承运人
            if(!validate.isParent()){//不是父级运单所有者不能修改
                throw new BusinessException("没有修改承运人的权限");
            }
            if(!validate.getFettle().bind() || !validate.getAssignFettle().notyet()){//运输状态之前的运单不能修改
                throw new BusinessException("不能修改该运单的承运人");
            }
        }
        if(EDIT_NUMBER == validate.getValidateCode()){//编辑运单号
            if(validate.getFettle().arrive() || validate.getFettle().receive()){//已送到或者确认到货状态的运单不能修改
                throw new BusinessException("当前状态不能修改运单号");
            }
        }
        return validate.isOwner() || validate.isParent();
    }

    private Collection<Conveyance> splitByRoute(Integer uKey, Conveyance conveyance, MergeRoute mergeRoute){
        Collection<Conveyance> collection = new ArrayList<Conveyance>(mergeRoute.getRouteLines().size());
        Date cdate = new Date();
        Conveyance preConveyance = null;
        for (MergeRouteLink link = routeService.buildRouteLink(mergeRoute); link != null && link.hasNext() ; link = link.nextLink()) {
            Long conveyanceKey = Globallys.nextKey();
            if(preConveyance != null){
                preConveyance.setNextKey(conveyanceKey);
            }
            Conveyance currentConveyance = defaultProperty(new Conveyance());
            currentConveyance.setId(conveyanceKey);
            currentConveyance.setCreateTime(cdate);
            currentConveyance.setWaybillKey(conveyance.getWaybillKey());
            currentConveyance.setDeliveryNumber(conveyance.getDeliveryNumber());
            currentConveyance.setBarcode(conveyance.getBarcode());
            if(link.hasPrev()){
                currentConveyance.setStartStation(link.self().getStation());
                currentConveyance.setSimpleStartStation(link.self().getSimpleStation());
            }else{
                currentConveyance.setStartStation(conveyance.getStartStation());
                currentConveyance.setSimpleStartStation(conveyance.getSimpleStartStation());
            }
            if(RouteLineType.convert(link.nextRoute().getLineType()).transit()){
                currentConveyance.setEndStation(link.nextRoute().getStation());
                currentConveyance.setSimpleEndStation(link.nextRoute().getSimpleStation());
            }else{
                currentConveyance.setEndStation(conveyance.getEndStation());
                currentConveyance.setSimpleEndStation(conveyance.getSimpleEndStation());
            }
            AssociateUser associateUser = link.self().getUser();
            if(associateUser != null){
                currentConveyance.setContactName(UserUtil.encodeName(associateUser.getUnamezn()));
                currentConveyance.setContactPhone(associateUser.getMobilephone());
                currentConveyance.setOrganization(associateUser.getCompany());
            }
            currentConveyance.setAssignFettle(AssignFettle.NOTYET.getCode());
            currentConveyance.setAssignFettleTime(cdate);

            currentConveyance.setHaveChild(false);
            currentConveyance.setParentKey(conveyance.getId());
            currentConveyance.setOwnerKey(link.self().getUserId());

            currentConveyance.setConveyanceFettle(WaybillFettle.BOUND.getCode());
            currentConveyance.setConveyanceFettleTime(cdate);
            currentConveyance.setNodeLevel(conveyance.getNodeLevel() + 1);
            //currentConveyance.setConveyanceNumber(buildNumber(conveyance.getBarcode(), currentConveyance.getNodeLevel(), index++));
            preConveyance = currentConveyance;
            collection.add(currentConveyance);
        }
        return collection;
    }

    /**
     * 执行指派
     * @param uKey 当前操作用户ID
     * @param conveyanceKeys 运单编号
     * @param routeKey       路由编号
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void saveRouteAssign(Integer uKey, Collection<Long> conveyanceKeys, Integer routeKey) throws ParameterException, BusinessException {
        Assert.notEmpty(conveyanceKeys, "至少选择一个运单信息");
        Assert.notBlank(routeKey, "请选择一个路由信息");
        logger.info("路由指派 uKey:{} conveyanceKeys:{} routeKey:{}", uKey, conveyanceKeys, routeKey );
        MergeRoute mergeRoute = routeService.getByKey(uKey, routeKey);
        if(mergeRoute == null){
            throw new ParameterException(mergeRoute, "指定的路由不存在");
        }
        validate(conveyanceKeys);
        try {
            LocalCacheManager<AssociateUser> associateUserCache = LocalCacheFactory.createFriendUserCache(friendsService);
            for (ConveyanceValidate validate : validateConveyance(conveyanceKeys, uKey, ASSIGN)) {
                Collection<Conveyance> collection = validateCycleAssign(splitByRoute(uKey, validate.getConveyance(), mergeRoute));
                if(CollectionUtils.isNotEmpty(collection)){
                    AssociateUser associateUser = associateUserCache.get(uKey, routeKey);
                    conveyanceMapper.inserts(collection);
                    Conveyance updater = new Conveyance();
                    updater.setId(validate.getConveyance().getId());
                    if(associateUser != null){
                        updater.setContactName(associateUser.getUnamezn());
                        updater.setContactPhone(associateUser.getMobilephone());
                        updater.setOrganization(associateUser.getCompany());
                    }
                    updater.setAssignFettle(AssignFettle.ROUTE.getCode());
                    updater.setAssignFettleTime(new Date());
                    updater.setHaveChild(true);
                    conveyanceMapper.updateByPrimaryKeySelective(updater);
                }
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("save route assign ================> uKey:{} conveyanceKeys:{} routeKey:{}", uKey, conveyanceKeys, routeKey, e);
            throw BusinessException.dbException("整包指派异常");
        }
    }

    /**
     * 保存整包指派
     * @param uKey 当前操作用户ID
     * @param conveyanceKeys 运单编号
     * @param userKey        用户ID
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void saveWholeAssign(Integer uKey, Collection<Long> conveyanceKeys, Integer userKey) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "当前操作用户ID不能为空");
        Assert.notEmpty(conveyanceKeys, "至少选择一个运单信息");
        Assert.notBlank(userKey, "请选择一个指派用信息");
        logger.info("整包指派 uKey:{} conveyanceKeys:{} userKey:{}", uKey, conveyanceKeys, userKey );
        Conveyance updater = new Conveyance();
        try {
            Date cdate = new Date();
            for (ConveyanceValidate validate : validateConveyance(conveyanceKeys, uKey, ASSIGN)) {
                if(validateCycleAssign(validate.getConveyance(), userKey)){
                    wholeAssign(validate, userKey, cdate);
                }
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("save whole assign================> conveyanceKeys:{} userKey:{}", conveyanceKeys, userKey, e);
            throw BusinessException.dbException("整包指派异常");
        }
    }
    private void wholeAssign(ConveyanceValidate validate, Integer assignKey, Date date){
        Conveyance conveyance = validate.getConveyance();
        /*
        if(!validate.getFettle().bind()){
            throw new BusinessException("该运单"+ validate.getFettle().getDesc() +",不能指派");
        }
        if(!validate.getAssignFettle().notyet()){
            throw new BusinessException("运单已经指派,不能继续指派");
        }
        */
        LocalCacheManager<AssociateUser> associateUserCache = LocalCacheFactory.createFriendUserCache(friendsService);
        AssociateUser associateUser = associateUserCache.get(validate.getOperateKey(), assignKey);
        if(associateUser == null){
            throw new BusinessException("承运人信息异常");
        }
        if(validate.getOperateKey() - associateUser.getId() == 0){
            throw new BusinessException("不能将运单指派给自己");
        }

        Conveyance current = defaultProperty(new Conveyance());
        current.setId(Globallys.nextKey());
        current.setCreateTime(date);
        current.setWaybillKey(conveyance.getWaybillKey());
        current.setDeliveryNumber(conveyance.getDeliveryNumber());
        current.setBarcode(conveyance.getBarcode());
        current.setStartStation(conveyance.getStartStation());
        current.setSimpleStartStation(conveyance.getSimpleStartStation());
        current.setEndStation(conveyance.getEndStation());
        current.setSimpleEndStation(conveyance.getSimpleEndStation());

        current.setOwnerKey(associateUser.getId());
        current.setOrganization(associateUser.getCompany());
        current.setContactName(UserUtil.encodeName(associateUser.getUnamezn()));
        current.setContactPhone(associateUser.getMobilephone());

        current.setAssignFettle(AssignFettle.NOTYET.getCode());
        current.setAssignFettleTime(date);
        current.setHaveChild(false);
        current.setParentKey(conveyance.getId());
        current.setConveyanceFettle(WaybillFettle.BOUND.getCode());
        current.setConveyanceFettleTime(date);
        current.setNodeLevel(conveyance.getNodeLevel() + 1);
        conveyanceMapper.insertSelective(current);

        Conveyance updater = new Conveyance();
        updater.setId(conveyance.getId());
        updater.setAssignFettle(AssignFettle.WHOLE.getCode());
        updater.setHaveChild(true);
        /*
        updater.setOrganization(associateUser.getCompany());
        updater.setContactName(UserUtil.encodeName(associateUser.getUnamezn()));
        updater.setContactPhone(associateUser.getMobilephone());
        */
        updater.setAssignFettleTime(date);
        conveyanceMapper.updateByPrimaryKeySelective(updater);
    }


    /**
     * 分享到项目组
     *
     * @param uKey          操作用户ID
     * @param conveyanceKeys 运单ID
     * @param groupKey      要分享的项目组ID
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void shareGroup(Integer uKey, Collection<Long> conveyanceKeys, Integer groupKey) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作用户ID不能为空");
        Assert.notEmpty(conveyanceKeys, "至少需要一个运单数据");
        Assert.notBlank(groupKey, "要分享的项目组ID不能为空");
        logger.info("分享到项目组 uKey:{} conveyanceKeys:{} groupKey:{}", uKey, conveyanceKeys, groupKey );
        if(!groupService.isMember(groupKey, uKey)){
            throw new BusinessException("你不是该项目成员,不能分享到该项目组");
        }
        try {
            Conveyance updater = new Conveyance();
            for (ConveyanceValidate validate : validateConveyance(conveyanceKeys, uKey, SHARE_GROUP)) {
                updater.setId(validate.getConveyance().getId());
                updater.setOwnerGroupKey(groupKey);
                conveyanceMapper.updateByPrimaryKeySelective(updater);
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("share group -> uKey:{} conveyanceKeys:{} groupKey:{}", uKey, conveyanceKeys, groupKey, e);
            throw BusinessException.dbException("分享到项目组异常");
        }
    }

    /**
     * 更改运单状态
     *
     * @param uKey
     * @param conveyanceKey
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public WaybillFettle changeFettle(Integer uKey, Long conveyanceKey) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作用户ID不能为空");
        Assert.notBlank(conveyanceKey, "运单编号不能为空");
        logger.info("修改运单号 uKey:{} conveyanceKey:{}", uKey, conveyanceKey);
        try {
            Conveyance conveyance = conveyanceMapper.selectByPrimaryKey(conveyanceKey);
            if(conveyance == null){
                throw new BusinessException("该运单不存在");
            }
            if(validate(validateConveyance(conveyance, uKey, EDIT_FETTLE))){
                WaybillFettle fettle = WaybillFettle.convert(conveyance.getConveyanceFettle());
                if(fettle.bind()){
                    fettle = WaybillFettle.ING;
                }else if(fettle.ing()){
                    fettle = WaybillFettle.ARRIVE;
                }else{
                    throw new BusinessException("状态不能修改");
                }
                loopFettle(conveyance, fettle, new Date());
                return fettle;
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("edit number -> uKey:{} conveyanceKey:{}", uKey, conveyanceKey, e);
            throw BusinessException.dbException("修改运单状态异常");
        }
        return null;
    }

    private void loopFettle(Conveyance conveyance, WaybillFettle fettle, Date date){
        if(conveyance != null && conveyance.getConveyanceFettle() < fettle.getCode()){
            Conveyance update = new Conveyance();
            update.setConveyanceFettle(fettle.getCode());
            update.setConveyanceFettleTime(date);
            update.setId(conveyance.getId());
            conveyanceMapper.updateByPrimaryKeySelective(update);
            if(conveyance.getParentKey() != null && conveyance.getParentKey() > 0){
                Conveyance parent = conveyanceMapper.selectByPrimaryKey(conveyance.getParentKey());
                if(fettle.arrive()){
                    if(conveyance.getNextKey() != null && conveyance.getNextKey() > 0 &&
                            !validateArriveChildren(parent)){//如果要更新的状态为已送达,检测是否还有子运单不是已送达状态
                        fettle = WaybillFettle.ING;
                    }
                }
                loopFettle(parent, fettle, date);
            }
        }
    }

    private boolean validateArriveChildren(Conveyance conveyance){
        Example example = new Example(Conveyance.class);
        example.createCriteria().andEqualTo("parentKey", conveyance.getId()).andLessThan("conveyanceFettle", WaybillFettle.ARRIVE.getCode());
        return conveyanceMapper.selectCountByExample(example) <= 0;
    }

    /**
     * 修改运单号
     *
     * @param uKey
     * @param conveyanceKey
     * @param number
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void editNumber(Integer uKey, Long conveyanceKey, String number) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作用户ID不能为空");
        Assert.notBlank(conveyanceKey, "运单编号不能为空");
        Assert.notBlank(number, "运单号不能为空");
        logger.info("修改运单号 uKey:{} conveyanceKey:{} number:{}", uKey, conveyanceKey, number );
        try {
            Conveyance conveyance = conveyanceMapper.selectByPrimaryKey(conveyanceKey);
            if(conveyance == null){
                throw new BusinessException("该运单不存在");
            }
            if(validate(validateConveyance(conveyance, uKey, EDIT_NUMBER))){
                Conveyance updater = new Conveyance();
                updater.setWaybillKey(conveyance.getWaybillKey());
                updater.setConveyanceNumber(number);
                if(conveyanceMapper.selectCount(updater) > 0){
                    throw new BusinessException("运单号重复");
                }
                updater.setWaybillKey(null);
                updater.setId(conveyance.getId());
                conveyanceMapper.updateByPrimaryKeySelective(updater);
                //updateConveyance(updater, AssignFettle.convert(conveyance.getAssignFettle()));
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("edit number -> uKey:{} conveyanceKey:{} number:{}", uKey, conveyanceKey, number, e);
            throw BusinessException.dbException("修改运单号异常");
        }
    }

    /**
     * 修改承运人
     *
     * @param uKey
     * @param cconveyanceKey
     * @param ownerKey
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void editAssign(Integer uKey, Long cconveyanceKey, Integer ownerKey) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作用户ID不能为空");
        Assert.notBlank(ownerKey, "承运人不能为空");
        logger.info("修改承运人 uKey:{} ownerKey:{}", uKey, ownerKey );
        try {
            Conveyance conveyance = conveyanceMapper.selectByPrimaryKey(cconveyanceKey);
            if(conveyance == null){
                throw new BusinessException("该运单不存在");
            }
            if(validate(validateConveyance(conveyance, uKey, EDIT_ASSIGN))){
                Conveyance updater = new Conveyance();
                updater.setId(conveyance.getId());
                updater.setOwnerKey(ownerKey);
                conveyanceMapper.updateByPrimaryKeySelective(updater);
                //updateConveyance(updater, AssignFettle.convert(conveyance.getAssignFettle()));
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("edit assign -> uKey:{} ownerKey:{}", uKey, ownerKey, e);
            throw BusinessException.dbException("修改承运人异常");
        }
    }

    /**
     * 取消指派
     * @param uKey
     * @param conveyanceKeys
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void cancelAssign(Integer uKey, Collection<Long> conveyanceKeys) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作用户ID不能为空");
        Assert.notEmpty(conveyanceKeys, "至少需要一个运单数据");
        logger.info("取消指派 uKey:{} conveyanceKeys:{}", uKey, conveyanceKeys);
        Date date = new Date();
        for (ConveyanceValidate validate : validateConveyance(conveyanceKeys, uKey, CANCEL_ASSIGN)) {
            cancelAssign(validate, date);
        }
    }

    @Override
    public Conveyance queryConveyanceInfo(Long id) throws ParameterException, BusinessException {
        return conveyanceMapper.selectByPrimaryKey(id);
    }

    private Map<Long, Conveyance> mapChildren(Long parentKey, Map<Long, Conveyance> map, boolean cycle){
        Collection<Conveyance> conveyances = conveyanceMapper.listByParentKey(parentKey);
        if(CollectionUtils.isNotEmpty(conveyances)){
            if(map == null){
                map = new HashMap<Long, Conveyance>();
            }
            for (Conveyance conveyance : conveyances) {
                WaybillFettle waybillFettle = WaybillFettle.convert(conveyance.getConveyanceFettle());
                if(waybillFettle.cancel()){
                    continue;
                }
                map.put(conveyance.getId(), conveyance);
                if(conveyance.getHaveChild() && cycle){
                    mapChildren(conveyance.getId(), map, cycle);
                }
            }
        }
        return map;
    }

    private void cancelAssign(ConveyanceValidate validate, Date date) throws ParameterException, BusinessException{
        Conveyance updater = new Conveyance();
        Conveyance conveyance = validate.getConveyance();
        if(conveyance.getHaveChild()){
            Map<Long, Conveyance> mapChildren = mapChildren(conveyance.getId(), null, true);
            if(mapChildren != null){
                for (Map.Entry<Long, Conveyance> entry : mapChildren.entrySet()) {
                    WaybillFettle waybillFettle = WaybillFettle.convert(entry.getValue().getConveyanceFettle());
                    if(!waybillFettle.bind()){
                        throw new BusinessException("该运单"+ waybillFettle.getDesc()  +"不能更改承运人");
                    }
                }
                conveyanceMapper.cancel(new ArrayList<Long>(mapChildren.keySet()));
            }
        }
        updater.setHaveChild(false);
        updater.setId(conveyance.getId());
        updater.setOwnerKey(validate.getOperateKey());
        updater.setContactName(StringUtils.EMPTY);
        updater.setContactPhone(StringUtils.EMPTY);
        updater.setOrganization(StringUtils.EMPTY);
        updater.setAssignFettle(AssignFettle.NOTYET.getCode());
        updater.setAssignFettleTime(date);
        conveyanceMapper.updateByPrimaryKeySelective(updater);
    }

    /**
     * 任务单绑定或更新完成事件
     * <p>
     *
     * @param context 任务单上下文
     * @param binding true:绑定，false:更新
     * @throws BusinessException
     */
    @Override
    public void onCompleteWaybill(WaybillContext context, boolean binding) throws BusinessException {
        Conveyance conveyance = conveyanceMapper.getByWaybillKey(context.getId());
        if(conveyance == null){
            conveyance = defaultProperty(new Conveyance());
            conveyance.setId(Globallys.nextKey());//运单编号
            conveyance.setParentKey(0L);//父运单编号
            conveyance.setWaybillKey(context.getId());
            conveyance.setDeliveryNumber(context.getDeliveryNumber());
            conveyance.setBarcode(context.getBarcode());//二维码号-任务单号
            if(context.getGroupid() != null && context.getGroupid() > 0){
                conveyance.setOwnerGroupKey(context.getGroupid());//设置项目组
            }
            conveyance.setOwnerKey(context.getUserKey());//创建人ID
            conveyance.setCreateTime(context.getBindTime());//生成时间
            conveyance.setConveyanceFettle(WaybillFettle.BOUND.getCode());//状态-带运输
            conveyance.setConveyanceFettleTime(context.getBindTime());//状态变更时间
            conveyance.setAssignFettle(AssignFettle.NOTYET.getCode());//指派状态
            conveyance.setAssignFettleTime(context.getBindTime());//指派时间
            conveyance.setStartStation(context.getStartStation());//发站
            conveyance.setSimpleStartStation(context.getSimpleStartStation());//发站简称
            conveyance.setEndStation(context.getEndStation());//到站
            conveyance.setSimpleEndStation(context.getSimpleEndStation());//到站简称
            conveyance.setHaveChild(false);//没有子节点
            conveyance.setNextKey(0L);//没有后续节点
            conveyance.setNodeLevel(1);//节点层级
            //conveyance.setConveyanceNumber(buildNumber(waybill.getBarcode(), conveyance.getNodeLevel(), 1));
            conveyanceMapper.insertSelective(conveyance);
        }else{
            Conveyance updater = new Conveyance();
            AssignFettle assignFettle = AssignFettle.convert(conveyance.getAssignFettle());
            if(!assignFettle.notyet()){
                if(!StringUtils.equals(context.getStartStation(), conveyance.getStartStation()) ||
                        !StringUtils.equals(context.getEndStation(), conveyance.getEndStation())){
                    throw new BusinessException("运单已经指派,不能更改任务单的始发地和目的地");
                }
            }
            updater.setStartStation(context.getStartStation());
            updater.setSimpleStartStation(context.getSimpleStartStation());
            updater.setEndStation(context.getEndStation());
            updater.setSimpleEndStation(context.getSimpleEndStation());
            updater.setId(conveyance.getId());
            updater.setBarcode(context.getBarcode());
            updater.setDeliveryNumber(context.getDeliveryNumber());
            updateByWaybillKey(updater, context.getId());
        }
    }

    /**
     * 确认收货通知
     * <p>
     *
     * @param context 任务单上下文
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-15 11:51:57
     */
    @Override
    public void onWaybillFettleChange(WaybillContext context) throws BusinessException {
        WaybillFettle fettle = context.getWaybillStatus();
        if((fettle.unbind() || fettle.bind())){ return; }
        Collection<Conveyance> conveyances = conveyanceMapper.listByWaybillKey(context.getId());
        if(CollectionUtils.isNotEmpty(conveyances)){
            Collection<Long> updateKeys = new ArrayList<Long>();
            for (Conveyance conveyance : conveyances) {
                if(fettle == WaybillFettle.convert(conveyance.getConveyanceFettle())){
                    continue;
                }
                if(fettle.ing() && !ConveyanceUtils.isRoot(conveyance)){
                    continue;
                }
                updateKeys.add(conveyance.getId());
            }
            if(CollectionUtils.isNotEmpty(updateKeys)){
                Conveyance updater = new Conveyance();
                updater.setConveyanceFettleTime(new Date());
                updater.setConveyanceFettle(Math.min(fettle.getCode(), WaybillFettle.ARRIVE.getCode()));
                updateByConveyanceKeys(updateKeys, updater);
            }
        }
    }

    /**
     * 删除任务单
     * <p>
     *
     * @param uKey    操作人
     * @param waybill 要删除的任务单
     * @throws BusinessException
     */
    @Override
    public void onDeleteWaybill(Integer uKey, Waybill waybill) throws BusinessException {
        Collection<Conveyance> conveyances = conveyanceMapper.listByWaybillKey(waybill.getId());
        if(CollectionUtils.isNotEmpty(conveyances)){
            Collection<Long> conveyanceKeys = new ArrayList<Long>();
            for (Conveyance conveyance : conveyances) {
                WaybillFettle fettle = WaybillFettle.convert(conveyance.getConveyanceFettle());
                AssignFettle assignFettle = AssignFettle.convert(conveyance.getAssignFettle());
                if(!fettle.cancel() && !assignFettle.notyet()){
                    throw new BusinessException("包含已指派的任务单,请先取消指派再删除");
                }
                conveyanceKeys.add(conveyance.getId());
            }
            if(CollectionUtils.isNotEmpty(conveyanceKeys)){
                Example example = new Example(Conveyance.class);
                example.createCriteria().andIn("id", conveyanceKeys);
                conveyanceMapper.deleteByExample(example);
            }
        }
    }

    /**
     * 通知任务单位置上报
     * <p>
     *
     * @param context
     * @param waybillTrack
     * @param driverScan
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-15 12:54:43
     */
    @Override
    public void notifyLocationReport(WaybillContext context, WaybillTrack waybillTrack, boolean driverScan) throws BusinessException {
        if(StringUtils.isNotBlank(context.getBarcode()) && driverScan){
            Collection<Conveyance> conveyances = conveyanceMapper.listByWaybillKey(context.getId());
            if(CollectionUtils.isNotEmpty(conveyances)){
                Collection<Long> updateKeys = new ArrayList<Long>();
                for (Conveyance conveyance : conveyances) {
                    WaybillFettle fettle = WaybillFettle.convert(conveyance.getConveyanceFettle());
                    if(fettle == WaybillFettle.BOUND && ConveyanceUtils.isRoot(conveyance)){
                        updateKeys.add(conveyance.getId());
                    }
                }
                if(CollectionUtils.isNotEmpty(updateKeys)){
                    Conveyance updater = new Conveyance();
                    updater.setConveyanceFettleTime(new Date());
                    updater.setConveyanceFettle(WaybillFettle.ING.getCode());
                    updateByConveyanceKeys(updateKeys, updater);
                }
            }
        }
    }

    @Override
    public void notifyBarcodeValidate(Barcode barcode, BarcodeContext context) {
        if(context instanceof GroupCodeContext){
            GroupCodeContext codeContext = (GroupCodeContext) context;
            if(codeContext.getSourceType() == null || (!codeContext.getSourceType().oneself() && !codeContext.getSourceType().group())){
                if(conveyanceMapper.countByCode(barcode.getBarcode(), context.getUserKey()) > 0){
                    codeContext.setSourceType(SourceType.ASSIGN);
                }
            }
        }
    }
}
