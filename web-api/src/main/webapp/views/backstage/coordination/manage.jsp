<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-日常工作</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}plugin/js/cityselect/chooseAddress/city.css"/>
    <link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css" />
    <link rel="stylesheet" href="${baseStatic}css/dailyWork.css"/>
	<link rel="stylesheet" href="${baseStatic}plugin/css/upload.img.css"/>
	<style type="text/css">
		.cactive {
			color: #fff !important;
			cursor: default;
			background-color: #337ab7 !important;
			border-color: #337ab7 !important;
		}
		.pagination a{
			cursor: pointer;
		}
		[v-cloak]{display: none !important;}
	</style>
</head>
<body>
<div class="track-content" style="overflow-x:auto;min-width:1350px !important;">
	<div class="cotent-wrapper">
		<div class="td-wrapper left-wrapper">
			<ul class="btn-box">
				<li class="current">我的待办</li>
				<li>进度跟踪</li>
			</ul>
			<div class="content-box">
				<div class="content-item active" id="list-wait-message" v-cloak>
					<div class="part-item" style="text-align: center;" v-if="days.length <= 0">暂无数据</div>
					<div v-else>
						<div class="part-item">
							<span style="margin-right: 10px;">上报位置:<b style="color: #e60000">{{loactions}}</b>项</span>
							<span>上传回单:<b style="color: #e60000">{{receipts}}</b>项</span>
						</div>
						<!--一天的任务单循环part-item-->
						<div class="part-item" v-for="day in days" v-if="days.length > 0">
							<h2 class="time">{{day.time}}</h2>
							<!--循环list-item-->
							<div class="list-item" v-for="n in day.news" v-on:click="showList(n.conveyanceId)">
								<div class="line-part">
									<span class="float-tag">送货单：</span>
									<p class="p-tag">{{n.deliveryNumber}}</p>
								</div>
								<p v-if="n.msgType == 1">需要上报位置，请尽快处理！</p>
								<p v-if="n.msgType == 2">需要上传回单，请尽快处理！</p>
								<div class="handle fs0">
									<div class="in-block"><span class="span-icon choose" v-if="n.forward" v-on:click="forward(n.id, n.conveyanceId)">选择催办对象</span></div>
									<div class="in-block" v-if="n.msgType == 1"><span class="span-icon location" v-on:click="done(n.waybillKey, n.msgType, n.id)">上报位置</span></div>
									<div class="in-block" v-if="n.msgType == 2"><span class="span-icon upload" v-on:click="done(n.waybillKey, n.msgType)">上传回单</span></div>
								</div>
								<!-- <i class="status-ico">news</i> -->
							</div>
						</div>
					</div>
				</div>
				<!--进度跟踪-->
				<div class="content-item pro_gress" id="list-follow-message" v-cloak>
					<div class="part-item" style="text-align: center;" v-if="days.length <= 0">暂无数据</div>
					<div v-else>
						<div class="part-item" v-for="day in days" v-if="days.length > 0">
							<h2 class="time">{{day.time}}</h2>
							<!--循环list-item-->
							<div class="list-item" v-for="n in day.news" v-on:click="showList(n.conveyanceId)">
								<div class="line-part">
									<span class="float-tag">送货单：</span>
									<p class="p-tag">{{n.deliveryNumber}}</p>
								</div>
								<p v-if="n.msgType == 1">需要上报位置，请尽快处理！</p>
								<p v-if="n.msgType == 2">需要上传回单，请尽快处理！</p>
								<p v-if="n.msgRemark != null">{{n.msgRemark}}</p>
								<div class="handle fs0">
									<div class="in-block" v-if="n.processingStatus == 0">未读</div>
									<div class="in-block" v-if="n.processingStatus == 1">已读</div>
									<div class="in-block" v-if="n.processingStatus == 2">已转催</div>
									<div class="in-block" v-if="n.processingStatus == 3">已处理</div>
									<div class="in-block" v-if="n.processingStatus == 4">已完成</div>
								</div>
							</div>
						</div>
						<div class="readmore">
							<a href="${basePath}/backstage/coordination/search/todo">查看更多</a>
						</div>
					</div>
				</div>
			</div>
			<span class="refresh-btn"><i class="layui-icon  reset">&#x1002;</i></span>
			<span class="toggle-btn"><i class="layui-icon to-left">&#xe65a;</i><i class="layui-icon to-right">&#xe65b;</i></span>
			<div class="tips">
				<span>待办事项</span>
			</div>
		</div>
		<div class="td-wrapper right-wrapper" id="conveyance">
			<div class="title fs0">
				<h2 class="in-block">运单列表</h2>
				<select class="in-block event-search" name="groupKey">
					<c:forEach items="${groups}" var="group">
						<c:if test="${groupKey != null && groupKey - group.id == 0}">
							<option value="${ group.id}" selected>${group.groupName }</option>
						</c:if>
						<c:if test="${groupKey == null || groupKey - group.id != 0}">
							<option value="${ group.id}">${group.groupName }</option>
						</c:if>
					</c:forEach>
					<c:if test="${groupKey ==null || groupKey <= 0}">
						<option value="0" selected>其他</option>
					</c:if>
					<c:if test="${groupKey !=null && groupKey > 0}">
						<option value="0">其他</option>
					</c:if>
				</select>
			</div>
			<div class="selection">
				<div class="selection-show select-tog">
					<div class="line-part">
						<span class="float-tag">运单状态：</span>
						<div class="text-tag">
							<label class="labe_1 active"><input type="radio" name="fettle" value="" checked="checked" class="event-search"/>全部</label>
							<label class="labe_1"><input type="radio" name="fettle" value="20" class="event-search"/>待运输</label>
							<label class="labe_1"><input type="radio" name="fettle" value="30" class="event-search"/>运输中</label>
							<label class="labe_1"><input type="radio" name="fettle" value="35" class="event-search"/>已送达</label>
						</div>
					</div>
					<div class="line-part">
						<span class="float-tag">回单状态：</span>
						<div class="text-tag">
							<label class="labe_2 active"><input type="radio" name="receipt" value="" checked="checked" class="event-search"/>全部</label>
							<label class="labe_2"><input type="radio" name="receipt" value="1" class="event-search"/>未上传</label>
							<label class="labe_2"><input type="radio" name="receipt" value="2" class="event-search"/>已上传</label>
							<label class="labe_2"><input type="radio" name="receipt" value="3" class="event-search" />审核中</label>
							<label class="labe_2"><input type="radio" name="receipt" value="4" class="event-search" />已审核</label>
						</div>
					</div>
					<div class="line-part">
						<span class="float-tag">上报位置：</span>
						<div class="text-tag">
							<label class="labe_3 active"><input type="radio" name="location" value="" checked="checked" class="event-search"/>全部</label>
							<label class="labe_3"><input type="radio" name="location" value="0" class="event-search"/>未上报</label>
							<label class="labe_3"><input type="radio" name="location" value="1" class="event-search"/>已上报1次</label>
							<label class="labe_3"><input type="radio" name="location" value="2" class="event-search"/>已上报2次</label>
							<label class="labe_3 other_more"><input type="radio" name="location" value="-1"/>其他</label>
							<label class="more-box report-label">已上报<input class="report-num event-search" name="locationCount" value="3" type="text"/>次</label>
						</div>
					</div>
					<div class="line-part">
						<span class="float-tag">到货要求：</span>
						<div class="text-tag">
							<label class="labe_4 active"><input type="radio" name="request" value="" checked="checked" class="event-search"/>全部</label>
							<label class="labe_4"><input type="radio" name="request" value="0" class="event-search"/>今日到货</label>
							<label class="labe_4"><input type="radio" name="request" value="1" class="event-search"/>明日到货</label>
							<label class="labe_4 other_more"><input type="radio" name="request" value="-1"/>其他</label>
							<label class="more-box time-label input-append date" id="otherArrive">
								<input type="text" class="sub-tag event-search" name="requestTime" value="${today}" readonly />
								<span class="add-on">
									<i class="icon-th"></i>
								</span>
							</label>
						</div>
					</div>
				</div>
				<div class="selection-more" style="display: none;">
					<p class="p-more">更多查询条件<span class="icon-down"><i class="layui-icon">&#xe623;</i></span></p>
				  	<div class="detail-select">
				  		<div class="line-block">
				  			<div class="line-part" style="width:25%">
					    		<span class="float-tag">单号查询：</span>
					    		<div class="text-tag"><input type="text" name="likeString" class="sub-tag" placeholder="任务单号/运单号/送货单号"/></div>
					    	</div>
							<!--
					  		<div class="line-part" style="width:25%">
					    		<span class="float-tag">承运人：</span>
					    		<div class="text-tag"><input type="text" name="driverName" class="sub-tag" /></div>
					    	</div>
					    	-->
					    	<div class="line-part" style="width:25%">
					    		<span class="float-tag">发货时间：</span>
					    		<div class="text-tag input-append date" id="sendGoodsTime">
									<input type="text" class="sub-tag" name="deliveryTime" value="" />
									<span class="add-on">
										<i class="icon-th"></i>
									</span>
								</div>
					    	</div>
				  		</div>
				  		<div class="line-block">
				  			<div class="line-part" style="width:25%">
					    		<span class="float-tag">发站：</span>
					    		<div class="text-tag manage-wrapper"><input type="text" name="startStation" readonly class="sub-tag dizhi_city" /></div>
					    	</div>
					    	<div class="line-part" style="width:25%">
					    		<span class="float-tag">到站：</span>
					    		<div class="text-tag manage-wrapper"><input type="text" name="endStation" readonly class="sub-tag dizhi_city" /></div>
					    	</div>
						  	<div class="line-part line-btn" style="width:12%;">
					    		<button class="layui-btn layui-btn-normal event-search">查询</button>
					    	</div>
				  		</div>
					</div>
				</div>
				<span class="toggle-icon open">展开</span>
			</div>
			<div class="table-style" id="list-conveyance" v-cloak>
				<div class="listConveyance">
	                <ul class="ul-header table-layout">
	                    <li class="table-td">
	                    	<label class="cur_pointer"><input type="checkbox" class="check_box" name="" value="" />全选</label>
	                    </li>
	                    <li class="table-td">送货单号</li>
	                    <li class="table-td">运单号</li>
	                    <li class="table-td">任务单号</li>
	                    <li class="table-td">承运人</li>
	                    <li class="table-td">发站</li>
	                    <li class="table-td">到站</li>
	                    <li class="table-td">发货时间</li>
	                    <li class="table-td">要求到货时间</li>
	                    <li class="table-td">最新位置</li>
	                    <li class="table-td">运单状态</li>
	                    <li class="table-td">回单状态</li>
	                    <li class="table-td">异常记录</li>
	                </ul>
	                <ul class="ul-wrapper" v-for="c in collection" v-if="collection.length > 0"> <!-- 循环ul -->
	                    <li class="li-content table-layout">
	                        <div class="li-item">
	                        	<div class="pos_relative">
									<input type="checkbox" class="check_box" v-bind:value="c.id" />
									<i class="layui-icon sub-status">&#xe622;</i>
									<!--<span class="bill-status key"></span> 有key时，样式上表示重点运单，没有时，非重点-->
								</div>
	                        </div>
	                        <div class="li-item">{{c.deliveryNumber}}</div>
	                        <div class="li-item">
	                        	<div class="in-block"><input type="text" v-bind:value="c.conveyanceNumber" v-bind:cid="c.id" v-bind:val="c.conveyanceNumber" readonly /><i class="layui-icon edit_icon" v-on:click.self="editNumber">&#xe642;</i></div>
	                        </div>
	                        <div class="li-item">
	                        	<a href="#" v-on:click="detail(c.waybill.id)">{{c.barcode}}</a>
	                        </div>
							<div class="li-item">
								<span v-if="c.owner != null">{{c.owner.unamezn}}</span>
							</div>
							<div class="li-item">{{c.simpleStartStation}}</div>
							<div class="li-item">{{c.simpleEndStation}}</div>
							<div class="li-item">{{c.waybill.createtime | date}}</div>
							<div class="li-item">
								<span v-if="c.waybill.arrivaltime != null">{{c.waybill.arrivaltime | datehour}}</span>
								<span v-else>无要求</span>
							</div>
							<div class="li-item">
								<div class="new_location" v-if="c.addressStatus == null || c.addressStatus == 2">
									<div class="loc_info address-tip">
										<span data-toggle="tooltip" v-bind:title="c.waybill.address">{{location(c.waybill.address)}}</span>
										<br />
										<span>{{c.waybill.loactionTime | time}}</span>
									</div>
								</div>
								<div class="new_location" v-if="c.addressStatus == 9">
									<span class="loc_text wait_tag smallSize" v-on:click="done(c.waybillKey, 1)">待办</span>
									<div class="loc_info address-tip">
										<span data-toggle="tooltip" v-bind:title="c.waybill.address">{{location(c.waybill.address)}}</span>
										<br />
										<span>{{c.waybill.loactionTime | time}}</span>
									</div>
								</div>
								<div class="new_location" v-if="c.addressStatus == 1">
									<span class="loc_text follow_tag smallSize">跟进</span>
									<div class="loc_info address-tip">
										<span data-toggle="tooltip" v-bind:title="c.waybill.address">{{location(c.waybill.address)}}</span>
										<br />
										<span>{{c.waybill.loactionTime | time}}</span>
									</div>
								</div>
							</div>
							<div class="li-item">
								<div class="waybill_status" v-if="c.conveyanceFettle == 20"><span class="have_icon wait" v-on:click="fettle" v-bind:value="c.id">待运输</span></div>
								<div class="waybill_status" v-if="c.conveyanceFettle == 30"><span class="have_icon on-way" v-on:click="fettle" v-bind:value="c.id">运输中</span></div>
								<div class="waybill_status" v-if="c.conveyanceFettle == 35">已送达</div>
							</div>
							<div class="li-item">
								<div class="new_location" v-if="c.receiptStatus == null || c.receiptStatus != 9" v-on:click="done(c.waybillKey, 2)">
									<div class="loc_info" v-if="c.waybill.receiptCount == null || c.waybill.receiptCount <= 0">
										<span>未上传</span>
									</div>
									<div class="loc_info" v-if="c.waybill.receiptCount != null && c.waybill.receiptCount > 0">
										<span>已上传</span>
									</div>
								</div>
								<div class="new_location" v-if="c.receiptStatus == 9" v-on:click="done(c.waybillKey, 2)">
									<span class="loc_text wait_tag smallSize">待办</span>
									<div class="loc_info address-tip">
										<span>上传回单</span>
										<br />
										<span>&nbsp;&nbsp;&nbsp;</span>
									</div>
								</div>
							</div>
							<div class="li-item">
								<div class="abnormal" v-on:click="saveException(c.id,c.waybill.id)">异常上报</div>
							</div>
	                    </li>
	                    <li class="child-content">
	                        <div class="table-layout child-box" v-for="cl in c.children"> <!-- 子级数据，循环这个child-box -->
	                            <div class="child-item"></div> <!-- 按照原型上样式，第一个空格里面不写数据 -->
	                            <div class="child-item">{{cl.deliveryNumber}}</div>
	                            <div class="child-item">
									<div class="in-block"><input type="text" v-bind:value="cl.conveyanceNumber" v-bind:cid="cl.id" v-bind:val="cl.conveyanceNumber" readonly /><i class="layui-icon edit_icon" v-on:click.self="editNumber">&#xe642;</i></div>
								</div>
	                            <div class="child-item">
									<a href="#" v-on:click="detail(cl.waybill.id)">{{cl.barcode}}</a>
								</div>
	                            <div class="child-item">
                                    <span v-if="cl.owner != null">{{cl.owner.unamezn}}</span>
                                </div>
	                            <div class="child-item">{{cl.simpleStartStation}}</div>
	                            <div class="child-item">{{cl.simpleEndStation}}</div>
	                            <div class="child-item">{{cl.waybill.createtime | date}}</div>
	                            <div class="child-item">
									<span v-if="cl.waybill.arrivaltime != null">{{cl.waybill.arrivaltime | datehour}}</span>
									<span v-else>无要求</span>
								</div>
	                            <div class="child-item">
									<div class="new_location">
                                        <div class="loc_info">
                                            <span class="loc_text report_loc" v-on:click="urgeLoaction(cl.id)">催报位置</span>
                                            <br />
                                            <span v-if="cl.addressTime != null">{{cl.addressTime | time}}</span>
                                        </div>
									</div>
								</div>
	                            <div class="child-item">
									<div class="waybill_status" v-if="cl.conveyanceFettle == 20"><span class="have_icon wait" v-on:click="fettle" v-bind:value="cl.id">待运输</span></div>
									<div class="waybill_status" v-if="cl.conveyanceFettle == 30"><span class="have_icon on-way" v-on:click="fettle" v-bind:value="cl.id">运输中</span></div>
									<div class="waybill_status" v-if="cl.conveyanceFettle == 35">已送达</div>
								</div>
	                            <div class="child-item">
									<div class="receipt_status">
                                        <div class="loc_info">
                                            <span class="have_icon turn" v-on:click="urgeReceipt(cl.id)">催传回单</span>
                                            <br />
                                            <span v-if="cl.receiptTime != null">{{cl.receiptTime | time}}</span>
                                        </div>
									</div>
								</div>
	                            <div class="child-item"></div>
	                        </div>
	                    </li>
	                </ul>


				</div>
				<div id="layui_div_page" class="col-sm-12 center-block" v-if="pages > 1">
					<ul class="pagination" style="margin-bottom: 0;">
						<li class="disabled"><span><span aria-hidden="true">共{{total}}条,每页{{pageSize}}条</span></span></li>
						<li v-if="!isFirstPage"><a v-on:click="flip(1)">首页</a></li>
						<li v-if="!isFirstPage"><a v-on:click="flip(prePage)">上一页</a></li>
						<li v-for="p in navigatepageNums">
							<span v-if="p == pageNum" class="cactive">{{p}}</span>
							<a v-if="p != pageNum" v-on:click="flip(p)">{{p}}</a>
						</li>
						<li v-if="!isLastPage"><a v-on:click="flip(nextPage)">下一页</a></li>
						<li v-if="!isLastPage"><a v-on:click="flip(pages)">尾页</a></li>
						<li class="disabled"><span><span>共{{pages}}页</span></span></li>
					</ul>
				</div>
			</div>
		</div>
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
		<div class="line-part">
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


