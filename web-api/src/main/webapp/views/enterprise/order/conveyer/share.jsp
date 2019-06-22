<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-运输管理-承运管理-订单分享管理</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}element-ui/lib/theme-chalk/index.css"/>
    <style type="text/css">

        /*::-webkit-scrollbar-track
        {
            -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3);
            background-color: #F5F5F5;
        }

        ::-webkit-scrollbar
        {
            width: 10px;
            background-color: #F5F5F5;
        }

        ::-webkit-scrollbar-thumb
        {
            background-color: #0ae;

        }*/

    </style>
</head>
<body>
<div class="share-content" id="vue-213213213">
    <template>
    <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="我的分享" name="first">
            <el-form :inline="true" class="demo-form-inline">
                <el-form-item>
                    <el-input v-model="share.likeString" placeholder="系统单号/送货单号"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-select v-model="share.timeType" placeholder="发货日期">
                        <el-option label="近三天发货" value="3"></el-option>
                        <el-option label="近一周发货" value="7"></el-option>
                        <el-option label="近一个月发货" value="30"></el-option>
                        <el-option label="全部发货日期" value=""></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-select v-model="share.targetKey" placeholder="分享至">
                        <el-option label="全部分享" value=""></el-option>
                        <el-option v-for="item in shareTargets" :label="item.companyName" :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="loadShares">查询</el-button>
                </el-form-item>
            </el-form>
            <el-table :data="shareCollection" style="width: 100%" height="450" border="true">
                <el-table-column fixed prop="bindCode" label="系统单号" width="150" :formatter="bindCodeformat"></el-table-column>
                <el-table-column fixed prop="deliveryNo" label="送货单号" width="120"></el-table-column>
                <el-table-column fixed prop="deliveryTime" label="发货日期" width="100" :formatter="dateformat"></el-table-column>
                <el-table-column prop="shipper.companyName" label="发货方" width="240"></el-table-column>
                <el-table-column prop="receive.companyName" label="收货客户" width="240"></el-table-column>
                <el-table-column prop="receiverName" label="收货人" width="80"></el-table-column>
                <el-table-column prop="receiverContact" label="联系方式" width="130"></el-table-column>
                <el-table-column prop="fettle" label="订单状态" width="100" :formatter="orderFettle"></el-table-column>
                <el-table-column prop="isReceipt" label="回单状态" width="100" :formatter="receiptFettle"></el-table-column>
                <el-table-column prop="shareName" label="分享至" width="250"></el-table-column>
                <el-table-column prop="location" label="当前位置" width="500"></el-table-column>
                <el-table-column fixed="right" label="操作" width="80">
                    <template slot-scope="scope">
                        <a href="javascript:;" @click="handleLook(scope.row.id)" style="color: #409EFF;">查看</a>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination background layout="prev, pager, next" :page-size="shareSize" :total="shareTotal" @current-change="handleSharePage" v-if="shareTotal > shareSize"></el-pagination>
        </el-tab-pane>
        <el-tab-pane label="分享给我的" name="second">
            <el-form :inline="true" class="demo-form-inline">
                <el-form-item>
                    <el-input v-model="recive.likeString" placeholder="系统单号/送货单号"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-select v-model="recive.uploadReceipt" placeholder="回单">
                        <el-option label="全部" value=""></el-option>
                        <el-option label="已上传回单" value="1"></el-option>
                        <el-option label="未上传回单" value="0"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-select v-model="recive.timeType" placeholder="发货日期">
                        <el-option label="近三天发货" value="3"></el-option>
                        <el-option label="近一周发货" value="7"></el-option>
                        <el-option label="近一个月发货" value="30"></el-option>
                        <el-option label="全部发货日期" value=""></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-select v-model="recive.targetKey" placeholder="数据来源">
                        <el-option label="全部数据来源" value=""></el-option>
                        <el-option v-for="item in reciveTargets" :label="item.companyName" :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="loadRecives">查询</el-button>
                </el-form-item>
            </el-form>
            <el-table :data="reciveCollection" style="width: 100%" height="450" border="true">
                <el-table-column fixed prop="bindCode" label="系统单号" width="150"></el-table-column>
                <el-table-column fixed prop="deliveryNo" label="送货单号" width="120"></el-table-column>
                <el-table-column fixed prop="deliveryTime" label="发货日期" width="100" :formatter="dateformat"></el-table-column>
                <el-table-column prop="shipper.companyName" label="发货方" width="220"></el-table-column>
                <el-table-column prop="receive.companyName" label="收货客户" width="220"></el-table-column>
                <el-table-column prop="receiverName" label="收货人" width="80"></el-table-column>
                <el-table-column prop="receiverContact" label="联系方式" width="130"></el-table-column>
                <el-table-column prop="receiveAddress" label="收货地址"width="300"></el-table-column>
                <el-table-column prop="fettle" label="订单状态" width="100" :formatter="orderFettle"></el-table-column>
                <el-table-column prop="location" label="当前位置" width="220"></el-table-column>
                <el-table-column prop="isReceipt" label="回单状态" width="100" :formatter="receiptFettle"></el-table-column>
                <el-table-column prop="shareName" label="来源" width="220"></el-table-column>
                <el-table-column fixed="right" label="操作" width="80">
                    <template slot-scope="scope">
                        <a href="javascript:;" @click="handleLook(scope.row.id)" style="color: #409EFF;">查看</a>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination background layout="prev, pager, next" :page-size="reciveSize" :total="reciveTotal" @current-change="handleRecivePage" v-if="reciveTotal > reciveSize"></el-pagination>
        </el-tab-pane>
    </el-tabs>
    </template>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
