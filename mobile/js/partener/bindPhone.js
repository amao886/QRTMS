 var state    = _common.getUrlParam('state'),
     bindCode = _common.getUrlParam('bindCode'),
     groupId  = _common.getUrlParam('groupId'),
     shareId  = _common.getUrlParam('shareId'),
     waybillId= _common.getUrlParam('waybillId');
//发送验证码
$(function(){
	 $('#getCode').click(function(){
	    sendCode(this);
	 });
});

$("#showTooltips").click(function() {
	var _this = this;
	var tel = $('#tel').val();
	var code = $('#code').val();
		if(!tel || !/1\d{10}$/.test(tel)){
			$.toptip('请输入正确的手机号');
		} else if(!code){
			$.toptip('请输入短信验证码');
		}else if(code.length != 6){
			$.toptip('验证码长度必须为6位');
		} else{
			_common.showLoad(_this,1);
			var baseValue = {
			    phone: tel,
			    code :code
			 }
			EasyAjax.ajax_Post_Json({
				url:'mobile/mine/bindphone',
				data:JSON.stringify(baseValue)
			},function(res){
				console.log(state)
				_common.showLoad(_this,0);
				if(res.success){
					/*
					switch (state){
					case "INDEX":	window.location.href=_common.version('../../index.html?state='+state);   //首页入口
						break;
					case "CENTER":	window.location.href=_common.version('../../view/partener/peopleCenter.html?state='+state);    //个人中心入口
						break;
					case "SCAN":	window.location.href=_common.version(api_host+'mobile/wechat/scan/'+bindCode+'?state='+state); //扫码入口
						break;
					case "SHARE":	window.location.href=_common.version(api_host+'mobile/wechat/share/'+shareId+'/'+waybillId+'?state='+state);
						break;
					case "GROUP":	window.location.href=_common.version(api_host+'mobile/wechat/group/'+groupId+'?state='+state);			
						break;
					default:        window.location.href=_common.version("../../index.html");
						break;
					}
					*/
					window.location.href= decodeURIComponent(_common.getUrlParam('callback'));
				}
			},function(res){
				_common.showLoad(_this,0);
			});
		}
});

var countdown=60;  
function settime(obj) {
    if (countdown == 0) { 
        obj.removeAttribute("disabled");    
        obj.innerHTML="获取验证码"; 
        $(obj).css('color','#31acfa');
        countdown = 60; 
        return;
    } else { 
        obj.setAttribute("disabled", true); 
        obj.innerHTML= "重新获取("+countdown+")"; 
        $(obj).css('color','#bbb');
        countdown--; 
    } 
    setTimeout(function() { 
        settime(obj) 
    },1000)
}

  //发送验证码
function sendCode(obj){
    var phonenum = $("#tel").val();
    var result = isPhoneNum();
    if(result){
        doPostBack(phonenum) ;         
        settime(obj);//开始倒计时
    }
}
       
 //将手机利用ajax提交到后台的发短信接口
 function doPostBack(phone) {
    var baseValue = {
		phone: phone
	}
	EasyAjax.ajax_Post_Json({
		url:'/mobile/mine/smsCode/'+phone,
		data:JSON.stringify(baseValue)
	},function(res){

	});
}

//校验手机号是否合法
function isPhoneNum(){
    var phonenum = $("#tel").val();
    if(phonenum=="" || !/1\d{10}$/.test(phonenum)){
    	$.toptip('请输入正确的手机号');
    	return false;
    }else{
        return true;
    }
}



