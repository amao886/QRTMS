<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>物流跟踪-发货计划</title>
<%@ include file="/views/include/head.jsp"%>
<link rel="stylesheet" href="${baseStatic}css/page.css?times=${times}" />
<link rel="stylesheet" href="${baseStatic}css/delivery.css" />
<link rel="stylesheet" href="${baseStatic}css/switch.css" />
</head>
<body>
<%-- 	<div class="track-content clearfix">
		发货计划  <a href="${basePath}/backstage/schedule/detail/1111">编辑</a>
    </div> --%>
    <div class="custom-content">
	    <div class="crumbs-nav">
	                       当前位置:<span><a href="javascript:;">发货计划</a></span>
	    </div>
	    <div class="content-search">
	        <div class="clearfix">
	            <div class="fl col-middle">
	                <label class="labe_l">资源组名:</label>
	                <select class="tex_t selec_t">
	                    <option value="">金发项目组</option>
	                    <option value="">蒙牛项目组</option>
	                    <option value="">九阳项目组</option>
	                </select>
	            </div>
	            <div class="fl col-middle status-hook" style="display: none;">
	                <label class="labe_l">电子围栏:</label>
	                <select class="tex_t selec_t">
	                    <option value="">请选择电子围栏开关</option>
	                    <option value="">开</option>
	                    <option value="">关</option>
	                </select>
	            </div>
	            <div class="fl col-middle status-hook" style="display: none;">
	                <label class="labe_l">发货状态:</label>
	                <select class="tex_t selec_t">
	                    <option value="">请选择发货状态</option>
	                    <option value="">已绑定</option>
	                    <option value="">已作废</option>
	                </select>
	            </div>
	            <div class="fl col-max-input overhidden">
	                <input type="text" class="text-exception" placeholder="请输入客户名称筛选">
	                <span class="max-input-search"></span>
	            </div>
	            <div class="fl col-xs-min">
	                <a href="javascript:;" class="more" id="more">» 更多</a>
	            </div>
	        </div>
	    </div>
	    <div class="table-style">
	    	<div class="btn-box">
	    		<a href="javascript:;" class="aModel model-download">模板下载</a>
	    		<a href="javascript:;" class="aModel model-upload">批量上传</a>
	    	</div>
	        <table>
	            <thead>
	                <tr>
	                    <th width="50">序号</th>
                        <th>项目组</th>
                        <th>收货客户</th>
                        <th width="80">重量(kg)</th>
                        <th width="80">体积(m³)</th>
                        <th width="80">数量(件)</th>
                        <th>收货地址</th>
                        <th width="90">联系人</th>
                        <th width="95">联系电话</th>
                        <th>要求到货时间</th>
                        <th width="100">电子围栏</th>
                        <th width="80">发货状态</th>
                        <th>操作</th>
	                </tr>
	            </thead>
	            <tbody>
	                <tr>
	                    <td>1</td>
	                    <td>金发</td>
	                    <td>蒙牛集团</td>
	                    <td>80</td>
	                    <td>80</td>
	                    <td>80</td>
	                    <td>上海杨浦靖宇中路</td>
	                    <td>老王</td>
	                    <td>18326458898</td>
	                    <td>发货后的第1天2点前</td>
	                    <td>
	                    	<label for="switchCP" class="switch-cp">
                                <input id="switchCP" class="switch-cp__input" type="checkbox" checked="checked">
                                <div class="switch-cp__box"></div>
                            </label>
                        </td>
	                    <td>已作废</td>
	                    <td>
	                        <a href="${basePath}/backstage/schedule/detail/1111" class="aHandle aEdit">编辑</a>
	                        <a href="javascript:;" class="aHandle aCancel">作废</a>
	                    </td>
	                </tr>
	            </tbody>
	        </table>
	    </div>
	    <!--  批量上传弹出框 -->
	    <div class="upload-dialog" style="display:none;">
            <div class="upload-dialog-con" style="width:500px;height: auto;">
                <div class="upload-dialog-item">
                    <label class="labe_l">项目组名：</label>
                    <select class="selec_t">
                        <option value="">项目组1</option>
                        <option value="">项目组1</option>
                        <option value="">项目组1</option>
                    </select>
                </div>
                <div class="upload-dialog-item">
                    <label class="labe_l">选择文件：</label>
                    <div class="input-item">
                        <input type="text" class="tex_t" placeholder="请点击选择上传文件" id="textfield">
                        <input type="file" class="fil_e" onchange="document.getElementById('textfield').value=this.value">
                    </div>    
                </div>
            </div>  
        </div>
	</div>
</body>
<%@ include file="/views/include/floor.jsp"%>
<script src="${baseStatic}plugin/js/fileDownload.js"></script>
<script src="${baseStatic}plugin/js/jquery.cookie.js"></script>
<script src="${baseStatic}plugin/js/jquery.mloading.js"></script>
<script>
$(document).ready(function(){
	var _cookie = $.cookie("dOpen");   
	
	//刷新页面时记录日期是否展开
   if(_cookie != undefined){
    	if(_cookie == 1){
    		$('.status-hook').show();
    		$('#more').html('« 收起');
    	}else{
    		$('.status-hook').hide();
    		$('#more').html('» 更多');
    	}
    } 
	//点击展开隐藏选项
	$('#more').on('click',function(){
        if($('.status-hook').is(':hidden')){
            $('.status-hook').show();
            $(this).html('« 收起');
            $.cookie("dOpen","1");
        }else{
            $('.status-hook').hide();
            $(this).html('» 更多');
            $.cookie("dOpen","0");
        }
    })
    
    //点击批量上传按钮
    $(".model-upload").on('click', function(){
    	$.util.form('批量上传', $(".upload-dialog").html(), function(model){
    		
    	})
    })
    
    //点击作废按钮
    $(".aCancel").on('click', function(){
    	$.util.confirmWarning("提示", "您确定要对此发货单作废吗？", destoryBill);
    })
    
    // 作废发货单
    function destoryBill(){
		$.alert('已作废');
	}
    
    //下载模板
    $(".model-download").click(function(){
    	var url ="${basePath}/backstage/download?p=FFC0274EC1E681B1A766F62D173958A3FE97B1954899CBF0F492DBF3934729FE76C003A74CB4419248FF023052F26CA6";
    	$.fileDownload(url);
    });
    
});
</script>
</html>