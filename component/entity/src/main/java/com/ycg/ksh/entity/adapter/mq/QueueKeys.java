package com.ycg.ksh.entity.adapter.mq;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/16
 */
public interface QueueKeys {

    //mq数据推送交换器
    String EXCHANGE_DEFAULT = "exchange.direct.rabbit.default";
    //mq数据推送队列
    String ROUTE_OUT_KYLIN = "route.out.kylin.key";
    String ROUTE_IN_TRANSPORT = "route.in.push.key";
    String ROUTE_IN_SEARCH = "route.in.search.key";


    /*  特殊消息类型 */
    String MESSAGE_TYPE_EMAIL = "__message_type_email__";

    /*  消息类型KEY */
    String MESSAGE_TYPE_KEY = "__media_message_type__";

    /*  空消息类型 */
    String MESSAGE_TYPE_EMPTY = "";

}
