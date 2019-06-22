package com.ycg.ksh.core.contract.application.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.entity.Page;
import com.ycg.ksh.common.excel.EasyExcelBuilder;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.util.*;
import com.ycg.ksh.core.Registrys;
import com.ycg.ksh.core.common.domain.Region;
import com.ycg.ksh.core.contract.application.ContractApplicationService;
import com.ycg.ksh.core.contract.application.dto.*;
import com.ycg.ksh.core.contract.application.transform.CommodityTransformer;
import com.ycg.ksh.core.contract.application.transform.ContractDtoTransformer;
import com.ycg.ksh.core.contract.application.transform.IncomeDtoTransformer;
import com.ycg.ksh.core.contract.domain.model.*;
import com.ycg.ksh.core.contract.search.dto.CommodityConfigDtoJdbc;
import com.ycg.ksh.core.contract.search.dto.ContractDtoJdbc;
import com.ycg.ksh.core.contract.search.dto.IncomeRecordDtoJdbc;
import com.ycg.ksh.entity.persistent.Customer;
import com.ycg.ksh.entity.persistent.enterprise.OrderExtra;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.CompanyConcise;
import com.ycg.ksh.entity.service.enterprise.CustomerConcise;
import com.ycg.ksh.entity.service.enterprise.OrderAlliance;
import com.ycg.ksh.service.api.CustomerService;
import com.ycg.ksh.service.api.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * 合同计价应用层
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2019/01/03 0003
 */
@Service("ksh.core.contract.contractApplicationService")
public class ContractApplicationServiceImpl implements ContractApplicationService {

    ContractDtoTransformer transformer;

    IncomeDtoTransformer incomeDtoTransformer;

    CommodityTransformer commodityTransformer;


    public ContractApplicationServiceImpl() {
        this.transformer = new ContractDtoTransformer();
        this.incomeDtoTransformer = new IncomeDtoTransformer();
        this.commodityTransformer = new CommodityTransformer();
    }

    @Resource
    OrderService orderService;

    @Resource
    CustomerService customerService;

