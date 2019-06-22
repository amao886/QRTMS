package com.ycg.ksh.service.impl;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/23
 */

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.util.encrypt.MD5;
import com.ycg.ksh.constant.CoreConstants;
import com.ycg.ksh.constant.LocationEvent;
import com.ycg.ksh.constant.LocationType;
import com.ycg.ksh.entity.persistent.LocationTrack;
import com.ycg.ksh.entity.persistent.activity.LotteryNote;
import com.ycg.ksh.service.persistence.activity.LotteryNoteMapper;
import com.ycg.ksh.service.api.ActivityService;
import com.ycg.ksh.service.observer.ActivityObserverAdapter;
import com.ycg.ksh.service.observer.LocationObserverAdapter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 活动相关业务
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/23
 */
@Service("ksh.core.service.activityService")
public class ActivityServiceImpl implements ActivityService, LocationObserverAdapter {

    private static final DateTimeFormatter DATETIMEFORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");


    //红包
    private static final double[] LOTTERY_REDENVELOPE = {0.02, 0.06, 0.08, 0.12, 0.15, 0.18, 0.20, 0.25};

    //积分
    private static final Integer[] LOTTERY_INTEGRAL = {1, 3, 5, 6, 8, 10, 12};

    //图片
    private static final Integer[] LOTTERY_IMAGES = {1, 2, 3, 4, 5};

    @Resource
    LotteryNoteMapper lotteryNoteMapper;


    @Autowired(required = false)
    Collection<ActivityObserverAdapter> observers;


    /**
     * 通知奖励
     *
     * @param uKey
     * @param awardType
     * @param value
     */
    private void modifyNoticeObserver(Integer uKey, Integer awardType, Long value) {
        if (CollectionUtils.isNotEmpty(observers)) {
            for (ActivityObserverAdapter observerAdapter : observers) {
                observerAdapter.notifyAwardSomething(uKey, awardType, value);
            }
        }
    }

    private String lotteryKey(Integer uKey, Integer lotteryType, Serializable objectKey) {
        if (objectKey == null) {
            objectKey = "";
        }
        return MD5.encrypt(uKey + "#" + lotteryType + "#" + objectKey.toString() + "#" + LocalDate.now().format(DATETIMEFORMATTER));
    }

    private LotteryNote lotteryNote(String lotteryKey) {
        return lotteryNoteMapper.selectByPrimaryKey(lotteryKey);
    }

    /**
     * 位置上报事件
     *
     * @param locationType
     * @param track
     * @throws BusinessException
     */
    @Override
    public void notifyLocationReport(LocationEvent locationEvent, LocationType locationType, LocationTrack track) throws BusinessException {
        //位置上报事件
        if (LocationType.ORDER == locationType) {
            Integer lotteryType = CoreConstants.LOTTERY_TYPE_DRIVER_CONTAINER;
            Serializable objectKey = "";
            if (locationEvent.scanCode()) {
                lotteryType = CoreConstants.LOTTERY_TYPE_SCAN_CODE;
                objectKey = track.getHostId();
            }
            String lotteryKey = lotteryKey(track.getUserId(), lotteryType, objectKey);
            LotteryNote lottery = lotteryNote(lotteryKey);
            if (lottery == null) {
                lotteryNoteMapper.insertSelective(new LotteryNote(lotteryKey, track.getUserId(), lotteryType, objectKey, new Date()));
            }
        }

    }

    /**
     * 抽奖
     *
     * @param uKey       用户编号
     * @param lotteryKey 抽奖资格编号
     * @param awardType  奖励类型
     * @return 奖励数值
     * @throws ParameterException 参数错误
     * @throws BusinessException  业务逻辑错误
     */
    @Override
    public Serializable lottery(Integer uKey, String lotteryKey, Integer awardType) throws ParameterException, BusinessException {
        //根据 awardType 随机不同的奖励
        //更新 LotteryNote.awardType 和 LotteryNote.awardValue
        Long value = 0L;
        if (CoreConstants.LOTTERY_AWARD_RED_ENVELOPE - awardType == 0) {//红包
            //小数点转为分存储
            int round = (int) (LOTTERY_REDENVELOPE[ThreadLocalRandom.current().nextInt(LOTTERY_REDENVELOPE.length)] * 100);
            value = Long.valueOf(round);
        }
        if (CoreConstants.LOTTERY_AWARD_INTEGRAL - awardType == 0) {//积分
            value = Long.valueOf(LOTTERY_INTEGRAL[ThreadLocalRandom.current().nextInt(LOTTERY_INTEGRAL.length)]);
        }
        if (CoreConstants.LOTTERY_AWARD_OTHER - awardType == 0) {//美女
            value = Long.valueOf(LOTTERY_IMAGES[ThreadLocalRandom.current().nextInt(LOTTERY_IMAGES.length)]);
        }
        LotteryNote lotteryNote = new LotteryNote(lotteryKey, awardType, new Date(), value);
        lotteryNoteMapper.updateByPrimaryKeySelective(lotteryNote);
        modifyNoticeObserver(uKey, awardType, value);
        return lotteryNote.getAwardValue();
    }

    /**
     * 获取抽奖资格
     *
     * @param uKey        用户编号
     * @param lotteryType 抽奖类型(1:扫码定位,2:装车定位)
     * @param objectKey   扫码定位时为订单编号，装车定位时为空
     * @return 为空表示没有抽奖资格
     * @throws ParameterException 参数错误
     * @throws BusinessException  业务逻辑错误
     */
    @Override
    public LotteryNote lotteryValidate(Integer uKey, Integer lotteryType, Serializable objectKey) throws ParameterException, BusinessException {
        LotteryNote lotteryNote = lotteryNote(lotteryKey(uKey, lotteryType, objectKey));
        if (lotteryNote != null && (lotteryNote.getAwardType() == null || lotteryNote.getAwardType() <= 0)) {
            return lotteryNote;
        }
        return null;
    }
}
