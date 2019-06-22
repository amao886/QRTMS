<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>物流跟踪-任务监控</title>
<%@ include file="/views/include/head.jsp"%>
<link rel="stylesheet" href="${baseStatic}css/pathway.css?times=${times}" />
<link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css" />
<style type="text/css">
body,html, #container{
  height: 100%;
  margin: 0px;
  padding: 0px;
}
.track{
	display: none
}
.infoTitle{
	font-size: 13px; 
	line-height:1.8em;
}
.infoBody{
	font-size: 12px; 
	line-height:1.8em;
}
.map_icon_red {
	background-color: hsla(360, 100%, 50%, 0.7); 
	height: 24px; 
	width: 24px; 
	border: 1px solid hsl(360, 100%, 40%); 
	border-radius: 12px; 
	box-shadow: hsl(360, 100%, 50%) 0px 0px 1px;
}
.map_icon_blue {
	background-color: hsla(240, 100%, 50%, 0.7); 
	height: 24px; 
	width: 24px; 
	border: 1px solid hsl(240, 100%, 40%); 
	border-radius: 12px; 
	box-shadow: hsl(240, 100%, 50%) 0px 0px 1px;
}
.position{
	top:120px;
	right:0px;
	position: absolute;
}
.content-search .content-search-btn{
	padding:5px 20px;
	color:#fff;
	border-radius: 3px;
	background:#31acfa;
	position:absolute;
	top:58px;
	right:7%;
	font-size:14px;
}
</style>
</head>
<body>
 <div class="custom-content">
     	 <form id='searchWaybillForm' action="${basePath}/backstage/trace/follow" method="post">
     <div class="content-search">
         <div class="clearfix">
             <div class="fl col-min">
                 <label class="labe_l">资源组名:</label>
                 <select class="tex_t selec_t" name="groupid" val="${condition.groupid}">
					<option value="0">其他</option>
					<c:forEach items="${groups}" var="group">
						<option value="${group.id}">${group.groupName }</option>
					</c:forEach>
				</select>
             </div>
             <div class="fl col-min">
                 <label class="labe_l">送货单号:</label>
                 <input type="text" class="tex_t" name="likeString" placeholder="请输入运单号/送货单号/客户名称/客户地址">
             </div>
             <div class="fl col-min">
						<label class="labe_l">状态:</label>
						<select class="tex_t selec_t" name="waybillFettles">
								<option value="20">已绑定</option>
								<option value="30" selected>运输中</option>
								<option value="35">已送达</option>
								<option value="40">已到货</option>
						</select>
					</div>
				</div>
			<div class="clearfix">
					<div class="fl col-min input-append date">
						<label class="labe_l">发货时间:</label>
						<div class="input-append date" id="datetimeStart">
							<input type="text" class="tex_t z_index"  name="bindStartTime" value="${condition.bindStartTime}" readonly>
                         	<span class="add-on">
								<i class="icon-th"></i>
							</span>
						</div> 
					</div>
					<div class="fl col-min">
					    <label class="labe_l">至</label>
						<div class="input-append date" id="datetimeEnd">
							 <input type="text" class="tex_t z_index" name="bindEndTime" value="${condition.bindEndTime}" readonly>
                         	 <span class="add-on">
								<i class="icon-th"></i>
							</span>
						</div> 
					</div>
				</div>	
         <div><a href="javascript:;" class="content-search-btn">查询</a></div>
     </div>
         </form>
     <!-- 地图放container中 -->
     <div id="container" style="height: 100%;"></div>
     <input id="lnglat" hidden="hidden"/>
     <input id="address" hidden="hidden"/>
     <input id="waybillId" hidden="hidden"/>
     <input id="barcode" hidden="hidden"/>
     <div id="position" class="position" style="display: none;">
     	<button id='start' type='button' class='btn btn-default' >开始选点</button>
		<button id='stop' type='button' class='btn btn-default' >取消选点</button>
		<button id='comfirm' type='button' class='btn btn-default'>确认地址</button>
     </div>
