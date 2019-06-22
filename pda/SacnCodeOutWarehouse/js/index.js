var codeList = new Vue({
	el: '#getScanCode',
	data: {
		deliverCustomer: '',
		deliverCustomerId: '',
		receiveCustomer: '',
		receiveCustomerId: '',
		deliverNo: '',
		sendGoodDate: '',
		defaultQuantity: 80,
		deleteArrayIndex: -2, //默认：-2，重置：-1，删除：数组index
		goodDetailList: [],
		goodDetailSummaryList: [],
		pageIndex: 0,
		scroll0: null,
		scroll1: null
	},
	created: function() {
		var _this = this;

		_this.getData();

		mui.init();
		mui.plusReady(function() {
			setInterval(_this.getScanCode, 200);
		});

		setTimeout(function() {
			$(".mui-control-content").height($(window).height() - $('#header').height() - $('#sliderSegmentedControl').height());

			mui('.mui-scroll-wrapper').scroll({
				deceleration: 0.0005
			});

			_this.scroll0 = mui('#scroll1').scroll();
			_this.scroll1 = mui('#scroll2').scroll();

			document.getElementById('slider').addEventListener('slide', function(e) {
				_this.pageIndex = e.detail.slideNumber;

				if(_this.pageIndex === 0) {
					//扫码出库
					_this.scroll0.reLayout();
					_this.scroll0.setTranslate(0, 0);
				} else if(_this.pageIndex === 1) {
					//查看汇总
					_this.scroll1.reLayout();
					_this.scroll1.setTranslate(0, 0);
				}
			});

			var data = localStorage.getItem("data");
			if(!data) {
				var recordData = localStorage.getItem("recordData");
				if(recordData) {
					_this.defaultQuantity = recordData;
				}
			}
		}, 200);
	},
	methods: {
		chooseDate: function() {
			var _this = this;

			var oDate = new Date(); //实例一个时间对象；
			var year = oDate.getFullYear(); //获取系统的年；
			var month = oDate.getMonth() + 1; //获取系统月份，由于月份是从0开始计算，所以要加1
			var day = oDate.getDate(); // 获取系统日

			var options = {
				type: "date",
				beginDate: new Date(year, month - 1, day),
				endYear: year + 10
			}
			var picker = new mui.DtPicker(options);
			picker.show(function(rs) {
				/*
				 * rs.value 拼合后的 value
				 * rs.text 拼合后的 text
				 * rs.y 年，可以通过 rs.y.vaue 和 rs.y.text 获取值和文本
				 * rs.m 月，用法同年
				 * rs.d 日，用法同年
				 * rs.h 时，用法同年
				 * rs.i 分（minutes 的第二个字母），用法同年
				 */
				_this.sendGoodDate = rs.text;
				/* 
				 * 返回 false 可以阻止选择框的关闭
				 * return false;
				 */
				/*
				 * 释放组件资源，释放后将将不能再操作组件
				 * 通常情况下，不需要示放组件，new DtPicker(options) 后，可以一直使用。
				 * 当前示例，因为内容较多，如不进行资原释放，在某些设备上会较慢。
				 * 所以每次用完便立即调用 dispose 进行释放，下次用时再创建新实例。
				 */
				picker.dispose();
			});
		},
		getScanCode: function() {
			var _this = this;

			var code = plus.scancode.getScanCode(_this.deleteArrayIndex);
			if(code != '') {
				_this.getGoodDetail(code);
			} else {
				_this.deleteArrayIndex = -2;
			}
		},
		getGoodDetail: function(batchNumber) {
			var _this = this;

			MuiAjax.ajax_Post_Json({
				url: 'enterprise/depot/batch/number/' + batchNumber,
			}, function(res) {
				console.log(res.order);

				_this.processGoodDetailData(res.order);
			});
		},
		submit: function() {
			var _this = this;

			if(_this.isNull(_this.deliverCustomerId)) {
				mui.toast('发货客户不能为空');
				return;
			}
			if(_this.isNull(_this.receiveCustomerId)) {
				mui.toast('收货客户不能为空');
				return;
			}
			if(_this.isNull(_this.deliverNo)) {
				mui.toast('送货单号不能为空');
				return;
			}
			if(_this.isNull(_this.sendGoodDate)) {
				mui.toast('发货日期不能为空');
				return;
			}
			if(_this.goodDetailList == null || _this.goodDetailList.length <= 0) {
				mui.toast('出库货物不能为空');
				return;
			}

			mui(".mui-btn").button('loading');

			var baseValue = {
				shipperId: _this.deliverCustomerId,
				shipperName: _this.deliverCustomer,
				receiveId: _this.receiveCustomerId,
				receiveName: _this.receiveCustomer,
				deliveryNo: _this.deliverNo,
				deliveryTime: _this.sendGoodDate,
				details: JSON.stringify(_this.goodDetailList)
			}
			MuiAjax.ajax_Post_Json({
				url: 'enterprise/depot/takeout',
				data: JSON.stringify(baseValue)
			}, function(res) {
				mui.toast(res.message);

				mui(".mui-btn").button('reset');

				localStorage.setItem("recordData", _this.defaultQuantity);

				localStorage.removeItem("data");

				_this.deleteArrayIndex = -1;

				setTimeout(function() {
					window.location.replace('./index.html');
				}, 2000);
			}, function(res, type, errorThrown) {
				mui.toast(res.message);

				mui(".mui-btn").button('reset');
			});
		},
		processGoodDetailData: function(order) {
			var _this = this;

			if(order == null) {
				return;
			}

			var obj = new Object();
			obj["batchNumber"] = order.batchNumber;
			obj["materialName"] = order.materialName;
			obj["outboundQuantity"] = _this.defaultQuantity;

			_this.goodDetailList.push(obj);

			_this.computerGoodDetailSummary();

			setTimeout(function() {
				if(_this.pageIndex === 0) {
					//扫码出库
					_this.scroll0.reLayout();
					_this.scroll0.scrollToBottom(100);
				}
//				else if(_this.pageIndex === 1) {
//					//查看汇总
//					_this.scroll1.reLayout();
//					_this.scroll1.scrollToBottom(100);
//				}
			}, 200);
		},
		computerGoodDetailSummary: function() {
			var _this = this;

			_this.goodDetailSummaryList.splice(0, _this.goodDetailSummaryList.length);
			if(_this.goodDetailList.length > 0) {
				var typeObj = _this.goodDetailList.reduce(function(prev, current) {
					if(typeof prev[current.materialName] === 'undefined') {
						prev[current.materialName] = [current];
					} else {
						prev[current.materialName].push(current);
					}
					return prev;
				}, {});

				Object.keys(typeObj).forEach(function(i) {
					var obj = new Object();
					var count = 0;

					typeObj[i].forEach(function(j) {
						count += parseInt(j.outboundQuantity);
					});

					obj["materialName"] = i;
					obj["outboundQuantity"] = count;

					_this.goodDetailSummaryList.push(obj);
				});
			}
		},
		deleteGood: function(index) {
			var _this = this;

			var btnArray = ['取消', '确定'];
			mui.confirm('确定要删除这个批次的货物吗？', '', btnArray, function(e) {
				if(e.index == 1) {
					_this.deleteArrayIndex = index;

					_this.goodDetailList.splice(index, 1);

					_this.computerGoodDetailSummary();

					if(_this.goodDetailList.length == 0) {
						_this.deleteArrayIndex = -1;
					}
				}
			})
		},
		reset: function() {
			var _this = this;

			var btnArray = ['取消', '确定'];
			mui.confirm('确定要清空已扫描出库的货物吗？', '', btnArray, function(e) {
				if(e.index == 1) {
					_this.deleteArrayIndex = -1;
					_this.goodDetailList.splice(0, _this.goodDetailList.length);
					_this.goodDetailSummaryList.splice(0, _this.goodDetailSummaryList.length);
				}
			})
		},
		getData: function() {
			var _this = this;

			var type = _common.getUrlParam("type");
			var customerObj = JSON.parse(_common.getUrlParam("chooseInfo"));

			var oDate = new Date(); //实例一个时间对象；
			var year = oDate.getFullYear(); //获取系统的年；
			var month = oDate.getMonth() + 1; //获取系统月份，由于月份是从0开始计算，所以要加1
			var day = oDate.getDate(); // 获取系统日
			var defaultDateString = year + "-" + (month > 9 ? month : "0" + month) + "-" + day;

			if(type) {
				if(type == 1) {
					_this.deliverCustomer = customerObj.name;
					_this.deliverCustomerId = customerObj.key;
				} else if(type == 2) {
					_this.receiveCustomer = customerObj.name;
					_this.receiveCustomerId = customerObj.key;
				}

				var data = localStorage.getItem("data");
				if(data) {
					var dataObj = JSON.parse(data);

					_this.deleteArrayIndex = dataObj.deleteArrayIndex;
					if(type == 1) {
						_this.receiveCustomerId = dataObj.receiveId;
						_this.receiveCustomer = dataObj.receiveName;
					} else if(type == 2) {
						_this.deliverCustomerId = dataObj.shipperId;
						_this.deliverCustomer = dataObj.shipperName;
					}
					_this.deliverNo = dataObj.deliveryNo;
					_this.sendGoodDate = dataObj.deliveryTime;
					_this.defaultQuantity = dataObj.defaultQuantity;
					_this.goodDetailList = dataObj.details;
					_this.goodDetailSummaryList = dataObj.summary;
				} else {
					_this.sendGoodDate = defaultDateString;
				}
			} else {
				var data = localStorage.getItem("data");
				if(data) {
					var dataObj = JSON.parse(data);

					_this.deliverCustomerId = dataObj.shipperId;
					_this.deliverCustomer = dataObj.shipperName;
					_this.receiveCustomerId = dataObj.receiveId;
					_this.receiveCustomer = dataObj.receiveName;
					_this.deleteArrayIndex = dataObj.deleteArrayIndex;
					_this.deliverNo = dataObj.deliveryNo;
					_this.sendGoodDate = dataObj.deliveryTime;
					_this.defaultQuantity = dataObj.defaultQuantity;
					_this.goodDetailList = dataObj.details;
					_this.goodDetailSummaryList = dataObj.summary;
				} else {
					_this.sendGoodDate = defaultDateString;
				}
			}
		},
		gotoPage: function(type) {
			var _this = this;

			var saveData = {
				deleteArrayIndex: _this.deleteArrayIndex,
				shipperId: _this.deliverCustomerId,
				shipperName: _this.deliverCustomer,
				receiveId: _this.receiveCustomerId,
				receiveName: _this.receiveCustomer,
				deliveryNo: _this.deliverNo,
				deliveryTime: _this.sendGoodDate,
				defaultQuantity: _this.defaultQuantity,
				details: _this.goodDetailList,
				summary: _this.goodDetailSummaryList
			}
			localStorage.setItem("data", JSON.stringify(saveData));
			// type: 1：发货客户     2：收货客户
			window.location.href = _common.version("./chooseCustomer.html?type=" + type);
		},
		quantityValidate: function() {
			var _this = this;

			_this.defaultQuantity = _this.defaultQuantity.replace(/[^\d\.]/g, '').replace(/^0*/g, '');

			if(_this.defaultQuantity == '') {
				_this.defaultQuantity = 0;
			}
		},
		quantityTableValidate: function(value, index) {
			var _this = this;

			value.outboundQuantity = value.outboundQuantity.replace(/[^\d\.]/g, '').replace(/^0*/g, '');

			if(value.outboundQuantity == '') {
				value.outboundQuantity = 0;
			}

			_this.goodDetailList[index].outboundQuantity = value.outboundQuantity;

			_this.computerGoodDetailSummary();
		},
		deliveryNoValidate: function() {
			var _this = this;

			_this.deliverNo = _this.deliverNo.replace(/[^\w\/]/ig, '');
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		}
	}
});

function loginOut() {
	var className = 'com.android.h5.H5WebviewActivity';

	var btnArray = ['取消', '确定'];
	mui.confirm('确定要退出登录吗？', '', btnArray, function(e) {
		if(e.index == 1) {
			codeList.deleteArrayIndex = -1;

			MuiAjax.ajax_Post_Json({
				url: 'special/logout',
			}, function(res) {
				localStorage.removeItem("data");
				localStorage.removeItem("recordData");
				plus.loginout.loginOut(className, null, null);
			});
		}
	})
}