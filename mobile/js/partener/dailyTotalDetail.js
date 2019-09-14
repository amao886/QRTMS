
//初始化项目组，日期
var groupname = _common.getUrlParam('groupname');
var groupid  =  _common.getUrlParam('groupid');
var bindtime =  _common.getUrlParam('bindtime');

//页面下拉刷新
var dLoading = true;  //状态标记,loading为true时，才会加载下一页
var dNum = 1;
var pageSize = 3;

window.onload = function(){

	
	$('#project-span').html(groupname);
	$('#time-span').html(bindtime);
	
	//获取项目组成功后获取数据
    if(sessionStorage.getItem("detailNum")){
    	//有缓存
    	//alert('有缓存');
        var tops = parseInt(sessionStorage.getItem("detailTop")); //距离  
        var num = sessionStorage.getItem("detailNum"); //页数
        var status = sessionStorage.getItem("detailStatus"); //任务状态 
        var texts = sessionStorage.getItem("detailText"); //搜索内容
        
        tops = tops ? tops : 0;
        num = num ? num : 1;
        
        //定位+赋值
        dNum = num;
        $('#task-sel').val(status);
        $('#searchData').val(texts);
        //样式判断
        if(status){ 
        	$('#task-sel').parent().addClass('active');
        }
        
        getDetails({
			num         : 1,
			size        : pageSize,
			bindstatus  : $('#task-sel').val(),
			customerName: $('#searchData').val()
		},num,tops);
   	
    }else{
    	//没有缓存
    	//alert('没缓存');
    	getDetails({
			num:dNum,
			size:pageSize
		});
    }
	
	//点击添加下划线
	$('.task-status .task-status-item').click(function(){
		$(this).addClass('active').siblings().removeClass('active');
	})
	
	//点击清空input框内容
	$('#weui-icon-clear').click(function(){
		$('#searchData').val('');
		searchDetails();
	})
	
	//点击搜索
	$('#search-msg').click(function(){
		if($.trim($('#searchData').val())){
			searchDetails();
		}else{
			$.toast('请先输入搜索内容','text');
		}
		
	})
	
	//点击enter键搜索
	$('#searchData').keyup(function(ev){
		if(ev.keyCode == 13){
			if($.trim($('#searchData').val())){
				searchDetails();
			}else{
				$.toast('请先输入搜索内容','text');
			}
		}
	})
	
	$('#task-sel').change(function(){
		searchDetails();
	})
	
}

//滚动时存储数据
$(window).scroll(function(){
    var totalheight = $(window).scrollTop();
    console.log('tops:'+totalheight);
   	if(window.sessionStorage){
       	sessionStorage.setItem("detailTop",totalheight); //距离
        sessionStorage.setItem("detailNum",dNum);  //页数
        sessionStorage.setItem("detailStatus",$('#task-sel').val()); //任务状态
        sessionStorage.setItem("detailText",$('#searchData').val()); //搜索内容
   	}
   	
});

$(document.body).infinite(0).on("infinite", function() {
	if(dLoading){
		$('.weui-loadmore').show();
		dLoading=false;
		dNum++;
		getDetails({
			num:dNum,
			size:pageSize,
			bindstatus:$('#task-sel').val(),
			customerName:$('#searchData').val()			
		});
	}	
});


//查询日统计任务详情
function getDetails(options,index,tops){
	var baseValue = {		
	    "size"         :options.size,
	    "num"          :options.num,
	    "groupid"      : groupid,
	    "bindtime"     : bindtime,
	    "bindstatus"   : options.bindstatus || '',
	    "customerName" : options.customerName || ''
	}
	EasyAjax.ajax_Post_Json({
		url:'mobile/wayBill/detail/search',
		data:JSON.stringify(baseValue)
	},function(res){
		if(dNum == 1 && (res.page.collection == null || res.page.collection.length == 0)){
			$('.task-list-item-con').html("<p style='text-align:center;font-size:15px;padding:10px 0;'>暂无数据。。。</p>");
			$('.weui-loadmore').hide();
			
		}else if(dNum != 1 && (res.page.collection == null || res.page.collection.length == 0)){
			$('.weui-loadmore').html('已经到底了');
		}else{
			var taskHtml = $('#taskList').render(res.page.collection);
			$('.task-list-item-con').append(taskHtml);
			dLoading=true;  //表明数据加载完毕	
			$('.weui-loadmore').hide();
		}
		
        //点击跳转页面
        $('.task-list-item-part').on('click tap', function(){
            var uid = $(this).attr('uid');
            window.location.href = _common.version('./myTaskDetails.html?waybillId='+uid);
        });
        
        //开始定位
        if(index){
			if(index == 1){
				//alert('开始定位')
				$(document).scrollTop(tops);
			}else{
				index --;
				options.num++;
				getDetails(options,index,tops);	
				
			}
		}
	})
	
	
}

//搜索、查询等方法
function searchDetails(){
	$('.task-list-item-con').html("");
	$('.weui-loadmore').show();
	dNum = 1;
	getDetails({
		num:dNum,
		size:pageSize,
		bindstatus:$('#task-sel').val(),
		customerName:$('#searchData').val()
	});
}
