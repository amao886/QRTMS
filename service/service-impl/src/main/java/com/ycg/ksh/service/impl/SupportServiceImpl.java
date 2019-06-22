package com.ycg.ksh.service.impl;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/23
 */

import com.google.common.collect.Lists;
import com.ycg.ksh.common.dubbo.ServiceMonitor;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.common.extend.mybatis.CustomExample;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.RegionUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.constant.ObjectType;
import com.ycg.ksh.constant.OperateEventType;
import com.ycg.ksh.constant.OperateType;
import com.ycg.ksh.constant.OrderEventType;
import com.ycg.ksh.entity.persistent.ESignBrank;
import com.ycg.ksh.entity.persistent.OperateNote;
import com.ycg.ksh.entity.persistent.Order;
import com.ycg.ksh.entity.persistent.SignAssociate;
import com.ycg.ksh.entity.persistent.support.Area;
import com.ycg.ksh.entity.service.support.GradeArea;
import com.ycg.ksh.entity.service.esign.BrankData;
import com.ycg.ksh.entity.service.support.GradeAreaLast;
import com.ycg.ksh.entity.service.support.OperateNoteAlliance;
import com.ycg.ksh.service.persistence.ESignBrankMapper;
import com.ycg.ksh.service.persistence.OperateNoteMapper;
import com.ycg.ksh.service.persistence.SignAssociateMapper;
import com.ycg.ksh.service.persistence.support.AreaMapper;
import com.ycg.ksh.service.api.SupportService;
import com.ycg.ksh.service.api.UserService;
import com.ycg.ksh.service.observer.OrderObserverAdapter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 辅助支撑服务
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/23
 */
@Service("ksh.core.service.supportService")
public class SupportServiceImpl implements SupportService , OrderObserverAdapter , ServiceMonitor {

    private static final List<String>  AREACACHE = new ArrayList<String>();

    @Resource
    private CacheManager cacheManager;
    @Resource
    private ESignBrankMapper brankMapper;
    @Resource
    private OperateNoteMapper operateNoteMapper;
    @Resource
    private SignAssociateMapper associateMapper;
    @Resource
    private UserService userService;
    @Resource
    private AreaMapper areaMapper;

    /*
    protected LocalCacheManager<BrankData> brankDataCache = new LocalCacheManager<BrankData>(BrankData.class, new CacheAdapter(TimeUnit.DAYS, 1L) {
        public Object persistence(Serializable... parameters) throws ParameterException, BusinessException {
            Collection<ESignBrank> collection = brankMapper.selectAll();
            Collection<String> branks = new ArrayList<String>();//银行名称
            Collection<String> provinces = new ArrayList<String>();//省或直辖市
            Map<String, Collection<String>> cityMap = new HashMap<String, Collection<String>>();//省市映射数据
            collection.parallelStream().forEach(brank -> {
                if(!branks.contains(brank.getBrankName())){
                    branks.add(brank.getBrankName());
                }
                if(!provinces.contains(brank.getProvince())){
                    provinces.add(brank.getProvince());
                }
                Collection<String> citys = Optional.ofNullable(cityMap.get(brank.getProvince())).orElseGet(() ->{
                   return new ArrayList<String>();
                });
                if(!citys.contains(brank.getCity())){
                    citys.add(brank.getCity());
                }
                cityMap.put(brank.getProvince(), citys);
            });
            return new BrankData(branks, provinces, cityMap);
        }

        @Override
        public CacheManager getCacheManager() {
            return cacheManager;
        }
    });

    protected LocalCacheManager<Collection<ESignBrank>> signBrankCache= new LocalCacheManager<Collection<ESignBrank>>(ESignBrank.class, new CacheAdapter(TimeUnit.DAYS, 1L) {
        public Object persistence(Serializable... parameters) throws ParameterException, BusinessException {
            CustomExample example = new CustomExample(ESignBrank.class);
            Example.Criteria criteria = example.createCriteria();
            if(parameters.length > 0){
                criteria.andEqualTo("brankName", String.valueOf(parameters[0]));
            }
            if(parameters.length > 1){
                criteria.andEqualTo("province", String.valueOf(parameters[1]));
            }
            if(parameters.length > 2){
                criteria.andEqualTo("city", String.valueOf(parameters[2]));
            }
            example.orderBy("branchName");
            return brankMapper.selectByExample(example);
        }

        @Override
        public CacheManager getCacheManager() {
            return cacheManager;
        }
    });
    */
    private String[] branchKeys(ESignBrank brank){
        return new String[]{brank.getBrankName(), brank.getBrankName()+ brank.getProvince(), brank.getBrankName() + brank.getProvince()+ brank.getCity(), brank.getProvince()+ brank.getCity()};
    }

