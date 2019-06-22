<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>任务单绑定</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${baseStatic}css/viewer.css?times=${times}"/>
    <link rel="stylesheet" type="text/css" href="${baseStatic}css/bindWaybill.css?times=${times}"/>
    <style>
        .table-style table{
            margin-top:0px;
        }
        .table-style table td{
            height: 27px;
            cursor: pointer;
        }
        .row{
            margin-top: 0;
            margin-bottom: 0;
        }
        .page-header{
            margin-right: -15px !important;
            margin-left: -15px !important;
            margin-top: 5px;
            margin-bottom: 10px;
        }
        .input-group-addon {
            font-size: 14px;
            font-weight: 400;
            line-height: 1;
            color: rgb(85, 85, 85);
            text-align: center;
            background-color: #FFFFFF;
            padding: 0px;
            margin: 0px;
            border-width: 1px;
            border-style: solid;
            border-color: rgb(204, 204, 204);
            border-image: initial;
            border-radius: 4px;
        }
        .input-group-addon>select{
            border: 0;
            font-family: Consolas;
        }

        .callout-item-group {
            border-left-color: #ce4844;
            padding: 5px;
            margin: 0 0 20px 0;
            border: 1px solid #eee;
            border-left-width: 5px;
            border-radius: 3px;
        }
        .line-ul{
            font-family: Consolas;
            width: 100%;
            padding: 5px 0px 5px 0px;
        }
        .line-ul>li{
            display: inline-block;
            padding-right: 20px;
        }
        .pager {
            padding-left: 0px;
            text-align: center;
            margin: 10px 0px;
            list-style: none;
        }
        .pager li > a, .pager li > span {
            padding: 2px 14px;
        }
