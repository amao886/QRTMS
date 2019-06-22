$("#showTooltips").click(function() {
	var name = $("#friendName").val();
	var nameReg = /^[a-zA-Z\u4E00-\u9FFF]{1,10}$/;
	if(!name){
		$.toptip('姓名不能为空');
		return;
	}else if(!nameReg.test(name)){
		$.toptip('姓名只能输入中文或字母');
		return;
	}
	
	var tel = $('#friendTel').val();
	var telReg = /^1[\d]{10}$/
	if(!tel){
		$.toptip('手机号不能为空');
		return;
	}else if(!telReg.test(tel)){
		$.toptip('请输入正确的手机号');
		return;
	}
	
	submitFrdInfo();
});

//手机号添加，表单提交
function submitFrdInfo(){
	var baseValue = {
		    "fullName": $("#friendName").val(),
		    "mobilePhone":$('#friendTel').val()		    
	}
	EasyAjax.ajax_Post_Json({
		url:'/mobile/friends/add/friend',
		data:JSON.stringify(baseValue)
	},function(res){
		if(res.success){
			//$("#page2").addClass("hide").prev("#page1").removeClass("hide");
			window.location.href = _common.version('../../view/partener/contactList.html');
		}
    });
}

//我的二维码展示
$("#myQrCode").click(function(){
	EasyAjax.ajax_Post_Json({
		url:'/mobile/mine/user/info'
	},function(res){
		if(res.success){
			var data = res.user;
			var friendId = data.id;
			$("#myName").text(data.unamezn);
			$("#myphone").text(data.mobilephone);
			
			//生成二维码
			if($("#qrcodeimg").find("canvas").length == 0){//没有生成过二维码
				$("#qrcodeimg").qrcode({ 
				    render: "canvas",
				    width: 230, //宽度 
				    height:230, //高度 
				    typeNumber  : -1,      //计算模式    
				    correctLevel: 0,//纠错等级    
				    background: "#fff",//背景颜色    
				    foreground: "#000", //前景颜色   
				    //添加组员  
				    text: api_host+'mobile/wechat/scan/add/friend/'+friendId
				});
			}
						
			//显示弹窗
			layer.open({
			  type: 1,
			  title: false,
			  closeBtn: 0,
			  //area: '300px',
			  skin: 'layui-layer-nobg', //没有背景色
			  shadeClose: true,
			  content: $('#qrCodeBox')
			});	
		}
    });
	
	
	
});

//点击手机号添加
$("#byPhone").click(function(){
	$("#page2").removeClass("hide").prev("#page1").addClass("hide");
});

//点击扫一扫添加
wx.ready(function(){
	$("#byScan").click(function(){	
		wx.scanQRCode({
		    needResult: 0, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，	
		    scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有	
		    success: function (res) {	
			    var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果	
			}
		});
	});	
});
    



