//动态获得放地图div的高度
var _height = document.documentElement.clientHeight-105;
var waybillId = _common.getUrlParam('waybillId');
var waybillSts = _common.getUrlParam('sts');  //判断是否是未绑定状态

if(waybillSts && waybillSts == 10){ //未绑定，底部导航隐藏
	$('#footer-con').hide();
}
//初始化数据
getTaskDetails();



function getTaskDetails(){
	EasyAjax.ajax_Post_Json({
		url:'mobile/wayBill/myTask/deatil/'+waybillId+'?shareId='+(_common.getUrlParam('shareId') || '')
	},function(res){
		var taskDetailHtml = $('#taskDetails').render(res);
		$('#taskDetailsCon').html(taskDetailHtml);
		
		if(res.waybill.goods){
			$('#addbt-hook').addClass('bordertop');
		}
		$('.tHeight').each(function(index,el){
			var $height = $(el).height();
			console.log('$height:'+$height);
			$(this).parent().find('.table-th').height($height);
			$('.goodsName').eq(index).height($height);			
		})
		
		//总数量、体积、重量
		$('.totalWeight').html(calcTotal(res.waybill.goods,'goodsWeight'));
		$('.totalVolume').html(calcTotal(res.waybill.goods,'goodsVolume'));
		$('.totalQuantity').html(calcTotal(res.waybill.goods,'goodsQuantity'));
		
		//初始化插件
		var scroll = new BScroll(document.querySelector('.part-wrapper'), {
	        scrollX: true,
	        scrollY: false,
	        momentum: false,
	        click: true,
	        bounce:false
	    });
		
		
		if(res.waybill.haveType != null){
			var footerHtml = $('#footerInfo').render(res);
			$('#footer-con').html(footerHtml);
		}else{
			$('#footer-con').addClass("hide")
		}
		/*==点击确认到货==*/		
		$('#isConfirm').click(function(){
			var needId = $(this).attr('uid');
			$.confirm({
			  	title: '提示',
			  	text: '是否确认到货及未审核的回单都设置为合格？',
			  	onOK: function () {
			    	EasyAjax.ajax_Post_Json({
						url:'mobile/trace/confirmArrive/'+needId
					},function(res){
						$('.arriveStatus').text('已到货');
						$('#arriveItem').hide();
						$.toast("操作成功");	
					})
			  	}
			});		
		})
		
		/*==上传回单部分==*/
		$('#upload').click(function(){
			var uploadId = $(this).attr('uid');
			window.location.href = _common.version('./upload.html?waybillId='+uploadId);
		})
		
		/*==异常上报部分==*/
		$('#abnormal').click(function(){
			var abnormalId = $(this).attr('uid');
			window.location.href = _common.version('./abnormal.html?waybillId='+abnormalId);
		})
		
		
		
		/*==获取分享需要的参数==*/
		var barcode = $('#useBarcode').attr('ubarcode');
		var orderSummary = $('#useOrderS').attr('uorderS');
		var bindtime = $('#useBindTime').attr('uBindT');
		var userid = $('#userId').attr('uUser');
		var shareId = _common.getUrlParam('shareId') || userid || '';
		
		/*==分享按钮部分==*/
		//不点击分享按钮直接点分享
		wx.ready(function(){
			shareMsg(barcode,orderSummary,waybillId,shareId,bindtime);
		});
		
		//点击分享按钮分享
		$('#share-a').click(function(){
			shareMsg(barcode,orderSummary,waybillId,shareId,bindtime);
			$("#weixin-tip").show();
			setTimeout(function(){
				$("#weixin-tip").hide();
			},3000)
			$("#weixin-tip").click(function(){
				$("#weixin-tip").hide();
			})
		})
		
		/*==点击放大图片==*/
		$('.show-img').click(function(){
			var imgSrc = $(this).attr('imgSrc');
			$('#gallery').show().children('.weui-gallery__img').css('background-image','url('+imgSrc+')').click(function(){
				$('#gallery').hide();
			})
		})
		
		
		/*==设置地图div的高度==*/
		$('#transport-map').height(_height);
		
		/*== 查看轨迹部分 ==*/
		//物流跟踪切换
		$("#changeMap").click(function(){
			if($(this).text() == "切换地图>>"){
				$(this).text("切换跟踪").addClass("changeBtn");
				$("#transport-map").removeClass("hide").siblings("#trackBox").addClass("hide");
			}else{
				$(this).text("切换地图>>").removeClass("changeBtn");
				$("#trackBox").removeClass("hide").siblings("#transport-map").addClass("hide");
			}
		});
		
		if(res.waybill.tracks){
			//最新位置的经纬度
			var showLocation = [res.waybill.tracks[0].longitude,res.waybill.tracks[0].latitude];
			var map = new AMap.Map('transport-map',{
				resizeEnable: true,   //监控地图容器尺寸变化
	            center:showLocation,
	            zoom:13,
	            zoomEnable:true
			});
			
			//添加控件
	        AMap.plugin(['AMap.ToolBar'],function(){
	            //创建并添加工具条控件
	            var toolBar = new AMap.ToolBar();
	            map.addControl(toolBar);
	        })
	        
	         //实例化多个marker对象
	         var lineArr = [];
	         var restTrack = res.waybill.tracks;
	         for(var i=0;i<restTrack.length;i++){
	         	var marker,markerLast;
	         	lineArr.push([restTrack[i].longitude,restTrack[i].latitude]);
				if(i == 0){  //最新位置
	         		
	         		var icon = new AMap.Icon({
			            size: new AMap.Size(40, 50),  //图标大小
			            image: "http://webapi.amap.com/theme/v1.3/images/newpc/way_btn2.png",
			            imageOffset: new AMap.Pixel(0, -60)
			        });
			        markerLast = new AMap.Marker({
			            icon:icon,
			            position:[restTrack[i].longitude,restTrack[i].latitude],
			            offset: new AMap.Pixel(-12,-12),
			            zIndex:200,
			            map:map
			        })
			        		
					//点击最新位置，获得信息窗体
					AMap.event.addListener(markerLast,'click',function(){
			            openInfo(map, contentHtml, showLocation);
			        })
	         		 		
	            }else if(i == restTrack.length-1){//起点
	         		
	         		marker = new AMap.Marker({
		                position:[restTrack[i].longitude,restTrack[i].latitude],
		                offset: new AMap.Pixel(-17, -42), //相对于基点的偏移位置
		                // draggable: true,  //是否可拖动
		                content: '<div class="marker-route marker-marker-bus-from"></div>',   //自定义点标记覆盖物内容
		                map:map
		            });
	         		
	         	}else{  //除起点外别的覆盖物
	         		marker = new AMap.Marker({
	                    position:[restTrack[i].longitude,restTrack[i].latitude],
	                    map:map
	                })
	         	}
	         }
	         
	        if(lineArr.length > 1){
	        	//绘制折线图
		        var polyline = new AMap.Polyline({
		            path: lineArr,          //设置线覆盖物路径
		            strokeColor: "#3366FF", //线颜色
		            strokeOpacity: 1,       //线透明度
		            strokeWeight: 5,        //线宽
		            strokeStyle: "solid",   //线样式
		            strokeDasharray: [10, 5] //补充线样式
		        });
		        polyline.setMap(map);
	        }
	
	
			
			//获取定位信息窗体
			var contentHtml = $('#mapTips').render(res.waybill.tracks[0]);
			
			openInfo(map,contentHtml,showLocation);
			
		}else{
			$('#task-tab2').html("<p style='padding:10px 0;text-align:center;'>暂无轨迹。。。</p>")
		}

	});
}

//调用信息窗体
function openInfo(map,con,location){
    var infoWindow = new AMap.InfoWindow({
        content: con  //使用默认信息窗体框样式，显示信息内容    
    });
    infoWindow.open(map, location);
}

//计算总和
function calcTotal(obj,value){
	var calc = 0;
	obj.forEach(function(val,index){
		var num = parseFloat(val[value]);
		if(num){	
			calc = accAdd(calc,num);
		}
	})
	console.log('calc:'+calc);
	return calc;	
}

function accAdd(arg1,arg2){ //解决js浮点数问题
	var r1,r2,m;  
	try{
		r1=arg1.toString().split(".")[1].length
	}catch(e){
		r1=0
	}try{
		r2=arg2.toString().split(".")[1].length
	}catch(e){r2=0}  m=Math.pow(10,Math.max(r1,r2))
		return (arg1*m+arg2*m)/m
}	    
