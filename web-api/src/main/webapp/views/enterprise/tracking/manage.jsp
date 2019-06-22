<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流管理-跟踪管理-跟踪管理</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}element-ui/lib/theme-chalk/index.css"/>
    <style type="text/css">
        .fn {
            width: 15%;
        }

        .el-input__inner {
            height: 30px;
            line-height: 30px;
        }

        .el-input__icon {
            line-height: 30px;
        }

        .table {
            margin-top: 40px;
        }

        .el-button {
            padding: 7px 20px;
        }

        .pagination {
            margin-top: 20px;
        }
        .searchBar {
            padding-top: 40px;
        }
        .trackingManagement {
            padding: 0 10px;
        }
        .m15 {
            margin-left: 15px;
        }
        .fr {
            float: right;
            margin-right: 20px;
        }
        .frequency {
            border: 1px solid #ccc;
            width: 40%;
        }
        .el-dialog__header {
            border-bottom: 1px solid #ccc;
            padding: 15px 20px 15px;
        }
        .el-dialog__footer {
            border-top: 1px solid #ccc;
            padding: 15px 20px 15px;
        }
        .el-dialog__body {
            position: relative;
            height: 250px;
        }
        .body-info {
            height: auto;
            width: 100%;
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%);
            text-align: center;
        }
        .el-tooltip,.cell {
            overflow: hidden;
            text-overflow:ellipsis;
            white-space: nowrap;

        }
        .el-table .cell {
            overflow: hidden;
            text-overflow:ellipsis;
            white-space: nowrap;
        }
    </style>
