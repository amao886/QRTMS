<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>物流跟踪-纸质回单-统计分析</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/analyze.css?times=${times}"/>
</head>
<body>
<div class="analyze-content">

    <form id="searchWaybillForm" action="" method="post">
        <input type="hidden" name="pageNum" value="${page.pageNum }">
        <input type="hidden" name="pageSize" value="${page.pageSize }">
        <div class="content-search">
            <div class="clearfix">
                <div class="fl col-middle">
                    <label class="labe_l">项目组:</label>
                    <select class="tex_t selec_t" name="groupid">
                        <option value="-1" <c:if test="${mergeReceiptStatistics.groupid ==null || mergeReceiptStatistics.groupid == -1}">selected</c:if>>全部</option>
                        <option value="0" <c:if test="${mergeReceiptStatistics.groupid ==0}">selected</c:if>>其他</option>
                        <c:forEach items="${groups}" var="group">
                            <c:if test="${mergeReceiptStatistics.groupid == group.id }">
                                <option value="${ group.id}" selected>${group.groupName }</option>
                            </c:if>
                            <c:if test="${mergeReceiptStatistics.groupid != group.id }">
                                <option value="${ group.id}">${group.groupName }</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                <div class="fl col-middle">
                    <label class="labe_l">状态:</label>
                    <select class="tex_t selec_t" name="selectYear">
                        <option value="-1">全部</option>

                        <c:forEach items="${year}" var="gs">
                            <c:if test="${mergeReceiptStatistics.selectYear == gs}">
                                <option value="${mergeReceiptStatistics.selectYear}"
                                        selected>${mergeReceiptStatistics.selectYear}</option>
                            </c:if>

                            <c:if test="${mergeReceiptStatistics.selectYear != gs}">
                                <option value="${gs}">${gs}</option>
                            </c:if>
                        </c:forEach>

                    </select>
                </div>
                <div class="fl col-middle">
                    <label class="labe_l">月份:</label>
                    <select class="tex_t selec_t" name="selectMonth">
                        <option value="-1">全部</option>
                        <c:forEach items="${group}" var="g">
                            <c:if test="${mergeReceiptStatistics.selectMonth == g}">
                                <option value="${mergeReceiptStatistics.selectMonth}"
                                        selected>${mergeReceiptStatistics.selectMonth}</option>
                            </c:if>

                            <c:if test="${mergeReceiptStatistics.selectMonth != g}">
                                <option value="${g}">${g}</option>
                            </c:if>

                        </c:forEach>


                    </select>

                </div>
                <div>
                    <a href="javascript:;" class="content-search-btn" style="top:50px;">查询</a>
                </div>
            </div>

        </div>
    </form>
    <div class="table-style">
        <table class="dtable" id="trackTable">
            <thead>
            <tr>
                <th rowspan="2">项目名称</th>
                <th rowspan="2">总数</th>
                <th colspan="6">待收</th>
                <th colspan="6">在库</th>
                <th colspan="2">已交</th>
            </tr>
            <tr>
                <th colspan="2">未回收</th>
                <th colspan="2">退供应商</th>
                <th colspan="2">合计</th>
                <th colspan="2">回收回单</th>
                <th colspan="2">客户退回</th>
                <th colspan="2">合计</th>
                <th colspan="2">送交客户</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.collection}" var="waybill">
                <tr>
                    <!-- td style="word-break:break-all"></td> -->
                    <td style="word-break:break-all">${waybill.groupName}</td>

                    <td><c:if test="${waybill.countGroupid > 0 }">
                        ${waybill.countGroupid}
                    </c:if></td>

                    <td><c:if test="${waybill.unRecycleCount > 0 }">
                        ${waybill.unRecycleCount}
                    </c:if></td>

                    <td>${waybill.unRecyclePercent}</td>

                    <td><c:if test="${waybill.retiredSupplierCount > 0 }">
                        ${waybill.retiredSupplierCount}
                    </c:if></td>

                    <td>${waybill.retiredSupplierPercent}</td>

                    <td><c:if test="${waybill.dueIn > 0 }">
                        ${waybill.dueIn}
                    </c:if></td>

                    <td>${waybill.dueInPercent}</td>

                    <td><c:if test="${waybill.recycleCount > 0 }">
                        ${waybill.recycleCount}
                    </c:if></td>

                    <td>${waybill.recycleCountPercent}</td>

                    <td><c:if test="${waybill.retiredClientsCount > 0 }">
                        ${waybill.retiredClientsCount}
                    </c:if></td>

                    <td>${waybill.retiredClientsPercent}</td>

                    <td><c:if test="${waybill.store > 0 }">
                        ${waybill.store}
                    </c:if></td>

                    <td>${waybill.storePercent}</td>

                    <td><c:if test="${waybill.endCustomercount > 0 }">
                        ${waybill.endCustomercount}
                    </c:if></td>

                    <td>${waybill.endCustomerPercent}</td>
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
</body>
<%@ include file="/views/include/floor.jsp" %>
<script>
    $(document).ready(function () {
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

</script>
</html>