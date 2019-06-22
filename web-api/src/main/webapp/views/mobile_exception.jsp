<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <title>操作提示</title>
	<meta charset="utf-8">
	<meta name="renderer" content="webkit" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	<meta name="Keywords" content="合同物流管理平台运输管理平台">
	<meta name="Description" content="合同物流管理平台运输管理平台">
	<link href="http://libs.baidu.com/fontawesome/4.0.3/css/font-awesome.min.css" rel="stylesheet">
	
	<style>
		* {
			padding: 0;
			margin: 0;
		}
		
		.weui-msg {
			padding-top: 36px;
			text-align: center;
		}
		/*错误icon*/
		
		.weui-msg__icon-area {
			margin-bottom: 30px;
		}
		
		.weui-icon_msg {
			font-size: 93px;
		}
		
		.weui-icon_msg.weui-icon-warn {
			color: #f76260;
		}
		/*错误提示*/
		
		.weui-msg__text-area {
			margin-bottom: 25px;
			padding: 0 20px;
		}
		
		.weui-msg__title {
			margin-bottom: 5px;
			font-weight: 400;
			font-size: 20px;
		}
		
		.weui-msg__desc {
			font-size: 14px;
			color: #999;
		}
		/*错误按钮*/
		
		.weui-msg__opr-area {
			margin-bottom: 25px;
		}
		
		.weui-btn-area {
			margin: 1.17647059em 15px .3em;
		}
		
		.weui-btn {
			position: relative;
			display: block;
			margin-left: auto;
			margin-right: auto;
			padding-left: 14px;
			padding-right: 14px;
			box-sizing: border-box;
			font-size: 18px;
			text-align: center;
			text-decoration: none;
			color: #fff;
			line-height: 2.55555556;
			border-radius: 5px;
			-webkit-tap-highlight-color: rgba(0, 0, 0, 0);
			overflow: hidden;
		}
		
		.weui-btn:after {
			content: " ";
			width: 200%;
			height: 200%;
			position: absolute;
			top: 0;
			left: 0;
			border: 1px solid rgba(0, 0, 0, .2);
			-webkit-transform: scale(.5);
			transform: scale(.5);
			-webkit-transform-origin: 0 0;
			transform-origin: 0 0;
			box-sizing: border-box;
			border-radius: 10px;
		}
		
		.weui-btn_primary:not(.weui-btn_disabled):visited {
			color: #fff;
		}
		
		.weui-btn_primary {
			background: #31acfa;
		}
		
		.weui-btn_default:not(.weui-btn_disabled):visited {
			color: #000;
		}
		
		.weui-btn+.weui-btn {
			margin-top: 15px;
		}
		
		.weui-btn_default {
			color: #000;
			background-color: #f8f8f8;
		}
		
		.weui-btn_primary:not(.weui-btn_disabled):active {
			background: #2d9ee6;
		}
		
		.weui-btn_default:not(.weui-btn_disabled):active {
			color: rgba(0, 0, 0, .6);
			background-color: #dedede;
		}

	</style>
</head>
<body>
<c:if test="${!prompt_exception}">
	<div class="weui-msg">
		<div class="weui-msg__icon-area"><i class="fa fa-exclamation-circle weui-icon-warn weui-icon_msg"></i></div>
		<div class="weui-msg__text-area">
			<h2 class="weui-msg__title">${exception_message}</h2>
			<p class="weui-msg__desc">${exception_date}</p>
			<!--
	        <p class="weui-msg__desc">${exception_url}</p>
	        <p class="weui-msg__desc">${exception_parameter}</p>
	      	 -->
		</div>
		<div class="weui-msg__opr-area">
			<p class="weui-btn-area">
				<!-- <a href="javascript:void(0);" onclick="GoBack()" class="weui-btn weui-btn_primary">关闭页面</a> -->
				<a href="${front_index}" class="weui-btn weui-btn_default">返回首页</a>
			</p>
		</div>
	</div>
</c:if>
<c:if test="${prompt_exception == true}">
	<!-- <div class="weui-msg__icon-area"><i class="fa fa-check"></i></div> -->
	<div class="weui-msg">
		<div class="weui-msg__text-area">
			<h2 class="weui-msg__title">${exception_message}</h2>
		</div>
	</div>
</c:if>
</body>
<script>
    $(document).ready(function () {

    });
</script>
</html>