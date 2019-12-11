<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>物流跟踪-轨迹跟踪</title>
<%@ include file="/views/include/head.jsp"%>
<link rel="stylesheet" href="${baseStatic}css/pathway.css?times=${times}" />
<link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css" />
<style type="text/css">
body,html, #container{
  height: 100%;
  margin: 0px;
  padding: 0px
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
</style>
</head>
<body>
 <div class="custom-content">
     	 <form id='searchWaybillForm' action="${basePath}/backstage/trace/follow" method="post">
     <div class="content-search">
         <div class="clearfix">
             <div class="fl col-middle">
                 <label class="labe_l">资源组名:</label>
                 <select class="tex_t selec_t" name="groupid">
					<option value="0">全部</option>
					<c:forEach items="${groups}" var="group">
						<c:if test="${condition.groupid == group.id }">
							<option value="${ group.id}" selected>${group.groupName }</option>
						</c:if>
						<c:if test="${condition.groupid != group.id }">
							<option value="${ group.id}">${group.groupName }</option>
						</c:if>
					</c:forEach>
				</select>
             </div>
             <div class="fl col-middle">
                 <label class="labe_l">送货单号:</label>
                 <input type="text" class="tex_t" name="deliveryNumber" value="${condition.deliveryNumber }" placeholder="请输入送货单号">
             </div>
             <div class="fl col-max">
                 <label class="labe_l">发货日期:</label>
                 <div class="clearfix tex_t reset_text">
                     <div class="input-append date fl"  id="datetimeStart">
                         <input type="text" name="bindStartTime" value="${condition.bindStartTime}" readonly>
                         <span class="add-on"><i class="icon-th"></i></span> 
                     </div>
                     <span class="to_link">至</span>
                     <div class="input-append date fl"  id="datetimeEnd">
                         <input type="text" name="bindEndTime" value="${condition.bindEndTime}" readonly>
                         <span class="add-on"><i class="icon-th"></i></span> 
                     </div>
                 </div>
             </div>
             
         </div>
         <div><a href="javascript:;" class="content-search-btn">查询</a></div>
     </div>
         </form>
     <!-- 地图放container中 -->
     <div id="container" style="height: 100%;"></div>
     <div style="display: none">
     		<!-- 回单信息 -->
		<div id="track-list" >
			<img class="map_marker_icon" src="${baseStatic}images/newcar.png">
			<c:forEach items="${waybills }" var="waybill">
				<ol class="waybill" barcode="${waybill.barcode.barcode }" address="${waybill.customer.address}">
					<c:forEach items="${waybill.tracks}" var="track">
					<li class="track" latitude="${track.latitude }" longitude="${track.longitude }">
						<div class="infoTitle">${track.user.unamezn }</div>
						<div class="infoBody">
					         <div class="map-tips">
					             <div class="tip-item clearfix">
					                 <span class="tip-title fl">任务单号：</span>
					                 <span class="tip-content fl">
					                 	<a href="${basePath}/backstage/trace/findById/${waybill.id}">${waybill.barcode.barcode }</a>
					                 </span>
					             </div>
					             <div class="tip-item clearfix">
					                 <span class="tip-title fl">送货单号：</span>
					                 <span class="tip-content fl">${waybill.deliveryNumber }</span>
					             </div>
					             <div class="tip-item clearfix">
					                 <span class="tip-title fl">上报位置：</span>
					                 <span class="tip-content fl">${track.locations }</span>
					             </div>
					             <div class="tip-item clearfix">
					                 <span class="tip-title fl">上报时间：</span>
					                 <span class="tip-content fl">
					                 	<someprefix:dateTime millis="${track.createtime}" pattern="yyyy年MM月dd日 HH:mm:ss" />
					                 </span>
					             </div>
					             <div class="tip-item clearfix">
					                 <span class="tip-title fl">联系方式：</span>
					                 <span class="tip-content fl">${track.user.mobilephone }</span>
					             </div>
					             <div class="tip-item tip-item-long clearfix">
					                 <span class="tip-title fl">要求到货时间：</span>
					                 <span class="tip-content fl">2017年09月05日 01:23:26</span>
					             </div>
					             <div class="tip-item clearfix">
					                 <span class="tip-title fl">收货信息：</span>
					                 <span class="tip-content fl">2017年09月05日 01:23:26</span>
					             </div>
					             <div class="tip-item clearfix">
					                 <span class="tip-title fl">收货客户：</span>
					                 <span class="tip-content fl">${waybill.customer.companyName }</span>
					             </div>
					              <div class="tip-item clearfix">
					                 <span class="tip-title fl">联系人员：</span>
					                 <span class="tip-content fl">${waybill.customer.contacts }</span>
					             </div>
					             <div class="tip-item clearfix">
					                 <span class="tip-title fl">联系方式：</span>
					                 <span class="tip-content fl">
					                 <c:if test="waybill.customer.contactNumber != null">
					                 	${waybill.customer.contactNumber }
					                 </c:if>
					                 <c:if test="waybill.customer.tel != null">
					                 	(${waybill.customer.tel })
					                 </c:if>
					                 </span>
					             </div>
					              <div class="tip-item clearfix">
					                 <span class="tip-title fl">收货地址：</span>
					                 <span class="tip-content fl">${waybill.customer.address }</span>
					             </div>
					             <div class="msg-tips">
					       			 <button type="button" class="btn btn-default btn_send_msg" onclick="sendsms('${waybill.id}')">短信提醒</button>     
					             </div>
					         </div>
						</div>
					</li>	
					</c:forEach>	
				</ol>	
			</c:forEach>
		</div>
     </div>
 </div>
 <!-- 回单提醒 
 <div class="waybill-tips">
     <h4 id="h4">回单提醒<i class="tip-icon icon-double-angle-up" aria-hidden="true"></i></h4>
     <ul>
         <li>
         <span class="wb-tips-name">任务单号</span>
         <span class="wb-tips-num">20170706011291</span>
         <span class="wb-tips-new">有新回单！</span>
         </li>
         <li>
         <span class="wb-tips-name">任务单号</span>
         <span class="wb-tips-num">20170706011291</span>
         <span class="wb-tips-new">有新回单！</span>
         </li>
         <li>
         <span class="wb-tips-name">任务单号</span>
         <span class="wb-tips-num">20170706011291</span>
         <span class="wb-tips-new">有新回单！</span>
         </li>
         <li>
         <span class="wb-tips-name">任务单号</span>
         <span class="wb-tips-num">20170706011291</span>
         <span class="wb-tips-new">有新回单！</span>
         </li>
         <li>
         <span class="wb-tips-name">任务单号</span>
         <span class="wb-tips-num">20170706011291</span>
         <span class="wb-tips-new">有新回单！</span>
         </li>
         <li>
         <span class="wb-tips-name">任务单号</span>
         <span class="wb-tips-num">20170706011291</span>
         <span class="wb-tips-new">有新回单！</span>
         </li>
     </ul>
 </div>
 -->
