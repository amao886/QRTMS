var barCode = _common.getUrlParam('bindCode');

//点击首页跳转
$('#toIndex').click(function(){
	window.location.href = _common.version('../../home.html?jstatus='+0);
})

//初始化页面数据
getBarCodeInfo();

//获取条码数据
function getBarCodeInfo(){
	EasyAjax.ajax_Post_Json({
		url:'mobile/barCode/getBindCache/'+barCode
	},function(data){
		if(data.success == true){
			$('.success-tip').show();
		}
		//渲染图片
		var imgShowHtml = $('#imgShow').render(data.results);
		$('#picShow').html(imgShowHtml);
		
		//渲染任务单
		var codeListHtml = $('#codeList').render(data.results);
		$('#billDetailCon').html(codeListHtml);
		
		var waybillId = $('#detailConTitle').attr('uid');
		
		//点击查看其它任务
		$('#toMyTask').click(function(){
			window.location.href = _common.version('../../view/partener/myTask.html');
		})
		
		//点击跳到任务详情
		$('#toMyTaskDetail').click(function(){
			window.location.href = _common.version('../../view/partener/myTaskDetails.html?waybillId='+waybillId);
		})
		
		//点击异常上报
		$('#exceptionReport').click(function(){
			window.location.href = _common.version('../../view/partener/abnormal.html?waybillId='+waybillId);
		})
		
		//点击上传回单
		$('#receipt').click(function(){
			window.location.href = _common.version('../../view/partener/upload.html?waybillId='+waybillId);
		})
		
		//点击继续扫码
		$('#scanCode').click(function(){
			scanCode();
		})
		//确认到货
		$('#isConfirm').click(function(){
			$.confirm({
			  	title: '提示',
			  	text: '是否确认到货及未审核的回单都设置为合格？',
			  	onOK: function () {
			    	EasyAjax.ajax_Post_Json({
						url:'mobile/trace/confirmArrive/'+waybillId
					},function(res){
						$('#isConfirm').hide();
						$.toast("操作成功");	
					})
			  	}
			});		
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
			        	geocoder(latitude,longitude,waybillId);
			    },
				fail: function (res) {
					window.location.href = _common.version('../../view/partener/locationErr.html');
				}
			}); 
		});
			
	});
}

//反向地理编码
function geocoder(latitude,longitude,waybillId) {
    var wgs84togcj02 = coordtransform.wgs84togcj02(longitude, latitude);
    longitude=wgs84togcj02[0];
    latitude=wgs84togcj02[1];
    $('#address').attr('data-latitude',latitude);
    $('#address').attr('data-longitude',longitude);
	AMap.service('AMap.Geocoder',function(){//回调函数
        //逆地理编码
		var lnglatXY=[longitude, latitude];//地图上所标点的坐标
		var geocoder = new AMap.Geocoder();
		geocoder.getAddress(lnglatXY, function(status, result) {
		    if (status === 'complete' && result.regeocode) {
		        //获得了有效的地址信息:
		        $('#address').text(result.regeocode.formattedAddress+"("+result.regeocode.addressComponent.district+result.regeocode.addressComponent.township+result.regeocode.addressComponent.street+result.regeocode.addressComponent.streetNumber+")");
		        //定位完成后自动上报位置
		        var addressTxt = $('#address').text();
		        var city = result.regeocode.addressComponent.city;
		        if(addressTxt == ""){
					$.toptip("定位地址不能为空", 'error');
				}else{
		      		uploadPosition({
						barCode   : barCode,
						latitude  : $('#address').attr('data-latitude'),
						longitude : $('#address').attr('data-longitude'),
						locations : addressTxt,
						city      : city,
						waybillid : waybillId	
					});					
				}
		    }else{
		       //获取地址失败
		      $.toast("获取地址失败",'cancel');
		      window.location.href = _common.timeStamp('../../view/partener/locationErr.html');
		    }
		});  
	});
}

//ajax上报经纬度
function uploadPosition(options){
	var baseValue = {
		wbarcode  : options.barCode,
	    latitude  : options.latitude,
	    locations : options.locations,
	    longitude : options.longitude,
	    city      : options.city,
	    waybillid : options.waybillid
	}
	EasyAjax.ajax_Post_Json({
		url:'mobile/uploadPosition',
		data:JSON.stringify(baseValue)
	},function(res){
		$.toast('定位成功');
		$('#locationTip').show();
	})
}


