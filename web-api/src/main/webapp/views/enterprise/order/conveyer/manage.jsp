<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-运输管理-承运管理</title>
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
            text-align: left;
            padding-left: 20px;
            font-weight: normal;
        }

        .jconfirm .jconfirm-box div.jconfirm-content-pane.no-scroll {
            overflow: hidden;
        }

        .jconfirm .jconfirm-box div.jconfirm-content-pane.no-scroll .jconfirm-content {
            overflow: hidden;
        }

        .radio-wrapper {
            font-size: 0;
        }

        .radio-item {
            position: relative;
            width: 30%;
            display: inline-block;
            vertical-align: top;
            font-size: 14px;
        }

        .radio-item + .radio-item {
            margin-left: 5%;
        }

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

        .radio-item > input[type="radio"] {
            position: absolute;
            top: 1px;
            left: -9999px;
        }

        .radio-item.active .text {
            color: #fff;
            background: #31acfa;
        }

        .col-min-new {
            width: 15%;
        }

        .col-min-new:nth-of-type(1) {
            width: 24%;
        }

        .col-min-new:nth-last-of-type(1) {
            width: 6%;
        }

        .col-min-new > .tex_t {
            width: 100%;
        }

        .content-search .content-search-btn {
            margin-left: 0;
        }

        .normalSts, .abnormalSts {
            display: inline-block;
            padding: 2px 6px;
        }

        .normalSts {
            background: #99cc00;
        }

        .abnormalSts {
            background: #ff9900;
        }

        .el-dialog .el-dialog__body input {
            width: 100%;
            height: 40px;
            border-radius: 6px;
            padding-left: 8px;
        }

        .el-dialog__body {
            padding: 30px 10px;
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
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
        }

        .el-dialog {
            background-color: #f9f9f9;
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
    <form id="shipper-manage-form" action="${basePath}/enterprise/order/covey/manage/search" method="get">
        <div class="content-search">
            <div class="clearfix">
                <div class="fl col-min-new radio-wrapper">
                    <div class="radio-item active">
                        <label class="text" for="scan1">全部</label>
                        <input type="radio" class="receiptFettle_search" name="fettle" id="scan1" checked="checked"
                               value="">
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
                    <select name="timeType" class="tex_t selectCondition" id="timeType">
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
                <button class="layui-btn layui-btn-normal" v-on:click="entryorder">订单录入</button>
                <button class="layui-btn layui-btn-normal" v-on:click="imports">订单导入</button>
                <button class="layui-btn layui-btn-normal" v-on:click="shareOrder">订单分享</button>
                <button class="layui-btn layui-btn-normal" v-on:click="toShareManage">我的分享</button>
                <button class="layui-btn layui-btn-normal" v-on:click="deletes">删除</button>
            </div>
            <table>
                <thead>
                <tr>
                    <th width="10">
                        <input type="checkbox" id="checkAll" v-on:click="selectAll"/>
                    </th>
                    <th style="width: 28px;">系统单号</th>
                    <th style="width: 28px;">送货单号</th>
                    <%--<th width="20">订单编号</th>--%>
                    <th width="20">发货日期</th>
                    <th width="25">发货方</th>
                    <th width="25">收货客户</th>
                    <th width="25">收货人</th>
                    <th width="25">联系方式</th>
                    <th width="50">收货地址</th>
                    <th width="15">订单状态</th>
                    <th width="50">当前位置</th>
                    <th width="25">回单</th>
                    <th width="25">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr v-if="collection.length <= 0">
                    <td colspan="13" style="text-align: center;">暂无数据</td>
                </tr>
                <tr v-for="o in collection" v-if="collection.length > 0">
                    <td><input type="checkbox" name="orderId" v-bind:value="o.id" v-bind:deliveryNo="o.deliveryNo"/>
                    </td>
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
                    <td>{{o.receive.companyName}}</td>
                    <td>{{o.receiverName}}</td>
                    <td>{{o.receiverContact}}</td>
                    <td>{{o.receiveAddress}}</td>
                    <td class="check-fettle" v-bind:data="o.fettle" v-bind:status="o.receiptFettle">
                        <span v-if="o.fettle == 4">已到货</span>
                        <span v-else>运输中</span>
                    </td>
                    <td>
                        <span v-if="o.location != null">{{o.location}}</span>
                        <span v-else style="color: #adadad;">未定位</span>
                    </td>
                    <td>
                        <span v-if="o.isReceipt > 0">已上传</span>
                        <span v-else style="color: #adadad;">未上传</span>
                    </td>
                    <td>
                        <a class="linkBtn" @click="look(o.id)">查看</a>&nbsp;&nbsp;
                        <a class="linkBtn" v-if="o.receiptFettle == 0 && o.fettle < 4 && o.insertType == 3"
                           @click="edit(o.id)">编辑</a>
                    </td>
                </tr>
                </tbody>
            </table>
            <%--编辑对话框--%>
            <el-dialog
                    title="送货单号"
                    :visible.sync="dialogVisible"
                    width="30%"
                    :before-close="handleClose">
                <input type="text" v-model="deliveryNo" :orderKey='orderKey'>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="dialogVisible = false">取 消</el-button>
                    <el-button type="primary" @click="submitDeliveryNo">确 定</el-button>
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
<!-- 上传导入 -->
<div class="import_task_panel" style="display: none">
    <div class="model-base-box import_task" style="width: 480px;overflow: hidden;">
        <form id="importForm" enctype="multipart/form-data" method="post">
            <div class="model-form-field file_box">
                <label>选择模板:</label>
                <select name="">
                    <option selected></option>
                </select>
            </div>
            <div class="model-form-field file_box">
                <label>选择文件:</label>
                <input type="text" name="fileName" id="fileName" readonly/>
                <span class="span-txt">请选择文件</span>
                <a href="javascript:;" class="file">
                    <input name="file" type="file" accept=".xls,.xlsx" id="uploadFile"/>
                </a>
            </div>
        </form>
    </div>
</div>

</body>
<%@ include file="/views/include/floor.jsp" %>
<script src="${baseStatic}js/component/select.customer.js?times=${times}"></script>
<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
<script>
    //给回单状态添加默认值
    $("select[name='receiptFettle']").val('');
    //签署样式添加
    $('.radio-item').click(function () {
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

        function importByExcel(element) {
            var panel = $.util.panel('导入发货单', $(".import_task_panel").html());
            panel.initialize = function (model) {
                var $editBody = this.$body;
                $editBody.find("input:file").on('change', function () {
                    var fileName = this.value;
                    //校验文件格式
                    var fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if (fileType != "xlsx" && fileType != "xls") {
                        $.util.error("选择的文件格式不支持！");
                        return false;
                    }
                    $(this).parent().parent().find("input[name='fileName']").val(fileName);
                })
                $.util.json(base_url + "/enterprise/template/list", {category: 1}, function (data) {
                    if (data.success) {
                        var $select = $editBody.find("select");
                        $select.html('');
                        $.each(data.templates, function (idx, obj) {
                            if (obj.fettle == 2) {
                                $select.append("<option value='" + obj.key + "' selected>" + obj.name + "</option>");
                            } else {
                                $select.append("<option value='" + obj.key + "'>" + obj.name + "</option>");
                            }
                        });
                    } else {
                        $.util.error(data.message);
                    }
                })
            };
            panel.addButton({
                text: '模板下载', alignment: 'left', action: function () {
                    var key = this.$body.find("select").val();
                    if (key) {
                        $.util.json(base_url + "/enterprise/template/download/" + key, null, function (data) {
                            if (data.success) {
                                $.util.download(data.url);
                            } else {
                                $.util.error(data.message);
                            }
                        })
                    } else {
                        $.util.warning("请先选择一个模板");
                    }
                    return false;
                }
            });
            panel.addButton(function () {
                var editBody = this.$body, formData = new FormData();
                var templateKey = editBody.find("select").val();
                if (!templateKey) {
                    return $.util.error("请选择导入需要使用的模板");
                }
                formData.append("templateKey", templateKey);
                var file = editBody.find("input:file");
                if (!file.val()) {
                    return $.util.error("请选择需要导入的excel文件！");
                }
                formData.append("id",527116460156928);
                formData.append("file", file[0].files[0]);
                $.ajax({
                    url: base_url + "/contract/import/excel",
                    type: 'POST',
                    data: formData,
                    processData: false, // 告诉jQuery不要去处理发送的数据
                    contentType: false,// 告诉jQuery不要去设置Content-Type请求头
                    success: function (response) {
                        if (response.success) {
                            $.util.success(response.message, function () {
                                setTimeout(function () {
                                    searchManage(1);
                                }, 1000);
                            });
                        } else {
                            if (response.result && response.result.url) {
                                $.util.danger('有异常数据', response.message, function () {
                                    $.util.download(response.result.url);
                                    $("select[name='receiptFettle']").val(0);
                                    setTimeout(function () {
                                        searchManage(1);
                                    }, 1000);
                                });
                            } else {
                                $.util.error(response.message);
                            }
                        }
                    }
                });
            });
            panel.open();
        }

        function deletes(element) {
            var chk_values = [];
            var flag_1 = true, flag_2 = true;
            var delivery_msg = "送货单号： ";
            var arrive_msg = "送货单号： ";
            $.each($("input[name='orderId']:checked"), function (idx, value) {
                chk_values.push($(this).val());
                var fettle = $(value).parent().siblings(".check-fettle");
                console.log($(this).attr("deliveryNo"));
                if (fettle.attr("data") == 2 || fettle.attr("data") == 4) {
                    arrive_msg = arrive_msg + $(this).attr("deliveryNo") + ",";
                    flag_1 = false;
                }
                if (fettle.attr("status") != 0) {     //0:未生成电子回单
                    delivery_msg = delivery_msg + $(this).attr("deliveryNo") + ",";
                    flag_2 = false;
                }
            });
            if (!flag_1) {
                $.util.error(arrive_msg + "已到货的订单,不可以删除");
                return false;
            }
            if (!flag_2) {
                $.util.error(delivery_msg + "已生成电子回单，不可以删除");
                return false;
            }
            if (chk_values == null || chk_values.length <= 0) {
                $.util.error("请至少选择一条数据");
                return false;
            }
            var parmas = {"ids": JSON.stringify(chk_values)};
            console.log(parmas);
            $.util.confirm("提示", "确定要删除吗?", function () {
                $.util.json(base_url + '/enterprise/order/conveyer/manage/delete', parmas, function (data) {
                    if (data.success) {//处理返回结果
                        $.util.success(data.message, function () {
                            setTimeout(function () {
                                $(":checkbox").attr("checked", false);
                                searchManage(1);
                            }, 1000);
                        });
                    } else {
                        $.util.error(data.message);
                    }
                });
            })

        }

        function downloadReceipt(element) {
            var chk_values = [], parmas = {};
            $('input[name="orderId"]:checked').each(function () {
                chk_values.push($(this).val());
            });
            if (chk_values == null || chk_values.length <= 0) {
                $.util.error("请至少选择一条数据");
                return false;
            }
            parmas.orderIds = chk_values.join(',');
            $.util.json(base_url + '/enterprise/order/down/order', parmas, function (data) {
                if (data.success) {//处理返回结果
                    var msg = "文件大小 : <font color='red'>" + data.size + "</font>MB";
                    $.util.alert("下载回单文件", msg, function () {
                        $.util.download(data.url);
                    });
                } else {
                    $.util.error(data.message);
                }
            });
        }

        var listManage = new Vue({
            el: '#list-manage',
            data: {
                orderKey: '',
                deliveryNo: '',
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
                isLastPage: false
            },
            methods: {
                //对话框确认关闭事件
                handleClose: function (done) {
                    // this.$confirm('确认关闭？')
                    //     .then(_ => {
                    //         done();
                    //     })
                    //     .catch(_ => {});
                },
                //送货单号编辑
                editDeliveryNo: function (deliveryNo, orderKey) {
                    this.dialogVisible = true;
                    this.deliveryNo = deliveryNo;
                    this.orderKey = orderKey
                },
                //提交修改后的单号
                submitDeliveryNo: function () {
                    var _this = this
                    var parmas = {
                        orderKey: _this.orderKey,
                        deliveryNo: _this.deliveryNo
                    }
                    $.util.json(base_url + '/enterprise/order/order/update/deliveryNo', parmas, function (data) {
                        console.log(data)
                        if (data.success) {//处理返回结果
                            _this.dialogVisible = false;
                            _this.$message({
                                message: data.message,
                                type: 'success',
                                duration: '500'
                            });
                            searchManage(1)

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
                entryorder: function () {
                    layer.full(layer.open({
                        type: 2,
                        title: '订单录入',
                        content: base_url + "/enterprise/order/conveyer/entrance"
                    }));
                    //window.location.href = base_url + "/enterprise/order/manage/entrance";
                },
                imports: function (event) {
                    importByExcel(event);
                },
                look: function (id) {//查看
                    layer.full(layer.open({
                        type: 2,
                        title: '查看订单详情',
                        content: base_url + "/enterprise/order/manage/detail?orderKey=" + id
                    }));
                    //window.location.href = base_url + "/enterprise/order/manage/detail?orderKey="+ id;
                },
                edit: function (id) {//编辑
                    var _this = this;
                    layer.full(layer.open({
                        type: 2,
                        title: '编辑订单信息',
                        content: base_url + "/enterprise/order/manage/edit?type=3&orderKey=" + id,
                        end: function () {
                            searchManage();
                        }
                    }));
                    //window.location.href = base_url + "/enterprise/order/manage/edit?orderKey="+ id;
                    /*
                    $.util.form('回单详情', $(".view_more_panel").html(), function (model) {
                        var $panel = this;
                        requestData(id, function (result) {
                            createVue($panel.$body.find('.model-base-box')[0], result);
                        });
                    })
                    */
                },
                deletes: function (event) {
                    deletes(event);
                },
                shareOrder: function () {
                    var orderKeys = [], parmas = {};
                    $('input[name="orderId"]:checked').each(function () {
                        orderKeys.push($(this).val());
                    });
                    if (orderKeys == null || orderKeys.length <= 0) {
                        $.util.error("请至少选择一条要分享的订单信息");
                        return false;
                    }
                    parmas['orderKeys'] = orderKeys.join(',');
                    var panel = $('document').selectCompanyCustomer({
                        title: '订单分享',
                        parameters: {userHost: true, reg: 1}
                    }, function (items, keys) {
                        var keys = items.map(function (item) {
                            return item.companyKey;
                        });
                        if (keys == null || keys.length <= 0) {
                            $.util.error("请至少选择一项要分享的客户信息");
                            return false;
                        }
                        parmas['customerKeys'] = keys.join(',');
                        $.util.json(base_url + "/enterprise/order/share/order", parmas, function (data) {
                            if (data.success) {//处理返回结果
                                $.util.success(data.message, function () {
                                    panel.close();
                                }, 3000);
                            } else {
                                $.util.error(data.message);
                            }
                        });
                    });
                },
                toShareManage: function () {
                    window.location.href = base_url + "/enterprise/order/share/conveyer";
                }
            }
        });
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
        $('.searchChange').on('input', function () {
            console.log(2222)
            $(".selectCondition").val('-1')
        })
        //提交事件
        $(".search-btn").click(function () {
            searchManage(1);
        });

        //单选框查询
        $(".receiptFettle_search").click(function () {
            searchManage(1);
        });

        searchManage(1);
    });
</script>
</html>