</body>
<%@ include file="/views/include/floor.jsp"%>
<script type="text/javascript" src="http://webapi.amap.com/maps?v=1.4.0&key=a4e0b28c9ca0d166b8872fc3823dbb8a&plugin=AMap.ToolBar,AMap.Scale,AMap.OverView,AMap.MapType,AMap.Geocoder"></script>
<script src="http://webapi.amap.com/ui/1.0/main.js?v=1.0.11"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${baseStatic}js/datetimepicker.js"></script>
<script type="text/javascript" src="${baseStatic}js/date.extend.js"></script>
<script>
//发送短信
function sendsms(id){
	if(id){
		$.util.json(base_url + '/backstage/trace/sendsms/'+ id, {}, function(data){
  	 		if(data.success){//处理返回结果  
  	 			$.util.success(data.message);
  	 		}else{//处理返回结果  
  	 			$.util.error(data.message);
  	 		}
  	 	});
	}
}
var caches = {};
function loadByCustomer(geocoder, waybills, index, callback){
	if(index >= waybills.length){//递归结束条件
		callback(waybills);
		return;
	}
	var waybill = waybills[index];
	if(waybill && waybill){
		if(waybill.longitude && waybill.latitude){
			waybill['location'] = {
				lng : waybill.longitude,
				lat : waybill.latitude,
				getLng : function(){ return this.lng; },
				getLat : function(){ return this.lat; }	
			};
			loadByCustomer(geocoder, waybills, ++index, callback);
		}else if(waybill.receiveAddress){
			if(caches[waybill.receiveAddress]){  
				waybill['location'] = caches[waybill.receiveAddress];
				loadByCustomer(geocoder, waybills, ++index, callback);
			}else{
				geocoder.getLocation(waybill.receiveAddress, function(status, result) {
			        if (status === 'complete' && result.info === 'OK') {
			        	if(result.geocodes && result.geocodes.length > 0){
			        		waybill['location'] = result.geocodes[0].location;
			        		caches[waybill.receiveAddress] = result.geocodes[0].location;
			        	}
			        }
					loadByCustomer(geocoder, waybills, ++index, callback);
			    });
			}
		}else{
			loadByCustomer(geocoder, waybills, ++index, callback);
		} 
	}else{
		loadByCustomer(geocoder, waybills, ++index, callback);
	}
}

var mywindow = {
	context : function(lable, centext, waybillid){
		var sss = "<div class='tip-item clearfix'>";
		sss += "<span class='tip-title fl'>"+ lable +"：</span>";
		sss += "<span class='tip-content fl'>";
		if(waybillid){
			sss += "<a href='"+ base_url+"/backstage/trace/findById/"+ waybillid +"'>"+ centext +"</a>";
		}else{
			sss += centext;
		}
		sss += "</span>";
		sss += "</div>";
		return sss;
	},
	title : function(title){
		return "<div>"+ title +"</div>";
	},
	trackBody : function(track){
		var sss = "<div class='map-tips'>";
		sss += this.context('任务单号', track.barcode, track.waybillid);
		if(track.deliveryNumber){
			sss += this.context('送货单号', track.deliveryNumber);
		}
		sss += this.context('上报位置', track.locations);
		sss += this.context('上报时间', new Date(track.createtime).Format('yyyy年MM月dd日 hh:mm:ss'));
		sss += this.context('联系电话', track.user.mobilephone);
		sss +="<div class='msg-tips'>";
		//sss +="<button type='button' class='btn btn-default btn_send_msg' onclick='sendsms("+ track.waybillid +")'>短信提醒</button>";
		sss +="</div>";
    	sss +="</div>";
    	return sss;
	},
	customerBody : function(waybill){
		var sss = "<div class='map-tips'>";
		sss += this.context('收货客户', waybill.receiverName);
		sss += this.context('联系人员', waybill.contactName);
		var tels = waybill.contactPhone;
		if(waybill.receiverTel){
			tels += "("+ customer.receiverTel +")";
		}
		sss += this.context('联系方式', tels);
		sss += this.context('收货地址', waybill.receiveAddress);
    	sss +="</div>";
    	return sss;
	},
	allBody : function(waybill, track){
		$("#waybillId").val(waybill.id);
		$("#barcode").val(waybill.barcode);
		var sss = "<div class='map-tips'>";
		sss += this.context('任务单号', waybill.barcode, waybill.id);
		if(waybill.deliveryNumber){
			sss += this.context('送货单号', waybill.deliveryNumber);
		}
		sss += this.context('上报位置', track.locations);
		sss += this.context('上报时间', new Date(track.createtime).Format('yyyy年MM月dd日 hh:mm:ss'));
		sss += this.context('联系电话', track.user.mobilephone);
		sss += this.context('收货客户', waybill.receiverName);
		sss += this.context('联系人员', waybill.contactName);
		var tels = waybill.contactPhone;
		if(waybill.receiverTel){
			tels += "("+ waybill.receiverTel +")";
		}
		sss += this.context('联系方式', tels);
		sss += this.context('收货地址', waybill.receiveAddress);
		
		sss +="<div class='msg-tips'>";
		//sss +="<button type='button' class='btn btn-default btn_send_msg' onclick='sendsms("+ track.waybillid +")'>短信提醒</button>";
		sss +="</div>";
    	sss +="</div>";
    	return sss;
	}
}

