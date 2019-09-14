var deliverGoods = new Vue({
	el: '#deliverGoods',
	data: {
		templates: [],
		required: [],
		optional: [],
		address: [],
		customers: [],
		templateKey: -1,
		isShowModleData: false,
		provinceCount: 0,
		provinceName: '',
		provinceId: 0,
		cityCount: 0,
		cityName: '',
		cityId: 0,
		cityArrayLength: 0,
		cityArray: null,
		areaVersion: 0,
		areaData: []
	},
	created: function() {
		var _this = this;

		this.getModelListData();
	},
	updated: function() {
		var _this = this;
		if(_this.isShowModleData) {
			if(_this.required.receiveName != null) {
				$('#receiveCustomer').editableSelect({
					effects: 'slide',
					onSelect: function(object) {
						console.log(object[0].id);
						EasyAjax.ajax_Post_Json({
							url: 'enterprise/customer/address/last/' + object[0].id
						}, function(res) {
							if(!res.success) {
								$.toast("加载失败，请稍后重试", "text");
							} else {
								console.log(res);
								_this.address = res.address;

								if(!_this.isNull(_this.address)) {
									$("#receive").val(_this.address.contacts);
									$("#contact").val(_this.address.contactNumber);
									$("#receiveAddress").val(_this.address.fullAddress);
								} else {
									$("#receive").val("");
									$("#contact").val("");
									$("#receiveAddress").val("");
								}
							}
						});
					}
				});
				$('#logistics').change(function() {
					console.log($(this).find("option:checked").attr("id"));
					EasyAjax.ajax_Post_Json({
						url: 'enterprise/customer/address/last/' + $(this).find("option:checked").attr("id")
					}, function(res) {
						if(!res.success) {
							$.toast("加载失败，请稍后重试", "text");
						} else {
							console.log(res);
							_this.address = res.address;

							if(!_this.isNull(_this.address)) {
								$("#contacts").val(_this.address.contacts);
								$("#contactPhone").val(_this.address.contactNumber);
							} else {
								$("#contacts").val("");
								$("#contactPhone").val("");
							}
						}
					});
				});
			}
			if(_this.required.deliveryTime != null) {
				$("#deliveryDate").mobiscroll($.extend(opt['date'], opt['default']));
			}
		}
	},
	computed: {
		typeArr: {
			get: function() {
				var typeArr = [[], [], []];
				var typeObj = this.customers.reduce(function(prev, current) {
					if(typeof prev[current.type] === 'undefined') {
						prev[current.type] = [current];
					} else {
						prev[current.type].push(current);
					}
					return prev;
				}, {});
				Object.keys(typeObj).forEach(function(k) {
					typeArr[k - 1] = typeObj[k];
				});
				return typeArr;
			}
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
		initAreaWidget: function() {
			var _this = this;

			if(_this.required.originStation != null) {
				$("#startPlaceChoose").cityPicker({
					title: "请选择始发地省市区/县",
				});
			}
			if(_this.required.arrivalStation != null) {
				$("#endPlaceChoose").cityPicker({
					title: "请选择目的地省市区/县"
				});
			}
			if(_this.required.distributeAddress != null) {
				$("#deliveryAddressChoose").cityPicker({
					title: "请选择发货地址省市区/县"
				});
			}
		},
		processData: function(name, id, districtListData) {
			var obj = new Object();
			var ary = new Array();

			for(var i = 0; i < districtListData.length; i++) {
				var districtObj = new Object();
				districtObj["name"] = districtListData[i].name;
				districtObj["code"] = districtListData[i].id;
				ary.push(districtObj);
			}
			obj["name"] = name;
			obj["code"] = id;
			obj["sub"] = ary;

			return obj;
		},
		getModelListData: function() {
			var _this = this;
			var baseValue = {
				category: 1 //1：发货模板  2：计划模板
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

					_this.getCustomerNameListData();
				}
			});
		},
		getCustomerNameListData: function() {
			var _this = this;
			var baseValue = {
				likeName: ""
			}
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/customer/list',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					console.log(res);
					_this.customers = res.customers;

					if(window.sessionStorage) {
						_this.areaVersion = sessionStorage.getItem('areaVersion');
						if(_this.areaVersion == null) {
							_this.areaVersion = 0;
						}
						_this.getAreasData(_this.areaVersion);
					} else {
						_this.getAreasData(_this.areaVersion);
					}
				}
			});
		},
		next: function() {
			var _this = this;
			var waybillInfo = {};

			if(_this.required.shipperName != null) {
//				if(_this.isNull($("#shipper").val())) {
//					$.toptip("发货方不能为空");
//					return;
//				} else {
					waybillInfo[_this.required.shipperName] = $("#shipper").val();
//				}
			}
			if(_this.required.deliveryTime != null) {
				//				if(_this.isNull($("#deliveryDate").val())) {
				//					$.toptip("发货日期不能为空");
				//					return;
				//				} else {
				waybillInfo[_this.required.deliveryTime] = $("#deliveryDate").val();
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
					waybillInfo[_this.required.deliveryNo] = $.trim($("#deliveryNumber").val());
//				}
			}
			if(_this.required.orderNo != null) {
				//				if(_this.isNull($.trim($("#orderNumber").val()))) {
				//					$.toptip("订单编号不能为空");
				//					return;
				//				} else {
				waybillInfo[_this.required.orderNo] = $.trim($("#orderNumber").val());
				//				}
			}
			if(_this.required.receiveName != null) {
//				if(_this.isNull($.trim($("#receiveCustomer").val()))) {
//					$.toptip("收货客户不能为空");
//					return;
//				} else {
					waybillInfo[_this.required.receiveName] = $.trim($("#receiveCustomer").val());
//				}
			}
			if(_this.required.receiverName != null) {
//				if(_this.isNull($.trim($("#receive").val()))) {
//					$.toptip("收货人不能为空");
//					return;
//				} else {
					waybillInfo[_this.required.receiverName] = $.trim($("#receive").val());
//				}
			}
			if(_this.required.receiverContact != null) {
				//				if(_this.isNull($.trim($("#contact").val()))) {
				//					$.toptip("联系方式不能为空");
				//					return;
				//				} else {
				waybillInfo[_this.required.receiverContact] = $.trim($("#contact").val());
				//				}
			}
			if(_this.required.receiveAddress != null) {
//				if(_this.isNull($.trim($("#receiveAddress").val()))) {
//					$.toptip("收货地址不能为空");
//					return;
//				} else {
					waybillInfo[_this.required.receiveAddress] = $.trim($("#receiveAddress").val());
//				}
			}
			if(_this.required.conveyName != null) {
//				if(_this.isNull($("#logistics").val())) {
//					$.toptip("物流商不能为空");
//					return;
//				} else {
					waybillInfo[_this.required.conveyName] = $("#logistics").val();
//				}
			}
			if(_this.required.conveyerName != null) {
				//				if(_this.isNull($.trim($("#contacts").val()))) {
				//					$.toptip("联系人不能为空");
				//					return;
				//				} else {
				waybillInfo[_this.required.conveyerName] = $.trim($("#contacts").val());
				//				}
			}
			if(_this.required.conveyerContact != null) {
				//				if(_this.isNull($.trim($("#contactPhone").val()))) {
				//					$.toptip("联系电话不能为空");
				//					return;
				//				} else {
				waybillInfo[_this.required.conveyerContact] = $.trim($("#contactPhone").val());
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
				var address = $("#startPlaceChoose").val().replace(/\s*/g, "") + $.trim($("#startPlace").val());
				waybillInfo[_this.required.originStation] = address;
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
				var address = $("#endPlaceChoose").val().replace(/\s*/g, "") + $.trim($("#endPlace").val());
				waybillInfo[_this.required.arrivalStation] = address;
				//				}
			}
			if(_this.required.careNo != null) {
				//				if(_this.isNull($.trim($("#plateNumber").val()))) {
				//					$.toptip("车牌号不能为空");
				//					return;
				//				} else {
				waybillInfo[_this.required.careNo] = $.trim($("#plateNumber").val());
				//				}
			}
			if(_this.required.driverName != null) {
				//				if(_this.isNull($.trim($("#driverName").val()))) {
				//					$.toptip("司机姓名不能为空");
				//					return;
				//				} else {
				waybillInfo[_this.required.driverName] = $.trim($("#driverName").val());
				//				}
			}
			if(_this.required.driverContact != null) {
				//				if(_this.isNull($.trim($("#driverPhone").val()))) {
				//					$.toptip("司机电话不能为空");
				//					return;
				//				} else {
				waybillInfo[_this.required.driverContact] = $.trim($("#driverPhone").val());
				//				}
			}
			if(_this.required.distributeAddress != null) {
				//				if(_this.isNull($("#deliveryAddressChoose").val())) {
				//					$.toptip("发货地址省市区/县不能为空");
				//					return;
				//				} else if(_this.isNull($.trim($("#deliveryAddress").val()))) {
				//					$.toptip("发货地址详细地址不能为空");
				//					return;
				//				} else {
				var address = $("#deliveryAddressChoose").val().replace(/\s*/g, "") + $.trim($("#deliveryAddress").val());
				waybillInfo[_this.required.distributeAddress] = address;
				//				}
			}
			if(_this.required.income != null) {
				//				if(_this.isNull($.trim($("#income").val()))) {
				//					$.toptip("收入不能为空");
				//					return;
				//				} else {
				waybillInfo[_this.required.income] = $.trim($("#income").val());
				//				}
			}
			if(_this.required.expenditure != null) {
				//				if(_this.isNull($.trim($("#transportCost").val()))) {
				//					$.toptip("运输成本不能为空");
				//					return;
				//				} else {
				waybillInfo[_this.required.expenditure] = $.trim($("#transportCost").val());
				//				}
			}
			if(_this.required.startStation != null) {
				//				if(_this.isNull($("#hairStation").val())) {
				//					$.toptip("发站省市区/县不能为空");
				//					return;
				//				} else {
				waybillInfo[_this.required.startStation] = $("#hairStation").val().replace(/\s*/g, "");
				//				}
			}
			if(_this.required.endStation != null) {
				//				if(_this.isNull($("#arriveStation").val())) {
				//					$.toptip("到站省市区/县不能为空");
				//					return;
				//				} else {
				waybillInfo[_this.required.endStation] = $("#arriveStation").val().replace(/\s*/g, "");
				//				}
			}
			if(_this.optional != null) {
				for(var i = 0; i < _this.optional.length; i++) {
					//					if(_this.isNull($.trim($("#" + i).val()))) {
					//						$.toptip(_this.optional[i][0] + "不能为空");
					//						return;
					//					} else {
					waybillInfo[_this.optional[i][1]] = $.trim($("#" + i).val());
					//					}
				}
			}

			var sendGoodsData = {
				data: waybillInfo
			}
			sessionStorage.setItem('sendGoodsPageStore', JSON.stringify(sendGoodsData));
			window.location.href = _common.version('./waybillEntryTwo.html?modelNo=' + _this.templateKey);

		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		}
	}
});

