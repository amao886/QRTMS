var complainList = new Vue({
	el: '#reveiveGoodFinish',
	data: {
		fromReceiveGoodNormalPage: _common.getUrlParam("fromReceiveGoodNormalPage"), //true:正常收货进入  false:异常收货进入
		tripContent: ''
	},
	created: function() {
		var _this = this;

		if (_this.fromReceiveGoodNormalPage == 'true') {
			_this.tripContent = '收货成功，感谢您的评价';
		} else {
			_this.tripContent = '异常提交成功，感谢您的反馈';
		}
	},
	methods: {
		backReceiveGoodManage: function() {
			var _this = this;

			window.location.href = _common.version("./transportManageList.html?transportSts=receive");
		},
		backHome: function() {
			var _this = this;

			window.location.href = _common.version("../../home.html");
		}
	}
});