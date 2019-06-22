
//函数变量初始化
var dNum = 1, 
	Psize = 5, 
	dLoad = true, 
	projectId='',  //项目组id、发货时间、延迟状态、任务状态
	pFettles='35',
	simTime='3',
	firTime='',
	secTime='',
	rangeTime = '', //存储时间段
	prevTime=''; //存储单独日期
	
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
	  title: "已绑定",
	  value: "20",
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

window.onload = function(){
	//获取项目组名称
	getItemName();
	
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
    	}else if(simTime && !prevTime){ //第一次就点击更多    		
    		$('#sendTime').val(getValName(sendTimeArr,3));
    	}else{
    		$('#sendTime').val(getValName(sendTimeArr,prevTime));
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
	    	searchTaskList();
    	}
    	
    })
    
    //发货时间
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
			  	searchTaskList();
			}
            
        }
	});
	
	//任务状态
	$('#fettles').select({
	  	title: "任务状态",
  		items: fettlesArr,
		onChange: function(data) {
          pFettles = data.values;
          console.log('任务状态：'+pFettles);
          searchTaskList();
        }
	});
    
    //解决ios readonly兼容问题
    $('input[readonly]').on('focus', function() {
	    $(this).trigger('blur');
	});
	
	//点击搜索按钮搜索
	$('#searchData').keyup(function(ev){
		if(ev.keyCode == 13){
			if($.trim($('#searchData').val())){
				searchTaskList();
			}else{
				$.toast('请先输入搜索内容','text');
			}
		}
	})
	
	//点击清空input框内容
	$('#searchClear').on('click tap', function(){
	    $('#searchData').val('');
	    searchTaskList();
	})

}

//滚动时存储数据
$(window).scroll(function(){
    var totalheight = $(window).scrollTop();
    console.log('tops:'+totalheight);
   	if(window.sessionStorage){
       	sessionStorage.setItem("checkTop",totalheight); //距离
        sessionStorage.setItem("checkNum",dNum);  //页数
        sessionStorage.setItem("checkSimTime",simTime); //时间
        sessionStorage.setItem("checkFirTime",firTime); //时间
        sessionStorage.setItem("checkSecTime",secTime); //时间
        sessionStorage.setItem("checkText",$('#searchData').val()); //搜索内容
        sessionStorage.setItem("checkGid",projectId); //项目组
        sessionStorage.setItem("checkStatus",pFettles); //任务状态
   	}
   	
});

