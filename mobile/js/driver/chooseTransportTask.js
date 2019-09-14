var recordList = new Vue({
	el: '#recordSearchWrapper',
	data: {
		pageNum: 1, //当前页
		pageSize: 5, //每页传几条数据
		fettle: 1, //订单运输中状态
		dLoading: false,
		collection: []
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
			window.location.href = _common.version("../../view/driver/uploadReceipt.html?waybillId=" + id + "&showClose=true");
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
	sessionStorage.removeItem("scrollData");
	var storeData = JSON.parse(sessionStorage.getItem('scrollData')),
		_tops = 0;
	if(storeData) { //走缓存
		recordList.pageNum = storeData.num;
		if(storeData.searchmsg) { //input有值的时候
			$('#inputSearch').val(storeData.searchmsg); //给input赋值
		}
		_tops = storeData.top;
	} else {
		recordList.pageNum = 1;
	}

	recordList.getListData(recordList.pageNum, _tops);

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
		var scrollData = {
			num: recordList.pageNum,
			searchmsg: $.trim($('#inputSearch').val()),
			top: totalheight
		}
		sessionStorage.setItem('scrollData', JSON.stringify(scrollData));
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