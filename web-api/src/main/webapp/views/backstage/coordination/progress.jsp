<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-日常工作-进度跟踪</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css" />
    <link rel="stylesheet" href="${baseStatic}css/progress.css?times=${times}" />
	<link rel="stylesheet" href="${baseStatic}plugin/css/upload.img.css"/>
</head>
<body>
	<div class="track-content clearfix">
		<form id="search_form" action="${basePath}/backstage/coordination/search/todo" method="post">
			<input type="hidden" name="num" value="${search.num}">
			<input type="hidden" name="size" value="20">
			<div class="content-search">
				<div class="title fs0">
					<h2 class="in-block">进度跟踪</h2>
					<!--
					<select class="in-block" name="groupKey" val="${search.groupKey}">
						<c:forEach items="${groups}" var="g">
							<option value="${g.id}">${g.groupName}</option>
						</c:forEach>
						<option value="0">其他</option>
					</select>
					-->
				</div>
				<div class="clearfix">
					<div class="fl col-min">
	                    <label class="labe_l">发送时间:</label>
	                    <div class="input-append date startDate" id="deliverStartTime">
	                        <input type="text" class="tex_t z_index" name="firstTime" value="${search.firstTime }" readonly>
	                        <span class="add-on"> <i class="icon-th"></i> </span>
	                    </div>
	                </div>
	                <div class="fl col-min">
	                    <label class="labe_l">至</label>
	                    <div class="input-append date endDate" id="deliverEndTime">
	                        <input type="text" class="tex_t z_index" name="secondTime" value="${search.secondTime }" readonly>
	                        <span class="add-on"> <i class="icon-th"></i></span>
	                    </div>
	                </div>
					<div class="fl col-min">
						<label class="labe_l">跟踪类型</label>
						<select class="tex_t selec_t" name="msgType"  val="${search.msgType}">
							<option value="-1" selected>全部</option>
							<option value="1">催报位置</option>
							<option value="2">催传回单</option>
						</select>
					</div>
					<div class="fl col-min">
						<label class="labe_l">最新进展</label>
						<select class="tex_t selec_t" name="fettle" val="${search.fettle}" >
							<option value="-1" selected>全部</option>
							<option value="0" >未读</option>
							<option value="1">已读</option>
							<option value="2">已转催</option>
							<option value="3">已处理</option>
							<option value="4">已完成</option>
						</select>
					</div>
				</div>
			</div>
		</form>
		<div style="text-align: right;"><button type="button" class="btn btn-default content-search-btn ml0">查询</button></div>
		<div class="table-style">
	        <table class="dtable" id="trackTable">
	            <thead>
		            <tr>
		            	<th>跟踪类型</th>
						<th>送货单号</th>
		                <th>运单号</th>
		                <th>发送时间</th>
		                <th>最新位置</th>
		                <th>进度</th>
		                <th>时间</th>
						<th>任务单号</th>
		                <th>操作</th>
		            </tr>
	            </thead>
	            <tbody>
					<c:forEach items="${results.collection}" var="m">
						<tr>
							<td>
								<c:if test="${m.msgType == 1}">
									催报位置
								</c:if>
								<c:if test="${m.msgType == 2}">
									催传回单
								</c:if>
							</td>
							<td>${m.conveyanceNumber}</td>
							<td>${m.deliveryNumber}</td>
							<td><fmt:formatDate value="${m.createtime}" pattern="yyyy-MM-dd HH:mm"/></td>
							<td>${m.loaction}</td>
							<td>
								<c:if test="${m.processingStatus == 0}">未读</c:if>
								<c:if test="${m.processingStatus == 1}">已读</c:if>
								<c:if test="${m.processingStatus == 2}">已转催</c:if>
								<c:if test="${m.processingStatus == 3}">已处理</c:if>
								<c:if test="${m.processingStatus == 4}">已完成</c:if>
							</td>
							<td><fmt:formatDate value="${m.updateTime}" pattern="yyyy-MM-dd HH:mm"/></td>
							<td><a class="aBtn" href="${basePath}/backstage/trace/findById/${m.waybillKey}" target="_blank">${m.barcode}</a></td>
							<td>
								<div class="iconBox">
                                    <span class="span-icon log-details" data-id="${m.id}">详情</span>
									<c:if test="${m.processingStatus < 3}">
										<c:if test="${m.msgType == 1}">
											<span class="span-icon location" data-type="${m.msgType}" data-wkey="${m.waybillKey}" data-id="${m.id}"></span>
										</c:if>
										<c:if test="${m.msgType == 2}">
											<span class="span-icon upload" data-wkey="${m.waybillKey}"></span>
										</c:if>
										<c:if test="${m.processingStatus == 0 || m.processingStatus == 1}">
											<span class="span-icon choose" data-type="${m.msgType}" data-id="${m.id}" data-conveyanceid="${m.conveyanceId}"></span>
										</c:if>
									</c:if>
								</div>
							</td>
						</tr>
					</c:forEach>
	            </tbody>
	        </table>
	        <c:if test="${results.collection != null && fn:length(results.collection) > 0}">
	            <someprefix:page pageNum="${results.pageNum}" pageSize="${results.pageSize}" pages="${results.pages}" total="${results.total}"/>
	        </c:if>
	    </div>
	</div>

	<!--选择催办对象弹出框-->
	<div class="forward-some-one" style="display: none;">
		<div class="urge_box" style="width:540px;">
			<div class="urge_content">
				<div class="my-team">
					<!--<p>我的组</p>-->
					<div class="label_div">
						<span class="label_span">我的组</span>
					</div>
					<div class="label_box">
						<!--
                        <input type="radio" name="group" class="rdolist"/>
                        <label class="rdobox">
                            <span class="radiobox-content">男</span>
                        </label>
                        -->
					</div>
				</div>
				<div class="my-next">
					<!--<p>我的下级</p>-->
					<div class="label_div">
						<span class="label_span">我的下级</span>
					</div>
					<div class="label_box">
						<!--
                        <input type="checkbox" name="chk" class="chklist"/>
                        <label class="chkbox">
                            <span class="radiobox-content">UI设计师</span>
                        </label>
                        <input type="checkbox" name="chk" class="chklist" />
                        <label class="chkbox">
                            <span class="radiobox-content">软件工程师</span>
                        </label>
                        <input type="checkbox" name="chk" class="chklist" />
                        <label class="chkbox">
                            <span class="radiobox-content">架构师</span>
                        </label>
                        -->
					</div>
				</div>
			</div>
		</div>
	</div>

	<!--上报位置弹出框-->
	<div class="upload_location" style="display: none">
		<div class="report_location_box" style="width:540px;">
			<div class="map_box">
				<div class="adr_box manage-wrapper">
					<input type="text" class="adr_input dizhi_city" placeholder="请选择省市区"/>
					<input type="text" class="adr_detail" placeholder="请输入详细地址"/>
				</div>
				<div class="adr_map">
					<!--这里放地图-->
				</div>
			</div>
			<div class="line-part clearfix">
				<span class="float-tag">上报司机：</span>
				<div class="text-tag fs0"><input type="text" class="sub-tag" readonly /><button class="layui-btn layui-btn-normal select-user">选择</button></div>
			</div>
			<div class="line-part other-message" style="margin-top: 20px;">
				<span class="float-tag">是否准时送达：</span>
				<div class="text-tag">
					<label><input type="radio" name="punctual" checked value="1" />是</label>
					<label><input type="radio" name="punctual" value="0"/>否</label>
					<div class="delay_arrive input-append date">
						<input type="text" class="arrive_time" name="selectTime" value="" readonly>
						<span class="add-on">
					<i class="icon-th"></i>
				</span>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 上传回单 -->
	<div class="upload_receipt" style="display: none">
		<div class="model-base-box import_task" style="width: 640px;">
			<!--
			<form action="" enctype="multipart/form-data" method="post">
				<div class="model-form-field file_box">
					<label>选择文件:</label>
					<input type="text" name="fileName" id="fileName" readonly/>
					<span class="span-txt">请选择文件</span>
					<a href="javascript:;" class="file">
						<input name="file" type="file" accept=".jpeg,.gif,.png,.jpg,.jgp,.bmp" id="uploadFile"/>
					</a>
				</div>
			</form>
			-->
		</div>
	</div>

