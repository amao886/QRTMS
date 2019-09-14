$("#showTooltips").click(function() {
	var name = $("#name").val();
	var nameReg = /^[\u4E00-\u9FFF]{2,15}$/;
	if(!name) {
		$.toptip('姓名不能为空');
		return;
	} else if(!nameReg.test(name)) {
		$.toptip('姓名只能输入中文');
		return;
	}

	var idcardNum = $("#idcardNum").val();
	var idcardNumReg1 = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
	var idcardNumReg2 = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
	if(!idcardNum) {
		$.toptip('身份证号码不能为空');
		return;
	} else {
		if(idcardNum.length == 15) {
			if(!idcardNumReg1.test(idcardNum)) {
				$.toptip('请输入正确的身份证号码');
				return;
			}
		} else {
			if(!idcardNumReg2.test(idcardNum)) {
				$.toptip('请输入正确的身份证号码');
				return;
			}
		}
	}

	var bankcardNum = $("#bankcardNum").val();
	if(!bankcardNum) {
		$.toptip('银行卡号不能为空');
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

	var baseValue = {
		employeeName: name,
		idCardNo: idcardNum,
		brankCardNo: bankcardNum,
		mobilePhone: mobileNum,
		code: code
	}
	EasyAjax.ajax_Post_Json({
		url: '/enterprise/company/personalAuth',
		data: JSON.stringify(baseValue)
	}, function(res) {
		var type = _common.getUrlParam("type");
		if(type == "signature") {
			window.location.href = _common.version("./choosePersonSignature.html");
		} else if(type == "withdraw") {
			window.location.href = _common.version("./applyWithdraw.html");
		}
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
		url: '/mobile/mine/smsCode/' + phone,
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