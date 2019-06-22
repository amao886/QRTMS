<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <title>企业管理-企业添加</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/businessadd.css?times=${times}"/>
    <style type="text/css">
        

    </style>
</head>
<body>
<div class="custom-content clearfix">
    <form id="searchCustomerForm" action="" method="post">
        <ul class="formUl">
        	<li>
        		<label><em>*</em>企业名称：</label>
        		<div class="inputBox">
        			<input type="text" name="companyName" />
        		</div>
        	</li>
        	<li>
        		<label><em>*</em>组织机构代码：</label>
        		<div class="inputBox">
        			<input type="text" name="organizationCode" />
        		</div>
        	</li>
        	<%--<li>--%>
        		<%--<label><em>*</em>营业执照：</label>--%>
       			<%--<div class="inputBox imgUpBox">--%>
                       <%--<div class="imgboxCon">--%>
                          <%--<div class="imgShowBox" datatype="upImg" errormsg="图片格式错误" nullmsg="图片不能为空">--%>
                             <%--<img id="img1" src="" />--%>
                          <%--</div>--%>
                          <%--<div class="imgOprateBox">--%>
                               <%--<input type="file" class="imgInput" name="file" id="upFile" onchange="up(this)"/>--%>
                               <%--<a class="imgUpBtn"></a>--%>
                               <%--<a class="imgDelBtn">删除</a>--%>
                          <%--</div>--%>
                       <%--</div>--%>
                   <%--</div>--%>
        	<%--</li>--%>
        	<li>
        		<label><em>*</em>您的姓名：</label>
        		<div class="inputBox">
        			<input type="text" name="realName" id="realName" value="${legalize.employeeName}"/>
        		</div>
        	</li>
        	<li>
        		<label><em>*</em>您的手机：</label>
        		<div class="inputBox">
        			<input type="text" name="mobilePhone" id="mobilePhone" value="${legalize.mobilePhone}" />
        		</div>
        	</li>
        	<li>
        		<a id="submitForm" class="btn btn-default" onclick="saveForm()" style="margin-left: 124px;">提交注册信息</a>
        	</li>
        </ul>
    </form>

</div>

</body>
<%@ include file="/views/include/floor.jsp" %>
<script src="${baseStatic}plugin/upload/viewImgs.js?times=${times}"></script>
<script>
$(function(){
	
});


$(".imgUpBox").on("change",".imgInput",function(){//上传图片的逻辑是：上传失败依然会执行change事件	   
	var _img = $(this).parents(".imgboxCon").find("img").attr("src");
	if(_img != ""){//图片上传成功
		$(this).next().hide().next().show();
		$(this).parents(".imgboxCon").find(".imgShowBox").removeClass("Validform_error");	
		console.log( $('input[name=file]')[0].files[0])
    }  
});

$(".imgUpBox").on("click",".imgDelBtn",function(){
	var _ipt = $("#upFile").clone().val("");
	$(this).hide().prev().show();
	$("#upFile").remove();
	$(".imgOprateBox").prepend(_ipt);
	console.log( $('input[name=file]')[0].files[0])
});


//上传图片
function up(obj){
	$(obj).viewImg({
		 'sizeImg':'3',
		 'upImg':$("#img1")
	}); 
}

//提交审核
function saveForm(){
	var companyName = $('input[name = companyName]').val(),
		organizationCode = $('input[name = organizationCode]').val(),
        realName = $('input[name = realName]').val(),
        mobilePhone = $('input[name = mobilePhone]').val(),
		file = $('input[name = file]')[0].files[0];
		console.log(file);
	if($.trim(companyName) ==""){
		$.util.error("企业名称不能为空");
		return false;
	}

	if($.trim(organizationCode) ==""){
		$.util.error("组织机构代码不能为空");
		return false;
	}

	if(!file || $("#img1").attr("src") == ""){
		$.util.error("营业执照不能为空");
		return false;
	}

	if(realName == ""){
        $.util.error("姓名不能为空");
        return false;
    }else if(!/^[\u4e00-\u9fa5]{2,10}$/.test(realName)){
        $.util.error("姓名请输入2-10位汉字");
        return false;
    }

    if(mobilePhone == ""){
        $.util.error("手机号不能为空");
        return false;
    }else if(!/^[1]{1}[0-9]{10}$/.test(mobilePhone)){
        $.util.error("手机号码格式输入错误");
        return false;
    }
			
	var formData = new FormData();
	formData.append("companyName", companyName);
	formData.append("companyCode", organizationCode);
	formData.append("realName", realName);
	formData.append("mobilePhone", mobilePhone);
	formData.append("file", file);
	$.ajax({
        url: base_url + "/enterprise/company/save",
        type: 'POST',
        data: formData,
        processData: false, // 告诉jQuery不要去处理发送的数据
        contentType: false,// 告诉jQuery不要去设置Content-Type请求头
        success: function (response) {
            if (response.success) {//处理返回结果
                $.util.success(response.message,function(){
                    window.location.href= base_url + "/enterprise/company/view/manage";
                });
            }else{
                $.util.error(response.message);
            }

        }
    });
};


</script>

</html>