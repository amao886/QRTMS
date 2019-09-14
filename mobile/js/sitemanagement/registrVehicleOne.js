var registrVehicle = new Vue({
	el: '#registrVehicleOneWrapper',
	data: {
		deliveryNo: '',
		deliveryCarDto: {},
		arrivalsCarDto: {},
		plateNo: '',
		driverName: '',
		driverPhone: '',
		deliveryNo: '',
		orderNo: ''
	},
	methods: {
		submit: function() {
			var _this = this;

			if(_this.isNull(_this.deliveryNo)) {
				$.toptip('送货单号不能为空');
				return;
			}

			EasyAjax.ajax_Post_Json({
				url: 'scene/queryDeliveryDetails/' + _this.deliveryNo,
			}, function(res) {
				if(!res.success) {
					$.toast(res.message, "text");
				} else {
					console.log(res);

					_this.orderNo = res.result.orderKey;
					_this.arrivalsCarDto = res.result.arrivalsCarDto;

					if(_this.arrivalsCarDto == null) { //arrivalsCarDto 为空：未到车，不为空：已到车
						_this.deliveryCarDto = res.result.deliveryCarDto;

						if(_this.deliveryCarDto != null) {
							if(_this.deliveryCarDto.deliveryNo != null) {
								_this.deliveryNo = _this.deliveryCarDto.deliveryNo;
							}
							if(_this.deliveryCarDto.license != null) {
								_this.plateNo = _this.deliveryCarDto.license;
							}
							if(_this.deliveryCarDto.driverName != null) {
								_this.driverName = _this.deliveryCarDto.driverName;
							}
							if(_this.deliveryCarDto.driverContact != null) {
								_this.driverPhone = _this.deliveryCarDto.driverContact;
							}
						}

						window.location.href = _common.version('./registrVehicleTwo.html?plateNo=' + _this.plateNo + '&driverName=' + _this.driverName + '&driverPhone=' + _this.driverPhone + '&deliveryNo=' + _this.deliveryNo + '&orderNo=' + _this.orderNo);
					} else {
						window.location.href = _common.version('./vehicleDetail.html?orderId=' + _this.orderNo);
					}
				}
			})
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		}
	}
});