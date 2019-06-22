var editCustomer = new Vue({
	el: '#editCustomer',
	data: {
		customers: [],
		customerKey: _common.getUrlParam('key'),
		customerName: '',
		customerType: -1
	},
	created: function() {
		var _this = this;

		_this.getCustomerTypeListData();
	},
	methods: {
		getCustomerTypeListData: function() {
			var _this = this;

			var customerTypeList = ["发货方", "物流商", "收货客户"];
			for(var i = 0; i < customerTypeList.length; i++) {
				var customer = new Object();
				if(i == 1) {
					customer["key"] = i + 2;
				} else if(i == 2) {
					customer["key"] = i;
				} else {
					customer["key"] = i + 1;
				}
				customer["name"] = customerTypeList[i];
				_this.customers.push(customer);
			}

			_this.customerName = _common.getUrlParam("name");
			_this.customerType = _common.getUrlParam("customerType");
			if(_this.isNull(customerType)) {
				_this.customerType = -1;
				$(".weui-select").removeClass("choosed");
			} else {
				$(".weui-select").addClass("choosed");
			}
		},
		save: function() {
			var _this = this;

			if(_this.isNull(_this.customerName)) {
				$.toptip("客户名称不能为空");
				return;
			}
			if(_this.isNull(_this.customerType) || _this.customerType == -1) {
				$.toptip("客户类型不能为空");
				return;
			}

			var baseValue = {
				customerKey: _this.customerKey,
				customerName: _this.customerName,
				customerType: _this.customerType
			}
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/customer/edit',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(res.success) {
					$.toast(res.message, function() {
						window.location.href = _common.version('./chooseCustomer.html?role=' + _common.getUrlParam('role') + '&type=' + _common.getUrlParam("type") + '&planKey=' + _common.getUrlParam('planKey') + '&isInputPlanPage=' + _common.getUrlParam('isInputPlanPage'));
					});
				}
			});
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		}
	}
});

$('#customerType').change(function() {
	var customerKey = $(this).val();

	if(editCustomer.isNull(customerKey)) {
		editCustomer.customerType = "";
		$(".weui-select").removeClass("choosed");
	} else {
		editCustomer.customerType = customerKey;
		$(".weui-select").addClass("choosed");
	}
	console.log(editCustomer.customerType);
});