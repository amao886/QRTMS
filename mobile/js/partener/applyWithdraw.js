var codeDetail = new Vue({
	el: '#applyWithdraw',
	data: {
		userResource: null,
		userLegalize: null,
		money: 0
	},
	created: function() {
		this.getData();
		this.chooseMoney(0);
	},
	mounted: function() {

	},
	methods: {
		getData: function() {
			var _this = this;
			EasyAjax.ajax_Post_Json({
				url: 'sys/user/resource/1'
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					_this.userResource = res.userResource;
					_this.userLegalize = res.userLegalize;
				}
			});
		},
		chooseMoney: function(index) {
			var _this = this;
			if(index == 0) {
				$("#400").removeClass("selected");
				$("#500").removeClass("selected");
				$("#300").addClass("selected");
				_this.money = 300;
			} else if(index == 1) {
				$("#300").removeClass("selected");
				$("#500").removeClass("selected");
				$("#400").addClass("selected");
				_this.money = 400;
			} else if(index == 2) {
				$("#300").removeClass("selected");
				$("#400").removeClass("selected");
				$("#500").addClass("selected");
				_this.money = 500;
			}
		},
		withdraw: function() {
			var _this = this;
			if(_this.userResource.money < (_this.money * 100)){
				$.toptip('当前账户余额不足你所选择的提现金额，不能提现');
				return;
			}
			var baseValue = {
				extractValue: _this.money
			}
			EasyAjax.ajax_Post_Json({
				url: 'sys/user/wallet/extract',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(res.success) {
					$.toast(res.message, function() {
						window.location.href = _common.version("./myWallet.html");
					});
				}
			});
		}
	}
});