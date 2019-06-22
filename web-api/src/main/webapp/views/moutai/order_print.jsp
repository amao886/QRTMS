<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<title>打印</title>
	<script type="text/css">
		@media print {
			/*不打印的元素*/
			*{margin:0;padding: 0;}
			.Noprn{ display:none;}
			td, th{
				border: 1px solid #000 !important;
			}
			.print-page{
				/*分页*/
				page-break-after:always;
				page-break-inside: avoid;
			}
		}

	</script>
	<%@ include file="/views/include/head.jsp" %>
	<link rel="stylesheet" href="${baseStatic}css/print.css?times=${times}"/>
</head>
<body>
<input type="hidden" id="ids" value="${ids}">
<button class="layui-btn layui-btn-normal btn-print Noprn top-btn">确认打印</button>
<button class="layui-btn layui-btn-normal btn-word Noprn top-btn">导出存档</button>
<button class="layui-btn layui-btn-normal btn-cancel Noprn top-btn">取消</button>
<div id="order-print-template" class="print-wrapper">
	<!-- 完整打印单循环开始 -->
	<div class="print-item"  v-for="key in orders" v-bind:id="key.index" style="width:210mm;height:309mm;overflow: hidden; ">
		<div style="width:200mm; height:280mm; padding:52mm 0 0 5mm; ">
	    <table class="" style="width:100%;">
	        <thead>
	            <tr>
	                <th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th>
	            </tr>
	        </thead>
	        <tbody>
                <tr>
                    <td colspan="2" rowspan="2">发货仓库</td>
                    <td colspan="2" rowspan="2">{{key.depot}}</td>
                    <td colspan="2">收货单位</td>
                    <td colspan="6" class="tl-pl">{{key.customerName}}</td>
                </tr>
                <tr>
                    <td colspan="2">收货地址</td>
                    <td colspan="6" class="tl-pl">
                        <span>{{key.detailaddress}}</span><br />
                        <span>{{key.contact}}</span>
                    </td>
                </tr>
	            <tr>
	                <td colspan="2" rowspan="2">调拨单号</td>
	                <td colspan="2" rowspan="2">客户编码</td>
	                <td colspan="6" rowspan="2">品种规格</td>
	                <td colspan="2">数量</td>
	            </tr>
	            <tr>
	                <td colspan="1">瓶数</td>
	                <td colspan="1">件数</td>
	            </tr>
	            <!-- 物品详情循环开始 -->
				<template v-for="order in key.list">
	            <tr>
	                <td colspan="2"><div class="goods-td">{{order.scheduleNo}}</div></td>
	                <td colspan="2"><div class="goods-td">{{order.customerNo}}</div></td>
	                <td colspan="6" class="tl-pl"><div class="goods-td">{{order.specification}}</div></td>
	                <td colspan="1"><div class="goods-td">{{order.bottles}}</div></td>
	                <td colspan="1"><div class="goods-td">{{order.quantity}}</div></td>
	            </tr>
				</template>
				<template v-for="n in (8- key.list.length)">
					<tr>
						<td colspan="2"><div class="goods-td"></div></td>
						<td colspan="2"><div class="goods-td"></div></td>
						<td colspan="6" class="tl-pl"><div class="goods-td"></div></td>
						<td colspan="1"><div class="goods-td"></div></td>
						<td colspan="1"><div class="goods-td"></div></td>
					</tr>
				</template>
	            <!-- 物品详情循环结束 -->
	            <tr>
	                <td colspan="2">铅封锁</td>
	                <td colspan="8" class="tl-pl">需要（&nbsp;&nbsp;&nbsp;&nbsp;）铅封锁号：</td>
	                <td colspan="2">不需要（ √ ）</td>
	            </tr>
	            <tr>
	                <td colspan="12" class="tl-pl">酒类流通随附单号（有客户编码时必填）：</td>
	            </tr>
	            <tr>
	                <td colspan="5" rowspan="3">企业盖章（未盖章无效）</td>
	                <td colspan="7" class="tl-pl">承运单位：<span>{{key.conveyName}}</span></td>
	            </tr>
	            <tr>
	                <td colspan="7" class="tl-pl">承运单号：</td>
	            </tr>
	            <tr>
	                <td colspan="7" class="tl-pl">取单人签字：</td>
	            </tr>
	            <tr>
	                <td colspan="5" class="tl-pl">制 单 人：<span class="nameStyle">{{key.username}}</span></td>
	                <td colspan="7" class="tl-pl">取单人身份证：{{key.takeIdcare}}</td>
	            </tr>
	            <tr>
	                <td colspan="5" class="tl-pl">通知日期：{{key.year}} 年 <span>{{key.month}}</span> 月<span class="widthShort">{{key.day}}</span>日</td>
	                <td colspan="7" class="tl-pl">领单日期：{{key.year}} 年 <span>{{key.month}}</span> 月<span class="widthShort">{{key.day}}</span>日</td>
	            </tr>
	            <tr>
	                <td colspan="1" rowspan="7">收<br />货<br />单<br />位<br />验<br />收<br />签<br />章</td>
	                <td colspan="6">正常</td>
	                <td colspan="3">异常</td>
	                <td colspan="2" rowspan="2" class="p9">第三方物流服<br />务评价</td>
	            </tr>
	            <tr>
	                <td colspan="6" rowspan="5" class="borderNone"></td>
	                <td colspan="3" rowspan="5" class="borderNone"></td>
	            </tr>
	            <tr>
	                <td colspan="2" class="p17">优（&nbsp;&nbsp;&nbsp;&nbsp;）</td>
	            </tr>
	            <tr>
	                <td colspan="2" class="p17">良（&nbsp;&nbsp;&nbsp;&nbsp;）</td>
	            </tr>
	            <tr>
	                <td colspan="2" class="p17">中（&nbsp;&nbsp;&nbsp;&nbsp;）</td>
	            </tr>
	            <tr>

	                <td colspan="2" rowspan="2" class="p17">差（&nbsp;&nbsp;&nbsp;&nbsp;）</td>
	            </tr>
	            <tr>
	                <td colspan="6" class="borderTopNone">
	                    <div class="receive">
	                        签收日期：{{key.year}} 年<span class="pwidth"></span>月<span class="pwidth"></span>日
	                        <span class="seal">（章）</span>
	                    </div>
	                </td>
	                <td colspan="3" class="borderTopNone"></td>
	            </tr>
	        </tbody>
	    </table>
		</div>
	</div>
	<!-- 完整打印单循环结束 -->
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}plugin/js/scroll/zUI.js"></script>
<script type="text/javascript" src="${baseStatic}plugin/js/jquery.print.min.js"></script>
<script type="text/javascript" src="${baseStatic}plugin/js/qrcode.min.js"></script>
<script type="text/javascript" src="${baseStatic}plugin/js/html2canvas.min.js"></script>
<script>
    $(document).ready(function () {
        var orderTemplate = new Vue({
            el: '#order-print-template',
            data: {
                orders:null,
            },
        });

        var listOrder = function(){
            var parmas = {ids:  $("#ids").val()};
            $.util.json(base_url + '/backstage/moutai/print/orders', parmas, function (data) {
                if (data.success) {//处理返回结果
                    orderTemplate.orders = data.orders;
                } else {
                    $.util.error(data.message);
                }
            });
        };
        $('.btn-cancel').on('click', function(){
            window.history.back(-1);
        });
        $('.btn-print').on('click', function(){
            var parmas = {ids:  $("#ids").val()};
            $.util.json(base_url + '/backstage/moutai/order/printSign', parmas, function (data) {
                if (data.success) {//处理返回结果
                    $("#order-print-template").print();
                } else {
                    $.util.error(data.message);
                }
            });
        });
        $('.btn-word').on('click', function(){
            var parmas = {ids:  $("#ids").val()};
            $.util.json(base_url + '/backstage/moutai/export/word', parmas, function (data) {
                if (data.success && data.result) {//处理返回结果
                    var res = data.result;
					var msg = "文件大小 : <font color='red'>" + res.size + "</font>MB";
                    $.util.alert("下载文件", msg, function () {
                        $.util.download(res.url);
                    });
                } else {
                    $.util.error(data.message);
                }
            });
        });
        listOrder();
    });
</script>
</html>