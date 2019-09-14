var groupId = _common.getUrlParam('groupId');  //查询收货客户参数
var customerId = _common.getUrlParam('customerId'); //查询收货客户参数
var taskId = _common.getUrlParam('taskId'); //任务单id,只要有这个，就是从编辑页过来的
var waybillgid;
var waybillcid;
var dName; //存储任务单中的发货人
var enterTag = 1;

sessionStorage.removeItem('submitFormData');
sessionStorage.removeItem('sendGoodsPageStore');
console.log('bindBarCode缓存：'+sessionStorage.getItem('submitFormData'))
console.log('发货页缓存：'+sessionStorage.getItem('sendGoodsPageStore'))
getItemName();

if(!isWeixin()){
	//浏览器，将手动录入隐藏
	$('.sendTab').hide();
	$('.sendImport').addClass('current').siblings().removeClass('current');
	$('.import-title').show();
	$('#navBarIn').hide(); //底部导航隐藏
	
	//alert('token:'+api_token);
		
	//批量导入有项目组时，显示发货信息
	$('#importProjectTeam').change(function(){
		if($(this).val() && ($(this).val() == 17)){
			$('#aboutCustom').show().find('#importSendMsg').html('');	
		}else{
			$('#aboutCustom').hide().find('#importSendMsg').html('');
		}		
		sessionStorage.removeItem("sendCustomID"); //清除发货客户id
	});
	
	//上传文件
	$('#inputFile')[0].addEventListener('change',function(){
		var fileName = this.files[0].name;
		//alert('文件名:'+fileName);
		var fileType = fileName.substring(fileName.lastIndexOf('.')+1);
		if((fileType != 'xls') && (fileType != 'xlsx')){
			this.value = '';
			$('.upload_txt').html('请选择上传文件');
			$.toast('请上传正确的文件格式','text');
		}else{
			$('.upload_txt').html(fileName);
		}
	},false);
	
	//批量导入提交数据
	$('#importUpdate').on('click',function(){
		console.log('提交按钮');
		var inputId = $('#importProjectTeam').val();
		var len = $('#importSendMsg').children().length;
		var fileVal = $('#inputFile').val();
		if((inputId == 17) && (len == 0)){
			$.toast('发货客户必填','text');
			return;
		}
		if(!fileVal){
			$.toast('文件必填','text');
			return;
		}
		saveImportData();
	});
	
}else{
	//微信页
	$('#inputFile,#importProjectTeam').attr('disabled','disabled');

	//点击提示
	$('#inputFile,#importUpdate,#importChooseCustom,#importProjectTeam').on('click',function(event){
		event.stopPropagation();
		layer.open({
		  type: 1,
		  title: false,
		  closeBtn: 0,
		  area: '80%',
		  skin: 'bgWhite',
		  shadeClose: false,
		  content: $('.showTips')
		});
	});
	
}

//tab切换
$('.sendHeader').on('click',function(){
	$(this).addClass('active').siblings().removeClass('active');
	$('.sendItem').eq($(this).index()).addClass('current').siblings().removeClass('current');
	if($('.sendImport').hasClass('current')){
		layer.open({
		  type: 1,
		  title: false,
		  closeBtn: 0,
		  area: '80%',
		  skin: 'bgWhite',
		  shadeClose: true,
		  content: $('.showTips')
		});
	}
})

//手动录入项目组
$("#projectTeam").on("change",function(){
	$("#sendInf").html("");
	$("#sendSelect").text("请选择发货信息");
	$("#receiveInf").html("");
	$("#receiveSelect").text("请选择收货信息");
	$('#startCity,#endCity').val('');
	
	sessionStorage.setItem('storeGroupName',$(this).val()); //存储手动录入项目组id
	sessionStorage.removeItem("sendCustomID");
	sessionStorage.removeItem("receiveCustomID");
});

