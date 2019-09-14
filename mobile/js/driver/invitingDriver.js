var vm = new Vue({
	el: '#invitingDriver',
	data: {
		driverName: '', //司机名称
		driverNumber: '', //司机号码
	},
	methods: {
		saveDriver: function() {
			var _this = this;

			if(_this.isNull(_this.driverName)) {
				$.toptip('司机姓名不能为空');
				return;
			}
			if(_this.isNull(_this.driverNumber)) {
				$.toptip('手机号码不能为空');
				return;
			}
			if(!/^[\u4E00-\u9FFF]{2,15}$/.test(_this.driverName)) {
				$.toptip('司机姓名只能输入中文');
				return;
			}
			if(!_this.driverNumber || !/1\d{10}$/.test(_this.driverNumber)) {
				$.toptip('请输入正确的手机号');
				return;
			}

			var baseValue = {
				driverName: _this.driverName,
				driverPhone: _this.driverNumber
			}
			EasyAjax.ajax_Post_Json({
				url: 'driver/company/inviteAsk',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if (res.success) {
					window.location.href = _common.version("../../view/driver/driverManage.html");
				}
			});
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		}
	}
})