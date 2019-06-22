<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <title>后台管理系统-后台首页</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/page.css?times=${times}"/>
    <link rel="stylesheet" href="${baseStatic}css/customInfo.css?times=${times}"/>
</head>
<body>
<div class="custom-content clearfix">
    <form id="searchBehaviorForm" action="${basePath}/admin/business/loadBehaviorTotal" method="post">
        <input type="hidden" name="num" value="${search.num }">
        <input type="hidden" name="size" value="${search.size }">
        <input type="hidden" id="device" name="appType" value="">
        <div class="tabBtnCon">
            <span class="tabBtn  <c:if test="${search.appType == ''}">curr</c:if> " data-val="">全部</span>
            <span class="tabBtn  <c:if test="${search.appType == 'backstage'}">curr</c:if> "
                  data-val="backstage">计算机</span>
            <span class="tabBtn <c:if test="${search.appType == 'api'}">curr</c:if> " data-val="api">移动设备</span>

        </div>
    </form>
    <div class="table-style">
        <table>
            <thead>
            <tr>
                <th rowspan="2" colspan="2" scope="col">功能点</th>
                <th colspan="7" scope="col">点击次数</th>
            </tr>
            <tr>
                <c:forEach items="${dates}" var="date" varStatus="st">
                    <c:choose>
                        <c:when test="${st.index == 0}">
                            <th>今天</th>
                        </c:when>
                        <c:when test="${st.index == 1}">
                            <th>昨天</th>
                        </c:when>
                        <c:otherwise>
                            <th><fmt:formatDate value="${date }" pattern="yyyy-MM-dd"/></th>
                        </c:otherwise>
                    </c:choose>

                </c:forEach>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="obj" items="${page.collection}" varStatus="st">
                <tr>
                    <td>${st.index + 1}</td>
                    <td>${obj.functionPoint}</td>
                    <c:forEach var="c" items="${obj.groupCount}">
                        <td>${c.count}</td>
                    </c:forEach>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <c:if test="${page.collection != null && fn:length(page.collection) > 0}">
        <someprefix:page pageNum="${page.pageNum}" pageSize="${page.pageSize}" pages="${page.pages}"
                         total="${page.total}"/>
    </c:if>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script src="${baseStatic}plugin/js/cityselect/city.js"></script>
<script src="${baseStatic}plugin/js/cityselect/select.js?time=${times}"></script>
<script>
    $(document).ready(function () {
        //tab切换
        $(".tabBtn").on("click", function () {
            var _val = $(this).attr("data-val");
            $(this).addClass("curr").siblings().removeClass("curr")
            $("#device").val(_val);
            $("#searchBehaviorForm").submit();
        });

        //翻页事件
        $(".pagination a").each(function (i) {
            $(this).on('click', function () {
                $("form input[name='num']").val($(this).attr("num"));
                $("#searchBehaviorForm").submit();
            });
        });


    });


</script>

</html>