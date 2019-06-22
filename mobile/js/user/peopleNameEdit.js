
$("#inputfocus").val(_common.getUrlParam('uname'));
	
$("#showTooltips").click(function() {
    var userName = $.trim($('#inputfocus').val());
    if(userName.length == 0){
    	$.toptip('用户昵称必填');
    	return;
    }else{
    	getPeopleName();
    }

});

//提交修改的用户名
function getPeopleName(){
	var baseValue = {
		uname    : $("#inputfocus").val() 
	}
	console.log(baseValue)
	EasyAjax.ajax_Post_Json({
		url:'/mobile/mine/user/editName',
		data:JSON.stringify(baseValue)
	},function(res){
		console.log(res)
       if(res.success){
         	window.location.href=_common.version("./peopleCenter.html");
       }
	});
}
