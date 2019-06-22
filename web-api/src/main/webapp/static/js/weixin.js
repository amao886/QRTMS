/**
 * 微信接口调用
 */
var imageSize = 5;//设置图片张数
var isDetail=true;//防止点分享进入详情
wx.config({
    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId:appId, // 必填，公众号的唯一标识
    timestamp:timestamp, // 必填，生成签名的时间戳
    nonceStr: nonceStr, // 必填，生成签名的随机串
    signature:signature,// 必填，签名，见附录1
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

var imagestr="";//定义多选后数据路径保存上
//回单、异常图片选择
function chooseImage(){
	wx.chooseImage({
		count : imageSize, // 默认9
		sizeType : ['compressed' ], // 可以指定是原图还是压缩图，默认二者都有
		sourceType : [ 'album', 'camera' ], // 可以指定来源是相册还是相机，默认二者都有
		success : function(res) {
			var oldspSize = imagestr.split(",").length-1;//取得之前存在数据数组长度
			imagestr+=res.localIds.join(",")+","
			var subImageStr = imagestr.substring(0,imagestr.length-1);
			var newspSize = subImageStr.split(",").length;//取得新存在数据数组长度
	        images.localId = subImageStr.split(",");//向数组中赋值
	        wxView(oldspSize,newspSize,0);
		}
	});
}

/**重新加载页面
 * arg1初始化值
 * arg2最大值
 * arg3判断是否清除父元素下图片，1是，0否
 */
function wxView(arg1,arg2,arg3){
	 var imgContainer = $(".weui-uploader__input-box").parents(".z_photo"); //存放图片的父亲元素
	 if(arg3==1){
		 $(".weui-uploader__file").remove();//删除元素中已存在的照片
	 }
	 for(var i = arg1;i<arg2;i++){
			var $section = $("<li class='weui-uploader__file' id='"+i+"'>");
			    imgContainer.prepend($section);
			var $i = $("<i class='close-upimg' onclick='deleteImage()'>");
				$i.appendTo($section);
		    var $img = $("<img class='up-img'>");
		        $img.attr("src",images.localId[i]);
		        $img.appendTo($section);
		    var $input = $("<input id='taglocation' name='taglocation' value='' type='hidden'>");
		        $input.appendTo($section);
		    var $input2 = $("<input id='tags' name='tags' value='' type='hidden'/>");
		        $input2.appendTo($section);
		   }
     $(this).val("");
	 setImgsClickEvent();
}

//扫描二维码  
//location.href='<%=basePath%>weixin/internalScan.html?barCode='+(res!=null && res.length>0)?res[1]:"";
function scanQRCode(){
	wx.scanQRCode({  
		needResult : 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，  
		scanType : [ "qrCode", "barCode" ], // 可以指定扫二维码还是一维码，默认二者都有  
		success : function(res) { 
			var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果  
			//document.getElementById("wm_id").value = result;//将扫描的结果赋予到jsp对应值上  
			//var res = result.split('=');
			if(result.indexOf('weixin')>=0){
				location.href=result;
			}else{
				$.toast("无效条码","cancel");
			}
		}  
	});  
}
$('.click-scan-con').click(function() {
	scanQRCode();
});
	/**
	 * 微信分享
	 * barcode任务单号
	 * orderSummary订单摘要
	 * waybillId任务单id
	 * userId分享人id
	 * createtime任务单创建时间
	 */
	function shareMsg(barcode,orderSummary,waybillId,userId,createtime){
		isDetail=false;//防止点分享进入详情
		if(createtime.indexOf('-')>0){//看是时间戳类型还是Date类型
			var srtArray=createtime.split('-');
			createtime=srtArray[0]+"年"+srtArray[1]+"月"+srtArray[2]+"日";
		}else{
			createtime=timeStamp2String(createtime);
		}
		
		//获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
        var curWwwPath = window.document.location.href;
        //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
        var pathName = window.document.location.pathname;
        var pos = curWwwPath.indexOf(pathName);
        //获取主机地址，如： http://localhost:8083
        var localhostPaht = curWwwPath.substring(0, pos);
        //获取带"/"的项目名，如：/uimcardprj
        //var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
        $(".weixin-tip").css({"height":$(window).height(),"z-index":501});
	    $(".weixin-tip").show();
        wx.onMenuShareAppMessage({
			title : '【合同物流管理平台】'+orderSummary+','+createtime+'订单', //分享标题
			desc : '任务摘要：'+orderSummary, // 分享描述
			link : localhostPaht+'/weixin/ToWaybillDetail.html?wayBillId='+waybillId+'&userId='+userId, //分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
			imgUrl : localhostPaht+'/images/logo.png', // 分享图标
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
//		$.confirm({
//			  title:'您确定要分享单号为'+barcode+'的任务单详情吗？',
//			  text: '',
//			  onOK: function () {
//				$(".weixin-tip").css({"height":$(window).height(),"z-index":501});
//		        $(".weixin-tip").show();
//		       
//			  }
//			});
	}
	
	function hideMask(){
		isDetail=true;
        $(".weixin-tip").hide();
	}
	
	//任务单详情
	function itemDetail(waybillId){
		if(isDetail){//防双click影响
			window.location.href = basePath+'weixin/ToWaybillDetail.html?wayBillId='+waybillId;
		}
	}
	
	//使用微信内置地图查看位置接口
	wx.getLocation({
		type : 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
		success : function(res) {
			var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
			var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
			var speed = res.speed; // 速度，以米/每秒计
			var accuracy = res.accuracy; // 位置精度
		}
	});
	
	
	var images = {  
	        localId: [],  
	        serverId: []  
	    };
	
	//选择上传图片
	$('#uploaderInput').click(function() {
		chooseImage();
	});
	 
	  /**
	   * ajax保存数据后台提交方法
	   * arg标记参数，0异常，1回单
	   */
	  function ajaxSubmit(arg){
		  var serverId = images.serverId.join(",");
		  var wayBillId = document.getElementById("wayBillId").value;
		  var textAre = document.getElementById("abNormalInfo").value;
  		$.ajax({
				url : basePath+"weixin/uploadWx.html", // 请求的url地址
				timeout : 5000,
				dataType : "json",
				data : {"serverId":serverId,"wayBillId":wayBillId,"abNormalInfo":textAre,"flag":arg}, // 参数值
				type : "POST", // 请求方式
				success : function(data) {
					// 请求成功时处理
					if (data.resultCode == 200) {
						$.toast(data.reason,'info');
						setTimeout("postredirect();",3000)
					} else {
						// 注册失败提示
						$.toast(data.reason,'cancel');
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					$.toast("网络异常，请求失败！","text");
					if(arg==1){
						 var buttonObject=$("#receiptUploadImage");
						  buttonObject.css({'background-color':'#31acfa'});
						  buttonObject.attr("disabled", false);
					}else{
						var buttonObject=$("#exceptionUploadImage");
						buttonObject.css({'background-color':'#31acfa'});
				  		buttonObject.attr("disabled", false);
					}
				}
			});
	  }
	  
	  //重新跳转到页面
	  function postredirect(){
		  	var wayBillId = document.getElementById("wayBillId").value;
		  	//网页重定向至订单详细页面
			if($('#shareId').val()==null||$('#shareId').val()==''){
				window.location.href="weixin/myTaskDetails.html?wayBillId="+wayBillId;
			}else{
				var shareId = $('#shareId').val();
				window.location.href="weixin/myTaskDetails.html?wayBillId="+wayBillId+"&shareId="+shareId;
			}
	  }
	
	  
		//异常图片上传
	  $('#exceptionUploadImage').click(function() {
		  var i = 0,length = images.localId.length;
		  var textAre = document.getElementById("abNormalInfo").value;
		  if(textAre==null || textAre==""){
			  $.alert('请输入异常情况！');
		      return;
		  }
	    if(length>imageSize){
	    	$.alert("选择照片不能超过"+imageSize+"张");
	    	return;
	    }
	    
	    var buttonObject=$("#exceptionUploadImage");
	    buttonObject.css({'background-color':'#cccccc'});
		buttonObject.attr("disabled", true);
	    
	    images.serverId = [];
	    	//微信上传图片接口
		  function upload() {
		      wx.uploadImage({
		        localId: images.localId[i],
		        success: function (res) {
		          i++;
//		          $.alert('已上传：' + i + '/' + length);
		          images.serverId.push(res.serverId);
		          if (i < length) {
		            upload();
		          }else{
		        	  //上传的个数大于当前选择的个数则记录数据到数据库
		        	  ajaxSubmit("0");
		          } 
		        },
		        fail: function (res) {
		          $.alert(JSON.stringify(res));
		          buttonObject.css({'background-color':'#31acfa'});
		  		  buttonObject.attr("disabled", false);
		        }
		      });
		    }
	    if(length>0){//选择图片后
	    	upload();//微信上传图片接口
	    }else{
	    	ajaxSubmit("0");//数据上传
	    }
	  
		});
	  
	  //回单图片上传
	  function receiptUploadImage(){
		  var i = 0,length = images.localId.length;
		  if(length==0){
			  $.alert('请选择图片！');
		      return;
		  }
	    if(length>imageSize){
	    	$.alert("选择照片不能超过"+imageSize+"张");
	    	return;
	    }
	    
	    var buttonObject=$("#receiptUploadImage");
	    buttonObject.css({'background-color':'#cccccc'});
		buttonObject.attr("disabled", true);
	    
	    images.serverId = [];
		//微信上传图片接口
		  function upload() {
		      wx.uploadImage({
		        localId: images.localId[i],
		        success: function (res) {
		          i++;
//		          $.alert('已上传：' + i + '/' + length);
		          images.serverId.push(res.serverId);
		          if (i < length) {
		            upload();
		          }else{
		        	  //上传的个数大于当前选择的个数则记录数据到数据库
		        	  ajaxSubmit("1");
		          } 
		        },
		        fail: function (res) {
		          $.alert(JSON.stringify(res));
		          buttonObject.css({'background-color':'#31acfa'});
		  		  buttonObject.attr("disabled", false);
		        }
		      });
		    }
		  upload();
	  };
	  
	//获取网络状态接口
	wx.getNetworkType({
		success : function(res) {
			var networkType = res.networkType; // 返回网络类型2g，3g，4g，wifi
		}
	});
	
	//timestamp转换成datetime
	function timeStamp2String(time){
        var datetime = new Date();
         datetime.setTime(time);
         var year = datetime.getFullYear();
         var month = datetime.getMonth() + 1;
         var date = datetime.getDate();
         return year + "年" + month + "月" + date + "日";
	};
	
	/**
	 * @author:113076
	 * @info:图片点击放大具体方法
	 * @time:2017-6-5
	 * */
	function setImgsClickEvent(){
    	var imgs = document.getElementsByTagName("img");
        var lens = imgs.length;

        for(var i = 0; i < lens; i++){
            imgs[i].onclick = function (event){
                event = event||window.event;
                var target = document.elementFromPoint(event.clientX, event.clientY);
                showBig(target.src);
            }
        }
    }
    
    function showBig(src){
    	$("#targetObj").attr("style","position:relative;transform-origin:center");
        popup.getElementsByTagName("img")[0].src = src;
        popup.style.display = "block";
    }
    
    //图片删除
    function deleteImage(){
   	 	var delParent = $(".close-upimg").parent();
   	  $.confirm({
			  title: '确认删除吗',
			  text: '',
			  onOK: function () {
				//在图片集合中删除该图片
			    var id=delParent.attr("id");
			    for(var i=0;i<images.localId.length;i++){
			    	if(i==id){
			    		images.localId.splice(i,1);//清楚数组中的数据
			    		imagestr = images.localId.join(",")
			    		if(images.localId.length>0){
			    			imagestr+=",";
			    		}
			    		delParent.remove();
			    		//删除后需要重新加载页面
			    		wxView(0,images.localId.length,1);
			    		break;
			    	}
			    }
			  }
			});
    }
    
    function scanQRCodeupload(){
    	wx.scanQRCode({  
    		needResult : 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，  
    		scanType : [ "qrCode", "barCode" ], // 可以指定扫二维码还是一维码，默认二者都有  
    		success : function(res) {
    			var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果  
    			//document.getElementById("wm_id").value = result;//将扫描的结果赋予到jsp对应值上  
    			//var res = result.split('=');
    			if(result.indexOf('weixin')>=0){
    				var resultupload = result.substring(0,result.indexOf("type"))+"type=10"+result.substring(result.indexOf("type=5")+6);
    				location.href=resultupload;
    			}else{
    				$.toast("无效条码","cancel");
    			}
    		}  
    	});  
    }
    
