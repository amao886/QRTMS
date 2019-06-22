<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>物流跟踪-企业管理-员工管理</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/employee.css?times=${times}"/>
    <link rel="stylesheet" href="${baseStatic}element-ui/lib/theme-chalk/index.css"/>
    <style type="text/css">
        .manage{
            /*color: #e60000;*/
            font-weight: bold;
        }
        .selectseal{
            border: #409EFF solid 1px;
        }
        .companyseal-content ul li{
            float: left;
        }
        .fixed-element {
            position: fixed;
            right: 15px;
            top: 75px;
         }
        .img-label{
            height: 75px;
            line-height: 75px;
        }
        .item-label{
            color: grey;
        }
        .ta-right{
            text-align: right;
        }
        .el-icon-edit{
            cursor: pointer;
        }
        .el-row{
            height: 30px;
            margin-bottom: 20px;
        }
        .item-context-no{
            color: grey;
        }
        .el-table td, .el-table th {
            padding: 5px 0;
        }
        .employee-content .content {
            height: 100%;
        }
        .employee-content .content ul {
            height: 100%;
            overflow-y: scroll;
        }
        .manageAccount {
        	font-size: 16px;
        	color: #409EFF;
        }
    </style>
</head>
<body>
<div class="qlm-content" id="employee-content">
    <template>
        <div class="employee" v-if="company != null">
            <div class="employee-title">
                <div class="left">
                    <div class="input-group" id="searchFrom">
                        <input type="text" name="likeString" placeholder="请输入姓名" class="form-control" style="height: 32px;" v-model="likeString">
                        <div class="input-group-btn">
                            <button type="button" class="btn btn-default" @click="searchEmployees">查询</button>
                        </div>
                    </div>
                </div>
                <div class="right">
                    <a href="javascript:;" @click="showAdd">添加员工</a>
                </div>
            </div>
            <div class="employee-content">
                <div class="content-left" ref="wrapper">
                    <div class="content">
                        <h3>{{company.companyName}} <span>({{count}})</span></h3>
                        <ul>
                            <li v-for="(item, index) in employees" @click="getEmployee(item.employeeId, index)"
                                :class="{active: index === selectIndex}">
                                <span class="glyphicon glyphicon-user head_img" aria-hidden="true"></span>
                                <p :id="'name' + index" v-if="item.employeeType == 1" v-html="item.employeeName + '管理员'"></p>
                                <p :id="'name' + index" v-else v-html="item.employeeName"></p>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="content-right" ref="rightBox" v-if="employee != null">
                    <div class="menu-content" style="padding-left: 10px;">
                        <el-tabs v-model="activeName">
                            <el-tab-pane label="基本信息" name="first">
                                <el-row :gutter="20" style="height: 75px;">
                                    <el-col :span="4" class="ta-right"><div class="img-label item-label">头像</div></el-col>
                                    <el-col :span="16">
                                        <img v-if="employee.headImg != null" :src="employee.headImg" width="75px" height="75px">
                                        <img v-else src="/static/images/head_img.png" width="75px" height="75px">
                                    </el-col>
                                </el-row>
                                <el-row :gutter="20">
                                    <el-col :span="4" class="ta-right"><div class="item-label">员工名称</div></el-col>
                                    <el-col :span="16">
                                        <div class="item-context" v-show="!editModel">
                                            <span style="margin-right: 20px;">{{employee.employeeName}}</span>
                                            <i class="el-icon-edit" @click="editModel = true"></i>
                                        </div>
                                        <el-input style="width: 200px;" v-model="employee.employeeName" v-show="editModel" @blur="editModel=false" :autofocus="true"></el-input>
                                    </el-col>
                                </el-row>
                                <el-row :gutter="20">
                                    <el-col :span="4" class="ta-right"><div class="item-label">手机号</div></el-col>
                                    <el-col :span="16"><div class="item-context">{{employee.mobilephone}}</div></el-col>
                                </el-row>
                                <el-row :gutter="20">
                                    <el-col :span="4" class="ta-right"><div class="item-label">真实姓名</div></el-col>
                                    <el-col :span="16">
                                        <div class="item-context" v-if="employee.name != null">{{employee.name}}</div>
                                        <div class="item-context-no" v-else>暂未实名认证</div>
                                    </el-col>
                                </el-row>
                                <el-row :gutter="20">
                                    <el-col :span="4" class="ta-right"><div class="item-label">身份证</div></el-col>
                                    <el-col :span="16">
                                        <div class="item-context" v-if="employee.idCardNo != null">{{employee.idCardNo}}</div>
                                        <div class="item-context-no" v-else>暂未实名认证</div>
                                    </el-col>
                                </el-row>
                                <el-row :gutter="20">
                                    <el-col :span="4" class="ta-right"><div class="item-label">银行卡</div></el-col>
                                    <el-col :span="16">
                                        <div class="item-context" v-if="employee.brankCardNo != null">{{employee.brankCardNo}}</div>
                                        <div class="item-context-no" v-else>暂未实名认证</div>
                                    </el-col>
                                </el-row>
                                <el-row :gutter="20">
                                    <el-col :span="4" class="ta-right"><div class="item-label">认证状态</div></el-col>
                                    <el-col :span="16">
                                        <div class="item-context" v-if="employee.fettle == 2">已认证</div>
                                        <div class="item-context" v-else>未认证</div>
                                    </el-col>
                                </el-row>
                                <el-row :gutter="20" v-if="!manage">
                                    <el-col :span="4" class="ta-right"><div class="item-label">账号状态</div></el-col>
                                    <el-col :span="16">
                                        <el-radio v-model="employee.userFettle" :label=1>启用</el-radio>
                                        <el-radio v-model="employee.userFettle" :label=0>禁用</el-radio>
                                    </el-col>
                                </el-row>
                                <el-row :gutter="20" v-if="!manage">
                                    <el-col :span="8">
                                        <button type="button" class="btn btn-danger center-block" @click="deleteEmployee">删除员工</button>
                                    </el-col>
                                </el-row>
                            </el-tab-pane>
                            <el-tab-pane label="菜单权限" name="second">
                                <div class="menu-con">
                                    <div class="part" v-if="authorities != null && authorities.length > 0">
                                        <div class="part-item">
                                            <div class="line" v-for="(item, j) in authorities">
                                                <div class="line-part">
                                                    <el-tag>{{item.menuName}}</el-tag>
                                                </div>

                                                <div class="line-part">
                                                    <el-checkbox size="small" v-model="item.checked" label="全选" :value="item.id" @change="select(j)" border style="font-weight: bold;"></el-checkbox>
                                                </div>
                                                <div class="line-part" v-for="(child, i) in item.children">
                                                    <el-checkbox size="small" v-model="child.checked" :label="child.menuName" :value="child.id" @change="select(j, i)" border></el-checkbox>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </el-tab-pane>
                            <el-tab-pane label="客户权限" name="third">
                                <el-form :inline="true">
                                    <el-form-item>
                                        <el-input v-model="cusomer.likeName" placeholder="客户名称"></el-input>
                                    </el-form-item>
                                    <el-form-item>
                                        <el-select v-model="cusomer.type" placeholder="客户类型">
                                            <el-option label="全部类型" value=""></el-option>
                                            <el-option label="货主" value="1"></el-option>
                                            <el-option label="收货客户" value="2"></el-option>
                                            <el-option label="物流商" value="3"></el-option>
                                        </el-select>
                                    </el-form-item>
                                    <el-form-item>
                                        <el-select v-model="cusomer.reg" placeholder="注册状态">
                                            <el-option label="全部状态" value=""></el-option>
                                            <el-option label="未注册" value="0"></el-option>
                                            <el-option label="已注册" value="1"></el-option>
                                        </el-select>
                                    </el-form-item>
                                    <el-form-item>
                                        <el-button type="primary" @click="loadCustomers">查询</el-button>
                                    </el-form-item>
                                </el-form>
                                <el-table ref="customerTable" :data="customers" tooltip-effect="dark" style="width: 100%" height="400" @selection-change="customerSelect">
                                    <el-table-column type="selection" width="55"></el-table-column>
                                    <el-table-column prop="name" label="客户名称"></el-table-column>
                                    <el-table-column prop="sourceType" label="来源" width="120" :formatter="customerformat"></el-table-column>
                                    <el-table-column prop="type" label="客户类型" width="120" :formatter="customerformat"></el-table-column>
                                    <el-table-column prop="company" label="关联状态" width="120" :formatter="customerformat"></el-table-column>
                                </el-table>
                                <div style="margin-top: 5px"  @click="showSelect">
                                    已选择<span style="color: #e60000; cursor: pointer;">{{selectCustomers.length}}</span>项客户
                                </div>
                            </el-tab-pane>
                            <el-tab-pane label="公章授权" name="fourth">
                                <div class="sign-content" style="">
                                    <el-row>
                                        <el-col :span="4" v-for="(s, index) in seals" :key="s.id" :offset="index > 0 ? 1 : 0">
                                            <el-card :body-style="{ padding: '5px' }" :class="{selectseal : s.checked}">
                                                <div style="text-align: center;">
                                                    <img :src="'/res/'+ s.sealData" class="image" width="150" height="150">
                                                </div>
                                                <div style="text-align: center; padding-top: 10px;">
                                                    <span>{{s.companyName}}</span>
                                                </div>
                                                <div style="padding: 5px 10px; text-align: right;">
                                                    <el-checkbox label="授权" :value="s.id" v-model="s.checked"></el-checkbox>
                                                </div>
                                            </el-card>
                                        </el-col>
                                    </el-row>
                                </div>
                            </el-tab-pane>
                        </el-tabs>
                        <div class="btn-content">
                            <a href="javascript:;" class="layui-btn layui-btn-normal fixed-element" @click="confirmSubmit" v-show="enableSubmit" style="height: 35px;">确认修改</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </template>
