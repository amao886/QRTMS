$("#showTooltips").click(function() {
	var _this = this;
	var name = $("#name").val();
	var nameReg = /^[\u4E00-\u9FFF]{1,15}$/;
	if(!name) {
		$.toptip('姓名不能为空');
		return;
	} else if(!nameReg.test(name)) {
		$.toptip('姓名只能输入中文');
		return;
	}

	var mobileNum = $('#mobileNum').val();
	var mobileNumReg = /^1[\d]{10}$/
	if(!mobileNum) {
		$.toptip('手机号不能为空');
		return;
	} else if(!mobileNumReg.test(mobileNum)) {
		$.toptip('请输入正确的手机号');
		return;
	}

	var code = $('#code').val();
	if(!code) {
		$.toptip('验证码不能为空');
		return;
	}
	if(code.length != 6) {
		$.toptip('验证码长度必须为6位');
	}

	_common.showLoad(_this, 1);
	var baseValue = {
		uuid: _common.getUrlParam('uuid'),
		name: name,
		phone: mobileNum,
		code: code
	}
	EasyAjax.ajax_Post_Json({
		url: 'mobile/wechat/register',
		data: JSON.stringify(baseValue)
	}, function(res) {
		_common.showLoad(_this, 0);
		if(res.success) {
			$.toast(res.message, function() {
				window.location.href = decodeURIComponent(res.callback);
			});
		}
	}, function(res) {
		_common.showLoad(_this, 0);
	});
});

//发送验证码
$(function() {
	$('#getCode').click(function() {
		sendCode(this);
	});
});

var countdown = 60;

function settime(obj) {
	if(countdown == 0) {
		obj.removeAttribute("disabled");
		obj.innerHTML = "获取验证码";
		countdown = 60;
		return;
	} else {
		obj.setAttribute("disabled", true);
		obj.innerHTML = "重新获取(" + countdown + ")";
		countdown--;
	}
	setTimeout(function() {
		settime(obj)
	}, 1000)
}

//发送验证码
function sendCode(obj) {
	var phonenum = $("#mobileNum").val();
	var result = isPhoneNum();
	if(result) {
		doPostBack(phonenum);
		settime(obj); //开始倒计时
	}
}

//将手机利用ajax提交到后台的发短信接口
function doPostBack(phone) {
	var baseValue = {
		phone: phone
	}
	EasyAjax.ajax_Post_Json({
		url: '/special/smsCode/' + phone,
		data: JSON.stringify(baseValue)
	}, function(res) {

	});
}

//校验手机号是否合法
function isPhoneNum() {
	var phonenum = $("#mobileNum").val();
	if(phonenum == "" || !/1\d{10}$/.test(phonenum)) {
		$.toptip('请输入正确的手机号');
		return false;
	} else {
		return true;
	}
}