'use strict';
var groupId = _common.getUrlParam('groupId');
var needId;   //群成员表主键ID
//页面初始化
getProjectDetail();

//更多
$('#showMoreList').on('click',function(){
	if($('.perMember').eq(4) && $('.perMember').eq(4).is(':hidden')){
		$('.perMember:gt(3)').show();
		$(this).children('p').html('<<收起');
	}else{
		$('.perMember:gt(3)').hide();
		$(this).children('p').html('更多>>');
	}
	
})

function getProjectDetail(_loginId){
	EasyAjax.ajax_Post_Json({
		url:'mobile/mine/group/detail/'+groupId
	},function(res){
		
		var list = res.results.list;		
		var _userid = res.results.userid; //组长Id
		var _loginId = res.userid;//当前用户Id
		console.log(_userid);
		console.log(_loginId);
        
        //渲染组成员列表
        
		if(list != null && list.length != 0){
			var listHtml = $('#getMemberList').render(list);
			$(listHtml).insertBefore('#memberDel');	
			
			//4个成员后就隐藏
			$('.perMember:gt(3)').hide();
			if(list.length > 4){
	        	$('#showMoreList').show();
	        }
			
			for(var i=0;i<list.length;i++){
				if(list[i].user != null){
					if(_loginId == list[i].user.id){
						needId = list[i].id;
					}
					
				}
				
			}
		}
		
		//渲染项目组其他信息
		var _proInfo = $('#getProInfo').render(res.results);
		$(_proInfo).insertAfter("#detailCon");
		
		//判断当前用户的权限
		if(_loginId == _userid){//如果当前用户是否是组长，如果是：显示删除组成员
			$('#memberDel').show().attr("href","projectTeamMemberDel.html?groupId="+groupId);
		}else{//否则：显示退组
			$('#delGroupCon').show();
		}
		
		$("#loginIdInput").val(_loginId);
		
		//删除并退出项目组
		console.log(needId);
		$('#delTeam').click(function(){
			$.confirm({
					title: '确认退出项目组？',
					text: '',
					onOK: function () {
					    delTeam(needId);
					}
				});
		})
		
	})	
	
}

function memberView(obj){
	var _loginId = $("#loginIdInput").val();
	var _url = $(obj).attr("data-link") + _loginId;
	
	$(obj).attr("href",_url);
}

//删除并退出项目组
function delTeam(id){
	EasyAjax.ajax_Post_Json({
		url:'mobile/mine/delete/groupUser/'+id
	},function(res){
		window.location.href = _common.version('./myProjectTeam.html');
	});
}

