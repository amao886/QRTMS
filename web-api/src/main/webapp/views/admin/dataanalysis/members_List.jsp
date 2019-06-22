<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>后台管理系统—会员列表</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css"/>
    <link rel="stylesheet" href="${baseStatic}css/members_List.css?times=${times}"/>
</head>
<body>
<div class="history-content clearfix">
    <form id="searchDailyForm" action="${basePath}/backstage/receipt/history" method="get">
        <div class="content-search">
            <div class="clearfix">
            	<div class="fl col-min">
                    <label class="labe_l">微信号:</label>
                    <input type="text" class="tex_t" placeholder="微信号" name="wechatId" value="">
                </div>
                <div class="fl col-min">
                    <label class="labe_l">手机号:</label>
                    <input type="text" class="tex_t" placeholder="手机号" name="mobile" value="">
                </div>
                <div class="fl col-min">
                    <label class="labe_l">项目组:</label>
                    <select class="tex_t selec_t" name="groupId">
                        <option value="-1" <c:if test="${param.groupId == -1}">selected</c:if> >全部</option>
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
            </div>
            <div class="clearfix">
            	<div class="fl col-min">
                    <label class="labe_l">项目组角色:</label>
                    <select class="tex_t selec_t" name="groupRole">                        
                        <option value="">全部</option>
                        <option value="1">组长</option>
                        <option value="2">调度</option>
                        <option value="3">客服</option>
                        <option value="4">组员</option>
                    </select>
                </div>
            	<div class="fl col-min input-append date">
                    <label class="labe_l">关注时间:</label>
                    <div class="input-append date" id="datetimeStart">
                        <input type="text" class="tex_t z_index" name="createtimeStart" value="${param.createtimeStart}" readonly>
                        <span class="add-on">
							<i class="icon-th"></i>
						</span>
                    </div>
                </div>
                <div class="fl col-min">
                    <label class="labe_l">至</label>
                    <div class="input-append date" id="datetimeEnd">
                        <input type="text" class="tex_t z_index" name="createtimeEnd" value="${param.createtimeEnd}" readonly>
                        <span class="add-on">
							<i class="icon-th"></i>
						</span>
                    </div>
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
                <th>序号</th>
                <th>微信号</th>
                <th>手机号</th>
                <th>项目组</th>
                <th>项目组角色</th>
                <th>时间</th>
            </tr>
            </thead>
            <tbody>
                <tr>
                    <td>1</td>
                    <td>zhangsan123</td>
                    <td>13958486666</td>
                    <td>金发项目组</td>
                    <td>组长</td>
                    <td>2018-12-30 15:56:45</td>
                </tr>
                <tr>
                    <td>1</td>
                    <td>zhangsan123</td>
                    <td>13958486666</td>
                    <td>金发项目组</td>
                    <td>组长</td>
                    <td>2018-12-30 15:56:45</td>
                </tr>
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
        $(".content-search-btn").on("click", function () {
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
</script>
</html>