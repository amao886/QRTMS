/*底部 导航部分*/
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

var vm = {
	commonFunc : new Vue({
        el: '#vue-common-function',
        data: {total:7,
        	   collection:[
            	{num:"101"},
            	{num:"102"},
            	{num:"201"},
            	{num:"202"},
            	{num:"301"},
            	{num:"302"},
            	{num:"401"},
            	]
        	},
        methods:{
        	/*listCode:function(num){
                var parmas = {likeString: $("#list-wait-code").find("input[name='likeString']").val(), num : num};
                $.util.json(base_url + '/backstage/driver/list/wait/code', parmas, function (data) {
                    if (data.success) {//处理返回结果
                        v.listCode.prePage = data.page.prePage;
                        v.listCode.nextPage = data.page.nextPage;
                        v.listCode.total = data.page.total;
                        v.listCode.collection = data.page.collection;
                        if(v.listCode.collection && v.listCode.collection.length > 0){
                            $('#detail-wait-code').show();
                            selectCode(v.listCode.collection[0].barcode);
                        }else{
                            $('#detail-wait-code').hide();
                        }
                    } else {
                        $.util.error(data.message);
                    }
                });
            },
            setMyId: function (index){
			   return "person_" +index				
			},*/
			jumpLink: function(num){
				
				var _url = "";
				switch(num){
					case "101": _url = "view/partener/sendGoods.html?index=index"; break;
					case "102": _url = "view/partener/projectTeamTask.html?index=index"; break;
					case "103": _url = "view/partener/sendMonitor.html"; break;
					case "104": _url = "view/partener/chooseCustom.html"; break;
					
					case "201": $("#swapCircle").trigger("click"); break;//扫一扫
					case "202": break;//无
					case "203": _url = "view/partener/loadGoods.html?index=index"; break;
					
					case "301": _url = "view/partener/projectTeamTask.html?index=index"; break;
					case "302": _url = "view/partener/checkStock.html?index=index"; break;
					case "303": $("#swapCircle").trigger("click"); break;//扫一扫
					case "304": _url = "view/partener/loadGoods.html?index=index"; break;
					
					case "401": _url = "view/partener/upload.html?showClose=10&waybillId="+""+"&index=index"; break;
					case "402": break;//无
					
					case "501": _url = "view/partener/myProjectTeam.html"; break;
					case "502": _url = "view/partener/applyResource.html"; break;
					case "503": break;//无
					
					case "601": break;//同APP，暂无
					case "602": _url = "view/partener/address.html"; break;
				}

				if(_url){
					window.location.href =  _common.version(_url);
				}
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
    		//this.listCode();
    	}
    }),
    myTaskList : new Vue({
        el: '#vue-task-list',
        data: {userid:null,attachment:[]},
        methods:{
        	taskList:function(options){
                var baseValue = {
			        num: options.num,
			        size: options.size,
			        groupId: options.groupId || '',
			        createtime: options.createtime || '',
			        delay: options.delay || '',
			        fettles: options.fettles || '',
			        likeString: options.likeString || '',
			        rptFettles: options.rptFettles || ''
			    }
			    EasyAjax.ajax_Post_Json({
			        url: 'mobile/wayBill/group/search',
			        data: JSON.stringify(baseValue)
			    }, function (res) {
			        var results = res.page.attachment;
			        vm.myTaskList.userid = res.userid
			        if(results != null){
			        	vm.myTaskList.attachment = vm.myTaskList.classify(results)
			        }
			        
			        console.info(vm.myTaskList.attachment);
			        				        								        					       	
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
            //attachment: function () { return classify(this.attachment); }
        },
    	mounted: function(){
    		this.taskList({
                num: 1,
                size: 20,
                groupId: "",
                createtime: "",
                likeString: "",
                fettles: "",
                delay: ""
            });
            
    	}
    })
    
}
