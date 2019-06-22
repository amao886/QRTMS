
$("#old-tel").val(_common.getUrlParam('phone'));

//发送验证码
$(function(){
	 $('#getCode').click(function(){
	    sendCode(this);
	 });
});

$("#showTooltips").click(function() {
	var tel = $('#tel').val();
	var code = $('#code').val();
		if(!tel || !/1\d{10}$/.test(tel)){
			$.toptip('请输入正确的手机号');
		} else if(!code){
			$.toptip('请输入短信验证码');
		} else{
			var baseValue = {
			    phone: tel,
			    code :code
			 }
			EasyAjax.ajax_Post_Json({
				url:'/mobile/mine/user/editPhone',
				data:JSON.stringify(baseValue)
			},function(res){
				window.location.href=_common.version("./peopleCenter.html");
			});
		}
});


var countdown=60;  
function settime(obj) {
    if (countdown == 0) { 
        obj.removeAttribute("disabled");    
        obj.innerHTML="获取验证码"; 
        countdown = 60; 
        return;
    } else { 
        obj.setAttribute("disabled", true); 
        obj.innerHTML= "重新获取("+countdown+")"; 
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



