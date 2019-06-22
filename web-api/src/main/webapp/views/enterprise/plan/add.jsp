<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">

	<head>
		<title>物流跟踪-计划管理-计划编辑</title>
		<%@ include file="/views/include/head.jsp" %>
		<link rel="stylesheet" href="${baseStatic}element-ui/lib/theme-chalk/index.css" />
		<style type="text/css">
			.plan-content {
				padding: 5px 24px 50px 30px;
				background: #fff;
				min-height: 100%;
				color: #636363;
			}
			
			.two {
				float: left;
				width: 32%;
			}
			
			.line-wrapper {
				font-size: 0;
			}
			
			.line-wrapper>.line-part {
				display: inline-block;
			}
			
			.line-wrapper>.line-part+.line-part {
				margin-left: 2%;
			}
			
			.line-part {
				position: relative;
				color: #333;
				font-size: 14px;
				margin-top: 10px;
			}
			
			.line-part .float-tag {
				float: left;
				height: 26px;
				line-height: 26px;
				text-align: right;
			}
			
			.line-part .text-tag {
				margin-left: 10px;
			}
			
			.line-part.large-tag .text-tag {
				margin-left: 10px;
			}
			
			.line-part .text-tag>.input-tag,
			.line-part .text-tag>.select-tag,
			.line-part .text-tag>.p-tag {
				height: 26px;
				line-height: 26px;
				box-sizing: border-box;
				-webkit-box-sizing: border-box;
				-moz-box-sizing: border-box;
				border: 1px solid #dedede;
				text-indent: 4px;
				border-radius: 4px;
			}
			
			.line-part .text-tag>.p-tag {
				border: none;
				text-indent: 10px;
			}
			
			.line-part .text-tag>.select-tag {
				text-indent: 0;
			}
			/*====布局结束====*/
			
			.line-detail {
				padding-top: 12px;
				position: relative;
			}
			
			.line-detail .line-part .float-tag {
				text-align: left;
				width: 90px
			}
			
			.line-detail .line-part .text-tag {
				margin-left: 76px;
			}
			
			.line-detail .travelPosition {
				position: absolute;
				top: 12px;
				right: 0;
				line-height: 26px;
				color: #31acfa;
				font-size: 14px;
				z-index: 2;
			}
			
			.line-detail>.line-part {
				width: 20%;
			}
			
			.part-wrapper {
				margin-top: 25px;
			}
			
			.part-wrapper .title {
				font-weight: 700;
				padding: 10px 0 10px 10px;
				background: #f5f5f5;
				color: #333;
				font-size: 15px;
				margin-bottom: 5px;
			}
			
			.part-wrapper .line-part {
				padding-left: 10px;
				height: 30px;
			}
			
			.part-wrapper .line-part .float-tag {
				text-align: left;
				width: 90px
			}
			
			.part-wrapper .table-style table th {
				background: transparent;
			}
			
			.part-wrapper .table-style table {
				margin-top: -5px;
			}
			
			.submit,
			.cancel {
				width: 100px;
				height: 40px;
				line-height: 40px;
				color: #fff;
				outline: none;
				border: 1px solid #169BD5;
				border-radius: 5px;
			}
			
			.submit {
				background-color: #169BD5;
				margin-right: 15px;
			}
			
			.cancel {
				background: #fff;
				color: #169BD5;
			}
			
			.footer {
				margin-top: 50px;
				text-align: center;
			}
			
			input {
				border: 1px solid #ccc;
				outline: none;
				border-radius: 5px;
				height: 26px;
				line-height: 26px;
				width: 60%;
				padding-left: 10px;
			}
			
			.elseInfo .text-tag {
				margin-top: 10px;
			}
			
			.el-input__inner {
				height: 26px;
				line-height: 26px;
			}
			
			.el-input__icon {
				line-height: 26px;
			}
			
			.detail-address {
				width: 17.5%;
				margin-left: 20px;
				padding-left: 10px;
			}
			
			.describe input {
				border: 0 !important;
				text-align: center;
			}
			
			.el-autocomplete {
				width: 60%;
				text-indent: 0;
			}
			
			.el-date-editor.el-input,
			.el-date-editor.el-input__inner {
				text-indent: 0;
				width: 60%;
			}
			.address {
				text-indent: 5px !important;
				width: 100%;
			}
			.transportRoute{
				width: 87%;
			}
			.el-select {
				width: 11%;
			}
			.clearTextIndent {
				text-indent: 0 !important;
			}
		</style>
	</head>

	<body data-type="${type}">
		<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
		<!--[if lt IE 9]>