</body>
<%@ include file="/views/include/floor.jsp"%>
<script type="text/javascript" src="https://webapi.amap.com/maps?v=1.4.0&key=a4e0b28c9ca0d166b8872fc3823dbb8a"></script>
<script src="https://webapi.amap.com/ui/1.0/main.js?v=1.0.11"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${baseStatic}js/datetimepicker.js"></script>
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
$(document).ready(function(){
	var hFlag = true;
    //设置地图的高度
    $('#container').height($(window).height() - 67);
    $(window).resize(function(){
    	$('#container').height($(window).height() - 67);
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
    
    //提交查询事件
	$(".content-search-btn").on("click", function(){
		$("#searchWaybillForm").submit();
	});
	
	var map = new AMap.Map('container', { resizeEnable: true, zoom: 15 });
	AMapUI.loadUI(['overlay/SimpleInfoWindow', 'misc/PathSimplifier'], function(SimpleInfoWindow, PathSimplifier) {
		map.plugin(['AMap.ToolBar','AMap.Scale','AMap.OverView','AMap.MapType','AMap.Geocoder'],function(){
		        map.addControl(new AMap.ToolBar());//集成了缩放、平移、定位等功能按钮在内的组合控件
		        map.addControl(new AMap.Scale());//展示地图在当前层级和纬度下的比例尺
		        map.addControl(new AMap.OverView({isOpen:true}));//在地图右下角显示地图的缩略图
		});
		var carIcon = new AMap.Icon({
			'size': new AMap.Size(70, 32), //图标尺寸 
			'image': $(".map_marker_icon").attr("src") //	图标的取图地址。默认为蓝色图钉图片
		});
		var infoWindow = new SimpleInfoWindow({
			offset: new AMap.Pixel(0, -30)
		});
		var objects = [], geocoder = new AMap.Geocoder();
		$("#track-list").children(".waybill").each(function(windex, welement){
			var address = $(welement).attr("address");
			console.log(address);
	        var flag = false, positions = [];
	      //地理编码,返回地理编码结果
        	geocoder.getLocation(address, function(status, result) {
	            if (status === 'complete' && result.info === 'OK') {
	                console.log(result);
	            	if(result.geocodes && result.geocodes.length > 0){
	                	positions.push([result.geocodes[0].location.getLng(), result.geocodes[0].location.getLat()]);
	                }
	            }
	            flag = true;
	        });
	        console.log(positions);
	        /*
			var track = $(welement).children().first();
			var position = [$(track).attr("longitude"), $(track).attr("latitude")];
			var marker = new AMap.Marker({'position':position, 'map': map, 'extData': element});
			marker.on('click', function(){
				var e = this.getExtData();
				var e = this.getExtData();
				infoWindow.setInfoTitle($(e).find('.infoTitle').clone());
				infoWindow.setInfoBody($(e).find('.infoBody').clone());
				infoWindow.open(map, this.getPosition());	
			});
			positions.push([$(track).attr("longitude"), $(track).attr("latitude")]);
			if(positions && positions.length > 1){
				var distance = new AMap.LngLat(positions[0][0], positions[0][1]).distance(positions[1]);//计算两个坐标点之前的直线距离，单位米
				objects.push({'name':'轨迹_'+ (objects.length + 1), 'distance': distance, 'positions': positions});
			}
			*/
		});
		if(objects && objects.length > 0){
			if(objects.length == 1){
				map.setCenter(objects[0].positions[1]);
			}else{
				//随机选取一个点作为中心点
				map.setCenter(objects[$.util.random(objects.length - 1)].positions[1]);
			}
			var simplifiers = [], polylines = [];
			for(j = 0, len = objects.length; j < len; j++) {
				var obj = objects[j];
				if(obj.distance > 100 && PathSimplifier.supportCanvas){//两点之间的距离大于100米并且支持轨迹展示
					simplifiers.push({'name': obj.name, 'path': obj.positions})
				}else{
					polylines.push(obj.positions);
				}
			}
			if(simplifiers && simplifiers.length > 0){
				var pathSimplifier = new PathSimplifier({
		            zIndex: 100,
		            autoSetFitView:false,
		            map: map, //所属的地图实例
		            getPath: function(pathData, pathIndex) {
		                return pathData.path;
		            },
		            renderOptions: {
		                renderAllPointsIfNumberBelow: 100 //绘制路线节点，如不需要可设置为-1
		            },
		            data: simplifiers
		        });
				for(j = 0, len = simplifiers.length; j < len; j++) {
					var obj = simplifiers[j];
					var navigator = pathSimplifier.createPathNavigator(j, {//创建一个巡航器
			            'loop': true, //循环播放
			            'speed': 2000 //巡航速度，单位千米/小时
			        });
			        navigator.start(); //启动巡航器 
				}
			}
			if(polylines && polylines.length > 0){
				for(j = 0, len = polylines.length; j < len; j++) {
					var polyline = new AMap.Polyline({
		    	        'strokeColor': "#3366FF", //线颜色
		    	        'strokeOpacity': 1,       //线透明度
		    	        'strokeWeight': 3,        //线宽
		    	        'strokeStyle': "dashed",   //线样式
		    	        'strokeDasharray': [10, 5], //补充线样式
		    	        'lineJoin': 'round' //折线拐点的绘制样式
		    	    });
		    		polyline.setPath(polylines[j]);//设置线覆盖物路径
		    		polyline.setMap(map); 
				}
			}
		}
	});
});
</script>
</html>