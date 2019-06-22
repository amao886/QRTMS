<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>运营后台-签收管理</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/history.css?times=${times}"/>
    <style>
    .assignBtn{color:#31acfa;cursor: pointer;}
    .model-base-box .model-form-field>label{width:90px;}
    </style>
</head>
<body>
<div class="history-content clearfix">
    <form id="searchSignedForm" action="${basePath}/enterprise/backstage/companySigned/manage/search" method="get">
        <input type="hidden" id="num" name="pageNum" value="${param.pageNum}">
        <input type="hidden" id="size" name="pageSize" value="10">
        <div class="content-search">
            <div class="clearfix">
                <div class="fl col-min">
                    <label class="labe_l">企业名称:</label>
                    <input type="text" class="tex_t" placeholder="企业名称" name="companyName" value="${companyName}">
                </div>
                <div class="fl col-min">
                	<a href="javascript:;" class="content-search-btn search-btn" style="top:50px;">查询</a>
                </div>
            </div>
        </div>
    </form>
    <div class="table-style">
        <table class="dtable" id="signNumTable">
            <thead>
            <tr>
                <th>企业名称</th>
                <th>剩余签署份数</th>
                <th>合计签署份数</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <c:forEach items="${page.collection}" var="sign"  >
                    <tr>
                        <td>${sign.companyName}</td>
                        <td>${sign.signRest}</td>
                        <td>${sign.signTotal}</td>
                        <td>
                            <span class="assignBtn" data-total="${sign.signTotal}" data-id="${sign.id}">赠送可签署份数</span>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <c:if test="${data.collection != null && fn:length(data.collection) > 0}">
            <someprefix:page pageNum="${data.pageNum}" pageSize="${data.pageSize}" pages="${data.pages}"
                             total="${data.total}"/>
        </c:if>
    </div>
</div>

<!-- 赠送可签署份数  弹窗 -->
<div class="assign_num_panel" style="display:none;">
    <div class="model-base-box assign_num" style="width:400px;height: auto;">      
        <div class="model-form-field">
            <label>累积赠送次数:</label>
            <span class="totalNum"></span>次
        </div>
        <div class="model-form-field">
            <label>赠送次数:</label>
            <input type="text" name="presentNum"onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                   onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'0')}else{this.value=this.value.replace(/\D/g,'')}"
                   style="width:60%"/>
            <span>份</span>
        </div>
    </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script>
    $(document).ready(function () {
        //提交查询事件
        $(".search-btn").on("click", function () {
            $("form input[name='pageNo']").val(1);
            $("#searchSignedForm").submit();
        });

        //翻页事件
        $(".pagination a").each(function (i) {
            $(this).on('click', function () {
                $("form input[name='pageNum']").val($(this).attr("num"));
                $("#searchSignedForm").submit();
            });
        });
        
        $("#signNumTable").on("click",".assignBtn",function(){
        	var $this = $(this),
        		_total = $this.attr("data-total"),
                _id = $this.attr("data-id");
        	$.util.form('赠送可签署份数', $(".assign_num_panel").html(), function () {
        		var editBody = this.$body
        		editBody.find(".totalNum").text(_total);
            }, function () {
            	var editBody = this.$body,
                    presentNum = editBody.find("input[name=presentNum]").val();
                $.ajax({
                    type: "POST",
                    url: base_url + "/enterprise/backstage/companySigned/presentNum",
                    data: {id:_id,presentNum:presentNum},
                    dataType: "json",
                    success: function (response) {
                        if (response.success) {
                            $("#searchSignedForm").submit();
                        } else {
                            $.util.error(response.message);
                        }
                    }
                });
            });
        });
    });



</script>
</html>