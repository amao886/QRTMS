<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-任务单管理</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/page.css?times=${times}"/>
    <link rel="stylesheet" href="${baseStatic}css/track.css?times=${times}"/>
    <link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css"/>
</head>
<body>
<div class="track-content">
	<form id="searchWaybillForm" action="${basePath}/backstage/trace/search" method="post">
		<input type="hidden" name="num" value="${search.num }">
        <input type="hidden" name="size" value="${search.size }">
		<div class="content-search">
			<div class="search-condition">
				<select class="selec_t" name="groupid">
					<c:forEach items="${groups}" var="group">
                      <c:if test="${search.groupid == group.id }">
                          <option value="${ group.id}" selected>${group.groupName }</option>
                      </c:if>
                      <c:if test="${search.groupid != group.id }">
                          <option value="${ group.id}">${group.groupName }</option>
                      </c:if>
                  </c:forEach>
				</select>
				<div class="search-con">
					<input type="text" class="search-input" name="likeString" value="${search.likeString }" maxlength="30" placeholder="项目组/运货单号/收货客户/任务摘要" />
					<button class="search-btn">订单搜索</button>
				</div>
				<div class="show-more">更多筛选条件<span class="show-more-icon"></span></div>
				
			</div>
			<div class="search-hide clearfix">
				<div class="fl col-min">
					<label class="label-tag">任务状态</label>
					<div class="rightBox">
						<select class="select-tag" name="waybillFettles">
							<c:if test="${10 == search.waybillFettles}">
								<option value="">全部</option>	
								<option value="10" selected>待绑定</option>
								<option value="20">已绑定</option>
								<option value="30">运输中</option>
								<option value="35">已送达</option>
								<option value="40">已到货</option>
							</c:if>					
							<c:if test="${20 == search.waybillFettles}">
								<option value="">全部</option>	
								<option value="10">待绑定</option>
								<option value="20" selected>已绑定</option>
								<option value="30">运输中</option>
								<option value="35">已送达</option>
								<option value="40">已到货</option>
							</c:if>
							<c:if test="${30 == search.waybillFettles}">
								<option value="">全部</option>	
								<option value="10">待绑定</option>
								<option value="20">已绑定</option>
								<option value="30" selected>运输中</option>
								<option value="35">已送达</option>
								<option value="40">已到货</option>
							</c:if>
							<c:if test="${40 == search.waybillFettles}">
								<option value="">全部</option>	
								<option value="10">待绑定</option>
								<option value="20">已绑定</option>
								<option value="30">运输中</option>
								<option value="35">已送达</option>
								<option value="40" selected>已到货</option>
							</c:if>
							<c:if test="${35 == search.waybillFettles}">
								<option value="">全部</option>	
								<option value="10">待绑定</option>
								<option value="20">已绑定</option>
								<option value="30">运输中</option>
								<option value="35" selected>已送达</option>
								<option value="40">已到货</option>
							</c:if>
							<c:if test="${search.waybillFettles == null || search.waybillFettles == ''}">
								<option value="" selected>全部</option>
								<option value="10">待绑定</option>	
								<option value="20">已绑定</option>
								<option value="30">运输中</option>
								<option value="35">已送达</option>
								<option value="40">已到货</option>
							</c:if>
						</select>
					</div>	                   	
				</div>
				<div class="fl col-min">
					<label class="label-tag">回单状态</label>
					<div class="rightBox">
						<select class="select-tag" name="receiptFettles">
	                        <c:if test="${1 == search.receiptFettles && search.receiptFettles != ''}">
	                            <option value="">全部</option>
	                            <option value="1" selected>未上传</option>
	                            <option value="2">有上传</option>
	                            <option value="3">审核中</option>
	                            <option value="4">已审核</option>
	                        </c:if>
	                        <c:if test="${2 == search.receiptFettles}">
	                            <option value="">全部</option>
	                            <option value="1">未上传</option>
	                            <option value="2" selected>有上传</option>
	                            <option value="3">审核中</option>
	                            <option value="4">已审核</option>
	                        </c:if>
	                        <c:if test="${3 == search.receiptFettles}">
	                            <option value="">全部</option>
	                            <option value="1">未上传</option>
	                            <option value="2">有上传</option>
	                            <option value="3" selected>审核中</option>
	                            <option value="4">已审核</option>
	                        </c:if>
	                        <c:if test="${4 == search.receiptFettles}">
	                            <option value="">全部</option>
	                            <option value="1">未上传</option>
	                            <option value="2">有上传</option>
	                            <option value="3">审核中</option>
	                            <option value="4" selected>已审核</option>
	                        </c:if>
	                        <c:if test="${search.receiptFettles == null || search.receiptFettles == ''}">
	                            <option value="" selected>全部</option>
	                            <option value="1">未上传</option>
	                            <option value="2">有上传</option>
	                            <option value="3">审核中</option>
	                            <option value="4">已审核</option>
	                        </c:if>
						</select>
					</div>
	                   	
				</div>
				<div class="fl col-max">
	               <label class="label-tag">绑定时间</label>
	               <div class="rightBox fs0">
	               		<div class="div-block input-append date" id="datetimeStart">
		                   <input type="text" class="input-time" name="bindStartTime" value="${search.bindStartTime }" readonly>
		                   <span class="add-on"><i class="icon-th"></i></span>
		               </div>
		               <span class="span-block">至</span>
		               <div class="div-block input-append date" id=datetimeEnd>
		                   <input type="text" class="input-time" name="bindEndTime" value="${search.bindEndTime }" readonly>
		                   <span class="add-on"><i class="icon-th"></i></span>
		               </div>
	               </div>
	           </div>
			</div>
		</div>
	</form>
	<div class="content-text">
		<div class="table-head">
			<span class="span-th" style="width:30%;">收货方</span>
			<span class="span-th" style="width:30%;">发货方</span>
			<span class="span-th" style="width:10%;">回单(已审/总数)</span>
			<span class="span-th" style="width:10%;">状态</span>
			<span class="span-th" style="width:10%;">电子围栏</span>
			<span class="span-th" style="width:10%;">交易操作</span>
		</div>
		<div class="table-handle">
			<a href="javascript:;" class="aBtn btn_send_msg">发送短信</a>
			<a href="javascript:;" class="aBtn btn_dowload_image">下载回单</a>
			<a href="${basePath}/backstage/trace/addview" class="aBtn btn_task_add">新增订单</a>
			<a href="javascript:;" class="aBtn btn_task_import">批量导入</a>
			<a href="javascript:;" class="aBtn" url="${template}" id="down_template">模板下载</a>
		</div>
		<div class="table-page clearfix">
			<div class="fl page-item" style="margin-top:6px;">
				<label><input type="checkbox" style="margin:0 20px;vertical-align:middle;" id="checkAll" />全选</label>
			</div>
			<div class="fr page-item">
				<c:if test="${page.collection != null && fn:length(page.collection) > 0}">
		            <someprefix:page pageNum="${page.pageNum}" pageSize="${page.pageSize}" pages="${page.pages}" total="${page.total}"/>
		        </c:if>
			</div>
		</div>
		<div class="table-style-group">
			<!-- 数据展示，循环整个table -->
			<c:forEach items="${page.collection }" var="waybill">
			<table>
				<thead>
					<tr>
						<th colspan="2" style="width: 50%;">
							<div class="title-left">
								<input type="checkbox" style="margin:0 20px;vertical-align:middle;" name="checksingle" value="${waybill.id}"/>
								<div class="ib dtime"><fmt:formatDate value="${waybill.createtime}" pattern="yyyy-MM-dd"/></div>
								<div class="ib waybill-num">任务单号：
									<span>
									<a href="${basePath}/backstage/trace/findById/${waybill.id}" id="${waybill.id }_barcode">${waybill.barcode}</a>
									</span>
								</div>
								<div class="ib deliver-num">送货单号：<span>${waybill.deliveryNumber }</span></div>
							</div>
						</th>
						<th colspan="2">
						<c:if test="${waybill.address == null || waybill.address == ''}">
							<div style="font-weight:normal;float: right;">
							暂无定位信息
							</div>
						</c:if>
						<c:if test="${waybill.address != null && waybill.address != ''}">
							<div class="clamp-div" style="cursor: pointer;">
								最新位置<div style="display:none;">${waybill.address}(<fmt:formatDate value="${waybill.createtime}" pattern="yyyy-MM-dd HH:mm"/>)</div>
							</div>
						</c:if>
						</th>
						<th colspan="2">
							<div class="title-right">
								发货时间：<span><fmt:formatDate value="${waybill.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
							</div>
						</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>
							<div class="receive-msg">
								<div class="msg-item clearfix"><span class="span-fl">收货人：</span><span class="span-fr">${waybill.receiverName}</span></div>
								<div class="msg-item clearfix"><span class="span-fl">联系人：</span>
									<span class="span-fr">
									${waybill.contactName }
			                        <c:if test="${waybill.contactPhone != null && waybill.contactPhone != ''}">
			                          ${waybill.contactPhone}
			                        </c:if>
									</span>
								</div>
								<div class="msg-item clearfix"><span class="span-fl">地址：</span><span class="span-fr">${waybill.receiveAddress}</span></div>
							</div>
						</td>
						<td>
							<div class="deliver-msg">
								<div class="msg-item clearfix"><span class="span-fl">发货人：</span><span class="span-fr">${waybill.shipperName}</span></div>
								<div class="msg-item clearfix"><span class="span-fl">联系人：</span><span class="span-fr">暂无</span></div>
								<div class="msg-item clearfix"><span class="span-fl">地址：</span><span class="span-fr">暂无</span></div>
							</div>
						</td>
						<td rowspan="2" class="td-center">
							<span>${waybill.receiptVerifyCount}</span>/<span>${waybill.receiptCount}</span>
						</td>
						<td rowspan="2" class="td-center">
	                        <c:if test="${waybill.waybillStatus== 10 }">未绑定</c:if>
	                        <c:if test="${waybill.waybillStatus== 20 }">已绑定</c:if>
	                        <c:if test="${waybill.waybillStatus== 30 }">运输中</c:if>
	                        <c:if test="${waybill.waybillStatus== 40 }">已到货</c:if>
	                        <c:if test="${waybill.waybillStatus== 35 }">已送达</c:if>
						</td>
						<td rowspan="2" class="td-center">
                        	<label for="${waybill.id}" class="switch-cp">
                            <c:if test="${waybill.fenceStatus != null && waybill.fenceStatus == 1}">
                              <input id="${waybill.id}" class="switch-cp__input" type="checkbox" checked="checked"/>
                            </c:if>
                            <c:if test="${waybill.fenceStatus == null || waybill.fenceStatus == 0}">
                              <input id="${waybill.id}" class="switch-cp__input" type="checkbox"/>
                            </c:if>
                            <div class="switch-cp__box"></div>
                        	</label>
						</td>
						<td rowspan="2">
							<span class="others_val" waybillid="${waybill.id}" groupid="${waybill.groupid }"
                              weight="${waybill.weight }" volume="${waybill.volume }" number="${waybill.number }"
                              verifyTotal="${waybill.receiptCount}" verifyCount="${waybill.receiptVerifyCount}"></span>
                            <c:if test="${waybill.waybillStatus > 10 && waybill.waybillStatus < 40}">
								<a href="javascript:;" class="aBtn-handle aBtn-sure link_sure">确认收货</a>
							</c:if>
							<a href="${basePath}/backstage/trace/findById/${waybill.id}" class="aBtn-handle link_look">查看</a>
							<c:if test="${waybill.waybillStatus == 10}">
			                    <li><a href="javascript:;" class="aBtn-handle link_delete">删除</a></li>
			                </c:if>
			                <c:if test="${waybill.waybillStatus < 35}">
			                    <li><a href="${basePath}/backstage/trace/eidtview/${waybill.id}" class="aBtn-handle link_edit">编辑</a></li>
			                    <li><a href="javascript:;" class="aBtn-handle link_customer">关联客户</a></li>
			                </c:if>
                  			<c:if test="${waybill.waybillStatus > 10 && waybill.waybillStatus < 35 }">
                                <li><a href="javascript:;" class="aBtn-handle link_deliver">确认送达</a></li>
                            </c:if>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<div class="message-div clearfix">
								<span class="span-fl">摘要：</span>
								<p class="span-fr">${waybill.orderSummary}</p>
							</div>
						</td>
					</tr>
				</tbody>	
			</table>
			</c:forEach>
		</div>
		
	</div>
