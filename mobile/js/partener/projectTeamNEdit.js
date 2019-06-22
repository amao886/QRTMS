'use strict';
var groupId = _common.getUrlParam('groupId');
function modifyProName(){
	
	var groupName = $("#groupName").val();
	if(groupName==""){
	    $.toptip('项目组名称必填');
	    return false;
	}
	
	console.log(groupId)
	var baseValue = {
		"id": groupId, 
		"groupName": utf16toEntities(groupName)
	}
	EasyAjax.ajax_Post_Json({
		url:'mobile/mine/edit/groupName',
		data:JSON.stringify(baseValue)
	},function(res){
		if(res.success){
		    $.toast(res.message,"text");
			window.location.href = _common.version("projectTeamDetail.html?groupId="+groupId);        
		}
	})
  
}

