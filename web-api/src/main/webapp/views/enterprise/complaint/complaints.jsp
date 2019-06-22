<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-投诉管理-客户投诉</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}element-ui/lib/theme-chalk/index.css"/>
    <style type="text/css">
        .customer-complaint {
            padding-left: 30px;
        }
        .search,.content{
         padding-top: 30px;
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
    </style>
</head>
<body>
<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
<!--[if lt IE 9]>
<script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
<script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->

<div class="customer-complaint" id="customer-complaint">
    <template>
        <div class="search">
            <el-input v-model="search.likeString" placeholder="送货单号/收货方/投诉人"></el-input>
            <el-date-picker v-model="search.firstTime" type="date" placeholder="开始时间" value-format="yyyy-MM-dd">
            </el-date-picker>
            --
            <el-date-picker v-model="search.secondTime" type="date" placeholder="结束时间" value-format="yyyy-MM-dd">
            </el-date-picker>
            <el-button type="primary" size='small' @click="searchRecord">查询</el-button>
        </div>
        <div class="content">
            <el-table :data="list" style="width: 100%" height="450" border="true" @selection-change="handleSelectChange">
                <el-table-column type="selection" width="35"></el-table-column>
                <el-table-column fixed label="送货单号" width="200" align="center" prop="order.deliveryNo"></el-table-column>
                <el-table-column fixed prop="receiveName" label="收货方" width="200" align="center" show-overflow-tooltip></el-table-column>
                <el-table-column fixed prop="complainant" label="投诉人" width="200" align="center" show-overflow-tooltip></el-table-column>
                <el-table-column fixed prop="complainantNumber" label="投诉人电话" width="200" align="center" show-overflow-tooltip></el-table-column>
                <el-table-column prop="complainantContent" label="投诉内容" width="600" align="center" show-overflow-tooltip></el-table-column>
                <el-table-column label="投诉时间" width="200" align="center" show-overflow-tooltip>
                    <template slot-scope="scope">
                        <span>{{ scope.row.createTime | date }}</span>
                    </template>
                </el-table-column>
                <el-table-column fixed="right" label="操作" width="160" align="center">
                    <template slot-scope="scope">
                        <span @click="operate(2,scope.row.orderId)" class="blue">查看</span>
                    </template>
                </el-table-column>
            </el-table>
            <!-- 分页  -->
            <div class="pagination">
                <el-pagination background layout="total,prev, pager, next" :total="total" @current-change="currentChange">
                </el-pagination>
            </div>
        </div>
    </template>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
<script type="text/javascript" src="${baseStatic}vue/vue-resource.js"></script>
<script>
    $(document).ready(function () {
        const vm = new Vue({
            el: '#customer-complaint',
            data: {
                search: {
                    likeString: '',//送货单号/收货方/投诉人
                    firstTime:'',//开始时间
                    secondTime:'',//结束时间
                },
                pageNum: 1,//页数
                pageSize: 10,//每一页数量
                total: 50,//总数量
                list:[]
            },
            mounted:function(){
                var _this =this;
               _this.render()
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
                  _this.render();
                },
                //查询
                searchRecord:function(){
               var  _this = this ;
                  _this.render()
                },
                render:function(num){
                    var _this = this;
                    var parmas = {
                        likeString:_this.search.likeString,//送货单号
                        firstTime:_this.search.firstTime,//开始时间
                        secondTime:_this.search.secondTime,//结束时间
                        size:_this.pageSize,
                        num:num?num:_this.pageNum,
                    }
                    console.log(parmas)
                    _this.request("/enterprise/complaint/list", parmas, function(data){
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
                    console.log(key)
                    var options = {
                        type: 2,
                        title: '订单详情',
                        content: "/enterprise/order/manage/detail?orderKey=" + key,
                    };
                    if (type === 2) {
                        options.title = '订单详情';
                        options.content ="/enterprise/order/manage/detail?orderKey=" + key;
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