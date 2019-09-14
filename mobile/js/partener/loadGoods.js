var pulicTime = '';
var dNum = 1,
	pageSize = 5,
	dLoading = true;

var lotteryKey = '';
var chooseIndex = -1;
var awardType = -1;

window.onload = function() {
	getloadGoodsList({
		num: dNum,
		size: pageSize
	});

	//localStorage.clear();
	if(window.localStorage) {
		var _visit = localStorage.getItem("visited");
		if(!_visit) {
			//显示弹窗
			layer.open({
				type: 1,
				title: false,
				closeBtn: 0,
				area: '310px',
				skin: 'layui-layer-nobg', //没有背景色
				shadeClose: true,
				content: $('#showBox')
			});

			localStorage.setItem("visited", "1");
		}
	}

	//点击清空input框内容
	$('#searchClear').click(function() {
		$('#searchData').val('');
		searchMytaskList();
	})

	//内容搜索
	$('#search-btn').click(function() {
		serchFunc();
	})

	//点击搜索按钮搜索
	$('#searchData').keyup(function(ev) {
		if(ev.keyCode == 13) {
			serchFunc();
		}
	})

	function serchFunc() {
		if($.trim($('#searchData').val())) {
			searchMytaskList();
		} else {
			$.toast('请先输入搜索内容', 'text')
		}
	}
}

//刷新翻页
$(document.body).infinite(0).on("infinite", function() {
	if(dLoading) {
		dLoading = false;
		$('.weui-loadmore').show();
		dNum++;

		getloadGoodsList({
			num: dNum,
			size: pageSize,
			likeString: $("#searchData").val()
		})

	}
});

//获取货物列表
function getloadGoodsList(options) {
	var baseValue = {
		"num": options.num,
		"size": options.size,
		"likeString": options.likeString || '',
		"unload": false
	}
	EasyAjax.ajax_Post_Json({
		url: 'mobile/driver/search',
		data: JSON.stringify(baseValue)
	}, function(res) {
		var results = res.page.collection;

		$(".totalGoods").text(res.loadCount);
		if(dNum == 1 && (results == null || results.length == 0)) {
			$('.goods-list-con').append('<img class="noData" src="../../images/load_noData.png" />');
			$("#locTopBox").hide();

		} else if(dNum != 1 && (results == null || results.length == 0)) {
			$('.weui-loadmore').html('已经到底了');

		} else {
			for(var item in results) {
				var mytaskHtml = $('#loadGoodsList').render(results[item]);
				$(".goods-list-con").append(mytaskHtml)
			}

			//加载定位信息
			var track = res.track;
			if(track == null) {
				$(".locAddr").text("暂无定位信息");
				$(".locTime").text("");
			} else {
				$(".locAddr").text(track.reportLoaction);
				$(".locTime").text(track.loaclTime);
			}

			if(!$(this).hasClass("noClick")) {
				$(this).addClass("noClick");
				$("#locationBtn").addClass("roundCss");
				getPosition();
			} else {
				$.toast("定位次数太频繁，请稍候重试", "text");
			}

			dLoading = true;
		}
		$('.weui-loadmore').hide();

	});
}

//搜索
function searchMytaskList() {
	$('.goods-list-con').html('');
	$('.weui-loadmore').show();
	dNum = 1;
	getloadGoodsList({
		num: dNum,
		size: pageSize,
		likeString: $("#searchData").val()
	})
}

//调用扫一扫接口
function uploadScanCode(type) {
	var t = type;
	wx.scanQRCode({
		needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
		scanType: ["qrCode", "barCode"], // 可以指定扫二维码还是一维码，默认二者都有
		success: function(res) {
			var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
			var code = '';

			//要判断是新条码还是旧条码
			if(result.indexOf('scan') > -1) { //是新条码
				var start = result.lastIndexOf('/') + 1;
				var end = result.lastIndexOf('?');
				code = result.substring(start, end);
				getScanCode(code, t);

			} else if(result.indexOf('code') > -1) { //旧条码	
				code = getWXParam('code', result);
				getScanCode(code, t);
			} else {
				$.toast("无效条码", "cancel");
			}

		}
	});
}

//获取扫码返回信息中的参数
function getWXParam(name, url) {
	var num = url.indexOf('?');
	var str = url.substring(num + 1);
	var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)');
	var result = str.match(reg);
	return result ? decodeURIComponent(result[2]) : null;
}

//扫码后调用接口
function getScanCode(code, type, obj) {
	var $this = $(obj);
	/*if(kind){//点击卸载按钮
		$.confirm({
		    title: '',
			text: '确定卸货吗？',
			onOK: function () {
			    confirmSubmit();
			}
		});
	}else{//扫码卸载
		confirmSubmit();
	}*/
	confirmSubmit();

	function confirmSubmit() {
		var url = "";
		if(type == 1) {
			url = "/mobile/driver/scan/load"
		} else {
			url = "/mobile/driver/scan/unload"
		}

		var baseValue = {
			"barcode": code
		}
		EasyAjax.ajax_Post_Json({
			url: url,
			data: JSON.stringify(baseValue)
		}, function(res) {
			if(type == 1) { //扫码装货
				if(res.success && res.barcode !== "") { //未上传绑定图片		 	
					window.location.href = "uploadCodePhoto.html?barcode=" + code;
				} else {
					$.toast("装货成功", function() {
						window.location.reload();
					});
					//$.toast("装货成功");

				}
			} else { //扫码卸货
				if(res.success) {
					$.modal({
						title: "",
						text: "卸货成功，是否继续上传回单？",
						buttons: [{
								text: "不了，谢谢",
								className: "default",
								onClick: function() {
									window.location.reload();
								}
							},
							{
								text: "继续上传",
								onClick: function() {
									window.location.href = "uploadReceipt.html?barcode=" + res.barcode;
								}
							}
						]
					});
					/*$.toast("卸货成功",function(){
						if(kind){
							$this.parents(".goodsPanel").remove();
						}else{						
							window.location.reload();	
						}	
					});	*/
				}
			}

		})
	}

}

