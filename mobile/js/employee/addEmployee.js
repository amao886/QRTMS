const vm = new Vue({
	el: '#app',
	data: {
		employeeName: '',
		employeeTel: ''
	},
	methods: {
		//添加员工
		Save: function() {

			const _this = this;
			var parmas = {
				employeeName: _this.employeeName,
				mobile: _this.employeeTel
			}
			if(_this.isNull(parmas.employeeName)) {
				$.toptip("姓名不能为空");
				return;
			}
			if(!(parmas.employeeName.length>0&&parmas.employeeName.length<=16)){
					$.toptip("名称长度不能超过16个字符");
					return;
				}
				if(_this.isNull(parmas.mobile)) {
				$.toptip("手机号码不能为空");
				return;
			}
				if(!(/^1\d{10}$/.test(parmas.mobile))){
					$.toptip("手机号码格式不正确");
				return;
				}
			console.log(parmas)
			EasyAjax.ajax_Post_Json({
				url: '/enterprise/company/employee/add',
				data: JSON.stringify(parmas)
			}, function(res) {
				console.log(res)
				if(res.success) {
					$.toast(res.message, function() {
						window.location.href = './allocatPermission.html?employeeId=' + res.employeeKey;
					})
				} else {
					$.toast(res.message)
				}
			})
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		}
	}
})