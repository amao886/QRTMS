/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-23 17:49:49
 */
package com.ycg.ksh.service.impl;

import com.ycg.ksh.adapter.api.AutoMapService;
import com.ycg.ksh.adapter.api.SmsService;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.cache.LocalCacheManager;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.DateUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.adapter.AutoMapLocation;
import com.ycg.ksh.entity.common.constant.WaybillFettle;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.service.*;
import com.ycg.ksh.service.persistence.*;
import com.ycg.ksh.service.api.DriverService;
import com.ycg.ksh.service.api.WaybillService;
import com.ycg.ksh.service.api.WaybillTrackService;
import com.ycg.ksh.service.observer.DriverContainerObserverAdapter;
import com.ycg.ksh.service.observer.TrackObserverAdapter;
import com.ycg.ksh.service.observer.WaybillObserverAdapter;
import com.ycg.ksh.service.support.assist.LocalCacheFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 运单轨迹业务逻辑实现
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-23 17:49:49
 */
@Service("ksh.core.service.waybillTrackService")
public class WaybillTrackServiceImpl implements WaybillTrackService, WaybillObserverAdapter, DriverContainerObserverAdapter {
	
	private final Logger logger = LoggerFactory.getLogger(WaybillTrackService.class);
	
	@Resource
	WaybillService waybillService;
    @Resource
    DriverService containerService;
    
	@Resource
	WaybillTrackMapper trackMapper;
	@Resource
	WaybillDriverScanMapper driverScanMapper;
	@Resource
	UserMapper userMapper;
    @Resource
    AutoMapService autoMapService;
    @Resource
	DriverTrackMapper driverTrackMapper;
    @Resource
	TransitionTrackMapper transitionTrackMapper;
    @Resource
    SmsService smsService;
	
	@Autowired(required=false)
	Collection<TrackObserverAdapter> observers;


	/**
	 * @see com.ycg.ksh.service.api.WaybillTrackService#saveLoactionReport(java.lang.Integer, WaybillTrack)
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-29 16:25:28
	 */
	@Override
	public void saveLoactionReport(Integer userId, WaybillTrack track) throws ParameterException, BusinessException {
		Assert.notNull(userId, "操作人编号不能为空");
		Assert.notNull(track, "轨迹信息不能为空不能为空");
		Assert.notNull(track.getWaybillid(), "任务单编号不能为空不能为空");
		Assert.notNull(track.getLocations(), "定位信息不能为空");
		try {
			Waybill waybill = waybillService.getWaybillById(track.getWaybillid());
            if(waybill == null) {
            	throw new ParameterException(track.getWaybillid(), "指定任务单不存在");
            }
			save(userId, waybill, track);
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("save bind -> userId:{} track:{}", userId, track, e);
            throw BusinessException.dbException("运单位置上报异常");
        }
	}

	private void save(Integer userId, Waybill waybill, WaybillTrack track){
		WaybillFettle waybillFettle = WaybillFettle.convert(waybill.getWaybillStatus());
		if(waybillFettle.unbind()) {
			throw new BusinessException("运单尚未绑定不能上报定位!!!");
		}
		/*
		if(waybillFettle.receive()) {
			throw new BusinessException("运单已经送达不能上报定位!!!");
		}
		*/
		logger.info("========来自扫码定位===========");
		if(track.getLatitude() == null || track.getLongitude() == null){
			try {
				AutoMapLocation location = autoMapService.coordinate(track.getLocations());
				if (location != null) {
					track.setLatitude(new Double(location.getLatitude()));
					track.setLongitude(new Double(location.getLongitude()));
				}
			} catch (Exception e) {
				logger.error("获取收货地址经纬度异常 {}", track.getLocations(), e);
			}
		}
		track.setWaybillid(waybill.getId());
		track.setCreatetime(new Date());
		if(track.getUserid() == null || track.getUserid() <= 0){
			track.setUserid(userId);
		}
		locationReport(WaybillContext.buildContext(userId, waybill), track, true);
	}