</body>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="https://webapi.amap.com/maps?v=1.4.0&key=a4e0b28c9ca0d166b8872fc3823dbb8a&plugin=AMap.Geocoder"></script>
<script src="${baseStatic}plugin/js/jquery.labelauty.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${baseStatic}js/datetimepicker1.js"></script>
<script src="${baseStatic}js/select.resource.js?times=${times}" ></script>
<script src="${baseStatic}plugin/js/upload.img.js?times=${times}"></script>
<script>
    $(document).ready(function () {
        $('select').each(function(){
            var target = $(this), val = target.attr("val");
            if(val && target.is('select')){
                target.val(val);
            }
        });
        var cnum = 1;

        function fromSubmit(num){
            if(!num){ num = cnum; }
            cnum = num;
            $("form input[name='num']").val(num);
            $("#search_form").submit();
		}

        $('.content-search-btn').on('click', function(){
            fromSubmit(1);
		});
        //翻页事件
        $(".pagination a").each(function (i) {
            $(this).on('click', function () {
                fromSubmit($(this).attr("num"));
            });
        });

        function buildParmas(geocode, location, parmas){
            parmas.lng = location.lng;
            parmas.lat = location.lat;
            var component = geocode.addressComponent;
            if(geocode.adcode){
                parmas.adcode = geocode.adcode;
            }else{
                parmas.adcode = component.adcode;
            }
            parmas.area = component.province;
            if(component.city){ parmas.area += component.city; }
            if(component.district){ parmas.area += component.district; }
            parmas.street = component.street;
            if(component.streetNumber){ parmas.street += component.streetNumber; }
            if(component.building){ parmas.street += component.building; }
            parmas.formattedAddress = geocode.formattedAddress;
            return parmas
        }
        function receipt(waybillKey){
            if(!waybillKey){ return; }
            var files = [];
            var form = $.util.form('回单上传', $(".upload_receipt").html(), function (model) {
                var $editBody = this.$body;
                $editBody.find('.model-base-box').uploadImage({
                    imgSelect: function(imgFiles, selects){
                        files = imgFiles;
                    },
                    imgDelete : function(imgFiles, index){
                        files = imgFiles;
                    }
                });
            }, function () {
                var editBody = this.$body, formData = new FormData();
                formData.append("waybillKey", waybillKey);
                if(files == null || files.length <= 0){
                    $.util.error("请选择需要上传的回单图片文件！");
                    return false;
                }
                for(var i = 0; i < files.length; i++){
                    formData.append("file"+ i, files[i]);
                }
                $.ajax({
                    url: base_url + "/backstage/receipt/upload",
                    type: 'POST',processData: false,contentType: false, data: formData,
                    success: function (response) {
                        if (response.success) {
                            $.util.success('上传成功',function(){
                                if(form){ form.close(); }
                                fromSubmit();
                            }, 3000);
                        } else {
                            $.util.error(response.message);
                        }
                    }
                });
                return false;
            });
        }
        function location(waybillKey, nid){
            if(!waybillKey){ return; }
            var parmas = {waybillKey: waybillKey, msgkey: nid};
            $.util.form('上报位置', $(".upload_location").html(), function (model) {
                var $editBody = this.$body;
                var map = new AMap.Map($editBody.find('.adr_map')[0], { resizeEnable: true, zoom:10 });
                var geocoder = new AMap.Geocoder();//城市，默认：“全国”
                var marker = new AMap.Marker({ map:map, bubble:true });
                map.on('click', function(e){
                    marker.setPosition(e.lnglat);
                    geocoder.getAddress([e.lnglat.lng, e.lnglat.lat], function(status, result) {
                        if (status === 'complete' && result.info === 'OK') {
                            parmas = buildParmas(result.regeocode, e.lnglat, parmas);
                            $editBody.find('.adr_input').val(parmas.area);
                            $editBody.find('.adr_detail').val(parmas.street);
                        }
                    });
                });
                map.setFitView();
                $editBody.find('.adr_box').find('input').on('change', function(){
                    parmas = {};
                    parmas.area = $editBody.find('.adr_input').val();
                    parmas.street = $editBody.find('.adr_detail').val();
                    parmas.formattedAddress = parmas.area + parmas.street;
                    geocoder.getLocation(parmas.formattedAddress, function(status, result) {
                        if (status === 'complete' && result.info === 'OK') {
                            var geocode = result.geocodes[0];
                            parmas = buildParmas(geocode, geocode.location, parmas);
                            marker.setPosition(geocode.location);
                        }else{
                            $.util.error('输入的地址无法获取经纬度,请检查地址');
                        }
                    });
                });
                $editBody.find('.select-user').on('click', function(){
                    $(this).parent().find('input').selectUser({selectClose:true, selectFun:function(item){
                            parmas.driverKey = item.friendKey;
                            $(this).val('姓名:'+ item.remarkName +' , 电话:'+ item.mobile);
                        }});
                });
                if(!nid){
                    $editBody.find('.other-message').hide();
                }else{
                    $editBody.find(".delay_arrive").datetimepicker({
                        language : 'zh-CN',
                        format : 'yyyy-mm-dd',
                        minView:'month',
                        weekStart : 1, /*以星期一为一星期开始*/
                        todayBtn : true,
                        autoclose : true,
                        pickerPosition : "top-left"
                    });
                }
            }, function () {
                var editBody = this.$body;
                if(!parmas.formattedAddress){ $.util.error("请输入上报地址"); return false; }
                parmas.punctual = editBody.find("input[name='punctual']:checked").val();
                parmas.selectTime = editBody.find("input[name='selectTime']").val();
                $.util.json(base_url + '/backstage/coordination/loaction', parmas, function (data) {
                    if (data.success) {//处理返回结果
                        $.util.success('上报成功',function(){
                            fromSubmit();
                        }, 3000);
                    } else {
                        $.util.error(data.message);
                    }
                });
            });
        }
        function forward(parmas){
            $.util.json(base_url + '/backstage/coordination/conveyance/forward', parmas, function (data) {
                if (data.success) {//处理返回结果
                    $.util.success('转催成功',function(){
                        fromSubmit();
                    }, 3000);
                } else {
                    $.util.error(data.message);
                }
            });
        }
        function forwardSomeOne(conveyanceKey, type,  nid){
            if(!conveyanceKey){ return; }
            var parmas = nid ? {id : nid} : {conveyanceId: conveyanceKey, msgType: type};
            $.util.json(base_url + '/backstage/coordination/get/reminders/'+ conveyanceKey, parmas, function (data) {
                if (data.success) {//处理返回结果
                    $.util.form('选择催办对象', $('.forward-some-one').html(), function (model) {
                        var $editBody = this.$body;
                        if(data.group && data.group.length > 0){
                            var team = $editBody.find('.my-team').find('.label_box');
                            team.html('');
                            $.each(data.group, function () {
                                team.append("<input type='radio' name='group' value='"+ this.id +"' class='labelauty' /><label><span>"+ this.groupName +"</span></label>");
                            })
                        }else{
                            $editBody.find('.my-team').hide();
                        }
                        if(data.user && data.user.length > 0){
                            var next = $editBody.find('.my-next').find('.label_box');
                            next.html('');
                            $.each(data.user, function () {
                                next.append("<input type='checkbox' name='wkey' value='"+ this.conveyanceId +"' class='labelauty' /><label><span>"+ this.unamezn +"</span></label>");
                            })
                        }else{
                            $editBody.find('.my-next').hide();
                        }
                        $(".labelauty").labelauty();
                        $editBody.find(':radio').on('click', function () {
                            $editBody.find(':checkbox').attr('checked', false).labelautychecked();
                        })
                        $editBody.find(':checkbox').on('click', function () {
                            $editBody.find(':radio').attr('checked', false).labelautychecked();
                        })
                    }, function () {
                        var $editBody = this.$body;
                        $.each($editBody.find('input:radio:checked'),function(){
                            parmas.groupKey = Number($(this).val());
                        });
                        var childs = [];
                        $.each($editBody.find('input:checkbox:checked'),function(){
                            childs.push(Number($(this).val()));
                        });
                        if(childs.length > 0){
                            parmas.forwardId = childs.join(',');
                        }
                        if(!parmas.groupKey && !parmas.forwardId){
                            $.util.error("请选择一个组或者一个下级");
                            return false;
                        }
                        forward(parmas);
                    });
                } else {
                    $.util.error(data.message);
                }
            });
        }
        $('.location').on('click', function(){
            location($(this).data('wkey'), $(this).data('id'));
		});
        $('.upload').on('click', function(){
            receipt($(this).data('wkey'));
        });
        $('.choose').on('click', function(){
            var target = $(this);
            forwardSomeOne(target.data('conveyanceid'), target.data('type'), target.data('id'));
        });
        $('.log-details').on('click', function(){
            $.util.json(base_url + '/backstage/coordination/list/todo/log', {todoKey: $(this).data('id')}, function (data) {
                if(data.success){
                    var htmls = [], c = data.conveyance, logs = data.logs;
                    htmls.push("<div>");
                    htmls.push("<ul class='infoList clearfix'><li>送货单号:"+ c.deliveryNumber +"</li><li>运单号:"+ c.conveyanceNumber +"</li><li>任务单号:"+ c.barcode +"</li></ul>");
                    htmls.push("<p class='mt10 textR'><a class='blue' href='"+ base_url +"/backstage/trace/findById/"+ c.waybillKey +"' target='_blank'>查看任务详情</a></p>");
                    htmls.push("<div class='table-style'>");
                    htmls.push("<table class='dtable'>");
                    htmls.push("<thead><tr><th>进度</th><th>时间</th><th>操作人</th></tr></thead>");
                    htmls.push("<tbody>");
                    if(logs && logs.length > 0){
                        logs.forEach(function( val, index ) {
                            htmls.push("<tr><td>"+ val.typeString +"</td><td>"+ val.timeString +"</td><td>"+ val.user.unamezn +"</td></tr>");
                        });
                    }
                    htmls.push("</tbody></table></div></div>");
                    $.dialog({content: htmls.join(''), closeIcon:true, title:'进度详情', boxWidth: 620, useBootstrap:false});
                }else{
                    $.util.error(data.message);
                }
            });
        });
    });
</script>
</html>