var vehicleDetail = new Vue({
	el: '#vehicleDetailWrapper',
	data: {
		orderId: _common.getUrlParam('orderId'),
		deliveryCarDto: {},
		arrivalsCarDto: {}
	},
	created: function() {
		var _this = this;

		_this.getData();
	},
	methods: {
		getData: function() {
			var _this = this;

			EasyAjax.ajax_Post_Json({
				url: 'scene/query/registration/' + _this.orderId,
			}, function(res) {
				if(!res.success) {
					$.toast(res.message, "text");
				} else {
					console.log(res);

					_this.deliveryCarDto = res.result.deliveryCarDto;
					_this.arrivalsCarDto = res.result.arrivalsCarDto;
				}
			})
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		}
	}
});