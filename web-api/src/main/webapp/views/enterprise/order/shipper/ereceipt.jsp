<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-运输管理-发货管理</title>
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
    <form id="shipper-manage-form" action="${basePath}/enterprise/order/receipt/shipper/search" method="get">
        <div class="content-search">
            <div class="clearfix">
                <div class="fl col-min-new radio-wrapper">
                    <div class="radio-item active">
                        <label class="text" for="scan1">全部</label>
                        <input type="radio" name="receiptFettle_r" class="receiptFettle_search" id="scan1"
                               checked="checked" value="">
                    </div>
                    <div class="radio-item">
                        <label class="text" for="scan2">未生成回单</label>
                        <input type="radio" name="receiptFettle_r" class="receiptFettle_search" id="scan2" value="1">
                    </div>
                    <div class="radio-item">
                        <label class="text" for="scan3">已生成回单</label>
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
                <div class="fl col-min-new fettle">
                    <select name="signFettle" class="tex_t" id="signFettle" placeholder="订单状态"
                            val="${search.fettle}">
                        <option value="0">全部</option>
                        <option value="1">异常签收</option>
                        <option value="2">正常签收</option>
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
                <button class="layui-btn layui-btn-normal"  v-on:click="creatReceipts()">生成电子回单</button>
                <button class="layui-btn layui-btn-normal" v-on:click="download">下载回单</button>
            </div>
            <table>
                <thead>
                <tr>
                    <th width="10">
                        <input type="checkbox" id="checkAll" v-on:click="selectAll"/>
                    </th>
                    <th width="35">送货单号</th>
                    <th width="30">发货日期</th>
                    <th width="45">发货方</th>
                    <th width="45">收货客户</th>
                    <th width="22">收货人</th>
                    <th width="27">联系方式</th>
                    <th width="80">收货地址</th>
                    <th width="24">物流商签</th>
                    <th width="24">收货方签</th>
                    <th width="24">签收状态</th>
                    <th width="40">到货时间</th>
                    <th width="50">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr v-if="collection.length <= 0">
                    <td colspan="13" style="text-align: center;">暂无数据</td>
                </tr>
                <tr v-for="o in collection" v-if="collection.length > 0">
                    <td><input type="checkbox" name="orderId" v-bind:status="o.receiptFettle" v-bind:value="o.id"/></td>
                    <td>{{o.deliveryNo}}</td>
                    <td>{{o.deliveryTime | dateFormat}}</td>
                    <td>{{o.shipper.companyName}}</td>
                    <td>{{o.receive.companyName}}</td>
                    <td>{{o.receiverName}}</td>
                    <td>{{o.receiverContact}}</td>
                    <td>{{o.receiveAddress}}</td>
                    <td>
                        <span v-if="o.receiptFettle == 3 || o.receiptFettle == 4">已签</span>
                        <span v-else>未签</span>
                    </td>
                    <td>
                        <span v-if="o.receiptFettle == 2 || o.receiptFettle == 4">已签</span>
                        <span v-else>未签</span>
                    </td>
                    <td>
                        <span class="abnormalSts" v-if="o.signFettle == 2">异常签收</span>
                        <span class="normalSts" v-else-if="o.signFettle == 1">正常签收</span>
                        <span v-else>未签收</span>
                    </td>
                    <td>
                        <span v-if="o.receiveTime != null">{{o.receiveTime | datetime}}</span>
                        <span v-if="o.receiveTime == null">未收货</span>
                    </td>

                    <td>
                        <a class="linkBtn" @click="look(o.id)">查看</a>&nbsp;&nbsp;
                        <a class="linkBtn" v-if="o.receiptFettle == 0" @click="creatReceipt(o.id)">生成回单</a>
                        <a class="linkBtn" v-if="o.receiptFettle == 1" @click="cancelReceipt(o.id)">作废</a>
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
<!-- 批量生成电子回单 -->
<div class="batch_build_panel" style="display: none">
    <div class="model-base-box batch_build" style="width: 480px;">
        <div class="model-form-field file_box">
            <span style="font-weight: bold;">确定已勾选项的发货信息正确？</span>
        </div>
        <div class="model-form-field file_box">
            <span class="span-radio-checkbox"><input type="radio" name="buildType" value="1" checked>逐个确认并生成电子回单</span>
        </div>
        <div class="model-form-field file_box">
            <span class="span-radio-checkbox"><input type="radio" name="buildType" value="2">无需逐个确认，直接生成电子回单</span>
        </div>
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


