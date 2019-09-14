const vm = new Vue({
	el: '#driverInformation',
	data: {
		driver:{
			driverKey: '', //司机id
			name: '', //司机姓名
			phone: '' ,//手机号
			routes: [{
				type: '', //路线类型
				starts: '', //路线起点
				ends: '', //路线终点
			}],
			cars: [{
				type: '', //车型
				length: '', //车长
				loadValue: '', //载重
				license: '', //车牌号
			}],	
		},
		areaVersion: 0
	},
	mounted: function() {
		var _this = this;
			EasyAjax.ajax_Post_Json({
				url: 'driver/personal/info',
			}, function(res) {
				console.log(res);
				if(res.success) {
					 if(res.driver!=null) {
					 _this.driver.driverKey = res.driver.driverKey;
					_this.driver.name = res.driver.name;
					_this.driver.phone = res.driver.phone;
					_this.driver.routes[0].type = res.driver.routes[0].type;
					 if(res.driver.routes[0].starts!=null){
					 	$("#startAddress").val(res.driver.routes[0].starts.join(' '))
					 }
					 if(res.driver.routes[0].ends!=null){
					 	$("#endAddress").val(res.driver.routes[0].ends.join(' '));
					 }
					_this.driver.cars[0].license = res.driver.cars[0].license
					_this.driver.cars[0].type = res.driver.cars[0].type
					_this.driver.cars[0].length = res.driver.cars[0].length
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
          console.log(_this.driver.driverKey)
			if(_this.driver.name == '' | _this.driver.name == null) {
				$.toptip("姓名不能为空")
				return;
			}
			if(!/^[\u4e00-\u9fa5]+$/gi.test(_this.driver.name)){
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
			if(_this.driver.driverKey) {
          var parmas = _this.driver
				console.log(parmas)
				EasyAjax.ajax_Post_Json({
					url: '/driver/personal/modify',
					data: JSON.stringify(parmas)
				}, function(res) {
					console.log(res)
					if(res.success) {
						window.location.href = '../../view/driver/personalDirverInformation.html'
					}
				});
			} else {
				var parmas = {
				name:_this.driver.name,
				phone:_this.driver.phone,
				cars: _this.driver.cars,
				routes: _this.driver.routes,
			}
				EasyAjax.ajax_Post_Json({
					url: 'driver/personal/register',
					data: JSON.stringify(parmas)
				}, function(res) {
					console.log(res)
					if(res.success) {
						window.location.href = '../../view/driver/personalDirverInformation.html'
					}
				});
			}
				
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