//选择项目组样式
$('#projectTeam').change(function () {
    if ($(this).val()) {
        $(this).css('color', '#333');
    } else {
        $(this).css('color', '#999');
    }
})

//手动录入提交数据
$('#showTooltips').on('click', function () {
    var deliverName = $.trim($("#sendSelect").text()),    //发货人
        projectTeam = $.trim($('#projectTeam').val()),    //项目组
        sendInf = $('#sendInf').children().length, //收货客户
        receiveInf = $('#receiveInf').children().length, //收货客户
        deliveryNumber = $.trim($('#deliveryNumber').val()) //送货单号

    //发货人
    if (sendInf == 0) {
        $.toptip('发货人不能为空');
        return;
    }
    //收货客户
    if (receiveInf == 0) {
        $.toptip('收货客户不能为空');
        return;
    }
    //送货单号
    var reg = /^[a-zA-Z0-9]{1,30}$/
    if (deliveryNumber == '') {
        $.toptip("送货单号不能为空");
        return;
    } else if (!reg.test(deliveryNumber)) {
        $.toptip("送货单号应为数字加英文，且长度不能大于30");
        return;
    }
    
    var txtReg = /^[`<>"']$/ig;
    var v = $("#textarea").val().replace(txtReg,'');
	if (v != "" && v.length > 50) {
		$.toptip("任务摘要长度不能大于50");                                                                                                                                                                            
		return;
	}
    
    if (taskId && taskId != 'null') { //此时是编辑页
        console.log('编辑页保存')
        editSave();

    } else { //此时是新增页
        console.log('新增页保存')
        var sendGoodsData = {
        	customerId: $("#receiveId").val() ,
        	sendCustomerId: $("#sendId").val(),
        	arrivalDay: $('#arrivalDay').val(),
        	arrivalHour: $('#arrivalHour').val(),
        	groupid: groupId ,
        	deliveryNumber: $.trim($('#deliveryNumber').val()),
        	orderSummary: $.trim($('#textarea').val())
        }
        sessionStorage.setItem('sendGoodsPageStore',JSON.stringify(sendGoodsData));
        window.location.href = _common.version('goodsDetail.html');
    }

})

//订单摘要
calcText();

function chooseCustom(obj, type) {
	var id;
	if(isWeixin()){ //微信页
		id = $('#projectTeam').val(); //手动录入的项目组id		
	}else{ //浏览器
		id = $('#importProjectTeam').val(); //手动录入的项目组id
	}
    sessionStorage.setItem("customerType", type)
    if (taskId && taskId != 'null') { //此时是编辑页
        window.location.href = _common.version("chooseCustom.html?dgroupid=" + id + '&fromPage=0' + '&taskId=' + taskId);
    } else { //此时是新增页
        window.location.href = _common.version("chooseCustom.html?dgroupid=" + id + '&fromPage=0');
    }
}

//获取项目组名称接口
function getItemName() {
    EasyAjax.ajax_Post_Json({
        url: 'mobile/mine/group/list'
    }, function (data) {
        var dataArr = data.groups;
        if (dataArr !== null && dataArr.length !== 0) {
            var groupHtml = $('#groupsName').render(dataArr);
            $('#projectTeam').append(groupHtml).val(sessionStorage.getItem('storeGroupName') || ''); //手动录入项目组
            $('#importProjectTeam').append(groupHtml).val(groupId || ''); //批量导入项目组

            if (taskId && taskId != 'null') {//此时是编辑页
                console.log('编辑页根据任务单id查数据')
                getTaskInfo();
                if (groupId != null && customerId != null) {
                    getCurtomerInf();
                }

            } else { //此时是新增页
                console.log('新增页获取客户信息');
                var sendCustomID = sessionStorage.getItem("sendCustomID"); //发货客户id
	            var receiveCustomID = sessionStorage.getItem("receiveCustomID"); //收货客户id
	            if(!isWeixin()){
	            	//浏览器
                	if(_common.getUrlParam('sendCid') && (groupId != 0)){
                		$('#aboutCustom').show(); //发货客户信息显示
                		importGetMsg(_common.getUrlParam('sendCid'));
                	}
	            }else{
	            	//微信
	            	if (receiveCustomID) {
	                    getCurtomerInf(receiveCustomID);
	                }
	                if (sendCustomID) {
	                    getCurtomerInf(sendCustomID);
	                }
	            }
            }


        }
    });
}

//保存手动录入数据接口
function saveBarCode() {
    var baseValue = {
        groupid: groupId,
        customerId: $("#receiveId").val(),
        sendCustomerId: $("#sendId").val(),
        deliveryNumber: $.trim($('#deliveryNumber').val()),  //送货单号
        orderSummary: $.trim($('#textarea').val()),  //摘要
        goodsQuantity: $.trim($('#number').val()),
        goodsWeight: $.trim($('#weight').val()),
        goodsVolume: $.trim($('#volume').val()),
        arrivalDay: $('#arrivalDay').val(),
        arrivalHour: $('#arrivalHour').val()
    }
    EasyAjax.ajax_Post_Json({
        url: '/mobile/wayBill/saveWaybill',
        data: JSON.stringify(baseValue)
    }, function (res) {
        $.toast(res.message);
        if (res.success) {
            window.location.href = _common.version("projectTeamTask.html");
            sessionStorage.removeItem("customerType")
            sessionStorage.removeItem("sendCustomID");
            sessionStorage.removeItem("receiveCustomID");
        }
    });
}

//保存批量导入接口
function saveImportData() {
	loadImg();
	var formData = new FormData();
    formData.append("groupid", $('#importProjectTeam').val());
    formData.append('senderKey',sessionStorage.getItem('sendCustomID'));
    formData.append('file',$('#inputFile')[0].files[0]);
    console.log(11);
    console.log($('#inputFile')[0].files[0]);
    
    $.ajax({
        url: api_host+'mobile/trace/import/excel',
        type: 'POST',
        headers:{
		    "token": api_token	
		},
        processData: false,contentType: false, data: formData,
        success: function (res) {
        	removeLoad();
        	if(res.success){
        		//导入成功
        		$.alert(res.message, '', function() {
					$('.import-title,.sendgood-wrapper').hide();
					$('.successTips').show();
				});
        	}else{
        		//导入失败
        		$.modal({
				    title: res.message,
				    text: "",
				    buttons: [
				        { 
				        	text: "重新导入",
				        	onClick: function(){
				        		$('#inputFile').val('');
				        		$('.upload_txt').html('请选择上传文件');
				        	}
				        },
				        { 
				        	text: "下载查看错误数据",
				        	onClick: function(){
				        		$('<form action="'+res.url+'" method="post"></form>').appendTo('body').submit().remove();
				        	}
				        }
				    ]
				});
        	}
			
		},
        error:function(){
        	
        }
    });
}


//手动录入收发货客户接口 
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
        if (data.city != "市辖区") {
            var city = data.city
        } else {
            var city = data.province
        }
        if (_type == 1) {//收货
            $("#receiveSelect").html(data.companyName);
            $('#receiveInf').html(customListHtml);
            $("#endCity").val(city);
            sessionStorage.setItem("receiveCustomID", data.id)
            //新增的要求到货时间
            $('#arrivalDay').val(data.arrivalDay);
            $('#arrivalHour').val(data.arrivalHour);
            
        } else if (_type == 2) {//发货
            $("#sendSelect").html(data.companyName);
            $('#sendInf').html(customListHtml);
            $("#startCity").val(city);
            sessionStorage.setItem("sendCustomID", data.id);
        }
    });
}

//批量导入发货信息接口
function importGetMsg(cid){
	customerId = cid || customerId;
    var baseValue = {
        customerId: customerId,
        groupId: groupId
    }
    EasyAjax.ajax_Post_Json({
        url: '/mobile/customer/choose/' + customerId,
        data: JSON.stringify(baseValue)
    }, function (res) {
    	var data = res.customer;
    	var customListHtml = $('#showCustomInf').render(data);
    	$('#importSendMsg').html(customListHtml).find('.seat').hide();
    });
}

//项目组任务进入，根据任务单id查询数据
function getTaskInfo() {
    EasyAjax.ajax_Post_Json({
        url: '/mobile/wayBill/queryById/' + taskId
    }, function (res) {
        var data = res.result;
        dName = data.shipperName;
        waybillgid = data.groupid;
        waybillcid = data.customerid;
        $('#deliverName').val(data.shipperName); //发货人
        $('#projectTeam').val(data.groupid).attr("disabled",true); //项目组
        
        //发货信息
        var sendHtml = $("#shipperInfo").render(data);
        $('#sendInf').html(sendHtml);
        
        //收货信息
        var taskHtml = $('#taskIdInfo').render(data);
        $('#receiveInf').html(taskHtml);
        
        //收货客户中的项目组名
        $("#groupName").html($("#projectTeam").find("option:selected").text());
        $('#deliveryNumber').val(data.deliveryNumber);  //送货单号
        $('#weight').val(data.weight == 0 ? "" : data.weight).prop('readonly', 'readonly').css('background', '#ddd');  //重量
        $('#volume').val(data.volume == 0 ? "" : data.volume).prop('readonly', 'readonly').css('background', '#ddd');  //体积
        $('#number').val(data.number == 0 ? "" : data.number).prop('readonly', 'readonly').css('background', '#ddd');  //数量
        $('#textarea').val(data.orderSummary);  //订单摘要
        
        $("#startCity").val($.views.helpers.address(data.startStation));
        $("#endCity").val($.views.helpers.address(data.endStation));
    });
}

//编辑页保存接口
function editSave() {
    var baseValue = {
        id: taskId,
        customerId: $("#receiveId").val(),
        sendCustomerId: $("#sendId").val(),
        groupid: $('#projectTeam').val(),
        deliveryNumber: $.trim($('#deliveryNumber').val()),  //送货单号
        orderSummary: $.trim($('#textarea').val()),  //摘要
        goodsQuantity: $.trim($('#number').val()),
        goodsWeight: $.trim($('#weight').val()),
        goodsVolume: $.trim($('#volume').val()),
        arrivalDay: $('#arrivaDay').val(),
        arrivalHour: $('#arrivaHour').val()
    }
    EasyAjax.ajax_Post_Json({
        url: 'mobile/wayBill/updateWaybill',
        data: JSON.stringify(baseValue)
    }, function (res) {
        $.toast(res.message);
        window.location.href = _common.version("projectTeamTask.html");
    });
}

//计算订单摘要剩余字数
function calcText() {
    var max = $('#count_max').text();
    $('#textarea').on('input', function () {
        var text = $(this).val();
        var len = text.length;
        $('#count').text(len);
        if (len >= max) {
            this.value = this.value.substr(0, max);
            $(this).closest('.weui-cell__bd').addClass('color-danger');
            $(".weui-textarea-counter").css('color', '#f6383a');
            $('#count').text(max);
        } else {
            $(this).closest('.weui-cell__bd').removeClass('color-danger');
            $(".weui-textarea-counter").css('color', '#B2B2B2');
        }
    });
}


//判断是否是在微信页
function isWeixin(){
	var ua = navigator.userAgent.toLowerCase();
	var isWeixin = ua.indexOf('micromessenger') != -1;
	if (isWeixin) {
		//alert('微信页');
		return true;
	}else{
		//alert('浏览器');
		return false;
	}
}

//关闭浏览器
function closeWindows() {
	var userAgent = navigator.userAgent;
	if (userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Chrome") != -1) {
		close();//直接调用JQUERY close方法关闭
	} else {
		window.opener = null;
		window.open("", "_self");
		window.close();
	}
};