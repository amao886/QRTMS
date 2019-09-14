var pulicTime = '';
var dNum = 1,
	pageSize = 5,
	dLoading = true,
	pDelay='',
	pFettles='',
	simTime='3',
	firTime='',
	secTime='',
	rangeTime='',
	prevTime=''; //存储选择时间段之前的日期
//发货时间
var sendTimeArr = [
	{
	  title: "今天",
	  value: "0",
	},
	{
	  title: "近三天",
	  value: "3",
	},
	{
	  title: "近一周",
	  value: "7",
	},
	{
	  title: "近一月",
	  value: "30",
	},
	{
	  title: "全部",
	  value: "",
	},
	{
	  title: "更多",
	  value: "-2",
	}
];
//任务状态
var fettlesArr = [
	{
	  title: "全部",
	  value: "",
	},
	{
	  title: "运输中",
	  value: "30",
	},
	{
	  title: "已送达",
	  value: "35",
	},
	{
	  title: "已到货",
	  value: "40",
	}
];
//延迟状态
var delayArr = [
	{
	  title: "全部",
	  value: "",
	},
	{
	  title: "延迟",
	  value: "true",
	},
	{
	  title: "正常",
	  value: "false",
	}
];
	
window.onload = function(){
	//页面初始化
	if(sessionStorage.getItem("taskNum")){  //有缓存
		console.log('走缓存');
        var tops = parseInt(sessionStorage.getItem("taskTop")); //距离
        var num = sessionStorage.getItem("taskNum"); //页数
        var simpletime = sessionStorage.getItem("taskSimTime"); //时间
        var firsttime = sessionStorage.getItem("taskFirTime"); //时间
        var secondtime = sessionStorage.getItem("taskSecTime"); //时间
        var stext = sessionStorage.getItem("taskText"); //搜索内容
        var delay = sessionStorage.getItem("taskDelay"); //延迟状态
        var status = sessionStorage.getItem("taskStatus"); //任务状态
        tops = tops ? tops : 0;
        num = num ? num : 1;
        
        console.log('时间判断：'+ simpletime)
        console.log('开始时间：'+ firsttime)
        console.log('结束时间：'+ secondtime)
        
        //定位+赋值
        dNum = num; 
        simTime = simpletime;
        firTime = firsttime,
		secTime = secondtime;
		pDelay=delay;  
		pFettles=status;       
        if(simpletime && (simpletime >= 0)){ //发货时间样式值
        	$('#sendTime').val(getValName(sendTimeArr,simpletime));
        }else if(simpletime && (simpletime == '-2')){
        	$('#sendTime').val(getDateStr(firsttime)+'-'+getDateStr(secondtime));
        }
       
		$('#fettles').val(getValName(fettlesArr,status))   //任务状态样式值
		$('#delayStatus').val(getValName(delayArr,delay))   //延迟状态样式值
		$('#searchData').val(stext);  //搜索内容
        getMytaskList({       	 	
       	 	num           : 1,
			size          : pageSize,
			simpleTime    : simTime,
    		firstTime     : firTime,
    		secondTime    : secTime,
			delay         : delay,
			likeString    : stext,
			waybillStatus : status
        },num,tops);	
        
    }else{ //没有缓存
	   	console.log('没有缓存时');
   		getMytaskList({
			num           :  dNum,
			size 		  :  pageSize,
			simpleTime    : simTime,
    		firstTime     : firTime,
    		secondTime    : secTime,
			delay         : pDelay,
			likeString    : $('#searchData').val(),
			waybillStatus : pFettles
		});
    }
    
    //初始化日期插件
    var currYear = (new Date()).getFullYear();	
	var opt={};
	opt.date = {preset : 'date'};
	opt.datetime = {preset : 'datetime'};
	opt.time = {preset : 'time'};
	opt.default = {
		theme: 'android-ics light', //皮肤样式
        display: 'modal', //显示方式 
        mode: 'scroller', //日期选择模式
		dateFormat: 'yyyy-mm-dd',
		lang: 'zh',
		showNow: true,
		nowText: "今天",
        startYear: currYear - 10, //开始年份
        endYear: currYear + 10 //结束年份
	};

  	$("#startDate").mobiscroll($.extend(opt['date'], opt['default']));
    $('#endDate').mobiscroll($.extend(opt['date'], opt['default']));
    
    //切换tab
    $('.selectTab .weui-flex__item').on('click',function(){
    	$(this).addClass('active').siblings().removeClass('active');
    })
    
    //日期弹出框取消按钮
    $('.range-time-cencel').on('click',function(){
    	$('#startDate').val('');
    	$('#endDate').val('');  
    	if(rangeTime){
    		$('#sendTime').val(rangeTime);
    	}else if(simTime && !prevTime){
    		$('#sendTime').val(getValName(sendTimeArr,3)); //第一次就点击更多
    	}else{
    		$('#sendTime').val(getValName(sendTimeArr,prevTime)); //点取消时，给发货时间赋上一个选中的条件
    	}    	
    	$('.range-time').hide();
    })
    
    //日期弹出框确定按钮
    $('.range-time-sure').on('click',function(){
    	var startTime = $('#startDate').val();
    	var endTime = $('#endDate').val();
    	if(checkDate(startTime,endTime)){
    		rangeTime = getDateStr(startTime)+'-'+getDateStr(endTime);
    		firTime = startTime;
    		secTime = endTime;
	    	$('#sendTime').val(rangeTime);
	    	$('.range-time').hide();
	    	searchMytaskList();
    	}
    	
    })
    
    //解决ios readonly兼容问题
    $('input[readonly]').on('focus', function() {
	    $(this).trigger('blur');
	});

	//点击清空input框内容
	$('#searchClear').click(function(){
		$('#searchData').val('');
		searchMytaskList();
	})
	
	//点击发货时间
	$('#sendTime').select({
	  	title: "发货时间",
  		items: sendTimeArr,
		onChange: function(data) {
			simTime = data.values;
			if(data.values === '-2'){
				$('.range-time').show();
			}else{
				rangeTime = '';
				prevTime = data.values;
				firTime='',
				secTime='';
			  	searchMytaskList();
			}
            
        }
	});
	
	//点击任务状态
	$('#fettles').select({
	  	title: "任务状态",
  		items: fettlesArr,
		onChange: function(data) {
          pFettles = data.values;
          console.log('任务状态：'+pFettles);
          searchMytaskList();
        }
	});
	
	//点击延迟状态
	$('#delayStatus').select({
	  	title: "延迟状态",
  		items: delayArr,
		onChange: function(data) {
          pDelay = data.values;
          console.log('延迟状态：'+pDelay)
          searchMytaskList();
        }
	});
	
	//点击搜索按钮搜索
	$('#searchData').keyup(function(ev){
		if(ev.keyCode == 13){
			if($.trim($('#searchData').val())){
				searchMytaskList();
			}else{
				$.toast('请先输入搜索内容','text')
			}
		}
	})
}