</div>

<!-- 收货客户弹出框 -->
<div class="bind_customer_panel" style="display: none">
    <div class="model-base-box bind_customer" style="width: 600px;">
        <div class="search-content">
            <div class="clearfix">
                <div class="fl inline-field">
                    <label class="labe_l">收货客户:</label>
                    <input type="text" class="tex_t" name="companyName">
                </div>
                <div class="fl inline-field">
                    <label class="labe_l">联系人:</label>
                    <input type="text" class="tex_t" name="contacts">
                </div>
                <div class="fr inline-field">
                    <input type="hidden" class="tex_t" name="groupId">
                    <button type="button" class="btn btn-default search_btn" id="search_customer">查询</button>
                </div>
            </div>
        </div>
        <!--收货客户维护  -->
        <div class="table-style">
            <table class="wtable">
                <thead>
                <tr>
                    <th>收货客户</th>
                    <th width="15%">联系人</th>
                    <th width="18%">联系电话</th>
                    <th>收货地址</th>
                    <th width="8%">添加</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
    </div>
</div>
<!-- 上传导入 -->
<div class="import_task_panel" style="display: none">
    <div class="model-base-box import_task" style="width: 480px;">
        <form id="importForm" action="${basePath}/backstage/trace/import/excel" enctype="multipart/form-data" method="post">
            <div class="model-form-field">
             <label>项目组:</label>
             <select id="select_group" class="tex_t selec_t" name="groupid">
                 <option value="">--请选择项目组--</option>
                    <c:forEach items="${groups}" var="group">
                        <option value="${group.id}">${group.groupName }</option>
                    </c:forEach>
                </select>
         </div>
         <div class="model-form-field">
             <label>选择文件:</label>
             <input type="text" name="fileName" id="fileName" readonly/>
             <a href="javascript:;" class="file">选择文件
	    		<input name="file" type="file" accept=".xls,.xlsx" id="uploadFile"/>
			</a>
         </div>
        </form>
    </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script src="${baseStatic}plugin/js/fileDownload.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.zh-CN.js"></script>
