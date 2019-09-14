
$("#inputfocus").val(_common.getUrlParam('companyName'));
	
$("#showTooltips").click(function() {
    var companyName= $.trim($('#inputfocus').val());
    console.log(companyName)
    if(companyName.length == 0){
    	$.toptip('企业名称必填');
    	return;
    }else{
    	getCompanyName();
    }

});

//提交修改的用户名
function getCompanyName(){
	var baseValue = {
		companyName : $("#inputfocus").val() 
	}
	EasyAjax.ajax_Post_Json({
		url:'/enterprise/company/modify/name/',
		data:JSON.stringify(baseValue)
	},function(res){
		console.log(res)
		 window.location.href=_common.version("./peopleCenter.html");
	});
}