//滑动时存储数据
$(window).scroll(function(){
    var totalheight = $(window).scrollTop();
    console.log('tops:'+totalheight);
   	if(window.sessionStorage){
       	sessionStorage.setItem("taskTop",totalheight); //距离
        sessionStorage.setItem("taskNum",dNum);  //页数
        sessionStorage.setItem("taskSimTime",simTime); //时间
        sessionStorage.setItem("taskFirTime",firTime); //时间
        sessionStorage.setItem("taskSecTime",secTime); //时间
        sessionStorage.setItem("taskText",$('#searchData').val()); //搜索内容
        sessionStorage.setItem("taskDelay",pDelay); //延迟状态
        sessionStorage.setItem("taskStatus",pFettles); //任务状态
   	}
});


//刷新翻页
$(document.body).infinite(0).on("infinite", function() {
	if(dLoading){
		dLoading=false;
		 $('.weui-loadmore').show();
		dNum++;
		getMytaskList({
			num           : dNum,
			size          : pageSize,
			simpleTime    : simTime,
    		firstTime     : firTime,
    		secondTime    : secTime,
			delay         : pDelay,
			likeString    : $('#searchData').val(),
			waybillStatus : pFettles	
		})
	}	
});


function getMytaskList(options,index,tops){
	var baseValue = {
		num		      : options.num,
		size          : options.size,
    	simpleTime    : options.simpleTime || '',
    	firstTime     : options.firstTime || '',
    	secondTime    : options.secondTime || '',
    	delay         : options.delay || '',
    	likeString    : options.likeString || '',
    	waybillStatus : options.waybillStatus || ''
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
			$('.weui-loadmore').html('已经到底了');

		}else{
			for(var item in results){
				var mytaskHtml = $('#myTaskList').render(results[item]);
				
				$('.task-list-item-title').each(function(index,ele){
					if(this.innerHTML == item){ //合并相同的日期
						item = '';
						return;
					}
				})
				var mytaskTitle = "<p class='task-list-item-title'>" + item + "</p>";
				$('.task-list-con').append("<div class='task-list-item'>" + mytaskTitle+ "<div class='task-list-item-con'>" + mytaskHtml + "</div>" + "</div>");
			}
			dLoading = true;
			
		}
		$('.weui-loadmore').hide();
		
		//没日期的p标签去掉样式
		$('.task-list-item-title').each(function(){
			if(this.innerHTML == ''){
				this.style.padding = '0';
			}
		})
		
		//点击跳转到详情页
		$('.task-list-item-part').click(function(){
			var uid = $(this).attr('uid');
			window.location.href = _common.version('./myTaskDetails.html?waybillId='+uid);	
		})
		
		//点击上传回单跳转到上传回单页
		$('.upload-task-link').click(function(event){
			var uploadId = $(this).parent('.task-list-item-part').attr('uid');
			window.location.href = _common.version('./upload.html?waybillId='+uploadId);
			event.stopPropagation(); //阻止事件冒泡
		})
		
		//缓存定位部分
		if(index){
			if(index == 1){
				$(document).scrollTop(tops);
			}else{
				index --;
				options.num++;
				getMytaskList(options,index,tops);
			}
		}
	});
}

function searchMytaskList(){
	$('.task-list-con').html('');
	 $('.weui-loadmore').show();
	dNum = 1;
	getMytaskList({
		num           : dNum,
		size          : pageSize,
		simpleTime    : simTime,
    	firstTime     : firTime,
    	secondTime    : secTime,
		delay         : pDelay,
		likeString    : $('#searchData').val(),
		waybillStatus : pFettles	
	})
}
//截取年月日中的月份日期
function getDateStr(dateStr){
    var reg = /^(\d{4})-(\d{1,2})-(\d{1,2})$/;
    dateStr.match(reg);
    return RegExp.$2+'.'+RegExp.$3;
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

//根据id获得项目组名字
function getValName(obj,idx) {
    for(var i=0;i<obj.length;i++){
        if(obj[i].value == idx){
            return obj[i].title;
        }
    }   
}