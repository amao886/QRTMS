var goodsDetail = new Vue({
	el:'#checkgGoodsDetail',
	data:{
		quantity    : 0,
		volume      : 0,
		boxCount    : 0,
		weight      : 0,
		commodities : [],
		
	},
	mounted: function(){
	    var _this = this;
	    _this.getListData();
	    
	    /*setTimeout(function(){
	       _this.initScroll();
	       _this.calcHeight();
	    }, 300);*/
	    
	    
	},
	methods:{
		getListData:function(){	
			var _this = this ;
			EasyAjax.ajax_Post_Json({
		        url: 'enterprise/order/commodity/' + sessionStorage.getItem("orderKey")
		    }, function (res) {
		    	if(!res.success){
		    		$.toast("加载失败，请稍后重试", "text");
		    		
		    	}else{
		    		_this.quantity = res.quantity;
		    		_this.volume = res.volume;
		    		_this.boxCount = res.boxCount;
		    		_this.weight = res.weight;
		    		_this.commodities = res.commodities;
		    		_this.$nextTick(function(){
		                _this.initScroll();
	       				_this.calcHeight();
		            })
		            //_this.listReslut = res.reslut;
		            //_this.pathUrl = res.pathPrefix;
		        }
		    });
					    
		},
		toLink:function(){
			window.location.href = "electronicReceipt.html?id="+sessionStorage.getItem("orderKey");
			//sessionStorage.removeItem("orderKey");
		},
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
		//计算高度
		calcHeight: function(){
			var _this = this;
			var titleEle = $('.listLeft .goodsName');
			var contentEle = $('.listRight .table-body');
			var heightArray = new Array();
			titleEle.each(function(i){
				var height = $(".line_"+i).height();
				heightArray[i] = height;
			});
			var height = Math.max.apply(null,heightArray);
			titleEle.each(function(i){
				var $this = $(this);
				$this.css({'height':height+'px'});
			});
			contentEle.each(function(i){
				var $this = $(this);
				$this.css({'height':height+'px'});
			});
		}
	}
});