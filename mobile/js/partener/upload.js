var uploadId = _common.getUrlParam('waybillId') || '';
var showClose = _common.getUrlParam('showClose');
console.log(localStorage.getItem("clickTag"));



//已明白图片要求按钮
$('.imgTipsBtn').on('click',function(){
	$('.showTaskList,.upload,.demos-content-padded').show();
	$('.img_tips').hide();
	addImage();
})


//初始化加载任务单详情
if(uploadId == ''){ //从首页进入，没有任务单id
	$('#upload-task-title').show();
}else{
	getTaskDetail();	
}

//点击添加图片
$('#uploaderRecipt').click(function(){	
	addImage();		
})

//点击跳转到选择任务单页面
$('#toSearchList').click(function(){
	window.location.href = './searchPositionList.html';
})

//点击调微信扫一扫接口
$('#up-task-icon').click(function(){
	uploadScanCode();
})

//点击x,重新选择任务单
$('#uploadTaskClose').click(function(){
	//显示二维码扫码接口
	$('.upload-task').removeClass('cancelPadding');
	$('#upload-task-title').show();
	$('#show-task-list').hide().find('.upload-task-content').html('');		
})

//点击添加图片
function addImage() {
	var len = $('#uploadTaskContent').children().length;
	
	if(len == 0){
		$.toast('请先选择任务单','text');
		
	}else if(!localStorage.getItem('clickTag')){
		//第一次点击
		$('.showTaskList,.upload,.demos-content-padded').hide();
		$('.img_tips').show();
		localStorage.setItem("clickTag","1");
		
	}else{
		chooseImage(images.localId);
	}
}

//获取任务单详情
function getTaskDetail(){
	EasyAjax.ajax_Get_Json({
		url:'mobile/wayBill/deatil/getWayBill?id='+uploadId
	},function(res){
		$('.upload-task').addClass('cancelPadding');
		if(showClose){
			$('#uploadTaskClose').show();
		}else{
			$('#uploadTaskClose').hide();
		}
		var showTaskHtml = $('#showTask').render(res);
		$('#uploadTaskContent').html(showTaskHtml);

	});
}

//调用扫一扫接口
function uploadScanCode(){
	wx.scanQRCode({
	    needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
	    scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
	    success: function (res) {
	    	var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
	    	var code = '';
	    	//alert(result);
	    	//要判断是新条码还是旧条码
	    	if(result.indexOf('scan')> -1){  //是新条码
	    		var start = result.lastIndexOf('/')+1;
	    		var end = result.lastIndexOf('?');
	    		code = result.substring(start,end);
	    		getScanCode(code);
	    		
	    	}else if(result.indexOf('code')> -1){ //旧条码	
	    		code = getWXParam('code',result);
	    		getScanCode(code);
	    	}else{
	    		$.toast("无效条码","cancel");
	    	}
	    	
		}
	});
}

//获取扫码返回信息中的参数
function getWXParam(name,url){
	var num = url.indexOf('?');
	var str = url.substring(num+1);
	var reg  = new RegExp('(^|&)' + name + '=([^&]*)(&|$)');
	var result = str.match(reg);
	return result ? decodeURIComponent(result[2]) : null;	
}


//扫码后调用接口
function getScanCode(code){
	EasyAjax.ajax_Get({
		url:'mobile/wayBill/deatil/getWayBill?barcode='+code
	},function(res){
		$('#upload-task-title').hide(); //扫码成功后隐藏再次扫码
		$('#uploadTaskContent').html('');
		$('#show-task-list').show(); //显示内容框
		$('#uploadTaskClose').show(); //删除显示
		var showTaskHtml = $('#showTask').render(res);
		$('#uploadTaskContent').html(showTaskHtml);
	})
}
