var friendkey = _common.getUrlParam('friendkey');


//获取好友资料
getFriendData(friendkey);

//邀请注册弹出框
$("#inviteReg").on('click',function(){
	$('#inviteRegBox').fadeIn(250);
})

//隐藏邀请注册弹出框
$('#boxCancel').on('click',function(){
	$('#inviteRegBox').fadeOut(250);
})

//复制内容
var clipboard = new Clipboard('#paste');
clipboard.on('success', function(e) {
    console.log(e);
    $.toast('复制成功！','text');
    e.clearSelection();
    $('#inviteRegBox').fadeOut(250);
});

clipboard.on('error', function(e) {
    console.log(e);
    $.toast('请长按文字选择拷贝进行复制','text');
});

//发送短信
$('#sendText').on('click',function(){
	var contactText = '关注合同物流管理平台公众号，有货即发、中转外包、车辆定位，协作更高效。';
	sendMsgAboutF({
		mobilePhone : $('#phoneNum').text(),
		remark      : contactText
	})
})

//打电话
$("#call").on('click',function(){
	$(this).attr('href','tel:'+$('#phoneNum').text());
})


//删除好友
$('#deletePeople').on('click',function(){
	$.confirm({
		title: '确定删除好友吗？',
		onOK: function () {
		    deleteFriend(friendkey);
		}
	});
})


//获取好友资料
function getFriendData(data){
    EasyAjax.ajax_Post_Json({
        url:'mobile/friends/query/friend/info/'+data
    },function(res){
    	//console.log(res);
    	var name = res.result.fullName,
    		phone = res.result.mobilePhone,
    		id = res.result.id,
    		company = res.result.company || '',
    		remark = res.result.remark || '';
    	$('#peopleName').text(name);
    	$('#phoneNum').text(phone);
    	
    	if(res.result.pid){
    		//已注册
    		$('.register').text('已注册');
    		$('#btnWrapper').show().find('#inviteReg').hide();
    	}else{
    		$('.register').text('未注册');
    		$('#btnWrapper').show();
    	}
    	
    	if(res.result.company || res.result.remark){
    		$('.setRemark').text('查看备注');
    	}
    	
    	//设置备注
    	$('.setRemark').on('click',function(){
    		window.location.href = _common.version('./contactListPEdit.html?homeid='+id+'&fname='+name+'&fphone='+phone+'&company='+company+'&remark='+remark);
    	})
    });
	
}

//短信发送接口
function sendMsgAboutF(options){
	var baseValue = {
	    mobilePhone : options.mobilePhone,
	    remark      : options.remark
	}	
	EasyAjax.ajax_Post_Json({
        url:'mobile/friends/send/msg',
        data:JSON.stringify(baseValue)
    },function(res){
    	$.toast(res.messsage);
    	$('#inviteRegBox').fadeOut(250);
    });
}

//删除 好友接口
function deleteFriend(id){
	EasyAjax.ajax_Post_Json({
        url:'mobile/friends/delete/friend/'+id
    },function(res){
    	$.toast(res.messsage);
    	window.location.href = _common.version('./contactList.html');
    });
}
