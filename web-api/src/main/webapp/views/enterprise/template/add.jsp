<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>

	<head>
		<title>运输管理-发货配置-新增模板</title>
		<%@ include file="/views/include/head.jsp" %>
		<link rel="stylesheet" href="${baseStatic}css/deliveryadd.css?times=${times}">
	</head>

	<body data-category="${category}">
		<div class="wrapper" id="main">
			<template>
				<div class="d_addTop">
					<form action="post">
						<label for="tempName">模板名称</label>
						<input type="text" id="tempName" />

						<span style="margin-left: 30px;"></span>
						<label for="mergeType">导入数据合并方式</label>
						<input type="radio" name="mergeType" v-model="mergeType" value="0" style="margin-left: 10px; cursor: pointer;">不合并
						<input type="radio" name="mergeType" v-model="mergeType" value="1" style="margin-left: 10px; cursor: pointer;">{{category==1? '按送货单号合并' : '按发货计划单号合并'}}
						<input type="radio" name="mergeType" v-model="mergeType" value="2" style="margin-left: 10px; cursor: pointer;">按收货信息合并
					</form>
				</div>
				<div class="d_addCenter">
					<div class="setTit">设置结果预览：（拖拽字段可调整前后顺序）</div>
					<ul class="addlist clearfix">
						<draggable :list="list" @start="dragging=true" @end="dragging=false" :move="getdata" @update="datadragEnd">
							<li v-for="(item, index) in list" :drag-id="item.id">
								<div :class="{blankClass: item.type == 4}">{{item.fieldName}}</div>
							</li>
						</draggable>
					</ul>
				</div>
				<div class="d_addBottom">
					<div class="dabTop clearfix">
						<div class="addstep fl">
							<div><span></span>第一步：请确认必选字段信息（必选字段）</div>
							<table border="1" class="stepTab">
								<tr>
									<td>字段名称</td>
									<td>字段说明</td>
									<td>选择</td>
									<td>必填</td>
								</tr>
								<tr v-for="(item, index) in list1">
									<td>{{item.fieldName}}</td>
									<td>{{item.fieldText}}</td>
									<td><label class="chooseFiledCheckBox"><input type="checkbox" checked="checked" disabled="true"><span style="cursor:auto;"></span></label></td>
									<td><label class="chooseFiledCheckBox"><input type="checkbox" name="requiredFiledMust" @click="chooseRequiredFiledMust(index)"><span></span></label></td>
								</tr>
							</table>
						</div>
						<div class="addstep fl">
							<div><span></span>第二步：请选择补充字段信息（可选字段）</div>
							<table border="1" class="stepTab">
								<tr>
									<td>字段名称</td>
									<td>字段说明</td>
									<td>选择</td>
									<td>必填</td>
								</tr>
								<tr v-for="(item, index) in list2">
									<td>{{item.fieldName}}</td>
									<td>{{item.fieldText}}</td>
									<td><label class="chooseFiledCheckBox"><input type="checkbox" name="normalFiled" @click="chooseNormalFiled(index)"><span></span></label></td>
									<td><label class="chooseFiledCheckBox"><input type="checkbox" name="normalFiledMust" disabled="true" @click="chooseNormalFiledMust(index)"><span></span></label></td>
								</tr>
							</table>
						</div>
						<div class="addstep fl">
							<div><span></span>第三步：请自定义设置需要导入的字段信息</div>
							<table border="1" class="stepTab customTab">
								<tr>
									<td>字段名称</td>
									<td>字段说明</td>
									<td>操作</td>
									<td>选择</td>
									<td>必填</td>
								</tr>
								<tr v-for="(item,index) in list3">
									<td><input type="text" name="customFiledName" @input="changeNameText(index)" @propertychange="changeNameText(index)"></td>
									<td><input type="text" name="customFiledDesc" @input="changeDescText(index)" @propertychange="changeDescText(index)" style="width: 100%;"></td>
									<td>
										<span class="subtract" v-if="index < list3.length - 1" @click="deleteCustomFiled(index)">-</span>
										<span class="add" v-if="index == list3.length - 1" @click="addCustomFiled()">+</span>
									</td>
									<td>
										<label class="chooseFiledCheckBox"><input type="checkbox" name="customFiled" @click="chooseCustomFiled(index)"><span></span></label>
									</td>
									<td><label class="chooseFiledCheckBox"><input type="checkbox" name="customFiledMust" disabled="true" @click="chooseCustomFiledMust(index)"><span></span></label></td>
								</tr>
							</table>
						</div>
					</div>
					<div class="addstep clearfix">
						<div><span></span>第四步：插入空白列（在预览区可以拖拽空白列）</div>
						<div>
							插入<input type="number" min="0" max="20" name="blankFiled" class="blankCell" value="0" @keyup="checkInput" @change="checkInput">列空白列
						</div>
						<div class="explain">说明：系统不记录空白列内的数据</div>
					</div>
					<div class="dabBottom clearfix">
						<button class="fl" @click="saveData(0)">保存</button>
						<button class="fl" @click="saveData(1)">保存并导出模板</button>
					</div>
				</div>
			</template>
		</div>
	</body>
	<%@ include file="/views/include/floor.jsp" %>
	<script type="text/javascript" src="${baseStatic}js/require.js" data-main="${baseStatic}js/deliveryconfig.js"></script>

</html>