if(!isWeixin()) {
	//浏览器，将手动录入隐藏
	//	$('.sendTab').hide();
	//	$('.sendImport').addClass('current').siblings().removeClass('current');
	//	$('.import-title').show();
	$('#navBarIn').hide(); //底部导航隐藏

	//上传文件
	$('#inputFile')[0].addEventListener('change', function() {
		var fileName = this.files[0].name;
		//alert('文件名:'+fileName);
		var fileType = fileName.substring(fileName.lastIndexOf('.') + 1);
		if((fileType != 'xls') && (fileType != 'xlsx')) {
			this.value = '';
			$('.upload_txt').html('请选择上传文件');
			$.toast('请上传正确的文件格式', 'text');
		} else {
			$('.upload_txt').html(fileName);
		}
	}, false);

	//批量导入提交数据
	$('#importUpdate').on('click', function() {
		console.log('提交按钮');
		var templateKey = $('#importModel').val();
		var fileVal = $('#inputFile').val();
		if(!fileVal) {
			$.toast('文件必填', 'text');
			return;
		}
		saveImportData();
	});
} else {
	//微信页
	$('#inputFile,#importModel').attr('disabled', 'disabled');

	//点击提示
	$('#inputFile,#importUpdate,#importModel').on('click', function(event) {
		event.stopPropagation();
		layer.open({
			type: 1,
			title: false,
			closeBtn: 0,
			area: '80%',
			skin: 'bgWhite',
			shadeClose: false,
			content: $('.showTips')
		});
	});

}