<!--确认异常-->
<div class="conform_abnormal_panel" style="display: none">

    <div class="model-base-box blackColor conform_abnormal" style="width:1020px;">
        <div class="pancelCon clearfix">
            <div class="h4">产品配送单</div>
            <div class="infoNo">
                <p>订单编号：{{orderNo}}</p>
                <p>日期：{{deliveryTime | date}}</p>
            </div>
            <ul class="pancelUl clearfix">
                <li><label>配送单号：</label><span>56156156146</span></li>
                <li><label>订单客户：</label><span>广州顺德电子商务有限公司</span></li>
                <li><label>收货客户：</label><span>广州顺德电子商务有限公司</span></li>
                <li><label>收货人员：</label><span>王小二</span></li>
                <li><label>收货地址：</label><span>中国湖北省孝感市孝南区辉晓路258号</span></li>
                <li><label>联系方式：</label><span>15987189536</span></li>
                <li><label>物流商名称：</label><span>远成集团有限公司杭州分公司（鄂陕宁线）</span></li>
                <li><label>补发前单号：</label><span>56156156146</span></li>
                <li>
                    <label>订单备注：</label>
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
                            <tr>
                                <td>25072058123</td>
                                <td>餐具套装</td>
                                <td>pcs</td>
                                <td>20</td>
                                <td>30</td>
                                <td>30</td>
                                <td>50</td>
                            </tr>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="3">合计</td>
                                <td>20</td>
                                <td>30</td>
                                <td>30</td>
                                <td>50</td>
                            </tr>
                            <tr>
                                <td colspan="3">异常说明</td>
                                <td colspan="4"><input type="text"></td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </li>
            </ul>

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

