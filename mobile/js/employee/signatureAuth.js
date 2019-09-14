const vm = new Vue({
	el: '#main',
	data: {
		list: [],
		imagePath: '',
		employeeName: '',
		seals: [],
		employeeType:0

	},
	mounted: function() {
		var _this = this;
		_this.employeeName = _common.getUrlParam('name')
		_this.employeeType = _common.getUrlParam('employeeType')
		//发送请求，获取员工列表数据渲染到页面
		EasyAjax.ajax_Post_Json({
			url: '/enterprise/company/company/seals',
			data: JSON.stringify({})
		}, function(res) {
			if(res.success) {
				console.log(res);
				_this.list = res.companySeals;

				var seal = _common.getUrlParam('seals');
				if(seal) {
					seal = seal.split(',');
					for(var i = 0; i < seal.length; i++) {
						_this.seals.push(seal[i]);
					}
				}
				_this.imagePath = res.imagePath;
			}
		})
	},
	methods: {
		isTrue: function(id) {
			var _this = this;

			var result = false;
			for(var j = 0; j < _this.seals.length; j++) {
				if(_this.seals[j] == id) {
					result = true;
					break;
				}
			}
			return result;
		},
		//授权
		authorization: function(event) {
			var sealsArr = $(event.target).parent().siblings('.signaturePicture').find('.imgItem');
			console.log(sealsArr);
			var newArr = [];
			var str = ''
			for(var i = 0; i < sealsArr.length; i++) {
				if($(sealsArr[i]).hasClass('active')) {
					newArr.push(sealsArr[i]);
					str += $(sealsArr[i]).find('img').data('seals') + ',';
				}
			}
			//			console.log(newArr)
			//			console.log(str.substr(0,str.length-1))
			var parmas = {
				employeeKey: _common.getUrlParam('employeeId'),
				seals: str.substr(0, str.length - 1)
			}
			console.log(parmas)
			EasyAjax.ajax_Post_Json({
				url: '/enterprise/company/signature/auth',
				data: JSON.stringify(parmas)
			}, function(res) {
				console.log(res)
				if(res.success) {
					$.toast(res.message, function() {
						window.location.href = './allocatPermission.html?employeeId=' + _common.getUrlParam('employeeId')
					})
				}

			})

		},
		toggleClass: function(event) {
			var _this = this;
			if(_this.employeeType==1){
				return
			}
			$(event.target).parent().toggleClass("active")
		}
	}
})