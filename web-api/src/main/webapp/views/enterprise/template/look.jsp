<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>

	<head>
		<title>运输管理-发货配置-查看模板</title>
		<%@ include file="/views/include/head.jsp" %>
		<link rel="stylesheet" href="${baseStatic}css/deliveryedit.css?times=${times}">
		<style type="text/css">
			.drag-item {
                float: left;
                height: 50px;
                line-height: 50px;
                padding: 0 15px;
                text-align: center;
                /* border: solid 1px #999; */
                margin-bottom: 10px;
                margin-right: 10px;
                cursor: pointer;
                font-weight: bold;
                -moz-box-shadow: 2px 2px 5px #333333;
                -webkit-box-shadow: 2px 2px 5px #333333;
                box-shadow: 2px 2px 5px #333333;
			}

			.fixed-element {
				position: fixed;
				right: 0;
				bottom: 10px;
			}
			.emptyClass{
				color: #CCCCCC !important;
				font-weight: normal !important;
				background: rgba(242, 242, 242, 1) !important;
			}
			.emptyNumber{
				border: 1px solid rgb(0, 0, 0);
				width: 60px;
				height: 30px;
				margin: 10px;
				font-size: 20px;
				text-align: center;
			}
		</style>
	</head>

	<body data-key="${key}">
		<%--<div>运输管理-发货配置-编辑模板, 模板ID为：${key}</div>--%>
		<div class="wrapper" id="vue-wrapper">
			<template>
				<div class="d_addTop">
					<form action="post">
						<label for="tempName">模板名称</label>
						<input type="text" id="tempName" v-model="template.name" readonly />

						<span style="margin-left: 30px;"></span>
						<label for="mergeType">导入数据合并方式</label>
						<input type="radio" name="mergeType" v-model="template.mergeType" value="0" style="margin-left: 10px; cursor: pointer;" disabled>不合并
						<input type="radio" name="mergeType" v-model="template.mergeType" value="1" style="margin-left: 10px; cursor: pointer;" disabled>按发货计划单号合并
						<input type="radio" name="mergeType" v-model="template.mergeType" value="2" style="margin-left: 10px; cursor: pointer;" disabled>按收货信息合并
					</form>
				</div>
				<div class="d_addCenter" style="height: 250px;">
					<div class="setTit">结果预览：</div>
					<draggable :list="selects" :options="{touchStartThreshold:2000,disabled: true}">
						<div v-for="r in selects" v-if="r" :key="r.index" class="drag-item" :class="{emptyClass: r.type==4}">{{r.name}}</div>
					</draggable>
				</div>
				<div class="d_addBottom">
					<div class="container-fluid">
						<div class="row">
							<div class="col-lg-4">
								<div class="addstep fl">
								<div><span></span>第一步：请确认必选字段信息（必选字段）</div>
								<table border="1" class="stepTab">
									<tr>
										<td>字段名称</td>
										<td>字段说明</td>
										<td>选择</td>
										<td>必填</td>
									</tr>
									<tr v-for="(b, index) in alls" v-if="b.type==1">
										<td>{{b.name}}</td>
										<td>{{b.description}}</td>
										<td><label class="chooseFiledCheckBox"><input type="checkbox" checked="checked" disabled="disabled"><span style="cursor: not-allowed;"></span></label></td>
										<td><label class="chooseFiledCheckBox"><input type="checkbox" v-model="b.required" disabled><span style="cursor: pointer;"></span></label></td>
									</tr>
								</table>
								</div>
							</div>
							<div class="col-lg-4">
								<div class="addstep fl">
									<div><span></span>第二步：请选择补充字段信息（可选字段）</div>
									<table border="1" class="stepTab">
										<tr>
											<td>字段名称</td>
											<td>字段说明</td>
											<td>选择</td>
											<td>必填</td>
										</tr>
										<tr v-for="(b, index) in alls" v-if="b.type==2">
											<td>{{b.name}}</td>
											<td>{{b.description}}</td>
											<td><label class="chooseFiledCheckBox"><input type="checkbox" v-model="b.checked" @change="selectOptional(b.index)" disabled><span></span></label></td>
											<td><label class="chooseFiledCheckBox"><input type="checkbox" v-model="b.required" disabled><span></span></label></td>
										</tr>
									</table>
								</div>
							</div>
							<div class="col-lg-4">
								<div class="addstep fl">
								<div><span></span>第三步：请自定义设置需要导入的字段信息</div>
								<table border="1" class="stepTab customTab">
									<tr>
										<td>字段名称</td>
										<td>字段说明</td>
										<!--<td>操作</td>-->
										<td>选择</td>
										<td>必填</td>
									</tr>
									<tr v-for="(c, index) in customs">
										<td><input type="text" v-model="c.name" readonly></td>
										<td><input type="text" v-model="c.description" style="width: 100%;" readonly></td>
										<!--<td>
											<span @click="changeCustom(-1)" v-if="index == customs.length - 1">+</span>
											<span @click="changeCustom(c.index)" v-if="index < customs.length - 1">-</span>
										</td>-->
										<td><label class="chooseFiledCheckBox"><input type="checkbox" v-model="c.checked" disabled @change="selectCustom(c.index)"><span></span></label></td>
										<td><label class="chooseFiledCheckBox"><input type="checkbox" v-model="c.required" disabled><span></span></label></td>
									</tr>
								</table>
								</div>

                                <div class="addstep fl">
                                    <div class="addstep fl" style="width: 250px;">
                                        <div><span></span>第四步：插入空白列</div>
                                        <div style="margin-left: 20px;">
                                            插入<input type="number" min="0" max="20" v-model="emptyCount" class="emptyNumber" readonly>列空白列
                                        </div>
                                        <div style="margin-left: 20px; color: #5e5e5e;">说明：系统不记录空白列内的数据</div>
                                    </div>
                                </div>
							</div>
						</div>
					</div>
					<div class="dabBottom  fixed-element hide">
						<button class="fl" @click="saveSubmit(false)">保存</button>
						<button class="fl" @click="saveSubmit(true)">保存并导出模板</button>
					</div>
				</div>
			</template>
		</div>
	</body>
	<%@ include file="/views/include/floor.jsp" %>
	<script type="text/javascript" src="${baseStatic}js/require.js"></script>
	<script>
		$(document).ready(function() {
			var templatekey = $('body').data('key'),
				sequence = 1000;
			require.config({
				paths: {
					"vue": base_static + 'vue/vue.min2',
					"sortablejs": base_static + 'vue/Sortable',
					"vuedraggable": base_static + 'vue/vuedraggable'
				},
				shim: {
					'vue': {
						exports: 'vue'
					}
				}
			});
			require(['vue', 'vuedraggable'], function(Vue, draggable) {
				Vue.component('draggable', draggable);
				//使用vue加载数据
				var v = new Vue({
					el: '#vue-wrapper',
					data: {
                        activeName:'first',
                        templateKey: templatekey,
						selects: [],
						template: {},
						flag: false,
						emptys:0,
						alls:[]
					},
					created: function() {
						this.loadTemplate();
					},
                    computed: {
                        customs: function () {
                            return this.alls.filter(function(o) {
                                return o.type === 3;
                            });
                        },
                        emptyCount: {
                            get: function () {
                                return this.emptys; //获取的时候直接获取值
                            },
                            set: function (v) {
                                this.emptys = Math.max(0, Math.min(20, v)); //设置的时候变为大写
                            }
                        }
                    },
                    watch: {
                        emptyCount: function(curVal, oldVal){
                            if(!this.flag){
                                this.flag = true;
                                return;
                            }
                            let def = curVal - oldVal;
							if(def < 0){
							    let _index = Math.abs(def);
                                this.selects = this.selects.filter(function(o) {
                                    if(_index > 0 && o.type === 4){
                                        _index--;
                                        return false;
                                    }
                                    return true;
                                });
							}else{
							    //let index = oldVal;
							    for(let i = 0; i< def; i++){
                                    this.selects[this.selects.length + i] = {type: 4 , name:'空白列'};
								}
							}
                            console.log("-------"+ this.selects);
                        }
                    },
					methods: {
						loadTemplate: function() {
							var vm = this;
							$.util.json(base_url + "/enterprise/template/detail", { templateKey: vm.templateKey }, function(data) {
								if(data.success) {
									let map = new Map();
									vm.template = data.template;
									if(data.template) {
                                        if(data.template.emptyColumns && data.template.emptyColumns.length > 0){
                                            vm.emptys = data.template.emptyColumns.length;
                                            $.each(data.template.emptyColumns, function(i, _index) {
                                                vm.selects[_index] = {type: 4, name:'空白列'};
                                            });
                                        }else{
                                            vm.flag = true;
										}
										if(data.template.describes && data.template.describes.length > 0){
                                            $.each(data.template.describes, function(i, obj) {
                                                if(obj.custom) {
                                                    vm.alls.push($.extend({ checked: true, index: vm.alls.length}, obj));
                                                } else {
                                                    map.set(obj.key, obj);
                                                }
                                            });
										}
									}
									if(vm.alls.length === 0) {
                                        vm.alls.push({ type: 3, name: '', description: '', checked:false, required:false, index: 0 });
									}
									$.each(data.bases, function(i, o) {
									    let obj = map.get(o.key);
									    if(obj){
									        o.detailKey = obj.detailKey;
                                            o.checked = true;
											o.required = obj.required;
                                            o.positionIndex = obj.positionIndex;
										}
                                        o.index = vm.alls.length;
                                        vm.alls.push(o);
									});
                                    $.each(vm.alls, function(i, o) {
										if(o.checked){
                                            vm.selects[o.positionIndex] = o;
										}
									})
								} else {
									$.util.error(data.message);
								}
							})
						},
						selectOptional: function(index) {
                            const obj = this.alls[index];
							if(obj.checked) {
                                this.selects.push({type: obj.type, index: index, name:obj.name});
							} else {
                                obj.required = false;
                                this.selects = this.selects.filter(function(o) {
                                    return o.index !== index;
                                });
							}
						},
						selectCustom: function(index) {
							const obj = this.alls[index];
							if(obj.checked) {
                                this.selects.push(obj);
							} else {
                                obj.required = false;
                                this.selects = this.selects.filter(function(o) {
                                    return o.index !== index;
                                });
							}
						},
						changeCustom: function(index) {
							if(index >= 0) {
                                this.alls = this.alls.filter(function(o, i) {
                                    return i !== index;
                                });
                                this.selects = this.selects.filter(function(o) {
                                    return o.index !== index
                                });
							} else {
                                this.alls.push({ type: 3, name: '', description: '', index: this.alls.length, required:false, checked:false });
							}
						},
						saveSubmit: function(exportFlag) {
							let vm = this;
                            let parmas = {
								templateName: this.template.name,
								templateKey: this.template.key,
                                mergeType: this.template.mergeType,
								templateDetails: null,
								export: exportFlag
							};
                            let arrays = [];
							for(let i = 0; i < this.selects.length; i++) {
                                let obj = this.selects[i];
                                console.log(i + "------" + obj.name);
								if(!obj.type || Number(obj.type) === 3) {
                                    let base = vm.alls[obj.index];
									arrays[i] = { type: obj.type, customName: obj.name, description: base.description, key: base.detailKey, required:base.required };
								} else if(obj.type === 1 || obj.type === 2) {
                                    let base = vm.alls[obj.index];
									arrays[i] = { type: obj.type, propertyKey: base.key, key: base.detailKey, required:base.required };
								}else{
                                    arrays[i] = { type: obj.type};
								}
							}
							parmas.templateDetails = JSON.stringify(arrays);
                            console.log(parmas)
							$.util.json(base_url + "/enterprise/template/edit", parmas, function(res) {
							    console.log(res)
								if(res.success) {
									if(res.url) {
										$.util.download(res.url);
									}
									$.util.success(res.message,function () {
                                        if(parent.layer){
                                            parent.layer.close(parent.layer.getFrameIndex(window.name));
                                        }
                                    },3000);
								} else {
									$.util.error(res.message);
								}
							});
						}
					}
				});
			});
		})
	</script>

</html>