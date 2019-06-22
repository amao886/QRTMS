<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <title>企业管理-企业认证</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/businessadd.css?times=${times}"/>
    <style type="text/css">
        

    </style>
</head>
<body>
<div class="custom-content clearfix">
    <form id="searchCustomerForm" action="${basePath}/backstage/customer/search" method="post">
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
        	<li>
        		<label><em>*</em>营业执照：</label>
       			<div class="inputBox imgUpBox">
                       <div class="imgboxCon">
                          <div class="imgShowBox" datatype="upImg" errormsg="图片格式错误" nullmsg="图片不能为空">
                             <img id="img1" src="" />
                          </div>
                          <div class="imgOprateBox">
                               <input type="file" class="imgInput" name="file" id="upFile" onchange="up(this)"/>
                               <a class="imgUpBtn"></a>
                               <a class="imgDelBtn">删除</a>
                          </div>
                       </div>
                   </div>
        	</li>
        	<li>
        		<label>对公银行账户：</label>
        		<div class="inputBox">
        			<input type="text" name="bankAccount" id="bankAccount" />
        		</div>
        	</li>
        	<li>
        		<label>开户行：</label>
        		<div class="inputBox">
        			<input type="text" name="bank" id="bank" />
        		</div>
        	</li>
        	<li>
        		<label class="agreeLabel">
        			<input type="checkbox" name="agreement" value="1" checked="true">我已阅读并同意
        			<a href="${basePath}/backstage/electronic/loadAgreement">《合同物流管理平台平台电子签收服务协议》</a>
        		</label>
        	</li>
        	<li>
        		<a id="submitForm" class="btn btn-default" onclick="saveForm()" style="margin-left: 124px;">提交认证信息</a>
        	</li>
        	
        </ul>
    </form>

</div>

</body>
<%@ include file="/views/include/floor.jsp" %>
<script src="${baseStatic}plugin/upload/viewImgs.js?times=${times}"></script>
<script src="${baseStatic}plugin/upload/ajaxfileupload.js?times=${times}"></script>
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
	var companyName = $('input[name=companyName]').val(),
		organizationCode = $('input[name=organizationCode]').val(),
		bankAccount = $('input[name=bankAccount]').val(),
		bank = $('input[name=bank]').val(),
		file= $('input[name=file]')[0].files[0]
	if($.trim(companyName) ==""){
		return false;
	}
	if($.trim(organizationCode) ==""){
		return false;
	}
	if(!file){
		return false;
	}
			
	var formData = new FormData();
	formData.append("companyName", companyName);
	formData.append("organizationCode", organizationCode);
	formData.append("bankAccount", bankAccount);
	formData.append("bank", bank);
	formData.append("file", file);
	$.ajax({
        url: base_url + "/backstage/coordination/save/exception",
        type: 'POST',
        data: formData,
        processData: false, // 告诉jQuery不要去处理发送的数据
        contentType: false,// 告诉jQuery不要去设置Content-Type请求头
        success: function (response) {
            if (response.success) {//处理返回结果
                $.util.success(response.message);
            }else{
                $.util.error(response.message);
            }
        }
    });
};


</script>

</html>