//tab切换
$('.sendHeader').on('click', function() {
	$(this).addClass('active').siblings().removeClass('active');
	$('.sendItem').eq($(this).index()).addClass('current').siblings().removeClass('current');
	if($('.sendImport').hasClass('current')) {
		layer.open({
			type: 1,
			title: false,
			closeBtn: 0,
			area: '80%',
			skin: 'bgWhite',
			shadeClose: true,
			content: $('.showTips')
		});
	}
})

$('#modle').change(function() {
	deliverGoods.templateKey = $(this).val();
	if(deliverGoods.isNull(deliverGoods.templateKey)) {
		$.toptip("请选择发货模板");
		return;
	}
	console.log(deliverGoods.templateKey);
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
			deliverGoods.required = res.required;
			deliverGoods.optional = res.optional;
			deliverGoods.isShowModleData = true;

			setTimeout(function() {
				deliverGoods.initAreaWidget();
			}, 200);
		}
	});
});

//保存批量导入接口
function saveImportData() {
	loadImg();
	var formData = new FormData();
	formData.append("templateKey", $('#importModel').val());
	formData.append('file', $('#inputFile')[0].files[0]);
	console.log(11);
	console.log($('#inputFile')[0].files[0]);

	$.ajax({
		url: api_host + 'enterprise/order/import/excel',
		type: 'POST',
		headers: {
			"token": api_token
		},
		processData: false,
		contentType: false,
		data: formData,
		success: function(res) {
			removeLoad();
			if(res.success) {
				//导入成功
				$.alert(res.message, '', function() {
					$('.import-title,.sendgood-wrapper').hide();
					$('.successTips').show();
				});
			} else {
				//导入失败
				$.modal({
					title: res.message,
					text: "",
					buttons: [{
							text: "重新导入",
							onClick: function() {
								$('#inputFile').val('');
								$('.upload_txt').html('请选择上传文件');
							}
						},
						{
							text: "下载查看错误数据",
							onClick: function() {
								$('<form action="' + res.url + '" method="post"></form>').appendTo('body').submit().remove();
							}
						}
					]
				});
			}

		},
		error: function() {

		}
	});
}

//判断是否是在微信页
function isWeixin() {
	var ua = navigator.userAgent.toLowerCase();
	var isWeixin = ua.indexOf('micromessenger') != -1;
	if(isWeixin) {
		//alert('微信页');
		return true;
	} else {
		//alert('浏览器');
		return false;
	}
}

//关闭浏览器
function closeWindows() {
	var userAgent = navigator.userAgent;
	if(userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Chrome") != -1) {
		close(); //直接调用JQUERY close方法关闭
	} else {
		window.opener = null;
		window.open("", "_self");
		window.close();
	}
};

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
	dateFormat: 'yyyy-mm-dd',
	lang: 'zh',
	showNow: true,
	nowText: "今天",
	startYear: currYear - 10, //开始年份
	endYear: currYear + 10 //结束年份
};