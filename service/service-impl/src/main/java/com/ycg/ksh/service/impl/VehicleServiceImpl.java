package com.ycg.ksh.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.github.pagehelper.Page;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.constant.CoreConstants;
import com.ycg.ksh.entity.persistent.Company;
import com.ycg.ksh.entity.persistent.enterprise.Vehicle;
import com.ycg.ksh.entity.persistent.enterprise.VehicleDesignate;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.CompanyConcise;
import com.ycg.ksh.entity.service.enterprise.VehicleAlliance;
import com.ycg.ksh.entity.service.enterprise.VehicleSearch;
import com.ycg.ksh.service.persistence.enterprise.VehicleDesignateMapper;
import com.ycg.ksh.service.persistence.enterprise.VehicleMapper;
import com.ycg.ksh.service.api.CompanyService;
import com.ycg.ksh.service.api.VehicleService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * 车辆管理实现
 *
 * @author: wangke
 * @create: 2018-10-21 09:25
 **/
@Service("ksh.core.service.vehicleService")
public class VehicleServiceImpl implements VehicleService {

    @Resource
    VehicleMapper vehicleMapper;

    @Resource
    CompanyService companyService;

    @Resource
    VehicleDesignateMapper designateMapper;


    private CustomPage<VehicleAlliance> custompage(Company company, Page<VehicleAlliance> page){
        if(page != null){
            Collection<VehicleAlliance> lists = Collections.emptyList();
            if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(page)){
                lists = page.stream().map(v -> alliance(company, v)).collect(Collectors.toList());
            }
            return new CustomPage<VehicleAlliance>(page.getPageNum(), page.getPageSize(), page.getTotal(), lists);
        }
        return null;
    }

    @Override
    public CustomPage<VehicleAlliance> listBySendCar(Integer uKey, VehicleSearch search, PageScope pageScope) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "用户编号不能为空");
        if (null == pageScope) {
            pageScope = PageScope.DEFAULT;
        }
        Company company = companyService.assertCompanyByUserKey(uKey);
        search.setCompanKey(company.getId());
        Page<VehicleAlliance> page = vehicleMapper.listSendCar(search, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
        return custompage(company, page);
    }

    @Override
    public CustomPage<VehicleAlliance> listByNeedCar(Integer uKey, VehicleSearch search, PageScope pageScope) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "用户编号不能为空");
        if (null == pageScope) {
            pageScope = PageScope.DEFAULT;
        }
        Company company = companyService.assertCompanyByUserKey(uKey);
        search.setCompanKey(company.getId());
        Page<VehicleAlliance> page = vehicleMapper.listNeedCar(search, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
        return custompage(company, page);
    }

    public VehicleAlliance alliance(Company company, VehicleAlliance alliance) {
        alliance.setConvey(companyService.getCompanyConciseByKey(alliance.getDesignate().getConveyId()));
        alliance.setLastCompany(companyService.getCompanyConciseByKey(alliance.getDesignate().getLastCompanyKey()));
        //录入要车计划的企业才可以做确认派车的操作
        alliance.setConfirm(alliance.getShipperId() - company.getId() == 0 && alliance.getCarStatus() == CoreConstants.CAR_STATUS_ALREADY);
        return alliance;
    }

    @Override
    public void saveVehicle(Vehicle vehicle, VehicleDesignate designate) throws ParameterException, BusinessException {
        Assert.notBlank(designate.getConveyId(), "请选择物流商");
        try {
            Company company = companyService.assertCompanyByUserKey(vehicle.getCreateUser());
            Company conveyr = companyService.getCompanyByKey(designate.getConveyId());
            if(conveyr == null){
                throw new ParameterException("指定的物流商未在合同物流管理平台平台注册");
            }
            vehicle.setKey(Globallys.nextKey());
            vehicle.setShipperId(company.getId());
            vehicle.setCreateTime(new Date());
            vehicle.setCarStatus(CoreConstants.CAR_STATUS_NO);
            if (vehicleMapper.insertSelective(vehicle) > 0) {
                designate.setKey(Globallys.nextKey());
                designate.setConveyId(conveyr.getId());
                designate.setLastCompanyKey(company.getId());
                designate.setvId(vehicle.getKey());
                designate.setCreateTime(new Date());
                designateMapper.insertSelective(designate);
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("要车单录入异常", e);
        }

    }

    @Override
    public void wantCar(Integer uKey, VehicleDesignate designate) throws ParameterException, BusinessException {
        Assert.notBlank(designate.getConveyId(), "请选择物流商");
        Assert.notBlank(uKey, "用户编号不能为空");
        Assert.notBlank(designate.getvId(), "要车单号不能为空");
        try {
            Company company = companyService.assertCompanyByUserKey(uKey);
            Vehicle vehicle = vehicleMapper.selectByPrimaryKey(designate.getvId());
            if(vehicle == null){
                throw new BusinessException("指定的要车单已经删除");
            }
            if(vehicle.getCarStatus() != CoreConstants.CAR_STATUS_NO){
                throw new BusinessException("物流商已经派车,不能继续要车");
            }
            Company conveyr = companyService.getCompanyByKey(designate.getConveyId());
            if(conveyr == null){
                throw new ParameterException("指定的物流商未在合同物流管理平台平台注册");
            }
            designate.setKey(Globallys.nextKey());
            designate.setvId(vehicle.getKey());
            designate.setConveyId(conveyr.getId());
            designate.setLastCompanyKey(company.getId());
            designate.setCreateTime(new Date());
            designateMapper.insertSelective(designate);
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("要车数据异常", e);
        }
    }

    @Override
    public void deleteVehicle(Long key) throws ParameterException, BusinessException {
        Assert.notBlank(key, "需要删除得车辆编号不能为空");
        try {
            Vehicle vehicle = vehicleMapper.selectByPrimaryKey(key);
            if(vehicle == null){
                throw new BusinessException("指定的要车单已经删除");
            }
            if(vehicle.getCarStatus() != CoreConstants.CAR_STATUS_NO){
                throw new BusinessException("物流商已经派车,不能删除");
            }
            if(vehicleMapper.deleteByPrimaryKey(vehicle.getKey()) > 0){
                designateMapper.delete(new VehicleDesignate(vehicle.getKey()));
            }
        } catch (Exception e) {
            throw new BusinessException("删除要车数据异常", e);
        }
    }

    @Override
    public void confirmCar(Long key) throws ParameterException, BusinessException {
        Assert.notBlank(key, "要车编号不能为空");
        //Vehicle vehicle = new Vehicle();
        //vehicle.setKey(key);
        //vehicle.setCarStatus(CoreConstants.CAR_STATUS_CARRY);
        try {
            Vehicle vehicle = vehicleMapper.selectByPrimaryKey(key);
            if(vehicle == null){
                throw new BusinessException("指定的要车单已经删除");
            }
            if(vehicle.getCarStatus() == CoreConstants.CAR_STATUS_NO){
                throw new BusinessException("物流商尚未派车,不能确认");
            }
            if(vehicle.getCarStatus() == CoreConstants.CAR_STATUS_CARRY){
                throw new BusinessException("已经确认，不能重复确认");
            }
            vehicle.setCarStatus(CoreConstants.CAR_STATUS_CARRY);
            vehicleMapper.updateByPrimaryKeySelective(vehicle);
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("确认派车异常", e);
        }
    }

    @Override
    public void modifyVehicle(Vehicle vehicle, Collection<Long> vids) throws ParameterException, BusinessException {
        Assert.notEmpty(vids, "请至少选择一项要车单");
        Assert.notBlank(vehicle.getCarNo(), "车牌号不能为空");
        Assert.notBlank(vehicle.getDriverName(), "司机姓名不能为空");
        Assert.notBlank(vehicle.getDriverNumber(), "司机电话不能为空");
        try {
            Collection<Vehicle> collection = vehicleMapper.selectByIdentities(vids);
            if (CollectionUtils.isNotEmpty(collection)) {
                //选过滤已经确认派车的数据
                collection = collection.stream().filter(v -> v.getCarStatus() != CoreConstants.CAR_STATUS_CARRY).peek(v->{
                    v.setCarNo(vehicle.getCarNo());
                    v.setDriverName(vehicle.getDriverName());
                    v.setDriverNumber(vehicle.getDriverNumber());
                    v.setCarStatus(CoreConstants.CAR_STATUS_ALREADY);
                }).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collection)) {
                    vehicleMapper.updates(collection);
                }
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("派车/派车调整异常", e);
        }
    }

    @Override
    public Collection<CompanyConcise> listSource(Long companyKey) throws ParameterException, BusinessException {
        Assert.notBlank(companyKey, "企业编号不能为空");
        return vehicleMapper.listSource(companyKey);
    }

    @Override
    public Vehicle queryVehicle(Long key) throws ParameterException, BusinessException {
        Assert.notBlank(key, "要车单编号不能为空");
        return vehicleMapper.selectByPrimaryKey(key);
    }
}