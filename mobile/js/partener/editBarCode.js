var groupId     =_common.getUrlParam('groupId'); //项目组id
var taskId      = _common.getUrlParam('taskId');//任务单号（新增）

//清缓存
sessionStorage.removeItem('editBarMsg');
sessionStorage.removeItem('bindBarMsg'); 


$(function() {
	//编辑功能——获取任务单信息
	getTaskInfo();
	
	var max = $('#count_max').text();
	$('#textarea').on('input',function() {//文本框剩余字数
			var text = $(this).val();
			var len = text.length;
			$('#count').text(len);
			if (len >= max) {
				this.value = this.value.substr(0, max);
				$(this).closest ('.weui-cell__bd').addClass('color-danger');
				$(".weui-textarea-counter").css('color','#f6383a');
				$('#count').text(max);
			} else {
				$(this).closest('.weui-cell__bd').removeClass('color-danger');
				$(".weui-textarea-counter").css('color','#B2B2B2');
			}
	});
})

//点击修改物料信息
$('#editGoodsDetail').on('click',function(){
	//只需要将waybillId传到物料编辑页
	sessionStorage.setItem('editBarMsg',taskId);
	sessionStorage.removeItem('editCalc');
	window.location.href = _common.version('goodsDetailEdit.html');
});

//提交数据
$('#showTooltips').on('click', function () {
    var deliverName = $.trim($("#sendSelect").text()),    //发货人
        projectTeam = $.trim($('#projectTeam').val()),    //项目组
        sendInf = $('#sendInf').children().length, //收货客户
        receiveInf = $('#receiveInf').children().length, //收货客户
        deliveryNumber = $.trim($('#deliveryNumber').val()), //送货单号
        weight = $.trim($("#weight").val()),
        volume = $.trim($("#volume").val()),
        numberv = $.trim($("#number").val());

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
    
    //重量
//  var weireg = /^([1-9][0-9]*)+(.[0-9]{1,2})?$/;
//  if (weight != "" && (!weireg.test(weight) || weight.length > 8)) {
//      $.toptip("重量应为数字或2位小数且长度不能大于8位");
//      return;
//  }
//
//  //体积
//  var volume = $("#volume").val();
//  if (volume != "" && (!weireg.test(volume) || volume.length > 8)) {
//      $.toptip("体积应为数字或2位小数且长度不能大于8位");
//      return;
//  }
//
//  //数量
//  var numreg = /^([1-9][0-9]*){1,8}$/;
//  if (numberv != "" && !numreg.test(numberv)) {
//      $.toptip("数量应为正整数且长度不能大于8位");
//      return;
//  }
    
    var txtReg = /^[`<>"']$/ig;
    var v = $("#textarea").val().replace(txtReg,'');
	if (v != "" && v.length > 50) {
		$.toptip("任务摘要长度不能大于50");                                                                                                                                                                            
		return;
	}

    editSave(); //保存  
})


//项目组任务进入，根据任务单id查询数据
function getTaskInfo() {
    EasyAjax.ajax_Post_Json({
        url: 'mobile/wayBill/queryById/' + taskId
    }, function (res) {
        var data = res.result;
        dName = data.shipperName;
        waybillgid = data.groupid;
        waybillcid = data.customerid;
        $("#barcode").val(data.barcode);
        
        var sendCustomID = sessionStorage.getItem("sendCustomID")
        var receiveCustomID = sessionStorage.getItem("receiveCustomID");       
        //收货信息
        if (receiveCustomID) {//选择客户信息后
            getCurtomerInf(receiveCustomID);
        }else{//未选择，通过taskId查询结果
        	var taskHtml = $('#taskIdInfo').render(data);
	        $('#receiveInf').html(taskHtml);
	        $("#receiveId").val("");	        
        }
        //发货信息
        if (sendCustomID) {//选择客户信息后
            getCurtomerInf(sendCustomID);
        }else{//未选择，通过taskId查询结果
        	var sendHtml = $("#shipperInfo").render(data);
        	$('#sendInf').html(sendHtml);
        	$("#sendId").val("");
        }                
        
        //收货客户中的项目组名
        $("#groupName").html($("#projectTeam").find("option:selected").text());
        $('#deliveryNumber').val(data.deliveryNumber);  //送货单号
        if(sessionStorage.getItem('editCalc')){
        	//修改物料页
        	var editCalc = JSON.parse(sessionStorage.getItem('editCalc'));
        	$('#weight').html(editCalc.weight);  //重量
        	$('#volum').html(editCalc.volume);  //体积
        	$('#number').html(editCalc.quantity);  //数量
        }else{
        	$('#weight').html(data.weight);  //重量
        	$('#volum').html(data.volume);  //体积
        	$('#number').html(data.number);  //数量
        }
        
        $('#textarea').val(data.orderSummary);  //订单摘要
        
        $("#startCity").val($.views.helpers.address(data.startStation));
        $("#endCity").val($.views.helpers.address(data.endStation));
        
        $("#sendSelect").html(data.shipperName);
        $("#receiveSelect").html(data.receiverName);
    });
}
 
//收货客户接口 
function getCurtomerInf(cid) {
    customerId = cid || customerId;
    var baseValue = {
        customerId: customerId,
        groupId: groupId || "0"
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
        } else if (_type == 2) {//发货
            $("#sendSelect").html(data.companyName);
            $('#sendInf').html(customListHtml);
            $("#startCity").val(city);
            sessionStorage.setItem("sendCustomID", data.id);
        }

    });
}

//编辑页保存接口
function editSave() {
    var baseValue = {
        id: taskId,
        customerId: $("#receiveId").val(),
        sendCustomerId:$("#sendId").val(),
        groupid: $('#projectTeam').val(),
        deliveryNumber: $.trim($('#deliveryNumber').val()),  //送货单号
        orderSummary: $.trim($('#textarea').val()),  //摘要
        goodsQuantity: $.trim($('#number').val()),
        goodsWeight: $.trim($('#weight').val()),
        goodsVolume: $.trim($('#volume').val())
    }
    EasyAjax.ajax_Post_Json({
        url: 'mobile/wayBill/updateWaybill',
        data: JSON.stringify(baseValue)
    }, function (res) {
        $.toast(res.message);
        if(res.success){
        	sessionStorage.removeItem("customerType")
            sessionStorage.removeItem("sendCustomID");
            sessionStorage.removeItem("receiveCustomID");
            window.location.href = _common.version("projectTeamTask.html");
        }        
    });
}

//选择地址信息
function chooseCustom(obj, type) {
    var id = (groupId == undefined ? "" : groupId);
    sessionStorage.setItem("customerType", type)
    window.location.href = _common.version("chooseCustom.html?dgroupid=" + id + '&taskId=' + taskId);   
}