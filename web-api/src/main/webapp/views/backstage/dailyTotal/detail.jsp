<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>物流跟踪-每日统计详情</title>
<%@ include file="/views/include/head.jsp"%>
<link rel="stylesheet" href="${baseStatic}css/countDetail.css?times=${times}" />
<link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css" />
<style type="text/css">
	.content-search .content-search-btn{
	    padding:5px 20px;
	    color:#fff;
	    border-radius: 3px;
	    background:#31acfa;
	    position:absolute;
	    top: 15px;
	    right:15%;
	    font-size:14px;
	}
</style>
</head>
<body>
	<div class="track-content">
		<form id="searchDailyForm" action="${basePath}/backstage/dailyTotal/detail" method="post">
			<input type="hidden" name="num" value="${search.num }"> 
			<input type="hidden" name="size" value="${search.size }">
			<input type="hidden" name="groupid" value="${search.groupid}">
			<input type="hidden" name="bindtime" value="${search.bindtime}">
			<div class="content-search">
				<div class="clearfix">
					<div class="fl col-min">
						<label class="labe_l">任务状态:</label> 
						<select class="tex_t selec_t" name=waybillFettles>
							<option value="">全部</option>
							 <c:if test="${20 == search.waybillFettles}">
								<option value=20 selected>已绑定</option>
								<option value=30>运输中</option>
								<option value=35>已送达</option>
								<option value=40>已到货</option>
							</c:if> 
							<c:if test="${30 == search.waybillFettles}">
								<option value=20>已绑定</option>
								<option value=30 selected>运输中</option>
								<option value=35>已送达</option>
								<option value=40>已到货</option>
							</c:if>
							<c:if test="${40 == search.waybillFettles}">
								<option value=20>已绑定</option>
								<option value=30>运输中</option>
								<option value=35>已送达</option>
								<option value=40 selected>已到货</option>
							</c:if>
							<c:if test="${35 == search.waybillFettles}">
								<option value=20>已绑定</option>
								<option value=30>运输中</option>
								<option value=35 selected>已送达</option>
								<option value=40>已到货</option>
							</c:if> 
							<c:if test="${search.waybillFettles == null or search.waybillFettles == ''}">
								<option value=20>已绑定</option>
								<option value=30>运输中</option>
								<option value=35>已送达</option>
								<option value=40>已到货</option>
							</c:if> 
						</select>
					</div>
					<div class="fl col-min">
						<label class="labe_l">收货客户:</label> 
						<input type="text" class="tex_t" name="likeString" placeholder="请输入任务单号/送货单号/收货客户筛选" value="${search.likeString }">
					</div>
				</div>
				<div>
					<a href="javascript:;" class="content-search-btn">查询</a>
				</div>
			</div>
		</form>
		<div class="table-style">
			<table class="dtable">
				<thead>
					<tr>
						<th>任务状态</th>
						<th>任务单号</th>
						<th>送货单号</th>
						<th>任务摘要</th>
						<th>收货客户</th>
						<th>收货地址</th>
						<th>要求到货时间</th>
						<th>绑定时间</th>
						<th>实际送达时间</th>
						<th>是否超时</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.collection }" var="waybill">
						<tr>
							<td>
								<c:if test="${waybill.waybillStatus == 20 }">
				    				已绑定
				    			</c:if> 
				    			<c:if test="${waybill.waybillStatus == 30 }">
				    				运输中
				    			</c:if> 
				    			<c:if test="${waybill.waybillStatus == 40 }">
				    				已到货
				    			</c:if>
				    			<c:if test="${waybill.waybillStatus == 35 }">
				    				已送达
				    			</c:if>
		    				</td>
							<td>${waybill.barcode }</td>
							<td>${waybill.deliveryNumber }</td>
							<td>${waybill.orderSummary }</td>
							<td>${waybill.receiverName }</td>
							<td>${waybill.receiveAddress }</td>
							<td>
								<c:if test="${waybill.arrivaltime != null }">
									<fmt:formatDate value="${waybill.arrivaltime}" pattern="yyyy-MM-dd HH:mm" /> 
								</c:if>
							</td>
							<td>
								<c:if test="${waybill.createtime != null }">
									<fmt:formatDate value="${waybill.createtime}" pattern="yyyy-MM-dd HH:mm" /> 
								</c:if>
							</td>
							<td>
								<c:if test="${waybill.actualArrivalTime !=null }">
									<fmt:formatDate value="${waybill.actualArrivalTime}" pattern="yyyy-MM-dd HH:mm" /> 
								</c:if>
							</td>
							<td>
							<c:if test="${ waybill.delay != null && waybill.delay >= 0 }">
								<c:if test="${ waybill.delay == 2 }">
									<font>否</font>   
								</c:if>
								<c:if test="${ waybill.delay == 1 || waybill.delay == 0 }">
									<font color="red">是</font>
								</c:if>
							</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<c:if test="${page.collection != null && fn:length(page.collection) > 0}">
			 <someprefix:page pageNum="${page.pageNum}" pageSize="${page.pageSize}"  pages="${page.pages}" total="${page.total}"/>
		</c:if> 
	</div>
</body>
<%@ include file="/views/include/floor.jsp"%>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.zh-CN.js"></script>
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
});
</script>
</html>