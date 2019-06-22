//页面初始化
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