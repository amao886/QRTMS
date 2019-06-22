var barCode = _common.getUrlParam('barcode') || '';

//点击首页跳转
$('#toIndex').click(function(){
	window.location.href = _common.version('../../index.html');
})

//初始化页面数据
getBarCodeInfo();

//获取条码数据
function getBarCodeInfo(){
	var baseValue = {
		"barcode"  : barCode
	}
	EasyAjax.ajax_Post_Json({
		url:'mobile/driver/detail',
		data:JSON.stringify(baseValue)
	},function(data){
		if(data.success == true){
			$('.success-tip').show();
		}
		var userId = data.user.id; //当前用户id
		var driverId = data.result.loadId; //扫码司机id
		
		//渲染图片
		var imgShowHtml = $('#imgShow').render(data.user);
		$('#picShow').html(imgShowHtml);
		
		//渲染任务单
		var codeListHtml = $('#codeList').render(data.result);
		$('#billDetailCon').html(codeListHtml);
		if(userId !== driverId){
			$('#loadBoard').show();
		}
		
		//点击异常上报
		$('#exceptionReport').click(function(){
			window.location.href = _common.version('abnormalCopy.html?barcode='+data.result.barcode);
		})
		
		//点击上传回单
		$('#receipt').click(function(){
			window.location.href = _common.version('uploadReceipt.html?barcode='+data.result.barcode);
		})
		
		//点击继续扫码
		$('#scanCode').click(function(){
			scanCode();
		})
		
		//点击装货上车
		$('#loadBoard').on('click',function(){
			driverLoad();
		})
		
		//获取地理位置
		wx.ready(function(){
			console.log('获取地理位置');
			wx.getLocation({
			    type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
			    success: function (res) {
				        var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
				        var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
				        var speed = res.speed; // 速度，以米/每秒计
				        var accuracy = res.accuracy; // 位置精度
			        	geocoder(latitude,longitude);
			    },
				fail: function (res) {
					window.location.href = _common.version('locationErr.html');
				}
			}); 
		});
			
	});
}

//反向地理编码
function geocoder(latitude,longitude) {
    var wgs84togcj02 = coordtransform.wgs84togcj02(longitude, latitude);
    longitude=wgs84togcj02[0];
    latitude=wgs84togcj02[1];
	AMap.service('AMap.Geocoder',function(){//回调函数
        //逆地理编码
		var lnglatXY=[longitude, latitude];//地图上所标点的坐标
		var geocoder = new AMap.Geocoder();
		geocoder.getAddress(lnglatXY, function(status, result) {
		    if (status === 'complete' && result.info === 'OK') {
		        //获得了有效的地址信息:
		        $('.latestText').text(result.regeocode.formattedAddress+"("+result.regeocode.addressComponent.district+result.regeocode.addressComponent.township+result.regeocode.addressComponent.street+result.regeocode.addressComponent.streetNumber+")");
		        //定位完成后自动上报位置
		        var addressTxt = $('.latestText').text();
		        if(addressTxt == ""){
					$.toptip("定位地址不能为空", 'error');
				}else{
		      		uploadPosition({
		      			barcode         : barCode,
		      			reportLoaction  : addressTxt,
	    				longitude       : longitude,
	    				latitude        : latitude
					});					
				}
		    }else{
		       //获取地址失败
		      $.toast("获取地址失败",'cancel');
		      window.location.href = _common.timeStamp('locationErr.html');
		    }
		});  
	});
}

//ajax上报经纬度
function uploadPosition(options){
	var baseValue = {
		barcode         : options.barcode,
		reportLoaction  : options.reportLoaction,
	    longitude       : options.longitude,
	    latitude        : options.latitude
	}
	EasyAjax.ajax_Post_Json({
		url:'mobile/driver/loaction',
		data:JSON.stringify(baseValue)
	},function(res){
		$.toast('定位成功');
	})
}

//司机装车接口
function driverLoad(){
	var baseValue = {
		barcode  : barCode
	}
	EasyAjax.ajax_Post_Json({
		url:'mobile/driver/scan/load',
		data:JSON.stringify(baseValue)
	},function(res){
		$.toast('装货成功');
		if(res.barcode){
			window.location.href = _common.version("uploadCodePhoto.html?barcode="+barCode);
		}
	})
}
