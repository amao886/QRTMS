$("#navBarIn").load("../common/navBarIn.html",function () {
    $(".weui-tabbar__item:first").addClass("weui-bar__item--on");
    
	$('.a-hook').on('click',function(){
    	$('.a-hook').removeClass('active');
    	$(this).addClass('active');     	
    })
    
	//跳转到首页
    $('#myIndex').on('click',function(){
    	window.location.href =_common.version('../../home.html?jstatus='+0);
    })
    
    //跳转到扫一扫
    $('#swapCircle').on('click',function(){
    	scanCode();
    })
    
    //跳转到通讯录
    $('#conList').on('click',function(){
    	window.location.href = _common.version('contactList.html?jstatus='+1);
    })
    
    //跳转到个人中心
    $('#peopleCenter').on('click',function(){
    	window.location.href = _common.version('../user/peopleCenter.html?jstatus='+2 + '&identity=' + _common.getUrlParam('identity'));
    })
    
    var jstatus = _common.getUrlParam('jstatus');
    console.log('jstatus:'+jstatus);
    if(jstatus == '0'){
    	//首页
    }else if(jstatus == '1'){
    	//通讯录
    	$('.a-hook').removeClass('active');
    	$('#conList').addClass('active');
    }else if(jstatus == '2'){
    	//个人中心
    	$('.a-hook').removeClass('active');
    	$('#peopleCenter').addClass('active');
    }
});

//初始化数据
getFriendList();

//点击搜索框
$('#showSearch').on('click',function(){
	$('#searchData').val('');
	$('#searchBgBox').css({
				   			'background':'rgba(0,0,0,.5)',
				   			'bottom':'0',
				   			'height':'100%'
				   		}).show();
})

//点击返回
$('#search-cancel').on('click',function(){
	$(this).parents('#searchBgBox').hide();
	getFriendList();
	//$('#navBar').css('display','block !important');
})

//点击添加好友
$('.addfriend').on('click',function(){
	window.location.href = _common.version('./addFriends.html');
})

//点击未注册
$('#list').delegate('.dd-item','click',function(){
	var uid = $(this).attr('uid'),
		hasHide = $(this).find('.reg-stutas')[0]; //未注册判断
	window.location.href = _common.version('./contactListPeople.html?friendkey='+uid);
})

//点击搜索和enter
$('#search-btn').on('click',function(){
	var sts = validatePhone($('#searchData').val());
	if(sts){
		getFriendList($.trim($('#searchData').val()));
	}
})

$('#searchData').keyup(function(ev){	
	if(ev.keyCode == 13){
		var sts = validatePhone($(this).val());
		if(sts){
			getFriendList($.trim($('#searchData').val()));
		}
	}
})

//点击清空按钮
$('#searchClear').on('click',function(){
	$('#searchData').val('');
	getFriendList();
})

//获取数据
function getFriendList(mobilePhone){
	var baseValue = {
	    likeString : mobilePhone || ''
	 }
	EasyAjax.ajax_Post_Json({
		url:'mobile/friends/query/friends',
		data:JSON.stringify(baseValue)
	},function(res){
	   	var provinces = res.results;
	   	console.log('provinces：'+$.isEmptyObject(provinces));
	   	if(!$.isEmptyObject(provinces)){
	   		$('#list').myList(provinces);
	   	}else{
	   		$('#list').html('<p style="text-align:center;padding:10px 0;font-size:15px;">暂无数据。。。</p>');
	   	}	   	
	   	//搜索时背景隐藏
	   	if(!$('#searchBgBox').is(':hidden')){
	   		$('.search-bg-box').css({
	   			'background':'none',
	   			'bottom':'auto',
	   			'height':'auto'
	   		});	   		
	   		$('#navBar').hide();
	   	}else{
	   		$('#navBar').show();
	   	}
	   	
	   	
   });	
}

//验证手机号是否为空或是否是11位
function validatePhone(phone){
	var phone = $.trim(phone);
	if(phone){
		return true;
	}else{
		$.toast('搜索内容不能为空','text');
		return false;
		
	}
}
