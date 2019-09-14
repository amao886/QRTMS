var vm = new Vue({
	el: '#askBecomeDriver',
	data: {
		inviteKey: _common.getUrlParam('inviteKey')
	},
	methods: {
		submit: function(agree) {
			var _this = this;

			var baseValue = {
				inviteKey: _this.inviteKey,
				result: agree
			}
			EasyAjax.ajax_Post_Json({
				url: 'driver/company/handleInviteAsk',
				data: JSON.stringify(baseValue)
			}, function(res) {
				$.toast(res.message, function() {
					if(_this.isWeixin()) {
						window.location.href = _common.version("../../home.html");
					} else {
						_this.closeWindows();
					}
				});
			});
		},
		//判断是否是在微信页
		isWeixin: function() {
			var ua = navigator.userAgent.toLowerCase();
			var isWeixin = ua.indexOf('micromessenger') != -1;
			if(isWeixin) {
				return true;
			} else {
				return false;
			}
		},
		//关闭浏览器
		closeWindows: function() {
			var userAgent = navigator.userAgent;
			if(userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Chrome") != -1) {
				close(); //直接调用JQUERY close方法关闭
			} else {
				window.opener = null;
				window.open("", "_self");
				window.close();
			}
		}
	}
})