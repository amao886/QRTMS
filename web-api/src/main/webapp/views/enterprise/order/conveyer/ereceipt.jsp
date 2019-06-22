<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-运输管理-承运管理</title>
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

        .span-radio-checkbox {
            text-align: left;
            padding-left: 20px;
            font-weight: normal;
        }

        .jconfirm .jconfirm-box div.jconfirm-content-pane.no-scroll {
            overflow: hidden;
        }

        .jconfirm .jconfirm-box div.jconfirm-content-pane.no-scroll .jconfirm-content {
            overflow: hidden;
        }

        .radio-wrapper {
            font-size: 0;
        }

        .radio-item {
            position: relative;
            width: 30%;
            display: inline-block;
            vertical-align: top;
            font-size: 14px;
        }

        .radio-item + .radio-item {
            margin-left: 5%;
        }

        .radio-item .text {
            display: inline-block;
            padding: 4px 0px;
            width: 100%;
            font-weight: 400;
            background: #e6e6e6;
            color: #636363;
            text-align: center;
            cursor: pointer;
        }

        .radio-item > input[type="radio"] {
            position: absolute;
            top: 1px;
            left: -9999px;
        }

        .radio-item.active .text {
            color: #fff;
            background: #31acfa;
        }

        .col-min-new {
            width: 15%;
        }

        .col-min-new:nth-of-type(1) {
            width: 24%;
        }

        .col-min-new:nth-last-of-type(1) {
            width: 6%;
        }

        .col-min-new > .tex_t {
            width: 100%;
        }

        .content-search .content-search-btn {
            margin-left: 0;
        }

        .normalSts, .abnormalSts {
            display: inline-block;
            padding: 2px 6px;
        }

        .normalSts {
            background: #99cc00;
        }

        .abnormalSts {
            background: #ff9900;
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
    <form id="shipper-manage-form" action="${basePath}/enterprise/order/receipt/convey/search" method="get">
        <div class="content-search">
            <div class="clearfix">
                <div class="fl col-min-new radio-wrapper">
                    <div class="radio-item active">
                        <label class="text" for="scan1">全部</label>
                        <input type="radio" name="receiptFettle_r" class="receiptFettle_search" id="scan1"
                               checked="checked" value="">
                    </div>
                    <div class="radio-item">
                        <label class="text" for="scan2">待签署</label>
                        <input type="radio" name="receiptFettle_r" class="receiptFettle_search" id="scan2" value="1">
                    </div>
                    <div class="radio-item">
                        <label class="text" for="scan3">已签署</label>
                        <input type="radio" name="receiptFettle_r" class="receiptFettle_search" id="scan3" value="2">
                    </div>
                </div>
                <div class="fl col-min-new">
                    <select name="timeType" class="tex_t" id="timeType" val="${search.timeType}">
                        <option value="3">近三天发货任务</option>
                        <option value="7">近一周发货任务</option>
                        <option value="30">近一个月发货任务</option>
                        <option value="-1">全部日期</option>
                    </select>
                </div>
                <div class="fl col-min-new receiptFettle">
                    <select name="receiptFettle" class="tex_t" id="receiptFettle" placeholder="回单状态"
                            val="">
                        <option value="1">物流商未签</option>
                        <option value="3">收货方未签</option>
                        <option value="5">全部已签</option>
                        <option value="">全部</option>
                    </select>
                </div>
                <div class="fl col-min-new">
                    <input type="text" class="tex_t" name="likeString" placeholder="送货单号/收货客户"
                           value="${search.likeString}"/>
                </div>
                <div class="fl col-min-new">
                    <a href="javascript:;" class="content-search-btn search-btn">查询</a>
                </div>
            </div>
        </div>
    </form>

    <!-- 表格部分 -->
    <div class="table-style" id="list-manage">
        <template>
            <div class="handle-box">
                <button class="layui-btn layui-btn-normal" @click="multiple">批量签署</button>
                <button class="layui-btn layui-btn-normal" @click="download">下载回单</button>
            </div>
            <table>
                <thead>
                <tr>
                    <th width="10">
                        <input type="checkbox" id="checkAll" @change="selectAll"/>
                    </th>
                    <th width="45">送货单号</th>
                    <%--<th width="35">订单编号</th>--%>
                    <th width="30">发货日期</th>
                    <th width="45">发货方</th>
                    <th width="45">收货客户</th>
                    <th width="18">收货人</th>
                    <th width="27">联系方式</th>
                    <th width="80">收货地址</th>
                    <th width="35">收货方签署状态</th>
                    <th width="24">签收状态</th>
                    <th width="45">到货时间</th>
                    <th width="50">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr v-if="collection.length <= 0">
                    <td colspan="12" style="text-align: center;">暂无数据</td>
                </tr>
                <tr v-for="(o, index) in collection" v-if="collection.length > 0">
                    <td><input type="checkbox" :value="o.id" v-model="options"/></td>
                    <td>{{o.deliveryNo}}</td>
                    <%--<td>{{o.orderNo}}</td>--%>
                    <td>{{o.deliveryTime | dateFormat}}</td>
                    <td>{{o.shipper.companyName}}</td>
                    <td>{{o.receive.companyName}}</td>
                    <td>{{o.receiverName}}</td>
                    <td>{{o.receiverContact}}</td>
                    <td>{{o.receiveAddress}}</td>
                    <td>
                        <span v-if="o.receiptFettle == 2 || o.receiptFettle == 4">已签</span>
                        <span v-else>未签</span>
                    </td>
                    <td>
                        <span class="abnormalSts" v-if="o.signFettle == 2">异常签收</span>
                        <span class="normalSts" v-else-if="o.signFettle == 1">完好签收</span>
                        <span v-else>未签收</span>
                    </td>
                    <td>
                        <span v-if="o.receiveTime != null">{{o.receiveTime | datetime}}</span>
                        <span v-if="o.receiveTime == null">未收货</span>
                    </td>

                    <td>
                        <a class="linkBtn" @click="look(o.id)">查看</a>&nbsp;&nbsp;
                        <a class="linkBtn" v-if="o.receiptFettle == 2 || o.receiptFettle == 1" @click="sign(o.id)">签署</a>
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
    //给回单状态添加默认值
    $("select[name='receiptFettle']").val('');
    //签署样式添加
    $('.radio-item').click(function () {
        $(this).addClass('active').siblings().removeClass('active');
    })

    var pageNum = 1;
    $(document).ready(function () {
        function checkNumber(value) {
            if (value === "" || value == null || isNaN(value)) {
                return 0;
            }
            return value;
        }
        function requestData(id, callBack) {
            //var vue = new Vue({ el: htmlElement, data : {} });
            $.util.json(base_url + '/enterprise/order/orderDetail/' + id, null, function (data) {
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

        function downloadReceipt(vm) {
            var keys = vm.options, parmas = {};
            if (keys == null || keys.length <= 0) {
                $.util.error("请至少选择一条数据");
                return false;
            }
            parmas.orderIds = keys.join(',');
            $.util.json(base_url + '/enterprise/order/down/order', parmas, function (data) {
                if (data.success) {//处理返回结果
                    var msg = "文件大小 : <font color='red'>" + data.size + "</font>MB";
                    $.util.alert("下载回单文件", msg, function () {
                        $.util.download(data.url);
                    });
                } else {
                    $.util.error(data.message);
                }
            });
        }
        function lookDetail(id) {
            $.util.form('回单详情', $(".view_more_panel").html(), function (model) {
                var $panel = this;
                requestData(id, function (result) {
                    createVue($panel.$body.find('.model-base-box')[0], result);
                });
            })
        }
        function batchSign(vm) {
            var selects = vm.selects(), parmas = {};
            if (selects == null || selects.length <= 0) {
                $.util.error("请至少选择一条数据");
                return false;
            }

            var result = true, comparison = null, keys = [], count = 0;
            for(var i = 0; i < selects.length; i++){
                var o = selects[i];
                count++;
                if(o.sign || o.receiptFettle == 0){continue;}
                if(comparison == null){
                    comparison = o.receive.id;
                }else if(comparison - o.receive.id != 0){
                    result = false;
                    break;
                }
                keys.push(o.id);
            }
            if(keys.length <= 0){
                $.util.error("选择的["+count+"]条数据,没有可以签署的回单");
                return false;
            }
            validate(function(){
                if(!result){
                    $.util.confirm('操作警告', '选中的回单中存在不同收货客户，请问是否继续批量签署?', function () {
                        window.location.href= base_url + '/enterprise/sign/view/multiple?rt=2&orderKeys='+ keys.join(',');
                    });
                }else{
                    window.location.href= base_url + '/enterprise/sign/view/multiple?rt=2&orderKeys='+ keys.join(',');
                }
            });
        }

        function validate(callback){
            $.util.json(base_url + '/enterprise/sign/signature/validate', null, function (data) {
                if (data.success) {//处理返回结果
                    if(callback){
                        callback();
                    }
                } else {
                    $.util.error(data.message);
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
                isLastPage: false,
                options:[],//选中的项
                maps:{}
            },
            methods: {
                //翻页
                flip: function (num) {
                    searchManage(num);
                },
                //全选
                selectAll: function (event) {
                    var $t = this;
                    if(!event.target.checked){
                        $t.options.splice(0, $t.options.length);
                    }else{
                        $.each($t.collection, function(index, val) {
                            $t.options.push(val.id);
                        });
                    }
                },
                //下载回单
                download: function (event) {
                    downloadReceipt(this);
                },
                //查看
                look: function (id) {//查看
                    lookDetail(id);
                },
                //单个签署
                sign: function (id) {
                    validate(function(){
                        window.location.href = base_url + '/enterprise/sign/view/single?orderKey=' + id;
                    });
                },
                //批量签署
                multiple:function(){
                    batchSign(this);
                },
                //获取选中的项
                selects:function(){
                    var $t = this, items = [];
                    $.each($t.options, function(index, val) {
                        items.push($t.maps.get(val));
                    });
                    return items;
                }
            }
        });
        var searchManage = function (num) {
            if (!num) {
                num = pageNum;
            } else {
                pageNum = num;
            }
            var parmas = {num: num, size: 10, matchMode: 1}, $form = $('#shipper-manage-form');
            $form.find('input, select').each(function (index, item) {
                parmas[item.name] = item.value;
            });
            var check = $("input[name='receiptFettle_r']:checked").val();
            if (check != "") {
                parmas.flag = check;
            } else {
                parmas.flag = check;
            }
            $.util.json($form.attr('action'), parmas, function (data) {
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
                    listManage.maps = new Map();
                    $.each(listManage.collection, function(index, val) {
                        listManage.maps.set(val.id, val);
                    });
                } else {
                    $.util.error(data.message);
                }
            });
        };
        //提交事件
        $(".search-btn").click(function () {
            searchManage(1);
        });
        $(".receiptFettle_search").click(function () {
            var val = $("input[name='receiptFettle_r']:checked").val();
            if (val == 1) {
                $(".receiptFettle").hide();
                $(".signfettle").hide();
            } else {
                $(".receiptFettle").show();
                $(".signfettle").show();
            }
            searchManage(1);
        });
        searchManage(1);
    });
</script>
</html>