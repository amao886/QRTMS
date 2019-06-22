var inputPlan = new Vue({
	el: '#inputPlan',
	data: {
		templates: [],
		required: [],
		optional: [],
		areaVersion: 0,
		areaData: [],
		templateKey: -1,
		describes: [],
		isChooseType: _common.getUrlParam('isChooseType')
	},
	created: function() {
		var _this = this;

		if(_this.isNull(_this.isChooseType)) {
			_this.getModelListData();
		} else {
			_this.getAreasData();
			_this.loadRecordData();
		}
	},
	methods: {
		getAreasData: function(version) {
			var _this = this;

			var baseValue = {
				version: version
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
				}
			});
		},
		initWidget: function() {
			$("#deliveryDate").mobiscroll($.extend(opt['date'], opt['default']));
			$("#deliveryTime").mobiscroll($.extend(opt['datetime'], opt['default']));
			$("#arrivalTime").mobiscroll($.extend(opt['datetime'], opt['default']));

			$("#startPlaceChoose").cityPicker({
				title: "请选择始发地省市区/县",
			});
			$("#endPlaceChoose").cityPicker({
				title: "请选择目的地省市区/县"
			});
			$("#deliveryAddressChoose").cityPicker({
				title: "请选择发货地址省市区/县"
			});
			$("#receiveAddressChoose").cityPicker({
				title: "请选择收货地址省市区/县"
			});
		},
		getModelListData: function() {
			var _this = this;
			var baseValue = {
				category: 2 //1：发货模板  2：计划模板
			}
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/template/list',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					console.log(res);
					_this.templates = res.templates;

					_this.getAreasData();
				}
			});
		},
		next: function() {
			var _this = this;

			if(_this.required.shipperName != null) {
				if(_this.isNull($("#shipper").val())) {
					$.toptip("发货方不能为空");
					return;
				}
			}
			if(_this.required.planNo != null) {
				if(_this.isNull($("#deliveryPlanNumber").val())) {
					$.toptip("发货计划单号不能为空");
					return;
				}
			}
			if(_this.required.receiveName != null) {
				if(_this.isNull($("#receiveCustomer").val())) {
					$.toptip("收货客户不能为空");
					return;
				}
			}
			if(_this.required.receiverName != null) {
				if(_this.isNull($("#receive").val())) {
					$.toptip("收货人不能为空");
					return;
				}
			}
			if(_this.required.receiverContact != null) {
				if(_this.isNull($("#contact").val())) {
					$.toptip("联系方式不能为空");
					return;
				}
			}
			if(_this.required.receiveAddress != null) {
				if(_this.isNull($("#receiveAddressChoose").val())) {
					$.toptip("收货地址省市区/县不能为空");
					return;
				} else if(_this.isNull($("#receiveAddress").val())) {
					$.toptip("收货地址不能为空");
					return;
				}
			}

			_this.saveData();

			window.location.href = _common.version('./inputDeliveryPlanTwo.html?modelNo=' + _this.templateKey);
		},
		chooseCustomer: function(type) {
			var _this = this;

			_this.saveData();

			window.location.href = _common.version('../choosecustomer/chooseCustomer.html?type=' + type + "&role=carrier&isInputPlanPage=true");
		},
		chooseAddress: function() {
			var _this = this;

			_this.saveData();

			window.location.href = _common.version('../choosedeliveryaddress/chooseDeliveryAddress.html?role=carrier&isInputPlanPage=true');
		},
		saveData: function() {
			var _this = this;

			var keyName = ["detailKey", "dataValue"];

			_this.describes.splice(0, _this.describes.length);

			if(_this.required.shipperName != null) {
				//				if(_this.isNull($("#shipper").val())) {
				//					$.toptip("发货方不能为空");
				//					return;
				//				} else {
				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.shipperName;
				waybillInfo[keyName[1]] = $("#shipper").val();

				_this.describes.push(waybillInfo);
				//				}
			}
			if(_this.required.deliveryTime != null) {
				//				if(_this.isNull($("#deliveryDate").val())) {
				//					$.toptip("发货日期不能为空");
				//					return;
				//				} else {
				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.deliveryTime;
				waybillInfo[keyName[1]] = $("#deliveryDate").val();

				_this.describes.push(waybillInfo);
				//				}
			}
			if(_this.required.planNo != null) {
				//				if(_this.isNull($("#deliveryPlanNumber").val())) {
				//					$.toptip("发货计划单号不能为空");
				//					return;
				//				} else {
				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.planNo;
				waybillInfo[keyName[1]] = $("#deliveryPlanNumber").val();

				_this.describes.push(waybillInfo);
				//				}
			}
			if(_this.required.orderNo != null) {
				//				if(_this.isNull($("#orderNumber").val())) {
				//					$.toptip("订单编号不能为空");
				//					return;
				//				} else {
				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.orderNo;
				waybillInfo[keyName[1]] = $("#orderNumber").val();

				_this.describes.push(waybillInfo);
				//				}
			}
			//送货单号
			//			var reg = /^[a-zA-Z0-9]{1,30}$/;
			if(_this.required.deliveryNo != null) {
				//				if(_this.isNull($.trim($("#deliveryNumber").val()))) {
				//					$.toptip("送货单号不能为空");
				//					return;
				//				} else if(!reg.test($.trim($("#deliveryNumber").val()))) {
				//					$.toptip("送货单号应为数字加英文，且长度不能大于30");
				//					return;
				//				} else {
				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.deliveryNo;
				waybillInfo[keyName[1]] = $.trim($("#deliveryNumber").val());

				_this.describes.push(waybillInfo);
				//				}
			}

			if(_this.required.distributeAddress != null) {
				//				if(_this.isNull($("#deliveryAddressChoose").val())) {
				//					$.toptip("发货地址省市区/县不能为空");
				//					return;
				//				} else if(_this.isNull($.trim($("#deliveryAddress").val()))) {
				//					$.toptip("发货地址详细地址不能为空");
				//					return;replace(/\s*/g, ",")
				//				} else {
				var address = $("#deliveryAddressChoose").val().replace(/\ +/g, ",") + " " + $.trim($("#deliveryAddress").val());

				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.distributeAddress;
				waybillInfo[keyName[1]] = address;

				_this.describes.push(waybillInfo);
				//				}
			}

			if(_this.required.conveyName != null) {
				//				if(_this.isNull($("#logistics").val())) {
				//					$.toptip("物流商不能为空");
				//					return;
				//				} else {
				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.conveyName;
				waybillInfo[keyName[1]] = $("#logistics").val();

				_this.describes.push(waybillInfo);
				//				}
			}
			if(_this.required.conveyerName != null) {
				//				if(_this.isNull($.trim($("#contacts").val()))) {
				//					$.toptip("联系人不能为空");
				//					return;
				//				} else {
				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.conveyerName;
				waybillInfo[keyName[1]] = $.trim($("#contacts").val());

				_this.describes.push(waybillInfo);
				//				}
			}
			if(_this.required.conveyerContact != null) {
				//				if(_this.isNull($.trim($("#contactPhone").val()))) {
				//					$.toptip("联系电话不能为空");
				//					return;
				//				} else {
				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.conveyerContact;
				waybillInfo[keyName[1]] = $.trim($("#contactPhone").val());

				_this.describes.push(waybillInfo);
				//				}
			}
			if(_this.required.transportRoute != null) {
				//				if(_this.isNull($.trim($("#transportRoute").val()))) {
				//					$.toptip("运输路线不能为空");
				//					return;
				//				} else {
				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.transportRoute;
				waybillInfo[keyName[1]] = $.trim($("#transportRoute").val());

				_this.describes.push(waybillInfo);
				//				}
			}
			if(_this.required.collectTime != null) {
				//				if(_this.isNull($("#deliveryTime").val())) {
				//					$.toptip("要求提货时间不能为空");
				//					return;
				//				} else {
				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.collectTime;
				waybillInfo[keyName[1]] = $("#deliveryTime").val();

				_this.describes.push(waybillInfo);
				//				}
			}
			if(_this.required.arrivalTime != null) {
				//				if(_this.isNull($("#arrivalTime").val())) {
				//					$.toptip("要求到货时间不能为空");
				//					return;
				//				} else {
				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.arrivalTime;
				waybillInfo[keyName[1]] = $("#arrivalTime").val();

				_this.describes.push(waybillInfo);
				//				}
			}

			if(_this.required.receiveName != null) {
				//				if(_this.isNull($("#receiveCustomer").val())) {
				//					$.toptip("收货客户不能为空");
				//					return;
				//				} else {
				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.receiveName;
				waybillInfo[keyName[1]] = $("#receiveCustomer").val();

				_this.describes.push(waybillInfo);
				//				}
			}
			if(_this.required.receiverName != null) {
				//				if(_this.isNull($("#receive").val())) {
				//					$.toptip("收货人不能为空");
				//					return;
				//				} else {
				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.receiverName;
				waybillInfo[keyName[1]] = $("#receive").val();

				_this.describes.push(waybillInfo);
				//				}
			}
			if(_this.required.receiverContact != null) {
				//				if(_this.isNull($("#contact").val())) {
				//					$.toptip("联系方式不能为空");
				//					return;
				//				} else {
				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.receiverContact;
				waybillInfo[keyName[1]] = $("#contact").val();

				_this.describes.push(waybillInfo);
				//				}
			}
			if(_this.required.receiveAddress != null) {
				//				if(_this.isNull($("#receiveAddressChoose").val())) {
				//					$.toptip("收货地址省市区/县不能为空");
				//					return;
				//				} else if(_this.isNull($("#receiveAddress").val())) {
				//					$.toptip("收货地址不能为空");
				//					return;
				//				} else {
				var address = $("#receiveAddressChoose").val().replace(/\ +/g, ",") + " " + $.trim($("#receiveAddress").val());

				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.receiveAddress;
				waybillInfo[keyName[1]] = address;

				_this.describes.push(waybillInfo);
				//				}
			}

			if(_this.required.originStation != null) {
				//				if(_this.isNull($("#startPlaceChoose").val())) {
				//					$.toptip("始发地省市区/县不能为空");
				//					return;
				//				} else if(_this.isNull($.trim($("#startPlace").val()))) {
				//					$.toptip("始发地详细地址不能为空");
				//					return;
				//				} else {
				var address = $("#startPlaceChoose").val().replace(/\ +/g, ",") + " " + $.trim($("#startPlace").val());

				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.originStation;
				waybillInfo[keyName[1]] = address;

				_this.describes.push(waybillInfo);
				//				}
			}
			if(_this.required.arrivalStation != null) {
				//				if(_this.isNull($("#endPlaceChoose").val())) {
				//					$.toptip("目的地省市区/县不能为空");
				//					return;
				//				} else if(_this.isNull($.trim($("#endPlace").val()))) {
				//					$.toptip("目的地详细地址不能为空");
				//					return;
				//				} else {
				var address = $("#endPlaceChoose").val().replace(/\ +/g, ",") + " " + $.trim($("#endPlace").val());

				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.arrivalStation;
				waybillInfo[keyName[1]] = address;

				_this.describes.push(waybillInfo);
				//				}
			}
			if(_this.required.careNo != null) {
				//				if(_this.isNull($.trim($("#plateNumber").val()))) {
				//					$.toptip("车牌号不能为空");
				//					return;
				//				} else {
				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.careNo;
				waybillInfo[keyName[1]] = $.trim($("#plateNumber").val());

				_this.describes.push(waybillInfo);
				//				}
			}
			if(_this.required.driverName != null) {
				//				if(_this.isNull($.trim($("#driverName").val()))) {
				//					$.toptip("司机姓名不能为空");
				//					return;
				//				} else {
				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.driverName;
				waybillInfo[keyName[1]] = $.trim($("#driverName").val());

				_this.describes.push(waybillInfo);
				//				}
			}
			if(_this.required.driverContact != null) {
				//				if(_this.isNull($.trim($("#driverPhone").val()))) {
				//					$.toptip("司机电话不能为空");
				//					return;
				//				} else {
				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.driverContact;
				waybillInfo[keyName[1]] = $.trim($("#driverPhone").val());

				_this.describes.push(waybillInfo);
				//				}
			}
			if(_this.required.startStation != null) {
				//				if(_this.isNull($.trim($("#hairStation").val()))) {
				//					$.toptip("发站不能为空");
				//					return;
				//				} else {
				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.startStation;
				waybillInfo[keyName[1]] = $.trim($("#hairStation").val());

				_this.describes.push(waybillInfo);
				//				}
			}
			if(_this.required.endStation != null) {
				//				if(_this.isNull($.trim($("#arriveStation").val()))) {
				//					$.toptip("到站不能为空");
				//					return;
				//				} else {
				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.endStation;
				waybillInfo[keyName[1]] = $.trim($("#arriveStation").val());

				_this.describes.push(waybillInfo);
				//				}
			}

			if(_this.required.income != null) {
				//				if(_this.isNull($.trim($("#income").val()))) {
				//					$.toptip("收入不能为空");
				//					return;
				//				} else {
				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.income;
				waybillInfo[keyName[1]] = $.trim($("#income").val());

				_this.describes.push(waybillInfo);
				//				}
			}
			if(_this.required.expenditure != null) {
				//				if(_this.isNull($.trim($("#transportCost").val()))) {
				//					$.toptip("运输成本不能为空");
				//					return;
				//				} else {
				var waybillInfo = {};
				waybillInfo[keyName[0]] = _this.required.expenditure;
				waybillInfo[keyName[1]] = $.trim($("#transportCost").val());

				_this.describes.push(waybillInfo);
				//				}
			}

			if(_this.optional != null) {
				for(var i = 0; i < _this.optional.length; i++) {
					//					if(_this.isNull($.trim($("#" + i).val()))) {
					//						$.toptip(_this.optional[i][0] + "不能为空");
					//						return;
					//					} else {
					var waybillInfo = new Object();
					waybillInfo[keyName[0]] = _this.optional[i][1];
					waybillInfo[keyName[1]] = $.trim($("#" + i).val());

					_this.describes.push(waybillInfo);
					//					}
				}
			}

			var invoiceData = {
				templateKey: _this.templateKey,
				describes: _this.describes
			}
			sessionStorage.removeItem('inputDeliveryPlanPageStore');
			sessionStorage.setItem('inputDeliveryPlanPageStore', JSON.stringify(invoiceData));
		},
		loadRecordData: function() {
			var _this = this;

			if(!_this.isNull(_this.isChooseType)) {
				var recordData = sessionStorage.getItem('inputDeliveryPlanPageStore');

				if(recordData) {
					var data = JSON.parse(recordData);

					_this.templateKey = data.templateKey;
					_this.describes = data.describes;

					EasyAjax.ajax_Post_Json({
						url: 'enterprise/template/templateInfo/' + _this.templateKey,
					}, function(res) {
						if(!res.success) {
							$.toast("加载失败，请稍后重试", "text");
						} else {
							$(".chooseModle").hide();
							$(".sendGoods").show();
							$(".btn-next").show();
							console.log(res);
							_this.required = res.required;
							_this.optional = res.optional;

							setTimeout(function() {
								if(_this.required.shipperName != null) {
									if(_this.isChooseType == 1) {
										$("#shipper").val(_common.getUrlParam('name'));
									} else {
										$("#shipper").val(_this.setValue(_this.required.shipperName));
									}
								}
								if(_this.required.deliveryTime != null) {
									$("#deliveryDate").val(_this.setValue(_this.required.deliveryTime));
								}
								if(_this.required.planNo != null) {
									$("#deliveryPlanNumber").val(_this.setValue(_this.required.planNo));
								}
								if(_this.required.orderNo != null) {
									$("#orderNumber").val(_this.setValue(_this.required.orderNo));
								}
								if(_this.required.deliveryNo != null) {
									$("#deliveryNumber").val(_this.setValue(_this.required.deliveryNo));
								}
								if(_this.required.distributeAddress != null) {
									var address = _this.setValue(_this.required.distributeAddress).split(" ");

									if(!_this.isNull(address[0])) {
										$("#deliveryAddressChoose").val(address[0].replace(/\,+/g, " "));
									}
									if(!_this.isNull(address[1])) {
										$("#deliveryAddress").val(address[1]);
									}
								}

								if(_this.required.conveyName != null) {
									if(_this.isChooseType == 3) {
										$("#logistics").val(_common.getUrlParam('name'));
									} else {
										$("#logistics").val(_this.setValue(_this.required.conveyName));
									}
								}
								if(_this.required.conveyerName != null) {
									$("#contacts").val(_this.setValue(_this.required.conveyerName));
								}
								if(_this.required.conveyerContact != null) {
									$("#contactPhone").val(_this.setValue(_this.required.conveyerContact));
								}
								if(_this.required.transportRoute != null) {
									$("#transportRoute").val(_this.setValue(_this.required.transportRoute));
								}
								if(_this.required.collectTime != null) {
									$("#deliveryTime").val(_this.setValue(_this.required.collectTime));
								}
								if(_this.required.arrivalTime != null) {
									$("#arrivalTime").val(_this.setValue(_this.required.arrivalTime));
								}

								var chooseAddressInfo = "";
								if(_this.isChooseType == 2) {
									chooseAddressInfo = JSON.parse(_common.getUrlParam('info'));
								}
								if(_this.required.receiveName != null) {
									if(_this.isChooseType == 2) {
										$("#receiveCustomer").val(chooseAddressInfo.customerName);
									} else {
										$("#receiveCustomer").val(_this.setValue(_this.required.receiveName));
									}
								}
								if(_this.required.receiverName != null) {
									if(_this.isChooseType == 2) {
										$("#receive").val(chooseAddressInfo.contacts);
									} else {
										$("#receive").val(_this.setValue(_this.required.receiverName));
									}
								}
								if(_this.required.receiverContact != null) {
									if(_this.isChooseType == 2) {
										$("#contact").val(chooseAddressInfo.contactNumber);
									} else {
										$("#contact").val(_this.setValue(_this.required.receiverContact));
									}
								}
								if(_this.required.receiveAddress != null) {
									if(_this.isChooseType == 2) {
										if(_this.isNull(chooseAddressInfo.district)) {
											$("#receiveAddressChoose").val(chooseAddressInfo.province.replace("市", "") + " " + chooseAddressInfo.province + " " + chooseAddressInfo.city);
										} else {
											$("#receiveAddressChoose").val(chooseAddressInfo.province + " " + chooseAddressInfo.city + " " + chooseAddressInfo.district);
										}
										$("#receiveAddress").val(chooseAddressInfo.address);
									} else {
										var address = _this.setValue(_this.required.receiveAddress).split(" ");

										if(!_this.isNull(address[0])) {
											$("#receiveAddressChoose").val(address[0].replace(/\,+/g, " "));
										}
										if(!_this.isNull(address[1])) {
											$("#receiveAddress").val(address[1]);
										}
									}
								}

								if(_this.required.originStation != null) {
									var address = _this.setValue(_this.required.originStation).split(" ");

									if(!_this.isNull(address[0])) {
										$("#startPlaceChoose").val(address[0].replace(/\,+/g, " "));
									}
									if(!_this.isNull(address[1])) {
										$("#startPlace").val(address[1]);
									}
								}
								if(_this.required.arrivalStation != null) {
									var address = _this.setValue(_this.required.arrivalStation).split(" ");

									if(!_this.isNull(address[0])) {
										$("#endPlaceChoose").val(address[0].replace(/\,+/g, " "));
									}
									if(!_this.isNull(address[1])) {
										$("#endPlace").val(address[1]);
									}
								}
								if(_this.required.careNo != null) {
									$("#plateNumber").val(_this.setValue(_this.required.careNo));
								}
								if(_this.required.driverName != null) {
									$("#driverName").val(_this.setValue(_this.required.driverName));
								}
								if(_this.required.driverContact != null) {
									$("#driverPhone").val(_this.setValue(_this.required.driverContact));
								}
								if(_this.required.startStation != null) {
									$("#hairStation").val(_this.setValue(_this.required.startStation));
								}
								if(_this.required.endStation != null) {
									$("#arriveStation").val(_this.setValue(_this.required.endStation));
								}

								if(_this.required.income != null) {
									$("#income").val(_this.setValue(_this.required.income));
								}
								if(_this.required.expenditure != null) {
									$("#transportCost").val(_this.setValue(_this.required.expenditure));
								}

								if(_this.optional != null) {
									for(var i = 0; i < _this.optional.length; i++) {
										$("#" + i).val(_this.setValue(_this.optional[i][0]));
									}
								}
								_this.initWidget();
							}, 200);
						}
					});
				}
			}
		},
		setValue: function(filedKey) {
			var _this = this;

			var result = '';

			for(var i = 0; i < _this.describes.length; i++) {
				if(_this.describes[i].detailKey == filedKey) {
					if(!_this.isNull(_this.describes[i].dataValue)) {
						result = _this.describes[i].dataValue;
						break;
					}
					break;
				}
			}
			return result;
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		}
	}
});

