var siteManageList = new Vue({
	el: '#siteManageWrapper',
	data: {
		vehicleState: 0,
		isShowFilterView: false,
		searchText: '',
		deliveryDateType: 0,
		deliveryDateString: '',
		status: '',
		pickTime: '',
		pageNum: 1, //当前页
		pageSize: 5, //每页传几条数据
		dLoading: false,
		collection: [],
		scrollData: {},
		pages: 1
	},
	methods: {
		getListData: function(index, tops) {
			var _this = this;

			var baseValue = {
				status: _this.status,
				likeString: _this.searchText,
				pickTime: _this.pickTime,
				num: _this.pageNum,
				size: _this.pageSize
			}

			EasyAjax.ajax_Post_Json({
				url: 'scene/search/list',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					console.log(res);
					var collections = res.page.results;

					if(_this.pageNum == 1 && (collections == null || collections.length == 0)) { //第一页
						$('.allDataFinish').remove();
						$('.manageList-wrapper').append('<p class="noData" style="text-align:center;font-size:15px;padding:10px 0;">暂无数据</p>');
						$('.weui-loadmore').hide();
					} else if(_this.pageNum != 1 && (collections == null || collections.length == 0)) { //最后一页
						_this.dLoading = false;
						$('.allDataFinish').remove();
						setTimeout(function() {
							$('.manageList-wrapper').append('<p class="allDataFinish" style="text-align:center;font-size:15px;padding:10px 0;">已经到底了</p>');
						}, 200)
						$('.weui-loadmore').hide();
					} else {
						$('.noData').remove();

						_this.collection = _this.collection.concat(collections);
						_this.pages = res.page.pages;
						_this.scrollData = {
							num: _this.pageNum,
							searchmsg: $.trim($('#inputSearch').val()),
							datevalue: _this.getSearchDate($("#showDate").html()),
							status: _this.status,
							top: $(window).scrollTop(),
							pages: _this.pages,
							data: _this.collection
						}
						sessionStorage.setItem('scrollData', JSON.stringify(_this.scrollData));

						if(res.page.num == res.page.pages) { //最后一页
							_this.dLoading = false;
							$('.allDataFinish').remove();

							setTimeout(function() {
								$('.manageList-wrapper').append('<p class="allDataFinish" style="text-align:center;font-size:15px;padding:10px 0;">已经到底了</p>');
							}, 200)
							$('.weui-loadmore').hide();
						} else {
							_this.dLoading = true;
							$('.allDataFinish').remove();
						}

						if(index) {
							if(index == 1) {
								$(document).scrollTop(tops);
							}
						}
					}
				}
			}, function(res) {
				$('.weui-loadmore').hide();
			});
		},
		chooseVehicleState: function(id) {
			var _this = this;

			_this.vehicleState = id;

			if(id == 0) {
				_this.status = '';
			} else if(id == 1) {
				_this.status = '0';
			} else if(id == 2) {
				_this.status = '1';
			}

			_this.searchText = '';
			_this.deliveryDateType = 0;
			_this.pickTime = '';

			sessionStorage.removeItem('scrollData');
			sessionStorage.removeItem('siteFilterData');

			_this.collection.splice(0, _this.collection.length);
			_this.pageNum = 1;

			$('.allDataFinish').remove();
			$('.noData').remove();

			$('.weui-loadmore').show();

			_this.getListData();
		},
		openFilterView: function() {
			var _this = this;

			_this.isShowFilterView = true;

			if(_this.deliveryDateType == 0) {
				$("#showDate").html('其他日期');
			} else if(_this.deliveryDateType == 1) {
				$("#showDate").html(_this.deliveryDateString);
				if(!_this.isNull(_this.getSearchDate($("#showDate").html()))) {
					_this.pickTime = $("#showDate").html();
				}
			}
		},
		closeFilterView: function(ev) {
			var _this = this;

			if(ev.target === $('.chooseCon')[0]) {
				_this.isShowFilterView = false;

				var filterData = sessionStorage.getItem("siteFilterData");
				if(filterData) {
					var dataObj = JSON.parse(filterData);

					_this.searchText = dataObj.searchText;
					_this.deliveryDateType = dataObj.deliveryDateType;
					_this.deliveryDateString = dataObj.deliveryDateString;
				} else {
					_this.searchText = '';
					_this.deliveryDateType = 0;
					_this.deliveryDateString = '';
				}
			}
		},
		chooseDeliveryDateType: function(id) {
			var _this = this;

			_this.deliveryDateType = id;

			if(id == 0) {
				_this.pickTime = '';
				$("#showDate").html('其他日期');
			} else if(id == 1) {
				_this.pickTime = _this.getSearchDate($("#showDate").html());
			}
		},
		clearText: function() {
			var _this = this;

			_this.searchText = '';
		},
		cancel: function() {
			var _this = this;

			_this.isShowFilterView = false;

			var filterData = sessionStorage.getItem("siteFilterData");
			if(filterData) {
				var dataObj = JSON.parse(filterData);

				_this.searchText = dataObj.searchText;
				_this.deliveryDateType = dataObj.deliveryDateType;
				_this.deliveryDateString = dataObj.deliveryDateString;
			} else {
				_this.searchText = '';
				_this.deliveryDateType = 0;
				_this.deliveryDateString = '';
			}
		},
		confirm: function() {
			var _this = this;

			if(_this.deliveryDateType == 1 && _this.isNull(_this.getSearchDate($("#showDate").html()))) {
				$.toptip('其他时间不能为空');
				return;
			}

			if(_this.deliveryDateType == 1 && !_this.isNull(_this.getSearchDate($("#showDate").html()))) {
				_this.pickTime = $("#showDate").html();
				_this.deliveryDateString = $("#showDate").html();
			}

			_this.isShowFilterView = false;

			var filterData = {
				searchText: _this.searchText,
				deliveryDateType: _this.deliveryDateType,
				deliveryDateString: $("#showDate").html()
			}
			sessionStorage.setItem("siteFilterData", JSON.stringify(filterData));

			_this.collection.splice(0, _this.collection.length);

			_this.getListData();
		},
		toPage: function(vehicleStatus, orderId, deliveryNo) {
			var _this = this;

			if (_this.isNull(deliveryNo)) {
				$.toptip('送货单号为空，不能操作');
				return;
			}

			localStorage.setItem("clickListTag", "1");

			// vehicleStatus 0:未到车  1:已到车
			if(vehicleStatus == 0) {
				EasyAjax.ajax_Post_Json({
					url: 'scene/queryDeliveryDetails/' + deliveryNo,
				}, function(res) {
					if(!res.success) {
						$.toast(res.message, "text");
					} else {
						console.log(res);

						var orderNo = res.result.orderKey;
						var deliveryCarDto = res.result.deliveryCarDto;

						var deliveryNo = '';
						var plateNo = '';
						var driverName = '';
						var driverPhone = '';

						if(deliveryCarDto != null) {
							if(deliveryCarDto.deliveryNo != null) {
								deliveryNo = deliveryCarDto.deliveryNo;
							}
							if(deliveryCarDto.license != null) {
								plateNo = deliveryCarDto.license;
							}
							if(deliveryCarDto.driverName != null) {
								driverName = deliveryCarDto.driverName;
							}
							if(deliveryCarDto.driverContact != null) {
								driverPhone = deliveryCarDto.driverContact;
							}
						}

						window.location.href = _common.version('./registrVehicleTwo.html?plateNo=' + plateNo + '&driverName=' + driverName + '&driverPhone=' + driverPhone + '&deliveryNo=' + deliveryNo + '&orderNo=' + orderNo);
					}
				})
			} else if(vehicleStatus == 1) {
				window.location.href = _common.version('./vehicleDetail.html?orderId=' + orderId);
			}
		},
		entryRegistrVehicle: function() {
			window.location.href = _common.version('./registrVehicleOne.html');
		},
		getSearchDate: function(text) {
			var result = "";
			if(text != '其他日期') {
				return text;
			}
			return result;
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		}
	}
});

