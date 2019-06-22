/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-06 15:28:28
 */
package com.ycg.ksh.common.constant;

/**
 * 通用常量外
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-06 15:28:28
 */
public interface Constant {
    //常量命名务必要要规范，写好文档注释

    /**
     * 默认字符编码
     */
    String CHARACTER_ENCODING_UTF8 = "utf8";
    String CHARACTER_ENCODING_GBK = "gbk";

    /**
     * http请求操作状态
     */
    int WRONG_CODE = 9999;// 异常

    int RIGHT_CODE = 200;// 成功

    int RIGHT_BIND_CODE = 202;// 已绑定
    int DELIVER_BIND_CODE = 204;// 已送达
    int END_BIND_CODE = 205;// 已结束

    int PARAM_CODE = 400;// 失败

    int NO_PERMISSION = 405;// 没有权限

    int INTNUM_REPORT = 999;// 最大回单值

    String SUCCESS_MSG = "操作成功";

    String SIGN_MSG = "订单已签收,不要重复操作";

    String FAIL_MSG = "操作失败";

    String FAIL_MSG_KEY = "mas";

    String OPER_REPEAT = "账号重复";

    String SYSTEM_ERROR = "系统异常";

    String PARAMS_ERROR = "非法参数";

    String QUERY_SUCCESS = "查询成功";

    String QUERY_FAIL = "查询失败";

    String GOODS_PARAMS_ERROR = "货品信息为空";

    String LOCATION_FAIL = "经纬度获取失败";

    String LOCATION_EMPTY = "该车辆的位置信息不存在";

    String ADDRESSTOLOCATION_FAIL = "地理编码失败";

    String LOCATIONTOADDRESS_FAIL = "逆地理编码失败";

    String LOGIN_SUCCESS = "登录成功";

    String LOGIN_FAIL = "登录失败";

    String NOT_LOGIN = "未登录，请先登录";

    String USERE_NAME_ERROR = "用户不存在！";

    String PASSWORD_ERROR = "密码错误";

    String CELL_PHONE_FORMAT_ERROR = "手机格式错误";

    String ROLE_NOTMATE = "权限不匹配";

    String CONNECT_ERROR = "连接异常";

    String BARCODE_NULL = "条码数据不存在，无法保存";

    String BARCODE_BIND = "条码未绑定，请先绑定条码";

    String NAMEORPASS_ERROR = "用户名或密码错误";

    String USERLOGIN_PARAMS_ERROR = "请重新输入信息或检查用户类型是否正确";

    String WAIT_A_JIFF = "服务器开小差了，请稍后重试";

    String HAVE_NO_PERMISSION = "您没有此权限";

    String INVALID_BARCODE = "无效条码";

    String WAYBILL_DELIVERED = "任务单已送达";

    String WAYBILL_END = "该运单已完成";

    String JSONERROR = "JSON转换错误";

    String IOERROR = "IO流读写错误";

    String INDEXOUTERROR = "数组下标越界";

    String CLASSCASTERROR = "类转换错误";

    String PARSEERROR = "日期转换错误";

    String UNLOAD_IMG_ERROR = "上传图片信息为空";

    String UNLOAD_IMG_FAIL = "上传图片失败";
    String UNLOAD_IMG_INFO = "上传图片成功";
    String IMG_non_existent = "图片不存在";

    String JM_EXCEPTION = "解码异常";

    String COMMIT_ERROR = "提交失败";
    String COMMIT_INFO = "提交成功";
    String UKEY_NOTNULL_INFO = "用户编号不能为空";

    String DATA_NULL = "暂无数据";

    String WAYBILL_ALL_RIGHT_UPLOAD = "运单在卸货状态，不能定位";

    String LOAD_TYPE = "LOAD";

    String UNLOAD_TYPE = "UNLOAD";

    int SERVICE_ID = 129294;

    /**
     * 坐标状态
     */
    int COORD_TYPE = 1;// 1：GPS经纬度坐标2：国测局加密经纬度坐标  3：百度加密经纬度坐标。

    /**
     * 货物操作状态
     */
    int LOAD = 1;// 装货

    int DISCHARGE = 2;// 卸货

    int SIGN = 3;// 签收

