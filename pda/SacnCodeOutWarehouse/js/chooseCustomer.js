var customerList = new Vue({
	el: '#chooseCustomer',
	data: {
		type: _common.getUrlParam("type"),
		status: 1,
		likeName: '',
		customers: []
	},
	created: function() {
		var _this = this;

		mui.init();
		_this.getListData();
	},
	methods: {
		getListData: function() {
			var _this = this;

			var baseValue = {
				likeName: _this.likeName,
				type: _this.type,
				status: _this.status
			}
			MuiAjax.ajax_Post_Json({
				url: 'enterprise/customer/list',
				data: JSON.stringify(baseValue)
			}, function(res) {
				console.log(res.customers);

				_this.customers = res.customers;

				setTimeout(function() {
					$(".mui-icon-clear").each(function(i) {
						mui(".mui-icon-clear")[i].addEventListener("tap", function() {
							if(i == 0) {
								customerList.likeName = '';
								customerList.getListData();
							}
						})
					})
				}, 200)
			});
		},
		inputName: function() {
			var _this = this;

			setTimeout(function() {
				_this.getListData();
			}, 500)
		},
		chooseCustomer: function(customerObj) {
			var _this = this;

			window.location.href = _common.version("./index.html?type=" + _this.type + "&chooseInfo=" + JSON.stringify(customerObj));
		}
	}
});