<!--异常记录弹出框-->
<div class="upload_exception" style="display: none;">
	<div class="abnormal_box" style="width:640px;">
		<div class="abnormal_con">
			<div class="line-part">
				<span class="float-tag">任务单号：</span>
				<div class="text-tag pos_relative">
					<p class="p-tag barcode">10171226000015</p>
					<div class="city">
						<span class="cityname start_station">上海普陀区</span>
						<i class="line">—</i>
						<span class="cityname end_station">北京朝阳区</span>
					</div>
				</div>
			</div>
			<div class="line-part">
				<span class="float-tag">异常说明：</span>
				<div class="text-tag"><textarea class="area-tag content" name="content" rows="3"></textarea></div>
			</div>
			<div class="line-part image-show"></div>
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
<script src="${baseStatic}plugin/js/cityselect/chooseAddress/area.js"></script>
<script src="${baseStatic}plugin/js/cityselect/chooseAddress/provinceSelect.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script src="${baseStatic}plugin/js/jquery.mloading.js"></script>
<script src="${baseStatic}plugin/js/jquery.cookie.js"></script>
<script src="${baseStatic}plugin/js/scroll/zUI.js"></script>
<script src="${baseStatic}js/select.resource.js?times=${times}"></script>
<script src="${baseStatic}plugin/js/upload.img.js?times=${times}"></script>
<script>
    $(document).ready(function () {
    	//给左边赋值一个高
		$('.content-box').height($(window).height() - 46);

    	//弹窗日期插件初始化
    	$("#sendGoodsTime,#otherArrive").datetimepicker({
    		language : 'zh-CN',
    		format : 'yyyy-mm-dd',
    		minView:'month',
    		weekStart : 1, /*以星期一为一星期开始*/
    		todayBtn : true,
    		autoclose : true,
    		pickerPosition : "bottom-left"
    	});
    	$(".delay_arrive").datetimepicker({
    		language : 'zh-CN',
    		format : 'yyyy-mm-dd',
    		minView:'month',
    		weekStart : 1, /*以星期一为一星期开始*/
    		todayBtn : true,
    		autoclose : true,
    		pickerPosition : "top-left"
    	});

    	//tab选项卡
    	$('.btn-box li').on('click',function(){
    		$(this).addClass('current').siblings().removeClass('current');
    		$('.content-item').eq($(this).index()).addClass('active').siblings().removeClass('active');
    	})

    	//点击左边切换
    	$('.toggle-btn').on('click',function(){
    		if($(this).hasClass('tog')){
    			$('.left-wrapper').removeClass('toggle');
    			$(this).removeClass('tog');
    			$('.right-wrapper').removeClass('newWidth');
    		}else{
    			$('.left-wrapper').addClass('toggle');
    			$(this).addClass('tog');
    			//让页面重新计算下宽度
    			$('.right-wrapper').addClass('newWidth');
    		}
    	})

    	//点击搜索条件
    	$('.labe_1,.labe_2,.labe_3,.labe_4').on('click',function(){
    		$(this).addClass('active').siblings().removeClass('active');
    		if($(this).hasClass('other_more')){
    			$(this).siblings('.more-box').show();
    		}else{
    			$(this).siblings('.more-box').hide();
    		}
    	})


    	//点击收起按钮
    	$('.toggle-icon').on('click',function(){
    		if($(this).hasClass('open')){
    			$('.selection-show').removeClass('select-tog');
    			$('.selection-more').show();
    			$(this).text('收起').removeClass('open');
    		}else{
    			$('.selection-show').addClass('select-tog');
    			$('.selection-more').hide();
    			$(this).text('展开').addClass('open');
    		}
    	})

    	//刷新页面判断更多条件是否显示
    	if($.cookie('condition') == 1){
    		$('.icon-down').html('<i class="layui-icon">&#xe625;</i>');
			$('.detail-select').show();
			$('.p-more').addClass('open');
    	}

    	//点击更多搜索条件
    	$('.p-more').on('click',function(){
    		if($(this).hasClass('open')){
    			$('.icon-down').html('<i class="layui-icon">&#xe623;</i>');
    			$('.detail-select').hide();
    			$(this).removeClass('open');
    			$.cookie('condition','2');
    		}else{
    			$('.icon-down').html('<i class="layui-icon">&#xe625;</i>');
    			$('.detail-select').show();
    			$(this).addClass('open');
    			$.cookie('condition','1');
    		}

    	})
    	//待运输状态点击改成运输中
    	$('.table-style').delegate('.wait','click',function(){
    		$(this).parent().html('<span class="have_icon on-way">运输中</span>');
    	})
        $(".address-tip").find('span').tooltip();

		var conveyanceNum = 1;
		/********************************逻辑代码****************************************/
		function classify(datas){
            var arrays = [], i = 0;
		    $.each(datas, function(index, v, array){
		        var key = moment(v.createtime).format('YYYY-MM-DD');
		        if(arrays.length == 0){
                    arrays[i] = {time: key, news:[ v ]};
                }else{
		            if(arrays[i].time != key){
                        arrays[++i] = {time: key, news:[ v ]};
                    }else{
                        arrays[i].news.push(v);
                    }
                }
            });
		    return arrays;
		}
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
                                listMessage();//刷新
                                listConveyance();
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
                    map.setFitView();
                    geocoder.getAddress([e.lnglat.lng, e.lnglat.lat], function(status, result) {
                        if (status === 'complete' && result.info === 'OK') {
                            parmas = buildParmas(result.regeocode, e.lnglat, parmas);
                            $editBody.find('.adr_input').val(parmas.area);
                            $editBody.find('.adr_detail').val(parmas.street);
                        }
                    });
                });
                $editBody.find('.adr_box').find('input').on('change', function(){
                    parmas.area = $editBody.find('.adr_input').val();
                    parmas.street = $editBody.find('.adr_detail').val();
                    parmas.formattedAddress = parmas.area + parmas.street;
                    geocoder.getLocation(parmas.formattedAddress, function(status, result) {
                        if (status === 'complete' && result.info === 'OK') {
                            var geocode = result.geocodes[0];
                            parmas = buildParmas(geocode, geocode.location, parmas);
                            marker.setPosition(geocode.location);
                            map.setFitView();
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
                //console.log(parmas);
                $.util.json(base_url + '/backstage/coordination/loaction', parmas, function (data) {
                    if (data.success) {//处理返回结果
                        $.util.success('上报成功',function(){
                            listMessage();//刷新
                            listConveyance();
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
                        listMessage();//刷新
                        listConveyance();
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

        function saveException(conveyancekey, waybillKey) {
            var files = [];
            var form = $.util.form('异常记录', $(".upload_exception").html(), function (model) {
                var editBody = this.$body;
				editBody.find('.image-show').uploadImage({
					imgSelect: function(imgFiles, selects){
                        files = imgFiles;
					},
					imgDelete : function(imgFiles, index){
                        files = imgFiles;
					}
				});
                getExceptionInfo(conveyancekey, editBody);
            }, function () {
                var editBody = this.$body;
                var content = editBody.find(".content").val();
                if(content == "" || content == null){
                    $.util.warning("请输入异常信息");
                    return false;
                }
                var formData = new FormData();
                for(var i = 0; i < files.length; i++){
                    formData.append("file"+ i, files[i]);
                }
                formData.append("content", content);
                formData.append("waybillid", waybillKey);
                formData.append("conveyanceId", conveyancekey);
                $.ajax({
                    url: base_url + "/backstage/coordination/save/exception",
                    type: 'POST',
                    data: formData,
                    processData: false, // 告诉jQuery不要去处理发送的数据
                    contentType: false,// 告诉jQuery不要去设置Content-Type请求头
                    success: function (response) {
                        if (response.success) {//处理返回结果
                            $.util.success(response.message, function(){
                                if(form){ form.close(); }
							}, 3000);
                        }else{
                            $.util.error(response.message);
                        }
                    }
                });
                return false;
            });
        }

        function getExceptionInfo(conveyanceId, editBody) {
            $.util.json(base_url + "/backstage/coordination/getExceptionInfo/" + conveyanceId, null, function (data) {
                var conveyance = data.conveyance;
                editBody.find(".start_station").html(conveyance.simpleStartStation);
                editBody.find(".end_station").html(conveyance.simpleEndStation);
                editBody.find(".barcode").html(conveyance.barcode);

            });
        }
		function editConveyanceNumber($target){
            var $input = $target.prev();
            var html = "<form action=\"\" style=\"width: 350px;\"><div class=\"form-group\"><input type=\"text\" placeholder=\"运单号\" class=\"name form-control\" value=\""+ $input.val() +"\"/></div></form>";
            $.util.form('修改运单号', html, function(){
                var editbody = this.$body;
                editbody.find('input').val($input.val());
            }, function(){
                var editbody = this.$body;
                var val = editbody.find('.name').val();
                if(!val || $input.val() === val){
                    return false;
                }
                var parmas = {'number': val, 'conveyanceKey': $input.attr('cid')};
                $.util.json(base_url + "/backstage/conveyance/edit/number", parmas, function (data) {
                    if (!data.success) {//处理返回结果
                        $.util.danger('修改异常', data.message);
                    }else{
                        $.util.success("操作提示", null, 3000);
                        $input.val(val);
                    }
                });
            });
		}


        function editConveyanceFettle(conveyanceKey){
            var parmas = {'conveyanceKey': conveyanceKey};
            $.util.confirm('操作确认', '运单状态修改不可逆,确认要修改吗?', function () {
                $.util.json(base_url + "/backstage/conveyance/edit/fettle", parmas, function (data) {
                    if (!data.success) {//处理返回结果
                        $.util.danger('修改异常', data.message);
                    }else{
                        $.util.success("操作提示", function(){
                            listConveyance();
                        }, 3000);
                    }
                });
            });
        }

        var vvv = {
            listWait : new Vue({
                el: '#list-wait-message',
                data: {waits:[], loactions:0, receipts:0},
                updated:function(){
                    $(".content-box").panel({iWheelStep:32});
                },
                methods:{
                    forward:function(nid, ckey){ forwardSomeOne(ckey, null, nid); },
                    done: function(waybillKey, type, nid){
                        if(type == 1){ location(waybillKey, nid); }
                        if(type == 2){ receipt(waybillKey); }
                    },
                    showList:function(conveyanceKey){listConveyanceJson({conveyanceKey: conveyanceKey});}
                },
                computed: {
                    days: function () { return classify(this.waits); }
                }
            }),
            listFollow : new Vue({
                el: '#list-follow-message',
                data: {waits:[]},
                computed: {
                    days: function () { return classify(this.waits); }
                },
                methods:{
                    showList:function(conveyanceKey){listConveyanceJson({conveyanceKey: conveyanceKey});}
                }
            }),
            listConveyance : new Vue({
                el: '#list-conveyance',
                data: {collection:[],pageNum:0,pageSize:0,navigatepageNums:[],total:0,pages:0,prePage:0,nextPage:0,isFirstPage:false,isLastPage:false},
                methods:{
                    flip:function(num){ listConveyance(num); },//翻页
                    editNumber:function(e){ editConveyanceNumber($(e.target)); },//编辑运单号
                    urgeReceipt: function(ckey){
                        forward({conveyanceId: ckey, msgType: 2, forwardId : ckey});
                    },//转催回单
                    urgeLoaction: function(ckey){
                        forward({conveyanceId: ckey, msgType: 1, forwardId : ckey});
                    },//转催位置
                    saveException:function (conveyancekey, waybillKey) {saveException(conveyancekey, waybillKey)},//异常上报
                    location: function (val) {
                        if(val && val.length > 6){
                            return val.substring(0, 6);
                        }
                        return val;
                    },
                    detail:function(wkey){
                        if(wkey){
                            window.location.href = base_url + '/backstage/trace/findById/' + wkey;
                        }
                    },
                    done: function(waybillKey, type){
                        if(type == 1){ location(waybillKey); }
                        if(type == 2){ receipt(waybillKey); }
                    },
                    fettle:function(e){
                        var $target = $(e.target), ckey = $target.attr('value');
                        editConveyanceFettle(ckey);
                    }
                }
            })
        };

        $('.reset').on('click',function(){
            listMessage();
        })

        var listConveyanceJson = function(parmas){
            $.util.json(base_url + '/backstage/coordination/get/conveyanceList', parmas, function (data) {
                if (data.success) {//处理返回结果
                    vvv.listConveyance.collection = data.page.collection;
                    vvv.listConveyance.pageNum = data.page.pageNum;
                    vvv.listConveyance.pageSize = data.page.pageSize;
                    vvv.listConveyance.navigatepageNums = data.page.navigatepageNums;
                    vvv.listConveyance.total = data.page.total;
                    vvv.listConveyance.pages = data.page.pages;
                    vvv.listConveyance.prePage = data.page.prePage;
                    vvv.listConveyance.nextPage = data.page.nextPage;
                    vvv.listConveyance.isFirstPage = data.page.isFirstPage;
                    vvv.listConveyance.isLastPage = data.page.isLastPage;
                } else {
                    $.util.error(data.message);
                }
            });
        };
		var listConveyance = function(num){
            if(!num){  num = conveyanceNum; } else{conveyanceNum = num;}
		    var parmas = {num: num};
            parmas['groupKey'] = $('#conveyance').find("select[name='groupKey']").val();
            $('#conveyance').find('.selection').find('select, input').each(function(){
                if(this.name){
                    if(this.type == 'radio'){
                        if(this.checked){ parmas[this.name] = this.value; }
                    }else{ parmas[this.name] = this.value; }
                }
            });
            listConveyanceJson(parmas);
		};
        var listMessage = function(){
            $.util.json(base_url + '/backstage/coordination/backlog/list', null, function (data) {
                if (data.success) {//处理返回结果
                    vvv.listWait.locations = data.locations;
                    vvv.listWait.receipts = data.receipts;
                    vvv.listWait.waits = data.withList;
                    vvv.listFollow.waits = data.trackList;
                } else {
                    $.util.error(data.message);
                }
            });
        };
        $('.event-search').each(function(){
			var $target = $(this);
            if($target.is('select') || $target.attr('type') == 'text'){
                $target.on('change', function(){
                    listConveyance(1);
                });
			}
            if($target.is('button') || $target.attr('type') == 'radio'){
                $target.on('click', function(){
                    listConveyance(1);
                });
            }
		});
        listMessage();
        listConveyance(1);
    });
</script>
</html>