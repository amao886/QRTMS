<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-用户行为</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/userbehavior.css?times=${times}"/>
    <link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css"/>
</head>
<body>
<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
<!--[if lt IE 9]>
<script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
<script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<div class="track-content">
    <!-- 搜索条件部分 -->
    <div class="check-wrapper">
        <form id="searchWaybillForm" action="${basePath}/admin/business/queryUserbehavior" method="post">
            <input type="hidden" name="num" value="${search.num }">
            <input type="hidden" name="size" value="${search.size }">
            <input type="hidden" name="falg" value="${search.falg}">
            <div class="selectCondition">
                <ul class="tabSelect">
                    <li>
                        <label class="transport-status <c:if test="${search.falg == 999}">active</c:if>"
                               onclick="tabsubmit()"><input
                                type="radio"/>全部</label>
                    </li>
                    <li>
                        <label class="transport-status <c:if test="${search.falg == 1}">active</c:if>"
                               onclick="tabsubmit(1)"><input
                                type="radio"/>昨天</label>
                    </li>
                    <li>
                        <label class="transport-status <c:if test="${search.falg == 2}">active</c:if>"
                               onclick="tabsubmit(2)"><input
                                type="radio"/>最近7天</label>
                    </li>
                    <li>
                        <label class="transport-status <c:if test="${search.falg == 3}">active</c:if>"
                               onclick="tabsubmit(3)"><input
                                type="radio"/>最近30天</label>
                    </li>
                </ul>
                <span class="more" id="moreCon"><span>更多搜索条件</span><i class="icon"></i></span>
            </div>
            <div class="moreCondition content-search" style="display:none;">
                <div class="clearfix">
                    <div class="fl col-middle">
                        <label class="labe_l">起止日期</label>
                        <div class="input-append date startDate" id="datetimeStart">
                            <input type="text" class="z_index" id="deliverStartTime" name="startTime"
                                   value="${search.startTime}" readonly>
                            <span class="add-on">
								<i class="icon-th"></i>
							</span>
                        </div>
                    </div>
                    <div class="fl col-middle">
                        <label class="labe_l">至</label>
                        <div class="input-append date endDate" id="datetimeEnd">
                            <input type="text" class="z_index" id="deliverEndTime" name="endTime"
                                   value="${search.endTime}" readonly>
                            <span class="add-on">
								<i class="icon-th"></i>
							</span>
                        </div>
                    </div>
                    <div class="fl col-middle">
                        <label class="labe_l">选择平台</label>
                        <div class="input-append">
                            <select class="selec_t" name="appType" id="appType">
                                <option value="">全部</option>
                                <option value="api" <c:if test="${search.appType == 'api'}">selected</c:if>>微信端</option>
                                <option value="backstage" <c:if test="${search.appType == 'backstage'}">selected</c:if>>
                                    web端
                                </option>
                            </select>
                        </div>
                    </div>
                    <div class="fl col-middle">
                        <label class="labe_l">项目组:</label>
                        <div class="input-append">
                            <select class="selec_t" name="groupId" id="selectGroup">
                                <option value="">请选择项目组</option>
                                <c:forEach items="${groups}" var="group">
                                    <c:if test="${search.groupId == group.id }">
                                        <option value="${group.id}" selected>${group.groupName }</option>
                                    </c:if>
                                    <c:if test="${search.groupId != group.id }">
                                        <option value="${ group.id}">${group.groupName }</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="clearfix">
                    <div class="fl col-middle">
                        <label class="labe_l">搜索条件</label>
                        <div class="input-append">
                            <input type="text" class="sub-tag" name="likeString" placeholder="用户ID/操作对象/动作"
                                   value="${search.likeString}"/>
                        </div>
                    </div>
                    <div class="fl col-middle">
                        <button class="layui-btn layui-btn-normal submitBtn submit" type="button">查询</button>
                    </div>
                </div>
            </div>
        </form>
    </div>
    <!-- 表格部分 -->
    <div class="table-style">
        <table>
            <thead>
            <tr>
                <th>用户ID</th>
                <th>平台</th>
                <th>操作模块</th>
                <th>动作</th>
                <th>日期</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="obj" items="${page.collection}">
                <tr>
                    <td>${obj.mobilephone}</td>
                    <c:if test="${obj.appType == 'backstage'}">
                        <td>web端</td>
                    </c:if>
                    <c:if test="${obj.appType == 'api'}">
                        <td>微信端</td>
                    </c:if>
                    <td>${obj.subordinateModule}</td>
                    <td>${obj.functionPoint}</td>
                    <td><fmt:formatDate value="${obj.dateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="page-item">
            <c:if test="${page.collection != null && fn:length(page.collection) > 0}">
                <someprefix:page pageNum="${page.pageNum}" pageSize="${page.pageSize}" pages="${page.pages}"
                                 total="${page.total}"/>
            </c:if>
        </div>
    </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script src="${baseStatic}plugin/js/jquery.mloading.js"></script>
<script src="${baseStatic}plugin/js/jquery.cookie.js"></script>
<script type="text/javascript" src="${baseStatic}js/datetimepicker.js"></script>
<script>


    //切换窗口提交
    function tabsubmit(org) {
        $("form input[name='falg']").val(org);
        $(":input[name='startTime']").val("");
        $(":input[name='endTime']").val("");
        $("form input[name='num']").val(1);
        $("#searchWaybillForm").submit();
    }

    $(document).ready(function () {
        //翻页事件
        $(".pagination a").each(function (i) {
            $(this).on('click', function () {
                $("form input[name='num']").val($(this).attr("num"));
                $("#searchWaybillForm").submit();
            });
        });

        $(".submit").click(function () {
            $("form input[name='falg']").val('');
            $("#searchWaybillForm").submit();
        });

        //tab切换
        $('.transport-status').click(function () {
            $('.transport-status').removeClass('active');
            $(this).addClass('active');
            //$("#searchWaybillForm").submit();
        })

        //判断更多搜索条件是否展示
        if ($.cookie('open') == 1) {
            $('.moreCondition').show();
            $('#moreCon').addClass('open-hook').children('span').text('收起更多').next().addClass('up');
        }

        //更多搜索条件显示
        $('#moreCon').on('click', function () {
            if ($(this).hasClass('open-hook')) {
                $('.moreCondition').hide();
                $(this).removeClass('open-hook').children('span').text('更多搜索条件').next().removeClass('up');
                $.cookie('open', '2');
            } else {
                $('.moreCondition').show();
                $(this).addClass('open-hook').children('span').text('收起更多').next().addClass('up');
                $.cookie('open', '1');
            }
        })
    });
</script>
</html>