var bindCode = _common.getUrlParam('bindCode');
var taskId   = _common.getUrlParam('taskId');
var pageGroupid = _common.getUrlParam('groupId'); //项目组id
var fromPage = _common.getUrlParam('fromPage'); //有这个值，表明来自我要发货页
var addCustomType = sessionStorage.getItem("customerType"); //1是收货，2是发货

if(!isWeixin()){
	$('#navBarIn').hide();
}

$('#radio-personal').on('click',function(){
	$('.pt-contact').hide();
})
$('#radio-pt').on('click',function(){
	$('.pt-contact').show();
})

$("#sendLabel").on('click',function(){
	$(".changePepleType .weui-label").html("<span>*</span>发货人");
	$("#companyName").attr("placeholder","请输入发货人");
	$('#arrivalDay,#arrivalHour').val('');
	$('.project-label-w').hide().next().hide();
});

$("#receiveLabel").on('click',function(){
	$(".changePepleType .weui-label").html("<span>*</span>收货人");
	$("#companyName").attr("placeholder","请输入收货人");
	$('.project-label-w').show().next().show();
});

if(addCustomType == 1){
	$("#receiveLabel").trigger('click');
}

//获取项目组名称接口
getItemName();
function getItemName(){	
    EasyAjax.ajax_Post_Json({
        url:'mobile/mine/group/list'
    },function(data){
        var dataArr = data.groups;
        if(dataArr !== null && dataArr.length !== 0){
        	var groupHtml = $('#groupsName').render(dataArr);
        	$('#groupList').append(groupHtml);           	
        }
    });
}


/* =============地图相关功能=============== */

function geocoder(searchText) {
    var geocoder = new AMap.Geocoder({
        city: "", //城市，默认：“全国”
        radius: 1000 //范围，默认：500
    });

    //地理编码,返回地理编码结果
    geocoder.getLocation(searchText,function(status,result){
	    if(status=='complete'&&result.geocodes.length){
	        $('#lng').val(result.geocodes[0].location.lng);
	        $('#lat').val(result.geocodes[0].location.lat);
	       	console.log('经度：'+result.geocodes[0].location.lng);
	       	console.log('纬度：'+result.geocodes[0].location.lat);
	    }else{
	    	//获取坐标失败
	    	$('#lng').val('');
	        $('#lat').val('');
	    }
    })
}
/*** 根据地址定位+获取经纬度结束 ***/
/* =============地图相关功能结束=============== */

//省市区必填
function cityRequire(){
	var $province = $('#province').val();
	var $city = $('#city').val();
	if( $province !== "" &&  $city !== "" ){
		return true;
	}
	return false;
}

//填写省市区
$('#tipinput').keydown(function(ev){
	if( !cityRequire() ){
		$.toptip('请先输入省市区(县)');
		return false;
	}
})

//获取坐标
$('#tipinput').on('input',function(){
	var p = $('#province').val(),
		c = $('#city').val(),
		d = $('#district').val(),
		detail = $(this).val();
		geocoder(p+c+d+detail);
})

//省市区改变时获取坐标
$('#province,#city,#district').change(function(){
	var p = $('#province').val(),
		c = $('#city').val(),
		d = $('#district').val(),
		detail = $(this).val();
		geocoder(p+c+d+detail);
})

$("#showTooltips").click(function() {
    var groupid= $.trim($('#groupList').val());
    var companyName = $.trim($('#companyName').val());
    var contacts = $.trim($('#contacts').val());
    var contactNumber = $.trim($('#contactNumber').val());
    var arrivalDay = $('#arrivalDay').val();
    var arrivalHour = $('#arrivalHour').val();
    var address = $.trim($('#tipinput').val());
    var lng = $('#lng').val();
    var radius = $.trim($('#radius').val());
    var grateSatus = $('#switchCP').val();
    //console.log('经纬度：'+lng);
    //点击围栏开
    if($('#switchCP').is(':checked')){
    	$('#switchCP').val('1');
    }
    
    if(groupid.length == 0 && $("input.weui-check[name='radio1']:checked").val() == 1){//1：项目组;2：个人；
    	$.toptip('项目组名不能为空');
    	return;
    }else if($("input.weui-check[name='radio1']:checked").val() == 2){
    	$('#groupList').val("");
    }
    
    if(companyName.length == 0){
    	if($("input.weui-check[name='radio2']:checked").val() == 1){//1、收货地址；2、发货地址
    		$.toptip('收货人不能为空');
    	}else{
    		$.toptip('发货人不能为空');
    	}   	
    	return;
    }
    if(contacts.length == 0){
    	$.toptip('联系人不能为空');
    	return;
    }
    if(contactNumber.length == 0 ){  //手机号不为空时，需要验证手机号码格式
    	$.toptip('手机号码不能为空');
    	return;
    }
    if(!/1[3|4|5|7|8|9]\d{9}$/.test(contactNumber)){
    	$.toptip('手机号码格式有误');
    	return;
    }
	if( !cityRequire() ){
		$.toptip('省市区(县)不能为空');
		return;
	}
	if(address.length == 0){
    	$.toptip('收货详细地址不能为空');
    	return;
   	}
	if(arrivalDay.length > 0 && arrivalHour.length == 0){
		$.toptip('要求到货时间点不能为空');
		return;
	}
	if(arrivalHour.length > 0 && arrivalDay.length == 0){
		$.toptip('要求到货天数不能为空');
		return;
	}
	if(lng.length == 0){
		$.toptip('收货地标不能为空,请稍后再试');
		return;
	}
	// 半径必填验证
	var radiusreg=/^([1-9][0-9]*)+(.[0-9]{1,2})?$/;
	if(grateSatus==1){
		if(!radiusreg.test(radius)){
			$.toptip('半径不能为空只能为整数或保留两位小数');
			return;
		}
	}
    submitForm();
});
    

//提交表单
function submitForm(){
	var baseValue = {
		belongType   : $("input.weui-check[name='radio1']:checked").val(),
		type         : $("input.weui-check[name='radio2']:checked").val(),
	    companyName  : utf16toEntities($("#companyName").val()),
	    contacts     : utf16toEntities($("#contacts").val()),
	    contactNumber: $("#contactNumber").val(),
	    tel          : $("#tel").val(),
	    address      : utf16toEntities($('#tipinput').val()),
	    arrivalDay    : $('#arrivalDay').val(),
	    arrivalHour   : $('#arrivalHour').val(),
	    province     : $('#province').val(),
	    city		 : $('#city').val(),
	    district     : $('#district').val()==""?"":$('#district').val(),
	    longitude    : $('#lng').val(),
	    latitude     : $('#lat').val(),
	    //grateSatus   : $('#switchCP').val(),
	    //radius       : $('#radius').val(),
	    groupId      : $("#groupList").val()
	}

	EasyAjax.ajax_Post_Json({
		url:'/mobile/customer/add',
		data:JSON.stringify(baseValue)
	},function(res){
		var pageName = sessionStorage.getItem("customList");
		if(pageName){
			window.location.href = _common.version("customList.html");
			sessionStorage.removeItem("customList");
		}else{
			window.location.href=_common.version("chooseCustom.html?bindCode="+bindCode+'&taskId='+taskId+'&dgroupid='+pageGroupid+'&fromPage='+fromPage);
		}
	})
}



