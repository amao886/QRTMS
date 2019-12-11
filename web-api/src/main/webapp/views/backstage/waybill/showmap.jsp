<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>物流跟踪-任务单管理</title>
<%@ include file="/views/include/head.jsp"%>
<link rel="stylesheet" href="${baseStatic}css/billDetails.css?times=${times}" />
<style type="text/css">
body,html,#container{
  height: 100%;
  margin: 0px;
  padding: 0px
}
#container {
	width:100% !important; 
	height: 100% !important; 
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
</style>
</head>
<body>
	<!-- 这里放百度地图 -->
	<div id="container"></div>
	<!-- 回单信息 -->
	<div class="track" >
		<img class="map_marker_icon" src="${baseStatic}images/newcar.png">
		<c:forEach items="${waybill.tracks }" var="track">
			<li class="marker-tip" latitude="${track.latitude }" longitude="${track.longitude }">
				<div class="infoTitle">&nbsp;&nbsp;&nbsp;</div>
				<div class="infoBody">
					<div class="marker-tip-body">
						<b>地址:</b>${track.locations }
					</div>
					<div class="marker-tip-body">
						<b>日期:</b><someprefix:dateTime date="${track.createtime}" pattern="yyyy年MM月dd日 HH:mm:ss" />
					</div>
					<div class="marker-tip-body">
						<b>联系人:</b>${track.user.mobilephone }
					</div>
				</div>
			</li>			
		</c:forEach>
		<span id="customer_info" address="${waybill.receiveAddress}" latitude="${waybill.latitude}" longitude="${waybill.longitude}" fenceStatus="${waybill.fenceStatus }" fenceRadius="${waybill.fenceRadius }">
			<div class="infoTitle">收获客户信息</div>
			<div class="infoBody">
				<div class="marker-tip-body">
					<b>名称:</b>${waybill.receiverName }
				</div>
				<div class="marker-tip-body">
					<b>地址:</b>${waybill.receiveAddress }
				</div>
				<div class="marker-tip-body">
					<b>联系人:</b>${track.user.mobilephone }
				</div>
			</div>
		</span>
	</div>
</body>
<%@ include file="/views/include/floor.jsp"%>
<script type="text/javascript" src="https://webapi.amap.com/maps?v=1.4.0&key=a4e0b28c9ca0d166b8872fc3823dbb8a"></script>
<script src="https://webapi.amap.com/ui/1.0/main.js?v=1.0.11"></script>
<script type="text/javascript" src="${baseStatic}js/date.extend.js"></script>
<script>
$(document).ready(function(){
	var map = new AMap.Map('container', { resizeEnable: true, zoom: 15 });
	AMapUI.loadUI(['overlay/SimpleInfoWindow', 'misc/PathSimplifier'], function(SimpleInfoWindow, PathSimplifier) {
		map.plugin(['AMap.ToolBar','AMap.Scale','AMap.OverView','AMap.MapType'],function(){
		        map.addControl(new AMap.ToolBar());//集成了缩放、平移、定位等功能按钮在内的组合控件
		        map.addControl(new AMap.Scale());//展示地图在当前层级和纬度下的比例尺
		        map.addControl(new AMap.OverView({isOpen:true}));//在地图右下角显示地图的缩略图
		});
		var offset = new AMap.Pixel(-12,-12);
		var infoWindow = new SimpleInfoWindow({
			offset: new AMap.Pixel(0,-12)
		});
		var items = $(".track li"), positions = [];
		$(items).each(function(index, element){
			var position = [$(element).attr("longitude"), $(element).attr("latitude")];
			var marker = new AMap.Marker({'offset': offset, 'map': map, 'position': position });
			marker.setContent("<div class='map_icon_blue'></div>");
			marker.setExtData(element);
			marker.on('click', function(){
				var e = this.getExtData();
				infoWindow.setInfoTitle($(e).find('.infoTitle').clone());
				infoWindow.setInfoBody($(e).find('.infoBody').clone());
				infoWindow.open(map, this.getPosition());	
			});
			positions.unshift(position);
			if(index == 0){
			    map.setCenter(position);// 圆心位置
				infoWindow.setInfoTitle($(element).find('.infoTitle').clone());
				infoWindow.setInfoBody($(element).find('.infoBody').clone());
				infoWindow.open(map, marker.getPosition());	
			}
		});

		var citem = $("#customer_info");
		var position = [$(citem).attr("longitude"), $(citem).attr("latitude")];
		if(position && position[0] && position[1]){
			var position = [$(citem).attr("longitude"), $(citem).attr("latitude")];
			var customer = new AMap.Marker({'offset': offset, 'position': position, 'map': map});
			customer.setContent("<div class='map_icon_red'></div>");
			customer.setExtData(citem);
			customer.on('click', function(){
				var e = this.getExtData();
				infoWindow.setInfoTitle($(e).find('.infoTitle').clone());
				infoWindow.setInfoBody($(e).find('.infoBody').clone());
				infoWindow.open(map, this.getPosition());	
			});
			positions.push(position);
			var fenceStatus = $(citem).attr("fenceStatus");
			if(fenceStatus && fenceStatus == 1){
				var circle = new AMap.Circle({
			        radius: $(citem).attr("fenceRadius") * 1000, //半径
			        strokeColor: "#3366FF", //线颜色
			        strokeOpacity: 0.8, //线透明度
			        strokeWeight: 1, //线粗细度
			        fillColor: "#1791fc", //填充颜色
			        fillOpacity: 0.2//填充透明度
			    });
			    circle.setCenter(position);// 圆心位置
			    circle.setMap(map);
			    circle.show();
			}
		}
		if(positions.length > 1){//轨迹数量大于1
			if (PathSimplifier.supportCanvas) {
				var pathSimplifierIns = new PathSimplifier({
		            zIndex: 100,
		            autoSetFitView:true,
		            map: map, //所属的地图实例
		            getPath: function(pathData, pathIndex) {
		                return pathData.path;
		            },
		            renderOptions: {
		            	getPathStyle: function(item, zoom){
		            		var color = '#00CD00';
		            		return {
		            			pathLineStyle: { strokeStyle: color },
		                        pathNavigatorStyle: {
		                        	fillStyle:color, strokeStyle : color, lineWidth: 1, pathLinePassedStyle: { strokeStyle : color }
					            }
		            		}
		            	}
		            },
		            data:[{ name: '运输轨迹', path: positions.slice(positions.length - 2) }]
		        });
		        //对第一条线路（即索引 0）创建一个巡航器
		        var navigator = pathSimplifierIns.createPathNavigator(0, {
		            loop: true, //循环播放
		            speed: 500 //巡航速度，单位千米/小时
		        });
		        navigator.start();
	        }
			if(positions.length > 2){
				var polyline = new AMap.Polyline({
	    	        'strokeColor': "#3366FF", //线颜色
	    	        'strokeOpacity': 1,       //线透明度
	    	        'strokeWeight': 3,        //线宽
	    	        'strokeDasharray': [10, 5], //补充线样式
	    	        'lineJoin': 'round' //折线拐点的绘制样式
	    	    });
	    		polyline.setPath(positions.slice(0, positions.length - 1));//设置线覆盖物路径
	    		polyline.setMap(map);
			}
		}
	});
});
</script>
</html>