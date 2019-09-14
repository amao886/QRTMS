var bindCode   = _common.getUrlParam('bindCode');  //任务单条码
var waybillId  = _common.getUrlParam('waybillId'); //任务单id
var customerId;

//清缓存
sessionStorage.removeItem('editBarMsg');
sessionStorage.removeItem('bindBarMsg');

//绑定任务单号
$('#waybillNum').text(bindCode);

//获取收货客户信息
getCustomInfo();

//点击修改物料信息
$('#editGoodsDetail').on('click',function(){
	var bindBarMsg = {
		bindCode : bindCode,
		waybillId : waybillId
	}
	sessionStorage.setItem('bindBarMsg',JSON.stringify(bindBarMsg));
	sessionStorage.removeItem('calcTotal'); //清缓存
	window.location.href = _common.version('goodsDetailEdit.html');
})

//点击确定按钮
$("#showTooltips").click(function() {
	var delivercode    = $.trim($("#delivercode").val()),     //送货单号
		receiverName   = $.trim($('#receiverName').val()),    //收货客户
		deliverName    = $.trim($('#deliverName').val()),     //发货人
		contacts       = $.trim($('#contacts').val()),        //联系人
		contactPhone   = $.trim($('#contactPhone').val()),    //联系电话
		contactAddress = $.trim($('#contactAddress').val()),  //联系地址
		weight         = $.trim($("#weight").val()),          //重量
		volume         = $.trim($("#volume").val()),          //体积
		numberv        = $.trim($("#number").val()),          //数量
		arriveDay      = $.trim($('#arriveDay').val()),       //要求到货天
		arriveHour     = $.trim($('#arriveHour').val());      //要求到货时间
				
	//送货单号
	var reg = /^[a-zA-Z0-9]{1,30}$/	
	if(delivercode == '') {
		$.toptip("送货单号不能为空");
		return;
	}else if(!reg.test(delivercode)){
		$.toptip("送货单号应为数字加英文，且长度不能大于30");
	    return;
	}
	
	//发货人
	if(!deliverName){
		$.toptip("发货人不能为空");
		return;
	}
	//收货客户
	if(!receiverName){
		$.toptip("收货客户不能为空");
		return;
	}
	//联系人
	if(!contacts){
		$.toptip("联系人不能为空");
		return;
	}
	//联系电话
	if(!contactPhone){
		$.toptip("联系电话不能为空");
		return;
	}
	if(!/1[3|4|5|7|8|9]\d{9}$/.test(contactPhone)){
    	$.toptip('手机号码格式有误');
    	return;
    }
	//联系地址
	if(!contactAddress){
		$.toptip("联系地址不能为空");
		return;
	}
	//要求到货时间
	if(arriveDay.length > 0 && arriveHour.length == 0){
		$.toptip('要求到货时间点不能为空');
		return;
	}
	if(arriveHour.length > 0 && arriveDay.length == 0){
		$.toptip('要求到货天数不能为空');
		return;
	}
	//保存
	position();		
});

//保存数据接口
function saveBarCode(){
	var baseValue = {    
	    barcode        : bindCode,  //任务单条码
	    id             : waybillId,  //任务单id          
	    customerid     : customerId,  //客户收货Id
        deliveryNumber : $.trim($("#delivercode").val()), //送货单号 
        receiverName   : $.trim($('#receiverName').val()),  //收货客户
        receiveAddress : $.trim($('#contactAddress').val()),  //收货地址
        contactName    : $.trim($('#contacts').val()),  //联系人
        contactPhone   : $.trim($('#contactPhone').val()), //联系电话
        shipperName    : $.trim($('#deliverName').val()), //发货人
//      weight         : $("#weight").val(), //重量
//      volume         : $("#volum").val(),  //体积
//      number         : $("#number").val(),  //运货数量
        latitude       : $("#latitude").val(),  //纬度
	    longitude      : $("#longitude").val(),  //经度
	    locations      : $("#address").val(),  //定位地址
	    arriveday	   : $.trim($('#arriveDay').val()),
	    arrivehour     : $.trim($('#arriveHour').val())
	}
	EasyAjax.ajax_Post_Json({
		url:'mobile/barCode/bind/waybill',
		data:JSON.stringify(baseValue)
	},function(res){
		//提交成功
		$.toast(res.message);
		window.location.href = _common.version('bindCodeSuccess.html');
	})
}

//收货客户接口
function getCustomInfo(){
	EasyAjax.ajax_Post_Json({
		url:'mobile/wayBill/queryById/'+waybillId
	},function(res){
		var result = res.result;
		customerId = result.customerid; //客户id
		$('#delivercode').val(result.deliveryNumber);  //送货单号
		$('#deliverName').val(result.shipperName);  //发货人
		$('#receiverName').val(result.receiverName); //收货客户
		$('#contacts').val(result.contactName);     //联系人
		$('#contactPhone').val(result.contactPhone);   //联系电话
		$('#contactAddress').val(result.receiveAddress);  //联系地址
		if(sessionStorage.getItem('calcTotal')){ //修改物料页
			var calcTotal = JSON.parse(sessionStorage.getItem('calcTotal'));
			$('#weight').html(calcTotal.weight); //重量
			$('#volum').html(calcTotal.volume);  //体积
			$('#number').html(calcTotal.quantity); //数量 
		}else{
			$('#weight').html(result.weight == 0 ? "": result.weight ); 
			$('#volum').html(result.volume == 0 ? "" : result.volume);
			$('#number').html(result.number == 0 ? "" : result.number);
		}
		$('#arriveDay').val(result.arriveDay == -1 ? "":result.arriveDay);  //天
		$('#arriveHour').val(result.arriveHour == -1 ? "":result.arriveHour);  //点
   });	          
}

//获取地理位置
function position(){
	wx.getLocation({
	    type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
	    success: function (res) {
		        var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
		        var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
	        	geocoder(latitude,longitude);
	    },
		fail: function (res) {
			window.location.href = _common.version('../../view/partener/locationErr.html');
		}
	}); 
	
}

//反向地理编码
function geocoder(latitude, longitude) {
    var wgs84togcj02 = coordtransform.wgs84togcj02(longitude, latitude);
    wgs84togcj02 = wgs84togcj02+"";
    wgs84togcj02 = wgs84togcj02.split(",");
    longitude=wgs84togcj02[0];
    latitude=wgs84togcj02[1];
     $('#latitude').val(latitude);
     $('#longitude').val(longitude);
	AMap.service('AMap.Geocoder',function(){//回调函数
        //逆地理编码
		var lnglatXY=[longitude, latitude];//地图上所标点的坐标
		var geocoder = new AMap.Geocoder();
		geocoder.getAddress(lnglatXY, function(status, result) {
		    if (status === 'complete' && result.info === 'OK') {
		        //获得了有效的地址信息:
		        var addressTxt = result.regeocode.formattedAddress+"("+result.regeocode.addressComponent.district+result.regeocode.addressComponent.township+result.regeocode.addressComponent.street+result.regeocode.addressComponent.streetNumber+")";
		        $("#address").val(addressTxt);
		        if(addressTxt == ""){
					$.toptip("定位地址不能为空", 'error');
				}else{
		      		saveBarCode();			
				}
		    }else{
		       //获取地址失败
		      $.toast("获取地址失败",'cancel');
		      window.location.href = _common.version('../../view/partener/locationErr.html');
		    }
		});  
	});
}
