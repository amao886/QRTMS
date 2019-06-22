<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <title>合同物流管理平台-出入库管理-入库单</title>
    <%@ include file="/views/include/head.jsp" %>

    <link rel="stylesheet" href="${baseStatic}element-ui/lib/theme-chalk/index.css"/>
    <link rel="stylesheet" href="${baseStatic}css/order.css?times=${times}"/>
    <style>
        .delete {
            color: #31acfa;
            cursor: pointer;
        }
        .topInfo .importInvoice, .topInfo .printing {
            float: left;
        }
        .operation {
            margin-top: 20px;
        }

    </style>
</head>

<body>
<div id="warehousing-management-content" class="clearfix">
    <div class="topInfo clearfix">
        <div class=" clearfix">
            <input type="text" placeholder="批次号/单号/车牌号" v-model="likefirst">
            <input type="text" placeholder="客户名称/品名" v-model="likesecond">
            <span class="date">入库日期</span>
            <el-date-picker v-model="firstTime" type="datetime" placeholder="开始日期"  value-format="yyyy-MM-dd HH:mm:ss">
            </el-date-picker>
            <span>至</span>
            <el-date-picker v-model="secondTime" type="datetime" placeholder="结束日期"  value-format="yyyy-MM-dd  HH:mm:ss">
            </el-date-picker>
            <el-button type="primary" class="search" @click="search">查询</el-button>
        </div>
        <div class=" clearfix operation">
            <el-button type="primary" class="importInvoice " @click='deleteData()'>删除</el-button>
            <el-button type="primary" class="printing " @click='printing'>打印</el-button>
            <el-button type="primary" class="importInvoice " @click='importInvoice'>导入</el-button>

        </div>
    </div>
    <template>
    <table>
        <thead>
        <tr>
            <th style="width:5%"><input type="checkbox" id="allAndNotAll" @click="allAndNotAll">全选</th>
            <th style="width:20%">批次号</th>
            <th style="width:10%">品名</th>
            <th style="width:10%">入库日期</th>
            <th style="width:15%">发货客户</th>
            <th style="width:15%">入库单号</th>
            <th style="width:15%">车牌号</th>
            <th style="width:10%">操作人</th>
        </tr>
        </thead>
        <tbody>

        <tr v-for="item in batchList" :key="item.key">
            <td><input type="checkbox" name="item" :data-key="item.batchNumber" :data-inbounds="item.key"></td>
            <td class="batchNumber"> {{item.batchNumber}}<p class="importNumber">(<span class="importNum">{{item.importTimes}}</span>)</p></td>
            <td>{{item.materialName}}</td>
            <td>{{item.storageTime | datetime}}</td>
            <td>{{item.customerName}}</td>
            <td>{{item.deliveryNo}}</td>
            <td>{{item.licensePlate}}</td>
            <td>{{item.uname}}</td>
            <%--<td>--%>
                <%--<span class="delete" :data-key="item.batchNumber" @click="deleteData(item.key,1)">删除</span>--%>
            <%--</td>--%>
        </tr>



        </tbody>
    </table>
    <el-pagination
            background
            next-text="下一页"
            prev-text="上一页"
            :total="totalCount"
            @prev-click="prevClick"
            @next-click="nextClick"
            :page-sizes="[10, 50, 100, 200]"
            :page-size="10"
            @size-change="handleSizeChange"
            @current-change="currentChange"
            layout="total,sizes,prev, pager, next"
    >
    </el-pagination>

    <!--  导入对话框 -->
    <el-dialog title="导入入库单" :visible.sync="dialogVisible1" width="80%" :before-close="handleClose1">
        <div class="box">
            <el-form label-width="80px" id="form"  enctype="multipart/form-data">
                <el-form-item label="文件名称" style="font-weight: 700">
                    <input type="text" v-model="fileText" id="file">
                    <span class="chooseFile">
                        <span>选择文件</span>
                      <input type="file" class="hideChooseFile" @change='files($event)' >
                    </span>

                </el-form-item>
            </el-form>
        </div>
        <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submit">导入</el-button>
                <el-button @click="dialogVisible1 = false">取 消</el-button>
            </span>
    </el-dialog>
    <!--  打印对话框 -->
    <el-dialog title="打印" :visible.sync="dialogVisible2" width="80%" :before-close="handleClose2">
        <div class="box1" id="subOutputRank-print">
            <p class="printOrder" v-for="(v,k) in barCodeList">
                <barcode :value="v.barcodes" :options="options" tag="img" ></barcode>
            </p>

        </div>
        <span slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="doPrint">打印</el-button>
                    <el-button @click="dialogVisible2 = false">取 消</el-button>
                </span>
    </el-dialog>
    </template>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
