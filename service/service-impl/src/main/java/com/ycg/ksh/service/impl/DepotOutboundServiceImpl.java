package com.ycg.ksh.service.impl;

import com.github.pagehelper.Page;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.excel.EasyExcelBuilder;
import com.ycg.ksh.common.excel.ExcelWriter;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.CustomExample;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.DateUtils;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.constant.CoreConstants;
import com.ycg.ksh.entity.persistent.Company;
import com.ycg.ksh.entity.persistent.depot.InboundOrder;
import com.ycg.ksh.entity.persistent.depot.OutboundDetail;
import com.ycg.ksh.entity.persistent.depot.OutboundOrder;
import com.ycg.ksh.entity.persistent.enterprise.CompanyCustomer;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.depot.DepotAlliance;
import com.ycg.ksh.entity.service.depot.DepotBatchSomething;
import com.ycg.ksh.entity.service.depot.DepotSearch;
import com.ycg.ksh.entity.service.depot.OutBoundPrintAlliance;
import com.ycg.ksh.service.api.UserService;
import com.ycg.ksh.service.persistence.depot.InboundOrderMapper;
import com.ycg.ksh.service.persistence.depot.OutboundDetailMapper;
import com.ycg.ksh.service.persistence.depot.OutboundOrderMapper;
import com.ycg.ksh.service.api.CompanyService;
import com.ycg.ksh.service.api.CustomerService;
import com.ycg.ksh.service.api.DepotOutboundService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 仓库出入库实现接口类
 *
 * @Auther: wangke
 * @Date: 2018/9/4 15:59
 * @Description:
 */
@Service("ksh.core.service.depotOutboundService")
public class DepotOutboundServiceImpl implements DepotOutboundService {

    @Resource
    InboundOrderMapper antiSortGoodsMapper;
    @Resource
    OutboundOrderMapper outboundOrderMapper;
    @Resource
    OutboundDetailMapper outboundDetailMapper;

    @Resource
    CompanyService companyService;

    @Resource
    CustomerService customerService;

    @Resource
    UserService userService;

    /**
     * 根据批次号获取批次详情
     *
     * @param batchNumber
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public InboundOrder getByBatchNumber(String batchNumber) throws ParameterException, BusinessException {
        return antiSortGoodsMapper.selectByBatchNumber(batchNumber);
    }

    @Override
    public void saveInboundOrders(Integer uKey, Map<String, InboundOrder> map) throws ParameterException,
            BusinessException {
        Assert.notBlank(uKey, "操作人编号不能为空");
        Assert.notEmpty(map, "导入数据不能为空");
        try {
            Company company = companyService.assertCompanyByUserKey(uKey);
            Collection<InboundOrder> collection = collectionStitching(map);
            Map<String, InboundOrder> receiveList = collection.stream().collect(Collectors.toMap(a -> a.getBatchNumber(), (p) -> p, (p, c) -> {
                c.setImportTimes(Optional.ofNullable(c.getImportTimes()).orElse(1));
                p.setImportTimes(Optional.ofNullable(p.getImportTimes()).orElse(1) + c.getImportTimes());
                return p;
            }));
            Collection<InboundOrder> modifyCommit = new ArrayList<InboundOrder>();
            Collection<InboundOrder> insertCommit = new ArrayList<InboundOrder>();
            receiveList.forEach((k, v) -> {
                InboundOrder goods = antiSortGoodsMapper.selectOne(new InboundOrder(v.getBatchNumber()));
                if (null != goods) {
                    goods.setImportTimes(goods.getImportTimes() + (Optional.ofNullable(v.getImportTimes()).orElse(1)));
                    goods.setUpdateTime(new Date());
                    modifyCommit.add(goods);
                } else {
                    CompanyCustomer customer = customerService.loadCustomer(uKey, company.getId(), v.getCustomerName(),
                            CoreConstants.COMPANYCUSTOMER_TYPE_SHIPPER, null, true);
                    v.setImportTimes(Optional.ofNullable(v.getImportTimes()).orElse(1));
                    v.setCustomerId(customer.getKey());
                    v.setOperatorId(uKey);
                    v.setCompanyKey(company.getId());
                    insertCommit.add(v);
                }
            });

            if (CollectionUtils.isNotEmpty(modifyCommit)) {
                antiSortGoodsMapper.updates(modifyCommit);
            }
            if (CollectionUtils.isNotEmpty(insertCommit)) {
                antiSortGoodsMapper.inserts(insertCommit);
            }

        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.info("入库单导入异常 uKey:{} map:{}", uKey, map);
            throw BusinessException.dbException("入库单导入异常");
        }

    }

    /**
     * 分割批次号获得完整集合
     *
     * @param map
     * @return
     */
    public Collection<InboundOrder> collectionStitching(Map<String, InboundOrder> map) {
        Collection<InboundOrder> collection = new ArrayList<InboundOrder>();
        for (Map.Entry<String, InboundOrder> stringCustomerEntry : map.entrySet()) {
            InboundOrder antiSortGoods = stringCustomerEntry.getValue();
            String[] batchArrys = StringUtils.segmentationBatch(antiSortGoods.getBatchNumber());
            for (String batchNumber : batchArrys) {
                collection.add(initAntiSortGoods(batchNumber, antiSortGoods));
            }
        }
        return collection;
    }

