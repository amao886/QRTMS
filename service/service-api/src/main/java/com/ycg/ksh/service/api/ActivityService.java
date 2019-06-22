package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.entity.persistent.activity.LotteryNote;

import java.io.Serializable;

/**
 * 活动相关业务
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/23
 */
public interface ActivityService {


    /**
     * 抽奖
     * @param uKey  用户编号
     * @param lotteryKey 抽奖资格编号
     * @param awardType 奖励类型
     * @return 奖励数值
     * @throws ParameterException  参数错误
     * @throws BusinessException   业务逻辑错误
     */
    Serializable lottery(Integer uKey, String lotteryKey, Integer awardType) throws ParameterException, BusinessException;

    /**
     * 获取抽奖资格
     * @param uKey 用户编号
     * @param lotteryType 抽奖类型(1:扫码定位,2:装车定位)
     * @param objectKey 扫码定位时为订单编号，装车定位时为空
     * @return 为空表示没有抽奖资格
     * @throws ParameterException  参数错误
     * @throws BusinessException   业务逻辑错误
     */
    LotteryNote lotteryValidate(Integer uKey, Integer lotteryType, Serializable objectKey) throws ParameterException, BusinessException;
}
