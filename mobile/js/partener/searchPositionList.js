var dNum = 1,
	pageSize = 5,
	dLoading = true;
	
//页面初始化
getTaskList({
	num  : dNum,
	size : pageSize
});

//点击清空搜索框内容
$('#searchClear').click(function(){
	$('#searchData').val('');
	searchTaskList();
})

//点击搜索查询
$('#search-btn').click(function(){
	console.log($('#searchData').val());
	if($.trim($('#searchData').val())){
		searchTaskList();
	}else{
		$.toast('请先输入搜索内容','text')
	}
})

//点enter键
$('#searchData').keyup(function(ev){
	if(ev.keyCode == 13){
		if($.trim($('#searchData').val())){
			searchTaskList();
		}else{
			$.toast('请先输入搜索内容','text')
		}
	}
})

//下拉刷新翻页
$(document.body).infinite(120).on("infinite", function() {
	if(dLoading){
		dLoading=false;
		dNum++;
		$('.weui-loadmore').show();
		getTaskList({
			num        : dNum,
			size       : pageSize,
			createtime : '',
			delay      : '',
			likeString : utf16toEntities($('#searchData').val()),
			waybillFettles     : ''	
		})
	}	
});

function getTaskList(options){
	var baseValue = {
		num		   : options.num,
		size       : options.size,
		groupId    : options.groupId || '',
    	createtime : options.createtime || '' ,
    	delay      : options.delay || '',
    	likeString : options.searchData || '',
    	waybillFettles     : options.status || ''
	}
	EasyAjax.ajax_Post_Json({
		url:'mobile/wayBill/myTask/search',
		data:JSON.stringify(baseValue)
	},function(res){
		var results = res.page.attachment;
		if(dNum == 1 && (results == null || results.length == 0)){
			
			$('.task-list-con').append('<p style="text-align:center;font-size:15px;padding:10px 0;">暂无数据。。。</p>');
			$('.weui-loadmore').hide();
			
		}else if(dNum != 1 && (results == null || results.length == 0)){
			$('.weui-loadmore').show().html('已经到底了');
			
		}else{
			for(var item in results){
				var mytaskTitle = "<p class='task-list-item-title'>" + item + "</p>";
				var mytaskHtml = $('#taskList').render(results[item]);
				$('.task-list-con').append("<div class='task-list-item'>" + mytaskTitle+ "<div class='task-list-item-con'>" + mytaskHtml + "</div>" + "</div>");
			}
			$('.weui-loadmore').hide();
			dLoading = true;
			
		}
		
		
		//点击跳转页面
		$('.task-list-item-part').click(function(){
			var uid = $(this).attr('uid');
			window.location.href = _common.version('./upload.html?showClose=10&waybillId='+uid);
		})
	});
}

//搜索
function searchTaskList(){
	$('.task-list-con').html('');
	$('.weui-loadmore').show();
	getTaskList({
		num        : dNum,
		size       : pageSize,
		createtime : '',
		delay      : '',
		likeString : $('#searchData').val(),
		waybillFettles     : ''	
	})
}