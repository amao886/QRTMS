<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="//webapi.amap.com/ui/1.0/ui/misc/PositionPicker/examples/" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<title>物流跟踪-客户管理</title>
<%@ include file="/views/include/head.jsp"%>
<link rel="stylesheet" href="${baseStatic}css/page.css?times=${times}" />
<link rel="stylesheet" href="${baseStatic}css/customInfo.css?times=${times}" />
<style type="text/css">
.amap-sug-result{
	z-index: 99999
}
.jconfirm {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	z-index: 100;
	font-family: inherit;
	overflow: hidden
}

.searchBtn{
	color: rgb(255, 255, 255);
    font-size: 14px;
    padding: 3px 20px;
    border-radius: 3px;
    background: rgb(49, 172, 250);
}

::-webkit-scrollbar{width:14px;}
::-webkit-scrollbar-track{background-color:#bee1eb;}
::-webkit-scrollbar-thumb{background-color:#00aff0;}
::-webkit-scrollbar-thumb:hover {background-color:#9c3}
::-webkit-scrollbar-thumb:active {background-color:#00aff0}
</style>
</head>
<body>
	<div class="custom-content">
		<form id="searchCustomerForm" action="${basePath}/backstage/requestserial/queryRequestSerialList" method="post">
			<input type="hidden" id="num" name="num" value="${ requestserial.num}">
			<input type="hidden" id="size" name="size" value="${requestserial.size}">
			<input type="hidden" name='mode' value='dragMarker'/>
			<div class="content-search">
				<div class="clearfix">
					<div class="fl col-min">
						<label class="labe_l">应用类型:</label>${requestSerial.appType}
						<select id="select_group" class="tex_t selec_t" name="appType">
							<option value="all">全部</option>
							<option value="backstage" <c:if test="${requestSerial.appType ==  'backstage'}"> selected</c:if> >web端</option>
							<option value="api"<c:if test="${requestSerial.appType ==  'api'}"> selected</c:if> >微信端</option>
						</select>
					</div>
					<a href="javascript:;" class="content-search-btn" style="top: 16px;right: 50%;">查询</a>
				</div>
			</div>
		</form>
		<%--<div class="edit">
			<button type="button" class="btn btn-default btn_add_customer">添加</button>
		</div>--%>
		<div class="table-style">
			<table>
				<thead>
					<tr>
						<th>应用类型</th>
						<th>请求时间</th>
						<th>请求实际地址</th>
						<th>服务端耗时</th>
						<th>请求方式</th>
						<th>客户端主机名</th>
						<th>用户标识</th>
						<th>操作系统</th>
						<th>浏览器版本</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${page.list }" var="requestSerialList">
					<tr>
						<td>${requestSerialList.appType}</td>
						<c:choose>
							<c:when test="${requestSerialList.dateTime != null}">
							<td><someprefix:dateTime millis="${requestSerialList.dateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							</c:when>
							<c:otherwise>
								<td>-</td>
							</c:otherwise>
						</c:choose>
						<td>${requestSerialList.uri}</td>
						<td>${requestSerialList.time}</td>
						<td>${requestSerialList.method}</td>
						<td>${requestSerialList.host}</td>
						<td>${requestSerialList.userKey}</td>
						<td>${requestSerialList.os}</td>
						<td>${requestSerialList.browser}</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
		<c:if test="${page.list != null && fn:length(page.list) > 0}">
			 <someprefix:page pageNum="${page.pageNum}" pageSize="${page.pageSize}"  pages="${page.pages}" total="${page.total}"/>
		</c:if>
	</div>

	<!--弹出框-->
</body>
<%@ include file="/views/include/floor.jsp"%>
<script type="text/javascript" src='//webapi.amap.com/maps?v=1.4.0&key=a4e0b28c9ca0d166b8872fc3823dbb8a&plugin=AMap.ToolBar,AMap.DistrictSearch,AMap.Geocoder'></script>
<!-- UI组件库 1.0 -->
<script src="//webapi.amap.com/ui/1.0/main.js?v=1.0.11"></script>
<script>
var searchBody;
$(document).ready(function(){
	//提交查询事件
	$(".content-search-btn").on("click", function(){
		$("#searchCustomerForm").submit();
	});
	//翻页事件
	$(".pagination a").each(function(i){
		$(this).on('click', function(){
			$("form input[name='num']").val($(this).attr("num"));
			$("#searchCustomerForm").submit();
		});   
	});

});
</script>

</html>