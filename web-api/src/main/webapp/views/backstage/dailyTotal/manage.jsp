<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>物流跟踪-每日统计</title>
<%@ include file="/views/include/head.jsp"%>
<link rel="stylesheet" href="${baseStatic}css/count.css?times=${times}" />
<link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css" />
</head>
<body>
	<div class="custom-content">
		<form id="searchDailyForm" action="${basePath}/backstage/dailyTotal/searchDayCount" method="post">
			<input type="hidden" name="num" value="${billTotal.num }"> 
			<input type="hidden" name="size" value="${billTotal.size }">
			<input type="hidden" name="flag">
			<div class="content-search">
				<div class="clearfix timeSelect">
                    <div class="radio-wrapper">
                        <div class="radio-item">
                            <label class="text" for="scan1" onclick="tabsubmit()">全部</label>
                            <c:if test="${billTotal.flag==0}">
                            <input type="radio" name="scanType"  id="scan1" checked="checked" /></c:if>
                            <c:if test="${billTotal.flag!=0}">
                            <input type="radio" name="scanType"  id="scan1"/></c:if>
                        </div>
                        <div class="radio-item">
                            <label class="text" for="scan2" onclick="tabsubmit(1)">昨天</label>
                            <c:if test="${billTotal.flag==1}">
                            <input type="radio" name="scanType" id="scan2"  checked="checked" /></c:if>
                            <c:if test="${billTotal.flag!=1}">
                            <input type="radio" name="scanType" id="scan2" /></c:if>
                        </div>
                        <div class="radio-item">
                            <label class="text"  for="scan3" onclick="tabsubmit(2)">最近7天</label>
                            <c:if test="${billTotal.flag==2}">
                            <input type="radio" name="scanType"  id="scan3" checked="checked" /></c:if>
                            <c:if test="${billTotal.flag!=2}">
                            <input type="radio" name="scanType"  id="scan3" /></c:if>
                        </div>
                        <div class="radio-item">
                            <label class="text" for="scan4" onclick="tabsubmit(3)">最近30天</label>
                            <c:if test="${billTotal.flag==3}">
                            <input type="radio" name="scanType"  id="scan4" checked="checked" /></c:if>
                            <c:if test="${billTotal.flag!=3}">
                            <input type="radio" name="scanType"  id="scan4" /></c:if>
                        </div>
                    </div>
                </div>
				<div class="clearfix otherSelect">
					<c:if test="${userType == 0}">
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
					</c:if>
					<div class="fl col-middle">
						<label class="labe_l">收货客户:</label>
						<input type="text" class="tex_t" placeholder="请输入客户名称筛选" name="companyName" value="${billTotal.companyName }">
					</div>
					<div class="fl col-max startTime">
						<label class="labe_l">发货日期:</label>
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
                    <div class="fl" style="width: 75px; height: 30px;">
                        <a href="javascript:;" id="search-btn" class="content-search-btn">查询</a>
                    </div>
				    <div class="fl" style="width: 85px; height: 30px; margin-left: 15px;">
	                      <a href="javascript:;"  id="link_excel" class="content-search-btn">跟踪表</a>
	                </div>
				</div>
			</div>
		</form>
		<div class="table-style">
			<table>
				<thead>
					<tr>
						<th>发货日期</th>
						<th>发货任务数</th>
						<th>应到任务数</th>
						<th>已送达任务数</th>
						<th>按时送达任务数</th>
						<th>按时送达率</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.collection}" var="dailyTotal">
						<tr>
							<td>${dailyTotal.createTime}</td>
							<td>${dailyTotal.allCount}</td>
							<td>${dailyTotal.toCount}</td>
							<td>${dailyTotal.sendCount}</td>
							<td>${dailyTotal.timeCount}</td>
							<td>${dailyTotal.sendRate}</td>
							<td>
								<a href="javascript:;" class="add-info">查看详情</a>
								<input type="hidden" name="createTime" value="${dailyTotal.createTime}"/>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="pageTest clearfix"></div>
		</div>
		<c:if test="${page.collection != null && fn:length(page.collection) > 0}">
			 <someprefix:page pageNum="${page.pageNum}" pageSize="${page.pageSize}"  pages="${page.pages}" total="${page.total}"/>
		</c:if>
	</div>
	<form action="${basePath}/backstage/dailyTotal/detail" method="post" id="detail_search">
		<input type="hidden" name="groupid"/>
		<input type="hidden" name="bindtime"/>
	</form>
</body>
<%@ include file="/views/include/floor.jsp"%>
<script src="${baseStatic}plugin/js/jquery.cookie.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${baseStatic}js/datetimepicker.js"></script>

<script>
$(document).ready(function(){
	$(":checked").parent().addClass("active");
	var flag = '${billTotal.flag}';
    var _cookie = $.cookie("open");    
	//console.log(_cookie);
	
	//刷新页面时记录日期是否展开
  /*  if(_cookie != undefined){
    	if(_cookie == 1){
    		$('.startTime').show();
    		$('#more').html('«收起');
    	}else{
    		$('.startTime').hide();
    		$('#more').html('»更多');
    	}
    }  */
   //tab切换，天数不显示查询日期
	/* if(flag!=""){
		$(".otherSelect").find('.startTime').hide();
		$(".otherSelect").find('.more').hide();
	}else{
		$(".otherSelect").find('.more').show();
	} */
   	//更多
    $('#more').click(function(){
        if($('.startTime').is(':hidden')){
            $('.startTime').show();
            $(this).html('«收起');
            $.cookie("open","1")
        }else{
            $('.startTime').hide();
            $(this).html('»更多');
            $.cookie("open","0")
        }       
    })
	$("#search-btn").on("click", function(){
		$("#searchDailyForm").submit();
	});
	$(".add-info").on("click", function(){
		var date = $(this).parent().find(":hidden[name='createTime']").val();
		var groupid = $("#selectGroup").val();
		if(date){
			$(":hidden[name='groupid']").val($("#selectGroup").val());
			$(":hidden[name='bindtime']").val(date);
			$("#detail_search").submit();
		}
	});
	//翻页事件
	$(".pagination a").each(function(i){
		$(this).on('click', function(){
			$("form input[name='num']").val($(this).attr("num"));
			$("#searchDailyForm").submit();
		});   
	});
});

//切换窗口提交
function tabsubmit(org){
	$("form input[name='flag']").val(org);
	$(":input[name='startTime']").val("");
	$(":input[name='endTime']").val("");
	$("form input[name='num']").val(1);
	$("#searchDailyForm").submit();
}
//导出任务单
$("#link_excel").on("click", function () {
    var parmas = {};
    var deliverStartTime = $(":input[name='startTime']").val();
    if(deliverStartTime && deliverStartTime !=""){
    	parmas["deliverStartTime"] = deliverStartTime;
    }
    
    var deliverEndTime = $(":input[name='endTime']").val();
    if(deliverEndTime && deliverEndTime != ""){
    	parmas["deliverEndTime"] = deliverEndTime;
    }
    
    $.util.json(base_url + '/backstage/trace/export/waybill', parmas, function (data) {
        if (data.success) {//处理返回结果
        	$.util.download(data.url)
        } else {
            $.util.error(data.message);
        }
    });
});

</script>
</html>