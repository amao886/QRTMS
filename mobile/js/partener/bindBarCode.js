var delivername = _common.getUrlParam('delivername');  //发货人名称
var bindCode=_common.getUrlParam('bindCode');
var groupId=_common.getUrlParam('groupId');  //项目组id
var customerId=_common.getUrlParam('customerId');  //客户id

//存储的信息用于goodsDetail.html,清缓存
sessionStorage.removeItem('submitFormData');
sessionStorage.removeItem('sendGoodsPageStore');
console.log('bindBarCode缓存：'+sessionStorage.getItem('submitFormData'))
console.log('发货页缓存：'+sessionStorage.getItem('sendGoodsPageStore'))

//任务单号绑定
$("#barcode").val(bindCode);
var sendCustomID = sessionStorage.getItem("sendCustomID")
var receiveCustomID = sessionStorage.getItem("receiveCustomID");       
//收货信息
if (receiveCustomID) {//选择客户信息后
    getCurtomerInf(receiveCustomID);
}
//发货信息
if (sendCustomID) {//选择客户信息后
    getCurtomerInf(sendCustomID);
}   

var scan_flag = $.trim($("#barcode").val()) != "" ? true : false ;//用来判断是
console.log('scan_flag:'+scan_flag);

$(function() {
	var max = $('#count_max').text();
	$('#textarea').on('input',function() {
			var text = $(this).val();
			var len = text.length;
			$('#count').text(len);
			if (len >= max) {
				this.value = this.value.substr(0, max);
				$(this).closest('.weui-cell__bd').addClass('color-danger');
				$(".weui-textarea-counter").css('color','#f6383a');
				$('#count').text(max);
			} else {
				$(this).closest('.weui-cell__bd').removeClass('color-danger');
				$(".weui-textarea-counter").css('color','#B2B2B2');
			}
	});
})

//点击下一步按钮
$("#showTooltips").click(function() {	
	var barcode = $.trim($("#barcode").val());  //任务单号
	if (barcode == '') {
		$.toptip("任务单号不能为空");
		return;
	}
	
	var sendInf = $('#sendInf').children().length; //收货客户
	if(sendInf == 0){
		$.toptip("发货信息不能为空");
		return;
	}
	
	var receiveInf = $('#receiveInf').children().length; //收货客户
	if(receiveInf == 0){
		$.toptip("收货信息不能为空");
		return;
	}
		
	var str=$('#deliveryNumber').val().trim();
	var reg = /^[a-zA-Z0-9]{1,30}$/	
	if(str == '') {
		$.toptip("送货单号不能为空");
		return;
	}else if(!reg.test(str)){
		$.toptip("送货单号应为数字加英文，且长度不能大于30");
	    return;
	}
	
	var txtReg = /^[`<>"']$/ig;
	var v = $("#textarea").val().replace(txtReg,'');
	if (v != "" && v.length > 50) {
		$.toptip("任务摘要长度不能大于50");                                                                                                                                                                            
		return;
	}
	
	if(scan_flag){
	    position(); //有bindCode
	  
	}else{  //没有bindCode,手动添加任务单号，功能已废弃
	    var barcode=$.trim($("#barcode").val());
	    ajaxPost(barcode);
	}		
});

//收货客户接口 
function getCurtomerInf(cid) {
    customerId = cid || customerId;
    var baseValue = {
        customerId: customerId,
        groupId: groupId
    }
    EasyAjax.ajax_Post_Json({
        url: '/mobile/customer/choose/' + customerId,
        data: JSON.stringify(baseValue)
    }, function (res) {
        var data = res.customer
        var _type = data.type
        var customListHtml = $('#showCustomInf').render(data);

        if (_type == 1) {//收货
            $("#receiveSelect").html(data.companyName);
            $('#receiveInf').html(customListHtml);
            sessionStorage.setItem("receiveCustomID", data.id);            
            //新增要求到货时间
            $('#arrivalDay').val(data.arrivalDay);
            $('#arrivalHour').val(data.arrivalHour);
            
        } else if (_type == 2) {//发货
            $("#sendSelect").html(data.companyName);
            $('#sendInf').html(customListHtml);
            sessionStorage.setItem("sendCustomID", data.id);
        }

    });
}

//获取地理位置
function position(){
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
					console.log('走到这一步。。。。')
		      		//submitForm();
		      		var submitFormData = {
		      			address       : $("#address").val(),
						sendCustomerId: $("#sendId").val(),
					    customerId    : $("#receiveId").val(),
					    deliveryNumber: $("#deliveryNumber").val(),
					    latitude      : $("#latitude").val(),
					    longitude     : $("#longitude").val(),
					    orderSummary  : utf16toEntities($("#textarea").val()),
						barcode       : $("#barcode").val(),
					    arrivalDay    : $("#arrivalDay").val(),
					    arrivalHour   : $("#arrivalHour").val()
		      		}
		      		sessionStorage.setItem('submitFormData',JSON.stringify(submitFormData));
		      		window.location.href = _common.version('goodsDetail.html');
				}
		    }else{
		       //获取地址失败
		      $.toast("获取地址失败",'cancel');
		      window.location.href = _common.version('../../view/partener/locationErr.html');
		    }
		});  
	});
}

//提交
function submitForm(){
	var baseValue = {
		address       : $("#address").val(),
		sendCustomerId: $("#sendId").val(),
	    customerId    : $("#receiveId").val(),
	    deliveryNumber: $("#deliveryNumber").val(),
	    latitude      : $("#latitude").val(),
	    longitude     : $("#longitude").val(),
	    number        : $("#number").val(),
	    orderSummary  : utf16toEntities($("#textarea").val()),
	    volume        : $("#volume").val(),
		barcode       : $("#barcode").val(),
	    weight        : $("#weight").val(),
	    arrivalDay    : $("#arrivalDay").val(),
	    arrivalHour   : $("#arrivalHour").val()
	}
	EasyAjax.ajax_Post_Json({
		url:'/mobile/barCode/add',
		data:JSON.stringify(baseValue)
	},function(res){
		//提交成功
		$.toast(res.message);
		if(res.success){
			sessionStorage.removeItem("customerType")
            sessionStorage.removeItem("sendCustomID");
            sessionStorage.removeItem("receiveCustomID");
            window.location.href = _common.version('../../view/partener/bindCodeSuccess.html');
		}		
	})
}


//异步请求条码
function ajaxPost(barcode){
		EasyAjax.ajax_Post_Json({
			url:'/mobile/barCode/verifyAjax/'+barcode
		},function(res){
			if(res.code=="200"){
			  	position();
			}else{
			    $.toast(res.message,'text');
			}
      });	 
}

//选择客户
function chooseCustom(obj, type) {
    var id = $('#projectTeam').val(); //dgroupid:项目组id
    sessionStorage.setItem("customerType", type)
    window.location.href = _common.version('chooseCustom.html?bindCode='+bindCode+'&customerId='+customerId+'&groupId='+groupId);    
}