<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-计划管理-计划详情</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}element-ui/lib/theme-chalk/index.css"/>
    <style type="text/css">
        .orderWrapper {
            padding: 5px 24px 50px 30px;
            background: #fff;
            min-height: 100%;
            color: #636363;
        }

        .two {
            float: left;
            width: 32%;
        }

        .line-wrapper {
            font-size: 0;
        }

        .line-wrapper > .line-part {
            display: inline-block;
        }

        .line-wrapper > .line-part + .line-part {
            margin-left: 2%;
        }

        .line-part {
            position: relative;
            color: #333;
            font-size: 14px;
        }

        .line-part .float-tag {
            float: left;
            height: 26px;
            line-height: 26px;
            text-align: right;
        }

        .line-part .text-tag {
            margin-left: 10px;
        }

        .line-part.large-tag .text-tag {
            margin-left: 10px;
        }

        .line-part .text-tag > .input-tag,
        .line-part .text-tag > .select-tag,
        .line-part .text-tag > .p-tag {

            height: 26px;
            line-height: 26px;
            box-sizing: border-box;
            -webkit-box-sizing: border-box;
            -moz-box-sizing: border-box;
            border: 1px solid #dedede;
            text-indent: 4px;
            border-radius: 4px;
        }

        .line-part .text-tag > .p-tag {
            border: none;
            text-indent: 8px;
        }

        .line-part .text-tag > .select-tag {
            text-indent: 0;
        }

        /*====布局结束====*/
        .line-detail {
            padding-top: 12px;
            position: relative;
        }

        .line-detail .line-part .float-tag {
            text-align: left;
        }

        .line-detail .line-part .text-tag {
            margin-left: 76px;
        }

        .line-detail .travelPosition {
            position: absolute;
            top: 12px;
            right: 0;
            line-height: 26px;
            color: #31acfa;
            font-size: 14px;
            z-index: 2;
        }

        .line-detail > .line-part {
            width: 20%;
        }

        .part-wrapper {
            margin-top: 25px;
        }

        .part-wrapper .title {
            font-weight: 700;
            padding: 10px 0 10px 10px;
            background: #f5f5f5;
            color: #333;
            font-size: 15px;
            margin-bottom: 5px;
        }

        .part-wrapper .line-part {
            padding-left: 10px;
            height: 30px;
        }

        .part-wrapper .line-part .float-tag {
            text-align: left;
        }

        .part-wrapper .table-style table th {
            background: transparent;
        }

        .part-wrapper .table-style table {
            margin-top: -5px;
        }

        .edit {
            color: #31ACFA;
            margin-left: 5px;

        }


    </style>
