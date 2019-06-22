package com.ycg.ksh.constant;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/4
 */
public interface CoreConstants {


    int USER_LOGIN = 1;//用户登录
    int USER_REGISTER = 2;//用户注册
    int USER_BINDMOBILEPHONE = 3;//用户绑定手机号
    int USER_AUTHORITY = 4;//加载用户权限
    int USER_REFRESH = 99;//用户刷新


    int LOGIN_TYPE_DEBUG = 0;//调试登陆
    int LOGIN_TYPE_WX = 1;//微信登陆
    int LOGIN_TYPE_SCAN = 2;//扫码登陆
    int LOGIN_TYPE_ACCOUNT = 3;//账号密码登陆
    int LOGIN_TYPE_SMS = 4;//短信登陆
    int LOGIN_TYPE_REG = 5;//微信注册

    int LOGIN_CLIENT_PC = 1;//PC端登陆
    int LOGIN_CLIENT_WX = 2;//微信端登陆
    int LOGIN_CLIENT_SMS = 3;//短信登陆

    int USER_CATEGORY_DRIVER = 1;//司机
    int USER_CATEGORY_SHIPPER = 2;//发货发
    int USER_CATEGORY_CONVEY = 3;//承运方
    int USER_CATEGORY_RECEIVE = 4;//收货发

    /* 企业印章状态 */
    int COMPANY_SEAL_FETTLE_ENABLE = 1;//可用
    int COMPANY_SEAL_FETTLE_DISABLED = 2;//禁用

    /* 模板状态  */
    int TEMPLATE_FETTLE_DEFAULT = 1;//1:默认
    int TEMPLATE_FETTLE_COMMONLY = 2;//2:常用


    /* 模板字段类型  */
    int TEMPLATE_PTYPE_MUST = 1;//必填
    int TEMPLATE_PTYPE_SELECT = 2;//选填
    int TEMPLATE_PTYPE_CUSTOM = 3;//自定义


    /* 三方类型  */
    int CLIENT_TYPE_COMPANY = 0;//1;//注册企业
    //int RECEIVE_CLIENT_TYPE_CUSTOMER = 2;//关联客户

    /* 抽奖类型 */
    int LOTTERY_TYPE_SCAN_CODE = 1;//扫码上报位置
    int LOTTERY_TYPE_DRIVER_CONTAINER = 2;//装车卸货上报位置
    /* 抽奖奖励类型 */
    int LOTTERY_AWARD_RED_ENVELOPE = 1;//1:红包
    int LOTTERY_AWARD_INTEGRAL = 2;//2:积分
    int LOTTERY_AWARD_OTHER = 3;//3:其他
    /*处理类型*/
    int RE_HANDLE_TYPE_SQ = 0; //申请
    int RE_HANDLE_TYPE_JE = 1; //拒绝
    int RE_HANDLE_TYPE_TG = 2;//通过

    /* 企业客户状态 */
    int COMPANYCUSTOMER_STATUS_NORMAL = 1;//正常
    int COMPANYCUSTOMER_STATUS_DELETE = 0;//删除
    /* 企业客户类型 */
    int COMPANYCUSTOMER_TYPE_SHIPPER = 1;//发货方
    int COMPANYCUSTOMER_TYPE_RECEIVE = 2;//收货方
    int COMPANYCUSTOMER_TYPE_CONVEY = 3;//承运方
    /* 企业客户类型 */
    int COMPANYCUSTOMER_SOURCE_CREATE = 1;//1:新建
    int COMPANYCUSTOMER_SOURCE_SHARE = 2;//2:分享
    int COMPANYCUSTOMER_SOURCE_ZHIPAI = 3;//3:指派

    /* 分享目标类型 1:我分享的-发货方,2:我分享的-分享至企业,3:分享给我的-发货方,4:分享给我的-数据来源企业*/
    int SHARE_TARGET_FS = 1;//我分享的-发货方
    int SHARE_TARGET_FT = 2;//我分享的-分享至企业
    int SHARE_TARGET_RS = 3;//分享给我的-发货方
    int SHARE_TARGET_RT = 4;//分享给我的-数据来源企业

    /*用户身份切换*/
    int USER_STATUS_DRIVER = 1; //司机
    int USER_STATUS_NON_DRIVER = 2;//非司机


    //计划接单状态
    int PLAN_ACCEPT_STATUS_NOT = 0;//未接单
    int PLAN_ACCEPT_STATUS_ALREADY = 1;//已接单
    int PLAN_ACCEPT_STATUS_SUB = 2;//下级已接单

    int PLAN_ALLOCATE_STATUS_NOT = 0;//已分配
    int PLAN_ALLOCATE_STATUS_ALREADY = 1;//已分配

    //订单延迟预警
    int DELAY_WARNING_NORMAL = 1; //正常
    int DELAY_WARNING_PROBABLE = 2;//可能延迟
    int DELAY_WARNING_ALREADY = 3;//已经延迟

    //定位检查
    int POSITIONING_CHECK_NORMAL = 1; //达标
    int POSITIONING_CHECK_ALREADY = 2; //没达标
    int POSITIONING_CHECK_DEFAULT = 0;//默认未检查

    //派车状态
    int CAR_STATUS_NO = 0;//未派车
    int CAR_STATUS_ALREADY = 1;//已派车
    int CAR_STATUS_CARRY = 2;//派车完成
}
