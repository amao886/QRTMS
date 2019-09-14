var goodsDetailEdit = new Vue({
	el:'#goodsDetailEdit',
	data:{
	    goodsList:[],
	    deleteStatus:false, //显示删除内容
	    allFlag: false,  //全选判断
	    checkFlag: false, //toggle判断
	    deleteItem: [],   //删除集合
	    idItem: [],
	    totalWeight: 0,
	    totalVolume: 0,
	    totalQuantity: 0,
	    bindBarMsg: JSON.parse(sessionStorage.getItem('bindBarMsg')),
	    editBarMsg: sessionStorage.getItem('editBarMsg')
	    
	},
	mounted: function(){
	    var _this = this;
	    setTimeout(function(){
	      	_this.initScroll();
			_this.getGoodsList(); //加载数据
			
	    }, 20);
	    if(this.bindBarMsg){ //来自bindBarCodeNew.html
			console.log('bindBarCodeNew.html');	
		}			
		if(this.editBarMsg){ //来自editBarMsg.html
			console.log('editBarCode.html');
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
		//获取物料信息
		getGoodsList:function(){
            var _this = this;
			var _url = '';			
			if(this.bindBarMsg){ //来自bindBarCodeNew.html
				_url = 'mobile/wayBill/listGoods/'+ this.bindBarMsg.waybillId;
			}			
			if(this.editBarMsg){ //来自editBarMsg.html
				_url = 'mobile/wayBill/listGoods/'+ this.editBarMsg;
			}
			EasyAjax.ajax_Post_Json({
					url:_url
				},function(data){
		        _this.goodsList = data.goods;
		        _this.calcTotalWeight();
		        _this.calcTotalVolume();
		        _this.calcTotalQuantity();
		    });
			
		},
		//保存发货数据
		saveBarCode: function(){

			if(this.goodsList.length < 1){
				$.toast('至少有一行物料信息','text');
				return;
			}
			if(!this.goodsList[0].goodsType && !this.goodsList[0].goodsWeight && !this.goodsList[0].goodsVolume && !this.goodsList[0].goodsQuantity && !this.goodsList[0].summary){
				$.toast('至少添加一个物料信息','text');
				return;
			}
			if(this.bindBarMsg){ //来自bindBarCodeNew.html
				this.saveBindBarCodeNew();
			}			
			if(this.editBarMsg){ //来自editBarMsg.html
				this.saveEditBarCode();
			}
		},
		//保存来自bindBarCodeNew页物料信息
		saveBindBarCodeNew:function(){
			var _this = this;
			//alert(this.bindBarMsg.waybillId)
			var baseValue = {
		        waybillId: this.bindBarMsg.waybillId,
	        	commodities: JSON.stringify(this.goodsList),
		   	}
		    EasyAjax.ajax_Post_Json({
		        url: 'mobile/wayBill/updateGoods',
		        data: JSON.stringify(baseValue)
		    }, function (res) {
		        $.toast(res.message);
		        if (res.success) {
		        	//保存总重量、体积、数量
		        	var calcTotal = {
		        		weight: _this.totalWeight,
		        		volume: _this.totalVolume,
		        		quantity: _this.totalQuantity
		        	}
		        	sessionStorage.setItem('calcTotal',JSON.stringify(calcTotal));
		            window.location.href = _common.version("bindBarCodeNew.html?bindCode="+_this.bindBarMsg.bindCode+"&waybillId="+ _this.bindBarMsg.waybillId);
		        }
		    });
		},
		//保存来自editBarCode页物料信息
		saveEditBarCode:function(){
			var _this = this;
			//console.log(this.editBarMsg)
			var baseValue = {
		        waybillId: this.editBarMsg,
	        	commodities: JSON.stringify(this.goodsList),
		   	}
		    EasyAjax.ajax_Post_Json({
		        url: 'mobile/wayBill/updateGoods',
		        data: JSON.stringify(baseValue)
		    }, function (res) {
		        $.toast(res.message);
		        if (res.success) {
		        	//保存总重量、体积、数量
		        	var calcTotal = {
		        		weight: _this.totalWeight,
		        		volume: _this.totalVolume,
		        		quantity: _this.totalQuantity
		        	}
		        	sessionStorage.setItem('editCalc',JSON.stringify(calcTotal));
		            window.location.href = _common.version("editBarCode.html?taskId="+_this.editBarMsg);	
		        }
		    });
		},
		//删除数据
		delGoods:function(){
			var waybillId;
			if(this.bindBarMsg){
                waybillId = this.bindBarMsg.waybillId
			}else {
				waybillId =  this.editBarMsg;
			}
			var baseValue = {
		        waybillId: waybillId,
	        	deleteCommodityKeys: JSON.stringify(this.idItem)
		    }
			var _this = this;
		    EasyAjax.ajax_Post_Json({
		        url: 'mobile/wayBill/deleteGoods',
		        data: JSON.stringify(baseValue)
		    }, function (res) {
		        if (res.success) {
                    _this.getGoodsList();
		        	console.log(res.message);
		            _this.deleteStatus = false;
		            _this.idItem = [];
		        }
		    });
		},
		//新增物料
		addGoods:function(){
		  	this.goodsList.push({
		  		id:'',
		  		goodsType:'',
		      	goodsWeight:'',
		      	goodsVolume:'',
		      	goodsQuantity:'',
		      	summary:''
		  	});
		},
		//删除物料
		deleteGoods:function(){
			if(this.deleteItem.length <= 0){
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
				//删除数据库总的物料
				if(_this.idItem.length>0){
					_this.delGoods();
				}				
				
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
				if(this.goodsList[num].id){
					this.idItem.push(this.goodsList[num].id);
				}
				console.log('选中idItem:'+ this.idItem);
			}else{
				//没有选中
				console.log('没有选中');
				var nowIndex = this.deleteItem.indexOf(num);
					this.deleteItem.splice(nowIndex,1);
				console.log(this.idItem);
				if(this.goodsList[num].id){
					var nowId = this.idItem.indexOf(this.goodsList[num].id);
					this.idItem.splice(nowId,1);
				}
				console.log('没有选中idItem:'+ this.idItem);
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
              _this.deleteItem.push(val.id);
              _this.idItem.push(val.id)
		  	if(typeof val.checked == 'undefined'){
				_this.$set(val,'checked',flag);
			}else{
				val.checked = flag;
			}
		  });
		  if(!flag){
              _this.deleteItem.splice(0,_this.deleteItem.length);
              _this.idItem.push(0,_this.idItem.length)
		  }
		  console.log(_this.deleteItem)
		},
		//切换全选
		toggleCheck:function(){
			if(this.checkFlag){
				alert(1231)
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
		    console.log(value.goodsWeight);
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
				r2=arg2.toString().split(".")[1].length}catch(e){r2=0}  m=Math.pow(10,Math.max(r1,r2))  
				return (arg1*m+arg2*m)/m
			}	    
		}
});