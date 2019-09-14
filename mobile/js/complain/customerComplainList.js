var complainList = new Vue({
	el: '#complainList',
	data: {
		searchText: '',
		pageNum: 1, //当前页
		pageSize: 5, //每页传几条数据
		dLoading: false,
		scrollData: {},
		pages: 1,
		collection: []
	},
	methods: {
		getListData: function(index, tops) {
			var _this = this;

			var baseValue = {
				num: _this.pageNum,
				size: _this.pageSize
			}
			if(!_this.isNull(_this.searchText)) {
				baseValue["likeString"] = _this.searchText;
			}
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/complaint/list',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					console.log(res);
					var collections = res.page.collection;

					if(_this.pageNum == 1 && (collections == null || collections.length == 0)) { //第一页
						$('.complainList-wrapper').html('<p class="noData" style="text-align:center;font-size:15px;padding:10px 0;">暂无数据</p>');
						$('.weui-loadmore').hide();
					} else if(_this.pageNum != 1 && (collections == null || collections.length == 0)) { //最后一页
						_this.dLoading = false;
						$('.allDataFinish').remove();
						$('.complainSearch-wrapper').append('<p class="allDataFinish" style="text-align:center;font-size:15px;padding:10px 0;">已经到底了</p>');
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
							$('.complainSearch-wrapper').append('<p class="allDataFinish" style="text-align:center;font-size:15px;padding:10px 0;">已经到底了</p>');
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
		callPhone: function(event, phoneNum) {
			var _this = this;

			var text = "现在拨打电话联系" + phoneNum +"？";
			$.modal({
				title: "温馨提示",
				text: text,
				buttons: [
					{ text: "拨打", onClick: function() {window.location.href="tel:" + phoneNum} },
					{ text: "取消", className: "default" },
				]
			});
		},
		toDetail: function(id) {
			var _this = this;

			localStorage.setItem("clickListTag", "1");
			window.location.href = _common.version("../partener/transportManageDetail.html?id=" + id + "&fromHomePage=false&fromComplainPage=true");
		},
		search: function() {
			var _this = this;

			if($.trim(_this.searchText)) {
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
		clear: function() {
			var _this = this;

			_this.searchText = '';
			_this.searchListData();
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
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
		complainList.pageNum = storeData.num;
		if(storeData.searchmsg) { //input有值的时候
			$('#inputSearch').val(storeData.searchmsg); //给input赋值
		}
		_tops = storeData.top;
		complainList.collection = storeData.data;
		if(complainList.pageNum == storeData.pages) { //最后一页
			complainList.dLoading = false;
			$('.allDataFinish').remove();
			$('.complainSearch-wrapper').append('<p class="allDataFinish" style="text-align:center;font-size:15px;padding:10px 0;">已经到底了</p>');
			$('.weui-loadmore').hide();
		} else {
			complainList.dLoading = true;
		}
		setTimeout(function() {
			$(document).scrollTop(_tops);
		}, 200);
	} else {
		complainList.pageNum = 1;
		complainList.getListData(complainList.pageNum, _tops);
	}

	//点击enter搜索
	$('#inputSearch').keyup(function(ev) {
		if(ev.keyCode == 13) {
			if($.trim($('#inputSearch').val())) {
				complainList.searchListData();
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
		complainList.scrollData.num = complainList.pageNum;
		complainList.scrollData.searchmsg = $.trim($('#inputSearch').val());
		complainList.scrollData.top = totalheight;
		complainList.scrollData.pages = complainList.pages;
		complainList.scrollData.data = complainList.collection;
		sessionStorage.setItem('scrollData', JSON.stringify(complainList.scrollData));
	}
});

//下拉刷新
$(document.body).infinite(0).on("infinite", function() {
	if(complainList.dLoading) {
		complainList.pageNum++;
		complainList.dLoading = false;
		$('.weui-loadmore').show();
		complainList.getListData();
	}
});