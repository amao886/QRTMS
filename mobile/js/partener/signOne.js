var ordrId = _common.getUrlParam('id');
var name = _common.getUrlParam('name');
var phone = _common.getUrlParam('phone');

showInfo();

function showInfo() {
	$("#nameText").html(name);
	$("#phoneNum").html(phone);
}

$("#showTooltips").click(function() {
	var code = $('#code').val();
	if(!code) {
		$.toptip('验证码不能为空');
		return;
	}

	window.location.href = _common.version('./signTwo.html?code=' + code + '&id=' + ordrId + '&name=' + name + '&phone=' + phone);
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
	var result = ordrId;
	if(result) {
		doPostBack();
		settime(obj); //开始倒计时
	}
}

//将手机利用ajax提交到后台的发短信接口
function doPostBack() {
	var baseValue = {
		orderKey: ordrId
	}
	EasyAjax.ajax_Post_Json({
		url: 'enterprise/sign/send/code/',
		data: JSON.stringify(baseValue)
	}, function(res) {

	});
}