var addAddress = new Vue({
	el: '#addAddress',
	data: {
		type: 2, //2：收货客户
		status: 1, //1：正常状态
		customers: [],
		areaVersion: 0,
		areaData: [],
		id: '',
		companyCustomerId: -1,
		contacts: '',
		contactNumber: '',
		chooseAreaData: '',
		address: ''
	},
	created: function() {
		var _this = this;

		_this.getCustomerList();
	},
	methods: {
		getCustomerList: function() {
			var _this = this;

			var baseValue = {
				type: _this.type,
				status: _this.status
			}
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/customer/list',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					console.log(res);

					_this.customers = res.customers;

					_this.getAreasData();
				}
			});
		},
		getAreasData: function(version) {
			var _this = this;

			var baseValue = {
				version: version
			}
			EasyAjax.ajax_Post_Json({
				url: 'special/support/area/all',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					console.log(res);

					if(window.sessionStorage) {
						if(_this.areaVersion < res.version) {
							sessionStorage.removeItem('areaData');
							sessionStorage.setItem('areaVersion', res.version);
							sessionStorage.setItem('areaData', JSON.stringify(res.result));
							loadData(res.result);
						} else {
							sessionStorage.setItem('areaVersion', res.version);

							var areaData = JSON.parse(sessionStorage.getItem('areaData'));
							if(areaData) {
								loadData(areaData);
							}
						}
					} else {
						loadData(res.result);
					}

					setTimeout(function() {
						_this.initWidget();
					}, 200);
				}
			});
		},
		initWidget: function() {
			var _this = this;

			$("#receiveAddressChoose").cityPicker({
				title: "请选择收货地址省市区/县"
			});

			_this.initData();
		},
		initData: function() {
			var _this = this;

			var data = _common.getUrlParam('info');
			if(data) {
				var address = JSON.parse(data);
				
				$('#customerName').find("option:selected").text(address.customerName);
				$(".weui-select").addClass("choosed");

				_this.id = address.id;
				_this.companyCustomerId = address.customerId;
				_this.contacts = address.contacts;
				_this.contactNumber = address.contactNumber;
				if (_this.isNull(address.district)) {
					_this.chooseAreaData = address.province.replace("市", "") + " " + address.province + " " + address.city;
				}else{
					_this.chooseAreaData = address.province + " " + address.city + " " + address.district;
				}
				_this.address = address.address;
			}
		},
		chooseCustomerName: function() {
			var _this = this;

			var customerKey = $('#customerName').find("option:selected").val();
			if(_this.isNull(customerKey)) {
				_this.companyCustomerId = "";
				$(".weui-select").removeClass("choosed");
			} else {
				_this.companyCustomerId = customerKey;
				$(".weui-select").addClass("choosed");
			}
			console.log(_this.companyCustomerId);
		},
		save: function() {
			var _this = this;

			_this.chooseAreaData = $("#receiveAddressChoose").val();

			if(_this.isNull(_this.companyCustomerId) || _this.companyCustomerId == -1) {
				$.toptip("客户名称不能为空");
				return;
			}
			if(_this.isNull(_this.contacts)) {
				$.toptip("收货人不能为空");
				return;
			}
			if(_this.isNull(_this.contactNumber)) {
				$.toptip("联系方式不能为空");
				return;
			}
			if(_this.isNull(_this.chooseAreaData)) {
				$.toptip("收货地址省市区/县不能为空");
				return;
			}
			if(_this.isNull(_this.address)) {
				$.toptip("收货地址不能为空");
				return;
			}

			var area = _this.chooseAreaData.split(" ");

			var baseValue = {
				id: _this.id,
				companyCustomerId: _this.companyCustomerId,
				contacts: _this.contacts,
				contactNumber: _this.contactNumber,
				province: area[0],
				city: area[1],
				district: area[2],
				address: _this.address
			}
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/customer/address/edit',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(res.success) {
					$.toast(res.message, function() {
						window.location.href = _common.version('./chooseDeliveryAddress.html?role=' + _common.getUrlParam('role') + '&type=' + _common.getUrlParam("type") + '&planKey=' + _common.getUrlParam('planKey') + '&isInputPlanPage=' + _common.getUrlParam('isInputPlanPage'));
					});
				}
			});
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		}
	}
});