<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <title>企业管理-企业列表</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/page.css?times=${times}"/>
    <link rel="stylesheet" href="${baseStatic}css/businessmanage.css?times=${times}"/>
    <style type="text/css">
        
    </style>
</head>
<body>
<div class="custom-content clearfix">
    <h2 class="mt10">企业管理</h2>
	<div>
		<div class="noList hide"><a href="${basePath}/backstage/electronic/loadBusinessAdd">+添加企业信息</a></div>
	  	<div class="manageCon">
	  		<a class="btn btn-default mt20">添加企业</a>
	  		<div class="company clearfix">
	  			<div class="titCon">
	  				<h3>九阳集团</h3>
	  				<a href="${basePath}/backstage/electronic/loadOpenElectronic" class="blue">开通电子签收</a>
	  			</div>
	  			<p>
	  				<a class="addPerson">添加企业人员</a>
	  			</p>
	  			<ul class="memberList">
	  				<li>
	  					<div>
	  						<p class="cName">王小二</p>
	  						<p>13856874596</p>
	  						<p class="text-r">
	  							<a class="revoke">取消签章授权</a>
	  							<a class="delete">删除</a>
	  						</p>
	  					</div>
	  				</li>
	  				<li>
	  					<div>
	  						<p class="cName">王小二</p>
	  						<p>13856874596</p>
	  						<p class="text-r">
	  							<a class="">签章授权</a>
	  							<a class="delete">删除</a>
	  						</p>
	  					</div>
	  				</li>
	  			</ul>
	  		</div>
	  	</div>
	</div>
</div>

<!-- 添加企业人员  弹窗 -->
<div class="add_member_panel" style="display:none;">
    <div class="model-base-box add_member" style="width:500px;height: auto;">      
        <div class="model-form-field">
            <label>姓名:</label>
            <input type="text" name="name" style="width:61%"/>
        </div>
        <div class="model-form-field">
            <label>手机号码:</label>
            <input type="tel" name="mobile" style="width:61%"/>
        </div>
    </div>
</div>

</body>
<%@ include file="/views/include/floor.jsp" %>

<script>

//添加企业人员
$(".addPerson").on('click', function () {
    $.util.form('添加企业人员', $(".add_member_panel").html(), function () {
    	console.log(222)
        
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


//取消签章授权
$(".revoke").on("click", function () {
	var _cName = $(this).parent().siblings(".cName").text()
	var _str = "确定取消"+ _cName +"的签章授权？<br>取消后Ta将不可再进行电子签收。"
    $.util.confirm("取消签章授权", _str, function () {
        var parmas = {"customerId": id, "groupId": groupId};
        $.util.json(base_url + '/backstage/customer/delete', parmas, function (data) {
            if (data.success) {
                $.util.alert('操作提示', data.message, function () {
                    $("#searchCustomerForm").submit();
                });
            } else {
                $.util.error(data.message);
            }
        });
    });
})

</script>

</html>