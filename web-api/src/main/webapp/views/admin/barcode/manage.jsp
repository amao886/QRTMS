<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-条码管理</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css"/>
    <link rel="stylesheet" href="${baseStatic}css/inventory.css?times=${times}"/>
    <link rel="stylesheet" href="${baseStatic}plugin/comboSelect/combo.select.css"/>
    <style>
        .jconfirm .jconfirm-box div.jconfirm-content-pane {
            position: static;
        }

        .jconfirm .jconfirm-box {
            overflow: visible;
        }

        .combo-input.text-input-user {
            padding: 4px 0;
        }
    </style>
</head>
<body>
<div class="history-content clearfix">
    <form id="searchWaybillForm" action="${basePath}/admin/barcode/all/search" method="post">
        <input type="hidden" name="num" value="${results.pageNum}">
        <input type="hidden" name="size" value="10">
    	<div class="content-search">
            <div class="clearfix">
                <div class="fl col-min">
                    <input type="text" class="tex_t" name="uname" placeholder="申请人" value="${search.uname}">
                </div>
                <div class="fl col-min">
                    <input type="text" class="tex_t" name="mobilephone" placeholder="申请手机号" value="${search.mobilephone}">
                </div>
                <div class="fl col-min">
                    <button type="button" class="btn btn-default content-search-btn">查询</button>
                </div>
            </div>
        </div>
    </form>
    <div class="btnBox">
        <button type="button" class="btn btn-default addBarcode">申请二维码</button>
        <!-- <button type="button" class="btn btn-default editBarcode">条码变更</button> -->
        <button type="button" class="btn btn-default jumpToOld">老版本</button>
    </div>
    <div class="table-style">
        <table class="dtable" id="trackTable">
            <thead>
            <tr>
                <th width="15%">申请人</th>
                <th width="15%">申请手机号</th>
                <th width="15%">申请时间</th>
                <!-- <th width="15%">企业名称</th> -->
                <th width="5%">申请数量</th>
                <!-- <th width="5%">已使用</th> -->
                <th width="23%">二维码开始编号</th>
                <th width="23%">二维码结束编号</th>
                <th width="15%">状态</th>
                <th width="15%">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${results.collection}" var="obj">
                <tr>
                    <td>${obj.unamezn}</td>
                    <td>${obj.mobilephone}</td>
                    <td><fmt:formatDate value="${obj.createtime}" pattern="yyyy-MM-dd hh:mm"/></td>
                   <%--  <td>${obj.companyName}</td> --%>
                    <td>${obj.number}</td>
                    <%-- <td>${obj.total}</td> --%>
                    <td>${obj.startNum}</td>
                    <td>${obj.endNum}</td>
                    <td id="${obj.id}printStatus">
                        <c:if test="${obj.printStatus == 0}">申请中</c:if>
                        <c:if test="${obj.printStatus == 1}">条码生成中</c:if>
                        <c:if test="${obj.printStatus == 2}">条码已生成</c:if>
                        <c:if test="${obj.printStatus == 3}">已下载</c:if>
                    </td>
                    <td>
                        <span class="others_val" resid="${obj.id}"></span>
                        <c:if test="${obj.printStatus == 0}">
                            <button type="button" class="btn btn-default BackToShipper btn_build">准备生成</button>
                        </c:if>
                        <c:if test="${obj.printStatus == 1}">正在生成条码</c:if>
                        <c:if test="${(obj.printStatus == 2 || obj.printStatus == 3) && obj.total > 0 }">
                            <button type="button" class="btn btn-default BackToShipper btn_download">下载条码</button>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <c:if test="${results.collection != null && fn:length(results.collection) > 0}">
            <someprefix:page pageNum="${results.pageNum}" pageSize="${results.pageSize}" pages="${results.pages}"
                             total="${results.total}"/>
        </c:if>
    </div>
