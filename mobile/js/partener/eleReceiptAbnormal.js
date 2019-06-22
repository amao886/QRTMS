var receiptWrapper = new Vue({
	el: '#receiptWrapper',
	data: {
		showMore: false,
		needId: _common.getUrlParam('abId'),
		listReslut: null,
		pathUrl:'',
		abnText:''
	},
	filters:{
		toFix: function(value){
			return value.toFixed(2);
		},
		addUrl: function(value){
			return receiptWrapper.pathUrl + '/' + value;
		}
	},
	mounted: function(){
		this.getListData();
	},
	methods: {
		//显示隐藏
		showMoreFun: function(){
			this.showMore = !this.showMore;
		},
		//获取数据
		getListData: function(){
			var _this = this;
		    EasyAjax.ajax_Post_Json({
		        url: 'enterprise/order/orderDetail/' + _this.needId
		    }, function (res) {
		    	if(!res.success){
		    		$.toast("加载失败，请稍后重试", "text");
		    		
		    	}else{
		            _this.listReslut = res.reslut;
		            _this.pathUrl = res.pathPrefix;
		        }
		    });
		},
		//签署
		signChecked: function(orderid,signrole){
			if(!this.abnText){
				$.toast('异常信息必填','text');
				return;
			}
			var _this = this;
			var baseValue = {
		        orderId: orderid,
	        	signRole: signrole,
	        	exception: '1',
	        	content: this.abnText
		    }
		    EasyAjax.ajax_Post_Json({
		        url: 'enterprise/order/Sign',
		        data: JSON.stringify(baseValue)
		    }, function (res) {
		    	if(res.success){
		    		if(signrole == 2)$.toast('物流商签署完成');
		    		if(signrole == 3)$.toast('完好签收');
		    		_this.getListData();	
		    	}
		    });
		}
	}
})

