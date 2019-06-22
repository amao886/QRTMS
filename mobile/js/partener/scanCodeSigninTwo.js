var createCompany = new Vue({
	el: '#createCompany',
	data: {
		areaVersion: 0,
		areaData: []
	},
	created: function() {
		var _this = this;

		if(window.sessionStorage) {
			var recoedAreaVersion = sessionStorage.getItem('areaVersion');
			if(recoedAreaVersion) {
				_this.areaVersion = recoedAreaVersion;
			}
		}
		_this.getAreasData();
	},
	methods: {
		getAreasData: function() {
			var _this = this;

			var baseValue = {
				version: _this.areaVersion
			}
			EasyAjax.ajax_Post_Json({
				url: 'special/support/area/all',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					console.log(res);

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
						_this.initWidget();
					}, 200);
				}
			});
		},
		initWidget: function() {
			$("#companyAddressChoose").cityPicker({
				title: "请选择企业地址省市区/县",
			});
		},
		submit: function() {
			var _this = this;

			var name = $("#name").val();
			if(!name) {
				$.toptip('企业名称不能为空');
				return;
			}

			if(_this.isNull($("#companyAddressChoose").val())) {
				$.toptip("企业地址省市区/县不能为空");
				return;
			} else if(_this.isNull($.trim($("#companyAddress").val()))) {
				$.toptip("企业地址不能为空");
				return;
			}

			var address = $("#companyAddressChoose").val().replace(/\s*/g, "") + $.trim($("#companyAddress").val());

			var baseValue = {
				companyName: name,
				customerKey: _common.getUrlParam('customerKey'),
				address: address
			}
			EasyAjax.ajax_Post_Json({
				url: '/enterprise/company/save',
				data: JSON.stringify(baseValue)
			}, function(res) {
				$.toast("操作成功", function() {
					window.location.href = _common.version("../../home.html");
				});
			});
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		}
	}
});