'use strict';
var userId   = _common.getUrlParam('userId');    //当前组成员id
var groupId  = _common.getUrlParam('groupId');  //项目组id
var loginId  = _common.getUrlParam('loginId');  //当前用户id
var roleCode = _common.getUrlParam('roleCode');  //角色编码
var dFlag;

//初始化权限
getRoleList()
function getRoleList(){
	EasyAjax.ajax_Post_Json({
		url:'mobile/mine/group/queryGrantRole'		
	},function(res){
		var dataArr = res.results;
		if(dataArr !=null || dataArr.length != 0){
			var listHtml = $('#roleList').render(res.results);
			$('#roleListCon').append(listHtml);	
		}
		
		//默认角色选中
		$.each(dataArr,function(index,ele){
			if(roleCode == this.role.roleCode){
				var index = this.role.id;
				$('.checkRadio').eq(index-1).prop("checked", true);
				$("#roleIdTxt").val(index);				
			}
		})
		
	    //组长权限变更，将组员权限变更为组长
	    $('.radio-style:first').click(function(){
	    	dFlag = false;
	    })
	    
	    //组员权限变更，将组员权限变为别的
	    $('.radio-style:gt(0)').click(function(){
	    	dFlag = true;
			
		})
		
	}) 
}

//点击展开效果
$(document).on("click",".roleItem",function(){
	var allRole = $('.rolePower');
	var roleList = $(this).parents('.roleItemCon').next();
	var roleItem = $('.roleItem');
	if($(this).hasClass('open')){
		roleList.slideUp(400);
		$(this).removeClass('open');
	}else{
		allRole.slideUp(400);
		roleList.slideDown(400);
		roleItem.removeClass('open');
		$(this).addClass('open');
	}
});

//选中效果
$('#roleListCon').delegate('.radio-style','click',function(){
	var roleId = $(this).attr("data-id");
	var inputRadio = $(this).find('.checkRadio');
	if(inputRadio.is(':checked')){			
		inputRadio.prop("checked", false);
	}else{
		inputRadio.prop("checked", true);
	}
	$("#roleIdTxt").val(roleId);
	//console.log($("#roleIdTxt").val())
})

//点击提交
$('#formSubmitBtn').click(function(){
	if(dFlag){
		submitRole();
	}else{
		changeLeaderRole();
	}
})


//修改组员角色
function submitRole(){
	var baseValue =  {
	    "groupid":groupId,
	    "userid":userId,
	    "roleid":$("#roleIdTxt").val()
	 }
	EasyAjax.ajax_Post_Json({
		url:'mobile/mine/add/userRole',
		data:JSON.stringify(baseValue)
	},function(res){
	    $.toast(res.message);
		window.location.href = _common.version("../../view/partener/projectTeamMemberDetail.html?userId="+userId+"&groupId="+groupId+'&loginId='+loginId);      
	})		
}

//修改组长角色
function changeLeaderRole(){
	EasyAjax.ajax_Post_Json({
		url:'mobile/mine/group/change/'+groupId+'/'+userId
	},function(res){
	    $.toast(res.message);		    
	    setTimeout(function(){
			window.location.href = _common.version("../../view/partener/projectTeamMemberDetail.html?userId="+userId+"&groupId="+groupId+'&loginId='+loginId);
	    },2000);        
	})		
}