    public InboundOrder initAntiSortGoods(String batchNumber, InboundOrder initAntiSortGoods) {
        InboundOrder antiSortGoods = new InboundOrder();
        BigDecimal bigDecimal = new BigDecimal(initAntiSortGoods.getDeliveryNo());
        antiSortGoods.setKey(Globallys.nextKey());
        antiSortGoods.setDeliveryTime(new Date());
        antiSortGoods.setStorageTime(new Date());
        antiSortGoods.setBatchNumber(batchNumber);
        antiSortGoods.setDeliveryNo(bigDecimal.toPlainString());
        antiSortGoods.setCustomerName(initAntiSortGoods.getCustomerName());
        antiSortGoods.setMaterialName(initAntiSortGoods.getMaterialName());
        antiSortGoods.setPickingQuantity(initAntiSortGoods.getPickingQuantity());
        antiSortGoods.setPickingNo(initAntiSortGoods.getPickingNo());
        antiSortGoods.setDeliveryQuantity(initAntiSortGoods.getDeliveryQuantity());
        antiSortGoods.setLicensePlate(initAntiSortGoods.getLicensePlate());
        antiSortGoods.setCustomerId(initAntiSortGoods.getCustomerId());
        antiSortGoods.setCompanyKey(initAntiSortGoods.getCompanyKey());
        return antiSortGoods;

    }

