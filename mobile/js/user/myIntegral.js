var codeDetail = new Vue({
	el: '#accountIntegral',
	data: {
		userResource: null,
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
				url: 'sys/user/resource/2'
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					_this.userResource = res.userResource;
					_this.listResource = res.listResource;
				}
			});
		}
	}
});