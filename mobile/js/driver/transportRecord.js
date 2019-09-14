var recordList = new Vue({
	el: '#recordSearchWrapper',
	data: {
		pageNum: 1, //当前页
		pageSize: 5, //每页传几条数据
		fettle: 0, //订单全部状态
		dLoading: false,
		collection: [],
		scrollData: {},
		pages: 1
	},
	methods: {
		getListData: function(index, tops) {
			var _this = this;

			var baseValue = {
				likeString: $('#inputSearch').val(),
				fettle: _this.fettle,
				num: _this.pageNum,
				size: _this.pageSize
			}
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/driver/search',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					console.log(res);
					var collections = res.page.collection;

					if(_this.pageNum == 1 && (collections == null || collections.length == 0)) { //第一页
						$('.recordList-wrapper').html('<p class="noData" style="text-align:center;font-size:15px;padding:10px 0;">暂无数据</p>');
						$('.weui-loadmore').hide();
					} else if(_this.pageNum != 1 && (collections == null || collections.length == 0)) { //最后一页
						_this.dLoading = false;
						$('.allDataFinish').remove();
						$('.recordSearch-wrapper').append('<p class="allDataFinish" style="text-align:center;font-size:15px;padding:10px 0;">已经到底了</p>');
						$('.weui-loadmore').hide();
					} else {
						$('.noData').remove();

						_this.collection = _this.collection.concat(collections);
						_this.pages = res.page.pages;
						_this.scrollData = {
							num: _this.pageNum,
							searchmsg: $.trim($('#inputSearch').val()),
							top: $(window).scrollTop(),
							pages: _this.pages,
							data: _this.collection
						}
						sessionStorage.setItem('scrollData', JSON.stringify(_this.scrollData));

						if(res.page.pageNum == res.page.pages) { //最后一页
							_this.dLoading = false;
							$('.allDataFinish').remove();
							$('.recordSearch-wrapper').append('<p class="allDataFinish" style="text-align:center;font-size:15px;padding:10px 0;">已经到底了</p>');
							$('.weui-loadmore').hide();
						} else {
							_this.dLoading = true;
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
		clearContent: function() {
			var _this = this;
			$('#inputSearch').val('');
			_this.searchListData();
		},
		search: function() {
			var _this = this;
			if($.trim($('#inputSearch').val())) {
				_this.searchListData();
			} else {
				$.toast('请先输入搜索内容', 'text')
			}
		},
		searchListData: function() {
			var _this = this;
			_this.pageNum = 1;
			_this.collection = [];
			$('.allDataFinish').remove();
			$('.weui-loadmore').show();
			_this.getListData();
		},
		toPage: function(id) {
			localStorage.setItem("clickListTag", "1");
			window.location.href = _common.version("../../view/partener/transportManageDetail.html?id=" + id);
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		},
		formatNullString: function(text) {
			var result = "--";
			if(this.isNull(text)) {
				return result;
			}
			return text;
		}
	}
});

$(function() {
	if(!localStorage.getItem('clickListTag')) {
		localStorage.removeItem("clickListTag");
		sessionStorage.removeItem("scrollData");
	} else {
		localStorage.removeItem("clickListTag");
	}
	var storeData = JSON.parse(sessionStorage.getItem('scrollData')),
		_tops = 0;
	if(storeData) { //走缓存
		recordList.pageNum = storeData.num;
		if(storeData.searchmsg) { //input有值的时候
			$('#inputSearch').val(storeData.searchmsg); //给input赋值
		}
		_tops = storeData.top;
		recordList.collection = storeData.data;
		if(recordList.pageNum == storeData.pages) { //最后一页
			recordList.dLoading = false;
			$('.allDataFinish').remove();
			$('.recordSearch-wrapper').append('<p class="allDataFinish" style="text-align:center;font-size:15px;padding:10px 0;">已经到底了</p>');
			$('.weui-loadmore').hide();
		} else {
			recordList.dLoading = true;
		}
		setTimeout(function() {
			$(document).scrollTop(_tops);
		}, 200);
	} else {
		recordList.pageNum = 1;
		recordList.getListData(recordList.pageNum, _tops);
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
})

//滑动存储数据
$(document).scroll(function() {
	var totalheight = $(window).scrollTop();
	if(window.sessionStorage) {
		recordList.scrollData.num = recordList.pageNum;
		recordList.scrollData.searchmsg = $.trim($('#inputSearch').val());
		recordList.scrollData.top = totalheight;
		recordList.scrollData.pages = recordList.pages;
		recordList.scrollData.data = recordList.collection;
		sessionStorage.setItem('scrollData', JSON.stringify(recordList.scrollData));
	}
});

//下拉刷新
$(document.body).infinite(0).on("infinite", function() {
	if(recordList.dLoading) {
		recordList.pageNum++;
		recordList.dLoading = false;
		$('.weui-loadmore').show();
		recordList.getListData();
	}
});