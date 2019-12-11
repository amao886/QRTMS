package com.ycg.ksh.service.impl;

import com.github.pagehelper.Page;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.constant.Directory;
import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.MessageException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.QRCode;
import com.ycg.ksh.common.extend.cache.LocalCacheManager;
import com.ycg.ksh.common.extend.mybatis.CustomExample;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.extend.rabbitmq.RabbitMessageListener;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.*;
import com.ycg.ksh.common.validate.Validator;
import com.ycg.ksh.entity.adapter.AutoMapLocation;
import com.ycg.ksh.common.extend.rabbitmq.MediaMessage;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.adapter.api.AutoMapService;
import com.ycg.ksh.adapter.api.MessageQueueService;
import com.ycg.ksh.service.util.P;
import com.ycg.ksh.constant.CoreConstants;
import com.ycg.ksh.constant.EmployeeType;
import com.ycg.ksh.service.util.O;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.persistent.enterprise.CompanyCustomer;
import com.ycg.ksh.entity.persistent.enterprise.EmployeeCustomer;
import com.ycg.ksh.entity.service.AddressSearch;
import com.ycg.ksh.entity.service.MergeCustomer;
import com.ycg.ksh.entity.service.WaybillContext;
import com.ycg.ksh.entity.service.enterprise.*;
import com.ycg.ksh.entity.service.plan.PlanAlliance;
import com.ycg.ksh.entity.service.plan.PlanTemplate;
import com.ycg.ksh.service.persistence.CustomerGroupMapper;
import com.ycg.ksh.service.persistence.CustomerMapper;
import com.ycg.ksh.service.persistence.ProjectGroupMapper;
import com.ycg.ksh.service.persistence.enterprise.CompanyCustomerMapper;
import com.ycg.ksh.service.persistence.enterprise.EmployeeCustomerMapper;
import com.ycg.ksh.service.api.CompanyService;
import com.ycg.ksh.service.api.CustomerService;
import com.ycg.ksh.service.api.UserService;
import com.ycg.ksh.service.observer.*;
import com.ycg.ksh.service.support.assist.LocalCacheFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("ksh.core.service.customerService")
public class CustomerServiceImpl implements CustomerService, RabbitMessageListener, WaybillObserverAdapter, CompanyObserverAdapter, OrderObserverAdapter, TransferObserverAdapter, PlanOrderObserverAdapter {

    @Resource
    private MessageQueueService queueService;

    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private CustomerGroupMapper customerGroupMapper;
    @Resource
    private AutoMapService autoMapService;
    @Resource
    private ProjectGroupMapper projectGroupMapper;
    @Resource
    private EmployeeCustomerMapper employeeCustomerMapper;

    @Resource
    private CompanyService companyService;
    @Resource
    private CompanyCustomerMapper companyCustomerMapper;
    @Resource
    private UserService userService;

    @Autowired(required = false)
    Collection<CustomeObserverAdapter> observerAdapters;


    private Collection<MergeCustomer> mergeCustomer(Collection<Customer> results) {
        Collection<MergeCustomer> collection = new ArrayList<MergeCustomer>(results.size());
        LocalCacheManager<ProjectGroup> cache = LocalCacheFactory.createCache(projectGroupMapper);
        for (Customer customer : results) {
            try {
                MergeCustomer mergeCustomer = new MergeCustomer(customer);
                mergeCustomer.setGroup(cache.get(customer.getGroupId()));
                collection.add(mergeCustomer);
            } catch (Exception e) {
            }
        }
        cache = null;
        return collection;
    }

