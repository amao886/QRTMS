var receiptFrom = _common.getUrlParam('receiptSts'),
	time = _common.getUrlParam('t'),
	shipperId = _common.getUrlParam('shipperId') || '',
	receiveId = _common.getUrlParam('receiveId') || '',
	conveyId = _common.getUrlParam('conveyId') || '',
	flag = 0,
	fettle = 0, // 签收状态
	signFettle = 0; // 回单签署状态
if(receiptFrom === 'shipper'){ //发货回单
	$('title').html('发货回单');
	$('.receiptTxt').show();
	$('.signTxt').hide();
	$('.sendSts').show();
	
}else if(receiptFrom === 'convey'){ //承运回单
	$('title').html('承运回单');
	$('.receiptTxt').hide();
	$('.signTxt').show();
	$('.shipperSts').show();
	
}else{ //收货回单
	$('title').html('收货回单');
	$('.receiptTxt').hide();
	$('.signTxt').show();
	$('.receiveSts').show();
}


//加载数据
getListData(receiptFrom);

//切换状态
$('.searchSelect').on('click',function(){
	$(this).addClass('borderBottom').siblings().removeClass('borderBottom');
	flag = $(this).attr('uid');
	console.log('flag:'+ flag);
	
	getListData(receiptFrom); //查询数据
		
	//未生成回单，回单签署状态，签收状态隐藏
	if($(this).hasClass('special')){
		$('.writeSts').hide();
		$('.choosePart').hide();
	}else{
		$('.writeSts').show();
		$('.choosePart').show();
	}

})

//显示筛选
$('.choose').click(function(){
  $('.chooseCon').addClass('show');
})

//取消按钮
$('.cancelBtn').click(function(){
	$('#inputSearch').val('');
	fettle = 0;
	signFettle = 0;
	$('.chooseCon').removeClass('show');
})

//遮罩层点击取消
$('.chooseCon').on('click',function(ev){
	if(ev.target === $('.chooseCon')[0]){
		$('#inputSearch').val('');
		fettle = 0;
		signFettle = 0;
		$('.chooseCon').removeClass('show');
	}
})

//状态选择
$('.choosePart>ul>li').click(function(){
	$(this).addClass('active').siblings().removeClass('active');
})

//确定按钮
$('.confirmBtn').click(function(){
    signFettle = $('input[name=signType]:checked').val() || 0;
    fettle = $('input[name=sendType]:checked').val() || 0;
	console.log('签收状态：' + signFettle +' 签署状态：' + fettle);
	getListData(receiptFrom); //查询数据
	$('.chooseCon').removeClass('show');
})

//筛选框清除icon
$('#iconClear').on('click',function(){
	$('#inputSearch').val('');
})

//跳转页面
function toPage(id){
	window.location.href = _common.version("electronicReceipt.html?id="+id);
}

//获取后台接口
function getListData(urlParam){
	var baseValue = {
        dayTime     : time,
		shipperKey  : shipperId,
		receiveKey  : receiveId,
		conveyKey   : conveyId,
		likeString  : $('#inputSearch').val(),
		flag        : flag,
		fettle      : fettle,
		signFettle  : signFettle
    }
    EasyAjax.ajax_Post_Json({
        url: 'enterprise/order/'+urlParam+'/concise/day',
        data: JSON.stringify(baseValue)
    }, function (res) {
    	if(!res.success){
    		$.toast("加载失败，请稍后重试", "text");
    		
    	}else{
    		var results = res.results;
    		if(results == null || results.length == 0){ //没有数据
    			$('.sendOrder-wrapper').html('<p style="text-align:center;font-size:15px;padding:10px 0;">暂无数据</p>');
    			
    		}else{
    			var listDataHtml = $('#listData').render(results);
    			$('.sendOrder-wrapper').html(listDataHtml);
    		}
    		removeLoad();
    	}
    })
}
