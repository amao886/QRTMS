//页面初始化
//页面初始化
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
    	window.location.href = _common.version('../partener/contactList.html?jstatus='+1);
    })
    
    //跳转到个人中心
    $('#peopleCenter').on('click',function(){
    	window.location.href = _common.version('peopleCenter.html?jstatus='+2 + '&identity=' + _common.getUrlParam('identity'));
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

$("#content").height($(window).height() - 52);

getMyCenterInfo();

function getMyCenterInfo() {
	EasyAjax.ajax_Post_Json({
		url: 'mobile/mine/user/info',
	}, function(res) {
		console.log(res);
		var myCenterHtml = $('#peopleCenterInfo').render(res.user);
		$('.centerBox').append(myCenterHtml);

		//点击跳转页面
		$('.weui-cell_access:eq(0)').click(function() {
			window.location.href = _common.version('./peopleNameEdit.html?uname=' + res.user.unamezn);
		})
		$('.weui-cell_access:eq(1)').click(function() {
			window.location.href = _common.version('./peoplePhoneEdit.html?phone=' + res.user.mobilephone);
		})

		var personAuthHtml = $('#peopleAuthInfo').render(res.user);
		$('.personAuthBox').append(personAuthHtml);

		var myCompanyHtml = $('#myCompanyInfo').render(res.user);
		$('.myCompanyBox').append(myCompanyHtml);

		if(isNull(res.user.companyName)) {
			$('.myCompanyBox').hide();
		}

		$('.weui-cell_access:eq(5)').click(function() {
			if (res.user.fettle > 0) {
				window.location.href = _common.version('./personRealNameAuthenticationInfo.html');
			}else{
				window.location.href = _common.version('./personRealNameAuthentication.html?type=');
			}
		})
	});
}

function isNull(text) {
	return !text && text !== 0 && typeof text !== "boolean" ? true : false;
}