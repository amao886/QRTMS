'use strict';
var groupId = _common.getUrlParam('groupId');
var ids;
console.info(groupId);

gerMemberList();

//点击确定
$('#showTooltips').click(function(){
	if(ids){
		delMenber(ids);
	}else{
		$.toast('请先删除组成员','text');
	}
})

function gerMemberList(){
	EasyAjax.ajax_Post_Json({
		url:'mobile/mine/group/detail/'+groupId
	},function(res){
		
		var list = res.results.list;
		var _userId =res.results.userid;
		var guserId="";//组长id
		if(list != null){
			for(var item in list){
			//	console.info(list[item])
				if(list[item] != null){
					if(_userId==list[item].userid){
						guserId=list[item].id;
					}
					var listHtml = $('#getMemberList').render(list[item]);
					$("#memberListCon").append(listHtml);									
				}
			}
			//判断如果是组长则不进行删除
		$("#memberListCon").find("a[uid='"+guserId+"']").removeClass('delImage').find(".delP").remove();
			$('.delImage').click(function(){
				var $this = $(this);
				var uid = $(this).attr('uid');
				$.confirm({
					title: '确认删除',
					text: '',
					onOK: function () {
					    ids = uid;
					    $this.remove();
					}
				});
			})
		}
				
	})		
}

function delMenber(ids){
	EasyAjax.ajax_Post_Json({
		url:'/mobile/mine/delete/groupUser/'+ids
	},function(res){
		$("#memberListCon").empty();
		gerMemberList()		
	})
}
