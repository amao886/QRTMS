var groupId  = _common.getUrlParam('groupId');  //接口参数
var bindCode = _common.getUrlParam('bindCode'); //任务单条码
var dNum = 1, 
	Psize = 5, 
	dLoad = true;
	
//绑定条码
$('.barcode-num > span').text(bindCode);

//点击编辑按钮跳转页面
$('#taskSearchBtn').on('click',function(){
	window.location.href=_common.version("bindBarCode.html?bindCode="+bindCode+'&groupId='+groupId);
})

//初始化获取数据
getWaybillList({
	groupId : groupId,
	num     : dNum,
	size    : Psize	
});

//点击按钮搜索
$('#iconSearch').on('click',function(){
	if($.trim($('#searchData').val())){
		$('.barcode-num').hide(); //任务单号隐藏
		searchWaybill();
	}else{
		$.toast('请先输入搜索内容','text');
	}
	
})

//按enter键搜索
$('#searchData').keyup(function(ev){
	if(ev.keyCode == 13){
		if($.trim($('#searchData').val())){
			$('.barcode-num').hide(); //任务单号隐藏
			searchWaybill();
		}else{
			$.toast('请先输入搜索内容','text')
		}
	}
})

//点击清空input框内容
$('#searchClear').on('click',function(){
	$('#searchData').val('');
	$('.barcode-num').show(); //任务单号显示
	searchWaybill();	
})

//点击跳转页面
$('#barcodeCon').delegate('.barcode-item','click',function(){
	var id = $(this).attr('uid'); //任务单id
	window.location.href = _common.version('./bindBarCodeNew.html?bindCode='+bindCode+'&waybillId='+id);
})

//下拉刷新
$(document.body).infinite(0).on("infinite", function() {
    if(dLoad){
        dLoad = false;
        dNum ++;
        $('.weui-loadmore').show();
        getWaybillList({
			groupId    : groupId,
			likeString : $.trim($('#searchData').val()),
			num        : dNum,
			size       : Psize	
		});
    }
});


//列表数据接口
function getWaybillList(options){
	var baseValue = {
    	groupId    : options.groupId,
    	likeString : options.likeString || '',
    	size       : options.size || '',
    	num        : options.num || ''    
	 }
	EasyAjax.ajax_Post_Json({
		url:'mobile/wayBill/query/unbound/waybill',
		data:JSON.stringify(baseValue)
	},function(res){
	    var results = res.page.collection;
	    if(dNum == 1 && (results == null || results.length == 0)){
			$('#barcodeCon').html('<p style="text-align:center;font-size:15px;padding:10px 0;">暂无数据。。。</p>');

		}else if(dNum != 1 && (results == null || results.length == 0)){
			$('.weui-loadmore').show().html('已经到底了');

		}else{
		 	var waybillHtml = $('#waybillList').render(results);
		    $('#barcodeCon').append(waybillHtml);
		    
		    dLoad = true;
	  }
		
		
		$('.weui-loadmore').hide();
	    
   });	          
}

//不同条件时触发
function searchWaybill(){
	$('.weui-loadmore').show();
	$('#barcodeCon').html('');	
	dNum = 1;
	getWaybillList({
		groupId    : groupId,
		likeString : $.trim($('#searchData').val()),
		num        : dNum,
		size       : Psize	
	});
	
}
