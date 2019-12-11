<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<title>物流跟踪-客户管理</title>
<%@ include file="/views/include/head.jsp"%>
<link rel="stylesheet" href="${baseStatic}css/page.css?times=${times}" />
<link rel="stylesheet" href="${baseStatic}css/customInfo.css?times=${times}" />
<style type="text/css">
.amap-sug-result{
	z-index: 99999
}
.jconfirm {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	z-index: 100;
	font-family: inherit;
	overflow: hidden
}

.searchBtn{
	color: rgb(255, 255, 255);
    font-size: 14px;
    padding: 3px 20px;
    border-radius: 3px;
    background: rgb(49, 172, 250);
}

::-webkit-scrollbar{width:14px;}
::-webkit-scrollbar-track{background-color:#bee1eb;}
::-webkit-scrollbar-thumb{background-color:#00aff0;}
::-webkit-scrollbar-thumb:hover {background-color:#9c3}
::-webkit-scrollbar-thumb:active {background-color:#00aff0}
</style>
</head>
<body>
	<div class="custom-content clearfix">
		<form id="searchCustomerForm" action="${basePath}/backstage/customer/search" method="post">
			<input type="hidden" id="num" name="num" value="${ search.num}"> 
			<input type="hidden" id="size" name="size" value="${ search.size}">
			<input type="hidden" name='mode' value='dragMarker'/>
			<div class="content-search">
				<div class="clearfix">
					<div class="fl col-min">
						<label class="labe_l">项目组:</label> 
						<select id="select_group" class="tex_t selec_t" name="groupId">
							<!-- <option value="">全部</option> -->
							<c:forEach items="${groups}" var="group">
								<c:if test="${search.groupId == group.id }">
									<option value="${ group.id}" selected>${group.groupName }</option>
								</c:if>
								<c:if test="${search.groupId != group.id }">
									<option value="${ group.id}">${group.groupName }</option>
								</c:if>
							</c:forEach>
						</select>
					</div>
					<div class="fl col-min">
						<label class="labe_l">收货客户:</label> 
						<input type="text" class="tex_t" name="companyName" value="${search.companyName }">
					</div>
					<div class="fl col-min">
						<label class="labe_l">联系人:</label> 
						<input type="text" class="tex_t" name="contacts" value="${search.contacts }">
					</div>
				</div>
				<div class="clearfix">
					<div class="fl col-min">
						<label class="labe_l">手机号码:</label> 
						<input type="text" class="tex_t" name="contactNumber" value="${search.contactNumber }">
					</div>
					<div class="fl col-min">
						<label class="labe_l">收货地址:</label> 
						<input id="search_address" type="text" class="tex_t" name="fullAddress" value="${search.fullAddress }">
					</div>
				</div>
			</div>
		</form>
		<div class="edit">
			<button type="button" class="btn btn-default btn_add_customer">添加</button>
        	<button type="button" class="btn btn-default content-search-btn" style="float: right;">查询</button>
		</div>
		<div class="table-style">
			<table>
				<thead>
					<tr>
						<th>项目组名</th>
						<th>收货客户</th>
						<th>联系人</th>
						<th>手机号码</th>
						<th>座机号码</th>
						<th>收货地址</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${page.collection }" var="customer">
					<tr>
						<td>${customer.group.groupName}</td>
						<td>${customer.companyName}</td>
						<td>${customer.contacts}</td>
						<td>${customer.contactNumber}</td>
						<td>${customer.tel}</td>
						<td>${customer.fullAddress}</td>
						<td>
							<input type="hidden" name="customer.group.id" value="${customer.id}">
							<button type="button" class="btn btn-link edit_something">编辑</button>
						</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
		<c:if test="${page.collection != null && fn:length(page.collection) > 0}">
			 <someprefix:page pageNum="${page.pageNum}" pageSize="${page.pageSize}"  pages="${page.pages}" total="${page.total}"/>
		</c:if>
	</div>

	<!--新增/编辑弹出框-->
	<div class="edit_customer_panel" style="display: none">
		<div class="model-base-box edit_customer" style="width:711px;height: auto;">
			<input type="hidden" name="id"/>
			<input type="hidden" id="longitude" name="longitude">
			<input type="hidden" id="latitude" name="latitude"> 
			<div class="model-form-field">
				<label for="">项目组:</label> 
				<select id="select_group2" class="tex_t selec_t" name="groupId"  style="width:180px">
					<c:forEach items="${groups}" var="group">
						<option value="${group.id}">${group.groupName }</option>
					</c:forEach>
				</select>
				<label for="">收货客户:</label><input type="text" name="companyName" style="width:350px"/>
			</div>
			<div class="model-form-field">
				<label for="">联系人:</label><input type="text" name="contacts" style="width:140px"/>
				<label for="">手机号码:</label> <input type="tel" name="contactNumber" style="width:150px"/>
				<label for="">座机号码:</label><input type="text" name="tel" id="tel" style="width:150px"/>
			</div>
			<div class="model-form-field p-relative">
				<label for="">要求时间:</label>
				<div class="relative-box">
					<span>发货后</span>
					<select id="selectDay" name="arriveday">
						<option value="">选择第几天</option>
						<c:forEach begin="0" end="31" var="day">
							<c:if test="${day == 0 }">
								<option value="${day}">当天</option>
							</c:if>
							<c:if test="${day != 0 }">
								<option value="${day}">第${day}天</option>
							</c:if>
						</c:forEach>
					</select>
					<select id="selectHour" name="arrivehour">
						<option value="">选择时间点</option>
						<c:forEach begin="1" end="24" var="hour">
							<%-- <c:if test="${hour == 1 }">
								<option value="${hour}">凌晨1点</option>
							</c:if> --%>
							<c:if test="${hour != 0 }">
								<option value="${hour}">${hour}点</option>
							</c:if>
						</c:forEach>
					</select>
					<span>之前</span>
				</div>
			</div>
			<div class="model-form-field">
				<div class="line-item">
		    	  <span class="switch-text">电子围栏开关:</span>
		          <label for="switchCP"  class="switch-cp">
		            	<input id="switchCP" class="switch-cp__input" name="grateSatus" type="checkbox" value="">
		            	<div class="switch-cp__box"></div>           
		          </label>
		        </div>
				<label for="">半径:</label><input type="number" name="radius" min="1.00" max="9999999.99" style="width:150px" maxlength="10"/>公里
			</div>
			<div class="model-form-field">
				<label for="">收货地址:</label>
				<select id='province' name="province" style="width:203px"></select>
				<select id='city' name="city"  style="width:203px">
					<option selected="selected" value="">--请选择城市--</option>
				</select>
				<select id='district' name="district" style="width:203px">
					<option selected="selected" value="">--请选择区县--</option>
				</select>
			</div>
			<div class="model-form-field">
				<label for=""></label>
				<input id="" type="text" name="address"  style="width:518px" placeholder="请输入详细地址" onchange="cleanxy()"/>
				<button type="button" class="btn btn-blue searchBtn" onclick="searchXY()">获取坐标</button> 
			</div>
			<div id="map_container" class="map_container" style="height:250px;"></div>
		</div>
	</div>
</body>
<%@ include file="/views/include/floor.jsp"%>
<script type="text/javascript" src='//webapi.amap.com/maps?v=1.4.0&key=a4e0b28c9ca0d166b8872fc3823dbb8a&plugin=AMap.ToolBar,AMap.DistrictSearch,AMap.Geocoder'></script>
<!-- UI组件库 1.0 -->
<script src="//webapi.amap.com/ui/1.0/main.js?v=1.0.11"></script>
<script>
var searchBody;//新增修改弹出框页面对象
$(document).ready(function(){
	//提交查询事件
	$(".content-search-btn").on("click", function(){
		$("#searchCustomerForm").submit();
	});
	//翻页事件
	$(".pagination a").each(function(i){
		$(this).on('click', function(){
			$("form input[name='num']").val($(this).attr("num"));
			$("#searchCustomerForm").submit();
		});   
	});
	$(".edit_something").on('click', function(){
		var id = $(this).parent().find(":hidden[name='customer.group.id']").val();
		$.post(base_url + '/backstage/customer/findById/'+id , {}, function(data){
			if(data.success){//处理返回结果  
				var customer = data.customer;
				$.util.form('编辑客户', $(".edit_customer_panel").html(), function(model){
					 var editBody = this.$body;
					 searchBody = this.$body;
					 editBody.find(":input[name='id']").val(customer.id);
					 editBody.find(":input[name='companyName']").val(customer.companyName);
					 editBody.find(":input[name='contacts']").val(customer.contacts);
					 editBody.find(":input[name='contactNumber']").val(customer.contactNumber);
					 editBody.find(":input[name='address']").val(customer.address);
					 editBody.find(":input[name='tel']").val(customer.tel);
					 editBody.find(":input[name='longitude']").val(customer.longitude);
					 editBody.find(":input[name='latitude']").val(customer.latitude);
					 var selectGroup = editBody.find("select[name='groupId']");
					 selectGroup.val($("#select_group").val());
					 selectGroup.attr("disabled",true);
					 editBody.find("select[name='arriveday']").val(customer.arriveday);
					 editBody.find("select[name='arrivehour']").val(customer.arrivehour);
					 editBody.find(":input[name='radius']").val(customer.radius);
					 var $grateStatus = editBody.find(":input[name='grateSatus']");
					 $grateStatus.val(customer.grateSatus);
					 if(customer.grateSatus == 1){
						 $grateStatus.prop("checked",true);
					 }else{
						 $grateStatus.prop("checked",false);
					 }
					 $grateStatus.prop("id","switchCP_edit");
					 editBody.find("label[class='switch-cp']").attr("for","switchCP_edit");
					 changeStatus($grateStatus);
					 var address = editBody.find(":input[name='address']");
					 $(address).val(customer.address);
					 var select_address_id = "select_address_"+ data.customer.customerid;
					 $(address).attr("id", select_address_id);
					 var selected = {'province': customer.province, 'city': customer.city, 'district': customer.district}
					 //var selected = {'province': '山东省', 'city': '济南市', 'district': '市中区'}
					 var map = loadMap(editBody.find(".map_container")[0], editBody, selected);
					 if(customer.longitude!="" && customer.latitude!=undefined){
					 	addMarker(customer.longitude,customer.latitude, customer.address || "");
					 }
				}, function(){
					var parmas = {}, editBody = this.$body;
					editBody.find("input, select, textarea").each(function(index, item){
						parmas[item.name] = item.value;
					});
					if(vlidated(parmas)){
						$.util.json(base_url+'/backstage/customer/edit', parmas, function(data){
							if(data.success){
								$.util.alert('操作提示', data.message, function(){
									$("#searchCustomerForm").submit();
								});
							}else{
								$.util.error(data.message);
							}
						});
					}
					return false;
				 });
			}else{
				$.util.error(data.message);	
			} 
	      },'json');
	});
	
	$(".btn_add_customer").on('click', function(){
		$.util.form('新增客户', $(".edit_customer_panel").html(), function(){
			var editBody = this.$body;
			searchBody = this.$body;
			var map = loadMap(editBody.find(".map_container")[0],editBody);
			editBody.find(":input[name='radius']").val(5.00);
			var $grateStatus = editBody.find(":input[name='grateSatus']");
			$grateStatus.val(0);
			$grateStatus.prop("id","switchCP_add");
			editBody.find("label[class='switch-cp']").attr("for","switchCP_add");
			changeStatus($grateStatus);
			var address = editBody.find(":input[name='address']");
			var select_address_id = "address_autocomplete";
			$(address).attr("id",select_address_id);
		}, function(){
			var parmas = {}, editBody = this.$body;
			editBody.find("input, select, textarea").each(function(index, item){
				parmas[item.name] = item.value;
			});
			if(vlidated(parmas)){
				parmas['groupId'] = editBody.find("select[name='groupId']").val();
				$.util.json(base_url+'/backstage/customer/save', parmas, function(data){
					if(data.success){
						$.util.alert('操作提示', data.message, function(){
							$("#searchCustomerForm").submit();
						});
					}else{
						$.util.error(data.message);
					}
				});
			}
			return false;
		 });
	});

    //电子围栏开关事件
  	$(".switch-cp__input").on("click", function(){
  		var self = $(this);
  		var checked = self.attr("checked");
  		var parmas = {"id": self.attr("id")};
        $.util.json(base_url + '/backstage/customer/updateGrateSatus', parmas, function (data) {
            if (data.success) {
            	self.attr("checked", (data.status && data.status == 1));
            	self.attr("value", data.status);
            } else {
                $.util.error(data.message);
                self.attr("checked", checked);
            }
        });
  	});
});
/**
 * 数据校验
 */
function vlidated(parmas){
	var result = false;
	var groupId = parmas['groupId'];
	var compayName = parmas['companyName'];
	var contacts = parmas['contacts'];
	var selectDay = parmas['arriveday'];
	var selectHour = parmas['arrivehour'];
	var province = parmas['province'];
	var city = parmas['city'];
	var district = parmas['district'];
	var address = parmas['address'];
	var longitude = parmas['longitude'];
	var latitude = parmas['latitude'];
	var grateSatus = parmas['grateSatus'];
	var radius = $.trim(parmas['radius']);
	var radiusreg=/^([1-9][0-9]*)+(.[0-9]{1,2})?$/;
	if(!groupId || groupId <= 0){
		$.util.error("请先选择一个项目组");
	}else if( !compayName || compayName == ''){
		$.util.error("收货客户不能为空");
	}else if( !contacts || contacts == ''){
		$.util.error("联系人不能为空");
	}else if(selectDay && !selectHour){
		$.util.error("要求到货时间点不能为空");
	}else if(selectHour && !selectDay){
		$.util.error("要求到货天不能为空");
	}else if(grateSatus=='1' && !radiusreg.test(radius)){
		$.util.error("半径不能为空只能为整数或保留两位小数");
	}else if( !province || province == ''){
		$.util.error("请选择省份");
	}else if( !city || city == ''){
		$.util.error("请选择城市");
	}else if( !address || address == ''){
		$.util.error("详细地址不能为空");
	}else if( !longitude || !latitude || longitude == '' || latitude == ''){
		$.util.error("未获取到经纬度请重新获取");
	}else{
		result = true;
	}
	return result;
}

var selects = {}, selectNames = ['province', 'city', 'district'];
//加载地图绑定三级联动change事件
function loadMap(container, editBody, selected) {
	selects['map'] = new AMap.Map(container, {
		resizeEnable: true,
	    center: [116.31, 39.94],
		zoom: 10
    })
	for (var i = 0; i < selectNames.length; i++) {
		var selectElement = editBody.find("select[name='"+ selectNames[i] +"']");
		if(selected){
			selectElement.attr("svalue", selected[selectNames[i]]);//记录要选中的text
		}
		//绑定change事件
		selectElement.on('change', function(event,param){
			if(param!="autochange"){
				cleanxy();//清空坐标及坐标点
			}
			var _select = this;
			//清除地图上所有覆盖物
		    for (var i = 0, l = selects['polygons'].length; i < l; i++) {
		    	selects.polygons[i].setMap(null);
		    }
		    var option = _select[_select.options.selectedIndex];
		    selects.search.setLevel(option.level); //行政区级别
		    selects.search.setExtensions('all');
		    //按照adcode进行查询可以保证数据返回的唯一性
		    selects.search.search(option.adcode, function(status, result) {
		        if(status === 'complete'){
		        	loadData(result.districtList[0], _select.id);
		        }
		    });
		});
		selects[selectNames[i]] = selectElement;
	}
	selects['polygons'] = [];
	selects['search'] = new AMap.DistrictSearch({
		subdistrict: 1,   //返回下一级行政区
		showbiz:false  //最后一级返回街道信息
	});
	selects.search.search('100000', function(status, result) {
        if(status =='complete'){
        	loadData(result.districtList[0], "country");//初始化省份下拉列表
        }
    });
	return selects.map;
}

//加载三级联动数据
function loadData(data, level) {
    var bounds = data.boundaries;
    if (bounds) {
        for (var i = 0, l = bounds.length; i < l; i++) {
            var polygon = new AMap.Polygon({
                map: selects.map,
                strokeWeight: 1,
                strokeColor: '#CC66CC',
                fillColor: '#CCF3FF',
                fillOpacity: 0.5,
                path: bounds[i]
            });
            selects.polygons.push(polygon);
        }
        selects.map.setFitView();//地图自适应
    }
    //清空下一级别的下拉列表
    var tvalue = "省份";
    if (level === 'province') {
    	selects.city.html('');
    	selects.district.html('');
    	tvalue = "城市";
    } else if (level === 'city') {
    	selects.district.html('');
    	tvalue = "区县";
    }
    if(data.districtList && level != 'district'){
    	var districts = data.districtList;
        var select = selects[districts[0].level];
    	var option = new Option('--请选择'+ tvalue +'--','');
        select.append(option);
        for (var i = 0, l = districts.length; i < l; i++) {
            var item = districts[i];
            option = new Option(item.name, item.name);
            option.level = item.level;
            option.center = item.center;
            option.adcode = item.adcode;
            select.append(option);
        }
        var selectValue = select.attr("svalue");//取出要选中的值
        if(selectValue){
        	select.removeAttr("svalue");//清除，防止再次选中
        	select.find("option").each(function(index, option){
    			if(option.text == selectValue){
    				option.selected = true;//设置选中
    				select.trigger('change',"autochange");//自动触发change事件
    			}
    		});
        }
    }
}

	//通过地址获取坐标
	function searchXY(){
		var province = searchBody.find("select[name='province'] option:selected").val();
		var city = searchBody.find("select[name='city'] option:selected").val();
		var district = searchBody.find("select[name='district'] option:selected").val();
		var address = searchBody.find(":input[name='address']").val();
		if(province == "" || city==""){
			$.util.warning("请选择省份和城市");
			return;
		}
		if(address == "" || $.trim(address) == ""){
			$.util.warning("请输入详细地址");
			return;
		}
	   var geocoder = new AMap.Geocoder();
        //地理编码,返回地理编码结果
        geocoder.getLocation(province+city+district+address, function(status, result) {
            if (status === 'complete' && result.info === 'OK') {
                geocoder_CallBack(result);
            }
        });
	}
  var marker;
 //添加坐标点并提示信息
 function addMarker(longitude,latitude,formattedAddress) {
        marker = new AMap.Marker({
            map: selects.map,
            position: [longitude,  latitude]
        });
        var infoWindow = new AMap.InfoWindow({
            content: formattedAddress,
            offset: {x: 0, y: -30}
        });
        marker.on("mouseover", function(e) {//鼠标移动显示坐标地址
            infoWindow.open(selects.map, marker.getPosition());
        });
        //地图上围栏圈创建
        var status = searchBody.find(":input[name='grateSatus']").val();
    	console.log(status);
    	if(status == 1){
	        createCircle(marker.getPosition());
    	}
    }
    //地理编码返回结果展示
    function geocoder_CallBack(data) {
        //地理编码结果数组
        var geocode = data.geocodes;
        var longitude = geocode[0].location.getLng();
        var latitude=geocode[0].location.getLat();
        /* for (var i = 0; i < geocode.length; i++) {
            longitude = geocode[i].location.getLng();
            latitude = geocode[i].location.getLat();
            addMarker(i, geocode[i]);
        } */
        addMarker(longitude,latitude, geocode[0].formattedAddress);
        selects.map.setFitView();
        var lg = searchBody.find("#longitude");
	    var lt = searchBody.find("#latitude");
	    if(longitude!="" && latitude!=""){
	    	$(lg).val(longitude);
 	     	$(lt).val(latitude);
 	     	$.util.success("坐标获取成功");
	   	}else{
	   		$(lg).val("");
 	     	$(lt).val("");
 	     	$.util.error("坐标获取失败，请从新获取");
	   	}
    }
    var circle;
    function createCircle(position){
    	var radius = Number(searchBody.find(":input[name='radius']").val() == "" ? 5 : searchBody.find(":input[name='radius']").val());
    	console.log(radius);
	    circle = new AMap.Circle({
	    	radius: radius*1000, //半径
	        strokeColor: "#3366FF", //线颜色
	        strokeOpacity: 0.8, //线透明度
	        strokeWeight: 1, //线粗细度
	        fillColor: "#1791fc", //填充颜色
	        fillOpacity: 0.2//填充透明度
	    });
	    circle.setCenter(position);// 圆心位置
	    circle.setMap(selects.map);
    }
  	//点击关闭电子围栏开关在地图上画圆或清除圆操作
    function changeStatus(obj) {
  		$(obj).bind('click',function(){
	  		var status = $(obj).val();
	  		if(status==1){
				$(obj).val(0);
				if(selects.map && circle){
					selects.map.remove(circle);
				}
	  		}else{
	  			$(obj).val(1);
				if(selects.map){
					if(circle){
						 circle.setMap(selects.map);
					}else{
						if(marker){
							createCircle(marker.getPosition());	
						}
					}
				}
			}
  		});
	}
    //清空坐标及坐标点
    function cleanxy(){
    	searchBody.find("#longitude").val('');
		searchBody.find("#latitude").val('');
		if(marker){//清除点标记
			marker.setMap(null);
		    marker = null;
		}
		/* if(circle){
			circle.setMap(null);
			circle = null;
		} */
    }
    //电子围栏开关
    function updateStatus(grateSatus,customerId){
    	if($.util.getvalue(customerId)==''){
    		$.util.error("客户Id不能为空");
    		return false;
    	}
    	var parmas = {"id" : customerId, "grateSatus" : grateSatus == 0 ? 1 : 0};
    	$.util.json(base_url+'/backstage/customer/updateGrateSatus', parmas, function(data){
			if(data.success){
				$.util.alert('操作提示', data.message, function(){
					$("#searchCustomerForm").submit();
				});
			}else{
				$.util.error(data.message);
			}
		});
    }
</script>

</html>