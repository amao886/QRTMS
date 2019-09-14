var vm = {
	commonFunc : new Vue({
        el: '#vue-common-function',
        data: {
        	collection:[]
        },
        methods:{
        	listCode:function(num){
                EasyAjax.ajax_Post_Json({
			        url: 'mobile/mine/user/commonly '
			    }, function (res) {
			        var results = res.commonlies;
			        if(res.success){
			        	vm.commonFunc.collection = results;
			        }			        
			        console.info(vm.commonFunc.collection);			        				        								        					       	
			    });
            },
            /*setMyId: function (index){
			   return "person_" +index				
			},*/
			jumpLink: function(num){
				goPage(num);		
			}
			
        },
        computed: {
        },
    	mounted: function(){
    		this.listCode();
    	}
    })
    
}


//点击跳转链接
$(".contentBox").on("click","a.funcLink",function(){
	var _num = $(this).attr("data-num")
	goPage(_num);
});

//页面跳转
var goPage = function(num){				
	var _url = "";
	switch(num){
		case "101": _url = "sendGoods.html?index=index&token="+api_token; break;
		case "102": _url = "projectTeamTask.html?index=index"; break;
		case "103": _url = "sendMonitor.html"; break;
		case "104": _url = "customList.html"; break;
		
		case "201": $("#swapCircle").trigger("click"); break;//扫一扫
		case "202": break;//无
		case "203": _url = "loadGoods.html?index=index"; break;
		
		case "301": _url = "projectTeamTask.html?index=index"; break;
		case "302": _url = "checkStock.html?index=index"; break;
		case "303": $("#swapCircle").trigger("click"); break;//扫一扫
		case "304": _url = "loadGoods.html?index=index"; break;
		
		case "401": _url = "upload.html?showClose=10&waybillId="+""+"&index=index"; break;
		case "402": _url = "electronicSearch.html"; break;
		
		case "501": _url = "myProjectTeam.html"; break;
		case "502": _url = "applyResource.html"; break;
		case "503": break;//无
		case "504": _url = "myTask.html"; break;//无
		case "505": _url = "transportManage.html"; break;
		
		case "601": break;//同APP，暂无
		case "602": _url = "address.html"; break;
	}

	if(_url){
		window.location.href =  _common.version(_url);
	}
}


