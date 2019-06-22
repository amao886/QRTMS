<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-车辆管理-要车管理</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}element-ui/lib/theme-chalk/index.css"/>
    <style type="text/css">
        .search {
            padding-top: 30px;
        }
        .topBtn {
            margin-top: 30px;
        }
        .table {
            margin-top: 20px;
        }
        .body-info {
            text-align: center;
        }
        .el-input {
            width: 220px;
        }
        .el-select {
            margin-right: 20px;
        }
        .el-button--small {
            margin-left: 20px;
            padding: 8px 15px;
        }
        .el-input__inner {
            height: 30px;
            line-height: 30px;
        }
        .el-input__icon {
            line-height: 30px;
        }
   .blue {
       color: #409EFF;
   }
        .el-table__empty-text {
            position: fixed;
            top: 50%;
            left: 25%;
        }
    </style>
</head>
<body>
<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
<!--[if lt IE 9]>
<script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
<script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->

<div class="car-management" id="car-management">
    <template>
        <div class="search">
            <el-select v-model="logisticsDealer" placeholder="请选择物流商" filterable>
                <el-option
                        v-for="item in customerList"
                        :key="item.companyKey"
                        :label="item.name"
                        :value="item.companyKey">
                </el-option>
            </el-select>
            <el-input v-model="likeString" placeholder="要车单号"></el-input>
            <el-button type="primary" size='small' @click="search">查询</el-button>
        </div>
        <div class="topBtn">
            <%--<el-button type="primary" >批量要车</el-button>--%>
            <el-button type="primary" @click="operate(2,121)">要车录入</el-button>
        </div>
        <div class="table">
            <el-table :data="list" style="width: 100%" height="450" border="true" @selection-change="handleSelectChange">
                <el-table-column type="selection" width="35"></el-table-column>
                <el-table-column fixed  prop="key" label="要车单号" width="150" align="center">
                </el-table-column>
                <el-table-column fixed prop="totalLoad" label="总重量(kg)" width="150" align="center" show-overflow-tooltip></el-table-column>
                <el-table-column fixed prop="totalVolume" label="总体积(m³)" width="150" align="center" show-overflow-tooltip></el-table-column>
                <el-table-column fixed prop="carModel" label="车型" width="150" align="center" show-overflow-tooltip></el-table-column>
                <el-table-column prop="carLength" label="车长" width="150" align="center" show-overflow-tooltip></el-table-column>
                <el-table-column label="载重(kg)"   prop="vehicleLoad" width="150" align="center" show-overflow-tooltip> </el-table-column>
                <el-table-column label="体积(m³)"   prop="volume" width="150" align="center"></el-table-column>
                <el-table-column label="物流商" prop="convey.companyName" width="150" align="center"> </el-table-column>
                <el-table-column prop="carNo" label="车牌号" width="150" align="center"></el-table-column>
                <el-table-column label="司机姓名" prop="driverName" width="150" align="center"> </el-table-column>
                <el-table-column label="司机电话"  prop="driverNumber" width="150" align="center"> </el-table-column>
                <el-table-column label="派车状态"  width="150" align="center">
                     <template slot-scope="scope">
                        <span v-if="scope.row.carStatus==0">未派车</span>
                        <span v-if="scope.row.carStatus==1">已派车</span>
                        <span v-if="scope.row.carStatus==2">确认派车</span>
                   </template>
                </el-table-column>
                <el-table-column fixed="right" label="操作" width="160" align="center">
                    <template slot-scope="scope">
                       <span @click="deleteCar(scope.row.key)" class="blue" v-if="scope.row.carStatus==0">删除</span>
                        <span @click="confirmationCar(scope.row.key)" class="blue" v-if="scope.row.confirm">确认派车</span>
                   </template>
                </el-table-column>
            </el-table>
            <!-- 分页  -->
            <div class="pagination">
                <el-pagination background layout="total,prev, pager, next" :total="total" @current-change="currentChange">

                </el-pagination>
            </div>
        </div>
        <!--要车计划删除  -->
        <el-dialog title="删除" :visible.sync="dialogVisible" width="35%">
            <div class="body-info">
                确定删除要车计划？
            </div>
            <span slot="footer" class="dialog-footer">
                    <el-button @click="dialogVisible = false">取 消</el-button>
                    <el-button type="primary" @click="deleteSubmit">确 定</el-button>
                </span>
        </el-dialog>
    </template>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
