package com.ycg.ksh.core.util;

import java.time.format.DateTimeFormatter;

/**
 * 常量
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/04 0004
 */
public interface Constants {

    DateTimeFormatter YMDHMS = DateTimeFormatter.ofPattern("yyyy-MM-dd :mm:ss");

    //车辆类型
    int CAR_TYPE_XS = 1;// 厢式车
    int CAR_TYPE_PB = 2;//平板车
    int CAR_TYPE_GL = 3;//高栏车
    int CAR_TYPE_ZL = 4;//中栏车
    int CAR_TYPE_DL = 5;// 低栏车
    int CAR_TYPE_BFB = 6;//半封闭
    int CAR_TYPE_GZ = 7;//罐装车
    int CAR_TYPE_LC = 8;//冷藏车
    int CAR_TYPE_JZ = 9;//集装车
    int CAR_TYPE_ZX = 10;//自卸车
    int CAR_TYPE_WXP = 11;//危险品车
    int CAR_TYPE_GC = 12;//挂车
    int CAR_TYPE_QT = 13;//其他

    //路线类型
    int ROUTE_TYPE_DT = 1;// 短途
    int ROUTE_TYPE_CT = 2;//长途

    //等货信息状态
    int AWAIT_STAUS_EFFECTIVE = 1;//有效
    int AWAIT_STAUS_INVALID = 2;//无效

    //邀请处理结果
    int INVITE_RESULT_WAIT = 1;//等待处理
    int INVITE_RESULT_AGREE = 2;//同意
    int INVITE_RESULT_REFUSE = 3;//拒绝


    //企业司机状态
    int CDRIVER_STATUS_WAIT = 1;//等待审核
    int CDRIVER_STATUS_QUALIFIED = 2;//合格
    int CDRIVER_STATUS_FAILED = 3;//不合格

    //现场管理
    int ARRIVAL_TYPE_CONSISTENT = 1;//派车信息一致
    int ARRIVAL_TYPE_INCONSISTENT = 2;//派车信息不一致
    int ARRIVAL_TYPE_NO = 3;//无派车信息


    //合同类型
    int CONTRACT_TYPE_CLIENTELE = 1;//客户合同
    int CONTRACT_TYPE_DOWNSTREAM = 2;//供应商合同(下游)

    //合同审核状态:  0：未审核  1：审核不通过  2：审核通过
    int CONTRACT_VERIFY_WAIT = 0;//待审核
    int CONTRACT_VERIFY_REFUSE = 1;//审核不通过
    int CONTRACT_VERIFY_PASS = 2;//审核通过

    //货物性质: 0：不限 1：重货 2：轻货
    int COMMODITY_CATEGORY_UNCERTAIN = 0;//不限
    int COMMODITY_CATEGORY_WEIGHT = 1;//重货
    int COMMODITY_CATEGORY_LIGHT = 2;//轻货

    //货物单位: 0：吨 1：千克/公斤 2：方 3：件
    int COMMODITY_UNIT_TON = 0;//吨/元
    int COMMODITY_UNIT_KG = 1;//千克/元
    int COMMODITY_UNIT_M3 = 2;//立方米/元
    int COMMODITY_UNIT_ITEM = 3;//件/元

    //核算运费方式: 0：重量 1：体积 2：数量
    int FARE_TYPE_WEIGHT = 0;//重货
    int FARE_TYPE_LIGHT = 1;//轻货
    int FARE_TYPE_NUMBER = 2;//数量

    //计价方式--------------1:固定价, 2:阶梯价格
    int FARE_CATEGORY_FIXED = 1;//固定价
    int FARE_CATEGORY_GRADE = 2;//阶梯价格

    //城市级别: 0：不限 1：一级/省会 2：二级/地级市 3：三级/县、区级
    int CITY_LEVEL_0 = 0;//不限
    int CITY_LEVEL_1 = 1;//一级/省会
    int CITY_LEVEL_2 = 2;//二级/地级市
    int CITY_LEVEL_3 = 2;//三级/县、区级

    //计价方式 重量，体积，数量,整车
    int PRICING_DIMENSION_WEIGHT = 0;
    int PRICING_DIMENSION_VOLUME = 1;
    int PRICING_DIMENSION_QUANTITY = 2;
    int PRICING_DIMENSION_VEHICLE = 3;

    String QUANTITY_STR = "零担-按数量核算运费";
    String VOLUME_STR = "零担-按体积核算运费";
    String VEHICLE_STR = "整车";
    String WEIGHT_STR = "零担-按重量核算运费";

}
