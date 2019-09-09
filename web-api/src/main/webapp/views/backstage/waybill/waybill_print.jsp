<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-任务单管理-任务单打印</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/waybill_print.css?times=${times}"/>
</head>
<body>
    <span id="waybillKeys" data-keys="${waybillKeys}" data-groupId="${groupId}"></span>
    <object id="wb" classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" width="0"></object>
    <div class="print-wrapper">
        <div class="print-tab">
        <div id="tab-scroll">
            <ul>
                <li class="li-item"><a href="#print1"><img src=""></a></li>
                <!-- 左边导航选中切换右边打印单是通过a标签的锚点实现的，<a href="#print1"></a>对应的就是id为print1的div，
                循环的时候对应id的对应
                 -->
            </ul>
        </div>
    </div>
        <div class="print-toggle Noprn" style="display: none;">
            <div class="menu-icon">
                <span class="menu-line"></span>
                <span class="menu-line"></span>
                <span class="menu-line"></span>
            </div>
        </div>
        <div class="print-right">
            <button class="layui-btn layui-btn-normal btn-print Noprn top-btn">确认打印</button>
            <div class="print-content" id="list-waybill-template">
                <div class="print-item print-page" v-for="w in waybills" v-bind:id="w.index"> <!-- 循环item -->
                    <h2 class="print-title">合同物流管理平台送货单</h2>
                    <div class="now-date">${ctime}</div>
                    <div class="print-text">
                        <div class="print-line">
                            <div class="line-part clearfix">
                                <span class="float-tag">订单号：</span>
                                <div class="text-tag">{{w.deliveryNumber}}</div>
                            </div>
                        </div>
                        <div class="print-line">
                            <div class="line-part clearfix">
                                <span class="float-tag">发货公司：</span>
                                <div class="text-tag">{{w.shipperName}}</div>
                            </div>
                            <div class="line-part clearfix">
                                <span class="float-tag">收货客户：</span>
                                <div class="text-tag">{{w.receiverName}}</div>
                            </div>
                        </div>
                        <div class="print-line">
                            <div class="line-part clearfix">
                                <span class="float-tag">联系人：</span>
                                <div class="text-tag">{{w.shipperContactName}}</div>
                            </div>
                            <div class="line-part clearfix">
                                <span class="float-tag">联系人：</span>
                                <div class="text-tag">{{w.contactName}}</div>
                            </div>
                        </div>
                        <div class="print-line">
                            <div class="line-part clearfix">
                                <span class="float-tag">联系电话：</span>
                                <div class="text-tag">{{w.shipperContactTel}}</div>
                            </div>
                            <div class="line-part clearfix">
                                <span class="float-tag">联系电话：</span>
                                <div class="text-tag">{{w.contactPhone}}</div>
                            </div>
                        </div>
                        <div class="print-line">
                            <div class="line-part clearfix">
                                <span class="float-tag">发货地址：</span>
                                <div class="text-tag">{{w.shipperAddress}}</div>
                            </div>
                            <div class="line-part clearfix">
                                <span class="float-tag">收货地址：</span>
                                <div class="text-tag">{{w.receiveAddress}}</div>
                            </div>
                        </div>
                    </div>
                    <div class="table-style">
                        <table>
                            <thead>
                            <tr>
                                <th>物料名称</th>
                                <th>重量（kg）</th>
                                <th>体积（m³）</th>
                                <th>数量</th>
                                <th>货物摘要</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="g in w.goods">
                                <td>{{g.goodsName}}</td>
                                <td>{{g.goodsWeight}}</td>
                                <td>{{g.goodsVolume}}</td>
                                <td>{{g.goodsQuantity}}</td>
                                <td>{{g.summary}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="print-sign">
                        <div class="sign-content">
                            <div class="sign-img" v-bind:data-url="w.codeString">

                            </div>
                            <div class="sign-summary">
                                <div class="line-part clearfix">
                                    <span class="float-tag">订单摘要：</span>
                                    <div class="text-tag">{{w.orderSummary}}</div>
                                </div>
                            </div>
                        </div>
                        <p class="sign-people">签收人：<span></span></p>
                        <p class="sign-date">签收日期：<span></span></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}plugin/js/scroll/zUI.js"></script>
<script type="text/javascript" src="${baseStatic}plugin/js/jquery.print.min.js"></script>
<script type="text/javascript" src="${baseStatic}plugin/js/qrcode.min.js"></script>
<script type="text/javascript" src="${baseStatic}plugin/js/html2canvas.min.js"></script>
<script>
    $(document).ready(function () {
    	//初始化侧导航
        $('#tab-scroll').height($('.print-tab').height()-40);
        $('#tab-scroll').panel({iWheelStep:32});
        //切换侧导航
        $('.print-toggle').on('click',function(){
            if($(this).hasClass('open')){
                $('.print-wrapper').removeClass('sidebar-show');
                $(this).removeClass('open');
            }else{
                $('.print-wrapper').addClass('sidebar-show');
                $(this).addClass('open');
            }
        })
        var waybillTemplate = new Vue({
            el: '#list-waybill-template',
            data: {waybills:[]},
            updated:function(){
                //var __loading = $.util.loading("正在生成打印数据...");
                $(this.$el).find('.sign-img').each(function(){
                    new QRCode(this, {
                        text: $(this).data('url'),
                        width: 106,
                        height: 106,
                        colorDark: "#000000",
                        colorLight: "#ffffff",
                        correctLevel: QRCode.CorrectLevel.H
                    });
                });
                //_loading.setContext("正在生成预览数据...");
                /*
                var preview = $('#tab-scroll').find('ul');
                preview.html('');
                $(this.$el).find('.print-item').each(function(){
                    var id = this.id;
                    preview.append("<li class='li-item'><a href='#"+ id +"' id='a-"+ id +"'></a></li>");
                    html2canvas(this,{async:true}).then(function(canvas) {
                        preview.find("#a-"+ id).append("<img src="+ canvas.toDataURL("image/png") +" />");
                    });
                });
                */
                //__loading.close();
            }
        });
        var listWaybill = function(){
            var parmas = {waybillKeys: $("#waybillKeys").data("keys")};
        	var groupId = $("#waybillKeys").attr("data-groupId");
            parmas["groupId"] = groupId;
            $.util.json(base_url + '/backstage/trace/print/waybills', parmas, function (data) {
                if (data.success) {//处理返回结果
                    waybillTemplate.waybills = data.waybills;
                } else {
                    $.util.error(data.message);
                }
            });
        };
        $('.btn-print').on('click', function(){
            //window.print();
            $("#list-waybill-template").print();
        });
        listWaybill();
    });
</script>
</html>
