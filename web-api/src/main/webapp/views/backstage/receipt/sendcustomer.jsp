<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>物流跟踪-纸质回单-送交客户</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css"/>
    <link rel="stylesheet" href="${baseStatic}css/sendcustomer.css?times=${times}"/>
</head>
<body>
<div class="history-content clearfix">
    <form id="searchWaybillForm" action="${basePath}/backstage/receipt/sendcustomer" method="post">
        <input type="hidden" name="pageNum" value="${search.pageNum}">
        <input type="hidden" name="pageSize" value="10">
        <div class="content-search">
            <div class="clearfix">
                <div class="fl col-min">
                    <label class="labe_l">项目组:</label>
                    <select class="tex_t selec_t" id="groupid" name="groupid">
                        <option value="">全部</option>
                        <c:forEach items="${groups}" var="group">
                            <c:if test="${search.groupid == group.id }">
                                <option value="${ group.id}" selected>${group.groupName }</option>
                            </c:if>
                            <c:if test="${search.groupid != group.id }">
                                <option value="${ group.id}">${group.groupName }</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                <div class="fl col-min">
                    <label class="labe_l">单号:</label>
                    <input type="text" id="waybillDeliveryNumber" name="waybillDeliveryNumber" class="tex_t"
                           value="${search.waybillDeliveryNumber}">
                </div>
                <div class="fl col-min">
                    <label class="labe_l">发货日期:</label>
                    <div class="input-append date startDate" id="datetimeStart0">
                        <input type="text" class="tex_t z_index" id="deliverStartTime" name="deliverStartTime"
                               value="${search.deliverStartTime}" readonly>
                        <span class="add-on">
								<i class="icon-th"></i>
							</span>
                    </div>
                </div>
                <div class="fl col-min">
                    <label class="labe_l">至</label>
                    <div class="input-append date endDate" id="datetimeEnd0">
                        <input type="text" class="tex_t z_index" id="deliverEndTime" name="deliverEndTime"
                               value="${search.deliverEndTime}" readonly>
                        <span class="add-on">
								<i class="icon-th"></i>
							</span>
                    </div>
                </div>
            </div>
            <div class="clearfix">
                <div class="fl col-min">
                    <label class="labe_l">送交日期:</label>
                    <div class="input-append date startDate" id="datetimeStart1">
                        <input type="text" class="tex_t z_index" id="sendStartTime" name="serviceStartTime"
                               value="${search.serviceStartTime}" readonly>
                        <span class="add-on">
								<i class="icon-th"></i>
							</span>
                    </div>
                </div>
                <div class="fl col-min">
                    <label class="labe_l">至</label>
                    <div class="input-append date endDate" id="datetimeEnd1">
                        <input type="text" class="tex_t z_index" id="sendEndTime" name="serviceEndTime"
                               value="${search.serviceEndTime}" readonly>
                        <span class="add-on">
								<i class="icon-th"></i>
							</span>
                    </div>
                </div>
                <div class="fl col-min">
                    <label class="labe_l">批次编号:</label>
                    <input type="text" class="tex_t" id="batchNumber" value="${search.batchNumber}" name="batchNumber">
                </div>
            </div>
        </div>
    </form>
    <div class="btnBox">
        <button type="button" class="btn btn-default returnBack">客户退回</button>
        <button type="button" class="btn btn-default receiptStorage">回单入库</button>
        <button type="button" class="fr btn btn-default btn_dowload_recepit" style="margin-left: 5px;">导出回单照片</button>
        <button type="button" class="fr btn btn-default btn_dowload_excel" style="margin-left: 5px;">导出</button>
        <button type="button" class="fr btn btn-default content-search-btn">查询</button>
    </div>
    <div class="table-style">
        <table class="dtable" id="trackTable">
            <thead>
            <tr>
                <th><input type="checkbox" id="checkAll">全选</th>
                <th>任务单号</th>
                <th>送货单号</th>
                <th>项目组名称</th>
                <th>发货日期</th>
                <th>送交日期</th>
                <th>回单状态</th>
                <th>批次编号</th>
                <th>客户名称</th>
                <th>收货地址</th>
                <th>收货人</th>
                <th>联系方式</th>
                <th>重量</th>
            </tr>
            </thead>
            <tbody id="datas">
            <c:forEach items="${results.collection}" var="waybill">
                <tr>
                    <td><input type="checkbox" name="checksingle" value="${waybill.id}"></td>
                    <td>${waybill.barcode}</td>
                    <td>${waybill.deliveryNumber}</td>
                    <td>${waybill.groupName}</td>
                    <td>
                        <fmt:formatDate value="${waybill.createtime}" pattern="yyyy-MM-dd"/>
                            <%--<someprefix:dateTime millis="${waybill.createtime}" pattern="yyyy-MM-dd"/>--%>
                    </td>
                    <td>
                        <fmt:formatDate value="${waybill.modifyTime}" pattern="yyyy-MM-dd"/>
                            <%--<someprefix:dateTime millis="${waybill.receiptStatus==1 ? waybill.modifyTime:null}"
                                                 pattern="yyyy-MM-dd"/>--%>
                            <%-- <fmt:formatDate value="${waybill.receiptStatus==1 ? waybill.modifyTime:null}" type="date" pattern="yyyy-MM-dd"></fmt:formatDate> --%>
                    </td>
                    <td>${waybill.receiptStatus==2 ? "已送客户" : "-"}</td>
                    <td>${waybill.batchNumber}</td>
                    <td>${waybill.companyName}</td>
                    <td>${waybill.address}</td>
                    <td>${waybill.contacts}</td>
                    <td>${waybill.contactNumber}</td>
                    <td>${waybill.weight}</td>
                </tr>
            </c:forEach>

            <%-- <c:forEach items="${page.list}" var="waybill">
                <tr>
                    <td><input type="checkbox" name="checksingle" value="${waybill.id}"></td>

                </tr>
            </c:forEach> --%>
            </tbody>
        </table>
        <c:if test="${results.collection != null && fn:length(results.collection) > 0}">
            <someprefix:page pageNum="${results.pageNum}" pageSize="${results.pageSize}" pages="${results.pages}"
                             total="${results.total}"/>
        </c:if>
    </div>
