<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <title>合同物流管理平台-入库管理-出库批次查询</title>
    <%@ include file="/views/include/head.jsp" %>

    <link rel="stylesheet" href="${baseStatic}element-ui/lib/theme-chalk/index.css"/>
    <link rel="stylesheet" href="${baseStatic}css/order.css?times=${times}"/>
    <style>
        .exportShipment, .printShipment {
            width: 110px;
            margin-left: 15px;
            height: 40px;
            line-height: 40px;
            text-align: center;
        }

        .btnInfo {
            margin-top: 20px;
        }

        .btnInfo .el-button {
            padding: 0px 10px;
        }

        .topLeft {
            float: none;
        }

        .fl {
            float: left;
        }

        .fr {
            float: right;
        }

        .box2 {
            width: 80%;
            margin: 0 auto;
            margin-bottom: 50px;
        }

        .topText p {
            font-weight: 700;
            margin-bottom: 30px;
        }

        .title {

            margin: 100px auto;
            text-align: center;
        }

        .printTable tr {
            height: 40px;
            line-height: 40px;
        }

        .printTable tr td {
            text-align: center;
            padding: 0px 5px;
        }

        .printTable tr td:nth-child(1), .printTable tr th:nth-child(1) {
            text-align: center;
            padding: 0px 5px;
        }

        .printContent {
            padding-top: 20px;
        }

        .printTable {
            border: 1px solid #000;
        }

        .printTable tr th,
        .printTable tr td {
            border: 1px solid #000;
        }

        .printDetail tr td:first-child {
            border-left: 0;
            border-top: 0;
            width: 58%;
        }

        .printDetail tr td:last-child {
            border-left: 0;
            border-top: 0;
            border-right: 0;
            width: 25%;
        }

        .printDetail tr:last-child td {
            border-bottom: 0;
        }

    </style>
</head>

<body>
<div id="warehousing-management-content" class="clearfix">
    <div class="topInfo clearfix">
        <div class="topLeft clearfix">
            <input type="text" placeholder="批次号" v-model="likefirst">
            <input type="text" placeholder="客户/品名" v-model="likesecond">
            <span class="date">发货日期</span>
            <el-date-picker v-model="firstTime" type="datetime" placeholder="开始日期" value-format="yyyy-MM-dd HH:mm:ss">
            </el-date-picker>
            <span>至</span>
            <el-date-picker v-model="secondTime" type="datetime" placeholder="结束日期" value-format="yyyy-MM-dd  HH:mm:ss">
            </el-date-picker>
            <el-button type="primary" class="search" @click="search">查询</el-button>
        </div>
        <div class="btnInfo">
            <el-button type="primary" class="exportShipment" @click="exportShipment">导出出货单</el-button>
            <el-button type="primary" class="printShipment" @click="printShipment">打印出货单</el-button>
        </div>
    </div>

    <template>
        <table>
            <thead>
            <tr>
                <th style="width:5%"><input type="checkbox" @click="allAndNotAll" id="allAndNotAll">全选</th>
                <th style="width:10%">收货客户</th>
                <th style="width:15%">发货日期</th>
                <th style="width:15%">品名</th>
                <th style="width:15%">批次号</th>
                <th style="width:10%">发货数量</th>
                <th style="width:10%">入库日期</th>
                <th style="width:10%">发货客户</th>
                <th style="width:10%">操作人</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="item in batchList" :key="item.key">
                <td><input type="checkbox" name="item" :data-key="item.key"></td>
                <td>{{item.receiveName}}</td>
                <td>{{item.deliveryTime|datetime}}</td>
                <td>{{item.materialName}}</td>
                <td>{{item.batchNumber}}</td>
                <td>{{item.outboundQuantity}}</td>
                <td>{{item.storageTime |datetime}}</td>
                <td>{{item.shipperName}}</td>
                <td>{{item.uname}}</td>
            </tr>

            </tbody>
        </table>
        <el-pagination
                background
                layout="total,prev, pager, next"
                next-text="下一页"
                prev-text="上一页"
                :total="totalCount"
                @prev-click="prevClick"
                @next-click="nextClick"
                @current-change="currentChange"
        >
        </el-pagination>

        <el-dialog title="打印" :visible.sync="dialogVisible2" width="50%" :before-close="handleClose2">
            <div class="box2" id="box2">
                <div class="printContent" v-for="(item,index) in printList1">
                    <h3 class="title">送货批次单</h3>
                    <div class="topText clearfix">
                        <p class="fl">经销商:{{item.receiveName}}</p>
                        <p class="fr">发货日期:{{item.dateFormat|date}}</p>
                    </div>
                    <table class="printTable" style="page-break-after:always">
                        <tr>
                            <th style="width: 35%">品名</th>
                            <th style="width: 35%">批次号</th>
                            <th style="width: 15%">数量</th>
                            <th style="width: 15%">汇总数量</th>
                        </tr>
                        <tr v-for="(s, i) in item.printSummary">
                            <td>{{s.materialName}}</td>
                            <td colspan="2" style="padding: 0;">
                                <table style="width: 100%;border: 0;margin-top: 0;height:100%" class="printDetail">
                                    <tr v-for="d in s.printDetail">
                                        <td>{{d.batchNumber}}</td>
                                        <td>{{d.outboundQuantity}}</td>
                                    </tr>
                                </table>
                            </td>
                            <td>{{s.summaryQuantity}}</td>
                        </tr>
                    </table>
                </div>


            </div>
            <span slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="print">打印</el-button>
                    <el-button @click="dialogVisible2 = false">取 消</el-button>
                </span>
        </el-dialog>
    </template>