    /**
     * 分页查询
     *
     * @param uKey
     * @param search
     * @param scope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public CustomPage<DepotAlliance> pageInboundOrder(Integer uKey, DepotSearch search, PageScope scope) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人编号不能为空");
        search.setCompanyKey(Optional.ofNullable(search.getCompanyKey()).orElse(Optional.ofNullable(companyService.getCompanyByUserKey(uKey)).map(c -> c.getId()).orElse(0L)));
        if (search.getCompanyKey() <= 0) {
            return new CustomPage<DepotAlliance>(0, 0, 0, Collections.emptyList());
        }
        Page<DepotAlliance> page = antiSortGoodsMapper.listInboundOrder(search, new RowBounds(scope.getPageNum(), scope.getPageSize()));
        return new CustomPage<DepotAlliance>(page.getPageNum(), page.getPageSize(), page.getTotal(), conversionInboundOrder(page));
    }

    @Override
    public CustomPage<DepotBatchSomething> pageBatchSomething(Integer uKey, DepotSearch search, PageScope scope) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人编号不能为空");
        search.setCompanyKey(Optional.ofNullable(search.getCompanyKey()).orElse(Optional.ofNullable(companyService.getCompanyByUserKey(uKey)).map(c -> c.getId()).orElse(0L)));
        if (search.getCompanyKey() <= 0) {
            return new CustomPage<DepotBatchSomething>(0, 0, 0, Collections.emptyList());
        }
        Page<DepotBatchSomething> page = outboundDetailMapper.listBySomething(search, new RowBounds(scope.getPageNum(), scope.getPageSize()));
        return new CustomPage<DepotBatchSomething>(page.getPageNum(), page.getPageSize(), page.getTotal(), page);
    }

    /**
     * 出库
     *
     * @param uKey
     * @param order
     * @param details
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void takeOutDepot(Integer uKey, OutboundOrder order, Collection<OutboundDetail> details) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人编号不能为空");
        Assert.notNull(order, "客户新不能为空");
        Assert.notBlank(order.getReceiveId(), "收货客户不能为空");
        Assert.notBlank(order.getShipperId(), "发货客户不能为空");
        Assert.notEmpty(details, "出库明细不能为空");
        Company company = companyService.assertCompanyByUserKey(uKey);

        if (StringUtils.isBlank(order.getReceiveName())) {
            CompanyCustomer customer = customerService.getCompanyCustomer(order.getReceiveId());
            if (customer != null) {
                order.setReceiveName(customer.getName());
            } else {
                throw new ParameterException("收货客户信息有误");
            }
        }
        if (StringUtils.isBlank(order.getShipperName())) {
            CompanyCustomer customer = customerService.getCompanyCustomer(order.getShipperId());
            if (customer != null) {
                order.setShipperName(customer.getName());
            } else {
                throw new ParameterException("发货客户信息有误");
            }
        }

        order.setKey(Globallys.nextKey());
        order.setCompanyKey(company.getId());
        order.setDeliveryTime(Optional.ofNullable(order.getDeliveryTime()).orElse(new Date()));

        if (outboundOrderMapper.insert(order) > 0) {
            Collection<OutboundDetail> collection = details.stream().map(d -> {
                d.setKey(Globallys.nextKey());
                d.setOutboundId(order.getKey());
                return d;
            }).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collection)) {
                outboundDetailMapper.inserts(collection);
            }
        }

    }

    @Override
    public void deleteInboun(Collection<Long> ids) throws ParameterException, BusinessException {
        Assert.notEmpty(ids, "请至少选择一条需要删除的数据");
        if (CollectionUtils.isNotEmpty(ids)) {
            try {
                antiSortGoodsMapper.deletes(ids);
            } catch (ParameterException | BusinessException e) {
                throw e;
            } catch (Exception e) {
                logger.info("入库单删除异常 ids:{}", ids);
                throw BusinessException.dbException("入库单删除异常");
            }
        }
    }


    public Collection<DepotAlliance> conversionInboundOrder(Collection<DepotAlliance> page) {
        if (CollectionUtils.isNotEmpty(page)) {
            page.forEach(a -> {
                try {
                    //  DepotAlliance inboundOrder = DepotAlliance.buildAlliance(a);
                    a.setUname(userService.getConciseUser(a.getOperatorId()).getUnamezn());
                    //   page.add(inboundOrder);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return page;
    }


    @Override
    public FileEntity listExportOrders(Collection<Long> key) throws ParameterException, BusinessException {
        Assert.notEmpty(key, "至少选择一条需要导出的值");
        ExcelWriter easyExcel = null;
        try {
            Collection<DepotAlliance> depotAlliances = conversionInboundOrder(outboundDetailMapper.listExportOrders(key));
            FileEntity fileEntity = new FileEntity();
            fileEntity.setSuffix(FileUtils.XLSX_SUFFIX);
            fileEntity.setDirectory(SystemUtils.directoryDownload());
            fileEntity.setFileName(FileUtils.appendSuffix("" + System.nanoTime(), FileUtils.XLSX_SUFFIX));
            File destFile = FileUtils.newFile(fileEntity.getDirectory(), fileEntity.getFileName());
            easyExcel = EasyExcelBuilder.createWriteExcel(destFile);
            easyExcel.createSheet("出库单列表");
            easyExcel.columnWidth(30, 25, 30, 30, 10, 20);
            easyExcel.header("日期", "客戶", "物料", "批次", "数量", "操作人");
            for (DepotAlliance alliance : depotAlliances) {
                easyExcel.row(DateUtils.formatDate(alliance.getDeliveryTime()), alliance.getReceiveName(), alliance.getMaterialName(),
                        alliance.getBatchNumber(), alliance.getOutboundQuantity(), alliance.getUname());
            }
            easyExcel.write();
            fileEntity.setPath(destFile.getPath());
            fileEntity.setCount(1);
            fileEntity.setSize(FileUtils.size(destFile.length(), FileUtils.ONE_MB));
            return fileEntity;
        } catch (Exception e) {
            logger.error("出库单导出异常 {} ", key);
            throw new BusinessException("出库单导出异常", e);
        } finally {
            if (easyExcel != null) {
                try {
                    easyExcel.close();
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public Collection<DepotAlliance> listPrintGroup(Collection<Long> key) throws ParameterException, BusinessException {
        Assert.notEmpty(key, "请至少选择一个需要打印的数据");
        Collection<DepotAlliance> depotAlliances = outboundDetailMapper.listPrintGroup(key);
        if (CollectionUtils.isNotEmpty(depotAlliances)) {
            depotAlliances.forEach(d -> {
                Collection<OutBoundPrintAlliance> outBounds = outboundDetailMapper.
                        listOutBoundByGroup(d.getReceiveName(), d.getDateFormat(), null)
                        .stream().collect(
                                Collectors.collectingAndThen(Collectors.toCollection(
                                        () -> new TreeSet<>(Comparator.comparing(o -> o.getMaterialName()))), ArrayList::new)
                        );
                d.setPrintSummary(outBounds);
                if (CollectionUtils.isNotEmpty(d.getPrintSummary())) {
                    d.getPrintSummary().forEach(o -> {
                        Collection<OutBoundPrintAlliance> outBoundDetail = outboundDetailMapper.
                                listOutBoundByGroup(d.getReceiveName(), d.getDateFormat(), o.getMaterialName());
                        o.setPrintDetail(outBoundDetail);
                    });
                }
            });
        }
        return depotAlliances;
    }
}