    int TRANSIT = 4;// 运输中

    /**
     * 用户角色状态
     */
    int BACK_MANAGER = 1;// 管理员

    int BACK_OPERATOR = 2;// 操作员

    int BACK_AUDITOR = 3;// 审核员

    // 证件管理状态
    int STATUS0 = 0;// 待审核

    int STATUS1 = 1;// 可用

    int STATUS2 = 2;// 停用

    int STATUS3 = 3;// 不合格

    int STATUS4 = 4;// 过期

    String LICENCETYPE0 = "身份证";

    String LICENCETYPE1 = "驾驶证";

    String LICENCETYPE2 = "从业资格证";

    String LICENCETYPE3 = "运营许可证";

    String LICENCETYPE4 = "车辆行驶证";

    String LICENCETYPE5 = "车辆运输保险卡卡号";

    String LICENCETYPE6 = "安全协议";

    String LICENCETYPE7 = "入场须知";

    String LICENCETYPE8 = "其他";

    // 订车管理状态
    int BOOKSTATUS10 = 10;// 待分配

    int BOOKSTATUS20 = 20;// 待派车

    int BOOKSTATUS30 = 30;// 已取消

    int BOOKSTATUS40 = 40;// 派车中

    int BOOKSTATUS50 = 50;// 已派完

    // 货物状态管理
    int CARSTATUS100 = 100;// 已到场

    int CARSTATUS200 = 200;// 在途

    int CARSTATUS300 = 300;// 已到货

    int CARSTATUS400 = 400;// 已签收

    int CARSTATUS500 = 500;// 已回单

    int CARSTATUS600 = 600;// 已取消

    String AJAX_STATUS_SUCCESS = "success";

    String AJAX_STATUS_ERROR = "error";

    String UNLOGIN = "unlogin";

    String AJAX_STATUS_FAIL = "fail";

    /**
     * 项目组成员状态
     */
    int PGROUP_MSTATUS_EFFECTIVE = 10;//有效组员
    int PGROUP_MSTATUS_INVALID = 20;//无效组员

    //回单审核状态Verify
    int VERIFYSTRTUS_APPROVAL = 1;//合格
    int VERIFYSTRTUS_REJECT = 0;//不合格


    //电子围栏开关GRATESATUS
    int GRATESATUS_ON = 1;//开
    int GRATESATUS_OFF = 0;//关

    /**
     * 地址类型
     */
    int ADDRESS_TYPE_COMMON = 1;//普通地址
    int ADDRESS_TYPE_OFTEN = 2;//常用地址

    /**
     * 条码状态
     */
    int BARCODE_STATUS_NO = 10;//未绑定
    int BARCODE_STATUS_BIND = 20;//已绑定

    /**
     * 条码资源申请状态
     */
    int APPRES_STATUS_NO = 0;//已申请未生成
    int APPRES_STATUS_BUILD = 1;//已生成可下载

    /**
     * 任务单状态
     */
    int WAYBILL_STATUS_WAIT = 10;//等待发货(待绑定)
    int WAYBILL_STATUS_BIND = 20;//已发货(绑定)
    int WAYBILL_STATUS_ING = 30;//运输中
    int WAYBILL_STATUS_ARRIVE = 35;//已送达
    int WAYBILL_STATUS_RECEIVE = 40;//收货


    /**
     * 回单审核状态
     */
    int RECEIPT_VERIFY_STATUS_NO = 1;//未上传
    int RECEIPT_VERIFY_STATUS_WAIT = 2;//待审核
    int RECEIPT_VERIFY_STATUS_ING = 3;//审核中
    int RECEIPT_VERIFY_STATUS_ALREADY = 4;//已审核

    /**
     * 纸质回单状态
     */
    int PAPERY_RECEIPT_STATUS_NOT = 0;//未回收
    int PAPERY_RECEIPT_STATUS_RECYCLED = 1;//已回收
    int PAPERY_RECEIPT_STATUS_SEND = 2;//已送客户
    int PAPERY_RECEIPT_STATUS_SUPPLIER = 3;//已退供应商
    int PAPERY_RECEIPT_STATUS_RETURN = 4;//客户退回

