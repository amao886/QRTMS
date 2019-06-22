package com.ycg.ksh.service.impl;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/30
 */

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.rabbitmq.MediaMessage;
import com.ycg.ksh.common.extend.rabbitmq.RabbitMessageListener;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.constant.LocationEvent;
import com.ycg.ksh.constant.LocationType;
import com.ycg.ksh.constant.ObjectType;
import com.ycg.ksh.constant.OrderFettleType;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.adapter.api.AutoMapService;
import com.ycg.ksh.adapter.api.MessageQueueService;
import com.ycg.ksh.constant.*;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.service.AllianceLocation;
import com.ycg.ksh.entity.service.enterprise.OrderAlliance;
import com.ycg.ksh.service.persistence.LocationTrackMapper;
import com.ycg.ksh.service.api.LocationTrackService;
import com.ycg.ksh.service.api.OrderService;
import com.ycg.ksh.service.api.UserService;
import com.ycg.ksh.service.observer.AdventiveObserverAdapter;
import com.ycg.ksh.service.observer.DriverContainerObserverAdapter;
import com.ycg.ksh.service.observer.LocationObserverAdapter;
import com.ycg.ksh.service.observer.OrderObserverAdapter;
import com.ycg.ksh.service.util.O;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * 位置轨迹服务
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/30
 */
@Service("ksh.core.service.locationTrackService")
public class LocationTrackServiceImpl implements LocationTrackService, RabbitMessageListener, DriverContainerObserverAdapter, OrderObserverAdapter, AdventiveObserverAdapter {


    @Resource
    LocationTrackMapper locationTrackMapper;

    @Resource
    AutoMapService autoMapService;

    @Resource
    OrderService orderService;
    @Resource
    UserService userService;
    @Resource
    MessageQueueService queueService;

    @Autowired(required = false)
    Collection<LocationObserverAdapter> observers;


    private void modifyNoticeObserver(LocationEvent locationEvent, LocationType locationType, LocationTrack locationTrack){
        if (CollectionUtils.isNotEmpty(observers)) {
            for (LocationObserverAdapter observerAdapter : observers) {
                observerAdapter.notifyLocationReport(locationEvent, locationType, locationTrack);
            }
        }
    }

    @Override
    public boolean handleMessage(String messageType, String messageKey, Object object) throws BusinessException {
        if (StringUtils.equalsIgnoreCase(messageType, LocationTrack.class.getName())) {
            try {
                LocationTrack locationTrack = (LocationTrack) object;
                modifyNoticeObserver(LocationEvent.SYNC, LocationType.convert(locationTrack.getHostType()), locationTrack);
                return true;
            } catch (Exception ex) {
                throw new BusinessException("定位异步处理异常", ex);
            }
        }
        return false;
    }

    /**
     * 保存位置轨迹
     *
     * @param uKey
     * @param locationType
     * @param locationTrack
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void saveTrack(Integer uKey, LocationType locationType, LocationTrack locationTrack) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人编号不能为空");
        Assert.notNull(locationType, "位置类型不能为空");
        Assert.notNull(locationTrack, "位置信息不能为空");
        Assert.notBlank(locationTrack.getHostId(), "关联编号不能为空");
        Assert.notNull(locationTrack.getLocation(), "位置地址不能为空");
        Assert.notNull(locationTrack.getLatitude(), "位置纬度不能为空");
        Assert.notNull(locationTrack.getLongitude(), "位置经度不能为空");
        try {

            //autoMapService.coordinate()


            locationTrack.setId(Globallys.nextKey());
            locationTrack.setCreateTime(new Date());
            locationTrack.setHostType(locationType.getCode());
            locationTrack.setUserId(uKey);

            locationTrackMapper.insertSelective(locationTrack);

            modifyNoticeObserver(LocationEvent.SCANCODE, locationType, locationTrack);

            queueService.sendCoreMessage(new MediaMessage(locationTrack.getId(), locationTrack));

        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("saveTrack  --> uKey:{} locationType:{} locationTrack:{}", uKey, locationType, locationTrack, e);
            throw BusinessException.dbException("保存位置信息异常");
        }
    }

    @Override
    public Collection<LocationTrack> listBySomething(LocationType locationType, Serializable hostKey) throws ParameterException, BusinessException {
        Assert.notNull(locationType, "位置类型不能为空");
        Assert.notNull(hostKey, "关联编号不能为空");
        Example example = new Example(LocationTrack.class);
        example.createCriteria().andEqualTo("hostType", locationType.getCode()).andEqualTo("hostId", hostKey);
        example.orderBy("createTime").desc();
        return locationTrackMapper.selectByExample(example);
    }

    @Override
    public int countBySomething(LocationType locationType, Serializable hostKey) throws ParameterException, BusinessException {
        Assert.notNull(locationType, "位置类型不能为空");
        Assert.notNull(hostKey, "关联编号不能为空");
        Example example = new Example(LocationTrack.class);
        example.createCriteria().andEqualTo("hostType", locationType.getCode()).andEqualTo("hostId", hostKey);
        return locationTrackMapper.selectCountByExample(example);
    }

    @Override
    public Collection<AllianceLocation> listAllianceBySomething(LocationType locationType, Serializable hostKey) throws ParameterException, BusinessException {
        Collection<LocationTrack> locationTracks = listBySomething(locationType, hostKey);
        if(locationTracks != null && locationTracks.size() > 0){
            Collection<AllianceLocation> allianceLocations = new ArrayList<AllianceLocation>(locationTracks.size());
            for (LocationTrack locationTrack : locationTracks) {
                AllianceLocation alliance = new AllianceLocation(locationTrack);
                User user = userService.getUserByKey(locationTrack.getUserId());
                if(user != null){
                    alliance.setReporterName(user.getUnamezn());
                    alliance.setReporterMobile(user.getMobilephone());
                }
                allianceLocations.add(alliance);
            }
            return allianceLocations;
        }
        return null;
    }

    /**
     * 联合订单数据
     *
     * @param uKey
     * @param alliance
     * @param flags
     * @throws BusinessException
     */
    @Override
    public void allianceOrder(Integer uKey, OrderAlliance alliance, Integer... flags) throws BusinessException {
        if(Arrays.binarySearch(flags, O.loaction) >= 0){
            alliance.setLocations(listBySomething(LocationType.ORDER, alliance.getId()));
        }
    }