<!--生成电子回单-->
<div class="creat_receipt_panel" style="display: none">
    <div class="model-base-box blackColor creat_receipt" style="width:1020px;">
        <div class="pancelCon clearfix">
            <div class="h4">产品配送单</div>
            <div class="infoNo">
                <p>订单编号：{{orderNo}}</p>
                <p>日期：{{deliveryTime | date}}</p>
            </div>
            <ul class="pancelUl clearfix">
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
                    <div class="tableCon signPos">
                        <table>
                            <thead>
                            <tr>
                                <th width="70">物料编号</th>
                                <th width="190">物料描述/物料名称</th>
                                <th width="40">单位</th>
                                <th width="80">数量</th>
                                <th width="80">箱数</th>
                                <th width="80">体积</th>
                                <th width="80">重量</th>
                                <th width="45">操作</th>
                            </tr>
                            </thead>
                            <tbody class="editTable">
                            <tr v-for="(c,index) in commodities">
                                <td><input type="text" class="editLong" v-model="c.commodityNo"/></td>
                                <td><input type="text" class="editLong" v-model="c.commodityName"/></td>
                                <td><input type="text" class="editLong" v-model="c.commodityUnit"/></td>
                                <td>
                                    <div class="oprateNumCon">
                                        <span class="minusIcon" @click="minusNum('quantity',index)">-</span>
                                        <input type="text" class="editTxt" v-model="c.quantity">
                                        <span class="addIcon" @click="addNum('quantity',index)">+</span>
                                    </div>
                                </td>
                                <td>
                                    <div class="oprateNumCon">
                                        <span class="minusIcon" @click="minusNum('boxCount',index)">-</span>
                                        <input type="text" class="editTxt" v-model="c.boxCount">
                                        <span class="addIcon" @click="addNum('boxCount',index)">+</span>
                                    </div>
                                </td>
                                <td>
                                    <div class="oprateNumCon">
                                        <span class="minusIcon" @click="minusNum('volume',index)">-</span>
                                        <input type="text" class="editTxt" v-model="c.volume">
                                        <span class="addIcon" @click="addNum('volume',index)">+</span>
                                    </div>
                                </td>
                                <td>
                                    <div class="oprateNumCon">
                                        <span class="minusIcon" @click="minusNum('weight',index)">-</span>
                                        <input type="text" class="editTxt" v-model="c.weight">
                                        <span class="addIcon" @click="addNum('weight',index)">+</span>
                                    </div>
                                </td>
                                <td><a class="linkBtn" @click="deleteLine(index)">删除</a></td>
                            </tr>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="3">合计</td>
                                <td>{{quantity}}</td>
                                <td>{{boxCount}}</td>
                                <td>{{volume | numberFixed}}</td>
                                <td>{{weight | numberFixed}}</td>
                                <td></td>
                            </tr>
                            </tfoot>
                        </table>
                        <a class="linkBtn addLine" @click="addGoodsLine">新增物料</a>
                    </div>
                </li>
            </ul>
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
        //生成电子回单
        function batchBuild(keys, index) {
            if (keys == null) {
                var orderKeys = [];
                var signdNum =  0;
                $('input[name="orderId"]:checked').each(function () {
                    if($(this).attr("status") > 0){
                        signdNum ++;
                    }
                    orderKeys.push($(this).val());
                });
                if (signdNum > 0) {
                    $.util.error("有"+ signdNum + "条已经生成过电子订单,请重新选择");
                    return false;
                }
                if (orderKeys == null || orderKeys.length <= 0) {
                    $.util.error("请至少选择一条数据");
                    return false;
                }
                var panel = $.util.panel('生成电子回单', $(".batch_build_panel").html());
                panel.addButton({text: '取消'});
                panel.addButton(function () {
                    var buildType = this.$body.find(":radio[name='buildType']:checked").val();
                    if (buildType && buildType == 2) {
                        batchBuild(orderKeys);
                    } else {
                        batchBuild(orderKeys, 0);
                    }
                });
                panel.open();
            } else {
                if (keys instanceof Array && (index == null || index < 0)) {//直接批量生成
                    var parmas = {orderKeys: keys.join(',')};
                    $.util.json(base_url + '/enterprise/order/build', parmas, function (data) {
                        if (data.success) {//处理返回结果
                            $.util.success(data.message, function(){
                                searchManage();//成功后刷新列表
                            }, 2000);
                        } else {
                            $.util.error(data.message);
                        }
                    });
                } else {
                    var _key = keys;
                    if (index != null && index >= 0) {
                        _key = keys[index];
                    }
                    requestData(_key, function (result) {
                        var panel = $.util.panel('生成电子回单', $(".creat_receipt_panel").html());
                        panel.initialize = function () {
                            panel.vm = createVue(this.$body.find('.model-base-box')[0], result);
                        }
                        panel.setButtons(createButtons(panel, keys, index));
                        panel.open();
                    });
                }
            }
        }

        function downloadReceipt(element) {
            var chk_values = [], parmas = {};
            $('input[name="orderId"]:checked').each(function () {
                chk_values.push($(this).val());
            });
            if (chk_values == null || chk_values.length <= 0) {
                $.util.error("请至少选择一条数据");
                return false;
            }
            parmas.orderIds = chk_values.join(',');
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

        function createSign(element) {
            var chk_values = [], parmas = {};
            $('input[name="orderId"]:checked').each(function () {
                chk_values.push($(this).val());
            });
            if(chk_values == null || chk_values.length <= 0){
                $.util.error("请至少选择一条数据");
                return false;
            }
            parmas.orderKeys = chk_values.join(',');
            $.util.json(base_url + '/enterprise/order/build', parmas, function (data) {
                if (data.success) {//处理返回结果
                    $.util.success(data.message, function(){
                        searchManage();//成功后刷新列表
                    }, 2000);
                } else {
                    $.util.error(data.message);
                }
            });
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
                methods: {
                    addGoodsLine: function () {//增加物料
                        var $this = this;
                        var _list = {
                            "id": "",
                            "orderId": "",
                            "commodityNo": "",
                            "commodityName": "",
                            "quantity": "",
                            "boxCount": "",
                            "volume": "",
                            "weight": "",
                            "remark": "",
                            "createUserId": "",
                            "createTime": "",
                            "updateTime": ""
                        }
                        $this.commodities.push(_list);
                    },
                    deleteLine: function (i) {//删除物料
                        var $this = this;
                        var arr = $this.commodities;
                        arr.splice(i, 1);
                        $this.commodities = arr;
                    },
                    minusNum: function (obj, i) {//减法
                        var $this = this;
                        if (Number($this.commodities[i][obj]) < 1) return;

                        var num = Number($this.commodities[i][obj]) - 1;
                        $this.commodities[i][obj] = num;
                    },
                    addNum: function (obj, i) {//加法
                        var $this = this,
                            num = Number($this.commodities[i][obj]) + 1;
                        $this.commodities[i][obj] = num;
                    }
                },
                computed: {
                    conveyUrl: function () {
                        return this.pathPrefix + "/" + this.convey.sealDate
                    },
                    receiveUrl: function () {
                        return this.pathPrefix + "/" + this.receive.sealDate
                    },
                },
                watch: {
                    commodities: {//监听求和
                        handler: function (newValue, oldValue) {
                            var $this = this, regExp = new RegExp('^[0-9]+.{0,1}[0-9]*$');
                            $this.quantity = $this.boxCount = $this.volume = $this.weight = 0;
                            $.each(newValue, function (i, item) {
                                item.quantity = checkNumber(String(item.quantity).replace(/\D+/g, ''));
                                item.boxCount = checkNumber(String(item.boxCount).replace(/\D+/g, ''));
                                item.volume = checkNumber(item.volume);
                                item.weight = checkNumber(item.weight);

                                $this.quantity += Number(item.quantity);
                                $this.boxCount += Number(item.boxCount);
                                $this.volume += Number(item.volume);
                                $this.weight += Number(item.weight);
                            });
                        },
                        deep: true
                    }
                }
            });
        }

        function createButtons(panel, keys, index) {
            var orderKey = keys, batch = (index != null && index >= 0), buttons = {};
            if (batch) {
                orderKey = keys[0];
                buttons['before'] = {
                    text: '上一条', alignment: 'left', isDisabled: (index <= 0), action: function () {
                        if (index > 0) {
                            orderKey = keys[--index];
                            requestData(orderKey, function (result) {
                                for (var key in result) {
                                    panel.vm[key] = result[key];
                                }
                            });
                            if (index <= 0) {
                                panel.target.buttons.before.disable();
                            }
                            if (index < keys.length - 1) {
                                panel.target.buttons.after.enable();
                            }
                        }
                        return false;
                    }
                };
                buttons['after'] = {
                    text: '下一条', alignment: 'left', isDisabled: (index >= keys.length - 1), action: function () {
                        if (index < keys.length - 1) {
                            orderKey = keys[++index];
                            requestData(orderKey, function (result) {
                                for (var key in result) {
                                    panel.vm[key] = result[key];
                                }
                            });
                            if (index > 0) {
                                panel.target.buttons.before.enable();
                            }
                            if (index >= keys.length - 1) {
                                panel.target.buttons.after.disable();
                            }
                        }
                        return false;
                    }
                };
            }
            buttons['ok'] = {
                text: '确定生成', action: function () {
                    var parmas = {id: orderKey, build: true}
                    if (panel.vm != null) {
                        parmas['commodities'] = JSON.stringify(panel.vm.commodities);
                    }
                    $.util.json(base_url + '/enterprise/order/edit/order', parmas, function (data) {
                        if (data.success) {//处理返回结果
                            $.util.success(data.message, function(){
                                searchManage();//成功后刷新列表
                            }, 2000);
                        } else {
                            $.util.error(data.message);
                        }
                    });
                    return !batch;
                }
            };
            return buttons;
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
                selectAll: function (event) {
                    $("input[name='orderId']").prop('checked', event.target.checked);
                },
                download: function (event) {
                    downloadReceipt(event);
                },
                look: function (id) {//查看
                    $.util.form('回单详情', $(".view_more_panel").html(), function (model) {
                        var $panel = this;
                        requestData(id, function (result) {
                            createVue($panel.$body.find('.model-base-box')[0], result);
                        });
                    })
                },
                creatReceipts: function () {
                    batchBuild();
                },
                creatReceipt: function (id, index) {//生成电子回单
                    batchBuild(id);
                },
                cancelReceipt: function (id) {//生成电子回单
                    $.util.confirm('电子回单作废', '确定将此回单作废？', function () {
                        var ids = new Array();
                        ids.push(id);
                        var param = {"ids": JSON.stringify(ids)};
                        var url = base_url + "/enterprise/order/shipper/manage/cancle";
                        $.util.json(url, param, function (data) {
                            if (data.success) {//处理返回结果
                                $.util.success(data.message, function(){
                                    searchManage();//成功后刷新列表
                                }, 2000);
                            } else {
                                $.util.error(data.message);
                            }
                        });
                    });
                },
                confirmAbnormal: function (id) {//确认异常
                    $.util.form('回单详情', $(".conform_abnormal_panel").html(), function (model) {

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
                $(".signFettle").hide();
            } else {
                $(".receiptFettle").show();
                $(".signFettle").show();
            }
            searchManage(1);
        });


        document.onkeydown = function (e) {
            if (!e) e = window.event;
            if ((e.keyCode || e.which) === 13) {
                searchManage(1);
            }
        }
        searchManage(1);
    });
</script>
</html>