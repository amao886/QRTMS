var friendId = _common.getUrlParam('friendId');

$("#showTooltips").click(function() {
	var name = $("#friendName").val();
	var nameReg = /^[a-zA-Z\u4E00-\u9FFF]{1,10}$/;
	if(!name){
		$.toptip('姓名不能为空');
		return;
	}else if(!nameReg.test(name)){
		$.toptip('姓名只能输入中文或字母');
		return;
	}
	
	submitFrdInfo();
});

//扫一扫添加，表单提交
function submitFrdInfo(){
	var baseValue = {
		    "pid": friendId,
   			"fullName": $("#friendName").val()
	}
	EasyAjax.ajax_Post_Json({
		url:'/mobile/friends/scan/add/friend',
		data:JSON.stringify(baseValue)
	},function(res){
		if(res.success){
			window.location.href = _common.version('../../view/partener/contactList.html');
		}
    });
}

    



