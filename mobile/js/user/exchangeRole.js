const vm = new Vue({
	el: '#content',
	data: {
		identify: '',
		headImg: '',
		identitys: []
	},
	methods: {
		changeRole: function(identityKey) {
			var _this = this;

			var baseValue = {
				identityKey: identityKey
			}
			EasyAjax.ajax_Post_Json({
				url: 'admin/user/identity/change/',
				data: JSON.stringify(baseValue)
			}, function(res) {
				window.location.href = _common.version("./peopleCenter.html");

			});
		},
		initData: function(identify) {
			var _this = this;

			var roles = ['司机', '货主', '承运商', '收货方'];

			for(var i = 0; i < roles.length; i++) {
				if(identify != i + 1) {
					var obj = new Object();
					obj['value'] = roles[i];
					obj['key'] = i + 1;
					_this.identitys.push(obj);
				}
			}
		}
	},
	created: function() {
		var _this = this;

		//获取地址栏参数 
		_this.identify = _common.getUrlParam('identify');
		_this.headImg = _common.getUrlParam('headImg');
		console.log(_this.identify)
		console.log(_this.headImg)

		_this.initData(_this.identify);
	}
})