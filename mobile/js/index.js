/*底部 导航部分*/
loadImg();
$("#navBar").load("view/common/navBar.html",function () {
	
	$('.a-hook').on('click',function(){
    	$('.a-hook').removeClass('active');
    	$(this).addClass('active');   	
   })
	
    //跳转到扫一扫
    $('#swapCircle').on('click',function(){
    	scanCode();
    })
    
    //跳转到通讯录
    $('#conList').on('click',function(){
    	window.location.href = _common.version('view/partener/contactList.html?jstatus='+ 1);
    })
    
    //跳转到个人中心
    $('#peopleCenter').on('click',function(){
    	window.location.href =  _common.version('view/partener/peopleCenter.html?jstatus='+ 2);
    })
    
    //选中状态
    var jstatus = _common.getUrlParam('jstatus');
    console.log('jstatus首页:'+jstatus);
	if(jstatus == '1'){
    	//通讯录
    	$('.a-hook').removeClass('active');
    	$('#conList').addClass('active');
    }else if(jstatus == '2'){
    	//个人中心
    	$('.a-hook').removeClass('active');
    	$('#peopleCenter').addClass('active');
    }
});

//跳到电子回单页
$('#elecBanner').on('click',function(){
	window.location.href = _common.version('view/partener/electronicManage.html');
	/*之前跳到这个页面 view/partener/electronicSearch.html*/
})

if($('.my-task').children().length == 1){
	$('.my-task').addClass('marBottom');
}

var dLoading = false,
	dNum = 1,
	pageSize = 3;
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
			jumpLink: function(num){
				
				var _url = "";
				switch(num){
					case "101": _url = "view/partener/sendGoods.html?index=index&token="+api_token; break;
					case "102": _url = "view/partener/projectTeamTask.html?index=index"; break;
					case "103": _url = "view/partener/sendMonitor.html"; break;
					case "104": _url = "view/partener/customList.html"; break;
					
					case "201": $("#swapCircle").trigger("click"); break;//扫一扫
					case "202": break;//无
					case "203": _url = "view/partener/loadGoods.html?index=index"; break;
					
					case "301": _url = "view/partener/projectTeamTask.html?index=index"; break;
					case "302": _url = "view/partener/checkStock.html?index=index"; break;
					case "303": $("#swapCircle").trigger("click"); break;//扫一扫
					case "304": _url = "view/partener/loadGoods.html?index=index"; break;
					
					case "401": _url = "view/partener/upload.html?showClose=10&waybillId="+""+"&index=index"; break;
					case "402": _url = "view/partener/electronicManage.html"; break;
					
					case "501": _url = "view/partener/myProjectTeam.html"; break;
					case "502": _url = "view/partener/applyResource.html"; break;
					case "503": break;//无
					case "504": _url = "myTask.html"; break;//无
					case "505": _url = "view/partener/transportManage.html"; break;
					
					case "601": break;//同APP，暂无
					case "602": _url = "view/partener/address.html"; break;
				}

				if(_url){
					window.location.href =  _common.version(_url);
				}
			},
            toAll:function(){
                window.location.href =  _common.version('view/partener/function.html');
			}
        },
        computed: {
            /*codes: function () {
                $.each(this.collection, function (i, item) {
                    if(item.loadTime){
                        item.loadTime = moment(item.loadTime).format('YYYY-MM-DD HH:mm');
                    }
                });
                return this.collection;
            }*/
        },
    	mounted: function(){
    		this.listCode();
    	}
    }),
    myTaskList : new Vue({
        el: '#vue-task-list',
        data: {
        	pageNum:0,
        	pages:0,
        	collection:[],
        	editShow:false
        },
        methods:{
        	taskList:function(options){
                var baseValue = {
			        num: options.num,
			        size: options.size
			    }
			    EasyAjax.ajax_Post_Json({
			        url: 'mobile/wayBill/three/days',
			        data: JSON.stringify(baseValue)
			    }, function (res) {
			    	var data = res.page;
			        var results = data.collection;
			        vm.myTaskList.pageNum = data.pageNum;
			        vm.myTaskList.pages = data.pages;
			        
			        if(!res.success){
			        	$.toast("加载失败，请稍后重试", "text");
			        	return false;
			        }
			        
			        if(data.total == 0){
			        	$('.weui-loadmore').hide();			        				        	
			        }else{
			        	if(vm.myTaskList.collection.length){
		        			var arr = vm.myTaskList.collection;
		        			arr = arr.concat(results)
			        		vm.myTaskList.collection = arr;
			        	}else{
			        		vm.myTaskList.collection = results	
			        	}
			        	
			        	if(dNum == data.pages){
			        		$('.weui-loadmore').show().html('已经到底了');
			        		dLoading = false;
			        	}else{        		
				        	dLoading = true;
			        	}
			        }
			        //console.info(vm.myTaskList.collection);
			        				        								        					       	
			    });
            },
            toDetail:function(uid,status){
            	window.location.href = _common.version('view/partener/myTaskDetails.html?waybillId=' + uid + '&sts=' + status);
            },
            classify:function(datas){
            	var arrays = [], i = 0;
			    $.each(datas, function(index, v, array){        
			        $.each(v,function(i,item){
			        	arrays.push(item)                                                                                            
			        })
			    });
			    return arrays;
            }
        },
        computed: {
            //collection: function () { return classify(this.collection); }
        },
    	mounted: function(){
    		this.taskList({
                num  : dNum,
                size : pageSize
            });
            
    	}
    })
    
}

//下拉翻页
$(document.body).infinite(0).on("infinite", function() {
	if(dLoading){
		dLoading = false;
		dNum++;
		$('.weui-loadmore').show();
		vm.myTaskList.taskList({
			num  : dNum,
            size : pageSize
		});
		
	}	
});
