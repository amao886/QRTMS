var tokenTag = _common.getUrlParam('token');  //判断是否是扫二维码添加组成员

//页面初始化
getMyProjectList();

function getMyProjectList(){
	EasyAjax.ajax_Post_Json({
		url:'mobile/mine/group/list/detail'
	},function(res){
		if(tokenTag){
			layer.open({
			  	type: 1,
			  	title: false,
			  	closeBtn: 0,
			  	area: '90%',
			  	skin:'tipRadius',
			  	time:2000,
			  	shadeClose: true,
			  	content: $('.successTips')
			});
		}
		var listHtml = $('#myTeamList').render(res.groups);
		$('#myTeamListCon').append(listHtml);
		$('.demos-content-padded').show();
	})		
}

