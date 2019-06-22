var transportFrom = _common.getUrlParam('transportSts'),
	time = _common.getUrlParam('t'),
	fettle = "",
	pageNum = 1, //当前页
	pageSize = 5, //每页传几条数据
	dLoading = false,
	isLoadMore = false,
	releaseGoodDate = "-1",
	releaseGoodDateTemp = "-1",
	bindStatus = "",
	bindStatusTemp = "",
	signStatus = "",
	signStatusTemp = "",
	dateValue = 0,
	dateValueTemp = 0,
	isChooseDate = false,
	chooseDateValueText = "",
	searchText = "",
	moreListHtmlData = "";
if(transportFrom === 'shipper') { //发货管理
	$('title').html('发货管理');
	$('.bindCodeSts').show();
	$('.writeSts').hide();

} else if(transportFrom === 'covey') { //承运管理
	$('title').html('承运管理');
	$('.bindCodeSts').hide();
	$('.writeSts').hide();

} else if(transportFrom === 'receive') { //收货管理
	$('title').html('收货管理');
	$('.bindCodeSts').hide();
	$('.writeSts').show();
}

//切换状态
$('.searchSelect').on('click', function() {
	$(this).addClass('borderBottom').siblings().removeClass('borderBottom');
	var flag = $(this).attr('uid');
	console.log('flag:' + flag);
	if(flag == 0) {
		fettle = "";
	} else if(flag == 1) {
		fettle = "1";
	} else if(flag == 2) {
		fettle = "4";
	}

	releaseGoodDate = "-1";
	releaseGoodDateTemp = "-1";
	bindStatus = "";
	bindStatusTemp = "";
	signStatus = "";
	signStatusTemp = "";
	dateValue = 0;
	dateValueTemp = 0;
	isChooseDate = false;
	chooseDateValueText = "";
	searchText = "";
	$('.cancelBtn').click();
	moreListHtmlData = "";
	$('.manageList-wrapper').html('');
	pageNum = 1;
	isLoadMore = false;
	getListData(transportFrom); //查询数据
})

//显示筛选
$('.choose').click(function() {
	$('.chooseCon').addClass('show');
})

//取消按钮
$('.cancelBtn').click(function() {
	$('#inputSearch').val(searchText);
	if(transportFrom === 'shipper') {
		if(bindStatusTemp == "") {
			$($('.choosePart .billSts>li')[0]).addClass('active').siblings().removeClass('active');
		} else if(bindStatusTemp == "0") {
			$($('.choosePart .billSts>li')[1]).addClass('active').siblings().removeClass('active');
		} else if(bindStatusTemp == "1") {
			$($('.choosePart .billSts>li')[2]).addClass('active').siblings().removeClass('active');
		}
	}

	if(transportFrom === 'receive') {
		if(signStatusTemp == "") {
			$($('.choosePart .signSts>li')[0]).addClass('active').siblings().removeClass('active');
		} else if(signStatusTemp == "0") {
			$($('.choosePart .signSts>li')[1]).addClass('active').siblings().removeClass('active');
		} else if(signStatusTemp == "1") {
			$($('.choosePart .signSts>li')[2]).addClass('active').siblings().removeClass('active');
		}
	}

	if(dateValueTemp < 4) {
		if(releaseGoodDateTemp == "-1") {
			$($('.choosePart>ul>li')[0]).addClass('active').siblings().removeClass('active');
			$("#showDate").html('其他日期');
		} else if(releaseGoodDateTemp == "3") {
			$($('.choosePart>ul>li')[1]).addClass('active').siblings().removeClass('active');
			$("#showDate").html('其他日期');
		} else if(releaseGoodDateTemp == "7") {
			$($('.choosePart>ul>li')[2]).addClass('active').siblings().removeClass('active');
			$("#showDate").html('其他日期');
		} else if(releaseGoodDateTemp == "30") {
			$($('.choosePart>ul>li')[3]).addClass('active').siblings().removeClass('active');
			$("#showDate").html('其他日期');
		}
	} else {
		$($('.choosePart>ul>li')[4]).addClass('active').siblings().removeClass('active');
		$("#showDate").html(chooseDateValueText);
	}
	$('.chooseCon').removeClass('show');
})

