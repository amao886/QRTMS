<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>物流跟踪-纸质回单-回单列表</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css"/>
    <link rel="stylesheet" href="${baseStatic}css/inventory.css?times=${times}"/>
</head>
<body>
<div class="history-content clearfix">
    <form id="searchWaybillForm" action="${basePath}/backstage/receipt/queryReceiptList" method="post">
        <input type="hidden" name="pageNum" value="${search.pageNum}">
        <input type="hidden" name="pageSize" value="10">
        <div class="content-search">
            <div class="clearfix">
                <div class="fl col-min">
                    <label class="labe_l">项目组:</label>
                    <select class="tex_t selec_t" name="groupId" id="groupid">
                        <option value="-1">全部</option>
                        <c:forEach items="${groups}" var="group">
                            <c:if test="${search.groupId == group.id }">
                                <option value="${ group.id}" selected>${group.groupName}</option>
                            </c:if>
                            <c:if test="${search.groupId != group.id }">
                                <option value="${group.id}">${group.groupName }</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                <div class="fl col-min">
                    <label class="labe_l">搜索条件:</label>
                    <input type="text" class="tex_t" name="likeString"
                           value="${search.likeString }" placeholder="任务单号/送货单号">
                </div>
                <div class="fl col-min">
                    <label class="labe_l">发货日期:</label>
                    <div class="input-append date startDate" id="deliverStartTime">
                        <input type="text" class="tex_t z_index" name="firstTime"
                               value="${search.firstTime }" readonly>
                        <span class="add-on">
								<i class="icon-th"></i>
							</span>
                    </div>
                </div>
                <div class="fl col-min">
                    <label class="labe_l">至</label>
                    <div class="input-append date endDate" id="secondTime">
                        <input type="text" class="tex_t z_index" name="secondTime" value="${search.secondTime }"
                               readonly>
                        <span class="add-on">
								<i class="icon-th"></i>
							</span>
                    </div>
                </div>
            </div>
            <div class="clearfix">
                <div class="fl col-min">
                    <label class="labe_l" style="width: 88px;">纸质回单状态:</label>
                    <select class="tex_t selec_t" name="paperyReceiptStatus" id="receiptStatus" style="left: 96px">
                        <option value="">全部</option>
                        <option value="0" <c:if test="${search.paperyReceiptStatus == '0'}"> selected</c:if>>未回收
                        </option>
                        <option value="1" <c:if test="${search.paperyReceiptStatus == 1}"> selected</c:if>>已回收</option>
                        <option value="4" <c:if test="${search.paperyReceiptStatus == 4}"> selected</c:if>>客户已退回
                        </option>
                        <option value="2" <c:if test="${search.paperyReceiptStatus == 2}"> selected</c:if>>已送交客户
                        </option>
                        <option value="3" <c:if test="${search.paperyReceiptStatus == 3}"> selected</c:if>>已退供应商
                        </option>
                    </select>
                </div>
                <div class="fl col-min">
                    <label class="labe_l" style="width: 88px;">电子回单状态:</label>
                    <select class="tex_t selec_t" name="waitFettle" id="receiptVerifyStatus" style="left: 96px">
                        <option value="">全部</option>
                        <option value="1" <c:if test="${search.waitFettle == 1}"> selected</c:if>>未上传</option>
                        <option value="2" <c:if test="${search.waitFettle == 2}"> selected</c:if>>已上传-全部未审核</option>
                        <option value="3" <c:if test="${search.waitFettle == 3}"> selected</c:if>>已上传-部分未审核</option>
                        <option value="4" <c:if test="${search.waitFettle == 4}"> selected</c:if>>已审核-有不合格</option>
                    </select>
                </div>
            </div>
        </div>
    </form>
    <div class="btnBox">
        <button type="button" class="btn btn-default receiptStorage">回单回收</button>
        <button type="button" class="btn btn-default sendToCustomer" onclick="updateSeceiptStatus(2)">送交客户</button>
        <button type="button" class="btn btn-default BackToShipper" onclick="updateSeceiptStatus(3)">退承运商</button>
        <button type="button" class="btn btn-default BackToShipper" onclick="updateSeceiptStatus(4)">客户退回</button>
        <button type="button" class="fr btn btn-default btn_dowload_excel" style="margin-left: 5px;">导出</button>
        <button type="button" class="fr btn btn-default btn_dowload_recepit" style="margin-left: 5px;">导出电子回单</button>
        <button type="button" class="fr btn btn-default content-search-btn">查询</button>
    </div>
    <div class="table-style">
        <table class="dtable" id="trackTable">
            <thead>
            <tr>
                <th><input type="checkbox" id="checkAll">全选</th>
                <th>项目组名称</th>
                <th>任务单号</th>
                <th>送货单号</th>
                <th>客户名称</th>
                <th>收货地址</th>
                <th>收货人</th>
                <th>联系方式</th>
                <th>发货日期</th>
                <th>纸质回单状态</th>
                <th>电子回单状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.collection}" var="waybill">
                <tr>
                    <td><input type="checkbox" name="checksingle" value="${waybill.id}"></td>
                    <td>${waybill.groupName}</td>
                    <td>${waybill.barcode}</td>
                    <td>${waybill.deliveryNumber}</td>
                    <td>${waybill.receiverName}</td>
                    <td>${waybill.receiveAddress}</td>
                    <td>${waybill.contactName}</td>
                    <td>${waybill.contactPhone}</td>
                    <td>
                        <fmt:formatDate value="${waybill.deliveryTime}" pattern="yyyy-MM-dd"/>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${waybill.paperyReceiptStatus == 0}">
                                未回收
                            </c:when>
                            <c:when test="${waybill.paperyReceiptStatus == 1}">
                                已回收
                            </c:when>
                            <c:when test="${waybill.paperyReceiptStatus == 2}">
                                已送客户
                            </c:when>
                            <c:when test="${waybill.paperyReceiptStatus == 3}">
                                已退供应商
                            </c:when>
                            <c:otherwise>
                                客户退回
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <c:choose>
                        <c:when test="${waybill.receiptStatusName != '未上传'}">
                            <td><a style="font-size:12px" class="btn btn-link" href="${basePath}/backstage/receipt/search?waybill.id=${waybill.id}">${waybill.receiptStatusName}</a></td>
                        </c:when>
                        <c:otherwise>
                            <td>${waybill.receiptStatusName}</td>
                        </c:otherwise>
                    </c:choose>
                    <td><a style="font-size:12px" class="btn btn-link" href="${basePath}/backstage/receipt/queryOperationRecord/${waybill.id}">操作记录</a></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <c:if test="${page.collection != null && fn:length(page.collection) > 0}">
            <someprefix:page pageNum="${page.pageNum}" pageSize="${page.pageSize}" pages="${page.pages}"
                             total="${page.total}"/>
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
<script>
    var receiptStatus = 0;
    function updateSeceiptStatus(receiptStatus) {
        var waybills = new Array()
        var selects = $("input[name='checksingle']:checked");
        if (!selects || selects.length <= 0) {
            $.util.warning("选择一个要送交的客户");
        } else {
            $.each(selects, function (idx, value) {
                waybills.push($(value).val());
            });
        }
        var paramData = {"pageNum": 1, "waybills": JSON.stringify(waybills), "receiptStatus": receiptStatus};
        var url = base_url + "/backstage/receipt/modifyReceiptStatus";
        $.util.json(url, paramData, function (data) {
            if (data.falg > 0) {
                $.util.success("操作成功", function () {
                    $("#searchWaybillForm").submit();
                }, 2000)
                ;
            }else{
                $.util.warning(" 当前选中任务单无可修改的状态");
            }
        });
    }

    $(document).ready(function () {
        //全选事件
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

        //导出回单
        $(".btn_dowload_recepit").on("click", function () {
            var parmas = {};
            $('#searchWaybillForm').find('input, select').each(function (index, item) {
                parmas[item.name] = item.value;
            });
            if (parmas.firstTime == null || parmas.secondTime == "") {
                $.util.warning("请选择发货时间段！");
                return;
            }
            if (parmas.groupId == null || parmas.groupId == "" || parmas.groupId == -1) {
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

        //查询按钮事件
        $(".content-search-btn").on("click", function () {
            $("#searchWaybillForm").submit();
        });
        //翻页事件
        $(".pagination a").each(function (i) {
            $(this).on('click', function () {
                $("form input[name='pageNum']").val($(this).attr("num"));
                $("#searchWaybillForm").submit();
            });
        });
    });

    //下载库存事件
    $(".btn_dowload_excel").on("click", function () {
        var parmas = {type: 1};
        $('#searchWaybillForm').find('input, select').each(function (index, item) {
            parmas[item.name] = item.value;
        });
        parmas.deliverStartTime = parmas.firstTime;
        parmas.deliverEndTime = parmas.secondTime
        console.log(parmas)
        if (parmas.deliverStartTime == "" || parmas.deliverEndTime == "") {
            $.util.warning("请选择发货时间段");
        } else {
            //查询条件
            $.util.json(base_url + '/backstage/receipt/papery/export', parmas, function (data) {
                if (data.success) {//处理返回结果
                    var msg = "文件数量 : <font color='red'>" + data.count + "</font>个,大小 : <font color='red'>" + data.size + "</font>MB";
                    $.util.alert("下载库存回单文件", msg, function () {
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