    /**
     * 分页查询根据热点排序
     *
     * @param search
     * @param pageScope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public CustomPage<Customer> pageQueryByHotspot(AddressSearch search, PageScope pageScope) throws ParameterException, BusinessException {
        Page<Customer> page = customerMapper.selectCustomer(search, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
        return new CustomPage<Customer>(page.getPageNum(), page.getPageSize(), page.getTotal(), page);
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-02 13:27:08
     * @see com.ycg.ksh.service.api.CustomerService#queryMergeCustomer(AddressSearch)
     * <p>
     */
    @Override
    public Collection<MergeCustomer> queryMergeCustomer(AddressSearch search) throws ParameterException, BusinessException {
        return mergeCustomer(customerMapper.queryCustomer(search));
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-02 13:27:08
     * @see com.ycg.ksh.service.api.CustomerService#pageQueryMergeCustomer(AddressSearch, PageScope)
     * <p>
     */
    @Override
    public CustomPage<MergeCustomer> pageQueryMergeCustomer(AddressSearch search, PageScope scope) throws ParameterException, BusinessException {
        Page<Customer> page = customerMapper.queryCustomer(search, new RowBounds(scope.getPageNum(), scope.getPageSize()));
        return new CustomPage<MergeCustomer>(page.getPageNum(), page.getPageSize(), page.getTotal(), mergeCustomer(page));
    }

    /**
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-26 14:39:21
     * @see com.ycg.ksh.service.api.CustomerService#pageQueryCustomer(AddressSearch, PageScope)
     * <p>
     */
    @Override
    public CustomPage<Customer> pageQueryCustomer(AddressSearch search, PageScope scope) throws ParameterException, BusinessException {
        Page<Customer> page = customerMapper.queryCustomer(search, new RowBounds(scope.getPageNum(), scope.getPageSize()));
        return new CustomPage<Customer>(page.getPageNum(), page.getPageSize(), page.getTotal(), page);
    }

    /**
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-26 15:52:36
     * @see com.ycg.ksh.service.api.CustomerService#queryCustomer(AddressSearch)
     * <p>
     */
    @Override
    public Collection<Customer> queryCustomer(AddressSearch search) throws ParameterException, BusinessException {
        return customerMapper.queryCustomer(search);
    }

    /**
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-26 16:54:12
     * @see com.ycg.ksh.service.api.CustomerService#queryByKey(java.lang.Integer)
     * <p>
     */
    @Override
    public Customer queryByKey(Integer id) throws ParameterException, BusinessException {
        Assert.notBlank(id, "客户主键为空");
        try {
            return customerMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            logger.debug("queryByKey  id:{}", id, e);
            throw BusinessException.dbException("查询客户信息异常");
        }
    }

    /**
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-26 16:54:09
     * @see com.ycg.ksh.service.api.CustomerService#save(java.lang.Integer, Customer)
     * <p>
     */
    @Override
    public void save(Integer belongType, Customer customer) throws ParameterException, BusinessException {
        if (belongType == 1) {
            Assert.notBlank(customer.getGroupId(), "项目组不能为空");
        } else {
            customer.setGroupId(0);
        }
        Assert.notBlank(customer.getProvince(), "客户地址省份不能为空");
        Assert.notBlank(customer.getCity(), "客户地址城市不能为空");
        Assert.notBlank(customer.getAddress(), "客户详细地址不能为空");
        Assert.notBlank(customer.getCustomerCode(), "客户编码不能为空");
        Assert.notBlank(customer.getCompanyName(), "客户名称不能为空");
        Assert.notBlank(customer.getContactNumber(), "客户联系人电话不能为空");
        Assert.notBlank(customer.getContacts(), "客户联系人不能为空");
        com.ycg.ksh.common.validate.Validator validator = com.ycg.ksh.common.validate.Validator.MOBILE;
        if (!validator.verify(customer.getContactNumber()) && !com.ycg.ksh.common.util.Validator.isTel(customer.getContactNumber())) {
            throw new ParameterException(validator.getMessage("联系人手机号"));
        }
        try {
            if (customer.getArrivalDay() == null) {
                customer.setArrivalDay(0);
            }
            if (customer.getArrivalHour() == null) {
                customer.setArrivalHour(0);
            }
            StringBuilder fulladdress = new StringBuilder(customer.getProvince() + customer.getCity());
            if (StringUtils.isNotBlank(customer.getDistrict())) {
                fulladdress.append(customer.getDistrict());
            }
            fulladdress.append(customer.getAddress());
            customer.setFullAddress(fulladdress.toString());
            coordinate(customer, true);
            customer.setCreatetime(new Date());
            customer.setUpdatetime(customer.getCreatetime());
            customerMapper.insertSelective(customer);
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.debug("save  customer ===>:{}", customer, e);
            throw BusinessException.dbException("保存客户信息异常");
        }
    }

    /**
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-26 16:54:05
     * @see com.ycg.ksh.service.api.CustomerService#update(Customer)
     * <p>
     */
    @Override
    public void update(Customer customer) throws ParameterException, BusinessException {
        Assert.notNull(customer.getId(), "客户主键不能为空");
        Assert.notBlank(customer.getProvince(), "客户地址省份不能为空");
        Assert.notBlank(customer.getCity(), "客户地址城市不能为空");
        Assert.notBlank(customer.getAddress(), "客户详细地址不能为空");
        Assert.notBlank(customer.getCompanyName(), "客户名称不能为空");
        Assert.notBlank(customer.getContactNumber(), "客户联系人电话不能为空");
        Assert.notBlank(customer.getContacts(), "客户联系人不能为空");
        com.ycg.ksh.common.validate.Validator validator = com.ycg.ksh.common.validate.Validator.MOBILE;
        if (!validator.verify(customer.getContactNumber())) {
            throw new ParameterException(validator.getMessage("联系人手机号"));
        }
        try {
            Customer exister = customerMapper.selectByPrimaryKey(customer.getId());
            exister.setType(customer.getType());
            exister.setGroupId(customer.getGroupId());
            exister.setProvince(customer.getProvince());
            exister.setCity(customer.getCity());
            exister.setAddress(customer.getAddress());
            exister.setCompanyName(customer.getCompanyName());
            exister.setContactNumber(customer.getContactNumber());
            exister.setContacts(customer.getContacts());
            exister.setArrivalDay(customer.getArrivalDay());
            exister.setArrivalHour(customer.getArrivalHour());
            StringBuilder fulladdress = new StringBuilder(customer.getProvince() + customer.getCity());
            if (StringUtils.isNotBlank(customer.getDistrict())) {
                fulladdress.append(customer.getDistrict());
            }
            fulladdress.append(customer.getAddress());
            exister.setFullAddress(fulladdress.toString());
            coordinate(exister, true);
            exister.setUpdatetime(new Date());
            customerMapper.updateByPrimaryKeySelective(customer);
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.debug("update customer:{}", customer, e);
            throw BusinessException.dbException("更新客户信息异常");
        }
    }

    /**
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-26 16:53:59
     * @see com.ycg.ksh.service.api.CustomerService#( Customer , FenceGrate )
     * <p>
     */
    @Override
    public Integer updateGrate(Integer customerId, Integer status) throws ParameterException, BusinessException {
        Assert.notNull(customerId, "客户编号不能为空");
        Customer exister = customerMapper.selectByPrimaryKey(customerId);
        if (exister == null) {
            throw new ParameterException(customerId, "客户信息不存在!!!");
        }
        if (StringUtils.isBlank(exister.getFullAddress())) {
            throw new BusinessException("客户地址异常不能开启电子围栏");
        }
        Customer customer = new Customer(customerId);
        if (exister.getLatitude() == null || exister.getLongitude() == null) {
            coordinate(exister, false);
            customer.setLatitude(exister.getLatitude());
            customer.setLongitude(exister.getLongitude());
            if (customer.getLatitude() == null || customer.getLongitude() == null) {
                throw new BusinessException("收货地址错误不能开启电子围栏");
            }
        }
        customerMapper.updateByPrimaryKeySelective(customer);
        return status;
    }

    private void coordinate(WaybillContext context, boolean exception) {
        String address = context.getEndStation() + context.getReceiveAddress();
        if (StringUtils.isNotBlank(address)) {
            try {
                AutoMapLocation location = autoMapService.coordinate(address);
                if (location != null) {
                    context.setLatitude(location.getLatitude());
                    context.setLongitude(location.getLongitude());
                }
            } catch (Exception e) {
                logger.error("获取客户地址经纬度异常 {} {}", context, e);
            }
        }
        if (exception) {
            if (context.getLatitude() == null || context.getLongitude() == null) {
                throw new BusinessException("经纬度获取异常,请检查收货地址!!!");
            }
        }
    }

    private Customer coordinate(Customer customer, boolean exception) {
        if (StringUtils.isNotBlank(customer.getFullAddress())) {
            try {
                AutoMapLocation location = autoMapService.coordinate(customer.getFullAddress());
                if (location != null) {
                    customer.setLatitude(new Double(location.getLatitude()));
                    customer.setLongitude(new Double(location.getLongitude()));
                }
            } catch (Exception e) {
                logger.error("获取客户地址经纬度异常 {} {}", customer, e);
            }
        }
        if (exception) {
            if (customer.getLatitude() == null || customer.getLongitude() == null) {
                throw new BusinessException("经纬度获取异常,请检查收货地址!!!");
            }
        }
        return customer;
    }

    @Override
    public void onInitializeWaybill(WaybillContext context) throws BusinessException {
        if (context.getCustomers() != null && context.getCustomers().length > 0) {
            for (int i = 0; i < context.getCustomers().length; i++) {
                Customer customer = context.getCustomers()[i];
                if (Constant.CUSTOMER_TYPE_SEND == customer.getType()) {
                    customer = saveOrModifyCustomer(context.getUserKey(), context.getGroupid(), customer, context.getSendType());
                    if (customer != null) {
                        context.setShipperAddress(customer.getAddress());
                        context.setShipperContactName(customer.getContacts());
                        context.setShipperContactTel(customer.getContactNumber());
                        context.setShipperName(customer.getCompanyName());
                        context.setShipperTel(customer.getTel());
                        context.setStartStation(RegionUtils.merge(customer.getProvince(), customer.getCity(), customer.getDistrict()));
                        context.setSimpleStartStation(RegionUtils.simple(customer.getProvince(), customer.getCity(), customer.getDistrict()));
                    }
                }
                if (Constant.CUSTOMER_TYPE_RECEIVER == customer.getType()) {
                    customer.setArrivalDay(context.getArriveDay());
                    customer.setArrivalHour(context.getArriveHour());
                    customer = saveOrModifyCustomer(context.getUserKey(), context.getGroupid(), customer, context.getReceiveType());
                    if (customer != null) {
                        context.setContactName(customer.getContacts());
                        context.setContactPhone(customer.getContactNumber());
                        context.setReceiveAddress(customer.getAddress());
                        context.setReceiverName(customer.getCompanyName());
                        context.setReceiverTel(customer.getTel());
                        context.setEndStation(RegionUtils.merge(customer.getProvince(), customer.getCity(), customer.getDistrict()));
                        context.setSimpleEndStation(RegionUtils.simple(customer.getProvince(), customer.getCity(), customer.getDistrict()));
                        context.setLatitude(customer.getLatitude() + "");
                        context.setLongitude(customer.getLongitude() + "");

                        context.setCustomerid(customer.getId());
                    }
                }
                context.getCustomers()[i] = customer;
            }
        } else {
            coordinate(context, true);
            if (null == context.getFenceRadius() || context.getFenceRadius() <= 0) {
                context.setFenceRadius(5D);
            }
            Validator validator = Validator.MOBILE;
            Assert.notNull(context.getShipperName(), "发货方名称不能为空");
            Assert.notNull(context.getShipperAddress(), "发货方地址不能为空");
            Assert.notNull(context.getShipperContactName(), "发货方联系人不能为空");
            Assert.notNull(context.getShipperContactTel(), "发货方联系人手机号不能为空");
            Assert.notNull(context.getStartStation(), "发货地区不能为空");
            context.setSimpleEndStation(RegionUtils.simple(context.getStartStation()));
            if (!validator.verify(context.getShipperContactTel())) {
                throw new ParameterException(context.getShipperContactTel(), validator.getMessage("发货方联系人手机号"));
            }
            Assert.notNull(context.getReceiverName(), "收货方名称不能为空");
            Assert.notNull(context.getReceiveAddress(), "收货方地址不能为空");
            Assert.notNull(context.getContactName(), "收货方联系人不能为空");
            Assert.notNull(context.getContactPhone(), "收货方联系人手机号不能为空");
            Assert.notNull(context.getEndStation(), "收货地区不能为空");
            if (!validator.verify(context.getContactPhone())) {
                throw new ParameterException(context.getContactPhone(), validator.getMessage("收货方联系人手机号"));
            }
            context.setSimpleEndStation(RegionUtils.simple(context.getEndStation()));
        }
    }

    public Customer saveOrModifyCustomer(Integer uKey, Integer gKey, Customer customer, int handleType) {
        String p = (Constant.CUSTOMER_TYPE_SEND == customer.getType() ? "发货" : "收货");
        if (Constant.CUSTOMER_SELECT_BYID - handleType == 0) {
            if (customer.getId() != null && customer.getId() > 0) {
                customer = customerMapper.selectByPrimaryKey(customer.getId());
                if (customer == null) {
                    throw new ParameterException(p + "信息有误");
                }
            }
        } else {
            Assert.notNull(customer.getCompanyName(), p + "方名称不能为空");
            Assert.notNull(customer.getAddress(), p + "地址不能为空");
            Assert.notNull(customer.getContacts(), p + "联系人不能为空");
            Assert.notNull(customer.getContactNumber(), p + "联系人手机号不能为空");
            String s = RegionUtils.merge(customer.getProvince(), customer.getCity(), customer.getDistrict());
            Assert.notNull(s, p + "地区不能为空");
            Validator validator = Validator.MOBILE;
            if (!validator.verify(customer.getContactNumber()) && !com.ycg.ksh.common.util.Validator.isTel(customer.getContactNumber())) {
                throw new ParameterException(customer.getContactNumber(), p + "联系人手机号格式错误");
            }
            customer.setUpdatetime(new Date());
            customer.setFullAddress(customer.getProvince() + customer.getCity() + customer.getDistrict() + customer.getAddress());
            customer = coordinate(customer, true);
            if (Constant.CUSTOMER_CLIENT_SAVE - handleType == 0) {
                if (customer.getId() == null || customer.getId() <= 0) {
                    customer.setUserid(uKey);
                    customer.setGroupId(gKey != null ? gKey : 0);
                    customer.setCreatetime(customer.getUpdatetime());
                    customerMapper.insertSelective(customer);
                } else {
                    customerMapper.updateByPrimaryKeySelective(customer);
                }
            }
        }
        return customer;
    }

    @Override
    public void delete(Customer customer) throws ParameterException, BusinessException {
        Assert.notBlank(customer.getId(), "常用地址主键为空");
        try {
            if (customer.getGroupId() != null && customer.getGroupId() > 0) {
                CustomerGroup customerGroup = new CustomerGroup();
                customerGroup.setGroupid(customer.getGroupId());
                customerGroup.setCustomerid(customer.getId());
                customerGroupMapper.delete(customerGroup);
                customerMapper.deleteByPrimaryKey(customer.getId());
            } else {
                customerMapper.deleteByPrimaryKey(customer.getId());
            }
        } catch (Exception e) {
            logger.debug("delete  customer:{}", customer, e);
            throw BusinessException.dbException("删除常用地址信息异常");
        }
    }


    /**************************************************************************************************************************/
    /***************企业客户****************************************************************************************************/
    /**************************************************************************************************************************/

    private static Long DEFAULT_VALUE = 0L;  //撤销时.企业的状态


    @Override
    public CustomPage<Customer> pageCompanyCustomer(AddressSearch search, PageScope scope) throws ParameterException, BusinessException {
        CompanyEmployee employee = companyService.getCompanyEmployee(search.getUserId());
        if (employee == null) {
            throw new BusinessException("当前用户没有所属企业");
        }
        search.setCompanyId(employee.getCompanyId());
        if (EmployeeType.convert(employee.getEmployeeType()).isCommon()) {
            search.setCustomerKeys(listCustomerKeys(employee.getEmployeeId()));
        }
        Page<Customer> page = customerMapper.queryCompanyCustomer(search, new RowBounds(scope.getPageNum(), scope.getPageSize()));
        return new CustomPage<Customer>(page.getPageNum(), page.getPageSize(), page.getTotal(), page);
    }

    /**
     * 新增客户关系
     *
     * @param uKey         操作人用户编号
     * @param customerName 要添加的客户企业名称
     * @throws ParameterException 参数错误
     * @throws BusinessException  逻辑错误
     */
    @Override
    public void saveCustomer(Integer uKey, String customerName, Integer type, String scanPhone) throws ParameterException, BusinessException {
        Assert.notBlank(customerName, "客户名称不能为空");
        Assert.notBlank(uKey, "用户编号不能为空");
        Assert.notBlank(type, "客户类型不能为空");
        Assert.notBlank(scanPhone,"客户手机号码不能为空");
        Company company = companyService.assertCompanyByUserKey(uKey);
        /*if (getCompanyCustomerByOwnerKeyAndName(company.getId(), customerName) != null){
            throw new BusinessException("客户名称已存在");
        }*/
        insert(uKey, company.getId(), customerName, type, CoreConstants.COMPANYCUSTOMER_SOURCE_CREATE, scanPhone);
    }

    @Override
    public CompanyCustomer saveCustomer(Integer uKey, Long ownerKey, Company company, Integer type, Integer sourceType) throws ParameterException, BusinessException {
        try {
            Example example = new Example(CompanyCustomer.class);
            example.createCriteria().andEqualTo("ownerKey", ownerKey).andEqualTo("companyKey", company.getId());
            List<CompanyCustomer> list = companyCustomerMapper.selectByExample(example);
            if (list == null || list.size() <= 0) {
                CompanyCustomer customer = new CompanyCustomer();
                customer.setKey(Globallys.nextKey());
                customer.setCompanyKey(company.getId());
                customer.setOwnerKey(ownerKey);
                customer.setCreateTime(new Date());
                customer.setName(company.getCompanyName());
                customer.setUserKey(uKey);
                customer.setType(type);
                customer.setSourceType(sourceType);
                customer.setStatus(CoreConstants.COMPANYCUSTOMER_STATUS_NORMAL);
                if (companyCustomerMapper.insert(customer) > 0) {
                    queueService.sendCoreMessage(new MediaMessage(Globallys.UUID(), new EmployeeCustomer(uKey, customer.getKey())));
                }
                return customer;
            } else {
                return list.get(0);
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("新增企业客户信息异常 {} {} {}", uKey, ownerKey, company, e);
            throw new BusinessException("新增企业客户信息异常");
        }
    }

    /**
     * 修改客户信息
     *
     * @param key
     * @param customerName
     * @param type
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void editCustomer(Integer uKey, Long key, String customerName, Integer type) throws ParameterException, BusinessException {
        Assert.notBlank(key, "客户编号不能为空");
        Assert.notBlank(customerName, "客户名称不能为空");
        Assert.notBlank(type, "客户类型不能为空");
        Company company = companyService.assertCompanyByUserKey(uKey);

        customerName = StringUtils.trim(customerName);
        //先查查是不是存在改客户,处于禁用状态
        CompanyCustomer searcher = new CompanyCustomer();
        searcher.setOwnerKey(company.getId());
        searcher.setName(customerName);
        CompanyCustomer customer = companyCustomerMapper.selectOne(searcher);
        if (customer != null && customer.getKey() - key != 0) {
            throw new BusinessException("客户名称已存在");
        }
        customer = new CompanyCustomer();
        customer.setKey(key);
        customer.setName(customerName);
        customer.setType(type);
        customer.setUpdateTime(new Date());
        companyCustomerMapper.updateByPrimaryKeySelective(customer);
    }

    private CompanyCustomer insert(Integer uKey, Long companyKey, String customerName, Integer customerType, Integer sourceType, String scanPhone) {
        try {
            customerType = Optional.ofNullable(customerType).orElse(CoreConstants.COMPANYCUSTOMER_TYPE_RECEIVE);
            customerName = Optional.ofNullable(customerName).map(StringUtils::trim).orElse(StringUtils.EMPTY);
            //先查查是不是存在改客户,处于禁用状态
            CompanyCustomer searcher = new CompanyCustomer();
            searcher.setOwnerKey(companyKey);
            searcher.setName(customerName);
            searcher.setUserKey(null);
            CompanyCustomer customer = companyCustomerMapper.selectOne(searcher);
            if (customer != null) {
                //如果是正常状态，提示异常
                if (customer.getStatus() - CoreConstants.COMPANYCUSTOMER_STATUS_NORMAL == 0) {
                    throw new BusinessException("客户名称已存在");
                } else {
                    //如果是不正常状态则修改正常状态
                    customer.setType(customerType);
                    customer.setSourceType(sourceType);
                    customer.setStatus(CoreConstants.COMPANYCUSTOMER_STATUS_NORMAL);
                    customer.setScanPhone(scanPhone);
                    companyCustomerMapper.updateByPrimaryKeySelective(customer);
                }
            } else {
                customer = new CompanyCustomer();
                customer.setKey(Globallys.nextKey());
                customer.setCompanyKey(DEFAULT_VALUE);
                customer.setOwnerKey(companyKey);
                customer.setCreateTime(new Date());
                customer.setName(customerName);
                customer.setUserKey(uKey);
                customer.setType(customerType);
                customer.setSourceType(sourceType);
                customer.setStatus(CoreConstants.COMPANYCUSTOMER_STATUS_NORMAL);
                customer.setScanPhone(scanPhone);
                if (companyCustomerMapper.insert(customer) > 0) {
                   /* queueService.sendCoreMessage(new MediaMessage(Globallys.UUID(), new EmployeeCustomer(uKey, customer.getKey())));*/
                }
            }
            return customer;
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("新增企业客户信息异常 {} {}", uKey, customerName, e);
            throw new BusinessException("新增企业客户信息异常");
        }
    }


    /**
     * 所属企业id 和 客户名称 查询企业客户关系
     *
     * @param ownerKey     所属企业id
     * @param customerName 客户名称
     * @return
     */
    private CompanyCustomer getCompanyCustomerByOwnerKeyAndName(Long ownerKey, String customerName) {
        CompanyCustomer customer = new CompanyCustomer();
        customer.setOwnerKey(ownerKey);
        customer.setUserKey(null);
        customer.setName(StringUtils.trim(customerName));
        customer.setStatus(CoreConstants.COMPANYCUSTOMER_STATUS_NORMAL);
        return companyCustomerMapper.selectOne(customer);
    }

    /**
     * 取消客户关系
     *
     * @param uKey        操作人用户编号
     * @param customerKey 要取消关系的编号
     * @throws ParameterException 参数错误
     * @throws BusinessException  逻辑错误
     */
    @Override
    public void cancleRelation(Integer uKey, Long customerKey) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "用户编号不能为空");
        Assert.notBlank(customerKey, "要取消关系客户编号不能为空");
        try {
            CompanyCustomer customer = new CompanyCustomer();
            customer.setKey(customerKey);
            customer.setCompanyKey(DEFAULT_VALUE);
            customer.setUpdateTime(new Date());
            companyCustomerMapper.updateByPrimaryKeySelective(customer);
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("取消客户关系异常 {} {}", uKey, customerKey, e);
            throw new BusinessException("取消客户关系异常");
        }
    }

    /**
     * 删除客户
     *
     * @param uKey         操作人用户编号
     * @param customerKeys 要删除的关系编号
     * @throws ParameterException 参数错误
     * @throws BusinessException  逻辑错误
     */
    @Override
    public void deleteCustomer(Integer uKey, Collection<Long> customerKeys) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "用户编号不能为空");
        Assert.notEmpty(customerKeys, "要取消关系客户编号不能为空");
        try {
            final Date ctime = new Date();
            Collection<CompanyCustomer> customers = companyCustomerMapper.selectByIdentities(customerKeys);
            if (CollectionUtils.isNotEmpty(customers)) {
                Collection<Long> keys = customers.stream().filter(c -> {
                    return !c.registered();//过滤调已经注册的
                }).map(CompanyCustomer::getKey).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(keys)) {
                    throw new BusinessException("已注册的信息不可以直接删除");
                }
                Example example = new Example(CompanyCustomer.class);
                example.createCriteria().andIn("key", keys);

                CompanyCustomer customer = new CompanyCustomer();
                customer.setStatus(CoreConstants.COMPANYCUSTOMER_STATUS_DELETE);
                customer.setUpdateTime(ctime);

                companyCustomerMapper.updateByExampleSelective(customer, example);
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("删除客户关系异常 {} {}", uKey, customerKeys, e);
            throw new BusinessException("删除客户关系异常");
        }
    }

    /**
     * 验证是否是客户关系
     *
     * @param ownerKey   当前企业编号
     * @param companyKey 要验证的企业编号
     * @return true:是客户关系，false:不是客户关系
     * @throws ParameterException 参数错误
     * @throws BusinessException  逻辑错误
     */
    @Override
    public boolean isCustomer(Long ownerKey, Long companyKey) throws ParameterException, BusinessException {
        Assert.notBlank(ownerKey, "当前企业编号不能为空");
        Assert.notBlank(companyKey, "要验证的企业编号不能为空");
        try {
            //根据 ownerKey 和 companyKey 查询记录，有记录则为true,反之为false --status是代表是属于启用状态
            CompanyCustomer customer = new CompanyCustomer();
            customer.setOwnerKey(ownerKey);
            customer.setCompanyKey(companyKey);
            customer.setStatus(CoreConstants.COMPANYCUSTOMER_STATUS_NORMAL);
            return companyCustomerMapper.selectCount(customer) > 0;
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("验证是否是客户关系异常 {} {}", ownerKey, companyKey, e);
            throw new BusinessException("验证是否是客户关系异常");
        }
    }

    /**
     * 根据客户名称查询客户企业信息
     *
     * @param ownerKey     当前企业编号
     * @param customerName 客户名称
     * @return 企业信息
     * @throws ParameterException 参数错误
     * @throws BusinessException  逻辑错误
     */
    @Override
    public Company loadCompanyByCustomer(Long ownerKey, String customerName) throws ParameterException, BusinessException {
        Assert.notBlank(ownerKey, "当前企业编号不能为空");
        Assert.notBlank(customerName, "客户名称不能为空");
        try {
            CompanyCustomer customer = getCompanyCustomerByOwnerKeyAndName(ownerKey, customerName);
            if (customer != null) {
                return companyService.getCompanyByKey(customer.getCompanyKey());
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("根据客户名称查询客户企业信息异常 {} {}", ownerKey, customerName, e);
            throw new BusinessException("查询客户企业信息异常");
        }
        return null;
    }

    /**
     * 订单分享
     *
     * @param company  发起分享的企业
     * @param receives 被分享的企业
     * @param orders   被分享的订单信息
     * @throws MessageException
     */
    @Override
    public void onShareOrders(final Company company, final Collection<Company> receives, final Collection<Order> orders) throws MessageException {
        if (CollectionUtils.isNotEmpty(receives)) {
            Globallys.executor(() -> {
                for (Company receive : receives) {
                    CompanyCustomer customer = new CompanyCustomer(receive.getId(), company.getCompanyName());
                    customer.setCompanyKey(company.getId());
                    customer.setType(CoreConstants.COMPANYCUSTOMER_TYPE_CONVEY);
                    customer.setSourceType(CoreConstants.COMPANYCUSTOMER_SOURCE_SHARE);
                    queueService.sendCoreMessage(new MediaMessage(Globallys.UUID(), customer));
                }
            });
        }
    }

    /**
     * 加载客户信息
     *
     * @param ownerKey     所属企业编号
     * @param customerName 客户名称
     * @param insert       true:没有找到时新增，false:没有找到部处理
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public CompanyCustomer loadCustomer(Integer uKey, Long ownerKey, String customerName, Integer customerType, Integer sourceType, boolean insert) throws ParameterException, BusinessException {
        if (StringUtils.isNotBlank(customerName)) {
            CompanyCustomer customer = getCompanyCustomerByOwnerKeyAndName(ownerKey, customerName);
            if (customer != null) {
                return customer;
            }
            if (insert) {
                return insert(uKey, ownerKey, customerName, customerType, sourceType, null);
            }
        }
        return null;
    }

    /**
     * 获取客户信息
     *
     * @param customerKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public CustomerConcise loadCustomerConcise(Long customerKey) throws ParameterException, BusinessException {
        return companyCustomerMapper.loadCustomerConcise(customerKey);
    }

    @Override
    public CustomerConcise loadCustomerConcise(Long key, boolean reg) throws ParameterException, BusinessException {
        if (reg) {
            Company company = companyService.getCompanyByKey(key);
            if (company != null) {
                return new CustomerConcise(company.getId(), company.getCompanyName(), company.getId(), company.getCompanyName());
            }
        } else {
            return loadCustomerConcise(key);
        }
        return null;
    }

    @Override
    public Collection<CustomerAlliance> searchCustomerBySomething(CustomerSearch search) throws ParameterException, BusinessException {
        Assert.notBlank(search.getUserKey(), "操作用户编号不能为空");
        CompanyEmployee employee = companyService.getCompanyEmployee(search.getUserKey());
        if (employee == null) {
            throw new BusinessException("当前用户没有所属企业");
        }
        search.setOwnerKey(employee.getCompanyId());
        /*if (EmployeeType.convert(employee.getEmployeeType()).isCommon()) {
            search.setCustomerKeys(listCustomerKeys(employee.getEmployeeId()));
        }*/
        Collection<CompanyCustomer> customers = null;
        if (search.getUserHost() != null && search.getUserHost()) {
            customers = companyCustomerMapper.selectBySomething(search);
        } else {
            customers = companyCustomerMapper.selectByExample(buildCustomExample(search));
        }
        return alliance(search.getUserKey(), customers);
    }

    /**
     * @param search
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Collection<CustomerAddress> searchAddressBySomething(AddressSearch search) throws ParameterException, BusinessException {
        Assert.notBlank(search.getUserId(), "操作用户编号不能为空");
        if (search.getCompanyId() == null || search.getCompanyId() <= 0) {
            Company company = companyService.assertCompanyByUserKey(search.getUserId());
            search.setCompanyId(company.getId());
        }
        return companyCustomerMapper.selectCustomerAddressBySomething(search);
    }

    private CustomExample buildCustomExample(CustomerSearch search) {
        CustomExample example = new CustomExample(CompanyCustomer.class);
        CustomExample.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ownerKey", search.getOwnerKey());
        if (search.getStatus() != null && search.getStatus() >= 0) {
            criteria.andEqualTo("status", search.getStatus());
        }
        if (search.getType() != null && search.getType() >= 0) {
            criteria.andEqualTo("type", search.getType());
        }
        search.setLikeName(StringUtils.trim(search.getLikeName()));
        if (StringUtils.isNotBlank(search.getLikeName())) {
            criteria.andLike("name", search.getLikeName());
        }

        if (null != search.getReg()) {
            if (search.getReg()) {
                criteria.andGreaterThan("companyKey", 0);
            } else {
                example.and().andIsNull("companyKey").orLessThanOrEqualTo("companyKey", 0);
            }
        }
        if (CollectionUtils.isNotEmpty(search.getCustomerKeys())) {
            criteria.andIn("key", search.getCustomerKeys());
        }
        example.orderBy("createTime").desc();
        return example;
    }

    /**
     * 分页查询客户关系
     *
     * @param search 查询条件
     * @param scope  分页信息
     * @return 满足条件的企业客户信息
     * @throws ParameterException 参数错误
     * @throws BusinessException  逻辑错误
     */
    @Override
    public CustomPage<CustomerAlliance> pageCustomers(CustomerSearch search, PageScope scope) throws ParameterException, BusinessException {
        Assert.notBlank(search.getUserKey(), "操作用户编号不能为空");
        try {
            scope = Optional.ofNullable(scope).orElse(PageScope.DEFAULT);

            if (search.getOwnerKey() == null || search.getOwnerKey() <= 0) {
                Company company = companyService.assertCompanyByUserKey(search.getUserKey());
                search.setOwnerKey(company.getId());
            }
            Page<CompanyCustomer> page = null;
            if (search.getUserHost() != null && search.getUserHost()) {
                page = companyCustomerMapper.selectBySomething(search, new RowBounds(scope.getPageNum(), scope.getPageSize()));
            } else {
                page = companyCustomerMapper.selectPageByExample(buildCustomExample(search), new RowBounds(scope.getPageNum(), scope.getPageSize()));
            }
            return new CustomPage<CustomerAlliance>(page.getPageNum(), page.getPageSize(), page.getTotal(), alliance(search.getUserKey(), page));
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("分页查询客户关系信息异常 {} {}", search, scope, e);
            throw new BusinessException("查询客户关系信息异常");
        }
    }

    private Collection<CustomerAlliance> alliance(Integer uKey, Collection<? extends CompanyCustomer> results) throws
            BusinessException {
        Collection<CustomerAlliance> collection = new ArrayList<CustomerAlliance>(results.size());
        for (CompanyCustomer customer : results) {
            collection.add(alliance(uKey, customer));
        }
        return collection;
    }

    private CustomerAlliance alliance(Integer uKey, CompanyCustomer customer) throws BusinessException {
        try {
            CustomerAlliance alliance = new CustomerAlliance(customer);
            if (alliance.registered()) {
                Company company = companyService.getCompanyByKey(alliance.getCompanyKey());
                if (company != null) {
                    alliance.setCompany(new CompanyConcise(company.getId(), company.getCompanyName()));
                    User user = userService.getUserByKey(company.getUserId());//companyService.getCompanyEmployeeByCompanyKey(company.getId());
                    if (user != null) {
                        alliance.setRegName(user.getUnamezn());
                        alliance.setRegMobile(UserUtil.encodeMobile(user.getMobilephone()));
                    }
                }
            }
            return alliance;
        } catch (Exception e) {
            throw new BusinessException("获取客户关系信息异常", e);
        }
    }

    /**
     * 根据员工编号查询员工有权限的客户编号
     *
     * @param employeeKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Collection<Long> listCustomerAuthoritys(Integer employeeKey) throws ParameterException, BusinessException {
        Assert.notBlank(employeeKey, "员工编号不能为空");
        try {
            CompanyEmployee employee = companyService.getCompanyEmployee(employeeKey);
            if (employee == null) {
                throw new BusinessException("当前用户没有所属企业");
            }
            //管理员查询所有正常的客户信息
            if (EmployeeType.convert(employee.getEmployeeType()).isManage()) {
                CompanyCustomer customer = new CompanyCustomer();
                customer.setOwnerKey(employee.getCompanyId());
                customer.setStatus(CoreConstants.COMPANYCUSTOMER_STATUS_NORMAL);
                customer.setUserKey(null);
                Collection<CompanyCustomer> customers = companyCustomerMapper.select(customer);
                return Optional.ofNullable(customers).map(cs -> {
                    return cs.stream().map(CompanyCustomer::getKey).collect(Collectors.toList());
                }).orElse(Collections.emptyList());
            } else {//非管理员，按权限查询
                return listCustomerKeys(employeeKey);
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("查询员工客户权限异常", e);
        }
    }

    private Collection<Long> listCustomerKeys(Integer employeeKey) throws ParameterException, BusinessException {
        Assert.notBlank(employeeKey, "员工编号不能为空");
        try {
            EmployeeCustomer customer = new EmployeeCustomer();
            customer.setEmployeeKey(employeeKey);
            Collection<EmployeeCustomer> customers = employeeCustomerMapper.select(customer);
            return Optional.ofNullable(customers).map(cs -> {
                return cs.stream().map(EmployeeCustomer::getCustomerKey).collect(Collectors.toList());
            }).orElse(Collections.emptyList());
        } catch (Exception e) {
            throw new BusinessException("查询员工客户权限异常", e);
        }
    }


    /**
     * 更新员工的客户权限
     *
     * @param employeeKey
     * @param customerKeys
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void modifyCustomerAuthoritys(Integer employeeKey, Collection<Long> customerKeys) throws ParameterException, BusinessException {
        Assert.notBlank(employeeKey, "员工编号不能为空");
        try {
            EmployeeCustomer customer = new EmployeeCustomer();
            customer.setEmployeeKey(employeeKey);
            employeeCustomerMapper.delete(customer);
            Collection<EmployeeCustomer> collection = Optional.ofNullable(customerKeys).map(cc -> {
                return cc.stream().map(k -> new EmployeeCustomer(Globallys.nextKey(), employeeKey, k)).collect(Collectors.toList());
            }).orElse(Collections.emptyList());
            if (CollectionUtils.isNotEmpty(collection)) {
                employeeCustomerMapper.inserts(collection);
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("更新员工的客户权限异常", e);
        }
    }

    public void associateCompany(Long customerKey, Long companyKey) throws ParameterException, BusinessException {
        try {
            CompanyCustomer customer = companyCustomerMapper.selectByPrimaryKey(customerKey);
            if (customer != null && customer.registered()) {
                throw new BusinessException("已经注册过");
            }
            CompanyCustomer updater = new CompanyCustomer();
            updater.setCompanyKey(companyKey);
            updater.setKey(customerKey);
            if (companyCustomerMapper.updateByPrimaryKeySelective(updater) > 0) {
                if (CollectionUtils.isNotEmpty(observerAdapters)) {
                    for (CustomeObserverAdapter observerAdapter : observerAdapters) {
                        observerAdapter.associateCustomerCompany(customerKey, companyKey);
                    }
                }
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("关联客户异常", e);
        }
    }

    /**
     * @param customerKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public CompanyCustomer getCompanyCustomer(Long customerKey) throws ParameterException, BusinessException {
        CompanyCustomer customer = companyCustomerMapper.selectByPrimaryKey(customerKey);
        if (customer != null && Optional.ofNullable(customer.getStatus()).orElse(0) > 0) //后面判断是否处于禁用状态
            return customer;
        return null;

    }

    @Override
    public CustomerAlliance allianceCompanyCustomer(Integer uKey, Long customerKey) throws ParameterException, BusinessException {
        CompanyCustomer customer = getCompanyCustomer(customerKey);
        if (customer != null) {
            return alliance(uKey, customer);
        }
        return null;
    }


    @Override
    public Collection<Long> listKeyLikeName(Long companyKey, String likeName) throws ParameterException, BusinessException {
        try {
            return companyCustomerMapper.listKeyByCompanyKey(companyKey, likeName);
        } catch (Exception e) {
            logger.error("根据客户名称模糊查询编号 {} {}", companyKey, likeName, e);
        }
        return Collections.emptyList();
    }

    /**
     * @param userKey
     * @param likeName
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Collection<Long> listKeyLikeName(Integer userKey, String likeName) throws ParameterException, BusinessException {
        try {
            CompanyEmployee employee = companyService.getCompanyEmployee(userKey);
            if (EmployeeType.convert(employee.getEmployeeType()).isManage()) {
                return companyCustomerMapper.listKeyByCompanyKey(employee.getCompanyId(), likeName);
            } else {
                return companyCustomerMapper.listKeyByName(userKey, likeName);
            }
        } catch (Exception e) {
            logger.error("根据客户名称模糊查询编号 {} {}", userKey, likeName, e);
        }
        return Collections.emptyList();
    }

    /**
     * 通知企业注册
     *
     * @param uKey        操作用户编号
     * @param companyKey  注册企业编号
     * @param customerKey 客户编号
     */
    @Override
    public void notifyCompanyRegister(Integer uKey, Long companyKey, Long customerKey) throws BusinessException {
        if (companyKey != null && companyKey > 0 && customerKey != null && customerKey > 0) {
            associateCompany(customerKey, companyKey);
        }
    }

    @Override
    public void saveAddress(Integer uKey, Customer customer) throws ParameterException, BusinessException {
        Assert.notNull(customer, "收货信息不能为空");
        Assert.notZero(customer.getCompanyCustomerId(), "客户编号不能为空");
        CompanyCustomer companyCustomer = getCompanyCustomer(customer.getCompanyCustomerId());
        if (companyCustomer == null) {
            throw new ParameterException(customer.getCompanyCustomerId(), "指定的客户信息不存在");
        }
        Assert.notNull(customer.getContacts(), "收货人名称不能为空");
        Assert.notNull(customer.getContactNumber(), "收货人联系方式不能为空");
        Assert.notNull(customer.getProvince(), "收货人省份不能为空");
        Assert.notNull(customer.getCity(), "收货人市不能为空");
        //Assert.notNull(customer.getDistrict(),"收货人区不能为空");
        Assert.notNull(customer.getAddress(), "收货人详细地址不能为空");
        try {
            customer.setUserid(uKey);       //所属用户-录入人
            customer.setCreatetime(new Date());
            StringBuilder sb = new StringBuilder("");
            sb.append(customer.getProvince()).append(customer.getCity());
            if (StringUtils.isNotBlank(customer.getDistrict())) {
                sb.append(customer.getDistrict());
            }
            sb.append(customer.getAddress());
            customer.setFullAddress(sb.toString());
            customer.setType(1);        //收货地址
            customerMapper.insert(customer);
        } catch (Exception e) {
            throw new BusinessException("新增客户地址异常", e);
        }
    }

    @Override
    public void editAddress(Integer uKey, Customer customer) throws ParameterException, BusinessException {
        Assert.notNull(customer, "收货信息不能为空");
        Assert.notNull(customer.getId(), "请选择收货的id");

        Assert.notNull(customer.getContacts(), "收货人名称不能为空");
        Assert.notNull(customer.getContactNumber(), "收货人联系方式不能为空");
        Assert.notNull(customer.getProvince(), "收货人省份不能为空");
        Assert.notNull(customer.getCity(), "收货人市不能为空");
        //Assert.notNull(customer.getDistrict(),"收货人区不能为空");
        Assert.notNull(customer.getAddress(), "收货人详细地址不能为空");

        try {
            customer.setUpdatetime(new Date());
            StringBuilder sb = new StringBuilder("");
            sb.append(customer.getProvince()).append(customer.getCity());
            if (StringUtils.isNotBlank(customer.getDistrict())) {
                sb.append(customer.getDistrict());
            }
            sb.append(customer.getAddress());
            customer.setFullAddress(sb.toString());
            customerMapper.updateByPrimaryKeySelective(customer);
        } catch (Exception e) {
            throw new BusinessException("修改客户地址异常", e);
        }
    }

    @Override
    public void deleteAddress(Integer uKey, Collection<Integer> ids) throws ParameterException, BusinessException {
        Assert.notEmpty(ids, "删除项不能为空");
        if (CollectionUtils.isNotEmpty(ids)) {
            try {
                Example example = new Example(Customer.class);
                example.createCriteria().andIn("id", ids);
                customerMapper.deleteByExample(example);
            } catch (Exception e) {
                throw new BusinessException("删除客户地址异常", e);
            }
        }
    }

    @Override
    public Customer getAddressByCompanyName(Integer uKey, String companyName) throws ParameterException, BusinessException {
        Assert.notBlank(companyName, "客户名称不能为空");
        Example example = new Example(Customer.class);
        example.createCriteria().andEqualTo("companyName", companyName);
        List<Customer> customers = customerMapper.selectByExample(example);
        //此处是运单录入的时候通过录入客户名称来带入其他的信息,但是如果该客户名称对应的是多个地址.则不返回.手填
        if (customers == null || customers.isEmpty()) {
            return null;
        }
        if (customers.size() == 1) {
            return customers.get(0);
        }
        throw new BusinessException("该客户名称下有多个地址.请手动输入");
    }

    /**
     * 根据客户编号查询最新的地址
     *
     * @param customerKey@return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Customer lastAddressByCustomerKey(Long customerKey) throws ParameterException, BusinessException {
        Assert.notBlank(customerKey, "客户编号不能为空");
        Example example = new Example(Customer.class);
        example.createCriteria().andEqualTo("companyCustomerId", customerKey);
        example.orderBy("createtime").desc();
        List<Customer> customers = customerMapper.selectByExample(example);
        //此处是运单录入的时候通过录入客户名称来带入其他的信息,但是如果该客户名称对应的是多个地址.则不返回.手填
        if (CollectionUtils.isNotEmpty(customers)) {
            return customers.get(0);
        }
        return null;
    }

    private CustomerConcise loadCustomerConcise(Integer uKey, Long companyKey, Long customerKey, String customerName, PartnerType partner) {
        CustomerConcise customerConcise = null;
        if (customerKey != null && customerKey > 0) {
            customerConcise = loadCustomerConcise(customerKey);
        }
        if (StringUtils.isNotBlank(customerName) && (customerConcise == null || StringUtils.equals(customerConcise.getCustomerName(), customerName))) {
            CompanyCustomer customer = loadCustomer(uKey, companyKey, customerName, partner.getCode(), CoreConstants.COMPANYCUSTOMER_SOURCE_CREATE, true);
            if (customer.registered()) {
                Company company = companyService.getCompanyByKey(customer.getCompanyKey());
                customerConcise = new CustomerConcise(customer.getKey(), customer.getName(), company.getId(), company.getCompanyName());
            } else {
                customerConcise = new CustomerConcise(customer.getKey(), customer.getName());
            }
        }
        return customerConcise;
    }

    @Override
    public void allianceOrder(Integer uKey, OrderAlliance alliance, Integer... flags) throws BusinessException {
        if (Arrays.binarySearch(flags, O.enterprise) >= 0) {
            for (PartnerType partnerType : PartnerType.values()) {
                if (partnerType.registered(alliance.getClientType())) {
                    continue;
                }
                if (partnerType.shipper()) {
                    alliance.setShipper(loadCustomerConcise(alliance.getShipperId()));
                } else if (partnerType.convey()) {
                    alliance.setConvey(loadCustomerConcise(alliance.getConveyId()));
                } else {
                    alliance.setReceive(loadCustomerConcise(alliance.getReceiveId()));
                }
            }
        }
    }

    @Override
    public void initializeOrder(Integer uKey, PartnerType partnerType, Long companyKey, OrderTemplate template, boolean insert) throws MessageException {
        if (insert) {
            //处理发货方
            template.setShipper(loadCustomerConcise(uKey, companyKey, template.getShipperKey(), template.getShipperName(), PartnerType.SHIPPER));
            //处理收货方
            template.setReceive(loadCustomerConcise(uKey, companyKey, template.getReceiveKey(), template.getReceiveName(), PartnerType.RECEIVE));
            //处理承运方
            template.setConvey(loadCustomerConcise(uKey, companyKey, template.getConveyKey(), template.getConveyName(), PartnerType.CONVEY));

            if (Stream.of(template.getShipper(), template.getReceive(), template.getConvey()).filter(Objects::isNull).count() > 1) {
                throw new MessageException("发货方/收货方/承运方不能同时有两项为空");
            }
        }
    }

    @Override
    public void initializeTemplate(Integer uKey, PartnerType partnerType, Long companyKey, PlanTemplate template, boolean insert) throws MessageException {
        //处理发货方
        if (!partnerType.shipper()) {
            template.setShipper(loadCustomerConcise(uKey, companyKey, template.getShipperKey(), template.getShipperName(), PartnerType.SHIPPER));
        }
        //处理收货方
        template.setReceive(loadCustomerConcise(uKey, companyKey, template.getReceiveKey(), template.getReceiveName(), PartnerType.RECEIVE));

        //处理承运方
        if (!insert && (template.getConveyKey() != null && template.getConveyKey() > 0)) {
            CustomerConcise concise = loadCustomerConcise(template.getConveyKey());
            if (concise == null || !concise.isEnterprise()) {
                throw new MessageException("当前承运商未注册合同物流管理平台平台不能分配");
            }
        } else {
            template.setConvey(loadCustomerConcise(uKey, companyKey, template.getConveyKey(), template.getConveyName(), PartnerType.CONVEY));
        }
        if (template.getReceive() == null) {
            throw new MessageException("收货客户不能为空");
        }
    }

    @Override
    public void allianceOrder(Integer uKey, PlanAlliance alliance, Integer... flags) throws BusinessException {
        if (Arrays.binarySearch(flags, P.enterprise) >= 0) {
            if (alliance.getShipperId() != null && alliance.getShipperId() > 0) {
                alliance.setShipper(loadCustomerConcise(alliance.getShipperId()));
            }
            alliance.setReceive(loadCustomerConcise(alliance.getReceiveId()));
        }
    }

    @Override
    public boolean handleMessage(String messageType, String messageKey, Object object) throws BusinessException {
        if (StringUtils.equalsIgnoreCase(messageType, CompanyCustomer.class.getName())) {
            try {
                CompanyCustomer customer = (CompanyCustomer) object;
                if(StringUtils.isBlank(customer.getName()) && customer.getCompanyKey() != null && customer.getCompanyKey() > 0){
                    Company company = companyService.getCompanyByKey(customer.getCompanyKey());
                    if(company != null){
                        customer.setName(company.getCompanyName());
                    }
                }
                CompanyCustomer exister = companyCustomerMapper.getByOwnerKeyAndName(customer.getOwnerKey(), customer.getName());
                customer.setUpdateTime(new Date());
                customer.setStatus(CoreConstants.COMPANYCUSTOMER_STATUS_NORMAL);
                if (exister != null) {
                    customer.setKey(exister.getKey());
                    companyCustomerMapper.updateByPrimaryKeySelective(customer);
                } else {
                    if(customer.getKey() == null || customer.getKey() <= 0){
                        customer.setKey(Globallys.nextKey());
                    }
                    customer.setCreateTime(customer.getUpdateTime());
                    companyCustomerMapper.insertSelective(customer);
                }
                return true;
            } catch (Exception ex) {
                throw new BusinessException("处理企业客户消息异常", ex);
            }
        }
        return false;
    }

    public FileEntity buildQrcode(Integer uKey, Long customerKey, String url) {
        //http://xxx.xxx.xx/mobile/wechat/company/associate/%d/%d
        try {
            CompanyCustomer customer = getCompanyCustomer(customerKey);
            if (customer == null) {
                throw new BusinessException("客户信息加载异常");
            }
            FileEntity fileEntity = new FileEntity(SystemUtils.fileRootPath(), Directory.DOWN, String.valueOf(customerKey), "png");

            fileEntity.setAliasName(customer.getName());

            Long expire = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1L);
            String qrcodeContext = String.format(url, customerKey, expire);
            byte[] data = new QRCode(400, fileEntity.getSuffix()).bytes(qrcodeContext);

            BufferedImage rectangle = rectangle(customer.getName(), DateUtils.formatTime(expire) + " 过期", data);
            ImageIO.write(rectangle, "PNG", FileUtils.file(fileEntity.getPath()));

            return fileEntity;
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception ex) {
            throw new BusinessException("生成二维码图片异常");
        }
    }


    public static BufferedImage rectangle(String customerKey, String expired, byte[] bytes) throws IOException {
        int w = 400, h = 500, p = 10, m = 20;
        //创建缓存
        BufferedImage buffered = new BufferedImage(w + 2 * p, h + 2 * p, BufferedImage.TYPE_INT_RGB);
        //获得画布
        Graphics2D gs = buffered.createGraphics();
        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gs.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);

        gs.setBackground(Color.WHITE);//设置背景色
        //g2.setBackground(Color.WHITE);//设置背景色
        gs.clearRect(0, 0, w + 2 * p, h + 2 * p);//通过使用当前绘图表面的背景色进行填充来清除指定的矩形。

        gs.setColor(Color.BLACK);

        //设置画笔宽度
        gs.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
        gs.setFont(new Font("楷体", Font.BOLD, 20));
        Font font = gs.getFont();
        Rectangle2D bounds = font.getStringBounds(customerKey, gs.getFontRenderContext());
        String[] chars = customerKey.split("");
        // 字符宽度＝字符串长度/字符数
        double char_interval = (bounds.getWidth() / chars.length) - 1;
        double y = 20 + p + bounds.getHeight() / 2;
        double x = p + (w - bounds.getWidth()) / 2;

        gs.drawString(customerKey, (float) x, (float) y);

        gs.setFont(new Font("楷体", Font.PLAIN, 15));
        font = gs.getFont();
        bounds = font.getStringBounds(expired, gs.getFontRenderContext());
        chars = expired.split("");
        // 字符宽度＝字符串长度/字符数
        char_interval = (bounds.getWidth() / chars.length) - 1;
        y = y + 20 + bounds.getHeight() / 2;
        x = p + (w - bounds.getWidth()) / 2;

        gs.drawString(expired, (float) x, (float) y);


        y = y + 20;
        x = p;

        //Image image = Toolkit.getDefaultToolkit().createImage(bytes, 0, bytes.length);
        BufferedImage bi = ImageIO.read(new ByteArrayInputStream(bytes));

        gs.drawImage(bi, (int) x, (int) y, null);

        gs.dispose();
        return buffered;
    }


}
