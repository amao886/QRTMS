var addressId = _common.getUrlParam('customerId');
var fromPage = _common.getUrlParam('fromPage'); //有这个值，表明来自我要发货页
var bindCode = _common.getUrlParam('bindCode');
var taskId = _common.getUrlParam('taskId');

if(!isWeixin()){
	$('#navBarIn').hide();
}

//获取项目组信息
getItemName();

//获取地址信息
getAddressInfo();

$('#radio-personal').on('click',function(){
	$('.pt-contact').hide().find('#groupList').val('');
})
$('#radio-pt').on('click',function(){
	$('.pt-contact').show();
})

$("#sendLabel").on('click',function(){
	$(".changePepleType .weui-label").html("<span>*</span>发货人");
	$("#companyName").attr("placeholder","请输入发货人");
	$('.project-label-w').hide().next().hide();
});

$("#receiveLabel").on('click',function(){
	$(".changePepleType .weui-label").html("<span>*</span>收货人");
	$("#companyName").attr("placeholder","请输入收货人");
	$('.project-label-w').show().next().show();
});


//获取项目组接口
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


//获取地址接口信息
function getAddressInfo(){
	EasyAjax.ajax_Post_Json({
		url:'mobile/customer/choose/'+addressId
	},function(res){
		var res = res.customer;
		if(res.groupId == 0){ //个人
			$('#personal').prop('checked','checked');
			$('.pt-contact').hide();
			
		}else{//项目组			
			$('#projectTeam').prop('checked','checked');
			$('#groupList').val(res.groupId);
		}
		if(res.type == 2){ //发货地址
			$('#sendAddress').prop('checked','checked');
			
		}else{ //收货地址
			$('#receiveAddress').prop('checked','checked');
			$(".changePepleType .weui-label").html("<span>*</span>收货人");
			$("#companyName").attr("placeholder","请输入收货人");
			$('.project-label-w').show().next().show();
			$('#arrivalDay').val(res.arrivalDay);
			$('#arrivalHour').val(res.arrivalHour);
		}
		$('#companyName').val(res.companyName);
		$('#contacts').val(res.contacts);
		$('#contactNumber').val(res.contactNumber);
		$('#tel').val(res.tel);
		if(res.province){
			geocoder(res.province+res.city+res.district+res.address);
			$('#province').val(res.province);
			selectName('province',res.city);
			selectName('city',res.district);
		}
		$('#tipinput').val(res.address);
		$('#lng').val(res.longitude);
		$('#lat').val(res.latitude);
		
 	});
}



/* =============地图相关功能=============== */

//转码定位
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
	    }else{
	    	//获取坐标失败
	    	$('#lng').val('');
	        $('#lat').val('');
	    }
    })
}
/* =============地图相关功能结束=============== */

//填写省市区
$('#tipinput').keydown(function(ev){
	if( !cityRequire() ){
		$.toptip('请先输入省市区(县)');
		return false;
	}
})

//点获取定位地址按钮搜索
$('#tipinput').on('input',function(){
	var p = $('#province').val(),
		c = $('#city').val(),
		d = $('#district').val(),
		detail = $('#tipinput').val();	
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
	var adrBelong = $('input[name=radio1]:checked').val();  //地址所属
	var adrType = $('input[name=radio2]:checked').val();    //地址类型
    var groupid= $.trim($('#groupList').val());     //项目组id
    var companyName = $.trim($('#companyName').val()); //发货人
    var contacts = $.trim($('#contacts').val()); //联系人
    var contactNumber = $.trim($('#contactNumber').val()); //联系电话
    var address = $.trim($('#tipinput').val()); //详细地址
	var lng = $('#lng').val();  //地址坐标值
	var arrivalDay = $('#arrivalDay').val();
    var arrivalHour = $('#arrivalHour').val();
	
	if(!adrBelong){
		$.toptip('地址所属不能为空');
    	return;
	}
	
	if(adrBelong == 1 && (!groupid)){
		$.toptip('项目组不能为空');
    	return;
	}
	
	if(!adrType){
		$.toptip('地址类型不能为空');
    	return;
	}
	if(adrType == 2){ //发货信息没有要求到货时间
		$('#arrivalDay,#arrivalHour').val('');
	}
    if(companyName.length == 0){
    	$.toptip('发货人不能为空');
    	return;
    }
    if(contacts.length == 0){
    	$.toptip('联系人不能为空');
    	return;
    }
    if(contactNumber.length ==0){
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
		$.toptip('详细地址不能为空');
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

  	submitForm();
});


//提交表单
function submitForm(){
	var baseValue = {
	    id           : addressId,
	    belongType   : $("input.weui-check[name='radio1']:checked").val(),
		type         : $("input.weui-check[name='radio2']:checked").val(),
	    groupid      : $("#groupid").val(),
	    companyName  : utf16toEntities($('#companyName').val()),
	    contacts     : utf16toEntities($('#contacts').val()),
	    contactNumber: $("#contactNumber").val(),
	    tel          : $("#tel").val(),
	    province     : $('#province').val(),
	    city		 : $('#city').val(),
	    district     : $('#district').val()==""?"":$('#district').val(),
	    address      : utf16toEntities($('#tipinput').val()),
	    longitude    : $('#lng').val(),
	    latitude     : $('#lat').val(),
	    arrivalDay    : $('#arrivalDay').val(),
	    arrivalHour   : $('#arrivalHour').val()
	}
	EasyAjax.ajax_Post_Json({
		url:'mobile/customer/update',
		data:JSON.stringify(baseValue)
	},function(res){
		var pageName = sessionStorage.getItem("customList");
		if(pageName){//客户列表页面进来
			window.location.href=_common.version("customList.html");
			sessionStorage.removeItem("customList");
		}else{
			var id = $("#groupid").val() || '';
			window.location.href=_common.version("chooseCustom.html?bindCode="+bindCode+'&taskId='+taskId+'&dgroupid='+id+'&fromPage='+fromPage);
		}
		
	})
}

//省市区必填
function cityRequire(){
	var $province = $('#province').val();
	var $city = $('#city').val();
	if( $province !== "" &&  $city !== "" ){
		return true;
	}
	return false;
}