    /**
     * 查询详情数据
     *
     * @param objectType 对象类型
     * @param objectKey  对象编号
     * @return
     */
    @Override
    public Object adventiveObjectByKey(ObjectType objectType, Serializable objectKey) throws Exception {
        if(ObjectType.LOCATIONTRACK == objectType){
            //AllianceLocation
            LocationTrack locationTrack = locationTrackMapper.selectByPrimaryKey(Long.parseLong(objectKey.toString()));
            if(locationTrack != null){
                AllianceLocation alliance = new AllianceLocation(locationTrack);
                User user = userService.getUserByKey(locationTrack.getUserId());
                if(user != null){
                    alliance.setReporterName(user.getUnamezn());
                    alliance.setReporterMobile(user.getMobilephone());
                }
                return alliance;
            }
        }
        return null;
    }


    /**
     * 位置上报通知
     *
     * @param uKey
     * @param barcodes
     * @throws BusinessException
     */
    @Override
    public void notifyLocationReport(Integer uKey, DriverTrack track, Collection<Barcode> barcodes) throws BusinessException {
        if (CollectionUtils.isNotEmpty(barcodes)) {
            try {
                Date cdate = new Date();
                Collection<LocationTrack> locationTracks = new ArrayList<LocationTrack>();
                for (Barcode barcode : barcodes) {
                    if(barcode.getCompanyId() == null || barcode.getCompanyId() <= 0){
                        continue;
                    }
                    Order order = orderService.getOrderByCode(barcode.getBarcode());
                    if (order != null && OrderFettleType.convert(order.getFettle()).isIng()) {
                        LocationTrack location = new LocationTrack(LocationType.ORDER, order.getId(), track.getLongitude(), track.getLatitude(), track.getReportLoaction());
                        location.setId(Globallys.nextKey());
                        location.setCreateTime(cdate);
                        location.setUserId(uKey);
                        locationTracks.add(location);
                       // locationTracks.add(new LocationTrack(LocationType.ORDER, order.getId(), track.getLongitude(), track.getLatitude(), track.getReportLoaction()));
                    }
                }
                if(CollectionUtils.isNotEmpty(locationTracks)){
                    locationTrackMapper.inserts(locationTracks);
                    for (LocationTrack locationTrack : locationTracks) {
                        modifyNoticeObserver(LocationEvent.DRIVERCONTAINER, LocationType.ORDER, locationTrack);

                        queueService.sendCoreMessage(new MediaMessage(locationTrack.getId(), locationTrack));
                    }
                }
            } catch (BusinessException | ParameterException e) {
                throw e;
            } catch (Exception e) {
                logger.error("notify location report --> uKey:{} track:{}", uKey, track, e);
                throw BusinessException.dbException("更新订单最新位置异常");
            }
        }
    }

}
