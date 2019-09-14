var signinSuccess = new Vue({
	el: '#signinSuccess',
	data: {
		identityKey: 0 //用户身份标识(1:司机,2:发货方,3:承运方,4:收货方)
	},
	created: function() {
		var _this = this;

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
		chooseHomepageCommonFunctionMenu: function(id) {
			var _this = this;

			if(id == 0) {
				_this.identityKey = 2;
			} else if(id == 1) {
				_this.identityKey = 4;
			} else if(id == 2) {
				_this.identityKey = 3;
			} else if(id == 3) {
				_this.identityKey = 1;
			}

			var baseValue = {
				identityKey: _this.identityKey
			}
			EasyAjax.ajax_Post_Json({
				url: 'admin/user/identity/change',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					$.toast("操作成功", function() {
						window.location.href = _common.version("../../home.html");
					});
				}
			});
		}
	}
});