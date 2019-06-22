<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>物流跟踪-任务单管理</title>
<%@ include file="/views/include/head.jsp"%>
<link rel="stylesheet" href="${baseStatic}css/page.css?times=${times}" />
<link rel="stylesheet" href="${baseStatic}css/track.css?times=${times}" />
<link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css" />
<style type="text/css">
dd{
	text-align: left;
}
</style>
</head>
<body>
	<div class="track-content clearfix">
		<form id="searchWaybillForm" action="${basePath}/backstage/billbook/search" method="post">
			<input type="hidden" name="num" value="${search.num }"> 
			<input type="hidden" name="size" value="${search.size }">
			<div class="content-search">
				<div class="clearfix">
					<div class="fl col-min">
						<label class="labe_l">项目组:</label> 
						<select class="tex_t selec_t" name="groupid">
							<c:forEach items="${groups}" var="group">
								<c:if test="${search.groupid == group.id }">
									<option value="${ group.id}" selected>${group.groupName }</option>
								</c:if>
								<c:if test="${search.groupid != group.id }">
									<option value="${ group.id}">${group.groupName }</option>
								</c:if>
							</c:forEach>
						</select>
					</div>
					<div class="fl col-min">
						<label class="labe_l">查询内容:</label>
						<input type="text" class="tex_t" name="likeString" placeholder="送货单号/收货客户/任务摘要" value="${search.likeString }" maxlength="30">
					</div>
					<div class="fl col-min">
						<label class="labe_l">状态:</label>
						<select class="tex_t selec_t" name="waybillFettles" >					
							<c:if test="${20 == search.waybillFettles}">
								<option value="">全部</option>	
								<option value="20" selected>已绑定</option>
								<option value="30">运输中</option>
								<option value="35">已送达</option>
								<option value="40">已到货</option>
							</c:if>
							<c:if test="${30 == search.waybillFettles}">
								<option value="">全部</option>	
								<option value="20">已绑定</option>
								<option value="30" selected>运输中</option>
								<option value="35">已送达</option>
								<option value="40">已到货</option>
							</c:if>
							<c:if test="${40 == search.waybillFettles}">
								<option value="">全部</option>	
								<option value="20">已绑定</option>
								<option value="30">运输中</option>
								<option value="35">已送达</option>
								<option value="40" selected>已到货</option>
							</c:if>
							<c:if test="${35 == search.waybillFettles}">
								<option value="">全部</option>	
								<option value="20">已绑定</option>
								<option value="30">运输中</option>
								<option value="35" selected>已送达</option>
								<option value="40">已到货</option>
							</c:if>
							<c:if test="${search.waybillFettles == null || search.waybillFettles == ''}">
								<option value="" selected>全部</option>	
								<option value="20">已绑定</option>
								<option value="30">运输中</option>
								<option value="35">已送达</option>
								<option value="40">已到货</option>
							</c:if>
						</select>
					</div>
				</div>
				<div class="clearfix">
					<div class="fl col-min input-append date">
						<label class="labe_l">绑定时间:</label>
						<div class="input-append date" id="datetimeStart">
							<input type="text" class="tex_t z_index" name="bindStartTime" value="${search.bindStartTime }" readonly >
							<span class="add-on">
								<i class="icon-th"></i>
							</span>
						</div> 
					</div>
					<div class="fl col-min">
					    <label class="labe_l">至</label>
						<div class="input-append date" id="datetimeEnd">
							<input type="text" class="tex_t z_index"  name="bindEndTime" value="${search.bindEndTime }"readonly>
							<span class="add-on">
								<i class="icon-th"></i>
							</span>
						</div> 
					</div>
				</div>
			</div>
		</form>
		<div  class="clearfix">
			<button type="button" class="btn btn-default btn_dowload_bill">下载对账单</button>
	        <button type="button" class="btn btn-default content-search-btn" style="float: right;">查询</button>
		</div>
		<div class="table-style">
			<table class="dtable" id="trackTable">
				<thead>
					<tr>
						<th>任务单号</th>
						<th>送货单号</th>
						<th>收货客户</th>
						<th>联系人</th>
						<th>状态</th>
						<th>收入</th>
						<th>收入备注</th>
						<th>支出</th>
						<th>支出备注</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.collection}" var="waybill">
						<tr>
							<td style="word-break:break-all">
								<a href="${basePath}/backstage/trace/findById/${waybill.id}" >${waybill.barcode }</a>
							</td>
							<td>${waybill.deliveryNumber }</td>
							<td>${waybill.receiverName }</td>
							<td>${waybill.contactName }</td>
							<td>
				    			<c:if test="${waybill.waybillStatus== 20 }">
				    				已绑定
				    			</c:if> 
				    			<c:if test="${waybill.waybillStatus== 30 }">
				    				运输中
				    			</c:if> 
				    			<c:if test="${waybill.waybillStatus== 40 }">
				    				已到货
				    			</c:if>
				    			<c:if test="${waybill.waybillStatus== 35 }">
			    					已送达
			    				</c:if>
		    				</td>
							<td id="${waybill.id}_income">${accounts[waybill.id].income}</td>
							<td id="${waybill.id}_incomeRemark">${accounts[waybill.id].incomeRemark}</td>
							<td id="${waybill.id}_expenditure">${accounts[waybill.id].expenditure}</td>
							<td id="${waybill.id}_expenditureRemark">${accounts[waybill.id].expenditureRemark}</td>
							<td>
								<input type="hidden" name="waybill.waybillid" value="${waybill.id}"/>
								<button type="button" class="btn btn-link edit_something" >记账</button>
								<button type="button" class="btn btn-link show_detail" >详情</button>
							</td>
						</tr>
						<tr style="display: none">
							<td colspan="10">
								<dl class="dl-horizontal">
								  <dt>任务摘要:</dt>
								  <dd>${waybill.orderSummary }</dd>
								  <dt>联系电话:</dt>
								  <dd>${waybill.contactPhone}</dd>
								  <dt>收货地址:</dt>
								  <dd>${waybill.receiveAddress}</dd>
								  <dt>发货时间:</dt>
								  <dd>
								  	<c:if test="${waybill.createtime != null}">
										<someprefix:dateTime date="${waybill.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
									</c:if>
								  </dd>
								  <dt>要求到货时间:</dt>
								  <dd>
								  	<c:if test="${waybill.arrivaltime != null}">
										<someprefix:dateTime date="${waybill.arrivaltime}" pattern="yyyy-MM-dd HH:mm:ss"/>
									</c:if>
								  </dd>
								  <dt>到货时间:</dt>
								  <dd>
								  	<c:if test="${waybill.actualArrivalTime != null}">
										<someprefix:dateTime date="${waybill.actualArrivalTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
									</c:if>
								  </dd>
								  <dt>回单数量:</dt>
								  <dd>${waybill.receiptCount }</dd>
								</dl>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<c:if test="${page.collection != null && fn:length(page.collection) > 0}">
				 <someprefix:page pageNum="${page.pageNum}" pageSize="${page.pageSize}" pages="${page.pages}" total="${page.total}"/>
			</c:if>
		</div>
	</div>
	<div class="edit_billbook_panel" style="display: none">
		<div class="model-base-box edit_customer" style="width: 470px;">
			<input name="waybillid" type="hidden">
			<div class="model-form-field">
				<label for="">收入 :</label> 
				<input name="income" type="number" min="0" max="999999.99">
				<label style="width: 20px;">元</label>
			</div>
			<div class="model-form-field">
				<label for="">收入备注 :</label>
				<textarea name="incomeRemark" rows="2"></textarea>
			</div>
			<div class="model-form-field">
				<label for="">支出 :</label> 
				<input name="expenditure" type="number" min="0" max="999999.99">
				<label style="width: 20px;">元</label>
			</div>
			<div class="model-form-field">
				<label for="">支出备注 :</label>
				<textarea name="expenditureRemark" rows="2" ></textarea>
			</div>
		</div>
	</div>
