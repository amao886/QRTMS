var goodsDetail = new Vue({
	el:'#goodsDetail',
	data:{
	    goodsList:[
	    	{
		  		goodsType:'',
		      	goodsWeight:'',
		      	goodsVolume:'',
		      	goodsQuantity:'',
		      	summary:''
		  	}
	    ],
	    deleteStatus:false, //显示删除内容
	    allFlag: false,  //全选判断
	    checkFlag: false, //toggle判断
	    deleteItem: [],   //删除集合
	    totalWeight: 0,
	    totalVolume: 0,
	    totalQuantity: 0
	},
	mounted: function(){
	    var _this = this;
	    setTimeout(function(){
	       _this.initScroll();
	    }, 20);
	    if(sessionStorage.getItem('sendGoodsPageStore')){
				//来自sendGoods.html页
				console.log('sendGoods.html');
			}
			if(sessionStorage.getItem('submitFormData')){
				//来自bindBarCode.html页
				console.log('bindBarCode.html')
			}
	},
	methods:{
	    //初始化better-scroll插件
		initScroll:function(){
	  		var _this = this;
		  	if (!_this.scroll) {
			    _this.scroll = new BScroll(_this.$refs.wrapper, {
			        scrollX: true,
			        scrollY: false,
			        momentum: false,
			        click: true,
			        bounce:false
			    });
		    } else {
		      _this.scroll.refresh();
		    }
		},
		//保存发货数据
		saveBarCode: function(){
			console.log(sessionStorage.getItem('sendGoodsPageStore'));
			if(this.goodsList.length < 1){
				$.toast('至少有一行物料信息','text');
				return;
			}
			if(!this.goodsList[0].goodsType && !this.goodsList[0].goodsWeight && !this.goodsList[0].goodsVolume && !this.goodsList[0].goodsQuantity && !this.goodsList[0].summary){
				$.toast('至少添加一个物料信息','text');
				return;
			}
			if(sessionStorage.getItem('sendGoodsPageStore')){
				//来自sendGoods.html页
				console.log('sendGoods.html');
				this.saveSendGoods();
			}
			if(sessionStorage.getItem('submitFormData')){
				//来自bindBarCode.html页
				console.log('bindBarCode.html')
				this.saveBindBarCode();
			}
		},
		//保存sendGoods.html页数据
		saveSendGoods:function(){
			var params = JSON.parse(sessionStorage.getItem('sendGoodsPageStore'));
			var baseValue = {
		        customerId: params.customerId,
	        	sendCustomerId: params.sendCustomerId,
	        	arrivalDay: params.arrivalDay,
	        	arrivalHour: params.arrivalHour,
	        	groupid: params.groupid ,
	        	deliveryNumber: params.deliveryNumber,
	        	orderSummary: params.orderSummary,
		        goods: JSON.stringify(this.goodsList)
		    }
		    EasyAjax.ajax_Post_Json({
		        url: 'mobile/wayBill/saveWaybill',
		        data: JSON.stringify(baseValue)
		    }, function (res) {
		        $.toast(res.message);
		        if (res.success) {
		            window.location.href = _common.version("projectTeamTask.html");
		            sessionStorage.removeItem("customerType");
		            sessionStorage.removeItem("sendCustomID");
		            sessionStorage.removeItem("receiveCustomID");
		        }
		    });
		},
		//保存bindBarCode.html页数据
		saveBindBarCode:function(){
			var params = JSON.parse(sessionStorage.getItem('submitFormData'));
			var baseValue = {
				address       : params.address,
				sendCustomerId: params.sendCustomerId,
			    customerId    : params.customerId,
			    deliveryNumber: params.deliveryNumber,
			    latitude      : params.latitude,
			    longitude     : params.longitude,
			    orderSummary  : params.orderSummary,
				barcode       : params.barcode,
			    arrivalDay    : params.arrivalDay,
			    arrivalHour   : params.arrivalHour,
			    commodities   : JSON.stringify(this.goodsList)
			}
			EasyAjax.ajax_Post_Json({
				url:'mobile/barCode/add',
				data:JSON.stringify(baseValue)
			},function(res){
				//提交成功
				$.toast(res.message);
				if(res.success){
					sessionStorage.removeItem("customerType");
		            sessionStorage.removeItem("sendCustomID");
		            sessionStorage.removeItem("receiveCustomID");
		            window.location.href = _common.version('bindCodeSuccess.html');
				}		
			})
		},
		//新增物料
		addGoods:function(){
		  	this.goodsList.push({
		  		goodsType:'',
		      	goodsWeight:'',
		      	goodsVolume:'',
		      	goodsQuantity:'',
		      	summary:''
		  	});
		},
		//删除物料
		deleteGoods:function(){
			if(this.deleteItem == null || this.deleteItem.length <= 0){
				$.toast("请先选中删除项", "text");
				return;
			}
			var _this = this;
			$.confirm("确定删除吗",'', function() {
			  	//点击确认后的回调函数
			  	var temps = []; 
				for(var i=0;i<_this.goodsList.length;i++){
					var flag = true;
					for(var j=0;j<_this.deleteItem.length;j++){
						if(i == _this.deleteItem[j]){
							flag = false;
							break;
						}
					}
					if(flag){
						temps.push(_this.goodsList[i]);
					}
				}
				_this.goodsList = temps;
				_this.deleteItem = [];
			}, function() {
			  //点击取消后的回调函数
			});
		},
		//单选
		checkOne:function(value,num){
		  	if(typeof value.checked == 'undefined'){
				this.$set(value,'checked',true);
			}else{
				value.checked = !value.checked;
			}
			if(value.checked){
				//选中
				console.log('选中');
				this.deleteItem.push(num);
			}else{
				//没有选中
				console.log('没有选中');
				var nowIndex = this.deleteItem.indexOf(num);
				this.deleteItem.splice(nowIndex,1);
			}
			var _this = this;
			this.goodsList.forEach(function(val,index){
			  	if(val.checked == false){
			  		_this.checkFlag = false;
			  		_this.allFlag = false; //取消全选
			  		return;
			  	}
			});
		  
		},
		//全选
		checkAll:function(flag){
		  this.allFlag = flag;
		  var _this = this;
		  this.goodsList.forEach(function(val,index){
		  	if(typeof val.checked == 'undefined'){
				_this.$set(val,'checked',flag);
			}else{
				val.checked = flag;
			}
		  });
		},
		//切换全选
		toggleCheck:function(){
			if(this.checkFlag){
				this.checkAll(false);
				this.checkFlag = false;
			}else{
				//第一次点击
				this.checkAll(true);
				this.checkFlag = true;	
			}
		},
		//取消全选
		cancelCheckAll:function(){
			this.deleteStatus = false;
			this.checkAll(false);
		},
		//点击删除按钮
	    showDeleteBox:function(){
	      this.deleteStatus = true;          
	    },
	    //重量，体积，数量验证
	    weightValidate: function(value){
	    	value.goodsWeight = value.goodsWeight.replace(/[^\d\.]/g,'');
		    this.calcTotalWeight();
	    },
	    volumeValidate: function(value){
	    	value.goodsVolume = value.goodsVolume.replace(/[^\d\.]/g,'');
	    	this.calcTotalVolume();
	    },
		quantityValidate: function(value){
			value.goodsQuantity = value.goodsQuantity.replace(/[^\d\.]/g,'');
		    this.calcTotalQuantity();
		},
		//计算总重量，总体积，总数量
		calcTotalWeight:function(){
			this.totalWeight = 0;
			var _this = this;
			this.goodsList.forEach(function(val,index){
				var num = parseFloat(val.goodsWeight);
				if(num){	
					_this.totalWeight = _this.accAdd(_this.totalWeight,num);
				}
			})
		},
		calcTotalVolume:function(){
			this.totalVolume = 0;
			var _this = this;
			this.goodsList.forEach(function(val,index){
				var num = parseFloat(val.goodsVolume);
				if(num){	
					_this.totalVolume = _this.accAdd(_this.totalVolume,num)
				}
			})
		},
		calcTotalQuantity:function(){
			this.totalQuantity = 0;
			var _this = this;
			this.goodsList.forEach(function(val,index){
				var num = parseFloat(val.goodsQuantity);
				if(num){	
					_this.totalQuantity = _this.accAdd(_this.totalQuantity,num);
				}
			})
		},
		accAdd:function(arg1,arg2){ //解决js浮点数问题
			var r1,r2,m;  
			try{
				r1=arg1.toString().split(".")[1].length
			}catch(e){
				r1=0
			}try{
				r2=arg2.toString().split(".")[1].length
			}catch(e){r2=0}  m=Math.pow(10,Math.max(r1,r2))
				return (arg1*m+arg2*m)/m
		}	    
	}
});