</head>
<body>
<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
<!--[if lt IE 9]>
<script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
<script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<div class="trackingManagement" id="trackingManagement">
    <template>
        <div class="searchBar">
            <el-input v-model="likeString" placeholder="送货单号/物流商/收货客户" class="fn"></el-input>
            <el-date-picker v-model="startTime" type="date" placeholder="开始时间">
            </el-date-picker>
            <span>-</span>
            <el-date-picker v-model="endTime" type="date" placeholder="结束时间">
            </el-date-picker>
            <el-select v-model="delayedWarning" placeholder="延迟预警">
                <el-option label="正常运输" value="1"></el-option>
                <el-option label="可能延迟" value="2"></el-option>
                <el-option label="已延迟" value="3"></el-option>
            </el-select>
            <el-select v-model="deliveryWarning" placeholder="提货预警">
                <el-option label="正常提货" value="1"></el-option>
                <el-option label="延迟提货" value="2"></el-option>
            </el-select>
            <el-button type="primary" class="m15" @click="search">查询</el-button>
            <el-button type="primary" @click='tracking' class="fr">跟踪设置</el-button>
        </div>

        <div class="table">
            <el-table :data="list" style="width: 100%" height="450" border="true" @selection-change="handleSelectChange">
                <el-table-column type="selection" width="35"></el-table-column>
                <el-table-column fixed  label="系统单号" width="150" align="center" show-overflow-tooltip>
                    <template te slot-scope="scope">
                        <span v-if="scope.row.bindCode!=null">{{ scope.row.bindCode }}</span>
                        <span v-else>未绑单</span>
                    </template>
                </el-table-column>
                <el-table-column fixed prop="deliveryNo" label="送货单号" width="150" align="center" show-overflow-tooltip></el-table-column>
                <el-table-column fixed prop="shipper.customerName" label="发货客户" width="150" align="center" show-overflow-tooltip></el-table-column>
                <el-table-column fixed prop="convey.companyName" label="物流商" width="150" align="center" show-overflow-tooltip></el-table-column>
                <el-table-column prop="receive.companyName" label="收货客户" width="150" align="center" show-overflow-tooltip></el-table-column>
                <el-table-column  label="运输路线" width="150" align="center" show-overflow-tooltip>
                    <template slot-scope="scope">
                        <span v-if="scope.row.extra!=null">{{scope.row.extra.originStation+'-'+scope.row.extra.arrivalStation }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="发货日期" width="150" align="center">
                    <template slot-scope="scope">
                        <span>{{ scope.row.deliveryTime | date }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="计划到货日期" width="150" align="center" >
                    <template slot-scope="scope">
                        <span>{{ scope.row.arrivalTime | date }}</span>
                    </template>
                </el-table-column>
                <el-table-column  label="最新动态" width="150" align="center" show-overflow-tooltip>
                     <template slot-scope="scope">
                         <span class="status" v-if="scope.row.operateNote.logType==1">录入发货单</span>
                         <span class="status" v-else-if="scope.row.operateNote.logType==12">导入发货单</span>
                         <span class="status" v-else-if="scope.row.operateNote.logType==13">生成发货单</span>
                         <span class="status" v-else-if="scope.row.operateNote.logType==14">系统传输发货单</span>
                         <span class="status" v-else-if="scope.row.operateNote.logType==2">扫码绑单</span>
                         <%--上报位置 --%>
                         <span class="status" v-else-if="scope.row.operateNote.logType==3">{{scope.row.operateNote.logContext}}</span>
                         <span class="status" v-else-if="scope.row.operateNote.logType==4">上传回单</span>
                         <span class="status" v-else-if="scope.row.operateNote.logType==9">已签收</span>
                         <span class="status" v-else>装车完成</span>
                     </template>
                </el-table-column>
                <el-table-column label="跟踪时间" width="150" align="center" >
                    <template slot-scope="scope">
                        <span v-if="scope.row.receiveTime!=null">{{scope.row.receiveTime|date}}</span>
                        <span v-else>{{new Date()|date}}</span>
                    </template>
                </el-table-column>
                <%--<el-table-column  label="提货预警" width="150" align="center">--%>
                    <%--<template slot-scope="scope">--%>
                        <%--<span v-if="scope.row.pickupWarning==1">正常提货</span>--%>
                        <%--<span v-if="scope.row.pickupWarning==2">延迟提货</span>--%>
                    <%--</template>--%>
                <%--</el-table-column>--%>
                <el-table-column  label="延迟预警" width="150" align="center">
                    <template slot-scope="scope">
                        <span v-if="scope.row.delayWarning==1">正常运输</span>
                        <span v-if="scope.row.delayWarning==2">可能延迟</span>
                        <span v-if="scope.row.delayWarning==3">已延迟</span>
                    </template>
                </el-table-column>
                <el-table-column fixed="right" label="操作" width="160" align="center">
                    <template slot-scope="scope">
                        <span @click="operate(2,scope.row.id)">查看</span>
                    </template>
                </el-table-column>
            </el-table>
            <!-- 分页  -->
            <div class="pagination">
                <el-pagination background layout="total,prev, pager, next" :total="total" @current-change="currentChange">
                </el-pagination>
            </div>
        </div>
        <!-- 跟踪设置对话框 -->
        <el-dialog title="跟踪设置" :visible.sync="dialogVisible" width="35%" >
            <div class="body-info">
                <span style="margin-right: 15px;">设置跟踪频次</span>
                <input  placeholder="请输入正整数" v-model="frequency" class="frequency"/>
                <span>次/天</span>
            </div>
            <span slot="footer" class="dialog-footer">
                    <el-button @click="dialogVisible = false">取 消</el-button>
                    <el-button type="primary" @click="frequencySubmit">确 定</el-button>
                </span>
        </el-dialog>
    </template>
</div>
</body>
<%@ include file="/views/include/floor.jsp"%>
<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
<script type="text/javascript" src="${baseStatic}vue/vue-resource.js"></script>
<script type="text/javascript">
    const vm = new Vue({
        el: '#trackingManagement',
        data: {
            likeString: '',//送货单号/物流商/收货客户
            startTime: '',//开始时间
            endTime: '',//结束时间
            delayedWarning: '',//延迟预警
            deliveryWarning: '',//提货预警
            list: [],//列表数据
            dialogVisible:false,
            frequency:'',//跟踪频次
            pageNum: 1,//页数
            pageSize: 10,//每一页数量
            total:'',//总数量
        },
        mounted:function(){
            var _this = this;
            _this.render()
        },
        methods: {
            //复选框变化时
            handleSelectChange: function (selects) {
                 console.log(selects)
            },
            tracking:function(){
                this.dialogVisible = true;
            },
            currentChange(val) {
                this.pageNum= val;
               this.render(this.pageNum)
            },
            render:function(num){
                var _this = this;
                var parmas = {
                    likeString:_this.likeString,//送货单号/物流商/收货客户
                    firstTime:this.startTime,//开始时间
                    secondTime:_this.endTime,//结束时间
                    pickupWarning:_this.deliveryWarning,// 提货预警
                    delayWarning:_this.delayedWarning,//延迟预警
                    size:_this.pageSize,
                    num:num?num:_this.pageNum,
                }
                console.log(parmas)
                _this.request("/enterprise/tracking/search/list", parmas, function(data){
                    console.log(data)
                    _this.list = data.page.collection;
                    _this.total = data.page.total;
                });
            },
            operate: function (type, key) {//查看
                var _this = this;
                var options = {
                    type: 2,
                    title: '发货单详情',
                    content: "/enterprise/tracking/operate/detail?key="+key,
                };
                if (type === 2) {
                    options.title = '发货单详情';
                    options.content = "/enterprise/order/manage/detail?orderKey=" + key;
                    options.end=function () {

                    }
                }
                layer.full(layer.open(options));
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
            //查询
            search:function(){
                var _this = this;
               _this.render();
            },
            //频次提交
            frequencySubmit:function () {
                var _this = this;
                var parmas= {
                    frequency:_this.frequency
                };
                console.log(parmas)
                if(parmas.frequency==''){
                    _this.$message({ message: '频次不能为空', type: 'error' })
                }
                _this.request("/enterprise/tracking/setting", parmas, function(data){
                    console.log(data)
                    if(data.success){
                        _this.dialogVisible = false;
                        _this.$message({ message: data.message, type: 'success' })
                    }
                });
            }
        }
    })
</script>
</html>