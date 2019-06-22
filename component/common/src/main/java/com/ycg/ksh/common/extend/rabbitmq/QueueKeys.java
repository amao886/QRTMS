package com.ycg.ksh.common.extend.rabbitmq;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/16
 */
public interface QueueKeys {

    //mq数据推送交换器
    String EXCHANGE_DIRECT = "exchange.direct.rabbit.default";

    //mq数据推送队列
    String ROUTE_OUT_KYLIN = "route.out.kylin.key";
    String ROUTE_IN_DIRECT = "route.in.direct.key";
    String ROUTE_IN_DIRECT_NET = "route.in.direct.net";
    String ROUTE_IN_DIRECT_CORE = "route.in.direct.core";
    String ROUTE_IN_DIRECT_COLLECT = "route.in.direct.collect";
    String ROUTE_IN_DIRECT_API = "route.in.direct.api";


    /*  特殊消息类型 */
    String MESSAGE_TYPE_EMAIL = "__message_type_email__";
    String MESSAGE_TYPE_ORDER_TIMEOUT = "__message_type_order_timeout__";//订单签收超时
    String MESSAGE_TYPE_GENERATE_BARCODE = "__message_type_generate_barcode__";//生成条码号

    /*  消息类型KEY */
    String MESSAGE_TYPE_KEY = "__media_message_type__";

    /*  空消息类型 */
    String MESSAGE_TYPE_EMPTY = "";

}
