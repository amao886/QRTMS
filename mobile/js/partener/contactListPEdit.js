var homeId = _common.getUrlParam('homeid'),
	friendName = _common.getUrlParam('fname'),
	friendPhone = _common.getUrlParam('fphone'),
	company =  _common.getUrlParam('company'),
	remark =  _common.getUrlParam('remark');

$('#friendName').text(friendName);
$('#friendPhone').text(friendPhone);

$('#companyName').val(company);
$('#remarkText').val(remark);


//保存信息
$('#saveFriend').on('click',function(){
	var companyName = $.trim($('#companyName').val()),
		remarkText = $.trim($('#remarkText').val());
		
	if(companyName === '' && companyName === ''){
		$.toast('请先输入所属公司或备注内容','text');
	}else{
		updateFriends({
			id      : homeId,
			company : companyName,
			remark  : remarkText
		});
	}
	
})



//接口
function updateFriends(options){
    var baseValue = {
	    id     : options.id,
	    company: options.company,
	    remark : options.remark
	 }
	EasyAjax.ajax_Post_Json({
		url:'mobile/friends/update/friend',
		data:JSON.stringify(baseValue)
	},function(res){
	    $.toast(res.message);
	    window.location.href = _common.version('./contactListPeople.html?friendkey='+baseValue.id);
   });	          
}