var map = null, complete = false;
function ajaxSearch(){
	//$(".position").hide();
	clear();
	if(map){
		map.destroy();
	}
	map = new AMap.Map('container', { resizeEnable: true, zoom: 10 });
	var parmas = {}
	$('.content-search').find('input, select').each(function(index, item){
		parmas[item.name] = item.value;
	});
	$.util.json(base_url +'/backstage/trace/followAjax', parmas, function(data){
		if(data.success){
			if(!data.waybills || data.waybills.length <= 0){
				$.util.success("没有数据");
				return;
			}
			var map_loading = null;
			var timeout = window.setInterval(function(){//两秒后跳转
				console.log(complete);
				if(!complete){
					if(map_loading == null){
                        map_loading = $.util.loading("地图数据绘制中,请稍后...");
					}
				}else{
                    window.clearInterval(timeout);
                    map_loading.close();
				}
	        },500);
			var longitude = null, latitude = null;//经度/维度
			AMapUI.loadUI(['overlay/SimpleInfoWindow', 'misc/PathSimplifier','misc/PositionPicker'], function(SimpleInfoWindow, PathSimplifier,PositionPicker) {
				map.addControl(new AMap.ToolBar());//集成了缩放、平移、定位等功能按钮在内的组合控件
		        map.addControl(new AMap.Scale());//展示地图在当前层级和纬度下的比例尺
		        map.addControl(new AMap.OverView({isOpen:true}));//在地图右下角显示地图的缩略图
				var supportCanvas = PathSimplifier.supportCanvas, pathSimplifier = null;
				var infoWindow = new SimpleInfoWindow({
					offset: new AMap.Pixel(0, -30)
				});
		        if(supportCanvas){
					pathSimplifier = new PathSimplifier({
			            zIndex: 100, autoSetFitView:true, map: map,
			            getPath: function(pathData, pathIndex) {
			                return pathData.path;
			            },
			            renderOptions: {
			            	getPathStyle: function(item, zoom){
			            		var color = '#00CD00';
			            		if(item.pathData.data.delay){
			            			color = '#FF0000';
			            		}
			            		return {
			            			pathLineStyle: { strokeStyle: color },
			                        pathNavigatorStyle: {
			                        	fillStyle:color, strokeStyle : color, lineWidth: 1, pathLinePassedStyle: { strokeStyle : color }
						            }
			            		}
			            	}
			            }
			        });
					pathSimplifier.on('pathClick', function(event, pathInfo){
		            	if(pathInfo.pathData.data.tracks && pathInfo.pathData.data.tracks.length > 0){
		            		var waybill = pathInfo.pathData.data;
		            		var track = pathInfo.pathData.data.tracks[0];
			            	infoWindow.setInfoTitle(mywindow.title('任务信息'));
							infoWindow.setInfoBody(mywindow.allBody(waybill, track));
							infoWindow.open(map, event.originalEvent.lnglat);
							$(".position").show();
							longitude = track.longitude;
							latitude = track.latitude;
		            	}
					});
				}
				var offset = new AMap.Pixel(-12,-12);
				loadByCustomer(new AMap.Geocoder(), data.waybills, 0, function(waybills){
					var objects = [];
					for (var i = 0; i < waybills.length; i++) {
						var waybill = waybills[i], positions = [];
						if(waybill.location){
							var position = [waybill.location.getLng(), waybill.location.getLat()];
							var customer = new AMap.Marker({'offset': offset, 'position': position, 'map': map, 'extData': waybill});
							customer.setContent("<div class='map_icon_red'></div>");
							customer.on('click', function(){
								var e = this.getExtData();
								infoWindow.setInfoTitle(mywindow.title('收货客户信息'));
								infoWindow.setInfoBody(mywindow.customerBody(e));
								infoWindow.open(map, this.getPosition());
							});
							positions.unshift(position);
							if(waybill.fenceStatus == 1){//开启
								var radius = waybill.fenceRadius || 5, radius = radius*1000;
								var circle = new AMap.Circle({
							        radius: radius, //半径
							        strokeColor: "#3366FF", //线颜色
							        strokeOpacity: 0.8, //线透明度
							        strokeWeight: 1, //线粗细度
							        fillColor: "#1791fc", //填充颜色
							        fillOpacity: 0.2//填充透明度
							    });
							    circle.setCenter(position);// 圆心位置
							    circle.setMap(map);
							}
						}
						if(waybill.tracks && waybill.tracks.length > 0){
							var track = waybill.tracks[0];
							track['deliveryNumber'] = waybill.deliveryNumber;
							track['barcode'] = waybill.barcode;
							var position = [track.longitude, track.latitude];
							var tracker = new AMap.Marker({'offset': offset, 'position':position, 'map': map, 'extData': track});
							tracker.setContent("<div class='map_icon_blue'></div>");
							tracker.on('click', function(){
								var e = this.getExtData();
								infoWindow.setInfoTitle(mywindow.title("最新定位信息"));
								infoWindow.setInfoBody(mywindow.trackBody(e));
								infoWindow.open(map, this.getPosition());
							});
							positions.unshift(position);
						}
						if(positions && positions.length >= 1){
							var distance = 0;//new AMap.LngLat(positions[0][0], positions[0][1]).distance(positions[1]);//计算两个坐标点之前的直线距离，单位米
							objects.push({'name':'轨迹_'+ (objects.length + 1), 'distance': distance, 'path': positions, 'data': waybill});
						}
					}
					if(objects && objects.length > 0){
						if(objects.length == 1){
							map.setCenter(objects[0].path[1]);
						}else{
							map.setCenter(objects[$.util.random(objects.length - 1)].path[1]);//随机选取一个点作为中心点
						}
						pathSimplifier.setData(objects);
						for(j = 0, len = objects.length; j < len; j++) {
							var obj = objects[j];
							var navigator = pathSimplifier.createPathNavigator(j, {//创建一个巡航器
					            'loop': true, //循环播放
					            'speed': 1000 //巡航速度，单位千米/小时
					        });
					        navigator.start(); //启动巡航器 
						}
					}
                    complete = true;
				});
				var positionPicker = new PositionPicker({
		            mode: 'dragMarker', map: map,
		            iconStyle: { //自定义外观
		                ancher: [24, 40], size: [48, 48],url: 'http://webapi.amap.com/ui/1.0/assets/position-picker2.png'
		            }
		        });
		        positionPicker.on('success', function(positionResult) {
		        	$("#lnglat").val(positionResult.position);
		        	$("#address").val(positionResult.address);
		        });
		        positionPicker.on('fail', function(positionResult) {
		       		clear();
		        });
		        var onModeChange = function(e) {
		            positionPicker.setMode(e.target.value)
		        }
		        var startButton = document.getElementById('start');
		        var stopButton = document.getElementById('stop');
		        AMap.event.addDomListener(startButton, 'click', function() {
		        	infoWindow.close();
		        	if(longitude!="" && longitude!=null){
		        		positionPicker.start(new AMap.LngLat(longitude,latitude))
		        	}else{
		        		positionPicker.start(map.getBounds().getSouthWest())
		        	}
		        })
		        AMap.event.addDomListener(stopButton, 'click', function() {
		        	clear();
		            positionPicker.stop();
		        })
				if(map_loading){
					map_loading.close();
				}
			});
		}else{
			$.util.error(data.message);
		}
	});
}

	function uploadPosition(){
		var waybillId = $("#waybillId").val();
		if(waybillId=="" || waybillId==null){
			$.util.warning("请选择任务信息");
			return;
		}
		var barcode = "任务单号："+$("#barcode").val();
		//坐标选中提交
		var lnglat = $("#lnglat").val();
		var address = $("#address").val();
		if(address=="" || lnglat==""){
			$.util.warning("请选择坐标位置");
			return;
		}
		$.util.confirm("是否提交任务单选中的位置", barcode+"</br>选择位置："+address, function(){
			var baseValue = {
				wbarcode  : $("#barcode").val(),
			    latitude  : lnglat.split(",")[1],
			    locations : address,
			    longitude : lnglat.split(",")[0],
			    waybillid :  $("#waybillId").val()
			}
			$.util.json(base_url + '/backstage/trace/uploadPosition',baseValue, function(data){
				clear();
			//	$(".position").hide();
	  	 		if(data.success){//处理返回结果  
	  	 			$.util.success(data.message);
	  	 			ajaxSearch();
	  	 		}else{//处理返回结果  
	  	 			$.util.error(data.message);
	  	 		}
	  	 	});
		});
	}
$(document).ready(function(){
	var hFlag = true;
    //设置地图的高度
    $('#container').height($(window).height() - 118);
    $(window).resize(function(){
    	$('#container').height($(window).height() - 118);
    });
    //回单操作
    $('#h4').click(function(){
        if( hFlag == true){
            $('.tip-icon').removeClass('icon-double-angle-up').addClass('icon-double-angle-down');
            $('.waybill-tips').animate({
                'bottom':0
            },300);
            hFlag = false;
        }else{
            $('.tip-icon').removeClass('icon-double-angle-down').addClass('icon-double-angle-up');
            $('.waybill-tips').animate({
                'bottom':'-160px'
            },300);
            hFlag = true;
        }
    })

    $('select').each(function(){
        $(this).val($(this).attr("val"));
    });

    ajaxSearch();
    //提交查询事件
	$(".content-search-btn").on("click", function(){
		ajaxSearch();
	});
	$("#comfirm").on("click", function(){
		uploadPosition();
	});
});

function clear(){
	$("#waybillId").val('');
	$("#barcode").val('');
	$("#lnglat").val('');
	$("#address").val('');
	$(".position").hide();
}
</script>
</html>