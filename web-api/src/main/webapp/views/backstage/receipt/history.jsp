<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>物流跟踪-纸质回单-历史查询</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css"/>
    <link rel="stylesheet" href="${baseStatic}css/history.css?times=${times}"/>
</head>
<body>
<div class="history-content clearfix">
    <form id="searchDailyForm" action="${basePath}/backstage/receipt/history" method="get">

        <input type="hidden" id="num" name="pageNum" value="${param.pageNum}">
        <input type="hidden" id="size" name="pageSize" value="10">
        <div class="content-search">
            <div class="clearfix">
                <div class="fl col-min">

                    <label class="labe_l">项目组:</label>
                    <select class="tex_t selec_t" name="groupId" id="groupid">
                        <option value="-1"
                                <c:if test="${param.groupId == -1}">selected</c:if> >全部
                        </option>
                        <option value="0" <c:if test="${param.groupId ==  0}">selected</c:if>>其他</option>
                        <c:forEach items="${groups}" var="group">
                            <c:if test="${param.groupId == group.id }">
                                <option value="${ group.id}" selected>${group.groupName }</option>
                            </c:if>
                            <c:if test="${param.groupId != group.id }">
                                <option value="${ group.id}">${group.groupName }</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                <div class="fl col-min">
                    <label class="labe_l">单号:</label>
                    <input type="text" class="tex_t" placeholder="任务单号/送货单号" name="orderNumber"
                           value="${param.orderNumber}">
                </div>
                <div class="fl col-min input-append date">
                    <label class="labe_l">操作日期:</label>
                    <div class="input-append date" id="datetimeStart">
                        <input type="text" class="tex_t z_index" name="createtimeStart" value="${param.createtimeStart}"
                               readonly>
                        <span class="add-on">
								<i class="icon-th"></i>
							</span>
                    </div>
                </div>
                <div class="fl col-min">
                    <label class="labe_l">至</label>
                    <div class="input-append date" id="datetimeEnd">
                        <input type="text" class="tex_t z_index" name="createtimeEnd" value="${param.createtimeEnd}"
                               readonly>
                        <span class="add-on">
								<i class="icon-th"></i>
							</span>
                    </div>
                </div>

            </div>
            <div class="clearfix">
                <div class="fl col-min">
                    <label class="labe_l">操作类型:</label>
                    <select class="tex_t selec_t" name="receiptStatus">

                        <c:if test="${param.receiptStatus == '1' }">
                            <option value="${param.receiptStatus}" selected>回单回收</option>
                        </c:if>

                        <c:if test="${param.receiptStatus == '2' }">
                            <option value="${param.receiptStatus}" selected>送交客户</option>
                        </c:if>

                        <c:if test="${param.receiptStatus == '3' }">
                            <option value="${param.receiptStatus}" selected>退供应商</option>
                        </c:if>

                        <c:if test="${param.receiptStatus == '4' }">
                            <option value="${param.receiptStatus}" selected>客户退回</option>
                        </c:if>
                        <option value="">全部</option>
                        <option value=1>回单回收</option>
                        <option value=2>送交客户</option>
                        <option value=3>退供应商</option>
                        <option value=4>客户退回</option>
                    </select>
                </div>
                <div>
                    <a href="javascript:;" class="content-search-btn search-btn" style="top:50px;">查询</a>
                    <a href="javascript:;" class="content-search-btn btn_dowload_recepit" style="margin-left: 5px;">导出回单照片</a>
                </div>
            </div>
        </div>
    </form>
    <div class="table-style">
        <table class="dtable" id="trackTable">
            <thead>
            <tr>
                <th><input type="checkbox" id="checkAll">全选</th>
                <th>任务单号</th>
                <th>送货单号</th>
                <th>操作日期</th>
                <th>操作类型</th>
                <th>操作人</th>
                <th>项目组名称</th>
                <th>客户名称</th>
                <th>收货地址</th>
                <th>收货人</th>
                <th>联系方式</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${data.collection}" var="info">
                <tr>
                    <td><input type="checkbox" name="checksingle" value="${info.id}"></td>
                    <td>${info.barcode}</td>
                    <td>${info.deliveryNumber}</td>

                    <td><fmt:formatDate value="${info.createtime}" pattern="yyyy-MM-dd"/></td>

                    <c:if test="${info.receiptStatus == 1}">
                        <td>回单回收</td>
                    </c:if>
                    <c:if test="${info.receiptStatus == 2}">
                        <td>送交客户</td>
                    </c:if>
                    <c:if test="${info.receiptStatus == 3}">
                        <td>退供应商</td>
                    </c:if>
                    <c:if test="${info.receiptStatus == 4}">
                        <td>客户退回</td>
                    </c:if>
                    <td>${info.uname}</td>
                    <td>${info.groupName}</td>
                    <td>${info.receiverName}</td>
                    <td>${info.address}</td>
                    <td>${info.contacts}</td>
                    <td>${info.contactNumber}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <c:if test="${data.collection != null && fn:length(data.collection) > 0}">
            <someprefix:page pageNum="${data.pageNum}" pageSize="${data.pageSize}" pages="${data.pages}"
                             total="${data.total}"/>
        </c:if>

    </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${baseStatic}js/datetimepicker.js"></script>
<script>
    $(document).ready(function () {
        //提交查询事件
        $(".search-btn").on("click", function () {
            $("form input[name='pageNo']").val(1);
            $("#searchDailyForm").submit();
        });

        //翻页事件
        $(".pagination a").each(function (i) {
            $(this).on('click', function () {
                $("form input[name='pageNum']").val($(this).attr("num"));
                $("#searchDailyForm").submit();
            });
        });
    });


    //导出回单
    $(".btn_dowload_recepit").on("click", function () {
        var parmas = {type: 1};
        $('#searchDailyForm').find('input, select').each(function (index, item) {
            parmas[item.name] = item.value;
        });

        if (parmas.createtimeStart == null || parmas.createtimeStart == "") {
            $.util.warning("请选择发货时间段！");
            return;
        }
        if (parmas.groupId == null || parmas.groupId == "") {
            $.util.warning("请选择项目组！");
            return;
        }
        parmas.groupid = parmas.groupId;
        parmas.deliverStartTime = parmas.createtimeStart;
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

</script>
</html>