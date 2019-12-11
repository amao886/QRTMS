<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-条码分配</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}element-ui/lib/theme-chalk/index.css"/>
    <link rel="stylesheet" href="${baseStatic}css/inventory.css?times=${times}"/>
    <link rel="stylesheet" href="${baseStatic}bootstrap/bootstrap-select.min.css"/>
    <link rel="stylesheet" href="${baseStatic}css/jquery.editable-select.min.css"/>
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


        .tex_t{
            /* height: 26px; */
            line-height: 34px;
        }
        .btn-default{
            color: #333 !important;
            background-color: #fff !important;
            border: 1px #ccc solid !important;
        }
        .btn-primary{
            background: #31acfa !important;
            border: 0 !important;
        }
       .es-list {

           z-index: 99999999;
       }
        .model-form-field {
            position: relative;
        }
        #company {
            height: 34px;
            border-radius: 5px;
            border: 1px solid #ccc;
        }
    </style>
</head>
<body>
<div class="history-content clearfix">
    <form id="searchWaybillForm" class="form-inline" action="${basePath}/admin/barcode/search" method="post" style="margin-top: 10px; margin-bottom: 10px;">
        <input type="hidden" name="num" value="${results.pageNum}">
        <input type="hidden" name="size" value="10">
        <div class="form-group" >
            <select class="body-selectpicker" id="company" name="companyId" data-live-search="true" placeholder="企业名称">
                <option value="0" ${empty search.companyId ? "selected='selected'":'' } >企业名称</option>
                <c:forEach items="${companys}" var="company">
                    <option value="${company.id}" ${company.id == search.companyId ? "selected='selected'":'' } >${company.companyName }</option>
                </c:forEach>

            </select>

        </div>
        <div class="form-group">
            <input type="text" class="form-control" name="startNum" placeholder="二维码开始编号" value="${search.startNum}" >
            <input type="text" class="form-control" name="endNum" placeholder="二维码结束编号" value="${search.endNum}" >
        </div>
        <button type="button" class="btn btn-primary content-search-btn" @click="search">查询</button>
    </form>
    <div class="btnBox">
        <button type="button" class="btn btn-primary editBarcode">分配二维码</button>
    </div>
    <div class="table-style">
        <table class="dtable" id="trackTable">
            <thead>
            <tr>
                <th width="15%">企业名称</th>
                <th width="5%">申请数量</th>
                <th width="5%">已使用</th>
                <th width="23%">二维码开始编号</th>
                <th width="23%">二维码结束编号</th>
                <th width="10%">操作人</th>
                <th width="15%">操作时间</th>
               <!--  <th width="15%">状态</th>
                <th width="15%">操作</th> -->
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${results.collection}" var="obj">
                <tr>
                    <td>${obj.companyName}</td>
                    <td>${obj.number}</td>
                    <td>${obj.total}</td>
                    <td>${obj.startNum}</td>
                    <td>${obj.endNum}</td>
                    <td>${obj.unamezn}</td>
                    <td><fmt:formatDate value="${obj.createtime}" pattern="yyyy-MM-dd hh:mm"/></td>
                    <%-- <td id="${obj.id}printStatus">
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
                        <c:if test="${obj.printStatus == 2 || obj.printStatus == 3 }">
                            <button type="button" class="btn btn-default BackToShipper btn_download">下载条码</button>
                        </c:if>
                    </td> --%>
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

<div class="edit_barcode_panel" style="display: none" id="edit_barcode_panel">
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
            <select class="addSelect" name="companyId" id="editable-select"></select>


        </div>


        <%--<div class="model-form-field">--%>
            <%--<label style="width:106px;"><span style="color:red;">*</span> 条码所属 :</label>--%>
            <%--<select class="addSelect" name="modifycompanyId">--%>
            <%--</select>--%>
        <%--</div>--%>




    </div>
</div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script src="${baseStatic}plugin/js/fileDownload.js"></script>
<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${baseStatic}js/datetimepicker1.js"></script>
<script src="${baseStatic}plugin/comboSelect/jquery.combo.select.js"></script>
<script src="${baseStatic}plugin/js/fileDownload.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${baseStatic}js/datetimepicker1.js"></script>
<script src="${baseStatic}plugin/comboSelect/jquery.combo.select.js"></script>
<script type="text/javascript" src="${baseStatic}bootstrap/bootstrap-select.min.js"></script>
<script type="text/javascript" src="${baseStatic}js/jquery.editable-select.min.js"></script>


<script>
    $(document).ready(function () {


            // $('.body-selectpicker').selectpicker();


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
               var companyId;
            $(".editBarcode").on('click', function () {

                $.util.form('变更二维码', $(".edit_barcode_panel").html(), function () {
                    var editBody = this.$body;
                    $.util.json(base_url + '/admin/barcode/companyList',null,function (data) {
                        if (data.success) {
                            // companyList = data.companyList
                            // console.log(companyList)
                            editBody.find('.addSelect').html('');
                            var str = "<option value=''>请选择企业</option>";
                            $.each(data.companyList, function (i, item) {
                                str += "<option value='" + item.id + "'>" + item.companyName + "</option>";
                            });
                            editBody.find('.addSelect').append(str);
                            $('.addSelect').editableSelect({
                                effects: 'slide',
                                onSelect: function (element) {
                                    companyId= element[0].getAttribute('value');
                                }
                            });
                        }
                    });



                }, function () {

                    var parmas = {}, editBody = this.$body;
                    parmas.startNum = editBody.find("input[name='startNum']").val();
                    parmas.endNum = editBody.find("input[name='endNum']").val();
                    // parmas.companyId = editBody.find("select[name='companyId']").val();
                    parmas.companyId = companyId;
                    console.log(parmas.companyId)
                    if ("" == parmas.startNum || "" == parmas.endNum) {
                        $.util.error("开始编号或结束编号不能为空");
                        return false;
                    }
                    if ("" == parmas.companyId && parmas.companyId <= 0) {
                        $.util.error("请选择条码变更的企业");
                        return false;
                    }
                    $.util.json(base_url + '/admin/barcode/changeBarcode/', parmas, function (data) {
                        if (data.success) {
                            $.util.alert('操作提示', data.message, function () {
                            	$("#searchWaybillForm").submit();
                            	//window.location.href = base_url +  "/admin/barcode/all/search";
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