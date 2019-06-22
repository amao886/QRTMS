var groupid = _common.getUrlParam('dgroupid') || "0"; //获取收货客户数据必填参数
var bindCode = _common.getUrlParam('bindCode');
var taskId = _common.getUrlParam('taskId');  //任务单id
var fromPage = _common.getUrlParam('fromPage'); //0表示来自我要发货页;
var customerId = _common.getUrlParam('customerId'); //发货页发货人名称
var type = sessionStorage.getItem("customerType"); //发货页发货人/发货人类型

if(fromPage == 0){
	bindCode = '';
}

if(!isWeixin()){
	$('#navBarIn').hide();
}

//下拉刷新
var dNum = 1;
var dLoading = true;
var pageSize = 3;

//初始化页面数据
getCustomList({
	groupid  : groupid,
	bindCode : bindCode,
	num      : dNum,
	size     : pageSize,
	type     : type
}) 

//下拉翻页
$(document.body).infinite(0).on("infinite", function() {
	if(dLoading){
		dLoading=false;
		dNum++;
		$('.weui-loadmore').show();
		getCustomList({
			groupid  : groupid,
			bindCode: bindCode,
			num     : dNum,
			size    : pageSize,
			type    : type,
			companyName : utf16toEntities($('#searchInput').val())			
		});
	}	
});

//点击清空输入框内容
$('#searchClear').click(function(){
	$('#searchInput').val('');
	searchCusList();
})

//点击搜索查询
$('#searchBtn').click(function(){
	if($.trim($('#searchInput').val())){
		searchCusList();
	}else{
		$.toast('请输入查询内容','text');
	}
	
})

//点击键盘的搜索
$('#searchInput').keyup(function(ev){
	if(ev.keyCode === 13){
		if($.trim($('#searchInput').val())){
			searchCusList();
		}else{
			$.toast('请输入查询内容','text');
		}
	}
})

//获取页面数据
function getCustomList(options){
	var baseValue = {
		groupid       : options.groupid || "0",
		bindCode      : options.bindCode || '',
    	c_companyName : options.companyName || '' ,
    	pageNum       : options.num || '',
    	pageSize      : options.size || '',
    	type          : options.type
	}
	EasyAjax.ajax_Post_Json({
		url:'mobile/customer/page',
		data:JSON.stringify(baseValue)
	},function(res){
		if(dNum == 1 && (res.results == null || res.results.length == 0)){
			$('#customListCon').html("<p style='text-align:center;font-size:15px;padding:15px 0;'>暂无数据。。。</p>");
			$('.weui-loadmore').hide();
			
		}else if(dNum != 1 && (res.results == null || res.results.length == 0)){
			$('.weui-loadmore').show().html('已经到底了');	
			
		}else{
			var customListHtml = $('#customList').render(res.results);
			$('#customListCon').append(customListHtml);
			if(type == 2){//收货信息改变label信息
				$('#customListCon').find(".showPersonType").text("发货信息");
				$('#customListCon').find('.changeAddress').text('发货地址');
			}
			dLoading = true;
			$('.weui-loadmore').hide();
		}
   
		//选中客户后，返回条码绑定页
		$('.position_list').delegate('.sin_check_p','click',function(){		
			var $input = $(this).find('.sin_check');
			if($input.is(':checked')){			
				$input.prop("checked", false);
			}else{
				$input.prop("checked", true);
				var customerId= $(this).parents(".position_list").attr("id");								
				
				if(type == 2){
					sessionStorage.setItem("sendCustomID",customerId);
					
				}else if(type == 1){
					sessionStorage.setItem("receiveCustomID",customerId);
				}
				
				if(fromPage && (fromPage == 0)){//我要发货页
					window.location.href=_common.version('sendGoods.html?groupId='+groupid+'&taskId='+taskId+'&sendCid='+customerId);
					
				}else if(taskId == "null" || taskId == null || taskId == undefined){//绑定任务单
					window.location.href=_common.version('bindBarCode.html?bindCode='+bindCode+'&groupId='+groupid);	
					
				}else{//编辑任务单
					window.location.href=_common.version('editBarCode.html?groupId='+groupid+'&taskId='+taskId);
				}
				
			}
		})
		
		//新增客户
		$('#addCustom').click(function(){
			var customerId= $(this).parents(".position_list").attr("id");	
	        window.location.href=_common.version('addCustom.html?bindCode='+bindCode+'&taskId='+taskId+'&fromPage='+fromPage);
	    });
	    
	    //编辑客户
    	$('.position_list').delegate('.cus_edit','click',function(){
    		var id=$(this).attr("data");
    	    window.location.href=_common.version('editCustom.html?customerId='+id+'&groupId='+groupid+'&bindCode='+bindCode+'&taskId='+taskId+'&fromPage='+fromPage);
    	});
		
	})
}

function searchCusList(){
	$('#customListCon').html('');
	$('.weui-loadmore').show();
	dNum = 1;
	getCustomList({
		groupid  : groupid,
		bindCode : bindCode,
		num      : dNum,
		size     : pageSize,
		type     : type,
		companyName:$('#searchInput').val()	
	})	
}
