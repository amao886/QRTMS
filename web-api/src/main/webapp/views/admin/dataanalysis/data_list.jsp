<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>物流跟踪-每日统计</title>
<%@ include file="/views/include/head.jsp"%>
<link rel="stylesheet" href="${baseStatic}css/data_list.css?times=${times}" />
<link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css" />
</head>
<body>
	<div class="custom-content">
		<form id="searchDailyForm" action="${basePath}/admin/business/search" method="post">
			<div class="content-search">
				<div class="clearfix timeSelect">
					<span class="singleSelect">全部</span>
  					<span class="singleSelect" data-day="1">昨天</span>
  					<span class="singleSelect" data-day="7">最近7天</span>
  					<span class="singleSelect" data-day="30">最近30天</span>
  					<input type="hidden" id="dayInput" value="">                              
                </div>
				<div class="clearfix otherSelect">					
					<div class="fl col-max startTime">
						<label class="labe_l">日期:</label>
						<div class="clearfix tex_t reset_text">
							<div class="input-append date fl" id="datetimeStart">
								<input type="text" class="" name="startTime" value="${billTotal.startTime }" readonly>
								<span class="add-on">
									<i class="icon-th"></i>
								</span>
							</div>
							<span class="to_link">至</span>
							<div class="input-append date fl" id="datetimeEnd">
								<input type="text" class="" name="endTime" value="${billTotal.endTime }" readonly>
								<span class="add-on">
									<i class="icon-th"></i>
								</span>
							</div>
						</div>
					</div>
					<div class="fl col-middle">
						<label class="labe_l">项目组:</label>
						<select class="tex_t selec_t" name="groupid" id="selectGroup">
							<c:forEach items="${groups}" var="group">
								<c:if test="${billTotal.groupid == group.id }">
									<option value="${ group.id}" selected>${group.groupName }</option>
								</c:if>
								<c:if test="${billTotal.groupid != group.id }">
									<option value="${ group.id}">${group.groupName }</option>
								</c:if>
							</c:forEach>
						</select>
					</div>
                    <div class="fl col-min">
                        <a href="javascript:;" class="content-search-btn">查询</a>
                    </div>
				</div>
			</div>
		</form>
		<div class="table-style">
			<table>
				<thead>
					<tr>
						<th>会员</th>
						<th>回单</th>
						<th>任务单</th>
						<th>已完成任务单</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>100</td>
						<td>100</td>
						<td>1151</td>
						<td>514514</td>
					</tr>
				</tbody>
			</table>
			<div class="pageTest clearfix"></div>
		</div>
		<c:if test="${page.collection != null && fn:length(page.collection) > 0}">
			 <someprefix:page pageNum="${page.pageNum}" pageSize="${page.pageSize}"  pages="${page.pages}" total="${page.total}"/>
		</c:if>
	</div>
</body>
<%@ include file="/views/include/floor.jsp"%>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${baseStatic}js/datetimepicker.js"></script>

<script>
$(document).ready(function(){
	$(".content-search-btn").on("click", function(){
		$("#searchDailyForm").submit();
	});

	//翻页事件
	$(".pagination a").each(function(i){
		$(this).on('click', function(){
			$("form input[name='num']").val($(this).attr("num"));
			$("#searchDailyForm").submit();
		});   
	});
	
	//选择天数
	$(".timeSelect").find(".singleSelect").eq(0).addClass("curr")
	$(".timeSelect").on("click","span",function(){
		var _day = $(this).attr("data-day")
		$(this).addClass("curr").siblings("span").removeClass("curr");
		$("#dayInput").val(_day);
		//alert($("#dayInput").val());
		$("#searchDailyForm").submit();
	});
});

</script>
</html>