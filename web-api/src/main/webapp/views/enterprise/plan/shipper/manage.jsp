<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-计划管理-发货方计划</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}element-ui/lib/theme-chalk/index.css"/>
    <style type="text/css">

        .inputEle, .btnPlan, .table {
            margin-left: 10px;
        }

        .table {
            padding-top: 10px;
            padding-right: 20px;
        }

        .inputEle input {
            outline: none;
            border: 1px solid #ccc;
            height: 30px;
            font-size: 12px;
            border-radius: 5px;
            padding: 5px;
            width: 200px;
        }

        table tr td:last-child span {
            color: #169BD5;
            cursor: pointer;
        }

        .el-form input{
            border: 1px solid #dcdfe6;
            width: 100%;
            padding: 0 15px;
        }
        .el-form .el-select {
            display: inline-block;
            position: relative;
            width: 100%;
        }

        /* 弹出框样式修改 */
        .file_box {
            position: relative;
        }

        .model-base-box .file_box > input {
            width: 255px;
        }

        .file_box > .span-txt {
            padding: 0 5px;
            margin-left: 3px;
            display: inline-block;
            vertical-align: top;
            height: 28px;
            line-height: 28px;
            color: #fff;
            border-radius: 3px;
            background: #31acfa;
        }

        .file_box > a.file {
            position: absolute;
            top: 1px;
            width: 72px;
            left: 356px;
            z-index: 22;
            overflow: hidden;
        }

        .file_box > a.file > input[type=file] {
            opacity: 0;
            filter: alpha(opacity=0);
        }
        .el-table th>.cell {
            text-align: center;
        }
        .el-table .cell.el-tooltip {
            text-align: center;
        }
        .el-upload , .el-upload-dragger{
            width: 100%;
        }
        .el-upload__input {
            display: none !important;
        }
        .btnPlan button{
            margin-left: 15px !important;
            border-radius: 5px !important;
        }
        .el-dialog__header {
            border-bottom: 1px solid #ccc;
        }
        .el-dialog__footer {
            border-top: 1px solid #ccc;
        }
        .el-dialog__body {
            position: relative;
            height: 350px;
        }
        .body-info {
            position: absolute;
            height:200px;
            width: 80%;
            margin: 0 auto;
            top: 50%;
            left: 50%;
            transform: translate(-50%,-100px);
        }
        .body-info .el-form-item__label {
            text-align: left;
        }
        .bartchCar {
            display: inline-block;
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

<div class="plan-content" id="plan-content">

    <template>
        <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="未分配物流商" name="first">
                <div class="inputEle">
                    <el-form :inline="true">
                        <el-form-item>
                            <el-input v-model="first.likeString" placeholder="发货计划单号/收货客户/运输路线" style="width: 250px;"></el-input>
                        </el-form-item>
                        <%--<el-form-item >--%>
                            <%--<el-select v-model="first.lastCompnayKey" placeholder="数据来源">--%>
                                <%--<el-option label="全部来源" value=""></el-option>--%>
                                <%--<el-option label="我方创建" value="0"></el-option>--%>
                                <%--<el-option v-for="item in sources" :key="item.id" :label="item.companyName" :value="item.id"> </el-option>--%>
                            <%--</el-select>--%>
                        <%--</el-form-item>--%>
                        <%--<el-form-item >
                            <el-select v-model="first.ordinateState" placeholder="接单状态">
                                <el-option key="0" label="全部接单状态" value=""></el-option>
                                <el-option label="未接单" value="0"></el-option>
                                <el-option label="已接单" value="1"></el-option>
                            </el-select>
                        </el-form-item>--%>
                        <%--<el-form-item >--%>
                            <%--<el-select v-model="first.generate" placeholder="发货单状态">--%>
                                <%--<el-option key="0" label="全部发货单状态" value=""></el-option>--%>
                                <%--<el-option label="未生成" value="false"></el-option>--%>
                                <%--<el-option label="已生成" value="true"></el-option>--%>
                            <%--</el-select>--%>
                        <%--</el-form-item>--%>
                        <el-form-item>
                            <el-button type="primary" @click="render(1)" size="small">查询</el-button>
                        </el-form-item>
                    </el-form>
                </div>
                <div class="btnPlan">
                    <el-row>
                        <el-col :span="12">
                            <el-button-group>
                                <el-button type="primary" @click="importFile()" size="small" >导入发货计划</el-button>
                                <el-button type="primary" @click="entryFile()" size="small">录入发货计划</el-button>
                                <%--<el-button type="primary" @click="accepts()" size="small">确认接单</el-button>--%>
                                <el-button type="primary" @click="designates()" size="small">分配物流商</el-button>
                                <%--<el-button type="primary" @click="generates()" size="small">生成发货单</el-button>--%>
                            </el-button-group>
                        </el-col>
                        <el-col :span="12">
                            <div v-if="first.total > first.size" style="float: right;">
                                <el-pagination layout="prev, pager, next" :total="first.total" @current-change="currentChange" ></el-pagination>
                            </div>
                        </el-col>
                    </el-row>
                </div>

                <div class="table">
                    <el-table :data="first.list" style="width: 100%" height="460" border="true" @selection-change="handleSelectChange">
                        <el-table-column type="selection" width="35"></el-table-column>
                        <el-table-column fixed prop="planNo" label="计划单号" width="150" align="center" show-overflow-tooltip></el-table-column>
                        <el-table-column fixed label="发货日期" width="100" align="center">
                            <template slot-scope="scope">
                                <span>{{ scope.row.deliveryTime | date }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column fixed label="收货客户" width="200" align="center" show-overflow-tooltip>
                            <template slot-scope="scope">
                                <span>{{ scope.row.receive.customerName}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="receiverName" label="收货人" width="100" align="center" show-overflow-tooltip></el-table-column>
                        <el-table-column prop="receiverContact" label="联系方式" width="120" align="center" show-overflow-tooltip></el-table-column>
                        <el-table-column prop="receiveAddress" label="收货地址" width="300" align="center" show-overflow-tooltip></el-table-column>
                        <el-table-column prop="transportRoute" label="运输路线" width="280" align="center" show-overflow-tooltip></el-table-column>
                        <el-table-column label="要求提货时间" width="120" align="center">
                            <template slot-scope="scope">
                                <span>{{ scope.row.collectTime | date }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="要求到货时间" width="120" align="center">
                            <template slot-scope="scope">
                                <span>{{ scope.row.arrivalTime | date }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column fixed="right" label="操作" width="200" align="center">
                            <template slot-scope="scope">
                                <span @click="operate(1, scope.row.id)">查看</span>
                                <span @click="operate(2, scope.row.id, 1)" v-if="!scope.row.allocate && scope.row.orderKey == 0">编辑</span>
                                <span @click="deletePlan(scope.row.id)" v-if="!scope.row.allocate && scope.row.orderKey == 0">删除</span>
                                <%--<span @click="operate(3, scope.row.id, 1)" v-if="scope.row.orderKey == 0">生成发货单</span>
                                <span @click="operate(4, scope .row.orderKey)" v-if="scope.row.orderKey > 0">查看发货单</span>--%>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </el-tab-pane>
            <el-tab-pane label="已分配物流商" name="second">
                <div class="inputEle">
                    <el-form :inline="true">
                        <el-form-item>
                            <el-input v-model="second.likeString" placeholder="发货计划单号/收货客户/运输路线" style="width: 250px;"></el-input>
                        </el-form-item>
                        <el-form-item >
                            <el-select v-model="second.ordinateState" placeholder="物流商接单状态">
                                <el-option key="0" label="全部接单状态" value=""></el-option>
                                <el-option label="物流商未接单" value="0"></el-option>
                                <el-option label="物流商已接单" value="1"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item >
                            <el-select v-model="second.generate" placeholder="发货单状态">
                                <el-option key="0" label="全部发货单状态" value=""></el-option>
                                <el-option label="未生成" value="false"></el-option>
                                <el-option label="已生成" value="true"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item >
                            <el-select v-model="second.carStatus" placeholder="派车状态">
                                <el-option label="未派车" value="0"></el-option>
                                <el-option label="已派车" value="1"></el-option>
                                <el-option label="确认派车" value="2"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-button-group>
                                <el-button type="primary" @click="render(1)" size="small">查询</el-button>
                            </el-button-group>
                        </el-form-item>
                    </el-form>
                </div>
                <div class="btnPlan">
                    <el-row>
                        <el-col :span="12">
                            <el-button type="primary" @click="generates()" size="small">生成发货单</el-button>
                            <el-button type="primary" @click="batchCar" size="small">批量派车</el-button>
                        </el-col>
                        <el-col :span="12" >
                            <div v-if="second.total > second.size" style="float: right;">
                                <el-pagination layout="prev, pager, next" :total="second.total" @current-change="currentChange" ></el-pagination>
                            </div>
                        </el-col>
                    </el-row>
                </div>
                <div class="table">
                    <el-table :data="second.list" style="width: 100%" height="460" border="true" @selection-change="handleSelectChange">
                        <el-table-column type="selection" width="35"></el-table-column>
                        <el-table-column fixed prop="planNo" label="计划单号" width="150" align="center" show-overflow-tooltip></el-table-column>
                        <el-table-column fixed label="发货日期" width="100" align="center">
                            <template slot-scope="scope">
                                <span>{{ scope.row.deliveryTime | date }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column fixed label="收货客户" width="200" align="center" show-overflow-tooltip>
                            <template slot-scope="scope">
                                <span>{{ scope.row.receive.customerName}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="receiverName" label="收货人" width="120" align="center" show-overflow-tooltip></el-table-column>
                        <el-table-column prop="receiverContact" label="联系方式" width="120" align="center" show-overflow-tooltip></el-table-column>
                        <el-table-column prop="receiveAddress" label="收货地址" width="300" align="center" show-overflow-tooltip></el-table-column>
                        <el-table-column prop="transportRoute" label="运输路线" width="120" align="center" show-overflow-tooltip></el-table-column>
                        <el-table-column label="要求提货时间" width="120" align="center">
                            <template slot-scope="scope">
                                <span>{{ scope.row.collectTime | date }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="要求到货时间" width="120" align="center">
                            <template slot-scope="scope">
                                <span>{{ scope.row.arrivalTime | date }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="分配对象" width="200" align="center" show-overflow-tooltip>
                            <template slot-scope="scope">
                                <span>{{ scope.row.target != null && scope.row.target.companyName }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="下级接单" width="100" align="center">
                            <template slot-scope="scope">
                                <span v-if="scope.row.designate != null && scope.row.designate.fettle >= 1">已接单</span>
                                <span v-else>未接单</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="发货单状态" width="100" align="center">
                            <template slot-scope="scope">
                                <span>{{ scope.row.orderKey > 0 ? '已生成' : '未生成' }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="派车状态"  width="150" align="center">
                            <template slot-scope="scope">
                                <span v-if="scope.row.carStatus==0">未派车</span>
                                <span v-if="scope.row.carStatus==1">已派车</span>
                                <span v-if="scope.row.carStatus==2">确认派车</span>
                            </template>
                        </el-table-column>
                        <el-table-column fixed="right" label="操作" width="200" align="center">
                            <template slot-scope="scope">
                                <span @click="operate(1, scope.row.id)">查看</span>
                                <span @click="operate(2, scope.row.id, 1)" v-if="!scope.row.allocate && scope.row.designate == null && scope.row.orderKey == 0">编辑</span>
                                <span @click="operate(3, scope.row.id, 1)" v-if="!scope.row.allocate && scope.row.orderKey == 0">生成发货单</span>
                                <span @click="operate(4, scope.row.orderKey)" v-if="scope.row.orderKey > 0">查看发货单</span>
                                <span @click="sendCar(scope.row.id,2)" v-if="scope.row.carStatus==1">派车调整</span>
                                <span @click="sendCar(scope.row.id)" v-if="scope.row.carStatus==0">派车</span>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </el-tab-pane>
        </el-tabs>
        <el-dialog title="分配物流商" :visible.sync="dialog.designate" width="40%">
            <el-form label-width="100px">
                <el-form-item label="选择物流商:">
                    <el-select v-model="designate.conveyKey" filterable placeholder="请选择物流商企业">
                        <el-option v-for="item in conveys" :key="item.value" :label="item.name" :value="item.key" v-if="item.company != null">
                            <span style="float: left">{{ item.name }}</span>
                            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.company.companyName }}</span>
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="联系人:">
                    <el-input placeholder="物流商联系人" v-model="designate.contacts"></el-input>
                </el-form-item>
                <el-form-item label="联系电话:">
                    <el-input placeholder="物流商联系方式" v-model="designate.contactNumber" maxlength="11"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="designates">确定</el-button>
                <el-button @click="dialog.designate = false">取消</el-button>
            </span>
        </el-dialog>
        <!-- 上传导入 -->
        <el-dialog title="上传文件导入发货计划" :visible.sync="dialog.import" width="40%">
            <el-row>
                <el-col :span="20">
                    <el-select v-model="fileData.templateKey" filterable placeholder="选择发货计划模板" style="width: 100%; margin-bottom: 10px;">
                        <el-option v-for="item in templates" :key="item.key" :label="item.name" :value="item.key">
                            <span style="float: left">{{ item.name }}</span>
                            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.mergeType == 0 ? '不合并' : item.mergeType == 1 ? '按计划单号合并' : '按收货客户信息合并' }}</span>
                        </el-option>
                    </el-select>
                </el-col>
                <el-col :span="4">
                    <el-button type="text" style="float: right;" @click="downTemplate">下载模板</el-button>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="24">
                    <el-upload drag action="/enterprise/plan/shipper/import/excel" accept=".xlsx,.xls" :data="fileData"  style="margin-bottom: 10px;" :on-error="importFile" :on-success="importFile" :disabled="fileData.templateKey == null" ref="upload" :auto-upload='false' >
                        <i class="el-icon-upload"></i>
                        <div class="el-upload__text">将文件拖到此处,或<em>点击上传</em></div>
                        <div class="el-upload__tip" slot="tip">只能上传xlsx/xls文件,请确保文件格式的正确性</div>
                    </el-upload>
                    <el-button style="margin-top: 10px; color: #409EFF; border: 1px solid #409EFF;" size="small"  @click="submitUpload">上传</el-button>
                </el-col>
            </el-row>
        </el-dialog>
        <%--批量派车--%>
        <el-dialog  :visible.sync="dialog.batchCar" width="35%">
            <span slot="title" class="dialog-title">
                   <h3 class="bartchCar">批量派车</h3>
                     <span style="font-size: 12px;">说明：批量派车是给选中的发货计划派同一辆车</span>
                </span>
            <div class="body-info">
                <el-form :model="extra" status-icon  label-width="100px" >
                    <el-form-item label="车牌号" >
                        <el-input  v-model="extra.careNo" ></el-input>
                    </el-form-item>
                    <el-form-item label="司机姓名" >
                        <el-input  v-model="extra.driverName" ></el-input>
                    </el-form-item>
                    <el-form-item label="司机号码" >
                        <el-input  v-model="extra.driverContact" ></el-input>
                    </el-form-item>

                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                    <el-button @click="dialog.batchCar=false">取 消</el-button>
                    <el-button type="primary" @click="batchCarSub">确 定</el-button>
                </span>
        </el-dialog>
        <!--派车/派车调整  -->
        <el-dialog title="派车/派车调整" :visible.sync="dialog.sendCar" width="35%">
            <div class="body-info">
                <el-form :model="extra" status-icon  label-width="100px" >
                    <el-form-item label="车牌号">
                        <el-input  v-model="extra.careNo"></el-input>
                    </el-form-item>
                    <el-form-item label="司机姓名">
                        <el-input  v-model="extra.driverName"></el-input>
                    </el-form-item>
                    <el-form-item label="司机号码">
                        <el-input  v-model="extra.driverContact" ></el-input>
                    </el-form-item>

                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                    <el-button @click="dialog.sendCar=false">取 消</el-button>
                    <el-button type="primary" @click="sendCarSubmit">确 定</el-button>
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
        new Vue({
            el: '#plan-content',
            data: {
                activeName: 'first',
                first:{
                    list:[],
                    selects:[],
                    likeString: '',//发货单号，收货客户，运输路线
                    ordinateState: '',//接单状态 0:为接单，1：已接单
                    subordinateState:'',
                    generate: '',//是否生成发货单 false：未生成 true：已生成 不传全部
                    lastCompnayKey: '',//来源 客户ID
                    allocate: false,//是否分配 true：已分配， false：未分配
                    total: 1,//总页数
                    num: 1,//当前页
                    size: 12//每页的数量
                },
                second:{
                    list:[],
                    selects:[],
                    likeString: '',//发货单号，收货客户，运输路线
                    ordinateState:'',//下级接单状态 0:为接单，1：已接单
                    subordinateState:'',
                    generate: '',//是否生成发货单 false：未生成 true：已生成 不传全部
                    allocate: true,//是否分配 true：已分配， false：未分配
                    carStatus:'',
                    total: 1,//总页数
                    num: 1,//当前页
                    size: 12//每页的数量
                },
                dialog:{
                    import:false,
                    designate:false,
                    batchCar:false,
                    sendCar:false
                },
                designate:{
                    conveyKey:'',
                    contacts:'',
                    contactNumber:''
                },
                fileData:{
                    templateKey: null,//选择的模板
                },
                extra:{
                    careNo:'',//车牌号
                    driverName:'',//司机姓名
                    driverContact:'',//司机电话
                },

                orders:[],//要车单id集合
                conveys:[],//承运商数据
                sources:[],//来源数据
                templates:[]//模板数据
            },
            computed: {
                current: {
                    get: function () {
                        return this.activeName === 'first' ? this.first : this.second;
                    }
                }
            },
            mounted: function () {
                var _this = this;
                _this.render();
                //加载来源数据
                _this.request("/enterprise/plan/list/source", null, function(data){
                    this.sources = data.pulls;
                });
            },
            methods: {
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
                //复选框变化时
                handleSelectChange:function(selects){
                    if(selects && selects.length > 0){
                        this.current.selects = selects.map(function (item) { return item.id; });
                    }else{
                        this.current.selects = [];
                    }
                },
                // 当前页改变时触发事件
                currentChange(val) {
                    this.current.num = val;
                    this.render()
                },
                handleClick() {
                    var _this = this;
                    if(!_this.current.list || _this.current.list.length <= 0){
                        _this.render();
                    }
                },
                //文件上传提交
                submitUpload:function(){
                    this.$refs.upload.submit();
                },
                //批量派车
                batchCar:function(){
                    var _this = this;
                    if(_this.current.selects && _this.current.selects.length > 0){
                        _this.dialog.batchCar =true;
                        _this.extra.careNo='';
                        _this.extra.driverName='';
                        _this.extra.driverContact='';
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
                        orders :JSON.stringify(_this.current.selects),//计划单号集合
                        extra:JSON.stringify(_this.extra),
                    }
                    console.log(parmas)
                    if (!(/^1\d{10}$/.test(_this.extra.driverContact))){
                        _this.$message({
                            message:'请输入正确的手机号码',
                            type: 'error'
                        });
                        return false;
                    }
                    _this.request("/enterprise/plan/car/send", parmas, function(data){
                        console.log(data)
                       _this.dialog.batchCar =false;
                      _this.render()
                    });
                },
                //派车/派车调整
                sendCar:function(key,type){
                    var _this =this;
                    console.log(key)
                    _this.dialog.sendCar =true;
                    _this.orders = [];
                    _this.extra.careNo='';
                    _this.extra.driverName='';
                    _this.extra.driverContact='';
                    if (type==2) {
                        _this.$http.get(base_url+"/enterprise/plan/detail/"+key, null, {emulateJSON: false})
                            .then(
                                (response)=>{
                                    console.log(response);
                                    if (response.body.success) {
                                        _this.extra.careNo=response.body.result.extra.careNo;
                                        _this.extra.driverName=response.body.result.extra.driverName;
                                        _this.extra.driverContact=response.body.result.extra.driverContact;
                                    }
                                },
                                (error)=>{
                                    console.log(error);
                                }
                            )
                    }
                    _this.orders.push(key)
                },
                //派车/派车调整提交
                sendCarSubmit: function () {
                    var _this = this;
                    var parmas = {
                        orders :JSON.stringify(_this.orders),//要车单号集合
                        extra:JSON.stringify(_this.extra),
                    }
                    console.log(parmas)
                    if (!(/^1\d{10}$/.test(_this.extra.driverContact))){
                        _this.$message({
                            message:'请输入正确的手机号码',
                            type: 'error'
                        });
                        return false;
                    }
                    _this.request("/enterprise/plan/car/send", parmas, function(data){
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
                render: function (num) {
                    var _this = this, c = _this.current;
                    console.log(c);
                    var parmas = {
                        likeString: c.likeString,//发货单号，收货客户，运输路线
                        subordinateState:c.subordinateState,//下级接单状态 0:下级未接单，1：下级已接单
                        ordinateState: c.ordinateState,//接单状态 0:为接单，1：已接单
                        generate: c.generate,//是否生成发货单 false：未生成 true：已生成 不传全部
                        lastCompnayKey: c.lastCompnayKey,//来源 客户ID
                        allocate: c.allocate,//是否分配 true：已分配， false：未分配
                        carStatus:c.carStatus?c.carStatus:'',//派车状态
                        num: num ? num: c.num,//当前页
                        size: c.size//每页的数量
                    };
                    _this.request("/enterprise/plan/shipper/search", parmas, function(data){
                        this.current.list = data.results.collection;
                        this.current.total = data.results.total
                    });
                },
                operate: function (type, key, identification) {//查看
                    var _this = this;
                    var options = {
                        type: 2,
                        title: '查看计划详情',
                        content: "/enterprise/plan/operate/detail?key=" + key,
                    };
                    if (type === 2) {
                        options.title = '编辑计划';
                        options.content = "/enterprise/plan/operate/edit?key=" + key+'&type='+identification;
                        options.end=function () {
                            _this.render()
                        }
                    } else if (type === 3) {
                        options.title = '生成发货单';
                        options.content = "/enterprise/plan/operate/build?key="+key+'&type='+identification;
                        options.end=function () {
                            _this.render()
                        }
                    } else if (type === 4) {
                        options.title = '订单详情';
                        options.content = "/enterprise/order/manage/detail?orderKey=" + key;
                    }
                    layer.full(layer.open(options));
                },
                deletePlan: function (key) {//删除
                    var _this = this;

					_this.$confirm('确定要删除该计划吗?', '删除发货计划', {
						showClose: false,
						closeOnClickModal: false,
						closeOnPressEscape: false,
						type: 'warning'
					}).then(function() {
						_this.request("/enterprise/plan/delete/" + key, null, function(data) {
							_this.$message({
								type: 'success',
								message: data.message
							});
							_this.render();
						});
					});
                },
                downTemplate:function(){
                    var _this = this;
                    if(_this.fileData.templateKey){
                        _this.request("/enterprise/template/download/" + _this.fileData.templateKey, null, function(data){
                            if (data.success) {
                                $.util.download(data.url);
                            } else {
                                _this.$message({ message: data.message, type: 'error' });
                            }
                        });
                    }else{
                        _this.$message({ message: '请选择一个发货计划模板', type: 'error' })
                    }
                },
                importFile: function (res, f, fs) {//导入
                    var _this = this;
                    if(_this.dialog.import){
                        if(res){
                            if(res.success){
                                $.util.success(res.message, function () {
                                    _this.dialog.import = false;
                                    _this.render();
                                }, 3000);
                            }else{
                                f.status='failure';
                                if (res.result && res.result.url) {
                                    $.util.danger('有异常数据', res.message, function () {
                                        $.util.download(res.result.url);
                                        _this.dialog.import = false;
                                        _this.render();
                                    }, 3000);
                                } else {
                                    $.util.error(res.message);
                                }
                            }
                        }
                    }else{
                        _this.fileData.templateKey = null;
                        _this.request("/enterprise/template/list", {category: 2}, function(data){
                            this.templates = data.templates;
                            _this.fileData.templateKey = _this.templates[0].key;
                        });
                        this.dialog.import = true;
                    }
                },
                //录入
                entryFile:function () {
                    var _this = this ;
                    layer.full(layer.open({
                        type: 2,
                        title: '录入发货计划',
                        content: "/enterprise/plan/operate/add?type=1",
                        end: function () {
                            _this.render()
                        }
                    }));
                },/*
                //确认接单
                accepts:function(){
                    var _this = this;
                    if(_this.current.selects && _this.current.selects.length > 0){
                        _this.$confirm('确定接受被分配到的计划?', '确认接单', {  type: 'warning' }).then(() => {
                            _this.request("/enterprise/plan/recive", { planKeys: _this.current.selects.join(',') }, function(data){
                                this.$message({ message: data.message, type: 'success' })
                                this.render();
                            });
                        });
                    }else{
                        _this.$message({ message: '请至少选择一条发货计划', type: 'error' })
                    }
                },*/
                //分配物流商
                designates: function () {
                    var _this = this;
                    if(_this.current.selects && _this.current.selects.length > 0){
                        if(_this.dialog.designate){
                            _this.request("/enterprise/plan/designate", {
                                planKeys: _this.current.selects.join(','),
                                customerKey:_this.designate.conveyKey,
                                driverName:_this.designate.contacts,
                                driverContact:_this.designate.contactNumber
                            }, function(data){
                                this.dialog.designate = false;
                                this.$message({ message: data.message, type: 'success' })
                                this.render();
                            });
                        }else{
                            _this.designate.conveyKey = '';
                            _this.designate.contacts = '';
                            _this.designate.contactNumber = '';
                            _this.request("/enterprise/customer/list", {reg:1, status: 1}, function(data){
                                this.conveys = data.customers;
                            });
                            this.dialog.designate = true;
                        }
                    }else{
                        _this.$message({ message: '请至少选择一条发货计划', type: 'error' })
                    }
                },
                //生成发货单
                generates: function () {
                    var _this = this;
                    if(_this.current.selects && _this.current.selects.length > 0){
                        _this.$confirm('确定要将选中的[ '+ _this.current.selects.length +' ]条发货计划生成发货单吗?', '生成发货单', {  type: 'warning' }).then(() => {
                            _this.request("/enterprise/plan/shipper/generate/barch", { planKeys: _this.current.selects.join(',') }, function(data){
                                this.$message({ message: data.message, type: 'success' })
                                this.render();
                            });
                        });
                    }else{
                        _this.$message({ message: '请至少选择一条发货计划', type: 'error' })
                    }
                }
            }
        });

        // function importByExcel(vm) {
        //     var panel = $.util.panel('导入发货单', $(".import_task_panel").html());
        //     panel.initialize = function (model) {
        //         var $editBody = this.$body;
        //         $editBody.find("input:file").on('change', function () {
        //             var fileName = this.value;
        //             //校验文件格式
        //             var fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        //             if (fileType != "xlsx" && fileType != "xls") {
        //                 $.util.error("选择的文件格式不支持！");
        //                 return false;
        //             }
        //             $(this).parent().parent().find("input[name='fileName']").val(fileName);
        //         })
        //         $.util.json(base_url + "/enterprise/template/list", {category: 2}, function (data) {
        //             if (data.success) {
        //                 var $select = $editBody.find("select");
        //                 $select.html('');
        //                 $.each(data.templates, function (idx, obj) {
        //                     if (obj.fettle == 2) {
        //                         $select.append("<option value='" + obj.key + "' selected>" + obj.name + "</option>");
        //                     } else {
        //                         $select.append("<option value='" + obj.key + "'>" + obj.name + "</option>");
        //                     }
        //                 });
        //             } else {
        //                 $.util.error(data.message);
        //             }
        //         })
        //     };
        //     panel.addButton({
        //         text: '模板下载', alignment: 'left', action: function () {
        //             var key = this.$body.find("select").val();
        //             if (key) {
        //                 $.util.json(base_url + "/enterprise/template/download/" + key, null, function (data) {
        //                     if (data.success) {
        //                         $.util.download(data.url);
        //                     } else {
        //                         $.util.error(data.message);
        //                     }
        //                 })
        //             } else {
        //                 $.util.warning("请先选择一个模板");
        //             }
        //             return false;
        //         }
        //     });
        //     panel.addButton(function () {
        //         var editBody = this.$body, formData = new FormData();
        //         var templateKey = editBody.find("select").val();
        //         if (!templateKey) {
        //             return $.util.error("请选择导入需要使用的模板");
        //         }
        //         formData.append("templateKey", templateKey);
        //         var file = editBody.find("input:file");
        //         if (!file.val()) {
        //             return $.util.error("请选择需要导入的excel文件！");
        //         }
        //         formData.append("file", file[0].files[0]);
        //         $.ajax({
        //             url: base_url + "/enterprise/plan/coveyer/import/excel",
        //             type: 'POST',
        //             data: formData,
        //             processData: false, // 告诉jQuery不要去处理发送的数据
        //             contentType: false,// 告诉jQuery不要去设置Content-Type请求头
        //             success: function (response) {
        //                 if (response.success) {
        //                     $.util.success(response.message, function () {
        //                         vm.render();
        //                     }, 3000);
        //                 } else {
        //                     if (response.result && response.result.url) {
        //                         $.util.danger('有异常数据', response.message, function () {
        //                             $.util.download(response.result.url);
        //                             vm.render();
        //                         }, 3000);
        //                     } else {
        //                         $.util.error(response.message);
        //                     }
        //                 }
        //             }
        //         });
        //     });
        //     panel.open();
        // }
    });
</script>
</html>