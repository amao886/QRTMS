var deliveryPlanDeatil = new Vue({
	el: '#deliveryPlanDeatil',
	data: {
		order: {},
		extra: {},
		receive: {},
		shipper: {},
		convey: {},
		customDatas: [],
		commodities: []
	},
	created: function() {
		var _this = this;

		_this.getDeliveryPlanDeatilInfo();
	},
	methods: {
		getDeliveryPlanDeatilInfo: function() {
			var _this = this;

			EasyAjax.ajax_Post_Json({
				url: 'enterprise/plan/detail/' + _common.getUrlParam('id'),
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					console.log(res);

					_this.order = res.result;
					_this.extra = res.result.extra;
					_this.receive = res.result.receive;
					_this.shipper = res.result.shipper;
					_this.convey = res.result.convey;
					_this.customDatas = res.result.customDatas;
					_this.commodities = res.result.commodities;

					_this.$nextTick(function() {
						_this.initScroll();
						_this.calcHeight();
					})
				}
			});
		},
		//初始化better-scroll插件
		initScroll: function() {
			var _this = this;
			if(!_this.scroll) {
				_this.scroll = new BScroll(_this.$refs.wrapper, {
					scrollX: true,
					scrollY: false,
					momentum: false,
					click: true,
					bounce: false
				});
			} else {
				_this.scroll.refresh();
			}
		},
		//计算高度
		calcHeight: function() {
			var _this = this;
			var titleEle = $('.listLeft .goodsName');
			titleEle.each(function(i) {
				var $this = $(this);
				var height = $(".line_" + i).height();
				$this.css({
					'height': height + 'px'
				});
			});
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		}
	}
});