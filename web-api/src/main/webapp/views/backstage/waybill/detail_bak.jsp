<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>物流跟踪-任务单管理</title>
<%@ include file="/views/include/head.jsp"%>
<link rel="stylesheet" href="${baseStatic}css/viewer.css" />
<link rel="stylesheet" href="${baseStatic}css/billDetails.css?times=${times}" />
<style type="text/css">
body,html,#container{
  height: 100%;
  margin: 0px;
}
#container {
	width:100%; 
	height: 380px; 
	border: dashed 1px #000000; 
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
.dcontent-part h2 {
    font-size: 16px;
    font-weight: 400;
    padding: 10px 0px 10px;
}
.docs-pictures img{
	width: 100px !important;
	height: 68px;
}
.status{
	font-size: 12px; 
	line-height:1.8em;
	font-weight: bold;
	font-family: "微软雅黑";
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
	<div class="detail-content" id="main">
		<div class="dcontent-part" id="part">
			<h2>
				任务轨迹
				<c:if test="${waybill.waybillStatus == 10}">
					<span class="status" id="span_status">未绑定</span>
				</c:if>
				<c:if test="${waybill.waybillStatus == 20}">
					<span class="status" id="span_status">待运输</span>
					<c:if test="${waybill.delay == 0 || waybill.delay == 1}">
						(<span class="status" style="color: #F00;">已延迟</span>)
					</c:if>
				</c:if>
				<c:if test="${waybill.waybillStatus == 30 }">
					<span class="status" id="span_status">运输中</span>
					<c:if test="${waybill.delay == 0 || waybill.delay == 1}">
						(<span class="status" style="color: #F00;">已延迟</span>)
					</c:if>
				</c:if>
				<c:if test="${waybill.waybillStatus == 20 || waybill.waybillStatus == 30 }">
					<span class="fr">
						<input type="hidden" name="waybillid" value="${waybill.id }"/>
						<button type="button" class="btn btn-link btn_deliver_arrive">确认送达</button>
						<button type="button" class="btn btn-link btn_confirm_arrive">确认到货</button>
					</span>
				</c:if>
				<c:if test="${waybill.waybillStatus == 35 }">
					<span class="status" id="span_status">已送达</span>
					<c:if test="${waybill.delay == 0 || waybill.delay == 1}">
						(<span class="status" style="color: #F00;">已延迟</span>)
					</c:if>
					<span class="fr">
						<input type="hidden" name="waybillid" value="${waybill.id }"/>
						<button type="button" class="btn btn-link btn_confirm_arrive">确认到货</button>
					</span>
				</c:if>
				<c:if test="${waybill.waybillStatus == 40 }">
					<span class="status" id="span_status">已到货</span>
					<c:if test="${waybill.delay == 0 || waybill.delay == 1}">
						(<span class="status" style="color: #F00;">已延迟</span>)
					</c:if>
				</c:if>
			</h2>
			<!-- 这里放百度地图 -->
			<div id="container"></div>
			<div id="show_message" class="track-sub-part track-con-bg"></div>
		</div>
		<!-- 任务单详情 -->
		<div class="dcontent-part">
			<h2>任务单详情</h2>
			<div class="track-con track-con-bg">
				<div class="clearfix">
					<div class="fl">
						<span class="spa_n">送货单号:</span>
						<p class="text_p">${waybill.deliveryNumber}</p>
					</div>
					<div class="fl">
						<span class="spa_n">任务摘要:</span>
						<p class="text_p">${waybill.orderSummary}
						</p>
					</div>
				</div>
				<div class="clearfix">
					<div class="fl">
						<span class="spa_n">任务单号:</span>
						<p class="text_p">${waybill.barcode}</p>
					</div>
					<div class="fl">
						<span class="spa_n">联系人:</span>
						<p class="text_p">${waybill.contactName }</p>
					</div>
				</div>
				<div class="clearfix">
				    <div class="fl">
                        <span class="spa_n">发货人:</span>
                        <p class="text_p">${waybill.shipperName}</p>
                    </div>
					<div class="fl">
                        <span class="spa_n">收货客户:</span>
                        <p class="text_p">${waybill.receiverName }</p>
                    </div>
				</div>
				<div class="clearfix">
					<div class="fl">
                        <span class="spa_n">联系电话:</span>
                        <p class="text_p">${waybill.contactPhone }</p>
                    </div>
                    <div class="fl">
						<span class="spa_n">发货时间:</span>
						<p class="text_p">
							<c:if test="${waybill.createtime != null}">
								<someprefix:dateTime date="${waybill.createtime}" pattern="yyyy-MM-dd HH:mm"/>
							</c:if>
						</p>
                    </div>
				</div>
				<div class="clearfix">
					<div class="fl">
                        <span class="spa_n">收货地址:</span>
                        <p class="text_p">${waybill.receiveAddress }</p>
                    </div>
					<div class="fl">
                        <span class="spa_n">要求到货时间:</span>
                        <p class="text_p">
                        <c:if test="${waybill.arrivaltime != null}">
                            <someprefix:dateTime date="${waybill.arrivaltime}" pattern="yyyy-MM-dd HH:mm"/>
                        </c:if>
                        </p>
                    </div>
				</div>
					<div class="clearfix">
						<div class="fl">
	                        <span class="spa_n">座机号:</span>
	                        <p class="text_p">${waybill.receiverTel }</p>
						</div>
						<div class="fl">
							<span class="spa_n">到货时间:</span>
							<p class="text_p">
								<c:if test="${waybill.actualArrivalTime != null}">
									<someprefix:dateTime date="${waybill.actualArrivalTime}" pattern="yyyy-MM-dd HH:mm"/>
								</c:if>
							</p>
						</div>
					</div>
				<div class="clearfix table-style">
				    <table>
				        <thead>
		                    <tr>
		                        <th>客户料号</th>
		                        <th>物料名称</th>
		                        <th>重量（kg）</th>
		                        <th>体积（m<sup>3</sup>）</th>
		                        <th>数量（件）</th>
		                        <th>订单摘要</th>
		                    </tr>
		                </thead>
		                <tbody>
						<c:if test="${waybill.goods == null || fn:length(waybill.goods) <= 0}">
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
						</c:if>
						<c:forEach items="${waybill.goods}" var="goods">
							<tr>
								<td>${goods.goodsType}</td>
								<td>${goods.goodsName}</td>
								<td>${goods.goodsWeight}</td>
								<td>${goods.goodsVolume}</td>
								<td>${goods.goodsQuantity}</td>
								<td>${goods.summary}</td>
							</tr>
						</c:forEach>

		                </tbody>
				    </table>
				</div>
			</div>
		</div>
		<!-- 回单信息 -->
		<div class="dcontent-part">
			<h2>回单信息</h2>
			<div class="track-con">
				<c:if test="${waybill.receipts == null || fn:length(waybill.receipts) <= 0}">
				 	<div class="track-sub-part track-con-bg">暂无回单信息</div>
				</c:if>
				<c:forEach items="${waybill.receipts}" var="receipt">
					<div class="track-sub-part track-con-bg">
						<div class="clearfix">
							<div class="fl">
								<span class="spa_n">上传时间:</span>
								<p class="text_p">
									<c:if test="${receipt.createtime != null}">
										<someprefix:dateTime date="${receipt.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
									</c:if>
								</p>
							</div>
							<div class="fl">
								<span class="spa_n">上传人:</span>
								<p class="text_p">
									<c:if test="${receipt.user != null }">
										${receipt.user.mobilephone }
									</c:if>
								</p>
							</div>
						</div>
						<div class="bill_test">
							<ul class="clearfix paddin_g docs-pictures">
								<c:forEach items="${receipt.images}" var="image">
									<li>
										<img src="${imagePath}${image.path}" alt="回单上传图片" title="${image.verifyRemark}">
										<c:if test="${image.verifyStatus != null && image.verifyStatus == 1}">
											<span class="img-status">合格</span>
										</c:if>
										<c:if test="${image.verifyStatus != null && image.verifyStatus == 0}">
											<span class="img-status img-unqualified">不合格</span>
										</c:if>
									</li>	
								</c:forEach>
							</ul>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>
		
		<!-- 异常情况 -->
		<div class="dcontent-part">
			<h2>异常情况</h2>
			<div class="track-con">
				<c:if test="${waybill.exceptions == null || fn:length(waybill.exceptions) <= 0}">
				 	<div class="track-sub-part track-con-bg">没有异常信息</div>
				</c:if>
				<c:forEach items="${waybill.exceptions}" var="exception">
					<div class="track-sub-part track-con-bg">
						<div class="clearfix">
							<div class="fl">
								<span class="spa_n">异常情况:</span>
								<p class="text_p">${exception.content }</p>
							</div>
							<div class="fl">
								<span class="spa_n">任务摘要:</span>
								<p class="text_p">${waybill.orderSummary }</p>
							</div>
						</div>
						<div class="clearfix">
							<div class="fl">
								<span class="spa_n">上报时间:</span>
								<p class="text_p">
									<c:if test="${exception.createtime != null}">
										<someprefix:dateTime date="${exception.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
									</c:if>
								</p>
							</div>
							<div class="fl">
								<span class="spa_n">上报人:</span>
								<p class="text_p">
									<c:if test="${exception.user != null}">
										${exception.user.mobilephone}
									</c:if>
								</p>
							</div>
						</div>
						<div class="bill_test">
							<ul class="clearfix paddin_g docs-pictures">
								<c:forEach items="${exception.images}" var="image">
									<li><img src="${imagePath}${image.path }" alt="异常上传图片"></li>	
								</c:forEach>
							</ul>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>
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
	</div>
</body>
<%@ include file="/views/include/floor.jsp"%>
<script type="text/javascript" src="http://webapi.amap.com/maps?v=1.4.0&key=a4e0b28c9ca0d166b8872fc3823dbb8a"></script>
<script src="http://webapi.amap.com/ui/1.0/main.js?v=1.0.11"></script>
<script type="text/javascript" src="${baseStatic}js/viewer.min.js"></script>
<script type="text/javascript" src="${baseStatic}js/date.extend.js"></script>
<script>
$(document).ready(function(){
	var map = new AMap.Map('container', { resizeEnable: true, zoom:15 });
	AMapUI.loadUI(['overlay/SimpleInfoWindow', 'misc/PathSimplifier'], function(SimpleInfoWindow, PathSimplifier) {
		map.plugin(['AMap.ToolBar','AMap.Scale','AMap.OverView','AMap.MapType'],function(){
		        map.addControl(new AMap.ToolBar());//集成了缩放、平移、定位等功能按钮在内的组合控件
		        map.addControl(new AMap.Scale());//展示地图在当前层级和纬度下的比例尺
		        map.addControl(new AMap.OverView({isOpen:true}));//在地图右下角显示地图的缩略图
		});
		var offset = new AMap.Pixel(-12,-12);
		var infoWindow = new SimpleInfoWindow({
			offset: new AMap.Pixel(0, -12)
		});
		var items = $(".track li"), positions = [];
		if(items && items.length > 0){
			var carIcon = new AMap.Icon({
				'size': new AMap.Size(70, 32), //图标尺寸 
				'image': $(".map_marker_icon").attr("src") //	图标的取图地址。默认为蓝色图钉图片
			});
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
		}
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
		map.setFitView();
	});

	//确认到货事件
	$(".btn_confirm_arrive").on("click", function(){
		var my = $(this);
		var waybillid = $(my).parent().find(":hidden[name='waybillid']").val();
		var num = $("ul").length;
		if(num>0){
			confirmEnd(my, '是否确认到货及未审核的回单都设置为合格？</br><a href="${basePath}/backstage/receipt/search?waybill.id='+ waybillid+'">回单审核</a>',waybillid);
		}else{
			confirmEnd(my, '是否确认到货？', waybillid);
		}
	});
	
	function confirmEnd(my, msg, waybillid){
		$.util.confirm('到货确认', msg, function(){
            $.post(base_url + '/backstage/trace/confirm/'+ waybillid, {} ,function(data){
            	if(data.success){//处理返回结果  
            		$(my).parent().html('已到货');
            		$("#span_status").html("已到货");
    			}else{
    				$.util.error(data.message);	
    			} 
            },'json');
		});
	}
	
	//已送达货事件
	$(".btn_deliver_arrive").on("click", function(){
		var my = $(this);
		var waybillid = $(my).parent().find(":hidden[name='waybillid']").val();
		$.util.confirm('送达确认', '是否确认送达？', function(){
            $.post(base_url + '/backstage/trace/deliver/'+ waybillid, {} ,function(data){
            	if(data.success){//处理返回结果  
            		$(my).hide();
            		$("#span_status").html("已送达");
    			}else{
    				$.util.error(data.message);	
    			} 
            },'json');
		});
	});
	$(".docs-pictures").viewer({
	    fullscreen: false
	});
});
</script>
</html>