var originParam = _common.getUrlParam('monitorParam'); //带这个参数，表明从发货监控页跳转过来
var monitorPageParam = JSON.parse(sessionStorage.getItem('monitorPageStore')); //获取发货监控页存储的数据
var dNum = 1,
    Psize = 5,
    dLoad = true,
    projectId='',  //项目组id、发货时间、延迟状态、任务状态
	pDelay='',
	pFettles='30',
	simTime='3',
	firTime='',
	secTime='',
	rangeTime = '', //存储时间段
	prevTime=''; //存储单独日期
console.log('发货页:'+originParam);
console.log(monitorPageParam);

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
window.onload = function () {
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
	    	searchTaskList();
    	}
    	
    })
    
    //解决ios readonly兼容问题
    $('input[readonly]').on('focus', function() {
	    $(this).trigger('blur');
	});

    //点击手机搜索按钮搜索
    $('#searchData').keyup(function (ev) {
        if (ev.keyCode == 13) {
            if ($.trim($('#searchData').val())) {
                searchTaskList();
            } else {
                $.toast('请先输入搜索内容', 'text');
            }
        }
    })

    //点击清空input框内容
    $('#searchClear').on('click tap', function () {
        $('#searchData').val('');
        searchTaskList();
    });
   
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
	
	//延迟状态
	$('#delayStatus').select({
	  	title: "延迟状态",
  		items: delayArr,
		onChange: function(data) {
          pDelay = data.values;
          console.log('延迟状态：'+pDelay)
          searchTaskList();
        }
	});
};

//滑动存储
$(window).scroll(function () {
    //滑动时存储下来
    var totalheight = $(window).scrollTop();
    console.log('tops:' + totalheight);
    if (window.sessionStorage) {
    	sessionStorage.setItem("ptPageTop", totalheight);
        sessionStorage.setItem("ptPageNum", dNum);  //页数
        sessionStorage.setItem("ptPageId", projectId); //项目组id
        sessionStorage.setItem("ptPageSimtime", simTime); //simple时间
        sessionStorage.setItem("ptPageFirtime", firTime); //开始时间
        sessionStorage.setItem("ptPageSectime", secTime); //结束时间
        sessionStorage.setItem("ptPageText", $('#searchData').val()); //搜索内容
        sessionStorage.setItem("ptPageDelay", pDelay); //延迟状态
        sessionStorage.setItem("ptPageStatus", pFettles); //任务状态        
       console.log('num:'+sessionStorage.getItem('ptPageNum'));
    }
});

//下拉刷新
$(document.body).infinite(0).on("infinite", function () {
    if (dLoad) {
        dLoad = false;
        dNum++;
        $('.weui-loadmore').show();
        getMytaskList({
	        num: dNum,
	        size: Psize,
	        groupId: projectId,
	        simpleTime: simTime,
        	firstTime: firTime,
        	secondTime: secTime,
	        likeString: utf16toEntities($('#searchData').val()),
	        fettles: pFettles,
	        delay: pDelay
	    })
    }
});

//获取后台数据
function getMytaskList(options, index, tops) {
    var baseValue = {
        num: options.num,
        size: options.size,
        groupId: options.groupId || '',
        simpleTime: options.simpleTime,
        firstTime: options.firstTime,
        secondTime: options.secondTime,
        delay: options.delay || '',
        fettles: options.fettles || '',
        likeString: options.likeString || '',
        rptFettles: options.rptFettles || ''
    }
    EasyAjax.ajax_Post_Json({
        url: 'mobile/wayBill/group/search',
        data: JSON.stringify(baseValue)
    }, function (res) {
        var curUserId = res.userid;
        var results = res.page.attachment;
        if (dNum == 1 && (results == null || results.length == 0)) {
            $('.task-list-con').append('<p style="text-align:center;padding:10px 0;font-size:15px;">暂无数据</p>');
            $('.weui-loadmore').hide();
        } else {
            if ((res.page.pageNum < res.page.pages) || (res.page.pageNum == res.page.pages)) {
                for (var item in results) {
                    var mytaskHtml = $('#task-list-items').render(results[item]);

                    $('.task-list-item-title').each(function (index, ele) {
                        console.log('日期：' + this.innerHTML);
                        if (this.innerHTML == item) { //合并相同的日期
                            item = '';
                            return;
                        }
                    })

                    var mytaskTitle = "<p class='task-list-item-title'>" + item + "</p>";
                    $('.task-list-con').append("<div class='task-list-item'>" + mytaskTitle + "<div class='task-list-item-con'>" + mytaskHtml + "</div>" + "</div>");
                }

                //判断是否显示绑定任务单编辑按钮
                $('.toGoodsPage').each(function (index, el) {
                    var $id = $(this).attr('uid');
                    var $status = $(this).attr('data-status');
                    if (curUserId == $id && $status == 20) {
                        $(this).show();
                    }
                })

                dLoad = true;
                //alert('数据加载完成');

            } else {
                //alert('已经到底了。。。。。。')
                $('.weui-loadmore').show().html('已经到底了');
            }
        }
        $('.weui-loadmore').hide();

        //没日期的p标签去掉样式
        $('.task-list-item-title').each(function () {
            if (this.innerHTML == '') {
                this.style.padding = '0';
            }
        })

        //点击跳转页面
        $('.task-list-item-part').on('click tap', function () {
            var uid = $(this).attr('uid');
            var status = $(this).attr('status');
            window.location.href = _common.version('./myTaskDetails.html?waybillId=' + uid + '&sts=' + status);
        });

        //开始定位
        if (index) {
            if (index == 1) {
                //alert('开始定位')
                $(document).scrollTop(tops);
            } else {
                index--;
                options.num++;
                getMytaskList(options, index, tops);
            }
        }

    });
}

