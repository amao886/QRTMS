/**
 * 微信接口调用
 */
var imageSize = 5;//设置图片张数
var images = {  
        localId: [],  //保存上传图片的路径
        localAbId: [],  //保存异常图片上传的路径
        serverId:[]
   };
var g_apis = ['checkJsApi', 'scanQRCode', 'chooseImage', 'previewImage', 'uploadImage', 'downloadImage', 'getNetworkType', 'openLocation', 'getLocation', 'onMenuShareTimeline', 'onMenuShareAppMessage'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2

//获取微信JSSDK授权参数
getJsApi();



/**
 * 微信分享，邀请注册
 * customerKey 公司id  * title 标题  * orderSummary 摘要 * 
 */
function shareRegister(customerKey, title, orderSummary ){
	wx.onMenuShareAppMessage({
		title : '邀请注册', //分享标题
		desc : title +',邀请您注册合同物流管理平台平台，运输查货更方便', // 分享描述
		link :  api_host+'mobile/wechat/company/associate/'+customerKey+'/0', //分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
		imgUrl :  api_host+'static/images/login_logo.png', // 分享图标
		type : 'link', // 分享类型,music、video或link，不填默认为link
		dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
		success : function() {
			// 用户确认分享后执行的回调函数
			$.toast('分享成功','info');
		},
		cancel : function() {
			// 用户取消分享后执行的回调函数
		}
	});
}
/**
 * 微信分享
 * barcode任务单号  * orderSummary订单摘要  * waybillId任务单id  * shareId分享人id  * bindtime任务单创建时间
 */
function shareMsg(barcode,orderSummary,waybillId,shareId,bindtime){
	if(bindtime.indexOf('-')>0){//看是时间戳类型还是Date类型,date也有带分钟和不带分钟的
		var srtArray= bindtime.substring(0,10).split('-');
		bindtime=srtArray[0]+"年"+srtArray[1]+"月"+srtArray[2]+"日";
	}else{
		bindtime=timeStamp2String(bindtime);
	}
	//获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
    var curWwwPath = window.document.location.href;
    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8083
    var localhostPath = curWwwPath.substring(0, pos);
    //获取带"/"的项目名，如：/uimcardprj
    //var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);

	wx.onMenuShareAppMessage({
		title : '【合同物流管理平台】'+orderSummary+','+bindtime+'订单', //分享标题
		desc : '任务摘要：'+orderSummary, // 分享描述
		link : localhostPath+'/mobile/wechat/share/'+shareId+'/'+waybillId, //分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
		imgUrl : localhostPath+'/mobile/images/logo.png', // 分享图标
		type : 'link', // 分享类型,music、video或link，不填默认为link
//		dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
		success : function() {
			// 用户确认分享后执行的回调函数
			$.toast('分享成功','info');
		},
		cancel : function() {
			// 用户取消分享后执行的回调函数
		}
	});
}

//调用扫一扫接口
function scanCode(){
	wx.scanQRCode({
	    needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
	    scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
	    success: function (res) {
	    	var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
	    	if(result.indexOf('backstage') >= 0){
	    		$.confirm({
				    title: '',
					text: '确定去回单扫描吗',
					onOK: function () {
					    window.location.href = result;
					}
				});
	    	}else if(result.indexOf('mobile')>=0 || result.indexOf('weixin')>=0){
			    window.location.href=result;
	        }else{
	         	$.toptip('无效条码','cancel');
	        }
		}
	});
}

//定义多选后数据路径保存上
//回单，异常图片选择
function chooseImage(arr){
	wx.chooseImage({
		count : imageSize, // 默认9
		sizeType : ['compressed' ], // 可以指定是原图还是压缩图，默认二者都有
		sourceType : [ 'album', 'camera' ], // 可以指定来源是相册还是相机，默认二者都有
		success : function(res) {
			var localIds = res.localIds;
			var imagestr="";
			for(var i=0;i<localIds.length;i++){
				arr.push(localIds[i]);
				imagestr += '<li class="weui-uploader__file z_photo" imgSrc="'+localIds[i]+'">'+
							'<img src="'+localIds[i]+'"/>'+
							'<span class="close-img"></span>';
			}
			$('#uploaderFiles').append(imagestr);
			$('.remove-hook').removeClass('txtCenter');
			
			//点击删除图片
			$('#uploaderFiles').delegate('.close-img','click',function(event){
				var $parentLi = $(this).parent('li');
				$.confirm({
				  	title: '确认删除图片？',
				  	text: '',
				  	onOK: function () {
				    	var imgSrc = $parentLi.attr('imgSrc');
			          	$parentLi.remove();
			          	for(var i=0;i<arr.length;i++){
			          		if(imgSrc == arr[i]){
			          			arr.splice(i,1);
			          			if(arr.length == 0) $('.remove-hook').addClass('txtCenter');
			          		}
			          	}
				  	}
				});
				event.stopPropagation(); //阻止事件冒泡
			})
			//点击放大图片
			$('#uploaderFiles').delegate('.z_photo','click',function(){
				var $src = $(this).attr('imgSrc');
				$('#gallery').show().children('#galleryImg').attr('src',$src);			
			})
			//点击关闭图片
			$('#gallery').click(function(){
				$(this).hide();
			})
		},
		cancel:function(){

		}
	});
}

//异常上报
$('#abTooltips').click(function(){
	//判断textarea值是否为空
	var areaTxt = $('#areaTxt').val();
	if(areaTxt == ''){
		$.toast('请输入异常情况说明','text');
		return;
	}
	
	//获得任务单id
	var wayBillId = parseInt(_common.getUrlParam('waybillId'));
	
	images.serverId = [];
	var len = images.localAbId.length;
	//alert(images.localAbId);
	if(len > 0){  //若是没有上传图片这个步骤，直接点确定上传，是没有反应的，wx.uploadImage这个方法是不执行的
        uploadImage(0);
	}else{
		submitAbnormalMsg({
			serverId  :  '',
			wayBillId : wayBillId,
			content   : areaTxt,
			flag      : 0
		},wayBillId);
	}
    function uploadImage(i){
        setTimeout(function(){
            wx.uploadImage({
                localId: images.localAbId[i], // 需要上传的图片的本地ID，由chooseImage接口获得
                success: function (res) {
                    images.serverId.push(res.serverId);
                    if(++i < len){
                        uploadImage(i);
                    }else{
                        submitAbnormalMsg({
                            serverId  :  images.serverId.join(','),
                            wayBillId : wayBillId,
                            content   : areaTxt,
                            flag      : 0
                        }, wayBillId);
                    }
                }
            });
        }, 100);
    }
    /*
	function uploadImg(){
		wx.uploadImage({
		    localId: images.localAbId[i], // 需要上传的图片的本地ID，由chooseImage接口获得
		    success: function (res) {
		        //var serverId = res.serverId; // 返回图片的服务器端ID,是单个id
		        i++;
		        images.serverId.push(res.serverId);
		        if(i<len){
		        	uploadImg();
		        }else{
		        	submitAbnormalMsg({
						serverId  :  images.serverId.join(','),
						wayBillId : wayBillId,
						content   : areaTxt,
						flag      : 0
					},wayBillId);

		        }
		    }
		});
	}
	*/
})

//点击上传回单提交
$('#uploadTooltips').click(function(){	
	var len = $('#uploadTaskContent').children().length;
	if(len == 0){
		$.toast('请先选择任务单','text');
		return;
	}
	
	//获得任务单id
	var wayBillId = parseInt($('#chooseId').attr('uid'));
	
	images.serverId = [];
	var len = images.localId.length;
	if(len == 0){  //若是没有上传图片这个步骤，直接点确定上传，是没有反应的，wx.uploadImage这个方法是不执行的
		$.toast('请上传回单照片','text');
	}else{
        uploadImage(0);
	}
	function uploadImage(i){
        setTimeout(function(){
            wx.uploadImage({
                localId: images.localId[i], // 需要上传的图片的本地ID，由chooseImage接口获得
                success: function (res) {
                    images.serverId.push(res.serverId);
                    if(++i < len){
                        uploadImage(i);
                    }else{
                        submitAbnormalMsg({
                            serverId  :  images.serverId.join(','),
                            wayBillId : wayBillId,
                            content   : '',
                            flag      : 1
                        }, wayBillId);
                    }
                }
            });
        }, 100);
	}

	/*
	function uploadImg(){
		wx.uploadImage({
		    localId: images.localId[i], // 需要上传的图片的本地ID，由chooseImage接口获得
		    success: function (res) {
		        i++;
		        images.serverId.push(res.serverId);
		        if(i<len){
		        	uploadImg();
		        }else{
		        	submitAbnormalMsg({
						serverId  :  images.serverId.join(','),
						wayBillId : wayBillId,
						content   : '',
						flag      : 1
					},wayBillId);

		        }
		    }
		});
	}
	*/
})

//将时间戳格式化
function timeStamp2String(time){
    var datetime = new Date();
     datetime.setTime(time);
     var year = datetime.getFullYear();
     var month = datetime.getMonth() + 1;
     var date = datetime.getDate();
     return year + "年" + month + "月" + date + "日";
};

function getJsApi(){
	var targetUrl = encodeURIComponent(window.location.href);
	EasyAjax.ajax_Post_Json({
		url: 'mobile/wechat/jsapi',
		data: JSON.stringify({'targetUrl': targetUrl})
	}, function(res){
		wx.config({
		    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
		    appId:res.appId, // 必填，公众号的唯一标识
		    timestamp:res.timestamp, // 必填，生成签名的时间戳
		    nonceStr: res.nonceStr, // 必填，生成签名的随机串
		    signature:res.signature,// 必填，签名，见附录1
		    jsApiList: g_apis // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
		});
	});
	/*
	EasyAjax.ajax_Post_Json({
		url:'mobile/wechat/jsapi?targetUrl='+encodeURIComponent(window.location.href)
	},function(res){
		wx.config({
		    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
		    appId:res.appId, // 必填，公众号的唯一标识
		    timestamp:res.timestamp, // 必填，生成签名的时间戳
		    nonceStr: res.nonceStr, // 必填，生成签名的随机串
		    signature:res.signature,// 必填，签名，见附录1
		    jsApiList: [
		                'checkJsApi',
		                'scanQRCode',
		                'chooseImage',
		                'previewImage',
		                'uploadImage',
		                'downloadImage',
		                'getNetworkType',
		                'openLocation',
		                'getLocation',
		                'onMenuShareTimeline',
		                'onMenuShareAppMessage'
		                ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
		});
	});
	*/
}

//上传回单、异常上报接口
function submitAbnormalMsg(options,waybillId){
	var baseValue = {
		serverId   : options.serverId,
		wayBillId  : options.wayBillId,
		content    : options.content,
    	flag       : options.flag
	}
	EasyAjax.ajax_Post_Json({
		url:'mobile/wayBill/uploadWx',
		data:JSON.stringify(baseValue)
	},function(res){
		$.toast(res.message,function(){
			window.location.href = '../../view/partener/myTaskDetails.html?waybillId='+waybillId;
		});
		
	});
}
