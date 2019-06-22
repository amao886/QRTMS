
//初始化数据
getBindCodeList();


//获取已绑定任务单列表
function getBindCodeList(){
	EasyAjax.ajax_Post_Json({
		url:'mobile/wayBill/list/bind'
	},function(res){
		var results = res.results;
		var time = res.results[0];
		console.log(time);
		if(results == null || results.length == 0 ){
			$('#barCodeList').append('<p style="text-align:center;font-size:15px;padding:10px 0;">暂无数据。。。</p>');
		}else{
			var timeHtml = $('#timeHtml').render(time);
			$('#barCodeList').append(timeHtml);
			for(var item in results){
				var listHtml = $('#listHtml').render(results[item]);
				$('#barCodeList').append(listHtml);
			}
		}
		
		//点击跳转到任务详情页
		$('.task-list-item-con').click(function(){
			var waybillId = $(this).attr('uid');
			window.location.href = _common.version('../../view/partener/myTaskDetails.html?waybillId='+waybillId);
		})
		
	});
}

//处理后台返回的时间
$.views.helpers({
	dateFormat:function(msg){
	  	var time   = new Date(msg);
	  	var year   = time.getFullYear();
	    var month  = time.getMonth()+1;
	    var day    = time.getDate();
	    var hour   = time.getHours();
	    var minute = time.getMinutes(); 
	    month  = month<10 ? '0'+month : month;
	    day    = day<10 ? '0'+day : day;
	    hour   = hour<10 ? '0'+hour : hour;
	    minute = minute<10 ? '0'+minute : minute;
		return year+'-'+month+'-'+day;
    }
});