//遮罩层点击取消
$('.chooseCon').on('click', function(ev) {
	if(ev.target === $('.chooseCon')[0]) {
		$('#inputSearch').val(searchText);
		if(transportFrom === 'shipper') {
			if(bindStatusTemp == "") {
				$($('.choosePart .billSts>li')[0]).addClass('active').siblings().removeClass('active');
			} else if(bindStatusTemp == "0") {
				$($('.choosePart .billSts>li')[1]).addClass('active').siblings().removeClass('active');
			} else if(bindStatusTemp == "1") {
				$($('.choosePart .billSts>li')[2]).addClass('active').siblings().removeClass('active');
			}
		}

		if(transportFrom === 'receive') {
			if(signStatusTemp == "") {
				$($('.choosePart .signSts>li')[0]).addClass('active').siblings().removeClass('active');
			} else if(signStatusTemp == "0") {
				$($('.choosePart .signSts>li')[1]).addClass('active').siblings().removeClass('active');
			} else if(signStatusTemp == "1") {
				$($('.choosePart .signSts>li')[2]).addClass('active').siblings().removeClass('active');
			}
		}

		if(dateValueTemp < 4) {
			if(releaseGoodDateTemp == "-1") {
				$($('.choosePart .dateSts>li')[0]).addClass('active').siblings().removeClass('active');
				$("#showDate").html('其他日期');
			} else if(releaseGoodDateTemp == "3") {
				$($('.choosePart .dateSts>li')[1]).addClass('active').siblings().removeClass('active');
				$("#showDate").html('其他日期');
			} else if(releaseGoodDateTemp == "7") {
				$($('.choosePart .dateSts>li')[2]).addClass('active').siblings().removeClass('active');
				$("#showDate").html('其他日期');
			} else if(releaseGoodDateTemp == "30") {
				$($('.choosePart .dateSts>li')[3]).addClass('active').siblings().removeClass('active');
				$("#showDate").html('其他日期');
			}
		} else {
			$($('.choosePart .dateSts>li')[4]).addClass('active').siblings().removeClass('active');
			$("#showDate").html(chooseDateValueText);
		}
		$('.chooseCon').removeClass('show');
	}
})

//状态选择
$('.choosePart>ul>li').click(function() {
	$(this).addClass('active').siblings().removeClass('active');
})

//确定按钮
$('.confirmBtn').click(function() {
	searchText = $('#inputSearch').val();
	console.log('发货时间：' + releaseGoodDate);
	releaseGoodDateTemp = releaseGoodDate;
	dateValueTemp = dateValue;
	if(transportFrom === 'shipper') {
		console.log('二维码绑单：' + bindStatus);
		bindStatusTemp = bindStatus;
	}
	if(transportFrom === 'receive') {
		console.log('签收状态：' + signStatus);
		signStatusTemp = signStatus;
	}
	if(dateValue == 4 && !isChooseDate) {
		$.toptip('其他时间不能为空');
		return;
	}

	$('.manageList-wrapper').html('');
	pageNum = 1;
	isLoadMore = false;
	moreListHtmlData = "";
	getListData(transportFrom); //查询数据
	$('.chooseCon').removeClass('show');
})

//筛选框清除icon
$('#iconClear').on('click', function() {
	$('#inputSearch').val('');
})

//跳转页面
function toPage(id) {
	window.location.href = _common.version("transportManageDetail.html?id=" + id + "&fromHomePage=false&fromComplainPage=false");
}

//获取后台接口
function getListData(urlParam, index, tops) {
	var baseValue1 = {
		likeString: $('#inputSearch').val(),
		timeType: releaseGoodDate,
		searchDate: getSearchDate($("#showDate").html()),
		bindStatus: bindStatus,
		fettle: fettle,
		num: pageNum,
		size: pageSize
	}
	var baseValue2 = {
		likeString: $('#inputSearch').val(),
		timeType: releaseGoodDate,
		searchDate: getSearchDate($("#showDate").html()),
		fettle: fettle,
		num: pageNum,
		size: pageSize
	}
	var baseValue3 = {
		likeString: $('#inputSearch').val(),
		timeType: releaseGoodDate,
		searchDate: getSearchDate($("#showDate").html()),
		signFettle: signStatus,
		fettle: fettle,
		num: pageNum,
		size: pageSize
	}
	var submitData = JSON.stringify(baseValue1);
	if(transportFrom != 'shipper') {
		if(transportFrom == 'covey') {
			submitData = JSON.stringify(baseValue2);
		} else {
			submitData = JSON.stringify(baseValue3);
		}
	}
	EasyAjax.ajax_Post_Json({
		url: 'enterprise/order/' + urlParam + '/manage/search',
		data: submitData
	}, function(res) {
		if(!res.success) {
			$.toast("加载失败，请稍后重试", "text");
		} else {
			var results = res.page.collection;
			console.log(results);
			if(pageNum == 1 && (results == null || results.length == 0)) { //没有数据
				$('.manageList-wrapper').html('<p class="noData" style="text-align:center;font-size:15px;padding:10px 0;">暂无数据</p>');
				$('.weui-loadmore').hide();
			} else if(pageNum != 1 && (results == null || results.length == 0)) {
				$('.weui-loadmore').show();
				$('.weui-loadmore').html('已经到底了');
			} else {
				var listDataHtml = "";

				$('.noData').remove();

				if(!isLoadMore) {
					listDataHtml = $('#listData').render(results);
					moreListHtmlData = listDataHtml;
					$('.manageList-wrapper').empty();
				} else {
					moreListHtmlData += $('#listData').render(results);
					listDataHtml = moreListHtmlData;
				}
				$('.manageList-wrapper').html(listDataHtml);

				dLoading = true;
				$('.weui-loadmore').hide();
			}

			if(index) {
				if(index == 1) {
					$(document).scrollTop(tops);
				} else {
					index--;
					pageNum++;
					isLoadMore = true;
					getListData(transportFrom, index, tops);
				}
			}
		}
	})
}