	/**
	 * 上报位置
	 *
	 * @param userId
	 * @param barcode
	 * @param waybillTrack
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	@Override
	public void saveLoactionReport(Integer userId, String barcode, WaybillTrack track) throws ParameterException, BusinessException {
		Assert.notNull(userId, "操作人编号不能为空");
		Assert.notNull(barcode, "任务单编号不能为空不能为空");
		Assert.notNull(track, "轨迹信息不能为空不能为空");
		Assert.notNull(track.getLocations(), "定位信息不能为空");
		try {
			Waybill waybill = waybillService.getWaybillByCode(barcode);
			if(waybill == null) {
				throw new ParameterException(barcode, "指定任务单不存在");
			}
			save(userId, waybill, track);
		} catch (BusinessException | ParameterException e) {
			throw e;
		} catch (Exception e) {
			logger.error("save bind -> userId:{} track:{}", userId, track, e);
			throw BusinessException.dbException("运单位置上报异常");
		}
	}

	private void locationReport(WaybillContext context, WaybillTrack track, boolean driver) throws ParameterException, BusinessException {
	    if(trackMapper.selectCount(new WaybillTrack(track.getWaybillid()))==0) {
	        String sendContent = String.format(Constant.SMS_LOCATION_STRING, context.getReceiverName(),context.getNumber(),context.getSimpleStartStation(),context.getDeliveryNumber(),DateUtils.date2Str(context.getArrivaltime()));
	        smsService.sendmsg(context.getContactPhone(), sendContent);//第一次定位发生短信
	    }
		if(trackMapper.insertSelective(track) > 0) {
			if(driver) {
	        	if(driverScanMapper.selectCount(new WaybillDriverScan(track.getWaybillid(), track.getUserid())) <= 0) {
	                driverScanMapper.insertSelective(new WaybillDriverScan(track.getWaybillid(), track.getUserid(), 1, track.getCreatetime()));
	            }
	        }
	        if(CollectionUtils.isNotEmpty(observers)) {
	            for (TrackObserverAdapter abstractObserver : observers) {
	                logger.info("TrackObserverAdapter=========测试定位==========");
	                abstractObserver.notifyLocationReport(context, track, driver);
	            }
	        }
		}
	}

	/**
	 * @see com.ycg.ksh.service.api.WaybillTrackService#save(WaybillTrack)
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-23 17:51:52
	 */
	@Override
	public WaybillTrack save(WaybillTrack track) throws ParameterException, BusinessException {
		Waybill waybill = waybillService.getWaybillById(track.getWaybillid());
		if(waybill == null) {
			throw new ParameterException("运单信息不存在!");
		}
		if(Constant.WAYBILL_STATUS_ARRIVE == waybill.getWaybillStatus()) {
			throw new ParameterException("该任务已经送达!");
		}
		if(Constant.WAYBILL_STATUS_RECEIVE == waybill.getWaybillStatus()) {
			throw new ParameterException("该任务已经结束!");
		}
		try {
			track.setCreatetime(new Date());
			trackMapper.insertSelective(track);
		} catch (Exception e) {
			logger.error("保存运单轨迹信息异常 track:{}", track, e);
			throw new BusinessException("保存运单轨迹信息异常");
		}
		return track;
	}

	/**
	 * 查询任务单轨迹
	 * @see com.ycg.ksh.service.api.WaybillTrackService#listByWaybillId(java.lang.Integer)
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-27 09:47:05
	 */
	@Override
	public List<MergeTrack> listByWaybillId(Integer waybillId) throws ParameterException, BusinessException {
		Assert.notNull(waybillId, "运单编号不能为空");
		try {
			Example example = new Example(WaybillTrack.class);
			Criteria criteria = example.createCriteria();
			criteria.andEqualTo("waybillid", waybillId);
			example.orderBy("createtime").desc();
			List<WaybillTrack> tracks = trackMapper.selectByExample(example);
			if(CollectionUtils.isNotEmpty(tracks)) {
				LocalCacheManager<AssociateUser> cache = LocalCacheFactory.createUserCache(userMapper);
				List<MergeTrack> mergeWaybillTracks = new ArrayList<MergeTrack>(tracks.size());
				for (WaybillTrack track : tracks) {
					mergeWaybillTracks.add(new MergeTrack(track, cache.get(track.getUserid())));
				}
				return mergeWaybillTracks;
			}
		} catch (Exception e) {
			logger.error("查询运单轨迹信息异常, waybillId : {}", waybillId, e);
			throw BusinessException.dbException("查询运单轨迹信息异常");
		}
		return null;
	}
	
