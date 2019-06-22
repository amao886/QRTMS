<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>异常</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
<%@ include file="/views/include/head.jsp"%>
	<style type="text/css">
		.bindBox{
			display: table;
			width: 650px;
			height:100%;
			margin: 0 auto;
		}
		.context{
			margin:0 auto;
			width: 620px;
			height: 520px;
			display: table-cell;
			vertical-align: middle;
		}
		img{
			cursor: pointer;
			border: 1px #000000 solid;
		}
		.btn-custom {
			padding: 6px 10px !important;
			width: 145px;
		}
		form{
			border: #0C0C0C 1px solid;
			padding: 20px 10px;
		}
	</style>
</head>
<body>
<div class="bindBox">
	<div class="context">
		<div class="panel panel-danger">
			<div class="panel-heading">操作异常-${exception_time}</div>
			<div class="panel-body">
				<div class="center-block" style="text-align: center;">${exception_message}</div>
			</div>
		</div>
	</div>
</div>
	<%@ include file="/views/include/floor.jsp"%>
</body>
</html>