</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
<script type="text/javascript" src="${baseStatic}vue/vue-resource.js"></script>
<%--<script type="text/javascript" src="${baseStatic}plugin/js/jquery-confirm.min.js"></script>--%>
<script>
    const vm = new Vue({

        el: '#warehousing-management-content',
        data: {
            //当前页
            pageNum: 1,
            //每页数量
            pageSize: 10,
            totalCount: 0,
            dialogVisible1: false,
            dialogVisible2: false,
            firstTime: '',//开始日期
            secondTime: '',//结束日期
            likefirst: '', //品名/客户名称
            likesecond: '',//批次号
            batchList: [],
            dialogVisible2: false,
            exportShipmentList: [],//导出出货单数组
            printShipmentList: [],//要打印的出货单数组
            printList1: [],//打印所需的数据


        },
        mounted() {
            this.render()
        },
        methods: {
            // 显示总页数事件
            handleSizeChange(val) {
                console.log(`每页 ${val} 条`);
            },
            //点击上一页事件
            prevClick(val) {
                console.log('上一页')
                if (this.pageNum < 1) {
                    return
                } else {
                    this.pageNum--;
                    this.render()
                }
            },
            //点击下一页事件
            nextClick(val) {
                console.log('下一页');
                if (this.pageNum > Math.ceil(this.totalCount / this.pageSize)) {
                    return
                } else {
                    this.pageNum++;
                    //渲染数据到页面上
                    this.render()
                }
            },

            // 当前页改变时触发事件
            currentChange(val) {
                console.log(val)
                this.pageNum = val;
                this.render()

            },
            //对话框关闭事件
            handleClose2(done) {
                done();
            },
            search: function () {
                this.render()
            },
            render: function () {
                var _this = this;
                var parmas = {

                    likefirst: _this.likefirst,
                    likesecond: _this.likesecond,
                    firstTime: _this.firstTime,
                    secondTime: _this.secondTime,
                    num: _this.pageNum,
                    size: _this.pageSize,
                }
                _this.$http.post(base_url + "/enterprise/depot/batch/search", parmas, {emulateJSON: false})
                    .then(
                        (response) => {
                    console.log(response);
                if (response.body.success) {
                    _this.batchList = response.body.page.collection
                    _this.totalCount = response.body.page.total
                }

            },
                (error) => {
                    console.log(error);
                }
            )
            },
            //导出出货单
            exportShipment: function () {
                var _this = this;
                _this.exportShipmentList = [];
                //获取所有被选中的批次单
                var arr = $('[name=item]:checked')
                console.log(arr)
                if (arr.length != 0) {
                    for (var i = 0; i < arr.length; i++) {
                        _this.exportShipmentList.push($(arr[i]).data('key'))
                    }
                    var parmas = {
                        outboundIds: JSON.stringify(_this.exportShipmentList)
                    }
                    console.log(_this.exportShipmentList)
                    _this.$http.post(base_url + "/enterprise/depot/export/outbound", parmas, {emulateJSON: false})
                        .then(
                            (response) => {
                        console.log(response);
                    if (response.body.success) {
                        $.util.download(response.body.url)
                    }

                },
                    (error) => {
                        console.log(error);
                    }
                )
                } else {
                    _this.$message({
                        message: '请先选择出货单',
                        type: 'warning',
                        duration: 1000
                    })
                }
            },
            //打印对话框
            printShipment: function () {
                var _this = this;
                _this.printShipmentList = [];
                //获取所有的打印数据
                var arr = $('[name=item]:checked');
                if (arr.length != 0) {
                    _this.dialogVisible2 = true;
                    for (var i = 0; i < arr.length; i++) {
                        _this.printShipmentList.push($(arr[i]).data('key'))
                    }
                    var parmas = {
                        outboundIds: JSON.stringify(_this.printShipmentList)
                    }
                    console.log(_this.printShipmentList)
                    _this.$http.post(base_url + "/enterprise/depot/print/outbound/", parmas, {emulateJSON: false})
                        .then(
                            (response) => {
                        console.log(response);
                    if (response.body.success) {
                        _this.printList1 = response.body.results

                    }
                },
                    (error) => {
                        console.log(error);
                    }
                )
                } else {
                    _this.$message({
                        message: '请先选择出货单',
                        type: 'warning',
                        duration: 1000
                    })
                }


            },
            print: function () {
                let subOutputRankPrint = document.getElementById('box2');// 获取打印区域
                let newContent = subOutputRankPrint.innerHTML;
                let oldContent = document.body.innerHTML;
                document.body.innerHTML = newContent;
                window.print();
                window.location.reload();
                document.body.innerHTML = oldContent;
                return false;
            },
            //全选反选
            allAndNotAll: function () {
                console.log($('#allAndNotAll')[0].checked)
                if ($('#allAndNotAll')[0].checked) {

                    $("[name=item]:checkbox").prop("checked", true)
                } else {
                    $("[name=item]:checkbox").prop("checked", false)

                }
            }
        }
    })
</script>
</html>