//更新地理位置
$("#locTopBox").on("click", function() {
	if(!$(this).hasClass("noClick")) {
		$(this).addClass("noClick");
		$("#locationBtn").addClass("roundCss");
		getPosition();
	} else {
		$.toast("定位次数太频繁，请稍候重试", "text");
		return;
	}
});

//获取地理位置
function getPosition() {
	wx.ready(function() {
		wx.getLocation({
			type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
			success: function(res) {
				var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
				var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
				var speed = res.speed; // 速度，以米/每秒计
				var accuracy = res.accuracy; // 位置精度

				geocoder(latitude, longitude);

			},
			fail: function(res) {
				$.toast("更新地址失败", 'cancel');
				$("#locTopBox").removeClass("noClick");
				$("#locationBtn").removeClass("roundCss");
			}
		});
	});

}

//反向地理编码
function geocoder(latitude, longitude) {
	var wgs84togcj02 = coordtransform.wgs84togcj02(longitude, latitude);
	wgs84togcj02 = wgs84togcj02 + "";
	wgs84togcj02 = wgs84togcj02.split(",");
	var longitude = wgs84togcj02[0];
	var latitude = wgs84togcj02[1];

	AMap.service('AMap.Geocoder', function() { //回调函数
		//逆地理编码
		var lnglatXY = [longitude, latitude]; //地图上所标点的坐标
		var geocoder = new AMap.Geocoder();
		geocoder.getAddress(lnglatXY, function(status, result) {
			if(status === 'complete' && result.info === 'OK') {
				//获得了有效的地址信息:

				var addressTxt = result.regeocode.formattedAddress + "(" + result.regeocode.addressComponent.district + result.regeocode.addressComponent.township + result.regeocode.addressComponent.street + result.regeocode.addressComponent.streetNumber + ")";
				if(addressTxt == "") {
					$.toptip("定位地址不能为空", 'error');
				} else {
					updateLocation(longitude, latitude, addressTxt);
				}
			} else {
				//获取地址失败
				$.toast("更新地址失败", 'cancel');
				$("#locTopBox").removeClass("noClick");
				$("#locationBtn").removeClass("roundCss");
			}
		});
	});
}

//更新定位
function updateLocation(y, x, addr) {
	var baseValue = {
		"reportLoaction": addr,
		"longitude": y,
		"latitude": x
	}
	EasyAjax.ajax_Post_Json({
		url: "mobile/driver/loaction",
		data: JSON.stringify(baseValue)
	}, function(res) {
		if(res.success) {
			lotteryKey = res.lotteryKey;
			if(!isNull(lotteryKey)) {
				$(".raffle").show();
				$(".goods-list-con").css("margin-top", 30);
			}
			$("#locationBtn").removeClass("roundCss");
			$(".locAddr").text(addr);
			$(".locTime").text("刚刚");
			setTimeout(function() {
				$("#locTopBox").removeClass("noClick");
			}, 10000);
		}
	})

}

//使用微信内置地图查看位置接口
function openLocation(lat, lon, text) {
	if(text == null) {
		text = '';
	}
	wx.openLocation({
		latitude: lat, // 纬度，浮点数，范围为90 ~ -90
		longitude: lon, // 经度，浮点数，范围为180 ~ -180。
		name: text, // 位置名
		address: text, // 地址详情说明
		scale: 20 // 地图缩放级别,整形值,范围从1~28。默认为最大
	});
}

function chooseReward(index) {
	chooseIndex = index;
	switch(index) {
		case 0:
			awardType = 1;
			break;
		case 1:
			awardType = 3;
			break;
		case 2:
			awardType = 2;
			break;
	}
	var baseValue = {
		"lotteryKey": lotteryKey,
		"awardType": awardType
	}
	EasyAjax.ajax_Post_Json({
		url: "sys/activity/loaction/lottery",
		data: JSON.stringify(baseValue)
	}, function(res) {
		if(res.success) {
			if(index == 1) {
				$.alert("该功能暂未上线，敬请期待", "提示");
				return;
				var picId = res.result;
				var basePath = "../../images/";
				var suffix = ".gif";
				$("#showPic").attr("src", basePath + "bellepic" + picId + suffix);
				$(".raffle").hide();
				$(".goods-list-con").hide();
				$(".showBelle").show();
				setTimeout(function() {
					$(".goods-list-con").show();
					$(".showBelle").hide();
					$(".goods-list-con").css("margin-top", 0);
				}, 10000);
				return;
			}
			$(".chooseReward").hide();
			$(".showResult").show();
			$(".title").html("奖励领取成功");
			switch(index) {
				case 0:
					var money = res.result / 100;
					$(".resultchildtitle").html("红包现金奖励");
					$(".showMoney").html("￥" + money);
					break;
				case 2:
					var integral = res.result;
					$(".resultchildtitle").html("积分奖励");
					$(".showMoney").html(integral + "积分");
					break;
			}
		}
	})
}

function look() {
	switch(chooseIndex) {
		case 0:
			window.location.href = "./myWallet.html";
			break;
		case 2:
			window.location.href = "./myIntegral.html";
			break;
	}
}

function isNull(text) {
	return !text && text !== 0 && typeof text !== "boolean" ? true : false;
}