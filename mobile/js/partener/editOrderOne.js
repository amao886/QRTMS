var editOrder = new Vue({
	el: '#editOrder',
	data: {
		areaVersion: 0,
		areaData: [],
		customDatas: [],
		commodities: [],
		order: {},
		extra: {},
		distributeAddress: [],
		distributeArea: "",
		receiveAddress: [],
		receiveArea: "",
		originStation: [],
		originStationArea: "",
		arrivalStation: [],
		arrivalStationArea: "",
		orderKey: _common.getUrlParam('id'),
		type: _common.getUrlParam('type'),
		fromHomePage: _common.getUrlParam("fromHomePage") //true:首页列表进入  false:列表进入
	},
	created: function() {
		var _this = this;

		if(window.sessionStorage) {
			_this.areaVersion = sessionStorage.getItem('areaVersion');
		}
		_this.getAreasData(_this.areaVersion);
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

					if(window.sessionStorage) {
						var recordData = sessionStorage.getItem('orderPageStore');

						if(recordData) {
							_this.loadRecordData(recordData);
						} else {
							_this.getModelInfo();
						}
					} else {
						_this.getModelInfo();
					}
				}
			});
		},
		getModelInfo: function() {
			var _this = this;

			var baseValue = {
				orderKey: _this.orderKey
			}
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/order/convert/order',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					console.log(res);

					_this.orderKey = res.orderKey;
					var results = res.results;
					if(results) {
						_this.order = results.order;
						_this.commodities = results.commodities;
						_this.customDatas = results.customDatas;
						if(res.results.extra != null) {
							_this.extra = res.results.extra;
						}

						_this.receiveAddress = _this.order.receiveAddress;
						if(!_this.isNull(_this.receiveAddress[0])) {
							_this.receiveArea = _this.receiveAddress[0] + " " + _this.receiveAddress[1] + " " + _this.receiveAddress[2];
						}

						if(_this.extra != null) {
							if(_this.extra.distributeAddress != null) {
								_this.distributeAddress = _this.extra.distributeAddress;
								if(!_this.isNull(_this.distributeAddress[0])) {
									_this.distributeArea = _this.distributeAddress[0] + " " + _this.distributeAddress[1] + " " + _this.distributeAddress[2];
								}
							}

							if(_this.extra.originStation != null) {
								_this.originStation = _this.extra.originStation;
								if(!_this.isNull(_this.originStation[0])) {
									_this.originStationArea = _this.originStation[0] + " " + _this.originStation[1] + " " + _this.originStation[2];
								}
							}

							if(_this.extra.arrivalStation != null) {
								_this.arrivalStation = _this.extra.arrivalStation;
								if(!_this.isNull(_this.arrivalStation[0])) {
									_this.arrivalStationArea = _this.arrivalStation[0] + " " + _this.arrivalStation[1] + " " + _this.arrivalStation[2];
								}
							}
						}

						setTimeout(function() {
							_this.initWidget();
						}, 200);
					}
				}
			});
		},
		initWidget: function() {
			var _this = this;

			$("#deliveryDate").mobiscroll($.extend(opt['date'], opt['default']));
			//			$("#deliveryTime").mobiscroll($.extend(opt['datetime'], opt['default']));
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
		next: function() {
			var _this = this;

			if(_this.isNull($("#shipper").val())) {
				$.toptip("发货方不能为空");
				return;
			}
			if(_this.isNull($.trim($("#receiveCustomer").val()))) {
				$.toptip("收货客户不能为空");
				return;
			}
			if(_this.isNull($.trim($("#receive").val()))) {
				$.toptip("收货人不能为空");
				return;
			}
			if(_this.isNull($.trim($("#contact").val()))) {
				$.toptip("联系方式不能为空");
				return;
			}
			if(_this.isNull($("#receiveAddressChoose").val())) {
				$.toptip("收货地址省市区/县不能为空");
				return;
			} else if(_this.isNull($.trim($("#receiveAddress").val()))) {
				$.toptip("收货地址不能为空");
				return;
			}

			_this.saveData();

			window.location.href = _common.version('./editOrderTwo.html?id=' + _this.orderKey + '&type=' + _this.type + '&fromHomePage=' + _this.fromHomePage);
		},
		saveData: function() {
			var _this = this;

			_this.order.shipperName = $("#shipper").val();
			_this.order.deliveryTime = $("#deliveryDate").val();
			//			_this.order.planNo = $("#deliveryPlanNumber").val();.replace(/\s*/g, "")
			_this.order.orderNo = $("#orderNumber").val();
			_this.order.deliveryNo = $.trim($("#deliveryNumber").val());
			var deliveryAddress = $("#deliveryAddressChoose").val().replace(/\ +/g, ",") + " " + $.trim($("#deliveryAddress").val());
			_this.extra.distributeAddress = deliveryAddress;

			_this.order.conveyName = $("#logistics").val();
			_this.extra.conveyerName = $.trim($("#contacts").val());
			_this.extra.conveyerContact = $.trim($("#contactPhone").val());
			//			_this.extra.conveyerContact = $.trim($("#transportRoute").val());
			//			_this.extra.conveyerContact = $.trim($("#deliveryTime").val());
			_this.order.arrivalTime = $("#arrivalTime").val();

			_this.order.receiveName = $.trim($("#receiveCustomer").val());
			_this.order.receiverName = $.trim($("#receive").val());
			_this.order.receiverContact = $.trim($("#contact").val());
			var receiveAddress = $("#receiveAddressChoose").val().replace(/\ +/g, ",") + " " + $.trim($("#receiveAddress").val());
			_this.order.receiveAddress = receiveAddress;

			var startPlaceAddress = $("#startPlaceChoose").val().replace(/\ +/g, ",") + " " + $.trim($("#startPlace").val());
			_this.extra.originStation = startPlaceAddress;
			var endPlaceAddress = $("#endPlaceChoose").val().replace(/\ +/g, ",") + " " + $.trim($("#endPlace").val());
			_this.extra.arrivalStation = endPlaceAddress;
			_this.extra.careNo = $.trim($("#plateNumber").val());
			_this.extra.driverName = $.trim($("#driverName").val());
			_this.extra.driverContact = $.trim($("#driverPhone").val());
			_this.extra.startStation = $.trim($("#hairStation").val());
			_this.extra.endStation = $.trim($("#arriveStation").val());

			_this.extra.income = $.trim($("#income").val());
			_this.extra.expenditure = $.trim($("#transportCost").val());

			_this.order.remark = $.trim($("#invoiceRemark").val());
			var customDatas = null;
			if(_this.customDatas != null) {
				customDatas = new Array();
				for(var i = 0; i < _this.customDatas.length; i++) {
					var customData = new Object();
					customData["customName"] = _this.customDatas[i][0];
					customData["customValue"] = $.trim($("#" + i).val());
					customDatas.push(customData);
				}
			}

			var invoiceData = {
				order: _this.order,
				extra: _this.extra,
				commodities: _this.commodities,
				customDatas: customDatas
			}
			sessionStorage.removeItem('orderPageStore');
			sessionStorage.setItem('orderPageStore', JSON.stringify(invoiceData));
		},
		loadRecordData: function(recordData) {
			var _this = this;

			var data = JSON.parse(recordData);

			_this.order = data.order;
			_this.extra = data.extra;
			_this.commodities = data.commodities;
			var customDatas = data.customDatas;

			setTimeout(function() {
				if(_this.extra != null) {
					var address = _this.extra.distributeAddress.split(" ");

					var area = null;
					_this.distributeAddress.splice(0, _this.distributeAddress.length);
					_this.distributeAddress = ['', '', '', ''];
					if(!_this.isNull(address[0])) {
						area = address[0].split(",");
						if(area.length < 3) {
							_this.distributeAddress[0] = area[0].replace("市", "");
							_this.distributeAddress[1] = area[0];
							_this.distributeAddress[2] = area[1];
						} else {
							_this.distributeAddress[0] = area[0];
							_this.distributeAddress[1] = area[1];
							_this.distributeAddress[2] = area[2];
						}

						_this.distributeArea = address[0].replace(/\,+/g, " ");
					}
					if(!_this.isNull(address[1])) {
						_this.distributeAddress[3] = address[1];
					}
				}

				if(_this.order.receiveAddress != null) {
					var address = _this.order.receiveAddress.split(" ");

					var area = null;
					_this.receiveAddress.splice(0, _this.receiveAddress.length);
					_this.receiveAddress = ['', '', '', ''];
					if(!_this.isNull(address[0])) {
						area = address[0].split(",");
						if(area.length < 3) {
							_this.receiveAddress[0] = area[0].replace("市", "");
							_this.receiveAddress[1] = area[0];
							_this.receiveAddress[2] = area[1];
						} else {
							_this.receiveAddress[0] = area[0];
							_this.receiveAddress[1] = area[1];
							_this.receiveAddress[2] = area[2];
						}

						_this.receiveArea = address[0].replace(/\,+/g, " ");
					}
					if(!_this.isNull(address[1])) {
						_this.receiveAddress[3] = address[1];
					}
				}

				if(_this.extra != null && _this.extra.originStation != null) {
					var address = _this.extra.originStation.split(" ");

					var area = null;
					_this.originStation.splice(0, _this.originStation.length);
					_this.originStation = ['', '', '', ''];
					if(!_this.isNull(address[0])) {
						area = address[0].split(",");
						if(area.length < 3) {
							_this.originStation[0] = area[0].replace("市", "");
							_this.originStation[1] = area[0];
							_this.originStation[2] = area[1];
						} else {
							_this.originStation[0] = area[0];
							_this.originStation[1] = area[1];
							_this.originStation[2] = area[2];
						}

						_this.originStationArea = address[0].replace(/\,+/g, " ");
					}
					if(!_this.isNull(address[1])) {
						_this.originStation[3] = address[1];
					}
				}
				if(_this.extra != null && _this.extra.arrivalStation != null) {
					var address = _this.extra.arrivalStation.split(" ");

					var area = null;
					_this.arrivalStation.splice(0, _this.arrivalStation.length);
					_this.arrivalStation = ['', '', '', ''];
					if(!_this.isNull(address[0])) {
						area = address[0].split(",");
						if(area.length < 3) {
							_this.arrivalStation[0] = area[0].replace("市", "");
							_this.arrivalStation[1] = area[0];
							_this.arrivalStation[2] = area[1];
						} else {
							_this.arrivalStation[0] = area[0];
							_this.arrivalStation[1] = area[1];
							_this.arrivalStation[2] = area[2];
						}

						_this.arrivalStationArea = address[0].replace(/\,+/g, " ");
					}
					if(!_this.isNull(address[1])) {
						_this.arrivalStation[3] = address[1];
					}
				}

				if(customDatas != null) {
					_this.customDatas.splice(0, _this.customDatas.length);

					for(var i = 0; i < customDatas.length; i++) {
						var customData = new Array();
						customData.push(customDatas[i].customName);
						customData.push(customDatas[i].customValue);
						_this.customDatas.push(customData);
					}
				}
				_this.initWidget();
			}, 200);
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		}
	}
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