</head>
<body data-key="${key}">
<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
<!--[if lt IE 9]>
<script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
<script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<div class="plan-content" id="plan-content">
    <%--物流跟踪-订单管理-订单详情--%>
    <%--订单ID - <span>${orderKey}</span>--%>
    <input type="hidden" value="${orderKey}" id="orderKey" data-share="${share}">
    <div class="orderWrapper" id="orderWrapper">
        <template>
            <form id="form">
                <%--发货信息--%>
                <div class="part-wrapper">
                    <div class="title">发货信息</div>
                    <div class="line-part ">
                        <div class="text-tag two" v-if="order.shipper != null">
                            <span class="float-tag">发货方:</span>
                            <p class="p-tag">{{order.shipper.customerName}}</p>
                        </div>

                        <div class="text-tag two">
                            <span class="float-tag">发货日期:</span>
                            <p class="p-tag">{{order.deliveryTime | datetime}}</p>
                        </div>
                    </div>

                    <div class="line-part">
                        <div class="text-tag two">
                            <span class="float-tag">发货计划单号:</span>
                            <p class="p-tag">{{order.planNo}}</p>
                        </div>
                        <div class="text-tag two">
                            <span class="float-tag">订单编号:</span>
                            <p class="p-tag">{{order.orderNo}}</p>
                        </div>
                    </div>
                    <div class="line-part" v-if="order.extra != null && order.extra.distributeAddress != null">
                        <div class="text-tag">
                            <span class="float-tag">发货地址：</span>
                            <p class="p-tag">{{order.extra.distributeAddress}}</p>
                        </div>
                    </div>
                </div>
                <%--承运信息--%>
                <div class="part-wrapper">
                    <div class="title">承运信息</div>
                    <div class="line-part large-tag">
                        <div class="text-tag two" v-if="order.convey != null">
                            <span class="float-tag">物流商:</span>
                            <p class="p-tag">{{order.convey.customerName}}</p>
                        </div>

                        <div class="text-tag two" v-if="order.extra != null && order.extra.conveyerName != null">
                            <span class="float-tag">联系人:</span>
                            <p class="p-tag">{{order.extra.conveyerName}}</p>
                        </div>

                        <div class="text-tag two" v-if="order.extra != null && order.extra.conveyerContact != null">
                            <span class="float-tag ">联系电话:</span>
                            <p class="p-tag">{{order.extra.conveyerContact}}</p>
                        </div>
                    </div>

                    <div class="line-part large-tag" v-if="order.transportRoute != null">
                        <div class="text-tag">
                            <span class="float-tag">运输路线:</span>
                            <p class="p-tag">{{order.transportRoute}}</p>
                        </div>

                    </div>
                    <div class="line-part large-tag">
                        <div class="text-tag two" v-if="order.collectTime != null">
                            <span class="float-tag">要求提货时间:</span>
                            <p class="p-tag">{{order.collectTime | datetime}}</p>
                        </div>

                        <div class="text-tag two" v-if="order.arrivalTime != null">
                            <span class="float-tag">要求到货时间:</span>
                            <p class="p-tag">{{order.arrivalTime | datetime}}</p>
                        </div>
                    </div>
                </div>
                <%--收货信息--%>
                <div class="part-wrapper">
                    <div class="title">收货信息</div>
                    <div class="line-part">
                        <div class="text-tag two" v-if="order.receive != null">
                            <span class="float-tag">收货客户:</span>
                            <p class="p-tag">{{order.receive.customerName}}</p>
                        </div>

                        <div class="text-tag two" v-if="order.receiverName != null">
                            <span class="float-tag">收货人:</span>
                            <p class="p-tag">{{order.receiverName}}</p>
                        </div>
                        <div class="text-tag two" v-if="order.receiverContact != null">
                            <span class="float-tag">联系方式:</span>
                            <p class="p-tag">{{order.receiverContact}}</p>
                        </div>
                    </div>
                    <div class="line-part">
                        <div class="text-tag" v-if="order.receiveAddress != null">
                            <span class="float-tag">收货地址：</span>
                            <p class="p-tag">{{order.receiveAddress}}</p>
                        </div>
                    </div>
                </div>
                <%--物料信息--%>
                <div class="part-wrapper">
                    <div class="title">货物信息</div>
                    <div class="table-style">
                        <table>
                            <thead>
                            <tr>
                                <th>物料编号</th>
                                <th width="20%">产品描述</th>
                                <th>单位</th>
                                <th>数量</th>
                                <th>箱数</th>
                                <th>体积</th>
                                <th>重量</th>
                                <th>备注</th>
                            </tr>
                            </thead>
                            <tbody v-if="order.commodities != null && order.commodities.length > 0">
                                <tr v-for="h in order.commodities">
                                    <td>{{h.commodityNo}}</td>
                                    <td>{{h.commodityName}}</td>
                                    <td>{{h.commodityUnit}}</td>
                                    <td>{{h.quantity}}</td>
                                    <td>{{h.boxCount}}</td>
                                    <td>{{h.volume | numberFixed}}</td>
                                    <td>{{h.weight | numberFixed}}</td>
                                    <td>{{h.remark}}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <%--运输信息--%>
                <div class="part-wrapper" v-if="order.extra != null">
                    <div class="title">运输信息</div>
                    <div class="line-part">
                        <div class="text-tag two" v-if="order.extra.originStation != null">
                            <span class="float-tag">始发地：</span>
                            <p class="p-tag">{{order.extra.originStation}}</p>
                        </div>
                    </div>
                    <div class="line-part">
                        <div class="text-tag two" v-if="order.extra.arrivalStation != null">
                            <span class="float-tag">目的地:</span>
                            <p class="p-tag">{{order.extra.arrivalStation}}</p>
                        </div>
                    </div>
                    <div class="line-part">
                        <div class="text-tag two" v-if="order.extra.careNo != null">
                            <span class="float-tag">车牌号:</span>
                            <p class="p-tag">{{order.extra.careNo}}</p>
                        </div>

                        <div class="text-tag two" v-if="order.extra.driverName != null">
                            <span class="float-tag">司机姓名:</span>
                            <p class="p-tag">{{order.extra.driverName}}</p>
                        </div>

                        <div class="text-tag two" v-if="order.extra.driverContact != null">
                            <span class="float-tag">司机电话:</span>
                            <p class="p-tag">{{order.extra.driverContact}}</p>
                        </div>
                    </div>
                    <div class="line-part">

                        <div class="text-tag two" v-if="order.extra.startStation != null">
                            <span class="float-tag">发站:</span>
                            <p class="p-tag">{{order.extra.startStation}}</p>
                        </div>

                        <div class="text-tag two" v-if="order.extra.endStation != null">
                            <span class="float-tag">到站:</span>
                            <p class="p-tag">{{order.extra.endStation}}</p>
                        </div>
                    </div>
                </div>
                <%--财务信息--%>
                <div class="part-wrapper" v-if="order.extra != null">
                    <div class="title">财务信息</div>
                    <!--订单备注-->
                    <div class="line-part large-tag">
                        <div class="text-tag two" v-if="order.extra.income != null">
                            <span class="float-tag ">收入:</span>
                            <p class="p-tag">{{order.extra.income}}元</p>
                        </div>
                        <div class="text-tag two" v-if="order.extra.expenditure != null">
                            <span class="float-tag">运输成本:</span>
                            <p class="p-tag">{{order.extra.expenditure}}元</p>
                        </div>
                    </div>


                </div>
                <%--其他信息--%>
                <div class="part-wrapper">
                    <div class="title">其他</div>
                    <!--订单备注-->
                    <div class="line-part large-tag" v-if="order.remark != null">
                        <div class="text-tag two">
                            <span class="float-tag">备注1:</span>
                            <p class="p-tag">{{order.remark}}</p>
                        </div>
                    </div>
                    <div class="line-part large-tag"  v-if="order.customDatas != null">
                        <div class="text-tag two" v-for="c in order.customDatas" v-if="c != null">
                            <span class="float-tag">{{c.customName}}:</span>
                            <p class="p-tag">{{c.customValue}}</p>
                        </div>
                    </div>
                </div>
            </form>
        </template>
    </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
<script>
    $(document).ready(function () {
        new Vue({
            el: '#plan-content',
            data: {
                id: $('body').data('key'),
                order: {}
            },
            created: function () {
                this.loadDetail();
            },
            methods: {
                loadDetail:function(){
                    var _this = this;
                    $.util.json('/enterprise/plan/detail/'+ _this.id, null, function (data) {
                        if (data.success) { //处理返回结果
                            _this.order = data.result;
                        } else {
                            $.util.error(data.message);
                        }
                    });
                }
            }
        })
    });
</script>
</html>