<%-- <script src="${baseStatic}plugin/js/jquery-hcheckbox.js"></script> --%>
<script src="${baseStatic}plugin/js/jquery.mloading.js"></script>
<script type="text/javascript" src="${baseStatic}js/datetimepicker.js"></script>
<script>
    $(document).ready(function () {
    	//点击展示更多搜索条件
    	$('.show-more').on('click',function(){
    		$('.search-hide').show();
    	})
    	
    	
    	//提交查询事件
        $(".content-search-btn").on("click", function () {
        	$("form input[name='num']").val(1);
        	$("#searchWaybillForm").submit();
        });
        //翻页事件
        $(".pagination a").each(function (i) {
            $(this).on('click', function () {
            	$("form input[name='num']").val($(this).attr("num"));
                $("#searchWaybillForm").submit();
            });
        });
        function searchCustomer(editBody) {
            var search = editBody.find(".search-content");
            var parmas = {
           		'companyName': search.find("input[name='companyName']").val(),
                'contacts': search.find("input[name='contacts']").val(),
                'groupId': search.find("input[name='groupId']").val()
            }
            $.util.json(base_url + "/backstage/customer/search/ajax", parmas, function (data) {
                if (data.success) {
                    var tbody = editBody.find('table tbody');
                    tbody.html('');
                    $.each(data.customers, function (i, item) {
                        var tr = "<tr>"
                            + "<td>" + item.companyName + "</td>"
                            + "<td>" + (item.contacts != null ? item.contacts : "") + "</td>"
                            + "<td>" + (item.contactNumber != null ? item.contactNumber : "") + "</td>"
                            + "<td>" + (item.fullAddress != null ? item.fullAddress : "") + "</td>"
                            + "<td><input type='radio' name='about' value='" + item.id + "'/></td>"
                            + "</tr>";
                        tbody.append(tr);
                    });
                }
            }, 'json');
        }
        //打开绑定客户弹窗事件
        $(".link_customer").on("click", function () {
            var obj = $(this).parents("td").find(".others_val");
            var waybillid = $(obj).attr("waybillid"), groupid = $(obj).attr("groupid");
            $.util.form('关联客户', $(".bind_customer_panel").html(), function (model) {
                var editBody = this.$body;
                editBody.find("input[name='groupId']").val(groupid);
                editBody.find("button").on('click', function () {
                    searchCustomer(editBody);
                });
            }, function () {
                var editBody = this.$body;
                var parmas = {
                    'id': waybillid,
                    'customerid': editBody.find('input:radio[name="about"]:checked').val()
                }
                if (parmas.customerid == null || parmas.customerid == "") {
                    $.util.warning("请选择一位要关联的客户");
                    return;
                }
                $.post(base_url + "/backstage/trace/bindCustomer", parmas, function (data) {
                    if (data.success) {//处理返回结果
                        $.util.alert("操作提示", data.message, function () {
                            $("#searchWaybillForm").submit();
                        });
                    } else {
                        $.util.error(data.message);
                    }
                }, 'json');
                return false;
            });
        });
        //确认到货事件
        $(".link_sure").on("click", function () {
            var my = $(this);
            var obj = $(my).parents("td").find(".others_val");
            var waybillid = obj.attr("waybillid"), total = obj.attr("verifyTotal"), count = obj.attr("verifyCount");
            console.log(waybillid + "------" + total + "---" + count);
            var cal_btn, msg;
            if (total) {
                msg = '是否确认到货?';
                if (Number(total) > Number(count)) {
                    msg = '有未审核回单,确认后回单自动设置为合格,是否确认到货?'
                    cal_btn = {
                        text: '前往回单审核', action: function () {
                            location.href = base_url + '/backstage/receipt/search?waybill.id=' + waybillid;
                        }
                    };
                }
            } else {
                msg = '还未上传回单,是否确认到货?';
            }
            $.util.confirm('到货确认', msg, function () {
                $.post(base_url + '/backstage/trace/confirm/' + waybillid, {}, function (data) {
                    if (data.success) {//处理返回结果
                        $("#searchWaybillForm").submit();
                    } else {
                        $.util.error(data.message);
                    }
                }, 'json');
            }, cal_btn);
        });
        //确认送达事件
        $(".link_deliver").on("click", function () {
            var my = $(this);
            var obj = $(my).parents("td").find(".others_val");
            var waybillid = obj.attr("waybillid");
            $.util.confirm('送达确认', '是否确认送达？', function () {
                $.post(base_url + '/backstage/trace/deliver/' + waybillid, {}, function (data) {
                    if (data.success) {//处理返回结果
                        /*
                    	$(my).html('已送达').unbind();
                        $("#" + waybillid + "_bindstatus").html('已送达');
                        $(my).parent().find(".link_customer").hide();
                        $(my).parent().find(".link_edit").hide();
                        $(my).hide();
						*/
                        $("#searchWaybillForm").submit();
                    } else {
                        $.util.error(data.message);
                    }
                }, 'json');
            });
        });
        //删除事件
        $(".link_delete").on("click", function () {
            var my = $(this);
            var obj = $(my).parents("td").find(".others_val");
            var waybillid = obj.attr("waybillid");
            $.util.confirm('删除确认', '是否确认删除该项数据？', function () {
                $.post(base_url + '/backstage/trace/delete', {"waybillId": waybillid}, function (data) {
                    if (data.success) {//处理返回结果
                        $("#searchWaybillForm").submit();
                    } else {
                        $.util.error(data.message);
                    }
                }, 'json');
            });
        });
        //全选事件
        $('#checkAll').on("click", function () {
            $("input[name='checksingle']").prop('checked', this.checked);
        });
        //单选事件
        $("input[name='checksingle']").on("click", function () {
            /*获取当前选中的个数，判断是否跟全部复选框的个数是否相等*/
            var all = $("input[name='checksingle']").length;
            var selects = $("input[name='checksingle']:checked").length;
            $('#checkAll').prop('checked', (all - selects == 0));
        });
        //发送通知短信事件
        $(".btn_send_msg").on("click", function () {
            var selects = $("input[name='checksingle']:checked");
            if (!selects || selects.length <= 0) {
                $.util.warning("选择一个要发送短信的收货客户");
            } else {
                var waybills = new Array();
                $(selects).each(function () {
                    waybills.push($(this).val());
                });
                if (waybills && waybills.length > 0) {
                    $.util.json(base_url + '/backstage/trace/sendsms', waybills, function (data) {
                        if (data.success) {//处理返回结果
                            $.util.success(data.message);
                        }
                    });
                }
            }
        });
        //下载任务回单事件
        $(".btn_dowload_image").on("click", function () {
            var selects = $("input[name='checksingle']:checked");
            if (!selects || selects.length <= 0) {
                $.util.warning("至少选择一个要下载回单的任务");
            } else {
                var waybills = new Array();
                $(selects).each(function () {
                    waybills.push($(this).val());
                });
                if (waybills.length > 0) {
                    $.util.json(base_url + '/backstage/trace/dowload/receipts', waybills, function (data) {
                        if (data.success) {//处理返回结果
                            var msg = "文件数量 : <font color='red'>" + data.count + "</font>个,大小 : <font color='red'>" + data.length + "</font>MB";
                            $.util.alert("下载回单文件", msg, function () {
                                $.fileDownload(data.url);
                            });
                        } else {
                            $.util.error(data.message);
                        }
                    });
                }
            }
        });
        $('.dtable').hcheckbox();
        $('#myModal .wtable').hradio();

        //批量上传
        $(".btn_task_import").on("click",function(){
        	$.util.form('批量上传', $(".import_task_panel").html(), function(model){
        		var editBody = this.$body;
        		editBody.find("input:file").on('change', function(){
        			var fileName = this.value;
        			//校验文件格式
        	    	var fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        	    	if(fileType != "xlsx" && fileType != "xls"){
        	    		$.util.error("选择的文件格式不支持！"); 
        	    		return false;
        	    	}
        			$(this).parent().parent().find("input[name='fileName']").val(fileName);
        		})
        	}, function(){
                var editBody = this.$body, formData = new FormData();
                var groupId = editBody.find("select").val()
    	    	if(!groupId){
    	    	    $.util.error("请选择一个项目组！");
    	    	    return false;
    	    	 }else{
    	             formData.append("groupid", groupId);
    	    	 }
                 var file = editBody.find("input:file");
    	    	 if(!file.val()){
    	    	    $.util.error("请选择需要导入的excel文件！");
    	    	    return false;
    	    	 }else{
    	             formData.append("file", file[0].files[0]);
    	    	 }
    	    	 $.ajax({ 
    	    		 url : base_url +"/backstage/trace/import/excel", 
    	    		 type : 'POST', 
    	    		 data : formData, 
    	    		 processData : false, // 告诉jQuery不要去处理发送的数据
    	    		 contentType : false,// 告诉jQuery不要去设置Content-Type请求头
    	    		 success : function(response) { 
    		    		 if(response.success){
    		    			$.util.success(response.message, function(){
    		    				 window.location.href = base_url + response.url;
 							});
    		    		 }else{
    						if(response.url){
    							$.util.danger('有异常数据', response.message, function(){
    								$.fileDownload(response.url);
    								$("select[name='waybillFettles']").val(10);
    				                $("#searchWaybillForm").submit();
    							});
    						}else{
    							$.util.error(response.message);
    						} 
    		    		 }
    	    		 }
    	    	});
         	});
     	});
	  	//任务单模板下载
	    $("#down_template").click(function(){
	    	$.fileDownload($(this).attr("url"));
	    });
	    //电子围栏开关事件
	  	$(".switch-cp__input").on("click", function(){
	  		var self = $(this);
	  		var checked = self.attr("checked");
	  		var parmas = {"id": self.attr("id")};
	        $.util.json(base_url + '/backstage/trace/updateFenceStatus', parmas, function (data) {
	            if (data.success) {
	            	self.attr("checked", (data.status && data.status == 1));
	            	self.attr("value", data.status);
	            } else {
	                $.util.error(data.message);
	                self.attr("checked", checked);
	            }
	        });
	  	});
	    $(".clamp-div").tooltip({
	    	title:function(v,m){
	    		return $(this).find("div").html();
	    	}
	    });
    });

    function formatTime(datetimes) {
        var datetime = new Date(datetimes);
        var year = datetime.getFullYear();
        var month = datetime.getMonth() + 1;//js从0开始取
        var date = datetime.getDate();
        if (month < 10) {
            month = "0" + month;
        }
        if (date < 10) {
            date = "0" + date;
        }
        return year + "-" + month + "-" + date;
    }
</script>
</html>