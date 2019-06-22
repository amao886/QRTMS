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
			        	
			        	//step 渲染小图标
				        $(".contentBox").find("a.funcLink").each(function(index,obj){
							var _num = $(obj).attr("data-num");
							$('<i class="addBtn" data-num="'+ _num +'"></i>').insertAfter(obj);
							$.each(vm.commonFunc.collection, function(i,item) {
								//console.log(item)
								if(_num == item){
									$(obj).next(".addBtn").addClass("nowBtn").removeClass("addBtn")
									return false;
								}
							});
						});
						
			        }
			        			        			        
			    });
            },
            /*setMyId: function (index){
			   return "person_" +index				
			},*/
			delFunc :function(num,i,obj){
				setFun.del(num,i,obj);				
			},
			jumpLink: function(num){				
				goPage(num)
			}
			
        },
        computed: {
        },
    	mounted: function(){
    		this.listCode();   		
    	}
    })
    
}


//点击"+"号
$(".contentBox").on("click",".addBtn",function(e){
	e.stopPropagation();
	var $this = $(this),
		_num = $this.attr("data-num");
	setFun.add(_num,$this)	
});
$(".contentBox li").on("click",".funcLink",function(){
	//alert($(this).attr("data-num"))
	$(this).next(".addBtn").trigger("click")
});

//保存按钮
$("#funcSave").on("click",function(){
	var _strArr = vm.commonFunc.collection.join(",");
	console.log(_strArr)
	setFun.getData({
		code:_strArr
	})
});


//常用图标：新增、删除、请求后台
var setFun = {	
	add :function(num,obj){
		var len = vm.commonFunc.collection.length;
			
		if(len >= 7){
			$.toast("抱歉，已达上限", "text");
		}else{
			var arr = vm.commonFunc.collection;
			arr.push(num);
			obj.addClass("nowBtn").removeClass("addBtn");
			
			/*setFun.getData({
				code : num,
				del  : 0
			},num,obj);	*/					
		}		
	},
	del : function(num,i,obj){
		var arr = vm.commonFunc.collection;
		arr.splice(i,1);
		vm.commonFunc.collection = arr;	
		$(".contentBox").find("a.funcLink[data-num="+ num +"]").next().addClass("addBtn").removeClass("nowBtn");	
					
		/*setFun.getData({
			code : num,
			del  : 1
		},num,obj,i);*/
	},
	getData : function(options){
		var baseValue = {
	        "code"   : options.code
	    }
	    EasyAjax.ajax_Post_Json({
	        url: 'mobile/mine/user/commonly/modify',
	        data: JSON.stringify(baseValue)
	    }, function (res) {	    	
	    	if(res.success){
	    		$.toast("保存成功", function(){
	    			window.location.href = _common.version("functionAll.html")
	    		});
	    	}else{
	    		$.toast("保存失败，请稍后重试", "cancel");
	    	}
	    });
	}
}


