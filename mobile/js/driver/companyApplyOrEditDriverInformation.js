const vm = new Vue({
	el: '#driverInformation',
	data: {
		identify: _common.getUrlParam('identify'),
		driver: {
			identify: '',
			name: "",
			phone: "",
			cars: [{
				type: '',
				length: '',
				loadValue: '',
				license: ""
			}],
			routes: [{
				type: 1,
				starts: [],
				ends: []
			}]
		},
		areaVersion: 0
	},
	mounted: function() {
		var _this = this;
		EasyAjax.ajax_Post_Json({
			url: 'driver/company/getDriver/' + _this.identify,
		}, function(res) {
			console.log(res);
			if(res.success) {
				_this.driver = res.driver;
				if(res.driver.routes[0].starts != null) {
					$("#startAddress").val(res.driver.routes[0].starts.join(' '))
				}
				if(res.driver.routes[0].ends != null) {
					$("#endAddress").val(res.driver.routes[0].ends.join(' '));
				}
			}
		})

		if(window.sessionStorage) {
			_this.areaVersion = sessionStorage.getItem('areaVersion') | 0;
		}
		_this.getAreasData(_this.areaVersion);
	},
	methods: {
		saveSubmit: function() {
			var _this = this;
			_this.driver.routes[0].starts = $("#startAddress").val().split(' ')
			_this.driver.routes[0].ends = $("#endAddress").val().split(' ')

			if(_this.driver.name == '' | _this.driver.name == null) {
				$.toptip("姓名不能为空")
				return;
			}
			if(!/^[\u4e00-\u9fa5]+$/gi.test(_this.driver.name)) {
				$.toptip("名字只能输入汉字");
				return;
			}
			if(_this.driver.phone == '' | _this.driver.phone == null) {
				$.toptip("手机号码不能为空")
				return;
			}
			if(!(/^1\d{10}$/.test(_this.driver.phone))) {
				$.toptip("手机号码格式不正确");
				return;
			}
			var parmas = _this.driver
			console.log(parmas)
			EasyAjax.ajax_Post_Json({
				url: 'driver/company/modify',
				data: JSON.stringify(parmas)
			}, function(res) {
				console.log(res)
				if(res.success) {
					window.location.href = '../../view/driver/driverManage.html'
				}
			});

		},
		getAreasData: function(version) {
			var _this = this;
			var baseValue = {
				version: version
			}
			EasyAjax.ajax_Post_Json({
				url: 'special/support/area/all',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(window.sessionStorage) {
					if(_this.areaVersion < res.version) {
						sessionStorage.removeItem('areaData');
						sessionStorage.setItem('areaVersion', res.version);
						sessionStorage.setItem('areaData', JSON.stringify(res.result));
						loadData(res.result);
					} else {
						sessionStorage.setItem('areaVersion', res.version);

						var areaData = JSON.parse(sessionStorage.getItem('areaData'));
						if(areaData) {
							loadData(areaData);
						}
					}
				} else {
					loadData(res.result);
				}
				setTimeout(function() {
					$("#startAddress").cityPicker({
						title: "请选择发货地址省市区/县"
					});
					$("#endAddress").cityPicker({
						title: "请选择发货地址省市区/县"
					});
				}, 200)
			});
		},
	},

})