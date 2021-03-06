

const  vm = new Vue({
	el:'#lookingGoods',
	data:{
		collection:[],
		size:5,//条数
		num:1,//页数
		dLoading: false,
	},
	mounted:function(){
     var _this = this;
     _this.getListData()
	},
	methods:{
		getListData: function(index, tops) {
			var _this = this;
			var parmas = {
				num: _this.num,
				size: _this.size
			}
			EasyAjax.ajax_Post_Json({
				url: 'driver/personal/search',
				data: JSON.stringify(parmas)
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					console.log(res);
					var collections = res.page.results;
					if(_this.num == 1 && (collections == null || collections.length == 0)) { //第一页
						$('.driverList').append('<p class="noData" style="text-align:center;font-size:15px;padding:10px 0;">暂无数据</p>');
						$('.weui-loadmore').hide();
							$('.allDataFinish').remove();
					} else if(_this.num != 1 && (collections == null || collections.length == 0)) { //最后一页
						_this.dLoading = false;
						$('.allDataFinish').remove();
						$('.driverList').append('<p class="allDataFinish" style="text-align:center;font-size:15px;padding:10px 0;">已经到底了</p>');
						$('.weui-loadmore').hide();
					} else {
						$('.noData').remove();

						_this.collection = _this.collection.concat(collections);

						if(res.page.num == res.page.pages) { //最后一页
							_this.dLoading = false;
							$('.allDataFinish').remove();
							$('.driverList').append('<p class="allDataFinish" style="text-align:center;font-size:15px;padding:10px 0;">已经到底了</p>');
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
								_this.num++;
								_this.getListData(index, tops);
							}
						}
					}
				}
			}, function(res) {
				$('.weui-loadmore').hide();
			});
		},
		lookingGoods:function(){
			console.log(1111)
			window.location.href='issueGoods.html';
		},
		
	}
})
//滑动存储数据
$(document).scroll(function() {
	var totalheight = $(window).scrollTop();
	if(window.sessionStorage) {
		var scrollData = {
			num: vm.num,
			searchmsg: $.trim($('#inputSearch').val()),
			top: totalheight
		}
		sessionStorage.setItem('scrollData', JSON.stringify(scrollData));
	}
});

//下拉刷新
$(document.body).infinite(0).on("infinite", function() {
	if(vm.dLoading) {
		vm.num++;
		vm.dLoading = false;
		$('.weui-loadmore').show();
		vm.getListData();
	}
});
