<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>

	<head>
		<title>运营后台-发货管理</title>
		<%@ include file="/views/include/head.jsp" %>
		<link rel="stylesheet" href="${baseStatic}css/jquery.editable-select.min.css?times=${times}"/>
		<link rel="stylesheet" href="${baseStatic}element-ui/lib/theme-chalk/index.css"/>
		<link rel="stylesheet" href="${baseStatic}css/shippermanage.css?times=${times}"/>
		<style>
			.el-input__inner {
				height: 26px;
				line-height: 26px;
			}
			.el-input__icon {
				line-height: 26px;
			}
		</style>
	</head>

	<body>
		<div class="shipper-content" id="list-manage">
			<form id="shipper-manage-form">
				<div class="content-search">
					<div class="clearfix">
					<div class="fl col-min shippper">
						<%--<select  class="tex_t" name="shipperKey" v-model="selected">--%>
							<%--<option value="0">请选择公司</option>--%>
							<%--<option v-for="company in companys" v-bind:value="company.id">--%>
								<%--{{ company.companyName }}--%>
							<%--</option>--%>
						<%--</select>--%>
						<el-select v-model="selected" filterable placeholder="请选择公司" >
							<el-option
									v-for="company in companys"
									:key="company.id"
									:label="company.companyName"
									:value="company.id">
							</el-option>
						</el-select>
					</div>
						<div class="fl col-min fettle">
							<select name="fettle" class="tex_t" id="fettle" placeholder="订单状态" val="${search.fettle}">
								<option value="">全部</option>
								<option value="1">运输中</option>
								<option value="4">已到货</option>
							</select>
						</div>
						<div class="fl col-min receiptFettle">
							<select name="receiptFettle" class="tex_t" id="receiptFettle" placeholder="回单状态" val="${search.receiptFettle}">
								<option value="-1">全部</option>
								<option value="0">未上传</option>
								<option value="1">已上传</option>
							</select>
						</div>
						<div class="fl col-min">
							<select name="timeType" class="tex_t" id="timeType" val="${search.timeType}">
								<option value="3">近三天发货任务</option>
								<option value="7">近一周发货任务</option>
								<option value="30">近一个月发货任务</option>
								<option value="-1">全部日期</option>
							</select>
						</div>
						<div class="fl col-min">
							<input type="text" class="tex_t" name="likeString" placeholder="系统单号/送货单号" value="" />
						</div>
						<div class="fl col-min">
							<a href="javascript:;" class="content-search-btn search-btn">查询</a>
						</div>
					</div>
				</div>
			</form>

			<!-- 表格部分 -->
			<div class="table-style">
				<template>
					<table>
						<thead>
							<tr>
								<th width="15%">系统单号</th>
								<th width="15%">送货单号</th>
								<th width="10%">发货日期</th>
								<th width="10%">发货方</th>
								<th width="10%">物流商</th>
								<th width="10%">收货客户</th>
								<th width="10%">订单状态</th>
								<th width="10%">回单</th>
								<th width="10%">操作</th>
							</tr>
						</thead>
						<tbody>
							<tr v-if="collection.length <= 0">
								<td colspan="10" style="text-align: center;">暂无数据</td>
							</tr>
							<tr v-for="o in collection" v-if="collection.length > 0">
								<td>{{o.bindCode}}</td>
								<td>{{o.deliveryNo}}</td>
								<td>{{o.deliveryTime | dateFormat}}</td>
								<td>{{o.shipper.companyName}}</td>
								<td>{{o.convey.companyName}}</td>
								<td>{{o.receive.companyName}}</td>
								<td>
									<span v-if="o.fettle == 1">运输中</span>
									<span v-else="o.fettle == 4">已到货</span>
								</td>
								<td>
									<span v-if="o.isReceipt == null || o.isReceipt == 0">未上传</span>
									<span v-else="o.isReceipt != null && o.isReceipt > 0">已上传</span>
								</td>
								<td>
									<a class="linkBtn" @click="lookDetail(o.id)">查看详情</a>
								</td>
							</tr>
						</tbody>
					</table>
					<div class="page-item">
						<div id="layui_div_page" class="col-sm-12 center-block" v-if="pages > 1">
							<ul class="pagination" style="margin-bottom: 0;">
								<li class="disabled"><span><span aria-hidden="true">共{{total}}条,每页{{pageSize}}条</span></span>
								</li>
								<li v-if="!isFirstPage">
									<a v-on:click="flip(1)">首页</a>
								</li>
								<li v-if="!isFirstPage">
									<a v-on:click="flip(prePage)">上一页</a>
								</li>
								<li v-for="p in navigatepageNums">
									<span v-if="p == pageNum" class="cactive">{{p}}</span>
									<a v-if="p != pageNum" v-on:click="flip(p)">{{p}}</a>
								</li>
								<li v-if="!isLastPage">
									<a v-on:click="flip(nextPage)">下一页</a>
								</li>
								<li v-if="!isLastPage">
									<a v-on:click="flip(pages)">尾页</a>
								</li>
								<li class="disabled"><span><span>共{{pages}}页</span></span>
								</li>
							</ul>
						</div>
					</div>
				</template>
			</div>
		</div>
	</body>
	<%@ include file="/views/include/floor.jsp" %>
	<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
	<script type="text/javascript" src="${baseStatic}js/jquery.editable-select.min.js"></script>
	<script>
		var pageNum = 1;
		$(document).ready(function() {
//			$('#shipperFettle').editableSelect({
//				effects: 'slide'
//			});

			var listManage = new Vue({
				el: '#list-manage',
				data: {
					collection: [],
					pageNum: 0,
					pageSize: 0,
					navigatepageNums: [],
					total: 0,
					pages: 0,
					prePage: 0,
					nextPage: 0,
					isFirstPage: false,
					isLastPage: false,
					companys: [],
					search: null,
					selected: '',
				},
				methods: {
					flip: function(num) {
						searchManage(num);
					}, //翻页
					lookDetail: function(id) {
                        layer.full(layer.open({
                            type: 2,
                            title:'查看订单详情',
                            content: base_url + "/enterprise/order/manage/detail?orderKey="+ id
                        }));
					}
				}
			});
			var searchManage = function(num) {
				if(!num) {
					num = pageNum;
				} else {
					pageNum = num;
				}
				var parmas = {
						num: num,
						size: 20
					},
					$form = $('#shipper-manage-form');
				$form.find('input, select').each(function(index, item) {
					parmas[item.name] = item.value;
				});
                   parmas.shipperKey = listManage.selected;
				$.util.json(base_url + "/enterprise/backstage/delivery/search", parmas, function(data) {
					if(data.success) { //处理返回结果
						listManage.collection = data.page.collection;
						listManage.pageNum = data.page.pageNum;
						listManage.pageSize = data.page.pageSize;
						listManage.navigatepageNums = data.page.navigatepageNums;
						listManage.total = data.page.total;
						listManage.pages = data.page.pages;
						listManage.prePage = data.page.prePage;
						listManage.nextPage = data.page.nextPage;
						listManage.isFirstPage = data.page.isFirstPage;
						listManage.isLastPage = data.page.isLastPage;
						listManage.companys = data.companys;
						listManage.search = data.search;
					} else {
						$.util.error(data.message);
					}
				});
			};
			//提交事件
			$(".search-btn").click(function() {
				searchManage(1);
			});

			searchManage(1);
		});
	</script>

</html>