<script type="text/javascript" src="${baseStatic}vue/vue-barcode/vue-barcode.js"></script>
<script type="text/javascript" src="${baseStatic}vue/vue-resource.js"></script>
<script type="text/javascript" src="${baseStatic}js/moment.min.js"></script>
<%--<script type="text/javascript" src="${baseStatic}plugin/js/jquery-confirm.min.js"></script>--%>
<script>
    const vm = new Vue({

        el: '#warehousing-management-content',
        data: {
            //当前页
            pageNum:1,
            //每页数量
            pageSize:10,
            totalCount:0,
            dialogVisible1: false,
            dialogVisible2:false,
            firstTime: '',//开始日期
            secondTime: '',//结束日期
            likefirst:'', //车牌号/客户名称
            likesecond:'',//批次号/物料名称/入库单号
            fileText: '',
            file:'',//导入的文件
            options:{
                format: "CODE128",
                displayValue: true,
                fontSize: 24,
                width:1,
                height:100
            },
            barCodeList:[],
            batchList:[],
            deleteList:[],//删除的数据数组


        },
        mounted() {
            this.render();

        },
        methods: {
            // 显示总页数事件
            handleSizeChange(val) {
                var _this = this;
                console.log(val);
               _this.pageSize = val;
               _this.render()
            },
            //点击上一页事件
            prevClick(val) {
                console.log('上一页')
                if (this.pageNum < 1){
                    return
                }else {
                    this.pageNum--;
                    this.render()
                }
            },
            //点击下一页事件
            nextClick(val) {
                console.log('下一页');
                if (this.pageNum>Math.ceil(this.totalCount/this.pageSize)) {
                    return
                }else {
                    this.pageNum++;
                    //渲染数据到页面上
                 this.render()
                }
            },

            // 当前页改变时触发事件
            currentChange(val){
                console.log(val)
                this.pageNum = val;
                this.render()

            },
            //对话框关闭确认事件
            handleClose1(done) {
                done();

            },
            handleClose2(done) {
                done();
            },
            importInvoice: function () {
                var _this = this;
                _this.dialogVisible1 = true;

            },
            //查询功能
            search:function(){
                this.render();
            },
            render:function(){
                var _this = this ;
                var parmas = {
                    likefirst:_this.likefirst,
                    likesecond:_this.likesecond,
                    firstTime:_this.firstTime,
                    secondTime:_this.secondTime,
                    num: _this.pageNum,
                    size:_this.pageSize,
                }
                console.log(parmas)
                _this.$http.post(base_url+"/enterprise/depot/inboun/search", parmas, {emulateJSON: false})
                    .then(
                        (response)=>{
                            console.log(response);
                            if (response.body.success){
                                _this.batchList = response.body.page.collection
                                _this.totalCount = response.body.page.total
                            }
                        },
                        (error)=>{
                            console.log(error);
                        }
                    )
            },
            //选择文件
            files:function(event){
                this.fileText = $(event.target).val()
                 this.file = event.target.files[0]
                console.log(this.file)
            },
            //上传文件
            submit:function(){
             var _this =this;
                var  formData =new FormData();
                formData.append('file',_this.file);
                // formData.file = _this.file;
                console.log(formData)
                $.ajax({
                    url: base_url + "/enterprise/depot/inboun/import",
                    type: 'POST',
                    data: formData,
                    processData: false, // 告诉jQuery不要去处理发送的数据
                    contentType: false,// 告诉jQuery不要去设置Content-Type请求头
                    success: function (response) {
                        if (response.success) {
                            $.util.success(response.message, function () {
                                _this.dialogVisible1 = false
                                _this.render()
                            });
                        } else {
                            if (response.result && response.result.url) {
                                $.util.danger('有异常数据', response.message, function () {
                                    _this.dialogVisible1 = false
                                });
                            } else {
                                $.util.error(response.message,function () {
                                    _this.dialogVisible1 = false
                                });
                            }
                        }
                    }
                });


            },
            //删除数据
            deleteData:function(){
                 var _this = this ;
                 _this.deleteList = [];
                 var arr=[];
                var arr= $('[name=item]:checked')
                if (arr.length!=0) {
                    for (var  i=0;i<arr.length;i++) {
                        _this.deleteList.push($(arr[i]).data('inbounds'))
                    }
                }
                var parmas = {
                    inbounIds:JSON.stringify(_this.deleteList)
                }
                if (_this.deleteList.length>0)  {
                    $.util.confirm("删除","是否确认删除!", function () {
                        _this.$http.post(base_url+"/enterprise/depot/delete/inbound", parmas, {emulateJSON: false})
                            .then(
                                (response)=>{
                                    console.log(response);
                                    if (response.body.success){
                                        _this.render()
                                        _this.$message({ message: response.body.message, type: 'success' })
                                    }else {
                                        _this.$message({ message: response.body.message, type: 'error' })
                                    }
                                },
                                (error)=>{
                                    console.log(error);
                                }
                            )
                    })
                } else {
                    _this.$message({ message:'请至少选择一条入库单', type: 'error' })
                }


            },
            printing:function(){
                //每次一点击的时候就把数组清零
                var _this = this;
                this.barCodeList = []
                //点击打印按钮，获取所有被选中的批次号和每个批次号的数量
                var arr= $('[name=item]:checked')
                console.log(arr)
                if (arr.length!=0) {
                    for (var i = 0;i<arr.length;i++) {
                        var num = $(arr[i]).parent().siblings().find('.importNum').html()
                        console.log(num)
                        if (num) {
                            for (var j=0;j<num;j++) {
                                this.barCodeList.push({barcodes:$(arr[i]).data('key')})
                            }
                        }
                    }
                    console.log(this.barCodeList)
                } else {
                    this.barCodeList = []
                }

                   if (this.barCodeList ==false) {
                       _this.$message({
                           message: '请先选择订单',
                           type: 'warning'
                       })

                   }else {
                       _this.dialogVisible2 = true;
                   }



            },
            //全选反选
            allAndNotAll:function () {
                console.log($('#allAndNotAll')[0].checked)
               if($('#allAndNotAll')[0].checked) {
                   $("[name=item]:checkbox").prop("checked", true)

               }else {
                   $("[name=item]:checkbox").prop("checked", false)
               }

            },
            // 打印功能
            doPrint: function () {
                let subOutputRankPrint = document.getElementById('subOutputRank-print');// 获取打印区域
                let newContent = subOutputRankPrint.innerHTML;
                let oldContent = document.body.innerHTML;
                document.body.innerHTML = newContent;
                window.print();
                window.location.reload();
                document.body.innerHTML = oldContent;
                return false;
            }
        },
        components:{
            'barcode':VueBarcode
        }



    })
</script>
</html>