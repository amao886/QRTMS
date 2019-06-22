package com.ycg.ksh.service.impl;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/17
 */

import com.github.pagehelper.Page;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.DateFormatUtils;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.CompanyTemplate;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.persistent.moutai.Convey;
import com.ycg.ksh.entity.persistent.moutai.Customer;
import com.ycg.ksh.entity.persistent.moutai.Order;
import com.ycg.ksh.entity.persistent.moutai.Taker;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.moutai.MaotaiOrder;
import com.ycg.ksh.entity.service.moutai.MoutaiCustomerSearch;
import com.ycg.ksh.entity.service.moutai.OrderPrint;
import com.ycg.ksh.service.persistence.moutai.MaotaiConveyMapper;
import com.ycg.ksh.service.persistence.moutai.MaotaiCustomerMapper;
import com.ycg.ksh.service.persistence.moutai.MaotaiOrderMapper;
import com.ycg.ksh.service.persistence.moutai.MaotaiTakerMapper;
import com.ycg.ksh.service.api.CompanyService;
import com.ycg.ksh.service.api.MoutaiService;
import com.ycg.ksh.service.api.UserService;
import com.ycg.ksh.service.support.assist.MaotaiDepot;
import com.ycg.ksh.service.support.assist.MoutaiUtils;
import com.ycg.ksh.service.support.word.WordFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 茅台打印
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/17
 */
@Service("ksh.core.service.moutaiService")
public class MoutaiServiceImpl implements MoutaiService {

    @Resource
    CacheManager cacheManager;
    @Resource
    MaotaiOrderMapper maotaiOrderMapper;
    @Resource
    MaotaiCustomerMapper maotaiCustomerMapper;
    @Resource
    MaotaiConveyMapper maotaiConveyMapper;
    @Resource
    MaotaiTakerMapper maotaiTakerMapper;
    @Resource
    UserService userService;
    @Resource
    CompanyService companyService;


    private static final String GLOBAL_COUNT_KEY = "GCK#%s";


    /**
     * 获取全局计数
     * @return
     */
    private long globalCountIndex(){
        String cacheKey = String.format(GLOBAL_COUNT_KEY, DateFormatUtils.format(new Date(), "yyyyMMdd"));
        long index = cacheManager.increment(cacheKey, 1);
        if(index == 1){
            cacheManager.expire(cacheKey, 25L, TimeUnit.HOURS);//有效期25小时
        }
        logger.info("茅台全局计数 {} {}", cacheKey, index);
        return index;
    }