<script type="text/javascript" src="${baseStatic}vue/vue-resource.js"></script>
<script>
    $(document).ready(function () {
        const vm = new Vue({
            el: '#car-management',
            data: {
                pageNum: 1,//页数
                pageSize: 10,//每一页数量
                total: 50,//总数量
                logisticsDealer:'',//物流商
                likeString:'',//要车单号
                list: [],//要车管理列表数据
                dialogVisible:false,
                key:'',//要车单号
                customerList:[]
            },
            mounted:function(){
                var _this =this;
            _this.render();
                _this.request('/enterprise/customer/list',{reg:1, status: 1},function (data) {
                    console.log(data)
                    if (data.success){
                        _this.customerList = data.customers
                    }
                })
            },
            methods: {
                //复选框变化时
                handleSelectChange: function (selects) {
                    console.log(selects)
                },
                //当前页改变时
                currentChange(val) {
                    var _this =this;
                    console.log(val);
                    _this.pageNum = val;
                    _this.render()
                },
                //搜索
                search:function(){
                    var _this =this;
                    _this.render()
                },
                //删除
                deleteCar:function(key){
                    var _this =this;
                    _this.dialogVisible=true
                    _this.key = key;
                },
                //删除提交
                deleteSubmit:function(){
                  var _this =this ;

                    _this.$http.get(base_url+"/enterprise/vehicle/car/delete/"+_this.key, null, {emulateJSON: false})
                        .then(
                            (response)=>{
                                console.log(response);
                                if (response.body.success){
                                    _this.dialogVisible =false;
                                    this.$message({
                                        message: response.body.message,
                                        type: 'success'
                                    });
                                    _this.render()
                                }
                            },
                            (error)=>{
                                console.log(error);
                            }
                        )
                },
                //确认派车
                confirmationCar:function (key) {
                    var _this = this;
                    console.log(11)
                    _this.$http.get(base_url+"/enterprise/vehicle/car/send/confirm/"+key, null, {emulateJSON: false})
                        .then(
                            (response)=>{
                                console.log(response);
                                if (response.body.success){
                                    _this.dialogVisible =false;
                                    this.$message({
                                        message: response.body.message,
                                        type: 'success'
                                    });
                                    _this.render()
                                }
                            },
                            (error)=>{
                                console.log(error);
                            }
                        )
                },
                render:function(num){
                    var _this = this;
                    var parmas = {
                        likeString:_this.likeString,//要车单号
                        conveyId:_this.logisticsDealer,//物流商id
                        size:_this.pageSize,
                        num:num?num:_this.pageNum,
                    }
                    console.log(parmas)
                    _this.request("/enterprise/vehicle/listNeedCar", parmas, function(data){
                        console.log(data)
                        _this.list = data.page.collection;
                        _this.total = data.page.total;
                    });
                },
                //发起请求
                request:function(url, parmas, callback){
                    var _this = this;
                    _this.$http.post(url, parmas, {emulateJSON: false})
                        .then(
                            (response) => {
                                if (response.body.success) {
                                    if(callback){

                                        callback.call(_this, response.body);
                                    }
                                }else{
                                    _this.$message({ message: response.body.message, type: 'error' })
                                }
                            },
                            (error) => {
                                _this.$message({ message: '数据请求异常', type: 'error' })
                            }
                        )
                },
                operate: function (type, key,) {//查看
                    var _this = this;
                    var options = {
                        type: 2,
                        title: '要车管理',
                        content: "/enterprise/vehicle/load/save?key=" + key,
                    };
                    if (type === 2) {
                        options.title = '要车管理';
                        options.content = "/enterprise/vehicle/load/save?key=" + key;
                        options.end=function () {
                            _this.render()
                        }
                    }
                    layer.full(layer.open(options));
                },
            }
        })
    });
</script>
</html>