window.onload = function() {
	if(!localStorage.getItem('clickListTag')) {
		localStorage.removeItem("clickListTag");
		sessionStorage.removeItem("scrollData");
	} else {
		localStorage.removeItem("clickListTag");
	}
	var storeData = JSON.parse(sessionStorage.getItem('scrollData')),
		_tops = 0;
	if(storeData) { //走缓存
		console.log('走缓存');
		siteManageList.pageNum = storeData.num; //页数
		if(storeData.searchmsg) { //input有值的时候
			siteManageList.searchText = storeData.searchmsg; //给input赋值
		}
		_tops = storeData.top; //距离
		siteManageList.status = storeData.status;
		siteManageList.pickTime = storeData.datevalue;
		if(siteManageList.isNull(siteManageList.status)) {
			siteManageList.vehicleState = 0;
		} else if(siteManageList.status == '0') {
			siteManageList.vehicleState = 1;
		} else if(siteManageList.status == '1') {
			siteManageList.vehicleState = 2;
		}
		if(!siteManageList.isNull(storeData.datevalue)) {
			$('#showDate').html(storeData.datevalue);
		}

		siteManageList.collection = storeData.data;
		if(siteManageList.pageNum == storeData.pages) { //最后一页
			siteManageList.dLoading = false;
			$('.allDataFinish').remove();
			setTimeout(function() {
				$('.manageList-wrapper').append('<p class="allDataFinish" style="text-align:center;font-size:15px;padding:10px 0;">已经到底了</p>');
			}, 200);
			$('.weui-loadmore').hide();
		} else {
			siteManageList.dLoading = true;
		}
		setTimeout(function() {
			$(document).scrollTop(_tops);
		}, 200);
	} else {
		//获取数据
		siteManageList.pageNum = 1;
		siteManageList.getListData(siteManageList.pageNum, _tops);
	}

	//点击enter搜索
	$('#inputSearch').keyup(function(ev) {
		if(ev.keyCode == 13) {
			if($.trim($('#inputSearch').val())) {
				recordList.searchListData();
			} else {
				$.toast('请先输入搜索内容', 'text')
			}
		}
	})

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
		endYear: currYear + 10, //结束年份
		onSelect: function(valueText, inst) {
			//显示的是select标签选中的值  
			console.log("valueText : " + valueText);
			//显示select 的value的值  
			console.log("inst._tempValue : " + inst._tempValue);
			console.dir("inst : " + inst);
			//显示select 的value的值  
			chooseDateValueText = valueText;
			$("#showDate").html(valueText);
		}
	};

	$("#date1").mobiscroll($.extend(opt['date'], opt['default']));

	//解决ios readonly兼容问题
	$('input[readonly]').on('focus', function() {
		$(this).trigger('blur');
	});
}

//滑动存储数据
$(document).scroll(function() {
	var totalheight = $(window).scrollTop();
	console.log('tops:' + totalheight);
	if(window.sessionStorage) {
		siteManageList.scrollData.num = siteManageList.pageNum;
		siteManageList.scrollData.searchmsg = $.trim($('#inputSearch').val());
		siteManageList.scrollData.datevalue = siteManageList.getSearchDate($("#showDate").html());
		siteManageList.scrollData.status = siteManageList.status;
		siteManageList.scrollData.top = totalheight;
		siteManageList.scrollData.pages = siteManageList.pages;
		siteManageList.scrollData.data = siteManageList.collection;

		sessionStorage.setItem('scrollData', JSON.stringify(siteManageList.scrollData));
	}
});

//下拉刷新
$(document.body).infinite(0).on("infinite", function() {
	if(siteManageList.dLoading) {
		siteManageList.pageNum++;
		siteManageList.dLoading = false;
		$('.weui-loadmore').show(); //显示加载更多
		siteManageList.getListData();
	}
});