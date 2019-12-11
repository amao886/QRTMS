<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>运单地图</title>
<%@ include file="/views/include/head.jsp"%>
<link rel="stylesheet" href="${baseStatic}css/billmap.css?times=${times}" />
<link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css" />
<link rel="stylesheet" href="${baseStatic}plugin/css/jquery.mloading.css" />
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

.formBox{padding: 10px;background: #f8f8f8;}
@media screen and (max-width:767px){
    .form-group { margin-bottom:0; display: inline-block;vertical-align: middle;}
}
@media screen and (max-width:359px){
	.formBox{padding:10px 5px;}
    .form-group.first { width:50%;}
}
</style>
</head>
<body>
<div class="container-fluid">
  <div class="row formBox">
    <form class="form-inline" method="post">
	  <div class="form-group first">
	    <label class="sr-only" for="phone">单号/手机号</label>
	    <input type="text" class="form-control" id="phone" name="phone" value="15201748774" placeholder="单号/手机号">
	  </div>
	  <div class="form-group">
	    <label class="sr-only" for="billstatus">状态</label>
	    <select class="form-control" id="billstatus" name="billstatus" placeholder="状态">
		  <option value="1" selected>运输中</option>
		  <option value="2">签收</option>
		</select>
	  </div>
	  <button type="button" class="btn btn-default content-search-btn">查询</button>
	</form>
  </div>
  <div class="row">
    <div id="container" style="height: 100%;"></div>
  </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp"%>
<script type="text/javascript" src="https://webapi.amap.com/maps?v=1.4.0&key=a4e0b28c9ca0d166b8872fc3823dbb8a&plugin=AMap.ToolBar,AMap.Scale,AMap.OverView,AMap.MapType,AMap.Geocoder"></script>
<script src="https://webapi.amap.com/ui/1.0/main.js?v=1.0.11"></script>
<script src="${baseStatic}plugin/js/jquery.mloading.js"></script>
<script>
var caches = {};
function loadByDestination(geocoder, bills, index, callback){
	if(index >= bills.length){//递归结束条件
		callback(bills);
		return;
	}
	var bill = bills[index];
	if(bill.destination){
		if(caches[bill.destination]){  
			bill['location'] = caches[bill.destination];
			loadByDestination(geocoder, bills, ++index, callback);
		}else{
			geocoder.getLocation(bill.destination, function(status, result) {
		        if (status === 'complete' && result.info === 'OK') {
		        	if(result.geocodes && result.geocodes.length > 0){
		        		bill['location'] = result.geocodes[0].location;
		        		caches[bill.destination] = result.geocodes[0].location;
		        	}
		        }
		        loadByDestination(geocoder, bills, ++index, callback);
		    });
		}
	}else{
		loadByDestination(geocoder, bills, ++index, callback);
	} 
}

var mywindow = {
	context : function(lable, centext){
		var sss = "<div class='tip-item clearfix'>";
			sss += "<span class='tip-title fl'>"+ lable +"：</span>";
			sss += "<span class='tip-content fl'>";
			sss += centext;
			sss += "</span>";
			sss += "</div>";
		return sss;
	},
	title : function(title){
		return "<div>"+ title +"</div>";
	},
	billBody : function(bill){
		var sss = "<div class='map-tips'>";
		    sss += this.context('单号', bill.billNumber);
			sss += this.context('地址', bill.destination);
			sss +="<div class='msg-tips'>";
			//sss +="<button type='button' class='btn btn-default btn_send_msg' onclick='sendsms("+ track.waybillid +")'>短信提醒</button>";
			sss +="</div>";
    		sss +="</div>";
    	return sss;
	}
}

var map = new AMap.Map('container', { resizeEnable: true, zoom: 10, autoSetFitView:true });
function ajaxSearch(){
	if(map){
		map.destroy();
	}
	map = new AMap.Map('container', { resizeEnable: true, zoom: 10, autoSetFitView:true});
	var parmas = {}
	$('form').find('input, select').each(function(index, item){
		parmas[item.name] = item.value;
	});
	$.util.loading($('body'));
	$.util.json(base_url +'/backstage/map/bill', parmas, function(data){
		if(data.success){
			AMapUI.loadUI(['overlay/SimpleInfoWindow'], function(SimpleInfoWindow) {
				map.addControl(new AMap.ToolBar());//集成了缩放、平移、定位等功能按钮在内的组合控件
		        map.addControl(new AMap.Scale());//展示地图在当前层级和纬度下的比例尺
		        map.addControl(new AMap.OverView({isOpen:true}));//在地图右下角显示地图的缩略图
				var infoWindow = new SimpleInfoWindow({
					offset: new AMap.Pixel(0, -30)
				});
				var offset = new AMap.Pixel(-12,-12), positions = [];
				for (var i = 0; i < data.bills.length; i++) {
					var bill = data.bills[i];
					var position = [bill.latitude, bill.longitude];
					var customer = new AMap.Marker({'offset': offset, 'position': position, 'map': map, 'extData': bill, autoSetFitView:true});
					if(bill.billStatus == 1){
						customer.setContent("<div class='map_icon_red'></div>");
					}else{
						customer.setContent("<div class='map_icon_blue'></div>");
					}
					customer.on('click', function(){
						var e = this.getExtData();
						infoWindow.setInfoTitle(mywindow.title('信息'));
						infoWindow.setInfoBody(mywindow.billBody(e));
						infoWindow.open(map, this.getPosition());
					});
					positions.unshift(position);
				}
				if(positions && positions.length > 0){
					if(positions.length == 1){
						map.setCenter(positions[0]);
					}else{
						//随机选取一个点作为中心点
						map.setCenter(positions[$.util.random(positions.length - 1)]);
					}
				}
				$('body').mLoading('hide');
			});
		}else{
			$('body').mLoading('hide');
			$.util.error(data.message);
		}
	});
}
$(document).ready(function(){
    //提交查询事件
	$(".content-search-btn").on("click", function(){
		ajaxSearch();
	});
    
	mapSize();
	$(window).resize(function(){
		setTimeout(function(){
			mapSize();
		},200)		
	});
    function mapSize(){
    	var _wh = $(window).height(),
        _boxH = $(".formBox").outerHeight(),
        _h = _wh - _boxH;
    	$("#container").css({"height": _h + "px"});
    }
    
});
</script>
</html>