</body>
<%@ include file="/views/include/floor.jsp"%>
<script src="${baseStatic}plugin/js/fileDownload.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${baseStatic}js/datetimepicker.js"></script>
<script>
$(document).ready(function(){
	//提交查询事件
	$(".content-search-btn").on("click", function(){
		$("#searchWaybillForm").submit();
	});
	//翻页事件
	$(".pagination a").each(function(i){
		$(this).on('click', function(){
			$("form input[name='num']").val($(this).attr("num"));
			$("#searchWaybillForm").submit();
		});   
	});
	//弹窗日期插件初始化
	$('#deadlineTime').datetimepicker({
		language : 'zh-CN',
		format : 'yyyy-mm-dd hh:ii',
		weekStart : 1, /*以星期一为一星期开始*/
		todayBtn : true,
		autoclose : true,
		pickerPosition : "bottom-left"
	});
	$(".edit_something").on('click', function(){
		var waybiilId = $(this).parent().find(":hidden[name='waybill.waybillid']").val();
		$.util.form('新增记账', $(".edit_billbook_panel").html(), function(){
			var editBody = this.$body;
			editBody.find(":hidden[name='waybillid']").val(waybiilId);
			editBody.find("input[name='income']").val($("#"+ waybiilId +"_income").html());
			editBody.find("textarea[name='incomeRemark']").val($("#"+ waybiilId +"_incomeRemark").html());
			editBody.find("input[name='expenditure']").val($("#"+ waybiilId +"_expenditure").html());
			editBody.find("textarea[name='expenditureRemark']").val($("#"+ waybiilId +"_expenditureRemark").html());
		}, function(){
			var parmas = {}, editBody = this.$body;
			editBody.find("input, textarea").each(function(index, item){
				parmas[item.name] = item.value;
			});
			console.log(parmas);
			$.util.json(base_url+'/backstage/billbook/save/', parmas, function(data){
				if(data.success){
					$.util.alert('操作提示', data.message, function(){
						$("#searchWaybillForm").submit();
					});
				}else{
					$.util.error(data.message);
				}
			});
			return false;
		 });
	});
	$(".show_detail").on('click', function(){
		$(this).parent().parent().next('tr').toggle();
	});
	//下载账单事件
	$(".btn_dowload_bill").on("click", function(){
		$.util.success('开发中...,敬请期待');
	});
});
</script>
</html>