window.onload = function() {
	var storeData = JSON.parse(sessionStorage.getItem('scrollData'));
	if(storeData) { //走缓存
		console.log('走缓存');
		var tops = parseInt(sessionStorage.getItem("top")); //距离
		var num = sessionStorage.getItem("num"); //页数
		$('#inputSearch').val(storeData.searchmsg);
		fettle = storeData.transportStatus;
		releaseGoodDate = storeData.choosedate;
		bindStatus = storeData.choosebind;
		$('#showDate').val(storeData.datevalue);

		tops = tops ? tops : 0;
		num = num ? num : 1;

		pageNum = num;

		getListData(transportFrom, 1, tops);

	} else {
		//获取数据
		getListData(transportFrom);
	}

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
			//          alert($('#date4').mobiscroll('getValue'));
			chooseDateValueText = valueText;
			isChooseDate = true;
			$("#showDate").html(valueText);
		},
		onCancel: function(event, inst) {
			dateValue = 4;
		}
	};

	$("#date4").mobiscroll($.extend(opt['date'], opt['default']));

	//切换tab
	$('.selectTab .weui-flex__item').on('click', function() {
		$(this).addClass('active').siblings().removeClass('active');
	});

	//解决ios readonly兼容问题
	$('input[readonly]').on('focus', function() {
		$(this).trigger('blur');
	});

	$("input[name='dateType']").click(function() {
		dateValue = $(this).val(); //获取选中的radio的值
		if(dateValue < 4) {
			$("#showDate").html('其他日期');
			isChooseDate = false;
			if(dateValue == 0) {
				releaseGoodDate = "-1";
			} else if(dateValue == 1) {
				releaseGoodDate = "3";
			} else if(dateValue == 2) {
				releaseGoodDate = "7";
			} else if(dateValue == 3) {
				releaseGoodDate = "30";
			}
		}
	});

	$("input[name='bindCodeType']").click(function() {
		var value = $(this).val(); //获取选中的radio的值
		if(value < 3) {
			if(value == 0) {
				bindStatus = "";
			} else if(value == 1) {
				bindStatus = "0";
			} else if(value == 2) {
				bindStatus = "1";
			}
		}
	});

	$("input[name='signStatus']").click(function() {
		var value = $(this).val(); //获取选中的radio的值
		if(value < 3) {
			if(value == 0) {
				signStatus = "";
			} else if(value == 1) {
				signStatus = "1";
			} else if(value == 2) {
				signStatus = "2";
			}
		}
	});
}

function evaluateSubmit(orderKey, evaluationKey, event) { //evaluationKey-- 0:差评  1:好评
	event.stopPropagation();

	var _this = this;

	var tripContent = '您选择的是好评，确定提交吗？';
	if(evaluationKey == 0) {
		tripContent = '您选择的是差评，确定提交吗？';
	}
	$.confirm(tripContent, function() {
		var baseValue = {
			orderKey: orderKey,
			evaluation: evaluationKey
		}
		EasyAjax.ajax_Post_Json({
			url: 'enterprise/order/evaluation',
			data: JSON.stringify(baseValue)
		}, function(res) {
			if(res.success) {
				$.toast(res.message, function() {
					window.location.reload();
				});
			} else {
				$.toast(res.message);
			}
		});
	});
}

function getSearchDate(text) {
	var result = "";
	if(text != '其他日期') {
		return text;
	}
	return result;
}

//滑动存储数据
$(document).scroll(function() {
	var totalheight = $(window).scrollTop();
	console.log('tops:' + totalheight);
	if(window.sessionStorage) {
		var scrollData = {
			num: pageNum,
			searchmsg: $.trim($('#inputSearch').val()),
			transportStatus: fettle,
			choosedate: releaseGoodDate,
			choosebind: bindStatus,
			datevalue: getSearchDate($("#showDate").html()),
			top: totalheight
		}
		sessionStorage.setItem('scrollData', JSON.stringify(scrollData));
	}
});

//下拉刷新
$(document.body).infinite(0).on("infinite", function() {
	if(dLoading) {
		$('.weui-loadmore').show(); //显示加载更多
		pageNum++;
		dLoading = false;
		isLoadMore = true;
		getListData(transportFrom);
	}
});