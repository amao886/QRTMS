var allotLogistics = new Vue({
	el: '#allotLogisticsWrapper',
	data: {
		type: 3, //3：承运商
		status: 1, //1：正常状态
		reg: 1, //1:注册
		customers: [],
		logisticsName: '',
		logisticsContact: '',
		logisticsContactPhoneNum: ''
	},
	created: function() {
		var _this = this;

		_this.getListData();
	},
	methods: {
		getListData: function() {
			var _this = this;

			var baseValue = {
				type: _this.type,
				status: _this.status,
				reg: _this.reg
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
				}
			});
		},
		allot: function() {
			var _this = this;

			if(_this.isNull(_this.logisticsName)) {
				$.toptip("物流商不能为空");
				return;
			}

			var baseValue = {
				planKeys: _common.getUrlParam('planKey'),
				customerKey: $("#logistics").find("option:selected").val(),
				driverName: _this.logisticsContact,
				driverContact: _this.logisticsContactPhoneNum
			}
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/plan/designate',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(res.success) {
					$.toast(res.message, function() {
						window.location.href = _common.version("./deliveryPlan.html");
					});
				}
			});
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		}
	}
});