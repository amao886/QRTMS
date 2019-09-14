var elecListData = {
	signStatus: '3', //签收状态默认值
	abnormalStatus: '', //异常状态默认值,默认近三天
	dateStatus: '3', //时间默认值
	pageNum:1, //当前页
	pageSize: 4,  //每页传几条数据
	dLoading: false,
	signArr: [
	  	{
	    	title: "全部",
	    	value: ""
	  	},
	  	{
	    	title: "未生成电子回单",
	    	value: "0"
	  	},
	  	{
	    	title: "物流商未签",
	    	value: "2"
	  	},
	  	{
	    	title: "收货方未签",
	    	value: "3"
	  	},
	  	{
	    	title: "全部已签",
	    	value: "4"
	  	}
	],
	abnormalArr: [
		{
	    	title: "全部",
	    	value: ""
	  	},
	  	{
	    	title: "有异常",
	    	value: "3"
	  	},
	  	{
	    	title: "无异常",
	    	value: "0"
	  	}
	],
	dateArr: [
		{
	    	title: "近三天",
	    	value: "3"
	  	},
	  	{
	    	title: "近一周",
	    	value: "7"
	  	},
	  	{
	    	title: "近一月",
	    	value: "30"
	  	},
	  	{
	    	title: "全部",
	    	value: ""
	  	}
	]
}

$(function(){
	var storeData = JSON.parse(sessionStorage.getItem('scrollData'));
	if(storeData){ //走缓存
		elecListData.pageNum = 1;
		elecListData.signStatus = storeData.signstatus;
		elecListData.abnormalStatus = storeData.abnstatus;
		elecListData.dateStatus = storeData.datestatus;
		document.querySelector('#signStatus').value = getValName(elecListData.signArr,elecListData.signStatus);
		document.querySelector('#abnormalStatus').value = getValName(elecListData.abnormalArr,elecListData.abnormalStatus);
		document.querySelector('#dateStatus').value = getValName(elecListData.dateArr,elecListData.dateStatus);
		if(storeData.searchmsg){ //input有值的时候
			$('.searchIcon').addClass('open');
			$('.searchInput').show().find('#inputSearch').val(storeData.searchmsg); //给input赋值
		}
		
		getListData(storeData.num, storeData.top);
		
	}else{
		//获取数据
		getListData();
	}
	
	
	//签收状态
	$("#signStatus").select({
		title: "签收状态",
		items: elecListData.signArr,
		onChange: function(val) {
			elecListData.signStatus = val.values;
			searchListData();
		  	console.log('elecListData.signStatus:'+elecListData.signStatus);
		},
	});
	
	//是否异常
	$("#abnormalStatus").select({
		title: "异常状态",
		items: elecListData.abnormalArr,
		onChange: function(val) {
			elecListData.abnormalStatus = val.values;
			searchListData();
		  	console.log('elecListData.abnormalStatus:'+elecListData.abnormalStatus);
		},
	});
	
	//日期选择
	$("#dateStatus").select({
		title: "时间",
		items: elecListData.dateArr,
		onChange: function(val) {
			elecListData.dateStatus = val.values;
			searchListData();
		  	console.log('elecListData.dateStatus:'+elecListData.dateStatus);
		},
	});
	
	//点击按钮搜索
	$('#iconClear').click(function(){
		$('#inputSearch').val('');
		searchListData();
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
	
	//点击出现搜索框
	$('.searchIcon').on('click',function(){
		if($(this).hasClass('open')){
			$('.searchInput').hide();
			$(this).removeClass('open');
		}else{
			$('.searchInput').show();
			$(this).addClass('open');
		}
	})
	
	//点击高亮
	$('.searchSelect').on('click',function(){
		$(this).addClass('active').siblings('.searchSelect').removeClass('active');
	})
	
	//解决ios readonly兼容问题
    $('input[readonly]').on('focus', function() {
	    $(this).trigger('blur');
	});
})


//获取数据
function getListData(index,tops){
	var baseValue = {
        receiptFettle: elecListData.signStatus,
        fettle: elecListData.abnormalStatus,
        timeType: elecListData.dateStatus, //simpleTime是我随便取的参数名，到时候只要改成正确的参数名就可以了,别的地方我都改好了
    	likeString: $('#inputSearch').val(),
    	num: elecListData.pageNum,
    	size: elecListData.pageSize
    }
    EasyAjax.ajax_Post_Json({
        //url: 'enterprise/order/shipper/manage/search',
        url: 'enterprise/order/concise/search',
        data: JSON.stringify(baseValue)
    }, function (res) {
    	if(!res.success){
    		$.toast("加载失败，请稍后重试", "text");
    		
    	}else{
    		var results = res.page.collection;
    		if(elecListData.pageNum == 1 && (results == null || results.length == 0)){ //第一页
    			$('.receipt-wrapper').append('<p style="text-align:center;font-size:15px;padding:10px 0;">暂无数据</p>');
    			
    		}else{
    			var listDataHtml = $('#listData').render(results);
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
					getListData(index,tops);
				}
            }
        }
    });
}

//重新查询数据
function searchListData(){
	$('.receipt-wrapper').html('');
	elecListData.pageNum = 1;
	getListData();
}

//跳转页面
function toPage(createTime , receiveKey, conveyKey){
    //存参
	var data = {
        "time" : createTime,
        "receiveKey" : receiveKey,
        "conveyKey": conveyKey
    };
	sessionStorage.setItem("elec-search-date-data", JSON.stringify(data));
    //跳转
    window.location.href = _common.version("elecSearchDate.html");
}

//滑动存储数据
$(document).scroll(function(){
	var totalheight = $(window).scrollTop();
	if(window.sessionStorage){
		var scrollData = {
			num: elecListData.pageNum,
			signstatus: elecListData.signStatus,
			abnstatus: elecListData.abnormalStatus,
			datestatus: elecListData.dateStatus,
			searchmsg: $.trim($('#inputSearch').val()),
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
		getListData();	
	}	
});


//根据value获得title
function getValName(obj,idx) {
    for(var i=0;i<obj.length;i++){
        if(obj[i].value == idx){
            return obj[i].title;
        }
    }   
}
