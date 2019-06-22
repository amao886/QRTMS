<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-运输管理-收货管理</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/receiptmanage.css?times=${times}"/>
    <link rel="stylesheet" href="${baseStatic}element-ui/lib/theme-chalk/index.css"/>
    <style type="text/css">
        .cactive {
            color: #fff !important;
            cursor: default;
            background-color: #337ab7 !important;
            border-color: #337ab7 !important;
        }

        .pagination a {
            cursor: pointer;
        }

        .model-base-box {
            padding-bottom: 20px;
        }

        .span-radio-checkbox {
            text-align:left;
            padding-left:20px;
            font-weight:normal;
        }
        .jconfirm .jconfirm-box div.jconfirm-content-pane.no-scroll {
            overflow: hidden;
        }
        .jconfirm .jconfirm-box div.jconfirm-content-pane.no-scroll .jconfirm-content{
            overflow: hidden;
        }
        .radio-wrapper{font-size:0;}
        .radio-item{position: relative;width:30%;display: inline-block;vertical-align: top;font-size: 14px;}
        .radio-item + .radio-item{margin-left:5%;}
        .radio-item .text {
            display: inline-block;
            padding: 4px 0px;
            width: 100%;
            font-weight: 400;
            background: #e6e6e6;
            color: #636363;
            text-align: center;
            cursor: pointer;
        }
        .radio-item>input[type="radio"] {
            position: absolute;
            top: 1px;
            left: -9999px;
        }
        .radio-item.active .text{color: #fff;background: #31acfa;}
        .col-min-new{width:15%;}
        .col-min-new:nth-of-type(1){width:24%;}
        .col-min-new:nth-last-of-type(1){width:6%;}
        .col-min-new>.tex_t{width:100%;}
        .content-search .content-search-btn{margin-left:0;}
        .normalSts,.abnormalSts{display: inline-block;padding:2px 6px;}
        .normalSts{background:#99cc00;}
        .abnormalSts{background:#ff9900;}
        .el-dialog .el-dialog__body input {
            width: 100%;
            height: 40px;
            border-radius: 6px;
            padding-left: 8px;
        }
        .el-dialog__body {
            padding: 50px 10px;
            min-height: 200px;
        }
        .edit {
            color: #31ACFA;
            float: right;
            margin-right: 5px;
            width: 20%;
        }
        .deliveryNo {
            width: 70%;
            float: left;
            margin-left: 5px;
            overflow:hidden;
            white-space:nowrap;
            text-overflow:ellipsis;
        }
        .el-dialog {
            background-color: #f9f9f9;
        }
        .img {
            width: 25px;
            height: 25px;
            display: inline-block;
            cursor: pointer;
        }
        .fl {
            float: left;
            margin-left: 30px;
        }
        .fr {
            float: right;
            margin-right: 30px;
        }
        .img img{
            width: 100%;
            height: 100%;
        }
        .el-select,.el-textarea {
            width: 80%;
        }
        .el-dialog__header {
            border-bottom: 1px solid #ccc;
        }
        .body-info {
            margin-top: 50px;
        }
        .el-textarea textarea{
            /*resize: none;*/
            min-height: 150px !important;
        }
    </style>
</head>
<body>
<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
<!--[if lt IE 9]>
<script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
<script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<div class="track-content">
    <form id="shipper-manage-form" action="${basePath}/enterprise/order/receive/manage/search" method="get">
        <div class="content-search">
            <div class="clearfix">
                <div class="fl col-min-new radio-wrapper">
                    <div class="radio-item active">
                        <label class="text" for="scan1">全部</label>
                        <input type="radio" class="receiptFettle_search" name="fettle" id="scan1" checked="checked" value="">
                    </div>
                    <div class="radio-item">
                        <label class="text" for="scan2">运输中</label>
                        <input type="radio" class="receiptFettle_search" name="fettle" id="scan2" value="3">
                    </div>
                    <div class="radio-item">
                        <label class="text" for="scan3">已到货</label>
                        <input type="radio" class="receiptFettle_search" name="fettle" id="scan3" value="4">
                    </div>
                </div>
                <div class="fl col-min-new">
                    <select name="timeType" class="tex_t selectCondition" id="timeType" >
                        <option value="3">近三天发货任务</option>
                        <option value="7">近一周发货任务</option>
                        <option value="30">近一个月发货任务</option>
                        <option value="-1">全部日期</option>
                    </select>
                </div>
                <div class="fl col-min-new">
                    <input type="text" class="tex_t searchChange" name="likeString" placeholder="系统单号/送货单号/收货客户"
                           value="${search.likeString}"/>
                </div>
                <div class="fl col-min-new">
                    <a href="javascript:;" class="content-search-btn search-btn">查询</a>
                </div>
            </div>
        </div>
    </form>

    <!-- 表格部分 -->
    <div class="table-style" id="list-manage">
        <template>
            <div class="handle-box">
                <%--<button class="layui-btn layui-btn-normal" v-on:click="arrivel">确认到货</button>--%>
                <button class="layui-btn layui-btn-normal" v-on:click="intact" v-show="isShow!=4">完好收货</button>
                <button class="layui-btn layui-btn-normal" v-on:click="abnormalReceive" v-show="isShow!=4">异常收货</button>
                <button class="layui-btn layui-btn-normal" v-on:click="complaint" >投诉</button>
            </div>
            <table style="table-layout: auto;">
                <thead>
                <tr>
                    <th width="10">
                        <input type="checkbox" id="checkAll" v-on:click="selectAll"/>
                    </th>
                    <th width="40">系统单号</th>
                    <th width="35">送货单号</th>
                    <%--<th width="20">订单编号</th>--%>
                    <th width="20">发货日期</th>

                    <th width="25">发货方</th>
                    <th width="25">物流商</th>
                    <th width="70">收货地址</th>
                    <th width="40">订单状态</th>
                    <th width="40">收货结果</th>
                    <th width="70">当前位置</th>
                    <th width="40">评价</th>
                    <th width="25">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr v-if="collection.length <= 0">
                    <td colspan="13" style="text-align: center;">暂无数据</td>
                </tr>
                <tr v-for="o in collection" v-if="collection.length > 0">
                    <td><input type="checkbox" name="orderId" v-bind:data="o.fettle" v-bind:value="o.id" :isComplaint="o.isComplaint"/></td>
                    <td>
                        <span v-if="o.bindCode != null">{{o.bindCode}}</span>
                        <span v-else style="color: #adadad;">未绑码</span>
                    </td>
                    <td class="clearfix">
                        {{o.deliveryNo}}
                        <%--<span class="deliveryNo">{{o.deliveryNo}}</span>--%>
                        <%--<a href="javascript:;" @click="editDeliveryNo(o.deliveryNo,o.id)" class="edit"> 编辑</a>--%>
                    </td>
                    <%--<td>{{o.orderNo}}</td>--%>
                    <td>{{o.deliveryTime | dateFormat}}</td>
                    <td>{{o.shipper.companyName}}</td>
                    <td>{{o.convey.companyName}}</td>
                    <td>{{o.receiveAddress}}</td>
                    <td>
                        <span v-if="o.fettle == 3">已到货</span>
                        <span v-if="o.fettle == 4">已签收</span>
                        <span v-else>运输中</span>
                    </td>
                    <td>
                        <span v-if="o.signFettle==0">未签收</span>
                        <span v-if="o.signFettle==1">完好收货</span>
                        <span v-if="o.signFettle==2">异常收货</span>
                    </td>
                    <td>
                        <span v-if="o.location != null">{{o.location}}</span>
                        <span v-else style="color: #adadad;">未定位</span>
                    </td>
                    <td class="clearfix">
                        <span @click.once="evaluate(o.id,1,$event)" class="img fl good" v-show="o.evaluation==null"><img src="../../../../static/images/good.png" /></span>
                        <span  class="img fl goodAfter" v-show="o.evaluation==1"><img src="../../../../static/images/goodAfter.png"/></span>
                        <span @click.once="evaluate(o.id,0,$event)" class="img fr bad" v-show="o.evaluation==null"><img src="../../../../static/images/bad.png"/></span>
                        <span  class="img fr badAfter" v-show="o.evaluation==0"><img src="../../../../static/images/badAtfer.png"/></span>
                    </td>
                    <td>
                        <a class="linkBtn" @click="look(o.id)">查 看</a>&nbsp;&nbsp;
                        <a   class="linkBtn" @click="complaint(o.id,1)" v-if="o.isComplaint==false" >投诉</a>&nbsp;&nbsp;
                        <%--<a  v-if="o.fettle !=2 && o.fettle!=4 " class="linkBtn" @click="edit(o.id,3)">确认到货</a>&nbsp;&nbsp;--%>
                    </td>
                </tr>
                </tbody>
            </table>
            <%--修改单号对话框--%>
            <el-dialog
                    title="送货单号"
                    :visible.sync="dialogVisible"
                    width="30%"
                    :before-close="handleClose">
                <input type="text" v-model="deliveryNo" :orderKey= 'orderKey'>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="dialogVisible = false">取 消</el-button>
                    <el-button type="primary" @click="submitDeliveryNo">确 定</el-button>
              </span>
            </el-dialog>
            <!-- 投诉 -->
            <el-dialog :visible.sync="dialog.complaint" width="35%" title="投诉">
                <div class="body-info">
                    <el-input type="textarea" v-model="complaintContent" placeholder="请输入投诉内容"></el-input>
                </div>
                <span slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="complaintSub">确 定</el-button>
                    <el-button @click="dialog.complaint=false">取 消</el-button>

                </span>
            </el-dialog>
            <!-- 异常收货 -->
            <el-dialog :visible.sync="dialog.abnormalReceiving" width="35%" title="异常收获">
                <div class="body-info">
                    <el-form ref="form" :model="abnormal" label-width="80px">
                        <el-form-item label="异常类型">
                            <el-select v-model="abnormal.abnormalType">
                                <el-option label="货物破损" value="2"></el-option>
                                <el-option label="货物数量不对" value="1"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="异常描述">
                            <el-input type="textarea" v-model="abnormal.abnormalContent" placeholder="请输入异常情况描述"></el-input>
                        </el-form-item>

                    </el-form>
                </div>
                <span slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="abnormalSub">确 定</el-button>
                    <el-button @click="dialog.abnormalReceiving=false">取 消</el-button>

                </span>
            </el-dialog>
            <div class="page-item">
                <div id="layui_div_page" class="col-sm-12 center-block" v-if="pages > 1">
                    <ul class="pagination" style="margin-bottom: 0;">
                        <li class="disabled"><span><span aria-hidden="true">共{{total}}条,每页{{pageSize}}条</span></span>
                        </li>
                        <li v-if="!isFirstPage"><a v-on:click="flip(1)">首页</a></li>
                        <li v-if="!isFirstPage"><a v-on:click="flip(prePage)">上一页</a></li>
                        <li v-for="p in navigatepageNums">
                            <span v-if="p == pageNum" class="cactive">{{p}}</span>
                            <a v-if="p != pageNum" v-on:click="flip(p)">{{p}}</a>
                        </li>
                        <li v-if="!isLastPage"><a v-on:click="flip(nextPage)">下一页</a></li>
                        <li v-if="!isLastPage"><a v-on:click="flip(pages)">尾页</a></li>
                        <li class="disabled"><span><span>共{{pages}}页</span></span></li>
                    </ul>
                </div>
            </div>
        </template>
    </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
<script>
    //给回单状态添加默认值
    $("select[name='receiptFettle']").val('');
    //签署样式添加
    $('.radio-item').click(function(){
        $(this).addClass('active').siblings().removeClass('active');
    })

    var pageNum = 1;
    $(document).ready(function () {
        function checkNumber(value) {
            if (value === "" || value == null || isNaN(value)) {
                return 0;
            }
            return value;
        }
        var listManage = new Vue({
            el: '#list-manage',
            data: {
                orderKey:'',
                deliveryNo:'',
                dialogVisible: false,
                collection: [],
                pageNum: 0,
                pageSize: 0,
                navigatepageNums: [],
                total: 0,
                pages: 0,
                prePage: 0,
                nextPage: 0,
                isFirstPage: false,
                isLastPage: false,
                dialog: {
                    complaint: false,//投诉
                    abnormalReceiving: false,//异常收货
                },
                complaintContent:'',//投诉内容

                abnormal:{
                    abnormalContent:'',//异常内容
                    abnormalType:'',//异常类型
                },
                hasArrive:false,//是否存在已到货数据 false 没有 true 有
                hasComplaint:false,//是否存在已投诉数据 false 没有 true 有
                isShow:'',
                orders :[],//单号数组
            },
            methods: {

                //对话框确认关闭事件
                handleClose: function(done) {
                    // this.$confirm('确认关闭？')
                    //     .then(_ => {
                    //         done();
                    //     })
                    //     .catch(_ => {});
                },
                //评价
                evaluate:function(orderKey,evaluation,event){
                    var _this = this;
                    var parmas = {
                        orderKey: orderKey,
                        evaluation: evaluation
                    }

                    $.util.json(base_url + '/enterprise/order/evaluation', parmas, function (data) {
                        console.log(data)
                        if (data.success) {//处理返回结果
                            $(event.target).parent().hide().next().show().siblings().hide()

                        } else {
                            $.util.error(data.message);
                        }
                    })
                },
                //送货单号编辑
                editDeliveryNo:function(deliveryNo,orderKey){
                    this.dialogVisible =true;
                    this.deliveryNo = deliveryNo;
                    this.orderKey = orderKey
                },
                //提交修改后的单号
                submitDeliveryNo:function(){
                    var _this = this
                    var parmas = {
                        orderKey:_this.orderKey,
                        deliveryNo:_this.deliveryNo
                    }
                    $.util.json(base_url + '/enterprise/order/order/update/deliveryNo', parmas, function (data) {
                        console.log(data)
                        if (data.success) {//处理返回结果
                            _this.dialogVisible = false;
                            _this.$message({
                                message: data.message,
                                type: 'success',
                                duration:'500'
                            });
                            searchManage(1)

                        } else {
                            $.util.error(data.message);
                        }
                    })
                },
                //完好收货
                intact: function () {
                    var _this = this;
                    _this.orders = [];
                    batchArrivel(0);
                    if (_this.orders == null || _this.orders.length <= 0) {
                        $.util.error("请至少选择一条数据");
                        return false;
                    }else if(!(_this.hasArrive)){
                        var param = {"orderIds": JSON.stringify(_this.orders)};
                        $.util.confirm("确认到货","是否确认到货!", function () {
                            $.util.json(base_url + '/enterprise/order/arrivel', param, function (data) {
                                if (data.success) {//处理返回结果
                                    _this.$message({ message: '收获完成,感谢您的使用', type: 'success' })
                                    searchManage(1);//成功后刷新列表
                                } else {
                                    $.util.error(data.message);
                                }
                            });
                        })
                    }


                },
                //投诉
                complaint: function (id,sigle) {
                    var _this = this;
                    _this.orders=[];
                    if (sigle==1) {//成立说明是单个投诉
                        _this.dialog.complaint = true;
                        _this.orders = [];
                        _this.orders.push(id)
                    } else {//批量投诉
                        batchArrivel(1);

                        if (_this.orders == null || _this.orders.length <= 0) {
                            $.util.error("请至少选择一条数据");
                            return false;
                        } else if(!(_this.hasComplaint)){
                            console.log(_this.orders)
                            _this.dialog.complaint = true;
                        }
                    }
                },
                //投诉提交
                complaintSub: function () {
                    var _this = this ;
                    var parmas = {
                        orderKeys:JSON.stringify(_this.orders),
                        content:_this.complaintContent,
                    }
                    console.log(parmas)
                    $.util.json(base_url + '/enterprise/complaint/entry/complaint', parmas, function (data) {
                        console.log(data)
                        if (data.success) {//处理返回结果
                            _this.$message({ message: data.message, type: 'success' })
                            _this.dialog.complaint = false;
                            searchManage(1);
                        } else {
                            $.util.error(data.message);
                        }
                    })
                },
                //异常收货
                abnormalReceive:function(){
                    var _this = this ;
                    _this.orders = [];
                    batchArrivel(0);
                    if (_this.orders == null || _this.orders.length <= 0) {
                        $.util.error("请至少选择一条数据");
                        return false;
                    } else if(!(_this.hasArrive)){
                        _this.dialog.abnormalReceiving = true;
                    }

                },
                //异常收货提交
                abnormalSub:function(){
                    var _this = this ;
                    var parmas = {
                        orderKeys:JSON.stringify(_this.orders),
                        content:_this.abnormal.abnormalContent,
                        type:_this.abnormal.abnormalType,
                    }
                    console.log(parmas)
                    $.util.json(base_url + '/enterprise/order/modify/exception', parmas, function (data) {
                        console.log(data)
                        if (data.success) {//处理返回结果
                            _this.$message({ message: data.message, type: 'success' })
                            _this.dialog.abnormalReceiving = false;
                            searchManage(1);
                        } else {
                            $.util.error(data.message);
                        }
                    })
                },
                flip: function (num) {
                    searchManage(num);
                },//翻页
                selectAll: function (event) {
                    $("input[name='orderId']").prop('checked', event.target.checked);
                },
                look: function (id) {//查看
                    layer.full(layer.open({
                        type: 2,
                        title:'查看订单详情',
                        content: base_url + "/enterprise/order/manage/detail?orderKey="+ id
                    }));
                    //window.location.href = base_url + "/enterprise/order/manage/detail?orderKey="+ id;
                },
                edit: function (id) {//编辑
                    var param = {"orderIds": JSON.stringify([id])};
                    $.util.confirm("确认到货","是否确认到货!", function () {
                        $.util.json(base_url + '/enterprise/order/arrivel', param, function (data) {
                            if (data.success) {//处理返回结果
                                searchManage(1);//成功后刷新列表
                            } else {
                                $.util.error(data.message);
                            }
                        });
                    })
                },
                // arrivel:function (event) {
                //     batchArrivel(event);
                // }
            }
        });
        function batchArrivel(type) {
            var param = {}, orders = [];
            var num = 0;
            var sum = 0;
            $('input[name="orderId"]:checked').each(function () {
                var status = $(this).attr("data");
                var complaint = $(this).attr("iscomplaint");
                if (complaint){
                    sum++
                }
                if(status == 2 || status == 4){
                    num ++;
                }
                orders.push($(this).val());
                listManage.orders = orders;
            });
            if(type==1){
                if (sum>0) {
                    console.log(sum)
                    listManage.hasComplaint = true;
                    $.util.error("存在已投诉的数据");
                }
            }
            if(type!=1){//已到货的时候是可以投诉的
                if(num > 0){
                    listManage.hasArrive = true;
                    $.util.error("存在已经到货的数据");

                    return false;
                }
            }


            // var param = {"orderIds": JSON.stringify(orders)};
            // $.util.confirm("确认到货","是否确认到货!", function () {
            //     $.util.json(base_url + '/enterprise/order/arrivel', param, function (data) {
            //         if (data.success) {//处理返回结果
            //             searchManage(1);//成功后刷新列表
            //         } else {
            //             $.util.error(data.message);
            //         }
            //     });
            // })

        }
        var searchManage = function (num) {
            if (!num) {
                num = pageNum;
            } else {
                pageNum = num;
            }

            var parmas = {num: num, size: 10, matchMode: 1}, $form = $('#shipper-manage-form');
            $form.find('input, select').each(function (index, item) {
                parmas[item.name] = item.value;
            });
            parmas.fettle = $("input[name='fettle']:checked").val()
            $.util.json($form.attr('action'), parmas, function (data) {
                console.log(data)
                if (data.success) {//处理返回结果
                    listManage.collection = data.page.collection;
                    listManage.pageNum = data.page.pageNum;
                    listManage.pageSize = data.page.pageSize;
                    listManage.navigatepageNums = data.page.navigatepageNums;
                    listManage.total = data.page.total;
                    listManage.pages = data.page.pages;
                    listManage.prePage = data.page.prePage;
                    listManage.nextPage = data.page.nextPage;
                    listManage.isFirstPage = data.page.isFirstPage;
                    listManage.isLastPage = data.page.isLastPage;

                } else {
                    $.util.error(data.message);
                }
            });
        };

        //change事件
        $('.searchChange').on('input',function(){

            $(".selectCondition").val('-1')
        })
        //提交事件
        $(".search-btn").click(function () {
            searchManage(1);
        });

        //单选框查询
        $(".receiptFettle_search").click(function () {
            // console.log($(this).val())
            listManage.isShow = $(this).val()
            searchManage(1);
        });


        document.onkeydown = function (e) {
            if (!e) e = window.event;
            if ((e.keyCode || e.which) === 13) {
                searchManage(1);
            }
        }

        searchManage(1);
    });
</script>
</html>