//var wayBillId = parseInt(_common.getUrlParam('waybillId'));

$(function(){
	//显示loading
	$.showLoading("配送地址识别中");
	
	//设置地图容器的高度；
	var _wh = $(window).height();
	$("#container").css({"height":_wh +"px"});
	$(".boxCon").css({"height":_wh +"px"});
	
	//请求参数
	var baseValue = [];
	var _id = _common.getUrlParam('val');	
	var _addrType = _common.getUrlParam('type');
	if(_id != ""){
		baseValue = _id.split(",");
	}
		
	getMarks();
	function getMarks(){			
		var markers = [];		
		var latitude,longitude;			
		EasyAjax.ajax_Post_Json({
			url:'mobile/dispatch/planning/'+_addrType,
			data:JSON.stringify(baseValue)
		},function(data){
			if(data.success){	
				var _prev="";
				
				//加载地图
				var map = new AMap.Map('container',{
				    resizeEnable: true,
				    zoom:11			    
				});
				
				//骑行导航
		        /*var riding = new AMap.Riding({
		            map: map,
		            hideMarkers:true
		        });*/
		        
		        //步行导航
			    var walking = new AMap.Walking({
			        map: map,
			        hideMarkers:true,
			        autoFitView:false
			    });
			    			    			    			    
				// 实例化点标记
			    for(var i=0 ; i <= data.address.length ; i++){
			    	if(i < data.address.length){
			    	    var _data = data.address[i];
				    	var locArr = [_data.longitude,_data.latitude];
				    	var marker = new AMap.Marker({
				            icon: "../../images/mappoint.png",
				            bubble:false,
				            position: locArr
				        });
				        marker.setMap(map);
				        marker.emit('click', {target: marker});
				        markers.push(marker);
				        marker.extData =   _data.address 
				                         + '<div class="packageBox"><span id="count" class="hide">此处有'+ (_data.packageCount != null ? _data.packageCount : 0)+'件包裹</span><i data-id="'+ _data.id +'" data-addr="'+ _data.address +'" data-lon="'+ _data.longitude +'" data-lat="'+ _data.latitude +'" id="editBtn">修改地址</i></div>'
				                         + '<input type="hidden" id="mIndex" value="'+ i +'">';
				        marker.on('click', markerClick);
				    }else{
				    	wx.ready(function(){
					    	wx.getLocation({
							    type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
							    success: function (res) {
							        latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
							        longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
							        var speed = res.speed; // 速度，以米/每秒计
							        var accuracy = res.accuracy; // 位置精度
							        
							        var marker = new AMap.Marker({
							            icon: "../../images/mapflag.png",
							            bubble:false,
							            position: [longitude,latitude]
							        });
							        marker.setMap(map);
							        markers.push(marker);
							        
							        //自适应标注点
				 					map.setFitView();
							    },
								fail: function (res) {
									$.toast("定位失败，请检查GPS是否开启", "text");
								}
							});			    	
					    })
				    }
			    }
			    //map.setCenter(markers[0].getPosition());			    	    			    
			    //自适应标注点
				map.setFitView();
	 			
	 			//隐藏loading
	 			$.hideLoading();
	 			
	 			//点聚合
	 			/*var count = markers.length;
	     		var cluster = new AMap.MarkerClusterer(map, markers, {
	     		      gridSize:30,
	     		      maxZoom:17,
	     		      averageCenter:true,
	     		      renderCluserMarker: function (context) {
	         		      var factor = Math.pow(context.count/count,1/18)
	         		      var div = document.createElement('div');
	         		      var Hue = 180 - factor* 180;
	         		      var bgColor = 'hsla('+Hue+',100%,50%,0.7)';
	         		      var fontColor = 'hsla('+Hue+',100%,20%,1)';
	         		      var borderColor = 'hsla('+Hue+',100%,40%,1)';
	         		      var shadowColor = 'hsla('+Hue+',100%,50%,1)';
	         		      div.style.backgroundColor = bgColor
	         		      var size = Math.round(30 + Math.pow(context.count/count,1/5) * 20);
	         		      div.style.width = div.style.height = size+'px';
	         		      div.style.border = 'solid 1px '+ borderColor;
	         		      div.style.borderRadius = size/2 + 'px';
	         		      div.style.boxShadow = '0 0 1px '+ shadowColor;
	         		      div.innerHTML = context.count +"件";
	         		      div.style.lineHeight = size+'px';
	         		      div.style.color = fontColor;
	         		      div.style.fontSize = '14px';
	         		      div.style.textAlign = 'center';
	         		      context.marker.setOffset(new AMap.Pixel(-size/2,-size/2));
	         		      context.marker.setContent(div)
	         		 }
	     		});*/
	 			
	 			//marker点击事件
			 	function markerClick(e) {
			 		var _pos = e.target.getPosition();
			 		$("#addr").html(e.target.extData);
			 		if(_addrType == 2){
			 			$("#count").show();
			 			$("#editBtn").css({"float":"right"});
			 		}
			 		$("#addrInfo").animate({"bottom":0},300);
			 		
			        //根据起终点坐标规划骑行路线
			        /*riding.search([longitude,latitude], _pos ,function(status, result) {
			            if( status == 'complete'){			
			                var distance = result.routes[0].distance/1000;//骑行距离
			                var time = result.routes[0].time/60;//骑行时间 
							$("#routeTips").html("距离："+ distance.toFixed(2) +"千米&nbsp;&nbsp;骑行耗时："+ Math.ceil(time) +"分钟");	               
			            }			
			        });*/
			        //根据起终点坐标规划步行路线
    				walking.search([longitude,latitude], _pos ,function(status, result) {
			            if( status == 'complete'){			
			                var distance = result.routes[0].distance/1000;//步行距离
			                var wTime = Math.ceil(result.routes[0].time/60);//步行时间 
			                var rTime = Math.ceil(wTime / 3.2);
			                
							$(".distanceBox").text(distance.toFixed(2) + "千米");
							$(".rideTime").text(rTime + "分钟");
							$(".walkTime").text(wTime + "分钟");
			 				map.setZoomAndCenter(14, _pos);
							//$("#routeTips").html("距离："+ distance.toFixed(2) +"千米&nbsp;&nbsp;骑行耗时："+ Math.ceil(time) +"分钟");	               
			            }			
			        });
			        
			        e.target.setIcon("../../images/mapnow.png"); 			        
			        if(_prev != "" && _prev != $("#addrInfo").find("#mIndex").val()){
			 			markers[_prev].setIcon("../../images/mappoint.png");
			 	    }
			        	
			        _prev = $("#addrInfo").find("#mIndex").val();
			        return _prev;
			    }
			 				 				 				 	
				//到这儿去 ——导航
			 	$("#goThere").unbind("click").click(function(){
			 		var _index = $("#mIndex").val(),
			 			_goLat = Number($("#editBtn").attr("data-lat")),
			 		    _goLon = Number($("#editBtn").attr("data-lon")),
			 		    _goAddr = $("#editBtn").attr("data-addr");
			 		//获取地理位置	
					wx.ready(function(){
						wx.openLocation({
						    latitude: _goLat, // 纬度，浮点数，范围为90 ~ -90
						    longitude: _goLon, // 经度，浮点数，范围为180 ~ -180。
						    name: '', // 位置名
						    address: _goAddr, // 地址详情说明
						    scale: 14, // 地图缩放级别,整形值,范围从1~28。默认为最大
						    infoUrl: api_header + '/wechat/view/partener/address.html' // 在查看位置界面底部显示的超链接,可点击跳转
						});												
					});
			 	});				
			}else{
				$.toast(data.message, "cancel");
			}
		});
	}
	
	
	//编辑按钮
 	$("#addrInfo").on("click", "#editBtn", function() {		
		var _addr = $("#editBtn").attr("data-addr");
		var _id = $("#editBtn").attr("data-id");
		var _index = $("#mIndex").val()
        $.prompt({
          title: "修改地址",
          input: _addr,
          empty: false, //不为空
          onOK: function(text){
          	if($.trim(text) != _addr){//地址不跟原地址重复
	          	var _val = {
	      		    id : _id,
	      		    address : text
	          	}
	          	EasyAjax.ajax_Post_Json({
					url:'mobile/dispatch/update/single',
					data:JSON.stringify(_val)
				},function(res){
					if(res.success){
						var res = res.address;
						$.showLoading("新地址识别中");
						var _html =   res.address
				                    + '<div class="packageBox"><span id="count" class="hide">此处有'+ (res.packageCount != null ? res.packageCount : 0)+'件包裹</span><i data-id="'+ _id +'" data-addr="'+ res.address +'" data-lon="'+ res.longitude +'" data-lat="'+ res.latitude +'" id="editBtn"></i></div>'
				                    + '<input type="hidden" id="mIndex" value="'+ $("#mIndex").val() +'">';
						$("#addr").html(_html);
						setTimeout(function(){
							getMarks();
						},500)					
					}				
				});
			}			
          },
          onCancel: function() {}         
        });
    });
	
	
})