    /**
     * 任务来源
     */
    int WAYBILL_HAVETYPE_GROUP = 0;//来自项目组
    int WAYBILL_HAVETYPE_SCAN = 1;//来自扫码
    int WAYBILL_HAVETYPE_SHARE = 2;//来自分享

    /**
     * 运单是否延迟
     **/
    int WAYBILL_DAY_WAIT = 999;// 未处理的
    int WAYBILL_DAY_NOTYET = 0;// 未到已延迟的
    int WAYBILL_DAY_DELAYED = 1;//已延迟
    int WAYBILL_DAY_NOT_DELAYED = 2;//未延迟
    /**
     * 确认送达的方式
     */
    int CONFIRM_DELIVERY_WAY_BACK = 1;//后台操作
    int CONFIRM_DELIVERY_WAY_RECEIPT = 2;//上传回单
    int CONFIRM_DELIVERY_WAY_FENCE = 3;//电子围栏

    /**
     * 回单待处理类型
     */
    int RECEIPT_VERIFY_STATUS_NOT = 1;//未上传
    int RECEIPT_VERIFY_STATUS_NOTVERIFY = 2;//已上传-全部未审核
    int RECEIPT_VERIFY_STATUS_PARTVERIFY = 3;//已上传-部分未审核
    int RECEIPT_VERIFY_STATUS_UNQUALIFY = 4;//已审核-有不合格
    int RECEIPT_VERIFY_STATUS_HAVENOT = 5;//已上传-有未审核的

    /**
     * 是否添加到收货人员列表
     */
    int CUSTOMER_TO_ADD = 1; // 添加收货人员

    int STARTROW = 3;

    /**
     * 图片存储类型
     */
    int IMAGE_TYPE_DELIVERY = 1;//送货单图片
    int IMAGE_TYPE_TRANSITION_RECEIPT = 2;//过渡期回单图片
    int IMAGE_TYPE_TRANSITION_EXCEPTION = 3;//过渡期异常图片
    int IMAGE_TYPE_RECEIPT = 4;//回单上传图片

    //0:已申请未生成, 1:准备生成,2:已生成未下载,3:已经下载过
    /**
     * 条码申请状态
     */
    int CODE_RES_APPLY = 0;//已申请未生成
    int CODE_RES_READY = 1;//准备生成
    int CODE_RES_BUILDED = 2;//已生成未下载
    int CODE_RES_DOWNLOAD = 3;//已经下载过


    /**
     * 1:收货客户  2:发货客户
     **/
    int CUSTOMER_TYPE_SEND = 2;
    int CUSTOMER_TYPE_RECEIVER = 1;


    /**
     * 发货/收货信息处理方式
     **/
    int CUSTOMER_SELECT_BYID = 10;//根据ID查询客户信息表
    int CUSTOMER_CLIENT_NONE = 21;//客户端传参,服务端不保存
    int CUSTOMER_CLIENT_SAVE = 22;//客户端传参,有ID服务端保存更新


    /**
     * 协同，1：我的待办事项   2：进度跟踪
     **/
    int BE_REMINDERS_CODE = 1;

    int REMINDERS_CODE = 2;

    //回单扫描
    public static final String ABNORMAL_STATE_OF_RECEIPT1 = "当前状态为[%s],不能进行[%s]操作";
    public static final String ABNORMAL_STATE_OF_RECEIPT2 = "单号[%s]当前状态为[%s],不能进行[%s]操作";

    public static final String SCAN_CODE_SUCCESS = "扫码成功";

    //回单状态批量修改提示
    public static final String MODIFY_RECEIPT_STATUS = "当前选中任务单存在当前状态不可修改的任务单，部分已提交";

    /**
     * 企业管理 - 个人认证
     */
    int PERSONA_AUTH = 1;
    //app 帮助页 启动入口
    String APPINDEXPAGE = "/apphelper/index.html";

    /**
     * 个人印章类型
     */
    int PERSONAL_CHAPTER_NUM = 1; //个人章

    int HANDWRITTEN_SIGNATURE_NUM = 2; //手写签名

    /**
     * 财务数据类型
     */
    int FINANCE_TYPE_ORDER = 1;// 订单


    /*发货通知配置前缀*/
    String SHIPPING_NOTICE_DEFAULT = "FHTZ";
}
