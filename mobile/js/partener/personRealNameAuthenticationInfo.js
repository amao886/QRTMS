var codeDetail = new Vue({
	el: '#personauthInfo',
	data: {
		listResult: null,
		pathPrefix: "",
		personals: [],
		enterprises: []
	},
	created: function() {
		this.getListData();
	},
	mounted: function() {

	},
	methods: {
		getListData: function() {
			var _this = this;
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/order/userCertified/search'
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					_this.listResult = res.reslut;
					if(res.reslut) {
						_this.personals = res.reslut.personals;
						_this.enterprises = res.reslut.enterprises;
						_this.pathPrefix = res.pathPrefix;
					}
				}
			});
		},
		setMySignature: function() {
			window.location.href = _common.version('./choosePersonSignature.html');
		}
	}
});