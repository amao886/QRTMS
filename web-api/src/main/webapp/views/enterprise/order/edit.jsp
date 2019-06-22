<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-订单管理-编辑订单</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}element-ui/lib/theme-chalk/index.css"/>
    <link rel="stylesheet" href="${baseStatic}css/shipperDetail.css?times=${times}"/>
    <style type="text/css">
        .plan-content {
            padding: 5px 24px 50px 30px;
            background: #fff;
            min-height: 100%;
            color: #636363;
        }

        .two {
            float: left;
            width: 32%;
        }
        .detail-address {
            width: 35%;
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
            margin-bottom: 15px;
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
            text-indent: 10px;
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
            width: 90px
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
            width: 90px
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

        /*  表单样式*/
        .el-input__inner {
            height: 26px;
            line-height: 26px;
        }

        .el-input__icon {
            line-height: 26px;
        }

        input {
            border: 1px solid #ccc;
            height: 26px;
            padding: 0 5px;
            border-radius: 5px;
            font-size: 14px;
            width: 60%;
        }

        .el-date-editor {
            text-indent: 0px;
        }

        .el-input--suffix {
            text-indent: 0px;
        }

        .large {
            width: 40%;
        }

        .el-icon-delete, .el-icon-circle-plus-outline {
            font-size: 16px;
        }

        .submit, .cancel {
            width: 100px;
            height: 40px;
            line-height: 40px;
            color: #fff;
            outline: none;
            border: 1px solid #169BD5;
            border-radius: 5px;
        }

        .submit {
            background-color: #169BD5;
            margin-right: 15px;
        }

        .cancel {
            background: #fff;
            color: #169BD5;
        }

        .footer {
            margin-top: 50px;
            text-align: center;
        }
        .describe input{
            border: 0 !important;
            text-align: center;
        }
        .el-autocomplete {
            width: 70%;
        }
    </style>
</head>
<body data-key="${orderKey}" data-type="${type}">
<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
<!--[if lt IE 9]>
<script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
<script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<div id="plan-content" class="plan-content">
    <div>
        <%--发货信息--%>
        <div class="part-wrapper">
            <div class="title">发货信息</div>
            <div class="line-part ">
                <div class="text-tag two">
                    <span class="float-tag">发货方:</span>
                    <p class="p-tag">
                        <input type="text" v-model="order.shipperName" readonly>
                    </p>
                </div>

                <div class="text-tag two">
                    <span class="float-tag">发货日期:</span>
                    <p class="p-tag">
                        <el-date-picker v-model="order.deliveryTime" type="date" placeholder="选择年月日">
                        </el-date-picker>
                    </p>
                </div>
            </div>

            <div class="line-part">
                <div class="text-tag two">
                    <span class="float-tag">订单编号:</span>
                    <p class="p-tag">
                        <input type="text" v-model="order.orderNo">
                    </p>
                </div>
                <div class="text-tag two">
                    <span class="float-tag">送货单号:</span>
                    <p class="p-tag">
                        <input type="text" v-model="order.deliveryNo">
                    </p>
                </div>
            </div>

            <div class="line-part">
                <div class="text-tag">
                    <span class="float-tag">发货地址：</span>
                    <p class="p-tag">
                        <el-select v-model="distributeAddress[0]" placeholder="--请选省份--" @change="selectChange(4, 1)">
                            <el-option v-for="item in areas.p4" :key="item.id" :label="item.name" :value="item.name"></el-option>
                        </el-select>
                        <el-select v-model="distributeAddress[1]" placeholder="--请选城市--" @change="selectChange(4, 2)">
                            <el-option v-for="item in areas.c4" :key="item.id" :label="item.name" :value="item.name"></el-option>
                        </el-select>
                        <el-select v-model="distributeAddress[2]" placeholder="--请选区县--" @change="selectChange(4, 3)">
                            <el-option v-if="areas.d4 != null" v-for="item in areas.d4" :key="item.id" :label="item.name" :value="item.name"></el-option>
                        </el-select>
                        <input type="text" v-model="distributeAddress[3]" class="detail-address">
                    </p>
                </div>
            </div>
        </div>
        <%--承运信息--%>
        <div class="part-wrapper">
            <div class="title">承运信息</div>
            <div class="line-part large-tag">
                <div class="text-tag two">
                    <span class="float-tag">物流商:</span>
                    <p class="p-tag">
                        <input type="text" v-model="order.conveyName" readonly>
                    </p>
                </div>
                <div class="text-tag two">
                    <span class="float-tag">联系人:</span>
                    <p class="p-tag">
                        <input type="text" v-model="extra.conveyerName">
                    </p>
                </div>

                <div class="text-tag two">
                    <span class="float-tag ">联系电话:</span>
                    <p class="p-tag">
                        <input type="text" v-model="extra.conveyerContact">
                    </p>
                </div>
            </div>
            <div class="line-part large-tag">
                <div class="text-tag two">
                    <span class="float-tag">要求到货时间:</span>
                    <p class="p-tag">
                        <el-date-picker v-model="order.arrivalTime" type="date" placeholder="选择年月日"></el-date-picker>
                    </p>
                </div>
            </div>
        </div>
        <%--收货信息--%>
        <div class="part-wrapper">
            <div class="title">收货信息</div>
            <div class="line-part">

                <div class="text-tag two">
                    <span class="float-tag">收货客户:</span>
                    <p class="p-tag">
                        <input type="text" v-model="order.receiveName" readonly>
                    </p>
                </div>

                <div class="text-tag two">
                    <span class="float-tag">收货人:</span>
                    <p class="p-tag">
                        <input type="text" v-model="order.receiverName" class="large">
                    </p>
                </div>
                <div class="text-tag two">
                    <span class="float-tag">联系方式:</span>
                    <p class="p-tag">
                        <input type="text" v-model="order.receiverContact" class="large">
                    </p>
                </div>
            </div>
            <div class="line-part">
                <div class="text-tag">
                    <span class="float-tag">收货地址：</span>
                    <p class="p-tag">
                        <el-select v-model="receiveAddress[0]" placeholder="--请选省份--" @change="selectChange(1, 1)">
                            <el-option v-for="item in areas.p1" :key="item.id" :label="item.name" :value="item.name"></el-option>
                        </el-select>
                        <el-select v-model="receiveAddress[1]" placeholder="--请选城市--" @change="selectChange(1, 2)">
                            <el-option v-for="item in areas.c1" :key="item.id" :label="item.name" :value="item.name"></el-option>
                        </el-select>
                        <el-select v-model="receiveAddress[2]" placeholder="--请选区县--" @change="selectChange(1, 3)">
                            <el-option v-if="areas.d1 != null" v-for="item in areas.d1" :key="item.id" :label="item.name" :value="item.name"></el-option>
                        </el-select>
                        <input type="text" v-model="receiveAddress[3]" class="detail-address">
                    </p>
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
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="(c, index) in commodities">
                        <td>
                            <el-input v-model="c.commodityNo" placeholder="请输入内容" class="describe"></el-input>
                        </td>
                        <td>
                            <el-input v-model="c.commodityName" placeholder="请输入内容" class="describe"></el-input>
                        </td>
                        <td>
                            <el-input v-model="c.commodityUnit" placeholder="请输入内容" class="describe"></el-input>
                        </td>
                        <td>
                            <el-input-number v-model="c.quantity" controls-position="right" :min="0" size="mini"></el-input-number>
                        </td>
                        <td>
                            <el-input-number v-model="c.boxCount" controls-position="right" :min="0" size="mini"></el-input-number>
                        </td>
                        <td>
                            <el-input-number v-model="c.volume" :precision="2" :step="0.01" :min="0" size="mini"></el-input-number>
                        </td>
                        <td>
                            <el-input-number v-model="c.weight" :precision="2" :step="0.01" :min="0" size="mini"></el-input-number>
                        </td>
                        <td>
                            <el-input v-model="c.remark" placeholder="请输入内容" class="describe"></el-input>
                        </td>
                        <td>
                            <span class="el-icon-circle-plus-outline" @click="changeCommodity()"></span>
                            <span class="el-icon-delete font" @click="changeCommodity(index)"></span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <%--运输信息--%>
        <div class="part-wrapper">
            <div class="title">运输信息</div>
            <div class="line-part">
                <div class="text-tag ">
                    <span class="float-tag">始发地:</span>
                    <p class="p-tag">
                        <el-select v-model="originStation[0]" placeholder="--请选省份--" @change="selectChange(2, 1)">
                            <el-option v-for="item in areas.p2" :key="item.id" :label="item.name" :value="item.name"></el-option>
                        </el-select>
                        <el-select v-model="originStation[1]" placeholder="--请选城市--" @change="selectChange(2, 2)">
                            <el-option v-for="item in areas.c2" :key="item.id" :label="item.name" :value="item.name"></el-option>
                        </el-select>
                        <el-select v-model="originStation[2]" placeholder="--请选区县--" @change="selectChange(2, 3)">
                            <el-option v-if="areas.d2 != null" v-for="item in areas.d2" :key="item.id" :label="item.name" :value="item.name"></el-option>
                        </el-select>
                    </p>
                </div>
            </div>
            <div class="line-part">
                <div class="text-tag ">
                    <span class="float-tag">目的地:</span>
                    <p class="p-tag">
                        <el-select v-model="arrivalStation[0]" placeholder="--请选省份--" @change="selectChange(3, 1)">
                            <el-option v-for="item in areas.p3" :key="item.id" :label="item.name" :value="item.name"></el-option>
                        </el-select>
                        <el-select v-model="arrivalStation[1]" placeholder="--请选城市--" @change="selectChange(3, 2)">
                            <el-option v-for="item in areas.c3" :key="item.id" :label="item.name" :value="item.name"></el-option>
                        </el-select>
                        <el-select v-model="arrivalStation[2]" placeholder="--请选区县--" @change="selectChange(3, 3)">
                            <el-option v-if="areas.d3 != null" v-for="item in areas.d3" :key="item.id" :label="item.name" :value="item.name"></el-option>
                        </el-select>
                    </p>
                </div>
            </div>
            <div class="line-part">
                <div class="text-tag two">
                    <span class="float-tag">车牌号:</span>
                    <p class="p-tag">
                        <input type="text" v-model="extra.careNo">
                    </p>
                </div>

                <div class="text-tag two">
                    <span class="float-tag">司机姓名:</span>
                    <p class="p-tag">
                        <input type="text" v-model="extra.driverName">
                    </p>
                </div>

                <div class="text-tag two">
                    <span class="float-tag">司机电话:</span>
                    <p class="p-tag">
                        <input type="text" v-model="extra.driverContact">
                    </p>
                </div>
            </div>
            <div class="line-part">

                <div class="text-tag two">
                    <span class="float-tag">发站:</span>
                    <p class="p-tag">
                        <input type="text" v-model="extra.startStation">
                    </p>
                </div>

                <div class="text-tag two">
                    <span class="float-tag">到站:</span>
                    <p class="p-tag">
                        <input type="text" v-model="extra.endStation">
                    </p>
                </div>
            </div>
        </div>
        <%--财务信息--%>
        <div class="part-wrapper">
            <div class="title">财务信息</div>
            <!--订单备注-->
            <div class="line-part large-tag">
                <div class="text-tag two">
                    <span class="float-tag ">收入:</span>
                    <p class="p-tag">
                        <input type="text" v-model="extra.income">
                    </p>
                </div>
                <div class="text-tag two">
                    <span class="float-tag">运输成本:</span>
                    <p class="p-tag">
                        <input type="text" v-model="extra.expenditure">
                    </p>
                </div>
            </div>
        </div>
        <%--其他信息--%>
        <div class="part-wrapper">
            <div class="title">其他</div>
            <!--订单备注-->
            <div class="line-part large-tag">
                <div class="text-tag two">
                    <span class="float-tag">发货单备注:</span>
                    <p class="p-tag">
                        <input type="text" v-model="order.remark">
                    </p>
                </div>
            </div>
            <div class="line-part large-tag" v-if="customDatas != null && customDatas.length > 0">
                <div class="text-tag two" v-for="c in customDatas">
                    <span class="float-tag">{{c[0]}}:</span>
                    <p class="p-tag">
                        <input type="text" v-model="c[1]">
                    </p>
                </div>
            </div>
        </div>
        <div class="footer">
            <button class="submit" @click="submitSomething">提交</button>
        </div>
    </div>
    </template>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
<script>
    $(document).ready(function () {
        new Vue({
            el: '#plan-content',
            data: {
                key: $('body').data('key'),
                type: $('body').data('type'),
                areas:{
                    p1 :[], p2 :[], p3 :[], p4 :[],
                    c1 :[], c2 :[], c3 :[], c4 :[],
                    d1 :[], d2 :[], d3 :[], d4 :[]
                },
                order: {},
                extra: {},
                commodities: [],
                customDatas: [],
                receiveAddress:[],
                distributeAddress:[],
                originStation:[],
                arrivalStation:[],
                templateKey: '',//当前选择的模板
                templates: [],//模板数组
                required: {},
                optional: {},
                cache: new Map()
            },
            methods: {
                changeCommodity:function(index){
                    var _this = this;
                    if(index ===0 || index > 0){
                        if(_this.commodities.length <= 1){
                            $.util.warning("至少保留一条货物信息");
                        }else{
                            _this.commodities.splice(index, 1);
                        }
                    }else{
                        _this.commodities.push({commodityNo: '', commodityName: '', commodityUnit: '', quantity: 0, boxCount: 0, volume: 0, weight: 0, remark: ''})
                    }
                },
                loadArea:function(pid, callback){
                    var _this = this;
                    if(_this.cache.has(pid)){
                        if (callback) {
                            callback.call(_this, _this.cache.get(pid));
                        }
                    }else{
                        $.util.json('/special/support/area', {parentid: pid}, function (info) {
                            if (info.success) {
                                if(info.result && info.result.length > 0){
                                    _this.cache.set(pid, info.result);
                                }
                                if (callback) {
                                    callback.call(_this, info.result);
                                }
                            } else {
                                $.util.error(info.message);
                            }
                        });
                    }
                },
                indexOfName:function(arrays, name){
                    for(var i = 0; i <arrays.length; i++){
                        if(name === arrays[i].name){
                            return arrays[i].id;
                        }
                    }
                    return null;
                },
                findArrays:function(index){
                    var _this = this;
                    var arrays = (index === 1 ?  _this.receiveAddress : (index === 2 ? _this.originStation : ( index === 3? _this.arrivalStation : _this.distributeAddress)));
                    if(!arrays || arrays.length <= 0){
                        arrays = ['','','',''];
                    }
                    return arrays;
                },
                //点击加载城市(index 1:收货地址 2:始发地 3:目的地 4:发货地址)
                selectChange: function (index, type, callback) {
                    var _this = this, arrays = _this.findArrays(index);
                    //type: 1-省份变化, 2-城市变化, 3-地区变化
                    if (type === 1) {
                        arrays[1] = arrays[2] = '';
                        _this.areas['c' + index] = _this.areas['d' + index] = [];
                        var pid = _this.indexOfName(_this.areas['p'+ index], arrays[0]);
                        _this.loadArea(pid, function(result){
                            _this.areas['c' + index] = result;
                            if (callback) {
                                callback.call(_this, _this.areas['c' + index]);
                            }
                        });
                    } else if (type === 2) {
                        arrays[2] = '';
                        _this.areas['d' + index] = [];
                        var cid = _this.indexOfName(_this.areas['c'+ index], arrays[1]);
                        _this.loadArea(cid, function(result){
                            _this.areas['d' + index] = result;
                            if (callback) {
                                callback.call(_this, _this.areas['d' + index]);
                            }
                        });
                    }
                },
                initializeArea:function(){
                    var _this = this;
                    for(let i = 1; i <= 4 ; i++){
                        let arrays = _this.findArrays(i);
                        let city = arrays[1], county = arrays[2];
                        _this.selectChange(i, 1, function(){
                            arrays[1] = city;
                            _this.selectChange(i, 2, function(){
                                arrays[2] = county;
                            })
                        })
                    }
                },
                submitSomething:function(){
                    var _this = this;
                    function joinArrays(arrays){
                        var sss = '';
                        for (let obj of arrays) {
                            sss += $.trim(obj);
                        }
                        return sss;
                    };
                    _this.order.receiveAddress = joinArrays(_this.receiveAddress);
                    _this.extra.distributeAddress = joinArrays(_this.distributeAddress);
                    _this.extra.originStation = joinArrays(_this.originStation);
                    _this.extra.arrivalStation = joinArrays(_this.arrivalStation);

                    var customs = [];
                    if(_this.customDatas && _this.customDatas.length > 0){
                        for (const x of _this.customDatas) {
                            customs.push({customName: x[0], customValue: x[1]});
                        }
                    }
                    var parmas = {
                        orderKey: _this.key,
                        order : JSON.stringify(_this.order),
                        extra : JSON.stringify(_this.extra),
                        commodities : JSON.stringify(_this.commodities),
                        customDatas : JSON.stringify(customs)
                    };
                    $.util.json("/enterprise/order/"+ (_this.type === 1 ? 'shipper' : 'coveyer') +"/edit/order", parmas, function (data) {
                        if (data.success) {
                            $.util.success(data.message, function(){
                                if(parent.layer){
                                    parent.layer.close(parent.layer.getFrameIndex(window.name));
                                }
                            }, 3000);
                        } else {
                            $.util.error(data.message);
                        }
                    });
                }
            },
            created:function(){
                var _this = this;
                _this.loadArea(0, function(result){
                    for(let i = 1; i <= 4 ; i++){
                        _this.areas['p' + i] = result;
                    }
                });
            },
            mounted: function () {
                var _this = this;
                $.util.json("/enterprise/order/convert/order", {orderKey:_this.key}, function (data) {
                    if (data.success) {
                        _this.orderKey = data.orderKey;
                        var results = data.results;
                        if (results) {
                            _this.order = results.order;
                            _this.extra = results.extra;
                            _this.commodities = results.commodities;
                            _this.customDatas = results.customDatas;

                            _this.receiveAddress = _this.order.receiveAddress;
                            _this.distributeAddress = _this.extra.distributeAddress;
                            _this.originStation = _this.extra.originStation;
                            _this.arrivalStation = _this.extra.arrivalStation;

                            _this.initializeArea();
                        }
                    } else {
                        $.util.error(data.message);
                    }
                });
                $.util.json("/enterprise/customer/list", {}, function (data) {
                    if (data.success) {
                        _this.customers = data.customers;
                    } else {
                        $.util.error(data.message);
                    }
                });
            }
        });
    });
</script>
</html>