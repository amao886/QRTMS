var complain = new Vue({
	el: '#complainWrapper',
	data: {
		complainantContent: '',
		orderId: _common.getUrlParam("id"),
		fromHomePage: _common.getUrlParam("fromHomePage"),
		fromComplainPage: _common.getUrlParam("fromComplainPage"),
	},
	methods: {
		submit: function() {
			var _this = this;

			if (_this.isNull(_this.complainantContent)) {
				$.toptip('投诉内容不能为空');
				return;
			}

			var orderIdAry = [_this.orderId];

			var baseValue = {
				orderKeys: JSON.stringify(orderIdAry),
				content: _this.complainantContent
			}
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/complaint/entry/complaint',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(res.success) {
					$.toast(res.message, function() {
						window.location.href = _common.version("../../view/partener/transportManageDetail.html?id=" + _this.orderId + "&fromHomePage=" + _this.fromHomePage + "&fromComplainPage=" + _this.fromComplainPage);
					});
				} else {
					$.toast(res.message);
				}
			});
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		}
	}
});