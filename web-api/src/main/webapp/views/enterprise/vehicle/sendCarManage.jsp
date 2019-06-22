<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-车辆管理-派车管理</title>
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
        .search .el-input {
            width: 220px;
        }
        .el-select {
            margin-right: 20px;


        }
        .el-dialog__body .el-select{
            width: 100%;
        }
        /*.el-input {*/
            /*width: 80%;*/
        /*}*/
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
            <el-select v-model="companyKey" placeholder="上级企业" filterable>
                <el-option
                        v-for="item in lastCompanys"
                        v-if="item!=null"
                        :key="item.id"
                        :label="item.companyName"
                        :value="item.id"
                        >
                </el-option>
            </el-select>
            <el-select v-model="logisticsDealer" placeholder="物流商" filterable>
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
            <el-button type="primary" @click='batchCar'>批量派车</el-button>

        </div>
        <div class="table">
            <el-table :data="list" style="width: 100%" height="450" border="true" @selection-change="handleSelectChange">
                <el-table-column type="selection" width="35"></el-table-column>
                <el-table-column fixed prop="key" label="要车单号" width="150" align="center"></el-table-column>
                <el-table-column fixed prop='lastCompany.companyName' label="上级企业" width="150" align="center"></el-table-column>
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
                        <span @click="sendCar(scope.row.key)" v-if="scope.row.carStatus==0" class="blue">派车</span>
                        <span @click="takeCar(scope.row.key)" v-if="scope.row.carStatus==0 " class="blue">要车</span>
                        <span @click="sendCar(scope.row.key,2)" v-if="scope.row.carStatus== 1" class="blue">派车调整</span>
                    </template>
                </el-table-column>
            </el-table>
            <!-- 分页  -->
            <div class="pagination">
                <el-pagination background layout="total,prev, pager, next" :total="total" @current-change="currentChange">

                </el-pagination>
            </div>
        </div>
        <%--批量派车--%>
        <el-dialog title="批量派车" :visible.sync="dialog.batchCar" width="35%">
            <div class="body-info">
                <el-form :model="vehicle2" status-icon  label-width="100px" >
                    <el-form-item label="车牌号">
                        <el-input  v-model="vehicle2.carNo"></el-input>
                    </el-form-item>
                    <el-form-item label="司机姓名">
                        <el-input  v-model="vehicle2.driverName"></el-input>
                    </el-form-item>
                    <el-form-item label="司机号码">
                        <el-input  v-model="vehicle2.driverNumber" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"></el-input>
                    </el-form-item>

                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                    <el-button @click="dialog.batchCar = false">取 消</el-button>
                    <el-button type="primary" @click="batchCarSub">确 定</el-button>
                </span>
        </el-dialog>
        <!--派车/派车调整  -->
        <el-dialog title="派车" :visible.sync="dialog.sendCar" width="35%">
            <div class="body-info">
                <el-form :model="vehicle1" status-icon  label-width="100px" >
                    <el-form-item label="车牌号">
                        <el-input  v-model="vehicle1.carNo"></el-input>
                    </el-form-item>
                    <el-form-item label="司机姓名">
                        <el-input  v-model="vehicle1.driverName"></el-input>
                    </el-form-item>
                    <el-form-item label="司机号码">
                        <el-input  v-model="vehicle1.driverNumber" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"></el-input>
                    </el-form-item>

                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                    <el-button @click="dialog.sendCar=false">取 消</el-button>
                    <el-button type="primary" @click="sendCarSubmit">确 定</el-button>
                </span>
        </el-dialog>
        <!--要车 -->
        <el-dialog title="要车" :visible.sync="dialog.takeCar" width="35%">
            <div class="body-info">
                <el-form  status-icon   label-width="100px" >
                    <el-form-item label="要车对象">
                        <el-select v-model="designate.conveyId" filterable placeholder="选择物流商">
                            <el-option
                                    v-for="item in customerList"
                                    :key="item.companyKey"
                                    :label="item.name"
                                    :value="item.companyKey">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="联系人" >
                        <el-input  v-model="designate .contact" ></el-input>
                    </el-form-item>
                    <el-form-item label="联系电话" >
                        <el-input  v-model="designate.contactNumber" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"></el-input>
                    </el-form-item>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                    <el-button @click="dialog.takeCar=false">取 消</el-button>
                    <el-button type="primary" @click="takeCarSub">确 定</el-button>
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
                companyKey:'',//上级企业
                logisticsDealer:'',//物流商
                likeString:'',//要车单号
                list: [],//要车管理列表数据
                dialogVisible1: false,//派车/派车调整
                dialogVisible2: false,//要车
                key:'',//要车单号
                //派车/派车调整
                vehicle1:{
                    carNo:'',//车牌号
                    driverName:'',//司机姓名
                    driverNumber:'',//司机电话
                },
                //批量派车
                vehicle2:{
                    carNo:'',//车牌号
                    driverName:'',//司机姓名
                    driverNumber:'',//司机电话
                },
                orders:[],//要车单id
                designate:{
                    vId:'',
                    conveyId: '',//物流商
                    contact: '',//联系人
                    contactNumber: "",//联系电话
                },
                customerList:[],//物流商列表
                lastCompanys:[],//上级企业
                dialog:{
                    batchCar:false,//批量派车
                    sendCar:false,//派车/派车调整
                    takeCar:false//要车
                }
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
                    var _this =this;
                    console.log(selects)
                    if(selects && selects.length > 0){
                        _this.orders = selects.map(function (item) { return item.key; });
                    }else{
                        _this.orders = [];
                    }
                },
                //当前页改变时
                currentChange(val) {
                    console.log(val);
                    var _this =this;
                    _this.pageNum = val;
                    _this.render()
                },
                //搜索
                search:function(){
                    var _this =this;
                    _this.render()
                },
                //批量派车
                batchCar:function(){
                    var _this = this;
                    if(_this.orders && _this.orders.length > 0){
                        _this.dialog.batchCar = true;
                    } else {
                        _this.$message({
                            message:'请至少选择一条需要派车的计划',
                            type: 'error'
                        });
                    }

                },
                //批量派车提交
                batchCarSub:function(){
                var _this = this ;
                    var parmas = {
                        vids :JSON.stringify(_this.orders),//要车单号集合
                        vehicle:JSON.stringify(_this.vehicle2),
                    }
                    console.log(parmas)
                    if (!(/^1\d{10}$/.test(_this.vehicle2.driverNumber))){
                        _this.$message({
                            message:'请输入正确的手机号码',
                            type: 'error'
                        });
                        return false;
                    }
                    _this.request("/enterprise/vehicle/car/send/modify", null, function(data){
                        console.log(data)
                        if (data.success){
                            this.$message({
                                message: data.message,
                                type: 'success'
                            });
                        }
                       _this.dialog.sendCar =false;
                      _this.render()
                    });
                },
                //派车
                sendCar:function(key,type){
                    var _this =this;
                    console.log(key)
                    _this.dialog.sendCar =true;
                    _this.orders = [];
                    _this.orders.push(key)
                    if (type==2) {
                        _this.$http.get(base_url+"/enterprise/vehicle/car/detail/"+key, null, {emulateJSON: false})
                            .then(
                                (response)=>{
                                    console.log(response);
                                    if (response.body.success) {
                                        _this.vehicle1.carNo=response.body.result.carNo;
                                        _this.vehicle1.driverName=response.body.result.driverName;
                                        _this.vehicle1.driverNumber=response.body.result.driverNumber;
                                    }
                                },
                                (error)=>{
                                    console.log(error);
                                }
                            )
                    }
                },
                //派车/派车调整提交
                sendCarSubmit: function (){
                    var _this = this;
                    var parmas = {
                        vids :JSON.stringify(_this.orders),//要车单号集合
                        vehicle:JSON.stringify(this.vehicle1),
                    }
                   console.log(parmas)
                    if (!(/^1\d{10}$/.test(_this.vehicle1.driverNumber))){
                        _this.$message({
                            message:'请输入正确的手机号码',
                            type: 'error'
                        });
                        return false;
                    }
                    _this.request("/enterprise/vehicle/car/send/modify", parmas, function(data){
                        // console.log(data)
                         if (data.success){
                             this.$message({
                                 message: data.message,
                                 type: 'success'
                             });
                         }
                       _this.dialog.sendCar =false;
                      _this.render()
                    });
                },
                //要车
                takeCar:function(key){
                    var _this = this;
                    _this.dialog.takeCar = true;
                    _this.designate.vId = key;
                },
                //要车提交
                takeCarSub:function(){
                    var _this = this;
                    var parmas = {
                        designate:JSON.stringify(_this.designate)
                    }
                    console.log(parmas)
                    if (!(/^1\d{10}$/.test(_this.designate.contactNumber))){
                        _this.$message({
                            message:'请输入正确的手机号码',
                            type: 'error'
                        });
                        return false;
                    }
                    _this.request("/enterprise/vehicle/car/want", parmas, function(data){
                        console.log(data)
                        if (data.success){
                            this.$message({
                                message: data.message,
                                type: 'success'
                            });
                        }
                       _this.dialog.takeCar =false;
                      _this.render()
                    });
                },
                render:function(num){
                    var _this = this;
                    var parmas = {
                        lastCompanyKey:_this.companyKey,
                        likeString:_this.likeString,//要车单号
                        conveyId:_this.logisticsDealer,//物流商id
                        size:_this.pageSize,
                        num:num?num:_this.pageNum,
                    }
                    console.log(parmas)
                    _this.request("/enterprise/vehicle/listSendCar", parmas, function(data){
                        console.log(data)
                        _this.list = data.page.collection;
                        _this.total = data.page.total;
                        _this.lastCompanys = data.lastCompanys
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
                }
            }
        })
    });
</script>
</html>