</div>
<div class="inventory" style="display: none">
    <div class="model-base-box edit_customer" style="width: 470px;">
        <input name="id" type="hidden">
        <div class="model-form-field">
            <label for="">送货单号 :</label>
            <input name="deliveryNumber" type="text" min="0" max="50">
        </div>
    </div>
</div>
<input type="hidden" class="startTxt" readonly/>
<input type="hidden" class="endTxt" readonly/>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${baseStatic}js/datetimepicker1.js"></script>
<script type="text/javascript" src="${baseStatic}js/Date.js?time=${times}"></script>
<script>
    $(document).ready(function () {
        //全选事件
        //init();
        $('#checkAll').on("click", function () {
            $("input[name='checksingle']").prop('checked', this.checked);
        });
        //单选事件
        $("input[name='checksingle']").on("click", function () {
            /*获取当前选中的个数，判断是否跟全部复选框的个数是否相等*/
            var all = $("input[name='checksingle']").length;
            var selects = $("input[name='checksingle']:checked").length;
            $('#checkAll').prop('checked', (all - selects == 0));
        });
        //回单入库
        $(".receiptStorage").on("click", function () {
            $.util.form('回单回收', $(".inventory").html(), function () {
                var editBody = this.$body;
            }, function () {
                var editBody = this.$body;
                var deliveryNumber = editBody.find("input[name='deliveryNumber']").val();
                if (null == deliveryNumber || "" == deliveryNumber) {
                    $.util.warning("请输入需要操作的送货单号");
                } else {
                    receiptStatus = 1;
                    var paramData = {"receiptStatus": receiptStatus, "deliveryNumber": deliveryNumber};
                    $.util.json(base_url + "/backstage/receipt/receiptStorage", paramData, function (data) {

                        if (data.flag == 1) {
                            $.util.warning(" 当前选中任务单没有可回收的任务单");
                        } else if (data.flag == 2) {
                            $.util.warning(" 当前选中任务单存在当前状态不可回收的任务单，部分已提交");
                        } else {
                            if (data.success) {
                                $.util.alert('操作提示', data.message, function () {
                                    $("#searchWaybillForm").submit();
                                });
                            } else {
                                $.util.error(data.message);
                            }
                        }
                    });
                }
            });
        });

        //导出回单
        $(".btn_dowload_recepit").on("click", function () {
            var parmas = {type: 1};
            $('#searchWaybillForm').find('input, select').each(function (index, item) {
                parmas[item.name] = item.value;
            });
            if (parmas.deliverStartTime == null || parmas.deliverStartTime == "") {
                $.util.warning("请选择发货时间段！");
                return;
            }
            if (parmas.groupid == null || parmas.groupid == "") {
                $.util.warning("请选择项目组！");
                return;
            }
            $.util.json(base_url + "/backstage/receipt/downloadReceipt", parmas, function (data) {
                if (data.success) {//处理返回结果
                    var msg = "文件大小 : <font color='red'>" + data.size + "</font>MB";
                    $.util.alert("下载回单文件", msg, function () {
                        $.util.download(data.url);
                    });
                } else {
                    $.util.error(data.message);
                }
            });

        });

        //客户退回
        $(".returnBack").on("click", function () {
            var barcodes = $("input[name='checksingle']:checked");
            if (!barcodes || barcodes.length <= 0) {
                $.util.warning("选择一个要退回的客户");
            } else {
                var waybills = new Array()
                $.each(barcodes, function (idx, value) {
                    waybills.push($(value).val());
                });
                var paramData = {"pageNum": 1, "waybills": JSON.stringify(waybills), "receiptStatus": 4};
                var url = base_url + "/backstage/receipt/modifyReceiptStatus";
                $.util.json(url, paramData, function (data) {
                    if (1 == data.falg) {
                        $.util.warning(" 当前选中任务单存在当前状态不可修改的任务单，部分已提交");
                    }
                    $.util.success("操作成功", function () {
                        location.href = "${basePath}/backstage/receipt/sendcustomer";
                    }, 2000)
                });
            }
        });

        //查询按钮事件
        $(".content-search-btn").on("click", function () {
            $("#searchWaybillForm").submit();
            // init();
        });
    });

    //翻页事件
    $(".pagination a").each(function (i) {
        $(this).on('click', function () {
            $("form input[name='pageNum']").val($(this).attr("num"));
            $("#searchWaybillForm").submit();
        });
    });

    function init() {
        var paramData = {"pageNum": 1, "pageSize": 15, "receiptStatus": 2};
        var waybillDeliveryNumber = $("#waybillDeliveryNumber").val();
        var deliverStartTime = $("#deliverStartTime").val();
        var deliverEndTime = $("#deliverEndTime").val();
        var sendStartTime = $("#sendStartTime").val();
        var sendEndTime = $("#sendEndTime").val();
        var batchNumber = $("#batchNumber").val();
        var groupId = $(".selec_t").val();
        if (waybillDeliveryNumber) {
            paramData["waybillDeliveryNumber"] = waybillDeliveryNumber;
        }
        if (deliverStartTime) {
            paramData["deliverStartTime"] = deliverStartTime;
        }
        if (deliverEndTime) {
            paramData["deliverEndTime"] = deliverEndTime;
        }
        if (sendStartTime) {
            paramData["sendStartTime"] = sendStartTime;
        }
        if (sendEndTime) {
            paramData["sendEndTime"] = sendEndTime;
        }
        if (batchNumber) {
            paramData["batchNumber"] = batchNumber;
        }
        if (groupId) {
            paramData["groupid"] = groupId;
        }
        var url = base_url + "/backstage/receipt/queryReceipt";
        $.util.json(url, paramData, function (data) {
            var con = "";
            if (data.groupList.length) {
                var selects = $(".selec_t");
                if (!selects.empty()) {
                    selects.remove();
                }
                selects.append('<option value="">全部</option>');
                $.each(data.groupList, function (index, value) {
                    if (value.id == groupId) {
                        selects.append('<option  value="' + value.id + '" selected="true" >' + value.groupName + '</option>');
                    } else {
                        selects.append('<option  value="' + value.id + '">' + value.groupName + '</option>');
                    }
                });
                if (data.results.list) {
                    $.each(data.results.list, function (index, value) {
                        con += "<tr>" +
                            "<td><input type='checkbox' name='checksingle' value='" + value.id + "'></td>" +
                            "<td>" + value.wbarcode + "</td>" +
                            "<td>" + (value.deliveryNumber == null ? "-" : value.deliveryNumber) + "</td>" +
                            "<td>" + value.groupName + "</td>" +
                            "<td>" + timeStamp2String(value.createtime) + "</td>" +
                            "<td>" + timeStamp2String(value.modifyTime) + "</td>" +
                            "<td>" + (value.receiptStatus == 2 ? "已送客户" : "-") + "</td>" +
                            "<td>" + (value.batchNumber == null ? "-" : value.batchNumber) + "</td>" +
                            "<td>" + (value.companyName == null ? "-" : value.companyName) + "</td>" +
                            "<td>" + (value.fulladdress == null ? "-" : value.fulladdress) + "</td>" +
                            "<td>" + (value.contacts == null ? "-" : value.contacts) + "</td>" +
                            "<td>" + (value.contactNumber == null ? "-" : value.contactNumber) + "</td>" +
                            "<td>" + (value.weight == null ? "-" : value.weight) + "</td>" +
                            "</tr>";

                    });
                    $("#datas").html(con);
                }
                /* else{
                                     $("#datas").html("<div class=''>暂无数据。。。</div>");
                                } */
                $("#waybillDeliveryNumber").val(waybillDeliveryNumber);
                $("#deliverStartTime").val(deliverStartTime);
                $("#deliverEndTime").val(deliverEndTime);
                $("#sendStartTime").val(sendStartTime);
                $("#sendEndTime").val(sendEndTime);
                $("#batchNumber").val(batchNumber);
            }
        });
    }

    //下载送交客户事件
    $(".btn_dowload_excel").on("click", function () {
        var parmas = {type: 2, receiptStatus: 2};


        $('#searchWaybillForm').find('input, select').each(function (index, item) {
            parmas[item.name] = item.value;
        });
        if (!(parmas.deliverStartTime && parmas.deliverEndTime) && !(parmas.batchNumber && parmas.batchNumber)) {
            $.util.warning("请选择发货时间段或者填写批次号！");
        } else {
            //查询条件
            $.util.json(base_url + '/backstage/receipt/papery/export', parmas, function (data) {
                if (data.success) {//处理返回结果
                    var msg = "文件数量 : <font color='red'>" + data.count + "</font>个,大小 : <font color='red'>" + data.size + "</font>MB";
                    $.util.alert("下载送交客户文件", msg, function () {
                        $.util.download(data.url);
                    });
                } else {
                    $.util.error(data.message);
                }
            });
        }
    });
</script>
</html>