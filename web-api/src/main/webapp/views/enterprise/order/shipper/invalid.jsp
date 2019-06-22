<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-电子回单管理-作废回单</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/receiptmanage.css?times=${times}"/>
    <style type="text/css">
        .cactive {
            color: #fff !important;
            cursor: default;
            background-color: #337ab7 !important;
            border-color: #337ab7 !important;
        }

        .pagination a {
            cursor: pointer;
        }

        .model-base-box {
            padding-bottom: 20px;
        }
    </style>
</head>
<body>
<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
<!--[if lt IE 9]>
<script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
<script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<div class="track-content">
    <form id="shipper-manage-form" action="${basePath}/enterprise/order/receipt/invalid/search" method="post">
        <div class="content-search">
            <div class="clearfix">
                <div class="fl col-min">
                    <input type="text" class="tex_t" name="likeString" placeholder="送货单号/收货客户" value="${search.likeString}"/>
                </div>
                <div class="fl col-min">
                    <a href="javascript:;" class="content-search-btn search-btn" style="top:50px;">查询</a>
                </div>
            </div>
        </div>
    </form>

    <!-- 表格部分 -->
    <div class="table-style" id="list-manage">
        <template>
            <table>
                <thead>
                <tr>
                    <th width="50">送货单号</th>
                    <th width="55">订单编号</th>
                    <th width="60">客户名称</th>
                    <th width="60">物流商</th>
                    <th width="25">物料数</th>
                    <th width="25">台数</th>
                    <th width="25">件数</th>
                    <th width="25">体积</th>
                    <th width="25">重量</th>
                    <th width="32">收货人</th>
                    <th width="50">联系方式</th>
                    <th width="100">收货地址</th>
                    <th width="27">状态</th>
                    <th width="60">作废时间</th>
                    <th width="25">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr v-if="collection.length <= 0">
                    <td colspan="15" style="text-align: center;">暂无数据</td>
                </tr>
                <tr v-for="o in collection" v-if="collection.length > 0">
                    <td>{{o.deliveryNo}}</td>
                    <td>{{o.orderNo}}</td>
                    <td>{{o.receive.companyName}}</td>
                    <td>{{o.convey.companyName}}</td>
                    <td>{{o.count}}</td>
                    <td>{{o.boxCount}}</td>
                    <td>{{o.quantity}}</td>
                    <td>{{o.volume | numberFixed}}</td>
                    <td>{{o.weight | numberFixed}}</td>
                    <td>{{o.receiverName}}</td>
                    <td>{{o.receiverContact}}</td>
                    <td>{{o.receiveAddress}}</td>
                    <td>
                        <span>已作废</span>
                    </td>
                    <td>
                        <span>{{o.invalidTime | datetime}}</span>
                    </td>
                    <td>
                        <a class="linkBtn" @click="viewMore(o.id)">查看</a>
                    </td>
                </tr>
                </tbody>
            </table>
            <div class="page-item">
                <div id="layui_div_page" class="col-sm-12 center-block" v-if="pages > 1">
                    <ul class="pagination" style="margin-bottom: 0;">
                        <li class="disabled"><span><span aria-hidden="true">共{{total}}条,每页{{pageSize}}条</span></span>
                        </li>
                        <li v-if="!isFirstPage"><a v-on:click="flip(1)">首页</a></li>
                        <li v-if="!isFirstPage"><a v-on:click="flip(prePage)">上一页</a></li>
                        <li v-for="p in navigatepageNums">
                            <span v-if="p == pageNum" class="cactive">{{p}}</span>
                            <a v-if="p != pageNum" v-on:click="flip(p)">{{p}}</a>
                        </li>
                        <li v-if="!isLastPage"><a v-on:click="flip(nextPage)">下一页</a></li>
                        <li v-if="!isLastPage"><a v-on:click="flip(pages)">尾页</a></li>
                        <li class="disabled"><span><span>共{{pages}}页</span></span></li>
                    </ul>
                </div>
            </div>
        </template>
    </div>