    /**
     * 查询合同信息
     *
     * @param contractKey 合同编号
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    @Transactional(readOnly = true)
    public ContractDetailDto getContract(Long contractKey) throws ParameterException, BusinessException {
        try {
            Contract contract = Registrys.contractRepository().findByKey(contractKey);
            if (contract != null) {
                return transformer.dtoDetailFromContract(contract);
            } else {
                throw new ParameterException("没有找到指定[" + contractKey + "]合同信息");
            }
        } catch (ParameterException | BusinessException e) {
            logger.error("get->: {}", contractKey, e);
            throw e;
        } catch (Exception e) {
            logger.error("get->: {}", contractKey, e);
            throw new BusinessException("查询合同详情异常", e);
        }
    }

    public void checkShippingAddress(Integer userKey, ContractDto dto) {
        Customer customer = customerService.lastAddressByCustomerKey(dto.getCustomerCode());
        if (null == customer || customer.getId() <= 0) {
            customer = new Customer();
            customer.setCompanyCustomerId(dto.getCustomerCode());
            customer.setCreatetime(new Date());
            customer.setCity(dto.getCustomerCity());
            customer.setProvince(dto.getCustomerProvince());
            customer.setDistrict(dto.getCustomerDistrict());
            customer.setAddress(dto.getCustomerAddress());
            StringBuilder sb = new StringBuilder("");
            sb.append(dto.getCustomerProvince()).append(dto.getCustomerCity());
            if (StringUtils.isNotBlank(customer.getDistrict())) {
                sb.append(dto.getCustomerDistrict());
            }
            sb.append(dto.getCustomerAddress());
            customer.setContacts(dto.getContactName());
            customer.setContactNumber(dto.getMobileNumber());
            customer.setFullAddress(sb.toString());
            customer.setType(2);        //发货地址
            customerService.saveAddress(userKey, customer);
        }
    }


    @Override
    @Transactional
    public void save(CompanyConcise company, Integer userKey, ContractDto dto, Collection<ValuationDto> valuationDtos) throws ParameterException, BusinessException {
        try {
            checkShippingAddress(userKey, dto);
            ValidityPeriod validityPeriod = new ValidityPeriod(dto.getContractStartDate(), dto.getContractEndDate());
            Contract contract = new Contract(dto.getContractNo(), company.getId(), company.getCompanyName(), dto.getContractType(), validityPeriod);
            Region customerRegion = new Region(dto.getCustomerProvince(), dto.getCustomerCity(), dto.getCustomerDistrict());
            CustomerConcise customerConcise = customerService.loadCustomerConcise(dto.getCustomerCode());
            if (customerConcise == null && customerConcise.getId() <= 0) {
                throw new BusinessException("当前" + dto.getCustomerName() + "客户没有关联企业");
            }
            contract.modifyCustomer(customerConcise.getCompanyKey(), dto.getCustomerCode(), dto.getCustomerName(), dto.getIndustryType(), dto.getContactName(), dto.getTelephoneNumber(), dto.getMobileNumber(), customerRegion, dto.getCustomerAddress());
            contract.modifyValuation(transformer.transformFragmentaryValuation(valuationDtos, new RegionDto(dto.getCustomerProvince(), dto.getCustomerCity(), dto.getCustomerDistrict())));
            contract.save();
        } catch (ParameterException | BusinessException e) {
            logger.error("save->: {}, : {}", company, dto, e);
            throw e;
        } catch (Exception e) {
            logger.error("save->: {}, : {}", company, dto, e);
            throw new BusinessException("合同保存异常", e);
        }
    }

    @Override
    @Transactional
    public void modify(CompanyConcise company, Integer userKey, ContractDto dto, Collection<ValuationDto> valuationDtos) throws ParameterException, BusinessException {
        try {
            Contract contract = Registrys.contractRepository().findByKey(dto.getId());
            if (contract != null) {
                checkShippingAddress(userKey, dto);
                ValidityPeriod validityPeriod = new ValidityPeriod(dto.getContractStartDate(), dto.getContractEndDate());
                contract.modify(dto.getContractNo(), company.getId(), company.getCompanyName(), dto.getContractType(), validityPeriod);

                Region customerRegion = new Region(dto.getCustomerProvince(), dto.getCustomerCity(), dto.getCustomerDistrict());
                CustomerConcise customerConcise = customerService.loadCustomerConcise(dto.getCustomerCode());
                if (customerConcise == null && customerConcise.getId() <= 0) {
                    throw new BusinessException("当前" + dto.getCustomerName() + "客户没有关联企业");
                }
                contract.modifyCustomer(customerConcise.getCompanyKey(), dto.getCustomerCode(), dto.getCustomerName(), dto.getIndustryType(), dto.getContactName(), dto.getTelephoneNumber(), dto.getMobileNumber(), customerRegion, dto.getCustomerAddress());
                contract.modifyValuation(transformer.transformFragmentaryValuation(valuationDtos, new RegionDto(dto.getCustomerProvince(), dto.getCustomerCity(), dto.getCustomerDistrict())));
                contract.update();
            } else {
                throw new ParameterException("没有找到指定[" + dto.getId() + "]合同信息");
            }
        } catch (ParameterException | BusinessException e) {
            logger.error("update->: {}, : {}", company, dto, e);
            throw e;
        } catch (Exception e) {
            logger.error("update->: {}, : {}", company, dto, e);
            throw new BusinessException("合同更新异常", e);
        }
    }

    @Override
    public void verify(Integer userKey, Long contractKey, ContractVerifyDto verifyDto) throws ParameterException, BusinessException {
        try {
            Contract contract = Registrys.contractRepository().findByKey(contractKey);
            if (contract != null) {
                contract.verify(verifyDto.getVerifyStatus(), verifyDto.getReason(), userKey);
            } else {
                throw new ParameterException("没有找到指定[" + contractKey + "]合同信息");
            }
        } catch (ParameterException | BusinessException e) {
            logger.error("verify->: {}, : {}, : {}", userKey, contractKey, verifyDto, e);
            throw e;
        } catch (Exception e) {
            logger.error("verify->: {}, : {}, : {}", userKey, contractKey, verifyDto, e);
            throw new BusinessException("合同审核异常", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContractDto> searchContract(CompanyConcise companyConcise, Integer contractType, String likeString, PageScope scope) throws ParameterException, BusinessException {
        Page<ContractDtoJdbc> page = Registrys.contractQueryRepository().pricingSearch(companyConcise,
                contractType, likeString, scope.getPageNum(), scope.getPageSize());
        return new Page<ContractDto>(page.getNum(), page.getSize(), page.getTotal(), transformer.tranContractDtoJdbcList(page.getResults()));
    }

    @Override
    @Transactional
    public void singleReceivable(Integer userKey, CompanyConcise companyConcise, Collection<IncomeRecordDto> dtos) throws ParameterException, BusinessException {
        Assert.notEmpty(dtos, "请至少选择一条订单");
        Collection<OrderExtra> orderExtras = new ArrayList<>();
        Collection<Long> orderKeys = dtos.stream().map(IncomeRecordDto::getSystemId).collect(Collectors.toList());
        dtos.forEach(o -> {
            OrderAlliance orderAlliance = orderService.getOrderAlliance(userKey, o.getSystemId(), true);
            if (Registrys.incomeRepository().existIncome(o.getSystemId())) {
                new IncomeRecord().modify(incomeDtoTransformer.IncomeRecordDtoByOrder(o, orderAlliance, companyConcise.getId()));
            } else {
                new IncomeRecord().save(incomeDtoTransformer.IncomeRecordDtoByOrder(o, orderAlliance, companyConcise.getId()));
            }
            orderExtras.add(transformerExtra(orderAlliance.getExtra(), o, orderKeys));
        });
        orderService.modifyOrderExtra(orderExtras);
    }

    public OrderExtra transformerExtra(OrderExtra extra, IncomeRecordDto dto, Collection<Long> orderKeys) {
        extra.setTransportReceivable(dto.getTransportReceivable());
        extra.setOtherTotalReceivable(dto.getOtherTotalReceivable());
        extra.setTotalReceivable(dto.getTotalReceivable());
        extra.setOtherReceivableRemark(dto.getOtherReceivableRemark());
        extra.setMergerOrder(StringUtils.join(orderKeys, ","));
        return extra;
    }

    public Region transformerRegion(String address) {
        if (StringUtils.isNotBlank(address)) {
            String[] addressArray = RegionUtils.analyze(address);
            if (null != addressArray && addressArray.length != 0) {
                return new Region(addressArray[0], addressArray[1], addressArray[2]);
            }
        }
        return null;
    }

    public double calculatedReceivable(FragmentaryValuation f, OrderAlliance orderAlliance, double number) {
        return f.expense(transformerRegion(orderAlliance.getExtra().getDistributeAddress()),
                transformerRegion(orderAlliance.getReceiveAddress()), number);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<IncomeRecordDto> receivableDetails(Collection<Long> orderKeys, CompanyConcise companyConcise, Integer userKey) throws ParameterException, BusinessException {
        Assert.notEmpty(orderKeys, "请至少选择一条订单");
        Collection<IncomeRecordDto> dtoCollection = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(orderKeys)) {
            //通过订单编号转换订单信息
            Collection<OrderAlliance> orders = orderKeys.stream().map(k -> orderService.getOrderAlliance(userKey, k, true)).filter(o -> CollectionUtils.isNotEmpty(o.getCommodities())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(orders)) {
                for (OrderAlliance order : orders) {
                    if (order.getExtra() == null || StringUtils.isBlank(order.getExtra().getDistributeAddress()) || StringUtils.isBlank(order.getReceiveAddress())) {
                        throw new BusinessException("订单" + order.getDeliveryNo() + "的发货地址或者收获地址为空,不能计算运输费用");
                    }
                    //获取发货地和收货地
                    Region dist = transformerRegion(order.getExtra().getDistributeAddress()), receive = transformerRegion(order.getReceiveAddress());
                    //获取合同信息
                    Contract contract = Registrys.contractRepository().getContract(companyConcise.getId(), order.getShipperId(), LocalDateTime.now());
                    if (contract != null && contract.getId() > 0) {
                        IncomeRecordDto incomeRecordDto = order.getCommodities().stream().reduce(new IncomeRecordDto(order.getId(), order.getDeliveryNo(), DateUtils.toLocalDateTime(order.getDeliveryTime())), (ird, c) -> {
                            //货物物料配置信息
                            CommodityConfig commodityConfig = contract.getCommodity(c.getCommodityNo());
                            //追加数量，重量，体积数据
                            ird.append(c.getQuantity(), commodityConfig.getUnitWeight(), commodityConfig.getUnitVolume());
                            //计算并追加运输费用
                            ird.append(contract.expense(commodityConfig, dist, receive, ird.getTotalQuantity()));
                            return ird;
                        }, (r, f) -> r);
                        dtoCollection.add(incomeRecordDto);
                    } else {
                        throw new BusinessException("沒有找到" + companyConcise.getCompanyName() + "相关的合同信息");
                    }
                }
            }
        }
        return dtoCollection;
    }

    @Override
    @Transactional
    public void batchImportCommodity(Long contractKey, FileEntity fileEntity) throws ParameterException, BusinessException {
        Assert.notBlank(contractKey, "合同编号不能为空");
        try {
            File excelFile = FileUtils.file(fileEntity.getPath());
            Collection<Object[]> objects = EasyExcelBuilder.readExcel(excelFile, 1, 4);
            if (CollectionUtils.isEmpty(objects)) {
                throw new BusinessException("上传的文件中没有可用的数据");
            }
            Contract contract = Registrys.contractRepository().findByKey(contractKey);
            if (contract != null) {
                contract.modify(commodityTransformer.transformerCommdityByArray(objects));
            } else {
                throw new ParameterException("没有找到指定[" + contractKey + "]合同信息");
            }
        } catch (Exception e) {
            throw new BusinessException("物料数据保存异常", e);
        }
    }

    @Override
    public Page<CommodityConfigDto> commoditySearch(Long contractKey, String likeString, PageScope scope) throws
            ParameterException, BusinessException {
        Assert.notBlank(contractKey, "物料编号不能为空");
        Page<CommodityConfigDtoJdbc> page = Registrys.contractQueryRepository().commoditySearch(contractKey,
                likeString, scope.getPageNum(), scope.getPageSize());
        return new Page<CommodityConfigDto>(page.getNum(), page.getSize(), page.getTotal(), commodityTransformer.transformerDto(page.getResults()));
    }

    @Override
    public Page<IncomeRecordDto> incomeSearch(String likeString, Integer statuts, LocalDateTime deliveryDateStart,
                                              LocalDateTime deliveryDateEnd, PageScope pageScope, Long companKey)
            throws ParameterException, BusinessException {
        Assert.notBlank(companKey, "公司编号不能为空");
        Page<IncomeRecordDtoJdbc> page = Registrys.contractQueryRepository().incomeRecordSearch(likeString,
                statuts, deliveryDateStart, deliveryDateEnd, companKey, pageScope.getPageNum(), pageScope.getPageSize());
        return new Page<IncomeRecordDto>(page.getNum(), page.getSize(), page.getTotal(), incomeDtoTransformer.tranIncomeDtoJdbcList(page.getResults()));
    }
}
