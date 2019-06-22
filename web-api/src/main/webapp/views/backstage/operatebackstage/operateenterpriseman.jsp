<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>运营后台-企业管理</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/history.css?times=${times}"/>
    <style>
    .oprateBtn{color:#31acfa;cursor: pointer;}
    .model-base-box .model-form-field>label{width:90px;}
    </style>
</head>
<body>
<div class="history-content clearfix">
    <form id="searchDailyForm" action="" method="get">

        <input type="hidden" id="num" name="pageNum" value="${param.pageNum}">
        <input type="hidden" id="size" name="pageSize" value="10">
        <div class="content-search">
            <div class="clearfix">
                <div class="fl col-min">
                    <label class="labe_l">企业名称:</label>
                    <input type="text" class="tex_t" placeholder="任务单号/送货单号" name="orderNumber">
                </div>
                <div class="fl col-min">
                    <label class="labe_l">企业名称:</label>
                    <select class="tex_t selec_t" name="companyName" id="companyName">
                    	<option value="">是否认证</option>
                    	<option value="1">是</option>
                    	<option value="0">否</option>
                    </select>
                </div>
                <div class="fl col-min">
                    <label class="labe_l">认证权限:</label>
                    <select class="tex_t selec_t" name="permission" id="permission">
                    	<option value="">是否开通认证权限</option>
                    	<option value="1">是</option>
                    	<option value="0">否</option>
                    </select>
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
                <th>是否认证</th>
                <th>是否分配电子签收认证权限</th>
                <th>注册时间</th>
                <th>认证时间</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <tr>
                    <td>九阳集团</td>
                    <td>是</td>
                    <td>是</td>
                    <td>2017-12-30 17:52:35</td>
                    <td>2012-02-12 13:32:26</td>
                    <td>
                    	<a class="oprateBtn viewBtn">查看</a>
                    </td>
                </tr>
                <tr>
                    <td>蒙牛集团</td>
                    <td>是</td>
                    <td>否</td>
                    <td>2017-12-30 17:52:35</td>
                    <td>2012-02-12 13:32:26</td>
                    <td>
                    	<a class="oprateBtn assignRights">分配电子签收认证权限</a>
                    	<a class="oprateBtn viewBtn">查看</a>
                    </td>
                </tr>
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
            <input type="text" name="mobile" style="width:60%"/>
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
            $("#searchDailyForm").submit();
        });

        //翻页事件
        $(".pagination a").each(function (i) {
            $(this).on('click', function () {
                $("form input[name='pageNum']").val($(this).attr("num"));
                $("#searchDailyForm").submit();
            });
        });
        
        $("#signNumTable").on("click",".assignBtn",function(){
        	var $this = $(this),
        		_total = $this.attr("data-total");
        	$.util.form('赠送可签署份数', $(".assign_num_panel").html(), function () {
        		var editBody = this.$body
        		editBody.find(".totalNum").text(_total);
            }, function () {
            	var editBody = this.$body,
            		_name = editBody.find("input[name=name]").val(),
            		_mobile = editBody.find("input[name=mobile]").val()
            	if($.trim(_name) == ""){
            		return false;
            	}
            	if(!/^1[\d]{10}$/.test(_mobile)){
            		return false;
            	}
            	alert(111);
            });
        });
    });



</script>
</html>