    /**
     * 批量导入订单
     *
     * @param uKey
     * @param conveyKey
     * @param mapData
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Map<String, String> saveOrders(Integer uKey, Long conveyKey, Map<String, Order> mapData) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人编号不能为空");
        Assert.notNull(conveyKey, "承运单位编号不能为空");
        if (conveyKey < 1)
            throw new ParameterException("承运单位标号不能为空");
        Assert.notEmpty(mapData, "导入数据不能为空");
        try {
            Date cdate = new Date();
            Map<String, String> exceptions = new HashMap<String, String>();
            Collection<Order> collection = new ArrayList<Order>(mapData.size());
            for (Map.Entry<String, Order> stringOrderEntry : mapData.entrySet()) {
                Order order = stringOrderEntry.getValue();
                order.setId(Globallys.nextKey());
                order.setCreateTime(cdate);
                order.setCreateUserId(uKey);
                order.setConveyId(conveyKey);
                collection.add(order);
            }
            if (CollectionUtils.isNotEmpty(collection)) {
                maotaiOrderMapper.inserts(collection);
            }
            return exceptions;
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("saveOrders -> uKey:{}", uKey, e);
            throw BusinessException.dbException("批量保存订单信息异常");
        }
    }

    /**
     * 批量导入客户信息
     *
     * @param uKey
     * @param mapData
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Map<String, String> saveCustomers(Integer uKey, Map<String, Customer> mapData) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人编号不能为空");
        Assert.notEmpty(mapData, "导入数据不能为空");
        try {
            Date cdate = new Date();
            Map<String, String> exceptions = new HashMap<String, String>();
            Collection<Customer> collection = new ArrayList<Customer>(mapData.size());
            Collection<String> existers = maotaiCustomerMapper.listKeys();
            for (Map.Entry<String, Customer> stringCustomerEntry : mapData.entrySet()) {
                Customer customer = stringCustomerEntry.getValue();

                if (existers.contains(customer.getCustomerNo())) {
                    exceptions.put(stringCustomerEntry.getKey(), "该项数据已经存在");
                } else {
                    customer.setCreateTime(cdate);
                    customer.setCreateUserId(uKey);
                    collection.add(customer);
                    existers.add(customer.getCustomerNo());
                }
            }
            if (CollectionUtils.isNotEmpty(collection)) {
                maotaiCustomerMapper.inserts(collection);
            }
            return exceptions;
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("saveCustomers -> uKey:{}", uKey, e);
            throw BusinessException.dbException("批量保存客户信息异常");
        }
    }


    @Override
    public List<Convey> listMoutaiConvey() {
        return maotaiConveyMapper.selectAll();
    }

    @Override
    public CustomPage<Order> queryOrderList(MaotaiOrder search, PageScope pageScope) throws ParameterException, BusinessException {
        if (pageScope == null) {
            pageScope = PageScope.DEFAULT;
        }
        search.handle();
        Page<Order> page = maotaiOrderMapper.searchOrderPage(search, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
        for (Order order : page) {
            OrderHandler(order);
        }
        return new CustomPage<>(page.getPageNum(), page.getPageSize(), page.getTotal(), page);
    }

    @Override
    public void addDepot(String depot, List<String> ids) throws ParameterException, BusinessException {
        Assert.hasText(depot, "请输入仓库");
        Assert.notEmpty(ids, "请选择发货单号");
        maotaiOrderMapper.updateOrderForAddDepot(depot, ids);
    }

    @Override
    public Collection<OrderPrint> getOrderPrintDataById(Collection<Long> orderKeys, String username) throws ParameterException, BusinessException{
        Assert.notEmpty(orderKeys, "至少需要选择一条数据");
        Collection<Order> maotaiOrders = maotaiOrderMapper.selectOrderByIds(orderKeys);
        if(CollectionUtils.isEmpty(maotaiOrders)){
            throw new BusinessException("未获取到选择的数据");
        }
        Map<Long, Taker> takerMap = selectTakers();//取货人-启用状态
        Map<Long, Convey> conveyMap = MoutaiUtils.conveyConvert(maotaiConveyMapper.selectAll()); //承运单位
        Map<String, Customer> customerMap = MoutaiUtils.conveyCustomer(maotaiCustomerMapper.selectAll());//收货客户
        Map<Integer, MaotaiDepot> depotMap = MaotaiDepot.toMap();//仓库
        Map<String, OrderPrint> printMap = new HashMap<String, OrderPrint>();
        LocalDate localDate = LocalDate.now();
        for (Order order : maotaiOrders) {
            String printKey = order.getStoreId() +"_"+ order.getCustomerNo();
            OrderPrint object = Optional.ofNullable(printMap.get(printKey)).orElseGet(()->{
                return buildOrderPrint(printKey, username, order, localDate, takerMap, conveyMap, customerMap, depotMap);
            });
            object.add(order);

            printMap.put(printKey, object);

        }
        Collection<OrderPrint> collection = new ArrayList<OrderPrint>();
        printMap.values().parallelStream().forEach(op -> {
            List<Order> orders = op.getList();
            while (CollectionUtils.isNotEmpty(orders)){
                OrderPrint orderPrint = op.copy();
                int index = 0;
                for (Iterator<Order> iterator = orders.iterator(); iterator.hasNext(); ) {
                    if(index++ >= 8){ break; }
                    orderPrint.add(iterator.next());
                    iterator.remove();
                }
                collection.add(orderPrint);
            }
        });
        return collection;
    }


    /**
     * 查询启用的取货人信息
     *
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    private Map<Long, Taker> selectTakers() throws ParameterException, BusinessException {
        Example example = new Example(Taker.class);
        example.createCriteria().andEqualTo("fettle", 1);
        return MoutaiUtils.conveyTaker(maotaiTakerMapper.selectByExample(example));
    }

    private OrderPrint buildOrderPrint( String printKey, String uname, Order order, LocalDate localDate, Map<Long, Taker> takerMap, Map<Long, Convey> conveyMap, Map<String, Customer> customerMap, Map<Integer, MaotaiDepot> depotMap){
        Customer customer = customerMap.get(order.getCustomerNo());
        if (customer == null) {
            throw new ParameterException("有不存在收货客户或系统中不存在的收货客户的数据");
        }
        Convey convey = conveyMap.get(order.getConveyId());
        /*
        if (convey == null) {
            throw new ParameterException("有不存在承运商的数据");
        }
        */
        Taker taker = takerMap.get(order.getConveyId());
        /*
        if (taker == null) {
            throw new ParameterException(convey.getConveyName()+",没有启用的取到人数据");
        }
        */
        MaotaiDepot depot = depotMap.get(order.getStoreId());
        OrderPrint printObject = new OrderPrint(printKey);

