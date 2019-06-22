<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>物流跟踪-纸质回单-操作记录</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css"/>
    <link rel="stylesheet" href="${baseStatic}css/sendcustomer.css?times=${times}"/>
</head>
<body>
<div class="history-content clearfix">
    <form id="searchWaybillForm" action="${basePath}/backstage/receipt/queryOperationRecord/search" method="post">
        <input type="hidden" name="waybillid" value="${waybillid}">
        <input type="hidden" name="pageNum" value="${seach.pageNum}">
        <input type="hidden" name="pageSize" value="10">
        <div class="content-search">
            <div class="clearfix">
                <div class="fl col-min">
                    <label class="labe_l">回单类型:</label>
                    <select class="tex_t selec_t"  name="searchType" id="searchType">
                        <option value="1" <c:if test="${seach.searchType == 1}"> selected </c:if>>纸质回单</option>
                        <option value="2" <c:if test="${seach.searchType == 2}"> selected </c:if>>电子回单</option>
                    </select>
                </div>
                <div class="fl col-min selectList selectList_1">
                    <label class="labe_l">回单动作:</label>
                    <select class="tex_t selec_t"  name="receiptStatus">
                        <option value="-1" <c:if test="${seach.receiptStatus == -1}"> selected </c:if>>全部动作</option>
                        <option value="1" <c:if test="${seach.receiptStatus == 1}"> selected </c:if>>回单回收</option>
                        <option value="2" <c:if test="${seach.receiptStatus == 2}"> selected </c:if>>送交客户</option>
                        <option value="3" <c:if test="${seach.receiptStatus == 3}"> selected </c:if>>退供应商</option>
                        <option value="4" <c:if test="${seach.receiptStatus == 4}"> selected </c:if>>客户退回</option>
                    </select>
                </div>
                <div class="fl col-min selectList selectList_2 hide">
                    <label class="labe_l">回单动作:</label>
                    <select class="tex_t selec_t"  name="electronicStatus">
                        <option value="-1" <c:if test="${seach.electronicStatus == 1}"> selected </c:if>>全部动作</option>
                        <option value="1" <c:if test="${seach.electronicStatus == 1}"> selected </c:if>>回单上传</option>
                        <option value="2" <c:if test="${seach.electronicStatus == 2}"> selected </c:if>>回单审核</option>
                    </select>
                </div>
                <div class="fl col-min">
                    <label class="labe_l">操作日期:</label>
                    <div class="input-append date startDate" id="datetimeStart0">
                        <input type="text" class="tex_t z_index" id="firstTime" name="firstTime"
                               value="${seach.firstTime}" readonly>
                        <span class="add-on">
								<i class="icon-th"></i>
							</span>
                    </div>
                </div>
                <div class="fl col-min">
                    <label class="labe_l">至</label>
                    <div class="input-append date endDate" id="datetimeEnd0">
                        <input type="text" class="tex_t z_index" id="secondTime" name="secondTime"
                               value="${seach.secondTime}" readonly>
                        <span class="add-on">
								<i class="icon-th"></i>
							</span>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <div class="btnBox">
        <button type="button" class="fr btn btn-default content-search-btn" style="float:right;margin-top:-2px">查询</button>
    </div>
    <div class="table-style">
        <table class="dtable" id="trackTable">
            <thead>
            <tr>
                <th>回单类型</th>
                <th>操作人</th>
                <th>操作时间</th>
                <th>动作</th>
            </tr>
            </thead>
            <tbody id="datas">
            <c:forEach items="${page.collection}" var="obj">
                <tr>
                    <td>${obj.receiptType}</td>
                    <td>${obj.operationName}</td>
                    <td><fmt:formatDate value="${obj.operationTime}" pattern="yyyy-MM-dd"/></td>
                    <td>${obj.receiptAction}</td>
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
        $("#searchType").on("change",function(){
            var  _val = $(this).val();
            $(".selectList_"+_val).removeClass("hide").siblings(".selectList").addClass("hide");

        })


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

</script>
</html>