</div>
<div class="add_barcode_panel" style="display: none">
    <div class="model-base-box edit_customer" style="width: 470px;">
        <div class="model-form-field">
            <label for="">二维码数量 :</label>
            <input name="add_number" type="number" min="1" max="10001">
        </div>
        <!-- <div class="model-form-field">
            <label for="">分配至 :</label>
            <select class="addSelect" name="companyId">
            </select>
        </div> -->
    </div>
</div>

<div class="edit_barcode_panel" style="display: none">
    <div class="model-base-box edit_customer" style="width: 470px;">
        <div class="model-form-field">
            <div style="text-align: center; color: #e60000;">请务必确认好，要变更的二维码区间!!!</div>
        </div>
        <%--<div class="model-form-field">--%>
            <%--<label style="width:106px;"><span style="color:red;">*</span> 条码所属 :</label>--%>
            <%--<select class="addSelect" name="modifycompanyId">--%>
            <%--</select>--%>
        <%--</div>--%>
        <div class="model-form-field">
            <label style="width:106px;"><span style="color:red;">*</span> 条码开始编号 :</label>
            <input name="startNum" type="text">
        </div>
        <div class="model-form-field">
            <label style="width:106px;"><span style="color:red;">*</span> 条码结束编号 :</label>
            <input name="endNum" type="text">
        </div>
        <div class="model-form-field">
            <label style="width:106px;"><span style="color:red;">*</span> 变更至 :</label>
            <select class="addSelect" name="companyId"></select>
        </div>

    </div>
</div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script src="${baseStatic}plugin/js/fileDownload.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${baseStatic}js/datetimepicker1.js"></script>
<script src="${baseStatic}plugin/comboSelect/jquery.combo.select.js"></script>
<script>
    $(document).ready(function () {
            $(".jumpToOld").on("click", function(){
                    window.location.href = base_url +  "/admin/barcode/search/old";
            })
			$(".content-search-btn").on("click",function(){
				 $("input[name='num']").val(1);
				 $("#searchWaybillForm").submit();
			})
            //翻页事件
            $(".pagination a").each(function (i) {
                $(this).on('click', function () {
                    $("form input[name='num']").val($(this).attr("num"));
                    $("#searchWaybillForm").submit();
                });
            });
            //下载事件
            $(".btn_download").on("click", function(){
                var obj = $(this).parents("td").find(".others_val");
                var resid = obj.attr("resid");
                $.util.json(base_url + '/admin/barcode/download/' + resid, null, function (response) {
                    if (response.success) {
                    	if(response.url){
                    		var msg = "二维码PDF文件下载,数量 : <font color='red'>" + response.count + "</font>个,大小 : <font color='red'>" + response.size + "</font>MB";
   							$.util.success(msg, function(){
   								$.fileDownload(response.url);
   								$("#"+ resid +"printStatus").html("已下载");
   							}, 3000);
   						}else{
   							$.util.error(response.message);
   						} 
                    } else {
                        $.util.error(response.message);
                    }
                });
            });
            //准备生成事件
            $(".btn_build").on("click", function(){
                var parent = $(this).parents("td");
                var obj = parent.find(".others_val");
                var parameters = {"appresid": obj.attr("resid")}
                $.util.json(base_url + '/admin/barcode/build/ready', parameters, function (data) {
                    if (data.success) {
                       parent.html("正在生成条码");
                    } else {
                        $.util.error(data.message);
                    }
                });
            });

            $(".addBarcode").on('click', function () {
            $.util.form('创建二维码', $(".add_barcode_panel").html(), null, function () {
                    var editBody = this.$body,parmas = {};
                    parmas.number = editBody.find("input[name='add_number']").val();
                    if (parmas.number <= 0 || null == parmas.number) {
                        $.util.error("请输入申请条码数");
                        return false;
                    }
                    $.util.json(base_url + '/admin/barcode/saveApplyResv2/', parmas, function (data) {
                        if (data.success) {
                            $.util.alert('操作提示', data.message, function () {
                                $("#searchWaybillForm").submit();
                            });
                        } else {
                            $.util.error(data.message);
                        }
                    });
                    return false;
                });
            });
        }
    
    )
    ;
</script>
</html>