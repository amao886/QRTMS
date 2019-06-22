<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>合同物流管理平台-订单录入</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/entering.css?times=${times}"/>
    <link rel="stylesheet" href="${baseStatic}element/css/index.css"/>
    <style type="text/css">
        /*.el-table__body tr.current-row > td {
              background-color: #409EFF !important;
          }*/
        /*.el-input__inner {
            height: 30px;
            line-height: 30px;
            width: 90%;
        }*/
    </style>
</head>
<body>
<div id="order-entrance-wrapper">
    <!--  -------------------------------------------------->
    <div>
        <el-dialog title="请选择录入发货模板" :visible.sync="dialogFormVisible" :show-close="false" :close-on-press-escape="false" :close-on-click-modal="false" width="800px">
            <el-table :data="templates" height="400"  highlight-current-row @current-change="selectedTempChange"  >
                <el-table-column property="name" label="模板名称"></el-table-column>
                <el-table-column property="mergeType" label="合并方式" width="200">
                    <template slot-scope="scope">
                        <span>{{ scope.row.mergeType == 0 ? '不合并' : scope.row.mergeType == 1 ? '按送货单号合并' : '按收货信息合并' }}</span>
                    </template>
                </el-table-column>
                <el-table-column property="fettle" label="常用" width="100">
                    <template slot-scope="scope">
                        <el-tag type="success" size="small" v-if="scope.row.fettle == 2">常用</el-tag>
                        <el-tag type="info" size="small" v-if="scope.row.fettle == 1">不常用</el-tag>
                    </template>
                </el-table-column>
            </el-table>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="selectTemplate">确 定</el-button>
            </div>
        </el-dialog>
    </div>
    <!--        ------------------------------------    v-show="isshow"     -->
    <div class="box" v-if="show">
        <el-form ref="form" :model="form" label-width="100px" label-position="left">
            <!--   -->
            <div>
                <el-form-item label="发货方：" class="inline right left" v-if="required.shipperName != null">
                    <el-autocomplete class="inline-input" v-model="form.shipperName" placeholder="请输入内容"
                            :fetch-suggestions="querySearch" :trigger-on-focus="false" @select="handleSelect" @focus="changeType(1)">
                    </el-autocomplete>
                </el-form-item>
                <el-form-item label="发货时间：" class="inline " v-if="required.deliveryTime != null">
                    <el-date-picker type="date" placeholder="选择日期" v-model="form.deliveryTime" value-format="yyyy-MM-dd"></el-date-picker>
                </el-form-item>
            </div>
            <!--  -->
            <div>
                <el-form-item label=" 发货地址：" v-if="required.distributeAddress != null">
                    <select id="province3" @change="loadCity(4, 1)" ref='CityId3' v-model="areaSelects.p4">
                        <option value="">--请选省份--</option>
                        <option v-for="item in areas.p4" :key="item.id"  :value="item.id">
                            {{item.name}}
                        </option>
                    </select>
                    <!--  -->
                    <select id="city3" @change="loadCity(4, 2)" ref="CountyId3" v-model="areaSelects.c4">
                        <option value="">--请选城市--</option>
                        <option v-for="item in areas.c4" :key="item.id"  :value="item.id">{{item.name}}</option>
                    </select>

                    <select id="county3" v-model='areaSelects.d4' @change="loadCity(4, 3)">
                        <option value="">--请选区县--</option>
                        <option v-if="areas.d4 != null" v-for="item in areas.d4" :key="item.id"  :value="item.id">
                            {{item.name}}
                        </option>
                    </select>
                    <el-input v-model="form.distributeAddress" placeholder="详细地址" class="address"></el-input>
                </el-form-item>
            </div>
            <!--  -->
            <div>
                <el-form-item label=" 始发地：" v-if="required.originStation != null">
                    <!-- 省 -->
                    <select id="province1" @change="loadCity(2, 1)" ref='CityId1' v-model="areaSelects.p2">
                        <option value="">--请选省份--</option>
                        <option v-for="item in areas.p2" :key="item.id"  :value="item.id">
                            {{item.name}}
                        </option>
                    </select>

                    <!-- 市 -->
                    <select id="city1" @change="loadCity(2, 2)" ref="CountyId1" v-model="areaSelects.c2">
                        <option value="">--请选城市--</option>
                        <option v-for="item in areas.c2" :key="item.id"  :value="item.id">
                            {{item.name}}
                        </option>
                    </select>
                    <!-- 区县 -->
                    <select id="county1" v-model='areaSelects.d2' @change="loadCity(2, 3)">
                        <option value="">--请选区县--</option>
                        <option v-if="areas.d2 != null" v-for="item in areas.d2" :key="item.id"  :value="item.id">
                            {{item.name}}
                        </option>
                    </select>
                </el-form-item>
            </div>
            <!--   -->
            <div>

                <el-form-item label="发站：" id="forwarding" class="inline left" v-if="required.startStation != null">
                    <el-input v-model="form.startStation" placeholder="请输入发站"></el-input>
                </el-form-item>
                <el-form-item label="到站：" id="draw" class="inline" v-if="required.endStation != null">
                    <el-input v-model="form.endStation" placeholder="请输入发站"></el-input>
                </el-form-item>
            </div>
            <!--  -->
            <div>
                <el-form-item label="送货单号：" class="inline left" v-if="required.deliveryNo != null">
                    <el-input v-model="form.deliveryNo"></el-input>
                </el-form-item>
                <el-form-item label="订单编号：" class="inline " v-if="required.orderNo != null">
                    <el-input v-model="form.orderNo"></el-input>
                </el-form-item>
            </div>
            <!--  -->
            <div>
                <el-form-item label="收货客户：" class="inline left" v-if="required.receiveName != null">
                    <el-autocomplete class="inline-input" v-model="form.receiveName" placeholder="请输入内容"
                                     :fetch-suggestions="querySearch" :trigger-on-focus="false" @select="handleSelect" @focus="changeType(2)">
                    </el-autocomplete>
                </el-form-item>
                <el-form-item label="收货人：" class="inline left" v-if="required.receiverName != null">
                    <el-input v-model="form.receiverName" id="signerId"></el-input>
                </el-form-item>
                <el-form-item label="联系方式：" class="inline " v-if="required.receiverContact != null">
                    <el-input v-model="form.receiverContact" id="interflowId1"></el-input>
                </el-form-item>
            </div>
            <!--  -->
            <div>
                <el-form-item label="收货地址：" v-if="required.receiveAddress != null">
                    <!-- 省 -->
                    <select id="province" @change="loadCity(1, 1)" ref='CityId' v-model="areaSelects.p1">
                        <option value="">--请选省份--</option>
                        <option v-for="item in areas.p1" :key="item.id"  :value="item.id">
                            {{item.name}}
                        </option>
                    </select>

                    <!-- 市 -->
                    <select id="city" @change="loadCity(1, 2)" ref="CountyId" v-model="areaSelects.c1">
                        <option value="">--请选城市--</option>
                        <option v-for="item in areas.c1" :key="item.id"  :value="item.id">
                            {{item.name}}
                        </option>
                    </select>
                    <!-- 区县 -->
                    <select id="county" ref="county" v-model='areaSelects.d1' @change="loadCity(1, 3)">
                        <option value="">--请选区县--</option>
                        <!-- v-if="this.form.county ==''?return false:'v-for="item in county "'" -->
                        <option v-for="item in areas.d1" :key="item.id"  :value="item.id">
                            {{item.name}}
                        </option>
                    </select>
                    <el-input v-model="form.receiveAddress" placeholder="详细地址" class="address"></el-input>
                </el-form-item>
            </div>
            <!--  -->
            <div>
                <el-form-item label=" 目的地：" v-if="required.arrivalStation != null">
                    <!-- 省 -->
                    <select id="province2" @change="loadCity(3, 1)" ref='CityId2' v-model="areaSelects.p3">
                        <option value="">--请选省份--</option>
                        <option v-for="item in areas.p3" :key="item.id"  :value=" item.id ">
                            {{item.name}}
                        </option>
                    </select>

                    <!-- 市 -->
                    <select id="city2" @change="loadCity(3, 2)" ref="CountyId2" v-model="areaSelects.c3">
                        <option value="">--请选城市--</option>
                        <option v-for="item in areas.c3" :key="item.id"  :value="item.id">
                            {{item.name}}
                        </option>
                    </select>
                    <!-- 区县 -->
                    <select id="county2" v-model='areaSelects.d3' @change="loadCity(3, 3)">
                        <option value="">--请选区县--</option>
                        <option v-if="areas.d3 != null" v-for="item in areas.d3" :key="item.id"  :value="item.id">
                            {{item.name}}
                        </option>
                    </select>
                </el-form-item>
            </div>
            <!--  -->
            <%----%>
            <div>

                <table class="entrance-table">
                    <tr>
                        <th class="description" v-if="required.commodityNo != null">物料编号</th>
                        <th class="description" v-if="required.commodityName != null">产品描述</th>
                        <th class="description" v-if="required.commodityUnit != null">单位</th>
                        <th class="description" v-if="required.quantity != null">数量(件)</th>
                        <th class="description" v-if="required.boxCount != null"> 箱数</th>
                        <th class="description" v-if="required.volume != null">体积(m³)</th>
                        <th class="description" v-if="required.weight != null">重量(㎏)</th>
                        <th class="description" v-if="required.remark != null">备注</th>
                        <th class="description" v-if="show" style="width: 80px;">操作</th>
                    </tr>
                    <tr v-for="(item,index) in list" :key="index" :value='index' style="text-align: center;">
                        <td class="depict" v-if="required.commodityNo != null" style="text-align: center;">
                            <el-input v-model="item.commodityNo" placeholder="请输入内容" class="describe"></el-input>
                        </td>
                        <td class="depict" v-if="required.commodityName != null">
                            <el-input v-model="item.commodityName" placeholder="请输入内容" class="describe"></el-input>
                        </td>
                        <td class="depict" v-if="required.commodityUnit != null">
                            <el-input v-model="item.commodityUnit" placeholder="请输入内容" class="describe"></el-input>
                        </td>
                        <td class="depict" v-if="required.quantity != null">
                            <el-input-number v-model="item.quantity" controls-position="right" :min="0" size="mini"></el-input-number>
                        </td>
                        <td class="depict" v-if="required.boxCount != null">
                            <el-input-number v-model="item.boxCount" controls-position="right" :min="0" size="mini"></el-input-number>
                        </td>
                        <td class="depict" v-if="required.volume != null">
                            <el-input-number v-model="item.volume" :precision="2" :step="0.01" :min="0" size="mini"></el-input-number>
                        </td>
                        <td class="depict" v-if="required.weight != null">
                            <el-input-number v-model="item.weight" :precision="2" :step="0.1" :min="0" size="mini"></el-input-number>
                        </td>
                        <td class="depict" v-if="required.remark != null">
                            <el-input v-model="item.remark" placeholder="请输入内容" class="describe"></el-input>
                        </td>
                        <td class="depict" v-if="show">
                            <span class="el-icon-circle-plus-outline" @click="addTable"></span>
                            <span class="el-icon-delete font " @click="delTable(index)"></span>
                            </el-input>
                        </td>

                    </tr>

                </table>
            </div>
            <!--  -->
            <div>
                <el-form-item label="物流商：" class="inline left" v-if="required.conveyName != null">
                    <el-autocomplete class="inline-input" v-model="form.conveyName" placeholder="请输入内容"
                                     :fetch-suggestions="querySearch" :trigger-on-focus="false" @select="handleSelect" @focus="changeType(3)">
                    </el-autocomplete>
                </el-form-item>
                <el-form-item label="联系人：" class="inline left" v-if="required.conveyerName != null">
                    <el-input v-model="form.conveyerName" id="ShipContactId"></el-input>
                </el-form-item>
                <el-form-item label="联系电话：" class="inline " v-if="required.conveyerContact != null">
                    <el-input v-model="form.conveyerContact" id="interflowId"></el-input>
                </el-form-item>
            </div>
            <!--  -->
            <div>
                <el-form-item label="车牌号：" class="inline left" v-if="required.careNo != null">
                    <el-input v-model="form.careNo"></el-input>
                </el-form-item>
                <el-form-item label="司机姓名：" class="inline left"   v-if="required.driverName != null">
                    <el-input v-model="form.driverName"></el-input>
                </el-form-item>
                <el-form-item label="司机电话：" class="inline " v-if="required.driverContact != null">
                    <el-input v-model="form.driverContact"></el-input>
                </el-form-item>
            </div>
            <!--  -->
            <div>

                <el-form-item label="收入：" class="inline" v-if="required.income != null">
                   <el-input v-model="form.income" placeholder="请输入收入"><template slot="append">元</template></el-input>
                </el-form-item>
                <el-form-item label="运输成本：" class="inline" v-if="required.expenditure != null" style="margin-left: 41px;">
                    <el-input v-model="form.expenditure" placeholder="请输入运输成本"><template slot="append">元</template></el-input>
                </el-form-item>
            </div>
            <!--  -->

            <%----%>
            <div>
                <el-form-item class="inline"  v-if="optional != null && optional.length > 0" v-for="(item, index) in optional" :class="{left : index % 2 == 0}" :label='item[0]+":"'>
                    <el-input :id=item[1]></el-input>
                </el-form-item>
            </div>
            <!--  -->
            <div>
                <el-form-item class="bao">
                    <el-button type="primary" class="baocun" @click="saveForm">保存</el-button>
                </el-form-item>

            </div>
            <!--  -->
        </el-form>
    </div>
    <el-dialog :title="selectAddressTitle" :visible.sync="selectAddressVisible" :close-on-press-escape="false" :close-on-click-modal="false">
        <el-table :data="selectAddress" highlight-current-row @current-change="selectedTempChange"  height="300">
           <%-- <el-table-column property="customerName" label="客户名称" width="150"></el-table-column>--%>
            <el-table-column property="contacts" label="联系人" width="100"></el-table-column>
            <el-table-column property="contactNumber" label="联系电话" width="150"></el-table-column>
            <el-table-column property="fullAddress" label="地址"></el-table-column>
        </el-table>
        <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="selectedAdddress(null)">确定</el-button>
        </span>
    </el-dialog>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
