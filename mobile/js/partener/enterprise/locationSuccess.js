var qrcode = _common.getUrlParam('qrcode');

var lotteryKey = '';
var chooseIndex = -1;

//初始化页面数据
getBarCodeInfo();

//获取订单详情数据接口
function getBarCodeInfo() {
    $.showLoading("数据加载中");
	EasyAjax.ajax_Post_Json({
		url: 'enterprise/order/detail/code/' + qrcode
	}, function(res) {
        $.hideLoading();
		if(!res.success) {
			$.toast("订单详情加载失败，请稍后重试", "text");
		} else {
			var qrcodeId = res.reslut.id;
			var listDataHtml = $('#listData').render(res.reslut);
			$('.receipt-wrapper').html(listDataHtml);

			//显示继续扫码按钮
			$('.weui-footer').show();

            getCurrentLocation(qrcodeId);


            //点击继续扫码
            $('#scanCode').click(function() {
                scanCode();
            })
            //点击上传回单
            $('#uploaderRecipt').click(function() {
                window.location.href = _common.version('../../../view/partener/enterprise/uploadReceipt.html?barcode=' + qrcodeId);
            })

		}
	});
}

function getCurrentLocation(qrcodeId) {
    $.showLoading("正在定位中");
	//获取地理位置
	wx.ready(function() {
		//console.log('获取地理位置');
		wx.getLocation({
			type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
			success: function(res) {
				try{
					var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
					var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
					geocoder(latitude, longitude, qrcodeId);
				}catch (e) {
					$.hideLoading();
					//alert(e);
				}
			},
			fail: function(res) {
				$.hideLoading();
				$.modal({
				  text: "定位失败，请检查GPS是否启用后重试？",
				  buttons: [
					{ text: "取消", className: "default"},
					{ text: "重新获取", onClick: function(){ getCurrentLocation(qrcodeId);} },
				  ]
				});
				//window.location.href = _common.version('../../../view/partener/locationErr.html');
			}
		});
	});
}

//反向地理编码
function geocoder(latitude, longitude, id) {
	var wgs84togcj02 = coordtransform.wgs84togcj02(longitude, latitude);
	longitude = wgs84togcj02[0];
	latitude = wgs84togcj02[1];
	AMap.service('AMap.Geocoder', function() { //回调函数
		//逆地理编码
		var lnglatXY = [longitude, latitude]; //地图上所标点的坐标
		var geocoder = new AMap.Geocoder();
		geocoder.getAddress(lnglatXY, function(status, result) {
			if(status === 'complete' && result.info === 'OK') {
				//获得了有效的地址信息:
				$('#address').text(result.regeocode.formattedAddress + "(" + result.regeocode.addressComponent.district + result.regeocode.addressComponent.township + result.regeocode.addressComponent.street + result.regeocode.addressComponent.streetNumber + ")");
				//定位完成后自动上报位置
				var addressTxt = $('#address').text();
				if(addressTxt == "") {
					$.toptip("定位地址不能为空", 'error');
				} else {
					uploadPosition({
						orderKey: id,
						latitude: latitude,
						longitude: longitude,
						location: addressTxt
					});
				}
			} else {
				//获取地址失败
				$.hideLoading();
				$.modal({
					text: "获取地址失败，需要重新获取地址信息吗？",
					buttons: [
						{ text: "取消", className: "default"},
						{ text: "重新获取", onClick: function(){ getCurrentLocation(id);} },
					]
				});
				//$.toast("获取地址失败", 'cancel');
				//window.location.href = _common.timeStamp('../../../view/partener/locationErr.html');
			}
		});
	});
}

//ajax上报经纬度
function uploadPosition(options) {
	var baseValue = {
		orderKey: options.orderKey,
		longitude: options.longitude,
		latitude: options.latitude,
		location: options.location
	}
	EasyAjax.ajax_Post_Json({
		url: 'enterprise/order/loaction',
		data: JSON.stringify(baseValue)
	}, function(res) {
		if(res.success) {
			lotteryKey = res.lotteryKey;
			if(!isNull(lotteryKey)) {
				$(".raffle").show();
			}
			$.toast('定位成功');
			$.hideLoading();
			$('.head-tips').show();
		} else {
			alert(111);
			$.hideLoading();
			$('.head-tips').show().html('<div class="success-tip">定位失败</div>')
		}

	})
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
				var basePath = "../../../images/";
				var suffix = ".gif";
				$("#showPic").attr("src", basePath + "bellepic" + picId + suffix);
				$(".raffle").hide();
				$(".showBelle").show();
				var timer = window.setTimeout(function() {
					$(".showBelle").hide();
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
			window.location.href = "../myWallet.html";
			break;
		case 2:
			window.location.href = "../myIntegral.html";
			break;
	}
}

function isNull(text) {
	return !text && text !== 0 && typeof text !== "boolean" ? true : false;
}