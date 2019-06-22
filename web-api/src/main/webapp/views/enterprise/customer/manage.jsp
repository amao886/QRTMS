<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>

	<head>
		<title>客户管理-客户管理</title>
		<%@ include file="/views/include/head.jsp" %>
		<link rel="stylesheet" href="${baseStatic}css/customermanage.css?times=${times}"/>
	</head>

	<body>
		<div class="customer-content">
			<form id="customer-manage-form">
				<div class="content-search">
					<div class="clearfix">
						<div class="fl col-min">
							<label class="labe_l">搜索条件:</label>
							<input type="text" class="tex_t" name="likeName" placeholder="客户名称" value="" />
						</div>
						<div class="fl col-min">
							<label class="labe_l">客户类型:</label>
							<select class="tex_t selec_t" name="type">
								<option value="">全部</option>
								<option value="1">货主</option>
								<option value="2">收货客户</option>
								<option value="3">承运商</option>
							</select>
						</div>
						<div class="fl col-min">
							<label class="labe_l">关联状态:</label>
							<select class="tex_t selec_t" name="reg">
								<option value="">全部</option>
								<option value="0">未关联</option>
								<option value="1">已关联</option>
							</select>
						</div>
						<div class="fl col-min">
							<a href="javascript:;" class="content-search-btn search-btn">查询</a>
						</div>
					</div>
				</div>
			</form>

			<!-- 表格部分 -->
			<div class="table-style" id="list-manage">
				<template>
					<div class="handle-box">
						<button class="layui-btn layui-btn-normal" v-on:click="addCustomer">添加客户</button>
						<button style="padding: 0 20px;background-color: white;border: 1px solid #999999;color: #333333;" class="layui-btn layui-btn-normal" v-on:click="deletes">删除</button>
					</div>
					<table>
						<thead>
							<tr>
								<th width="10">
									<label class="chooseDeleteCheckBox"><input type="checkbox" id="checkAll" v-on:click="selectAll" /><span></span></label>
								</th>
								<th style="width: 28px;">客户名称</th>
								<th style="width: 28px;">客户类型</th>
								<th style="width: 28px;">注册状态</th>
								<th width="25">注册人</th>
								<th width="25">手机号</th>
								<th width="25">操作</th>
							</tr>
						</thead>
						<tbody>
							<tr v-if="collection.length <= 0">
								<td colspan="7" style="text-align: center;">暂无数据</td>
							</tr>
							<tr v-for="o in collection" v-if="collection.length > 0">
								<td>
									<label class="chooseDeleteCheckBox"><input type="checkbox" name="orderId" v-bind:value="o.key" v-bind:customerName="o.name" /><span></span></label>
								</td>
								<td>{{o.name}}</td>
								<td>
									<span v-if="o.type == 1">货主</span>
									<span v-if="o.type == 2">收货客户</span>
									<span v-if="o.type == 3">承运商</span>
								</td>
								<td class="check-signin" v-bind:data="o.companyKey">
									<span v-if="o.companyKey == null || o.companyKey <= 0">未注册</span>
									<span v-else="o.companyKey != null && o.companyKey > 0">已注册</span>
								</td>
								<td>{{o.regName}}</td>
								<td>{{o.regMobile}}</td>
								<td>
									<a class="linkBtn" @click="editCustomer(o.key)">编辑</a>&nbsp;&nbsp;
									<a class="linkBtn" v-if="o.companyKey == null || o.companyKey <= 0" @click="downloadCode(o.key)" title="请让客户企业管理员扫描二维码完成注册">下载二维码图片</a>&nbsp;&nbsp;
									<a class="linkBtn" v-else="o.companyKey != null && o.companyKey > 0" @click="cancelSignin(o.key)">取消注册</a>
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
					<div class="trips">
						提示：关联后，贵公司和已关联的公司之间在系统中建立合作关系，关系建立成功后，订单将自动分发。
					</div>
				</template>
			</div>
		</div>
		<!-- 添加客户 -->
		<div class="add_enterprise_panel" style="display: none;">
			<div class="model-base-box add_enterprise" style="width: 500px;overflow: hidden;">
				<form action="" enctype="multipart/form-data" method="post">
					<div class="model-form-field">
						<label>企业名称:</label>
						<input type="text" name="companyName" placeholder="请输入企业名称" />
					</div>
					<div class="model-form-field">
						<label>管理员手机号码:</label>
						<input type="text" name="scanPhone" placeholder="请输入对方管理员的手机号码，以保证企业注册准确性" />
					</div>
					<div class="model-form-field">
						<label>客户类型:</label>
						<input type='radio' name='customerType' value='1' class='labelauty' /><label><span>货主</span></label>
						<input type='radio' name='customerType' value='2' class='labelauty' /><label><span>收货客户</span></label>
						<input type='radio' name='customerType' value='3' class='labelauty' /><label><span>承运商</span></label>
					</div>
				</form>
			</div>
		</div>
	</body>
	<%@ include file="/views/include/floor.jsp" %>
	<script src="${baseStatic}plugin/js/jquery.labelauty.js"></script>
	<script>
		var pageNum = 1;
		$(document).ready(function() {
			function add_customer() {
			    var panel = $.util.panel('添加客户', $(".add_enterprise_panel").html(), 500);
				panel.initialize = function(self){
                    var $editBody = this.$body;
                    $editBody.find(".labelauty").labelauty();
                    $editBody.find(':radio').on('click', function () {
                        $editBody.find(':checkbox').attr('checked', false).labelautychecked();
                    })
				};
				panel.addButton(function() {
					var editBody = this.$body;
					var parmas = {
						"customerName": editBody.find("input[name='companyName']").val(),
                        "customerType": editBody.find("input[name='customerType']:checked").val(),
						"scanPhone":editBody.find("input[name='scanPhone']").val(),
					};
					if(isNull(parmas.customerName)) {
						$.util.error('客户名称不能为空');
						return;
					}
                    if(isNull(parmas.customerType)) {
                        $.util.error('客户类型不能为空');
                        return;
                    }
                    if (isNull(parmas.scanPhone)) {
                        $.util.error('手机号码不能为空');
                        return;
					}
					var reg = /^1\d{10}$/;//手机号码必须是以1开头的11位数字
					if (!reg.test(parmas.scanPhone)){
                        $.util.error('手机号码必须是以1开头的11位数字');
                        return;

					}
					console.log(parmas)
					$.util.json(base_url + "/enterprise/customer/add", parmas, function(data) {
						if(data.success) {
							$.util.success(data.message, function() {
								searchManage(1); //成功后刷新列表
							}, 2000);
						} else {
							$.util.error(data.message);
						}
					});
				});
				panel.open();
			}
            function edit_customer(id) {
                var panel = $.util.panel('修改客户', $(".add_enterprise_panel").html(), 500);
                panel.initialize = function(self){
                    var $editBody = this.$body;
                    $editBody.find(".labelauty").labelauty();
                    $editBody.find(':radio').on('click', function () {
                        $editBody.find(':checkbox').attr('checked', false).labelautychecked();
                    })
					$.util.get(base_url +'/enterprise/customer/detail/'+ id, function(data){
                        if(data.success) {
                            $editBody.find("input[name='companyName']").val(data.customer.name);
                            $editBody.find("input[name='customerType'][value='"+ data.customer.type +"']").attr("checked", true).labelautychecked();
                        } else {
                            $.util.error(data.message);
                        }
					});
                };
                panel.addButton(function() {
                    var editBody = this.$body;
                    var parmas = {
                        "customerKey": id,
                        "customerName": editBody.find("input[name='companyName']").val(),
                        "customerType": editBody.find("input[name='customerType']:checked").val()
                    };
                    if(isNull(parmas.customerName)) {
                        $.util.error('客户名称不能为空');
                        return;
                    }
                    if(isNull(parmas.customerType)) {
                        $.util.error('客户类型不能为空');
                        return;
                    }
                    $.util.json(base_url + "/enterprise/customer/edit", parmas, function(data) {
                        if(data.success) {
                            $.util.success(data.message, function() {
                                searchManage(1); //成功后刷新列表
                            }, 2000);
                        } else {
                            $.util.error(data.message);
                        }
                    });
                });
                panel.open();
            }
			function deletes(element) {
				var chk_values = [];
				var flag = true;
				var customer_msg = "客户名称： ";
				$.each($("input[name='orderId']:checked"), function(idx, value) {
					chk_values.push($(this).val());
					var signin = $(value).parent().parent().siblings(".check-signin");
					if(signin.attr("data") > 0) { //大于0:已注册
						customer_msg = customer_msg + $(this).attr("customerName") + ",";
						flag = false;
					}
				});
				if(!flag) {
					$.util.error(customer_msg + "已注册的企业,不可以删除");
					return false;
				}
				if(chk_values == null || chk_values.length <= 0) {
					$.util.error("请至少选择一条数据");
					return false;
				}
				$.util.confirm('删除客户', '删除客户后将不能进行业务往来，确定删除？', function() {
					var parmas = {
						"customerKey": chk_values.join(",")
					};
					console.log(parmas);
					$.util.json(base_url + "/enterprise/customer/delete", parmas, function(data) {
						if(data.success) { //处理返回结果
							$.util.success(data.message, function() {
								$(":checkbox").attr("checked", false);
								searchManage(1);
							}, 2000);
						} else {
							$.util.error(data.message);
						}
					});
				});
			}

			function isNull(text) {
				return !text && text !== 0 && typeof text !== "boolean" ? true : false;
			}

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
					isLastPage: false
				},
				methods: {
					flip: function(num) {
						searchManage(num);
					}, //翻页
					selectAll: function(event) {
						$("input[name='orderId']").prop('checked', event.target.checked);
					},
					addCustomer: function() {
                        add_customer();
					},
                    editCustomer: function(id) {
                        edit_customer(id);
                    },
					deletes: function(event) {
						deletes(event);
					},
					downloadCode: function(id) { //下载二维码
						$.util.json(base_url + "/enterprise/customer/download/" + id, null, function(res) {
							if(res.success && res.url) {
								$.util.download(res.url);
							} else {
								$.util.error(res.message);
							}
						});
					},
					cancelSignin: function(id) { //取消注册
						$.util.confirm('取消注册', '确定取消注册？', function() {
							var parmas = {
								"customerKey": id
							};
							console.log(parmas.customerKey);
							$.util.json(base_url + "/enterprise/customer/cancle", parmas, function(data) {
								if(data.success) { //处理返回结果
									$.util.success(data.message, function() {
										searchManage(1); //成功后刷新列表
									}, 2000);
								} else {
									$.util.error(data.message);
								}
							});
						});
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
						size: 10
					},
					$form = $('#customer-manage-form');
				$form.find('input, select').each(function(index, item) {
					parmas[item.name] = item.value;
				});

				$.util.json(base_url + "/enterprise/customer/manage", parmas, function(data) {
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