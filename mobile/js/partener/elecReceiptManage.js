var elecListData = {
	urlParam: _common.getUrlParam('receiptSts'),
	firstTime:'',
	secondTime: '',
	pageNum:1, //当前页
	pageSize: 5,  //每页传几条数据
	dLoading: false,
	isLoadMore: false
}

$(function(){
	var titleEle = document.getElementsByTagName('title')[0];
	if(elecListData.urlParam === 'convey'){
		titleEle.innerHTML = '承运回单';
	}else if(elecListData.urlParam === 'receive'){
		titleEle.innerHTML = '收货回单';
	}
	
	
	
	var storeData = JSON.parse(sessionStorage.getItem('scrollData'));
	if(storeData){ //走缓存
		elecListData.pageNum = 1;
		elecListData.firstTime = storeData.ftime;
		elecListData.secondTime = storeData.stime;
		elecListData.urlParam = storeData.urlparam;
		getListData(storeData.urlparam, storeData.num, storeData.top);
		
	}else{
		//获取数据
		getListData(elecListData.urlParam);
	}
	
	//开始时间
	$("#startDate").calendar({
	    onChange: function (p, values, displayValues) {
	    	elecListData.startTime = displayValues[0];
	    	elecListData.firstTime = values[0];
	    }
	});
	
	//结束时间
	$("#endDate").calendar({
	    onChange: function (p, values, displayValues) {
	    	elecListData.endTime = displayValues[0];
	    	elecListData.secondTime = values[0];
	    }
	});
	
	//点击查询
	$('#aSureBtn').on('click',function(){
		if(checkDate(elecListData.firstTime,elecListData.secondTime)){
			elecListData.pageNum = 1;
			elecListData.isLoadMore = false;
			getListData(elecListData.urlParam);
		}
	})
})

//查询数据接口
function getListData(urlParam,index,tops){
	var baseValue = {
        likeString : '',
		firstTime  : elecListData.firstTime,
		secondTime : elecListData.secondTime,
		fettle     : '',
		signFettle : '',
		size       : elecListData.pageSize,
		num        : elecListData.pageNum
    }
    EasyAjax.ajax_Post_Json({
        url: 'enterprise/order/'+urlParam+'/concise/search',
        data: JSON.stringify(baseValue)
    }, function (res) {
    	if(!res.success){
    		$.toast("加载失败，请稍后重试", "text");
    		
    	}else{
    		var results = res.page.collection;
    		if(elecListData.pageNum == 1 && (results == null || results.length == 0)){ //第一页
    			$('.receipt-wrapper').html('<p class="noData" style="text-align:center;font-size:15px;padding:10px 0;">暂无数据</p>');
    			
    		}else{
    			var listDataHtml = '';

    			$('.noData').remove();

    			if(urlParam === 'shipper'){ //发货回单
    				listDataHtml = $('#shipperData').render(results);
    				
	    		}else if(urlParam === 'receive'){ //收货回单
	    			listDataHtml = $('#receiveData').render(results);
	    			
	    		}else{ //承运回单
	    			listDataHtml = $('#conveyData').render(results);
	    		}
	    		if(!elecListData.isLoadMore){
	    			$('.receipt-wrapper').empty();
	    		}
    			$('.receipt-wrapper').append(listDataHtml);
    			
    		}
    		$('.weui-loadmore').hide(); //请求成功后隐藏加载更多

            if(res.page.pageNum == res.page.pages){ //最后一页
            	elecListData.dLoading = false;
            	$('.weui-loadmore').show().html('已经到底了');
            }else{
            	elecListData.dLoading = true;
            }
            
            if(index){
            	if(index == 1){
					$(document).scrollTop(tops);
				}else{
					index --;
					elecListData.pageNum++;
					elecListData.isLoadMore = true;
					getListData(urlParam,index,tops);
				}
            }
    	}
    });
}

//滑动存储数据
$(document).scroll(function(){
	var totalheight = $(window).scrollTop();
	if(window.sessionStorage){
		var scrollData = {
			num: elecListData.pageNum,
			ftime: elecListData.firstTime,
			stime: elecListData.secondTime,
			urlparam: elecListData.urlParam,
			top: totalheight
		}
		sessionStorage.setItem('scrollData',JSON.stringify(scrollData));
	}	
});

//下拉刷新
$(document.body).infinite(0).on("infinite", function() {	
	if(elecListData.dLoading){
		$('.weui-loadmore').show(); //显示加载更多
		elecListData.pageNum ++;
		elecListData.dLoading = false;
		elecListData.isLoadMore = true;
		getListData(elecListData.urlParam);	
	}	
});

//跳到发货回单页
function toShipperPage(time,receiveKey,conveyKey) {
	window.location.href = _common.version('elecReceiptList.html?t='+time+'&receiptSts=shipper'+'&receiveId=' + receiveKey + '&conveyId=' + conveyKey);
}
//跳到收货回单页
function toReceivePage(time,shipperKey,conveyKey) {
	window.location.href = _common.version('elecReceiptList.html?t='+time+'&receiptSts=receive'+'&shipperId=' + shipperKey + '&conveyId=' + conveyKey);
}
//跳到承运回单页
function toConveyPage(time,shipperKey,receiveKey) {
	window.location.href = _common.version('elecReceiptList.html?t='+time+'&receiptSts=convey'+'&receiveId=' + receiveKey + '&shipperId=' + shipperKey);
}

//判断开始日期不能大于结束日期
function checkDate(startTime, endTime) {
    if (startTime.length > 0 && endTime.length > 0) {
        var startTmp = startTime.split("-");
        var endTmp = endTime.split("-");
        var sd = new Date(startTmp[0], startTmp[1], startTmp[2]);
        var ed = new Date(endTmp[0], endTmp[1], endTmp[2]);
        if (sd.getTime() > ed.getTime()) {
            $.toast("开始日期不能大于结束日期", "text");
            return false;
        }else{
        	return true;
        }
    }else {
    	$.toast("日期填写不完整", "text");
    	return false;
    }
}