</div>
<!--查看详情-->
<div class="view_more_panel" style="display: none" id="query-detail">
    <div class="model-base-box blackColor view_more" style="width:1020px;">
        <div class="pancelCon clearfix">
            <div class="h4">产品配送单</div>
            <div class="infoNo">
                <p>订单编号：{{orderNo}}</p>
                <p>日期：{{deliveryTime | date}}</p>
            </div>
            <ul class="pancelUl clearfix">
                <!-- 模版 1 -->
                <li><label>配送单号：</label><span>{{deliveryNo}}</span></li>
                <li><label>订单客户：</label><span v-if="receive != null">{{receive.companyName}}</span></li>
                <li><label>收货客户：</label><span v-if="receive != null">{{receive.companyName}}</span></li>
                <li><label>收货人员：</label><span>{{receiverName}}</span></li>
                <li><label>物流商名称：</label><span v-if="convey != null">{{convey.companyName}}</span></li>
                <li><label>联系方式：</label><span>{{receiverContact}}</span></li>
                <li><label>收货地址：</label><span>{{receiveAddress}}</span></li>
                <li><label>订单备注：</label><span>{{remark}}</span></li>
                <li>
                    <label>订单明细：</label>
                    <div class="tableCon">
                        <table>
                            <thead>
                            <tr>
                                <th width="90">物料编号</th>
                                <th width="190">物料描述/物料名称</th>
                                <th width="80">单位</th>
                                <th width="80">数量</th>
                                <th width="80">箱数</th>
                                <th width="80">体积</th>
                                <th width="80">重量</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="c in commodities">
                                <td>{{c.commodityNo}}</td>
                                <td>{{c.commodityName}}</td>
                                <td>{{c.commodityUnit}}</td>
                                <td>{{c.quantity}}</td>
                                <td>{{c.boxCount}}</td>
                                <td>{{c.volume}}</td>
                                <td>{{c.weight}}</td>
                            </tr>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="3">合计</td>
                                <td>{{quantity}}</td>
                                <td>{{boxCount}}</td>
                                <td>{{volume | numberFixed}}</td>
                                <td>{{weight | numberFixed}}</td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </li>
            </ul>


            <!-- 模版 2 -->
            <div class="clearfix">
                <div class="abnormalSign">
                    <p v-if="exception != null">
                        异常:{{exception.content}}
                    </p>
                </div>
                <!--
		    	<ul class="operaterList clearfix">
		    		<li><label>打印人：</label><span></span></li>
		    		<li><label>备货员：</label><span></span></li>
		    		<li><label>复核员：</label><span></span></li>
		    	</ul>
		    	-->
                <div class="tableCon signPos">
                    <p class="fb">物流商签字盖章：{{convey.companyName}}</p>
                    <p style="text-align:left;padding-bottom: 20px;">
                        温馨提示：物流配送责任以签收单据为主要依据，故请您严格按照收货标准验收签字、盖章确认。如有异常情况，请及时致电九阳物流：</p>
                    <p class="col-3">
                        <span>杭州RDC：0571-12345678</span>
                        <span>济南RDC：0531-12345678</span>
                        <span>广州RDC：0757-12345678</span>
                    </p>
                    <p class="fb">实收件数(大写)：{{realNumber}}</p>
                    <p class="fb">收货单位业务章：</p>
                    <p>我已确认此配送单信息与实际接收货物数量、型号、配送标识（防窜货码）一致。</p>

                    <div class="signIcon conveyrSign" v-if="convey.sealDate !=null"><img :src="conveyUrl"></div>
                    <div class="signIcon receiveSign" v-if="receive.sealDate !=null"><img :src="receiveUrl"></div>
                </div>
            </div>
        </div>
    </div>

</div>

</body>
<%@ include file="/views/include/floor.jsp" %>
<script>
    var pageNum = 1;
    $(document).ready(function () {
        function requestData(id, callBack) {
            //var vue = new Vue({ el: htmlElement, data : {} });
            $.util.json(base_url + '/enterprise/order/detail/cancle/' + id, null, function (data) {
                if (data.success) {//处理返回结果
                    if (callBack != null) {
                        var result = data.reslut;
                        result.pathPrefix = data.pathPrefix;
                        callBack(result);

                    }
                } else {
                    $.util.error(data.message);
                }
            });
            //return vue;
        }

        function createVue(element, result) {
            return new Vue({
                el: element,
                data: result,
                computed: {
                    conveyUrl: function () {
                        return this.pathPrefix + "/" + this.convey.sealDate
                    },
                    receiveUrl: function () {
                        return this.pathPrefix + "/" + this.receive.sealDate
                    },
                }
            });
        }
        var listManage = new Vue({
            el: '#list-manage',
            data: {
                collection: [],
                pageNum: 0,
                pageSize: 0,
                navigatepageNums: [],
                total: 0,
                pages: 0,
                prePage: 0,
                nextPage: 0,
                isFirstPage: false,
                isLastPage: false
            },
            methods: {
                flip: function (num) {
                    searchManage(num);
                },//翻页
                viewMore: function (id) {//查看
                    //window.location.href = base_url + "/enterprise/order/detail/cancle/"+ id;
                    $.util.form('回单详情', $(".view_more_panel").html(), function (model) {
                        var $panel = this;
                        $panel.$contentPane.css("border", "1px solid #000");
                        requestData(id, function (result) {
                            createVue($panel.$body.find('.model-base-box')[0], result);
                        });
                    })
                }
            }
        });
        var searchManage = function (num) {
            if (!num) {
                num = pageNum;
            } else {
                pageNum = num;
            }
            var parmas = {num: num, size: 10};
            $('#shipper-manage-form').find('input, select').each(function (index, item) {
                parmas[item.name] = item.value;
            });
            $.util.json($('#shipper-manage-form').attr('action'), parmas, function (data) {
                if (data.success) {//处理返回结果
                    listManage.collection = data.page.collection;
                    listManage.pageNum = data.page.pageNum;
                    listManage.pageSize = data.page.pageSize;
                    listManage.navigatepageNums = data.page.navigatepageNums;
                    listManage.total = data.page.total;
                    listManage.pages = data.page.pages;
                    listManage.prePage = data.page.prePage;
                    listManage.nextPage = data.page.nextPage;
                    listManage.isFirstPage = data.page.isFirstPage;
                    listManage.isLastPage = data.page.isLastPage;
                } else {
                    $.util.error(data.message);
                }
            });
        };
        //提交事件
        $(".search-btn").click(function () {
            searchManage(1);
        });
        searchManage(1);
    });
</script>
</html>