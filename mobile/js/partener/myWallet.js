var codeDetail = new Vue({
	el: '#accountBalance',
	data: {
		userResource: null,
		userLegalize: null,
		listResource: []
	},
	created: function() {
		this.getData();
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
					_this.listResource = res.listResource;
				}
			});
		},
		withdraw: function() {
			var _this = this;
			if(_this.userLegalize == null) {
				$.modal({
					title: "",
					text: "完成实名认证后才可以提现哦！",
					buttons: [{
							text: "取消",
							className: "default"
						},
						{
							text: "去认证",
							onClick: function() {
								window.location.href = _common.version('./personRealNameAuthentication.html?type=withdraw');
							}
						}
					]
				});
			} else {
				window.location.href = _common.version("./applyWithdraw.html");
			}
		}
	}
});