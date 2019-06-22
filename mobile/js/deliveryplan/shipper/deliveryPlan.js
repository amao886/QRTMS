var planList = new Vue({
	el: '#deliveryPlanWrapper',
	data: {
		logisticsState: 0,
		isShowFilterView: false,
		searchText: '',
		deliverBillState: 0,
		subOrderTakingState: 0,
		pageNum: 1, //当前页
		pageSize: 5, //每页传几条数据
		allocate: false, //是否分配 true：已分配， false：未分配
		subordinateState: 0, //下级接单状态 0:未接单，1：已接单 不传全部
		generate: false, //是否生成发货单 false：未生成 true：已生成 不传全部
		dLoading: false,
		collection: []
	},
	methods: {
		getListData: function(index, tops) {
			var _this = this;

			var baseValue = {
				allocate: _this.allocate,
				num: _this.pageNum,
				size: _this.pageSize
			}
			if(!_this.isNull(_this.searchText)) {
				baseValue["likeString"] = _this.searchText;
			}
			if(_this.deliverBillState != 0) {
				baseValue["generate"] = _this.generate;
			}
			if(_this.subOrderTakingState != 0) {
				baseValue["subordinateState"] = _this.subordinateState;
			}
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/plan/shipper/search',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					console.log(res);
					var collections = res.results.collection;

					if(_this.pageNum == 1 && (collections == null || collections.length == 0)) { //第一页
						$('.planList-wrapper').html('<p class="noData" style="text-align:center;font-size:15px;padding:10px 0;">暂无数据</p>');
						$('.weui-loadmore').hide();
					} else if(_this.pageNum != 1 && (collections == null || collections.length == 0)) { //最后一页
						_this.dLoading = false;
						$('.allDataFinish').remove();
						$('.planList-wrapper').append('<p class="allDataFinish" style="text-align:center;font-size:15px;padding:10px 0;">已经到底了</p>');
						$('.weui-loadmore').hide();
					} else {
						$('.noData').remove();

						_this.collection = _this.collection.concat(collections);

						if(res.results.pageNum == res.results.pages) { //最后一页
							_this.dLoading = false;
							$('.allDataFinish').remove();
							$('.planList-wrapper').append('<p class="allDataFinish" style="text-align:center;font-size:15px;padding:10px 0;">已经到底了</p>');
							setTimeout(function() {
								$(".allDataFinish").insertAfter($("#" + (_this.collection.length - 1)));
							}, 200)
							$('.weui-loadmore').hide();
						} else {
							_this.dLoading = true;
							$('.allDataFinish').remove();
						}

						if(index) {
							if(index == 1) {
								$(document).scrollTop(tops);
							} else {
								index--;
								_this.pageNum++;
								_this.getListData(index, tops);
							}
						}
					}
				}
			}, function(res) {
				$('.weui-loadmore').hide();
			});
		},
		chooseLogisticsState: function(id) {
			var _this = this;

			_this.logisticsState = id;

			if(id == 0) {
				_this.allocate = false;
			} else if(id == 1) {
				_this.allocate = true;
			}

			_this.collection.splice(0, _this.collection.length);
			_this.pageNum = 1;

			_this.getListData();
		},
		openFilterView: function() {
			var _this = this;

			_this.isShowFilterView = true;
		},
		closeFilterView: function(ev) {
			var _this = this;

			if(ev.target === $('.chooseCon')[0]) {
				_this.isShowFilterView = false;

				var filterData = sessionStorage.getItem("shipperFilterData");
				if(filterData) {
					var dataObj = JSON.parse(filterData);

					_this.searchText = dataObj.searchText;
					_this.deliverBillState = dataObj.deliverBillState;
					_this.subOrderTakingState = dataObj.subOrderTakingState;
				} else {
					_this.searchText = '';
					_this.deliverBillState = 0;
					_this.subOrderTakingState = 0;
				}
			}
		},
		chooseDeliverBillState: function(id) {
			var _this = this;

			_this.deliverBillState = id;

			if(id == 1) {
				_this.generate = false;
			} else if(id == 2) {
				_this.generate = true;
			}
		},
		chooseSubOrderTakingState: function(id) {
			var _this = this;

			_this.subOrderTakingState = id;

			_this.subordinateState = id - 1;
		},
		clearText: function() {
			var _this = this;

			_this.searchText = '';
		},
		cancel: function() {
			var _this = this;

			_this.isShowFilterView = false;

			var filterData = sessionStorage.getItem("shipperFilterData");
			if(filterData) {
				var dataObj = JSON.parse(filterData);

				_this.searchText = dataObj.searchText;
				_this.deliverBillState = dataObj.deliverBillState;
				_this.subOrderTakingState = dataObj.subOrderTakingState;
			} else {
				_this.searchText = '';
				_this.deliverBillState = 0;
				_this.subOrderTakingState = 0;
			}
		},
		confirm: function() {
			var _this = this;

			_this.isShowFilterView = false;

			var filterData = {
				searchText: _this.searchText,
				deliverBillState: _this.deliverBillState,
				subOrderTakingState: _this.subOrderTakingState
			}
			sessionStorage.setItem("filterData", JSON.stringify(filterData));

			_this.collection.splice(0, _this.collection.length);

			_this.getListData();
		},
		toDetail: function(id) {
			window.location.href =_common.version('../deliveryPlanDetail.html?id=' + id);
		},
		entryPlan: function() {
			window.location.href =_common.version('./inputDeliveryPlanOne.html');
		},
		allotCarrier: function(id) {
			window.location.href =_common.version('./allotLogistics.html?planKey=' + id);
		},
		generateInvoice: function(id) {
			window.location.href =_common.version('./generateInvoiceOne.html?planKey=' + id);
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		}
	}
});

$(function() {
	sessionStorage.removeItem("scrollData");
	var storeData = JSON.parse(sessionStorage.getItem('scrollData')),
		_tops = 0;
	if(storeData) { //走缓存
		planList.pageNum = storeData.num;
		if(storeData.searchmsg) { //input有值的时候
			$('#inputSearch').val(storeData.searchmsg); //给input赋值
		}
		_tops = storeData.top;
	} else {
		planList.pageNum = 1;
	}

	planList.getListData(planList.pageNum, _tops);

	//点击enter搜索
	$('#inputSearch').keyup(function(ev) {
		if(ev.keyCode == 13) {
			if($.trim($('#inputSearch').val())) {
				planList.searchListData();
			} else {
				$.toast('请先输入搜索内容', 'text')
			}
		}
	})
})

//滑动存储数据
$(document).scroll(function() {
	var totalheight = $(window).scrollTop();
	if(window.sessionStorage) {
		var scrollData = {
			num: planList.pageNum,
			searchmsg: $.trim($('#inputSearch').val()),
			top: totalheight
		}
		sessionStorage.setItem('scrollData', JSON.stringify(scrollData));
	}
});

//下拉刷新
$(document.body).infinite(0).on("infinite", function() {
	if(planList.dLoading) {
		planList.pageNum++;
		planList.dLoading = false;
		$('.weui-loadmore').show();
		planList.getListData();
	}
});