<script type="text/javascript" src="${baseStatic}vue/vue-resource.js"></script>


<script>
    function endWith(str, endStr){
        var d = str.length - endStr.length;
        if(str.lastIndexOf(endStr) !== d){
            return str + endStr;
        }
        return str;
    }
    $(document).ready(function () {
        var vm = new Vue({
            el: '#order-entrance-wrapper',
            data: {
                template: null,
                selectAddressTitle:'',
                selectAddressVisible: false,
                selectAddress:[],
                customerType: 1,
                areas:{
                    p1 :[], p2 :[], p3 :[], p4 :[],
                    c1 :[], c2 :[], c3 :[], c4 :[],
                    d1 :[], d2 :[], d3 :[], d4 :[]
                },
                areaSelects:{
                    p1 :'', p2 :'', p3 :'', p4 :'',
                    c1 :'', c2 :'', c3 :'', c4 :'',
                    d1 :'', d2 :'', d3 :'', d4 :''
                },
                dialogFormVisible: true,
                //操作
                show: false,
                templates: [],
                required: [],
                optional: [],
                customers: [],
                list: [{
                    commodityNo: null, commodityName: null, commodityUnit: null, quantity: null, boxCount: null, volume: null, weight: null, remark: null
                }],
                form: {
                    shipperName: null, //发货方
                    deliveryTime:null,  //发货时间
                    endStation : null,//到站
                    startStation: null,  //发站
                    deliveryNo: null,//运单单号
                    orderNo: null,//订单编号
                    receiveAddress:null,//详细地址
                    receiverName: null,//收获客户
                    receiveName: null,//收货人
                    receiverContact: null,//联系方式,
                    distributeAddress:null,//详细地址
                    conveyerName: null,//物流商
                    conveyName: null,//联系人
                    conveyerContact: null,//联系电话
                    careNo: null,  //车牌号
                    driverName: null,//司机姓名
                    driverContact:null,//司机电话
                    income: null,//收入
                    expenditure: null,//运输成本
                    arrivalStation:null,//目的地
                    originStation: null //始发地
                }
            },
            created: function () {
                var _this = this;
                _this.getModelListData();
            },
            computed: {
                typeArr: {
                    get: function () {
                        var _this = this;
                        var typeArr = [[],[],[]];
                        var typeObj = _this.customers.reduce(function (prev, current) {
                            if (typeof prev[current.type] === 'undefined') {
                                prev[current.type] = [{'value': current.name, 'id': current.key}];
                            } else {
                                prev[current.type].push({'value': current.name, 'id': current.key});
                            }
                            return prev;
                        }, {});
                        Object.keys(typeObj).forEach(function (k) {
                            typeArr[k - 1] = typeObj[k];
                        });
                        return typeArr;
                    }
                }
            },
            methods: {
                changeType:function(ctype){
                    this.customerType = ctype;
                },
                createFilter: function (queryString) {
                    return function (customers) {
                        return (customers.value.toLowerCase().indexOf(queryString.toLowerCase()) >= 0);
                    };
                },
                querySearch: function (queryString, cb) {
                    var _this = this;
                    var customers = _this.typeArr[_this.customerType - 1];
                    if(customers){
                        // 调用 callback 返回建议列表的数据
                        cb(queryString ? customers.filter(this.createFilter(queryString)) : customers);
                    }
                },
                handleSelect: function (item) {
                    var _this = this;
                    $.util.json(base_url + "/enterprise/customer/address/list", {customerId: item.id}, function (data) {
                        if (data.success) {
                            if(!data.addresss || data.addresss.length <= 0){
                                return;
                            }
                            if(data.addresss.length === 1){
                                _this.selectedAdddress(data.addresss[0]);
                            }else{
                                _this.selectAddressTitle = _this.customerType == 2 ? '选择收货客户' : _this.customerType == 1 ? '选择发货方' : '选择物流商';
                                _this.selectAddressVisible = true;
                                _this.selectAddress = data.addresss;
                            }
                        } else {
                            $.util.error(data.message);
                        }
                    });
                },
                selectedTempChange:function(selectItem){
                    this.template = selectItem;
                },
                selectedAdddress:function(address){//选择一个地址
                    var _this = this;
                    if(!address){
                        _this.selectAddressVisible = false;
                        address =_this.selectedTemp;
                    }
                    if(_this.customerType === 1){
                        _this.form.distributeAddress = address.address;

                        _this.setArea(4, address.province, address.city, address.district);//发货地址
                        _this.setArea(2, address.province, address.city, address.district);//始发地
                    }
                    if(_this.customerType === 2){
                        _this.form.receiverName = address.contacts;
                        _this.form.receiverContact = address.contactNumber;
                        _this.form.receiveAddress = address.address;

                        _this.setArea(1, address.province, address.city, address.district);//收货地址
                        _this.setArea(3, address.province, address.city, address.district);//目的地
                    }
                    if(_this.customerType === 3){
                        _this.form.conveyerContact = address.contactNumber;//物流商联系电话
                        _this.form.conveyerName = address.contacts;//物流商联系人
                    }
                },
                getModelListData: function () {
                    var _this = this;
                    $.util.json(base_url + "/enterprise/template/list", {category:1}, function (data) {
                        if (data.success) {
                            _this.templates = data.templates;
                        } else {
                            $.util.error(data.message);
                        }
                    });
                },
                joinArea: function (index) {
                    var _this = this;
                    var findItem = function(arrays, id){
                        if(arrays && arrays.length > 0){
                            for(var i= 0; i<arrays.length; i++){
                                var item = arrays[i];
                                if(id == item.id){
                                    return item;
                                }
                            }
                        }
                        return null;
                    }
                    var address = '';
                    $.each(['p', 'c', 'd'], function(i, v){
                        var item = findItem(_this.areas[v + index], _this.areaSelects[v+ index]);
                        if(item && item.name){
                            address += item.name;
                        }
                    })
                    return address;
                },
                getCustomerNameListData: function () {
                    var _this = this;
                    $.util.json(base_url + "/enterprise/customer/list", {
                        likeName: ""
                    }, function (data) {
                        if (data.success) {
                            _this.customers = data.customers;
                        } else {
                            $.util.error(data.message);
                        }
                    });
                },
                indexOfName:function(arrays, name){
                    for(var i = 0; i <arrays.length; i++){
                        if(name === arrays[i].name){
                            return arrays[i];
                        }
                    }
                    return null;
                },
                //点击加载城市(index 1:收货地址 2:始发地 3:目的地 4:发货地址)
                loadCity: function (index, type, callback) {
                    var _this = this;
                    //type: 1-省份变化, 2-城市变化, 3-地区变化
                    if(type === 1){
                        _this.areas['c'+ index] = [];
                        _this.areas['d'+ index] = [];
                        var p_id = _this.areaSelects['p'+ index];

                        $.util.json(base_url + '/special/support/area', {parentid: p_id}, function(info){
                            _this.areas['c'+ index] = info.result;
                            if(!isNaN(_this.areaSelects['c'+ index])){
                                _this.areaSelects['c'+ index] = _this.areaSelects['c'+ index];
                            }else{
                                _this.areaSelects['c'+ index] = '';
                            }
                            if(callback){
                                callback.call();
                            }else{
                                _this.areaSelects['c'+ index] = '';
                            }
                        });
                    }else if(type == 2){
                        _this.areas['d'+ index] = [];
                        var c_id = _this.areaSelects['c'+ index];
                        $.util.json(base_url + '/special/support/area', {parentid: c_id}, function(info){
                            _this.areas['d'+ index] = info.result;
                            if(!isNaN(_this.areaSelects['d'+ index])){
                                _this.areaSelects['d'+ index] = _this.areaSelects['d'+ index];
                            }else{
                                _this.areaSelects['d'+ index] = '';
                            }
                            if(callback){
                                callback.call();
                            }else{
                                _this.areaSelects['d'+ index] = '';
                            }
                        });
                    }
                },
                setArea:function(i, pn, cn, dn){
                    var _this = this;
                    var province = _this.indexOfName(_this.areas['p' + i] , pn);
                    _this.areaSelects['p'+ i] = province.id;
                    _this.loadCity(i, 1, function(){
                        if(!cn){ return; }
                        var city = _this.indexOfName(_this.areas['c' + i] , pn);
                        _this.areaSelects['c'+ i] = city.id;
                        _this.loadCity(i, 2, function(){
                            if(!dn){ return; }
                            var county = _this.indexOfName(_this.areas['d' + i] , dn);
                            _this.areaSelects['d'+ i] = county.id;
                        });
                    });
                },
                addTable: function () {
                    var _this = this;
                    _this.list.push({ contact: '' })
                },
                validate:function(key, value){
                    var _this = this;
                    if('receiveName' == key && !value){
                        _this.$message.error('收货客户不能为空');
                        return false;
                    }
                    if('deliveryTime' == key && !value){
                        _this.$message.error('发货时间不能为空');
                        return false;
                    }
                    if('receiverContact' == key && !value){
                        _this.$message.error('联系方式不能为空');
                        return false;
                    }
                    if('receiveAddress' == key && !value){
                        _this.$message.error('收货地址不能为空');
                        return false;
                    }
                    if('deliveryNo' == key && !value ){
                        _this.$message.error('送货单号不能为空');
                        return false;
                    }
                    return true;
                },
                saveForm: function () {
                    var _this = this;
                    var describes = [];
                    $.each(_this.form, function(i, item){
                        if(!_this.validate(i, item)){
                            return false;
                        }
                        if(_this.required[i]){
                            var value = $.trim(item);
                            //1:收货地址 2:始发地 3:目的地 4:发货地址
                            if('receiveAddress' == i){
                                value = $.trim(_this.joinArea(1)) + $.trim(item);
                            }else if('originStation' == i){
                                value = $.trim(_this.joinArea(2)) + $.trim(item);
                            }else if('arrivalStation' == i){
                                value = $.trim(_this.joinArea(3)) + $.trim(item);
                            }else if('distributeAddress' == i){
                                value = $.trim(_this.joinArea(4)) + $.trim(item);
                            }
                            describes.push({"detailKey": _this.required[i], "dataValue": value});
                        }
                    });
                    $.each(_this.list, function(index, item) {
                        $.each(item, function (j, v) {
                            if(_this.required[j]){
                                describes.push({"detailKey": _this.required[j], "dataValue": $.trim(v)});
                            }
                        })
                    });

                    $.each(_this.optional, function(i, v){
                        describes.push({"detailKey": v[1], "dataValue": $('#'+ v[1]).val()});
                    });
                    $.util.json(base_url + "/enterprise/template/entrance/conveyer", {
                        templateKey:  _this.template.key,
                        describes: JSON.stringify(describes)
                    }, function (data) {
                        if (data.success) {
                            $.util.success(data.message, function(){
                                if(parent.layer){
                                    parent.layer.close(parent.layer.getFrameIndex(window.name));
                                }
                            }, 3000);
                        }else{
                            _this.$message.error(data.message);
                        }
                    });
                },
                delTable:function(index) {
                    if (this.list.length <= 1) {
                        return;
                    }
                    this.list.splice(index, 1)
                },
                selectTemplate: function () {
                    var _this = this;
                    if (!_this.template) {
                        _this.$message.error('请选择录入发货模板');
                    }else{
                        _this.dialogFormVisible = false;
                        $.util.json(base_url + "/enterprise/template/templateInfo/" + _this.template.key, null, function (data) {
                            if (data.success) {
                                _this.required = data.required;
                                _this.optional = data.optional;
                                _this.show = true;
                            }
                        });
                    }
                }
            },
            mounted: function () {
                var _this = this;
                _this.getCustomerNameListData();
                $.util.json(base_url + '/special/support/area', {parentid:0}, function(info){
                    for(var i = 1; i <= 4 ; i++){
                        _this.areas['p'+ i] = info.result;
                    }
                });
            }
        })
    })
</script>
</html>