'use strict';

function addRes(){
	var groupName = $("#groupName").val();
	if(groupName==""){
	    $.toptip('项目组名称必填');
	    return false;
	}
	
	var baseValue = {
		"groupName": utf16toEntities(groupName)
	}
	EasyAjax.ajax_Post_Json({
		url:'mobile/mine/addGroup',
		data:JSON.stringify(baseValue)
	},function(res){
		if(res.success){
		    $.toast(res.message);
			window.location.href=_common.version("myProjectTeam.html");        
		}
	})
  
}