</style>
</head>
<body>
<div class="track-content">
    <div class="container-fluid">
        <div class="row" style="margin-bottom: 10px;">
            <div class="col-xs-5" id="list-wait-code">
                <div class="page-header">
                    <h3>待绑定二维码</h3>
                </div>
                <div class="row">
                    <div class="input-group">
                        <input type="text" name="likeString" class="form-control" placeholder="输入二维码号查询" style="height: 32px;">
                        <div class="input-group-btn">
                            <button type="button" class="btn btn-default" v-on:click="search">查询</button>
                        </div>
                    </div>
                </div>
                <nav aria-label="...">
                    <ul class="pager">
                        <li class="previous"><a href="#"><span aria-hidden="true" v-on:click="page(prePage)">上一页</span></a></li>
                        <li>共<font color="#ff6347">{{ total }}</font>条待绑定二维码</li>
                        <li class="next"><a href="#"><span aria-hidden="true" v-on:click="page(nextPage)">下一页</span></a></li>
                    </ul>
                </nav>
                <div class="row">
                    <div class="barcode-list" style="min-height: 167px;">
                        <div class="table-style">
                            <table>
                                <thead>
                                <tr>
                                    <th>二维码号</th>
                                    <th>上传时间</th>
                                </tr>
                                </thead>
                                <tbody id="fristTBody">
                                <tr v-for="item in codes" v-if="codes.length > 0" v-bind:id="item.barcode" v-on:click="select(item.barcode)">
                                    <td>{{ item.barcode }}</td>
                                    <td>{{ item.loadTime }}</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xs-7" style="padding-left: 50px;" id="list-wait-waybill">
                <div class="page-header">
                    <h3>待绑定任务单</h3>
                </div>
                <div class="row">
                    <div class="input-group">
                        <input type="text" name="likeString" class="form-control" placeholder="输入发货方/收货方查询" style="height: 32px;">
                        <div class="input-group-btn">
                            <button type="button" class="btn btn-default" v-on:click="search">查询</button>
                        </div>
                    </div>
                </div>
                <nav aria-label="...">
                    <ul class="pager">
                        <li class="previous"><a href="#"><span aria-hidden="true" v-on:click="page(prePage)">上一页</span></a></li>
                        <li>共<font color="#ff6347">{{ total }}</font>条待绑定任务单</li>
                        <li class="next"><a href="#"><span aria-hidden="true" v-on:click="page(nextPage)">下一页</span></a></li>
                    </ul>
                </nav>
                <div class="row">
                    <div class="barcode-list" style="min-height: 167px;">
                        <div class="table-style">
                            <table>
                                <thead>
                                <tr>
                                    <th width="30%">发货方</th>
                                    <th width="55%">收货方</th>
                                    <th width="15%">发货时间</th>
                                </tr>
                                </thead>
                                <tbody id="goods_list">
                                <tr  v-for="item in waybills" v-if="waybills.length > 0" v-bind:id="item.id" v-on:click="select(item.id)">
                                    <td>{{ item.shipperName }}</td>
                                    <td>{{ item.receiverName }}</td>
                                    <td>{{ item.createtime }}</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-5" id="detail-wait-code" style="display: none;">
                <div class="page-header" style="height: 35px;">
                    <div style="width: 70%; float: left">
                        <h3>待绑定二维码-详情</h3>
                    </div>
                    <div style="width: 30%; float: right; text-align: right">
                        <button type="button" class="btn btn-success btn-sm" id="done-bind">确认绑定二维码</button>
                    </div>
                </div>
                <div class="row">
                    <ul class="line-ul">
                        <li><label>上报人:</label><span>{{loader.unamezn}}</span></li>
                        <li><label>手机号:</label><span>{{loader.mobilephone}}</span></li>
                        <li><label>上报时间:</label><span>{{time}}</span></li>
                    </ul>
                    <ul class="line-ul">
                        <li>
                            <label>最新位置:</label><span v-for="(item, index) in tracks" v-if="index == 0">{{item.reportLoaction}}</span>
                        </li>
                    </ul>
                </div>
                <div class="row">
                    <div class="barcode-img">
                        <div class="ul-box" id="ul-box">
                            <ul class="barcodeImg">
                                <li v-for="item in imgs">
                                    <!--  src="http://ksh.ycgwl.com/res/upload/2017-12-22/IaH4xfTnxftCs1Rkg-X-ZLykVjNuoHjCiIGiXBR13bristiM_wQYIENbBp2h2URW.jpg" -->
                                    <img v-bind:src="item" class="picImg">
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xs-7" style="padding-left: 50px; display: none;"  id="detail-wait-waybill" >
                <div class="page-header" style="height: 35px;">
                    <div style="width: 70%; float: left">
                        <h3>待绑定任务单-详情</h3>
                    </div>
                    <div style="width: 30%; float: right; text-align: right">
                        <a href="#" v-on:click="edit(id)">编辑任务单</a>
                    </div>
                </div>
                <div class="row">
                    <div class="callout-item-group">
                        <ul class="line-ul">
                            <li><label>送货单号:</label><span id="deliveryNumber">{{deliveryNumber}}</span></li>
                            <li><label>发货时间:</label><span>{{vcreatetime}}</span></li>
                            <li><label>要求到货时间:</label><span>{{varrivaltime}}</span></li>
                        </ul>
                        <ul class="line-ul">
                            <li><label>件数:</label><span>{{number}}</span></li>
                            <li><label>重量:</label><span>{{weight}}</span>kg</li>
                            <li><label>体积:</label><span>{{volume}}</span>m³</li>
                        </ul>
                    </div>
                    <div class="callout-item-group">
                        <ul class="line-ul">
                            <li><label>始发地:</label><span>{{startStation}}</span></li>
                        </ul>
                        <ul class="line-ul">
                            <li><label>发货方:</label><span>{{shipperName}}</span></li>
                            <li><label>联系人:</label><span>{{shipperContactName}}</span></li>
                            <li><label>联系人电话:</label><span>{{shipperContactTel}}</span></li>
                        </ul>
                        <ul class="line-ul">
                            <li><label>发货地址:</label><span>{{fullShipperAddress}}</span></li>
                        </ul>
                    </div>
                    <div class="callout-item-group">
                        <ul class="line-ul">
                            <li><label>目的地:</label><span>{{endStation}}</span></li>
                        </ul>
                        <ul class="line-ul">
                            <li><label>收货方:</label><span>{{receiverName}}</span></li>
                            <li><label>联系人:</label><span>{{contactName}}</span></li>
                            <li><label>联系人电话:</label><span>{{contactPhone}}</span></li>
                        </ul>
                        <ul class="line-ul">
                            <li><label>收货地址:</label><span>{{fullReceiveAddress}}</span></li>
                        </ul>
                    </div>
                </div>
                <div class="row">
                    <div class="table-style">
                        <table>
                            <thead>
                                <tr>
                                    <th>客户料号</th>
                                    <th>物料名称</th>
                                    <th width="10%">重量/kg</th>
                                    <th width="10%">体积/m³</th>
                                    <th width="10%">数量/件</th>
                                    <th>货物摘要</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="item in goods">
                                    <td>{{item.goodsType}}</td>
                                    <td>{{item.goodsName}}</td>
                                    <td>{{item.goodsQuantity}}</td>
                                    <td>{{item.goodsWeight}}kg</td>
                                    <td>{{item.goodsVolume}}m³</td>
                                    <td>{{item.summary}}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<!-- <script type="text/javascript" src="${baseStatic}plugin/js/viewer-jquery.min.js"></script> -->