$('#modle').change(function() {
	inputPlan.templateKey = $(this).val();
	if(inputPlan.isNull(inputPlan.templateKey)) {
		$.toptip("请选择发货计划模板");
		return;
	}
	console.log(inputPlan.templateKey);
	EasyAjax.ajax_Post_Json({
		url: 'enterprise/template/templateInfo/' + $(this).val(),
	}, function(res) {
		if(!res.success) {
			$.toast("加载失败，请稍后重试", "text");
		} else {
			$(".chooseModle").hide();
			$(".sendGoods").show();
			$(".btn-next").show();
			console.log(res);
			inputPlan.required = res.required;
			inputPlan.optional = res.optional;

			setTimeout(function() {
				inputPlan.initWidget();
			}, 200);
		}
	});
});

//初始化日期插件
var currYear = (new Date()).getFullYear();
var opt = {};
opt.date = {
	preset: 'date'
};
opt.datetime = {
	preset: 'datetime'
};
opt.time = {
	preset: 'time'
};
opt.default = {
	theme: 'android-ics light', //皮肤样式
	display: 'modal', //显示方式 
	mode: 'scroller', //日期选择模式
	dateFormat: 'yy-mm-dd',
	lang: 'zh',
	showNow: true,
	nowText: "今天",
	startYear: currYear - 10, //开始年份
	endYear: currYear + 10 //结束年份
};