/*获取项目组名称*/
function getItemName() {
    EasyAjax.ajax_Post_Json({
        url: 'mobile/mine/group/list'
    }, function (data) {
        var dataArr = data.groups;
        if (dataArr == null || dataArr.length == 0) {
            $('#task-list').html('<p style="text-align:center;padding:10px 0;font-size:15px;">暂无数据</p>');
            $('.weui-loadmore').hide();
        } else {
			var projectName = [{
				title: '全部',
				value: ''
			}];
        	for(var i=0;i<dataArr.length;i++){
        		projectName.push({
        			title: dataArr[i].groupName,
        			value: dataArr[i].id
        		})
        	}
        	projectName.push({
        		title: '其他',
        		value: '0'
        	})       	
            //获取项目组成功后，初次加载数据
            if(!originParam){
            	//首页直接点进来
            	$('#projectName').val(dataArr[0].groupName);
				projectId = dataArr[0].id;
            }else{
            	//发货监控页点进来，有groupId，显示项目组，没有，显示其他
            	console.log(projectName);
        		$('#projectName').val(getValName(projectName,monitorPageParam.groupid));        	
            }
			
			
			
		    //项目组
			$("#projectName").select({
			  	title: "项目组名称",
		  		items: projectName,
		  		onChange: function(data) {
		         	projectId = data.values;
		          	console.log('项目组id:'+projectId);
		          	searchTaskList();
		        }
			});

            //定位到当前位置
            if (sessionStorage.getItem("ptPageNum")) {  //有缓存
                //alert('走缓存');
                var tops = parseInt(sessionStorage.getItem("ptPageTop")); //距离
                var num = sessionStorage.getItem("ptPageNum"); //页数
                var gid = sessionStorage.getItem("ptPageId"); //项目组id
                var simpletime = sessionStorage.getItem("ptPageSimtime"); //时间
                var firsttime = sessionStorage.getItem("ptPageFirtime"); //时间
                var secondtime = sessionStorage.getItem("ptPageSectime"); //时间
                var stext = sessionStorage.getItem("ptPageText"); //搜索内容
                var delay = sessionStorage.getItem("ptPageDelay"); //延迟状态
                var status = sessionStorage.getItem("ptPageStatus"); //任务状态
                tops = tops ? tops : 0;
                num = num ? num : 1;

                //定位+赋值
                //****** 这里要重新赋下值
                dNum = num;
                projectId = gid;
                pDelay = delay;
                pFettles = status;                
                $('#searchData').val(stext);  //搜索内容
                //发货时间展示
                if(simpletime && (simpletime >= 0)){
                	$('#sendTime').val(getValName(sendTimeArr,simpletime));
                }else if(simpletime && (simpletime == '-2')){
                	$('#sendTime').val(getDateStr(firsttime)+'-'+getDateStr(secondtime));
                }
                
                $('#projectName').val(getValName(projectName,gid))  //项目组展示
                $('#fettles').val(getValName(fettlesArr,status));  //任务状态展示
                $('#delayStatus').val(getValName(delayArr,delay));  //延迟状态展示
                console.log('任务状态：'+ getValName(fettlesArr,status));
                
                
                getMytaskList({
                    num: 1,
                    size: Psize,
                    groupId: gid,
                    simpleTime: simpletime,
        			firstTime: firsttime,
        			secondTime: secondtime,
                    likeString: stext,
                    fettles: status,
                    delay: delay
                }, num, tops);
                
            } else { //没有缓存
                console.log('没有缓存时');
                    if(originParam){
				    	//从发货页来
				    	console.log('从发货页来')
				    	projectId = monitorPageParam.groupid;
				    	simTime = monitorPageParam.moreTag;
				    	firTime = monitorPageParam.sendTime;
				    	secTime = monitorPageParam.sendTime;
				    	pDelay = monitorPageParam.delayStatus;
				    	pFettles = monitorPageParam.bindStatus;
				    	
				    	$('#sendTime').val(firTime); //发货时间
				    	if(pFettles == '20'){ //任务状态
				    		$('#fettles').val('已绑定'); 
				    		
				    	}else if(pFettles == '30') {
				    		$('#fettles').val('运输中');
				    	}
				    	
				    	if(pDelay){//延迟状态
				    		$('#delayStatus').val('延迟'); 
				    	}
				    	
				    	
				    	getMytaskList({
		                    num: dNum,
		                    size: Psize,
		                    groupId: projectId,
		                    simpleTime: simTime,
		        			firstTime: firTime,
		        			secondTime: secTime,
		                    likeString: $('#searchData').val(),
		                    fettles: pFettles,
		                    delay: pDelay
		                });
				    	
				    }else{
				    	//首页点击进入
				    	console.log('从首页进入')
				    	getMytaskList({
		                    num: dNum,
		                    size: Psize,
		                    groupId: projectId,		                    
		                    simpleTime: simTime,
		        			firstTime: firTime,
		        			secondTime: secTime,
		                    likeString: $('#searchData').val(),
		                    fettles: pFettles,  //默认的是运输中的状态
		                    delay: pDelay
		               });
				    }
                
            }
        }
    });
}

//选择条件触发
function searchTaskList() {
    $('#task-list').html('');
    dNum = 1;
    $('.weui-loadmore').show();
    getMytaskList({
        num: dNum,
        size: Psize,
        groupId: projectId,
        simpleTime: simTime,
        firstTime: firTime,
        secondTime: secTime,
        likeString: utf16toEntities($('#searchData').val()),
        fettles: pFettles,
        delay: pDelay
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



