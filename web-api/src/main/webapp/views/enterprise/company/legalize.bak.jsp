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
	<form id="searchCustomerForm" action="" method="post">
		<ul data-id="" class="formUl">
			<li>
				<label><em>*</em>企业名称：</label>
				<div class="inputBox">
					<input type="text" id="companyName" name="companyName" v-model="" readonly/>
				</div>
			</li>
			<li>
				<label><em>*</em>组织机构代码：</label>
				<div class="inputBox">
					<input type="text" id="companyCode" name="companyCode" />
				</div>
			</li>
			<li>
				<label><em>*</em>营业执照：</label>
				<div class="inputBox imgUpBox">
					<div class="imgboxCon">
						<div class="imgShowBox" datatype="upImg" errormsg="图片格式错误" nullmsg="图片不能为空">
							<img id="img_license" src="" />
						</div>
						<div class="imgOprateBox">
							<input type="file" class="imgInput" name="license" id="file_license" onchange="up(this)"/>
							<a class="imgUpBtn"></a>
							<a class="imgDelBtn">删除</a>
						</div>
					</div>
				</div>
			</li>
			<li>
				<label><em>*</em>上传公章：</label>
				<div class="inputBox imgUpBox">
					<div class="imgboxCon">
						<div class="imgShowBox" datatype="upImg" errormsg="图片格式错误" nullmsg="图片不能为空">
							<img id="img_seal" src="" />
						</div>
						<div class="imgOprateBox">
							<input type="file" class="imgInput" name="seal" id="file_seal" onchange="up(this)"/>
							<a class="imgUpBtn"></a>
							<a class="imgDelBtn">删除</a>
						</div>
					</div>
				</div>
			</li>
			<!--
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
            </li> -->
			<li>
				<label class="agreeLabel">
					<input type="checkbox" id="checkbox_agreement" name="agreement">我已阅读并同意
					<a href="${basePath}/backstage/electronic/loadAgreement">《合同物流管理平台平台电子签收服务协议》</a>
				</label>
			</li>
			<li>
				<a id="submitForm" class="btn btn-default" style="margin-left: 124px;">提交认证信息</a>
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
        $.ajax({
            url: base_url + "/enterprise/company/getCompanyByUserKey",
            type: 'GET',
            processData: false, // 告诉jQuery不要去处理发送的数据
            contentType: false,// 告诉jQuery不要去设置Content-Type请求头
            success: function (response) {
                if (response.success && null != response.results) {//处理返回结果
                    $(".formUl").attr("data-id",response.results.id);
                    $('input[name=companyName]').val(response.results.companyName);
                    $('input[name=companyCode]').val(response.results.companyCode);
                    if(response.results.licensePath != null){
                        $("#img_license").attr("src", response.imagePath +"/"+ response.results.licensePath);
                        $('input[name=license]').next().hide().next().show();
                    }
                    if(response.seal != null){
                        $("#img_seal").attr("src", response.imagePath +"/"+ response.seal.sealData);
                        $('input[name=seal]').next().hide().next().show();
                    }
                    $("#submitForm").attr('disabled',"true");

                }else{
                    $.util.error(response.message);
                }
            }
        });
    });

    $("#checkbox_agreement").on("change", function(){//上传图片的逻辑是：上传失败依然会执行change事件
        var _this = $(this);
        if(_this.is(':checked')){
            $("#submitForm").removeAttr("disabled");
        }else{
            $("#submitForm").attr('disabled',"true");
        }
    });

    $(".imgUpBox").on("change",".imgInput",function(){//上传图片的逻辑是：上传失败依然会执行change事件
        var _img = $(this).parents(".imgboxCon").find("img").attr("src");
        if(_img != ""){//图片上传成功
            $(this).next().hide().next().show();
            $(this).parents(".imgboxCon").find(".imgShowBox").removeClass("Validform_error");
            //console.log( $('input[name=file]')[0].files[0])
        }
    });

    $(".imgUpBox").on("click",".imgDelBtn",function(){
        var _ipt = $("#upFile").clone().val("");
        $(this).hide().prev().show();
        $("#upFile").remove();
        $(".imgOprateBox").prepend(_ipt);
        if($(this).prev().prev().attr("name") == "license"){
            $("#img_license").attr("src","");
        }
        if($(this).prev().prev().attr("name") == "seal"){
            $("#img_seal").attr("src","");
        }
        //console.log( $('input[name=file]')[0].files[0])
    });


    //上传图片
    function up(obj){
        $(obj).viewImg({
            'sizeImg':'3',
            'upImg':$("#img_"+obj.name)
        });
    }
    //提交审核
    $('#submitForm').on('click', function(){
        if($(this).attr('disabled')){ return false; }
        var formData = new FormData();
        var id = $(".formUl").attr("data-id"),
            companyName = $('input[name=companyName]').val(),
            organizationCode = $('input[name=companyCode]').val(),
            //bankAccount = $('input[name=bankAccount]').val(),
            //bank = $('input[name=bank]').val(),
            license = $('input[name=license]')[0].files[0],
            seal = $('input[name=seal]')[0].files[0];
        if($.trim(companyName) ==""){
            $.util.error("企业名称不能为空");
            return false;
        }
        if($.trim(organizationCode) ==""){
            $.util.error("组织机构代码不能为空");
            return false;
        }
        if(!$("#img_license").attr("src")){
            $.util.error("企业营业执照不能为空");
            return false;
        }
        if(!$("#img_seal").attr("src")){
            $.util.error("企业电子公章不能为空");
            return false;
        }
        formData.append("id", id);
        formData.append("companyName", companyName);
        formData.append("companyCode", organizationCode);
        formData.append("license", license);
        formData.append("seal", seal);
        $.ajax({
            url: base_url + "/enterprise/backstage/authentication",
            type: 'POST',
            data: formData,
            processData: false, // 告诉jQuery不要去处理发送的数据
            contentType: false,// 告诉jQuery不要去设置Content-Type请求头
            success: function (response) {
                if (response.success) {//处理返回结果
                    $.util.success(response.message, function(){
                        window.location.href = base_url + "/enterprise/company/view/manage";
                    }, 3000);
                }else{
                    $.util.error(response.message);
                }
            }
        });
    });
</script>

</html>