//获取列表接口
function getMytaskList(options,index,tops){
    var baseValue = {
        pageNum	   : options.num || '',
        pageSize   : options.size || '',
        groupid    : options.groupId    || '',
        simpleTime : options.simpleTime || '',
        firstTime  : options.firstTime  || '',
        secondTime : options.secondTime || '',
        bindstatus : options.bindstatus || '',
        delay      : options.delay      || '',
        searchData : options.searchData || ''
    }
    EasyAjax.ajax_Post_Json({
        url:'mobile/wayBill/listMyReceive',
        data:JSON.stringify(baseValue)
    },function(res){
		var results = res.page.attachment;		
		if(dNum == 1 && (results == null || results.length == 0)){
			$('.task-list-con').append('<p style="text-align:center;padding:10px 0;font-size:15px;">暂无数据。。。</p>');
			$('.weui-loadmore').hide();
			
		}else if(dNum != 1 && (results == null || results.length == 0)){
			$('.weui-loadmore').show().html('已经到底了');
			
		}else{
			for(var item in results){
				var mytaskHtml = $('#task-list-items').render(results[item]);
				
				$('.task-list-item-title').each(function(index,ele){
					console.log('日期：'+this.innerHTML);
					if(this.innerHTML == item){ //合并相同的日期
						item = '';
						return;
					}
				})
				
				var mytaskTitle = "<p class='task-list-item-title'>" + item + "</p>";
				$('.task-list-con').append("<div class='task-list-item'>" + mytaskTitle+ "<div class='task-list-item-con'>" + mytaskHtml + "</div>" + "</div>");
			}
			
			dLoad = true;
			$('.weui-loadmore').hide();
		}
		
		//没日期的p标签去掉样式
		$('.task-list-item-title').each(function(){
			if(this.innerHTML == ''){
				this.style.padding = '0';
			}
		})
		
        //点击跳转页面
        $('.task-list-item-part').on('click tap', function(){
            var uid = $(this).attr('uid');
            window.location.href = _common.version('./myTaskDetails.html?waybillId='+uid);
        });
        
        //缓存定位
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

//下拉刷新
$(document.body).infinite(0).on("infinite", function() {
    if(dLoad){
        dLoad = false;
        dNum ++;
        $('.weui-loadmore').show();
        getMytaskList({
            num        : dNum,
            size       : Psize,
			groupId    : projectId,
		    simpleTime : simTime,
	        firstTime  : firTime,
	        secondTime : secTime,
	        bindstatus : pFettles,
		    searchData : utf16toEntities($('#searchData').val())      	 	
        })
    }
});

/*获取项目组名称*/
function getItemName(){
    EasyAjax.ajax_Post_Json({
        url:'mobile/mine/group/list/contactNumber'
    },function(data){
        var dataArr = data.groups;
        if(dataArr !== null || dataArr.length !== 0){
        	
        	var projectName = [{title: '全部',value: ''}];
        	for(var i=0;i<dataArr.length;i++){
        		projectName.push({
        			title: dataArr[i].groupName,
        			value: dataArr[i].id
        		})
        	}
        	
        	$("#projectTeam").select({
			  	title: "项目组名称",
		  		items: projectName,
		  		onChange: function(data) {
		         	projectId = data.values;
		          	console.log('项目组id:'+projectId);
		          	searchTaskList();
		        }
			});
        	
        	//获取项目组成功后获取数据
	        if(sessionStorage.getItem("checkNum")){
	        	//有缓存
	        	//alert('有缓存');
		        var tops = parseInt(sessionStorage.getItem("checkTop")); //距离
		        var num = sessionStorage.getItem("checkNum"); //页数
		        var simpletime = sessionStorage.getItem("checkSimTime");
		        var firsttime = sessionStorage.getItem("checkFirTime");
		        var secondtime = sessionStorage.getItem("checSeckTime");
		        var stext = sessionStorage.getItem("checkText"); //搜索内容
		        var gid = sessionStorage.getItem("checkGid"); //项目组id
		        var status = sessionStorage.getItem("checkStatus"); //任务状态
		        tops = tops ? tops : 0;
		        num = num ? num : 1;
		        
		        //定位+赋值
		        dNum = num;
		        projectId = gid;
		        simTime = simpletime;
		        firTime = firsttime;
		        secTime = secondtime;
		        pFettles = status;
				$('#searchData').val(stext);  //搜索内容展示
                if(simpletime && (simpletime >= 0)){  //发货时间展示
                	$('#sendTime').val(getValName(sendTimeArr,simpletime));
                }else if(simpletime && (simpletime == '-2')){
                	$('#sendTime').val(getDateStr(firsttime)+'-'+getDateStr(secondtime));
                }
                $('#projectTeam').val(getValName(projectName,gid));  //项目组展示
                $('#fettles').val(getValName(fettlesArr,status))  //任务状态展示
                
		        getMytaskList({       	 	
		       	 	num        : 1,
					size       : Psize,
					groupId    : projectId,
				    simpleTime : simTime,
			        firstTime  : firTime,
			        secondTime : secTime,
			        bindstatus : pFettles,
				    searchData : stext  
		        },num,tops);
	        	
	        }else{
	        	//没有缓存
	        	//alert('没缓存');
				getMytaskList({
				    num        : dNum,
				    size       : Psize,
				    groupId    : projectId,
				    simpleTime : simTime,
			        firstTime  : firTime,
			        secondTime : secTime,
			        bindstatus : pFettles,
				    searchData : utf16toEntities($('#searchData').val())    
				})
	        }
        }
        

        
    });
}

function searchTaskList (){/*---选择条件触发的时候-----*/
    $('#task-list').html('');
    dNum = 1;
    $('.weui-loadmore').show();
    getMytaskList({
        num        : dNum,
        size       : Psize,
        groupId    : projectId,
        simpleTime : simTime,
        firstTime  : firTime,
        secondTime : secTime,
        bindstatus : pFettles,
        searchData : utf16toEntities($('#searchData').val())    
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