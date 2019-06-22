'use strict';

var userId = _common.getUrlParam('userId'); //当前组成员id
var groupId = _common.getUrlParam('groupId');  //项目组id
var loginId = _common.getUrlParam('loginId');  //当前用户id

//处理后台返回的时间
$.views.helpers({
	dateFormat:function(msg,type){
	  	var time   = new Date(msg);
	  	var year   = time.getFullYear();
	    var month  = time.getMonth()+1;
	    var day    = time.getDate();
	    var hour   = time.getHours();
	    var minute = time.getMinutes(); 
	    month  = month<10 ? '0'+month : month;
	    day    = day<10 ? '0'+day : day;
	    hour   = hour<10 ? '0'+hour : hour;
	    minute = minute<10 ? '0'+minute : minute;
	    //console.log(type)
	    switch(type){
	    	case 1:
	    	    return year+'-'+month+'-'+day;
	    	default:
	    	    return year+'年'+month+'月'+day+'日'+' '+hour+':'+minute;
	    }
		
  	},
    "showDom": function(guserid){
        if(guserid == loginId){//判断当前操作人是否组长
        	console.log("组长");
        	//组长不允许自己修改自己的角色,如果修改后，则会存在无组长
        	if(guserid !=userId){
        		return true;
        	}
            return false;
        }else{
        	console.log("组员");
            return false;
        }
    }
});

//获取组成员信息
getUserInfo();


//查询项目组信息
function getUserInfo(){

	EasyAjax.ajax_Post_Json({
		url:'mobile/mine/userRole/detail/'+ userId + '/' +groupId
	},function(res){
		var roleCode = res.results.roleCode;
		
		var infoHtml = $('#userInfo').render(res.results);
		$('#userInfoCon').append(infoHtml);	
		
		//点击跳转
		$('#userInfoCon').delegate('#goRolePage','click',function(){
			window.location.href=_common.version("./projectTeamMemberRole.html?userId="+ userId +"&groupId="+groupId+'&loginId='+loginId+'&roleCode='+roleCode);
		})
	})
	
}


