var bindData = {
	pageNum:1, //当前页
	pageSize: 5,  //每页传几条数据
	dLoading: false,
	isLoadMore: false
}

$(function(){
	var storeData = JSON.parse(sessionStorage.getItem('scrollData'));
	if(storeData){ //走缓存
		bindData.pageNum = 1;
		$('#inputSearch').val(storeData.searchmsg);
		
		getListData(storeData.num, storeData.top);
		
	}else{
		//获取数据
		getListData();
	}
	
	//将二维码号存储下来
	sessionStorage.setItem('qrcode',_common.getUrlParam('qrcode'));

	//点击按钮搜索
	$('#searchClear').click(function(){
		$('#searchInput').val('');
		searchListData();
	})
	
	//点击搜索
	$('#searchMsg').click(function(){
		if($.trim($('#searchInput').val())){
			searchListData();
		}else{
			$.toast('请先输入搜索内容','text')
		}
	})
	
	//点击enter搜索
	$('#inputSearch').keyup(function(ev){
		if(ev.keyCode == 13){
			if($.trim($('#inputSearch').val())){
				searchListData();
			}else{
				$.toast('请先输入搜索内容','text')
			}
		}
	})
})


//获取数据
function getListData(index,tops){
	var baseValue = {
    	likeString: $('#searchInput').val(),
    	num: bindData.pageNum,
    	size: bindData.pageSize
    }
    EasyAjax.ajax_Post_Json({
        url: 'enterprise/order/shipper/bind/search',
        data: JSON.stringify(baseValue)
    }, function (res) {
    	if(!res.success){
    		$.toast("加载失败，请稍后重试", "text");
    		
    	}else{
    		var results = res.page.collection;
    		if(bindData.pageNum == 1 && (results == null || results.length == 0)){ //第一页
    			$('.receipt-wrapper').html('<p class="noData" style="text-align:center;font-size:15px;padding:10px 0;">暂无数据</p>');
    			
    		}else{
    			var listDataHtml = $('#listData').render(results);
    			
    			$('.noData').remove();

    			if(!bindData.isLoadMore){
	    			$('.receipt-wrapper').empty();
	    		}
    			$('.receipt-wrapper').append(listDataHtml);
    			
    		}
    		
    		$('.weui-loadmore').hide(); //请求成功后隐藏加载更多

            if(res.page.pageNum == res.page.pages){ //最后一页
            	bindData.dLoading = false;
            	$('.weui-loadmore').show().html('已经到底了');
            }else{
            	bindData.dLoading = true;
            }
            
            if(index){
            	if(index == 1){
					$(document).scrollTop(tops);
				}else{
					index --;
					bindData.pageNum++;
					bindData.isLoadMore = true;
					getListData(index,tops);
				}
            }
        }
    });
}

//重新查询数据
function searchListData(){
	$('.receipt-wrapper').html('');
	bindData.pageNum = 1;
	bindData.isLoadMore = false;
	getListData();
}

//跳转页面
function toPage(key){
    window.location.href = _common.version("bindCodeDetail.html?orderKey="+key);
}

//滑动存储数据
$(document).scroll(function(){
	var totalheight = $(window).scrollTop();
	if(window.sessionStorage){
		var scrollData = {
			num: bindData.pageNum,
			searchmsg: $.trim($('#searchInput').val()),
			top: totalheight
		}
		sessionStorage.setItem('scrollData',JSON.stringify(scrollData));
	}
});

//下拉刷新
$(document.body).infinite(0).on("infinite", function() {	
	if(bindData.dLoading){
		$('.weui-loadmore').show(); //显示加载更多
		bindData.pageNum ++;
		bindData.dLoading = false;
		bindData.isLoadMore = true;
		getListData();	
	}	
});