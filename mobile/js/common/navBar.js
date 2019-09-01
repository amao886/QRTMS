/**
 * 引入公共部分
 * 导航部分
 */
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
    	window.location.href = _common.version('peopleCenter.html?jstatus='+2);
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