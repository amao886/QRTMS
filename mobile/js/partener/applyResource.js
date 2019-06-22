'use strict';

getCodeInfo()

//查询项目组信息
function getCodeInfo(){
	EasyAjax.ajax_Post_Json({
		url:'mobile/barCode/queryTotalCount'
	},function(res){
		var data = res.results;
		$(".totalCode").text(data.total);	
		$(".leftCode").text(data.availableTotal);
	})
}


