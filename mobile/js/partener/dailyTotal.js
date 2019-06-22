window.onload = function(){
	//初始化年月
	getYearMonth();
	
	//获得项目组
	getProjectInfo();
	
	//改变筛选条件
	$('#sel-project,#sel-year,#sel-month').change(function(){
		var $project = $('#sel-project').val();
		var $year = $('#sel-year').val();
		var $month = $('#sel-month').val();
		console.log('p:'+$project,'year:'+$year,'month:'+$month);
		if($project){
			getCountList($year,$month,$project);
		}
		
	})
}

//滚动时存储数据
$(window).scroll(function(){
    var totalheight = $(window).scrollTop();
    console.log('tops:'+totalheight);
   	if(window.sessionStorage){
       	sessionStorage.setItem("dailyTop",totalheight); //距离        
        sessionStorage.setItem("dailyGid",$('#sel-project').val()); //项目组
        sessionStorage.setItem("dailyYear",$('#sel-year').val()); //年
        sessionStorage.setItem("dailyMonth",$('#sel-month').val()); //月
   	}
   	
});



//查询项目组信息
function getProjectInfo(){
	EasyAjax.ajax_Post_Json({
		url:'mobile/mine/group/list'
	},function(res){
		if(res.groups == null || res.groups.length == 0){
			$('#countCon').html('<p style="text-align:center;font-size:15px;padding:10px 0;">暂无数据。。。</p>')
		}else{
			var selectHtml = $('#selectOption').render(res.groups);
			$('#sel-project').html(selectHtml);
			var initSelValue = $('#sel-project').val();
			console.log('initSelValue:'+initSelValue);
			
			//页面初始化获取数据
	        if(sessionStorage.getItem("dailyTop")){
	        	//有缓存
	        	//alert('有缓存');
		        var tops = parseInt(sessionStorage.getItem("dailyTop")); //距离
		        var gid = sessionStorage.getItem("dailyGid"); //项目组
		        var dyear = sessionStorage.getItem("dailyYear"); //年
		        var dmonth = sessionStorage.getItem("dailyMonth"); //月
		        tops = tops ? tops : 0;
		        $('#sel-project').val(gid);
		        $('#sel-year').val(dyear);
		        $('#sel-month').val(dmonth);
		        getCountList(dyear,dmonth,gid,tops);
	        	
	        }else{
	        	//没有缓存
	        	//alert('没缓存');
				getCountList($('#sel-year').val(),$('#sel-month').val(),initSelValue);
	        }
			
			
			
		}
	
	})
}

//查询发货统计列表
function getCountList(selectYear,selectMonth,groupid,tops){
	var baseValue = {
		"year"     : selectYear,
		"month"    : selectMonth,
		"groupid"  : groupid
	}
	EasyAjax.ajax_Post_Json({
		url:'mobile/dailyTotal/search',
		data:JSON.stringify(baseValue)
	},function(res){
		if(res.entity == null || res.entity.length == 0){
			$('.count-con').html("<p style='text-align:center;'>暂无数据。。。</p>");
		}else{
			var countHtml = $('#countList').render(res.entity);
			$('.count-con').html(countHtml);
			//点击进入详情页
			$('.count-con-item').click(function(){
				var groupid = $('#sel-project').val();
				var bindtime = $(this).find('.count-con-item-title').children().text();
				var groupname = $('#sel-project option:selected').text();
				window.location.href = _common.version('./dailyTotalDetail.html?groupid='+groupid+'&bindtime='+bindtime+'&groupname='+groupname);
			})	
		}
		
		//缓存定位
		if(tops){
			$(document).scrollTop(tops);
		}
		
	})
}

//获得年,月
function getYearMonth(){
	var date = new Date();
	$('#sel-year').val(date.getFullYear());
	$('#sel-month').val(date.getMonth()+1);	
}



 