    /**
     * 加载银行数据
     *
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public BrankData loadBrankData() throws ParameterException, BusinessException {
        Collection<ESignBrank> collection = brankMapper.selectAll();
        ArrayList<String> branks = new ArrayList<String>();//银行名称
        Collection<String> provinces = new ArrayList<String>();//省或直辖市
        Map<String, Collection<String>> cityMap = new HashMap<String, Collection<String>>();//省市映射数据
        collection.parallelStream().forEach(brank -> {
            if(!branks.contains(brank.getBrankName())){
                branks.add(brank.getBrankName());
            }
            if(!provinces.contains(brank.getProvince())){
                provinces.add(brank.getProvince());
            }
            Collection<String> citys = Optional.ofNullable(cityMap.get(brank.getProvince())).orElseGet(ArrayList::new);
            if(!citys.contains(brank.getCity())){
                citys.add(brank.getCity());
            }
            cityMap.put(brank.getProvince(), citys);
        });
//临时排序
        List<String> list = Lists.newArrayList("工商银行","农业银行","中国银行","建设银行","交通银行");
        for (int i = 0; i < branks.size(); i++) {
            String bak = branks.get(i);
            if (StringUtils.isEmpty(bak)) {
                continue;
            }
            for (String b : list) {
                if(bak.indexOf(b) != -1){
                    branks.remove(bak);
                    branks.add(0,bak);
                }
            }

        }
//结束
        return new BrankData(branks, provinces, cityMap);
        /*
        return cacheManager.get("BRANK-DATA", TimeUnit.DAYS.toMinutes(1L), () -> {
            Collection<ESignBrank> collection = brankMapper.selectAll();
            Collection<String> branks = new ArrayList<String>();//银行名称
            Collection<String> provinces = new ArrayList<String>();//省或直辖市
            Map<String, Collection<String>> cityMap = new HashMap<String, Collection<String>>();//省市映射数据
            collection.parallelStream().forEach(brank -> {
                if(!branks.contains(brank.getBrankName())){
                    branks.add(brank.getBrankName());
                }
                if(!provinces.contains(brank.getProvince())){
                    provinces.add(brank.getProvince());
                }
                Collection<String> citys = Optional.ofNullable(cityMap.get(brank.getProvince())).orElseGet(() ->{
                    return new ArrayList<String>();
                });
                if(!citys.contains(brank.getCity())){
                    citys.add(brank.getCity());
                }
                cityMap.put(brank.getProvince(), citys);
            });
            return new BrankData(branks, provinces, cityMap);
        });
        */
    }

    /**
     * 根据大额行号获取银行数据
     *
     * @param prcptcd
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public ESignBrank getESignBrankByPrcptcd(String prcptcd) throws ParameterException, BusinessException {
        if(StringUtils.isNotBlank(prcptcd)){
           return brankMapper.getByBrankCode(prcptcd);
        }
        return null;
    }

    @Override
    public Collection<ESignBrank> loadBranks(String brank, String province, String city) throws ParameterException, BusinessException {
        CustomExample example = new CustomExample(ESignBrank.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(brank)){
            criteria.andEqualTo("brankName", brank);
        }
        if(StringUtils.isNotBlank(province)){
            criteria.andEqualTo("province", province);
        }
        if(StringUtils.isNotBlank(city)){
            criteria.andEqualTo("city", city);
        }
        example.orderBy("branchName");
        return brankMapper.selectByExample(example);
    }
    /**
     * 更新订单
     *
     * @param uKey
     * @param order
     * @param eventType
     * @throws BusinessException
     */
    @Override
    public void modifyOrder(Integer uKey, Order order, OrderEventType eventType) throws BusinessException {
        if(eventType.log()){
            String logContext = null;
            if(OrderEventType.RECEIVE == eventType){
                if(StringUtils.isNotEmpty(order.getLocation())){
                    logContext = order.getLocation();
                }
            }
            if(OrderEventType.LOCATE == eventType){
                if(StringUtils.isNotEmpty(order.getLocation())){
                    logContext = order.getLocation();
                }
            }
            if(OrderEventType.UPLOADRECEIPT == eventType){
                if(StringUtils.isNotEmpty(order.getLocation())){
                    logContext = order.getLocation();
                }
            }
            if(StringUtils.isEmpty(logContext)){
                logContext = eventType.getDesc();
            }
            saveOperateNote(uKey, OperateType.ORDER, order.getId(), eventType, logContext);
        }

    }

    /**
     * 操作日志记录
     *
     * @param uKey
     * @param operateType
     * @param hostKey
     * @param eventType
     * @param context
     */
    @Override
    public void saveOperateNote(Integer uKey, OperateType operateType, Serializable hostKey, OperateEventType eventType, String context) {
        saveOperateNote(uKey, operateType, hostKey, eventType, context, null);
    }

    /**
     * 操作日志记录
     *
     * @param uKey
     * @param operateType
     * @param hostKey
     * @param eventType
     * @param context
     * @param adjunctKey
     */
    @Override
    public void saveOperateNote(Integer uKey, OperateType operateType, Serializable hostKey, OperateEventType eventType, String context, Serializable adjunctKey) {
        try{
            OperateNote operateNote = new OperateNote();
            operateNote.setId(Globallys.nextKey());
            operateNote.setHostType(operateType.getCode());
            operateNote.setCreateTime(new Date());
            operateNote.setHostId(String.valueOf(hostKey));
            operateNote.setLogType(eventType.getCode());
            operateNote.setLogContext(context);
            operateNote.setUserKey(uKey);
            if(adjunctKey != null){
                operateNote.setAdjunctKey(String.valueOf(adjunctKey));
            }
            operateNote.setUserKey(uKey);
            operateNoteMapper.insert(operateNote);
        }catch (Exception e){
            logger.error("操作记录异常 uKey:{} operateType:{} hostKey:{} eventType:{} context:{} adjunctKey:{}", uKey, operateType.getDesc(), hostKey, eventType.getDesc(), context, adjunctKey, e);
            //throw new BusinessException("记录订单操作异常");
        }
    }

    @Override
    public Collection<OperateNoteAlliance> listOperateNoteByType(OperateType operateType, Serializable hostKey) throws ParameterException, BusinessException {
        String hKey = String.valueOf(hostKey);
        Assert.notBlank(hKey, "关联主键不能为空");
        try{
            Example example = new Example(OperateNote.class);
            example.createCriteria().andEqualTo("hostType", operateType.getCode()).andEqualTo("hostId", hKey);
            example.orderBy("createTime").desc();
            example.orderBy("logType");
            Collection<OperateNote> collection = operateNoteMapper.selectByExample(example);
            if(CollectionUtils.isNotEmpty(collection)){
                Collection<OperateNoteAlliance> alliances = new ArrayList<OperateNoteAlliance>();
                for (OperateNote operateNote : collection) {
                    OperateNoteAlliance alliance = new OperateNoteAlliance(operateNote);
                    if(operateNote.getUserKey() != null && operateNote.getUserKey() > 0){
                        alliance.setUser(userService.getConciseUser(operateNote.getUserKey()));
                    }
                    alliances.add(alliance);
                }
                return alliances;
            }
            return Collections.emptyList();
        }catch (Exception e){
            logger.error("操作记录查询异常 operateType:{} hostKey:{}", operateType, hostKey, e);
            throw new BusinessException("操作记录查询异常");
        }
    }

    /**
     * 保存第三方关联信息
     *
     * @param associate
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void saveSignAssociate(SignAssociate associate) throws ParameterException, BusinessException {
        Assert.notNull(associate, "关联信息不能为空");
        Assert.notBlank(associate.getObjectType(), "关联信息不能为空");
        Assert.notBlank(associate.getObjectKey(), "关联信息不能为空");
        Assert.notBlank(associate.getThirdType(), "关联信息不能为空");
        Assert.notBlank(associate.getThirdObjectKey(), "关联信息不能为空");

        associate.setOperateTime(new Date());
        associateMapper.insert(associate);
    }

    /**
     * 查询第三方关联信息
     *
     * @param objectType
     * @param objectKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public SignAssociate getSignAssociate(ObjectType objectType, Serializable objectKey) throws ParameterException, BusinessException {
        return associateMapper.selectOne(new SignAssociate(objectType, objectKey));
    }


    /**
     * @param parentKey
     *
     * @return
     */
    public Collection<Area> listAreaByParentKey(Integer parentKey) throws ParameterException, BusinessException{
        return cacheManager.get("AREA#" + parentKey, 60L, ()->{
            return areaMapper.selectByParentKey(parentKey);
        });
    }


    /**
     * @param parentKey
     * @return 省市区
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Collection<GradeAreaLast> listGradeArea(Integer parentKey) throws ParameterException, BusinessException {
        Collection<Area> collection = areaMapper.selectByParentKey(parentKey);
        return Optional.ofNullable(collection).map(cc -> {
            return cc.parallelStream().map(a->{
                if(a.getSubordinate()){
                    return new GradeArea(a.getCode(), a.getName(), listGradeArea(a.getId()));
                }else{
                    return new GradeAreaLast(a.getCode(), a.getName());
                }
            }).collect(Collectors.toList());
        }).orElse(Collections.emptyList());
    }

    @Override
    public void onServerStrated(ApplicationContext context, String activeProfile) throws BusinessException {
        logger.info("开始加载省市区数据");
        Globallys.executor(new Runnable() {
            @Override
            public void run() {
                Collection<Area> provinces = listAreaByParentKey(0);
                if(CollectionUtils.isNotEmpty(provinces)){
                    for (Area province : provinces) {
                        AREACACHE.add(province.getName());
                        Collection<Area> cities = listAreaByParentKey(province.getId());
                        if(CollectionUtils.isNotEmpty(cities)){
                            for (Area city : cities) {
                                AREACACHE.add(province.getName()+ city.getName());
                                Collection<Area> districts = listAreaByParentKey(city.getId());
                                if(CollectionUtils.isNotEmpty(districts)){
                                    for (Area district : districts) {
                                        AREACACHE.add(province.getName() + city.getName() + district.getName());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean validateArea(String address) {
        String[] areas = RegionUtils.analyze(address);
        if(areas.length < 3){
            return false;
        }
        return validateArea(areas[0], areas[1], areas[2]);
    }

    @Override
    public boolean validateArea(String p, String c, String d) {
        StringBuilder builder = new StringBuilder();
        if(StringUtils.isNotBlank(p)){
            if(StringUtils.equelsOne(p, "上海市", "北京市", "天津市", "重庆市")){
                builder.append(p, 0, p.length() - 1);//去掉‘市’
            }else{
                builder.append(p);
            }
        }
        if(StringUtils.isNotBlank(c)){
            builder.append(c);
        }
        if(StringUtils.isNotBlank(d)){
            builder.append(d);
        }
        return AREACACHE.contains(builder.toString());
    }
}
