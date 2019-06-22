var pulicTime = '';
var dNum = 1,
	pageSize = 5,
	dLoading = true;

window.onload = function(){		   	
	getloadGoodsList({
		num  :  dNum,
		size :  pageSize
	});
		
	//点击清空input框内容
	$('#searchClear').click(function(){
		$('#searchData').val('');
		searchMytaskList();
	})
	
	//内容搜索
	$('#search-btn').click(function(){
		serchFunc();	
	})
	
	//点击搜索按钮搜索
	$('#searchData').keyup(function(ev){
		if(ev.keyCode == 13){
			serchFunc();
		}
	})
	
	function serchFunc(){
		if($.trim($('#searchData').val())){
			searchMytaskList();
		}else{
			$.toast('请先输入搜索内容','text')
		}
	}
}

//刷新翻页
$(document.body).infinite(0).on("infinite", function() {
	if(dLoading){
		dLoading=false;
		$('.weui-loadmore').show();
		dNum++;
		
		getloadGoodsList({
			num   : dNum,
			size  : pageSize,
			likeString : $("#searchData").val()
		})
		
	}	
});

//获取货物列表
function getloadGoodsList(options){
	var baseValue = {
		"num"		    : options.num,
		"size"          : options.size,
    	"likeString"    : options.likeString || '' ,
    	"unload"        : true
	}
	EasyAjax.ajax_Post_Json({
		url:'mobile/driver/search',
		data:JSON.stringify(baseValue)
	},function(res){
		var results = res.page.collection;
		
		$(".totalGoods").text(res.page.total);
		if(dNum == 1 && (results == null || results.length == 0)){
			$('.goods-list-con').append('<p style="text-align:center;font-size:15px;padding:10px 0;">暂无数据。。。</p>');
			$("#locTopBox").hide();

		}else if(dNum != 1 && (results == null || results.length == 0)){
			$('.weui-loadmore').html('已经到底了');

		}else{
			for(var item in results){
				var mytaskHtml = $('#loadGoodsList').render(results[item]);		
				$(".goods-list-con").append(mytaskHtml)
			}
			
			//加载定位信息
			var track = res.track;
			if(track == null){
				$(".locAddr").text("暂无定位信息");
				$(".locTime").text("");							    
			}else{
				$(".locAddr").text(track.reportLoaction);
			    $(".locTime").text(track.reportTime);
			}			
			
			dLoading = true;
		}
		$('.weui-loadmore').hide();
		
	});
}

//搜索
function searchMytaskList(){
	$('.goods-list-con').html('');
	$('.weui-loadmore').show();
	dNum = 1;
	getloadGoodsList({
		num        : dNum,
		size       : pageSize,
		likeString : $("#searchData").val()
	})
}