<script type="text/javascript" src="${baseStatic}vue/vue-resource.js"></script>
<script>
    $(document).ready(function () {
        var vm = new Vue({
            el:'#vue-213213213',
            data:{
                activeName: 'first',
                shareTargets:[],
                shareCollection:[],
                shareTotal:0,
                shareSize:8,
                reciveTargets:[],
                reciveCollection:[],
                reciveTotal:0,
                reciveSize:8,
                share: {
                    likeString: '',
                    timeType: '',
                    targetKey:'',
                },
                recive:{
                    likeString: '',
                    timeType: '',
                    targetKey:'',
                    uploadReceipt: ''
                }
            },
            created:function () {
                this.loadtargets(true);
                this.loadtargets(false);
                this.loadShares();
            },
            methods: {
                handleClick: function(tab, event) {
                    if(tab.name =='first'){
                        if(this.shareCollection.length <= 0){
                            this.loadShares();
                        }
                    }else{
                        if(this.reciveCollection.length <= 0){
                            this.loadRecives();
                        }
                    }
                },
                handleLook: function(id) {
                    layer.full(layer.open({
                        type: 2,
                        title:'查看订单详情',
                        content: base_url + "/enterprise/order/share/detail?orderKey="+ id
                    }));

                    //window.location.href = base_url + "/enterprise/order/manage/detail?orderKey="+ id;
                },
                loadShares:function(page){
                    var _this = this;
                    var parmas = {likeString : _this.share.likeString, timeType:_this.share.timeType, targetKey:_this.share.targetKey, flag: true, num: page | 1 };
                    $.util.json(base_url+"/enterprise/order/share/search", parmas ,function(data){
                        if(data.success){
                            _this.shareCollection = data.page.collection;
                            _this.shareTotal = data.page.total;
                            _this.shareSize = data.page.pageSize;
                        }
                    });
                },
                handleSharePage:function(val) {
                    this.loadShares(val);
                },
                loadRecives:function(page){
                    var _this = this;
                    var parmas = {likeString : _this.share.likeString, timeType:_this.share.timeType, targetKey:_this.share.targetKey, uploadReceipt: _this.recive.uploadReceipt, flag: false, num: page | 1  };
                    $.util.json(base_url+"/enterprise/order/share/search", parmas ,function(data){
                        if(data.success){
                            _this.reciveCollection = data.page.collection;
                            _this.reciveTotal = data.page.total;
                            _this.reciveSize = data.page.pageSize;
                        }
                    });
                },
                handleRecivePage:function(val) {
                    this.loadRecives(val);
                },
                bindCodeformat:function(row, column, cellValue, index){
                    return cellValue ? cellValue : '未绑码';
                },
                dateformat:function(row, column, cellValue, index){
                    return moment(cellValue).format("YYYY-MM-DD");
                },
                orderFettle:function(row, column, cellValue, index){
                    return cellValue == 4 ? '已到货': '运输中';
                },
                receiptFettle:function(row, column, cellValue, index){
                    return (cellValue == null || cellValue <= 0) ? '未上传': '已上传';
                },
                loadtargets:function(flag){
                    var _this = this;
                    $.util.json(base_url+"/enterprise/order/share/targets", {type : flag ? 2 : 4} ,function(data){
                        if(data.success){
                            if(flag){
                                _this.shareTargets = data.targets;
                            }else{
                                _this.reciveTargets = data.targets;
                            }
                        }
                    });
                }
            }
        });
    });
</script>
</html>