</div>
<div class="addContent" style="display: none;">
    <div class="addBox" style="width:700px;">
        <template>
            <div class="line-wrapper">
                <div class="line-part">
                    <span class="float-tag">姓名：</span>
                    <div class="text-tag"><input type="text" class="input-tag" name="employeeName" v-model="employeeName"></div>
                </div>
                <div class="line-part">
                    <span class="float-tag">手机号：</span>
                    <div class="text-tag"><input type="text" class="input-tag" name="employeePhone" v-model="mobile">
                    </div>
                </div>
            </div>
        </template>
    </div>
</div>
<div id="show-select-customers" style="display: none;">
    <div class="addBox" style="width:700px;">
        <template>
            <el-table :data="customers" tooltip-effect="dark" style="width: 100%" height="400">
                <el-table-column prop="name" label="客户名称"></el-table-column>
                <el-table-column prop="sourceType" label="来源" width="120" :formatter="customerformat"></el-table-column>
                <el-table-column prop="type" label="客户类型" width="120"  :formatter="customerformat"></el-table-column>
                <el-table-column prop="company" label="关联状态" width="120" :formatter="customerformat"></el-table-column>
            </el-table>
        </template>
    </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<%--<script src="${baseStatic}plugin/js/jquery.labelauty.js"></script>--%>