<script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
<script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
		<%--<input type="hidden" value="${orderKey}" id="orderKey" data-share="${share}">--%>
		<div class="plan-content" id="plan-content">
			<%--物流跟踪-计划管理-编辑计划------当前计划详情编号为 : ${key}--%>
			<template>
				<el-dialog title="请选择录入发货计划的模板" :visible.sync="dialogFormVisible" :show-close="false" :close-on-press-escape="false" :close-on-click-modal="false" width="800px">
					<el-table :data="templates" height="400" highlight-current-row @current-change="selectedTempChange">
						<el-table-column property="name" label="模板名称"></el-table-column>
						<el-table-column property="mergeType" label="合并方式" width="200">
							<template slot-scope="scope">
								<span>{{ scope.row.mergeType == 0 ? '不合并' : scope.row.mergeType == 1 ? '按送货单号合并' : '按收货信息合并' }}</span>
							</template>
						</el-table-column>
						<el-table-column property="fettle" label="常用" width="100">
							<template slot-scope="scope">
								<el-tag type="success" size="small" v-if="scope.row.fettle == 2">常用</el-tag>
								<el-tag type="info" size="small" v-if="scope.row.fettle == 1">不常用</el-tag>
							</template>
						</el-table-column>
					</el-table>
					<div slot="footer" class="dialog-footer">
						<el-button @click="cancelTemplate">取 消</el-button>
						<el-button type="primary" @click="selectTemplate">确 定</el-button>
					</div>
				</el-dialog>

				<div v-show="show">
					<%--发货信息--%>
					<div class="part-wrapper">
						<div class="title">发货信息</div>
						<div class="line-part " v-if="required.shipperName != null || required.deliveryTime != null">
							<div class="text-tag two" v-if="required.shipperName != null">
								<span class="float-tag">*发货方:</span>
								<p class="p-tag">
									<el-autocomplete class="inline-input" v-model="order.shipperName" placeholder="请输入内容" :fetch-suggestions="querySearch" :trigger-on-focus="false" @select="handleSelect" @focus="changeType(1)">
									</el-autocomplete>
								</p>
							</div>
							<div class="text-tag two" v-if="required.deliveryTime != null">
								<span class="float-tag">发货日期:</span>
								<p class="p-tag">
									<el-date-picker v-model="order.deliveryTime" type="date" placeholder="选择年月日" value-format="yyyy-MM-dd">
									</el-date-picker>
								</p>
							</div>
						</div>

						<div class="line-part">
							<div class="text-tag two" v-if="required.planNo != null">
								<span class="float-tag">*计划单号:</span>
								<p class="p-tag">
									<input type="text" v-model="order.planNo">
								</p>
							</div>
							<div class="text-tag two" v-if="required.orderNo != null">
								<span class="float-tag">订单编号:</span>
								<p class="p-tag">
									<input type="text" v-model="order.orderNo">
								</p>
							</div>
						</div>
						
						<div class="line-part" v-if="required.distributeAddress != null">
							<div class="text-tag">
								<span class="float-tag">发货地址：</span>
								<p class="p-tag address">
									<el-select v-model="distributeAddress[0]" placeholder="--请选省份--" @change="selectChange(4, 1)">
										<el-option v-for="item in areas.p4" :key="item.id" :label="item.name" :value="item.name"></el-option>
									</el-select>
									<el-select v-model="distributeAddress[1]" placeholder="--请选城市--" @change="selectChange(4, 2)">
										<el-option v-for="item in areas.c4" :key="item.id" :label="item.name" :value="item.name"></el-option>
									</el-select>
									<el-select v-model="distributeAddress[2]" placeholder="--请选区县--" @change="selectChange(4, 3)">
										<el-option v-if="areas.d4 != null" v-for="item in areas.d4" :key="item.id" :label="item.name" :value="item.name"></el-option>
									</el-select>
									<input type="text" v-model="distributeAddress[3]" class="detail-address">
								</p>
							</div>
						</div>
					</div>
					<%--承运信息--%>
					<div class="part-wrapper">
						<div class="title">承运信息</div>
						<div class="line-part large-tag">

							<div class="text-tag two" v-if="required.conveyName != null">
								<span class="float-tag">物流商:</span>
								<p class="p-tag">
									<el-autocomplete class="inline-input" v-model="order.conveyName" placeholder="请输入内容" :fetch-suggestions="querySearch" :trigger-on-focus="false" @select="handleSelect" @focus="changeType(3)">
									</el-autocomplete>
								</p>
							</div>

							<div class="text-tag two" v-if="required.conveyerName != null">
								<span class="float-tag">联系人:</span>
								<p class="p-tag">
									<input type="text" v-model="extra.conveyerName">
								</p>
							</div>

							<div class="text-tag two" v-if="required.conveyerContact != null">
								<span class="float-tag ">联系电话:</span>
								<p class="p-tag">
									<input type="text" v-model="extra.conveyerContact">
								</p>
							</div>
						</div>
						<div class="line-part large-tag" v-if="required.transportRoute != null">
							<div class="text-tag">
								<span class="float-tag">运输路线:</span>
								<p class="p-tag transportRoute">
									<input type="text" v-model="order.transportRoute">
								</p>
							</div>

						</div>
						<div class="line-part large-tag" v-if="required.collectTime != null || required.arrivalTime != null">
							<div class="text-tag two" v-if="required.collectTime != null">
								<span class="float-tag">要求提货时间:</span>
								<p class="p-tag">
									<el-date-picker v-model="order.collectTime" type="date" placeholder="选择年月日" value-format="yyyy-MM-dd"></el-date-picker>
								</p>
							</div>
							<div class="text-tag two" v-if="required.arrivalTime != null">
								<span class="float-tag">要求到货时间:</span>
								<p class="p-tag arriveTime">
									<el-date-picker v-model="order.arrivalTime" type="date" placeholder="选择年月日" value-format="yyyy-MM-dd"></el-date-picker>
								</p>
							</div>
						</div>
					</div>
					<%--收货信息--%>
					<div class="part-wrapper">
						<div class="title">收货信息</div>
						<div class="line-part" v-if="required.receiveName != null || required.receiverName != null || required.receiverContact != null">
							<div class="text-tag two" v-if="required.receiveName != null">
								<span class="float-tag">*收货客户:</span>
								<p class="p-tag receiveCustomer">
									<el-autocomplete class="inline-input" v-model="order.receiveName" placeholder="请输入内容" :fetch-suggestions="querySearch" :trigger-on-focus="false" @select="handleSelect" @focus="changeType(2)">
									</el-autocomplete>
								</p>
							</div>
							<div class="text-tag two" v-if="required.receiverName != null">
								<span class="float-tag">*收货人:</span>
								<p class="p-tag">
									<input type="text" v-model="order.receiverName" class="large">
								</p>
							</div>
							<div class="text-tag two" v-if="required.receiverContact != null">
								<span class="float-tag">*联系方式:</span>
								<p class="p-tag">
									<input type="text" v-model="order.receiverContact" class="large">
								</p>
							</div>
						</div>
						<div class="line-part" v-if="required.receiveAddress != null">
							<div class="text-tag">
								<span class="float-tag">*收货地址：</span>
								<p class="p-tag address">
									<el-select v-model="receiveAddress[0]" placeholder="--请选省份--" @change="selectChange(1, 1)">
										<el-option v-for="item in areas.p1" :key="item.id" :label="item.name" :value="item.name"></el-option>
									</el-select>
									<el-select v-model="receiveAddress[1]" placeholder="--请选城市--" @change="selectChange(1, 2)">
										<el-option v-for="item in areas.c1" :key="item.id" :label="item.name" :value="item.name"></el-option>
									</el-select>
									<el-select v-model="receiveAddress[2]" placeholder="--请选区县--" @change="selectChange(1, 3)">
										<el-option v-if="areas.d1 != null" v-for="item in areas.d1" :key="item.id" :label="item.name" :value="item.name"></el-option>
									</el-select>
									<input type="text" v-model="receiveAddress[3]" class="detail-address">
								</p>
							</div>
						</div>
					</div>
					<%--物料信息--%>
					<div class="part-wrapper">
						<div class="title">货物信息</div>
						<div class="table-style">
							<table>
								<thead>
									<tr>
										<th class="description" v-if="required.commodityNo != null">物料编号</th>
										<th class="description" v-if="required.commodityName != null">产品描述</th>
										<th class="description" v-if="required.commodityUnit != null">单位</th>
										<th class="description" v-if="required.quantity != null">数量(件)</th>
										<th class="description" v-if="required.boxCount != null"> 箱数</th>
										<th class="description" v-if="required.volume != null">体积(m³)</th>
										<th class="description" v-if="required.weight != null">重量(㎏)</th>
										<th class="description" v-if="required.remark != null">备注</th>
										<th class="description" v-if="show" style="width: 80px;">操作</th>
									</tr>
								</thead>
								<tbody>

									<tr v-for="(c, index) in commodities">
										<td class="depict" v-if="required.commodityNo != null" style="text-align: center;">
											<el-input v-model="c.commodityNo" placeholder="请输入内容" class="describe"></el-input>
										</td>
										<td class="depict" v-if="required.commodityName != null">
											<el-input v-model="c.commodityName" placeholder="请输入内容" class="describe"></el-input>
										</td>
										<td class="depict" v-if="required.commodityUnit != null">
											<el-input v-model="c.commodityUnit" placeholder="请输入内容" class="describe"></el-input>
										</td>
										<td class="depict" v-if="required.quantity != null">
											<el-input-number v-model="c.quantity" controls-position="right" :min="0" size="mini"></el-input-number>
										</td>
										<td class="depict" v-if="required.boxCount != null">
											<el-input-number v-model="c.boxCount" controls-position="right" :min="0" size="mini"></el-input-number>
										</td>
										<td class="depict" v-if="required.volume != null">
											<el-input-number v-model="c.volume" :precision="2" :step="0.01" :min="0" size="mini"></el-input-number>
										</td>
										<td class="depict" v-if="required.weight != null">
											<el-input-number v-model="c.weight" :precision="2" :step="0.1" :min="0" size="mini"></el-input-number>
										</td>
										<td class="depict" v-if="required.remark != null">
											<el-input v-model="c.remark" placeholder="请输入内容" class="describe"></el-input>
										</td>
										<td>
											<span class="el-icon-circle-plus-outline" @click="changeCommodity()" v-if="index == commodities.length -1"></span>
											<span class="el-icon-delete font" @click="changeCommodity(index)" v-if="index != commodities.length -1"></span>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<%--运输信息--%>
					<div class="part-wrapper">
						<div class="title">运输信息</div>
						<div class="line-part" v-if="required.originStation != null">
							<div class="text-tag ">
								<span class="float-tag">始发地：</span>
								<p class="p-tag" style="text-indent: 0;">
									<el-select v-model="originStation[0]" placeholder="--请选省份--" @change="selectChange(2, 1)">
										<el-option v-for="item in areas.p2" :key="item.id" :label="item.name" :value="item.name"></el-option>
									</el-select>
									<el-select v-model="originStation[1]" placeholder="--请选城市--" @change="selectChange(2, 2)">
										<el-option v-for="item in areas.c2" :key="item.id" :label="item.name" :value="item.name"></el-option>
									</el-select>
									<el-select v-model="originStation[2]" placeholder="--请选区县--" @change="selectChange(2, 3)">
										<el-option v-if="areas.d2 != null" v-for="item in areas.d2" :key="item.id" :label="item.name" :value="item.name"></el-option>
									</el-select>
								</p>
							</div>
						</div>
						<div class="line-part" v-if="required.arrivalStation != null">
							<div class="text-tag">
								<span class="float-tag">目的地:</span>
								<p class="p-tag" style="text-indent: 0;">
									<el-select v-model="arrivalStation[0]" placeholder="--请选省份--" @change="selectChange(3, 1)">
										<el-option v-for="item in areas.p3" :key="item.id" :label="item.name" :value="item.name"></el-option>
									</el-select>
									<el-select v-model="arrivalStation[1]" placeholder="--请选城市--" @change="selectChange(3, 2)">
										<el-option v-for="item in areas.c3" :key="item.id" :label="item.name" :value="item.name"></el-option>
									</el-select>
									<el-select v-model="arrivalStation[2]" placeholder="--请选区县--" @change="selectChange(3, 3)">
										<el-option v-if="areas.d3 != null" v-for="item in areas.d3" :key="item.id" :label="item.name" :value="item.name"></el-option>
									</el-select>
								</p>
							</div>
						</div>
						<div class="line-part" v-if="required.careNo != null || required.driverName != null || required.driverContact != null">
							<div class="text-tag two" v-if="required.careNo != null">
								<span class="float-tag">车牌号:</span>
								<p class="p-tag clearTextIndent">
									<input type="text" v-model="extra.careNo">
								</p>
							</div>

							<div class="text-tag two" v-if="required.driverName != null">
								<span class="float-tag">司机姓名:</span>
								<p class="p-tag">
									<input type="text" v-model="extra.driverName">
								</p>
							</div>

							<div class="text-tag two" v-if="required.driverContact != null">
								<span class="float-tag">司机电话:</span>
								<p class="p-tag">
									<input type="text" v-model="extra.driverContact">
								</p>
							</div>
						</div>
						<div class="line-part" v-if="required.startStation != null || required.endStation != null">
							<div class="text-tag two" v-if="required.startStation != null">
								<span class="float-tag">发站:</span>
								<p class="p-tag clearTextIndent">
									<input type="text" v-model="extra.startStation">
								</p>
							</div>

							<div class="text-tag two" v-if="required.endStation != null">
								<span class="float-tag">到站:</span>
								<p class="p-tag">
									<input type="text" v-model="extra.endStation">
								</p>
							</div>
						</div>
					</div>
					<%--财务信息--%>
					<div class="part-wrapper" v-if="required.expenditure != null || required.income != null">
						<div class="title">财务信息</div>
						<!--订单备注-->
						<div class="line-part large-tag">
							<div class="text-tag two" v-if="required.income != null">
								<span class="float-tag">收入:</span>
								<p class="p-tag clearTextIndent">
									<input type="text" v-model="extra.income">
								</p>
							</div>
							<div class="text-tag two" v-if="required.expenditure != null">
								<span class="float-tag">运输成本:</span>
								<p class="p-tag">
									<input type="text" v-model="extra.expenditure">
								</p>
							</div>
						</div>
					</div>
					<%--其他信息--%>
					<div class="part-wrapper elseInfo">
						<div class="title">其他信息</div>
						<!--自定义信息-->
						<div class="line-part large-tag" v-if="optional != null && optional.length > 0">
							<div class="text-tag two" v-for="(item, index) in optional" :class="{left : index % 2 == 0}">
								<span class="float-tag">{{item[0] + ":"}}</span>
								<p class="p-tag" :class="{'clearTextIndent' : index % 3 == 0}">
									<input type="text" :id=item[1]>
								</p>
							</div>
						</div>
					</div>
					<div class="footer">
						<button class="submit" @click="editSubmit">提交</button>
						<button class="cancel" @click="back()">取消</button>
					</div>
				</div>
				<el-dialog :title="selectAddressTitle" :visible.sync="selectAddressVisible" :close-on-press-escape="false" :close-on-click-modal="false">
					<el-table :data="selectAddress" highlight-current-row @current-change="modifyAdddress" height="300">
						<%-- <el-table-column property="customerName" label="客户名称" width="150"></el-table-column>--%>
						<el-table-column property="contacts" label="联系人" width="150"></el-table-column>
						<el-table-column property="contactNumber" label="联系电话" width="150"></el-table-column>
						<el-table-column property="fullAddress" label="地址"></el-table-column>
					</el-table>
					<span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="modifyAdddress(1)">确定</el-button>
        </span>
				</el-dialog>
			</template>
		</div>
	</body>
	<%@ include file="/views/include/floor.jsp" %>
	<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
	<script>
		$(document).ready(function() {
			new Vue({
				el: '#plan-content',
				data: {
					type: $('body').data('type'),
					dialogFormVisible: true,
					customerType: 1,
					//操作
					show: false,
					optional: [],
					required: [], //模板数据
					customers: [], //物流商列表
					template: null, //已选择的模板
					templates: [], //模板数组
					order: {}, //订单信息
					extra: {}, //物流商信息
					commodities: [{
						commodityNo: '',
						commodityName: '',
						commodityUnit: '',
						quantity: 0,
						boxCount: 0,
						volume: 0,
						weight: 0,
						remark: ''
					}], //物料信息
					customDatas: [], //自定义属性
					areas: {
						p1: [],
						p2: [],
						p3: [],
						p4: [],
						c1: [],
						c2: [],
						c3: [],
						c4: [],
						d1: [],
						d2: [],
						d3: [],
						d4: []
					},
					receiveAddress: [], //收货地址
					distributeAddress: [], //发货地址
					originStation: [], //始发地
					arrivalStation: [], //目的地
					cache: new Map(),
					address: {},
					selectAddressTitle: '',
					selectAddressVisible: false,
					selectAddress: []
				},
				mounted: function() {
					var _this = this;
					//获取模板信息
					$.util.json(base_url + "/enterprise/template/list", {
						category: 2
					}, function(data) {
						console.log(data)
						if(data.success) {
							_this.templates = data.templates;
						} else {
							$.util.error(data.message);
						}
					});
					//加载省份数据
					_this.loadArea(0, function(result) {
						for(let i = 1; i <= 4; i++) {
							_this.areas['p' + i] = result;
						}
					});
					//加载客户数据
					$.util.json("/enterprise/customer/list", {
						category: 1
					}, function(data) {
						console.log(data)
						if(data.success) {
							_this.customers = data.customers;
						} else {
							$.util.error(data.message);
						}
					});
				},
				computed: {
					customerArray: {
						get: function() {
							var _this = this;
							var typeArr = [
								[],
								[],
								[]
							];
							var typeObj = _this.customers.reduce(function(prev, current) {
								if(typeof prev[current.type] === 'undefined') {
									prev[current.type] = [{
										'value': current.name,
										'id': current.key
									}];
								} else {
									prev[current.type].push({
										'value': current.name,
										'id': current.key
									});
								}
								return prev;
							}, {});
							Object.keys(typeObj).forEach(function(k) {
								typeArr[k - 1] = typeObj[k];
							});
							return typeArr;
						}
					}
				},
				methods: {
					cancelTemplate: function() {
						var _this = this;

						_this.dialogFormVisible = false;

						_this.back();
					},
					back: function() {
						if(parent.layer) {
							parent.layer.close(parent.layer.getFrameIndex(window.name));
						}
					},
					changeType: function(ctype) {
						this.customerType = ctype;
					},
					createFilter: function(queryString) {
						return function(customers) {
							return(customers.value.toLowerCase().indexOf(queryString.toLowerCase()) >= 0);
						};
					},
					querySearch: function(queryString, cb) {
						var _this = this;
						var customers = _this.customerArray[_this.customerType - 1];
						if(customers) {
							// 调用 callback 返回建议列表的数据
							cb(queryString ? customers.filter(this.createFilter(queryString)) : customers);
						}
					},
					handleSelect: function(item) {
						var _this = this;
						if(_this.customerType === 1) { //选择发货方
							_this.order.shipperName = item.value;
							_this.order.shipperKey = item.id;
						} else if(_this.customerType === 2) { //选择收货客户
							_this.order.receiveName = item.value;
							_this.order.receiveKey = item.id;

						} else { //选择物流商
							_this.order.conveyName = item.value;
							_this.order.conveyKey = item.id;
						}
						$.util.json(base_url + "/enterprise/customer/address/list", {
							customerId: item.id
						}, function(data) {
							if(data.success) {
								if(!data.addresss || data.addresss.length <= 0) {
									return;
								}
								if(data.addresss.length === 1) {
									_this.modifyAdddress(data.addresss[0]);
								} else {
									_this.selectAddressTitle = _this.customerType === 2 ? '选择收货客户' : _this.customerType === 1 ? '选择发货方' : '选择物流商';
									_this.selectAddressVisible = true;
									_this.selectAddress = data.addresss;
								}
							} else {
								$.util.error(data.message);
							}
						});

					},
					modifyAdddress: function(address) { //选择一个地址
						var _this = this;
						if(address && address !== 1) {
							_this.address = address;
						}
						if(address === 1) {
							_this.selectAddressVisible = false;
							if(_this.customerType === 1) {
								_this.distributeAddress = [_this.address.province, _this.address.city, _this.address.district, _this.address.address];
								_this.originStation = [_this.address.province, _this.address.city, _this.address.district];
								_this.initializeArea([2, 4]);
							}
							if(_this.customerType === 2) {
								_this.order.receiverName = _this.address.contacts;
								_this.order.receiverContact = _this.address.contactNumber;
								_this.receiveAddress = [_this.address.province, _this.address.city, _this.address.district, _this.address.address];
								_this.arrivalStation = [_this.address.province, _this.address.city, _this.address.district];

								_this.initializeArea([1, 3]);
							}
							if(_this.customerType === 3) {
								_this.extra.conveyerContact = _this.address.contactNumber; //物流商联系电话
								_this.extra.conveyerName = _this.address.contacts; //物流商联系人
							}
						}
					},
					selectedTempChange: function(selectItem) {
						this.template = selectItem;
					},
					selectTemplate: function() {
						var _this = this;
						if(!_this.template) {
							_this.$message.error('请选择录入发货计划模板');
						} else {
							_this.dialogFormVisible = false;
							$.util.json(base_url + "/enterprise/template/templateInfo/" + _this.template.key, null, function(data) {
								if(data.success) {
									_this.required = data.required;
									_this.optional = data.optional;
									_this.show = true;
								}
							});
						}
					},

					editSubmit: function() {
						var _this = this;

						function joinArrays(arrays) {
							var sss = '';
							for(let obj of arrays) {
								sss += $.trim(obj);
							}
							return sss;
						};
						_this.order.receiveAddress = joinArrays(_this.receiveAddress);
						_this.extra.distributeAddress = joinArrays(_this.distributeAddress);
						_this.extra.originStation = joinArrays(_this.originStation);
						_this.extra.arrivalStation = joinArrays(_this.arrivalStation);

						var customs = [];
						if(_this.customDatas && _this.customDatas.length > 0) {
							for(const x of _this.customDatas) {
								customs.push({
									customName: x[0],
									customValue: x[1]
								});
							}
						}

						var describes = [];
						$.each(_this.order, function(i, item) {
							if(_this.required[i]) {
								describes.push({
									"detailKey": _this.required[i],
									"dataValue": $.trim(item)
								});
							}
						});
						$.each(_this.extra, function(i, item) {
							if(_this.required[i]) {
								describes.push({
									"detailKey": _this.required[i],
									"dataValue": $.trim(item)
								});
							}
						});
						$.each(_this.commodities, function(index, item) {
							$.each(item, function(j, v) {
								if(_this.required[j]) {
									describes.push({
										"detailKey": _this.required[j],
										"dataValue": $.trim(v)
									});
								}
							})
						});
						$.each(_this.optional, function(i, v) {
							describes.push({
								"detailKey": v[1],
								"dataValue": $('#' + v[1]).val()
							});
						});

						//计划单号不能为空
						if(_this.order.planNo == null || _this.order.planNo == '') {
							_this.$message.error('计划单号不能为空');
							return false
						}
						$.util.json("/enterprise/template/entrance/" + (_this.type === 1 ? 'shipper' : 'conveyer'), {
							templateKey: _this.template.key,
							describes: JSON.stringify(describes)
						}, function(data) {
							if(data.success) {
								$.util.success(data.message, function() {
									_this.back();
								}, 3000);
							} else {
								$.util.error(data.message);
							}
						});
					},
					indexOfName: function(arrays, name) {
						for(var i = 0; i < arrays.length; i++) {
							if(name === arrays[i].name) {
								return arrays[i].id;
							}
						}
						return null;
					},
					changeCommodity: function(index) {
						var _this = this;
						if(index === 0 || index > 0) {
							if(_this.commodities.length <= 1) {
								$.util.warning("至少保留一条货物信息");
							} else {
								_this.commodities.splice(index, 1);
							}
						} else {
							_this.commodities.push({
								commodityNo: '',
								commodityName: '',
								commodityUnit: '',
								quantity: 0,
								boxCount: 0,
								volume: 0,
								weight: 0,
								remark: ''
							})
						}
					},
					findArrays: function(index) {
						var _this = this;
						var arrays = (index === 1 ? _this.receiveAddress : (index === 2 ? _this.originStation : (index === 3 ? _this.arrivalStation : _this.distributeAddress)));
						if(!arrays || arrays.length <= 0) {
							arrays = ['', '', '', ''];
						}
						return arrays;
					},
					loadArea: function(pid, callback) {
						var _this = this;
						if(pid == null) {
							return;
						}
						if(_this.cache.has(pid)) {
							if(callback) {
								callback.call(_this, _this.cache.get(pid));
							}
						} else {
							$.util.json('/special/support/area', {
								parentid: pid
							}, function(info) {
								if(info.success) {
									if(info.result && info.result.length > 0) {
										_this.cache.set(pid, info.result);
									}
									if(callback) {
										callback.call(_this, info.result);
									}
								} else {
									$.util.error(info.message);
								}
							});
						}
					},
					//点击加载城市(index 1:收货地址 2:始发地 3:目的地 4:发货地址)
					selectChange: function(index, type, callback) {
						var _this = this,
							arrays = _this.findArrays(index);
						//type: 1-省份变化, 2-城市变化, 3-地区变化
						if(type === 1) {
							arrays[1] = arrays[2] = '';
							_this.areas['c' + index] = _this.areas['d' + index] = [];
							var pid = _this.indexOfName(_this.areas['p' + index], arrays[0]);
							_this.loadArea(pid, function(result) {
								_this.areas['c' + index] = result;
								if(callback) {
									callback.call(_this, _this.areas['c' + index]);
								}
							});
						} else if(type === 2) {
							arrays[2] = '';
							_this.areas['d' + index] = [];
							var cid = _this.indexOfName(_this.areas['c' + index], arrays[1]);
							_this.loadArea(cid, function(result) {
								_this.areas['d' + index] = result;
								if(callback) {
									callback.call(_this, _this.areas['d' + index]);
								}
							});
						}
					},
					initializeArea: function(indexs) {
						var _this = this;
						for(let i of indexs) {
							let arrays = _this.findArrays(i);
							let city = arrays[1],
								county = arrays[2];
							_this.selectChange(i, 1, function() {
								arrays[1] = city;
								_this.selectChange(i, 2, function() {
									arrays[2] = county;
								})
							})
						}
					},
				}
			})
		});
	</script>

</html>