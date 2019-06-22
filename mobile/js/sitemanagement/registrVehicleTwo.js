var registrVehicle = new Vue({
	el: '#registrVehicleTwoWrapper',
	data: {
		dispatchCarStatus: 0,
		plateNo: '',
		driverName: '',
		driverPhone: '',
		arrivalType: 1,
		showPlateNo: _common.getUrlParam('plateNo'),
		showDriverName: _common.getUrlParam('driverName'),
		showDriverPhone: _common.getUrlParam('driverPhone'),
		showDeliveryNo: _common.getUrlParam('deliveryNo'),
		orderNo: _common.getUrlParam('orderNo')
	},
	created: function() {
		var _this = this;

		if(_this.isNull(_this.showPlateNo) && _this.isNull(_this.showDriverName) && _this.isNull(_this.showDriverPhone)) {
			_this.dispatchCarStatus = 2;
			_this.arrivalType = 3;
		}
	},
	methods: {
		choose: function(value) {
			var _this = this;

			if(value != 0) {
				$("#show" + (value - 1)).before($(".dispatchCarInfo"));
			}

			_this.arrivalType = parseInt(value) + 1;

			_this.plateNo = '';
			_this.driverName = '';
			_this.driverPhone = '';
		},
		submit: function() {
			var _this = this;

			if(_this.dispatchCarStatus != 0) {
				if(_this.isNull(_this.plateNo)) {
					$.toptip('车牌号不能为空');
					return;
				}
				if(_this.isNull(_this.driverName)) {
					$.toptip('司机姓名不能为空');
					return;
				}
				if(_this.isNull(_this.driverPhone)) {
					$.toptip('司机电话不能为空');
					return;
				}
				if(!/^[\u4E00-\u9FFF]{1,15}$/.test(_this.driverName)) {
					$.toptip('姓名只能输入中文');
					return;
				}
				if(!/^1[\d]{10}$/.test(_this.driverPhone)) {
					$.toptip('请输入正确的手机号');
					return;
				}
			}

			var deliveryCarDto = {
				deliveryNo: _this.showDeliveryNo
			};
			if(!_this.isNull(_this.showDriverName)) {
				deliveryCarDto['driverName'] = _this.showDriverName;
			}
			if(!_this.isNull(_this.showDriverPhone)) {
				deliveryCarDto['driverContact'] = _this.showDriverPhone;
			}
			if(!_this.isNull(_this.showPlateNo)) {
				deliveryCarDto['license'] = _this.showPlateNo;
			}

			var arrivalsCarDto = {
				arrivalType: _this.arrivalType
			};
			if(_this.arrivalType != 1) {
				arrivalsCarDto['inDriverName'] = _this.driverName;
				arrivalsCarDto['inDriverContact'] = _this.driverPhone;
				arrivalsCarDto['inLicense'] = _this.plateNo;
			}
			var baseValue = {
				orderKey: _this.orderNo,
				arrivalsCarDto: arrivalsCarDto,
				deliveryCarDto: deliveryCarDto
			}

			EasyAjax.ajax_Post_Json({
				url: 'scene/vehicle/save',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(!res.success) {
					$.toast(res.message, "text");
				} else {
					$.toast(res.message, function() {
						if(localStorage.getItem('clickListTag')) {
							localStorage.removeItem("clickListTag");
						}
						if(localStorage.getItem('scrollData')) {
							sessionStorage.removeItem("scrollData");
						}
						window.location.href = _common.version('./siteManageList.html');
					});
				}
			})
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		}
	}
});