        if (StringUtils.isEmpty(order.getProvince()) && StringUtils.isEmpty(order.getContactName())) {  //为空去客户表取默认数据
            printObject.setDetailaddress(customer.getProvince(), customer.getCity(), customer.getAddress());
            printObject.setContact(customer.getContactName(), customer.getContactPhone(), customer.getFax());

        }else{
            printObject.setDetailaddress(order.getProvince(), order.getCity(), order.getAddress());
            printObject.setContact(order.getContactName(), order.getContactTel(), customer.getFax());
            printObject.setFax(customer.getFax());
        }
        printObject.setDate(localDate);
        printObject.setCustomerName(customer.getCustomerName());
        printObject.setUsername(uname);
        printObject.setDepot(depot.getDepot());
        if(convey != null){
            printObject.setConveyName(convey.getConveyName());
        }else{
            printObject.setConveyName("");
        }
        if(taker != null){
            printObject.setTakeIdcare(taker.getTakeIdcare());
            printObject.setTakeName(taker.getTakeName());
        }else{
            printObject.setTakeIdcare("");
            printObject.setTakeName("");
        }
        return printObject;
    }

    /**
     * 生成word
     *
     * @param uKey
     * @param orderKeys
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public FileEntity buildWord(Integer uKey, Collection<Long> orderKeys) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人用户编号不能为空");
        CompanyTemplate template = companyService.getTemplateByCompanyByKey(200197841102849L);
        if(template == null){
            throw new BusinessException("没有配置导出模板数据");
        }
        String user_temp_path = SystemUtils.directoryUserTemp(uKey), keyString = "%s月份发货记录/%s%s%s%s(%s)";//05月份发货记录/0531客户名称(仓库)
        try{
            User user = userService.getUserByKey(uKey);
            Collection<OrderPrint> collection = getOrderPrintDataById(orderKeys, user.getUnamezn());
            Map<String, List<OrderPrint>> maps = collection.parallelStream().collect(
                Collectors.groupingByConcurrent(o -> {
                    o.fill();//不足8个的填充空对象
                    //%02d ---- 流水号不足两位的前面补0
                    return String.format(keyString, o.getMonth(), o.getMonth(), o.getDay(), "%02d", o.getCustomerName(), o.getDepot());
                })
            );
            WordFactory factory = WordFactory.getInstance();
            String cssContent = new String(Base64.decodeBase64(template.getCssString()), Charset.forName("utf-8"));
            String htmlTemplate = new String(Base64.decodeBase64(template.getHtmlString()), Charset.forName("utf-8"));
            for (Map.Entry<String, List<OrderPrint>> entry : maps.entrySet()) {
                String name = String.format(entry.getKey(), globalCountIndex());//流水号
                String filePath = FileUtils.path(user_temp_path, FileUtils.appendSuffix(name, FileUtils.DOC_SUFFIX));
                factory.createByTemplate(filePath, entry.getValue(), htmlTemplate, cssContent);
            }
            return new FileEntity(FileUtils.zip(user_temp_path, SystemUtils.directoryDownload()), collection.size());
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("导出WORD文件异常 --> uKey:{} orderKeys:{}", uKey, orderKeys, e);
            throw BusinessException.dbException("导出WORD文件异常");
        }finally {
            try {
                FileUtils.deleteDirectory(user_temp_path);
            } catch (IOException e) {  }
        }
    }

    @Override
    public void updateOrderPrintSign(List<Long> ids) {
        maotaiOrderMapper.updatePrintSignByIds(ids, 1);
    }

    @Override
    public List<Long> checkPrintDataByIds(Collection<Long> ids) {
        Collection<Order> orders = maotaiOrderMapper.selectOrderByIds(ids);

        List<Long> conveyList = maotaiConveyMapper.selectAll().stream()
                .map(convey -> convey.getId())
                .collect(Collectors.toList());
        List<String> customerList = maotaiCustomerMapper.selectAll()
                .stream().map(customer -> customer.getCustomerNo())
                .collect(Collectors.toList());
        Map<Long, Taker> takerMap = selectTakers();

        return MoutaiUtils.handleFilterOrderPrint(orders, conveyList, customerList, takerMap);
    }

    @Override
    public Collection<String> listMoutaiCustomerKey() {
        return maotaiCustomerMapper.listKeys();
    }


    public void OrderHandler(Order order) {
        String customerNo = "";
        if (order != null && (customerNo = order.getCustomerNo()) != null
                && StringUtils.isEmpty(order.getProvince()) && StringUtils.isEmpty(order.getContactName())) {
            Customer customer = maotaiCustomerMapper.selectByPrimaryKey(order.getCustomerNo());
            if (customer != null) {
                order.setContactName(customer.getContactName());
                order.setContactTel(customer.getContactPhone());
                order.setProvince(customer.getProvince());
                order.setCity(customer.getCity());
                order.setAddress(customer.getAddress());
            }
        }
    }

    @Override
    public Order searchOneOrderById(Long id) throws ParameterException, BusinessException {
        Order order = maotaiOrderMapper.selectByPrimaryKey(id);
        OrderHandler(order);
        return order;
    }

    @Override
    public void updateOrderById(Order order) throws ParameterException, BusinessException {
        Assert.notBlank(order.getId(), "Id不能为空");
        maotaiOrderMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    public void deleteOrderById(List<String> ids) throws ParameterException, BusinessException {
        Assert.notEmpty(ids, "请选择一条发货单");
        maotaiOrderMapper.deleteBatchByIds(ids);
    }

    @Override
    public CustomPage<Customer> queryCustomerList(MoutaiCustomerSearch search, PageScope pageScope) {
        if (pageScope == null) {
            pageScope = PageScope.DEFAULT;
        }
        search.handle();
        Page<Customer> page = maotaiCustomerMapper.queryCustomerList(search, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
        return new CustomPage<>(page.getPageNum(), page.getPageSize(), page.getTotal(), page);
    }

    @Override
    public Customer searchOneCustomerById(String customerNo) {
        Assert.notBlank(customerNo, "id不能为空");
        return maotaiCustomerMapper.selectByPrimaryKey(customerNo);
    }

    @Override
    public void updateCustomerById(Customer customer) {
        Assert.notBlank(customer.getCustomerNo(), "编号不能为空");
        maotaiCustomerMapper.updateByPrimaryKeySelective(customer);
    }

    @Override
    public void addCustomer(Customer customer) throws ParameterException, BusinessException {
        String customerNo = customer.getCustomerNo();
        Assert.notBlank(customerNo, "收货客户编号不能为空");
        Assert.notBlank(customer.getProvince(), "收货客户省份不能为空");
        Assert.notBlank(customer.getCity(), "收货客户市不能为空");
        Assert.notBlank(customer.getAddress(), "收货客户详细地址不能为空");
        Assert.notBlank(customer.getCustomerName(), "收货客户名称不能为空");
//        Assert.notBlank(customer.getContactName(), "收货客户联系人员不能为空");
//        Assert.notBlank(customer.getContactPhone(), "收货客户联系电话不能为空");
        Collection<String> existers = maotaiCustomerMapper.listKeys();
        if (existers.contains(customerNo)) {
            throw new ParameterException("收货客户的客户编号不能重复");
        }
        customer.setCreateTime(new Date());
        maotaiCustomerMapper.insert(customer);
    }

    @Override
    public void deleteCustomerBycustomerNos(List<String> customerNos) {
        Assert.notEmpty(customerNos, "id不能空");
        maotaiCustomerMapper.deleteBatchBycustomerNos(customerNos);
    }

    @Override
    public CustomPage<Taker> queryTakerList(PageScope pageScope) {
        if (pageScope == null) {
            pageScope = PageScope.DEFAULT;
        }
        Page<Taker> page = maotaiTakerMapper.queryTakerList(new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
        return new CustomPage<>(page.getPageNum(), page.getPageSize(), page.getTotal(), page);
    }

    @Override
    public void addTaker(Taker taker) throws ParameterException, BusinessException {
        if (taker.getFettle() == 1 && this.selectTakerWithInUse(taker.getConveyId()) != null) {
            throw new BusinessException("同一个承运单位只能有一条记录时启用状态");
        }
        taker.setCreateTime(new java.util.Date());
        taker.setId(Globallys.nextKey());
        maotaiTakerMapper.insert(taker);
    }

    @Override
    public Taker searchOneTakerById(Long id) {
        Assert.notBlank(id, "id不能为空");
        Taker taker = maotaiTakerMapper.selectByPrimaryKey(id);
        if (taker.getFettle() == null || taker.getFettle() < 0) {
            taker.setFettle(0);
        }
        return taker;
    }

    @Override
    public void updateTakerById(Taker taker) throws ParameterException, BusinessException {
        Assert.notBlank(taker.getId(), "id不能为空");
        Assert.notBlank(taker.getConveyId(), "承运单位不能为空");
        Taker takerWithInUse = this.selectTakerWithInUse(taker.getConveyId());
        if (taker.getFettle() == 1) {//更新操作为：更新数据为启用状态
            if (takerWithInUse != null && !taker.getId().equals(takerWithInUse.getId())) { //Long值超过-128~127后不可以用==
                throw new BusinessException("同一个承运单位只能有一条记录时启用状态");
            }
            //else: ==null,当前承运单位都未启用, id = id : 代表可以修改的是当前启用的数据
        }
        maotaiTakerMapper.updateByPrimaryKeySelective(taker);
    }


    /**
     * 承运单位当前处于使用中的取单人
     * 一个承运单位同一时间只能有一条处于使用中的数据
     *
     * @param conveyId
     * @return
     */
    public Taker selectTakerWithInUse(Long conveyId) {
        List<Taker> takerList = maotaiTakerMapper.selectTakerByConveyId(conveyId)
                .parallelStream()
                .filter(takerItem -> takerItem.getFettle() == 1)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(takerList)) {
            return null;
        } else {
            return takerList.get(0);
        }
    }


}