<script type="text/javascript" src="${baseStatic}plugin/js/viewer.min.js"></script>
<script src="${baseStatic}plugin/js/jquery.mloading.js"></script>
<script src="${baseStatic}js/Date.js"></script>
<script>
    $(document).ready(function () {
        var wabillDefault = {
            id: null,
            arrivaltime: null,
            createtime: null,
            weight: null,
            volume: null,
            number: null,
            deliveryNumber: null,
            receiverName: null,
            receiverTel: null,
            receiveAddress: null,
            contactName: null,
            contactPhone: null,
            shipperName: null,
            startStation: null,
            simpleStartStation: null,
            endStation: null,
            simpleEndStation: null,
            shipperAddress: null,
            shipperTel: null,
            shipperContactName: null,
            shipperContactTel: null,
            arriveDay:0,
            arriveHour:0,
            goods: []
        };
        var v = {
            detailCode : new Vue({
                el: '#detail-wait-code',
                data:{ barcode: null, loadTime: null, loader: {}, images: [], tracks: [], picView: null },
                computed: {
                    imgs: function () {
                        var imgs = [];
                        $.each(this.images, function (i, item) {
                            imgs.push( base_img + item.storagePath);
                        });
                        return imgs;
                    },
                    time: function () {
                        if(this.loadTime){
                            return moment(this.loadTime).format('YYYY-MM-DD HH:mm');
                        }
                        return '';
                    }
                },
                mounted: function () {
                    this.$nextTick(function () {
                    })
                },
                updated: function(){
                    if(this.picView){
                        this.picView.destroy();
                    }
                    this.showBigImg();
                },
                methods: {
                    showBigImg: function(){
                        var _this = this;
                        var picWidth,picLeft;
                        var picItemWidth = $('#ul-box').width();
                        _this.picView = new Viewer(document.querySelector('#ul-box'), {
                            navbar: false,
                            button:false,
                            title:false,
                            tooltip:false,
                            transition:false,
                            viewed: function(){
                                /* $('.viewer-canvas>img').css({
                                    'height':'320px'
                                }); */
                                picWidth = $(_this.$el).find('.viewer-move').width();
                                picLeft = (picItemWidth -picWidth )/2;
                                _this.picView.moveTo(picLeft, 0);
                            }
                        });
                        $(this.$el).find('.picImg').trigger('click');
                    }
                }
            }),
            listCode : new Vue({
                el: '#list-wait-code',
                data: {total:0, collection:[], prePage:0, nextPage:0},
                methods:{
                    search:function(event){ listCode(1); },
                    page: function(num){ listCode(num); },
                    select:function(code){ selectCode(code); }
                },
                computed: {
                    codes: function () {
                        $.each(this.collection, function (i, item) {
                            if(item.loadTime){
                                item.loadTime = moment(item.loadTime).format('YYYY-MM-DD HH:mm');
                            }
                        });
                        return this.collection;
                    }
                }
            }),
            listWaybill : new Vue({
                el: '#list-wait-waybill',
                data: {total:0,collection:[],prePage:0,nextPage:0},
                methods:{
                    search:function(event){ listWaybill(1); },
                    page: function(num){ listWaybill(num); },
                    select:function(wkey){ selectWaybill(wkey); }
                },
                computed: {
                    waybills: function () {
                        $.each(this.collection, function (i, item) {
                            if(item.createtime){
                                item.createtime = moment(item.createtime).format('YYYY-MM-DD HH:mm');
                            }
                        });
                        return this.collection;
                    }
                }
            }),
            detailWaybill : new Vue({
                el: '#detail-wait-waybill',
                data: wabillDefault,
                computed: {
                    fullReceiveAddress: function () {
                        if(this.endStation){
                            return this.endStation.replace(/-/g, '') + this.receiveAddress;
                        }
                        return this.receiveAddress;
                    },
                    fullShipperAddress: function () {
                        if(this.startStation){
                            return this.startStation.replace(/-/g, '') + this.shipperAddress;
                        }
                        return this.shipperAddress;
                    },
                    vcreatetime: function () {
                        if(this.createtime){
                            return moment(this.createtime).format('YYYY-MM-DD HH:mm');
                        }
                        return '';
                    },
                    varrivaltime: function () {
                        if(this.bindstatus == 10 && this.arriveDay > 0 && this.arriveHour > 0){
                            return '发货后'+ this.arriveDay+'天'+ this.arriveHour +'点前';
                        }else if(this.arrivaltime){
                            return moment(this.arrivaltime).format('YYYY-MM-DD HH:mm');
                        }
                        return '无要求';
                    }
                },
                methods:{
                    edit:function(wkey){
                        if(wkey){
                            location.href = base_url + '/backstage/trace/eidtview/' + wkey;
                        }
                    }
                }
            })
        };

        var listCode = function(num){
            var parmas = {likeString: $("#list-wait-code").find("input[name='likeString']").val(), num : num};
            $.util.json(base_url + '/backstage/driver/list/wait/code', parmas, function (data) {
                if (data.success) {//处理返回结果
                    v.listCode.prePage = data.page.prePage;
                    v.listCode.nextPage = data.page.nextPage;
                    v.listCode.total = data.page.total;
                    v.listCode.collection = data.page.collection;
                    if(data.page.collection && data.page.collection.length > 0){
                        $('#detail-wait-code').show();
                        selectCode(data.page.collection[0].barcode);
                    }else{
                        modifyWaybills(null);
                        $('#detail-wait-code').hide();
                    }
                } else {
                    $.util.error(data.message);
                }
            });
        };
        var modifyWaybills = function(page){
            v.wkey = null;
            if(page){
                v.listWaybill.prePage = page.prePage;
                v.listWaybill.nextPage = page.nextPage;
                v.listWaybill.total = page.total;
                v.listWaybill.collection = page.collection;
                if(page.collection && page.collection.length > 0){
                    $('#detail-wait-waybill').show();
                    selectWaybill(page.collection[0].id);
                }else{
                    $('#detail-wait-waybill').hide();
                }
            }else{
                v.listWaybill.prePage = 0;
                v.listWaybill.nextPage = 0;
                v.listWaybill.total = 0;
                v.listWaybill.collection = [];
                $('#detail-wait-waybill').hide();
            }
        }
        var listWaybill = function(num){
            if(!v.barcode){
                $.util.error("请先选择待绑定二维码");
                return;
            }
            var parmas = {barcode : v.barcode, likeString: $("#list-wait-waybill").find("input[name='likeString']").val(), num : num};
            $.util.json(base_url + '/backstage/driver/list/unbind/waybill', parmas, function (data) {
                if (data.success) {//处理返回结果
                    modifyWaybills(data.page);
                } else {
                    $.util.error(data.message);
                }
            });
        };

        var selectCode = function(code){
            var parmas = {barcode: code};
            $.util.json(base_url + '/backstage/driver/wait/code/detail', parmas, function (data) {
                if (data.success) {//处理返回结果

                    v.barcode = code;

                    $("#"+ code).css("background","#31acfa").siblings().css("background","#FFFFFF");

                    var info = data.info;
                    v.detailCode.barcode = info.barcode;
                    v.detailCode.loadTime = info.loadTime;
                    v.detailCode.loader = info.loader;
                    v.detailCode.images = info.images;
                    v.detailCode.tracks = info.tracks;

                    modifyWaybills(data.waybills);

                } else {
                    v.barcode = null;
                    $.util.error(data.message);
                }
            });
        };

        var selectWaybill = function(wkey){
            if(!wkey){
                for (var k in wabillDefault){
                    Vue.set( v.detailWaybill, k, '');
                }
            }else{
                $.util.json(base_url + '/backstage/trace/detail/'+ wkey , {}, function (data) {
                    if (data.success) {//处理返回结果
                        v.wkey = wkey;
                        $("#"+ wkey).css("background","#31acfa").siblings().css("background","#FFFFFF");
                        var waybill = data.waybill;
                        v.wkey = waybill.id;
                        for (var k in waybill){
                            Vue.set( v.detailWaybill, k, waybill[k]);
                        }
                    } else {
                        v.wkey = null;
                        $.util.error(data.message);
                    }
                });
            }
        };
        $('#done-bind').on('click', function(){
            if(!v.barcode || !v.wkey){
                $.util.error("请选择待绑定二维码和任务单");
                return false;
            }
            var parmas = {id: v.wkey , barcode: v.barcode};
            var deliveryNumber = $('#deliveryNumber');
            if(!deliveryNumber.html()){
                var html ='<form action=""><div class="form-group"><input type="text" placeholder="送货单号" class="name form-control"/></div></form>';
                $.util.form('输入送货单号', html, null, function () {
                    var editBody = this.$body;
                    var deliveryVal = $.trim(editBody.find("input").val());
                    if (!deliveryVal) {
                        $.util.error("请输入送货单号");
                        return false;
                    }
                    deliveryNumber.html(deliveryVal);
                    parmas.deliveryNumber = deliveryVal;
                    bindCode(parmas);
                });
            }else{
                parmas.deliveryNumber = deliveryNumber.html();
                $.util.confirm('操作确认','二维码和任务单绑定确认', function(){
                    bindCode(parmas);
                })
            }
        });

        var bindCode = function(parmas){
            $.util.json(base_url + '/backstage/driver/bind/barcode' , parmas, function (data) {
                if (data.success) {//处理返回结果
                    $.util.success(data.message);
                    listCode(1);
                    v.barcode = v.wkey = null;
                } else {
                    $.util.error(data.message);
                }
            });
        }
        listCode(1);
    });
</script>
</html>