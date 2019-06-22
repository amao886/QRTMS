
getPersonInfo();

function getPersonInfo() {
	EasyAjax.ajax_Post_Json({
		url: '/admin/user/info ',
	}, function(res) {
		console.log(res);
		var myCenterHtml = $('#peopleCenterInfo').render(res);
		$('.centerBox').append(myCenterHtml);

		//点击跳转页面
		$('.weui-cell_access:eq(0)').click(function() {
			window.location.href = _common.version('./peopleNameEdit.html?uname=' + res.unamezn);
		})
		$('.weui-cell_access:eq(1)').click(function() {
			window.location.href = _common.version('./peoplePhoneEdit.html?phone=' + res.mobilephone);
		})

		
	});
}