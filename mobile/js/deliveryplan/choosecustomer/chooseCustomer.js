var customerList = new Vue({
	el: '#customerList',
	data: {
		likeName: '',
		type: _common.getUrlParam("type"), //1：发货客户，2：收货客户，3：承运商
		status: 1, //1：正常状态
		customers: [],
		customersServer: []
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

			_this.getCustomerListData();
		},
		getCustomerListData: function() {
			var _this = this;

			var baseValue = {
				likeName: _this.likeName,
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
					_this.customersServer = res.customers;

					setTimeout(function() {
						$('.weui-cell_swiped').swipeout();
					}, 200)
				}
			});
		},
		chooseCustomerType: function() {
			var _this = this;

			_this.closeSwipe();

			_this.type = $("#customerType").val();

			_this.getCustomerListData();
		},
		search: function() {
			var _this = this;

			_this.closeSwipe();

			_this.getCustomerListData();
		},
		clear: function() {
			var _this = this;

			_this.closeSwipe();

			_this.likeName = '';
		},
		addCustomer: function() {
			window.location.href = _common.version('./addCustomer.html?role=' + _common.getUrlParam('role') + '&type=' + _common.getUrlParam("type") + '&planKey=' + _common.getUrlParam('planKey') + '&isInputPlanPage=' + _common.getUrlParam('isInputPlanPage'));
		},
		editCustomer: function(customerKey, conpmayName, customerType) {
			window.location.href = _common.version('./editCustomer.html?key=' + customerKey + '&name=' + conpmayName + '&customerType=' + customerType + '&role=' + _common.getUrlParam('role') + '&type=' + _common.getUrlParam("type") + '&planKey=' + _common.getUrlParam('planKey') + '&isInputPlanPage=' + _common.getUrlParam('isInputPlanPage'));
		},
		closeSwipe: function() {
			$('.weui-cell_swiped').swipeout('close') //关闭
		},
		chooseCustomerInfo: function(index, conpmayName) {
			var _this = this;

			var transform = $($('.weui-cell__bd')[index]).css('transform');
			var transformX = transform.slice(7, transform.length - 1).split(', ')[4];
			console.log(transformX);

			if(transformX < 0) {
				return;
			}

			if(_common.getUrlParam('isInputPlanPage') == 'true') {
				window.location.href = _common.version('../' + _common.getUrlParam('role') + '/inputDeliveryPlanOne.html?isChooseType=' + _common.getUrlParam("type") + '&name=' + conpmayName);
			} else {
				window.location.href = _common.version('../' + _common.getUrlParam('role') + '/generateInvoiceOne.html?isChooseType=' + _common.getUrlParam("type") + '&name=' + conpmayName + '&planKey=' + _common.getUrlParam('planKey'));
			}
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		}
	}
});