var allocatPermission = new Vue({
	el: '#permissionWrapper',
	data: {
		employeeId: _common.getUrlParam('employeeId'),
		employee: {},
		seals: [],
		baseAuthoritys: [],
		customers: [],
		authoritys: [],
	},
	created: function() {
		var _this = this;

		_this.getData();
	},
	computed: {
		isShowClearIcon: function() {
			var _this = this;

			return _this.isNull(_this.employee.employeeName) ? false : true;
		}
	},
	methods: {
		getData: function() {
			var _this = this;

			EasyAjax.ajax_Post_Json({
				url: 'enterprise/company/employee/detail/' + _this.employeeId,
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					console.log(res);
					_this.employee = res.employee;
					_this.seals = res.seals;
					_this.baseAuthoritys = res.baseAuthoritys;
					_this.customers = res.customers;
					_this.authoritys = res.authoritys;

					setTimeout(function() {
						_this.loadData();
						_this.getPermissionData();
						_this.changeAccountState();
					}, 200)
				}
			});
		},
		getPermissionData: function() {
			var _this = this;

			if(_this.baseAuthoritys == null || _this.baseAuthoritys.length <= 0) {
				return;
			}
			if(_this.authoritys == null || _this.authoritys.length <= 0) {
				return;
			}

			var chooseGroup = [];
			var chooseAll = [];
			for(var i = 0; i < _this.baseAuthoritys.length; i++) {
				chooseGroup.push(false);
				chooseAll.push(false);
			}

			for(var i = 0; i < _this.baseAuthoritys.length; i++) {
				for(var j = 0; j < _this.authoritys.length; j++) {
					if(_this.baseAuthoritys[i].id == _this.authoritys[j]) {
						chooseGroup[i] = true;
						break;
					}
				}
			}

			if(window.sessionStorage) {
				var permissionData = JSON.parse(sessionStorage.getItem("permissionData"));

				if(permissionData) {
					var count = 0;
					for(var i = 0; i < chooseGroup.length; i++) {
						for(var j = 0; j < _this.baseAuthoritys[i].children.length; j++) {
							for(var k = 0; k < _this.authoritys.length; k++) {
								if(_this.baseAuthoritys[i].children[j].id == _this.authoritys[k]) {
									$("#item" + i + j).find("label").attr('data-afterContent', _this.baseAuthoritys[i].children[j].menuName);
									$('#childInput' + i + j).prop("checked", true);
									$("#item" + i + j).find("label").html("");
									count++;
									break;
								}
							}
						}
						if(_this.baseAuthoritys[i].children.length == count) {
							chooseAll[i] = true;
						}
						count = 0;
					}
				} else {
					var count = 0;
					for(var i = 0; i < chooseGroup.length; i++) {
						if(chooseGroup[i]) {
							for(var j = 0; j < _this.baseAuthoritys[i].children.length; j++) {
								for(var k = 0; k < _this.authoritys.length; k++) {
									if(_this.baseAuthoritys[i].children[j].id == _this.authoritys[k]) {
										$("#item" + i + j).find("label").attr('data-afterContent', _this.baseAuthoritys[i].children[j].menuName);
										$('#childInput' + i + j).prop("checked", true);
										$("#item" + i + j).find("label").html("");
										count++;
										break;
									}
								}
							}
							if(_this.baseAuthoritys[i].children.length == count) {
								chooseAll[i] = true;
							}
							count = 0;
						}
					}
				}
			} else {
				var count = 0;
				for(var i = 0; i < chooseGroup.length; i++) {
					if(chooseGroup[i]) {
						for(var j = 0; j < _this.baseAuthoritys[i].children.length; j++) {
							for(var k = 0; k < _this.authoritys.length; k++) {
								if(_this.baseAuthoritys[i].children[j].id == _this.authoritys[k]) {
									$("#item" + i + j).find("label").attr('data-afterContent', _this.baseAuthoritys[i].children[j].menuName);
									$('#childInput' + i + j).prop("checked", true);
									$("#item" + i + j).find("label").html("");
									count++;
									break;
								}
							}
						}
						if(_this.baseAuthoritys[i].children.length == count) {
							chooseAll[i] = true;
						}
						count = 0;
					}
				}
			}

			for(var i = 0; i < chooseAll.length; i++) {
				if(chooseAll[i]) {
					$("#group" + i).text("取消全选");
					$('#groupInput' + i).prop("checked", true);
				} else {
					$("#group" + i).text("全选");
					$('#groupInput' + i).prop("checked", false);
				}
			}

			if(_this.employee.employeeType == 1) {
				for(var i = 0; i < _this.baseAuthoritys.length; i++) {
					$('#groupInput' + i).prop("disabled", true);
					for(var j = 0; j < _this.baseAuthoritys[i].children.length; j++) {
						$('#childInput' + i + j).prop("disabled", true);
					}
				}
			}
		},
		itemClick: function(groupIndex, itemIndex) {
			var _this = this;

			var chooseAll = true;
			$("#item" + groupIndex + itemIndex).find("label").attr('data-afterContent', _this.baseAuthoritys[groupIndex].children[itemIndex].menuName);
			if($('#childInput' + groupIndex + itemIndex).is(':checked')) {
				$("#item" + groupIndex + itemIndex).find("label").html("");
			} else {
				$("#item" + groupIndex + itemIndex).find("label").html(_this.baseAuthoritys[groupIndex].children[itemIndex].menuName);
			}

			for(var i = 0; i < _this.baseAuthoritys[groupIndex].children.length; i++) {
				if(!$('#childInput' + groupIndex + i).is(':checked')) {
					chooseAll = false;
					break;
				}
			}

			if(chooseAll) {
				$("#group" + groupIndex).text("取消全选");
				$('#groupInput' + groupIndex).prop("checked", true);
			} else {
				$("#group" + groupIndex).text("全选");
				$('#groupInput' + groupIndex).prop("checked", false);
			}
		},
		groupClick: function(groupIndex) {
			var _this = this;

			if($('#groupInput' + groupIndex).is(':checked')) {
				$("#group" + groupIndex).text("取消全选");
				for(var i = 0; i < _this.baseAuthoritys[groupIndex].children.length; i++) {
					$('#childInput' + groupIndex + i).prop("checked", true);
					$("#item" + groupIndex + i).find("label").attr('data-afterContent', _this.baseAuthoritys[groupIndex].children[i].menuName);
					if($('#childInput' + groupIndex + i).is(':checked')) {
						$("#item" + groupIndex + i).find("label").html("");
					} else {
						$("#item" + groupIndex + i).find("label").html(_this.baseAuthoritys[groupIndex].children[i].menuName);
					}
				}
			} else {
				$("#group" + groupIndex).text("全选");
				for(var i = 0; i < _this.baseAuthoritys[groupIndex].children.length; i++) {
					$('#childInput' + groupIndex + i).prop("checked", false);
					$("#item" + groupIndex + i).find("label").attr('data-afterContent', _this.baseAuthoritys[groupIndex].children[i].menuName);
					if($('#childInput' + groupIndex + i).is(':checked')) {
						$("#item" + groupIndex + i).find("label").html("");
					} else {
						$("#item" + groupIndex + i).find("label").html(_this.baseAuthoritys[groupIndex].children[i].menuName);
					}
				}
			}
		},
		changeAccountState: function() {
			var _this = this;

			if($('#accountState').is(':checked')) {
				_this.employee.userFettle = 1;
			} else {
				_this.employee.userFettle = 0;
			}
		},
		deleteEmployee: function() {
			var _this = this;

			$.confirm("删除员工后，该员工将无法再操作企业订单，是否确认删除？", "删除员工", function() {
				EasyAjax.ajax_Post_Json({
					url: 'enterprise/company/employee/delete/' + _this.employeeId,
				}, function(res) {
					$.toast(res.message, function() {
						window.location.href = _common.version('./myEmployee.html');
					});
				});
			});
		},
		save: function() {
			var _this = this;

			var name = _this.employee.employeeName;
			if(_this.isNull(name)) {
				$.toptip("姓名不能为空");
				return;
			}

			if(!/^[\u4e00-\u9fa5]+$/gi.test(name)) {
				$.toptip("姓名只能输入中文");
				return;
			}

			var arr = _this.getChoosePermissionData();
			var authoritys = arr.join(",");
			console.log(authoritys);

			var baseValue = {
				"employeeId": _this.employeeId,
				"authorityKeys": authoritys,
				"employeeName": name,
				"userFettle": _this.employee.userFettle
			}
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/company/employee/edit',
				data: JSON.stringify(baseValue)
			}, function(res) {
				$.toast(res.message, function() {
					if(window.sessionStorage) {
						sessionStorage.removeItem("permissionData");
					}
					window.location.href = _common.version('./myEmployee.html');
				});
			});
		},
		getChoosePermissionData: function() {
			var _this = this;

			var arr = new Array();

			if(_this.baseAuthoritys == null || _this.baseAuthoritys.length <= 0) {
				return;
			}

			var arrayIndex = 0;
			for(var i = 0; i < _this.baseAuthoritys.length; i++) {
				if($('#groupInput' + i).is(':checked')) {
					arr[arrayIndex] = $('#groupInput' + i).val();
					arrayIndex++;
				}
				for(var j = 0; j < _this.baseAuthoritys[i].children.length; j++) {
					if($('#childInput' + i + j).is(':checked')) {
						arr[arrayIndex] = $('#childInput' + i + j).val();
						arrayIndex++;
					}
				}
			}

			return arr;
		},
		saveData: function() {
			var _this = this;

			if(window.sessionStorage) {
				var permissionData = {
					name: _this.employee.employeeName,
					authoritys: _this.getChoosePermissionData(),
					accountStatus: _this.employee.userFettle
				};

				sessionStorage.setItem("permissionData", JSON.stringify(permissionData));
			}
		},
		loadData: function() {
			var _this = this;

			if(window.sessionStorage) {
				var permissionData = JSON.parse(sessionStorage.getItem("permissionData"));

				if(permissionData) {
					_this.employee.employeeName = permissionData.name;
					_this.authoritys = permissionData.authoritys;
					_this.employee.userFettle = permissionData.accountStatus;

					if(_this.employee.userFettle == 1) {
						$('#accountState').prop("checked", true);
					} else {
						$('#accountState').prop("checked", false);
					}
				}
			}
		},
		toCustomerPage: function() {
			var _this = this;

			_this.saveData();

			window.location.href = _common.version('./customerPermission.html?employeeId=' + _this.employeeId + '&customers=' + _this.customers + '&employeeType=' + _this.employee.employeeType);
		},
		toSignaturePage: function() {
			var _this = this;

			_this.saveData();

			window.location.href = _common.version('./signatureAuth.html?employeeId=' + _this.employeeId + '&name=' + _this.employee.employeeName + '&seals=' + _this.seals + '&employeeType=' + _this.employee.employeeType);
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		},
		clear: function() {
			var _this = this;

			_this.employee.employeeName = '';
		}
	}
});