<script src="${baseStatic}js/bscroll/bscroll.js"></script>
<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
<script>
    $(document).ready(function () {
        var v = new Vue({
            el: '#employee-content',
            data: {
                activeName:'first',
                likeString:'', //查询字符串

                authorities: [], //当前用户的所有权限信息
                customers: [], //公司客户信息
                seals: [], //公司印章信息

                cusomer:{likeName:'', type:'', reg:''},


                company: {},//企业信息
                count: 0,//员工数
                employees: [],//员工列表

                selectIndex: 0,//当前选择的员工序号
                employee: null,//选中的员工信息

                alreadys: {
                    authorityKeys:[],//已授权的权限编号
                    customerKeys: [],//已授权的客户编号
                    sealKeys: []//已授权的印章编号
                },

                selectCustomers:[],//已选择的客户信息

                authorityMap: new Map(),//已选择的权限编号

                editModel: false,//是否可以编辑员工名称
                imgPath: base_img //图片基本路径
            },
            created: function () {
                this.searchEmployees();
            },
            computed: {
                manage: function () {
                    return this.employee && this.employee.employeeType === 1;
                },
                enableSubmit: function () {
                    return !this.manage || this.activeName === 'first';
                },
                authorityKeys:function(){
                    var arrays = [];
                    $.each(this.authorities, function (i, auth) {
                        if (auth.checked) {
                            arrays.push(auth.id);
                        }
                        if (auth.children) {
                            $.each(auth.children, function (j, child) {
                                if (child.checked) {
                                    arrays.push(child.id);
                                }
                            });
                        }
                    });
                    return arrays;
                },
                customerKeys:function(){
                    var arrays = [], handers = new Map();
                    this.selectCustomers.forEach(function (v, i) {
                        handers.set(v.key, true);//要新增的客户权限
                    });
                    this.customers.forEach(function (v, i) {
                        if(!handers.has(v.key)){
                            handers.set(v.key, false);//要删除的客户权限
                        }
                    });
                    this.alreadys.customerKeys.forEach(function(v,i){
                        if(!handers.has(v) || handers.get(v)){
                            arrays.push(v);
                        }
                        handers.delete(v);
                    });
                    handers.forEach(function(v, k){
                        if(v){
                            arrays.push(k);
                        }
                    });
                    return arrays;
                },
                sealKeys:function(){
                    var arrays = [];
                    $.each(this.seals, function (i, seal) {
                        if (seal.checked) {
                            arrays.push(seal.id);
                        }
                    });
                    return arrays;
                }
            },
            methods: {
                loadAuthority:function(authorities, map){
                    $.each(authorities, function (i, auth) {
                        var checked = true;
                        if (auth.children) {
                            $.each(auth.children, function (j, child) {
                                if (map.has(child.id)) {
                                    child.checked = true;
                                } else {
                                    child.checked = checked = false;
                                }
                            });
                        }
                        auth.checked = checked;
                    });
                    return authorities;
                },
                loadSeals:function(seals, keys){
                    var map = new Map();
                    if(keys && keys.length > 0){
                        $.each(keys, function (i, k) {
                            map.set(k, i);
                        });
                    }
                    $.each(seals, function (i, seal) {
                        seal.checked = map.has(seal.id);
                    });
                    return seals;
                },
                loadCustomerRows:function(customerTable, customers, keys){
                    var map = new Map(), selectRows = [];
                    if(keys && keys.length > 0){
                        $.each(keys, function (i, k) {
                            map.set(k, i);
                        });
                    }
                    $.each(customers, function (i, customer) {
                        if(map.has(customer.key)){
                            selectRows.push(customer);
                        }
                    });
                    if(customerTable){
                        customerTable.clearSelection();
                        selectRows.forEach(function(row){
                            customerTable.toggleRowSelection(row);
                        });
                    }
                    return selectRows;
                },
                searchEmployees: function () {
                    var vm = this;
                    $.util.json(base_url + '/enterprise/company/employee/manage', {likeString: vm.likeString, seals: true, customers:true, authoritys:true}, function (data) {
                        if (data.success) {//处理返回结果
                            vm.company = data.company;
                            vm.count = data.count;
                            vm.employees = data.employees;
                            vm.seals = vm.loadSeals(data.seals, null);
                            vm.customers = data.customers;
                            vm.authorities = vm.loadAuthority(data.authorities, vm.authorityMap);
                            if(data.employees && data.employees.length > 0){
                                setTimeout(function(){
                                	for (var i = 0; i < data.employees.length; i++) {
                            			$('#name' + i).html($('#name' + i).text().replace(/管理员/ig,"<span style='font-size: 18px;color: #409EFF;padding-left: 10px'>$&</span>"));
                            		}
                                },200);
                                vm.getEmployee(data.employees[0].employeeId, 0);
                            }
                        } else {
                            $.util.error(data.message);
                        }
                    });
                },
                getEmployee: function (employeeKey, index) {
                    var vm = this;
                    if (index >= 0) {
                        vm.selectIndex = index;
                    }
                    $.util.json(base_url + '/enterprise/company/employee/detail/' + employeeKey, null, function (data) {
                        if (data.success) {//处理返回结果
                            vm.authorityMap.clear();
                            if(data.employee != null){
                                vm.employee = data.employee;
                                vm.alreadys.authorityKeys = data.authoritys;
                                vm.alreadys.customerKeys = data.customers;
                                vm.alreadys.sealKeys = data.seals;
                                if(data.authoritys && data.authoritys.length > 0){
                                    $.each(data.authoritys, function (i, k) {
                                        vm.authorityMap.set(k, i);
                                    });
                                }
                            }else{
                                vm.employee = null;
                                vm.manage = false;
                                vm.alreadys.authorityKeys = [];
                                vm.alreadys.customerKeys = [];
                                vm.alreadys.sealKeys = [];
                            }
                            vm.editModel = false;

                            vm.$nextTick(function(){
                                vm.authorities = vm.loadAuthority(vm.authorities, vm.authorityMap);
                                vm.seals = vm.loadSeals(vm.seals, vm.alreadys.sealKeys);
                                vm.loadCustomerRows(vm.$refs.customerTable, vm.customers, vm.alreadys.customerKeys);
                            });
                        } else {
                            $.util.error(data.message);
                        }
                    });
                },
                deleteEmployee:function () {
                    var vm = this, employee = vm.employee, employeeKey = employee.employeeId;
                    $.util.confirm('删除员工', '你确定要将员工['+ employee.employeeName +']从公司移除吗?', function(){
                        $.util.get(base_url + '/enterprise/company/employee/delete/'+ employeeKey, function (data) {
                            if (data.success) {//处理返回结果
                                $.util.success(data.message);
                                vm.employees = vm.employees.filter(function(o) {
                                    return o.employeeId !== employeeKey;
                                });
                                vm.getEmployee(vm.employees[0].employeeId);
                            } else {
                                $.util.error(data.message);
                            }
                        });
                    });
                },
                select: function (p, c) {
                    var vm = this, authority = vm.authorities[p];
                    if(c >= 0){
                        var child = authority.children[c];
                        if(child.checked){
                            vm.authorityMap.set(child.id, c);
                        }else{
                            vm.authorityMap.delete(child.id);
                        }

                        var checked = true;
                        $.each(authority.children, function (j, child) {
                            if (!child.checked) {
                                checked = false;
                            }
                        });
                        authority.checked = checked;
                        if (authority.checked) {
                            vm.authorityMap.set(authority.id, p);
                        } else {
                            vm.authorityMap.delete(authority.id);
                        }
                    }else{
                        if (authority.checked) {//当前全选
                            vm.authorityMap.set(authority.id, p);
                            $.each(authority.children, function (j, child) {
                                child.checked = authority.checked;
                                vm.authorityMap.set(child.id, j);
                            });
                        } else {//当前没有全选
                            vm.authorityMap.delete(authority.id);
                            $.each(authority.children, function (j, child) {
                                child.checked = authority.checked;
                                vm.authorityMap.delete(child.id);
                            });
                        }
                    }
                },
                confirmSubmit: function () {
                    var vm = this, parmas = { };
                    var url = '/enterprise/company/employee/edit';
                    if('first' === vm.activeName){//修改基本信息
                        parmas['employeeId'] = vm.employee.employeeId;
                        parmas['employeeName'] = vm.employee.employeeName;
                        parmas['userFettle'] = vm.employee.userFettle;
                    }else if('second' === vm.activeName){//修改菜单权限
                        parmas['employeeId'] = vm.employee.employeeId;
                        parmas['authorityKeys'] = vm.authorityKeys.join(',');
                        url = '/enterprise/company/employee/authority/modify';
                    }else if('third' === vm.activeName){//修改客户权限
                        parmas['employeeKey'] = vm.employee.employeeId;
                        parmas['customerKeys'] = vm.customerKeys.join(',');
                        url = '/enterprise/customer/employee/authority/modify';
                    }else if('fourth' === vm.activeName){//修改公章授权信息
                        parmas['employeeKey'] = vm.employee.employeeId;
                        parmas['seals'] = vm.sealKeys.join(',');
                        url = '/enterprise/company/signature/auth';
                    }
                    $.util.json(base_url + url, parmas, function (data) {
                        if (data.success) {//处理返回结果
                            $.util.success(data.message);
                            vm.getEmployee(vm.employee.employeeId);
                            vm.employees[vm.selectIndex].employeeName = vm.employee.employeeName;
                        } else {
                            $.util.error(data.message);
                        }
                    });
                },
                customerSelect:function(selects){
                    this.selectCustomers = selects;
                },
                showAdd: function () {
                    addEmployee();
                },
                customerformat:function(row, column, v, index){
                    return formatSomething(column.property, v);
                },
                loadCustomers:function(){
                    var vm = this;
                    $.util.json(base_url + "/enterprise/customer/list", vm.cusomer, function(data) {
                        if(data.success) { //处理返回结果
                            vm.customers = data.customers;
                            vm.$nextTick(function(){
                                vm.loadCustomerRows(vm.$refs.customerTable, vm.customers, vm.alreadys.customerKeys);
                            });
                        } else {
                            $.util.error(data.message);
                        }
                    });
                },
                showSelect:function(){
                    if(this.selectCustomers && this.selectCustomers.length > 0){
                        showSelectCustomers(this.selectCustomers);
                    }
                }
            }
        });
        function addEmployee() {
            var panel = $.util.panel('添加员工', $(".addContent").html());
            //var pcas = JSON.parse(jsonpcs), wxas = JSON.parse(jsonwxs);
            panel.initialize = function () {
                panel.vm = new Vue({
                    el: this.$body.find(".addBox")[0],
                    data: {
                        employeeName: '', mobile: ''
                    }
                });
            }
            panel.addButton({
                text: '确定', action: function () {
                    var vm = panel.vm;
                    var parmas = {
                        employeeName: vm.employeeName,
                        mobile: vm.mobile
                    };
                    $.util.json(base_url + '/enterprise/company/employee/add', parmas, function (data) {
                        if (data.success) {//处理返回结果
                            $.util.success(data.message, function () {
                                v.searchEmployees();
                                if (confirm) {
                                    confirm.close();
                                }
                            }, 2000);
                        } else {
                            $.util.error(data.message);
                        }
                    });
                    return false;
                }
            });
            var confirm = panel.open();
        }
        function showSelectCustomers(selects) {
            var panel = $.util.panel('已选择的客户', $("#show-select-customers").html());
            panel.initialize = function () {
                panel.vm = new Vue({
                    el: this.$body.find(".addBox")[0],
                    data: {
                        customers: selects
                    },
                    methods:{
                        customerformat:function(row, column, v, index){
                            return formatSomething(column.property, v);
                        }
                    }

                });
            }
            panel.open();
        }
        function formatSomething(type, v){
            if('sourceType' === type){//1:新建,2:分享,3:指派
                return v === 1 ? '手动添加' : (v===2 ? '分享' : '指派');
            }else if('type' === type){
                return v === 1 ? '货主' : (v===2 ? '收货客户' : '物流商');
            }else if('company' === type){
                return (v != null && v > 0) ? '已关联':'未关联';
            }
            return v;
        }
    });
</script>
</html>