    /**
     * @see WaybillObserverAdapter#mergeWaybill(MergeWaybill, WaybillAssociate)
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-02 17:01:18
     */
    @Override
    public void onMergeWaybill(MergeWaybill waybill, WaybillAssociate associate) throws BusinessException {
        if(associate.isAssociateTrack()) {
            waybill.setTracks(listByWaybillId(waybill.getId()));
        }
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
		if(binding && StringUtils.isNotBlank(context.getBarcode())) {
			List<WaybillTrack> waybillTracks = new ArrayList<WaybillTrack>();
			boolean dirver = true;
			Collection<DriverTrack> transitionTracks = driverTrackMapper.selectByBarcode(context.getBarcode());
			if(CollectionUtils.isNotEmpty(transitionTracks)) {
				for (DriverTrack track : transitionTracks) {
					WaybillTrack waybillTrack = new WaybillTrack();
					waybillTrack.setUserid(track.getUserId());
					waybillTrack.setCreatetime(track.getReportTime());
					waybillTrack.setWaybillid(context.getId());
					waybillTrack.setLatitude(Double.valueOf(track.getLatitude()));
					waybillTrack.setLongitude(Double.valueOf(track.getLongitude()));
					waybillTrack.setLocations(track.getReportLoaction());
					waybillTracks.add(waybillTrack);
				}
			}else if(context.getWaybillTrack() != null)  {
				WaybillTrack waybillTrack = context.getWaybillTrack();
				if((waybillTrack.getLatitude() == null || waybillTrack.getLongitude() == null) && StringUtils.isNotBlank(waybillTrack.getLocations())) {
					try {
						AutoMapLocation location = autoMapService.coordinate(waybillTrack.getLocations());
						if (location != null) {
							waybillTrack.setLatitude(new Double(location.getLatitude()));
							waybillTrack.setLongitude(new Double(location.getLongitude()));
						}
					} catch (Exception e) {
						logger.error("获取收货地址经纬度异常 {}", context.getAddress(), e);
					}
				}
				if(context.getLatitude() != null && context.getLongitude() != null) {
					waybillTrack.setWaybillid(context.getId());
					waybillTrack.setUserid(context.getUserKey());
					waybillTrack.setCreatetime(context.getBindTime());
					waybillTracks.add(waybillTrack);
				}
				dirver = false;
			}
			if(CollectionUtils.isNotEmpty(waybillTracks)) {
				for (WaybillTrack waybillTrack : waybillTracks) {
					locationReport(context, waybillTrack, dirver);
				}
			}
		}
	}


	/**
	 * 位置上报通知
	 *
	 * @param uKey
	 * @param driverTrack
	 * @param barcodes
	 * @throws BusinessException
	 */
	@Override
	public void notifyLocationReport(Integer uKey, DriverTrack track, Collection<Barcode> barcodes) throws BusinessException {
		if (CollectionUtils.isNotEmpty(barcodes)) {
			try {
				Date cdate = new Date();
				Collection<WaybillTrack> waybillTracks = new ArrayList<WaybillTrack>();
				for (Barcode barcode : barcodes) {
					if(barcode.getGroupid() == null || barcode.getGroupid() <= 0){
						continue;
					}
					Waybill waybill = waybillService.getWaybillByCode(barcode.getBarcode());
					if (waybill != null && WaybillFettle.convert(waybill.getWaybillStatus()).ing()) {
						WaybillTrack waybillTrack = new WaybillTrack(waybill.getId(), uKey, new Double(track.getLongitude()), new Double(track.getLatitude()), track.getReportLoaction(), track.getReportTime());
						if(track!=null) {
                            StringBuilder subStr = new StringBuilder();
                            waybillTrack.setDescribe(subStr.append(track.getReportLoaction().substring(0,track.getReportLoaction().lastIndexOf("市")+1)).append("【扫描装车】").toString());
                        }
						if(waybillTrack.getLatitude() == null || waybillTrack.getLongitude() == null){
							try {
								AutoMapLocation location = autoMapService.coordinate(waybillTrack.getLocations());
								if (location != null) {
									waybillTrack.setLatitude(new Double(location.getLatitude()));
									waybillTrack.setLongitude(new Double(location.getLongitude()));
								}
							} catch (Exception e) {
								logger.error("获取收货地址经纬度异常 {}", waybillTrack.getLocations(), e);
							}
						}
						waybillTrack.setCreatetime(cdate);
						waybillTrack.setUserid(uKey);
						waybillTracks.add(waybillTrack);
					}
				}
				if(CollectionUtils.isNotEmpty(waybillTracks)){
					//trackMapper.inserts(waybillTracks);
					for (WaybillTrack waybillTrack : waybillTracks) {
						locationReport(WaybillContext.buildContext(uKey, waybillService.getWaybillById(waybillTrack.getWaybillid())), waybillTrack, true);
					}
				}
			} catch (BusinessException | ParameterException e) {
				throw e;
			} catch (Exception e) {
				logger.error("notify location report --> uKey:{} track:{}", uKey, track, e);
				throw BusinessException.dbException("更新任务单最新位置异常");
			}
		}
	}
}
